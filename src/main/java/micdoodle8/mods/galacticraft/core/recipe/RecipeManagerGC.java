package micdoodle8.mods.galacticraft.core.recipe;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.CircuitFabricatorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.IRecipeUpdatable;
import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;
import micdoodle8.mods.galacticraft.api.world.SpaceStationType;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.GCDimensions;
import micdoodle8.mods.galacticraft.core.items.ItemEmergencyKit;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerClient;
import micdoodle8.mods.galacticraft.core.util.*;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.items.VenusItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.lang.reflect.Method;
import java.util.*;

public class RecipeManagerGC
{
//    public static ArrayList<ItemStack> aluminumIngots = new ArrayList<ItemStack>();
    private static boolean configSaved_RequireGCmetals = true;
    private static boolean configSaved_QuickMode = false;
    private static String configSaved_Silicon = "-";

    public static void loadCompatibilityRecipes()
    {
//        if (CompatibilityManager.isBCraftTransportLoaded())
//        {
//            RecipeManagerGC.addBuildCraftCraftingRecipes();
//        }

//        if (CompatibilityManager.isIc2Loaded())
//        {
//            CompatModuleIC2.addIndustrialCraft2Recipes();
//        }

//        if (CompatibilityManager.isAppEngLoaded())
//        {
//            RecipeManagerGC.addAppEngRecipes();
//        }

//        if (CompatibilityManager.modAALoaded)
//        {
//            CompatModuleActuallyAdditions.addRecipes();
//        }

//        RecipeManagerGC.addExNihiloHeatSource();
    }

