package micdoodle8.mods.galacticraft;

import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.Item;
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
	
	static EnumArmorMaterial OXYGENMASK = EnumHelper.addArmorMaterial("OXYGENMASK", 200, new int[] {0, 0, 0, 0}, 0);
	
	public static void initItems() 
	{
		lightOxygenTankFull = new GCItem(GCConfigManager.idItemLightOxygenTank).setMaxDamage(60).setIconIndex(0).setItemName("lightFull");
		lightOxygenTankEmpty = new GCItem(GCConfigManager.idItemLightOxygenTankEmpty).setIconIndex(3).setItemName("lightEmpty");
		medOxygenTankFull = new GCItem(GCConfigManager.idItemMedOxygenTank).setMaxDamage(120).setIconIndex(1).setItemName("medFull");
		medOxygenTankEmpty = new GCItem(GCConfigManager.idItemMedOxygenTankEmpty).setIconIndex(4).setItemName("medEmpty");
		heavyOxygenTankFull = new GCItem(GCConfigManager.idItemHeavyOxygenTank).setMaxDamage(90).setIconIndex(2).setItemName("heavyFull");
		heavyOxygenTankEmpty = new GCItem(GCConfigManager.idItemHeavyOxygenTankEmpty).setIconIndex(5).setItemName("heavyEmpty");
		oxygenMask = new GCItemOxygenMask(GCConfigManager.idArmorOxygenMask, OXYGENMASK, 5, 0).setIconIndex(7).setItemName("oxygenMask");
		spaceship = new GCItemSpaceship(GCConfigManager.idItemSpaceship).setIconIndex(8).setItemName("spaceship");
	}

	public static void addNames() 
	{
		LanguageRegistry.instance().addNameForObject(oxygenMask, "en_US", "Oxygen Mask");
		LanguageRegistry.instance().addNameForObject(spaceship, "en_US", "Spaceship");
	}
}
