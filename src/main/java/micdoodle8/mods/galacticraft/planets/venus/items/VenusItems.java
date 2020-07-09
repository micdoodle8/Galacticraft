package micdoodle8.mods.galacticraft.planets.venus.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.ItemNames;
import micdoodle8.mods.galacticraft.core.items.ItemBase;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.venus.items.*;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static micdoodle8.mods.galacticraft.core.GCBlocks.register;

public class VenusItems
{
    @ObjectHolder(VenusItemNames.shieldController)
    public static Item shieldController;
    @ObjectHolder(VenusItemNames.ingotLead)
    public static Item ingotLead;
    @ObjectHolder(VenusItemNames.radioisotopeCore)
    public static Item radioisotopeCore;
    @ObjectHolder(VenusItemNames.thermalFabric)
    public static Item thermalFabric;
    @ObjectHolder(VenusItemNames.solarDust)
    public static Item solarDust;
    @ObjectHolder(VenusItemNames.solarModule2)
    public static Item solarModule2;
    @ObjectHolder(VenusItemNames.thinSolarWafer)
    public static Item thinSolarWafer;
    @ObjectHolder(VenusItemNames.thermal_helmet_t2)
    public static Item thermal_helmet_t2;
    @ObjectHolder(VenusItemNames.thermal_chestplate_t2)
    public static Item thermal_chestplate_t2;
    @ObjectHolder(VenusItemNames.thermal_leggings_t2)
    public static Item thermal_leggings_t2;
    @ObjectHolder(VenusItemNames.thermal_boots_t2)
    public static Item thermal_boots_t2;
    @ObjectHolder(VenusItemNames.volcanicPickaxe)
    public static Item volcanicPickaxe;
    @ObjectHolder(VenusItemNames.keyT3)
    public static Item keyT3;
    @ObjectHolder(VenusItemNames.atomicBattery)
    public static Item atomicBattery;

    public static Item.Properties defaultBuilder()
    {
        return new Item.Properties()/*.group(GalacticraftCreativeTab.INSTANCE)*/;
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> evt)
    {
        IForgeRegistry<Item> r = evt.getRegistry();
        register(r, new ItemBase(defaultBuilder()), VenusItemNames.shieldController);
        register(r, new ItemBase(defaultBuilder()), VenusItemNames.ingotLead);
        register(r, new ItemBase(defaultBuilder()), VenusItemNames.radioisotopeCore);
        register(r, new ItemBase(defaultBuilder()), VenusItemNames.thermalFabric);
        register(r, new ItemBase(defaultBuilder()), VenusItemNames.solarDust);
        register(r, new ItemBase(defaultBuilder()), VenusItemNames.solarModule2);
        register(r, new ItemBase(defaultBuilder()), VenusItemNames.thinSolarWafer);
        register(r, new ItemThermalPaddingTier2(defaultBuilder()), VenusItemNames.thermal_helmet_t2);
        register(r, new ItemThermalPaddingTier2(defaultBuilder()), VenusItemNames.thermal_chestplate_t2);
        register(r, new ItemThermalPaddingTier2(defaultBuilder()), VenusItemNames.thermal_leggings_t2);
        register(r, new ItemThermalPaddingTier2(defaultBuilder()), VenusItemNames.thermal_boots_t2);
        register(r, new ItemVolcanicPickaxe(defaultBuilder()), VenusItemNames.volcanicPickaxe);
        register(r, new ItemKeyVenus(defaultBuilder().maxStackSize(1).maxDamage(0)), VenusItemNames.keyT3);
        register(r, new ItemBatteryAtomic(defaultBuilder()), VenusItemNames.atomicBattery);

//        VenusItems.registerItems();
//        VenusItems.registerHarvestLevels();
    }

//    public static void registerHarvestLevels()
//    {
//    }
//
//    private static void registerItems()
//    {
//        VenusItems.registerItem(VenusItems.thermalPaddingTier2);
//        VenusItems.registerItem(VenusItems.basicItem);
//        VenusItems.registerItem(VenusItems.volcanicPickaxe);
//        VenusItems.registerItem(VenusItems.key);
//        VenusItems.registerItem(VenusItems.atomicBattery);
//    }
//
//    public static void registerItem(Item item)
//    {
//        String name = item.getUnlocalizedName().substring(5);
//        GCCoreUtil.registerGalacticraftItem(name, item);
//        GalacticraftCore.itemListTrue.add(item);
//        item.setRegistryName(name);
//    }
}
