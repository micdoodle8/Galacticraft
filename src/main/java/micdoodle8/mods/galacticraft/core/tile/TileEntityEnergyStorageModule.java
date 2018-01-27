package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.item.IItemElectricBase;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachineTiered;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectricalSource;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class TileEntityEnergyStorageModule extends TileBaseUniversalElectricalSource implements ISidedInventory, IInventoryDefaults, IConnector, IMachineSides
{
    private final static float BASE_CAPACITY = 500000;
    private final static float TIER2_CAPACITY = 2500000;
    private ItemStack[] containingItems = new ItemStack[2];

    public final Set<EntityPlayer> playersUsing = new HashSet<EntityPlayer>();
    public int scaledEnergyLevel;
    public int lastScaledEnergyLevel;
    private float lastEnergy = 0;

    private boolean initialised = false;

    public TileEntityEnergyStorageModule()
    {
        this(1);
    }

    /*
     * @param tier: 1 = Electric Furnace  2 = Electric Arc Furnace
     */
    public TileEntityEnergyStorageModule(int tier)
    {
        this.initialised = true;
        if (tier == 1)
        {
            //Designed so that Tier 1 Energy Storage can power up to 10 Tier 1 machines
            this.storage.setCapacity(BASE_CAPACITY);
            this.storage.setMaxExtract(300);
            return;
        }

        this.setTier2();
    }

    private void setTier2()
    {
        this.storage.setCapacity(TIER2_CAPACITY);
        this.storage.setMaxExtract(1800);
        this.setTierGC(2);
    }

    @Override
    public void update()
    {
        if (!this.initialised)
        {
            int metadata = this.getBlockMetadata();

            //for version update compatibility
            Block b = this.worldObj.getBlockState(this.getPos()).getBlock();
            if (b == GCBlocks.machineBase)
            {
                this.worldObj.setBlockState(this.getPos(), GCBlocks.machineTiered.getDefaultState(), 2);
            }
            else if (metadata >= 8)
            {
                this.setTier2();
            }
            this.initialised = true;
        }

        float energy = this.storage.getEnergyStoredGC();
        if (this.getTierGC() == 1 && !this.worldObj.isRemote)
        {
            if (this.lastEnergy - energy > this.storage.getMaxExtract() - 1)
            {
                //Deplete faster if being drained at maximum output
                this.storage.extractEnergyGC(25, false);
            }
        }
        this.lastEnergy = energy;

        super.update();

        this.scaledEnergyLevel = (int) Math.floor((this.getEnergyStoredGC() + 49) * 16 / this.getMaxEnergyStoredGC());

        if (this.scaledEnergyLevel != this.lastScaledEnergyLevel)
        {
            this.worldObj.notifyLightSet(this.getPos());
        }

        if (!this.worldObj.isRemote)
        {
            this.recharge(this.containingItems[0]);
            this.discharge(this.containingItems[1]);
        }

        if (!this.worldObj.isRemote)
        {
            this.produce();
        }

        this.lastScaledEnergyLevel = this.scaledEnergyLevel;
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        if (this.storage.getEnergyStoredGC() > BASE_CAPACITY)
        {
            this.setTier2();
            this.initialised = true;
        }
        else
        {
            this.initialised = false;
        }

        NBTTagList var2 = par1NBTTagCompound.getTagList("Items", 10);
        this.containingItems = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

            if (var5 < this.containingItems.length)
            {
                this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
        
        this.readMachineSidesFromNBT(par1NBTTagCompound);  //Needed by IMachineSides
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        if (this.tierGC == 1 && this.storage.getEnergyStoredGC() > BASE_CAPACITY)
        {
            this.storage.setEnergyStored(BASE_CAPACITY);
        }

        super.writeToNBT(par1NBTTagCompound);

        NBTTagList var2 = new NBTTagList();
        for (int var3 = 0; var3 < this.containingItems.length; ++var3)
        {
            if (this.containingItems[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.containingItems[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        par1NBTTagCompound.setTag("Items", var2);

        this.addMachineSidesToNBT(par1NBTTagCompound);  //Needed by IMachineSides
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
            ItemStack var2 = this.containingItems[par1];
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
        return GCCoreUtil.translate(this.tierGC == 1 ? "tile.machine.1.name" : "tile.machine.8.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getTileEntity(this.getPos()) == this && par1EntityPlayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }


    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[0];
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        return ItemElectricBase.isElectricItem(itemstack.getItem());
    }

//    @Override
//    public int[] getAccessibleSlotsFromSide(int slotID)
//    {
//        return new int[] { 0, 1 };
//    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (itemstack.getItem() instanceof IItemElectricBase)
        {
            if (slotID == 0)
            {
                return ((IItemElectricBase) itemstack.getItem()).getTransfer(itemstack) > 0;
            }
            else if (slotID == 1)
            {
                return ((IItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) > 0;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (itemstack.getItem() instanceof IItemElectricBase)
        {
            if (slotID == 0)
            {
                return ((IItemElectricBase) itemstack.getItem()).getTransfer(itemstack) <= 0;
            }
            else if (slotID == 1)
            {
                return ((IItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) <= 0 || this.getEnergyStoredGC() >= this.getMaxEnergyStoredGC();
            }
        }

        return false;

    }

    @Override
    public EnumSet<EnumFacing> getElectricalInputDirections()
    {
        return EnumSet.of(getElectricInputDirection());
    }

    @Override
    public EnumSet<EnumFacing> getElectricalOutputDirections()
    {
        return EnumSet.of(getElectricOutputDirection());
    }

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        if (direction == null || type != NetworkType.POWER)
        {
            return false;
        }

        return getElectricalInputDirections().contains(direction) || getElectricalOutputDirections().contains(direction);
    }
    
    @Override
    public EnumFacing getFront()
    {
        IBlockState state = this.worldObj.getBlockState(getPos()); 
        if (state.getBlock() instanceof BlockMachineTiered)
        {
            return (state.getValue(BlockMachineTiered.FACING));
        }
        else if (state.getBlock() instanceof BlockMachine)
        {
            return (state.getValue(BlockMachine.FACING));
        }
        return EnumFacing.NORTH;
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        switch (this.getSide(MachineSide.ELECTRIC_IN))
        {
        case LEFT:
            return getFront().rotateY();
        case REAR:
            return getFront().getOpposite();
        case TOP:
            return EnumFacing.UP;
        case BOTTOM:
            return EnumFacing.DOWN;
        case RIGHT:
        default:
            return getFront().rotateYCCW();
        }
    }

    @Override
    public EnumFacing getElectricOutputDirection()
    {
        switch (this.getSide(MachineSide.ELECTRIC_OUT))
        {
        case RIGHT:
            return getFront().rotateYCCW();
        case REAR:
            return getFront().getOpposite();
        case TOP:
            return EnumFacing.UP;
        case BOTTOM:
            return EnumFacing.DOWN;
        case LEFT:
        default:
            return getFront().rotateY();
        }
    }

    //------------------
    //Added these methods and field to implement IMachineSides properly 
    //------------------
    @Override
    public MachineSide[] listConfigurableSides()
    {
        return new MachineSide[] { MachineSide.ELECTRIC_IN, MachineSide.ELECTRIC_OUT };
    }

    @Override
    public Face[] listDefaultFaces()
    {
        return new Face[] { Face.RIGHT, Face.LEFT };
    }

    private MachineSidePack[] machineSides;

    @Override
    public MachineSidePack[] getAllMachineSides()
    {
        if (this.machineSides == null)
        {
            this.initialiseSides();
        }

        return this.machineSides;
    }

    @Override
    public void setupMachineSides(int length)
    {
        this.machineSides = new MachineSidePack[length];
    }
    
    @Override
    public void onLoad()
    {
        this.clientOnLoad();
    }
    
    @Override
    public IMachineSidesProperties getConfigurationType()
    {
        return BlockMachineTiered.MACHINESIDES_RENDERTYPE;
    }
    //------------------END OF IMachineSides implementation
}