    public static void addUniversalRecipes()
    {
//        RecipeSorter.register("galacticraftcore:shapedore", OreRecipeUpdatable.class, Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless");

        Object meteoricIronIngot = new ItemStack(GCItems.ingotMeteoricIron, 1);
//        Object meteoricIronPlate = new ItemStack(GCItems.itemBasicMoon, 1, 1);
//    	Object deshIngot = GalacticraftCore.isPlanetsLoaded ? new ItemStack(MarsItems.marsItemBasic, 1, 2) : GCItems.heavyPlatingTier1;
//
//    	FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCBlocks.basicBlock, 1, 5), new ItemStack(GCItems.basicItem, 1, 3), 0.5F);
//        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCBlocks.basicBlock, 1, 6), new ItemStack(GCItems.basicItem, 1, 4), 0.5F);
//        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCBlocks.basicBlock, 1, 7), new ItemStack(GCItems.basicItem, 1, 5), 0.5F);
//        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCItems.meteorChunk, 1, 0), new ItemStack(GCItems.meteorChunk, 1, 1), 0.1F);
//        FurnaceRecipes.instance().addSmelting(GCItems.meteoricIronRaw, new ItemStack(GCItems.itemBasicMoon), 1.0F);
//        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCBlocks.blockMoon, 1, 0), new ItemStack(GCItems.basicItem, 1, 3), 1.0F);
//        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCBlocks.blockMoon, 1, 1), new ItemStack(GCItems.basicItem, 1, 4), 1.0F);
//        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCBlocks.blockMoon, 1, 2), new ItemStack(GCItems.cheeseCurd), 1.0F);
//        //Recycling: smelt tin/copper canisters back into ingots
//        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCItems.canister, 1, 0), new ItemStack(GCItems.basicItem, 3, 4), 1.0F);
//        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCItems.canister, 1, 1), new ItemStack(GCItems.basicItem, 3, 3), 1.0F);
//        if (CompatibilityManager.useAluDust())
//        {
//            FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCItems.ic2compat, 1, 0), new ItemStack(GCItems.basicItem, 1, 5), 1.0F);
//        }
//        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCItems.foodItem, 1, 6), new ItemStack(GCItems.foodItem, 1, 7), 1.0F);
//        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCBlocks.blockMoon, 1, 6), new ItemStack(GCItems.itemBasicMoon, 1, 2), 1.0F);

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
        input.put(12, new ItemStack(GCItems.rocketEngineT1));
        input.put(13, new ItemStack(GCItems.partFins));
        input.put(14, new ItemStack(GCItems.partFins));
        input.put(15, ItemStack.EMPTY);
        input.put(16, ItemStack.EMPTY);
        input.put(17, ItemStack.EMPTY);
        RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTierOne, 1), input);

        Tag<Block> woodChestTag = BlockTags.getCollection().getOrCreate(new ResourceLocation("forge", "chests/wooden"));

        HashMap<Integer, ItemStack> input2;

        for (Block chestBlock : woodChestTag.getAllElements())
        {
            ItemStack woodChest = new ItemStack(chestBlock);

            input2 = new HashMap<Integer, ItemStack>(input);
            input2.put(15, woodChest);
            input2.put(16, ItemStack.EMPTY);
            input2.put(17, ItemStack.EMPTY);
            RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTierOneCargo1, 1), input2);

            input2 = new HashMap<Integer, ItemStack>(input);
            input2.put(15, ItemStack.EMPTY);
            input2.put(16, woodChest);
            input2.put(17, ItemStack.EMPTY);
            RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTierOneCargo1, 1), input2);

            input2 = new HashMap<Integer, ItemStack>(input);
            input2.put(15, ItemStack.EMPTY);
            input2.put(16, ItemStack.EMPTY);
            input2.put(17, woodChest);
            RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTierOneCargo1, 1), input2);

            input2 = new HashMap<Integer, ItemStack>(input);
            input2.put(15, woodChest);
            input2.put(16, woodChest);
            input2.put(17, ItemStack.EMPTY);
            RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTierOneCargo2, 1), input2);

            input2 = new HashMap<Integer, ItemStack>(input);
            input2.put(15, woodChest);
            input2.put(16, ItemStack.EMPTY);
            input2.put(17, woodChest);
            RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTierOneCargo2, 1), input2);

            input2 = new HashMap<Integer, ItemStack>(input);
            input2.put(15, ItemStack.EMPTY);
            input2.put(16, woodChest);
            input2.put(17, woodChest);
            RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTierOneCargo2, 1), input2);

            input2 = new HashMap<Integer, ItemStack>(input);
            input2.put(15, woodChest);
            input2.put(16, woodChest);
            input2.put(17, woodChest);
            RecipeUtil.addRocketBenchRecipe(new ItemStack(GCItems.rocketTierOneCargo3, 1), input2);
        }



        //

        input = new HashMap<Integer, ItemStack>();
        input.put(1, new ItemStack(GCItems.heavyPlatingTier1));
        input.put(2, new ItemStack(GCItems.heavyPlatingTier1));
        input.put(3, new ItemStack(GCItems.heavyPlatingTier1));
        input.put(4, new ItemStack(GCItems.heavyPlatingTier1));
        input.put(5, new ItemStack(GCItems.heavyPlatingTier1));
        input.put(6, new ItemStack(GCItems.buggyMaterialSeat));
        input.put(7, new ItemStack(GCItems.heavyPlatingTier1));
        input.put(8, new ItemStack(GCItems.heavyPlatingTier1));
        input.put(9, new ItemStack(GCItems.heavyPlatingTier1));
        input.put(10, new ItemStack(GCItems.heavyPlatingTier1));
        input.put(11, new ItemStack(GCItems.heavyPlatingTier1));
        input.put(12, new ItemStack(GCItems.heavyPlatingTier1));
        input.put(13, new ItemStack(GCItems.buggyMaterialWheel));
        input.put(14, new ItemStack(GCItems.buggyMaterialWheel));
        input.put(15, new ItemStack(GCItems.buggyMaterialWheel));
        input.put(16, new ItemStack(GCItems.buggyMaterialWheel));
        input.put(17, ItemStack.EMPTY);
        input.put(18, ItemStack.EMPTY);
        input.put(19, ItemStack.EMPTY);
        RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggy), input);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(17, new ItemStack(GCItems.buggyMaterialStorage));
        input2.put(18, ItemStack.EMPTY);
        input2.put(19, ItemStack.EMPTY);
        RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggyInventory1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(17, ItemStack.EMPTY);
        input2.put(18, new ItemStack(GCItems.buggyMaterialStorage));
        input2.put(19, ItemStack.EMPTY);
        RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggyInventory1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(17, ItemStack.EMPTY);
        input2.put(18, ItemStack.EMPTY);
        input2.put(19, new ItemStack(GCItems.buggyMaterialStorage));
        RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggyInventory1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(17, new ItemStack(GCItems.buggyMaterialStorage));
        input2.put(18, new ItemStack(GCItems.buggyMaterialStorage));
        input2.put(19, ItemStack.EMPTY);
        RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggyInventory2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(17, new ItemStack(GCItems.buggyMaterialStorage));
        input2.put(18, ItemStack.EMPTY);
        input2.put(19, new ItemStack(GCItems.buggyMaterialStorage));
        RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggyInventory2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(17, ItemStack.EMPTY);
        input2.put(18, new ItemStack(GCItems.buggyMaterialStorage));
        input2.put(19, new ItemStack(GCItems.buggyMaterialStorage));
        RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggyInventory2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(17, new ItemStack(GCItems.buggyMaterialStorage));
        input2.put(18, new ItemStack(GCItems.buggyMaterialStorage));
        input2.put(19, new ItemStack(GCItems.buggyMaterialStorage));
        RecipeUtil.addBuggyBenchRecipe(new ItemStack(GCItems.buggyInventory3), input2);

