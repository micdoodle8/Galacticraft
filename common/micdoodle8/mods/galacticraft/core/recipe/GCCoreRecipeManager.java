package micdoodle8.mods.galacticraft.core.recipe;

import java.lang.reflect.Method;
import java.util.HashMap;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;
import micdoodle8.mods.galacticraft.api.world.SpaceStationType;
import micdoodle8.mods.galacticraft.core.GCCoreCompatibilityManager;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockEnclosed.EnumEnclosedBlock;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemFlag;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import micdoodle8.mods.galacticraft.moon.blocks.GCMoonBlocks;
import micdoodle8.mods.galacticraft.moon.items.GCMoonItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import thermalexpansion.api.item.ItemRegistry;

public class GCCoreRecipeManager
{

    public static void loadRecipes()
    {
        if (GCCoreCompatibilityManager.isTELoaded() && GCCoreConfigManager.useRecipesTE)
        {
            GCCoreRecipeManager.addThermalExpansionCraftingRecipes();
        }

        if (GCCoreCompatibilityManager.isBCraftLoaded())
        {
            GCCoreRecipeManager.addBuildCraftCraftingRecipes();
        }

        if (GCCoreCompatibilityManager.isGTLoaded() && GCCoreConfigManager.useRecipesGT)
        {
            GCCoreRecipeManager.addGregTechCraftingRecipes();
        }

        if (GCCoreCompatibilityManager.isIc2Loaded() && GCCoreConfigManager.useRecipesIC2)
        {
            GCCoreRecipeManager.addIndustrialcraftCraftingRecipes();
        }

        if (GCCoreCompatibilityManager.isBCLoaded() && GCCoreConfigManager.useRecipesUE)
        {
            GCCoreRecipeManager.addBasicComponentsCraftingRecipes();
        }

        GCCoreRecipeManager.addUniversalRecipes();
    }

