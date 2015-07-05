package micdoodle8.mods.galacticraft.core.tile;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.block.IOxygenReliantBlock;
import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3Dim;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.entities.EntityBubble;
import micdoodle8.mods.galacticraft.core.entities.IBubble;
import micdoodle8.mods.galacticraft.core.entities.IBubbleProvider;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class TileEntityOxygenDistributor extends TileEntityOxygen implements IInventory, ISidedInventory, IBubbleProvider
{
    public boolean active;
    public boolean lastActive;

    private ItemStack[] containingItems = new ItemStack[2];
    public EntityBubble oxygenBubble;
    public static ArrayList<BlockVec3Dim> loadedTiles = Lists.newArrayList();
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
        if (!this.worldObj.isRemote) TileEntityOxygenDistributor.loadedTiles.add(new BlockVec3Dim(this.xCoord, this.yCoord, this.zCoord, this.worldObj.provider.dimensionId));
    }

    @Override
    public void onChunkUnload()
    {
    	Iterator<BlockVec3Dim> i = loadedTiles.iterator();
    	while (i.hasNext())
    	{
    		BlockVec3Dim vec = i.next();
    		if (vec.dim == this.worldObj.provider.dimensionId && xCoord == vec.x && yCoord == vec.y && zCoord == vec.z)
    		{
    			i.remove();
    		}
    	}
    	super.onChunkUnload();
    }

    @Override
    public void invalidate()
    {
        if (!this.worldObj.isRemote && this.oxygenBubble != null)
        {
            double size = this.oxygenBubble.getSize();
        	int bubbleR = MathHelper.ceiling_double_int(size);
            int bubbleR2 = (int) (size * size);
        	for (int x = this.xCoord - bubbleR; x < this.xCoord + bubbleR; x++)
            {
                for (int y = this.yCoord - bubbleR; y < this.yCoord + bubbleR; y++)
                {
                    for (int z = this.zCoord - bubbleR; z < this.zCoord + bubbleR; z++)
                    {
                        Block block = this.worldObj.getBlock(x, y, z);

                        if (block instanceof IOxygenReliantBlock && this.getDistanceFromServer(x, y, z) <= bubbleR2)
                        {
                        	this.worldObj.scheduleBlockUpdateWithPriority(x, y, z, block, 1, 0);
                        }
                    }
                }
            }
        	this.oxygenBubble.setDead();
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
            networkedList.add(loadedTiles.size());
            for (BlockVec3Dim distributor : new ArrayList<BlockVec3Dim>(loadedTiles))
            {
            	networkedList.add(distributor.x);
            	networkedList.add(distributor.y);
            	networkedList.add(distributor.z);
            	networkedList.add(distributor.dim);
            }
        }
    }

    public void readExtraNetworkedData(ByteBuf dataStream)
    {
    	loadedTiles.clear();
        if (this.worldObj.isRemote)
        {
            if (dataStream.readBoolean())
            {
                this.oxygenBubble = (EntityBubble) worldObj.getEntityByID(dataStream.readInt());
            }
            int size = dataStream.readInt();
            for (int i = 0; i < size; ++i)
            {
            	this.loadedTiles.add(new BlockVec3Dim(dataStream.readInt(), dataStream.readInt(), dataStream.readInt(), dataStream.readInt()));
            }
        }
    }

    public int getDistanceFromServer(int par1, int par3, int par5)
    {
        final int d3 = this.xCoord - par1;
        final int d4 = this.yCoord - par3;
        final int d5 = this.zCoord - par5;
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
            //Check it's a world without a breathable atmosphere
        	if (this.oxygenBubble == null && this.worldObj.provider instanceof IGalacticraftWorldProvider && !((IGalacticraftWorldProvider)this.worldObj.provider).hasBreathableAtmosphere())
            {
                this.oxygenBubble = new EntityBubble(this.worldObj, new Vector3(this), this);
                this.hasValidBubble = true;
                this.worldObj.spawnEntityInWorld(this.oxygenBubble);
            }
        }

        if (!this.worldObj.isRemote && this.oxygenBubble != null)
        {
            this.active = this.oxygenBubble.getSize() >= 1 && this.hasEnoughEnergyToRun && this.storedOxygen > this.oxygenPerTick;

            if (this.ticks % (this.active ? 20 : 4) == 0)
	        {
                double size = this.oxygenBubble.getSize();
                int bubbleR = MathHelper.floor_double(size) + 4;
                int bubbleR2 = (int) (size * size);
            	for (int x = this.xCoord - bubbleR; x <= this.xCoord + bubbleR; x++)
                {
                    for (int y = this.yCoord - bubbleR; y <= this.yCoord + bubbleR; y++)
                    {
                        for (int z = this.zCoord - bubbleR; z <= this.zCoord + bubbleR; z++)
                        {
                            Block block = this.worldObj.getBlock(x, y, z);

                            if (block instanceof IOxygenReliantBlock)
                            {
                            	if (this.getDistanceFromServer(x, y, z) <= bubbleR2)
                                {
                                    ((IOxygenReliantBlock) block).onOxygenAdded(this.worldObj, x, y, z);
                                }
                                else
                                {
                                	//Do not necessarily extinguish it - it might be inside another oxygen system
                                	this.worldObj.scheduleBlockUpdateWithPriority(x, y, z, block, 1, 0);
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
            final int var5 = var4.getByte("Slot") & 255;

            if (var5 < this.containingItems.length)
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
    		return FluidUtil.isEmptyContainer(itemstack);
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
    public EnumFacing getElectricInputDirection()
    {
        return EnumFacing.getOrientation(this.getBlockMetadata() + 2);
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
    public IBubble getBubble()
    {
        return this.oxygenBubble;
    }

    public boolean inBubble(double pX, double pY, double pZ)
    {
		if (this.oxygenBubble != null)
		{
	        double r = this.oxygenBubble.getSize();
	        r *= r;
	        double d3 = this.xCoord + 0.5D - pX;
	        d3 *= d3;
	        if (d3 > r) return false;
	        double d4 = this.zCoord + 0.5D - pZ;
	        d4 *= d4;
	        if (d3 + d4 > r) return false;
	        double d5 = this.yCoord + 0.5D - pY;
	        return d3 + d4 + d5 * d5 < r;
		}
		return false;
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
