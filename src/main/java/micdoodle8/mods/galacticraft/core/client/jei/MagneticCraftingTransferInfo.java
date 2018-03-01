package micdoodle8.mods.galacticraft.core.client.jei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.Slot;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCrafting;

public class MagneticCraftingTransferInfo implements IRecipeTransferInfo<ContainerCrafting>
{
    @Override
    public Class getContainerClass()
    {
        return ContainerCrafting.class;
    }

    @Override
    public String getRecipeCategoryUid()
    {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    // Crafting matrix inventory slots
    @Override
    public List<Slot> getRecipeSlots(ContainerCrafting container)
    {
        List<Slot> slots = new ArrayList<>();
        for (int i = 1; i < 10; i++)
        {
          slots.add(container.getSlot(i));
        }
        return slots;
    }

    // Player inventory slots
    @Override
    public List<Slot> getInventorySlots(ContainerCrafting container)
    {
        List<Slot> slots = new ArrayList<>();
        for (int i = 10; i < container.inventorySlots.size(); i++)
        {
          slots.add(container.getSlot(i));
        }
        return slots;
    }

    @Override
    public boolean canHandle(ContainerCrafting container)
    {
        return true;
    }
}
