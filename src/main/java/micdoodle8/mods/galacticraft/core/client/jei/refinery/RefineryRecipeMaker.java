package micdoodle8.mods.galacticraft.core.client.jei.refinery;

import micdoodle8.mods.galacticraft.core.GCItems;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RefineryRecipeMaker
{
    public static List<RefineryRecipeWrapper> getRecipesList()
    {
        List<RefineryRecipeWrapper> recipes = new ArrayList<>();

        recipes.add(new RefineryRecipeWrapper(new ItemStack(GCItems.oilCanister, 1, 1), new ItemStack(GCItems.fuelCanister, 1, 1)));
        recipes.add(new RefineryRecipeWrapper(new ItemStack(GCItems.bucketOil), new ItemStack(GCItems.fuelCanister, 1, 1)));
        recipes.add(new RefineryRecipeWrapper(new ItemStack(GCItems.oilCanister, 1, 1), new ItemStack(GCItems.bucketFuel)));
        recipes.add(new RefineryRecipeWrapper(new ItemStack(GCItems.bucketOil), new ItemStack(GCItems.bucketFuel)));

        return recipes;
    }
}
