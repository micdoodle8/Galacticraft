package micdoodle8.mods.galacticraft.planets;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigManagerPlanets
{
    ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

//    public static int dimensionIDMars;
//    public static int dimensionIDAsteroids;
//    public static int dimensionIDVenus;

    public static int idSchematicRocketT2;
    public static int idSchematicCargoRocket;
    public static int idSchematicRocketT3;

    public static boolean launchControllerChunkLoad;
    public static boolean launchControllerAllDims;
    public static boolean disableGalacticraftHelium;
    public static boolean disableSmallAsteroids;
    public static int astroMinerMax;
    public static boolean disableAmbientLightning;

    public static boolean disableDeshGenMars;
    public static boolean disableTinGenMars;
    public static boolean disableCopperGenMars;
    public static boolean disableIronGenMars;
    public static boolean disableIlmeniteGenAsteroids;
    public static boolean disableIronGenAsteroids;
    public static boolean disableAluminumGenAsteroids;
    public static boolean disableAluminumGenVenus;
    public static boolean disableCopperGenVenus;
    public static boolean disableGalenaGenVenus;
    public static boolean disableQuartzGenVenus;
    public static boolean disableSiliconGenVenus;
    public static boolean disableTinGenVenus;
    public static boolean disableSolarGenVenus;

    public ConfigManagerPlanets()
    {
        COMMON_BUILDER.push("WORLDGEN");

        disableAluminumGenVenus = COMMON_BUILDER.comment("Disable Aluminum Ore Gen on Venus.")
                .translation("gc.configgui.disable_venus_aluminum_gen")
                .define("disable_aluminum_ore_gen_on_venus", false).get();

        disableCopperGenVenus = COMMON_BUILDER.comment("Disable Copper Ore Gen on Venus.")
                .translation("gc.configgui.disable_venus_copper_gen")
                .define("disable_copper_ore_gen_on_venus", false).get();

        disableGalenaGenVenus = COMMON_BUILDER.comment("Disable Galena Ore Gen on Venus.")
                .translation("gc.configgui.disable_venus_galena_gen")
                .define("disable_galena_ore_gen_on_venus", false).get();

        disableQuartzGenVenus = COMMON_BUILDER.comment("Disable Quartz Ore Gen on Venus.")
                .translation("gc.configgui.disable_venus_quartz_gen")
                .define("disable_quartz_ore_gen_on_venus", false).get();

        disableSiliconGenVenus = COMMON_BUILDER.comment("Disable Silicon Ore Gen on Venus.")
                .translation("gc.configgui.disable_venus_silicon_gen")
                .define("disable_silicon_ore_gen_on_venus", false).get();

        disableTinGenVenus = COMMON_BUILDER.comment("Disable Tin Ore Gen on Venus.")
                .translation("gc.configgui.disable_venus_tin_gen")
                .define("disable_tin_ore_gen_on_venus", false).get();

        disableSolarGenVenus = COMMON_BUILDER.comment("Disable Solar Ore Gen on Venus.")
                .translation("gc.configgui.disable_venus_solar_gen")
                .define("disable_solar_ore_gen_on_venus", false).get();

        disableIronGenAsteroids = COMMON_BUILDER.comment("Disable Iron Ore Gen on Asteroids.")
                .translation("gc.configgui.disable_iron_gen_asteroids")
                .define("disable_iron_ore_gen_on_asteroids", false).get();

        disableAluminumGenAsteroids = COMMON_BUILDER.comment("Disable Aluminum Ore Gen on Asteroids.")
                .translation("gc.configgui.disable_aluminum_gen_asteroids")
                .define("disable_aluminum_ore_gen_on_asteroids", false).get();

        disableIlmeniteGenAsteroids = COMMON_BUILDER.comment("Disable Ilmenite Ore Gen on Asteroids.")
                .translation("gc.configgui.disable_ilmenite_gen_asteroids")
                .define("disable_ilmenite_ore_gen_on_asteroids", false).get();

        disableIronGenMars = COMMON_BUILDER.comment("Disable Iron Ore Gen on Mars.")
                .translation("gc.configgui.disable_iron_gen_mars")
                .define("disable_iron_ore_gen_on_mars", false).get();

        disableCopperGenMars = COMMON_BUILDER.comment("Disable Copper Ore Gen on Mars.")
                .translation("gc.configgui.disable_copper_gen_mars")
                .define("disable_copper_ore_gen_on_mars", false).get();

        disableTinGenMars = COMMON_BUILDER.comment("Disable Tin Ore Gen on Mars.")
                .translation("gc.configgui.disable_tin_gen_mars")
                .define("disable_tin_ore_gen_on_mars", false).get();

        disableDeshGenMars = COMMON_BUILDER.comment("Disable Desh Ore Gen on Mars.")
                .translation("gc.configgui.disable_desh_gen_mars")
                .define("disable_desh_ore_gen_on_mars", false).get();

        COMMON_BUILDER.pop();

        COMMON_BUILDER.push("DIMENSIONS");

//        dimensionIDVenus = COMMON_BUILDER.comment("Dimension ID for Venus")
//                .translation("gc.configgui.dimension_id_venus")
//                .defineInRange("dimensionIDVenus", -31, Integer.MIN_VALUE, Integer.MAX_VALUE).get();
//
//        dimensionIDAsteroids = COMMON_BUILDER.comment("Dimension ID for Asteroids")
//                .translation("gc.configgui.dimension_id_asteroids")
//                .defineInRange("dimensionIDAsteroids", -30, Integer.MIN_VALUE, Integer.MAX_VALUE).get();
//
//        dimensionIDMars = COMMON_BUILDER.comment("Dimension ID for Mars")
//                .translation("gc.configgui.dimension_id_mars")
//                .defineInRange("dimensionIDMars", -29, Integer.MIN_VALUE, Integer.MAX_VALUE).get();

        COMMON_BUILDER.pop();

        COMMON_BUILDER.push("SCHEMATIC");

        idSchematicRocketT3 = COMMON_BUILDER.comment("Schematic ID for Tier 3 Rocket, must be unique.")
                .translation("gc.configgui.id_schematic_rocket_t3")
                .defineInRange("idSchematicRocketT3", 4, Integer.MIN_VALUE, Integer.MAX_VALUE).get();

        idSchematicRocketT2 = COMMON_BUILDER.comment("Schematic ID for Tier 2 Rocket, must be unique.")
                .translation("gc.configgui.id_schematic_rocket_t2")
                .defineInRange("idSchematicRocketT2", 2, Integer.MIN_VALUE, Integer.MAX_VALUE).get();

        idSchematicCargoRocket = COMMON_BUILDER.comment("Schematic ID for Cargo Rocket, must be unique.")
                .translation("gc.configgui.id_schematic_cargo_rocket")
                .defineInRange("idSchematicCargoRocket", 3, Integer.MIN_VALUE, Integer.MAX_VALUE).get();

        COMMON_BUILDER.pop();

        COMMON_BUILDER.push("GENERAL");

        disableAmbientLightning = COMMON_BUILDER.comment("Disables background thunder and lightning.")
                .translation("gc.configgui.disable_ambient_lightning")
                .define("disableAmbientLightning", false).get();

        disableGalacticraftHelium = COMMON_BUILDER.comment("Option to disable Helium gas in Galacticraft (because it will be registered by another mod eg GregTech).")
                .translation("gc.configgui.disable_galacticraft_helium")
                .define("disableGalacticraftHelium", false).get();

        astroMinerMax = COMMON_BUILDER.comment("Maximum number of Astro Miners each player is allowed to have active (default 6).")
                .translation("gc.configgui.astro_miners_max")
                .defineInRange("maximumAstroMiners", 6, Integer.MIN_VALUE, Integer.MAX_VALUE).get();

        disableSmallAsteroids = COMMON_BUILDER.comment("Option to disable small asteroids from spawning in the Asteroids Dimension.")
                .translation("gc.configgui.disable_small_asteroids")
                .define("disableSmallAsteroids", false).get();

        launchControllerChunkLoad = COMMON_BUILDER.comment("Whether or not the launch controller acts as a chunk loader. Will cause issues if disabled!")
                .translation("gc.configgui.launch_controller_chunk_load")
                .define("launchControllerChunkLoad", true).get();

        launchControllerAllDims = COMMON_BUILDER.comment("May rarely cause issues if enabled, depends on how the other mod's dimensions are.")
                .translation("gc.configgui.launch_controller_all_dims")
                .define("launchControllerAllDims", false).get();

        COMMON_BUILDER.pop();
    }
}
