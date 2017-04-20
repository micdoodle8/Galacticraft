package micdoodle8.mods.galacticraft.planets.mars.client.jei.methanesynthesizer;

import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MethaneSynthesizerRecipeMaker
{
    public static List<MethaneSynthesizerRecipeWrapper> getRecipesList()
    {
        List<MethaneSynthesizerRecipeWrapper> recipes = new ArrayList<>();

        recipes.add(new MethaneSynthesizerRecipeWrapper(new ItemStack(AsteroidsItems.atmosphericValve), new ItemStack(AsteroidsItems.methaneCanister, 1, 1)));
        recipes.add(new MethaneSynthesizerRecipeWrapper(new ItemStack(MarsItems.carbonFragments, 25, 0), new ItemStack(AsteroidsItems.methaneCanister, 1, 1)));

        return recipes;
    }
}
