package micdoodle8.mods.galacticraft.planets.asteroids.client.jei.tier3rocket;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeMaker;

import java.util.ArrayList;
import java.util.List;

public class Tier3RocketRecipeMaker
{
    public static List<INasaWorkbenchRecipe> getRecipesList()
    {
        List<INasaWorkbenchRecipe> recipes = new ArrayList<>();

        int chestCount = -1;
        for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getRocketT3Recipes())
        {
            int chests = Tier1RocketRecipeMaker.countChests(recipe); 
            if (chests == chestCount)
                continue;
            chestCount = chests;
            recipes.add(recipe);
        }

        return recipes;
    }
}
