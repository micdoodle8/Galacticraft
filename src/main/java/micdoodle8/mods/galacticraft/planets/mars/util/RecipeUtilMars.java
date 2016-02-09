package micdoodle8.mods.galacticraft.planets.mars.util;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.planets.asteroids.inventory.InventorySchematicAstroMiner;
import micdoodle8.mods.galacticraft.planets.asteroids.inventory.InventorySchematicTier3Rocket;
import micdoodle8.mods.galacticraft.planets.mars.inventory.InventorySchematicCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.inventory.InventorySchematicTier2Rocket;
import net.minecraft.item.ItemStack;

public class RecipeUtilMars
{
    public static ItemStack findMatchingSpaceshipT2Recipe(InventorySchematicTier2Rocket inventoryRocketBench)
    {
        for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getRocketT2Recipes())
        {
            if (recipe.matches(inventoryRocketBench))
            {
                return recipe.getRecipeOutput();
            }
        }

        return null;
    }

    public static ItemStack findMatchingCargoRocketRecipe(InventorySchematicCargoRocket inventoryRocketBench)
    {
        for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getCargoRocketRecipes())
        {
            if (recipe.matches(inventoryRocketBench))
            {
                return recipe.getRecipeOutput();
            }
        }

        return null;
    }

    public static ItemStack findMatchingSpaceshipT3Recipe(InventorySchematicTier3Rocket inventoryRocketBench)
    {
        for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getRocketT3Recipes())
        {
            if (recipe.matches(inventoryRocketBench))
            {
                return recipe.getRecipeOutput();
            }
        }

        return null;
    }

	public static ItemStack findMatchingAstroMinerRecipe (InventorySchematicAstroMiner craftMatrix)
	{
        for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getAstroMinerRecipes())
        {
            if (recipe.matches(craftMatrix))
            {
                return recipe.getRecipeOutput();
            }
        }

        return null;
	}
}
