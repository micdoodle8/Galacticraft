package micdoodle8.mods.galacticraft.mars.recipe;

import java.util.HashMap;

import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlockMachine;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import micdoodle8.mods.galacticraft.mars.util.GCMarsUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

/**
 * GCMarsRecipeManager.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsRecipeManager
{
	public static void loadRecipes()
	{
		GCMarsRecipeManager.addUniversalRecipes();
	}

	private static void addUniversalRecipes()
	{
		OreDictionary.registerOre("ingotDesh", new ItemStack(GCMarsItems.marsItemBasic, 1, 2));
		OreDictionary.registerOre("plateDesh", new ItemStack(GCMarsItems.marsItemBasic, 1, 5));
		OreDictionary.registerOre("plateHeavyT2", new ItemStack(GCMarsItems.marsItemBasic, 1, 3));

		RecipeUtil.addRecipe(new ItemStack(GCMarsItems.deshBoots), new Object[] { "X X", "X X", 'X', new ItemStack(GCMarsItems.marsItemBasic, 1, 2) });

		RecipeUtil.addRecipe(new ItemStack(GCMarsBlocks.marsBlock, 1, 8), new Object[] { "XXX", "XXX", "XXX", 'X', "ingotDesh" });

		RecipeUtil.addRecipe(new ItemStack(GCMarsBlocks.machine, 1, 0), new Object[] { "XWX", "XZX", "WVW", 'V', GCItems.oxygenConcentrator, 'W', "plateDesh", 'X', "ingotDesh", 'Z', new ItemStack(GCItems.canister) });

		RecipeUtil.addRecipe(new ItemStack(GCMarsBlocks.machine, 1, 4), new Object[] { "XYX", "XZX", "XYX", 'X', "plateDesh", 'Y', "plateHeavyT2", 'Z', Items.bed });

		RecipeUtil.addRecipe(new ItemStack(GCMarsItems.marsItemBasic, 1, 1), new Object[] { "X", "X", 'X', "ingotDesh" });

		RecipeUtil.addRecipe(new ItemStack(GCMarsItems.marsItemBasic, 1, 4), new Object[] { "XWX", "XYX", " Z ", 'W', Items.diamond, 'X', Items.leather, 'Y', Items.slime_ball, 'Z', Blocks.chest });

		RecipeUtil.addRecipe(new ItemStack(GCMarsItems.deshSword), new Object[] { "X", "X", "Y", 'X', new ItemStack(GCMarsItems.marsItemBasic, 1, 2), 'Y', new ItemStack(GCMarsItems.marsItemBasic, 1, 1) });

		RecipeUtil.addRecipe(new ItemStack(GCMarsItems.deshPickaxe), new Object[] { "XXX", " Y ", " Y ", 'X', new ItemStack(GCMarsItems.marsItemBasic, 1, 2), 'Y', new ItemStack(GCMarsItems.marsItemBasic, 1, 1) });

		RecipeUtil.addRecipe(new ItemStack(GCMarsItems.deshSpade), new Object[] { "X", "Y", "Y", 'X', new ItemStack(GCMarsItems.marsItemBasic, 1, 2), 'Y', new ItemStack(GCMarsItems.marsItemBasic, 1, 1) });

		RecipeUtil.addRecipe(new ItemStack(GCMarsItems.deshHoe), new Object[] { "XX", "Y ", "Y ", 'X', new ItemStack(GCMarsItems.marsItemBasic, 1, 2), 'Y', new ItemStack(GCMarsItems.marsItemBasic, 1, 1) });

		RecipeUtil.addRecipe(new ItemStack(GCMarsItems.deshHoe), new Object[] { "XX", " Y", " Y", 'X', new ItemStack(GCMarsItems.marsItemBasic, 1, 2), 'Y', new ItemStack(GCMarsItems.marsItemBasic, 1, 1) });

		RecipeUtil.addRecipe(new ItemStack(GCMarsItems.deshAxe), new Object[] { "XX", "XY", " Y", 'X', new ItemStack(GCMarsItems.marsItemBasic, 1, 2), 'Y', new ItemStack(GCMarsItems.marsItemBasic, 1, 1) });

		RecipeUtil.addRecipe(new ItemStack(GCMarsItems.deshAxe), new Object[] { "XX", "YX", "Y ", 'X', new ItemStack(GCMarsItems.marsItemBasic, 1, 2), 'Y', new ItemStack(GCMarsItems.marsItemBasic, 1, 1) });

		RecipeUtil.addRecipe(new ItemStack(GCMarsItems.deshHelmet), new Object[] { "XXX", "X X", 'X', new ItemStack(GCMarsItems.marsItemBasic, 1, 2) });

		RecipeUtil.addRecipe(new ItemStack(GCMarsItems.deshChestplate), new Object[] { "X X", "XXX", "XXX", 'X', new ItemStack(GCMarsItems.marsItemBasic, 1, 2) });

		RecipeUtil.addRecipe(new ItemStack(GCMarsItems.deshLeggings), new Object[] { "XXX", "X X", "X X", 'X', new ItemStack(GCMarsItems.marsItemBasic, 1, 2) });

		RecipeUtil.addRecipe(new ItemStack(GCMarsItems.marsItemBasic, 9, 2), new Object[] { "X", 'X', new ItemStack(GCMarsBlocks.marsBlock, 1, 8) });

		for (int var2 = 0; var2 < 16; ++var2)
		{
			CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCMarsBlocks.tintedGlassPane, 1, 15 - var2), new Object[] { new ItemStack(Items.dye, 1, var2), Blocks.glass_pane, OreDictionary.getOres("plateDesh").get(0) });
		}

		// Smelting
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCMarsItems.marsItemBasic, 1, 0), new ItemStack(GCMarsItems.marsItemBasic, 1, 2), 0.2F);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCMarsBlocks.marsBlock, 1, 4), new ItemStack(GCMarsBlocks.marsBlock, 1, 9), 0.0F);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCMarsBlocks.marsBlock, 1, 0), OreDictionary.getOres("ingotCopper").get(0), 1.0F);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCMarsBlocks.marsBlock, 1, 1), OreDictionary.getOres("ingotTin").get(0), 1.0F);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCMarsBlocks.marsBlock, 1, 2), new ItemStack(GCMarsItems.marsItemBasic, 1, 2), 0.2F);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCMarsBlocks.marsBlock, 1, 3), new ItemStack(Items.iron_ingot), 0.2F);

		// Schematic
		HashMap<Integer, ItemStack> input = new HashMap<Integer, ItemStack>();
		input.put(1, new ItemStack(GCItems.partNoseCone));
		input.put(2, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
		input.put(3, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
		input.put(4, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
		input.put(5, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
		input.put(6, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
		input.put(7, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
		input.put(8, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
		input.put(9, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
		input.put(10, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
		input.put(11, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
		input.put(12, new ItemStack(GCItems.rocketEngine, 1, 1));
		input.put(13, new ItemStack(GCItems.partFins));
		input.put(14, new ItemStack(GCItems.partFins));
		input.put(15, new ItemStack(GCItems.rocketEngine));
		input.put(16, new ItemStack(GCItems.rocketEngine, 1, 1));
		input.put(17, new ItemStack(GCItems.partFins));
		input.put(18, new ItemStack(GCItems.partFins));
		input.put(19, null);
		input.put(20, null);
		input.put(21, null);
		GCMarsUtil.addRocketBenchT2Recipe(new ItemStack(GCMarsItems.spaceship, 1, 0), input);

		HashMap<Integer, ItemStack> input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(19, new ItemStack(Blocks.chest));
		input2.put(20, null);
		input2.put(21, null);
		GCMarsUtil.addRocketBenchT2Recipe(new ItemStack(GCMarsItems.spaceship, 1, 1), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(19, null);
		input2.put(20, new ItemStack(Blocks.chest));
		input2.put(21, null);
		GCMarsUtil.addRocketBenchT2Recipe(new ItemStack(GCMarsItems.spaceship, 1, 1), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(19, null);
		input2.put(20, null);
		input2.put(21, new ItemStack(Blocks.chest));
		GCMarsUtil.addRocketBenchT2Recipe(new ItemStack(GCMarsItems.spaceship, 1, 1), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(19, new ItemStack(Blocks.chest));
		input2.put(20, new ItemStack(Blocks.chest));
		input2.put(21, null);
		GCMarsUtil.addRocketBenchT2Recipe(new ItemStack(GCMarsItems.spaceship, 1, 2), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(19, new ItemStack(Blocks.chest));
		input2.put(20, null);
		input2.put(21, new ItemStack(Blocks.chest));
		GCMarsUtil.addRocketBenchT2Recipe(new ItemStack(GCMarsItems.spaceship, 1, 2), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(19, null);
		input2.put(20, new ItemStack(Blocks.chest));
		input2.put(21, new ItemStack(Blocks.chest));
		GCMarsUtil.addRocketBenchT2Recipe(new ItemStack(GCMarsItems.spaceship, 1, 2), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(19, new ItemStack(Blocks.chest));
		input2.put(20, new ItemStack(Blocks.chest));
		input2.put(21, new ItemStack(Blocks.chest));
		GCMarsUtil.addRocketBenchT2Recipe(new ItemStack(GCMarsItems.spaceship, 1, 3), input2);

		//

		input = new HashMap<Integer, ItemStack>();
		input.put(1, new ItemStack(GCItems.partNoseCone));
		input.put(2, new ItemStack(GCItems.basicItem, 1, 14));
		input.put(3, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
		input.put(4, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
		input.put(5, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
		input.put(6, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
		input.put(7, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
		input.put(8, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
		input.put(9, new ItemStack(GCItems.partFins));
		input.put(10, new ItemStack(GCItems.partFins));
		input.put(11, new ItemStack(GCItems.rocketEngine));
		input.put(12, new ItemStack(GCItems.partFins));
		input.put(13, new ItemStack(GCItems.partFins));

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(14, new ItemStack(Blocks.chest));
		input2.put(15, null);
		input2.put(16, null);
		GCMarsUtil.adCargoRocketRecipe(new ItemStack(GCMarsItems.spaceship, 1, 11), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(14, null);
		input2.put(15, new ItemStack(Blocks.chest));
		input2.put(16, null);
		GCMarsUtil.adCargoRocketRecipe(new ItemStack(GCMarsItems.spaceship, 1, 11), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(14, null);
		input2.put(15, null);
		input2.put(16, new ItemStack(Blocks.chest));
		GCMarsUtil.adCargoRocketRecipe(new ItemStack(GCMarsItems.spaceship, 1, 11), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(14, new ItemStack(Blocks.chest));
		input2.put(15, new ItemStack(Blocks.chest));
		input2.put(16, null);
		GCMarsUtil.adCargoRocketRecipe(new ItemStack(GCMarsItems.spaceship, 1, 12), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(14, new ItemStack(Blocks.chest));
		input2.put(15, null);
		input2.put(16, new ItemStack(Blocks.chest));
		GCMarsUtil.adCargoRocketRecipe(new ItemStack(GCMarsItems.spaceship, 1, 12), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(14, null);
		input2.put(15, new ItemStack(Blocks.chest));
		input2.put(16, new ItemStack(Blocks.chest));
		GCMarsUtil.adCargoRocketRecipe(new ItemStack(GCMarsItems.spaceship, 1, 12), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(14, new ItemStack(Blocks.chest));
		input2.put(15, new ItemStack(Blocks.chest));
		input2.put(16, new ItemStack(Blocks.chest));
		GCMarsUtil.adCargoRocketRecipe(new ItemStack(GCMarsItems.spaceship, 1, 13), input2);

		RecipeUtil.addRecipe(new ItemStack(GCMarsBlocks.machine, 1, GCMarsBlockMachine.LAUNCH_CONTROLLER_METADATA), new Object[] { "ZVZ", "YXY", "ZWZ", 'V', new ItemStack(GCItems.basicItem, 1, 19), 'W', new ItemStack(GCBlocks.aluminumWire, 1, 0), 'X', new ItemStack(GCItems.basicItem, 1, 14), 'Y', "plateDesh", 'Z', "ingotDesh" });
	}
}
