package micdoodle8.mods.galacticraft.planets.mars.recipe;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMars;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.util.MarsUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;

public class RecipeManagerMars
{
    public static void loadRecipes()
    {
        RecipeManagerMars.addUniversalRecipes();
    }

    private static void addUniversalRecipes()
    {
        OreDictionary.registerOre("ingotDesh", new ItemStack(MarsItems.marsItemBasic, 1, 2));
        OreDictionary.registerOre("compressedDesh", new ItemStack(MarsItems.marsItemBasic, 1, 5));
    	Object meteoricIronIngot = ConfigManagerCore.recipesRequireGCAdvancedMetals ? new ItemStack(GCItems.itemBasicMoon, 1, 0) : "ingotMeteoricIron";
    	Object deshIngot = ConfigManagerCore.recipesRequireGCAdvancedMetals ? new ItemStack(MarsItems.marsItemBasic, 1, 2) : "ingotDesh";
    	Object deshPlate = ConfigManagerCore.recipesRequireGCAdvancedMetals ? new ItemStack(MarsItems.marsItemBasic, 1, 5) : "compressedDesh";

        RecipeUtil.addRecipe(new ItemStack(MarsItems.deshBoots), new Object[] { "X X", "X X", 'X', new ItemStack(MarsItems.marsItemBasic, 1, 2) });

        RecipeUtil.addRecipe(new ItemStack(MarsBlocks.marsBlock, 1, 8), new Object[] { "XXX", "XXX", "XXX", 'X', deshIngot });

        RecipeUtil.addRecipe(new ItemStack(MarsBlocks.machine, 1, 0), new Object[] { "XWX", "XZX", "WVW", 'V', GCItems.oxygenConcentrator, 'W', deshPlate, 'X', deshIngot, 'Z', new ItemStack(GCItems.canister) });

        RecipeUtil.addRecipe(new ItemStack(MarsBlocks.machine, 1, 4), new Object[] { "XYX", "XZX", "XYX", 'X', deshPlate, 'Y', new ItemStack(MarsItems.marsItemBasic, 1, 3), 'Z', Items.BED });
        //Gas liquefier
        RecipeUtil.addRecipe(new ItemStack(MarsBlocks.machineT2, 1, 0), new Object[] { "TVS", "FWS", "PXO", 'T', new ItemStack(GCItems.oxTankHeavy, 1, GCItems.oxTankHeavy.getMaxDamage()), 'V', GCItems.oxygenVent, 'F', new ItemStack(GCBlocks.oxygenPipe, 1, 0), 'X', new ItemStack(GCItems.basicItem, 1, 10), 'W', new ItemStack(MarsItems.marsItemBasic, 1, 6), 'O', new ItemStack(GCBlocks.oxygenCompressor, 1, 0), 'P', new ItemStack(GCBlocks.oxygenCompressor, 1, 4), 'S', new ItemStack(GCItems.oxTankMedium, 1, GCItems.oxTankMedium.getMaxDamage()) });
        //Methane Synthesizer
        RecipeUtil.addRecipe(new ItemStack(MarsBlocks.machineT2, 1, 4), new Object[] { "TVT", "FWF", "CXO", 'T', new ItemStack(GCItems.oxTankHeavy, 1, GCItems.oxTankHeavy.getMaxDamage()), 'V', GCItems.oxygenVent, 'F', new ItemStack(GCBlocks.oxygenPipe, 1, 0), 'X', new ItemStack(GCItems.basicItem, 1, 10), 'W', new ItemStack(MarsItems.marsItemBasic, 1, 6), 'O', new ItemStack(GCBlocks.oxygenCompressor, 1, 0), 'C', new ItemStack(GCBlocks.machineTiered, 1, 4) });
        //Water Electrolyzer
        RecipeUtil.addRecipe(new ItemStack(MarsBlocks.machineT2, 1, 8), new Object[] { "TVT", "FWF", "BXB", 'T', new ItemStack(GCItems.oxTankHeavy, 1, GCItems.oxTankHeavy.getMaxDamage()), 'V', GCItems.oxygenVent, 'F', new ItemStack(GCBlocks.oxygenPipe, 1, 0), 'X', new ItemStack(GCItems.basicItem, 1, 10), 'W', new ItemStack(MarsItems.marsItemBasic, 1, 6), 'B', new ItemStack(GCItems.basicItem, 1, 6) });
        //Fluid Manipulator - crafting item
        RecipeUtil.addRecipe(new ItemStack(MarsItems.marsItemBasic, 1, 6), new Object[] { "MXM", "SWS", "MXM", 'S', Items.SLIME_BALL, 'X', GCItems.oxygenFan, 'M', meteoricIronIngot, 'W', new ItemStack(GCItems.basicItem, 1, 14) });

        RecipeUtil.addRecipe(new ItemStack(MarsItems.marsItemBasic, 1, 1), new Object[] { "X", "X", 'X', deshIngot });

        RecipeUtil.addRecipe(new ItemStack(MarsItems.marsItemBasic, 1, 4), new Object[] { "XWX", "XYX", " Z ", 'W', Items.DIAMOND, 'X', Items.LEATHER, 'Y', Items.SLIME_BALL, 'Z', Blocks.CHEST });

        RecipeUtil.addRecipe(new ItemStack(MarsItems.deshSword), new Object[] { "X", "X", "Y", 'X', new ItemStack(MarsItems.marsItemBasic, 1, 2), 'Y', new ItemStack(MarsItems.marsItemBasic, 1, 1) });

        RecipeUtil.addRecipe(new ItemStack(MarsItems.deshPickaxe), new Object[] { "XXX", " Y ", " Y ", 'X', new ItemStack(MarsItems.marsItemBasic, 1, 2), 'Y', new ItemStack(MarsItems.marsItemBasic, 1, 1) });

        RecipeUtil.addRecipe(new ItemStack(MarsItems.deshSpade), new Object[] { "X", "Y", "Y", 'X', new ItemStack(MarsItems.marsItemBasic, 1, 2), 'Y', new ItemStack(MarsItems.marsItemBasic, 1, 1) });

        RecipeUtil.addRecipe(new ItemStack(MarsItems.deshHoe), new Object[] { "XX", "Y ", "Y ", 'X', new ItemStack(MarsItems.marsItemBasic, 1, 2), 'Y', new ItemStack(MarsItems.marsItemBasic, 1, 1) });

        RecipeUtil.addRecipe(new ItemStack(MarsItems.deshHoe), new Object[] { "XX", " Y", " Y", 'X', new ItemStack(MarsItems.marsItemBasic, 1, 2), 'Y', new ItemStack(MarsItems.marsItemBasic, 1, 1) });

        RecipeUtil.addRecipe(new ItemStack(MarsItems.deshAxe), new Object[] { "XX", "XY", " Y", 'X', new ItemStack(MarsItems.marsItemBasic, 1, 2), 'Y', new ItemStack(MarsItems.marsItemBasic, 1, 1) });

        RecipeUtil.addRecipe(new ItemStack(MarsItems.deshAxe), new Object[] { "XX", "YX", "Y ", 'X', new ItemStack(MarsItems.marsItemBasic, 1, 2), 'Y', new ItemStack(MarsItems.marsItemBasic, 1, 1) });

        RecipeUtil.addRecipe(new ItemStack(MarsItems.deshHelmet), new Object[] { "XXX", "X X", 'X', new ItemStack(MarsItems.marsItemBasic, 1, 2) });

        RecipeUtil.addRecipe(new ItemStack(MarsItems.deshChestplate), new Object[] { "X X", "XXX", "XXX", 'X', new ItemStack(MarsItems.marsItemBasic, 1, 2) });

        RecipeUtil.addRecipe(new ItemStack(MarsItems.deshLeggings), new Object[] { "XXX", "X X", "X X", 'X', new ItemStack(MarsItems.marsItemBasic, 1, 2) });

        RecipeUtil.addRecipe(new ItemStack(MarsItems.marsItemBasic, 9, 2), new Object[] { "X", 'X', new ItemStack(MarsBlocks.marsBlock, 1, 8) });

        // Mars Cobblestone Stairs
        RecipeUtil.addRecipe(new ItemStack(MarsBlocks.marsCobblestoneStairs, 4), new Object[] { "  X", " XX", "XXX", 'X', new ItemStack(MarsBlocks.marsBlock, 1, 4) });
        RecipeUtil.addRecipe(new ItemStack(MarsBlocks.marsCobblestoneStairs, 4), new Object[] { "X  ", "XX ", "XXX", 'X', new ItemStack(MarsBlocks.marsBlock, 1, 4) });

        // Mars Dungeon Brick Stairs
        RecipeUtil.addRecipe(new ItemStack(MarsBlocks.marsBricksStairs, 4), new Object[] { "  X", " XX", "XXX", 'X', new ItemStack(MarsBlocks.marsBlock, 1, 7) });
        RecipeUtil.addRecipe(new ItemStack(MarsBlocks.marsBricksStairs, 4), new Object[] { "X  ", "XX ", "XXX", 'X', new ItemStack(MarsBlocks.marsBlock, 1, 7) });

//		// Slab Block
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.slabGCHalf, 6, 4), new Object[] { "XXX", 'X', new ItemStack(MarsBlocks.marsBlock, 1, 4) });
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.slabGCHalf, 6, 5), new Object[] { "XXX", 'X', new ItemStack(MarsBlocks.marsBlock, 1, 7) });
//
//		// Wall Block
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.wallGC, 6, 4), new Object[] { "XXX", "XXX", 'X', new ItemStack(MarsBlocks.marsBlock, 1, 4) });
		RecipeUtil.addRecipe(new ItemStack(GCBlocks.wallGC, 6, 5), new Object[] { "XXX", "XXX", 'X', new ItemStack(MarsBlocks.marsBlock, 1, 7) });

        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(MarsItems.carbonFragments, 8, 0), new ItemStack(Items.COAL, 1, 0));
        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(MarsItems.carbonFragments, 32, 0), new ItemStack(Items.COAL, 1, 0), new ItemStack(Items.COAL, 1, 0), new ItemStack(Items.COAL, 1, 0), new ItemStack(Items.COAL, 1, 0));

        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(MarsItems.carbonFragments, 4, 0), new ItemStack(Items.COAL, 1, 1));
        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(MarsItems.carbonFragments, 16, 0), new ItemStack(Items.COAL, 1, 1), new ItemStack(Items.COAL, 1, 1), new ItemStack(Items.COAL, 1, 1), new ItemStack(Items.COAL, 1, 1));

        // Smelting
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MarsItems.marsItemBasic, 1, 0), new ItemStack(MarsItems.marsItemBasic, 1, 2), 0.2F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MarsBlocks.marsBlock, 1, 4), new ItemStack(MarsBlocks.marsBlock, 1, 9), 0.0F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MarsBlocks.marsBlock, 1, 0), new ItemStack(GCItems.basicItem, 1, 3), 1.0F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MarsBlocks.marsBlock, 1, 1), new ItemStack(GCItems.basicItem, 1, 4), 1.0F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MarsBlocks.marsBlock, 1, 2), new ItemStack(MarsItems.marsItemBasic, 1, 2), 0.2F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MarsBlocks.marsBlock, 1, 3), new ItemStack(Items.IRON_INGOT), 0.2F);

        // Schematic
        HashMap<Integer, ItemStack> input = new HashMap<Integer, ItemStack>();
        input.put(1, new ItemStack(GCItems.partNoseCone));
        input.put(2, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(3, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(4, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(5, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(6, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(7, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(8, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(9, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(10, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(11, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(12, new ItemStack(GCItems.rocketEngine, 1, 1));
        input.put(13, new ItemStack(GCItems.partFins));
        input.put(14, new ItemStack(GCItems.partFins));
        input.put(15, new ItemStack(GCItems.rocketEngine));
        input.put(16, new ItemStack(GCItems.rocketEngine, 1, 1));
        input.put(17, new ItemStack(GCItems.partFins));
        input.put(18, new ItemStack(GCItems.partFins));
        input.put(19, ItemStack.EMPTY);
        input.put(20, ItemStack.EMPTY);
        input.put(21, ItemStack.EMPTY);
        MarsUtil.addRocketBenchT2Recipe(new ItemStack(MarsItems.rocketMars, 1, 0), input);

        HashMap<Integer, ItemStack> input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, new ItemStack(Blocks.CHEST));
        input2.put(20, ItemStack.EMPTY);
        input2.put(21, ItemStack.EMPTY);
        MarsUtil.addRocketBenchT2Recipe(new ItemStack(MarsItems.rocketMars, 1, 1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, ItemStack.EMPTY);
        input2.put(20, new ItemStack(Blocks.CHEST));
        input2.put(21, ItemStack.EMPTY);
        MarsUtil.addRocketBenchT2Recipe(new ItemStack(MarsItems.rocketMars, 1, 1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, ItemStack.EMPTY);
        input2.put(20, ItemStack.EMPTY);
        input2.put(21, new ItemStack(Blocks.CHEST));
        MarsUtil.addRocketBenchT2Recipe(new ItemStack(MarsItems.rocketMars, 1, 1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, new ItemStack(Blocks.CHEST));
        input2.put(20, new ItemStack(Blocks.CHEST));
        input2.put(21, ItemStack.EMPTY);
        MarsUtil.addRocketBenchT2Recipe(new ItemStack(MarsItems.rocketMars, 1, 2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, new ItemStack(Blocks.CHEST));
        input2.put(20, ItemStack.EMPTY);
        input2.put(21, new ItemStack(Blocks.CHEST));
        MarsUtil.addRocketBenchT2Recipe(new ItemStack(MarsItems.rocketMars, 1, 2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, ItemStack.EMPTY);
        input2.put(20, new ItemStack(Blocks.CHEST));
        input2.put(21, new ItemStack(Blocks.CHEST));
        MarsUtil.addRocketBenchT2Recipe(new ItemStack(MarsItems.rocketMars, 1, 2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, new ItemStack(Blocks.CHEST));
        input2.put(20, new ItemStack(Blocks.CHEST));
        input2.put(21, new ItemStack(Blocks.CHEST));
        MarsUtil.addRocketBenchT2Recipe(new ItemStack(MarsItems.rocketMars, 1, 3), input2);

        //

        input = new HashMap<Integer, ItemStack>();
        input.put(1, new ItemStack(GCItems.partNoseCone));
        input.put(2, new ItemStack(GCItems.basicItem, 1, 14));
        input.put(3, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(4, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(5, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(6, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(7, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(8, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(9, new ItemStack(GCItems.partFins));
        input.put(10, new ItemStack(GCItems.partFins));
        input.put(11, new ItemStack(GCItems.rocketEngine));
        input.put(12, new ItemStack(GCItems.partFins));
        input.put(13, new ItemStack(GCItems.partFins));

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(14, new ItemStack(Blocks.CHEST));
        input2.put(15, ItemStack.EMPTY);
        input2.put(16, ItemStack.EMPTY);
        MarsUtil.adCargoRocketRecipe(new ItemStack(MarsItems.rocketMars, 1, 11), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(14, ItemStack.EMPTY);
        input2.put(15, new ItemStack(Blocks.CHEST));
        input2.put(16, ItemStack.EMPTY);
        MarsUtil.adCargoRocketRecipe(new ItemStack(MarsItems.rocketMars, 1, 11), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(14, ItemStack.EMPTY);
        input2.put(15, ItemStack.EMPTY);
        input2.put(16, new ItemStack(Blocks.CHEST));
        MarsUtil.adCargoRocketRecipe(new ItemStack(MarsItems.rocketMars, 1, 11), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(14, new ItemStack(Blocks.CHEST));
        input2.put(15, new ItemStack(Blocks.CHEST));
        input2.put(16, ItemStack.EMPTY);
        MarsUtil.adCargoRocketRecipe(new ItemStack(MarsItems.rocketMars, 1, 12), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(14, new ItemStack(Blocks.CHEST));
        input2.put(15, ItemStack.EMPTY);
        input2.put(16, new ItemStack(Blocks.CHEST));
        MarsUtil.adCargoRocketRecipe(new ItemStack(MarsItems.rocketMars, 1, 12), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(14, ItemStack.EMPTY);
        input2.put(15, new ItemStack(Blocks.CHEST));
        input2.put(16, new ItemStack(Blocks.CHEST));
        MarsUtil.adCargoRocketRecipe(new ItemStack(MarsItems.rocketMars, 1, 12), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(14, new ItemStack(Blocks.CHEST));
        input2.put(15, new ItemStack(Blocks.CHEST));
        input2.put(16, new ItemStack(Blocks.CHEST));
        MarsUtil.adCargoRocketRecipe(new ItemStack(MarsItems.rocketMars, 1, 13), input2);

        RecipeUtil.addRecipe(new ItemStack(MarsBlocks.machine, 1, BlockMachineMars.LAUNCH_CONTROLLER_METADATA), new Object[] { "ZVZ", "YXY", "ZWZ", 'V', new ItemStack(GCItems.basicItem, 1, 19), 'W', new ItemStack(GCBlocks.aluminumWire, 1, 0), 'X', new ItemStack(GCItems.basicItem, 1, 14), 'Y', deshPlate, 'Z', deshIngot });
    }
}
