package micdoodle8.mods.galacticraft.planets.mars.client.jei.gasliquefier;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GasLiquefierRecipeMaker
{
    public static List<GasLiquefierRecipeWrapper> getRecipesList()
    {
        List<GasLiquefierRecipeWrapper> recipes = new ArrayList<>();

        recipes.add(new GasLiquefierRecipeWrapper(new ItemStack(AsteroidsItems.methaneCanister, 1, 1), new ItemStack(GCItems.fuelCanister, 1, 1)));
        recipes.add(new GasLiquefierRecipeWrapper(new ItemStack(AsteroidsItems.atmosphericValve), new ItemStack(AsteroidsItems.canisterLN2, 1, 1)));
        recipes.add(new GasLiquefierRecipeWrapper(new ItemStack(AsteroidsItems.atmosphericValve), new ItemStack(AsteroidsItems.canisterLOX, 1, 1)));
        recipes.add(new GasLiquefierRecipeWrapper(new ItemStack(AsteroidsItems.canisterLN2, 1, 501), new ItemStack(AsteroidsItems.canisterLN2, 1, 1)));
        recipes.add(new GasLiquefierRecipeWrapper(new ItemStack(AsteroidsItems.canisterLOX, 1, 501), new ItemStack(AsteroidsItems.canisterLOX, 1, 1)));

        return recipes;
    }
}
