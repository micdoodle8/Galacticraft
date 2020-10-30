package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBase;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.wrappers.PartialCanister;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_PLANETS)
public class AsteroidsItems
{
    @ObjectHolder(AsteroidItemNames.grapple)
    public static Item grapple;
    @ObjectHolder(AsteroidItemNames.rocketTierThree)
    public static Item rocketTierThree;
    @ObjectHolder(AsteroidItemNames.rocketTierThreeCargo1)
    public static Item rocketTierThreeCargo1;
    @ObjectHolder(AsteroidItemNames.rocketTierThreeCargo2)
    public static Item rocketTierThreeCargo2;
    @ObjectHolder(AsteroidItemNames.rocketTierThreeCargo3)
    public static Item rocketTierThreeCargo3;
    @ObjectHolder(AsteroidItemNames.rocketTierThreeCreative)
    public static Item rocketTierThreeCreative;
    @ObjectHolder(AsteroidItemNames.astroMiner)
    public static Item astroMiner;
    @ObjectHolder(AsteroidItemNames.thermalHelm)
    public static Item thermalHelm;
    @ObjectHolder(AsteroidItemNames.thermalChestplate)
    public static Item thermalChestplate;
    @ObjectHolder(AsteroidItemNames.thermalLeggings)
    public static Item thermalLeggings;
    @ObjectHolder(AsteroidItemNames.thermalBoots)
    public static Item thermalBoots;
    @ObjectHolder(AsteroidItemNames.methaneCanister)
    public static Item methaneCanister;
    @ObjectHolder(AsteroidItemNames.canisterLOX)
    public static Item canisterLOX;
    @ObjectHolder(AsteroidItemNames.canisterLN2)
    public static Item canisterLN2;
//    @ObjectHolder(AsteroidItemNames.canisterLAr)
//    public static Item canisterLAr;
    @ObjectHolder(AsteroidItemNames.atmosphericValve)
    public static Item atmosphericValve;
    @ObjectHolder(AsteroidItemNames.heavyNoseCone)
    public static Item heavyNoseCone;
    @ObjectHolder(AsteroidItemNames.orionDrive)
    public static Item orionDrive;
    @ObjectHolder(AsteroidItemNames.titaniumHelmet)
    public static Item titaniumHelmet;
    @ObjectHolder(AsteroidItemNames.titaniumChestplate)
    public static Item titaniumChestplate;
    @ObjectHolder(AsteroidItemNames.titaniumLeggings)
    public static Item titaniumLeggings;
    @ObjectHolder(AsteroidItemNames.titaniumBoots)
    public static Item titaniumBoots;
    @ObjectHolder(AsteroidItemNames.titaniumAxe)
    public static Item titaniumAxe;
    @ObjectHolder(AsteroidItemNames.titaniumPickaxe)
    public static Item titaniumPickaxe;
    @ObjectHolder(AsteroidItemNames.titaniumSpade)
    public static Item titaniumSpade;
    @ObjectHolder(AsteroidItemNames.titaniumHoe)
    public static Item titaniumHoe;
    @ObjectHolder(AsteroidItemNames.titaniumSword)
    public static Item titaniumSword;
    @ObjectHolder(AsteroidItemNames.strangeSeed0)
    public static Item strangeSeed0;
    @ObjectHolder(AsteroidItemNames.strangeSeed1)
    public static Item strangeSeed1;
    @ObjectHolder(AsteroidItemNames.ingotTitanium)
    public static Item ingotTitanium;
    @ObjectHolder(AsteroidItemNames.engineT2)
    public static Item engineT2;
    @ObjectHolder(AsteroidItemNames.rocketFinsT2)
    public static Item rocketFinsT2;
    @ObjectHolder(AsteroidItemNames.shardIron)
    public static Item shardIron;
    @ObjectHolder(AsteroidItemNames.shardTitanium)
    public static Item shardTitanium;
    @ObjectHolder(AsteroidItemNames.reinforcedPlateT3)
    public static Item reinforcedPlateT3;
    @ObjectHolder(AsteroidItemNames.compressedTitanium)
    public static Item compressedTitanium;
    @ObjectHolder(AsteroidItemNames.thermalCloth)
    public static Item thermalCloth;
    @ObjectHolder(AsteroidItemNames.beamCore)
    public static Item beamCore;
    @ObjectHolder(AsteroidItemNames.dustTitanium)
    public static Item dustTitanium;

//    public static Item.ToolMaterial TOOL_TITANIUM = EnumHelper.addToolMaterial("titanium", 4, 760, 14.0F, 4.0F, 16);
//    public static ArmorItem.ArmorMaterial ARMOR_TITANIUM = EnumHelper.addArmorMaterial("titanium", "", 26, new int[] { 5, 7, 10, 5 }, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name)
    {
        GCBlocks.register(reg, thing, new ResourceLocation(Constants.MOD_ID_PLANETS, name));
    }