//        aluminumIngots.addAll(OreDictionary.getOres("ingotAluminum"));
//        ArrayList<ItemStack> addedList = new ArrayList<ItemStack>();
//        for (ItemStack ingotNew : OreDictionary.getOres("ingotAluminium"))
//        {
//            boolean flag = false;
//            for (ItemStack ingotDone : aluminumIngots)
//            {
//                if (ItemStack.areItemStacksEqual(ingotNew, ingotDone))
//                {
//                    flag = true;
//                    break;
//                }
//            }
//            if (!flag)
//            {
//                addedList.add(ingotNew);
//                OreDictionary.registerOre("ingotAluminum", ingotNew);
//            }
//        }
//        if (addedList.size() > 0)
//        {
//            aluminumIngots.addAll(addedList);
//            addedList.clear();
//        }
//        for (ItemStack ingotNew : OreDictionary.getOres("ingotNaturalAluminum"))
//        {
//            for (ItemStack ingotDone : aluminumIngots)
//            {
//                if (!ItemStack.areItemStacksEqual(ingotNew, ingotDone))
//                {
//                    addedList.add(ingotNew);
//                }
//            }
//        }
//        if (addedList.size() > 0)
//        {
//            aluminumIngots.addAll(addedList);
//        }

        final HashMap<Object, Integer> spaceStationRequirements = new HashMap<Object, Integer>(4, 1.0F);
        spaceStationRequirements.put(new ResourceLocation("forge", "ingots/tin"), 32);
        spaceStationRequirements.put(new ResourceLocation("forge", "ingots/aluminum"), 16);
        spaceStationRequirements.put(new ResourceLocation("forge", "wafers/advanced"), 1);
        spaceStationRequirements.put(new ResourceLocation("forge", "ingots/iron"), 24);
        GalacticraftRegistry.registerSpaceStation(new SpaceStationType(/*GCDimensions.SPACE_STATION_MOD_DIMENSION, */DimensionType.OVERWORLD, new SpaceStationRecipe(spaceStationRequirements)));

        //EmergencyKit
//        RecipeUtil.addCustomRecipe(new ShapedRecipeNBT(new ItemStack(GCItems.emergencyKit), ItemEmergencyKit.getRecipe())); TODO Kit recipe

        CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.compressedCopper, 1), "ingots/copper", "ingots/copper");
        CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.compressedTin, 1), "ingots/tin", "ingots/tin");
        CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.compressedAluminum, 1), "ingots/aluminum", "ingots/aluminum");

