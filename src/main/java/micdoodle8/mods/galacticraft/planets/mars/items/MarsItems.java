package micdoodle8.mods.galacticraft.planets.mars.items;

import micdoodle8.mods.galacticraft.core.items.ItemBase;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems.register;

public class MarsItems
{
    @ObjectHolder(MarsItemNames.deshPickaxe)
    public static Item deshPickaxe;
    @ObjectHolder(MarsItemNames.deshPickSlime)
    public static Item deshPickSlime;
    @ObjectHolder(MarsItemNames.deshAxe)
    public static Item deshAxe;
    @ObjectHolder(MarsItemNames.deshHoe)
    public static Item deshHoe;
    @ObjectHolder(MarsItemNames.deshSpade)
    public static Item deshSpade;
    @ObjectHolder(MarsItemNames.deshSword)
    public static Item deshSword;
    @ObjectHolder(MarsItemNames.deshHelmet)
    public static Item deshHelmet;
    @ObjectHolder(MarsItemNames.deshChestplate)
    public static Item deshChestplate;
    @ObjectHolder(MarsItemNames.deshLeggings)
    public static Item deshLeggings;
    @ObjectHolder(MarsItemNames.deshBoots)
    public static Item deshBoots;
    @ObjectHolder(MarsItemNames.rocketTierTwo)
    public static Item rocketTierTwo;
    @ObjectHolder(MarsItemNames.rocketTierTwoCargo1)
    public static Item rocketTierTwoCargo1;
    @ObjectHolder(MarsItemNames.rocketTierTwoCargo2)
    public static Item rocketTierTwoCargo2;
    @ObjectHolder(MarsItemNames.rocketTierTwoCargo3)
    public static Item rocketTierTwoCargo3;
    @ObjectHolder(MarsItemNames.rocketTierTwoCreative)
    public static Item rocketTierTwoCreative;
    @ObjectHolder(MarsItemNames.rocketCargo1)
    public static Item rocketCargo1;
    @ObjectHolder(MarsItemNames.rocketCargo2)
    public static Item rocketCargo2;
    @ObjectHolder(MarsItemNames.rocketCargo3)
    public static Item rocketCargo3;
    @ObjectHolder(MarsItemNames.rocketCargoCreative)
    public static Item rocketCargoCreative;
    @ObjectHolder(MarsItemNames.key)
    public static Item key;
    @ObjectHolder(MarsItemNames.schematicRocketT3)
    public static Item schematicRocketT3;
    @ObjectHolder(MarsItemNames.schematicCargoRocket)
    public static Item schematicCargoRocket;
    @ObjectHolder(MarsItemNames.schematicAstroMiner)
    public static Item schematicAstroMiner;
    @ObjectHolder(MarsItemNames.carbonFragments)
    public static Item carbonFragments;
    @ObjectHolder(MarsItemNames.rawDesh)
    public static Item rawDesh;
    @ObjectHolder(MarsItemNames.deshStick)
    public static Item deshStick;
    @ObjectHolder(MarsItemNames.ingotDesh)
    public static Item ingotDesh;
    @ObjectHolder(MarsItemNames.reinforcedPlateT2)
    public static Item reinforcedPlateT2;
    @ObjectHolder(MarsItemNames.slimelingCargo)
    public static Item slimelingCargo;
    @ObjectHolder(MarsItemNames.compressedDesh)
    public static Item compressedDesh;
    @ObjectHolder(MarsItemNames.fluidManip)
    public static Item fluidManip;
//    public static Item bucketSludge;

