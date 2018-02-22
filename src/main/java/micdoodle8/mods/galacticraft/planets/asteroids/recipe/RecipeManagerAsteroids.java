package micdoodle8.mods.galacticraft.planets.asteroids.recipe;

import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import ic2.api.recipe.Recipes;

public class RecipeManagerAsteroids
{
    public static void loadRecipes()
    {
        // Add compatibility stuffz here

        addUniversalRecipes();
    }

    private static void addUniversalRecipes()
    {
        ItemStack titaniumIngot = new ItemStack(AsteroidsItems.basicItem, 1, 0);
    	ItemStack platingTier3 = new ItemStack(AsteroidsItems.basicItem, 1, 5);
    	
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(AsteroidBlocks.blockBasic, 1, 3), new ItemStack(GCItems.basicItem, 1, 5), 0.5F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(AsteroidBlocks.blockBasic, 1, 4), new ItemStack(AsteroidsItems.basicItem, 1, 0), 0.5F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(AsteroidBlocks.blockBasic, 1, 5), new ItemStack(Items.IRON_INGOT), 0.5F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 3), new ItemStack(Items.IRON_INGOT), 0.5F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 4), new ItemStack(AsteroidsItems.basicItem, 1, 0), 0.5F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 9), new ItemStack(AsteroidsItems.basicItem, 1, 0), 0.5F);

        //Cobblestone->Gravel, Gravel->Sand, Sand->Clay
        CompressorRecipes.addRecipeAdventure(new ItemStack(Blocks.GRAVEL, 9, 0), "XXX", "XXX", "XXX", 'X', new ItemStack(Blocks.COBBLESTONE, 1));
        CompressorRecipes.addRecipeAdventure(new ItemStack(Blocks.SAND, 9, 0), "XXX", "XXX", "XXX", 'X', new ItemStack(Blocks.GRAVEL, 1));
        CompressorRecipes.addRecipeAdventure(new ItemStack(Blocks.CLAY, 8, 0), "XXX", "XBX", "XXX", 'X', new ItemStack(Blocks.SAND), 'B', new ItemStack(Items.WATER_BUCKET));
        CompressorRecipes.addRecipeAdventure(new ItemStack(Blocks.SOUL_SAND, 4, 0), "XFX", "FEF", "XFX", 'X', new ItemStack(Blocks.SAND), 'F', new ItemStack(Items.ROTTEN_FLESH), 'E', new ItemStack(Items.FERMENTED_SPIDER_EYE));
        CompressorRecipes.addRecipeAdventure(new ItemStack(Blocks.OBSIDIAN, 1, 0), "XXX", "XBX", "XXX", 'X', new ItemStack(Blocks.STONE), 'B', new ItemStack(Items.BLAZE_POWDER));
        //Charcoal into coal
    	CompressorRecipes.addShapelessAdventure(new ItemStack(Items.COAL, 2, 0), new ItemStack(Items.COAL, 1, 1), new ItemStack(AsteroidBlocks.blockBasic, 1, 0), new ItemStack(Items.COAL, 1, 1));
    	CompressorRecipes.addShapelessAdventure(new ItemStack(Items.COAL, 2, 0), new ItemStack(Items.COAL, 1, 1), new ItemStack(AsteroidBlocks.blockBasic, 1, 1), new ItemStack(Items.COAL, 1, 1));
    	CompressorRecipes.addShapelessAdventure(new ItemStack(Items.COAL, 2, 0), new ItemStack(Items.COAL, 1, 1), new ItemStack(AsteroidBlocks.blockBasic, 1, 2), new ItemStack(Items.COAL, 1, 1));
    	//Splintered ice into Ice
    	CompressorRecipes.addShapelessRecipe(new ItemStack(Blocks.ICE), new ItemStack(AsteroidBlocks.blockDenseIce), new ItemStack(AsteroidBlocks.blockDenseIce));
    	
    	CompressorRecipes.addShapelessRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 6), titaniumIngot, titaniumIngot);
    	CompressorRecipes.addShapelessRecipe(platingTier3, new ItemStack(MarsItems.marsItemBasic, 1, 3), new ItemStack(MarsItems.marsItemBasic, 1, 5));

        //All this is for NEI's benefit
        NonNullList<Ingredient> list1 = NonNullList.create();
        NonNullList<Ingredient> list2 = NonNullList.create();
        NonNullList<Ingredient> list3 = NonNullList.create();
        list1.add(Ingredient.fromStacks(new ItemStack(AsteroidsItems.canisterLOX, 1, 751)));
        list2.add(Ingredient.fromStacks(new ItemStack(AsteroidsItems.canisterLOX, 1, 834)));
        list3.add(Ingredient.fromStacks(new ItemStack(AsteroidsItems.canisterLOX, 1, 918)));
        list1.add(Ingredient.fromStacks(new ItemStack(GCItems.oxTankHeavy, 1, 2700)));
        list2.add(Ingredient.fromStacks(new ItemStack(GCItems.oxTankMedium, 1, 1800)));
        list3.add(Ingredient.fromStacks(new ItemStack(GCItems.oxTankLight, 1, 900)));
        RecipeUtil.addCustomRecipe(new CanisterRecipes(new ItemStack(GCItems.oxTankHeavy, 1, 0), list1));
        RecipeUtil.addCustomRecipe(new CanisterRecipes(new ItemStack(GCItems.oxTankMedium, 1, 0), list2));
        RecipeUtil.addCustomRecipe(new CanisterRecipes(new ItemStack(GCItems.oxTankLight, 1, 0), list3));
        
        if (CompatibilityManager.isIc2Loaded())
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
}