/*        // Support for all the spellings of Aluminum
        for (ItemStack stack : aluminumIngots)
        {
          	CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 8), stack, stack);
        }
*/
        Collection<Item> bronzeItems = ItemTags.getCollection().getOrCreate(new ResourceLocation("forge", "ingots/bronze")).getAllElements();
        if (bronzeItems.size() > 0)
        {
            CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.compressedBronze, 1), "ingots/bronze", "ingots/bronze");
        }
        Collection<Item> steelItems = ItemTags.getCollection().getOrCreate(new ResourceLocation("forge", "ingots/steel")).getAllElements();
        if (steelItems.size() > 0)
        {
            CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.compressedSteel, 1), "ingots/steel", "ingots/steel");
            CompressorRecipes.steelIngotsPresent = true;
        }
        CompressorRecipes.steelRecipeGC = Arrays.asList(new ItemStack(Items.COAL), new ItemStack(GCItems.compressedIron, 1), new ItemStack(Items.COAL));
        CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.compressedSteel, 1), Items.COAL, new ItemStack(GCItems.compressedIron, 1), Items.COAL);
        CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.compressedBronze, 1), new ItemStack(GCItems.compressedCopper, 1), new ItemStack(GCItems.compressedTin, 1));
        CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.compressedIron, 1), Items.IRON_INGOT, Items.IRON_INGOT);
        CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.compressedMeteoricIron, 1), meteoricIronIngot);
        CompressorRecipes.addRecipe(new ItemStack(GCItems.heavyPlatingTier1, 2), "XYZ", "XYZ", 'X', new ItemStack(GCItems.compressedSteel, 1), 'Y', new ItemStack(GCItems.compressedAluminum, 1), 'Z', new ItemStack(GCItems.compressedBronze, 1));

        ItemStack solarPanels = new ItemStack(GCItems.compressedWaferSolar, 9);
        ItemStack basicWafers = new ItemStack(GCItems.compressedWaferBasic, 3);
        ItemStack advancedWafers = new ItemStack(GCItems.compressedWaferAdvanced, 1);
        ItemStack silicon =  new ItemStack(GCItems.rawSilicon, 1);
        CircuitFabricatorRecipes.addRecipe(solarPanels, Arrays.asList( new ItemStack(Items.DIAMOND), silicon, silicon, new ItemStack(Items.REDSTONE), new ItemStack(Items.LAPIS_LAZULI, 1) ));
        CircuitFabricatorRecipes.addRecipe(basicWafers, Arrays.asList( new ItemStack(Items.DIAMOND), silicon, silicon, new ItemStack(Items.REDSTONE), new ItemStack(Blocks.REDSTONE_TORCH) ));
        CircuitFabricatorRecipes.addRecipe(advancedWafers, Arrays.asList( new ItemStack(Items.DIAMOND), silicon, silicon, new ItemStack(Items.REDSTONE), new ItemStack(Items.REPEATER) ));
    }

