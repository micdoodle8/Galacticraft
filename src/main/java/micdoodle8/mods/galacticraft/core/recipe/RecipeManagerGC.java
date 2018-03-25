package micdoodle8.mods.galacticraft.core.recipe;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPED;
import buildcraft.api.recipes.BuildcraftRecipeRegistry;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.CircuitFabricatorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.IRecipeUpdatable;
import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;
import micdoodle8.mods.galacticraft.api.world.SpaceStationType;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockEnclosed;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine3;
import micdoodle8.mods.galacticraft.core.items.ItemBasic;
import micdoodle8.mods.galacticraft.core.items.ItemEmergencyKit;
import micdoodle8.mods.galacticraft.core.items.ItemParaChute;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerClient;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.venus.VenusItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

//import appeng.api.AEApi;
//import appeng.api.util.AEColor;

public class RecipeManagerGC
{
    public static ArrayList<ItemStack> aluminumIngots = new ArrayList<ItemStack>();
    private static boolean configSaved_RequireGCmetals = true;
    private static boolean configSaved_QuickMode = false;
    private static String configSaved_Silicon = "-";

    public static void loadRecipes()
    {
        if (CompatibilityManager.isBCraftTransportLoaded())
        {
            RecipeManagerGC.addBuildCraftCraftingRecipes();
        }

        if (CompatibilityManager.isIc2Loaded())
        {
            CompatModuleIC2.addIndustrialCraft2Recipes();
        }

        if (CompatibilityManager.isAppEngLoaded())
        {
            RecipeManagerGC.addAppEngRecipes();
        }

        if (CompatibilityManager.modAALoaded)
        {
            CompatModuleActuallyAdditions.addRecipes();
        }
        
        RecipeManagerGC.addUniversalRecipes();

        RecipeManagerGC.addExNihiloRecipes();
    }

