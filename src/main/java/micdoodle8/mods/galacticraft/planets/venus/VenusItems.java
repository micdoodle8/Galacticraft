package micdoodle8.mods.galacticraft.planets.venus;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.items.*;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

public class VenusItems
{
    public static Item thermalPaddingTier2;
    public static Item basicItem;
    public static Item volcanicPickaxe;
    public static Item key;
    public static Item bucketSulphuricAcid;
    public static Item atomicBattery;

    public static Item.ToolMaterial TOOL_VOLCANIC = EnumHelper.addToolMaterial("volcanic", 3, 1561, 32.0F, 1.5F, 10);

    public static void initItems()
    {
        VenusItems.thermalPaddingTier2 = new ItemThermalPaddingTier2("thermal_padding_t2");
        VenusItems.basicItem = new ItemBasicVenus("basic_item_venus");
        VenusItems.volcanicPickaxe = new ItemVolcanicPickaxe("volcanic_pickaxe");
        VenusItems.key = new ItemKeyVenus("key_t3");
        VenusItems.atomicBattery = new ItemBatteryAtomic("atomic_battery");

        VenusItems.registerItems();
        VenusItems.registerHarvestLevels();
    }

    public static void registerHarvestLevels()
    {
    }

    private static void registerItems()
    {
        VenusItems.registerItem(VenusItems.thermalPaddingTier2);
        VenusItems.registerItem(VenusItems.basicItem);
        VenusItems.registerItem(VenusItems.volcanicPickaxe);
        VenusItems.registerItem(VenusItems.key);
        VenusItems.registerItem(VenusItems.atomicBattery);
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
