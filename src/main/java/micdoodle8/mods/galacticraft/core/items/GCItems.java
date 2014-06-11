package micdoodle8.mods.galacticraft.core.items;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * GCCoreItems.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
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
	public static Item oilExtractor;
	public static Item schematic;
	public static Item key;
	public static Item partBuggy;
	public static Item basicItem;
	public static Item battery;
	public static Item infiniteBatery;
	public static Item meteorChunk;
	public static Item wrench;
	public static Item cheeseCurd;
	public static Item meteoricIronRaw;
	public static Item meteoricIronIngot;
	public static Item cheeseBlock;

	public static ArmorMaterial ARMOR_SENSOR_GLASSES = EnumHelper.addArmorMaterial("SENSORGLASSES", 200, new int[] { 0, 0, 0, 0 }, 0);
	public static ArmorMaterial ARMOR_STEEL = EnumHelper.addArmorMaterial("steel", 30, new int[] { 3, 8, 6, 3 }, 12);
	public static ToolMaterial TOOL_STEEL = EnumHelper.addToolMaterial("steel", 3, 768, 5.0F, 2, 8);

	public static ArrayList<Item> hiddenItems = new ArrayList<Item>();

	public static void initItems()
	{
		GCItems.oxTankLight = new ItemOxygenTank(1, "oxygenTankLightFull");
		GCItems.oxTankMedium = new ItemOxygenTank(2, "oxygenTankMedFull");
		GCItems.oxTankHeavy = new ItemOxygenTank(3, "oxygenTankHeavyFull");
		GCItems.oxMask = new ItemOxygenMask("oxygenMask");
		GCItems.rocketTier1 = new ItemTier1Rocket("spaceship");
		GCItems.sensorGlasses = new ItemSensorGlasses("sensorGlasses");
		GCItems.steelPickaxe = new ItemPickaxeGC("steel_pickaxe");
		GCItems.steelAxe = new ItemAxeGC("steel_axe");
		GCItems.steelHoe = new ItemHoeGC("steel_hoe");
		GCItems.steelSpade = new ItemSpaceGC("steel_shovel");
		GCItems.steelSword = new ItemSwordGC("steel_sword");
		GCItems.steelHelmet = new ItemArmorGC(0, "helmet");
		GCItems.steelChestplate = new ItemArmorGC(1, "chestplate");
		GCItems.steelLeggings = new ItemArmorGC(2, "leggings");
		GCItems.steelBoots = new ItemArmorGC(3, "boots");
		GCItems.canister = new ItemCanister("canister");
		GCItems.oxygenVent = new ItemBase("airVent");
		GCItems.oxygenFan = new ItemBase("airFan");
		GCItems.oxygenConcentrator = new ItemBase("oxygenConcentrator");
		GCItems.heavyPlatingTier1 = new ItemBase("heavyPlating");
		GCItems.rocketEngine = new ItemRocketEngineGC("engine");
		GCItems.partFins = new ItemBase("rocketFins");
		GCItems.partNoseCone = new ItemBase("noseCone");
		GCItems.sensorLens = new ItemBase("sensorLens");
		GCItems.buggy = new ItemBuggy("buggy");
		GCItems.flag = new ItemFlag("flag");
		GCItems.oxygenGear = new ItemOxygenGear("oxygenGear");
		GCItems.parachute = new ItemParaChute("parachute");
		GCItems.canvas = new ItemBase("canvas");
		GCItems.oilCanister = new ItemOilCanister("oilCanisterPartial");
		GCItems.fuelCanister = new ItemFuelCanister("fuelCanisterPartial");
		GCItems.flagPole = new ItemBase("steelPole");
		GCItems.oilExtractor = new ItemOilExtractor("oilExtractor");
		GCItems.schematic = new ItemSchematic("schematic");
		GCItems.key = new ItemKey("key");
		GCItems.partBuggy = new ItemBuggyMaterial("buggymat");
		GCItems.basicItem = new ItemBasic("basicItem");
		GCItems.battery = new ItemBattery("battery");
		GCItems.infiniteBatery = new ItemBatteryInfinite("infiniteBattery");
		GCItems.meteorChunk = new ItemMeteorChunk("meteorChunk");
		GCItems.wrench = new ItemUniversalWrench("standardWrench");
		GCItems.cheeseCurd = new ItemCheese(1, 0.1F, false);
		GCItems.cheeseBlock = new ItemBlockCheese(GCBlocks.cheeseBlock, "cheeseBlock");
		GCItems.meteoricIronRaw = new ItemMeteoricIron("meteoricIronRaw");
		GCItems.meteoricIronIngot = new ItemMoon("meteoricIronIngot");

		GCItems.registerHarvestLevels();

		GCCoreUtil.registerGalacticraftItem("oxygenTankLightFull", GCItems.oxTankLight);
		GCCoreUtil.registerGalacticraftItem("oxygenTankMediumFull", GCItems.oxTankMedium);
		GCCoreUtil.registerGalacticraftItem("oxygenTankHeavyFull", GCItems.oxTankHeavy);
		GCCoreUtil.registerGalacticraftItem("oxygenTankLightEmpty", GCItems.oxTankLight, 90);
		GCCoreUtil.registerGalacticraftItem("oxygenTankMediumEmpty", GCItems.oxTankMedium, 180);
		GCCoreUtil.registerGalacticraftItem("oxygenTankHeavyEmpty", GCItems.oxTankHeavy, 270);
		GCCoreUtil.registerGalacticraftItem("oxygenMask", GCItems.oxMask);
		GCCoreUtil.registerGalacticraftItem("rocketTier1", GCItems.rocketTier1, 0);
		GCCoreUtil.registerGalacticraftItem("rocketTier1_18cargo", GCItems.rocketTier1, 1);
		GCCoreUtil.registerGalacticraftItem("rocketTier1_36cargo", GCItems.rocketTier1, 2);
		GCCoreUtil.registerGalacticraftItem("rocketTier1_54cargo", GCItems.rocketTier1, 3);
		GCCoreUtil.registerGalacticraftItem("rocketTier1_prefueled", GCItems.rocketTier1, 4);
		GCCoreUtil.registerGalacticraftItem("heavyDutyPickaxe", GCItems.steelPickaxe);
		GCCoreUtil.registerGalacticraftItem("heavyDutyShovel", GCItems.steelSpade);
		GCCoreUtil.registerGalacticraftItem("heavyDutyAxe", GCItems.steelAxe);
		GCCoreUtil.registerGalacticraftItem("heavyDutyHoe", GCItems.steelHoe);
		GCCoreUtil.registerGalacticraftItem("heavyDutySword", GCItems.steelSword);
		GCCoreUtil.registerGalacticraftItem("heavyDutyHelmet", GCItems.steelHelmet);
		GCCoreUtil.registerGalacticraftItem("heavyDutyChestplate", GCItems.steelChestplate);
		GCCoreUtil.registerGalacticraftItem("heavyDutyLeggings", GCItems.steelLeggings);
		GCCoreUtil.registerGalacticraftItem("heavyDutyBoots", GCItems.steelBoots);
		GCCoreUtil.registerGalacticraftItem("tinCanister", GCItems.canister, 0);
		GCCoreUtil.registerGalacticraftItem("copperCanister", GCItems.canister, 1);
		GCCoreUtil.registerGalacticraftItem("oxygenVent", GCItems.oxygenVent);
		GCCoreUtil.registerGalacticraftItem("oxygenFan", GCItems.oxygenFan);
		GCCoreUtil.registerGalacticraftItem("oxygenConcentrator", GCItems.oxygenConcentrator);
		GCCoreUtil.registerGalacticraftItem("heavyPlatingTier1", GCItems.heavyPlatingTier1);
		GCCoreUtil.registerGalacticraftItem("rocketEngineTier1", GCItems.rocketEngine, 0);
		GCCoreUtil.registerGalacticraftItem("rocketBoosterTier1", GCItems.rocketEngine, 1);
		GCCoreUtil.registerGalacticraftItem("rocketFins", GCItems.partFins);
		GCCoreUtil.registerGalacticraftItem("rocketNoseCone", GCItems.partNoseCone);
		GCCoreUtil.registerGalacticraftItem("sensorLens", GCItems.sensorLens);
		GCCoreUtil.registerGalacticraftItem("moonBuggy", GCItems.buggy, 0);
		GCCoreUtil.registerGalacticraftItem("moonBuggy_18cargo", GCItems.buggy, 1);
		GCCoreUtil.registerGalacticraftItem("moonBuggy_36cargo", GCItems.buggy, 2);
		GCCoreUtil.registerGalacticraftItem("moonBuggy_54cargo", GCItems.buggy, 3);
		GCCoreUtil.registerGalacticraftItem("flagAmerican", GCItems.flag, 0);
		GCCoreUtil.registerGalacticraftItem("flagBlack", GCItems.flag, 1);
		GCCoreUtil.registerGalacticraftItem("flagLightBlue", GCItems.flag, 2);
		GCCoreUtil.registerGalacticraftItem("flagLime", GCItems.flag, 3);
		GCCoreUtil.registerGalacticraftItem("flagBrown", GCItems.flag, 4);
		GCCoreUtil.registerGalacticraftItem("flagBlue", GCItems.flag, 5);
		GCCoreUtil.registerGalacticraftItem("flagGray", GCItems.flag, 6);
		GCCoreUtil.registerGalacticraftItem("flagGreen", GCItems.flag, 7);
		GCCoreUtil.registerGalacticraftItem("flagLightGray", GCItems.flag, 8);
		GCCoreUtil.registerGalacticraftItem("flagMagenta", GCItems.flag, 9);
		GCCoreUtil.registerGalacticraftItem("flagOrange", GCItems.flag, 10);
		GCCoreUtil.registerGalacticraftItem("flagPink", GCItems.flag, 11);
		GCCoreUtil.registerGalacticraftItem("flagPurple", GCItems.flag, 12);
		GCCoreUtil.registerGalacticraftItem("flagRed", GCItems.flag, 13);
		GCCoreUtil.registerGalacticraftItem("flagCyan", GCItems.flag, 14);
		GCCoreUtil.registerGalacticraftItem("flagYellow", GCItems.flag, 15);
		GCCoreUtil.registerGalacticraftItem("flagWhite", GCItems.flag, 16);
		GCCoreUtil.registerGalacticraftItem("oxygenGear", GCItems.oxygenGear);
		GCCoreUtil.registerGalacticraftItem("parachuteWhite", GCItems.parachute, 0);
		GCCoreUtil.registerGalacticraftItem("parachuteBlack", GCItems.parachute, 1);
		GCCoreUtil.registerGalacticraftItem("parachuteLightBlue", GCItems.parachute, 2);
		GCCoreUtil.registerGalacticraftItem("parachuteLime", GCItems.parachute, 3);
		GCCoreUtil.registerGalacticraftItem("parachuteBrown", GCItems.parachute, 4);
		GCCoreUtil.registerGalacticraftItem("parachuteBlue", GCItems.parachute, 5);
		GCCoreUtil.registerGalacticraftItem("parachuteGray", GCItems.parachute, 6);
		GCCoreUtil.registerGalacticraftItem("parachuteGreen", GCItems.parachute, 7);
		GCCoreUtil.registerGalacticraftItem("parachuteLightGray", GCItems.parachute, 8);
		GCCoreUtil.registerGalacticraftItem("parachutePink", GCItems.parachute, 9);
		GCCoreUtil.registerGalacticraftItem("parachuteOrange", GCItems.parachute, 10);
		GCCoreUtil.registerGalacticraftItem("parachutePink", GCItems.parachute, 11);
		GCCoreUtil.registerGalacticraftItem("parachutePurple", GCItems.parachute, 12);
		GCCoreUtil.registerGalacticraftItem("parachuteRed", GCItems.parachute, 13);
		GCCoreUtil.registerGalacticraftItem("parachuteCyan", GCItems.parachute, 14);
		GCCoreUtil.registerGalacticraftItem("parachuteYellow", GCItems.parachute, 15);
		GCCoreUtil.registerGalacticraftItem("canvas", GCItems.canvas);
		GCCoreUtil.registerGalacticraftItem("fuelCanisterFull", GCItems.fuelCanister, 1);
		GCCoreUtil.registerGalacticraftItem("oilCanisterFull", GCItems.oilCanister, 1);
		GCCoreUtil.registerGalacticraftItem("liquidCanisterEmpty", GCItems.oilCanister, GCItems.oilCanister.getMaxDamage());
		GCCoreUtil.registerGalacticraftItem("steelPole", GCItems.flagPole);
		GCCoreUtil.registerGalacticraftItem("oilExtractor", GCItems.oilExtractor);
		GCCoreUtil.registerGalacticraftItem("schematicMoonBuggy", GCItems.schematic, 0);
		GCCoreUtil.registerGalacticraftItem("schematicRocketTier2", GCItems.schematic, 1);
		GCCoreUtil.registerGalacticraftItem("tier1Key", GCItems.key);
		GCCoreUtil.registerGalacticraftItem("buggyMaterialWheel", GCItems.partBuggy, 0);
		GCCoreUtil.registerGalacticraftItem("buggyMaterialSeat", GCItems.partBuggy, 1);
		GCCoreUtil.registerGalacticraftItem("buggyMaterialStorage", GCItems.partBuggy, 2);
		GCCoreUtil.registerGalacticraftItem("solarModuleSingle", GCItems.basicItem, 0);
		GCCoreUtil.registerGalacticraftItem("solarModuleFull", GCItems.basicItem, 1);
		GCCoreUtil.registerGalacticraftItem("batteryEmpty", GCItems.battery, 100);
		GCCoreUtil.registerGalacticraftItem("batteryFull", GCItems.battery, 0);
		GCCoreUtil.registerGalacticraftItem("infiniteBattery", GCItems.infiniteBatery);
		GCCoreUtil.registerGalacticraftItem("rawSilicon", GCItems.basicItem, 2);
		GCCoreUtil.registerGalacticraftItem("ingotCopper", GCItems.basicItem, 3);
		GCCoreUtil.registerGalacticraftItem("ingotTin", GCItems.basicItem, 4);
		GCCoreUtil.registerGalacticraftItem("ingotAluminum", GCItems.basicItem, 5);
		GCCoreUtil.registerGalacticraftItem("compressedCopper", GCItems.basicItem, 6);
		GCCoreUtil.registerGalacticraftItem("compressedTin", GCItems.basicItem, 7);
		GCCoreUtil.registerGalacticraftItem("compressedAluminum", GCItems.basicItem, 8);
		GCCoreUtil.registerGalacticraftItem("compressedSteel", GCItems.basicItem, 9);
		GCCoreUtil.registerGalacticraftItem("compressedBronze", GCItems.basicItem, 10);
		GCCoreUtil.registerGalacticraftItem("compressedIron", GCItems.basicItem, 11);
		GCCoreUtil.registerGalacticraftItem("waferSolar", GCItems.basicItem, 12);
		GCCoreUtil.registerGalacticraftItem("waferBasic", GCItems.basicItem, 13);
		GCCoreUtil.registerGalacticraftItem("waferAdvanced", GCItems.basicItem, 14);
		GCCoreUtil.registerGalacticraftItem("dehydratedApple", GCItems.basicItem, 15);
		GCCoreUtil.registerGalacticraftItem("dehydratedCarrot", GCItems.basicItem, 16);
		GCCoreUtil.registerGalacticraftItem("dehydratedMelon", GCItems.basicItem, 17);
		GCCoreUtil.registerGalacticraftItem("dehydratedPotato", GCItems.basicItem, 18);
		GCCoreUtil.registerGalacticraftItem("frequencyModule", GCItems.basicItem, 19);
		GCCoreUtil.registerGalacticraftItem("meteorThrowable", GCItems.meteorChunk);
		GCCoreUtil.registerGalacticraftItem("meteorThrowableHot", GCItems.meteorChunk, 1);
		GCCoreUtil.registerGalacticraftItem("standardWrench", GCItems.wrench);

		for (int i = 0; i < ItemBasic.names.length; i++)
		{
			if (ItemBasic.names[i].contains("ingot") || ItemBasic.names[i].contains("compressed") || ItemBasic.names[i].contains("wafer"))
			{
				OreDictionary.registerOre(ItemBasic.names[i], new ItemStack(GCItems.basicItem, 1, i));
			}
		}

		OreDictionary.registerOre("plateMeteoricIron", new ItemStack(GCItems.meteoricIronIngot, 1, 1));
		OreDictionary.registerOre("ingotMeteoricIron", new ItemStack(GCItems.meteoricIronIngot, 1, 0));
		
		registerItems();
	}

	public static void registerHarvestLevels()
	{
//		MinecraftForge.setToolClass(GCCoreItems.steelPickaxe, "pickaxe", 4);
//		MinecraftForge.setToolClass(GCCoreItems.steelAxe, "axe", 4);
//		MinecraftForge.setToolClass(GCCoreItems.steelSpade, "shovel", 4); TODO Set harvest levels
	}

	public static void registerItems()
	{
		registerItem(GCItems.rocketTier1);
		registerItem(GCItems.oxMask);
		registerItem(GCItems.oxygenGear);
		registerItem(GCItems.oxTankLight);
		registerItem(GCItems.oxTankMedium);
		registerItem(GCItems.oxTankHeavy);
		registerItem(GCItems.sensorLens);
		registerItem(GCItems.sensorGlasses);
		registerItem(GCItems.steelPickaxe);
		registerItem(GCItems.steelAxe);
		registerItem(GCItems.steelHoe);
		registerItem(GCItems.steelSpade);
		registerItem(GCItems.steelSword);
		registerItem(GCItems.steelHelmet);
		registerItem(GCItems.steelChestplate);
		registerItem(GCItems.steelLeggings);
		registerItem(GCItems.steelBoots);
		registerItem(GCItems.canister);
		registerItem(GCItems.oxygenVent);
		registerItem(GCItems.oxygenFan);
		registerItem(GCItems.oxygenConcentrator);
		registerItem(GCItems.rocketEngine);
		registerItem(GCItems.heavyPlatingTier1);
		registerItem(GCItems.partNoseCone);
		registerItem(GCItems.partFins);
		registerItem(GCItems.flagPole);
		registerItem(GCItems.canvas);
		registerItem(GCItems.oilCanister);
		registerItem(GCItems.fuelCanister);
		registerItem(GCItems.oilExtractor);
		registerItem(GCItems.schematic);
		registerItem(GCItems.key);
		registerItem(GCItems.partBuggy);
		registerItem(GCItems.buggy);
		registerItem(GCItems.basicItem);
		registerItem(GCItems.battery);
		registerItem(GCItems.infiniteBatery);
		registerItem(GCItems.meteorChunk);
		registerItem(GCItems.wrench);
		registerItem(GCItems.cheeseCurd);
		registerItem(GCItems.meteoricIronRaw);
		registerItem(GCItems.meteoricIronIngot);
		registerItem(GCItems.cheeseBlock);
		registerItem(GCItems.flag);
		registerItem(GCItems.parachute);
	}

	private static void registerItem(Item item)
	{
		GameRegistry.registerItem(item, item.getUnlocalizedName(), GalacticraftCore.MODID);
	}
}