    private static void addUniversalRecipes()
    {
        RecipeSorter.register("galacticraftcore:shapedore", OreRecipeUpdatable.class, SHAPED, "after:minecraft:shaped before:minecraft:shapeless");
        
        Object meteoricIronIngot = new ItemStack(GCItems.itemBasicMoon, 1, 0);
        Object meteoricIronPlate = new ItemStack(GCItems.itemBasicMoon, 1, 1);
    	Object deshIngot = GalacticraftCore.isPlanetsLoaded ? new ItemStack(MarsItems.marsItemBasic, 1, 2) : GCItems.heavyPlatingTier1;
    	
    	FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCBlocks.basicBlock, 1, 5), new ItemStack(GCItems.basicItem, 1, 3), 0.5F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCBlocks.basicBlock, 1, 6), new ItemStack(GCItems.basicItem, 1, 4), 0.5F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCBlocks.basicBlock, 1, 7), new ItemStack(GCItems.basicItem, 1, 5), 0.5F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCItems.meteorChunk, 1, 0), new ItemStack(GCItems.meteorChunk, 1, 1), 0.1F);
        FurnaceRecipes.instance().addSmelting(GCItems.meteoricIronRaw, new ItemStack(GCItems.itemBasicMoon), 1.0F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCBlocks.blockMoon, 1, 0), new ItemStack(GCItems.basicItem, 1, 3), 1.0F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCBlocks.blockMoon, 1, 1), new ItemStack(GCItems.basicItem, 1, 4), 1.0F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCBlocks.blockMoon, 1, 2), new ItemStack(GCItems.cheeseCurd), 1.0F);
        //Recycling: smelt tin/copper canisters back into ingots
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCItems.canister, 1, 0), new ItemStack(GCItems.basicItem, 3, 4), 1.0F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCItems.canister, 1, 1), new ItemStack(GCItems.basicItem, 3, 3), 1.0F);
        if (CompatibilityManager.useAluDust())
        {
            FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCItems.ic2compat, 1, 0), new ItemStack(GCItems.basicItem, 1, 5), 1.0F);
        }
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCItems.foodItem, 1, 6), new ItemStack(GCItems.foodItem, 1, 7), 1.0F);

        RecipeUtil.addRecipeUpdatable(new ItemStack(GCItems.rocketEngine, 1, 1), new Object[] { "ZYZ", "ZWZ", "XVX", 'V', GCItems.oxygenVent, 'W', new ItemStack(GCItems.fuelCanister, 1, 1), 'X', GCItems.heavyPlatingTier1, 'Y', new ItemStack(Blocks.wool, 1, 4), 'Z', meteoricIronPlate });

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

        aluminumIngots.addAll(OreDictionary.getOres("ingotAluminum"));
        ArrayList<ItemStack> addedList = new ArrayList<ItemStack>();
        for (ItemStack ingotNew : OreDictionary.getOres("ingotAluminium"))
        {
            boolean flag = false;
            for (ItemStack ingotDone : aluminumIngots)
            {
                if (ItemStack.areItemStacksEqual(ingotNew, ingotDone))
                {
                    flag = true;
                    break;
                }
            }
            if (!flag)
            {
                addedList.add(ingotNew);
                OreDictionary.registerOre("ingotAluminum", ingotNew);
            }
        }
        if (addedList.size() > 0)
        {
            aluminumIngots.addAll(addedList);
            addedList.clear();
        }
        for (ItemStack ingotNew : OreDictionary.getOres("ingotNaturalAluminum"))
        {
            for (ItemStack ingotDone : aluminumIngots)
            {
                if (!ItemStack.areItemStacksEqual(ingotNew, ingotDone))
                {
                    addedList.add(ingotNew);
                }
            }
        }
        if (addedList.size() > 0)
        {
            aluminumIngots.addAll(addedList);
        }

        final HashMap<Object, Integer> spaceStationRequirements = new HashMap<Object, Integer>(4, 1.0F);
        spaceStationRequirements.put("ingotTin", 32);
        spaceStationRequirements.put(aluminumIngots, 16);
        spaceStationRequirements.put(new ItemStack(GCItems.basicItem, 1, ItemBasic.WAFER_ADVANCED), 1);
        spaceStationRequirements.put(Items.iron_ingot, 24);
        GalacticraftRegistry.registerSpaceStation(new SpaceStationType(ConfigManagerCore.idDimensionOverworldOrbit, ConfigManagerCore.idDimensionOverworld, new SpaceStationRecipe(spaceStationRequirements)));

        RecipeUtil.addRecipeUpdatable(new ItemStack(GCBlocks.aluminumWire, 6), new Object[] { "WWW", "CCC", "WWW", 'W', Blocks.wool, 'C', "ingotAluminum" });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.aluminumWire, 1, 1), new Object[] { "X", "Y", "Z", 'X', Blocks.wool, 'Y', new ItemStack(GCBlocks.aluminumWire, 1), 'Z', "ingotAluminum" });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.aluminumWire, 1, 1), new Object[] { "Z", "Y", "X", 'X', Blocks.wool, 'Y', new ItemStack(GCBlocks.aluminumWire, 1), 'Z', "ingotAluminum" });

        RecipeUtil.addShapelessRecipe(new ItemStack(GCBlocks.aluminumWire, 1, 2), new ItemStack(GCBlocks.aluminumWire, 1), Items.repeater );

        RecipeUtil.addShapelessRecipe(new ItemStack(GCBlocks.aluminumWire, 1, 3), new ItemStack(GCBlocks.aluminumWire, 1, 1), Items.repeater );

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.machineBase, 1, 0), new Object[] { "WWW", "XZX", "XYX", 'W', "ingotCopper", 'X', Items.iron_ingot, 'Y', new ItemStack(GCBlocks.aluminumWire, 1, 0), 'Z', Blocks.furnace });
        //Energy Storage Module:
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.machineTiered, 1, 0), new Object[] { "SSS", "BBB", "SSS", 'B', new ItemStack(GCItems.battery, 1, GCItems.battery.getMaxDamage()), 'S', "compressedSteel" });
        //Electric Furnace:
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.machineTiered, 1, 4), new Object[] { "XXX", "XZX", "WYW", 'W', "compressedAluminum", 'X', "compressedSteel", 'Y', "waferBasic", 'Z', Blocks.furnace });
        //Energy Storage Cluster:
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.machineTiered, 1, 8), new Object[] { "BSB", "SWS", "BSB", 'B', new ItemStack(GCBlocks.machineTiered, 1, 0), 'S', "compressedSteel", 'W', "waferAdvanced" });
        //Electric Arc Furnace:
        RecipeUtil.addRecipeUpdatable(new ItemStack(GCBlocks.machineTiered, 1, 12), new Object[] { "XXX", "XZX", "WYW", 'W', meteoricIronIngot, 'X', new ItemStack(GCItems.heavyPlatingTier1), 'Y', "waferAdvanced", 'Z', new ItemStack(GCBlocks.machineTiered, 1, 4) });
        //Ingot Compressor
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.machineBase, 1, 12), new Object[] { "WXW", "WYW", "WZW", 'W', "ingotAluminum", 'X', Blocks.anvil, 'Y', "ingotCopper", 'Z', "waferBasic" });
        //Electric Compressor - 2 recipes
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.machineBase2, 1, 0), new Object[] { "WXW", "WYW", "VZV", 'V', new ItemStack(GCBlocks.aluminumWire), 'W', "compressedSteel", 'X', Blocks.anvil, 'Y', "compressedBronze", 'Z', "waferAdvanced" });
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.machineBase2, 1, 0), new Object[] { "WXW", "WYW", "VZV", 'V', new ItemStack(GCBlocks.aluminumWire), 'W', "compressedSteel", 'X', "compressedTin", 'Y', new ItemStack(GCBlocks.machineBase, 1, 12), 'Z', "waferAdvanced" });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.machineBase2, 1, 4), new Object[] { "WXW", "UYU", "VZV", 'U', Blocks.stone_button, 'V', new ItemStack(GCBlocks.aluminumWire), 'W', "ingotAluminum", 'X', Blocks.lever, 'Y', Blocks.furnace, 'Z', Blocks.redstone_torch });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.machineBase2, 1, 8), new Object[] { "SSS", "BBB", "SSS", 'B', new ItemStack(GCItems.oxTankHeavy, 1, GCItems.oxTankHeavy.getMaxDamage()), 'S', "compressedSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.machineBase2, 1, 12), new Object[] { "WCW", "VAV", "WBW", 'V', new ItemStack(GCBlocks.aluminumWire), 'W', "compressedSteel", 'A', Blocks.anvil, 'B', Blocks.furnace, 'C', Items.shears });

        RecipeUtil.addRecipeUpdatable(new ItemStack(GCItems.battery, 2, 100), new Object[] { " T ", "TRT", "TCT", 'T', "compressedTin", 'R', "dustRedstone", 'C', Items.coal });

        RecipeUtil.addRecipe(new ItemStack(GCItems.rocketEngine, 1), new Object[] { " YV", "XWX", "XZX", 'V', Blocks.stone_button, 'W', new ItemStack(GCItems.canister, 1, OreDictionary.WILDCARD_VALUE), 'X', GCItems.heavyPlatingTier1, 'Y', Items.flint_and_steel, 'Z', GCItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCItems.rocketEngine, 1), new Object[] { "VY ", "XWX", "XZX", 'V', Blocks.stone_button, 'W', new ItemStack(GCItems.canister, 1, OreDictionary.WILDCARD_VALUE), 'X', GCItems.heavyPlatingTier1, 'Y', Items.flint_and_steel, 'Z', GCItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCItems.partNoseCone, 1), new Object[] { " Y ", " X ", "X X", 'X', GCItems.heavyPlatingTier1, 'Y', Blocks.redstone_torch });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.oxygenPipe, 6), new Object[] { "XXX", "   ", "XXX", 'X', Blocks.glass_pane });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.fluidTank, 1), new Object[] { " X ", "X X", "XXX", 'X', Blocks.glass_pane });
        
        RecipeUtil.addRecipe(new ItemStack(GCItems.oxTankLight, 1, GCItems.oxTankLight.getMaxDamage()), new Object[] { "Z", "X", "Y", 'X', new ItemStack(GCItems.canister, 1, 0), 'Y', "compressedCopper", 'Z', new ItemStack(Blocks.wool, 1, 5) });

        RecipeUtil.addRecipe(new ItemStack(GCItems.oxTankMedium, 1, GCItems.oxTankMedium.getMaxDamage()), new Object[] { "ZZ", "XX", "YY", 'X', new ItemStack(GCItems.canister, 1, 0), 'Y', "compressedTin", 'Z', new ItemStack(Blocks.wool, 1, 1) });

        RecipeUtil.addRecipeUpdatable(new ItemStack(GCItems.sensorGlasses, 1), new Object[] { "ZWZ", "Z Z", "XYX", 'W', "gemDiamond", 'X', GCItems.sensorLens, 'Y', meteoricIronIngot, 'Z', Items.string });

        RecipeUtil.addRecipeUpdatable(new ItemStack(GCItems.sensorLens, 1), new Object[] { "ZXZ", "XYX", "ZXZ", 'X', Blocks.glass_pane, 'Y', meteoricIronPlate, 'Z', "dustRedstone" });

        if (!ConfigManagerCore.alternateCanisterRecipe)
        {
            RecipeUtil.addRecipe(new ItemStack(GCItems.canister, 2, 0), new Object[] { "X X", "X X", "XXX", 'X', "ingotTin" });
            RecipeUtil.addRecipe(new ItemStack(GCItems.canister, 2, 1), new Object[] { "X X", "X X", "XXX", 'X', "ingotCopper" });
        }
        else
        {
            RecipeUtil.addRecipe(new ItemStack(GCItems.canister, 2, 0), new Object[] { "XXX", "X  ", "XXX", 'X', "ingotTin" });
            RecipeUtil.addRecipe(new ItemStack(GCItems.canister, 2, 1), new Object[] { "XXX", "X  ", "XXX", 'X', "ingotCopper" });
        }

        RecipeUtil.addRecipe(new ItemStack(GCItems.oxMask, 1), new Object[] { "XXX", "XYX", "XXX", 'X', Blocks.glass_pane, 'Y', Items.iron_helmet });

        RecipeUtil.addRecipe(new ItemStack(GCItems.canvas, 1), new Object[] { " XY", "XXX", "YX ", 'Y', "stickWood", 'X', Items.string });

        RecipeUtil.addRecipe(new ItemStack(GCItems.parachute, 1, 0), new Object[] { "XXX", "Y Y", " Y ", 'X', GCItems.canvas, 'Y', Items.string });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, 1), new Object[] { "XYX", 'Y', GCBlocks.oxygenPipe, 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, 14), new Object[] { "XYX", 'Y', new ItemStack(GCBlocks.aluminumWire, 1, 0), 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, 15), new Object[] { "XYX", 'Y', new ItemStack(GCBlocks.aluminumWire, 1, 1), 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });

        RecipeUtil.addRecipe(new ItemStack(GCItems.oxygenGear), new Object[] { " Y ", "YXY", "Y Y", 'X', GCItems.oxygenConcentrator, 'Y', GCBlocks.oxygenPipe });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.basicBlock, 4, 3), new Object[] { "XX ", "XXY", "   ", 'X', "stone", 'Y', "compressedTin" });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.basicBlock, 4, 4), new Object[] { "XX ", "XX ", " Y ", 'X', "stone", 'Y', "compressedTin" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.flag), new Object[] { "XYY", "XYY", "X  ", 'X', GCItems.flagPole, 'Y', GCItems.canvas });

        for (int var2 = 0; var2 < 16; ++var2)
        {
            RecipeUtil.addShapelessRecipe(new ItemStack(GCItems.parachute, 1, ItemParaChute.getParachuteDamageValueFromDye(var2)), new Object[] { new ItemStack(Items.dye, 1, var2), new ItemStack(GCItems.parachute, 1, 0) });
        }

        RecipeUtil.addRecipe(new ItemStack(GCItems.partFins, 1), new Object[] { " Y ", "XYX", "X X", 'X', GCItems.heavyPlatingTier1, 'Y', "compressedSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.landingPad, 9, 0), new Object[] { "YYY", "XXX", 'X', "blockIron", 'Y', "compressedIron" });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.landingPad, 9, 1), new Object[] { "YYY", "XXX", 'X', "blockIron", 'Y', "compressedSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.partBuggy, 1, 0), new Object[] { " W ", "WXW", " W ", 'W', Items.leather, 'X', "compressedSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.partBuggy, 1, 1), new Object[] { "  Y", " ZY", "XXX", 'X', "compressedSteel", 'Y', "compressedSteel", 'Z', "compressedIron" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.partBuggy, 1, 2), new Object[] { "XXX", "YZY", "XXX", 'X', "compressedSteel", 'Y', "compressedIron", 'Z', Blocks.chest });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.oxygenDetector, 1), new Object[] { "WWW", "YVY", "ZUZ", 'U', "compressedAluminum", 'V', "waferBasic", 'W', "compressedSteel", 'X', GCItems.oxygenFan, 'Y', GCItems.oxygenVent, 'Z', "dustRedstone" });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.oxygenDistributor, 1), new Object[] { "WXW", "YZY", "WXW", 'W', "compressedSteel", 'X', GCItems.oxygenFan, 'Y', GCItems.oxygenVent, 'Z', "compressedAluminum" });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.oxygenSealer, 1), new Object[] { "UZU", "YXY", "UZU", 'U', "compressedAluminum", 'V', GCBlocks.aluminumWire, 'W', "compressedSteel", 'X', GCItems.oxygenFan, 'Y', GCItems.oxygenVent, 'Z', "compressedSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.oxygenCollector, 1), new Object[] { "WWW", "YXZ", "UVU", 'U', "compressedAluminum", 'V', GCItems.oxygenConcentrator, 'W', "compressedSteel", 'X', new ItemStack(GCItems.canister, 1, 0), 'Y', GCItems.oxygenFan, 'Z', GCItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.nasaWorkbench, 1), new Object[] { "XZX", "UWU", "YVY", 'U', Blocks.lever, 'V', Blocks.redstone_torch, 'W', "waferAdvanced", 'X', "compressedSteel", 'Y', "compressedSteel", 'Z', Blocks.crafting_table });

        RecipeUtil.addRecipe(new ItemStack(GCItems.oxTankHeavy, 1, GCItems.oxTankHeavy.getMaxDamage()), new Object[] { "ZZZ", "XXX", "YYY", 'X', new ItemStack(GCItems.canister, 1, 0), 'Y', "compressedSteel", 'Z', new ItemStack(Blocks.wool, 1, 14) });

        RecipeUtil.addRecipe(new ItemStack(GCItems.oxygenFan, 1), new Object[] { "Z Z", " Y ", "ZXZ", 'X', Items.redstone, 'Y', "waferBasic", 'Z', "compressedSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.oxygenConcentrator, 1), new Object[] { "ZWZ", "WYW", "ZXZ", 'W', "compressedTin", 'X', GCItems.oxygenVent, 'Y', new ItemStack(GCItems.canister, 1, 0), 'Z', "compressedSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.steelPickaxe, 1), new Object[] { "YYY", " X ", " X ", 'Y', "compressedSteel", 'X', "stickWood" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.steelAxe, 1), new Object[] { "YY ", "YX ", " X ", 'Y', "compressedSteel", 'X', "stickWood" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.steelAxe, 1), new Object[] { " YY", " XY", " X ", 'Y', "compressedSteel", 'X', "stickWood" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.steelHoe, 1), new Object[] { " YY", " X ", " X ", 'Y', "compressedSteel", 'X', "stickWood" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.steelHoe, 1), new Object[] { "YY ", " X ", " X ", 'Y', "compressedSteel", 'X', "stickWood" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.steelSpade, 1), new Object[] { " Y ", " X ", " X ", 'Y', "compressedSteel", 'X', "stickWood" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.steelSword, 1), new Object[] { " Y ", " Y ", " X ", 'Y', "compressedSteel", 'X', "stickWood" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.steelBoots, 1), new Object[] { "X X", "X X", 'X', "compressedSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.steelChestplate, 1), new Object[] { "X X", "XXX", "XXX", 'X', "compressedSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.steelLeggings, 1), new Object[] { "XXX", "X X", "X X", 'X', "compressedSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.steelHelmet, 1), new Object[] { "XXX", "X X", 'X', "compressedSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.flagPole, 2, 0), new Object[] { "X", "X", "X", 'X', "compressedSteel" });

        RecipeUtil.addShapelessOreRecipe(new ItemStack(GCItems.oxygenVent, 1), new Object[] { "compressedTin", "compressedTin", "compressedTin", "compressedSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.airLockFrame, 4, 0), new Object[] { "XXX", "YZY", "XXX", 'X', "compressedAluminum", 'Y', "compressedSteel", 'Z', GCItems.oxygenConcentrator });

        RecipeUtil.addRecipeUpdatable(new ItemStack(GCBlocks.airLockFrame, 1, 1), new Object[] { "YYY", "WZW", "YYY", 'W', meteoricIronPlate, 'Y', "compressedSteel", 'Z', new ItemStack(GCItems.basicItem, 1, 13) });

        // Disable oil extractor:
        // RecipeUtil.addRecipe(new ItemStack(GCItems.oilExtractor), new Object[] { "X  ", " XY", "ZYY", 'X', "compressedSteel", 'Y', "compressedBronze", 'Z', Items.redstone });

        RecipeUtil.addRecipe(new ItemStack(GCItems.basicItem, 1, 20), new Object[] { "WVW", "YXY", "YZY", 'X', "compressedSteel", 'Y', "compressedBronze", 'Z', "waferBasic", 'W', "dustRedstone", 'V', GCItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(GCItems.oilCanister, 1, GCItems.oilCanister.getMaxDamage()), new Object[] { "WXW", "WYW", "WZW", 'X', "compressedSteel", 'Y', Blocks.glass, 'Z', new ItemStack(GCItems.canister, 1, 0), 'W', "compressedTin" });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.refinery), new Object[] { " Z ", "WZW", "XYX", 'X', "compressedSteel", 'Y', Blocks.furnace, 'Z', new ItemStack(GCItems.canister, 1, 1), 'W', "stone" });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.oxygenCompressor, 1, 0), new Object[] { "XWX", "WZW", "XYX", 'W', "compressedAluminum", 'X', "compressedSteel", 'Y', "compressedBronze", 'Z', GCItems.oxygenConcentrator });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.oxygenCompressor, 1, 4), new Object[] { "XVX", "WZW", "XYX", 'V', GCItems.oxygenFan, 'W', "compressedAluminum", 'X', "compressedSteel", 'Y', Blocks.redstone_torch, 'Z', GCItems.oxygenConcentrator });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.fuelLoader), new Object[] { "XXX", "XZX", "WYW", 'W', "compressedTin", 'X', "compressedCopper", 'Y', "waferBasic", 'Z', new ItemStack(GCItems.canister, 1, 0) });

        RecipeUtil.addRecipe(new ItemStack(GCItems.basicItem, 2, 0), new Object[] { "XXX", "YYY", "ZZZ", 'X', Blocks.glass, 'Y', "waferSolar", 'Z', new ItemStack(GCBlocks.aluminumWire, 1, 0) });

        RecipeUtil.addRecipe(new ItemStack(GCItems.basicItem, 1, 1), new Object[] { "XXX", "YYY", "XXX", 'X', new ItemStack(GCItems.basicItem, 1, 0), 'Y', new ItemStack(GCBlocks.aluminumWire, 1, 0) });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.solarPanel, 1, 0), new Object[] { "XYX", "XZX", "VWV", 'V', new ItemStack(GCBlocks.aluminumWire, 1, 0), 'W', "waferBasic", 'X', "compressedSteel", 'Y', new ItemStack(GCItems.basicItem, 1, 1), 'Z', GCItems.flagPole });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.solarPanel, 1, 4), new Object[] { "XYX", "XZX", "VWV", 'V', new ItemStack(GCBlocks.aluminumWire, 1, 1), 'W', "waferAdvanced", 'X', "compressedSteel", 'Y', new ItemStack(GCItems.basicItem, 1, 1), 'Z', GCItems.flagPole });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.cargoLoader, 1, 0), new Object[] { "XWX", "YZY", "XXX", 'W', Blocks.hopper, 'X', "compressedSteel", 'Y', "compressedAluminum", 'Z', Blocks.chest });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.cargoLoader, 1, 4), new Object[] { "XXX", "YZY", "XWX", 'W', Blocks.hopper, 'X', "compressedSteel", 'Y', "compressedAluminum", 'Z', Blocks.chest });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.glowstoneTorch, 4), new Object[] { "Y", "X", 'X', "stickWood", 'Y', "dustGlowstone" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.basicItem, 1, 19), new Object[] { " X ", "YUY", "ZWZ", 'U', Items.repeater, 'W', "waferBasic", 'X', "compressedAluminum", 'Y', "compressedIron", 'Z', "dustRedstone" });

        RecipeUtil.addRecipe(new ItemStack(GCItems.wrench), new Object[] { "  Y", " X ", "X  ", 'X', "compressedBronze", 'Y', "compressedSteel" });

        RecipeUtil.addRecipe(new ItemStack(Blocks.lit_pumpkin), new Object[] { "P  ", "T  ", "   ", 'P', new ItemStack(Blocks.pumpkin), 'T', new ItemStack(GCBlocks.unlitTorch) });

        RecipeUtil.addRecipeUpdatable(new ItemStack(GCBlocks.brightLamp), new Object[] { "XYX", "YZY", "XYX", 'X', deshIngot, 'Y', Items.glowstone_dust, 'Z', new ItemStack(GCItems.battery, 1, 0) });

        RecipeUtil.addRecipeUpdatable(new ItemStack(GCBlocks.spinThruster), new Object[] { "   ", "YWZ", "PXP", 'W', "waferAdvanced", 'X', meteoricIronIngot, 'Y', new ItemStack(GCItems.fuelCanister, 1, 1), 'Z', new ItemStack(GCItems.rocketEngine, 1, 0), 'P', "compressedSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.screen), new Object[] { "XYX", "YGY", "XYX", 'X', "compressedSteel", 'Y', "waferBasic", 'G', Blocks.glass });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.telemetry), new Object[] { "XFX", "XWX", "YYY", 'W', "waferBasic", 'X', "compressedTin", 'Y', "compressedCopper", 'F', new ItemStack(GCItems.basicItem, 1, 19) });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.radioTelescope), new Object[] { "XFX", " X ", "WYW", 'X', "compressedAluminum", 'Y', "blockIron", 'W', "waferAdvanced", 'F', new ItemStack(GCItems.basicItem, 1, 19) });
        
        RecipeUtil.addBlockRecipe(new ItemStack(GCBlocks.basicBlock, 1, 9), "ingotCopper", new ItemStack(GCItems.basicItem, 1, 3));

        RecipeUtil.addBlockRecipe(new ItemStack(GCBlocks.basicBlock, 1, 10), "ingotTin", new ItemStack(GCItems.basicItem, 1, 4));

        RecipeUtil.addBlockRecipe(new ItemStack(GCBlocks.basicBlock, 1, 11), "ingotAluminum", new ItemStack(GCItems.basicItem, 1, 5));

        RecipeUtil.addBlockRecipe(new ItemStack(GCBlocks.basicBlock, 1, 13), ConfigManagerCore.otherModsSilicon, new ItemStack(GCItems.basicItem, 1, 2));
	
        RecipeUtil.addRecipeUpdatable(new ItemStack(GCBlocks.basicBlock, 1, 12), new Object[] { "YYY", "YYY", "YYY", 'Y', meteoricIronIngot });
	
        RecipeUtil.addRecipe(new ItemStack(GCItems.basicItem, 9, 3), new Object[] { "X", 'X', new ItemStack(GCBlocks.basicBlock, 1, 9) });

        RecipeUtil.addRecipe(new ItemStack(GCItems.basicItem, 9, 4), new Object[] { "X", 'X', new ItemStack(GCBlocks.basicBlock, 1, 10) });

        RecipeUtil.addRecipe(new ItemStack(GCItems.basicItem, 9, 5), new Object[] { "X", 'X', new ItemStack(GCBlocks.basicBlock, 1, 11) });

        RecipeUtil.addRecipe(new ItemStack(GCItems.basicItem, 9, 2), new Object[] { "X", 'X', new ItemStack(GCBlocks.basicBlock, 1, 13) });
	
        RecipeUtil.addRecipe(new ItemStack(GCItems.itemBasicMoon, 9, 0), new Object[] { "X", 'X', new ItemStack(GCBlocks.basicBlock, 1, 12) });
	
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.cheeseBlock, 1), new Object[] { "YYY", "YXY", "YYY", 'X', Items.milk_bucket, 'Y', GCItems.cheeseCurd });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.spaceGlassClear, 5), new Object[] { "YXY", "XXX", "YXY", 'X', Blocks.glass, 'Y', "ingotAluminum" });
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.spaceGlassVanilla, 5), new Object[] { "YXY", "XXX", "YXY", 'X', Blocks.glass, 'Y', "ingotTin" });
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.spaceGlassStrong, 5), new Object[] { "YXY", "XXX", "YXY", 'X', Blocks.glass, 'Y', "compressedAluminum" });  //https://en.wikipedia.org/wiki/List_of_Star_Trek_materials#Transparent_aluminum
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.spaceGlassClear, 5, 1), new Object[] { "YXY", "XXX", "YXY", 'X', GCBlocks.spaceGlassClear, 'Y', new ItemStack(GCBlocks.basicBlock, 1, 4)  });
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.spaceGlassVanilla, 5, 1), new Object[] { "YXY", "XXX", "YXY", 'X', GCBlocks.spaceGlassVanilla, 'Y', new ItemStack(GCBlocks.basicBlock, 1, 4)   });
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.spaceGlassStrong, 5, 1), new Object[] { "YXY", "XXX", "YXY", 'X', GCBlocks.spaceGlassStrong, 'Y', new ItemStack(GCBlocks.basicBlock, 1, 4)   });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.panelLighting, 1, 0), new Object[] { "XXX", "XYX", "XZX", 'X', Blocks.glass_pane, 'Y', GCBlocks.glowstoneTorch, 'Z', "compressedSteel" });
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.panelLighting, 1, 1), new Object[] { "X X", " Y ", "XZX", 'X', Blocks.glass_pane, 'Y', GCBlocks.glowstoneTorch, 'Z', "compressedSteel" });
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.panelLighting, 1, 2), new Object[] { "X X", "XYX", "XZX", 'X', Blocks.glass_pane, 'Y', GCBlocks.glowstoneTorch, 'Z', "compressedSteel" });
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.panelLighting, 1, 3), new Object[] { "   ", "XYX", " Z ", 'X', Blocks.glass_pane, 'Y', GCBlocks.glowstoneTorch, 'Z', "compressedSteel" });
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.panelLighting, 1, 4), new Object[] { " X ", "XY ", " Z ", 'X', Blocks.glass_pane, 'Y', GCBlocks.glowstoneTorch, 'Z', "compressedSteel" });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.platform, 4, 0), new Object[] { "PAP", "BXB", "PAP", 'X', "waferBasic", 'A', "dustGlowstone", 'B', "compressedSteel", 'P', Blocks.piston });

        RecipeUtil.addRecipe(new ItemStack(GCBlocks.machineBase3, 1, BlockMachine3.PAINTER_METADATA), new Object[] { "ABC", "DEF", "GHI", 'A', "dyeRed", 'B', "dyeMagenta", 'C', "dyeBlue", 'D', "dyeOrange", 'E', "compressedSteel", 'F', "dyeCyan", 'G', "dyeYellow", 'H', "dyeLime", 'I', "dyeGreen" });
        RecipeUtil.addShapelessOreRecipe(new ItemStack(GCBlocks.crafting, 1), new Object[] { Blocks.crafting_table, "compressedIron" });

        // Furnace on moon
        RecipeUtil.addRecipe(new ItemStack(Blocks.furnace), new Object[] { "XXX", "X X", "XXX", 'X', new ItemStack(GCBlocks.blockMoon, 1, 4) });

