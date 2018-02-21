package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.api.tile.ITileClientUpdates;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockOxygenSealer;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.fluid.OxygenPressureProtocol;
import micdoodle8.mods.galacticraft.core.fluid.ThreadFindSeal;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.energy.tile.EnergyStorageTile;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public class TileEntityOxygenSealer extends TileEntityOxygen implements IInventoryDefaults, ISidedInventory, ITileClientUpdates
{
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean sealed;
    public boolean lastSealed = false;

    public boolean lastDisabled = false;

    @NetworkedField(targetSide = Side.CLIENT)
    public boolean active;
    private ItemStack[] containingItems = new ItemStack[3];
    public ThreadFindSeal threadSeal;
    @NetworkedField(targetSide = Side.CLIENT)
    public int stopSealThreadCooldown;
    @NetworkedField(targetSide = Side.CLIENT)
    public int threadCooldownTotal;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean calculatingSealed;
    public static int countEntities = 0;
    private static int countTemp = 0;
    private static boolean sealerCheckedThisTick = false;
    public static ArrayList<TileEntityOxygenSealer> loadedTiles = new ArrayList<>();
    private static final int UNSEALED_OXYGENPERTICK = 12;
    public List<BlockVec3> leaksClient;


    public TileEntityOxygenSealer()
    {
        super(10000, UNSEALED_OXYGENPERTICK);
        this.noRedstoneControl = true;
        this.storage.setMaxExtract(5.0F);  //Half of a standard machine's power draw
        this.storage.setMaxReceive(25.0F);
        this.storage.setCapacity(EnergyStorageTile.STANDARD_CAPACITY * 2);  //Large capacity so it can keep working for a while even if chunk unloads affect its power supply
    }

    @Override
    public void onLoad()
    {
        if (!this.worldObj.isRemote)
        {
            if (!TileEntityOxygenSealer.loadedTiles.contains(this))
            {
                TileEntityOxygenSealer.loadedTiles.add(this);
            }
            this.stopSealThreadCooldown = 126 + countEntities;
        }
        else
        {
            this.clientOnLoad();
        }
    }

    @Override
    public void invalidate()
    {
        if (!this.worldObj.isRemote)
        {
            TileEntityOxygenSealer.loadedTiles.remove(this);
        }
        super.invalidate();
    }

    @Override
    public void onChunkUnload()
    {
        if (!this.worldObj.isRemote)
        {
            TileEntityOxygenSealer.loadedTiles.remove(this);
        }
        super.onChunkUnload();
    }

    public int getScaledThreadCooldown(int i)
    {
        if (this.active)
        {
            return Math.min(i, (int) Math.floor(this.stopSealThreadCooldown * i / (double) this.threadCooldownTotal));
        }
        return 0;
    }

    public int getFindSealChecks()
    {
        if (!this.active || this.getOxygenStored() < this.oxygenPerTick || !this.hasEnoughEnergyToRun)
        {
            return 0;
        }
        BlockPos posAbove = new BlockPos(this.getPos().getX(), this.getPos().getY() + 1, this.getPos().getZ());
        Block blockAbove = this.worldObj.getBlockState(posAbove).getBlock();
        if (!(blockAbove.isAir(this.worldObj, posAbove)) && !OxygenPressureProtocol.canBlockPassAir(this.worldObj, blockAbove, this.getPos().up(), EnumFacing.UP))
        {
            // The vent is blocked
            return 0;
        }

        return 1250;
    }

    public boolean thermalControlEnabled()
    {
        ItemStack oxygenItemStack = this.getStackInSlot(2);
        return oxygenItemStack != null && oxygenItemStack.getItem() == GCItems.basicItem && oxygenItemStack.getItemDamage() == 20 && this.hasEnoughEnergyToRun && !this.disabled;
    }

    @Override
    public void update()
    {
        if (!this.worldObj.isRemote)
        {
            ItemStack oxygenItemStack = this.getStackInSlot(1);
            if (oxygenItemStack != null && oxygenItemStack.getItem() instanceof IItemOxygenSupply)
            {
                IItemOxygenSupply oxygenItem = (IItemOxygenSupply) oxygenItemStack.getItem();
                int oxygenDraw = (int) Math.floor(Math.min(UNSEALED_OXYGENPERTICK * 2.5F, this.getMaxOxygenStored() - this.getOxygenStored()));
                this.setOxygenStored(getOxygenStored() + oxygenItem.discharge(oxygenItemStack, oxygenDraw));
                if (this.getOxygenStored() > this.getMaxOxygenStored())
                {
                    this.setOxygenStored(this.getOxygenStored());
                }
            }

            if (this.thermalControlEnabled())
            {
                if (this.storage.getMaxExtract() != 20.0F)
                {
                    this.storage.setMaxExtract(20.0F);
                }
            }
            else if (this.storage.getMaxExtract() != 5.0F)
            {
                this.storage.setMaxExtract(5.0F);
                this.storage.setMaxReceive(25.0F);
            }
        }

        this.oxygenPerTick = this.sealed ? 2 : UNSEALED_OXYGENPERTICK;
        super.update();

        if (!this.worldObj.isRemote)
        {
            // Some code to count the number of Oxygen Sealers being updated,
            // tick by tick - needed for queueing
            TileEntityOxygenSealer.countTemp++;

            this.active = this.getOxygenStored() >= 1 && this.hasEnoughEnergyToRun && !this.disabled;
            if (this.disabled != this.lastDisabled)
            {
                this.lastDisabled = this.disabled;
                if (!this.disabled) this.stopSealThreadCooldown = this.threadCooldownTotal * 3 / 5;
            }

            //TODO: if multithreaded, this codeblock should not run if the current threadSeal is flagged looping
            if (this.stopSealThreadCooldown > 0)
            {
                this.stopSealThreadCooldown--;
            }
            else if (!TileEntityOxygenSealer.sealerCheckedThisTick)
            {
                // This puts any Sealer which is updated to the back of the queue for updates
                this.threadCooldownTotal = this.stopSealThreadCooldown = 75 + TileEntityOxygenSealer.countEntities;
                if (this.active || this.sealed)
                {
                    TileEntityOxygenSealer.sealerCheckedThisTick = true;
                    OxygenPressureProtocol.updateSealerStatus(this);
                }
            }

            //TODO: if multithreaded, this.threadSeal needs to be atomic
            if (this.threadSeal != null)
            {
            	if (this.threadSeal.looping.get())
            	{
            		this.calculatingSealed = this.active;
            	}
            	else
            	{
            		this.calculatingSealed = false;
            		this.sealed = this.threadSeal.sealedFinal.get();
            	}
            }
            else
            {
                this.calculatingSealed = true;  //Give an initial 'Check pending' in GUI when first placed
            }

            this.lastSealed = this.sealed;
        }
    }
    
    public static void onServerTick()
    {
        TileEntityOxygenSealer.countEntities = TileEntityOxygenSealer.countTemp;
        TileEntityOxygenSealer.countTemp = 0;
        TileEntityOxygenSealer.sealerCheckedThisTick = false;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);

        final NBTTagList var2 = par1NBTTagCompound.getTagList("Items", 10);
        this.containingItems = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            final int var5 = var4.getByte("Slot") & 255;

            if (var5 < this.containingItems.length)
            {
                this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);

        final NBTTagList list = new NBTTagList();

        for (int var3 = 0; var3 < this.containingItems.length; ++var3)
        {
            if (this.containingItems[var3] != null)
            {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.containingItems[var3].writeToNBT(var4);
                list.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", list);
    }

    @Override
    public int getSizeInventory()
    {
        return this.containingItems.length;
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.containingItems[par1];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.containingItems[par1] != null)
        {
            ItemStack var3;

            if (this.containingItems[par1].stackSize <= par2)
            {
                var3 = this.containingItems[par1];
                this.containingItems[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.containingItems[par1].splitStack(par2);

                if (this.containingItems[par1].stackSize == 0)
                {
                    this.containingItems[par1] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int par1)
    {
        if (this.containingItems[par1] != null)
        {
            final ItemStack var2 = this.containingItems[par1];
            this.containingItems[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.containingItems[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getName()
    {
        return GCCoreUtil.translate("container.oxygensealer.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getTileEntity(this.getPos()) == this && par1EntityPlayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] { 0, 1 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (this.isItemValidForSlot(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return ItemElectricBase.isElectricItemCharged(itemstack);
            case 1:
                return itemstack.getItemDamage() < itemstack.getItem().getMaxDamage();
            case 2:
                return itemstack.getItem() == GCItems.basicItem && itemstack.getItemDamage() == 20;
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        switch (slotID)
        {
        case 0:
            return ItemElectricBase.isElectricItemEmpty(itemstack);
        case 1:
            return FluidUtil.isEmptyContainer(itemstack);
        default:
            return false;
        }
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        if (itemstack == null)
        {
            return false;
        }
        if (slotID == 0)
        {
            return ItemElectricBase.isElectricItem(itemstack.getItem());
        }
        if (slotID == 1)
        {
            return itemstack.getItem() instanceof IItemOxygenSupply;
        }
        if (slotID == 2)
        {
            return itemstack.getItem() == GCItems.basicItem && itemstack.getItemDamage() == 20;
        }
        return false;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.getOxygenStored() > this.oxygenPerTick && !this.getDisabled(0);
    }

    @Override
    public EnumFacing getFront()
    {
        IBlockState state = this.worldObj.getBlockState(getPos()); 
        if (state.getBlock() instanceof BlockOxygenSealer)
        {
            return state.getValue(BlockOxygenSealer.FACING);
        }
        return EnumFacing.NORTH;
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        return getFront().rotateY();
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }

    @Override
    public boolean shouldUseOxygen()
    {
        return this.hasEnoughEnergyToRun && this.active;
    }

    @Override
    public EnumSet<EnumFacing> getOxygenInputDirections()
    {
        return EnumSet.of(this.getElectricInputDirection().getOpposite());
    }

    @Override
    public EnumSet<EnumFacing> getOxygenOutputDirections()
    {
        return EnumSet.noneOf(EnumFacing.class);
    }

    public static HashMap<BlockVec3, TileEntityOxygenSealer> getSealersAround(World world, BlockPos pos, int rSquared)
    {
        HashMap<BlockVec3, TileEntityOxygenSealer> ret = new HashMap<BlockVec3, TileEntityOxygenSealer>();

        for (TileEntityOxygenSealer tile : new ArrayList<TileEntityOxygenSealer>(TileEntityOxygenSealer.loadedTiles))
        {
            if (tile != null && tile.getWorld() == world && tile.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) < rSquared)
            {
                ret.put(new BlockVec3(tile.getPos()), tile);
            }
        }

        return ret;
    }

    public static TileEntityOxygenSealer getNearestSealer(World world, double x, double y, double z)
    {
        TileEntityOxygenSealer ret = null;
        double dist = 96 * 96D;

        for (Object tile : world.loadedTileEntityList)
        {
            if (tile instanceof TileEntityOxygenSealer)
            {
                double testDist = ((TileEntityOxygenSealer) tile).getDistanceSq(x, y, z);
                if (testDist < dist)
                {
                    dist = testDist;
                    ret = (TileEntityOxygenSealer) tile;
                }
            }
        }

        return ret;
    }

    @Override
    public void sendUpdateToClient(EntityPlayerMP player)
    {
        if (this.sealed || this.threadSeal == null || this.threadSeal.leakTrace == null || this.threadSeal.leakTrace.isEmpty())
        {
            return;
        }
        Integer[] data = new Integer[this.threadSeal.leakTrace.size()];
        int index = 0;
        for (BlockVec3 vec : this.threadSeal.leakTrace)
        {
            int dx = vec.x - this.pos.getX() + 128;
            int dz = vec.z - this.pos.getZ() + 128;
            int dy = vec.y;
            if (dx < 0 || dx > 255 || dz < 0 || dz > 255)
                continue;
            
            int composite = dz + ((dy + (dx << 8)) << 8);
            data[index++] = composite;
        }
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_LEAK_DATA, GCCoreUtil.getDimensionID(player.worldObj), new Object[] { ((TileEntity)this).getPos(), data }), player);
    }

    @Override
    public void buildDataPacket(int[] data)
    {
        //unused
    }

    @Override
    public void updateClient(List<Object> data)
    {
        this.leaksClient = new ArrayList<>();
        if (data.size() > 1)
        {
            for (int i = 1; i < data.size(); i ++)
            {
                int comp = (Integer) data.get(i);
                int dx = (comp >> 16) - 128;
                int dy = (comp >> 8) & 255;
                int dz = (comp & 255) - 128;
                this.leaksClient.add(new BlockVec3(this.pos.getX() + dx, dy, this.pos.getZ() + dz));
            }
        }
    }

    public List<BlockVec3> getLeakTraceClient()
    {
        this.clientOnLoad();
        return this.leaksClient;
    }
}
