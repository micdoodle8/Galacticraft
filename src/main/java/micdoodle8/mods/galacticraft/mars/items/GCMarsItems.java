package micdoodle8.mods.galacticraft.mars.items;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

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

	public static ArmorMaterial ARMORDESH = EnumHelper.addArmorMaterial("DESH", 42, new int[] { 4, 9, 7, 4 }, 12);
	public static ToolMaterial TOOLDESH = EnumHelper.addToolMaterial("DESH", 3, 1024, 5.0F, 2.5F, 8);

	public static void initItems()
	{
		GCMarsItems.marsItemBasic = new GCMarsItem();
		GCMarsItems.deshPickaxe = new GCMarsItemPickaxe(GCMarsItems.TOOLDESH).setUnlocalizedName("deshPick");
		GCMarsItems.deshAxe = new GCMarsItemAxe(GCMarsItems.TOOLDESH).setUnlocalizedName("deshAxe");
		GCMarsItems.deshHoe = new GCMarsItemHoe(GCMarsItems.TOOLDESH).setUnlocalizedName("deshHoe");
		GCMarsItems.deshSpade = new GCMarsItemSpade(GCMarsItems.TOOLDESH).setUnlocalizedName("deshSpade");
		GCMarsItems.deshSword = new GCMarsItemSword(GCMarsItems.TOOLDESH).setUnlocalizedName("deshSword");
		GCMarsItems.deshHelmet = new GCMarsItemArmor(GCMarsItems.ARMORDESH, 7, 0, false).setUnlocalizedName("deshHelmet");
		GCMarsItems.deshChestplate = new GCMarsItemArmor(GCMarsItems.ARMORDESH, 7, 1, false).setUnlocalizedName("deshChestplate");
		GCMarsItems.deshLeggings = new GCMarsItemArmor(GCMarsItems.ARMORDESH, 7, 2, false).setUnlocalizedName("deshLeggings");
		GCMarsItems.deshBoots = new GCMarsItemArmor(GCMarsItems.ARMORDESH, 7, 3, false).setUnlocalizedName("deshBoots");
		GCMarsItems.spaceship = new GCMarsItemSpaceshipTier2().setUnlocalizedName("spaceshipTier2");
		GCMarsItems.key = new GCMarsItemKey().setUnlocalizedName("key");
		GCMarsItems.schematic = new GCMarsItemSchematic().setUnlocalizedName("schematic");
	}

	public static void registerHarvestLevels()
	{
//		MinecraftForge.setToolClass(GCMarsItems.deshPickaxe, "pickaxe", 4);
//		MinecraftForge.setToolClass(GCMarsItems.deshAxe, "axe", 4);
//		MinecraftForge.setToolClass(GCMarsItems.deshSpade, "shovel", 4); TODO Fix harvest levels
	}
}
