package micdoodle8.mods.galacticraft.core.client.jei.oxygencompressor;

import micdoodle8.mods.galacticraft.core.GCItems;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class OxygenCompressorRecipeMaker
{
    public static List<OxygenCompressorRecipeWrapper> getRecipesList()
    {
        List<OxygenCompressorRecipeWrapper> recipes = new ArrayList<>();

        recipes.add(new OxygenCompressorRecipeWrapper(new ItemStack(GCItems.oxTankLight)));
        recipes.add(new OxygenCompressorRecipeWrapper(new ItemStack(GCItems.oxTankMedium)));
        recipes.add(new OxygenCompressorRecipeWrapper(new ItemStack(GCItems.oxTankHeavy)));

        return recipes;
    }
}
