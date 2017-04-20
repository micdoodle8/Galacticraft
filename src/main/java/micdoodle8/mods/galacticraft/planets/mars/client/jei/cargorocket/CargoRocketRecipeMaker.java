package micdoodle8.mods.galacticraft.planets.mars.client.jei.cargorocket;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;

import java.util.ArrayList;
import java.util.List;

public class CargoRocketRecipeMaker
{
    public static List<CargoRocketRecipeWrapper> getRecipesList()
    {
        List<CargoRocketRecipeWrapper> recipes = new ArrayList<>();

        for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getCargoRocketRecipes())
        {
            CargoRocketRecipeWrapper wrapper = new CargoRocketRecipeWrapper(recipe);
            recipes.add(wrapper);
        }

        return recipes;
    }
}
