package micdoodle8.mods.galacticraft.planets.venus;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.items.ItemBasicVenus;
import micdoodle8.mods.galacticraft.planets.venus.items.ItemThermalPaddingTier2;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class VenusItems
{
    public static Item thermalPaddingTier2;
    public static Item basicItem;

    public static void initItems()
    {
        VenusItems.thermalPaddingTier2 = new ItemThermalPaddingTier2("thermal_padding_t2");
        VenusItems.basicItem = new ItemBasicVenus("basic_item_venus");

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
    }

    public static void registerItem(Item item)
    {
        String name = item.getUnlocalizedName().substring(5);
        GCCoreUtil.registerGalacticraftItem(name, item);
        GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
        GalacticraftPlanets.proxy.postRegisterItem(item);
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            GCItems.registerSorted(item);
        }
    }
}
