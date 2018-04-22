package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.wrappers.PartialCanister;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
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
    public static Item orionDrive;
    public static Item titaniumHelmet;
    public static Item titaniumChestplate;
    public static Item titaniumLeggings;
    public static Item titaniumBoots;
    public static Item titaniumAxe;
    public static Item titaniumPickaxe;
    public static Item titaniumSpade;
    public static Item titaniumHoe;
    public static Item titaniumSword;
    public static Item strangeSeed;

    public static Item.ToolMaterial TOOL_TITANIUM = EnumHelper.addToolMaterial("titanium", 4, 760, 14.0F, 4.0F, 16);
    public static ItemArmor.ArmorMaterial ARMOR_TITANIUM = EnumHelper.addArmorMaterial("titanium", "", 26, new int[] { 5, 7, 10, 5 }, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);

    public static void initItems()
    {
        AsteroidsItems.grapple = new ItemGrappleHook("grapple");
        AsteroidsItems.tier3Rocket = new ItemTier3Rocket("rocket_t3");
        AsteroidsItems.astroMiner = new ItemAstroMiner("astro_miner");
        AsteroidsItems.thermalPadding = new ItemThermalPadding("thermal_padding");
        AsteroidsItems.basicItem = new ItemBasicAsteroids("item_basic_asteroids");
        AsteroidsItems.methaneCanister = new ItemCanisterMethane("methane_canister_partial");
        AsteroidsItems.canisterLOX = new ItemCanisterLiquidOxygen("canister_partial_lox");
        AsteroidsItems.canisterLN2 = new ItemCanisterLiquidNitrogen("canister_partial_ln2");
        //AsteroidsItems.canisterLAr = new ItemCanisterLiquidArgon("canisterPartialLAr");
        AsteroidsItems.atmosphericValve = new ItemAtmosphericValve("atmospheric_valve");
        AsteroidsItems.heavyNoseCone = new ItemHeavyNoseCone("heavy_nose_cone");
        AsteroidsItems.orionDrive = new ItemOrionDrive("orion_drive");
        AsteroidsItems.titaniumHelmet = new ItemArmorAsteroids(EntityEquipmentSlot.HEAD, "helmet");
        AsteroidsItems.titaniumChestplate = new ItemArmorAsteroids(EntityEquipmentSlot.CHEST, "chestplate");
        AsteroidsItems.titaniumLeggings = new ItemArmorAsteroids(EntityEquipmentSlot.LEGS, "leggings");
        AsteroidsItems.titaniumBoots = new ItemArmorAsteroids(EntityEquipmentSlot.FEET, "boots");
        AsteroidsItems.titaniumAxe = new ItemAxeAsteroids("titanium_axe");
        AsteroidsItems.titaniumPickaxe = new ItemPickaxeAsteroids("titanium_pickaxe");
        AsteroidsItems.titaniumSpade = new ItemSpadeAsteroids("titanium_shovel");
        AsteroidsItems.titaniumHoe = new ItemHoeAsteroids("titanium_hoe");
        AsteroidsItems.titaniumSword = new ItemSwordAsteroids("titanium_sword");
        AsteroidsItems.strangeSeed = new ItemStrangeSeed("strange_seed");

        AsteroidsItems.registerItems();

        AsteroidsItems.registerHarvestLevels();

        GalacticraftCore.proxy.registerCanister(new PartialCanister(AsteroidsItems.methaneCanister, Constants.MOD_ID_PLANETS, "methane_canister_partial", 7));
        GalacticraftCore.proxy.registerCanister(new PartialCanister(AsteroidsItems.canisterLOX, Constants.MOD_ID_PLANETS, "canister_partial_lox", 7));
        GalacticraftCore.proxy.registerCanister(new PartialCanister(AsteroidsItems.canisterLN2, Constants.MOD_ID_PLANETS, "canister_partial_ln2", 7));
    }

    public static void oreDictRegistrations()
    {
        OreDictionary.registerOre("compressedTitanium", new ItemStack(AsteroidsItems.basicItem, 1, 6));
        OreDictionary.registerOre("ingotTitanium", new ItemStack(AsteroidsItems.basicItem, 1, 0));
        OreDictionary.registerOre("shardTitanium", new ItemStack(AsteroidsItems.basicItem, 1, 4));
        OreDictionary.registerOre("shardIron", new ItemStack(AsteroidsItems.basicItem, 1, 3));
        OreDictionary.registerOre("dustTitanium", new ItemStack(AsteroidsItems.basicItem, 1, 9));
    }

    public static void registerHarvestLevels()
    {
        AsteroidsItems.titaniumPickaxe.setHarvestLevel("pickaxe", 5);
        AsteroidsItems.titaniumAxe.setHarvestLevel("axe", 5);
        AsteroidsItems.titaniumSpade.setHarvestLevel("shovel", 5);
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
        registerItem(AsteroidsItems.orionDrive);
        registerItem(AsteroidsItems.titaniumHelmet);
        registerItem(AsteroidsItems.titaniumChestplate);
        registerItem(AsteroidsItems.titaniumLeggings);
        registerItem(AsteroidsItems.titaniumBoots);
        registerItem(AsteroidsItems.titaniumAxe);
        registerItem(AsteroidsItems.titaniumPickaxe);
        registerItem(AsteroidsItems.titaniumSpade);
        registerItem(AsteroidsItems.titaniumHoe);
        registerItem(AsteroidsItems.titaniumSword);
        registerItem(AsteroidsItems.strangeSeed);
        
        ARMOR_TITANIUM.setRepairItem(new ItemStack(AsteroidsItems.basicItem, 1, 0));
        
        GCItems.canisterTypes.add((ItemCanisterGeneric) AsteroidsItems.canisterLOX);
        GCItems.canisterTypes.add((ItemCanisterGeneric) AsteroidsItems.methaneCanister);
        GCItems.canisterTypes.add((ItemCanisterGeneric) AsteroidsItems.canisterLN2);
    }

    public static void registerItem(Item item)
    {
        String name = item.getUnlocalizedName().substring(5);
        GCCoreUtil.registerGalacticraftItem(name, item);
        GalacticraftCore.itemListTrue.add(item);
        item.setRegistryName(name);
        GalacticraftPlanets.proxy.postRegisterItem(item);
    }
}
