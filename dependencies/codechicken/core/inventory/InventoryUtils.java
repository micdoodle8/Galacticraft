package codechicken.core.inventory;

import com.google.common.base.Objects;

import codechicken.lib.vec.Vector3;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class InventoryUtils
{
    public static ItemStack decrStackSize(IInventory inv, int slot, int size)
    {
        ItemStack item = inv.getStackInSlot(slot);
        
        if(item != null)
        {
            if(item.stackSize <= size)
            {
                ItemStack itemstack = item;
                inv.setInventorySlotContents(slot, null);
                inv.onInventoryChanged();
                return itemstack;
            }
            ItemStack itemstack1 = item.splitStack(size);
            if(item.stackSize == 0)
            {
                inv.setInventorySlotContents(slot, null);
            }
            inv.onInventoryChanged();
            return itemstack1;
        }
        return null;
    }

    public static ItemStack getStackInSlotOnClosing(IInventory inv, int slot)
    {
        ItemStack stack = inv.getStackInSlot(slot);
        inv.setInventorySlotContents(slot, null);
        return stack;
    }

    /**
     * 
     * @param base
     * @param addition
     * @return The quantity of items from addition that can be added to base
     */
    public static int incrStackSize(ItemStack base, ItemStack addition)
    {
        if (canStack(base, addition))
            return incrStackSize(base, addition.stackSize);
        
        return 0;
    }
    
    /**
     * 
     * @param base
     * @param addition
     * @return The quantity of items from addition that can be added to base
     */
    public static int incrStackSize(ItemStack base, int addition)
    {
        int totalSize = base.stackSize + addition;

        if (totalSize <= base.getMaxStackSize())
            return addition;
        else if (base.stackSize < base.getMaxStackSize())
            return base.getMaxStackSize() - base.stackSize;
        
        return 0;
    }

    public static NBTTagList writeItemStacksToTag(ItemStack[] items)
    {
        return writeItemStacksToTag(items, 64);
    }
    
    public static NBTTagList writeItemStacksToTag(ItemStack[] items, int maxQuantity)
    {
        NBTTagList tagList = new NBTTagList();
        for(int i = 0; i < items.length; i++)
        {
            if (items[i] != null)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setShort("Slot", (short) i);
                items[i].writeToNBT(tag);
                
                if(maxQuantity > Short.MAX_VALUE)
                    tag.setInteger("Quantity", items[i].stackSize);
                else if(maxQuantity > Byte.MAX_VALUE)
                    tag.setShort("Quantity", (short) items[i].stackSize);
                
                tagList.appendTag(tag);
            }
        }
        return tagList;
    }
    
    public static void readItemStacksFromTag(ItemStack[] items, NBTTagList tagList)
    {
        for(int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
            int b = tag.getShort("Slot");
            items[b] = ItemStack.loadItemStackFromNBT(tag);
            if(tag.hasKey("Quantity"))
            {
                NBTBase qtag = tag.getTag("Quantity");
                if(qtag instanceof NBTTagInt)
                    items[b].stackSize = ((NBTTagInt)qtag).data;
                else if(qtag instanceof NBTTagShort)
                    items[b].stackSize = ((NBTTagShort)qtag).data;
            }
        }
    }

    public static void dropItem(ItemStack stack, World world, Vector3 dropLocation)
    {
        EntityItem item = new EntityItem(world, dropLocation.x, dropLocation.y, dropLocation.z, stack);
        item.motionX = world.rand.nextGaussian() * 0.05;
        item.motionY = world.rand.nextGaussian() * 0.05 + 0.2F;
        item.motionZ = world.rand.nextGaussian() * 0.05;
        world.spawnEntityInWorld(item);
    }

    public static ItemStack copyStack(ItemStack stack, int quantity)
    {
        if(stack == null)
            return null;
        
        stack = stack.copy();
        stack.stackSize = quantity;
        return stack;
    }
    
    /**
     * {@link ItemStack}s with damage -1 are wildcards allowing all damages. Eg all colours of wool are allowed to create Beds.
     * @param stack1 The {@link ItemStack} being compared.
     * @param stack2 The {@link ItemStack} to compare to.
     * @return whether the two items are the same from the perspective of a crafting inventory.
     */
    public static boolean areStacksSameTypeCrafting(ItemStack stack1, ItemStack stack2)
    {
        if(stack1 == null || stack2 == null)
            return false;
        
        return stack1.itemID == stack2.itemID && 
                (stack1.getItemDamage() == stack2.getItemDamage() || 
                stack1.getItemDamage() == -1 || stack2.getItemDamage() == -1 || 
                stack1.getItem().isDamageable());
    }
    
    public static int getInsertableQuantity(InventoryRange inv, ItemStack stack)
    {
        int quantity = 0;
        stack = copyStack(stack, Integer.MAX_VALUE);
        for(int slot : inv.slots)
            quantity+=fitStackInSlot(inv, slot, stack);
        
        return quantity;
    }
    
    public static int fitStackInSlot(InventoryRange inv, int slot, ItemStack stack)
    {
        ItemStack base = inv.inv.getStackInSlot(slot);
        if(!canStack(base, stack) || !inv.canInsertItem(slot, stack))
            return 0;

        int fit = base != null ? incrStackSize(base, inv.inv.getInventoryStackLimit()-base.stackSize) : inv.inv.getInventoryStackLimit();
        return Math.min(fit, stack.stackSize);
    }

    public static boolean mergeItemStack(InventoryRange inv, ItemStack stack, boolean doMerge)
    {
        if(doMerge && !mergeItemStack(inv, stack, false))
            return false;

        stack = stack.copy();
        for(int pass = 0; pass < 2; pass++)
        {
            for(int slot : inv.slots)
            {
                ItemStack base = inv.inv.getStackInSlot(slot);
                int fit = fitStackInSlot(inv, slot, stack);
                if(fit == 0)
                    continue;
                
                if(base != null)
                {
                    stack.stackSize-=fit;
                    if(doMerge)
                    {
                        base.stackSize+=fit;
                        inv.inv.setInventorySlotContents(slot, base);
                    }
                }
                else if(pass == 1)
                {
                    if(doMerge)
                        inv.inv.setInventorySlotContents(slot, copyStack(stack, fit));
                    stack.stackSize-=fit;
                }
                if(stack.stackSize == 0)
                    return true;
            }
        }
        return false;
    }

    public static boolean areStacksIdentical(ItemStack stack1, ItemStack stack2)
    {
        if(stack1 == null || stack2 == null)
            return stack1 == stack2;
        return stack1.itemID == stack2.itemID
                && stack1.getItemDamage() == stack2.getItemDamage()
                && stack1.stackSize == stack2.stackSize
                && Objects.equal(stack1.getTagCompound(), stack2.getTagCompound());
    }
    
    public static IInventory getInventory(World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if(!(tile instanceof IInventory))
            return null;
        
        if(tile instanceof TileEntityChest)
            return getChest((TileEntityChest) tile);
        return (IInventory)tile;
        
    }
    
    public static final ForgeDirection[] chestSides = new ForgeDirection[]{ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH};
    public static IInventory getChest(TileEntityChest chest)
    {
        for(ForgeDirection fside : chestSides)
        {
            if(chest.worldObj.getBlockId(chest.xCoord+fside.offsetX, chest.yCoord+fside.offsetY, chest.zCoord+fside.offsetZ) == chest.getBlockType().blockID)
                return new InventoryLargeChest("container.chestDouble", 
                        (TileEntityChest)chest.worldObj.getBlockTileEntity(chest.xCoord+fside.offsetX, chest.yCoord+fside.offsetY, chest.zCoord+fside.offsetZ), chest);
        }
        return chest;
    }

    public static boolean canStack(ItemStack stack1, ItemStack stack2)
    {
        return stack1 == null || stack2 == null || 
                (stack1.itemID == stack2.itemID && 
                (!stack2.getHasSubtypes() || stack2.getItemDamage() == stack1.getItemDamage()) && 
                ItemStack.areItemStackTagsEqual(stack2, stack1)) && 
                stack1.isStackable();
    }

    public static void consumeItem(IInventory inv, int slot)
    {
        ItemStack stack = inv.getStackInSlot(slot);
        Item item = stack.getItem();
        if(item.hasContainerItem())
        {
            ItemStack container = item.getContainerItemStack(stack);
            inv.setInventorySlotContents(slot, container);
        }
        else
        {
            inv.decrStackSize(slot, 1);
        }
    }

    public static int stackSize(IInventory inv, int slot)
    {
        ItemStack stack = inv.getStackInSlot(slot);
        return stack == null ? 0 : stack.stackSize;
    }
    
    public static ItemStack getRemovableStack(InventoryRange inv, int slot)
    {
        ItemStack stack = inv.inv.getStackInSlot(slot);
        if(stack == null || !inv.canExtractItem(slot, stack))
            return null;
        
        return stack;
    }

    public static void dropOnClose(EntityPlayer player, IInventory inv)
    {
        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack stack = inv.getStackInSlotOnClosing(i);
            if (stack != null)
                player.dropPlayerItem(stack);
        }
    }
    
    public static int actualDamage(ItemStack stack)
    {
        return Item.diamond.getDamage(stack);
    }
}
