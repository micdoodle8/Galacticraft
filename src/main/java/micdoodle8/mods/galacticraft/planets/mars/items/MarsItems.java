package micdoodle8.mods.galacticraft.planets.mars.items;

import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import cpw.mods.fml.common.registry.GameRegistry;

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
			return GalacticraftPlanets.proxyMars.getArmorRenderID(this);
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
		MarsItems.marsItemBasic = new ItemMars();
		MarsItems.deshPickaxe = new ItemPickaxeMars(EnumToolTypeMars.TOOL_DESH).setUnlocalizedName("deshPick");
		MarsItems.deshAxe = new ItemAxeMars(EnumToolTypeMars.TOOL_DESH).setUnlocalizedName("deshAxe");
		MarsItems.deshHoe = new ItemHoeMars(EnumToolTypeMars.TOOL_DESH).setUnlocalizedName("deshHoe");
		MarsItems.deshSpade = new ItemSpadeMars(EnumToolTypeMars.TOOL_DESH).setUnlocalizedName("deshSpade");
		MarsItems.deshSword = new ItemSwordMars(EnumToolTypeMars.TOOL_DESH).setUnlocalizedName("deshSword");
		MarsItems.deshHelmet = new ItemArmorMars(EnumArmorIndexMars.DESH, 0).setUnlocalizedName("deshHelmet");
		MarsItems.deshChestplate = new ItemArmorMars(EnumArmorIndexMars.DESH, 1).setUnlocalizedName("deshChestplate");
		MarsItems.deshLeggings = new ItemArmorMars(EnumArmorIndexMars.DESH, 2).setUnlocalizedName("deshLeggings");
		MarsItems.deshBoots = new ItemArmorMars(EnumArmorIndexMars.DESH, 3).setUnlocalizedName("deshBoots");
		MarsItems.spaceship = new ItemTier2Rocket().setUnlocalizedName("spaceshipTier2");
		MarsItems.key = new ItemKey().setUnlocalizedName("key");
		MarsItems.schematic = new ItemSchematicMars().setUnlocalizedName("schematic");
		
		registerItems();
	}
	
	private static void registerItems()
	{
		GameRegistry.registerItem(marsItemBasic, marsItemBasic.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
		GameRegistry.registerItem(deshPickaxe, deshPickaxe.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
		GameRegistry.registerItem(deshAxe, deshAxe.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
		GameRegistry.registerItem(deshHoe, deshHoe.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
		GameRegistry.registerItem(deshSpade, deshSpade.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
		GameRegistry.registerItem(deshSword, deshSword.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
		GameRegistry.registerItem(deshHelmet, deshHelmet.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
		GameRegistry.registerItem(deshChestplate, deshChestplate.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
		GameRegistry.registerItem(deshLeggings, deshLeggings.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
		GameRegistry.registerItem(deshBoots, deshBoots.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
		GameRegistry.registerItem(spaceship, spaceship.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
		GameRegistry.registerItem(key, key.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
		GameRegistry.registerItem(schematic, schematic.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
	}
}
