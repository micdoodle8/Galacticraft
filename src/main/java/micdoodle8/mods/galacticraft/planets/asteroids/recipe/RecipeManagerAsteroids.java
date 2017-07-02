package micdoodle8.mods.galacticraft.planets.asteroids.recipe;

import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachineTiered;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class RecipeManagerAsteroids
{
    public static void loadRecipes()
    {
        // Add compatibility stuffz here

        addUniversalRecipes();
    }

    private static void addUniversalRecipes()
    {
    	Object titaniumIngot = ConfigManagerCore.recipesRequireGCAdvancedMetals ? new ItemStack(AsteroidsItems.basicItem, 1, 0) : "ingotTitanium";
    	Object titaniumPlate = ConfigManagerCore.recipesRequireGCAdvancedMetals ? new ItemStack(AsteroidsItems.basicItem, 1, 6) : "compressedTitanium";
    	ItemStack platingTier3 = new ItemStack(AsteroidsItems.basicItem, 1, 5);
    	
    	FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(AsteroidBlocks.blockBasic, 1, 3), new ItemStack(GCItems.basicItem, 1, 5), 0.0F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(AsteroidBlocks.blockBasic, 1, 4), new ItemStack(AsteroidsItems.basicItem, 1, 0), 0.0F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(AsteroidBlocks.blockBasic, 1, 5), new ItemStack(Items.IRON_INGOT), 0.0F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 3), new ItemStack(Items.IRON_INGOT), 0.5F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 4), new ItemStack(AsteroidsItems.basicItem, 1, 0), 0.5F);

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.heavyNoseCone, 1), new Object[] { " Y ", " X ", "X X", 'X', platingTier3, 'Y', Blocks.REDSTONE_TORCH });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 7), new Object[] { " X ", "XYX", " X ", 'X', Blocks.WOOL, 'Y', Items.REDSTONE });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.thermalPadding, 1, 0), new Object[] { "XXX", "X X", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 7) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.thermalPadding, 1, 1), new Object[] { "X X", "XXX", "XXX", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 7) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.thermalPadding, 1, 2), new Object[] { "XXX", "X X", "X X", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 7) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.thermalPadding, 1, 3), new Object[] { "X X", "X X", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 7) });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumHelmet, 1), new Object[] { "XXX", "X X", 'X', titaniumPlate });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumChestplate, 1), new Object[] { "X X", "XXX", "XXX", 'X', titaniumPlate });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumLeggings, 1), new Object[] { "XXX", "X X", "X X", 'X', titaniumPlate });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumBoots, 1), new Object[] { "X X", "X X", 'X', titaniumPlate });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumPickaxe, 1), new Object[] { "YYY", " X ", " X ", 'Y', titaniumPlate, 'X', Items.IRON_INGOT });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumAxe, 1), new Object[] { "YY ", "YX ", " X ", 'Y', titaniumPlate, 'X', Items.IRON_INGOT });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumAxe, 1), new Object[] { " YY", " XY", " X ", 'Y', titaniumPlate, 'X', Items.IRON_INGOT });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumHoe, 1), new Object[] { " YY", " X ", " X ", 'Y', titaniumPlate, 'X', Items.IRON_INGOT });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumHoe, 1), new Object[] { "YY ", " X ", " X ", 'Y', titaniumPlate, 'X', Items.IRON_INGOT });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumSpade, 1), new Object[] { " Y ", " X ", " X ", 'Y', titaniumPlate, 'X', Items.IRON_INGOT });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumSword, 1), new Object[] { " Y ", " Y ", " X ", 'Y', titaniumPlate, 'X', Items.IRON_INGOT });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 1), new Object[] { " YV", "XWX", "XZX", 'V', Blocks.STONE_BUTTON, 'W', new ItemStack(GCItems.canister, 1, OreDictionary.WILDCARD_VALUE), 'X', platingTier3, 'Y', Items.FLINT_AND_STEEL, 'Z', GCItems.oxygenVent });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 1), new Object[] { "VY ", "XWX", "XZX", 'V', Blocks.STONE_BUTTON, 'W', new ItemStack(GCItems.canister, 1, OreDictionary.WILDCARD_VALUE), 'X', platingTier3, 'Y', Items.FLINT_AND_STEEL, 'Z', GCItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 2), new Object[] { " Y ", "XYX", "X X", 'X', platingTier3, 'Y', new ItemStack(MarsItems.marsItemBasic, 1, 3) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 2), new Object[] { " Y ", "XYX", "X X", 'X', platingTier3, 'Y', titaniumPlate });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.grapple, 1), new Object[] { "  Z", "XZ ", "XX ", 'X', Items.IRON_INGOT, 'Z', Items.STRING });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.atmosphericValve, 1, 0), new Object[] { "   ", "XYX", " X ", 'X', new ItemStack(MarsItems.marsItemBasic, 1, 2), 'Y', GCItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.blockWalkway, 5, 0), new Object[] { "XXX", " X ", 'X', titaniumPlate });
        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.blockWalkway, 5, 1), new Object[] { "XXX", "YXY", "YYY", 'X', titaniumPlate, 'Y', new ItemStack(GCBlocks.aluminumWire, 1, 1) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.blockWalkway, 5, 2), new Object[] { "XXX", "YXY", "YYY", 'X', titaniumPlate, 'Y', new ItemStack(GCBlocks.oxygenPipe) });
        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(AsteroidBlocks.blockWalkway, 1, 1), new ItemStack(AsteroidBlocks.blockWalkway, 1), new ItemStack(GCBlocks.aluminumWire, 1, 1));
        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(AsteroidBlocks.blockWalkway, 1, 2), new ItemStack(AsteroidBlocks.blockWalkway, 1), new ItemStack(GCBlocks.oxygenPipe, 1));

        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.shortRangeTelepad), new Object[] { "XWX", "ZYZ", "XXX", 'W', new ItemStack(AsteroidsItems.basicItem, 1, 8), 'X', titaniumPlate, 'Y', Items.REDSTONE, 'Z', Items.ENDER_PEARL });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 8), new Object[] { "XYX", "YZY", "XYX", 'X', Items.REDSTONE, 'Y', "compressedIron", 'Z', Blocks.GLASS_PANE });

        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.beamReceiver), new Object[] { " X ", "XYX", " X ", 'X', titaniumPlate, 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 8) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.beamReflector), new Object[] { " Y ", "ZX ", "XXX", 'X', titaniumPlate, 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 8), 'Z', Blocks.LEVER });
        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.beamReflector), new Object[] { " Y ", " XZ", "XXX", 'X', titaniumPlate, 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 8), 'Z', Blocks.LEVER });

        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.blockMinerBase, 4, 0), new Object[] { "XCX", "W W", "XBX", 'X', "compressedDesh", 'W', new ItemStack(AsteroidsItems.basicItem, 1, 8), 'C', new ItemStack(Blocks.CHEST), 'B', new ItemStack(GCBlocks.machineTiered, 1, BlockMachineTiered.STORAGE_MODULE_METADATA) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.orionDrive, 1, 0), new Object[] { "ABC", "DOE", "FGH", 'A', "oreDiamond", 'B', "oreLapis", 'C', "oreGold", 'D', "oreRedstone", 'E', "oreCoal", 'F', "oreCheese", 'G', "oreDesh", 'H', "oreIlmenite", 'O', new ItemStack(AsteroidsItems.basicItem, 1, 8) });

        //Cobblestone recipe
        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(Blocks.COBBLESTONE, 2), new ItemStack(AsteroidBlocks.blockBasic, 1, 0), new ItemStack(AsteroidBlocks.blockBasic, 1, 1));
        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(Blocks.COBBLESTONE, 2), new ItemStack(AsteroidBlocks.blockBasic, 1, 0), new ItemStack(AsteroidBlocks.blockBasic, 1, 2));
        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(Blocks.COBBLESTONE, 2), new ItemStack(AsteroidBlocks.blockBasic, 1, 1), new ItemStack(AsteroidBlocks.blockBasic, 1, 2));
        //Green dye from yellow and blue
        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(Items.DYE, 2, 2), new ItemStack(Items.DYE, 1, 11), new ItemStack(Items.DYE, 1, 4));

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
    	//Slimeball
    	RecipeUtil.addRecipe(new ItemStack(Items.SLIME_BALL), new Object [] { "XFX", "FEF", "XFX", 'X', new ItemStack(Items.DYE, 1, 2), 'E', new ItemStack(GCItems.cheeseCurd), 'F', new ItemStack(Items.SUGAR) } );
    	
    	if (ConfigManagerCore.recipesRequireGCAdvancedMetals)
    		CompressorRecipes.addShapelessRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 6), titaniumIngot, titaniumIngot);
    	else
    	{
	        if (OreDictionary.getOres("ingotTitanium").size() > 0)
	        {
	            for (ItemStack stack : OreDictionary.getOres("ingotTitanium"))
	            {
	                CompressorRecipes.addShapelessRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 6), stack, stack);
	            }
	        }
    	}

        CompressorRecipes.addShapelessRecipe(platingTier3, new ItemStack(MarsItems.marsItemBasic, 1, 3), new ItemStack(MarsItems.marsItemBasic, 1, 5));

        //All this is for NEI's benefit
        List<ItemStack> list1 = new ArrayList();
        List<ItemStack> list2 = new ArrayList();
        List<ItemStack> list3 = new ArrayList();
        list1.add(new ItemStack(AsteroidsItems.canisterLOX, 1, 751));
        list2.add(new ItemStack(AsteroidsItems.canisterLOX, 1, 834));
        list3.add(new ItemStack(AsteroidsItems.canisterLOX, 1, 918));
        list1.add(new ItemStack(GCItems.oxTankHeavy, 1, 2700));
        list2.add(new ItemStack(GCItems.oxTankMedium, 1, 1800));
        list3.add(new ItemStack(GCItems.oxTankLight, 1, 900));
        CraftingManager.getInstance().getRecipeList().add(new CanisterRecipes(new ItemStack(GCItems.oxTankHeavy, 1, 0), list1));
        CraftingManager.getInstance().getRecipeList().add(new CanisterRecipes(new ItemStack(GCItems.oxTankMedium, 1, 0), list2));
        CraftingManager.getInstance().getRecipeList().add(new CanisterRecipes(new ItemStack(GCItems.oxTankLight, 1, 0), list3));
    }
}
