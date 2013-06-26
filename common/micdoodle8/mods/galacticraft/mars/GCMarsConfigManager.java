package micdoodle8.mods.galacticraft.mars;

import java.io.File;
import java.util.logging.Level;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCMarsConfigManager
{
    public static boolean loaded;

    static Configuration configuration;

    public GCMarsConfigManager(File file)
    {
        if (!GCMarsConfigManager.loaded)
        {
            GCMarsConfigManager.configuration = new Configuration(file);
            this.setDefaultValues();
        }
    }

    // DIMENSIONS
    public static int dimensionIDMars;

    // BLOCKS
    public static int idBlockMars;

    // ITEMS
    public static int idItemRawDesh;
    public static int idItemDeshStick;
    public static int idItemIngotDesh;
    public static int idItemSpaceshipTier2;

    // ARMOR
    public static int idArmorDeshHelmet;
    public static int idArmorDeshChestplate;
    public static int idArmorDeshLeggings;
    public static int idArmorDeshBoots;

    // TOOLS
    public static int idToolDeshSword;
    public static int idToolDeshPickaxe;
    public static int idToolDeshAxe;
    public static int idToolDeshSpade;
    public static int idToolDeshHoe;

    // MOBS
    public static int idEntityCreeperBoss;
    public static int idEntityProjectileTNT;
    public static int idEntitySpaceshipTier2;

    // GUI
    public static int idGuiRocketCraftingBenchT2;
    
    // SCHEMATIC
    public static int idSchematicRocketT2;

    private void setDefaultValues()
    {
        try
        {
            GCMarsConfigManager.configuration.load();

            GCMarsConfigManager.dimensionIDMars = GCMarsConfigManager.configuration.get("Dimensions", "Mars Dimension ID", -29).getInt(-29);
            
            GCMarsConfigManager.idBlockMars = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockMars", 3390, "Even though this will be generated, it can use block IDs greater than 256").getInt(3390);

            GCMarsConfigManager.idItemRawDesh = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemRawDesh", 9905).getInt(9905);
            GCMarsConfigManager.idItemDeshStick = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemDeshStick", 9906).getInt(9906);
            GCMarsConfigManager.idItemIngotDesh = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemIngotDesh", 9908).getInt(9908);
            GCMarsConfigManager.idItemSpaceshipTier2 = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemSpaceshipTier2", 9936).getInt(9936);

            GCMarsConfigManager.idToolDeshPickaxe = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolDeshPickaxe", 9915).getInt(9915);
            GCMarsConfigManager.idToolDeshSpade = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolDeshSpade", 9916).getInt(9916);
            GCMarsConfigManager.idToolDeshHoe = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolDeshHoe", 9917).getInt(9917);
            GCMarsConfigManager.idToolDeshAxe = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolDeshAxe", 9918).getInt(9918);

            GCMarsConfigManager.idArmorDeshHelmet = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorDeshHelmet", 9929).getInt(9929);
            GCMarsConfigManager.idArmorDeshChestplate = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorDeshChestplate", 9930).getInt(9930);
            GCMarsConfigManager.idArmorDeshLeggings = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorDeshLeggings", 9931).getInt(9931);
            GCMarsConfigManager.idArmorDeshBoots = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorDeshBoots", 9932).getInt(9932);

            GCMarsConfigManager.idEntityCreeperBoss = GCMarsConfigManager.configuration.get("Entities", "idEntityCreeperBoss", 162).getInt(162);
            GCMarsConfigManager.idEntityProjectileTNT = GCMarsConfigManager.configuration.get("Entities", "idEntityProjectileTNT", 164).getInt(164);
            GCMarsConfigManager.idEntitySpaceshipTier2 = GCMarsConfigManager.configuration.get("Entities", "idEntitySpaceshipTier2", 167).getInt(167);
            
            GCMarsConfigManager.idGuiRocketCraftingBenchT2 = GCMarsConfigManager.configuration.get("GUI", "idGuiRocketCraftingBenchT2", 142).getInt(142);
            
            GCMarsConfigManager.idSchematicRocketT2 = GCMarsConfigManager.configuration.get("Schematic", "idSchematicRocketT2", 2).getInt(2);
        }
        catch (final Exception e)
        {
            FMLLog.log(Level.SEVERE, e, "Galacticraft has a problem loading it's configuration");
        }
        finally
        {
            GCMarsConfigManager.configuration.save();
            GCMarsConfigManager.loaded = true;
        }
    }
}
