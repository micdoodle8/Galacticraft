package micdoodle8.mods.galacticraft.core.recipe;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;
import micdoodle8.mods.galacticraft.api.world.SpaceStationType;
import micdoodle8.mods.galacticraft.core.GCCoreCompatibilityManager;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockEnclosed.EnumEnclosedBlock;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemFlag;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
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
public class GCCoreRecipeManager
{
	public static void loadRecipes()
	{
		if (GCCoreCompatibilityManager.isBCraftLoaded())
		{
			GCCoreRecipeManager.addBuildCraftCraftingRecipes();
		}

		if (GCCoreCompatibilityManager.isIc2Loaded())
		{
			GCCoreRecipeManager.addIndustrialCraft2Recipes();
		}

		GCCoreRecipeManager.addUniversalRecipes();
	}

	@SuppressWarnings("unchecked")
	private static void addUniversalRecipes()
	{
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCCoreBlocks.basicBlock, 1, 5), new ItemStack(GCCoreItems.basicItem, 1, 3), 0.5F);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCCoreBlocks.basicBlock, 6), new ItemStack(GCCoreItems.basicItem, 1, 4), 0.5F);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCCoreBlocks.basicBlock, 7), new ItemStack(GCCoreItems.basicItem, 1, 5), 0.5F);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCCoreItems.meteorChunk, 0), new ItemStack(GCCoreItems.meteorChunk, 1, 1), 0.1F);
		FurnaceRecipes.smelting().func_151396_a(GCCoreItems.meteoricIronRaw, new ItemStack(GCCoreItems.meteoricIronIngot), 1.0F);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCCoreBlocks.blockMoon, 0), OreDictionary.getOres("ingotCopper").get(0), 1.0F);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCCoreBlocks.blockMoon, 1), OreDictionary.getOres("ingotTin").get(0), 1.0F);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GCCoreBlocks.blockMoon, 2), new ItemStack(GCCoreItems.cheeseCurd), 1.0F);

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1, 1), new Object[] { "ZYZ", "ZWZ", "XVX", 'V', GCCoreItems.oxygenVent, 'W', new ItemStack(GCCoreItems.canister, 1, 0), 'X', GCCoreItems.heavyPlatingTier1, 'Y', new ItemStack(Blocks.wool, 1, 4), 'Z', new ItemStack(GCCoreItems.meteoricIronIngot, 1, 1) });

		HashMap<Integer, ItemStack> input = new HashMap<Integer, ItemStack>();
		input.put(1, new ItemStack(GCCoreItems.partNoseCone));
		input.put(2, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(3, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(4, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(5, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(6, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(7, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(8, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(9, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(10, new ItemStack(GCCoreItems.partFins));
		input.put(11, new ItemStack(GCCoreItems.partFins));
		input.put(12, new ItemStack(GCCoreItems.rocketEngine));
		input.put(13, new ItemStack(GCCoreItems.partFins));
		input.put(14, new ItemStack(GCCoreItems.partFins));
		input.put(15, null);
		input.put(16, null);
		input.put(17, null);
		RecipeUtil.addRocketBenchRecipe(new ItemStack(GCCoreItems.rocketTier1, 1, 0), input);

		HashMap<Integer, ItemStack> input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(15, new ItemStack(Blocks.chest));
		input2.put(16, null);
		input2.put(17, null);
		RecipeUtil.addRocketBenchRecipe(new ItemStack(GCCoreItems.rocketTier1, 1, 1), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(15, null);
		input2.put(16, new ItemStack(Blocks.chest));
		input2.put(17, null);
		RecipeUtil.addRocketBenchRecipe(new ItemStack(GCCoreItems.rocketTier1, 1, 1), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(15, null);
		input2.put(16, null);
		input2.put(17, new ItemStack(Blocks.chest));
		RecipeUtil.addRocketBenchRecipe(new ItemStack(GCCoreItems.rocketTier1, 1, 1), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(15, new ItemStack(Blocks.chest));
		input2.put(16, new ItemStack(Blocks.chest));
		input2.put(17, null);
		RecipeUtil.addRocketBenchRecipe(new ItemStack(GCCoreItems.rocketTier1, 1, 2), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(15, new ItemStack(Blocks.chest));
		input2.put(16, null);
		input2.put(17, new ItemStack(Blocks.chest));
		RecipeUtil.addRocketBenchRecipe(new ItemStack(GCCoreItems.rocketTier1, 1, 2), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(15, null);
		input2.put(16, new ItemStack(Blocks.chest));
		input2.put(17, new ItemStack(Blocks.chest));
		RecipeUtil.addRocketBenchRecipe(new ItemStack(GCCoreItems.rocketTier1, 1, 2), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(15, new ItemStack(Blocks.chest));
		input2.put(16, new ItemStack(Blocks.chest));
		input2.put(17, new ItemStack(Blocks.chest));
		RecipeUtil.addRocketBenchRecipe(new ItemStack(GCCoreItems.rocketTier1, 1, 3), input2);

		//

		input = new HashMap<Integer, ItemStack>();
		input.put(1, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(2, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(3, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(4, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(5, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(6, new ItemStack(GCCoreItems.partBuggy, 1, 1));
		input.put(7, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(8, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(9, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(10, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(11, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(12, new ItemStack(GCCoreItems.heavyPlatingTier1));
		input.put(13, new ItemStack(GCCoreItems.partBuggy));
		input.put(14, new ItemStack(GCCoreItems.partBuggy));
		input.put(15, new ItemStack(GCCoreItems.partBuggy));
		input.put(16, new ItemStack(GCCoreItems.partBuggy));
		input.put(17, null);
		input.put(18, null);
		input.put(19, null);
		RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCCoreItems.buggy, 1, 0), input);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(17, new ItemStack(GCCoreItems.partBuggy, 1, 2));
		input2.put(18, null);
		input2.put(19, null);
		RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCCoreItems.buggy, 1, 1), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(17, null);
		input2.put(18, new ItemStack(GCCoreItems.partBuggy, 1, 2));
		input2.put(19, null);
		RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCCoreItems.buggy, 1, 1), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(17, null);
		input2.put(18, null);
		input2.put(19, new ItemStack(GCCoreItems.partBuggy, 1, 2));
		RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCCoreItems.buggy, 1, 1), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(17, new ItemStack(GCCoreItems.partBuggy, 1, 2));
		input2.put(18, new ItemStack(GCCoreItems.partBuggy, 1, 2));
		input2.put(19, null);
		RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCCoreItems.buggy, 1, 2), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(17, new ItemStack(GCCoreItems.partBuggy, 1, 2));
		input2.put(18, null);
		input2.put(19, new ItemStack(GCCoreItems.partBuggy, 1, 2));
		RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCCoreItems.buggy, 1, 2), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(17, null);
		input2.put(18, new ItemStack(GCCoreItems.partBuggy, 1, 2));
		input2.put(19, new ItemStack(GCCoreItems.partBuggy, 1, 2));
		RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCCoreItems.buggy, 1, 2), input2);

		input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(17, new ItemStack(GCCoreItems.partBuggy, 1, 2));
		input2.put(18, new ItemStack(GCCoreItems.partBuggy, 1, 2));
		input2.put(19, new ItemStack(GCCoreItems.partBuggy, 1, 2));
		RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCCoreItems.buggy, 1, 3), input2);

		ArrayList<ItemStack> aluminumIngots = new ArrayList<ItemStack>();
		aluminumIngots.addAll(OreDictionary.getOres("ingotAluminum"));
		aluminumIngots.addAll(OreDictionary.getOres("ingotAluminium"));
		aluminumIngots.addAll(OreDictionary.getOres("ingotNaturalAluminum"));

		final HashMap<Object, Integer> inputMap = new HashMap<Object, Integer>();
		inputMap.put("ingotTin", 32);
		inputMap.put(aluminumIngots, 16);
		inputMap.put("waferAdvanced", 1);
		inputMap.put(Items.iron_ingot, 24);
		GalacticraftRegistry.registerSpaceStation(new SpaceStationType(GCCoreConfigManager.idDimensionOverworldOrbit, 0, new SpaceStationRecipe(inputMap)));

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.aluminumWire, 6), new Object[] { "WWW", "CCC", "WWW", 'W', Blocks.wool, 'C', "ingotAluminum" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.aluminumWire, 1, 1), new Object[] { "X", "Y", "Z", 'X', Blocks.wool, 'Y', new ItemStack(GCCoreBlocks.aluminumWire, 1), 'Z', "ingotAluminum" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.aluminumWire, 1, 1), new Object[] { "Z", "Y", "X", 'X', Blocks.wool, 'Y', new ItemStack(GCCoreBlocks.aluminumWire, 1), 'Z', "ingotAluminum" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.machineBase, 1, 0), new Object[] { "WWW", "XZX", "XYX", 'W', "ingotCopper", 'X', Items.iron_ingot, 'Y', new ItemStack(GCCoreBlocks.aluminumWire, 1, 0), 'Z', Blocks.furnace });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.machineBase, 1, 4), new Object[] { "SSS", "BBB", "SSS", 'B', new ItemStack(GCCoreItems.battery, 1, GCCoreItems.battery.getMaxDamage()), 'S', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.machineBase, 1, 8), new Object[] { "XXX", "XZX", "WYW", 'W', "compressedAluminum", 'X', "compressedSteel", 'Y', "waferAdvanced", 'Z', Blocks.furnace });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.machineBase, 1, 12), new Object[] { "WXW", "WYW", "WZW", 'W', "ingotAluminum", 'X', Blocks.anvil, 'Y', "ingotCopper", 'Z', "waferBasic" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.machineBase2, 1, 0), new Object[] { "WXW", "WYW", "VZV", 'V', new ItemStack(GCCoreBlocks.aluminumWire), 'W', "compressedSteel", 'X', Blocks.anvil, 'Y', "compressedBronze", 'Z', "waferAdvanced" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.machineBase2, 1, 4), new Object[] { "WXW", "UYU", "VZV", 'U', Blocks.stone_button, 'V', new ItemStack(GCCoreBlocks.aluminumWire), 'W', "ingotAluminum", 'X', Blocks.lever, 'Y', Blocks.furnace, 'Z', Blocks.redstone_torch });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.machineBase2, 1, 8), new Object[] { "SSS", "BBB", "SSS", 'B', new ItemStack(GCCoreItems.oxTankHeavy, 1, GCCoreItems.oxTankHeavy.getMaxDamage()), 'S', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.battery, 1, 100), new Object[] { " T ", "TRT", "TCT", 'T', "compressedTin", 'R', Items.redstone, 'C', Items.coal });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] { " YV", "XWX", "XZX", 'V', Blocks.stone_button, 'W', new ItemStack(GCCoreItems.canister, 1, 0), 'X', GCCoreItems.heavyPlatingTier1, 'Y', Items.flint_and_steel, 'Z', GCCoreItems.oxygenVent });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] { "VY ", "XWX", "XZX", 'V', Blocks.stone_button, 'W', new ItemStack(GCCoreItems.canister, 1, 0), 'X', GCCoreItems.heavyPlatingTier1, 'Y', Items.flint_and_steel, 'Z', GCCoreItems.oxygenVent });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partNoseCone, 1), new Object[] { " Y ", " X ", "X X", 'X', GCCoreItems.heavyPlatingTier1, 'Y', Blocks.redstone_torch });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenPipe, 4), new Object[] { "XXX", "   ", "XXX", 'X', Blocks.glass_pane });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxTankLight, 1, GCCoreItems.oxTankLight.getMaxDamage()), new Object[] { "Z", "X", "Y", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', "compressedCopper", 'Z', new ItemStack(Blocks.wool, 1, 5) });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxTankMedium, 1, GCCoreItems.oxTankMedium.getMaxDamage()), new Object[] { "ZZ", "XX", "YY", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', "compressedTin", 'Z', new ItemStack(Blocks.wool, 1, 1) });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.sensorGlasses, 1), new Object[] { "ZWZ", "Z Z", "XYX", 'W', Items.diamond, 'X', GCCoreItems.sensorLens, 'Y', GCCoreItems.meteoricIronIngot, 'Z', Items.string });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.sensorLens, 1), new Object[] { "ZXZ", "XYX", "ZXZ", 'X', Blocks.glass_pane, 'Y', new ItemStack(GCCoreItems.meteoricIronIngot, 1, 1), 'Z', Items.redstone });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.canister, 2, 0), new Object[] { "X X", "X X", "XXX", 'X', "ingotTin" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.canister, 2, 1), new Object[] { "X X", "X X", "XXX", 'X', "ingotCopper" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxMask, 1), new Object[] { "XXX", "XYX", "XXX", 'X', Blocks.glass_pane, 'Y', Items.iron_helmet });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.canvas, 1), new Object[] { " XY", "XXX", "YX ", 'Y', Items.stick, 'X', Items.string });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.parachute, 1, 0), new Object[] { "XXX", "Y Y", " Y ", 'X', GCCoreItems.canvas, 'Y', Items.string });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, 1), new Object[] { "XYX", 'Y', GCCoreBlocks.oxygenPipe, 'X', new ItemStack(GCCoreBlocks.basicBlock, 1, 4) });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, 14), new Object[] { "XYX", 'Y', new ItemStack(GCCoreBlocks.aluminumWire, 1, 0), 'X', new ItemStack(GCCoreBlocks.basicBlock, 1, 4) });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, 15), new Object[] { "XYX", 'Y', new ItemStack(GCCoreBlocks.aluminumWire, 1, 1), 'X', new ItemStack(GCCoreBlocks.basicBlock, 1, 4) });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxygenGear), new Object[] { " Y ", "YXY", "Y Y", 'X', GCCoreItems.oxygenConcentrator, 'Y', GCCoreBlocks.oxygenPipe });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.basicBlock, 4, 3), new Object[] { "   ", " XY", "   ", 'X', new ItemStack(Blocks.stone, 4, 0), 'Y', "compressedTin" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.basicBlock, 4, 4), new Object[] { "   ", " X ", " Y ", 'X', new ItemStack(Blocks.stone, 4, 0), 'Y', "compressedTin" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.flag, 1, 16), new Object[] { "XYY", "XYY", "X  ", 'X', GCCoreItems.flagPole, 'Y', GCCoreItems.canvas });

		for (int var2 = 0; var2 < 16; ++var2)
		{
			CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.parachute, 1, GCCoreItemParachute.getParachuteDamageValueFromDye(var2)), new Object[] { new ItemStack(Items.dye, 1, var2), new ItemStack(GCCoreItems.parachute, 1, 0) });
		}

		for (int var2 = 0; var2 < 16; ++var2)
		{
			CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.flag, 1, GCCoreItemFlag.getFlagDamageValueFromDye(var2)), new Object[] { new ItemStack(Items.dye, 1, var2), new ItemStack(GCCoreItems.flag, 1, 16) });
		}

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partFins, 1), new Object[] { " Y ", "XYX", "X X", 'X', GCCoreItems.heavyPlatingTier1, 'Y', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.landingPad, 9, 0), new Object[] { "YYY", "XXX", 'X', Blocks.iron_block, 'Y', "compressedIron" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.landingPad, 9, 1), new Object[] { "YYY", "XXX", 'X', Blocks.iron_block, 'Y', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partBuggy, 1, 0), new Object[] { " W ", "WXW", " W ", 'W', Items.leather, 'X', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partBuggy, 1, 1), new Object[] { "  Y", " ZY", "XXX", 'X', "compressedSteel", 'Y', "compressedSteel", 'Z', "compressedIron" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partBuggy, 1, 2), new Object[] { "XXX", "YZY", "XXX", 'X', "compressedSteel", 'Y', "compressedIron", 'Z', Blocks.chest });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenDetector, 1), new Object[] { "WWW", "YVY", "ZUZ", 'U', "compressedAluminum", 'V', "waferBasic", 'W', "compressedSteel", 'X', GCCoreItems.oxygenFan, 'Y', GCCoreItems.oxygenVent, 'Z', Items.redstone });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenDistributor, 1), new Object[] { "WXW", "YZY", "WXW", 'W', "compressedSteel", 'X', GCCoreItems.oxygenFan, 'Y', GCCoreItems.oxygenVent, 'Z', "compressedAluminum" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenSealer, 1), new Object[] { "UZU", "YXY", "UZU", 'U', "compressedAluminum", 'V', GCCoreBlocks.aluminumWire, 'W', "compressedSteel", 'X', GCCoreItems.oxygenFan, 'Y', GCCoreItems.oxygenVent, 'Z', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenCollector, 1), new Object[] { "WWW", "YXZ", "UVU", 'U', "compressedAluminum", 'V', GCCoreItems.oxygenConcentrator, 'W', "compressedSteel", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', GCCoreItems.oxygenFan, 'Z', GCCoreItems.oxygenVent });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.nasaWorkbench, 1), new Object[] { "XZX", "UWU", "YVY", 'U', Blocks.lever, 'V', Blocks.redstone_torch, 'W', "waferAdvanced", 'X', "compressedSteel", 'Y', "compressedSteel", 'Z', Blocks.crafting_table });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxTankHeavy, 1, GCCoreItems.oxTankHeavy.getMaxDamage()), new Object[] { "ZZZ", "XXX", "YYY", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', "compressedSteel", 'Z', new ItemStack(Blocks.wool, 1, 14) });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxygenFan, 1), new Object[] { "Z Z", " Y ", "ZXZ", 'X', Items.redstone, 'Y', "waferBasic", 'Z', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxygenConcentrator, 1), new Object[] { "ZWZ", "WYW", "ZXZ", 'W', "compressedTin", 'X', GCCoreItems.oxygenVent, 'Y', new ItemStack(GCCoreItems.canister, 1, 0), 'Z', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelPickaxe, 1), new Object[] { "YYY", " X ", " X ", 'Y', "compressedSteel", 'X', Items.stick });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] { "YY ", "YX ", " X ", 'Y', "compressedSteel", 'X', Items.stick });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] { " YY", " XY", " X ", 'Y', "compressedSteel", 'X', Items.stick });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] { " YY", " X ", " X ", 'Y', "compressedSteel", 'X', Items.stick });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] { "YY ", " X ", " X ", 'Y', "compressedSteel", 'X', Items.stick });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelSpade, 1), new Object[] { " Y ", " X ", " X ", 'Y', "compressedSteel", 'X', Items.stick });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelSword, 1), new Object[] { " Y ", " Y ", " X ", 'Y', "compressedSteel", 'X', Items.stick });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelBoots, 1), new Object[] { "X X", "X X", 'X', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelChestplate, 1), new Object[] { "X X", "XXX", "XXX", 'X', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelLeggings, 1), new Object[] { "XXX", "X X", "X X", 'X', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelHelmet, 1), new Object[] { "XXX", "X X", 'X', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.flagPole, 2, 0), new Object[] { "X", "X", "X", 'X', "compressedSteel" });

		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.oxygenVent, 1), new Object[] { "compressedTin", "compressedTin", "compressedTin", "compressedSteel" }));

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.airLockFrame, 4, 0), new Object[] { "XXX", "YZY", "XXX", 'X', "compressedAluminum", 'Y', "compressedSteel", 'Z', GCCoreItems.oxygenConcentrator });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.airLockFrame, 1, 1), new Object[] { "YYY", "WZW", "YYY", 'W', new ItemStack(GCCoreItems.meteoricIronIngot, 1, 1), 'Y', "compressedSteel", 'Z', new ItemStack(GCCoreItems.basicItem, 1, 13) });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oilExtractor), new Object[] { "X  ", " XY", "ZYY", 'X', "compressedSteel", 'Y', "compressedBronze", 'Z', Items.redstone });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage()), new Object[] { "WXW", "WYW", "WZW", 'X', "compressedSteel", 'Y', Blocks.glass, 'Z', new ItemStack(GCCoreItems.canister, 1, 0), 'W', "compressedTin" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.refinery), new Object[] { " Z ", "WZW", "XYX", 'X', "compressedSteel", 'Y', Blocks.furnace, 'Z', new ItemStack(GCCoreItems.canister, 1, 1), 'W', Blocks.stone });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenCompressor, 1, 0), new Object[] { "XWX", "WZW", "XYX", 'W', "compressedAluminum", 'X', "compressedSteel", 'Y', "compressedBronze", 'Z', GCCoreItems.oxygenConcentrator });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenCompressor, 1, 4), new Object[] { "XVX", "WZW", "XYX", 'V', GCCoreItems.oxygenFan, 'W', "compressedAluminum", 'X', "compressedSteel", 'Y', Blocks.redstone_torch, 'Z', GCCoreItems.oxygenConcentrator });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.fuelLoader), new Object[] { "XXX", "XZX", "WYW", 'W', "compressedAluminum", 'X', "compressedSteel", 'Y', "waferBasic", 'Z', new ItemStack(GCCoreItems.canister, 1, 0) });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.basicItem, 2, 0), new Object[] { "XXX", "YYY", "ZZZ", 'X', Blocks.glass, 'Y', "waferSolar", 'Z', new ItemStack(GCCoreBlocks.aluminumWire, 1, 0) });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.basicItem, 1, 1), new Object[] { "XXX", "YYY", "XXX", 'X', new ItemStack(GCCoreItems.basicItem, 1, 0), 'Y', new ItemStack(GCCoreBlocks.aluminumWire, 1, 0) });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.solarPanel, 1, 0), new Object[] { "XYX", "XZX", "VWV", 'V', new ItemStack(GCCoreBlocks.aluminumWire, 1, 0), 'W', "waferBasic", 'X', "compressedSteel", 'Y', new ItemStack(GCCoreItems.basicItem, 1, 1), 'Z', GCCoreItems.flagPole });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.solarPanel, 1, 4), new Object[] { "XYX", "XZX", "VWV", 'V', new ItemStack(GCCoreBlocks.aluminumWire, 1, 1), 'W', "waferAdvanced", 'X', "compressedSteel", 'Y', new ItemStack(GCCoreItems.basicItem, 1, 1), 'Z', GCCoreItems.flagPole });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.cargoLoader, 1, 0), new Object[] { "XWX", "YZY", "XXX", 'W', Blocks.hopper, 'X', "compressedSteel", 'Y', "compressedAluminum", 'Z', Blocks.chest });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.cargoLoader, 1, 4), new Object[] { "XXX", "YZY", "XWX", 'W', Blocks.hopper, 'X', "compressedSteel", 'Y', "compressedAluminum", 'Z', Blocks.chest });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.glowstoneTorch, 4), new Object[] { "Y", "X", 'X', Items.stick, 'Y', Items.glowstone_dust });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.basicItem, 1, 19), new Object[] { " X ", "YUY", "ZWZ", 'U', Items.repeater, 'W', "waferBasic", 'X', "compressedAluminum", 'Y', "compressedIron", 'Z', Items.redstone });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.wrench), new Object[] { "  Y", " X ", "X  ", 'X', "compressedBronze", 'Y', "compressedSteel" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.basicBlock, 1, 9), new Object[] { "XXX", "XXX", "XXX", 'X', "ingotCopper" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.basicBlock, 1, 10), new Object[] { "XXX", "XXX", "XXX", 'X', "ingotTin" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.basicBlock, 1, 11), new Object[] { "XXX", "XXX", "XXX", 'X', "ingotAluminum" });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.basicItem, 9, 3), new Object[] { "X", 'X', new ItemStack(GCCoreBlocks.basicBlock, 1, 9) });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.basicItem, 9, 4), new Object[] { "X", 'X', new ItemStack(GCCoreBlocks.basicBlock, 1, 10) });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.basicItem, 9, 5), new Object[] { "X", 'X', new ItemStack(GCCoreBlocks.basicBlock, 1, 11) });

		RecipeUtil.addRecipe(new ItemStack(GCCoreItems.cheeseBlock, 1), new Object[] { "YYY", "YXY", "YYY", 'X', Items.milk_bucket, 'Y', GCCoreItems.cheeseCurd });

		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.basicItem, 1, 15), new Object[] { new ItemStack(GCCoreItems.canister, 1, 0), Items.apple, Items.apple }));

		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.basicItem, 1, 16), new Object[] { new ItemStack(GCCoreItems.canister, 1, 0), Items.carrot, Items.carrot }));

		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.basicItem, 1, 17), new Object[] { new ItemStack(GCCoreItems.canister, 1, 0), Items.melon, Items.melon }));

		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.basicItem, 1, 18), new Object[] { new ItemStack(GCCoreItems.canister, 1, 0), Items.potato, Items.potato }));

		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.meteorChunk, 3), new Object[] { GCCoreItems.meteoricIronRaw }));
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

			RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.BC_ITEM_COBBLESTONEPIPE.getMetadata()), new Object[] { "XYX", 'Y', pipeItemsCobblestone, 'X', new ItemStack(GCCoreBlocks.basicBlock, 1, 4) });
			RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.BC_ITEM_STONEPIPE.getMetadata()), new Object[] { "XYX", 'Y', pipeItemsStone, 'X', new ItemStack(GCCoreBlocks.basicBlock, 1, 4) });
			RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.BC_FLUIDS_COBBLESTONEPIPE.getMetadata()), new Object[] { "XYX", 'Y', pipeFluidsCobblestone, 'X', new ItemStack(GCCoreBlocks.basicBlock, 1, 4) });
			RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.BC_FLUIDS_STONEPIPE.getMetadata()), new Object[] { "XYX", 'Y', pipeFluidsStone, 'X', new ItemStack(GCCoreBlocks.basicBlock, 1, 4) });
			RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.BC_POWER_STONEPIPE.getMetadata()), new Object[] { "XYX", 'Y', pipePowerStone, 'X', new ItemStack(GCCoreBlocks.basicBlock, 1, 4) });
			RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.BC_POWER_GOLDPIPE.getMetadata()), new Object[] { "XYX", 'Y', pipePowerGold, 'X', new ItemStack(GCCoreBlocks.basicBlock, 1, 4) });

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void addIndustrialCraft2Recipes()
	{
//		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.IC2_GOLD_CABLE.getMetadata()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("insulatedGoldCableItem"), 'X', new ItemStack(GCCoreBlocks.basicBlock, 1, 4) });
//		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.IC2_HV_CABLE.getMetadata()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("insulatedIronCableItem"), 'X', new ItemStack(GCCoreBlocks.basicBlock, 1, 4) });
//		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.IC2_GLASS_FIBRE_CABLE.getMetadata()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("glassFiberCableItem"), 'X', new ItemStack(GCCoreBlocks.basicBlock, 1, 4) });
//		RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.IC2_LV_CABLE.getMetadata()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("tinCableItem"), 'X', new ItemStack(GCCoreBlocks.basicBlock, 1, 4) });
//
//		try
//		{
//			Class<?> clazz = Class.forName("ic2.core.Ic2Items");
//
//			Object copperDustObject = clazz.getField("crushedCopperOre").get(null);
//			ItemStack copperDustItemStack = (ItemStack) copperDustObject;
//			Class<?> clazz2 = Class.forName("ic2.api.recipe.RecipeInputItemStack");
//			Object o = clazz2.getConstructor(ItemStack.class).newInstance(new ItemStack(GCCoreBlocks.blockMoon, 1, 0));
//			Method addRecipe = Class.forName("ic2.api.recipe.IMachineRecipeManager").getMethod("addRecipe", Class.forName("ic2.api.recipe.IRecipeInput"), NBTTagCompound.class, ItemStack[].class);
//			addRecipe.invoke(Class.forName("ic2.api.recipe.Recipes").getField("macerator").get(null), o, null, new ItemStack[] { new ItemStack(copperDustItemStack.getItem(), 2, copperDustItemStack.getItemDamage()) });
//
//			Object tinDustObject = clazz.getField("crushedTinOre").get(null);
//			ItemStack tinDustItemStack = (ItemStack) tinDustObject;
//			o = clazz2.getConstructor(ItemStack.class).newInstance(new ItemStack(GCCoreBlocks.blockMoon, 1, 1));
//			addRecipe.invoke(Class.forName("ic2.api.recipe.Recipes").getField("macerator").get(null), o, null, new ItemStack[] { new ItemStack(tinDustItemStack.getItem(), 2, tinDustItemStack.getItemDamage()) });
//		}
//		catch (Throwable e)
//		{
//			e.printStackTrace();
//		} TODO IC2 recipes
	}
}
