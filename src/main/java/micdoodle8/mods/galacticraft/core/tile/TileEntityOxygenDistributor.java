package micdoodle8.mods.galacticraft.core.tile;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.block.IOxygenReliantBlock;
import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.entities.EntityBubble;
import micdoodle8.mods.galacticraft.core.entities.IBubble;
import micdoodle8.mods.galacticraft.core.entities.IBubbleProvider;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class TileEntityOxygenDistributor extends TileEntityOxygen implements IInventory, ISidedInventory, IBubbleProvider
{
    public boolean active;
    public boolean lastActive;

    private ItemStack[] containingItems = new ItemStack[2];
    public EntityBubble oxygenBubble;
    public static ArrayList<TileEntityOxygenDistributor> loadedTiles = new ArrayList();
    /**
     * Used for saving/loading old oxygen bubbles
     */
    private boolean hasValidBubble;

    public TileEntityOxygenDistributor()
    {
        super(6000, 8);
        this.oxygenBubble = null;
    }

    @Override
    public void validate()
    {
    	super.validate();
        if (!this.worldObj.isRemote) TileEntityOxygenDistributor.loadedTiles.add(this);
    }

    @Override
    public void onChunkUnload()
    {
        if (!this.worldObj.isRemote) TileEntityOxygenDistributor.loadedTiles.add(this);
    	super.onChunkUnload();
    }

    @Override
    public void invalidate()
    {
        if (this.oxygenBubble != null)
        {
            for (int x = (int) Math.floor(this.xCoord - this.oxygenBubble.getSize()); x < Math.ceil(this.xCoord + this.oxygenBubble.getSize()); x++)
            {
                for (int y = (int) Math.floor(this.yCoord - this.oxygenBubble.getSize()); y < Math.ceil(this.yCoord + this.oxygenBubble.getSize()); y++)
                {
                    for (int z = (int) Math.floor(this.zCoord - this.oxygenBubble.getSize()); z < Math.ceil(this.zCoord + this.oxygenBubble.getSize()); z++)
                    {
                        Block block = this.worldObj.getBlock(x, y, z);

                        if (block instanceof IOxygenReliantBlock)
                        {
                            ((IOxygenReliantBlock) block).onOxygenRemoved(this.worldObj, x, y, z);
                        }
                    }
                }
            }
        }

        if (!this.worldObj.isRemote) TileEntityOxygenDistributor.loadedTiles.remove(this);
        super.invalidate();
    }

    public void addExtraNetworkedData(List<Object> networkedList)
    {
        if (!this.worldObj.isRemote)
        {
            networkedList.add(this.oxygenBubble != null);
            if (this.oxygenBubble != null)
            {
                networkedList.add(this.oxygenBubble.getEntityId());
            }
        }
    }

    public void readExtraNetworkedData(ByteBuf dataStream)
    {
        if (this.worldObj.isRemote)
        {
            if (dataStream.readBoolean())
            {
                this.oxygenBubble = (EntityBubble) worldObj.getEntityByID(dataStream.readInt());
            }
        }
    }

    public double getDistanceFromServer(double par1, double par3, double par5)
    {
        final double d3 = this.xCoord + 0.5D - par1;
        final double d4 = this.yCoord + 0.5D - par3;
        final double d5 = this.zCoord + 0.5D - par5;
        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    @Override
    public void updateEntity()
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
    	
    	super.updateEntity();

        if (!hasValidBubble && !this.worldObj.isRemote && (this.oxygenBubble == null || this.ticks < 25))
        {
            if (this.oxygenBubble == null)
            {
                this.oxygenBubble = new EntityBubble(this.worldObj, new Vector3(this), this);
                this.hasValidBubble = true;
                this.worldObj.spawnEntityInWorld(this.oxygenBubble);
            }
        }

        if (!this.worldObj.isRemote && this.oxygenBubble != null)
        {
            this.active = this.oxygenBubble.getSize() >= 1 && this.hasEnoughEnergyToRun;
        }

        if (!this.worldObj.isRemote && (this.active != this.lastActive || this.ticks % 20 == 0))
        {
            if (this.active && this.oxygenBubble != null)
            {
                double bubbleR2 = this.oxygenBubble.getSize() - 0.5D;
                bubbleR2 *= bubbleR2;
                int bubbleR = MathHelper.floor_double(this.oxygenBubble.getSize() + 4);
            	for (int x = this.xCoord - bubbleR; x <= this.xCoord + bubbleR; x++)
                {
                    for (int y = this.yCoord - bubbleR; y <= this.yCoord + bubbleR; y++)
                    {
                        for (int z = this.zCoord - bubbleR; z <= this.zCoord + bubbleR; z++)
                        {
                            Block block = this.worldObj.getBlock(x, y, z);

                            if (block instanceof IOxygenReliantBlock)
                            {
                            	if (this.getDistanceFromServer(x, y, z) < bubbleR2)
                                {
                                    ((IOxygenReliantBlock) block).onOxygenAdded(this.worldObj, x, y, z);
                                }
                                else
                                {
                                    ((IOxygenReliantBlock) block).onOxygenRemoved(this.worldObj, x, y, z);
                                }
                            }
                        }
                    }
                }
            }
        }

        this.lastActive = this.active;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        this.hasValidBubble = nbt.getBoolean("hasValidBubble");

        final NBTTagList var2 = nbt.getTagList("Items", 10);
        this.containingItems = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            final byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.containingItems.length)
            {
                this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setBoolean("hasValidBubble", this.hasValidBubble);

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

        nbt.setTag("Items", list);
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
    public String getInventoryName()
    {
        return GCCoreUtil.translate("container.oxygendistributor.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return new int[] { 0, 1 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
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
    public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
    {
    	switch (slotID)
    	{
    	case 0:
    		return itemstack.getItem() instanceof ItemElectricBase && ((ItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) <= 0;
    	case 1:
    		return FluidContainerRegistry.isEmptyContainer(itemstack);
    	default:
    		return false;
    	}
    }

    @Override
    public boolean hasCustomInventoryName()
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
    public void openInventory()
    {

    }

    @Override
    public void closeInventory()
    {

    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.storedOxygen > this.oxygenPerTick;
    }

    @Override
    public ForgeDirection getElectricInputDirection()
    {
        return ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }

    @Override
    public boolean shouldUseOxygen()
    {
        return this.hasEnoughEnergyToRun;
    }

    @Override
    public EnumSet<ForgeDirection> getOxygenInputDirections()
    {
        return EnumSet.of(this.getElectricInputDirection().getOpposite());
    }

    @Override
    public EnumSet<ForgeDirection> getOxygenOutputDirections()
    {
        return EnumSet.noneOf(ForgeDirection.class);
    }

    @Override
    public IBubble getBubble()
    {
        return this.oxygenBubble;
    }

    @Override
    public void setBubbleVisible(boolean shouldRender)
    {
        if (this.oxygenBubble == null)
        {
            return;
        }

        this.oxygenBubble.setShouldRender(shouldRender);
    }
}
