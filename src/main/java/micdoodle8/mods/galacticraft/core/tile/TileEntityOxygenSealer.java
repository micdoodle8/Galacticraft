package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenPressureProtocol;
import micdoodle8.mods.galacticraft.core.oxygen.ThreadFindSeal;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.EnumSet;

public class TileEntityOxygenSealer extends TileEntityOxygen implements IInventory, ISidedInventory
{
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean sealed;
    public boolean lastSealed = false;

    public boolean lastDisabled = false;

    @NetworkedField(targetSide = Side.CLIENT)
    public boolean active;
    private ItemStack[] containingItems = new ItemStack[2];
    public ThreadFindSeal threadSeal;
    @NetworkedField(targetSide = Side.CLIENT)
    public int stopSealThreadCooldown;
    @NetworkedField(targetSide = Side.CLIENT)
    public int threadCooldownTotal;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean calculatingSealed;
    public static int countEntities = 0;
    private static int countTemp = 0;
    private static long ticksSave = 0L;
    private static boolean sealerCheckedThisTick = false;
    public static ArrayList<TileEntityOxygenSealer> loadedTiles = new ArrayList();


    public TileEntityOxygenSealer()
    {
        super(10000, 16);
    }

    @Override
    public void validate()
    {
    	super.validate();
        if (!this.worldObj.isRemote) TileEntityOxygenSealer.loadedTiles.add(this);
    }

    @Override
    public void invalidate()
    {
        if (!this.worldObj.isRemote) TileEntityOxygenSealer.loadedTiles.remove(this);
    	super.invalidate();
    }

    @Override
    public void onChunkUnload()
    {
        if (!this.worldObj.isRemote) TileEntityOxygenSealer.loadedTiles.remove(this);
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
        if (!this.active || this.storedOxygen < this.oxygenPerTick || !this.hasEnoughEnergyToRun)
        {
            return 0;
        }
        BlockPos posAbove = new BlockPos(this.getPos().getX(), this.getPos().getY() + 1, this.getPos().getZ());
        Block blockAbove = this.worldObj.getBlockState(posAbove).getBlock();
        if (!(blockAbove.isAir(this.worldObj, posAbove)) && !OxygenPressureProtocol.canBlockPassAir(this.worldObj, blockAbove, new BlockVec3(this.getPos().getX(), this.getPos().getY() + 1, this.getPos().getZ()), 1))
        {
            // The vent is blocked
            return 0;
        }

        return 1250;
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
	    		float oxygenDraw = Math.min(this.oxygenPerTick * 2.5F, this.maxOxygen - this.storedOxygen);
	    		this.storedOxygen += oxygenItem.discharge(oxygenItemStack, oxygenDraw);
	    		if (this.storedOxygen > this.maxOxygen) this.storedOxygen = this.maxOxygen;
	    	}
        }
    	
        super.update();

        if (!this.worldObj.isRemote)
        {
            // Some code to count the number of Oxygen Sealers being updated,
            // tick by tick - needed for queueing
            if (this.ticks == TileEntityOxygenSealer.ticksSave)
            {
                TileEntityOxygenSealer.countTemp++;
            }
            else
            {
                TileEntityOxygenSealer.ticksSave = this.ticks;
                TileEntityOxygenSealer.countEntities = TileEntityOxygenSealer.countTemp;
                TileEntityOxygenSealer.countTemp = 1;
                TileEntityOxygenSealer.sealerCheckedThisTick = false;
            }

            this.active = this.storedOxygen >= 1 && this.hasEnoughEnergyToRun && !this.disabled;

            if (this.stopSealThreadCooldown > 0)
            {
                this.stopSealThreadCooldown--;
            }
            else if (!TileEntityOxygenSealer.sealerCheckedThisTick)
            {
                // This puts any Sealer which is updated to the back of the queue for updates
                this.threadCooldownTotal = this.stopSealThreadCooldown = 75 + TileEntityOxygenSealer.countEntities;
                TileEntityOxygenSealer.sealerCheckedThisTick = true;
                OxygenPressureProtocol.updateSealerStatus(this);
            }

            if (this.threadSeal != null)
            {
                this.sealed = this.active && this.threadSeal.sealedFinal.get();
                this.calculatingSealed = this.active && this.threadSeal.looping.get();
            }

            this.lastDisabled = this.disabled;
            this.lastSealed = this.sealed;
        }
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
    public ItemStack getStackInSlotOnClosing(int par1)
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

    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
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
                return itemstack.getItem() instanceof ItemElectricBase && ((ItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) > 0;
            case 1:
            	return itemstack.getItemDamage() < itemstack.getItem().getMaxDamage();
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
    		return itemstack.getItem() instanceof ItemElectricBase && ((ItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) <= 0;
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
    	if (itemstack == null) return false; 
    	if (slotID == 0) return ItemElectricBase.isElectricItem(itemstack.getItem());
        if (slotID == 1) return itemstack.getItem() instanceof IItemOxygenSupply;
        return false;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.storedOxygen > this.oxygenPerTick && !this.getDisabled(0);
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        return getFacing();
//        return EnumFacing.getFront(this.getBlockMetadata() + 2);
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }

    @Override
    public boolean shouldUseOxygen()
    {
        return this.hasEnoughEnergyToRun && this.active && this.sealed;
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

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public IChatComponent getDisplayName() {
        return null;
    }
}
