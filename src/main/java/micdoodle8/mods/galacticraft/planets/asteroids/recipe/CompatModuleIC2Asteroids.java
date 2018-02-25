package micdoodle8.mods.galacticraft.planets.asteroids.recipe;

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
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 9), new Object[] { "XXX", "XXX", "XXX", 'X', new ItemStack(GCItems.ic2compat, 1, 7) });
        Recipes.compressor.addRecipe(Recipes.inputFactory.forStack(new ItemStack(GCItems.ic2compat, 1, 7), 9), null, false, new ItemStack(AsteroidsItems.basicItem, 1, 9));
        Recipes.macerator.addRecipe(Recipes.inputFactory.forStack(titaniumIngot, 1), null, false, new ItemStack(AsteroidsItems.basicItem, 1, 9));
        Recipes.macerator.addRecipe(Recipes.inputFactory.forStack(new ItemStack(AsteroidsItems.basicItem, 1, 4), 1), null, false, new ItemStack(AsteroidsItems.basicItem, 1, 9));
        Recipes.macerator.addRecipe(Recipes.inputFactory.forStack(new ItemStack(AsteroidsItems.basicItem, 1, 6), 1), null, false, new ItemStack(AsteroidsItems.basicItem, 1, 9));
        // Asteroids and Venus versions of aluminium ore (because IC2 oredicting or .ini will not pick up aluminium ores)
        Recipes.macerator.addRecipe(Recipes.inputFactory.forStack(new ItemStack(AsteroidBlocks.blockBasic, 1, 3), 1), null, false, new ItemStack(GCItems.ic2compat, 2, 2));
        Recipes.macerator.addRecipe(Recipes.inputFactory.forStack(new ItemStack(VenusBlocks.venusBlock, 1, 6), 1), null, false, new ItemStack(GCItems.ic2compat, 2, 2));
    }
}
