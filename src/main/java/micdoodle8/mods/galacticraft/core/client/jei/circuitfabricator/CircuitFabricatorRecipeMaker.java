package micdoodle8.mods.galacticraft.core.client.jei.circuitfabricator;

import com.google.common.collect.ImmutableMap;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.CircuitFabricatorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CircuitFabricatorRecipeMaker
{
    public static List<CircuitFabricatorRecipeWrapper> getRecipesList()
    {
        List<CircuitFabricatorRecipeWrapper> recipes = new ArrayList<>();

        ImmutableMap<ItemStack[], ItemStack> recipesMap = CircuitFabricatorRecipes.getRecipes();
        for (Map.Entry<ItemStack[], ItemStack> entry : recipesMap.entrySet())
        {
            CircuitFabricatorRecipeWrapper wrapper = new CircuitFabricatorRecipeWrapper(entry.getKey(), entry.getValue());
            recipes.add(wrapper);
        }

        return recipes;
    }
}
