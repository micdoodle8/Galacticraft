package micdoodle8.mods.galacticraft.planets.asteroids.client.jei.tier3rocket;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;

import java.util.ArrayList;
import java.util.List;

public class Tier3RocketRecipeMaker
{
    public static List<Tier3RocketRecipeWrapper> getRecipesList()
    {
        List<Tier3RocketRecipeWrapper> recipes = new ArrayList<>();

        for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getRocketT3Recipes())
        {
            Tier3RocketRecipeWrapper wrapper = new Tier3RocketRecipeWrapper(recipe);
            recipes.add(wrapper);
        }

        return recipes;
    }
}
