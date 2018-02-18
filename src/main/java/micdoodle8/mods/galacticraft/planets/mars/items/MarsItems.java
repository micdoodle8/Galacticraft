package micdoodle8.mods.galacticraft.planets.mars.items;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

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
    public static Item rocketMars;
    public static Item key;
    public static Item schematic;
    public static Item carbonFragments;
    public static Item bucketSludge;

    public static ArmorMaterial ARMORDESH = EnumHelper.addArmorMaterial("DESH", "", 42, new int[] { 4, 9, 7, 4 }, 12);
    public static ToolMaterial TOOLDESH = EnumHelper.addToolMaterial("DESH", 3, 1024, 5.0F, 2.5F, 10);


    public static void initItems()
    {
        MarsItems.marsItemBasic = new ItemBasicMars("item_basic_mars");
        MarsItems.deshPickaxe = new ItemPickaxeMars(MarsItems.TOOLDESH).setUnlocalizedName("desh_pick");
        MarsItems.deshPickSlime = new ItemPickaxeStickyMars(MarsItems.TOOLDESH).setUnlocalizedName("desh_pick_slime");
        MarsItems.deshAxe = new ItemAxeMars(MarsItems.TOOLDESH).setUnlocalizedName("desh_axe");
        MarsItems.deshHoe = new ItemHoeMars(MarsItems.TOOLDESH).setUnlocalizedName("desh_hoe");
        MarsItems.deshSpade = new ItemSpadeMars(MarsItems.TOOLDESH).setUnlocalizedName("desh_spade");
        MarsItems.deshSword = new ItemSwordMars(MarsItems.TOOLDESH).setUnlocalizedName("desh_sword");
        MarsItems.deshHelmet = new ItemArmorMars(MarsItems.ARMORDESH, 7, 0).setUnlocalizedName("desh_helmet");
        MarsItems.deshChestplate = new ItemArmorMars(MarsItems.ARMORDESH, 7, 1).setUnlocalizedName("desh_chestplate");
        MarsItems.deshLeggings = new ItemArmorMars(MarsItems.ARMORDESH, 7, 2).setUnlocalizedName("desh_leggings");
        MarsItems.deshBoots = new ItemArmorMars(MarsItems.ARMORDESH, 7, 3).setUnlocalizedName("desh_boots");
        MarsItems.rocketMars = new ItemTier2Rocket().setUnlocalizedName("rocket_t2");
        MarsItems.key = new ItemKeyMars().setUnlocalizedName("key");
        MarsItems.schematic = new ItemSchematicTier2().setUnlocalizedName("schematic");
        MarsItems.carbonFragments = new ItemCarbonFragments().setUnlocalizedName("carbon_fragments");

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
        MarsItems.registerItem(MarsItems.rocketMars);
        MarsItems.registerItem(MarsItems.key);
        MarsItems.registerItem(MarsItems.schematic);
    }

    public static void registerItem(Item item)
    {
        String name = item.getUnlocalizedName().substring(5);
        GCCoreUtil.registerGalacticraftItem(name, item);
        GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
        GalacticraftPlanets.proxy.postRegisterItem(item);
        if (GCCoreUtil.getEffectiveSide() == Side.CLIENT)
        {
            GCItems.registerSorted(item);
        }
    }
}
