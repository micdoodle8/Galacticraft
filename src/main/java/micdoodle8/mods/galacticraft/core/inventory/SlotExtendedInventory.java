package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.item.EnumExtendedInventorySlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotExtendedInventory extends Slot
{
    public SlotExtendedInventory(IInventory par2IInventory, int par3, int par4, int par5)
    {
        super(par2IInventory, par3, par4, par5);
    }

    @Override
    public int getSlotStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack)
    {
        int gearID = GalacticraftRegistry.findMatchingGearID(itemstack, getTypeFromSlot());

        return gearID >= 0;
    }

    private EnumExtendedInventorySlot getTypeFromSlot()
    {
        switch (this.getSlotIndex())
        {
        case 0:
            return EnumExtendedInventorySlot.MASK;
        case 1:
            return EnumExtendedInventorySlot.GEAR;
        case 2:
            return EnumExtendedInventorySlot.LEFT_TANK;
        case 3:
            return EnumExtendedInventorySlot.RIGHT_TANK;
        case 4:
            return EnumExtendedInventorySlot.PARACHUTE;
        case 5:
            return EnumExtendedInventorySlot.FREQUENCY_MODULE;
        case 6:
            return EnumExtendedInventorySlot.THERMAL_HELMET;
        case 7:
            return EnumExtendedInventorySlot.THERMAL_CHESTPLATE;
        case 8:
            return EnumExtendedInventorySlot.THERMAL_LEGGINGS;
        case 9:
            return EnumExtendedInventorySlot.THERMAL_BOOTS;
        case 10:
            return EnumExtendedInventorySlot.SHIELD_CONTROLLER;
        }

        return null;
    }
}
