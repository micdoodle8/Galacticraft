package micdoodle8.mods.galacticraft.core.recipe;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.CircuitFabricatorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;
import micdoodle8.mods.galacticraft.api.world.SpaceStationType;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.blocks.BlockEnclosed;
import micdoodle8.mods.galacticraft.core.blocks.BlockEnclosed.EnumEnclosedBlockType;
import micdoodle8.mods.galacticraft.core.items.ItemBasic;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import appeng.api.AEApi;
import appeng.api.util.AEColor;

public class RecipeManagerGC
{
    public static ArrayList<ItemStack> aluminumIngots = new ArrayList<ItemStack>();

    public static void loadCompatibilityRecipes()
    {
        if (CompatibilityManager.isBCraftTransportLoaded())
        {
            RecipeManagerGC.addBuildCraftCraftingRecipes();
        }

        if (CompatibilityManager.isIc2Loaded())
        {
            RecipeManagerGC.addIndustrialCraft2Recipes();
        }

        if (CompatibilityManager.isAppEngLoaded())
        {
            RecipeManagerGC.addAppEngRecipes();
        }

        RecipeManagerGC.addExNihiloHeatSource();
    }

    @SuppressWarnings("unchecked")
    public
    static void addUniversalRecipes()
    {
    	Object meteoricIronIngot = ConfigManagerCore.recipesRequireGCAdvancedMetals ? new ItemStack(GCItems.itemBasicMoon, 1, 0) : "ingotMeteoricIron";
    	
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

        HashMap<Integer, ItemStack> input = new HashMap<>();
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
        input.put(15, ItemStack.EMPTY);
        input.put(16, ItemStack.EMPTY);
        input.put(17, ItemStack.EMPTY);
        RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTier1, 1, 0), input);

        HashMap<Integer, ItemStack> input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(15, new ItemStack(Blocks.CHEST));
        input2.put(16, ItemStack.EMPTY);
        input2.put(17, ItemStack.EMPTY);
        RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTier1, 1, 1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(15, ItemStack.EMPTY);
        input2.put(16, new ItemStack(Blocks.CHEST));
        input2.put(17, ItemStack.EMPTY);
        RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTier1, 1, 1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(15, ItemStack.EMPTY);
        input2.put(16, ItemStack.EMPTY);
        input2.put(17, new ItemStack(Blocks.CHEST));
        RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTier1, 1, 1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(15, new ItemStack(Blocks.CHEST));
        input2.put(16, new ItemStack(Blocks.CHEST));
        input2.put(17, ItemStack.EMPTY);
        RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTier1, 1, 2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(15, new ItemStack(Blocks.CHEST));
        input2.put(16, ItemStack.EMPTY);
        input2.put(17, new ItemStack(Blocks.CHEST));
        RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTier1, 1, 2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(15, ItemStack.EMPTY);
        input2.put(16, new ItemStack(Blocks.CHEST));
        input2.put(17, new ItemStack(Blocks.CHEST));
        RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTier1, 1, 2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(15, new ItemStack(Blocks.CHEST));
        input2.put(16, new ItemStack(Blocks.CHEST));
        input2.put(17, new ItemStack(Blocks.CHEST));
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
        input.put(17, ItemStack.EMPTY);
        input.put(18, ItemStack.EMPTY);
        input.put(19, ItemStack.EMPTY);
        RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggy, 1, 0), input);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(17, new ItemStack(GCItems.partBuggy, 1, 2));
        input2.put(18, ItemStack.EMPTY);
        input2.put(19, ItemStack.EMPTY);
        RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggy, 1, 1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(17, ItemStack.EMPTY);
        input2.put(18, new ItemStack(GCItems.partBuggy, 1, 2));
        input2.put(19, ItemStack.EMPTY);
        RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggy, 1, 1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(17, ItemStack.EMPTY);
        input2.put(18, ItemStack.EMPTY);
        input2.put(19, new ItemStack(GCItems.partBuggy, 1, 2));
        RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggy, 1, 1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(17, new ItemStack(GCItems.partBuggy, 1, 2));
        input2.put(18, new ItemStack(GCItems.partBuggy, 1, 2));
        input2.put(19, ItemStack.EMPTY);
        RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggy, 1, 2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(17, new ItemStack(GCItems.partBuggy, 1, 2));
        input2.put(18, ItemStack.EMPTY);
        input2.put(19, new ItemStack(GCItems.partBuggy, 1, 2));
        RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggy, 1, 2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(17, ItemStack.EMPTY);
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

        final HashMap<Object, Integer> spaceStationRequirements = new HashMap<Object, Integer>();
        spaceStationRequirements.put("ingotTin", 32);
        spaceStationRequirements.put(aluminumIngots, 16);
        if (ConfigManagerCore.recipesRequireGCAdvancedMetals)
        {
            spaceStationRequirements.put(new ItemStack(GCItems.basicItem, 1, ItemBasic.WAFER_ADVANCED), 1);
        }
        else
        {
            spaceStationRequirements.put("waferAdvanced", 1);
        }
        spaceStationRequirements.put(Items.IRON_INGOT, 24);
        GalacticraftRegistry.registerSpaceStation(new SpaceStationType(ConfigManagerCore.idDimensionOverworldOrbit, 0, new SpaceStationRecipe(spaceStationRequirements)));

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

        CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 10), new ItemStack(GCItems.basicItem, 1, 6), new ItemStack(GCItems.basicItem, 1, 7));
        CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 11), Items.IRON_INGOT, Items.IRON_INGOT);
        CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.itemBasicMoon, 1, 1), meteoricIronIngot);
        CompressorRecipes.addRecipe(new ItemStack(GCItems.heavyPlatingTier1, 2, 0), "XYZ", "XYZ", 'X', new ItemStack(GCItems.basicItem, 1, 9), 'Y', new ItemStack(GCItems.basicItem, 1, 8), 'Z', new ItemStack(GCItems.basicItem, 1, 10));
    }

    public static void setConfigurableRecipes()
    {
        ItemStack solarPanels = new ItemStack(GCItems.basicItem, 9, 12);
        ItemStack basicWafers = new ItemStack(GCItems.basicItem, 3, 13);
        ItemStack advancedWafers = new ItemStack(GCItems.basicItem, 1, 14);

        CircuitFabricatorRecipes.removeRecipe(solarPanels);
        CircuitFabricatorRecipes.removeRecipe(basicWafers);
        CircuitFabricatorRecipes.removeRecipe(advancedWafers);
        List<ItemStack> silicons = OreDictionary.getOres(ConfigManagerCore.otherModsSilicon);
        int siliconCount = silicons.size();
        for (int j = 0; j <= siliconCount; j++)
        {
            ItemStack silicon;
            if (j == 0)
            {
                silicon = new ItemStack(GCItems.basicItem, 1, 2);
            }
            else
            {
                silicon = silicons.get(j - 1);
                if (silicon.getItem() == GCItems.basicItem && silicon.getItemDamage() == 2) continue;
            }

            NonNullList<ItemStack> input1 = NonNullList.create();
            input1.add(new ItemStack(Items.DIAMOND));
            input1.add(silicon);
            input1.add(silicon);
            input1.add(new ItemStack(Items.REDSTONE));
            input1.add(new ItemStack(Items.DYE, 1, 4));
            CircuitFabricatorRecipes.addRecipe(solarPanels, input1);
            NonNullList<ItemStack> input2 = NonNullList.create();
            input2.add(new ItemStack(Items.DIAMOND));
            input2.add(silicon);
            input2.add(silicon);
            input2.add(new ItemStack(Items.REDSTONE));
            input2.add(new ItemStack(Blocks.REDSTONE_TORCH));
            CircuitFabricatorRecipes.addRecipe(basicWafers, input2);
            NonNullList<ItemStack> input3 = NonNullList.create();
            input3.add(new ItemStack(Items.DIAMOND));
            input3.add(silicon);
            input3.add(silicon);
            input3.add(new ItemStack(Items.REDSTONE));
            input3.add(new ItemStack(Items.REPEATER));
            CircuitFabricatorRecipes.addRecipe(advancedWafers, input3);
        }

        CompressorRecipes.removeRecipe(new ItemStack(GCItems.basicItem, 1, 9));
        boolean steelDone = false;
        if (OreDictionary.getOres("ingotSteel").size() > 0)
        {
            CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 9), "ingotSteel", "ingotSteel");
            steelDone = true;
        }
        if (!ConfigManagerCore.hardMode || !steelDone)
        {
            CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 9), Items.COAL, new ItemStack(GCItems.basicItem, 1, 11), Items.COAL);
        }
        else
        {
            CompressorRecipes.addShapelessAdventure(new ItemStack(GCItems.basicItem, 1, 9), Items.COAL, new ItemStack(GCItems.basicItem, 1, 11), Items.COAL);
        }
    }

    private static void addBuildCraftCraftingRecipes()
    {
        boolean refineryDone = false;
        try
        {
//            BuildcraftRecipeRegistry.refinery.addRecipe("buildcraft:fuel", new FluidStack(GCFluids.fluidOil, 1), new FluidStack(FluidRegistry.getFluid("fuel"), 1), 120, 1);
//            refineryDone = true;
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
        	if (CompatibilityManager.classBCTransport != null)
        	{
        		//TODO: Add the two Power pipes once they are included in Buildcraft 8.0.x
	            for (int i = BlockEnclosed.EnumEnclosedBlockType.BC_ITEM_STONEPIPE.getMeta(); i <= BlockEnclosed.EnumEnclosedBlockType.BC_FLUIDS_COBBLESTONEPIPE.getMeta(); i++)
	            {
	                String pipeName = EnumEnclosedBlockType.values()[i].getBCPipeType();
	                Object pipeItemBC = (Item) CompatibilityManager.classBCTransport.getField(pipeName).get(null);
	                RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, i), new Object[] { "XYX", 'Y', pipeItemBC, 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
	            }
        	}
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void addIndustrialCraft2Recipes()
    {
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, BlockEnclosed.EnumEnclosedBlockType.IC2_COPPER_CABLE.getMeta()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("cable", "type:copper,insulation:0"), 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, BlockEnclosed.EnumEnclosedBlockType.IC2_GOLD_CABLE.getMeta()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("cable", "type:gold,insulation:1"), 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, BlockEnclosed.EnumEnclosedBlockType.IC2_HV_CABLE.getMeta()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("cable", "type:iron,insulation:1"), 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, BlockEnclosed.EnumEnclosedBlockType.IC2_GLASS_FIBRE_CABLE.getMeta()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("cable", "type:glass,insulation:0"), 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
        RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, BlockEnclosed.EnumEnclosedBlockType.IC2_LV_CABLE.getMeta()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("cable", "type:tin,insulation:1"), 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
    }

    private static void addAppEngRecipes()
    {
         RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, EnumEnclosedBlockType.ME_CABLE.getMeta()), new Object[] { "XYX", 'Y', AEApi.instance().definitions().parts().cableGlass().stack(AEColor.TRANSPARENT, 1), 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
    }

    private static void addExNihiloHeatSource()
    {
        try
        {
            Class registry = Class.forName("exnihilo.registries.HeatRegistry");
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
