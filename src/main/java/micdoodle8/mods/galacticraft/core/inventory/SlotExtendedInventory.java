package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemThermal;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterOxygenInfinite;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenMask;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenTank;
import micdoodle8.mods.galacticraft.core.items.ItemParaChute;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockBasicVenus;
import micdoodle8.mods.galacticraft.planets.venus.items.ItemBasicVenus;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
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
        switch (this.getSlotIndex())
        {
        case 0:
            return itemstack.getItem() instanceof ItemOxygenMask;
        case 1:
            return itemstack.getItem() == GCItems.oxygenGear;
        case 2:
        case 3:
            return itemstack.getItem() instanceof ItemOxygenTank || itemstack.getItem() instanceof ItemCanisterOxygenInfinite;
        case 4:
            return itemstack.getItem() instanceof ItemParaChute;
        case 5:
            return itemstack.getItem() == GCItems.basicItem && itemstack.getItemDamage() == 19;
        case 6:
            return this.thermalArmorSlotValid(itemstack, 0);
        case 7:
            return this.thermalArmorSlotValid(itemstack, 1);
        case 8:
            return this.thermalArmorSlotValid(itemstack, 2);
        case 9:
            return this.thermalArmorSlotValid(itemstack, 3);
        case 10:
            return this.shieldControllerSlotValid(itemstack);
        }

        return super.isItemValid(itemstack);
    }

    public boolean thermalArmorSlotValid(ItemStack stack, int slotIndex)
    {
        if (stack.getItem() instanceof IItemThermal)
        {
            return ((IItemThermal) stack.getItem()).isValidForSlot(stack, slotIndex);
        }

        return false;
    }

    public boolean shieldControllerSlotValid(ItemStack stack)
    {
        if (GalacticraftCore.isPlanetsLoaded && stack.getItem() instanceof ItemBasicVenus && stack.getItemDamage() == 0)
        {
            return true;
        }

        return false;
    }
}
