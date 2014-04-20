package micdoodle8.mods.galacticraft.planets.mars.items;

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
public class MarsItems
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
		MarsItems.marsItemBasic = new ItemBasicMars();
		MarsItems.deshPickaxe = new ItemPickaxeMars(MarsItems.TOOLDESH).setUnlocalizedName("deshPick");
		MarsItems.deshAxe = new ItemAxeMars(MarsItems.TOOLDESH).setUnlocalizedName("deshAxe");
		MarsItems.deshHoe = new ItemHoeMars(MarsItems.TOOLDESH).setUnlocalizedName("deshHoe");
		MarsItems.deshSpade = new ItemSpadeMars(MarsItems.TOOLDESH).setUnlocalizedName("deshSpade");
		MarsItems.deshSword = new ItemSwordMars(MarsItems.TOOLDESH).setUnlocalizedName("deshSword");
		MarsItems.deshHelmet = new ItemArmorMars(MarsItems.ARMORDESH, 7, 0, false).setUnlocalizedName("deshHelmet");
		MarsItems.deshChestplate = new ItemArmorMars(MarsItems.ARMORDESH, 7, 1, false).setUnlocalizedName("deshChestplate");
		MarsItems.deshLeggings = new ItemArmorMars(MarsItems.ARMORDESH, 7, 2, false).setUnlocalizedName("deshLeggings");
		MarsItems.deshBoots = new ItemArmorMars(MarsItems.ARMORDESH, 7, 3, false).setUnlocalizedName("deshBoots");
		MarsItems.spaceship = new ItemTier2Rocket().setUnlocalizedName("spaceshipTier2");
		MarsItems.key = new ItemKeyMars().setUnlocalizedName("key");
		MarsItems.schematic = new ItemSchematicTier2().setUnlocalizedName("schematic");
	}

	public static void registerHarvestLevels()
	{
//		MinecraftForge.setToolClass(GCMarsItems.deshPickaxe, "pickaxe", 4);
//		MinecraftForge.setToolClass(GCMarsItems.deshAxe, "axe", 4);
//		MinecraftForge.setToolClass(GCMarsItems.deshSpade, "shovel", 4); TODO Fix harvest levels
	}
}
