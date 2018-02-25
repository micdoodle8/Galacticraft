package micdoodle8.mods.galacticraft.planets.asteroids.recipe;

import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import net.minecraft.item.ItemStack;

public class CompatModuleIC2Asteroids
{
    public static void addIC2Recipes(ItemStack titaniumIngot, ItemStack titaniumDust)
    {
        // Titanium dust from small dust
        RecipeUtil.addRecipe(titaniumDust, new Object[] { "XXX", "XXX", "XXX", 'X', new ItemStack(GCItems.ic2compat, 1, 7) });
        Recipes.compressor.addRecipe(new RecipeInputItemStack(new ItemStack(GCItems.ic2compat, 1, 7), 9), null, false, titaniumDust);
        Recipes.macerator.addRecipe(new RecipeInputItemStack(titaniumIngot, 1), null, false, titaniumDust);
        Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(AsteroidsItems.basicItem, 1, 4), 1), null, false, titaniumDust);
        Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(AsteroidsItems.basicItem, 1, 6), 1), null, false, titaniumDust);
        // Asteroids and Venus versions of aluminium ore (because IC2 oredicting or .ini will not pick up aluminium ores)
        Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(AsteroidBlocks.blockBasic, 1, 3), 1), null, false, new ItemStack(GCItems.ic2compat, 2, 2));
        Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(VenusBlocks.venusBlock, 1, 6), 1), null, false, new ItemStack(GCItems.ic2compat, 2, 2));
    }
}
