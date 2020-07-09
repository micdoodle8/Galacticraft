package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotArmorGC extends Slot
{
    private static final String[] ARMOR_SLOT_TEXTURES = new String[]{"item/empty_armor_slot_boots", "item/empty_armor_slot_leggings", "item/empty_armor_slot_chestplate", "item/empty_armor_slot_helmet"};
    private static final EquipmentSlotType[] VALID_EQUIPMENT_SLOTS = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
    final int armorType;
    final PlayerEntity thePlayer;

    public SlotArmorGC(PlayerEntity thePlayer, IInventory par2IInventory, int par3, int par4, int par5, int par6)
    {
        super(par2IInventory, par3, par4, par5);
        this.thePlayer = thePlayer;
        this.armorType = par6;
    }

    @Override
    public int getSlotStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack.canEquip(VALID_EQUIPMENT_SLOTS[this.armorType], thePlayer);
//        final Item item = par1ItemStack.isEmpty() ? null : par1ItemStack.getItem();
//        return item != null && item.isValidArmor(par1ItemStack, VALID_EQUIPMENT_SLOTS[this.armorType], this.thePlayer);
    }

    @Override
    public String getSlotTexture()
    {
        return ARMOR_SLOT_TEXTURES[VALID_EQUIPMENT_SLOTS[this.armorType].getIndex()];
    }
}
