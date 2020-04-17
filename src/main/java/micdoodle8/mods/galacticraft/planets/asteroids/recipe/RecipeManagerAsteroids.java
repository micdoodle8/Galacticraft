package micdoodle8.mods.galacticraft.planets.asteroids.recipe;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.recipe.NasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;

public class RecipeManagerAsteroids
{
    public static void addUniversalRecipes()
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

        HashMap<Integer, ItemStack> input = new HashMap<>();
        ItemStack plateTier3 = new ItemStack(AsteroidsItems.basicItem, 1, 5);
        ItemStack rocketFinsTier2 = new ItemStack(AsteroidsItems.basicItem, 1, 2);
        input.put(1, new ItemStack(AsteroidsItems.heavyNoseCone));
        input.put(2, plateTier3);
        input.put(3, plateTier3);
        input.put(4, plateTier3);
        input.put(5, plateTier3);
        input.put(6, plateTier3);
        input.put(7, plateTier3);
        input.put(8, plateTier3);
        input.put(9, plateTier3);
        input.put(10, plateTier3);
        input.put(11, plateTier3);
        input.put(12, new ItemStack(GCItems.rocketEngine, 1, 1));
        input.put(13, rocketFinsTier2);
        input.put(14, rocketFinsTier2);
        input.put(15, new ItemStack(AsteroidsItems.basicItem, 1, 1));
        input.put(16, new ItemStack(GCItems.rocketEngine, 1, 1));
        input.put(17, rocketFinsTier2);
        input.put(18, rocketFinsTier2);
        input.put(19, ItemStack.EMPTY);
        input.put(20, ItemStack.EMPTY);
        input.put(21, ItemStack.EMPTY);
        GalacticraftRegistry.addT3RocketRecipe(new NasaWorkbenchRecipe(new ItemStack(AsteroidsItems.tier3Rocket, 1, 0), input));

        NonNullList<ItemStack> woodChests = OreDictionary.getOres("chestWood");
        HashMap<Integer, ItemStack> input2;

        for (ItemStack woodChest : woodChests)
        {
            input2 = new HashMap<Integer, ItemStack>(input);
            input2.put(19, woodChest);
            input2.put(20, ItemStack.EMPTY);
            input2.put(21, ItemStack.EMPTY);
            GalacticraftRegistry.addT3RocketRecipe(new NasaWorkbenchRecipe(new ItemStack(AsteroidsItems.tier3Rocket, 1, 1), input2));

            input2 = new HashMap<Integer, ItemStack>(input);
            input2.put(19, ItemStack.EMPTY);
            input2.put(20, woodChest);
            input2.put(21, ItemStack.EMPTY);
            GalacticraftRegistry.addT3RocketRecipe(new NasaWorkbenchRecipe(new ItemStack(AsteroidsItems.tier3Rocket, 1, 1), input2));

            input2 = new HashMap<Integer, ItemStack>(input);
            input2.put(19, ItemStack.EMPTY);
            input2.put(20, ItemStack.EMPTY);
            input2.put(21, woodChest);
            GalacticraftRegistry.addT3RocketRecipe(new NasaWorkbenchRecipe(new ItemStack(AsteroidsItems.tier3Rocket, 1, 1), input2));

            input2 = new HashMap<Integer, ItemStack>(input);
            input2.put(19, woodChest);
            input2.put(20, woodChest);
            input2.put(21, ItemStack.EMPTY);
            GalacticraftRegistry.addT3RocketRecipe(new NasaWorkbenchRecipe(new ItemStack(AsteroidsItems.tier3Rocket, 1, 2), input2));

            input2 = new HashMap<Integer, ItemStack>(input);
            input2.put(19, woodChest);
            input2.put(20, ItemStack.EMPTY);
            input2.put(21, woodChest);
            GalacticraftRegistry.addT3RocketRecipe(new NasaWorkbenchRecipe(new ItemStack(AsteroidsItems.tier3Rocket, 1, 2), input2));

            input2 = new HashMap<Integer, ItemStack>(input);
            input2.put(19, ItemStack.EMPTY);
            input2.put(20, woodChest);
            input2.put(21, woodChest);
            GalacticraftRegistry.addT3RocketRecipe(new NasaWorkbenchRecipe(new ItemStack(AsteroidsItems.tier3Rocket, 1, 2), input2));

            input2 = new HashMap<Integer, ItemStack>(input);
            input2.put(19, woodChest);
            input2.put(20, woodChest);
            input2.put(21, woodChest);
            GalacticraftRegistry.addT3RocketRecipe(new NasaWorkbenchRecipe(new ItemStack(AsteroidsItems.tier3Rocket, 1, 3), input2));
        }

        for (ItemStack woodChest : woodChests)
        {
            input = new HashMap<Integer, ItemStack>();
            input.put(1, new ItemStack(GCItems.heavyPlatingTier1));
            input.put(3, new ItemStack(GCItems.heavyPlatingTier1));
            input.put(5, new ItemStack(GCItems.heavyPlatingTier1));
            input.put(11, new ItemStack(GCItems.heavyPlatingTier1));
            input.put(2, new ItemStack(AsteroidsItems.orionDrive));
            input.put(4, new ItemStack(AsteroidsItems.orionDrive));
            input.put(9, new ItemStack(AsteroidsItems.orionDrive));
            input.put(10, new ItemStack(AsteroidsItems.orionDrive));
            input.put(12, new ItemStack(AsteroidsItems.orionDrive));
            input.put(6, new ItemStack(GCItems.basicItem, 1, 14));
            input.put(7, woodChest);
            input.put(8, woodChest);
            input.put(13, new ItemStack(AsteroidsItems.basicItem, 1, 8));
            input.put(14, new ItemStack(GCItems.flagPole));
            GalacticraftRegistry.addAstroMinerRecipe(new NasaWorkbenchRecipe(new ItemStack(AsteroidsItems.astroMiner, 1, 0), input));
        }

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
    }
    
    public static void loadCompatibilityRecipes()
    {
        ItemStack titaniumIngot = new ItemStack(AsteroidsItems.basicItem, 1, 0);
        if (CompatibilityManager.isIc2Loaded())
        {
            CompatModuleIC2Asteroids.addIC2Recipes(titaniumIngot, new ItemStack(AsteroidsItems.basicItem, 1, 9));
        }
    }
}
