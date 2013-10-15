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
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapelessOreRecipe;

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
        FurnaceRecipes.smelting().addSmelting(GCCoreBlocks.decorationBlocks.blockID, 5, new ItemStack(GCCoreItems.basicItem, 1, 3), 0.5F);
        FurnaceRecipes.smelting().addSmelting(GCCoreBlocks.decorationBlocks.blockID, 6, new ItemStack(GCCoreItems.basicItem, 1, 4), 0.5F);
        FurnaceRecipes.smelting().addSmelting(GCCoreBlocks.decorationBlocks.blockID, 7, new ItemStack(GCCoreItems.basicItem, 1, 5), 0.5F);
        FurnaceRecipes.smelting().addSmelting(GCCoreItems.meteorChunk.itemID, 0, new ItemStack(GCCoreItems.meteorChunk.itemID, 1, 1), 0.1F);
        
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

        if (!GalacticraftCore.setSpaceStationRecipe)
        {
            final HashMap<Object, Integer> inputMap = new HashMap<Object, Integer>();
            inputMap.put("ingotTin", 32);
            inputMap.put("ingotAluminum", 16);
            inputMap.put("waferAdvanced", 1);
            inputMap.put(Item.ingotIron, 24);
            GalacticraftRegistry.registerSpaceStation(new SpaceStationType(GCCoreConfigManager.idDimensionOverworldOrbit, 0, new SpaceStationRecipe(inputMap)));
            GalacticraftCore.setSpaceStationRecipe = true;
        }

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.aluminumWire, 6), new Object[] { "WWW", "CCC", "WWW", 'W', Block.cloth, 'C', "ingotAluminum" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.aluminumWire, 6), new Object[] { "WWW", "CCC", "WWW", 'W', Block.cloth, 'C', "ingotAluminium" });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.aluminumWire, 1, 1), new Object[] { "X", "Y", "Z", 'X', Block.cloth, 'Y', new ItemStack(GCCoreBlocks.aluminumWire, 1), 'Z', new ItemStack(GCCoreItems.basicItem, 1, 5) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.aluminumWire, 1, 1), new Object[] { "Z", "Y", "X", 'X', Block.cloth, 'Y', new ItemStack(GCCoreBlocks.aluminumWire, 1), 'Z', new ItemStack(GCCoreItems.basicItem, 1, 5) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.machineBase, 1, 0), new Object[] { "WWW", "XZX", "XYX", 'W', new ItemStack(GCCoreItems.basicItem, 1, 3), 'X', Item.ingotIron, 'Y', new ItemStack(GCCoreBlocks.aluminumWire, 1, 0), 'Z', Block.furnaceIdle });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.machineBase, 1, 4), new Object[] { "SSS", "BBB", "SSS", 'B', new ItemStack(GCCoreItems.battery, 1, 0), 'S', new ItemStack(GCCoreItems.basicItem, 1, 9) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.machineBase, 1, 8), new Object[] { "XXX", "XZX", "WYW", 'W', new ItemStack(GCCoreItems.basicItem, 1, 8), 'X', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 14), 'Z', Block.furnaceIdle });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.machineBase, 1, 12), new Object[] { "WXW", "WYW", "WZW", 'W', new ItemStack(GCCoreItems.basicItem, 1, 5), 'X', Block.anvil, 'Y', new ItemStack(GCCoreItems.basicItem, 1, 3), 'Z', new ItemStack(GCCoreItems.basicItem, 1, 13) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.machineBase2, 1, 0), new Object[] { "WXW", "WYW", "VZV", 'V', new ItemStack(GCCoreBlocks.aluminumWire), 'W', new ItemStack(GCCoreItems.basicItem, 1, 9), 'X', Block.anvil, 'Y', new ItemStack(GCCoreItems.basicItem, 1, 10), 'Z', new ItemStack(GCCoreItems.basicItem, 1, 14) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.machineBase2, 1, 4), new Object[] { "WXW", "UYU", "VZV", 'U', Block.stoneButton, 'V', new ItemStack(GCCoreBlocks.aluminumWire), 'W', new ItemStack(GCCoreItems.basicItem, 1, 5), 'X', Block.lever, 'Y', Block.furnaceIdle, 'Z', Block.torchRedstoneActive });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.battery), new Object[] { " T ", "TRT", "TCT", 'T', new ItemStack(GCCoreItems.basicItem, 1, 7), 'R', Item.redstone, 'C', Item.coal });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] { " YV", "XWX", "XZX", 'V', Block.stoneButton, 'W', new ItemStack(GCCoreItems.canister, 1, 0), 'X', GCCoreItems.heavyPlatingTier1, 'Y', Item.flintAndSteel, 'Z', GCCoreItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] { "VY ", "XWX", "XZX", 'V', Block.stoneButton, 'W', new ItemStack(GCCoreItems.canister, 1, 0), 'X', GCCoreItems.heavyPlatingTier1, 'Y', Item.flintAndSteel, 'Z', GCCoreItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partNoseCone, 1), new Object[] { " Y ", " X ", "X X", 'X', GCCoreItems.heavyPlatingTier1, 'Y', Block.torchRedstoneActive });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenPipe, 4), new Object[] { "XXX", "   ", "XXX", 'X', Block.thinGlass });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxTankLight, 1, GCCoreItems.oxTankLight.getMaxDamage()), new Object[] { "Z", "X", "Y", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 6), 'Z', new ItemStack(Block.cloth, 1, 5) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxTankMedium, 1, GCCoreItems.oxTankMedium.getMaxDamage()), new Object[] { "ZZ", "XX", "YY", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 7), 'Z', new ItemStack(Block.cloth, 1, 1) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.sensorGlasses, 1), new Object[] { "ZWZ", "Z Z", "XYX", 'W', Item.diamond, 'X', GCCoreItems.sensorLens, 'Y', GCMoonItems.meteoricIronIngot, 'Z', Item.silk });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.sensorLens, 1), new Object[] { "ZXZ", "XYX", "ZXZ", 'X', Block.thinGlass, 'Y', new ItemStack(GCMoonItems.meteoricIronIngot, 1, 1), 'Z', Item.redstone });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.canister, 1, 0), new Object[] { "X X", "X X", "XXX", 'X', new ItemStack(GCCoreItems.basicItem, 1, 7) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.canister, 1, 1), new Object[] { "X X", "X X", "XXX", 'X', new ItemStack(GCCoreItems.basicItem, 1, 6) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxMask, 1), new Object[] { "XXX", "XYX", "XXX", 'X', Block.thinGlass, 'Y', Item.helmetIron });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.canvas, 1), new Object[] { " XY", "XXX", "YX ", 'Y', Item.stick, 'X', Item.silk });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.parachute, 1, 0), new Object[] { "XXX", "Y Y", " Y ", 'X', GCCoreItems.canvas, 'Y', Item.silk });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, 1), new Object[] { "XYX", 'Y', GCCoreBlocks.oxygenPipe, 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, 14), new Object[] { "XYX", 'Y', new ItemStack(GCCoreBlocks.aluminumWire, 1, 0), 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, 15), new Object[] { "XYX", 'Y', new ItemStack(GCCoreBlocks.aluminumWire, 1, 1), 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxygenGear), new Object[] { " Y ", "YXY", "Y Y", 'X', GCCoreItems.oxygenConcentrator, 'Y', GCCoreBlocks.oxygenPipe });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 3), new Object[] { "   ", " XY", "   ", 'X', new ItemStack(Block.stone, 4, 0), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 7) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 4), new Object[] { "   ", " X ", " Y ", 'X', new ItemStack(Block.stone, 4, 0), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 7) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.flag, 1, 16), new Object[] { "XYY", "XYY", "X  ", 'X', GCCoreItems.flagPole, 'Y', GCCoreItems.canvas });

        for (int var2 = 0; var2 < 16; ++var2)
        {
            CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.parachute, 1, GCCoreItemParachute.getParachuteDamageValueFromDye(var2)), new Object[] { new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.parachute, 1, 0) });
        }

        for (int var2 = 0; var2 < 16; ++var2)
        {
            CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.flag, 1, GCCoreItemFlag.getFlagDamageValueFromDye(var2)), new Object[] { new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.flag, 1, 16) });
        }
        
        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partFins, 1), new Object[] { " Y ", "XYX", "X X", 'X', GCCoreItems.heavyPlatingTier1, 'Y', new ItemStack(GCCoreItems.basicItem, 1, 9) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.landingPad, 9, 0), new Object[] { "YYY", "XXX", 'X', Block.blockIron, 'Y', new ItemStack(GCCoreItems.basicItem, 1, 11) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.landingPad, 9, 1), new Object[] { "YYY", "XXX", 'X', Block.blockIron, 'Y', new ItemStack(GCCoreItems.basicItem, 1, 9) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partBuggy, 1, 0), new Object[] { " W ", "WXW", " W ", 'W', Item.leather, 'X', new ItemStack(GCCoreItems.basicItem, 1, 9) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partBuggy, 1, 1), new Object[] { "  Y", " ZY", "XXX", 'X', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Z', new ItemStack(GCCoreItems.basicItem, 1, 11) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.partBuggy, 1, 2), new Object[] { "XXX", "YZY", "XXX", 'X', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 11), 'Z', Block.chest });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenDetector, 1), new Object[] { "WWW", "YVY", "ZUZ", 'U', new ItemStack(GCCoreItems.basicItem, 3, 8), 'V', new ItemStack(GCCoreItems.basicItem, 3, 13), 'W', new ItemStack(GCCoreItems.basicItem, 1, 9), 'X', GCCoreItems.oxygenFan, 'Y', GCCoreItems.oxygenVent, 'Z', Item.redstone });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenDistributor, 1), new Object[] { "WXW", "YZY", "WXW", 'W', new ItemStack(GCCoreItems.basicItem, 1, 9), 'X', GCCoreItems.oxygenFan, 'Y', GCCoreItems.oxygenVent, 'Z', new ItemStack(GCCoreItems.basicItem, 1, 8) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenSealer, 1), new Object[] { "UZU", "YXY", "UZU", 'U', new ItemStack(GCCoreItems.basicItem, 1, 8), 'V', GCCoreBlocks.aluminumWire, 'W', new ItemStack(GCCoreItems.basicItem, 1, 9), 'X', GCCoreItems.oxygenFan, 'Y', GCCoreItems.oxygenVent, 'Z', new ItemStack(GCCoreItems.basicItem, 1, 9) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenCollector, 1), new Object[] { "WWW", "YXZ", "UVU", 'U', new ItemStack(GCCoreItems.basicItem, 1, 8), 'V', GCCoreItems.oxygenConcentrator, 'W', new ItemStack(GCCoreItems.basicItem, 1, 9), 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', GCCoreItems.oxygenFan, 'Z', GCCoreItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.nasaWorkbench, 1), new Object[] { "XZX", "UWU", "YVY", 'U', Block.lever, 'V', Block.torchRedstoneActive, 'W', new ItemStack(GCCoreItems.basicItem, 1, 14), 'X', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Z', Block.workbench });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxTankHeavy, 1, GCCoreItems.oxTankHeavy.getMaxDamage()), new Object[] { "ZZZ", "XXX", "YYY", 'X', new ItemStack(GCCoreItems.canister, 1, 0), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Z', new ItemStack(Block.cloth, 1, 14) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxygenFan, 1), new Object[] { "Z Z", " Y ", "ZXZ", 'X', Item.redstone, 'Y', new ItemStack(GCCoreItems.basicItem, 3, 13), 'Z', new ItemStack(GCCoreItems.basicItem, 1, 9) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oxygenConcentrator, 1), new Object[] { "ZWZ", "WYW", "ZXZ", 'W', new ItemStack(GCCoreItems.basicItem, 1, 7), 'X', GCCoreItems.oxygenVent, 'Y', new ItemStack(GCCoreItems.canister, 1, 0), 'Z', new ItemStack(GCCoreItems.basicItem, 1, 9) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelPickaxe, 1), new Object[] { "YYY", " X ", " X ", 'Y', new ItemStack(GCCoreItems.basicItem, 1, 9), 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] { "YY ", "YX ", " X ", 'Y', new ItemStack(GCCoreItems.basicItem, 1, 9), 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] { " YY", " XY", " X ", 'Y', new ItemStack(GCCoreItems.basicItem, 1, 9), 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] { " YY", " X ", " X ", 'Y', new ItemStack(GCCoreItems.basicItem, 1, 9), 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] { "YY ", " X ", " X ", 'Y', new ItemStack(GCCoreItems.basicItem, 1, 9), 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelSpade, 1), new Object[] { " Y ", " X ", " X ", 'Y', new ItemStack(GCCoreItems.basicItem, 1, 9), 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelSword, 1), new Object[] { " Y ", " Y ", " X ", 'Y', new ItemStack(GCCoreItems.basicItem, 1, 9), 'X', Item.stick });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelBoots, 1), new Object[] { "X X", "X X", 'X', new ItemStack(GCCoreItems.basicItem, 1, 9) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelChestplate, 1), new Object[] { "X X", "XXX", "XXX", 'X', new ItemStack(GCCoreItems.basicItem, 1, 9) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelLeggings, 1), new Object[] { "XXX", "X X", "X X", 'X', new ItemStack(GCCoreItems.basicItem, 1, 9) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.steelHelmet, 1), new Object[] { "XXX", "X X", 'X', new ItemStack(GCCoreItems.basicItem, 1, 9) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.flagPole, 2, 0), new Object[] { "X", "X", "X", 'X', new ItemStack(GCCoreItems.basicItem, 1, 9) });

        CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.oxygenVent, 1), new Object[] { new ItemStack(GCCoreItems.basicItem, 1, 7), new ItemStack(GCCoreItems.basicItem, 1, 7), new ItemStack(GCCoreItems.basicItem, 1, 7), new ItemStack(GCCoreItems.basicItem, 1, 9) }));

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.airLockFrame, 2), new Object[] { "XXX", "YVY", "WZW", 'V', new ItemStack(GCMoonItems.meteoricIronIngot, 1, 1), 'W', Item.redstone, 'X', new ItemStack(GCCoreItems.basicItem, 1, 8), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Z', GCCoreItems.oxygenConcentrator });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oilExtractor), new Object[] { "X  ", " XY", "ZYY", 'X', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 10), 'Z', Item.redstone });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage()), new Object[] { "WXW", "WYW", "WZW", 'X', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Y', Block.glass, 'Z', new ItemStack(GCCoreItems.canister, 1, 0), 'W', new ItemStack(GCCoreItems.basicItem, 1, 7) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.refinery), new Object[] { " Z ", "WZW", "XYX", 'X', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Y', Block.furnaceIdle, 'Z', new ItemStack(GCCoreItems.canister, 1, 1), 'W', Block.stone });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.oxygenCompressor), new Object[] { "XWX", "WZW", "XYX", 'W', new ItemStack(GCCoreItems.basicItem, 1, 8), 'X', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 10), 'Z', GCCoreItems.oxygenConcentrator });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.fuelLoader), new Object[] { "XXX", "XZX", "WYW", 'W', new ItemStack(GCCoreItems.basicItem, 1, 8), 'X', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Y', new ItemStack(GCCoreItems.basicItem, 3, 13), 'Z', new ItemStack(GCCoreItems.canister, 1, 0) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.basicItem, 2, 0), new Object[] { "XXX", "YYY", "ZZZ", 'X', Block.glass, 'Y', new ItemStack(Block.cloth, 1, 11), 'Z', new ItemStack(GCCoreBlocks.aluminumWire, 1, 0) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.basicItem, 1, 1), new Object[] { "XXX", "YYY", "XXX", 'X', new ItemStack(GCCoreItems.basicItem, 1, 0), 'Y', new ItemStack(GCCoreBlocks.aluminumWire, 1, 0) });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.solarPanel, 1, 0), new Object[] { "XYX", "XZX", "VWV", 'V', new ItemStack(GCCoreBlocks.aluminumWire, 1, 0), 'W', new ItemStack(GCCoreItems.basicItem, 3, 13), 'X', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 1), 'Z', GCCoreItems.flagPole });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.solarPanel, 1, 4), new Object[] { "XYX", "XZX", "VWV", 'V', new ItemStack(GCCoreBlocks.aluminumWire, 1, 1), 'W', new ItemStack(GCCoreItems.basicItem, 3, 14), 'X', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 1), 'Z', GCCoreItems.flagPole });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.cargoLoader, 1, 0), new Object[] { "XWX", "YZY", "XXX", 'W', Block.hopperBlock, 'X', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 8), 'Z', Block.chest });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.cargoLoader, 1, 4), new Object[] { "XXX", "YZY", "XWX", 'W', Block.hopperBlock, 'X', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 8), 'Z', Block.chest });

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.glowstoneTorch, 4), new Object[] { "Y", "X", 'X', Item.stick, 'Y', Item.glowstone });

        RecipeUtil.addRecipe(new ItemStack(GCCoreItems.basicItem, 1, 19), new Object[] { " X ", "YUY", "ZWZ", 'U', Item.redstoneRepeater, 'W', new ItemStack(GCCoreItems.basicItem, 1, 13), 'X', new ItemStack(GCCoreItems.basicItem, 1, 8), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 11), 'Z', Item.redstone });
        
        CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.basicItem, 1, 15), new Object[] { new ItemStack(GCCoreItems.canister, 1, 0), Item.appleRed, Item.appleRed }));

        CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.basicItem, 1, 16), new Object[] { new ItemStack(GCCoreItems.canister, 1, 0), Item.carrot, Item.carrot }));

        CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.basicItem, 1, 17), new Object[] { new ItemStack(GCCoreItems.canister, 1, 0), Item.melon, Item.melon }));

        CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.basicItem, 1, 18), new Object[] { new ItemStack(GCCoreItems.canister, 1, 0), Item.potato, Item.potato }));
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
    
    private static void addIndustrialCraft2Recipes()
    {

        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.IC2_GOLD_CABLE.getMetadata()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("insulatedGoldCableItem"), 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });
        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.IC2_HV_CABLE.getMetadata()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("insulatedIronCableItem"), 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });
        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.IC2_GLASS_FIBRE_CABLE.getMetadata()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("glassFiberCableItem"), 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });
        RecipeUtil.addRecipe(new ItemStack(GCCoreBlocks.sealableBlock, 1, EnumEnclosedBlock.IC2_LV_CABLE.getMetadata()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("tinCableItem"), 'X', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4) });
        
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
