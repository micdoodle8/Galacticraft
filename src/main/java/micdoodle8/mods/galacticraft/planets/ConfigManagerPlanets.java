package micdoodle8.mods.galacticraft.planets;

import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigManagerPlanets
{
    public static final ConfigManagerPlanets INSTANCE;
    public static final ForgeConfigSpec COMMON_SPEC;
    static
    {
        final Pair<ConfigManagerPlanets, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ConfigManagerPlanets::new);
        COMMON_SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }
    private final ForgeConfigSpec.Builder COMMON_BUILDER;

//    public static int dimensionIDMars;
//    public static int dimensionIDAsteroids;
//    public static int dimensionIDVenus;

    public static IntValue idSchematicRocketT2;
    public static IntValue idSchematicCargoRocket;
    public static IntValue idSchematicRocketT3;

    public static BooleanValue launchControllerChunkLoad;
    public static BooleanValue launchControllerAllDims;
    public static BooleanValue disableGalacticraftHelium;
    public static BooleanValue disableSmallAsteroids;
    public static IntValue astroMinerMax;
    public static BooleanValue disableAmbientLightning;

    public static BooleanValue disableDeshGenMars;
    public static BooleanValue disableTinGenMars;
    public static BooleanValue disableCopperGenMars;
    public static BooleanValue disableIronGenMars;
    public static BooleanValue disableIlmeniteGenAsteroids;
    public static BooleanValue disableIronGenAsteroids;
    public static BooleanValue disableAluminumGenAsteroids;
    public static BooleanValue disableAluminumGenVenus;
    public static BooleanValue disableCopperGenVenus;
    public static BooleanValue disableGalenaGenVenus;
    public static BooleanValue disableQuartzGenVenus;
    public static BooleanValue disableSiliconGenVenus;
    public static BooleanValue disableTinGenVenus;
    public static BooleanValue disableSolarGenVenus;

    public ConfigManagerPlanets(ForgeConfigSpec.Builder builder)
    {
        COMMON_BUILDER = builder;

        builder.push("WORLDGEN");

        disableAluminumGenVenus = builder.comment("Disable Aluminum Ore Gen on Venus.")
                .translation("gc.configgui.disable_venus_aluminum_gen")
                .define("disable_aluminum_ore_gen_on_venus", false);

        disableCopperGenVenus = builder.comment("Disable Copper Ore Gen on Venus.")
                .translation("gc.configgui.disable_venus_copper_gen")
                .define("disable_copper_ore_gen_on_venus", false);

        disableGalenaGenVenus = builder.comment("Disable Galena Ore Gen on Venus.")
                .translation("gc.configgui.disable_venus_galena_gen")
                .define("disable_galena_ore_gen_on_venus", false);

        disableQuartzGenVenus = builder.comment("Disable Quartz Ore Gen on Venus.")
                .translation("gc.configgui.disable_venus_quartz_gen")
                .define("disable_quartz_ore_gen_on_venus", false);

        disableSiliconGenVenus = builder.comment("Disable Silicon Ore Gen on Venus.")
                .translation("gc.configgui.disable_venus_silicon_gen")
                .define("disable_silicon_ore_gen_on_venus", false);

        disableTinGenVenus = builder.comment("Disable Tin Ore Gen on Venus.")
                .translation("gc.configgui.disable_venus_tin_gen")
                .define("disable_tin_ore_gen_on_venus", false);

        disableSolarGenVenus = builder.comment("Disable Solar Ore Gen on Venus.")
                .translation("gc.configgui.disable_venus_solar_gen")
                .define("disable_solar_ore_gen_on_venus", false);

        disableIronGenAsteroids = builder.comment("Disable Iron Ore Gen on Asteroids.")
                .translation("gc.configgui.disable_iron_gen_asteroids")
                .define("disable_iron_ore_gen_on_asteroids", false);

        disableAluminumGenAsteroids = builder.comment("Disable Aluminum Ore Gen on Asteroids.")
                .translation("gc.configgui.disable_aluminum_gen_asteroids")
                .define("disable_aluminum_ore_gen_on_asteroids", false);

        disableIlmeniteGenAsteroids = builder.comment("Disable Ilmenite Ore Gen on Asteroids.")
                .translation("gc.configgui.disable_ilmenite_gen_asteroids")
                .define("disable_ilmenite_ore_gen_on_asteroids", false);

        disableIronGenMars = builder.comment("Disable Iron Ore Gen on Mars.")
                .translation("gc.configgui.disable_iron_gen_mars")
                .define("disable_iron_ore_gen_on_mars", false);

        disableCopperGenMars = builder.comment("Disable Copper Ore Gen on Mars.")
                .translation("gc.configgui.disable_copper_gen_mars")
                .define("disable_copper_ore_gen_on_mars", false);

        disableTinGenMars = builder.comment("Disable Tin Ore Gen on Mars.")
                .translation("gc.configgui.disable_tin_gen_mars")
                .define("disable_tin_ore_gen_on_mars", false);

        disableDeshGenMars = builder.comment("Disable Desh Ore Gen on Mars.")
                .translation("gc.configgui.disable_desh_gen_mars")
                .define("disable_desh_ore_gen_on_mars", false);

        builder.pop();

        builder.push("DIMENSIONS");

//        dimensionIDVenus = builder.comment("Dimension ID for Venus")
//                .translation("gc.configgui.dimension_id_venus")
//                .defineInRange("dimensionIDVenus", -31, Integer.MIN_VALUE, Integer.MAX_VALUE);
//
//        dimensionIDAsteroids = builder.comment("Dimension ID for Asteroids")
//                .translation("gc.configgui.dimension_id_asteroids")
//                .defineInRange("dimensionIDAsteroids", -30, Integer.MIN_VALUE, Integer.MAX_VALUE);
//
//        dimensionIDMars = builder.comment("Dimension ID for Mars")
//                .translation("gc.configgui.dimension_id_mars")
//                .defineInRange("dimensionIDMars", -29, Integer.MIN_VALUE, Integer.MAX_VALUE);

        builder.pop();

        builder.push("SCHEMATIC");

        idSchematicRocketT3 = builder.comment("Schematic ID for Tier 3 Rocket, must be unique.")
                .translation("gc.configgui.id_schematic_rocket_t3")
                .defineInRange("idSchematicRocketT3", 4, Integer.MIN_VALUE, Integer.MAX_VALUE);

        idSchematicRocketT2 = builder.comment("Schematic ID for Tier 2 Rocket, must be unique.")
                .translation("gc.configgui.id_schematic_rocket_t2")
                .defineInRange("idSchematicRocketT2", 2, Integer.MIN_VALUE, Integer.MAX_VALUE);

        idSchematicCargoRocket = builder.comment("Schematic ID for Cargo Rocket, must be unique.")
                .translation("gc.configgui.id_schematic_cargo_rocket")
                .defineInRange("idSchematicCargoRocket", 3, Integer.MIN_VALUE, Integer.MAX_VALUE);

        builder.pop();

        builder.push("GENERAL");

        disableAmbientLightning = builder.comment("Disables background thunder and lightning.")
                .translation("gc.configgui.disable_ambient_lightning")
                .define("disableAmbientLightning", false);

        disableGalacticraftHelium = builder.comment("Option to disable Helium gas in Galacticraft (because it will be registered by another mod eg GregTech).")
                .translation("gc.configgui.disable_galacticraft_helium")
                .define("disableGalacticraftHelium", false);

        astroMinerMax = builder.comment("Maximum number of Astro Miners each player is allowed to have active (default 6).")
                .translation("gc.configgui.astro_miners_max")
                .defineInRange("maximumAstroMiners", 6, Integer.MIN_VALUE, Integer.MAX_VALUE);

        disableSmallAsteroids = builder.comment("Option to disable small asteroids from spawning in the Asteroids Dimension.")
                .translation("gc.configgui.disable_small_asteroids")
                .define("disableSmallAsteroids", false);

        launchControllerChunkLoad = builder.comment("Whether or not the launch controller acts as a chunk loader. Will cause issues if disabled!")
                .translation("gc.configgui.launch_controller_chunk_load")
                .define("launchControllerChunkLoad", true);

        launchControllerAllDims = builder.comment("May rarely cause issues if enabled, depends on how the other mod's dimensions are.")
                .translation("gc.configgui.launch_controller_all_dims")
                .define("launchControllerAllDims", false);

        builder.pop();
    }
}
