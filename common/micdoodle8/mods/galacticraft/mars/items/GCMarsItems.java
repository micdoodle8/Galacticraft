package micdoodle8.mods.galacticraft.mars.items;

import micdoodle8.mods.galacticraft.mars.GCMarsConfigManager;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;

/**
 * GCMarsItems.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsItems
{
	public static Item marsItemBasic;
	public static Item deshPickaxe;
	public static Item deshAxe;
	public static Item deshHoe;
	public static Item deshSpade;
	public static Item deshSword;
	public static Item deshHelmet;
	public static Item deshChestplate;
	public static Item deshLeggings;
	public static Item deshBoots;
	public static Item spaceship;
	public static Item key;
	public static Item schematic;

	public static EnumArmorMaterial ARMORDESH = EnumHelper.addArmorMaterial("DESH", 42, new int[] { 4, 9, 7, 4 }, 12);
	public static EnumToolMaterial TOOLDESH = EnumHelper.addToolMaterial("DESH", 3, 1024, 5.0F, 2.5F, 8);

	public static void initItems()
	{
		GCMarsItems.marsItemBasic = new GCMarsItem(GCMarsConfigManager.idItemMarsBasic);
		GCMarsItems.deshPickaxe = new GCMarsItemPickaxe(GCMarsConfigManager.idToolDeshPickaxe, GCMarsItems.TOOLDESH).setUnlocalizedName("deshPick");
		GCMarsItems.deshAxe = new GCMarsItemAxe(GCMarsConfigManager.idToolDeshAxe, GCMarsItems.TOOLDESH).setUnlocalizedName("deshAxe");
		GCMarsItems.deshHoe = new GCMarsItemHoe(GCMarsConfigManager.idToolDeshHoe, GCMarsItems.TOOLDESH).setUnlocalizedName("deshHoe");
		GCMarsItems.deshSpade = new GCMarsItemSpade(GCMarsConfigManager.idToolDeshSpade, GCMarsItems.TOOLDESH).setUnlocalizedName("deshSpade");
		GCMarsItems.deshSword = new GCMarsItemSword(GCMarsConfigManager.idToolDeshSword, GCMarsItems.TOOLDESH).setUnlocalizedName("deshSword");
		GCMarsItems.deshHelmet = new GCMarsItemArmor(GCMarsConfigManager.idArmorDeshHelmet, GCMarsItems.ARMORDESH, 7, 0, false).setUnlocalizedName("deshHelmet");
		GCMarsItems.deshChestplate = new GCMarsItemArmor(GCMarsConfigManager.idArmorDeshChestplate, GCMarsItems.ARMORDESH, 7, 1, false).setUnlocalizedName("deshChestplate");
		GCMarsItems.deshLeggings = new GCMarsItemArmor(GCMarsConfigManager.idArmorDeshLeggings, GCMarsItems.ARMORDESH, 7, 2, false).setUnlocalizedName("deshLeggings");
		GCMarsItems.deshBoots = new GCMarsItemArmor(GCMarsConfigManager.idArmorDeshBoots, GCMarsItems.ARMORDESH, 7, 3, false).setUnlocalizedName("deshBoots");
		GCMarsItems.spaceship = new GCMarsItemSpaceshipTier2(GCMarsConfigManager.idItemSpaceshipTier2).setUnlocalizedName("spaceshipTier2");
		GCMarsItems.key = new GCMarsItemKey(GCMarsConfigManager.idItemKeyT2).setUnlocalizedName("key");
		GCMarsItems.schematic = new GCMarsItemSchematic(GCMarsConfigManager.idItemSchematicMars).setUnlocalizedName("schematic");
	}

	public static void registerHarvestLevels()
	{
		MinecraftForge.setToolClass(GCMarsItems.deshPickaxe, "pickaxe", 4);
		MinecraftForge.setToolClass(GCMarsItems.deshAxe, "axe", 4);
		MinecraftForge.setToolClass(GCMarsItems.deshSpade, "shovel", 4);
	}
}
