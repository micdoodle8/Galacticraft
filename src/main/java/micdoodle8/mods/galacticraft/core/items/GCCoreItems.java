package micdoodle8.mods.galacticraft.core.items;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
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
public class GCCoreItems
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
	public static Item knowledgeBook;
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
		HEAVY_DUTY("steel", 30, new int[] { 3, 8, 6, 3}, 12),
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
		GCCoreItems.oxTankLight = new GCCoreItemOxygenTank(1, "oxygenTankLightFull");
		GCCoreItems.oxTankMedium = new GCCoreItemOxygenTank(2, "oxygenTankMedFull");
		GCCoreItems.oxTankHeavy = new GCCoreItemOxygenTank(3, "oxygenTankHeavyFull");
		GCCoreItems.oxMask = new GCCoreItemOxygenMask("oxygenMask");
		GCCoreItems.rocketTier1 = new GCCoreItemSpaceship("spaceship");
		GCCoreItems.sensorGlasses = new GCCoreItemSensorGlasses("sensorGlasses");
		GCCoreItems.steelPickaxe = new GCCoreItemPickaxe(EnumToolType.TOOL_STEEL, "steel_pickaxe");
		GCCoreItems.steelAxe = new GCCoreItemAxe("steel_axe");
		GCCoreItems.steelHoe = new GCCoreItemHoe("steel_hoe");
		GCCoreItems.steelSpade = new GCCoreItemSpade("steel_shovel");
		GCCoreItems.steelSword = new GCCoreItemSword("steel_sword");
		GCCoreItems.steelHelmet = new GCCoreItemArmor(0, EnumArmorIndex.HEAVY_DUTY, "helmet");
		GCCoreItems.steelChestplate = new GCCoreItemArmor(1, EnumArmorIndex.HEAVY_DUTY, "chestplate");
		GCCoreItems.steelLeggings = new GCCoreItemArmor(2, EnumArmorIndex.HEAVY_DUTY, "leggings");
		GCCoreItems.steelBoots = new GCCoreItemArmor(3, EnumArmorIndex.HEAVY_DUTY, "boots");
		GCCoreItems.canister = new GCCoreItemCanister("canister");
		GCCoreItems.oxygenVent = new GCCoreItem("airVent");
		GCCoreItems.oxygenFan = new GCCoreItem("airFan");
		GCCoreItems.oxygenConcentrator = new GCCoreItem("oxygenConcentrator");
		GCCoreItems.bowGravity = new GCCoreItemBow("bow");
		GCCoreItems.heavyPlatingTier1 = new GCCoreItem("heavyPlating");
		GCCoreItems.rocketEngine = new GCCoreItemRocketEngine("engine");
		GCCoreItems.partFins = new GCCoreItem("rocketFins");
		GCCoreItems.partNoseCone = new GCCoreItem("noseCone");
		GCCoreItems.sensorLens = new GCCoreItem("sensorLens");
		GCCoreItems.buggy = new GCCoreItemBuggy("buggy");
		GCCoreItems.flag = new GCCoreItemFlag("flag");
		GCCoreItems.oxygenGear = new GCCoreItemOxygenGear("oxygenGear");
		GCCoreItems.parachute = new GCCoreItemParachute("parachute");
		GCCoreItems.canvas = new GCCoreItem("canvas");
		GCCoreItems.oilCanister = new GCCoreItemOilCanister("oilCanisterPartial");
		GCCoreItems.fuelCanister = new GCCoreItemFuelCanister("fuelCanisterPartial");
		GCCoreItems.flagPole = new GCCoreItem("steelPole");
		GCCoreItems.oilExtractor = new GCCoreItemOilExtractor("oilExtractor");
		GCCoreItems.schematic = new GCCoreItemSchematic("schematic");
		GCCoreItems.key = new GCCoreItemKey("key");
		GCCoreItems.partBuggy = new GCCoreItemBuggyMaterial("buggymat");
		GCCoreItems.knowledgeBook = new GCCoreItemKnowledgeBook("knowledgeBook");
		GCCoreItems.basicItem = new GCCoreItemBasic("basicItem");
		GCCoreItems.battery = new GCCoreItemBattery("battery");
		GCCoreItems.infiniteBatery = new GCCoreItemInfiniteBattery("infiniteBattery");
		GCCoreItems.meteorChunk = new GCCoreItemMeteorChunk("meteorChunk");
		GCCoreItems.wrench = new GCCoreItemWrench("standardWrench");
		GCCoreItems.cheeseCurd = new GCMoonItemCheese(1, 0.1F, false).setUnlocalizedName("cheeseCurd");
		GCCoreItems.cheeseBlock = new GCMoonItemReed(GCCoreBlocks.cheeseBlock).setUnlocalizedName("cheeseBlock");
		GCCoreItems.meteoricIronRaw = new GCMoonItemRawIron("meteoric_iron_raw").setUnlocalizedName("meteoricIronRaw");
		GCCoreItems.meteoricIronIngot = new GCMoonItem().setUnlocalizedName("meteoricIronIngot");

		GCCoreItems.hiddenItems.add(GCCoreItems.bowGravity);
		GCCoreItems.hiddenItems.add(GCCoreItems.knowledgeBook);

		GCCoreItems.registerHarvestLevels();
		GCCoreItems.registerItems();