    public static Item.Properties defaultBuilder()
    {
        return new Item.Properties()/*.group(GalacticraftCreativeTab.INSTANCE)*/;
    }

//    public static ArmorMaterial ARMORDESH = EnumHelper.addArmorMaterial("DESH", "", 42, new int[] { 4, 7, 9, 4 }, 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 3.0F);
//    public static ToolMaterial TOOLDESH = EnumHelper.addToolMaterial("DESH", 3, 1024, 5.0F, 2.5F, 10);

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> evt)
    {
        IForgeRegistry<Item> r = evt.getRegistry();
        register(r, new ItemBase(defaultBuilder()), MarsItemNames.rawDesh);
        register(r, new ItemBase(defaultBuilder()), MarsItemNames.deshStick);
        register(r, new ItemBase(defaultBuilder()), MarsItemNames.ingotDesh);
        register(r, new ItemBase(defaultBuilder()), MarsItemNames.reinforcedPlateT2);
        register(r, new ItemBase(defaultBuilder()), MarsItemNames.slimelingCargo);
        register(r, new ItemBase(defaultBuilder()), MarsItemNames.compressedDesh);
        register(r, new ItemBase(defaultBuilder()), MarsItemNames.fluidManip);
        register(r, new ItemPickaxeMars(defaultBuilder()), MarsItemNames.deshPickaxe);
        register(r, new ItemPickaxeMars(defaultBuilder()), MarsItemNames.deshPickSlime);
        register(r, new ItemAxeMars(defaultBuilder()), MarsItemNames.deshAxe);
        register(r, new ItemHoeMars(defaultBuilder()), MarsItemNames.deshHoe);
        register(r, new ItemShovelMars(defaultBuilder()), MarsItemNames.deshSpade);
        register(r, new ItemSwordMars(defaultBuilder()), MarsItemNames.deshSword);
        register(r, new ItemArmorMars(EquipmentSlotType.HEAD, defaultBuilder()), MarsItemNames.deshHelmet);
        register(r, new ItemArmorMars(EquipmentSlotType.CHEST, defaultBuilder()), MarsItemNames.deshChestplate);
        register(r, new ItemArmorMars(EquipmentSlotType.LEGS, defaultBuilder()), MarsItemNames.deshLeggings);
        register(r, new ItemArmorMars(EquipmentSlotType.FEET, defaultBuilder()), MarsItemNames.deshBoots);
//        @ObjectHolder(MarsItemNames.rocketTierTwo) public static Item rocketTierTwo;
//        @ObjectHolder(MarsItemNames.rocketTierTwoCargo1) public static Item rocketTierTwoCargo1;
//        @ObjectHolder(MarsItemNames.rocketTierTwoCargo2) public static Item rocketTierTwoCargo2;
//        @ObjectHolder(MarsItemNames.rocketTierTwoCargo3) public static Item rocketTierTwoCargo3;
//        @ObjectHolder(MarsItemNames.rocketTierTwoCreative) public static Item rocketTierTwoCreative;
//        @ObjectHolder(MarsItemNames.rocketCargo1) public static Item rocketCargo1;
//        @ObjectHolder(MarsItemNames.rocketCargo2) public static Item rocketCargo2;
//        @ObjectHolder(MarsItemNames.rocketCargo3) public static Item rocketCargo3;
//        @ObjectHolder(MarsItemNames.rocketCargoCreative) public static Item rocketCargoCreative;
        register(r, new ItemTier2Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.rocketTierTwo);
        register(r, new ItemTier2Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.rocketTierTwoCargo1);
        register(r, new ItemTier2Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.rocketTierTwoCargo2);
        register(r, new ItemTier2Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.rocketTierTwoCargo3);
        register(r, new ItemTier2Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.rocketTierTwoCreative);
        register(r, new ItemTier2Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.rocketCargo1);
        register(r, new ItemTier2Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.rocketCargo2);
        register(r, new ItemTier2Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.rocketCargo3);
        register(r, new ItemTier2Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.rocketCargoCreative);
        register(r, new ItemKeyMars(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.key);
        register(r, new ItemSchematicTier2(defaultBuilder()), MarsItemNames.schematicRocketT3);
        register(r, new ItemSchematicTier2(defaultBuilder()), MarsItemNames.schematicCargoRocket);
        register(r, new ItemSchematicTier2(defaultBuilder()), MarsItemNames.schematicAstroMiner);
        register(r, new ItemCarbonFragments(defaultBuilder()), MarsItemNames.carbonFragments);
    }

//    public static void registerHarvestLevels()
//    {
//        MarsItems.deshPickaxe.setHarvestLevel("pickaxe", 4);
//        MarsItems.deshPickSlime.setHarvestLevel("pickaxe", 4);
//        MarsItems.deshAxe.setHarvestLevel("axe", 4);
//        MarsItems.deshSpade.setHarvestLevel("shovel", 4);
//    }
//
//    private static void registerItems()
//    {
//        MarsItems.registerItem(MarsItems.carbonFragments);
//        MarsItems.registerItem(MarsItems.marsItemBasic);
//        MarsItems.registerItem(MarsItems.deshPickaxe);
//        MarsItems.registerItem(MarsItems.deshPickSlime);
//        MarsItems.registerItem(MarsItems.deshAxe);
//        MarsItems.registerItem(MarsItems.deshHoe);
//        MarsItems.registerItem(MarsItems.deshSpade);
//        MarsItems.registerItem(MarsItems.deshSword);
//        MarsItems.registerItem(MarsItems.deshHelmet);
//        MarsItems.registerItem(MarsItems.deshChestplate);
//        MarsItems.registerItem(MarsItems.deshLeggings);
//        MarsItems.registerItem(MarsItems.deshBoots);
//        MarsItems.registerItem(MarsItems.rocketMars);
//        MarsItems.registerItem(MarsItems.key);
//        MarsItems.registerItem(MarsItems.schematic);
//    }
//
//    public static void registerItem(Item item)
//    {
//        String name = item.getUnlocalizedName().substring(5);
//        GCCoreUtil.registerGalacticraftItem(name, item);
//        GalacticraftCore.itemListTrue.add(item);
//        item.setRegistryName(name);
//        GalacticraftPlanets.proxy.postRegisterItem(item);
//    }
}
