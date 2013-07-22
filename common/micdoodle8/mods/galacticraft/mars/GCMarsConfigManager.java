package micdoodle8.mods.galacticraft.mars;

import java.io.File;
import java.util.logging.Level;
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
    public static int idBlockBacterialSludge;
    public static int idBlockVine;
    public static int idBlockRock;
    public static int idBlockTreasureChestT2;

    // ITEMS
    public static int idItemMarsBasic;
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
    public static int idEntitySludgeling;
    public static int idEntitySlimeling;

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

            GCMarsConfigManager.idBlockMars = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockMars", 3390).getInt(3390);
            GCMarsConfigManager.idBlockBacterialSludge = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockBacterialSludge", 3391).getInt(3391);
            GCMarsConfigManager.idBlockVine = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockVine", 3392).getInt(3392);
            GCMarsConfigManager.idBlockRock = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockRock", 3393).getInt(3393);
            GCMarsConfigManager.idBlockTreasureChestT2 = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockTreasureChestT2", 3394).getInt(3394);

            GCMarsConfigManager.idItemMarsBasic = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemMarsBasic", 9905).getInt(9905);
            GCMarsConfigManager.idItemSpaceshipTier2 = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemSpaceshipTier2", 9906).getInt(9906);

            GCMarsConfigManager.idToolDeshSword = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolDeshSword", 9907).getInt(9907);
            GCMarsConfigManager.idToolDeshPickaxe = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolDeshPickaxe", 9908).getInt(9908);
            GCMarsConfigManager.idToolDeshSpade = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolDeshSpade", 9909).getInt(9909);
            GCMarsConfigManager.idToolDeshHoe = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolDeshHoe", 9910).getInt(9910);
            GCMarsConfigManager.idToolDeshAxe = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolDeshAxe", 9911).getInt(9911);

            GCMarsConfigManager.idArmorDeshHelmet = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorDeshHelmet", 9912).getInt(9912);
            GCMarsConfigManager.idArmorDeshChestplate = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorDeshChestplate", 9913).getInt(9913);
            GCMarsConfigManager.idArmorDeshLeggings = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorDeshLeggings", 9914).getInt(9914);
            GCMarsConfigManager.idArmorDeshBoots = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorDeshBoots", 9915).getInt(9915);

            GCMarsConfigManager.idEntityCreeperBoss = GCMarsConfigManager.configuration.get("Entities", "idEntityCreeperBoss", 171).getInt(171);
            GCMarsConfigManager.idEntityProjectileTNT = GCMarsConfigManager.configuration.get("Entities", "idEntityProjectileTNT", 172).getInt(172);
            GCMarsConfigManager.idEntitySpaceshipTier2 = GCMarsConfigManager.configuration.get("Entities", "idEntitySpaceshipTier2", 173).getInt(173);
            GCMarsConfigManager.idEntitySludgeling = GCMarsConfigManager.configuration.get("Entities", "idEntitySludgeling", 174).getInt(174);
            GCMarsConfigManager.idEntitySlimeling = GCMarsConfigManager.configuration.get("Entities", "idEntitySlimeling", 175).getInt(175);

            GCMarsConfigManager.idGuiRocketCraftingBenchT2 = GCMarsConfigManager.configuration.get("GUI", "idGuiRocketCraftingBenchT2", 143).getInt(143);

            GCMarsConfigManager.idSchematicRocketT2 = GCMarsConfigManager.configuration.get("Schematic", "idSchematicRocketT2", 2).getInt(2);
        }
        catch (final Exception e)
        {
            FMLLog.log(Level.SEVERE, e, "Galacticraft Mars has a problem loading it's configuration");
        }
        finally
        {
            GCMarsConfigManager.configuration.save();
            GCMarsConfigManager.loaded = true;
        }
    }
}