//		// Tin Stairs 1
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.tinStairs1, 4), new Object[] { "  X", " XX", "XXX", 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.tinStairs1, 4), new Object[] { "X  ", "XX ", "XXX", 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
//
//		// Tin Stairs 2
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.tinStairs2, 4), new Object[] { "  X", " XX", "XXX", 'X', new ItemStack(GCBlocks.basicBlock, 1, 3) });
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.tinStairs2, 4), new Object[] { "X  ", "XX ", "XXX", 'X', new ItemStack(GCBlocks.basicBlock, 1, 3) });
//
//		// Moon Stone Stairs
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.moonStoneStairs, 4), new Object[] { "  X", " XX", "XXX", 'X', new ItemStack(GCBlocks.blockMoon, 1, 4) });
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.moonStoneStairs, 4), new Object[] { "X  ", "XX ", "XXX", 'X', new ItemStack(GCBlocks.blockMoon, 1, 4) });
//
//		// Moon Dungeon Brick Stairs
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.moonBricksStairs, 4), new Object[] { "  X", " XX", "XXX", 'X', new ItemStack(GCBlocks.blockMoon, 1, 14) });
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.moonBricksStairs, 4), new Object[] { "X  ", "XX ", "XXX", 'X', new ItemStack(GCBlocks.blockMoon, 1, 14) });
//
//		// Slab Block
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.slabGCHalf, 6, 0), new Object[] { "XXX", 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.slabGCHalf, 6, 1), new Object[] { "XXX", 'X', new ItemStack(GCBlocks.basicBlock, 1, 3) });
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.slabGCHalf, 6, 2), new Object[] { "XXX", 'X', new ItemStack(GCBlocks.blockMoon, 1, 4) });
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.slabGCHalf, 6, 3), new Object[] { "XXX", 'X', new ItemStack(GCBlocks.blockMoon, 1, 14) });
//
//		// Wall Block
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.wallGC, 6, 0), new Object[] { "XXX", "XXX", 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.wallGC, 6, 1), new Object[] { "XXX", "XXX", 'X', new ItemStack(GCBlocks.basicBlock, 1, 3) });
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.wallGC, 6, 2), new Object[] { "XXX", "XXX", 'X', new ItemStack(GCBlocks.blockMoon, 1, 4) });
//
//		// Dungeon Brick Wall
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.wallGC, 6, 3), new Object[] { "XXX", "XXX", 'X', new ItemStack(GCBlocks.blockMoon, 1, 14) });

		// Concealed redstone
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.concealedRedstone, 4, 0), new Object[] { " X ", "XYX", " X ", 'X', new ItemStack(GCBlocks.basicBlock, 1, 4), 'Y', "dustRedstone" });
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.concealedRepeater_Unpowered, 1, 0), new Object[] { "   ", "XYX", "   ", 'X', new ItemStack(GCBlocks.basicBlock, 1, 4), 'Y', Items.repeater });
		
        // Food
        RecipeUtil.addShapelessOreRecipe(new ItemStack(GCItems.foodItem, 1, 0), new Object[] { new ItemStack(GCItems.canister, 1, 0), Items.apple, Items.apple });

        RecipeUtil.addShapelessOreRecipe(new ItemStack(GCItems.foodItem, 1, 1), new Object[] { new ItemStack(GCItems.canister, 1, 0), Items.carrot, Items.carrot });

        RecipeUtil.addShapelessOreRecipe(new ItemStack(GCItems.foodItem, 1, 2), new Object[] { new ItemStack(GCItems.canister, 1, 0), Items.melon, Items.melon });

        RecipeUtil.addShapelessOreRecipe(new ItemStack(GCItems.foodItem, 1, 3), new Object[] { new ItemStack(GCItems.canister, 1, 0), Items.potato, Items.potato });

        RecipeUtil.addShapelessOreRecipe(new ItemStack(GCItems.foodItem, 6, 4), new Object[] { new ItemStack(GCBlocks.cheeseBlock) });
        RecipeUtil.addShapelessOreRecipe(new ItemStack(GCItems.foodItem, 2, 5), new Object[] { Items.wheat, Items.wheat, Items.egg, Items.milk_bucket });
        RecipeUtil.addShapelessOreRecipe(new ItemStack(GCItems.foodItem, 2, 6), new Object[] { Items.beef });
        RecipeUtil.addShapelessOreRecipe(new ItemStack(GCItems.foodItem, 1, 8), new Object[] { new ItemStack(GCItems.foodItem, 1, 4), new ItemStack(GCItems.foodItem, 1, 7), new ItemStack(GCItems.foodItem, 1, 5) });
        RecipeUtil.addShapelessOreRecipe(new ItemStack(GCItems.foodItem, 1, 9), new Object[] { new ItemStack(GCItems.canister, 1, 0), new ItemStack(GCItems.foodItem, 1, 6), new ItemStack(GCItems.foodItem, 1, 6) });
        
        //EmergencyKit
        RecipeUtil.addRecipe(new ItemStack(GCItems.emergencyKit), ItemEmergencyKit.getRecipe());
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.emergencyBox), new Object[] { "XYX", "Y Y", "XYX", 'X', GCBlocks.glowstoneTorch, 'Y', new ItemStack(GCItems.basicItem, 1, 7) });
        RecipeUtil.addShapelessOreRecipe(new ItemStack(GCBlocks.emergencyBox, 1, 1), new ItemStack(GCBlocks.emergencyBox, 1, 0), new ItemStack(GCItems.emergencyKit) );

        RecipeUtil.addShapelessOreRecipe(new ItemStack(GCBlocks.grating, 4, 0), new Object[] { Blocks.iron_bars, Blocks.iron_bars, Blocks.iron_bars, Blocks.iron_bars } );
        
        RecipeUtil.addShapelessOreRecipe(new ItemStack(GCItems.meteorChunk, 3), new Object[] { GCItems.meteoricIronRaw });

        CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 6), "ingotCopper", "ingotCopper");
        CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 7), "ingotTin", "ingotTin");
        CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 8), "ingotAluminum", "ingotAluminum");