    public static Item.Properties defaultBuilder()
    {
        return new Item.Properties().group(GalacticraftCore.galacticraftItemsTab);
    }

    public static void registerItems(RegistryEvent.Register<Item> evt)
    {
        IForgeRegistry<Item> r = evt.getRegistry();
        register(r, new ItemGrappleHook(defaultBuilder()), AsteroidItemNames.grapple);
        register(r, new ItemTier3Rocket(defaultBuilder()), AsteroidItemNames.rocketTierThree);
        register(r, new ItemTier3Rocket(defaultBuilder()), AsteroidItemNames.rocketTierThreeCargo1);
        register(r, new ItemTier3Rocket(defaultBuilder()), AsteroidItemNames.rocketTierThreeCargo2);
        register(r, new ItemTier3Rocket(defaultBuilder()), AsteroidItemNames.rocketTierThreeCargo3);
        register(r, new ItemTier3Rocket(defaultBuilder()), AsteroidItemNames.rocketTierThreeCreative);
        register(r, new ItemAstroMiner(defaultBuilder().maxDamage(0).maxStackSize(1)), AsteroidItemNames.astroMiner);
        register(r, new ItemThermalPadding(defaultBuilder()), AsteroidItemNames.thermalHelm);
        register(r, new ItemThermalPadding(defaultBuilder()), AsteroidItemNames.thermalChestplate);
        register(r, new ItemThermalPadding(defaultBuilder()), AsteroidItemNames.thermalLeggings);
        register(r, new ItemThermalPadding(defaultBuilder()), AsteroidItemNames.thermalBoots);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.ingotTitanium);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.engineT2);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.rocketFinsT2);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.shardIron);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.shardTitanium);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.reinforcedPlateT3);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.compressedTitanium);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.thermalCloth);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.beamCore);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.dustTitanium);
        register(r, new ItemCanisterMethane(defaultBuilder()), AsteroidItemNames.methaneCanister);
        register(r, new ItemCanisterLiquidOxygen(defaultBuilder()), AsteroidItemNames.canisterLOX);
        register(r, new ItemCanisterLiquidNitrogen(defaultBuilder()), AsteroidItemNames.canisterLN2);