    private static void addUniversalRecipes()
    {
        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1, 1), new Object[] { "ZYZ", "ZWZ", "XVX", 'V', GCCoreItems.oxygenVent, 'W', new ItemStack(GCCoreItems.canister, 1, 0), 'X', GCCoreItems.heavyPlatingTier1, 'Y', new ItemStack(Block.cloth, 1, 4), 'Z', new ItemStack(GCMoonItems.meteoricIronIngot, 1, 1) });

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
        input2.put(15, new ItemStack(Block.chest));
        input2.put(16, null);
        input2.put(17, null);
        RecipeUtil.addRocketBenchRecipe(new ItemStack(GCCoreItems.rocketTier1, 1, 1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(15, null);
        input2.put(16, new ItemStack(Block.chest));
        input2.put(17, null);
        RecipeUtil.addRocketBenchRecipe(new ItemStack(GCCoreItems.rocketTier1, 1, 1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(15, null);
        input2.put(16, null);
        input2.put(17, new ItemStack(Block.chest));
        RecipeUtil.addRocketBenchRecipe(new ItemStack(GCCoreItems.rocketTier1, 1, 1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(15, new ItemStack(Block.chest));
        input2.put(16, new ItemStack(Block.chest));
        input2.put(17, null);
        RecipeUtil.addRocketBenchRecipe(new ItemStack(GCCoreItems.rocketTier1, 1, 2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(15, new ItemStack(Block.chest));
        input2.put(16, null);
        input2.put(17, new ItemStack(Block.chest));
        RecipeUtil.addRocketBenchRecipe(new ItemStack(GCCoreItems.rocketTier1, 1, 2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(15, null);
        input2.put(16, new ItemStack(Block.chest));
        input2.put(17, new ItemStack(Block.chest));
        RecipeUtil.addRocketBenchRecipe(new ItemStack(GCCoreItems.rocketTier1, 1, 2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(15, new ItemStack(Block.chest));
        input2.put(16, new ItemStack(Block.chest));
        input2.put(17, new ItemStack(Block.chest));
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
    }

    @SuppressWarnings("unchecked")
    private static void addGregTechCraftingRecipes()
    {
        if (!GalacticraftCore.setSpaceStationRecipe)
        {
            final HashMap<Object, Integer> inputMap = new HashMap<Object, Integer>();
            inputMap.put(RecipeUtil.getGregtechItem(0, 1, 78), 4);
            inputMap.put(RecipeUtil.getGregtechItem(0, 1, 67), 8);
            inputMap.put("ingotSteel", 14);
            inputMap.put(RecipeUtil.getGregtechBlock(0, 1, 10), 1);
            GalacticraftRegistry.registerSpaceStation(new SpaceStationType(GCCoreConfigManager.idDimensionOverworldOrbit, 0, new SpaceStationRecipe(inputMap)));
            GalacticraftCore.setSpaceStationRecipe = true;
        }

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] { " V ", "XWX", "XZX", 'V', new ItemStack(GCCoreItems.canister, 1, 0), 'W', RecipeUtil.getGregtechBlock(1, 1, 31), 'X', GCCoreItems.heavyPlatingTier1, 'Z', RecipeUtil.getGregtechBlock(1, 1, 37) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partNoseCone, 1), new Object[] { " Z ", " X ", "XYX", 'X', GCCoreItems.heavyPlatingTier1, 'Y', RecipeUtil.getGregtechBlock(1, 1, 4), 'Z', RecipeUtil.getIndustrialCraftItem("reinforcedGlass"), });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenPipe, 4), new Object[] { "XXX", "   ", "XXX", 'X', Block.thinGlass });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxTankLight, 1, GCCoreItems.oxTankLight.getMaxDamage()), new Object[] { "Z", "X", "Y", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', "ingotCopper", 'Z', new ItemStack(Block.cloth, 1, 5) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxTankMedium, 1, GCCoreItems.oxTankMedium.getMaxDamage()), new Object[] { "ZZ", "XX", "YY", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', "ingotTin", 'Z', new ItemStack(Block.cloth, 1, 1) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.sensorGlasses, 1), new Object[] { "YYY", "Y Y", "XYX", 'X', GCCoreItems.sensorLens, 'Y', GCMoonItems.meteoricIronIngot });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.sensorLens, 1), new Object[] { "ZYZ", "YXY", "ZYZ", 'X', Block.thinGlass, 'Y', GCMoonItems.meteoricIronIngot, 'Z', Item.redstone });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.canister, 1, 0), new Object[] { "X X", "X X", "XXX", 'X', "ingotTin" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.canister, 1, 1), new Object[] { "X X", "X X", "XXX", 'X', "ingotCopper" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxMask, 1), new Object[] { "XXX", "XYX", "XXX", 'X', Block.thinGlass, 'Y', Item.helmetIron });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.canvas, 1), new Object[] { " XY", "XXX", "YX ", 'Y', Item.stick, 'X', Item.silk });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.parachute, 1, 0), new Object[] { "XXX", "Y Y", " Y ", 'X', GCCoreItems.canvas, 'Y', Item.silk });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, 1), new Object[] { "XYX", 'X', GCCoreBlocks.oxygenPipe, 'Y', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxygenGear), new Object[] { " Y ", "YXY", "Y Y", 'X', GCCoreItems.oxygenConcentrator, 'Y', GCCoreBlocks.oxygenPipe });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 3), new Object[] { "   ", " XY", "   ", 'X', new ItemStack(Block.stone, 4, 0), 'Y', "ingotTin" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 4), new Object[] { "   ", " X ", " Y ", 'X', new ItemStack(Block.stone, 4, 0), 'Y', "ingotTin" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.flag, 1, 16), new Object[] { "XYY", "XYY", "X  ", 'X', GCCoreItems.flagPole, 'Y', GCCoreItems.canvas });

        for (int var2 = 0; var2 < 16; ++var2)
        {
            CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.parachute, 1, GCCoreItemParachute.getParachuteDamageValueFromDye(var2)), new Object[] { new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.parachute, 1, 0) });
        }

        for (int var2 = 0; var2 < 16; ++var2)
        {
            CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.flag, 1, GCCoreItemFlag.getFlagDamageValueFromDye(var2)), new Object[] { new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.flag, 1, 16) });
        }

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.heavyPlatingTier1, 2), new Object[] { "X", "Y", "Z", 'X', RecipeUtil.getGregtechItem(0, 1, 83), 'Y', RecipeUtil.getGregtechItem(0, 1, 77), 'Z', RecipeUtil.getGregtechItem(0, 1, 75), });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partFins, 1), new Object[] { " Y ", "XYX", "X X", 'X', GCCoreItems.heavyPlatingTier1, 'Y', RecipeUtil.getGregtechItem(0, 1, 78) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.landingPad, 9, 0), new Object[] { "YYY", "XXX", 'X', Block.blockIron, 'Y', "plateIron" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.landingPad, 9, 1), new Object[] { "YYY", "XXX", 'X', Block.blockIron, 'Y', RecipeUtil.getGregtechItem(0, 1, 78) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partBuggy, 1, 0), new Object[] { "WWW", "WXW", "WWW", 'W', Item.leather, 'X', RecipeUtil.getGregtechItem(0, 1, 66) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partBuggy, 1, 1), new Object[] { "  Y", " ZY", "XXX", 'X', RecipeUtil.getGregtechItem(0, 1, 26), 'Y', RecipeUtil.getGregtechItem(0, 1, 78), 'Z', RecipeUtil.getGregtechItem(0, 1, 64) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partBuggy, 1, 2), new Object[] { "XXX", "YZY", "XXX", 'X', RecipeUtil.getGregtechItem(0, 1, 66), 'Y', RecipeUtil.getGregtechItem(0, 1, 78), 'Z', Block.chest });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenDetector, 1), new Object[] { "WXW", "YZY", "WVW", 'V', RecipeUtil.getGregtechItem(0, 1, 83), 'W', "ingotSteel", 'X', GCCoreItems.oxygenFan, 'Y', GCCoreItems.oxygenVent, 'Z', RecipeUtil.getGregtechBlock(1, 0, 15) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenDistributor, 1), new Object[] { "WWW", "WZW", "XVX", 'V', RecipeUtil.getGregtechItem(0, 1, 83), 'W', "ingotSteel", 'X', Item.redstone, 'Y', GCCoreItems.oxygenVent, 'Z', RecipeUtil.getGregtechBlock(1, 0, 15) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenSealer, 1), new Object[] { "WZW", "YTX", "WUW", 'T', RecipeUtil.getGregtechBlock(1, 0, 15), 'V', "copperWire", 'W', "ingotSteel", 'X', GCCoreItems.oxygenFan, 'Y', GCCoreItems.oxygenVent, 'Z', RecipeUtil.getGregtechItem(0, 1, 78), 'U', RecipeUtil.getGregtechItem(0, 1, 83) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenCollector, 1), new Object[] { "WVW", "YXZ", "WUW", 'U', RecipeUtil.getGregtechItem(0, 1, 83), 'V', GCCoreItems.oxygenConcentrator, 'W', "ingotSteel", 'X', RecipeUtil.getGregtechBlock(1, 0, 15), 'Y', GCCoreItems.oxygenFan, 'Z', GCCoreItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.nasaWorkbench, 1), new Object[] { "X Y", "ZAB", "DCD", 'X', RecipeUtil.getGregtechItem(42, 1, 1), 'Y', RecipeUtil.getGregtechItem(46, 1, 1), 'Z', RecipeUtil.getIndustrialCraftItem("diamondDrill"), 'A', RecipeUtil.getGregtechBlock(1, 1, 16), 'B', RecipeUtil.getIndustrialCraftItem("electricWrench"), 'C', RecipeUtil.getGregtechBlock(1, 1, 60), 'D', RecipeUtil.getGregtechBlock(1, 0, 10), });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxTankHeavy, 1, GCCoreItems.oxTankHeavy.getMaxDamage()), new Object[] { "ZZZ", "XXX", "YYY", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', "ingotSteel", 'Z', new ItemStack(Block.cloth, 1, 14) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxygenFan, 1), new Object[] { "Z Z", " Y ", "ZXZ", 'X', Item.redstone, 'Y', "motor", 'Z', "ingotSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxygenConcentrator, 1), new Object[] { "ZWZ", "WYW", "ZXZ", 'W', "ingotTin", 'X', GCCoreItems.oxygenVent, 'Y', new ItemStack(GCCoreItems.canister, 1, 0), 'Z', "ingotSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelPickaxe, 1), new Object[] { "YYY", " X ", " X ", 'Y', "ingotSteel", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] { "YY ", "YX ", " X ", 'Y', "ingotSteel", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] { " YY", " XY", " X ", 'Y', "ingotSteel", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] { " YY", " X ", " X ", 'Y', "ingotSteel", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] { "YY ", " X ", " X ", 'Y', "ingotSteel", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelSpade, 1), new Object[] { " Y ", " X ", " X ", 'Y', "ingotSteel", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelSword, 1), new Object[] { " Y ", " Y ", " X ", 'Y', "ingotSteel", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelBoots, 1), new Object[] { "X X", "X X", 'X', "ingotSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelChestplate, 1), new Object[] { "X X", "XXX", "XXX", 'X', "ingotSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelLeggings, 1), new Object[] { "XXX", "X X", "X X", 'X', "ingotSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelHelmet, 1), new Object[] { "XXX", "X X", 'X', "ingotSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, 0), new Object[] { "XYX", 'X', "copperWire", 'Y', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.flagPole, 2, 0), new Object[] { "X", "X", "X", 'X', "ingotSteel" });

        CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.oxygenVent, 1), new Object[] { "ingotTin", "ingotTin", "ingotTin", "ingotSteel" }));

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.airLockFrame, 2), new Object[] { "XXX", "YVY", "ZWZ", 'V', GCCoreBlocks.oxygenPipe, 'W', Item.redstone, 'X', GCMoonItems.meteoricIronIngot, 'Y', "ingotSteel", 'Z', GCCoreItems.oxygenConcentrator });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oilExtractor), new Object[] { "X  ", " XY", "ZYY", 'X', "ingotSteel", 'Y', "ingotBronze", 'Z', Item.redstone });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage()), new Object[] { "WXW", "WYW", "WZW", 'X', "ingotSteel", 'Y', Block.glass, 'Z', new ItemStack(GCCoreItems.canister, 1, 0), 'W', "ingotTin" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.refinery), new Object[] { " Z ", "WYW", "XVX", 'X', "ingotSteel", 'Y', RecipeUtil.getGregtechBlock(1, 0, 15), 'Z', new ItemStack(GCCoreItems.canister, 1, 1), 'W', Block.stone, 'V', RecipeUtil.getGregtechItem(0, 1, 83) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenCompressor), new Object[] { "XZX", "XWX", "XYX", 'X', "ingotSteel", 'Y', RecipeUtil.getGregtechItem(0, 1, 83), 'Z', GCCoreItems.oxygenConcentrator, 'W', RecipeUtil.getGregtechBlock(1, 0, 15) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.fuelLoader), new Object[] { "XZX", "ZWZ", "XYX", 'X', "ingotSteel", 'Y', RecipeUtil.getGregtechItem(0, 1, 83), 'Z', RecipeUtil.getIndustrialCraftItem("reinforcedGlass"), 'W', RecipeUtil.getGregtechBlock(1, 0, 15) });
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

            RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.BC_ITEM_COBBLESTONEPIPE.getMetadata()), new Object[] { "XYX", 'Y', pipeItemsCobblestone, 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });
            RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.BC_ITEM_STONEPIPE.getMetadata()), new Object[] { "XYX", 'Y', pipeItemsStone, 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });
            RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.BC_FLUIDS_COBBLESTONEPIPE.getMetadata()), new Object[] { "XYX", 'Y', pipeFluidsCobblestone, 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });
            RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.BC_FLUIDS_STONEPIPE.getMetadata()), new Object[] { "XYX", 'Y', pipeFluidsStone, 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });
            RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.BC_POWER_STONEPIPE.getMetadata()), new Object[] { "XYX", 'Y', pipePowerStone, 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });
            RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.BC_POWER_GOLDPIPE.getMetadata()), new Object[] { "XYX", 'Y', pipePowerGold, 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    private static void addThermalExpansionCraftingRecipes()
    {
        if (!GalacticraftCore.setSpaceStationRecipe)
        {
            final HashMap<Object, Integer> inputMap = new HashMap<Object, Integer>();
            inputMap.put("ingotTin", 16);
            inputMap.put("ingotInvar", 24);
            inputMap.put("ingotCopper", 8);
            inputMap.put(ItemRegistry.getItem("powerCoilGold", 1), 1);
            GalacticraftRegistry.registerSpaceStation(new SpaceStationType(GCCoreConfigManager.idDimensionOverworldOrbit, 0, new SpaceStationRecipe(inputMap)));
            GalacticraftCore.setSpaceStationRecipe = true;
        }

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] { " YV", "XWX", "XZX", 'V', Block.stoneButton, 'W', new ItemStack(GCCoreItems.canister, 1, 0), 'X', GCCoreItems.heavyPlatingTier1, 'Y', Item.flintAndSteel, 'Z', GCCoreItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] { "VY ", "XWX", "XZX", 'V', Block.stoneButton, 'W', new ItemStack(GCCoreItems.canister, 1, 0), 'X', GCCoreItems.heavyPlatingTier1, 'Y', Item.flintAndSteel, 'Z', GCCoreItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partNoseCone, 1), new Object[] { " Y ", " X ", "X X", 'X', GCCoreItems.heavyPlatingTier1, 'Y', Block.torchRedstoneActive });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenPipe, 4), new Object[] { "XXX", "   ", "XXX", 'X', Block.thinGlass });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxTankLight, 1, GCCoreItems.oxTankLight.getMaxDamage()), new Object[] { "Z", "X", "Y", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', "ingotCopper", 'Z', new ItemStack(Block.cloth, 1, 5) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxTankMedium, 1, GCCoreItems.oxTankMedium.getMaxDamage()), new Object[] { "ZZ", "XX", "YY", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', "ingotTin", 'Z', new ItemStack(Block.cloth, 1, 1) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.sensorGlasses, 1), new Object[] { "YYY", "Y Y", "XYX", 'X', GCCoreItems.sensorLens, 'Y', GCMoonItems.meteoricIronIngot });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.sensorLens, 1), new Object[] { "ZYZ", "YXY", "ZYZ", 'X', Block.thinGlass, 'Y', GCMoonItems.meteoricIronIngot, 'Z', Item.redstone });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.canister, 1, 0), new Object[] { "X X", "X X", "XXX", 'X', "ingotTin" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.canister, 1, 1), new Object[] { "X X", "X X", "XXX", 'X', "ingotCopper" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxMask, 1), new Object[] { "XXX", "XYX", "XXX", 'X', Block.thinGlass, 'Y', Item.helmetIron });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.canvas, 1), new Object[] { " XY", "XXX", "YX ", 'Y', Item.stick, 'X', Item.silk });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.parachute, 1, 0), new Object[] { "XXX", "Y Y", " Y ", 'X', GCCoreItems.canvas, 'Y', Item.silk });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, 1), new Object[] { "XYX", 'X', GCCoreBlocks.oxygenPipe, 'Y', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxygenGear), new Object[] { " Y ", "YXY", "Y Y", 'X', GCCoreItems.oxygenConcentrator, 'Y', GCCoreBlocks.oxygenPipe });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 3), new Object[] { "   ", " XY", "   ", 'X', new ItemStack(Block.stone, 4, 0), 'Y', "ingotTin" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 4), new Object[] { "   ", " X ", " Y ", 'X', new ItemStack(Block.stone, 4, 0), 'Y', "ingotTin" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.flag, 1, 16), new Object[] { "XYY", "XYY", "X  ", 'X', GCCoreItems.flagPole, 'Y', GCCoreItems.canvas });

        for (int var2 = 0; var2 < 16; ++var2)
        {
            CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.parachute, 1, GCCoreItemParachute.getParachuteDamageValueFromDye(var2)), new Object[] { new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.parachute, 1, 0) });
        }

        for (int var2 = 0; var2 < 16; ++var2)
        {
            CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.flag, 1, GCCoreItemFlag.getFlagDamageValueFromDye(var2)), new Object[] { new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.flag, 1, 16) });
        }

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.heavyPlatingTier1, 2), new Object[] { "XXX", "YYY", "ZZZ", 'X', "ingotInvar", 'Y', "ingotCopper", 'Z', "ingotTin", });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partFins, 1), new Object[] { " Y ", "XYX", "X X", 'X', GCCoreItems.heavyPlatingTier1, 'Y', "ingotInvar" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.landingPad, 9, 0), new Object[] { "YYY", "XXX", 'X', Block.blockIron, 'Y', "ingotInvar" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.landingPad, 9, 1), new Object[] { "YYY", "XXX", 'X', Block.blockIron, 'Y', "ingotLead" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partBuggy, 1, 0), new Object[] { "WWW", "WXW", "WWW", 'W', Item.leather, 'X', "ingotInvar" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partBuggy, 1, 1), new Object[] { "  X", " YX", "XXX", 'X', "ingotInvar", 'Y', Item.ingotIron });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partBuggy, 1, 2), new Object[] { "XXX", "YZY", "XXX", 'X', "ingotInvar", 'Y', "ingotLead", 'Z', Block.chest });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenDetector, 1), new Object[] { "WWW", "YZY", "VWV", 'V', Item.redstone, 'W', "ingotLead", 'X', GCCoreItems.oxygenFan, 'Y', GCCoreItems.oxygenVent, 'Z', ItemRegistry.getItem("machineFrame", 1) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenDistributor, 1), new Object[] { "WXW", "YZY", "WVW", 'V', ItemRegistry.getItem("powerCoilGold", 1), 'W', "ingotLead", 'X', GCCoreItems.oxygenFan, 'Y', GCCoreItems.oxygenVent, 'Z', ItemRegistry.getItem("machineFrame", 1) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenSealer, 1), new Object[] { "WZW", "YTX", "WUW", 'T', ItemRegistry.getItem("machineFrame", 1), 'V', "copperWire", 'W', "ingotLead", 'X', GCCoreItems.oxygenFan, 'Y', GCCoreItems.oxygenVent, 'Z', "ingotInvar", 'U', ItemRegistry.getItem("powerCoilGold", 1) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenCollector, 1), new Object[] { "WVW", "YXZ", "WUW", 'U', ItemRegistry.getItem("powerCoilGold", 1), 'V', GCCoreItems.oxygenConcentrator, 'W', "ingotLead", 'X', ItemRegistry.getItem("machineFrame", 1), 'Y', GCCoreItems.oxygenFan, 'Z', GCCoreItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.nasaWorkbench, 1), new Object[] { "XXX", "YZY", "YWY", 'X', "ingotLead", 'Y', "ingotInvar", 'Z', Block.workbench, 'W', "ingotElectrum" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxTankHeavy, 1, GCCoreItems.oxTankHeavy.getMaxDamage()), new Object[] { "ZZZ", "XXX", "YYY", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', "ingotLead", 'Z', new ItemStack(Block.cloth, 1, 14) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxygenFan, 1), new Object[] { "Z Z", " Y ", "ZXZ", 'X', Item.redstone, 'Y', ItemRegistry.getItem("gearInvar", 1), 'Z', "ingotInvar" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxygenConcentrator, 1), new Object[] { "ZWZ", "WYW", "ZXZ", 'W', "ingotTin", 'X', GCCoreItems.oxygenVent, 'Y', new ItemStack(GCCoreItems.canister, 1, 0), 'Z', "ingotInvar" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelPickaxe, 1), new Object[] { "YYY", " X ", " X ", 'Y', "ingotInvar", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] { "YY ", "YX ", " X ", 'Y', "ingotInvar", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] { " YY", " XY", " X ", 'Y', "ingotInvar", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] { " YY", " X ", " X ", 'Y', "ingotInvar", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] { "YY ", " X ", " X ", 'Y', "ingotInvar", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelSpade, 1), new Object[] { " Y ", " X ", " X ", 'Y', "ingotInvar", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelSword, 1), new Object[] { " Y ", " Y ", " X ", 'Y', "ingotInvar", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelBoots, 1), new Object[] { "X X", "X X", 'X', "ingotInvar" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelChestplate, 1), new Object[] { "X X", "XXX", "XXX", 'X', "ingotInvar" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelLeggings, 1), new Object[] { "XXX", "X X", "X X", 'X', "ingotInvar" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelHelmet, 1), new Object[] { "XXX", "X X", 'X', "ingotInvar" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.flagPole, 2, 0), new Object[] { "X", "X", "X", 'X', "ingotLead" });

        CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.oxygenVent, 1), new Object[] { "ingotTin", "ingotTin", "ingotTin", "ingotLead" }));

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.airLockFrame, 2), new Object[] { "XXX", "YVY", "ZWZ", 'V', GCCoreBlocks.oxygenPipe, 'W', Item.redstone, 'X', GCMoonItems.meteoricIronIngot, 'Y', "ingotLead", 'Z', GCCoreItems.oxygenConcentrator });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oilExtractor), new Object[] { "X  ", " XY", "ZYY", 'X', "ingotLead", 'Y', "ingotBronze", 'Z', Item.redstone });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage()), new Object[] { "WXW", "WYW", "WZW", 'X', "ingotLead", 'Y', Block.glass, 'Z', new ItemStack(GCCoreItems.canister, 1, 0), 'W', "ingotTin" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.refinery), new Object[] { " Z ", "WYW", "XVX", 'X', "ingotLead", 'Y', ItemRegistry.getItem("machineFrame", 1), 'Z', new ItemStack(GCCoreItems.canister, 1, 1), 'W', Block.stone, 'V', ItemRegistry.getItem("powerCoilGold", 1) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenCompressor), new Object[] { "XZX", "XWX", "XYX", 'X', "ingotLead", 'Y', ItemRegistry.getItem("powerCoilGold", 1), 'Z', GCCoreItems.oxygenConcentrator, 'W', ItemRegistry.getItem("machineFrame", 1) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.fuelLoader), new Object[] { "XZX", "ZWZ", "XYX", 'X', "ingotLead", 'Y', ItemRegistry.getItem("powerCoilGold", 1), 'Z', ItemRegistry.getItem("hardenedGlass", 1), 'W', ItemRegistry.getItem("machineFrame", 1) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.basicItem, 2, 0), new Object[] { "XXX", "YYY", "ZZZ", 'X', Block.glass, 'Y', new ItemStack(Block.cloth, 1, 11), 'Z', ItemRegistry.getItem("powerCoilElectrum", 1) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.basicItem, 1, 1), new Object[] { "XXX", "YYY", "XXX", 'X', new ItemStack(GCCoreItems.basicItem, 1, 0), 'Y', Item.redstone });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.solarPanel, 1, 0), new Object[] { " ZY", "XUX", "VWV", 'U', ItemRegistry.getItem("machineFrame", 1), 'V', ItemRegistry.getItem("gearCopper", 1), 'W', ItemRegistry.getItem("powerCoilSilver", 1), 'X', "ingotCopper", 'Y', new ItemStack(GCCoreItems.basicItem, 1, 1), 'Z', GCCoreItems.flagPole });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.solarPanel, 1, 4), new Object[] { " ZY", "XUX", "VWV", 'U', ItemRegistry.getItem("machineFrame", 1), 'V', ItemRegistry.getItem("gearInvar", 1), 'W', ItemRegistry.getItem("powerCoilSilver", 1), 'X', "ingotInvar", 'Y', new ItemStack(GCCoreItems.basicItem, 1, 1), 'Z', GCCoreItems.flagPole });
    }

    @SuppressWarnings("unchecked")
    private static void addBasicComponentsCraftingRecipes()
    {
        if (!GalacticraftCore.setSpaceStationRecipe)
        {
            final HashMap<Object, Integer> inputMap = new HashMap<Object, Integer>();
            inputMap.put("ingotTin", 32);
            inputMap.put("ingotSteel", 16);
            inputMap.put(Item.ingotIron, 24);
            GalacticraftRegistry.registerSpaceStation(new SpaceStationType(GCCoreConfigManager.idDimensionOverworldOrbit, 0, new SpaceStationRecipe(inputMap)));
            GalacticraftCore.setSpaceStationRecipe = true;
        }

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.aluminumWire, 6), new Object[] { "WWW", "CCC", "WWW", 'W', Block.cloth, 'C', "ingotCopper" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.machineBase, 1, 4), new Object[] { "SSS", "BBB", "SSS", 'B', new ItemStack(GCCoreItems.battery, 1, GCCoreItems.battery.getMaxDamage()), 'S', "ingotSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.machineBase, 1, 0), new Object[] { "MMM", "MOM", "MCM", 'M', "ingotSteel", 'C', "motor", 'O', Block.furnaceIdle });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.machineBase, 1, 0), new Object[] { "MMM", "MOM", "MCM", 'M', "ingotBronze", 'C', "motor", 'O', Block.furnaceIdle });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.machineBase, 1, 8), new Object[] { "SSS", "SCS", "SMS", 'S', "ingotSteel", 'C', "circuitAdvanced", 'M', "motor" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.battery), new Object[] { " T ", "TRT", "TCT", 'T', "ingotTin", 'R', Item.redstone, 'C', Item.coal });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] { " YV", "XWX", "XZX", 'V', Block.stoneButton, 'W', new ItemStack(GCCoreItems.canister, 1, 0), 'X', GCCoreItems.heavyPlatingTier1, 'Y', Item.flintAndSteel, 'Z', GCCoreItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] { "VY ", "XWX", "XZX", 'V', Block.stoneButton, 'W', new ItemStack(GCCoreItems.canister, 1, 0), 'X', GCCoreItems.heavyPlatingTier1, 'Y', Item.flintAndSteel, 'Z', GCCoreItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partNoseCone, 1), new Object[] { " Y ", " X ", "X X", 'X', GCCoreItems.heavyPlatingTier1, 'Y', Block.torchRedstoneActive });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenPipe, 4), new Object[] { "XXX", "   ", "XXX", 'X', Block.thinGlass });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxTankLight, 1, GCCoreItems.oxTankLight.getMaxDamage()), new Object[] { "Z", "X", "Y", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', "ingotCopper", 'Z', new ItemStack(Block.cloth, 1, 5) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxTankMedium, 1, GCCoreItems.oxTankMedium.getMaxDamage()), new Object[] { "ZZ", "XX", "YY", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', "ingotTin", 'Z', new ItemStack(Block.cloth, 1, 1) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.sensorGlasses, 1), new Object[] { "YYY", "Y Y", "XYX", 'X', GCCoreItems.sensorLens, 'Y', GCMoonItems.meteoricIronIngot });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.sensorLens, 1), new Object[] { "ZYZ", "YXY", "ZYZ", 'X', Block.thinGlass, 'Y', GCMoonItems.meteoricIronIngot, 'Z', Item.redstone });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.canister, 1, 0), new Object[] { "X X", "X X", "XXX", 'X', "ingotTin" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.canister, 1, 1), new Object[] { "X X", "X X", "XXX", 'X', "ingotCopper" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxMask, 1), new Object[] { "XXX", "XYX", "XXX", 'X', Block.thinGlass, 'Y', Item.helmetIron });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.canvas, 1), new Object[] { " XY", "XXX", "YX ", 'Y', Item.stick, 'X', Item.silk });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.parachute, 1, 0), new Object[] { "XXX", "Y Y", " Y ", 'X', GCCoreItems.canvas, 'Y', Item.silk });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, 1), new Object[] { "XYX", 'Y', GCCoreBlocks.oxygenPipe, 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, 0), new Object[] { "XYX", 'Y', "copperWire", 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxygenGear), new Object[] { " Y ", "YXY", "Y Y", 'X', GCCoreItems.oxygenConcentrator, 'Y', GCCoreBlocks.oxygenPipe });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 3), new Object[] { "   ", " XY", "   ", 'X', new ItemStack(Block.stone, 4, 0), 'Y', "ingotTin" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 4), new Object[] { "   ", " X ", " Y ", 'X', new ItemStack(Block.stone, 4, 0), 'Y', "ingotTin" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.flag, 1, 16), new Object[] { "XYY", "XYY", "X  ", 'X', GCCoreItems.flagPole, 'Y', GCCoreItems.canvas });

        for (int var2 = 0; var2 < 16; ++var2)
        {
            CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.parachute, 1, GCCoreItemParachute.getParachuteDamageValueFromDye(var2)), new Object[] { new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.parachute, 1, 0) });
        }

        for (int var2 = 0; var2 < 16; ++var2)
        {
            CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.flag, 1, GCCoreItemFlag.getFlagDamageValueFromDye(var2)), new Object[] { new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.flag, 1, 16) });
        }

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.heavyPlatingTier1, 2), new Object[] { "X", "Y", "Z", 'X', "plateIron", 'Y', "plateSteel", 'Z', "plateBronze", });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partFins, 1), new Object[] { " Y ", "XYX", "X X", 'X', GCCoreItems.heavyPlatingTier1, 'Y', "plateSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.landingPad, 9, 0), new Object[] { "YYY", "XXX", 'X', Block.blockIron, 'Y', "plateIron" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.landingPad, 9, 1), new Object[] { "YYY", "XXX", 'X', Block.blockIron, 'Y', "plateSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partBuggy, 1, 0), new Object[] { "WWW", "WXW", "WWW", 'W', Item.leather, 'X', "ingotSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partBuggy, 1, 1), new Object[] { "  Y", " ZY", "XXX", 'X', "ingotSteel", 'Y', "plateSteel", 'Z', Item.ingotIron });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partBuggy, 1, 2), new Object[] { "XXX", "YZY", "XXX", 'X', "plateSteel", 'Y', "plateIron", 'Z', Block.chest });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenDetector, 1), new Object[] { "WWW", "YWY", "ZWZ", 'W', "ingotSteel", 'X', GCCoreItems.oxygenFan, 'Y', GCCoreItems.oxygenVent, 'Z', Item.redstone });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenDistributor, 1), new Object[] { "WXW", "YWY", "WXW", 'W', "ingotSteel", 'X', GCCoreItems.oxygenFan, 'Y', GCCoreItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenSealer, 1), new Object[] { "WZW", "YXY", "WZW", 'V', "copperWire", 'W', "ingotSteel", 'X', GCCoreItems.oxygenFan, 'Y', GCCoreItems.oxygenVent, 'Z', "plateSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenCollector, 1), new Object[] { "WWW", "YXZ", "WVW", 'V', GCCoreItems.oxygenConcentrator, 'W', "ingotSteel", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', GCCoreItems.oxygenFan, 'Z', GCCoreItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.nasaWorkbench, 1), new Object[] { "XXX", "YZY", "YWY", 'X', "ingotSteel", 'Y', "plateSteel", 'Z', Block.workbench, 'W', "circuitAdvanced" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxTankHeavy, 1, GCCoreItems.oxTankHeavy.getMaxDamage()), new Object[] { "ZZZ", "XXX", "YYY", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', "ingotSteel", 'Z', new ItemStack(Block.cloth, 1, 14) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxygenFan, 1), new Object[] { "Z Z", " Y ", "ZXZ", 'X', Item.redstone, 'Y', "motor", 'Z', "ingotSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxygenConcentrator, 1), new Object[] { "ZWZ", "WYW", "ZXZ", 'W', "ingotTin", 'X', GCCoreItems.oxygenVent, 'Y', new ItemStack(GCCoreItems.canister, 1, 0), 'Z', "ingotSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelPickaxe, 1), new Object[] { "YYY", " X ", " X ", 'Y', "ingotSteel", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] { "YY ", "YX ", " X ", 'Y', "ingotSteel", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] { " YY", " XY", " X ", 'Y', "ingotSteel", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] { " YY", " X ", " X ", 'Y', "ingotSteel", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] { "YY ", " X ", " X ", 'Y', "ingotSteel", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelSpade, 1), new Object[] { " Y ", " X ", " X ", 'Y', "ingotSteel", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelSword, 1), new Object[] { " Y ", " Y ", " X ", 'Y', "ingotSteel", 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelBoots, 1), new Object[] { "X X", "X X", 'X', "ingotSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelChestplate, 1), new Object[] { "X X", "XXX", "XXX", 'X', "ingotSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelLeggings, 1), new Object[] { "XXX", "X X", "X X", 'X', "ingotSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelHelmet, 1), new Object[] { "XXX", "X X", 'X', "ingotSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.flagPole, 2, 0), new Object[] { "X", "X", "X", 'X', "ingotSteel" });

        CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.oxygenVent, 1), new Object[] { "ingotTin", "ingotTin", "ingotTin", "ingotSteel" }));

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.airLockFrame, 2), new Object[] { "XXX", "YVY", "WZW", 'V', new ItemStack(GCMoonItems.meteoricIronIngot, 1, 1), 'W', Item.redstone, 'X', GCMoonItems.meteoricIronIngot, 'Y', "ingotSteel", 'Z', GCCoreItems.oxygenConcentrator });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oilExtractor), new Object[] { "X  ", " XY", "ZYY", 'X', "ingotSteel", 'Y', "ingotBronze", 'Z', Item.redstone });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage()), new Object[] { "WXW", "WYW", "WZW", 'X', "ingotSteel", 'Y', Block.glass, 'Z', new ItemStack(GCCoreItems.canister, 1, 0), 'W', "ingotTin" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.refinery), new Object[] { " Z ", "WZW", "XYX", 'X', "ingotSteel", 'Y', Block.furnaceIdle, 'Z', new ItemStack(GCCoreItems.canister, 1, 1), 'W', Block.stone });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenCompressor), new Object[] { "XXX", "XZX", "XYX", 'X', "ingotSteel", 'Y', "ingotBronze", 'Z', GCCoreItems.oxygenConcentrator });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.fuelLoader), new Object[] { "XXX", "XZX", "XYX", 'X', "ingotSteel", 'Y', "circuitBasic", 'Z', "motor" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.basicItem, 2, 0), new Object[] { "XXX", "YYY", "ZZZ", 'X', Block.glass, 'Y', new ItemStack(Block.cloth, 1, 11), 'Z', "copperWire" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.basicItem, 1, 1), new Object[] { "XXX", "YYY", "XXX", 'X', new ItemStack(GCCoreItems.basicItem, 1, 0), 'Y', "copperWire" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.solarPanel, 1, 0), new Object[] { "XYX", "XZX", "VWV", 'V', "copperWire", 'W', "circuitBasic", 'X', "plateSteel", 'Y', new ItemStack(GCCoreItems.basicItem, 1, 1), 'Z', GCCoreItems.flagPole });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.solarPanel, 1, 4), new Object[] { "XYX", "XZX", "VWV", 'V', "copperWire", 'W', "circuitAdvanced", 'X', "plateSteel", 'Y', new ItemStack(GCCoreItems.basicItem, 1, 1), 'Z', GCCoreItems.flagPole });
    }

    @SuppressWarnings("unchecked")
    private static void addIndustrialcraftCraftingRecipes()
    {
        Object plate = null;
        
        if (RecipeUtil.getIndustrialCraftItem("plateiron") != null)
        {
            plate = RecipeUtil.getIndustrialCraftItem("plateiron");
        }
        else
        {
            plate = "ingotRefinedIron";
        }
        
        
        if (!GalacticraftCore.setSpaceStationRecipe)
        {
            final HashMap<Object, Integer> inputMap = new HashMap<Object, Integer>();
            inputMap.put(RecipeUtil.getIndustrialCraftItem("machine"), 1);
            inputMap.put("ingotTin", 24);
            inputMap.put(Item.ingotIron, 12);
            GalacticraftRegistry.registerSpaceStation(new SpaceStationType(GCCoreConfigManager.idDimensionOverworldOrbit, 0, new SpaceStationRecipe(inputMap)));
            GalacticraftCore.setSpaceStationRecipe = true;
        }

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] { " YV", "XWX", "XZX", 'V', Block.stoneButton, 'W', new ItemStack(GCCoreItems.canister, 1, 0), 'X', GCCoreItems.heavyPlatingTier1, 'Y', Item.flintAndSteel, 'Z', GCCoreItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] { "VY ", "XWX", "XZX", 'V', Block.stoneButton, 'W', new ItemStack(GCCoreItems.canister, 1, 0), 'X', GCCoreItems.heavyPlatingTier1, 'Y', Item.flintAndSteel, 'Z', GCCoreItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partNoseCone, 1), new Object[] { " Y ", " X ", "X X", 'X', GCCoreItems.heavyPlatingTier1, 'Y', Block.torchRedstoneActive });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenPipe, 4), new Object[] { "XXX", "   ", "XXX", 'X', Block.thinGlass });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxTankLight, 1, GCCoreItems.oxTankLight.getMaxDamage()), new Object[] { "Z", "X", "Y", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', "ingotCopper", 'Z', new ItemStack(Block.cloth, 1, 5) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxTankMedium, 1, GCCoreItems.oxTankMedium.getMaxDamage()), new Object[] { "ZZ", "XX", "YY", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', "ingotTin", 'Z', new ItemStack(Block.cloth, 1, 1) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.sensorGlasses, 1), new Object[] { "YYY", "Y Y", "XYX", 'X', GCCoreItems.sensorLens, 'Y', GCMoonItems.meteoricIronIngot });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.sensorLens, 1), new Object[] { "ZYZ", "YXY", "ZYZ", 'X', Block.thinGlass, 'Y', GCMoonItems.meteoricIronIngot, 'Z', Item.redstone });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.canister, 1, 0), new Object[] { "X X", "X X", "XXX", 'X', "ingotTin" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.canister, 1, 1), new Object[] { "X X", "X X", "XXX", 'X', "ingotCopper" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxMask, 1), new Object[] { "XXX", "XYX", "XXX", 'X', Block.thinGlass, 'Y', Item.helmetIron });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.canvas, 1), new Object[] { " XY", "XXX", "YX ", 'Y', Item.stick, 'X', Item.silk });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.parachute, 1, 0), new Object[] { "XXX", "Y Y", " Y ", 'X', GCCoreItems.canvas, 'Y', Item.silk });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, 1), new Object[] { "XYX", 'X', GCCoreBlocks.oxygenPipe, 'Y', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxygenGear), new Object[] { " Y ", "YXY", "Y Y", 'X', GCCoreItems.oxygenConcentrator, 'Y', GCCoreBlocks.oxygenPipe });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 3), new Object[] { "   ", " XY", "   ", 'X', new ItemStack(Block.stone, 4, 0), 'Y', "ingotTin" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 4), new Object[] { "   ", " X ", " Y ", 'X', new ItemStack(Block.stone, 4, 0), 'Y', "ingotTin" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.flag, 1, 16), new Object[] { "XYY", "XYY", "X  ", 'X', GCCoreItems.flagPole, 'Y', GCCoreItems.canvas });

        for (int var2 = 0; var2 < 16; ++var2)
        {
            CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.parachute, 1, GCCoreItemParachute.getParachuteDamageValueFromDye(var2)), new Object[] { new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.parachute, 1, 0) });
        }

        for (int var2 = 0; var2 < 16; ++var2)
        {
            CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.flag, 1, GCCoreItemFlag.getFlagDamageValueFromDye(var2)), new Object[] { new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.flag, 1, 16) });
        }

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.heavyPlatingTier1, 2), new Object[] { "X", "X", 'X', RecipeUtil.getIndustrialCraftItem("carbonPlate") });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partFins, 1), new Object[] { " Y ", "XYX", "X X", 'X', GCCoreItems.heavyPlatingTier1, 'Y', RecipeUtil.getIndustrialCraftItem("carbonPlate") });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.landingPad, 9, 0), new Object[] { "YYY", "XXX", 'X', Block.blockIron, 'Y', plate });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.landingPad, 9, 1), new Object[] { "YYY", "XXX", 'X', Block.blockIron, 'Y', RecipeUtil.getIndustrialCraftItem("carbonPlate") });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partBuggy, 1, 0), new Object[] { "WWW", "WXW", "WWW", 'W', Item.leather, 'X', plate });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partBuggy, 1, 1), new Object[] { "  X", " YX", "XXX", 'X', plate, 'Y', Item.ingotIron });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partBuggy, 1, 2), new Object[] { "XXX", "YZY", "XXX", 'X', plate, 'Y', RecipeUtil.getIndustrialCraftItem("carbonPlate"), 'Z', Block.chest });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenDetector, 1), new Object[] { "WWW", "YWY", "WXW", 'W', plate, 'X', Item.redstone, 'Y', GCCoreItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenDistributor, 1), new Object[] { "WXW", "YWY", "WXW", 'W', plate, 'X', GCCoreItems.oxygenFan, 'Y', GCCoreItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenCollector, 1), new Object[] { "WWW", "YXZ", "WVW", 'V', GCCoreItems.oxygenConcentrator, 'W', plate, 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', GCCoreItems.oxygenFan, 'Z', GCCoreItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.nasaWorkbench, 1), new Object[] { "XXX", "YZY", "YWY", 'X', plate, 'Y', plate, 'Z', Block.workbench, 'W', RecipeUtil.getIndustrialCraftItem("advancedCircuit") });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxTankHeavy, 1, GCCoreItems.oxTankHeavy.getMaxDamage()), new Object[] { "ZZZ", "XXX", "YYY", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', plate, 'Z', new ItemStack(Block.cloth, 1, 14) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxygenFan, 1), new Object[] { "Z Z", " Y ", "ZXZ", 'X', Item.redstone, 'Y', RecipeUtil.getIndustrialCraftItem("electronicCircuit"), 'Z', plate });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.fuelLoader), new Object[] { "XXX", "X X", "XYX", 'X', plate, 'Y', RecipeUtil.getIndustrialCraftItem("electronicCircuit") });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenCompressor), new Object[] { "XXX", "XZX", "XYX", 'X', plate, 'Y', "ingotBronze", 'Z', GCCoreItems.oxygenConcentrator });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.refinery), new Object[] { " Z ", "WZW", "XYX", 'X', plate, 'Y', Block.furnaceIdle, 'Z', new ItemStack(GCCoreItems.canister, 1, 1), 'W', Block.stone });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage()), new Object[] { "WXW", "WYW", "WZW", 'X', plate, 'Y', Block.glass, 'Z', new ItemStack(GCCoreItems.canister, 1, 0), 'W', "ingotTin" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oilExtractor), new Object[] { "X  ", " XY", "ZYY", 'X', plate, 'Y', "ingotBronze", 'Z', Item.redstone });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.airLockFrame, 2), new Object[] { "XXX", "YVY", "ZWZ", 'V', GCCoreBlocks.oxygenPipe, 'W', Item.redstone, 'X', GCMoonItems.meteoricIronIngot, 'Y', plate, 'Z', GCCoreItems.oxygenConcentrator });

        CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.oxygenVent, 1), new Object[] { "ingotTin", "ingotTin", "ingotTin", plate }));

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxygenConcentrator, 1), new Object[] { "ZWZ", "WYW", "ZXZ", 'W', "ingotTin", 'X', GCCoreItems.oxygenVent, 'Y', new ItemStack(GCCoreItems.canister, 1, 0), 'Z', plate });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelPickaxe, 1), new Object[] { "YYY", " X ", " X ", 'Y', plate, 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] { "YY ", "YX ", " X ", 'Y', plate, 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] { " YY", " XY", " X ", 'Y', plate, 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] { " YY", " X ", " X ", 'Y', plate, 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] { "YY ", " X ", " X ", 'Y', plate, 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelSpade, 1), new Object[] { " Y ", " X ", " X ", 'Y', plate, 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelSword, 1), new Object[] { " Y ", " Y ", " X ", 'Y', plate, 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelBoots, 1), new Object[] { "X X", "X X", 'X', plate });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelChestplate, 1), new Object[] { "X X", "XXX", "XXX", 'X', plate });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelLeggings, 1), new Object[] { "XXX", "X X", "X X", 'X', plate });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelHelmet, 1), new Object[] { "XXX", "X X", 'X', plate });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.flagPole, 2, 0), new Object[] { "X", "X", "X", 'X', plate });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenSealer, 1), new Object[] { "WZW", "YXY", "WZW", 'V', RecipeUtil.getIndustrialCraftItem("insulatedCopperCableItem"), 'W', plate, 'X', GCCoreItems.oxygenFan, 'Y', GCCoreItems.oxygenVent, 'Z', RecipeUtil.getIndustrialCraftItem("carbonPlate") });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, 2), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("insulatedCopperCableItem"), 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.IC2_GOLD_CABLE.getMetadata()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("insulatedGoldCableItem"), 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.IC2_HV_CABLE.getMetadata()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("insulatedIronCableItem"), 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.IC2_GLASS_FIBRE_CABLE.getMetadata()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("glassFiberCableItem"), 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.IC2_LV_CABLE.getMetadata()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("tinCableItem"), 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.basicItem, 2, 0), new Object[] { "XXX", "YYY", "ZZZ", 'X', Block.glass, 'Y', new ItemStack(Block.cloth, 1, 11), 'Z', RecipeUtil.getIndustrialCraftItem("insulatedCopperCableItem") });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.basicItem, 1, 1), new Object[] { "XXX", "YYY", "XXX", 'X', new ItemStack(GCCoreItems.basicItem, 1, 0), 'Y', RecipeUtil.getIndustrialCraftItem("insulatedCopperCableItem") });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.solarPanel, 1, 0), new Object[] { "XYX", "XZX", "VWV", 'V', RecipeUtil.getIndustrialCraftItem("insulatedCopperCableItem"), 'W', RecipeUtil.getIndustrialCraftItem("electronicCircuit"), 'X', plate, 'Y', new ItemStack(GCCoreItems.basicItem, 1, 1), 'Z', GCCoreItems.flagPole });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.solarPanel, 1, 4), new Object[] { "XYX", "XZX", "VWV", 'V', RecipeUtil.getIndustrialCraftItem("insulatedCopperCableItem"), 'W', RecipeUtil.getIndustrialCraftItem("advancedCircuit"), 'X', plate, 'Y', new ItemStack(GCCoreItems.basicItem, 1, 1), 'Z', GCCoreItems.flagPole });

        try
        {
            Class<?> clazz = Class.forName("ic2.core.Ic2Items");

            Object copperDustObject = clazz.getField("crushedCopperOre").get(null);
            ItemStack copperDustItemStack = (ItemStack) copperDustObject;
            Class<?> clazz2 = Class.forName("ic2.api.recipe.RecipeInputItemStack");
            Object o = clazz2.getConstructor(ItemStack.class).newInstance(new ItemStack(GCMoonBlocks.blockMoon.blockID, 1, 0));
            Method addRecipe = Class.forName("ic2.api.recipe.IMachineRecipeManager").getMethod("addRecipe", Class.forName("ic2.api.recipe.IRecipeInput"), NBTTagCompound.class, ItemStack[].class);
            addRecipe.invoke(Class.forName("ic2.api.recipe.Recipes").getField("macerator").get(null), o, null, new ItemStack[] { new ItemStack(copperDustItemStack.getItem(), 2, copperDustItemStack.getItemDamage()) });

            Object tinDustObject = clazz.getField("crushedTinOre").get(null);
            ItemStack tinDustItemStack = (ItemStack) tinDustObject;
            o = clazz2.getConstructor(ItemStack.class).newInstance(new ItemStack(GCMoonBlocks.blockMoon.blockID, 1, 1));
            addRecipe.invoke(Class.forName("ic2.api.recipe.Recipes").getField("macerator").get(null), o, null, new ItemStack[] { new ItemStack(tinDustItemStack.getItem(), 2, tinDustItemStack.getItemDamage()) });
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
}
