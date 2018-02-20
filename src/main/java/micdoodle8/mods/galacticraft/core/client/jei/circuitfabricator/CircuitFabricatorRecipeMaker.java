package micdoodle8.mods.galacticraft.core.client.jei.circuitfabricator;

import micdoodle8.mods.galacticraft.api.recipe.CircuitFabricatorRecipes;
import java.util.ArrayList;
import java.util.List;

public class CircuitFabricatorRecipeMaker
{
    public static List<CircuitFabricatorRecipeWrapper> getRecipesList()
    {
        List<CircuitFabricatorRecipeWrapper> recipes = new ArrayList<>();

        int count = 0;
        for (List<Object> entry : CircuitFabricatorRecipes.getRecipes())
        {
            CircuitFabricatorRecipeWrapper wrapper = new CircuitFabricatorRecipeWrapper(entry, CircuitFabricatorRecipes.getOutput(count));
            recipes.add(wrapper);
            count++;
        }

        return recipes;
    }
}
