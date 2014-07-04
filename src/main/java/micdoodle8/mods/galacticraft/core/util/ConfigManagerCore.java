package micdoodle8.mods.galacticraft.core.util;

import com.google.common.primitives.Ints;
import cpw.mods.fml.client.config.IConfigElement;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConfigManagerCore
{
	public static boolean loaded;

	static Configuration configuration;

	public static int idDimensionOverworldOrbit;
	public static int idDimensionOverworldOrbitStatic;
	public static int idDimensionMoon;

	// SCHEMATICS
	public static int idSchematicRocketT1;
	public static int idSchematicMoonBuggy;
	public static int idSchematicAddSchematic;

	// ACHIEVEMENTS
	public static int idAchievBase;

	public static int idEntityEvolvedSpider;
	public static int idEntityEvolvedCreeper;
	public static int idEntityEvolvedZombie;
	public static int idEntityEvolvedSkeleton;
	public static int idEntityEvolvedSkeletonBoss;
	public static int idEntitySpaceship;
	public static int idEntityAntiGravityArrow;
	public static int idEntityMeteor;
	public static int idEntityBuggy;
	public static int idEntityFlag;
	public static int idEntityAstroOrb;
	public static int idEntityParaChest;
	public static int idEntityAlienVillager;
	public static int idEntityOxygenBubble;
	public static int idEntityLander;
	public static int idEntityLanderChest;
	public static int idEntityMeteorChunk;

	// GENERAL
	public static boolean moreStars;
	public static boolean wasdMapMovement;
	public static String[] sealableIDs;
	public static String[] detectableIDs;
	public static boolean disableSpaceshipParticles;
	public static boolean disableSpaceshipGrief;
	public static boolean oxygenIndicatorLeft;
	public static boolean oxygenIndicatorBottom;
	public static double oilGenFactor;
	public static boolean disableLeafDecay;
	public static boolean spaceStationsRequirePermission;
	public static boolean disableSpaceStationCreation;
	public static boolean overrideCapes;
	public static double spaceStationEnergyScalar;
	public static boolean disableLander;
	public static double dungeonBossHealthMod;
	public static int suffocationCooldown;
	public static int suffocationDamage;
	public static int[] externalOilGen;
	public static boolean forceOverworldRespawn;
	public static boolean enableDebug;
	public static boolean enableCopperOreGen;
	public static boolean enableTinOreGen;
	public static boolean enableAluminumOreGen;
	public static boolean enableSiliconOreGen;
	public static int[] staticLoadDimensions = {};
	public static boolean disableCheeseMoon;
	public static boolean disableTinMoon;
	public static boolean disableCopperMoon;
	public static boolean disableMoonVillageGen;
	public static boolean enableSealerMultithreading;
	public static boolean enableSealerEdgeChecks;
	public static boolean alternateCanisterRecipe;
	public static boolean enableSmallMoons;

	public static void initialize(File file)
	{
		if (!ConfigManagerCore.loaded)
		{
			ConfigManagerCore.configuration = new Configuration(file);
		}

        ConfigManagerCore.configuration.load();
        ConfigManagerCore.syncConfig();
	}

    public static void syncConfig()
    {
        try
        {
            ConfigManagerCore.idDimensionMoon = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "idDimensionMoon", -28).getInt(-28);
            ConfigManagerCore.idDimensionOverworldOrbit = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "idDimensionOverworldOrbit", -27).getInt(-27);
            ConfigManagerCore.idDimensionOverworldOrbitStatic = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "idDimensionOverworldOrbitStatic", -26, "Static Space Station ID").getInt(-26);
            ConfigManagerCore.staticLoadDimensions = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "Static Loaded Dimensions", ConfigManagerCore.staticLoadDimensions, "IDs to load at startup, and keep loaded until server stops. Can be added via /gckeeploaded").getIntList();

            ConfigManagerCore.idSchematicRocketT1 = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicRocketT1", 0).getInt(0);
            ConfigManagerCore.idSchematicMoonBuggy = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicMoonBuggy", 1).getInt(1);
            ConfigManagerCore.idSchematicAddSchematic = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicAddSchematic", Integer.MAX_VALUE).getInt(Integer.MAX_VALUE);

            ConfigManagerCore.idAchievBase = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_ACHIEVEMENTS, "idAchievBase", 1784).getInt(1784);

            ConfigManagerCore.idEntityEvolvedSpider = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityEvolvedSpider", 155).getInt(155);
            ConfigManagerCore.idEntityEvolvedCreeper = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityEvolvedCreeper", 156).getInt(156);
            ConfigManagerCore.idEntityEvolvedZombie = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityEvolvedZombie", 157).getInt(157);
            ConfigManagerCore.idEntityEvolvedSkeleton = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityEvolvedSkeleton", 158).getInt(158);
            ConfigManagerCore.idEntitySpaceship = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntitySpaceship", 159).getInt(159);
            ConfigManagerCore.idEntityAntiGravityArrow = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityAntiGravityArrow", 160).getInt(160);
            ConfigManagerCore.idEntityMeteor = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityMeteor", 161).getInt(161);
            ConfigManagerCore.idEntityBuggy = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityBuggy", 162).getInt(162);
            ConfigManagerCore.idEntityFlag = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityFlag", 163).getInt(163);
            ConfigManagerCore.idEntityAstroOrb = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityAstroOrb", 164).getInt(164);
            ConfigManagerCore.idEntityParaChest = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityParaChest", 165).getInt(165);
            ConfigManagerCore.idEntityAlienVillager = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityAlienVillager", 166).getInt(166);
            ConfigManagerCore.idEntityOxygenBubble = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityOxygenBubble", 167).getInt(167);
            ConfigManagerCore.idEntityLander = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityLander", 168).getInt(168);
            ConfigManagerCore.idEntityLanderChest = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityLanderChest", 169).getInt(169);
            ConfigManagerCore.idEntityEvolvedSkeletonBoss = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityEvolvedSkeletonBoss", 170).getInt(170);
            ConfigManagerCore.idEntityMeteorChunk = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityMeteorChunk", 179).getInt(179);

            ConfigManagerCore.moreStars = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "More Stars", true, "Setting this to false will revert night skies back to default minecraft star count").getBoolean(true);
            ConfigManagerCore.wasdMapMovement = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "WASD Map Movement", true, "If you prefer to move the Galaxy map with your mouse, set to false").getBoolean(true);
            ConfigManagerCore.oilGenFactor = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Oil Generation Factor", 1.8, "Increasing this will increase amount of oil that will generate in each chunk.").getDouble(1.8);
            ConfigManagerCore.disableSpaceshipParticles = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Disable Spaceship Particles", false, "If you have FPS problems, setting this to true will help if spaceship particles are in your sights").getBoolean(false);
            ConfigManagerCore.disableSpaceshipGrief = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Disable Spaceship Explosion", false, "Spaceships will not explode on contact if set to true").getBoolean(false);
            ConfigManagerCore.oxygenIndicatorLeft = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Minimap Left", false, "If true, this will move the Oxygen Indicator to the left side. You can combine this with \"Minimap Bottom\"").getBoolean(false);
            ConfigManagerCore.oxygenIndicatorBottom = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Minimap Bottom", false, "If true, this will move the Oxygen Indicator to the bottom. You can combine this with \"Minimap Left\"").getBoolean(false);
            ConfigManagerCore.disableLeafDecay = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Disable Oxygen Collector Leaf Decay", false, "If set to true, Oxygen Collectors will not consume leaf blocks.").getBoolean(false);
            ConfigManagerCore.spaceStationsRequirePermission = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Space Stations Require Permission", true, "While true, space stations require you to invite other players using /ssinvite <playername>").getBoolean(true);
            ConfigManagerCore.disableSpaceStationCreation = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Disable Space Station creation", false, "If set to true on a server, players will be completely unable to create space stations.").getBoolean(false);
            ConfigManagerCore.overrideCapes = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Override Capes", true, "By default, Galacticraft will override capes with the mod's donor cape. Set to false to disable.").getBoolean(true);
            ConfigManagerCore.spaceStationEnergyScalar = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Space Station Solar Energy Multiplier", 2.0, "If Mekanism is installed, solar panels will work (default 2x) more effective on space stations.").getDouble(2.0);
            ConfigManagerCore.sealableIDs = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "External Sealable IDs", new String[] { String.valueOf(Block.getIdFromBlock(Blocks.glass_pane) + ":0") }, "List IDs of non-opaque blocks from other mods (for example, special types of glass) that the Oxygen Sealer should recognize as solid seals. Format is ID:METADATA").getStringList();
            ConfigManagerCore.detectableIDs = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "External Detectable IDs", new String[] { String.valueOf(Block.getIdFromBlock(Blocks.coal_ore) + ":0"), String.valueOf(Block.getIdFromBlock(Blocks.diamond_ore) + ":0"), String.valueOf(Block.getIdFromBlock(Blocks.gold_ore) + ":0"), String.valueOf(Block.getIdFromBlock(Blocks.iron_ore) + ":0"), String.valueOf(Block.getIdFromBlock(Blocks.lapis_ore) + ":0"), String.valueOf(Block.getIdFromBlock(Blocks.redstone_ore) + ":0"), String.valueOf(Block.getIdFromBlock(Blocks.lit_redstone_ore) + ":0") }, "List IDs from other mods that the Sensor Glasses should recognize as solid blocks. Format is ID:METADATA").getStringList();
            ConfigManagerCore.dungeonBossHealthMod = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Dungeon Boss Health Modifier", 1.0D, "Change this is you wish to balance the mod (if you have more powerful weapon mods)").getDouble(1.0D);
            ConfigManagerCore.suffocationCooldown = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Suffocation Cooldown", 100, "Lower/Raise this value to change time between suffocation damage ticks").getInt(100);
            ConfigManagerCore.suffocationDamage = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Suffocation Damage", 2, "Change this value to modify the damage taken per suffocation tick").getInt(2);
            ConfigManagerCore.externalOilGen = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Oil gen in external dimensions", new int[] { 0 }, "List of non-galacticraft dimension IDs to generate oil in").getIntList();
            ConfigManagerCore.forceOverworldRespawn = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Force Overworld Spawn", false, "By default, you will respawn on galacticraft dimensions if you die. If you set this to true, you will respawn back on earth.").getBoolean(false);
            ConfigManagerCore.enableDebug = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Enable Debug Messages", false, "If this is enabled, debug messages will appear in the console. This is useful for finding bugs in the mod.").getBoolean(false);
            ConfigManagerCore.enableSealerMultithreading = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Enable Sealer Multithreading", false, "(Experimental) If this is enabled, Oxygen Sealers seal checks will run in a separate thread - faster but there may be block deletions or other severe artifacts.").getBoolean(false);
            ConfigManagerCore.enableSealerEdgeChecks = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Enable Sealed edge checks", true, "If this is enabled, areas sealed by Oxygen Sealers will run a seal check when the player breaks or places a block (or on block updates).  This should be enabled for a 100% accurate sealed status is accurate, but can be disabled on servers for performance reasons.").getBoolean(true);
            ConfigManagerCore.enableCopperOreGen = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Enable Copper Ore Gen", true, "If this is enabled, copper ore will generate on the overworld.").getBoolean(true);
            ConfigManagerCore.enableTinOreGen = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Enable Tin Ore Gen", true, "If this is enabled, tin ore will generate on the overworld.").getBoolean(true);
            ConfigManagerCore.enableAluminumOreGen = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Enable Aluminum Ore Gen", true, "If this is enabled, aluminum ore will generate on the overworld.").getBoolean(true);
            ConfigManagerCore.enableSiliconOreGen = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Enable Silicon Ore Gen", true, "If this is enabled, silicon ore will generate on the overworld.").getBoolean(true);
            ConfigManagerCore.disableCheeseMoon = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Disable Cheese Ore Gen on Moon", false).getBoolean(false);
            ConfigManagerCore.disableTinMoon = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Disable Tin Ore Gen on Moon", false).getBoolean(false);
            ConfigManagerCore.disableCopperMoon = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Disable Copper Ore Gen on Moon", false).getBoolean(false);
            ConfigManagerCore.disableMoonVillageGen = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Disable Moon Village Gen", false).getBoolean(false);
            ConfigManagerCore.alternateCanisterRecipe = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Alternate recipe for canisters", false, "Enable this if the standard canister recipe causes a conflict.").getBoolean(false);
            ConfigManagerCore.enableSmallMoons = ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Enable Small Moons", true, "This will cause some dimensions to appear round, disable if render transformations cause a conflict.").getBoolean(true);
        }
        catch (final Exception e)
        {
            GCLog.severe("Problem loading core config (\"core.conf\")");
        }
        finally
        {
            if (ConfigManagerCore.configuration.hasChanged())
            {
                ConfigManagerCore.configuration.save();
            }

            ConfigManagerCore.loaded = true;
        }
    }

	public static boolean setLoaded(int newID)
	{
		boolean found = false;

		for (int staticLoadDimension : ConfigManagerCore.staticLoadDimensions)
		{
			if (staticLoadDimension == newID)
			{
				found = true;
				break;
			}
		}

		if (!found)
		{
			int[] oldIDs = ConfigManagerCore.staticLoadDimensions;
			ConfigManagerCore.staticLoadDimensions = new int[ConfigManagerCore.staticLoadDimensions.length + 1];

			for (int i = 0; i < oldIDs.length; i++)
			{
				ConfigManagerCore.staticLoadDimensions[i] = oldIDs[i];
			}

			ConfigManagerCore.staticLoadDimensions[ConfigManagerCore.staticLoadDimensions.length - 1] = newID;
			String[] values = new String[ConfigManagerCore.staticLoadDimensions.length];
			Arrays.sort(ConfigManagerCore.staticLoadDimensions);

			for (int i = 0; i < values.length; i++)
			{
				values[i] = String.valueOf(ConfigManagerCore.staticLoadDimensions[i]);
			}

			ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "Static Loaded Dimensions", ConfigManagerCore.staticLoadDimensions, "IDs to load at startup, and keep loaded until server stops. Can be added via /gckeeploaded").set(values);
			ConfigManagerCore.configuration.save();
		}

		return !found;
	}

	public static boolean setUnloaded(int idToRemove)
	{
		int foundCount = 0;

		for (int staticLoadDimension : ConfigManagerCore.staticLoadDimensions)
		{
			if (staticLoadDimension == idToRemove)
			{
				foundCount++;
			}
		}

		if (foundCount > 0)
		{
			List<Integer> idArray = new ArrayList<Integer>(Ints.asList(ConfigManagerCore.staticLoadDimensions));
			idArray.removeAll(Collections.singleton(idToRemove));

			ConfigManagerCore.staticLoadDimensions = new int[idArray.size()];

			for (int i = 0; i < idArray.size(); i++)
			{
				ConfigManagerCore.staticLoadDimensions[i] = idArray.get(i);
			}

			String[] values = new String[ConfigManagerCore.staticLoadDimensions.length];
			Arrays.sort(ConfigManagerCore.staticLoadDimensions);

			for (int i = 0; i < values.length; i++)
			{
				values[i] = String.valueOf(ConfigManagerCore.staticLoadDimensions[i]);
			}

			ConfigManagerCore.configuration.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "Static Loaded Dimensions", ConfigManagerCore.staticLoadDimensions, "IDs to load at startup, and keep loaded until server stops. Can be added via /gckeeploaded").set(values);
			ConfigManagerCore.configuration.save();
		}

		return foundCount > 0;
	}

    public static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.addAll(new ConfigElement(configuration.getCategory(Constants.CONFIG_CATEGORY_DIMENSIONS)).getChildElements());
        list.addAll(new ConfigElement(configuration.getCategory(Constants.CONFIG_CATEGORY_SCHEMATIC)).getChildElements());
        list.addAll(new ConfigElement(configuration.getCategory(Constants.CONFIG_CATEGORY_ACHIEVEMENTS)).getChildElements());
        list.addAll(new ConfigElement(configuration.getCategory(Constants.CONFIG_CATEGORY_ENTITIES)).getChildElements());
        list.addAll(new ConfigElement(configuration.getCategory(Constants.CONFIG_CATEGORY_GENERAL)).getChildElements());
        return list;
    }
}