//        register(r, new ItemCanisterLiquidArgon(defaultBuilder()), AsteroidItemNames.canisterLAr);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.atmosphericValve);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.heavyNoseCone);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.orionDrive);
        register(r, new ItemArmorAsteroids(EquipmentSlotType.HEAD, defaultBuilder()), AsteroidItemNames.titaniumHelmet);
        register(r, new ItemArmorAsteroids(EquipmentSlotType.CHEST, defaultBuilder()), AsteroidItemNames.titaniumChestplate);
        register(r, new ItemArmorAsteroids(EquipmentSlotType.LEGS, defaultBuilder()), AsteroidItemNames.titaniumLeggings);
        register(r, new ItemArmorAsteroids(EquipmentSlotType.FEET, defaultBuilder()), AsteroidItemNames.titaniumBoots);
        register(r, new ItemAxeAsteroids(defaultBuilder()), AsteroidItemNames.titaniumAxe);
        register(r, new ItemPickaxeAsteroids(defaultBuilder()), AsteroidItemNames.titaniumPickaxe);
        register(r, new ItemSpadeAsteroids(defaultBuilder()), AsteroidItemNames.titaniumSpade);
        register(r, new ItemHoeAsteroids(defaultBuilder()), AsteroidItemNames.titaniumHoe);
        register(r, new ItemSwordAsteroids(defaultBuilder()), AsteroidItemNames.titaniumSword);
        register(r, new ItemStrangeSeed(defaultBuilder()), AsteroidItemNames.strangeSeed0);
        register(r, new ItemStrangeSeed(defaultBuilder()), AsteroidItemNames.strangeSeed1);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
        {
            ClientProxyCore.registerCanister(new PartialCanister(AsteroidsItems.methaneCanister, Constants.MOD_ID_PLANETS, "methane_canister_partial", 7));
            ClientProxyCore.registerCanister(new PartialCanister(AsteroidsItems.canisterLOX, Constants.MOD_ID_PLANETS, "canister_partial_lox", 7));
            ClientProxyCore.registerCanister(new PartialCanister(AsteroidsItems.canisterLN2, Constants.MOD_ID_PLANETS, "canister_partial_ln2", 7));
        });
    }

//    public static void oreDictRegistrations()
//    {
//        OreDictionary.registerOre("compressedTitanium", new ItemStack(AsteroidsItems.basicItem, 1, 6));
//        OreDictionary.registerOre("ingotTitanium", new ItemStack(AsteroidsItems.basicItem, 1, 0));
//        OreDictionary.registerOre("shardTitanium", new ItemStack(AsteroidsItems.basicItem, 1, 4));
//        OreDictionary.registerOre("shardIron", new ItemStack(AsteroidsItems.basicItem, 1, 3));
//        OreDictionary.registerOre("dustTitanium", new ItemStack(AsteroidsItems.basicItem, 1, 9));
//    }
//
//    public static void registerHarvestLevels()
//    {
//        AsteroidsItems.titaniumPickaxe.setHarvestLevel("pickaxe", 5);
//        AsteroidsItems.titaniumAxe.setHarvestLevel("axe", 5);
//        AsteroidsItems.titaniumSpade.setHarvestLevel("shovel", 5);
//    }

//    private static void registerItems()
//    {
//        registerItem(AsteroidsItems.grapple);
//        registerItem(AsteroidsItems.tier3Rocket);
//        registerItem(AsteroidsItems.astroMiner);
//        registerItem(AsteroidsItems.thermalPadding);
//        registerItem(AsteroidsItems.methaneCanister);
//        registerItem(AsteroidsItems.canisterLOX);
//        registerItem(AsteroidsItems.canisterLN2);
//        //registerItem(AsteroidsItems.canisterLAr);
//        registerItem(AsteroidsItems.atmosphericValve);
//        registerItem(AsteroidsItems.heavyNoseCone);
//        registerItem(AsteroidsItems.orionDrive);
//        registerItem(AsteroidsItems.titaniumHelmet);
//        registerItem(AsteroidsItems.titaniumChestplate);
//        registerItem(AsteroidsItems.titaniumLeggings);
//        registerItem(AsteroidsItems.titaniumBoots);
//        registerItem(AsteroidsItems.titaniumAxe);
//        registerItem(AsteroidsItems.titaniumPickaxe);
//        registerItem(AsteroidsItems.titaniumSpade);
//        registerItem(AsteroidsItems.titaniumHoe);
//        registerItem(AsteroidsItems.titaniumSword);
//        registerItem(AsteroidsItems.strangeSeed);
//
//        ARMOR_TITANIUM.setRepairItem(new ItemStack(AsteroidsItems.basicItem, 1, 0));
//
//        GCItems.canisterTypes.add((ItemCanisterGeneric) AsteroidsItems.canisterLOX);
//        GCItems.canisterTypes.add((ItemCanisterGeneric) AsteroidsItems.methaneCanister);
//        GCItems.canisterTypes.add((ItemCanisterGeneric) AsteroidsItems.canisterLN2);
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
