package micdoodle8.mods.galacticraft.core.energy;

import cpw.mods.fml.common.Loader;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * The Universal Electricity compatibility module allows your mod to be
 * compatible with most major power systems in Minecraft.
 *
 * @author Calclavia, Micdoodle
 */
public class EnergyConfigHandler
{
    private static Configuration config;

    /**
     * Ratio of Build craft(MJ) energy to Galacticraft energy(kJ).
     * Multiply BC3 energy by this to convert to kJ.
     */
    public static float BC3_RATIO = 16F;

    //Note on energy equivalence:
    //
    //In BuildCraft, 1 lump of coal produces 1600 MJ (Minecraft Joules)
    //in a Stirling Engine.  This is by design: coal has 1600 ticks burn time
    //in a vanilla Furnace, which corresponds with 1600 MJ in BuildCraft.
    //
    //In Galacticraft, 1 lump of coal produces 38,400 gJ
    //in a coal generator operating at 100% hull heat (less efficient at lower hull heats).

    //If 1600 MJ = 38,400 gJ then strictly 1 MJ = 24 gJ.
    //But, that feels imbalanced - for example redstone engines make too much gJ if the ratio is 24.
    //So, the BC conversion ratio is set at 16.
    //Think of it as the Galacticraft coal generator at full heat turning coal into
    //electrical energy 50% more efficiently than BuildCraft's Stirling Engine can.

    /**
     * Ratio of RF energy to Galacticraft energy(kJ).
     * Multiply TE energy by this to convert to kJ.
     */
    public static float TE_RATIO = EnergyConfigHandler.BC3_RATIO / 10F;

    /**
     * Ratio of IC2 energy (EU) to Galacticraft energy(kJ).
     * Multiply IC2 power by this to convert to kJ.
     */
    public static float IC2_RATIO = EnergyConfigHandler.BC3_RATIO / 2.44F;

    public static float MEKANISM_RATIO = EnergyConfigHandler.IC2_RATIO / 10F;

    /**
     * Convert kJ back to Buildcraft MJ
     */
    public static float TO_BC_RATIO = 1 / EnergyConfigHandler.BC3_RATIO;

    /**
     * Convert kJ back to TE3 RF
     */
    public static float TO_TE_RATIO = 1 / EnergyConfigHandler.TE_RATIO;

    /**
     * Convert kJ back to IC2 EU
     */
    public static float TO_IC2_RATIO = 1 / EnergyConfigHandler.IC2_RATIO;

    public static float TO_MEKANISM_RATIO = 1 / EnergyConfigHandler.MEKANISM_RATIO;

    /**
     * Oxygen gas used when Mekanism is loaded. Always null otherwise.
     */
    public static Object gasOxygen = null;
    public static Object gasHydrogen = null;
    
    public static boolean displayEnergyUnitsBC = false;
    public static boolean displayEnergyUnitsIC2 = false;
    public static boolean displayEnergyUnitsMek = false;
    public static boolean displayEnergyUnitsRF = false;

    private static boolean cachedIC2Loaded = false;
    private static boolean cachedIC2LoadedValue = false;
    private static boolean cachedBCLoaded = false;
    private static boolean cachedBCLoadedValue = false;
    private static boolean cachedBCReallyLoaded = false;
    private static boolean cachedBCReallyLoadedValue = false;
    private static int cachedBCVersion = -1;
    private static boolean cachedMekLoaded = false;
    private static boolean cachedMekLoadedValue = false;


    /**
     * You must call this function to enable the Universal Network module.
     */
    public static void setDefaultValues(File file)
    {
        if (EnergyConfigHandler.config == null)
        {
            EnergyConfigHandler.config = new Configuration(file);
        }

        EnergyConfigHandler.config.load();
        EnergyConfigHandler.IC2_RATIO = (float) EnergyConfigHandler.config.get("Compatibility", "IndustrialCraft Conversion Ratio", EnergyConfigHandler.IC2_RATIO).getDouble(EnergyConfigHandler.IC2_RATIO);
        //EnergyConfigHandler.TE_RATIO = (float) EnergyConfigHandler.config.get("Compatibility", "Thermal Expansion Conversion Ratio", EnergyConfigHandler.TE_RATIO).getDouble(EnergyConfigHandler.TE_RATIO);
        EnergyConfigHandler.BC3_RATIO = (float) EnergyConfigHandler.config.get("Compatibility", "BuildCraft Conversion Ratio", EnergyConfigHandler.BC3_RATIO).getDouble(EnergyConfigHandler.BC3_RATIO);
        EnergyConfigHandler.MEKANISM_RATIO = (float) EnergyConfigHandler.config.get("Compatibility", "Mekanism Conversion Ratio", EnergyConfigHandler.MEKANISM_RATIO).getDouble(EnergyConfigHandler.MEKANISM_RATIO);
        EnergyConfigHandler.TO_IC2_RATIO = 1 / EnergyConfigHandler.IC2_RATIO;
        EnergyConfigHandler.TO_BC_RATIO = 1 / EnergyConfigHandler.BC3_RATIO;
        EnergyConfigHandler.TO_TE_RATIO = 1 / EnergyConfigHandler.TE_RATIO;
        EnergyConfigHandler.TO_MEKANISM_RATIO = 1 / EnergyConfigHandler.MEKANISM_RATIO;

        EnergyConfigHandler.displayEnergyUnitsBC = EnergyConfigHandler.config.get("Display", "If BuildCraft is loaded, show Galacticraft machines energy as MJ instead of gJ?", false).getBoolean(false);
        EnergyConfigHandler.displayEnergyUnitsIC2 = EnergyConfigHandler.config.get("Display", "If IndustrialCraft2 is loaded, show Galacticraft machines energy as EU instead of gJ?", false).getBoolean(false);
        EnergyConfigHandler.displayEnergyUnitsMek = EnergyConfigHandler.config.get("Display", "If Mekanism is loaded, show Galacticraft machines energy as Joules (J) instead of gJ?", false).getBoolean(false);
        EnergyConfigHandler.displayEnergyUnitsRF = EnergyConfigHandler.config.get("Display", "Show Galacticraft machines energy in RF instead of gJ?", false).getBoolean(false);
        if (!EnergyConfigHandler.isBuildcraftLoaded())
        {
            EnergyConfigHandler.displayEnergyUnitsBC = false;
        }
        if (!EnergyConfigHandler.isIndustrialCraft2Loaded())
        {
            EnergyConfigHandler.displayEnergyUnitsIC2 = false;
        }
        if (!EnergyConfigHandler.isMekanismLoaded())
        {
            EnergyConfigHandler.displayEnergyUnitsMek = false;
        }
        if (EnergyConfigHandler.displayEnergyUnitsIC2)
        {
            EnergyConfigHandler.displayEnergyUnitsBC = false;
        }
        if (EnergyConfigHandler.displayEnergyUnitsMek)
        {
            EnergyConfigHandler.displayEnergyUnitsBC = false;
            EnergyConfigHandler.displayEnergyUnitsIC2 = false;
        }
        if (EnergyConfigHandler.displayEnergyUnitsRF)
        {
            EnergyConfigHandler.displayEnergyUnitsBC = false;
            EnergyConfigHandler.displayEnergyUnitsIC2 = false;
            EnergyConfigHandler.displayEnergyUnitsMek = false;
        }

        EnergyConfigHandler.config.save();
    }

