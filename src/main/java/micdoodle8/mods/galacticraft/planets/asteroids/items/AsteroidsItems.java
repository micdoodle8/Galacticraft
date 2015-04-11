package micdoodle8.mods.galacticraft.planets.asteroids.items;

import cpw.mods.fml.common.registry.GameRegistry;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;

public class AsteroidsItems
{
    public static Item grapple;
    public static Item tier3Rocket;
    public static Item astroMiner;
    public static Item thermalPadding;
    public static Item basicItem;
    public static Item methaneCanister;
    public static Item canisterLOX;
    public static Item canisterLN2;
    //public static Item canisterLAr;
    public static Item atmosphericValve;
    public static ItemHeavyNoseCone heavyNoseCone;
    public static Item titaniumHelmet;
    public static Item titaniumChestplate;
    public static Item titaniumLeggings;
    public static Item titaniumBoots;
    public static Item titaniumAxe;
    public static Item titaniumPickaxe;
    public static Item titaniumSpade;
    public static Item titaniumHoe;
    public static Item titaniumSword;

    public static Item.ToolMaterial TOOL_TITANIUM = EnumHelper.addToolMaterial("titanium", 3, 520, 8.0F, 3.0F, 10);
    public static ItemArmor.ArmorMaterial ARMOR_TITANIUM = EnumHelper.addArmorMaterial("titanium", 26, new int[] { 5, 10, 7, 5 }, 10);

    public static void initItems()
    {
        AsteroidsItems.grapple = new ItemGrappleHook("grapple");
        AsteroidsItems.tier3Rocket = new ItemTier3Rocket("itemTier3Rocket");
        AsteroidsItems.astroMiner = new ItemAstroMiner("itemAstroMiner");
        AsteroidsItems.thermalPadding = new ItemThermalPadding("thermalPadding");
        AsteroidsItems.basicItem = new ItemBasicAsteroids();
        AsteroidsItems.methaneCanister = new ItemCanisterMethane("methaneCanisterPartial");
        AsteroidsItems.canisterLOX = new ItemCanisterLiquidOxygen("canisterPartialLOX");
        AsteroidsItems.canisterLN2 = new ItemCanisterLiquidNitrogen("canisterPartialLN2");
        //AsteroidsItems.canisterLAr = new ItemCanisterLiquidArgon("canisterPartialLAr");
        AsteroidsItems.atmosphericValve = new ItemAtmosphericValve("atmosphericValve");
        AsteroidsItems.heavyNoseCone = new ItemHeavyNoseCone("heavyNoseCone");
        AsteroidsItems.titaniumHelmet = new ItemArmorAsteroids(0, "helmet");
        AsteroidsItems.titaniumChestplate = new ItemArmorAsteroids(1, "chestplate");
        AsteroidsItems.titaniumLeggings = new ItemArmorAsteroids(2, "leggings");
        AsteroidsItems.titaniumBoots = new ItemArmorAsteroids(3, "boots");
        AsteroidsItems.titaniumAxe = new ItemAxeAsteroids("titanium_axe");
        AsteroidsItems.titaniumPickaxe = new ItemPickaxeAsteroids("titanium_pickaxe");
        AsteroidsItems.titaniumSpade = new ItemSpadeAsteroids("titanium_shovel");
        AsteroidsItems.titaniumHoe = new ItemHoeAsteroids("titanium_hoe");
        AsteroidsItems.titaniumSword = new ItemSwordAsteroids("titanium_sword");

        AsteroidsItems.registerItems();

        OreDictionary.registerOre("compressedTitanium", new ItemStack(AsteroidsItems.basicItem, 1, 6));
        OreDictionary.registerOre("ingotTitanium", new ItemStack(AsteroidsItems.basicItem, 1, 5));
        OreDictionary.registerOre("shardTitanium", new ItemStack(AsteroidsItems.basicItem, 1, 4));
        OreDictionary.registerOre("shardIron", new ItemStack(AsteroidsItems.basicItem, 1, 3));
    }

    private static void registerItems()
    {
        registerItem(AsteroidsItems.grapple);
        registerItem(AsteroidsItems.tier3Rocket);
        registerItem(AsteroidsItems.astroMiner);
        registerItem(AsteroidsItems.thermalPadding);
        registerItem(AsteroidsItems.basicItem);
        registerItem(AsteroidsItems.methaneCanister);
        registerItem(AsteroidsItems.canisterLOX);
        registerItem(AsteroidsItems.canisterLN2);
        //registerItem(AsteroidsItems.canisterLAr);
        registerItem(AsteroidsItems.atmosphericValve);
        registerItem(AsteroidsItems.heavyNoseCone);
        registerItem(AsteroidsItems.titaniumHelmet);
        registerItem(AsteroidsItems.titaniumChestplate);
        registerItem(AsteroidsItems.titaniumLeggings);
        registerItem(AsteroidsItems.titaniumBoots);
        registerItem(AsteroidsItems.titaniumAxe);
        registerItem(AsteroidsItems.titaniumPickaxe);
        registerItem(AsteroidsItems.titaniumSpade);
        registerItem(AsteroidsItems.titaniumHoe);
        registerItem(AsteroidsItems.titaniumSword);
        
        //These exact names are important, ItemCanisterGeneric searches for "CanisterFull"
        GCCoreUtil.registerGalacticraftItem("LOXCanisterFull", AsteroidsItems.canisterLOX, 1);
        GCCoreUtil.registerGalacticraftItem("LN2CanisterFull", AsteroidsItems.canisterLN2, 1);
        GCCoreUtil.registerGalacticraftItem("methaneCanisterFull", AsteroidsItems.methaneCanister, 1);
    }

    private static void registerItem(Item item)
    {
        GameRegistry.registerItem(item, item.getUnlocalizedName(), Constants.MOD_ID_PLANETS);
        
    }
}