//    @SubscribeEvent
//    public static void onRecipesUpdated(RecipesUpdatedEvent event)
//    {
//        boolean doQuickMode = ConfigManagerCore.quickMode.get() != configSaved_QuickMode;
//        boolean doMetalsToOreDict = configSaved_RequireGCmetals && !ConfigManagerCore.recipesRequireGCAdvancedMetals.get();
//        boolean doMetalsToGC = !configSaved_RequireGCmetals && ConfigManagerCore.recipesRequireGCAdvancedMetals.get();
//        if (doQuickMode || doMetalsToOreDict || doMetalsToGC)
//        {
//            ItemStack aluWire = new ItemStack(GCBlocks.aluminumWire, ConfigManagerCore.quickMode.get() ? 9 : 6);
//            ItemStack battery = new ItemStack(GCItems.battery, ConfigManagerCore.quickMode.get() ? 3 : 2);
//            battery.setDamage(100);
//            ItemStack meteoricIronIngot = new ItemStack(GCItems.ingotMeteoricIron, 1);
//            ItemStack meteoricIronPlate = new ItemStack(GCItems.compressedMeteoricIron, 1);
//            for (IRecipe<?> recipe : event.getRecipeManager().getRecipes())
//            {
//                if (recipe instanceof IRecipeUpdatable)
//                {
//                    if (doQuickMode)
//                    {
//                        ItemStack test = recipe.getRecipeOutput();
//                        if (ItemStack.areItemsEqual(test, aluWire))
//                        {
//                            test.setCount(aluWire.getCount());
//                        }
//                        else if (ItemStack.areItemsEqual(test, battery))
//                        {
//                            test.setCount(battery.getCount());
//                        }
//                    }
//                    if (doMetalsToOreDict)
//                    {
//                        ((IRecipeUpdatable)recipe).replaceInput(meteoricIronIngot, OreDictionary.getOres("ingotMeteoricIron"));
//                        ((IRecipeUpdatable)recipe).replaceInput(meteoricIronPlate, OreDictionary.getOres("compressedMeteoricIron"));
//                        if (GalacticraftCore.isPlanetsLoaded)
//                        {
//                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(MarsItems.ingotDesh, 1), OreDictionary.getOres("ingotDesh"));
//                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(MarsItems.compressedDesh, 1), OreDictionary.getOres("compressedDesh"));
//                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(AsteroidsItems.ingotTitanium, 1), OreDictionary.getOres("ingotTitanium"));
//                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(AsteroidsItems.compressedTitanium, 1), OreDictionary.getOres("compressedTitanium"));
//                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(VenusItems.ingotLead, 1), OreDictionary.getOres("ingotLead"));
//                        }
//                    }
//                    else if (doMetalsToGC)
//                    {
//                        ((IRecipeUpdatable)recipe).replaceInput(meteoricIronIngot);
//                        ((IRecipeUpdatable)recipe).replaceInput(meteoricIronPlate);
//                        if (GalacticraftCore.isPlanetsLoaded)
//                        {
//                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(MarsItems.ingotDesh, 1));
//                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(MarsItems.compressedDesh, 1));
//                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(AsteroidsItems.ingotTitanium, 1));
//                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(AsteroidsItems.compressedTitanium, 1));
//                            ((IRecipeUpdatable)recipe).replaceInput(new ItemStack(VenusItems.ingotLead, 1));
//                        }
//                    }
//                }
//            }
//        }
//    }

