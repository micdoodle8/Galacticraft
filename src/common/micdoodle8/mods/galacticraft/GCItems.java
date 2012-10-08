package micdoodle8.mods.galacticraft;

import net.minecraft.src.Block;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBucketMilk;
import net.minecraftforge.common.EnumHelper;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCItems 
{
	public static Item lightOxygenTankFull;
	public static Item medOxygenTankFull;
	public static Item heavyOxygenTankFull;
	public static Item lightOxygenTankEmpty;
	public static Item medOxygenTankEmpty;
	public static Item heavyOxygenTankEmpty;
	public static Item oxygenMask;
	public static Item spaceship;
	public static Item sensorGlasses;
	public static Item sensorGlassesWithOxygenMask;
	public static Item reinforcedBucketEmpty;
	public static Item reinforcedBucketMilk;
	public static Item reinforcedBucketWater;
	public static Item reinforcedBucketLava;
	public static Item reinforcedBucketBacteria;
	
	static EnumArmorMaterial OXYGENMASK = EnumHelper.addArmorMaterial("OXYGENMASK", 200, new int[] {0, 0, 0, 0}, 0);
	static EnumArmorMaterial SENSORGLASSES = EnumHelper.addArmorMaterial("SENSORGLASSES", 200, new int[] {0, 0, 0, 0}, 0);
	
	public static void initItems() 
	{
		lightOxygenTankFull = new GCItem(GCConfigManager.idItemLightOxygenTank).setMaxDamage(90).setIconIndex(0).setItemName("lightFull");
		lightOxygenTankEmpty = new GCItem(GCConfigManager.idItemLightOxygenTankEmpty).setIconIndex(3).setItemName("lightEmpty");
		medOxygenTankFull = new GCItem(GCConfigManager.idItemMedOxygenTank).setMaxDamage(90).setIconIndex(1).setItemName("medFull");
		medOxygenTankEmpty = new GCItem(GCConfigManager.idItemMedOxygenTankEmpty).setIconIndex(4).setItemName("medEmpty");
		heavyOxygenTankFull = new GCItem(GCConfigManager.idItemHeavyOxygenTank).setMaxDamage(90).setIconIndex(2).setItemName("heavyFull");
		heavyOxygenTankEmpty = new GCItem(GCConfigManager.idItemHeavyOxygenTankEmpty).setIconIndex(5).setItemName("heavyEmpty");
		oxygenMask = new GCItemOxygenMask(GCConfigManager.idArmorOxygenMask, OXYGENMASK, 5, 0).setIconIndex(7).setItemName("oxygenMask");
		spaceship = new GCItemSpaceship(GCConfigManager.idItemSpaceship).setIconIndex(8).setItemName("spaceship");
		sensorGlasses = new GCItemSensorGlasses(GCConfigManager.idItemSensorGlasses, SENSORGLASSES, 6, 0, false).setIconIndex(9).setItemName("sensorGlasses");
		sensorGlassesWithOxygenMask = new GCItemSensorGlasses(GCConfigManager.idItemSensorGlassesWithOxygenMask, SENSORGLASSES, 7, 0, true).setIconIndex(10).setItemName("sensorGlassesWithOxygenMask");
		reinforcedBucketEmpty = new GCItemReinforcedBucket(GCConfigManager.idItemReinforcedBucket, 0).setIconIndex(11).setItemName("reinfocedBucket");
		reinforcedBucketMilk = new GCItemReinforcedBucket(GCConfigManager.idItemReinforcedBucketMilk, 1).setIconIndex(14).setItemName("reinfocedBucketMilk");
		reinforcedBucketWater = new GCItemReinforcedBucket(GCConfigManager.idItemReinforcedBucketWater, Block.waterMoving.blockID).setIconIndex(12).setItemName("reinfocedBucketWater");
		reinforcedBucketLava = new GCItemReinforcedBucket(GCConfigManager.idItemReinforcedBucketLava, Block.lavaMoving.blockID).setIconIndex(13).setItemName("reinfocedBucketLava");
		reinforcedBucketBacteria = new GCItemReinforcedBucket(GCConfigManager.idItemReinforcedBucketBacteria, GCBlocks.bacterialSludgeMoving.blockID).setIconIndex(15).setItemName("reinfocedBucketSludge");
	}

	public static void addNames() 
	{
		LanguageRegistry.instance().addNameForObject(oxygenMask, "en_US", "Oxygen Mask");
		LanguageRegistry.instance().addNameForObject(spaceship, "en_US", "Spaceship");
		LanguageRegistry.instance().addNameForObject(lightOxygenTankFull, "en_US", "Light Oxygen Tank");
		LanguageRegistry.instance().addNameForObject(lightOxygenTankEmpty, "en_US", "Empty Light Oxygen Tank");
		LanguageRegistry.instance().addNameForObject(medOxygenTankFull, "en_US", "Medium Oxygen Tank");
		LanguageRegistry.instance().addNameForObject(medOxygenTankEmpty, "en_US", "Empty Medium Oxygen Tank");
		LanguageRegistry.instance().addNameForObject(heavyOxygenTankFull, "en_US", "Heavy Oxygen Tank");
		LanguageRegistry.instance().addNameForObject(heavyOxygenTankEmpty, "en_US", "Empty Heavy Oxygen Tank");
		LanguageRegistry.instance().addNameForObject(sensorGlasses, "en_US", "Sensor Glasses");
		LanguageRegistry.instance().addNameForObject(sensorGlassesWithOxygenMask, "en_US", "Sensor Glasses with Oxygen Mask");
		LanguageRegistry.instance().addNameForObject(reinforcedBucketEmpty, "en_US", "Reinforced Bucket");
		LanguageRegistry.instance().addNameForObject(reinforcedBucketMilk, "en_US", "Reinforced Milk Bucket");
		LanguageRegistry.instance().addNameForObject(reinforcedBucketWater, "en_US", "Reinforced Water Bucket");
		LanguageRegistry.instance().addNameForObject(reinforcedBucketLava, "en_US", "Reinforced Lava Bucket");
		LanguageRegistry.instance().addNameForObject(reinforcedBucketBacteria, "en_US", "Reinforced Sludge Bucket");
	}
}
