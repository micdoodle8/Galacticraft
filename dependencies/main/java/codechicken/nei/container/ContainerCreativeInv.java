package codechicken.nei.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCreativeInv extends Container {

    public static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET, EntityEquipmentSlot.OFFHAND };
    public static final String[] SLOT_TEXTURES = new String[] { "", "minecraft:items/empty_armor_slot_boots", "minecraft:items/empty_armor_slot_leggings", "minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_helmet", "minecraft:items/empty_armor_slot_shield" };

    private class SlotArmor extends Slot {
        EntityEquipmentSlot equipmentSlot;
        int stackLimit;

        public SlotArmor(IInventory inv, int slot, int x, int y, EntityEquipmentSlot armor, int stackLimit) {
            super(inv, slot, x, y);
            equipmentSlot = armor;
            this.stackLimit = stackLimit;
        }

        public SlotArmor(IInventory inv, int slot, int x, int y, EntityEquipmentSlot armor) {
            super(inv, slot, x, y);
            equipmentSlot = armor;
            stackLimit = 1;
        }

        @Override
        public int getSlotStackLimit() {
            return stackLimit;
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return equipmentSlot.equals(EntityEquipmentSlot.OFFHAND) || stack != null && stack.getItem().isValidArmor(stack, equipmentSlot, player);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public String getSlotTexture() {
            return SLOT_TEXTURES[equipmentSlot.getSlotIndex()];
        }
    }

    public EntityPlayer player;

    public ContainerCreativeInv(EntityPlayer player, ExtendedCreativeInv extraInv) {
        this.player = player;
        InventoryPlayer invPlayer = player.inventory;
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(extraInv, col + row * 9, 8 + col * 18, 5 + row * 18));
            }
        }

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                addSlotToContainer(new Slot(invPlayer, col + row * 9 + 9, 8 + col * 18, 118 + row * 18));
            }
        }

        for (int col = 0; col < 9; ++col) {
            addSlotToContainer(new Slot(invPlayer, col, 8 + col * 18, 176));
        }
        for (int i = 0; i < 4; i++) {
            EntityEquipmentSlot entityEquipmentSlot = VALID_EQUIPMENT_SLOTS[i];
            addSlotToContainer(new SlotArmor(invPlayer, 36 + (3 - i), -15, 23 + i * 18, entityEquipmentSlot));
        }
        addSlotToContainer(new SlotArmor(invPlayer, 40, -15, 23 + 4 * 18, VALID_EQUIPMENT_SLOTS[4], 64));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotIndex) {
        ItemStack transferredStack = null;
        Slot slot = inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            transferredStack = stack.copy();

            if (stack.getItem() instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor) stack.getItem();
                if (!getSlot(90 + armor.armorType.getIndex()).getHasStack()) {
                    getSlot(90 + armor.armorType.getIndex()).putStack(transferredStack);
                    slot.putStack(null);
                    return transferredStack;
                }
            }

            if (slotIndex < 54) {
                if (!this.mergeItemStack(stack, 54, 90, true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(stack, 0, 54, false)) {
                return null;
            }

            if (stack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }

        return transferredStack;
    }

    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
