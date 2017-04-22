package micdoodle8.mods.galacticraft.planets.asteroids.client.jei.astrominer;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;

import java.util.ArrayList;
import java.util.List;

public class AstroMinerRecipeMaker
{
    public static List<AstroMinerRecipeWrapper> getRecipesList()
    {
        List<AstroMinerRecipeWrapper> recipes = new ArrayList<>();

        for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getAstroMinerRecipes())
        {
            AstroMinerRecipeWrapper wrapper = new AstroMinerRecipeWrapper(recipe);
            recipes.add(wrapper);
        }

        return recipes;
    }
}
