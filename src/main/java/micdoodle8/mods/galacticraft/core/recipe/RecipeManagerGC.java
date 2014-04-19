package micdoodle8.mods.galacticraft.core.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.CircuitFabricatorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;
import micdoodle8.mods.galacticraft.api.world.SpaceStationType;
import micdoodle8.mods.galacticraft.core.blocks.BlockEnclosed.EnumEnclosedBlock;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.ItemBasic;
import micdoodle8.mods.galacticraft.core.items.ItemParaChute;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * GCCoreRecipeManager.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class RecipeManagerGC
{
	public static void loadRecipes()
	{
		if (CompatibilityManager.isBCraftLoaded())
		{
			RecipeManagerGC.addBuildCraftCraftingRecipes();
		}

		if (CompatibilityManager.isIc2Loaded())
		{
			RecipeManagerGC.addIndustrialCraft2Recipes();
		}

		RecipeManagerGC.addUniversalRecipes();
	}

	@SuppressWarnings("unchecked")
	private static void addUniversalRecipes()
	{
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCBlocks.basicBlock, 1, 5), new ItemStack(GCItems.basicItem, 1, 3), 0.5F);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCBlocks.basicBlock, 6), new ItemStack(GCItems.basicItem, 1, 4), 0.5F);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCBlocks.basicBlock, 7), new ItemStack(GCItems.basicItem, 1, 5), 0.5F);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCItems.meteorChunk, 0), new ItemStack(GCItems.meteorChunk, 1, 1), 0.1F);
		FurnaceRecipes.smelting().func_151396_a(GCItems.meteoricIronRaw, new ItemStack(GCItems.meteoricIronIngot), 1.0F);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCBlocks.blockMoon, 0), OreDictionary.getOres("ingotCopper").get(0), 1.0F);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCBlocks.blockMoon, 1), OreDictionary.getOres("ingotTin").get(0), 1.0F);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCBlocks.blockMoon, 2), new ItemStack(GCItems.cheeseCurd), 1.0F);

		RecipeUtil.addRecipe(new ItemStack(GCItems.rocketEngine, 1, 1), new Object[] { "ZYZ", "ZWZ", "XVX", 'V', GCItems.oxygenVent, 'W', new ItemStack(GCItems.canister, 1, 0), 'X', GCItems.heavyPlatingTier1, 'Y', new ItemStack(Blocks.wool, 1, 4), 'Z', new ItemStack(GCItems.meteoricIronIngot, 1, 1) });

		HashMap<Integer, ItemStack> input = new HashMap<Integer, ItemStack>();
		input.put(1, new ItemStack(GCItems.partNoseCone));
		input.put(2, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(3, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(4, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(5, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(6, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(7, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(8, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(9, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(10, new ItemStack(GCItems.partFins));
		input.put(11, new ItemStack(GCItems.partFins));
		input.put(12, new ItemStack(GCItems.rocketEngine));
		input.put(13, new ItemStack(GCItems.partFins));
		input.put(14, new ItemStack(GCItems.partFins));
		input.put(15, null);
		input.put(16, null);
		input.put(17, null);
		RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTier1, 1, 0), input);

		HashMap<Integer, ItemStack> input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(15, new ItemStack(Blocks.chest));
		input2.put(16, null);
		input2.put(17, null);
		RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTier1, 1, 1), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(15, null);
		input2.put(16, new ItemStack(Blocks.chest));
		input2.put(17, null);
		RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTier1, 1, 1), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(15, null);
		input2.put(16, null);
		input2.put(17, new ItemStack(Blocks.chest));
		RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTier1, 1, 1), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(15, new ItemStack(Blocks.chest));
		input2.put(16, new ItemStack(Blocks.chest));
		input2.put(17, null);
		RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTier1, 1, 2), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(15, new ItemStack(Blocks.chest));
		input2.put(16, null);
		input2.put(17, new ItemStack(Blocks.chest));
		RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTier1, 1, 2), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(15, null);
		input2.put(16, new ItemStack(Blocks.chest));
		input2.put(17, new ItemStack(Blocks.chest));
		RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTier1, 1, 2), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(15, new ItemStack(Blocks.chest));
		input2.put(16, new ItemStack(Blocks.chest));
		input2.put(17, new ItemStack(Blocks.chest));
		RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTier1, 1, 3), input2);

		//

		input = new HashMap<Integer, ItemStack>();
		input.put(1, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(2, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(3, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(4, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(5, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(6, new ItemStack(GCItems.partBuggy, 1, 1));
		input.put(7, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(8, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(9, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(10, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(11, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(12, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(13, new ItemStack(GCItems.partBuggy));
		input.put(14, new ItemStack(GCItems.partBuggy));
		input.put(15, new ItemStack(GCItems.partBuggy));
		input.put(16, new ItemStack(GCItems.partBuggy));
		input.put(17, null);
		input.put(18, null);
		input.put(19, null);
		RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggy, 1, 0), input);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(17, new ItemStack(GCItems.partBuggy, 1, 2));
		input2.put(18, null);
		input2.put(19, null);
		RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggy, 1, 1), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(17, null);
		input2.put(18, new ItemStack(GCItems.partBuggy, 1, 2));
		input2.put(19, null);
		RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggy, 1, 1), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(17, null);
		input2.put(18, null);
		input2.put(19, new ItemStack(GCItems.partBuggy, 1, 2));
		RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggy, 1, 1), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(17, new ItemStack(GCItems.partBuggy, 1, 2));
		input2.put(18, new ItemStack(GCItems.partBuggy, 1, 2));
		input2.put(19, null);
		RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggy, 1, 2), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(17, new ItemStack(GCItems.partBuggy, 1, 2));
		input2.put(18, null);
		input2.put(19, new ItemStack(GCItems.partBuggy, 1, 2));
		RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggy, 1, 2), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(17, null);
		input2.put(18, new ItemStack(GCItems.partBuggy, 1, 2));
		input2.put(19, new ItemStack(GCItems.partBuggy, 1, 2));
		RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggy, 1, 2), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(17, new ItemStack(GCItems.partBuggy, 1, 2));
		input2.put(18, new ItemStack(GCItems.partBuggy, 1, 2));
		input2.put(19, new ItemStack(GCItems.partBuggy, 1, 2));
		RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggy, 1, 3), input2);

		ArrayList<ItemStack> aluminumIngots = new ArrayList<ItemStack>();
		aluminumIngots.addAll(OreDictionary.getOres("ingotAluminum"));
		aluminumIngots.addAll(OreDictionary.getOres("ingotAluminium"));
		aluminumIngots.addAll(OreDictionary.getOres("ingotNaturalAluminum"));

		final HashMap<Object, Integer> inputMap = new HashMap<Object, Integer>();
		inputMap.put("ingotTin", 32);
		inputMap.put(aluminumIngots, 16);
		inputMap.put("waferAdvanced", 1);
		inputMap.put(Items.iron_ingot, 24);
		GalacticraftRegistry.registerSpaceStation(new SpaceStationType(ConfigManagerCore.idDimensionOverworldOrbit, 0, new SpaceStationRecipe(inputMap)));

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.aluminumWire, 6), new Object[] { "WWW", "CCC", "WWW", 'W', Blocks.wool, 'C', "ingotAluminum" });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.aluminumWire, 1, 1), new Object[] { "X", "Y", "Z", 'X', Blocks.wool, 'Y', new ItemStack(GCBlocks.aluminumWire, 1), 'Z', "ingotAluminum" });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.aluminumWire, 1, 1), new Object[] { "Z", "Y", "X", 'X', Blocks.wool, 'Y', new ItemStack(GCBlocks.aluminumWire, 1), 'Z', "ingotAluminum" });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.machineBase, 1, 0), new Object[] { "WWW", "XZX", "XYX", 'W', "ingotCopper", 'X', Items.iron_ingot, 'Y', new ItemStack(GCBlocks.aluminumWire, 1, 0), 'Z', Blocks.furnace });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.machineBase, 1, 4), new Object[] { "SSS", "BBB", "SSS", 'B', new ItemStack(GCItems.battery, 1, GCItems.battery.getMaxDamage()), 'S', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.machineBase, 1, 8), new Object[] { "XXX", "XZX", "WYW", 'W', "compressedAluminum", 'X', "compressedSteel", 'Y', "waferAdvanced", 'Z', Blocks.furnace });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.machineBase, 1, 12), new Object[] { "WXW", "WYW", "WZW", 'W', "ingotAluminum", 'X', Blocks.anvil, 'Y', "ingotCopper", 'Z', "waferBasic" });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.machineBase2, 1, 0), new Object[] { "WXW", "WYW", "VZV", 'V', new ItemStack(GCBlocks.aluminumWire), 'W', "compressedSteel", 'X', Blocks.anvil, 'Y', "compressedBronze", 'Z', "waferAdvanced" });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.machineBase2, 1, 4), new Object[] { "WXW", "UYU", "VZV", 'U', Blocks.stone_button, 'V', new ItemStack(GCBlocks.aluminumWire), 'W', "ingotAluminum", 'X', Blocks.lever, 'Y', Blocks.furnace, 'Z', Blocks.redstone_torch });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.machineBase2, 1, 8), new Object[] { "SSS", "BBB", "SSS", 'B', new ItemStack(GCItems.oxTankHeavy, 1, GCItems.oxTankHeavy.getMaxDamage()), 'S', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCItems.battery, 1, 100), new Object[] { " T ", "TRT", "TCT", 'T', "compressedTin", 'R', Items.redstone, 'C', Items.coal });

		RecipeUtil.addRecipe(new ItemStack(GCItems.rocketEngine, 1), new Object[] { " YV", "XWX", "XZX", 'V', Blocks.stone_button, 'W', new ItemStack(GCItems.canister, 1, 0), 'X', GCItems.heavyPlatingTier1, 'Y', Items.flint_and_steel, 'Z', GCItems.oxygenVent });

		RecipeUtil.addRecipe(new ItemStack(GCItems.rocketEngine, 1), new Object[] { "VY ", "XWX", "XZX", 'V', Blocks.stone_button, 'W', new ItemStack(GCItems.canister, 1, 0), 'X', GCItems.heavyPlatingTier1, 'Y', Items.flint_and_steel, 'Z', GCItems.oxygenVent });

		RecipeUtil.addRecipe(new ItemStack(GCItems.partNoseCone, 1), new Object[] { " Y ", " X ", "X X", 'X', GCItems.heavyPlatingTier1, 'Y', Blocks.redstone_torch });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.oxygenPipe, 4), new Object[] { "XXX", "   ", "XXX", 'X', Blocks.glass_pane });

		RecipeUtil.addRecipe(new ItemStack(GCItems.oxTankLight, 1, GCItems.oxTankLight.getMaxDamage()), new Object[] { "Z", "X", "Y", 'X', new ItemStack(GCItems.canister, 1, 0), 'Y', "compressedCopper", 'Z', new ItemStack(Blocks.wool, 1, 5) });

		RecipeUtil.addRecipe(new ItemStack(GCItems.oxTankMedium, 1, GCItems.oxTankMedium.getMaxDamage()), new Object[] { "ZZ", "XX", "YY", 'X', new ItemStack(GCItems.canister, 1, 0), 'Y', "compressedTin", 'Z', new ItemStack(Blocks.wool, 1, 1) });

		RecipeUtil.addRecipe(new ItemStack(GCItems.sensorGlasses, 1), new Object[] { "ZWZ", "Z Z", "XYX", 'W', Items.diamond, 'X', GCItems.sensorLens, 'Y', GCItems.meteoricIronIngot, 'Z', Items.string });

		RecipeUtil.addRecipe(new ItemStack(GCItems.sensorLens, 1), new Object[] { "ZXZ", "XYX", "ZXZ", 'X', Blocks.glass_pane, 'Y', new ItemStack(GCItems.meteoricIronIngot, 1, 1), 'Z', Items.redstone });

		RecipeUtil.addRecipe(new ItemStack(GCItems.canister, 2, 0), new Object[] { "X X", "X X", "XXX", 'X', "ingotTin" });

		RecipeUtil.addRecipe(new ItemStack(GCItems.canister, 2, 1), new Object[] { "X X", "X X", "XXX", 'X', "ingotCopper" });

		RecipeUtil.addRecipe(new ItemStack(GCItems.oxMask, 1), new Object[] { "XXX", "XYX", "XXX", 'X', Blocks.glass_pane, 'Y', Items.iron_helmet });

		RecipeUtil.addRecipe(new ItemStack(GCItems.canvas, 1), new Object[] { " XY", "XXX", "YX ", 'Y', Items.stick, 'X', Items.string });

		RecipeUtil.addRecipe(new ItemStack(GCItems.parachute, 1, 0), new Object[] { "XXX", "Y Y", " Y ", 'X', GCItems.canvas, 'Y', Items.string });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, 1), new Object[] { "XYX", 'Y', GCBlocks.oxygenPipe, 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, 14), new Object[] { "XYX", 'Y', new ItemStack(GCBlocks.aluminumWire, 1, 0), 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, 15), new Object[] { "XYX", 'Y', new ItemStack(GCBlocks.aluminumWire, 1, 1), 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });

		RecipeUtil.addRecipe(new ItemStack(GCItems.oxygenGear), new Object[] { " Y ", "YXY", "Y Y", 'X', GCItems.oxygenConcentrator, 'Y', GCBlocks.oxygenPipe });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.basicBlock, 4, 3), new Object[] { "   ", " XY", "   ", 'X', new ItemStack(Blocks.stone, 4, 0), 'Y', "compressedTin" });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.basicBlock, 4, 4), new Object[] { "   ", " X ", " Y ", 'X', new ItemStack(Blocks.stone, 4, 0), 'Y', "compressedTin" });

		RecipeUtil.addRecipe(new ItemStack(GCItems.flag, 1, 16), new Object[] { "XYY", "XYY", "X  ", 'X', GCItems.flagPole, 'Y', GCItems.canvas });

		for (int var2 = 0; var2 < 16; ++var2)
		{
			CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCItems.parachute, 1, ItemParaChute.getParachuteDamageValueFromDye(var2)), new Object[] { new ItemStack(Items.dye, 1, var2), new ItemStack(GCItems.parachute, 1, 0) });
		}

		RecipeUtil.addRecipe(new ItemStack(GCItems.partFins, 1), new Object[] { " Y ", "XYX", "X X", 'X', GCItems.heavyPlatingTier1, 'Y', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.landingPad, 9, 0), new Object[] { "YYY", "XXX", 'X', Blocks.iron_block, 'Y', "compressedIron" });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.landingPad, 9, 1), new Object[] { "YYY", "XXX", 'X', Blocks.iron_block, 'Y', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCItems.partBuggy, 1, 0), new Object[] { " W ", "WXW", " W ", 'W', Items.leather, 'X', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCItems.partBuggy, 1, 1), new Object[] { "  Y", " ZY", "XXX", 'X', "compressedSteel", 'Y', "compressedSteel", 'Z', "compressedIron" });

		RecipeUtil.addRecipe(new ItemStack(GCItems.partBuggy, 1, 2), new Object[] { "XXX", "YZY", "XXX", 'X', "compressedSteel", 'Y', "compressedIron", 'Z', Blocks.chest });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.oxygenDetector, 1), new Object[] { "WWW", "YVY", "ZUZ", 'U', "compressedAluminum", 'V', "waferBasic", 'W', "compressedSteel", 'X', GCItems.oxygenFan, 'Y', GCItems.oxygenVent, 'Z', Items.redstone });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.oxygenDistributor, 1), new Object[] { "WXW", "YZY", "WXW", 'W', "compressedSteel", 'X', GCItems.oxygenFan, 'Y', GCItems.oxygenVent, 'Z', "compressedAluminum" });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.oxygenSealer, 1), new Object[] { "UZU", "YXY", "UZU", 'U', "compressedAluminum", 'V', GCBlocks.aluminumWire, 'W', "compressedSteel", 'X', GCItems.oxygenFan, 'Y', GCItems.oxygenVent, 'Z', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.oxygenCollector, 1), new Object[] { "WWW", "YXZ", "UVU", 'U', "compressedAluminum", 'V', GCItems.oxygenConcentrator, 'W', "compressedSteel", 'X', new ItemStack(GCItems.canister, 1, 0), 'Y', GCItems.oxygenFan, 'Z', GCItems.oxygenVent });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.nasaWorkbench, 1), new Object[] { "XZX", "UWU", "YVY", 'U', Blocks.lever, 'V', Blocks.redstone_torch, 'W', "waferAdvanced", 'X', "compressedSteel", 'Y', "compressedSteel", 'Z', Blocks.crafting_table });

		RecipeUtil.addRecipe(new ItemStack(GCItems.oxTankHeavy, 1, GCItems.oxTankHeavy.getMaxDamage()), new Object[] { "ZZZ", "XXX", "YYY", 'X', new ItemStack(GCItems.canister, 1, 0), 'Y', "compressedSteel", 'Z', new ItemStack(Blocks.wool, 1, 14) });

		RecipeUtil.addRecipe(new ItemStack(GCItems.oxygenFan, 1), new Object[] { "Z Z", " Y ", "ZXZ", 'X', Items.redstone, 'Y', "waferBasic", 'Z', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCItems.oxygenConcentrator, 1), new Object[] { "ZWZ", "WYW", "ZXZ", 'W', "compressedTin", 'X', GCItems.oxygenVent, 'Y', new ItemStack(GCItems.canister, 1, 0), 'Z', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCItems.steelPickaxe, 1), new Object[] { "YYY", " X ", " X ", 'Y', "compressedSteel", 'X', Items.stick });

		RecipeUtil.addRecipe(new ItemStack(GCItems.steelAxe, 1), new Object[] { "YY ", "YX ", " X ", 'Y', "compressedSteel", 'X', Items.stick });

		RecipeUtil.addRecipe(new ItemStack(GCItems.steelAxe, 1), new Object[] { " YY", " XY", " X ", 'Y', "compressedSteel", 'X', Items.stick });

		RecipeUtil.addRecipe(new ItemStack(GCItems.steelHoe, 1), new Object[] { " YY", " X ", " X ", 'Y', "compressedSteel", 'X', Items.stick });

		RecipeUtil.addRecipe(new ItemStack(GCItems.steelHoe, 1), new Object[] { "YY ", " X ", " X ", 'Y', "compressedSteel", 'X', Items.stick });

		RecipeUtil.addRecipe(new ItemStack(GCItems.steelSpade, 1), new Object[] { " Y ", " X ", " X ", 'Y', "compressedSteel", 'X', Items.stick });

		RecipeUtil.addRecipe(new ItemStack(GCItems.steelSword, 1), new Object[] { " Y ", " Y ", " X ", 'Y', "compressedSteel", 'X', Items.stick });

		RecipeUtil.addRecipe(new ItemStack(GCItems.steelBoots, 1), new Object[] { "X X", "X X", 'X', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCItems.steelChestplate, 1), new Object[] { "X X", "XXX", "XXX", 'X', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCItems.steelLeggings, 1), new Object[] { "XXX", "X X", "X X", 'X', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCItems.steelHelmet, 1), new Object[] { "XXX", "X X", 'X', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCItems.flagPole, 2, 0), new Object[] { "X", "X", "X", 'X', "compressedSteel" });

		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCItems.oxygenVent, 1), new Object[] { "compressedTin", "compressedTin", "compressedTin", "compressedSteel" }));

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.airLockFrame, 4, 0), new Object[] { "XXX", "YZY", "XXX", 'X', "compressedAluminum", 'Y', "compressedSteel", 'Z', GCItems.oxygenConcentrator });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.airLockFrame, 1, 1), new Object[] { "YYY", "WZW", "YYY", 'W', new ItemStack(GCItems.meteoricIronIngot, 1, 1), 'Y', "compressedSteel", 'Z', new ItemStack(GCItems.basicItem, 1, 13) });

		RecipeUtil.addRecipe(new ItemStack(GCItems.oilExtractor), new Object[] { "X  ", " XY", "ZYY", 'X', "compressedSteel", 'Y', "compressedBronze", 'Z', Items.redstone });

		RecipeUtil.addRecipe(new ItemStack(GCItems.oilCanister, 1, GCItems.oilCanister.getMaxDamage()), new Object[] { "WXW", "WYW", "WZW", 'X', "compressedSteel", 'Y', Blocks.glass, 'Z', new ItemStack(GCItems.canister, 1, 0), 'W', "compressedTin" });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.refinery), new Object[] { " Z ", "WZW", "XYX", 'X', "compressedSteel", 'Y', Blocks.furnace, 'Z', new ItemStack(GCItems.canister, 1, 1), 'W', Blocks.stone });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.oxygenCompressor, 1, 0), new Object[] { "XWX", "WZW", "XYX", 'W', "compressedAluminum", 'X', "compressedSteel", 'Y', "compressedBronze", 'Z', GCItems.oxygenConcentrator });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.oxygenCompressor, 1, 4), new Object[] { "XVX", "WZW", "XYX", 'V', GCItems.oxygenFan, 'W', "compressedAluminum", 'X', "compressedSteel", 'Y', Blocks.redstone_torch, 'Z', GCItems.oxygenConcentrator });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.fuelLoader), new Object[] { "XXX", "XZX", "WYW", 'W', "compressedAluminum", 'X', "compressedSteel", 'Y', "waferBasic", 'Z', new ItemStack(GCItems.canister, 1, 0) });

		RecipeUtil.addRecipe(new ItemStack(GCItems.basicItem, 2, 0), new Object[] { "XXX", "YYY", "ZZZ", 'X', Blocks.glass, 'Y', "waferSolar", 'Z', new ItemStack(GCBlocks.aluminumWire, 1, 0) });

		RecipeUtil.addRecipe(new ItemStack(GCItems.basicItem, 1, 1), new Object[] { "XXX", "YYY", "XXX", 'X', new ItemStack(GCItems.basicItem, 1, 0), 'Y', new ItemStack(GCBlocks.aluminumWire, 1, 0) });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.solarPanel, 1, 0), new Object[] { "XYX", "XZX", "VWV", 'V', new ItemStack(GCBlocks.aluminumWire, 1, 0), 'W', "waferBasic", 'X', "compressedSteel", 'Y', new ItemStack(GCItems.basicItem, 1, 1), 'Z', GCItems.flagPole });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.solarPanel, 1, 4), new Object[] { "XYX", "XZX", "VWV", 'V', new ItemStack(GCBlocks.aluminumWire, 1, 1), 'W', "waferAdvanced", 'X', "compressedSteel", 'Y', new ItemStack(GCItems.basicItem, 1, 1), 'Z', GCItems.flagPole });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.cargoLoader, 1, 0), new Object[] { "XWX", "YZY", "XXX", 'W', Blocks.hopper, 'X', "compressedSteel", 'Y', "compressedAluminum", 'Z', Blocks.chest });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.cargoLoader, 1, 4), new Object[] { "XXX", "YZY", "XWX", 'W', Blocks.hopper, 'X', "compressedSteel", 'Y', "compressedAluminum", 'Z', Blocks.chest });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.glowstoneTorch, 4), new Object[] { "Y", "X", 'X', Items.stick, 'Y', Items.glowstone_dust });

		RecipeUtil.addRecipe(new ItemStack(GCItems.basicItem, 1, 19), new Object[] { " X ", "YUY", "ZWZ", 'U', Items.repeater, 'W', "waferBasic", 'X', "compressedAluminum", 'Y', "compressedIron", 'Z', Items.redstone });

		RecipeUtil.addRecipe(new ItemStack(GCItems.wrench), new Object[] { "  Y", " X ", "X  ", 'X', "compressedBronze", 'Y', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.basicBlock, 1, 9), new Object[] { "XXX", "XXX", "XXX", 'X', "ingotCopper" });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.basicBlock, 1, 10), new Object[] { "XXX", "XXX", "XXX", 'X', "ingotTin" });

		RecipeUtil.addRecipe(new ItemStack(GCBlocks.basicBlock, 1, 11), new Object[] { "XXX", "XXX", "XXX", 'X', "ingotAluminum" });

		RecipeUtil.addRecipe(new ItemStack(GCItems.basicItem, 9, 3), new Object[] { "X", 'X', new ItemStack(GCBlocks.basicBlock, 1, 9) });

		RecipeUtil.addRecipe(new ItemStack(GCItems.basicItem, 9, 4), new Object[] { "X", 'X', new ItemStack(GCBlocks.basicBlock, 1, 10) });

		RecipeUtil.addRecipe(new ItemStack(GCItems.basicItem, 9, 5), new Object[] { "X", 'X', new ItemStack(GCBlocks.basicBlock, 1, 11) });

		RecipeUtil.addRecipe(new ItemStack(GCItems.cheeseBlock, 1), new Object[] { "YYY", "YXY", "YYY", 'X', Items.milk_bucket, 'Y', GCItems.cheeseCurd });

		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCItems.basicItem, 1, 15), new Object[] { new ItemStack(GCItems.canister, 1, 0), Items.apple, Items.apple }));

		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCItems.basicItem, 1, 16), new Object[] { new ItemStack(GCItems.canister, 1, 0), Items.carrot, Items.carrot }));

		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCItems.basicItem, 1, 17), new Object[] { new ItemStack(GCItems.canister, 1, 0), Items.melon, Items.melon }));

		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCItems.basicItem, 1, 18), new Object[] { new ItemStack(GCItems.canister, 1, 0), Items.potato, Items.potato }));

		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCItems.meteorChunk, 3), new Object[] { GCItems.meteoricIronRaw }));
		
		for (int i = 3; i < 8; i++)
		{
			if (ItemBasic.names[i].contains("ingot"))
			{
				for (ItemStack stack : OreDictionary.getOres(ItemBasic.names[i]))
				{
					CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, i + 3), stack, stack);
				}
			}
		}

		// Support for the other spelling of Aluminum
		if (OreDictionary.getOres("ingotAluminium").size() > 0 || OreDictionary.getOres("ingotNaturalAluminum").size() > 0)
		{
			List<ItemStack> aluminumIngotList = new ArrayList<ItemStack>();
			aluminumIngotList.addAll(OreDictionary.getOres("ingotAluminium"));
			aluminumIngotList.addAll(OreDictionary.getOres("ingotNaturalAluminum"));

			for (ItemStack stack : aluminumIngotList)
			{
				CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 8), stack, stack);
			}
		}

		if (OreDictionary.getOres("ingotBronze").size() > 0)
		{
			for (ItemStack stack : OreDictionary.getOres("ingotBronze"))
			{
				CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 10), stack, stack);
			}
		}

		if (OreDictionary.getOres("ingotSteel").size() > 0)
		{
			for (ItemStack stack : OreDictionary.getOres("ingotSteel"))
			{
				CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 9), stack, stack);
			}
		}

		CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 9), Items.coal, new ItemStack(GCItems.basicItem, 1, 11), Items.coal);
		CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 10), new ItemStack(GCItems.basicItem, 1, 6), new ItemStack(GCItems.basicItem, 1, 7));

		CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 11), Items.iron_ingot, Items.iron_ingot);
		CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.meteoricIronIngot, 1, 1), new ItemStack(GCItems.meteoricIronIngot, 1, 0));

		CompressorRecipes.addRecipe(new ItemStack(GCItems.heavyPlatingTier1, 1, 0), "XYZ", "XYZ", 'X', new ItemStack(GCItems.basicItem, 1, 9), 'Y', new ItemStack(GCItems.basicItem, 1, 8), 'Z', new ItemStack(GCItems.basicItem, 1, 10));

		CircuitFabricatorRecipes.addRecipe(new ItemStack(GCItems.basicItem, 9, 12), new ItemStack[] { new ItemStack(Items.diamond), new ItemStack(GCItems.basicItem, 1, 2), new ItemStack(GCItems.basicItem, 1, 2), new ItemStack(Items.redstone), new ItemStack(Items.dye, 1, 4) });

		CircuitFabricatorRecipes.addRecipe(new ItemStack(GCItems.basicItem, 3, 13), new ItemStack[] { new ItemStack(Items.diamond), new ItemStack(GCItems.basicItem, 1, 2), new ItemStack(GCItems.basicItem, 1, 2), new ItemStack(Items.redstone), new ItemStack(Blocks.redstone_torch) });

		CircuitFabricatorRecipes.addRecipe(new ItemStack(GCItems.basicItem, 1, 14), new ItemStack[] { new ItemStack(Items.diamond), new ItemStack(GCItems.basicItem, 1, 2), new ItemStack(GCItems.basicItem, 1, 2), new ItemStack(Items.redstone), new ItemStack(Items.repeater) });

		for (int i = 3; i < 8; i++)
		{
			if (ItemBasic.names[i].contains("ingot"))
			{
				for (ItemStack stack : OreDictionary.getOres(ItemBasic.names[i]))
				{
					CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, i + 3), stack, stack);
				}
			}
		}

		// Support for the other spelling of Aluminum
		if (OreDictionary.getOres("ingotAluminium").size() > 0 || OreDictionary.getOres("ingotNaturalAluminum").size() > 0)
		{
			List<ItemStack> aluminumIngotList = new ArrayList<ItemStack>();
			aluminumIngotList.addAll(OreDictionary.getOres("ingotAluminium"));
			aluminumIngotList.addAll(OreDictionary.getOres("ingotNaturalAluminum"));

			for (ItemStack stack : aluminumIngotList)
			{
				CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 8), stack, stack);
			}
		}

		if (OreDictionary.getOres("ingotBronze").size() > 0)
		{
			for (ItemStack stack : OreDictionary.getOres("ingotBronze"))
			{
				CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 10), stack, stack);
			}
		}

		if (OreDictionary.getOres("ingotSteel").size() > 0)
		{
			for (ItemStack stack : OreDictionary.getOres("ingotSteel"))
			{
				CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 9), stack, stack);
			}
		}

		CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 9), Items.coal, new ItemStack(GCItems.basicItem, 1, 11), Items.coal);
		CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 10), new ItemStack(GCItems.basicItem, 1, 6), new ItemStack(GCItems.basicItem, 1, 7));

		CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 11), Items.iron_ingot, Items.iron_ingot);
		CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.meteoricIronIngot, 1, 1), new ItemStack(GCItems.meteoricIronIngot, 1, 0));

		CompressorRecipes.addRecipe(new ItemStack(GCItems.heavyPlatingTier1, 1, 0), "XYZ", "XYZ", 'X', new ItemStack(GCItems.basicItem, 1, 9), 'Y', new ItemStack(GCItems.basicItem, 1, 8), 'Z', new ItemStack(GCItems.basicItem, 1, 10));

		CircuitFabricatorRecipes.addRecipe(new ItemStack(GCItems.basicItem, 9, 12), new ItemStack[] { new ItemStack(Items.diamond), new ItemStack(GCItems.basicItem, 1, 2), new ItemStack(GCItems.basicItem, 1, 2), new ItemStack(Items.redstone), new ItemStack(Items.dye, 1, 4) });

		CircuitFabricatorRecipes.addRecipe(new ItemStack(GCItems.basicItem, 3, 13), new ItemStack[] { new ItemStack(Items.diamond), new ItemStack(GCItems.basicItem, 1, 2), new ItemStack(GCItems.basicItem, 1, 2), new ItemStack(Items.redstone), new ItemStack(Blocks.redstone_torch) });

		CircuitFabricatorRecipes.addRecipe(new ItemStack(GCItems.basicItem, 1, 14), new ItemStack[] { new ItemStack(Items.diamond), new ItemStack(GCItems.basicItem, 1, 2), new ItemStack(GCItems.basicItem, 1, 2), new ItemStack(Items.redstone), new ItemStack(Items.repeater) });
	}

	private static void addBuildCraftCraftingRecipes()
	{
		try
		{
			Class<?> clazz = Class.forName("buildcraft.BuildCraftTransport");

			Object pipeItemsStone = clazz.getField("pipeItemsStone").get(null);
			Object pipeItemsCobblestone = clazz.getField("pipeItemsCobblestone").get(null);
			Object pipeFluidsCobblestone = clazz.getField("pipeFluidsCobblestone").get(null);
			Object pipeFluidsStone = clazz.getField("pipeFluidsStone").get(null);
			Object pipePowerStone = clazz.getField("pipePowerStone").get(null);
			Object pipePowerGold = clazz.getField("pipePowerGold").get(null);

			RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, EnumEnclosedBlock.BC_ITEM_COBBLESTONEPIPE.getMetadata()), new Object[] { "XYX", 'Y', pipeItemsCobblestone, 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
			RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, EnumEnclosedBlock.BC_ITEM_STONEPIPE.getMetadata()), new Object[] { "XYX", 'Y', pipeItemsStone, 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
			RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, EnumEnclosedBlock.BC_FLUIDS_COBBLESTONEPIPE.getMetadata()), new Object[] { "XYX", 'Y', pipeFluidsCobblestone, 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
			RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, EnumEnclosedBlock.BC_FLUIDS_STONEPIPE.getMetadata()), new Object[] { "XYX", 'Y', pipeFluidsStone, 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
			RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, EnumEnclosedBlock.BC_POWER_STONEPIPE.getMetadata()), new Object[] { "XYX", 'Y', pipePowerStone, 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
			RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, EnumEnclosedBlock.BC_POWER_GOLDPIPE.getMetadata()), new Object[] { "XYX", 'Y', pipePowerGold, 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void addIndustrialCraft2Recipes()
	{
		// RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1,
		// EnumEnclosedBlock.IC2_GOLD_CABLE.getMetadata()), new Object[] {
		// "XYX", 'Y',
		// RecipeUtil.getIndustrialCraftItem("insulatedGoldCableItem"), 'X', new
		// ItemStack(GCCoreBlocks.basicBlock, 1, 4) });
		// RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1,
		// EnumEnclosedBlock.IC2_HV_CABLE.getMetadata()), new Object[] { "XYX",
		// 'Y', RecipeUtil.getIndustrialCraftItem("insulatedIronCableItem"),
		// 'X', new ItemStack(GCCoreBlocks.basicBlock, 1, 4) });
		// RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1,
		// EnumEnclosedBlock.IC2_GLASS_FIBRE_CABLE.getMetadata()), new Object[]
		// { "XYX", 'Y',
		// RecipeUtil.getIndustrialCraftItem("glassFiberCableItem"), 'X', new
		// ItemStack(GCCoreBlocks.basicBlock, 1, 4) });
		// RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1,
		// EnumEnclosedBlock.IC2_LV_CABLE.getMetadata()), new Object[] { "XYX",
		// 'Y', RecipeUtil.getIndustrialCraftItem("tinCableItem"), 'X', new
		// ItemStack(GCCoreBlocks.basicBlock, 1, 4) });
		//
		// try
		// {
		// Class<?> clazz = Class.forName("ic2.core.Ic2Items");
		//
		// Object copperDustObject =
		// clazz.getField("crushedCopperOre").get(null);
		// ItemStack copperDustItemStack = (ItemStack) copperDustObject;
		// Class<?> clazz2 =
		// Class.forName("ic2.api.recipe.RecipeInputItemStack");
		// Object o = clazz2.getConstructor(ItemStack.class).newInstance(new
		// ItemStack(GCCoreBlocks.blockMoon, 1, 0));
		// Method addRecipe =
		// Class.forName("ic2.api.recipe.IMachineRecipeManager").getMethod("addRecipe",
		// Class.forName("ic2.api.recipe.IRecipeInput"), NBTTagCompound.class,
		// ItemStack[].class);
		// addRecipe.invoke(Class.forName("ic2.api.recipe.Recipes").getField("macerator").get(null),
		// o, null, new ItemStack[] { new
		// ItemStack(copperDustItemStack.getItem(), 2,
		// copperDustItemStack.getItemDamage()) });
		//
		// Object tinDustObject = clazz.getField("crushedTinOre").get(null);
		// ItemStack tinDustItemStack = (ItemStack) tinDustObject;
		// o = clazz2.getConstructor(ItemStack.class).newInstance(new
		// ItemStack(GCCoreBlocks.blockMoon, 1, 1));
		// addRecipe.invoke(Class.forName("ic2.api.recipe.Recipes").getField("macerator").get(null),
		// o, null, new ItemStack[] { new ItemStack(tinDustItemStack.getItem(),
		// 2, tinDustItemStack.getItemDamage()) });
		// }
		// catch (Throwable e)
		// {
		// e.printStackTrace();
		// } TODO IC2 recipes
	}
}