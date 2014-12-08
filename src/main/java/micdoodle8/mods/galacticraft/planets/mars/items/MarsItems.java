package micdoodle8.mods.galacticraft.planets.mars.items;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.items.ItemBucketGC;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public class MarsItems
{
    public static Item marsItemBasic;
    public static Item deshPickaxe;
    public static Item deshPickSlime;
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
    public static Item carbonFragments;
    public static Item bucketSludge;

    public static ArmorMaterial ARMORDESH = EnumHelper.addArmorMaterial("DESH", 42, new int[] { 4, 9, 7, 4 }, 12);
    public static ToolMaterial TOOLDESH = EnumHelper.addToolMaterial("DESH", 3, 1024, 5.0F, 2.5F, 8);


    public static void initItems()
    {
        MarsItems.marsItemBasic = new ItemBasicMars();
        MarsItems.deshPickaxe = new ItemPickaxeMars(MarsItems.TOOLDESH).setUnlocalizedName("deshPick");
        MarsItems.deshPickSlime = new ItemPickaxeStickyMars(MarsItems.TOOLDESH).setUnlocalizedName("deshPickSlime");
        MarsItems.deshAxe = new ItemAxeMars(MarsItems.TOOLDESH).setUnlocalizedName("deshAxe");
        MarsItems.deshHoe = new ItemHoeMars(MarsItems.TOOLDESH).setUnlocalizedName("deshHoe");
        MarsItems.deshSpade = new ItemSpadeMars(MarsItems.TOOLDESH).setUnlocalizedName("deshSpade");
        MarsItems.deshSword = new ItemSwordMars(MarsItems.TOOLDESH).setUnlocalizedName("deshSword");
        MarsItems.deshHelmet = new ItemArmorMars(MarsItems.ARMORDESH, 7, 0).setUnlocalizedName("deshHelmet");
        MarsItems.deshChestplate = new ItemArmorMars(MarsItems.ARMORDESH, 7, 1).setUnlocalizedName("deshChestplate");
        MarsItems.deshLeggings = new ItemArmorMars(MarsItems.ARMORDESH, 7, 2).setUnlocalizedName("deshLeggings");
        MarsItems.deshBoots = new ItemArmorMars(MarsItems.ARMORDESH, 7, 3).setUnlocalizedName("deshBoots");
        MarsItems.spaceship = new ItemTier2Rocket().setUnlocalizedName("spaceshipTier2");
        MarsItems.key = new ItemKeyMars().setUnlocalizedName("key");
        MarsItems.schematic = new ItemSchematicTier2().setUnlocalizedName("schematic");
        MarsItems.carbonFragments = new ItemCarbonFragments().setUnlocalizedName("carbonFragments");
		MarsItems.bucketSludge = new ItemBucketGC(MarsBlocks.blockSludge, MarsModule.TEXTURE_PREFIX).setUnlocalizedName("bucketSludge");

        MarsItems.registerItems();
        MarsItems.registerHarvestLevels();
    }

    public static void registerHarvestLevels()
    {
        MarsItems.deshPickaxe.setHarvestLevel("pickaxe", 4);
        MarsItems.deshPickSlime.setHarvestLevel("pickaxe", 4);
        MarsItems.deshAxe.setHarvestLevel("axe", 4);
        MarsItems.deshSpade.setHarvestLevel("shovel", 4);
    }

    private static void registerItems()
    {
        MarsItems.registerItem(MarsItems.carbonFragments);
        MarsItems.registerItem(MarsItems.marsItemBasic);
        MarsItems.registerItem(MarsItems.deshPickaxe);
        MarsItems.registerItem(MarsItems.deshPickSlime);
        MarsItems.registerItem(MarsItems.deshAxe);
        MarsItems.registerItem(MarsItems.deshHoe);
        MarsItems.registerItem(MarsItems.deshSpade);
        MarsItems.registerItem(MarsItems.deshSword);
        MarsItems.registerItem(MarsItems.deshHelmet);
        MarsItems.registerItem(MarsItems.deshChestplate);
        MarsItems.registerItem(MarsItems.deshLeggings);
        MarsItems.registerItem(MarsItems.deshBoots);
        MarsItems.registerItem(MarsItems.spaceship);
        MarsItems.registerItem(MarsItems.key);
        MarsItems.registerItem(MarsItems.schematic);
        MarsItems.registerItem(MarsItems.bucketSludge);
    }

    private static void registerItem(Item item)
    {
        GameRegistry.registerItem(item, item.getUnlocalizedName(), Constants.MOD_ID_PLANETS);
    }
}
