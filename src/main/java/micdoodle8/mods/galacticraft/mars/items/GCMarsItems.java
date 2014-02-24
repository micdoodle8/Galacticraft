package micdoodle8.mods.galacticraft.mars.items;

import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
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

	public static enum EnumArmorIndexMars
	{
		DESH("desh", 42, new int[] { 4, 9, 7, 4 }, 12);

		EnumArmorIndexMars(String name, int durability, int[] reductionAmounts, int enchantability)
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
			return GalacticraftMars.proxy.getArmorRenderID(this);
		}

		private ArmorMaterial material;
		private String name;
		private int durability;
		private int[] reductionAmounts;
		private int enchantability;
	}

	public static enum EnumToolTypeMars
	{
		TOOL_DESH("desh", 4, 1024, 5.0F, 2.5F, 8);

		EnumToolTypeMars(String name, int harvestLevel, int maxUses, float efficiency, float damage, int enchantability)
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

	public static void initItems()
	{
		GCMarsItems.marsItemBasic = new GCMarsItem();
		GCMarsItems.deshPickaxe = new GCMarsItemPickaxe(EnumToolTypeMars.TOOL_DESH).setUnlocalizedName("deshPick");
		GCMarsItems.deshAxe = new GCMarsItemAxe(EnumToolTypeMars.TOOL_DESH).setUnlocalizedName("deshAxe");
		GCMarsItems.deshHoe = new GCMarsItemHoe(EnumToolTypeMars.TOOL_DESH).setUnlocalizedName("deshHoe");
		GCMarsItems.deshSpade = new GCMarsItemSpade(EnumToolTypeMars.TOOL_DESH).setUnlocalizedName("deshSpade");
		GCMarsItems.deshSword = new GCMarsItemSword(EnumToolTypeMars.TOOL_DESH).setUnlocalizedName("deshSword");
		GCMarsItems.deshHelmet = new GCMarsItemArmor(EnumArmorIndexMars.DESH, 0).setUnlocalizedName("deshHelmet");
		GCMarsItems.deshChestplate = new GCMarsItemArmor(EnumArmorIndexMars.DESH, 1).setUnlocalizedName("deshChestplate");
		GCMarsItems.deshLeggings = new GCMarsItemArmor(EnumArmorIndexMars.DESH, 2).setUnlocalizedName("deshLeggings");
		GCMarsItems.deshBoots = new GCMarsItemArmor(EnumArmorIndexMars.DESH, 3).setUnlocalizedName("deshBoots");
		GCMarsItems.spaceship = new GCMarsItemSpaceshipTier2().setUnlocalizedName("spaceshipTier2");
		GCMarsItems.key = new GCMarsItemKey().setUnlocalizedName("key");
		GCMarsItems.schematic = new GCMarsItemSchematic().setUnlocalizedName("schematic");
	}
}
