package micdoodle8.mods.galacticraft.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

import mezz.jei.api.ingredients.IIngredientBlacklist;
import micdoodle8.mods.galacticraft.core.items.*;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.StackSorted;
import micdoodle8.mods.galacticraft.core.wrappers.PartialCanister;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GCItems
{
    public static Item oxTankLight;
    public static Item oxTankMedium;
    public static Item oxTankHeavy;
    public static Item oxMask;
    public static Item rocketTier1;
    public static Item sensorGlasses;
    public static Item sensorLens;
    public static Item steelPickaxe;
    public static Item steelAxe;
    public static Item steelHoe;
    public static Item steelSpade;
    public static Item steelSword;
    public static Item steelHelmet;
    public static Item steelChestplate;
    public static Item steelLeggings;
    public static Item steelBoots;
    public static Item canister;
    public static Item oxygenVent;
    public static Item oxygenFan;
    public static Item oxygenConcentrator;
    public static Item rocketEngine;
    public static Item heavyPlatingTier1;
    public static Item partNoseCone;
    public static Item partFins;
    public static Item buggy;
    public static Item flag;
    public static Item oxygenGear;
    public static Item parachute;
    public static Item canvas;
    public static Item flagPole;
    public static Item oilCanister;
    public static Item fuelCanister;
    public static Item oxygenCanisterInfinite;
    public static Item schematic;
    public static Item key;
    public static Item partBuggy;
    public static Item basicItem;
    public static Item foodItem;
    public static Item battery;
    public static Item infiniteBatery;
    public static Item meteorChunk;
    public static Item wrench;
    public static Item cheeseCurd;
    public static Item meteoricIronRaw;
    public static Item itemBasicMoon;
    public static Item bucketOil;
    public static Item bucketFuel;
//	public static Item cheeseBlock;
    public static Item prelaunchChecklist;
    public static Item dungeonFinder;
    public static Item ic2compat;
    public static Item emergencyKit;

    public static ArmorMaterial ARMOR_SENSOR_GLASSES = EnumHelper.addArmorMaterial("SENSORGLASSES", "", 200, new int[] { 0, 0, 0, 0 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    public static ArmorMaterial ARMOR_STEEL = EnumHelper.addArmorMaterial("steel", "", 30, new int[] { 3, 6, 8, 3 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);
    public static ToolMaterial TOOL_STEEL = EnumHelper.addToolMaterial("steel", 3, 768, 5.0F, 2, 8);

    public static ArrayList<Item> hiddenItems = new ArrayList<Item>();
    public static LinkedList<ItemCanisterGeneric> canisterTypes = new LinkedList<ItemCanisterGeneric>();
    public static Map<EnumSortCategoryItem, List<StackSorted>> sortMapItems = Maps.newHashMap();
    public static HashMap<ItemStack, ItemStack> itemChanges = new HashMap<>(4, 1.0F);

    public static void initItems()
    {
        GCItems.oxTankLight = new ItemOxygenTank(1, "oxygen_tank_light_full");
        GCItems.oxTankMedium = new ItemOxygenTank(2, "oxygen_tank_med_full");
        GCItems.oxTankHeavy = new ItemOxygenTank(3, "oxygen_tank_heavy_full");
        GCItems.oxMask = new ItemOxygenMask("oxygen_mask");
        GCItems.rocketTier1 = new ItemTier1Rocket("rocket_t1");
        GCItems.sensorGlasses = new ItemSensorGlasses("sensor_glasses");
        GCItems.steelPickaxe = new ItemPickaxeGC("steel_pickaxe");
        GCItems.steelAxe = new ItemAxeGC("steel_axe");
        GCItems.steelHoe = new ItemHoeGC("steel_hoe");
        GCItems.steelSpade = new ItemSpadeGC("steel_shovel");
        GCItems.steelSword = new ItemSwordGC("steel_sword");
        GCItems.steelHelmet = new ItemArmorGC(EntityEquipmentSlot.HEAD, "helmet");
        GCItems.steelChestplate = new ItemArmorGC(EntityEquipmentSlot.CHEST, "chestplate");
        GCItems.steelLeggings = new ItemArmorGC(EntityEquipmentSlot.LEGS, "leggings");
        GCItems.steelBoots = new ItemArmorGC(EntityEquipmentSlot.FEET, "boots");
        GCItems.canister = new ItemCanister("canister");
        GCItems.oxygenVent = new ItemBase("air_vent");
        GCItems.oxygenFan = new ItemBase("air_fan");
        GCItems.oxygenConcentrator = new ItemBase("oxygen_concentrator");
        GCItems.heavyPlatingTier1 = new ItemBase("heavy_plating").setSmeltingXP(1F);
        GCItems.rocketEngine = new ItemRocketEngineGC("engine");
        GCItems.partFins = new ItemBase("rocket_fins");
        GCItems.partNoseCone = new ItemBase("nose_cone");
        GCItems.sensorLens = new ItemBase("sensor_lens");
        GCItems.buggy = new ItemBuggy("buggy");
        GCItems.flag = new ItemFlag("flag");
        GCItems.oxygenGear = new ItemOxygenGear("oxygen_gear");
        GCItems.parachute = new ItemParaChute("parachute");
        GCItems.canvas = new ItemBase("canvas");
        GCItems.oilCanister = new ItemOilCanister("oil_canister_partial");
        GCItems.fuelCanister = new ItemFuelCanister("fuel_canister_partial");
        GCItems.oxygenCanisterInfinite = new ItemCanisterOxygenInfinite("infinite_oxygen");
        GCItems.flagPole = new ItemBase("steel_pole");
        GCItems.schematic = new ItemSchematic("schematic");
        GCItems.key = new ItemKey("key");
        GCItems.partBuggy = new ItemBuggyMaterial("buggymat");
        GCItems.basicItem = new ItemBasic("basic_item");
        GCItems.foodItem = new ItemFood("food");
        GCItems.battery = new ItemBattery("battery");
        GCItems.infiniteBatery = new ItemBatteryInfinite("infinite_battery");
        GCItems.meteorChunk = new ItemMeteorChunk("meteor_chunk");
        GCItems.wrench = new ItemUniversalWrench("standard_wrench");
        GCItems.cheeseCurd = new ItemCheese(1, 0.1F, false);
//		GCItems.cheeseBlock = new ItemBlockCheese(GCBlocks.cheeseBlock, "cheeseBlock");
        GCItems.meteoricIronRaw = new ItemMeteoricIron("meteoric_iron_raw");
        GCItems.itemBasicMoon = new ItemMoon("item_basic_moon");
        GCItems.prelaunchChecklist = new ItemPreLaunchChecklist("prelaunch_checklist");
        GCItems.dungeonFinder = new ItemBase("dungeonfinder");
        GCItems.ic2compat = new ItemIC2Compat("ic2compat");
        GCItems.emergencyKit = new ItemEmergencyKit("emergency_kit"); 

        GCItems.registerHarvestLevels();

        GCItems.registerItems();
        GCItems.itemChanges.put(new ItemStack(GCItems.basicItem, 1, 15), new ItemStack(GCItems.foodItem, 1, 0));
        GCItems.itemChanges.put(new ItemStack(GCItems.basicItem, 1, 16), new ItemStack(GCItems.foodItem, 1, 1));
        GCItems.itemChanges.put(new ItemStack(GCItems.basicItem, 1, 17), new ItemStack(GCItems.foodItem, 1, 2));
        GCItems.itemChanges.put(new ItemStack(GCItems.basicItem, 1, 18), new ItemStack(GCItems.foodItem, 1, 3));

        GalacticraftCore.proxy.registerCanister(new PartialCanister(GCItems.oilCanister, Constants.MOD_ID_CORE, "oil_canister_partial", 7));
        GalacticraftCore.proxy.registerCanister(new PartialCanister(GCItems.fuelCanister, Constants.MOD_ID_CORE, "fuel_canister_partial", 7));
    }
    
    public static void oreDictRegistrations()
    {
        for (int i = 0; i < ItemBasic.names.length; i++)
        {
            if (ItemBasic.names[i].contains("ingot") || ItemBasic.names[i].contains("compressed") || ItemBasic.names[i].contains("wafer"))
            {
                String name = ItemBasic.names[i];
                while (name.contains("_"))
                {
                    int loc = name.indexOf("_");
                    name = name.substring(0, loc) + name.substring(loc + 1, loc + 2).toUpperCase() + name.substring(loc + 2, name.length());
                }
                OreDictionary.registerOre(name, new ItemStack(GCItems.basicItem, 1, i));
            }
        }

        OreDictionary.registerOre("foodCheese", new ItemStack(GCItems.cheeseCurd, 1, 0));
        OreDictionary.registerOre("compressedMeteoricIron", new ItemStack(GCItems.itemBasicMoon, 1, 1));
        OreDictionary.registerOre("ingotMeteoricIron", new ItemStack(GCItems.itemBasicMoon, 1, 0));
        if (CompatibilityManager.useAluDust())
        {
            OreDictionary.registerOre("dustAluminum", new ItemStack(GCItems.ic2compat, 1, 0));
            OreDictionary.registerOre("dustAluminium", new ItemStack(GCItems.ic2compat, 1, 0));
        }
        if (CompatibilityManager.isIc2Loaded())
        {
            OreDictionary.registerOre("crushedAluminum", new ItemStack(GCItems.ic2compat, 1, 2));
            OreDictionary.registerOre("crushedPurifiedAluminum", new ItemStack(GCItems.ic2compat, 1, 1));
            OreDictionary.registerOre("dustTinyTitanium", new ItemStack(GCItems.ic2compat, 1, 7));
        }

        GalacticraftCore.proxy.registerCanister(new PartialCanister(GCItems.oilCanister, Constants.MOD_ID_CORE, "oil_canister_partial", 7));
        GalacticraftCore.proxy.registerCanister(new PartialCanister(GCItems.fuelCanister, Constants.MOD_ID_CORE, "fuel_canister_partial", 7));
        OreDictionary.registerOre(ConfigManagerCore.otherModsSilicon, new ItemStack(GCItems.basicItem, 1, 2));
    }
    
    

    /**
     * Do not call this until after mod loading is complete
     * because JEI doesn't have an internal item blacklist
     * until it services an FMLLoadCompleteEvent.
     * (Seriously?!)
     */
    public static void hideItemsJEI(IIngredientBlacklist jeiHidden)
    {
        if (jeiHidden != null)
        {
            for (ItemStack item : GCItems.itemChanges.keySet())
            {
                jeiHidden.addIngredientToBlacklist(item.copy());
            }

            for (Item item : GCItems.hiddenItems)
            {
                jeiHidden.addIngredientToBlacklist(new ItemStack(item, 1, 0));
            }

            for (Block block : GCBlocks.hiddenBlocks)
            {
                jeiHidden.addIngredientToBlacklist(new ItemStack(block, 1, 0));
                if (block == GCBlocks.slabGCDouble)
                {
                    for (int j = 1; j < (GalacticraftCore.isPlanetsLoaded ? 7 : 4); j++)
                        jeiHidden.addIngredientToBlacklist(new ItemStack(block, 1, j));
                }
            }
        }
    }

    public static void finalizeSort()
    {
        List<StackSorted> itemOrderListItems = Lists.newArrayList();
        for (EnumSortCategoryItem type : EnumSortCategoryItem.values())
        {
            List stackSorteds = sortMapItems.get(type);
            if (stackSorteds != null)
            {
                itemOrderListItems.addAll(stackSorteds);
            }
            else
            {
                System.out.println("ERROR: null sort stack: " + type.toString());
            }
        }

        Comparator<ItemStack> tabSorterItems = Ordering.explicit(itemOrderListItems).onResultOf(input -> new StackSorted(input.getItem(), input.getItemDamage()));

        GalacticraftCore.galacticraftItemsTab.setTabSorter(tabSorterItems);
    }

    public static void registerHarvestLevels()
    {
        GCItems.steelPickaxe.setHarvestLevel("pickaxe", 4);
        GCItems.steelAxe.setHarvestLevel("axe", 4);
        GCItems.steelSpade.setHarvestLevel("shovel", 4);
    }

    public static void registerSorted(Item item)
    {
        if (item instanceof ISortableItem)
        {
            ISortableItem sortableItem = (ISortableItem) item;
            NonNullList<ItemStack> items = NonNullList.create();
            item.getSubItems(GalacticraftCore.galacticraftItemsTab, items);
            for (ItemStack stack : items)
            {
                EnumSortCategoryItem categoryItem = sortableItem.getCategory(stack.getItemDamage());
                if (!sortMapItems.containsKey(categoryItem))
                {
                    sortMapItems.put(categoryItem, new ArrayList<>());
                }
                sortMapItems.get(categoryItem).add(new StackSorted(stack.getItem(), stack.getItemDamage()));
            }
        }
        else if (item.getCreativeTab() == GalacticraftCore.galacticraftItemsTab)
        {
            throw new RuntimeException(item.getClass() + " must inherit " + ISortableItem.class.getSimpleName() + "!");
        }
    }

    public static void registerItems()
    {
        GCItems.registerItem(GCItems.rocketTier1);
        GCItems.registerItem(GCItems.oxMask);
        GCItems.registerItem(GCItems.oxygenGear);
        GCItems.registerItem(GCItems.oxTankLight);
        GCItems.registerItem(GCItems.oxTankMedium);
        GCItems.registerItem(GCItems.oxTankHeavy);
        GCItems.registerItem(GCItems.oxygenCanisterInfinite);
        GCItems.registerItem(GCItems.sensorLens);
        GCItems.registerItem(GCItems.sensorGlasses);
        GCItems.registerItem(GCItems.wrench);
        GCItems.registerItem(GCItems.steelPickaxe);
        GCItems.registerItem(GCItems.steelAxe);
        GCItems.registerItem(GCItems.steelHoe);
        GCItems.registerItem(GCItems.steelSpade);
        GCItems.registerItem(GCItems.steelSword);
        GCItems.registerItem(GCItems.steelHelmet);
        GCItems.registerItem(GCItems.steelChestplate);
        GCItems.registerItem(GCItems.steelLeggings);
        GCItems.registerItem(GCItems.steelBoots);
        GCItems.registerItem(GCItems.canister);
        GCItems.registerItem(GCItems.oxygenVent);
        GCItems.registerItem(GCItems.oxygenFan);
        GCItems.registerItem(GCItems.oxygenConcentrator);
        GCItems.registerItem(GCItems.rocketEngine);
        GCItems.registerItem(GCItems.heavyPlatingTier1);
        GCItems.registerItem(GCItems.partNoseCone);
        GCItems.registerItem(GCItems.partFins);
        GCItems.registerItem(GCItems.flagPole);
        GCItems.registerItem(GCItems.canvas);
        GCItems.registerItem(GCItems.oilCanister);
        GCItems.registerItem(GCItems.fuelCanister);
        GCItems.registerItem(GCItems.schematic);
        GCItems.registerItem(GCItems.key);
        GCItems.registerItem(GCItems.partBuggy);
        GCItems.registerItem(GCItems.buggy);
        GCItems.registerItem(GCItems.basicItem);
        GCItems.registerItem(GCItems.foodItem);
        GCItems.registerItem(GCItems.battery);
        GCItems.registerItem(GCItems.infiniteBatery);
        GCItems.registerItem(GCItems.meteorChunk);
        GCItems.registerItem(GCItems.cheeseCurd);
        GCItems.registerItem(GCItems.meteoricIronRaw);
        GCItems.registerItem(GCItems.itemBasicMoon);
//		GCItems.registerItem(GCItems.cheeseBlock);
        GCItems.registerItem(GCItems.flag);
        GCItems.registerItem(GCItems.parachute);
        GCItems.registerItem(GCItems.prelaunchChecklist);
        GCItems.registerItem(GCItems.dungeonFinder);
        GCItems.registerItem(GCItems.emergencyKit);
        
        GCItems.canisterTypes.add((ItemCanisterGeneric) GCItems.fuelCanister);
        GCItems.canisterTypes.add((ItemCanisterGeneric) GCItems.oilCanister);
        
        if (CompatibilityManager.useAluDust()) GCItems.registerItem(GCItems.ic2compat);
    }

    public static void registerItem(Item item)
    {
        String name = item.getUnlocalizedName().substring(5);
        if (item.getRegistryName() == null)
        {
            item.setRegistryName(name);
        }
        GCCoreUtil.registerGalacticraftItem(name, item);
        GalacticraftCore.itemListTrue.add(item);
        GalacticraftCore.proxy.postRegisterItem(item);
    }
    
    public static void registerItems(IForgeRegistry<Item> registry)
    {
        for (ItemStack item : GalacticraftCore.itemList)
        {
            registry.register(item.getItem());
        }
    }
}