//    public static void setConfigurableRecipes()
//    {
//        if (GCCoreUtil.getEffectiveSide() == LogicalSide.CLIENT && CompatibilityManager.modJEILoaded) TickHandlerClient.updateJEIhiding = true;
//
//        // Update Aluminium Wire and Battery crafting recipes and GC Advanced Metals recipes
//        boolean doQuickMode = ConfigManagerCore.quickMode.get() != configSaved_QuickMode;
//        configSaved_QuickMode = ConfigManagerCore.quickMode.get();
//
//        boolean doMetalsToOreDict = configSaved_RequireGCmetals && !ConfigManagerCore.recipesRequireGCAdvancedMetals.get();
//        boolean doMetalsToGC = !configSaved_RequireGCmetals && ConfigManagerCore.recipesRequireGCAdvancedMetals.get();
//        configSaved_RequireGCmetals = ConfigManagerCore.recipesRequireGCAdvancedMetals.get();
//
//        boolean doSilicon = !ConfigManagerCore.otherModsSilicon.get().equals(configSaved_Silicon);
//        configSaved_Silicon = ConfigManagerCore.otherModsSilicon.get();
//
//        if (doSilicon)
//        {
//            ItemStack siliconGC =  new ItemStack(GCItems.rawSilicon, 1);
//            boolean needNewList = true;
//            List<ItemStack> silicons = OreDictionary.getOres(ConfigManagerCore.otherModsSilicon.get());
//            if (silicons.size() > 0)
//            {
//                for (ItemStack s : silicons)
//                {
//                    if (ItemStack.areItemsEqual(s, siliconGC))
//                    {
//                        needNewList = false;
//                        break;
//                    }
//                }
//            }
//            if (needNewList)
//            {
//                List<ItemStack> newList = new ArrayList<>(1 + silicons.size());
//                newList.add((ItemStack) siliconGC);
//                if (silicons.size() > 0) newList.addAll(silicons);
//                silicons = newList;
//            }
//            CircuitFabricatorRecipes.replaceRecipeIngredient(siliconGC, silicons);
//        }
//
//        ItemStack meteoricIronIngot = new ItemStack(GCItems.ingotMeteoricIron, 1);
//
//        if (doMetalsToOreDict || doMetalsToGC)
//        {
//            if (ConfigManagerCore.recipesRequireGCAdvancedMetals.get())
//            {
//                CompressorRecipes.replaceRecipeIngredient(meteoricIronIngot);
//            }
//            else
//            {
//                CompressorRecipes.replaceRecipeIngredient(meteoricIronIngot, OreDictionary.getOres("ingotMeteoricIron"));
//            }
//            if (GalacticraftCore.isPlanetsLoaded)
//            {
//                setConfigurableRecipesPlanets();
//            }
//
//            // Update Advanced Wafer in space station recipe
//            ItemStack sswafer = new ItemStack(GCItems.compressedWaferAdvanced, 1);
//            for (SpaceStationType station : GalacticraftRegistry.getSpaceStationData())
//            {
//                HashMap<Object,Integer> ssrecipe = station.getRecipeForSpaceStation().getInput();
//                if (doMetalsToOreDict)
//                {
//                    ItemStack found = null;
//                    for (Object test : ssrecipe.keySet())
//                    {
//                        if (test instanceof ItemStack && ItemStack.areItemsEqual((ItemStack) test, sswafer))
//                        {
//                            found = (ItemStack) test;
//                            break;
//                        }
//                    }
//                    if (found != null)
//                    {
//                        ssrecipe.remove(found);
//                        ssrecipe.put(OreDictionary.getOres("waferAdvanced"), 1);
//                    }
//                }
//                else if (doMetalsToGC)
//                {
//                    Object found = null;
//                    for (Object test : ssrecipe.keySet())
//                    {
//                        if (test instanceof List<?> && OreRecipeUpdatable.itemListContains((List<?>) test, sswafer))
//                        {
//                            found = test;
//                            break;
//                        }
//                    }
//                    if (found != null)
//                    {
//                        ssrecipe.remove(found);
//                        ssrecipe.put(sswafer, 1);
//                    }
//                }
//            }
//        }
//    }

//    private static void setConfigurableRecipesPlanets()
//    {
//        if (ConfigManagerCore.recipesRequireGCAdvancedMetals.get())
//        {
//            CompressorRecipes.replaceRecipeIngredient(new ItemStack(MarsItems.ingotDesh, 1));
//            CompressorRecipes.replaceRecipeIngredient(new ItemStack(AsteroidsItems.ingotTitanium, 1));
//        }
//        else
//        {
//            CompressorRecipes.replaceRecipeIngredient(new ItemStack(MarsItems.ingotDesh, 1), OreDictionary.getOres("ingotDesh"));
//            CompressorRecipes.replaceRecipeIngredient(new ItemStack(AsteroidsItems.ingotTitanium, 1), OreDictionary.getOres("ingotTitanium"));
//        }
//    }

