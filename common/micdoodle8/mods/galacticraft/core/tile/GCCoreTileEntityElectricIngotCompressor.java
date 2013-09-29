package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockMachine2;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class GCCoreTileEntityElectricIngotCompressor extends GCCoreTileEntityElectric implements IInventory, ISidedInventory, IPacketReceiver
{
    public static final int PROCESS_TIME_REQUIRED = 200;
    public static final float WATTS_PER_TICK_PER_STACK = 0.15F;
    public int processTicks1 = 0;
    public int processTicks2 = 0;
    private long ticks;

    private ItemStack[] containingItems = new ItemStack[5];

    public GCCoreTileEntityElectricIngotCompressor()
    {
        super(WATTS_PER_TICK_PER_STACK * 2, 50);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            boolean updateInv = false;
            int compressingCount = 0;
            
            if (this.getEnergyStored() > 0.0F)
            {                
                if (this.canSmelt1())
                {
                    ++this.processTicks1;
                    
                    compressingCount++;
                    
                    if (this.processTicks1 == PROCESS_TIME_REQUIRED)
                    {
                        this.worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "random.anvil_land", 0.2F, 0.5F);
                        this.processTicks1 = 0;
                        this.smeltItem1();
                        updateInv = true;
                    }
                }
                else
                {
                    this.processTicks1 = 0;
                }

                if (this.canSmelt2())
                {
                    ++this.processTicks2;
                    
                    compressingCount++;
                    
                    if (this.processTicks2 == PROCESS_TIME_REQUIRED)
                    {
                        this.worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "random.anvil_land", 0.2F, 0.5F);
                        this.processTicks2 = 0;
                        this.smeltItem2();
                        updateInv = true;
                    }
                }
                else
                {
                    this.processTicks2 = 0;
                }
                
                this.ueWattsPerTick = compressingCount * GCCoreTileEntityElectricIngotCompressor.WATTS_PER_TICK_PER_STACK;
            }
            else
            {
                this.processTicks1 = 0;
                this.processTicks2 = 0;
            }
            
            if (updateInv)
            {
                this.onInventoryChanged();
            }
        }
        
        if (this.ticks >= Long.MAX_VALUE)
        {
            this.ticks = 0;
        }
        
        this.ticks++;
    }

    @Override
    public void openChest()
    {
    }

    @Override
    public void closeChest()
    {
    }

    private boolean canSmelt1()
    {
        if (this.containingItems[1] == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = CompressorRecipes.getCompressedResult(this.containingItems[1]);
            if (itemstack == null) 
                return false;
            if (this.containingItems[3] == null) 
                return true;
            if (!this.containingItems[3].isItemEqual(itemstack)) 
                return false;
            int result = containingItems[3].stackSize + itemstack.stackSize;
            return (result <= getInventoryStackLimit() && result <= itemstack.getMaxStackSize());
        }
    }

    private boolean canSmelt2()
    {
        if (this.containingItems[2] == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = CompressorRecipes.getCompressedResult(this.containingItems[2]);
            if (itemstack == null) return false;
            if (this.containingItems[4] == null) return true;
            if (!this.containingItems[4].isItemEqual(itemstack)) return false;
            int result = containingItems[4].stackSize + itemstack.stackSize;
            return (result <= getInventoryStackLimit() && result <= itemstack.getMaxStackSize());
        }
    }
    
    public void smeltItem1()
    {
        if (this.canSmelt1())
        {
            ItemStack resultItemStack = CompressorRecipes.getCompressedResult(this.containingItems[1]);

            if (this.containingItems[3] == null)
            {
                this.containingItems[3] = resultItemStack.copy();
            }
            else if (this.containingItems[3].isItemEqual(resultItemStack))
            {
                if (this.containingItems[3].stackSize + resultItemStack.stackSize > 64)
                {
                    for (int i = 0; i < this.containingItems[3].stackSize + resultItemStack.stackSize - 64; i++)
                    {
                        float var = 0.7F;
                        double dx = this.worldObj.rand.nextFloat() * var + (1.0F - var) * 0.5D;
                        double dy = this.worldObj.rand.nextFloat() * var + (1.0F - var) * 0.5D;
                        double dz = this.worldObj.rand.nextFloat() * var + (1.0F - var) * 0.5D;
                        EntityItem entityitem = new EntityItem(this.worldObj, this.xCoord + dx, this.yCoord + dy, this.zCoord + dz, new ItemStack(resultItemStack.getItem(), 1, resultItemStack.getItemDamage()));

                        entityitem.delayBeforeCanPickup = 10;

                        this.worldObj.spawnEntityInWorld(entityitem);
                    }
                }
                
                this.containingItems[3].stackSize += resultItemStack.stackSize;
            }

            this.containingItems[1].stackSize--;

            if (this.containingItems[1].stackSize <= 0)
            {
                this.containingItems[1] = null;
            }
        }
    }
    
    public void smeltItem2()
    {
        if (this.canSmelt2())
        {
            ItemStack resultItemStack = CompressorRecipes.getCompressedResult(this.containingItems[2]);

            if (this.containingItems[4] == null)
            {
                this.containingItems[4] = resultItemStack.copy();
            }
            else if (this.containingItems[4].isItemEqual(resultItemStack))
            {
                if (this.containingItems[4].stackSize + resultItemStack.stackSize > 64)
                {
                    for (int i = 0; i < this.containingItems[5].stackSize + resultItemStack.stackSize - 64; i++)
                    {
                        float var = 0.7F;
                        double dx = this.worldObj.rand.nextFloat() * var + (1.0F - var) * 0.5D;
                        double dy = this.worldObj.rand.nextFloat() * var + (1.0F - var) * 0.5D;
                        double dz = this.worldObj.rand.nextFloat() * var + (1.0F - var) * 0.5D;
                        EntityItem entityitem = new EntityItem(this.worldObj, this.xCoord + dx, this.yCoord + dy, this.zCoord + dz, new ItemStack(resultItemStack.getItem(), 1, resultItemStack.getItemDamage()));

                        entityitem.delayBeforeCanPickup = 10;

                        this.worldObj.spawnEntityInWorld(entityitem);
                    }
                }
                
                this.containingItems[4].stackSize += resultItemStack.stackSize;
            }

            this.containingItems[2].stackSize--;

            if (this.containingItems[2].stackSize <= 0)
            {
                this.containingItems[2] = null;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.processTicks1 = par1NBTTagCompound.getInteger("smeltingTicks");
        this.processTicks2 = par1NBTTagCompound.getInteger("smeltingTicks2");
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.containingItems = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.containingItems.length)
            {
                this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("smeltingTicks", this.processTicks1);
        par1NBTTagCompound.setInteger("smeltingTicks2", this.processTicks2);
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
    public String getInvName()
    {
        return LanguageRegistry.instance().getStringLocalization("tile.machine2.4.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
    {
        return slotID == 1 ? FurnaceRecipes.smelting().getSmeltingResult(itemStack) != null : slotID == 0 ? itemStack.getItem() instanceof IItemElectric : false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return side == 0 ? new int[] { 2 } : side == 1 ? new int[] { 0, 1 } : new int[] { 0 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack par2ItemStack, int par3)
    {
        return this.isItemValidForSlot(slotID, par2ItemStack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack par2ItemStack, int par3)
    {
        return slotID == 2;
    }

    @Override
    public boolean shouldPullEnergy()
    {
        return this.getEnergyStored() <= this.getMaxEnergyStored() - this.ueWattsPerTick;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.processTicks1 > 0 || this.processTicks2 > 0;
    }

    @Override
    public void readPacket(ByteArrayDataInput data)
    {
        try
        {
            this.processTicks1 = data.readInt();
            this.processTicks2 = data.readInt();
            this.setEnergyStored(data.readFloat());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Packet getPacket()
    {
        return PacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.processTicks1, this.processTicks2, this.getEnergyStored());
    }

    @Override
    public ForgeDirection getElectricInputDirection()
    {
        return ForgeDirection.getOrientation(this.getBlockMetadata() - GCCoreBlockMachine2.ELECTRIC_COMPRESSOR_METADATA + 2);
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }
}