//		GCCoreUtil.registerGalacticraftItem("oxygenTankLightFull", GCCoreItems.oxTankLight) TODO Register GC items
//		GCCoreUtil.registerGalacticraftItem("oxygenTankMediumFull", GCCoreItems.oxTankMedium)
//		GCCoreUtil.registerGalacticraftItem("oxygenTankHeavyFull", GCCoreItems.oxTankHeavy)
//		GCCoreUtil.registerGalacticraftItem("oxygenTankLightEmpty", GCCoreItems.oxTankLight, 90)
//		GCCoreUtil.registerGalacticraftItem("oxygenTankMediumEmpty", GCCoreItems.oxTankMedium, 180)
//		GCCoreUtil.registerGalacticraftItem("oxygenTankHeavyEmpty", GCCoreItems.oxTankHeavy, 270)
//		GCCoreUtil.registerGalacticraftItem("oxygenMask", GCCoreItems.oxMask)
//		GCCoreUtil.registerGalacticraftItem("rocketTier1", GCCoreItems.rocketTier1, 0)
//		GCCoreUtil.registerGalacticraftItem("rocketTier1_18cargo", GCCoreItems.rocketTier1, 1)
//		GCCoreUtil.registerGalacticraftItem("rocketTier1_36cargo", GCCoreItems.rocketTier1, 2)
//		GCCoreUtil.registerGalacticraftItem("rocketTier1_54cargo", GCCoreItems.rocketTier1, 3)
//		GCCoreUtil.registerGalacticraftItem("rocketTier1_prefueled", GCCoreItems.rocketTier1, 4)
//		GCCoreUtil.registerGalacticraftItem("heavyDutyPickaxe", GCCoreItems.steelPickaxe)
//		GCCoreUtil.registerGalacticraftItem("heavyDutyShovel", GCCoreItems.steelSpade)
//		GCCoreUtil.registerGalacticraftItem("heavyDutyAxe", GCCoreItems.steelAxe)
//		GCCoreUtil.registerGalacticraftItem("heavyDutyHoe", GCCoreItems.steelHoe)
//		GCCoreUtil.registerGalacticraftItem("heavyDutySword", GCCoreItems.steelSword)
//		GCCoreUtil.registerGalacticraftItem("heavyDutyHelmet", GCCoreItems.steelHelmet)
//		GCCoreUtil.registerGalacticraftItem("heavyDutyChestplate", GCCoreItems.steelChestplate)
//		GCCoreUtil.registerGalacticraftItem("heavyDutyLeggings", GCCoreItems.steelLeggings)
//		GCCoreUtil.registerGalacticraftItem("heavyDutyBoots", GCCoreItems.steelBoots)
//		GCCoreUtil.registerGalacticraftItem("tinCanister", GCCoreItems.canister, 0)
//		GCCoreUtil.registerGalacticraftItem("copperCanister", GCCoreItems.canister, 1)
//		GCCoreUtil.registerGalacticraftItem("oxygenVent", GCCoreItems.oxygenVent)
//		GCCoreUtil.registerGalacticraftItem("oxygenFan", GCCoreItems.oxygenFan)
//		GCCoreUtil.registerGalacticraftItem("oxygenConcentrator", GCCoreItems.oxygenConcentrator)
//		GCCoreUtil.registerGalacticraftItem("heavyPlatingTier1", GCCoreItems.heavyPlatingTier1)
//		GCCoreUtil.registerGalacticraftItem("rocketEngineTier1", GCCoreItems.rocketEngine, 0)
//		GCCoreUtil.registerGalacticraftItem("rocketBoosterTier1", GCCoreItems.rocketEngine, 1)
//		GCCoreUtil.registerGalacticraftItem("rocketFins", GCCoreItems.partFins)
//		GCCoreUtil.registerGalacticraftItem("rocketNoseCone", GCCoreItems.partNoseCone)
//		GCCoreUtil.registerGalacticraftItem("sensorLens", GCCoreItems.sensorLens)
//		GCCoreUtil.registerGalacticraftItem("moonBuggy", GCCoreItems.buggy, 0)
//		GCCoreUtil.registerGalacticraftItem("moonBuggy_18cargo", GCCoreItems.buggy, 1)
//		GCCoreUtil.registerGalacticraftItem("moonBuggy_36cargo", GCCoreItems.buggy, 2)
//		GCCoreUtil.registerGalacticraftItem("moonBuggy_54cargo", GCCoreItems.buggy, 3)
//		GCCoreUtil.registerGalacticraftItem("flagAmerican", GCCoreItems.flag, 0)
//		GCCoreUtil.registerGalacticraftItem("flagBlack", GCCoreItems.flag, 1)
//		GCCoreUtil.registerGalacticraftItem("flagLightBlue", GCCoreItems.flag, 2)
//		GCCoreUtil.registerGalacticraftItem("flagLime", GCCoreItems.flag, 3)
//		GCCoreUtil.registerGalacticraftItem("flagBrown", GCCoreItems.flag, 4)
//		GCCoreUtil.registerGalacticraftItem("flagBlue", GCCoreItems.flag, 5)
//		GCCoreUtil.registerGalacticraftItem("flagGray", GCCoreItems.flag, 6)
//		GCCoreUtil.registerGalacticraftItem("flagGreen", GCCoreItems.flag, 7)
//		GCCoreUtil.registerGalacticraftItem("flagLightGray", GCCoreItems.flag, 8)
//		GCCoreUtil.registerGalacticraftItem("flagMagenta", GCCoreItems.flag, 9)
//		GCCoreUtil.registerGalacticraftItem("flagOrange", GCCoreItems.flag, 10)
//		GCCoreUtil.registerGalacticraftItem("flagPink", GCCoreItems.flag, 11)
//		GCCoreUtil.registerGalacticraftItem("flagPurple", GCCoreItems.flag, 12)
//		GCCoreUtil.registerGalacticraftItem("flagRed", GCCoreItems.flag, 13)
//		GCCoreUtil.registerGalacticraftItem("flagCyan", GCCoreItems.flag, 14)
//		GCCoreUtil.registerGalacticraftItem("flagYellow", GCCoreItems.flag, 15)
//		GCCoreUtil.registerGalacticraftItem("flagWhite", GCCoreItems.flag, 16)
//		GCCoreUtil.registerGalacticraftItem("oxygenGear", GCCoreItems.oxygenGear)
//		GCCoreUtil.registerGalacticraftItem("parachuteWhite", GCCoreItems.parachute, 0)
//		GCCoreUtil.registerGalacticraftItem("parachuteBlack", GCCoreItems.parachute, 1)
//		GCCoreUtil.registerGalacticraftItem("parachuteLightBlue", GCCoreItems.parachute, 2)
//		GCCoreUtil.registerGalacticraftItem("parachuteLime", GCCoreItems.parachute, 3)
//		GCCoreUtil.registerGalacticraftItem("parachuteBrown", GCCoreItems.parachute, 4)
//		GCCoreUtil.registerGalacticraftItem("parachuteBlue", GCCoreItems.parachute, 5)
//		GCCoreUtil.registerGalacticraftItem("parachuteGray", GCCoreItems.parachute, 6)
//		GCCoreUtil.registerGalacticraftItem("parachuteGreen", GCCoreItems.parachute, 7)
//		GCCoreUtil.registerGalacticraftItem("parachuteLightGray", GCCoreItems.parachute, 8)
//		GCCoreUtil.registerGalacticraftItem("parachutePink", GCCoreItems.parachute, 9)
//		GCCoreUtil.registerGalacticraftItem("parachuteOrange", GCCoreItems.parachute, 10)
//		GCCoreUtil.registerGalacticraftItem("parachutePink", GCCoreItems.parachute, 11)
//		GCCoreUtil.registerGalacticraftItem("parachutePurple", GCCoreItems.parachute, 12)
//		GCCoreUtil.registerGalacticraftItem("parachuteRed", GCCoreItems.parachute, 13)
//		GCCoreUtil.registerGalacticraftItem("parachuteCyan", GCCoreItems.parachute, 14)
//		GCCoreUtil.registerGalacticraftItem("parachuteYellow", GCCoreItems.parachute, 15)
//		GCCoreUtil.registerGalacticraftItem("canvas", GCCoreItems.canvas)
//		GCCoreUtil.registerGalacticraftItem("fuelCanisterFull", GCCoreItems.fuelCanister, 1)
//		GCCoreUtil.registerGalacticraftItem("oilCanisterFull", GCCoreItems.oilCanister, 1)
//		GCCoreUtil.registerGalacticraftItem("liquidCanisterEmpty", GCCoreItems.oilCanister, GCCoreItems.oilCanister.getMaxDamage())
//		GCCoreUtil.registerGalacticraftItem("steelPole", GCCoreItems.flagPole)
//		GCCoreUtil.registerGalacticraftItem("oilExtractor", GCCoreItems.oilExtractor)
//		GCCoreUtil.registerGalacticraftItem("schematicMoonBuggy", GCCoreItems.schematic, 0)
//		GCCoreUtil.registerGalacticraftItem("schematicRocketTier2", GCCoreItems.schematic, 1)
//		GCCoreUtil.registerGalacticraftItem("tier1Key", GCCoreItems.key)
//		GCCoreUtil.registerGalacticraftItem("buggyMaterialWheel", GCCoreItems.partBuggy, 0)
//		GCCoreUtil.registerGalacticraftItem("buggyMaterialSeat", GCCoreItems.partBuggy, 1)
//		GCCoreUtil.registerGalacticraftItem("buggyMaterialStorage", GCCoreItems.partBuggy, 2)
//		GCCoreUtil.registerGalacticraftItem("solarModuleSingle", GCCoreItems.basicItem, 0)
//		GCCoreUtil.registerGalacticraftItem("solarModuleFull", GCCoreItems.basicItem, 1)
//		GCCoreUtil.registerGalacticraftItem("batteryEmpty", GCCoreItems.battery, 100)
//		GCCoreUtil.registerGalacticraftItem("batteryFull", GCCoreItems.battery, 0)
//		GCCoreUtil.registerGalacticraftItem("infiniteBattery", GCCoreItems.infiniteBatery)
//		GCCoreUtil.registerGalacticraftItem("rawSilicon", GCCoreItems.basicItem, 2)
//		GCCoreUtil.registerGalacticraftItem("ingotCopper", GCCoreItems.basicItem, 3)
//		GCCoreUtil.registerGalacticraftItem("ingotTin", GCCoreItems.basicItem, 4)
//		GCCoreUtil.registerGalacticraftItem("ingotAluminum", GCCoreItems.basicItem, 5)
//		GCCoreUtil.registerGalacticraftItem("compressedCopper", GCCoreItems.basicItem, 6)
//		GCCoreUtil.registerGalacticraftItem("compressedTin", GCCoreItems.basicItem, 7)
//		GCCoreUtil.registerGalacticraftItem("compressedAluminum", GCCoreItems.basicItem, 8)
//		GCCoreUtil.registerGalacticraftItem("compressedSteel", GCCoreItems.basicItem, 9)
//		GCCoreUtil.registerGalacticraftItem("compressedBronze", GCCoreItems.basicItem, 10)
//		GCCoreUtil.registerGalacticraftItem("compressedIron", GCCoreItems.basicItem, 11)
//		GCCoreUtil.registerGalacticraftItem("waferSolar", GCCoreItems.basicItem, 12)
//		GCCoreUtil.registerGalacticraftItem("waferBasic", GCCoreItems.basicItem, 13)
//		GCCoreUtil.registerGalacticraftItem("waferAdvanced", GCCoreItems.basicItem, 14)
//		GCCoreUtil.registerGalacticraftItem("dehydratedApple", GCCoreItems.basicItem, 15)
//		GCCoreUtil.registerGalacticraftItem("dehydratedCarrot", GCCoreItems.basicItem, 16)
//		GCCoreUtil.registerGalacticraftItem("dehydratedMelon", GCCoreItems.basicItem, 17)
//		GCCoreUtil.registerGalacticraftItem("dehydratedPotato", GCCoreItems.basicItem, 18)
//		GCCoreUtil.registerGalacticraftItem("frequencyModule", GCCoreItems.basicItem, 19)
//		GCCoreUtil.registerGalacticraftItem("meteorThrowable", GCCoreItems.meteorChunk)
//		GCCoreUtil.registerGalacticraftItem("meteorThrowableHot", GCCoreItems.meteorChunk, 1)
//		GCCoreUtil.registerGalacticraftItem("standardWrench", GCCoreItems.wrench)

		for (int i = 0; i < GCCoreItemBasic.names.length; i++)
		{
			if (GCCoreItemBasic.names[i].contains("ingot") || GCCoreItemBasic.names[i].contains("compressed") || GCCoreItemBasic.names[i].contains("wafer"))
			{
				OreDictionary.registerOre(GCCoreItemBasic.names[i], new ItemStack(GCCoreItems.basicItem, 1, i));
			}
		}

		OreDictionary.registerOre("plateMeteoricIron", new ItemStack(GCCoreItems.meteoricIronIngot, 1, 1));
		OreDictionary.registerOre("ingotMeteoricIron", new ItemStack(GCCoreItems.meteoricIronIngot, 1, 0));
	}

	public static void registerHarvestLevels()
	{
//		MinecraftForge.setToolClass(GCCoreItems.steelPickaxe, "pickaxe", 4) TODO Harvest levels
//		MinecraftForge.setToolClass(GCCoreItems.steelAxe, "axe", 4)
//		MinecraftForge.setToolClass(GCCoreItems.steelSpade, "shovel", 4)
	}
	
	public static void registerItems()
	{
		GameRegistry.registerItem(oxTankLight, oxTankLight.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(oxTankMedium, oxTankMedium.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(oxTankHeavy, oxTankHeavy.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(oxMask, oxMask.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(rocketTier1, rocketTier1.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(sensorGlasses, sensorGlasses.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(sensorLens, sensorLens.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(steelPickaxe, steelPickaxe.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(steelAxe, steelAxe.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(steelHoe, steelHoe.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(steelSpade, steelSpade.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(steelSword, steelSword.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(steelHelmet, steelHelmet.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(steelChestplate, steelChestplate.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(steelLeggings, steelLeggings.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(steelBoots, steelBoots.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(canister, canister.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(oxygenVent, oxygenVent.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(oxygenFan, oxygenFan.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(oxygenConcentrator, oxygenConcentrator.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(bowGravity, bowGravity.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(rocketEngine, rocketEngine.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(heavyPlatingTier1, heavyPlatingTier1.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(partNoseCone, partNoseCone.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(partFins, partFins.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(buggy, buggy.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(flag, flag.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(oxygenGear, oxygenGear.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(parachute, parachute.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(canvas, canvas.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(flagPole, flagPole.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(oilCanister, oilCanister.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(fuelCanister, fuelCanister.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(oilExtractor, oilExtractor.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(schematic, schematic.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(key, key.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(partBuggy, partBuggy.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(knowledgeBook, knowledgeBook.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(basicItem, basicItem.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(battery, battery.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(infiniteBatery, infiniteBatery.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(meteorChunk, meteorChunk.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(wrench, wrench.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(cheeseCurd, cheeseCurd.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(meteoricIronRaw, meteoricIronRaw.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(meteoricIronIngot, meteoricIronIngot.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerItem(cheeseBlock, cheeseBlock.getUnlocalizedName(), GalacticraftCore.MOD_ID);
	}
}
