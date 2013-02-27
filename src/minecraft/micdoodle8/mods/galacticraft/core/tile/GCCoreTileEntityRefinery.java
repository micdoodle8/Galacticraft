package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreTileEntityRefinery extends TileEntity implements IInventory, ISidedInventory
{
    private ItemStack[] refineryItemStacks = new ItemStack[4];
    
    public boolean isActive;

    public int refineryBurnTime = 0;
    
    public int currentItemBurnTime = 0;
    public int currentItemBurnTime2 = 0;

    public int refineryCookTime = 0;

    public int getSizeInventory()
    {
        return this.refineryItemStacks.length;
    }

    public ItemStack getStackInSlot(int par1)
    {
        return this.refineryItemStacks[par1];
    }

    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.refineryItemStacks[par1] != null)
        {
            ItemStack var3;

            if (this.refineryItemStacks[par1].stackSize <= par2)
            {
                var3 = this.refineryItemStacks[par1];
                this.refineryItemStacks[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.refineryItemStacks[par1].splitStack(par2);

                if (this.refineryItemStacks[par1].stackSize == 0)
                {
                    this.refineryItemStacks[par1] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.refineryItemStacks[par1] != null)
        {
            ItemStack var2 = this.refineryItemStacks[par1];
            this.refineryItemStacks[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.refineryItemStacks[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    public String getInvName()
    {
        return "refinery";
    }

    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.refineryItemStacks = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.refineryItemStacks.length)
            {
                this.refineryItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        this.refineryBurnTime = par1NBTTagCompound.getShort("BurnTime");
        this.refineryCookTime = par1NBTTagCompound.getShort("CookTime");
        this.currentItemBurnTime = getItemBurnTime(this.refineryItemStacks[1]);
        this.currentItemBurnTime2 = getItemBurnTime(this.refineryItemStacks[2]);
    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("BurnTime", (short)this.refineryBurnTime);
        par1NBTTagCompound.setShort("CookTime", (short)this.refineryCookTime);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.refineryItemStacks.length; ++var3)
        {
            if (this.refineryItemStacks[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.refineryItemStacks[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", var2);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int par1, int par2)
    {
        return this.refineryCookTime * par1 / 800 * par2;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int par1)
    {
        if (this.currentItemBurnTime == 0)
        {
            this.currentItemBurnTime = 800;
        }

        return this.refineryBurnTime * par1 / this.currentItemBurnTime;
    }

    public boolean isCookin()
    {
        return this.refineryBurnTime > 0;
    }

    public void updateEntity()
    {
        boolean var1 = this.refineryBurnTime > 0;
        boolean var2 = false;

        if (this.refineryBurnTime > 0)
        {
            --this.refineryBurnTime;
        }
        
        if (this.refineryBurnTime == 0 && this.canSmelt())
        {
            this.currentItemBurnTime = this.currentItemBurnTime2 = this.refineryBurnTime = getItemBurnTime(this.refineryItemStacks[1]) + this.getItemBurnTime(this.refineryItemStacks[2]);
          
            if (this.currentItemBurnTime == currentItemBurnTime2 && this.refineryBurnTime > 0)
            {
                var2 = true;

                if (this.refineryItemStacks[1] != null)
                {
                    --this.refineryItemStacks[1].stackSize;

                    if (this.refineryItemStacks[1].stackSize == 0)
                    {
                        this.refineryItemStacks[1] = this.refineryItemStacks[1].getItem().getContainerItemStack(refineryItemStacks[1]);
                    }
                }

                if (this.refineryItemStacks[2] != null)
                {
                    --this.refineryItemStacks[2].stackSize;

                    if (this.refineryItemStacks[2].stackSize == 0)
                    {
                        this.refineryItemStacks[2] = this.refineryItemStacks[2].getItem().getContainerItemStack(refineryItemStacks[2]);
                    }
                }
            }
        }

        if (this.isCookin() && this.canSmelt())
        {
            ++this.refineryCookTime;

            if (this.refineryCookTime == 800)
            {
                this.refineryCookTime = 0;
                this.smeltItem();
                var2 = true;
            }
        }
        else
        {
            this.refineryCookTime = 0;
        }

        if (var1 != this.refineryBurnTime > 0)
        {
            var2 = true;
            this.isActive = this.refineryBurnTime > 0;
        }

        if (var2)
        {
            this.onInventoryChanged();
        }
    }

    private boolean canSmelt()
    {
        if (this.refineryItemStacks[0] == null)
        {
            return false;
        }
        else
        {
            ItemStack var1 = new ItemStack(GCCoreItems.rocketFuelBucket);
            
            if (this.refineryItemStacks[3] == null) 
            {
            	return true;
            }
            	
            if (!(this.refineryItemStacks[3].itemID == var1.itemID && (this.refineryItemStacks[3].getMaxDamage() - this.refineryItemStacks[3].getItemDamage()) >= 0 && (this.refineryItemStacks[3].getMaxDamage() - this.refineryItemStacks[3].getItemDamage()) < 60))
            {
            	return false;
            }
            
            int result = refineryItemStacks[3].stackSize + var1.stackSize;
            
            return (result <= getInventoryStackLimit() && result <= var1.getMaxStackSize());
        }
    }

    public void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack var1 = new ItemStack(GCCoreItems.rocketFuelBucket, 1, this.refineryItemStacks[0].getItemDamage());

            if (this.refineryItemStacks[3] == null)
            {
                this.refineryItemStacks[3] = var1.copy();
            }
            else if (this.refineryItemStacks[3].isItemEqual(var1))
            {
                refineryItemStacks[3].stackSize += var1.stackSize;
            }

            --this.refineryItemStacks[0].stackSize;

            if (this.refineryItemStacks[0].stackSize <= 0)
            {
                this.refineryItemStacks[0] = null;
            }
        }
    }

    public static int getItemBurnTime(ItemStack par0ItemStack)
    {
        if (par0ItemStack == null)
        {
            return 0;
        }
        else
        {
            int var1 = par0ItemStack.getItem().itemID;
            Item var2 = par0ItemStack.getItem();

            if (par0ItemStack.getItem() instanceof ItemBlock && Block.blocksList[var1] != null)
            {
                Block var3 = Block.blocksList[var1];

                if (var3 == Block.woodSingleSlab)
                {
                    return 75;
                }

                if (var3.blockMaterial == Material.wood)
                {
                    return 150;
                }
            }

            if (var2 instanceof ItemTool && ((ItemTool) var2).getToolMaterialName().equals("WOOD")) return 100;
            if (var2 instanceof ItemSword && ((ItemSword) var2).getToolMaterialName().equals("WOOD")) return 100;
            if (var2 instanceof ItemHoe && ((ItemHoe) var2).func_77842_f().equals("WOOD")) return 100;
            if (var1 == Item.stick.itemID) return 50;
            if (var1 == Item.coal.itemID) return 800;
            if (var1 == Item.bucketLava.itemID) return 10000;
            if (var1 == Block.sapling.blockID) return 50;
            if (var1 == Item.blazeRod.itemID) return 1200;
            return GameRegistry.getFuelValue(par0ItemStack) / 2;
        }
    }

    public static boolean isItemFuel(ItemStack par0ItemStack)
    {
        return getItemBurnTime(par0ItemStack) > 0;
    }

    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openChest() {}

    public void closeChest() {}

    @Override
    public int getStartInventorySide(ForgeDirection side)
    {
        if (side == ForgeDirection.DOWN) return 1;
        if (side == ForgeDirection.UP) return 0;
        return 2;
    }

    @Override
    public int getSizeInventorySide(ForgeDirection side)
    {
        return 1;
    }
}
