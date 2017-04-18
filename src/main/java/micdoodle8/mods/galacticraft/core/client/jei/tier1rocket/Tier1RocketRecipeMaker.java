package micdoodle8.mods.galacticraft.core.client.jei.tier1rocket;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;

import java.util.ArrayList;
import java.util.List;

public class Tier1RocketRecipeMaker
{
    public static List<Tier1RocketRecipeWrapper> getRecipesList()
    {
        List<Tier1RocketRecipeWrapper> recipes = new ArrayList<>();

        for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getRocketT1Recipes())
        {
            Tier1RocketRecipeWrapper wrapper = new Tier1RocketRecipeWrapper(recipe);
            recipes.add(wrapper);
        }

        return recipes;
    }
}