//    private static void addBuildCraftCraftingRecipes()
//    {
//        boolean refineryDone = false;
//        try
//        {
////            BuildcraftRecipeRegistry.refinery.addRecipe("buildcraft:fuel", new FluidStack(GCFluids.fluidOil, 1), new FluidStack(FluidRegistry.getFluid("fuel"), 1), 120, 1);
////            refineryDone = true;
//        }
//        catch (Exception e)
//        {
//        }
//
//        if (refineryDone)
//        {
//            GCLog.info("Successfully added GC oil to Buildcraft Refinery recipes.");
//        }
//
//        try
//        {
//        	if (CompatibilityManager.classBCTransport != null)
//        	{
//        		//TODO: Add the two Power pipes once they are included in Buildcraft 8.0.x
//	            for (int i = BlockEnclosed.EnumEnclosedBlockType.BC_ITEM_STONEPIPE.getMeta(); i <= BlockEnclosed.EnumEnclosedBlockType.BC_FLUIDS_COBBLESTONEPIPE.getMeta(); i++)
//	            {
//	                String pipeName = EnumEnclosedBlockType.values()[i].getBCPipeType();
//	                Object pipeItemBC = (Item) CompatibilityManager.classBCTransport.getField(pipeName).get(null);
//	                RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, i), new Object[] { "XYX", 'Y', pipeItemBC, 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
//	            }
//        	}
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

//    private static void addAppEngRecipes()
//    {
//        RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, EnumEnclosedBlockType.ME_CABLE.getMeta()), new Object[] { "XYX", 'Y', AEApi.instance().definitions().parts().cableGlass().stack(AEColor.TRANSPARENT, 1), 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
//        IGrinderRegistry a = AEApi.instance().registries().grinder();
//        boolean officialAE2API = false;
//        try {
//            officialAE2API = a.getClass().getMethod("builder") != null;
//        } catch (Exception ignore) { }
//        if (officialAE2API)
//        {
//            a.addRecipe(a.builder().withInput(new ItemStack(GCBlocks.basicBlock, 1, 7)).withOutput(new ItemStack(GCItems.ic2compat, 1, 0)).withFirstOptional(new ItemStack(GCItems.ic2compat, 1, 0), 0.8F).withTurns(8).build());
//            if (GalacticraftCore.isPlanetsLoaded)
//            {
//                a.addRecipe(a.builder().withInput(new ItemStack(AsteroidBlocks.blockBasic, 1, 3)).withOutput(new ItemStack(GCItems.ic2compat, 1, 0)).withFirstOptional(new ItemStack(GCItems.ic2compat, 1, 0), 0.9F).withTurns(8).build());
//                a.addRecipe(a.builder().withInput(new ItemStack(VenusBlocks.venusBlock, 1, 6)).withOutput(new ItemStack(GCItems.ic2compat, 1, 0)).withFirstOptional(new ItemStack(GCItems.ic2compat, 1, 0), 1.0F).withTurns(8).build());
//                // Grind Ilmenite Ore for 1 Titanium Dust, 1 Iron Dust
//                Optional<ItemStack> ironDust = AEApi.instance().definitions().materials().ironDust().maybeStack(1);
//                ironDust.ifPresent(dustStack -> a.addRecipe(a.builder().withInput(new ItemStack(AsteroidBlocks.blockBasic, 1, 4)).withOutput(new ItemStack(AsteroidsItems.basicItem, 1, 9)).withFirstOptional(dustStack, 1.0F).withTurns(8).build()));
//            }
//        } else {
//            GCLog.debug("Failed to add Applied Energistics 2 Recipes");
//        }
//    }

//    private static void addExNihiloHeatSource()
//    {
//        try
//        {
//            Class<?> registry = Class.forName("exnihilo.registries.HeatRegistry");
//            Method m = registry.getMethod("register", Block.class, float.class);
//            m.invoke(null, GCBlocks.unlitTorchLit, 0.1F);
//            for (Block torch : GCBlocks.otherModTorchesLit)
//            {
//                m.invoke(null, torch, 0.1F);
//            }
//            GCLog.info("Successfully added space torches as heat sources for Ex Nihilo crucibles etc");
//        }
//        catch (Throwable e)
//        {
//        }
//    }
}
