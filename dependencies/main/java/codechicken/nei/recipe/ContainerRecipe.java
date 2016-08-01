package codechicken.nei.recipe;

import codechicken.nei.PositionedStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;

public class ContainerRecipe extends Container {
    private class RecipeInventory implements IInventory {
        @Override
        public boolean isUseableByPlayer(EntityPlayer entityplayer) {
            return true;
        }

        @Override
        public int getSizeInventory() {
            return inventorySlots.size();
        }

        @Override
        public ItemStack getStackInSlot(int i) {
            if (i < 0 || i > inventoryItemStacks.size()) {
                return null;
            }
            return (ItemStack) inventoryItemStacks.get(i);
        }

        @Override
        public ItemStack decrStackSize(int i, int j) {
            return null;
        }

        @Override
        public void setInventorySlotContents(int slot, ItemStack itemstack) {
            if (slot < 0 || slot >= inventoryItemStacks.size()) {
                return;
            }

            inventoryItemStacks.set(slot, itemstack);
        }

        @Override
        public int getInventoryStackLimit() {
            return 1;
        }

        @Override
        public void markDirty() {
        }

        @Override
        public ItemStack removeStackFromSlot(int slot) {
            return null;
        }

        @Override
        public boolean isItemValidForSlot(int i, ItemStack itemstack) {
            return false;
        }

        @Override
        public void openInventory(EntityPlayer player) {
        }

        @Override
        public void closeInventory(EntityPlayer player) {
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
        public String getName() {
            return null;
        }

        @Override
        public boolean hasCustomName() {
            return false;
        }

        @Override
        public IChatComponent getDisplayName() {
            return null;
        }
    }

    private RecipeInventory recipeInventory = new RecipeInventory();

    public void clearInventory() {
        inventoryItemStacks.clear();
        inventorySlots.clear();
    }

    public ItemStack slotClick(int slot, int button, boolean flag, EntityPlayer entityplayer) {
        if (slot < 0) {
            return null;
        }

        ItemStack stack = recipeInventory.getStackInSlot(slot);
        if (stack != null) {
            if (button == 0) {
                GuiCraftingRecipe.openRecipeGui("item", stack);
            } else if (button == 1) {
                GuiUsageRecipe.openRecipeGui("item", stack);
            }
        }
        return null;
    }

    public void addSlot(PositionedStack stack, int recipex, int recipey) {
        int slot = inventorySlots.size();
        addSlotToContainer(new Slot(recipeInventory, slot, recipex + stack.relx, recipey + stack.rely) {
            @Override
            public boolean isItemValid(ItemStack par1ItemStack) {
                return false;
            }
        });
        recipeInventory.setInventorySlotContents(slot, stack.item);
    }

    public Slot getSlotWithStack(PositionedStack stack, int recipex, int recipey) {
        for (int i = 0; i < inventorySlots.size(); i++) {
            Slot slot = (Slot) inventorySlots.get(i);
            if (slot.xDisplayPosition == (stack.relx + recipex) && slot.yDisplayPosition == (stack.rely + recipey)) {
                return slot;
            }
        }
        return null;
    }

    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void putStackInSlot(int par1, ItemStack par2ItemStack) {
        //Server side updates do nothing!
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        return null;//no shift clicking (scrolling...)
    }
}