/*        // Support for all the spellings of Aluminum
        for (ItemStack stack : aluminumIngots)
        {
          	CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 8), stack, stack);
        }
*/
        if (OreDictionary.getOres("ingotBronze").size() > 0)
        {
            CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 10), "ingotBronze", "ingotBronze");
        }
        if (OreDictionary.getOres("ingotSteel").size() > 0)
        {
            CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 9), "ingotSteel", "ingotSteel");
            CompressorRecipes.steelIngotsPresent = true; 
        }
        CompressorRecipes.steelRecipeGC = Arrays.asList(new ItemStack(Items.coal), new ItemStack(GCItems.basicItem, 1, 11), new ItemStack(Items.coal));
        CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 9), Items.coal, new ItemStack(GCItems.basicItem, 1, 11), Items.coal);
        CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 10), new ItemStack(GCItems.basicItem, 1, 6), new ItemStack(GCItems.basicItem, 1, 7));
        CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 11), Items.iron_ingot, Items.iron_ingot);
        CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.itemBasicMoon, 1, 1), meteoricIronIngot);
        CompressorRecipes.addRecipe(new ItemStack(GCItems.heavyPlatingTier1, 2, 0), "XYZ", "XYZ", 'X', new ItemStack(GCItems.basicItem, 1, 9), 'Y', new ItemStack(GCItems.basicItem, 1, 8), 'Z', new ItemStack(GCItems.basicItem, 1, 10));

        ItemStack solarPanels = new ItemStack(GCItems.basicItem, 9, 12);
        ItemStack basicWafers = new ItemStack(GCItems.basicItem, 3, 13);
        ItemStack advancedWafers = new ItemStack(GCItems.basicItem, 1, 14);
        ItemStack silicon =  new ItemStack(GCItems.basicItem, 1, 2);
        CircuitFabricatorRecipes.addRecipe(solarPanels, new ItemStack[] { new ItemStack(Items.diamond), silicon, silicon, new ItemStack(Items.redstone), new ItemStack(Items.dye, 1, 4) });
        CircuitFabricatorRecipes.addRecipe(basicWafers, new ItemStack[] { new ItemStack(Items.diamond), silicon, silicon, new ItemStack(Items.redstone), new ItemStack(Blocks.redstone_torch) });
        CircuitFabricatorRecipes.addRecipe(advancedWafers, new ItemStack[] { new ItemStack(Items.diamond), silicon, silicon, new ItemStack(Items.redstone), new ItemStack(Items.repeater) });

        RecipeUtil.addShapelessOreRecipe(new ItemStack(GCItems.prelaunchChecklist), new Object[] { new ItemStack(Items.dye, 1, 1), GCItems.canvas });
    }

    public static void setConfigurableRecipes()
    {
        if (GCCoreUtil.getEffectiveSide() == Side.CLIENT && CompatibilityManager.modJEILoaded) TickHandlerClient.updateJEIhiding = true;

        // Update Aluminium Wire and Battery crafting recipes and GC Advanced Metals recipes
        ItemStack aluWire = new ItemStack(GCBlocks.aluminumWire, ConfigManagerCore.quickMode ? 9 : 6, 0);
        ItemStack battery = new ItemStack(GCItems.battery, ConfigManagerCore.quickMode ? 3 : 2, 100);
        boolean doQuickMode = ConfigManagerCore.quickMode != configSaved_QuickMode;
        configSaved_QuickMode = ConfigManagerCore.quickMode;

        ItemStack meteoricIronIngot = new ItemStack(GCItems.itemBasicMoon, 1, 0);
        ItemStack meteoricIronPlate = new ItemStack(GCItems.itemBasicMoon, 1, 1);
        boolean doMetalsToOreDict = configSaved_RequireGCmetals && !ConfigManagerCore.recipesRequireGCAdvancedMetals;
        boolean doMetalsToGC = !configSaved_RequireGCmetals && ConfigManagerCore.recipesRequireGCAdvancedMetals;
        configSaved_RequireGCmetals = ConfigManagerCore.recipesRequireGCAdvancedMetals; 

        boolean doSilicon = !ConfigManagerCore.otherModsSilicon.equals(configSaved_Silicon);
        configSaved_Silicon = ConfigManagerCore.otherModsSilicon;

        if (doQuickMode || doMetalsToOreDict || doMetalsToGC)
        {
            List<IRecipe> standardRecipes = CraftingManager.getInstance().getRecipeList();
            for (IRecipe recipe : standardRecipes)
            {
                if (recipe instanceof IRecipeUpdatable)
                {
                    if (doQuickMode)
                    {
                        ItemStack test = recipe.getRecipeOutput();
                        if (ItemStack.areItemsEqual(test, aluWire))
                        {
                            test.stackSize = aluWire.stackSize;
                        }
                        else if (ItemStack.areItemsEqual(test, battery))
                        {
                            test.stackSize = battery.stackSize;
                        }
                    }
                    if (doMetalsToOreDict)
                    {
                        ((IRecipeUpdatable)recipe).replaceInput(meteoricIronIngot, OreDictionary.getOres("ingotMeteoricIron"));
                        ((IRecipeUpdatable)recipe).replaceInput(meteoricIronPlate, OreDictionary.getOres("compressedMeteoricIron"));
                        if (GalacticraftCore.isPlanetsLoaded)
                        {
                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(MarsItems.marsItemBasic, 1, 2), OreDictionary.getOres("ingotDesh"));
                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(MarsItems.marsItemBasic, 1, 5), OreDictionary.getOres("compressedDesh"));
                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(AsteroidsItems.basicItem, 1, 0), OreDictionary.getOres("ingotTitanium"));
                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(AsteroidsItems.basicItem, 1, 6), OreDictionary.getOres("compressedTitanium"));
                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(VenusItems.basicItem, 1, 1), OreDictionary.getOres("ingotLead"));
                        }
                    }
                    else if (doMetalsToGC)
                    {
                        ((IRecipeUpdatable)recipe).replaceInput(meteoricIronIngot);
                        ((IRecipeUpdatable)recipe).replaceInput(meteoricIronPlate);
                        if (GalacticraftCore.isPlanetsLoaded)
                        {
                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(MarsItems.marsItemBasic, 1, 2));
                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(MarsItems.marsItemBasic, 1, 5));
                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(AsteroidsItems.basicItem, 1, 0));
                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(AsteroidsItems.basicItem, 1, 6));
                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(VenusItems.basicItem, 1, 1));
                        }
                    }
                }
            }
        }

        if (doSilicon)
        {
            ItemStack siliconGC =  new ItemStack(GCItems.basicItem, 1, 2);
            boolean needNewList = true;
            List<ItemStack> silicons = OreDictionary.getOres(ConfigManagerCore.otherModsSilicon);
            if (silicons.size() > 0)
            {
                for (ItemStack s : silicons)
                {
                    if (ItemStack.areItemsEqual(s, siliconGC))
                    {
                        needNewList = false;
                        break;
                    }
                }
            }
            if (needNewList)
            {
                List<ItemStack> newList = new ArrayList<>(1 + silicons.size());
                newList.add((ItemStack) siliconGC);
                if (silicons.size() > 0) newList.addAll(silicons);
                silicons = newList;
            }
            CircuitFabricatorRecipes.replaceRecipeIngredient(siliconGC, silicons);
        }

        if (doMetalsToOreDict || doMetalsToGC)
        {
            if (ConfigManagerCore.recipesRequireGCAdvancedMetals)
            {
                CompressorRecipes.replaceRecipeIngredient(meteoricIronIngot);
            }
            else
            {
                CompressorRecipes.replaceRecipeIngredient(meteoricIronIngot, OreDictionary.getOres("ingotMeteoricIron"));
            }
            if (GalacticraftCore.isPlanetsLoaded)
            {
                setConfigurableRecipesPlanets();
            }
            
            // Update Advanced Wafer in space station recipe
            ItemStack sswafer = new ItemStack(GCItems.basicItem, 1, ItemBasic.WAFER_ADVANCED);
            for (SpaceStationType station : GalacticraftRegistry.getSpaceStationData())
            {
                HashMap<Object,Integer> ssrecipe = station.getRecipeForSpaceStation().getInput();
                if (doMetalsToOreDict)
                {
                    ItemStack found = null;
                    for (Object test : ssrecipe.keySet())
                    {
                        if (test instanceof ItemStack && ItemStack.areItemsEqual((ItemStack) test, sswafer))
                        {
                            found = (ItemStack) test;
                            break;
                        }
                    }
                    if (found != null)
                    {
                        ssrecipe.remove(found);
                        ssrecipe.put(OreDictionary.getOres("waferAdvanced"), 1);
                    }
                }
                else if (doMetalsToGC)
                {
                    Object found = null;
                    for (Object test : ssrecipe.keySet())
                    {
                        if (test instanceof List<?> && OreRecipeUpdatable.itemListContains((List<?>) test, sswafer))
                        {
                            found = test;
                            break;
                        }
                    }
                    if (found != null)
                    {
                        ssrecipe.remove(found);
                        ssrecipe.put(sswafer, 1);
                    }
                }
            }
        }
    }

    private static void setConfigurableRecipesPlanets()
    {
        if (ConfigManagerCore.recipesRequireGCAdvancedMetals)
        {
            CompressorRecipes.replaceRecipeIngredient(new ItemStack(MarsItems.marsItemBasic, 1, 2));
            CompressorRecipes.replaceRecipeIngredient(new ItemStack(AsteroidsItems.basicItem, 1, 0));
        }
        else
        {
            CompressorRecipes.replaceRecipeIngredient(new ItemStack(MarsItems.marsItemBasic, 1, 2), OreDictionary.getOres("ingotDesh"));
            CompressorRecipes.replaceRecipeIngredient(new ItemStack(AsteroidsItems.basicItem, 1, 0), OreDictionary.getOres("ingotTitanium"));
        }
    }

    private static void addBuildCraftCraftingRecipes()
    {
        boolean refineryDone = false;
        try
        {
            BuildcraftRecipeRegistry.refinery.addRecipe("buildcraft:fuel", new FluidStack(GCFluids.fluidOil, 1), new FluidStack(FluidRegistry.getFluid("fuel"), 1), 120, 1);
            refineryDone = true;
        }
        catch (Exception e)
        {
        }

        if (refineryDone)
        {
            GCLog.info("Successfully added GC oil to Buildcraft Refinery recipes.");
        }

        try
        {
            Class<?> clazz = Class.forName("buildcraft.BuildCraftTransport");

            Object pipeItemsStone = clazz.getField("pipeItemsStone").get(null);
            Object pipeItemsCobblestone = clazz.getField("pipeItemsCobblestone").get(null);
            Object pipeFluidsCobblestone = clazz.getField("pipeFluidsCobblestone").get(null);
            Object pipeFluidsStone = clazz.getField("pipeFluidsStone").get(null);
            Object pipePowerStone = clazz.getField("pipePowerStone").get(null);
            Object pipePowerGold = clazz.getField("pipePowerGold").get(null);

            RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, BlockEnclosed.EnumEnclosedBlockType.BC_ITEM_COBBLESTONEPIPE.getMeta()), new Object[] { "XYX", 'Y', pipeItemsCobblestone, 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
            RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, BlockEnclosed.EnumEnclosedBlockType.BC_ITEM_STONEPIPE.getMeta()), new Object[] { "XYX", 'Y', pipeItemsStone, 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
            RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, BlockEnclosed.EnumEnclosedBlockType.BC_FLUIDS_COBBLESTONEPIPE.getMeta()), new Object[] { "XYX", 'Y', pipeFluidsCobblestone, 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
            RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, BlockEnclosed.EnumEnclosedBlockType.BC_FLUIDS_STONEPIPE.getMeta()), new Object[] { "XYX", 'Y', pipeFluidsStone, 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
            RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, BlockEnclosed.EnumEnclosedBlockType.BC_POWER_STONEPIPE.getMeta()), new Object[] { "XYX", 'Y', pipePowerStone, 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
            RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, BlockEnclosed.EnumEnclosedBlockType.BC_POWER_GOLDPIPE.getMeta()), new Object[] { "XYX", 'Y', pipePowerGold, 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void addAppEngRecipes()
    {
//         RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, EnumEnclosedBlock.ME_CABLE.getMetadata()), new Object[] { "XYX", 'Y', AEApi.instance().definitions().parts().cableGlass().stack(AEColor.Transparent, 1), 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
    }

    private static void addExNihiloRecipes()
    {
        try
        {
            Class<?> registry = Class.forName("exnihilo.registries.HeatRegistry");
            Method m = registry.getMethod("register", Block.class, float.class);
            m.invoke(null, GCBlocks.unlitTorchLit, 0.1F);
            for (Block torch : GCBlocks.otherModTorchesLit)
            {
                m.invoke(null, torch, 0.1F);
            }
            GCLog.info("Successfully added space torches as heat sources for Ex Nihilo crucibles etc");
        }
        catch (Throwable e)
        {
        }
    }
}