    public static void initGas()
    {
        if (EnergyConfigHandler.isMekanismLoaded())
        {
            Gas oxygen = GasRegistry.getGas("oxygen");

            if (oxygen == null)
            {
                EnergyConfigHandler.gasOxygen = GasRegistry.register(new Gas("oxygen")).registerFluid();
            }
            else
            {
                EnergyConfigHandler.gasOxygen = oxygen;
            }

            Gas hydrogen = GasRegistry.getGas("hydrogen");

            if (hydrogen == null)
            {
                EnergyConfigHandler.gasHydrogen = GasRegistry.register(new Gas("hydrogen")).registerFluid();
            }
            else
            {
                EnergyConfigHandler.gasHydrogen = hydrogen;
            }
        }
    }

    /**
     * Checks using the FML loader too see if IC2 is loaded
     */
    public static boolean isIndustrialCraft2Loaded()
    {
        if (!cachedIC2Loaded)
        {
            cachedIC2Loaded = true;
            cachedIC2LoadedValue = Loader.isModLoaded("IC2");
        }

        return cachedIC2LoadedValue;
    }

    /**
     * Checks using the FML loader to see if BC (or BC API) is loaded
     */
    public static boolean isBuildcraftLoaded()
    {
        if (!cachedBCLoaded)
        {
            cachedBCLoaded = true;
            if (Loader.isModLoaded("BuildCraft|Energy"))
            	cachedBCLoadedValue = true;
            else
            {
            	int count = 0;
            	try {
	            	if (Class.forName("buildcraft.api.mj.MjAPI") != null) count++;           			
	            	if (Class.forName("buildcraft.api.power.IPowerReceptor") != null) count++;
	            	if (Class.forName("buildcraft.api.power.PowerHandler") != null) count++;
	            	if (Class.forName("buildcraft.api.power.IPowerEmitter") != null) count++;
	            	if (Class.forName("buildcraft.api.mj.IBatteryObject") != null) count++;
	            	if (Class.forName("buildcraft.api.mj.ISidedBatteryProvider") != null) count++;
            	} catch (Exception e) { }
            	
            	cachedBCLoadedValue = (count==6) ;
            }
        }

        return cachedBCLoadedValue;
    }

    public static boolean isBuildcraftReallyLoaded()
    {
        if (!cachedBCReallyLoaded)
        {
            cachedBCReallyLoaded = true;
            cachedBCReallyLoadedValue = Loader.isModLoaded("BuildCraft|Energy");
        }

        return cachedBCReallyLoadedValue;
    }

    public static int getBuildcraftVersion()
    {
        if (cachedBCVersion != -1)
        {
            return cachedBCVersion;
        }

        if (cachedBCLoaded)
        {
            boolean bc6Found = true;

            try
            {
                Class.forName("buildcraft.api.mj.MjAPI");
            }
            catch (Throwable t)
            {
                bc6Found = false;
            }

            if (bc6Found)
            {
                cachedBCVersion = 6;
            }
            else
            {
                cachedBCVersion = 5;
            }
        }

        return cachedBCVersion;
    }

    public static boolean isThermalExpansionLoaded()
    {
        return Loader.isModLoaded("ThermalExpansion");
    }

    public static boolean isMekanismLoaded()
    {
        if (!cachedMekLoaded)
        {
            cachedMekLoaded = true;
            cachedMekLoadedValue = Loader.isModLoaded("Mekanism");
        }

        return cachedMekLoadedValue;
    }
}
