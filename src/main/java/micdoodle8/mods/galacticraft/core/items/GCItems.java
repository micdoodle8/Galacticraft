package micdoodle8.mods.galacticraft.core.items;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.CoreUtil;
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
	public static Item bowGravity;
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

	public static enum EnumArmorIndex
	{
		HEAVY_DUTY("steel", 30, new int[] { 3, 8, 6, 3 }, 12),
		SENSOR_GLASSES("sensor_glasses", 200, new int[] { 0, 0, 0, 0 }, 0);

		EnumArmorIndex(String name, int durability, int[] reductionAmounts, int enchantability)
		{
			this.name = name;
			this.durability = durability;
			this.reductionAmounts = reductionAmounts;
			this.enchantability = enchantability;
		}

		public ArmorMaterial getMaterial()
		{
			if (this.material == null)
			{
				this.material = EnumHelper.addArmorMaterial(this.name, this.durability, this.reductionAmounts, this.enchantability);
			}

			return this.material;
		}

		public int getRenderIndex()
		{
			return GalacticraftCore.proxy.getArmorRenderID(this);
		}

		private ArmorMaterial material;
		private String name;
		private int durability;
		private int[] reductionAmounts;
		private int enchantability;
	}

	public static enum EnumToolType
	{
		TOOL_STEEL("steel", 3, 768, 5.0F, 2, 8);

		EnumToolType(String name, int harvestLevel, int maxUses, float efficiency, float damage, int enchantability)
		{
			this.name = name;
			this.harvestLevel = harvestLevel;
			this.maxUses = maxUses;
			this.efficiency = efficiency;
			this.damage = damage;
			this.enchantability = enchantability;
		}

		public ToolMaterial getMaterial()
		{
			if (this.material == null)
			{
				this.material = EnumHelper.addToolMaterial(this.name, this.harvestLevel, this.maxUses, this.efficiency, this.damage, this.enchantability);
			}

			return this.material;
		}

		private ToolMaterial material;
		private String name;
		private int harvestLevel;
		private int maxUses;
		private float efficiency;
		private float damage;
		private int enchantability;
	}

	public static ArrayList<Item> hiddenItems = new ArrayList<Item>();

	public static void initItems()
	{
		GCItems.oxTankLight = new ItemOxygenTank(1, "oxygenTankLightFull");
		GCItems.oxTankMedium = new ItemOxygenTank(2, "oxygenTankMedFull");
		GCItems.oxTankHeavy = new ItemOxygenTank(3, "oxygenTankHeavyFull");
		GCItems.oxMask = new ItemOxygenMask("oxygenMask");
		GCItems.rocketTier1 = new ItemRocketTier1("spaceship");
		GCItems.sensorGlasses = new ItemSensorGlasses("sensorGlasses");
		GCItems.steelPickaxe = new ItemPickaxeGC(EnumToolType.TOOL_STEEL, "steel_pickaxe");
		GCItems.steelAxe = new ItemAxeGC(EnumToolType.TOOL_STEEL, "steel_axe");
		GCItems.steelHoe = new ItemHoeGC(EnumToolType.TOOL_STEEL, "steel_hoe");
		GCItems.steelSpade = new ItemSpadeGC(EnumToolType.TOOL_STEEL, "steel_shovel");
		GCItems.steelSword = new ItemSwordGC(EnumToolType.TOOL_STEEL, "steel_sword");
		GCItems.steelHelmet = new ItemArmorGC(0, EnumArmorIndex.HEAVY_DUTY, "helmet");
		GCItems.steelChestplate = new ItemArmorGC(1, EnumArmorIndex.HEAVY_DUTY, "chestplate");
		GCItems.steelLeggings = new ItemArmorGC(2, EnumArmorIndex.HEAVY_DUTY, "leggings");
		GCItems.steelBoots = new ItemArmorGC(3, EnumArmorIndex.HEAVY_DUTY, "boots");
		GCItems.canister = new ItemCanister("canister");
		GCItems.oxygenVent = new ItemSimple("airVent");
		GCItems.oxygenFan = new ItemSimple("airFan");
		GCItems.oxygenConcentrator = new ItemSimple("oxygenConcentrator");
		GCItems.bowGravity = new ItemBowGC("bow");
		GCItems.heavyPlatingTier1 = new ItemSimple("heavyPlating");
		GCItems.rocketEngine = new ItemRocketEngine("engine");
		GCItems.partFins = new ItemSimple("rocketFins");
		GCItems.partNoseCone = new ItemSimple("noseCone");
		GCItems.sensorLens = new ItemSimple("sensorLens");
		GCItems.buggy = new ItemBuggy("buggy");
		GCItems.flag = new ItemFlag("flag");
		GCItems.oxygenGear = new ItemOxygenGear("oxygenGear");
		GCItems.parachute = new ItemParachute("parachute");
		GCItems.canvas = new ItemSimple("canvas");
		GCItems.oilCanister = new ItemOilCanister("oilCanisterPartial");
		GCItems.fuelCanister = new ItemFuelCanister("fuelCanisterPartial");
		GCItems.flagPole = new ItemSimple("steelPole");
		GCItems.oilExtractor = new ItemOilExtractor("oilExtractor");
		GCItems.schematic = new ItemSchematic("schematic");
		GCItems.key = new ItemKey("key");
		GCItems.partBuggy = new ItemBuggyMaterial("buggymat");
		GCItems.basicItem = new ItemBasic("basicItem");
		GCItems.battery = new ItemBattery("battery");
		GCItems.infiniteBatery = new ItemBatteryInf("infiniteBattery");
		GCItems.meteorChunk = new ItemThrowableMeteor("meteorChunk");
		GCItems.wrench = new ItemStandardWrench("standardWrench");
		GCItems.cheeseCurd = new ItemCheeseCurd(1, 0.1F, false).setUnlocalizedName("cheeseCurd");
		GCItems.cheeseBlock = new ItemCheeseCube(GCBlocks.cheeseBlock).setUnlocalizedName("cheeseBlock");
		GCItems.meteoricIronRaw = new ItemMeteoricIronRaw("meteoric_iron_raw").setUnlocalizedName("meteoricIronRaw");
		GCItems.meteoricIronIngot = new ItemMoon().setUnlocalizedName("meteoricIronIngot");

		GCItems.hiddenItems.add(GCItems.bowGravity);

		GCItems.registerItems();

		CoreUtil.registerGalacticraftItem("oxygenTankLightFull", GCItems.oxTankLight);
		CoreUtil.registerGalacticraftItem("oxygenTankMediumFull", GCItems.oxTankMedium);
		CoreUtil.registerGalacticraftItem("oxygenTankHeavyFull", GCItems.oxTankHeavy);
		CoreUtil.registerGalacticraftItem("oxygenTankLightEmpty", GCItems.oxTankLight, 90);
		CoreUtil.registerGalacticraftItem("oxygenTankMediumEmpty", GCItems.oxTankMedium, 180);
		CoreUtil.registerGalacticraftItem("oxygenTankHeavyEmpty", GCItems.oxTankHeavy, 270);
		CoreUtil.registerGalacticraftItem("oxygenMask", GCItems.oxMask);
		CoreUtil.registerGalacticraftItem("rocketTier1", GCItems.rocketTier1, 0);
		CoreUtil.registerGalacticraftItem("rocketTier1_18cargo", GCItems.rocketTier1, 1);
		CoreUtil.registerGalacticraftItem("rocketTier1_36cargo", GCItems.rocketTier1, 2);
		CoreUtil.registerGalacticraftItem("rocketTier1_54cargo", GCItems.rocketTier1, 3);
		CoreUtil.registerGalacticraftItem("rocketTier1_prefueled", GCItems.rocketTier1, 4);
		CoreUtil.registerGalacticraftItem("heavyDutyPickaxe", GCItems.steelPickaxe);
		CoreUtil.registerGalacticraftItem("heavyDutyShovel", GCItems.steelSpade);
		CoreUtil.registerGalacticraftItem("heavyDutyAxe", GCItems.steelAxe);
		CoreUtil.registerGalacticraftItem("heavyDutyHoe", GCItems.steelHoe);
		CoreUtil.registerGalacticraftItem("heavyDutySword", GCItems.steelSword);
		CoreUtil.registerGalacticraftItem("heavyDutyHelmet", GCItems.steelHelmet);
		CoreUtil.registerGalacticraftItem("heavyDutyChestplate", GCItems.steelChestplate);
		CoreUtil.registerGalacticraftItem("heavyDutyLeggings", GCItems.steelLeggings);
		CoreUtil.registerGalacticraftItem("heavyDutyBoots", GCItems.steelBoots);
		CoreUtil.registerGalacticraftItem("tinCanister", GCItems.canister, 0);
		CoreUtil.registerGalacticraftItem("copperCanister", GCItems.canister, 1);
		CoreUtil.registerGalacticraftItem("oxygenVent", GCItems.oxygenVent);
		CoreUtil.registerGalacticraftItem("oxygenFan", GCItems.oxygenFan);
		CoreUtil.registerGalacticraftItem("oxygenConcentrator", GCItems.oxygenConcentrator);
		CoreUtil.registerGalacticraftItem("heavyPlatingTier1", GCItems.heavyPlatingTier1);
		CoreUtil.registerGalacticraftItem("rocketEngineTier1", GCItems.rocketEngine, 0);
		CoreUtil.registerGalacticraftItem("rocketBoosterTier1", GCItems.rocketEngine, 1);
		CoreUtil.registerGalacticraftItem("rocketFins", GCItems.partFins);
		CoreUtil.registerGalacticraftItem("rocketNoseCone", GCItems.partNoseCone);
		CoreUtil.registerGalacticraftItem("sensorLens", GCItems.sensorLens);
		CoreUtil.registerGalacticraftItem("moonBuggy", GCItems.buggy, 0);
		CoreUtil.registerGalacticraftItem("moonBuggy_18cargo", GCItems.buggy, 1);
		CoreUtil.registerGalacticraftItem("moonBuggy_36cargo", GCItems.buggy, 2);
		CoreUtil.registerGalacticraftItem("moonBuggy_54cargo", GCItems.buggy, 3);
		CoreUtil.registerGalacticraftItem("flagAmerican", GCItems.flag, 0);
		CoreUtil.registerGalacticraftItem("flagBlack", GCItems.flag, 1);
		CoreUtil.registerGalacticraftItem("flagLightBlue", GCItems.flag, 2);
		CoreUtil.registerGalacticraftItem("flagLime", GCItems.flag, 3);
		CoreUtil.registerGalacticraftItem("flagBrown", GCItems.flag, 4);
		CoreUtil.registerGalacticraftItem("flagBlue", GCItems.flag, 5);
		CoreUtil.registerGalacticraftItem("flagGray", GCItems.flag, 6);
		CoreUtil.registerGalacticraftItem("flagGreen", GCItems.flag, 7);
		CoreUtil.registerGalacticraftItem("flagLightGray", GCItems.flag, 8);
		CoreUtil.registerGalacticraftItem("flagMagenta", GCItems.flag, 9);
		CoreUtil.registerGalacticraftItem("flagOrange", GCItems.flag, 10);
		CoreUtil.registerGalacticraftItem("flagPink", GCItems.flag, 11);
		CoreUtil.registerGalacticraftItem("flagPurple", GCItems.flag, 12);
		CoreUtil.registerGalacticraftItem("flagRed", GCItems.flag, 13);
		CoreUtil.registerGalacticraftItem("flagCyan", GCItems.flag, 14);
		CoreUtil.registerGalacticraftItem("flagYellow", GCItems.flag, 15);
		CoreUtil.registerGalacticraftItem("flagWhite", GCItems.flag, 16);
		CoreUtil.registerGalacticraftItem("oxygenGear", GCItems.oxygenGear);
		CoreUtil.registerGalacticraftItem("parachuteWhite", GCItems.parachute, 0);
		CoreUtil.registerGalacticraftItem("parachuteBlack", GCItems.parachute, 1);
		CoreUtil.registerGalacticraftItem("parachuteLightBlue", GCItems.parachute, 2);
		CoreUtil.registerGalacticraftItem("parachuteLime", GCItems.parachute, 3);
		CoreUtil.registerGalacticraftItem("parachuteBrown", GCItems.parachute, 4);
		CoreUtil.registerGalacticraftItem("parachuteBlue", GCItems.parachute, 5);
		CoreUtil.registerGalacticraftItem("parachuteGray", GCItems.parachute, 6);
		CoreUtil.registerGalacticraftItem("parachuteGreen", GCItems.parachute, 7);
		CoreUtil.registerGalacticraftItem("parachuteLightGray", GCItems.parachute, 8);
		CoreUtil.registerGalacticraftItem("parachutePink", GCItems.parachute, 9);
		CoreUtil.registerGalacticraftItem("parachuteOrange", GCItems.parachute, 10);
		CoreUtil.registerGalacticraftItem("parachutePink", GCItems.parachute, 11);
		CoreUtil.registerGalacticraftItem("parachutePurple", GCItems.parachute, 12);
		CoreUtil.registerGalacticraftItem("parachuteRed", GCItems.parachute, 13);
		CoreUtil.registerGalacticraftItem("parachuteCyan", GCItems.parachute, 14);
		CoreUtil.registerGalacticraftItem("parachuteYellow", GCItems.parachute, 15);
		CoreUtil.registerGalacticraftItem("canvas", GCItems.canvas);
		CoreUtil.registerGalacticraftItem("fuelCanisterFull", GCItems.fuelCanister, 1);
		CoreUtil.registerGalacticraftItem("oilCanisterFull", GCItems.oilCanister, 1);
		CoreUtil.registerGalacticraftItem("liquidCanisterEmpty", GCItems.oilCanister, GCItems.oilCanister.getMaxDamage());
		CoreUtil.registerGalacticraftItem("steelPole", GCItems.flagPole);
		CoreUtil.registerGalacticraftItem("oilExtractor", GCItems.oilExtractor);
		CoreUtil.registerGalacticraftItem("schematicMoonBuggy", GCItems.schematic, 0);
		CoreUtil.registerGalacticraftItem("schematicRocketTier2", GCItems.schematic, 1);
		CoreUtil.registerGalacticraftItem("tier1Key", GCItems.key);
		CoreUtil.registerGalacticraftItem("buggyMaterialWheel", GCItems.partBuggy, 0);
		CoreUtil.registerGalacticraftItem("buggyMaterialSeat", GCItems.partBuggy, 1);
		CoreUtil.registerGalacticraftItem("buggyMaterialStorage", GCItems.partBuggy, 2);
		CoreUtil.registerGalacticraftItem("solarModuleSingle", GCItems.basicItem, 0);
		CoreUtil.registerGalacticraftItem("solarModuleFull", GCItems.basicItem, 1);
		CoreUtil.registerGalacticraftItem("batteryEmpty", GCItems.battery, 100);
		CoreUtil.registerGalacticraftItem("batteryFull", GCItems.battery, 0);
		CoreUtil.registerGalacticraftItem("infiniteBattery", GCItems.infiniteBatery);
		CoreUtil.registerGalacticraftItem("rawSilicon", GCItems.basicItem, 2);
		CoreUtil.registerGalacticraftItem("ingotCopper", GCItems.basicItem, 3);
		CoreUtil.registerGalacticraftItem("ingotTin", GCItems.basicItem, 4);
		CoreUtil.registerGalacticraftItem("ingotAluminum", GCItems.basicItem, 5);
		CoreUtil.registerGalacticraftItem("compressedCopper", GCItems.basicItem, 6);
		CoreUtil.registerGalacticraftItem("compressedTin", GCItems.basicItem, 7);
		CoreUtil.registerGalacticraftItem("compressedAluminum", GCItems.basicItem, 8);
		CoreUtil.registerGalacticraftItem("compressedSteel", GCItems.basicItem, 9);
		CoreUtil.registerGalacticraftItem("compressedBronze", GCItems.basicItem, 10);
		CoreUtil.registerGalacticraftItem("compressedIron", GCItems.basicItem, 11);
		CoreUtil.registerGalacticraftItem("waferSolar", GCItems.basicItem, 12);
		CoreUtil.registerGalacticraftItem("waferBasic", GCItems.basicItem, 13);
		CoreUtil.registerGalacticraftItem("waferAdvanced", GCItems.basicItem, 14);
		CoreUtil.registerGalacticraftItem("dehydratedApple", GCItems.basicItem, 15);
		CoreUtil.registerGalacticraftItem("dehydratedCarrot", GCItems.basicItem, 16);
		CoreUtil.registerGalacticraftItem("dehydratedMelon", GCItems.basicItem, 17);
		CoreUtil.registerGalacticraftItem("dehydratedPotato", GCItems.basicItem, 18);
		CoreUtil.registerGalacticraftItem("frequencyModule", GCItems.basicItem, 19);
		CoreUtil.registerGalacticraftItem("meteorThrowable", GCItems.meteorChunk);
		CoreUtil.registerGalacticraftItem("meteorThrowableHot", GCItems.meteorChunk, 1);
		CoreUtil.registerGalacticraftItem("standardWrench", GCItems.wrench);

		for (int i = 0; i < ItemBasic.names.length; i++)
		{
			if (ItemBasic.names[i].contains("ingot") || ItemBasic.names[i].contains("compressed") || ItemBasic.names[i].contains("wafer"))
			{
				OreDictionary.registerOre(ItemBasic.names[i], new ItemStack(GCItems.basicItem, 1, i));
			}
		}

		OreDictionary.registerOre("plateMeteoricIron", new ItemStack(GCItems.meteoricIronIngot, 1, 1));
		OreDictionary.registerOre("ingotMeteoricIron", new ItemStack(GCItems.meteoricIronIngot, 1, 0));
	}

	public static void registerItems()
	{
		GameRegistry.registerItem(GCItems.oxTankLight, GCItems.oxTankLight.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.oxTankMedium, GCItems.oxTankMedium.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.oxTankHeavy, GCItems.oxTankHeavy.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.oxMask, GCItems.oxMask.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.rocketTier1, GCItems.rocketTier1.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.sensorGlasses, GCItems.sensorGlasses.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.sensorLens, GCItems.sensorLens.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.steelPickaxe, GCItems.steelPickaxe.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.steelAxe, GCItems.steelAxe.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.steelHoe, GCItems.steelHoe.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.steelSpade, GCItems.steelSpade.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.steelSword, GCItems.steelSword.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.steelHelmet, GCItems.steelHelmet.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.steelChestplate, GCItems.steelChestplate.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.steelLeggings, GCItems.steelLeggings.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.steelBoots, GCItems.steelBoots.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.canister, GCItems.canister.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.oxygenVent, GCItems.oxygenVent.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.oxygenFan, GCItems.oxygenFan.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.oxygenConcentrator, GCItems.oxygenConcentrator.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.bowGravity, GCItems.bowGravity.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.rocketEngine, GCItems.rocketEngine.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.heavyPlatingTier1, GCItems.heavyPlatingTier1.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.partNoseCone, GCItems.partNoseCone.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.partFins, GCItems.partFins.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.buggy, GCItems.buggy.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.flag, GCItems.flag.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.oxygenGear, GCItems.oxygenGear.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.parachute, GCItems.parachute.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.canvas, GCItems.canvas.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.flagPole, GCItems.flagPole.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.oilCanister, GCItems.oilCanister.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.fuelCanister, GCItems.fuelCanister.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.oilExtractor, GCItems.oilExtractor.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.schematic, GCItems.schematic.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.key, GCItems.key.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.partBuggy, GCItems.partBuggy.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.basicItem, GCItems.basicItem.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.battery, GCItems.battery.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.infiniteBatery, GCItems.infiniteBatery.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.meteorChunk, GCItems.meteorChunk.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.wrench, GCItems.wrench.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.cheeseCurd, GCItems.cheeseCurd.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.meteoricIronRaw, GCItems.meteoricIronRaw.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.meteoricIronIngot, GCItems.meteoricIronIngot.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(GCItems.cheeseBlock, GCItems.cheeseBlock.getUnlocalizedName(), GalacticraftCore.MOD_ID);
	}
}
