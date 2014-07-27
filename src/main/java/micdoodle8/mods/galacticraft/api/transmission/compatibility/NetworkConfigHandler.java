package micdoodle8.mods.galacticraft.api.transmission.compatibility;

import cpw.mods.fml.common.Loader;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * The Universal Electricity compatibility module allows your mod to be
 * compatible with most major power systems in Minecraft.
 * 
 * @author Calclavia, Micdoodle
 */
public class NetworkConfigHandler
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
	public static float TE_RATIO = NetworkConfigHandler.BC3_RATIO / 10F;

	/**
	 * Ratio of IC2 energy (EU) to Galacticraft energy(kJ).
	 * Multiply IC2 power by this to convert to kJ.
	 */
	public static float IC2_RATIO = NetworkConfigHandler.BC3_RATIO / 2.44F;

	public static float MEKANISM_RATIO = NetworkConfigHandler.IC2_RATIO / 100F;

	/**
	 * Convert kJ back to Buildcraft MJ
	 */
	public static float TO_BC_RATIO = 1 / NetworkConfigHandler.BC3_RATIO;

	/**
	 * Convert kJ back to TE3 RF
	 */
	public static float TO_TE_RATIO = 1 / NetworkConfigHandler.TE_RATIO;

	/**
	 * Convert kJ back to IC2 EU
	 */
	public static float TO_IC2_RATIO = 1 / NetworkConfigHandler.IC2_RATIO;

	public static float TO_MEKANISM_RATIO = 1 / NetworkConfigHandler.MEKANISM_RATIO;
	
	/**
	 * Oxygen gas used when Mekanism is loaded. Always null otherwise.
	 */
	public static Object gasOxygen = null;
	
	public static boolean displayEnergyUnitsBC = false;
	public static boolean displayEnergyUnitsIC2 = false;
	
	private static boolean cachedIC2Loaded = false;
	private static boolean cachedIC2LoadedValue = false;
	private static boolean cachedBCLoaded = false;
	private static boolean cachedBCLoadedValue = false;
	private static int cachedBCVersion = -1;


	/** You must call this function to enable the Universal Network module. */
	public static void setDefaultValues(File file)
	{
		if (NetworkConfigHandler.config == null)
		{
			NetworkConfigHandler.config = new Configuration(file);
		}

		NetworkConfigHandler.config.load();
		NetworkConfigHandler.IC2_RATIO = (float) NetworkConfigHandler.config.get("Compatibility", "IndustrialCraft Conversion Ratio", NetworkConfigHandler.IC2_RATIO).getDouble(NetworkConfigHandler.IC2_RATIO);
		//NetworkConfigHandler.TE_RATIO = (float) NetworkConfigHandler.config.get("Compatibility", "Thermal Expansion Conversion Ratio", NetworkConfigHandler.TE_RATIO).getDouble(NetworkConfigHandler.TE_RATIO);
		NetworkConfigHandler.BC3_RATIO = (float) NetworkConfigHandler.config.get("Compatibility", "BuildCraft Conversion Ratio", NetworkConfigHandler.BC3_RATIO).getDouble(NetworkConfigHandler.BC3_RATIO);
		//NetworkConfigHandler.MEKANISM_RATIO = (float) NetworkConfigHandler.config.get("Compatibility", "Mekanism Conversion Ratio", NetworkConfigHandler.MEKANISM_RATIO).getDouble(NetworkConfigHandler.MEKANISM_RATIO);
		NetworkConfigHandler.TO_IC2_RATIO = 1 / NetworkConfigHandler.IC2_RATIO;
		NetworkConfigHandler.TO_BC_RATIO = 1 / NetworkConfigHandler.BC3_RATIO;
		NetworkConfigHandler.TO_TE_RATIO = 1 / NetworkConfigHandler.TE_RATIO;
		NetworkConfigHandler.TO_MEKANISM_RATIO = 1 / NetworkConfigHandler.MEKANISM_RATIO;

		NetworkConfigHandler.displayEnergyUnitsBC = NetworkConfigHandler.config.get("Display", "If BuildCraft is loaded, show Galacticraft machines energy as MJ instead of gJ?", false).getBoolean(false);
		NetworkConfigHandler.displayEnergyUnitsIC2 = NetworkConfigHandler.config.get("Display", "If IndustrialCraft2 is loaded, show Galacticraft machines energy as EU instead of gJ?", false).getBoolean(false);
		if (!NetworkConfigHandler.isBuildcraftLoaded()) NetworkConfigHandler.displayEnergyUnitsBC = false;
		if (!NetworkConfigHandler.isIndustrialCraft2Loaded()) NetworkConfigHandler.displayEnergyUnitsIC2 = false;
		if (NetworkConfigHandler.isIndustrialCraft2Loaded()) NetworkConfigHandler.displayEnergyUnitsBC = false;
		
		NetworkConfigHandler.config.save();
	}

	public static void initGas()
	{
		//		if (NetworkConfigHandler.isMekanismLoaded())
		//		{
		//			Gas oxygen = GasRegistry.getGas("oxygen");
		//
		//			if (oxygen == null)
		//			{
		//				NetworkConfigHandler.gasOxygen = GasRegistry.register(new Gas("oxygen")).registerFluid();
		//			}
		//			else
		//			{
		//				NetworkConfigHandler.gasOxygen = oxygen;
		//			}
		//		} TODO
	}

	/** Checks using the FML loader too see if IC2 is loaded */
	public static boolean isIndustrialCraft2Loaded()
	{
		if (!cachedIC2Loaded)
		{
			cachedIC2Loaded = true;
			cachedIC2LoadedValue = Loader.isModLoaded("IC2");
		}
	
		return cachedIC2LoadedValue;		
	}

	/** Checks using the FML loader too see if BC3 is loaded */
	public static boolean isBuildcraftLoaded()
	{
		if (!cachedBCLoaded)
		{
			cachedBCLoaded = true;
			cachedBCLoadedValue = Loader.isModLoaded("BuildCraft|Energy");
		}
	
		return cachedBCLoadedValue;
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
		return Loader.isModLoaded("Mekanism");
	}

	private static Boolean mekanismOldClassFound;

	public static boolean isMekanismV6Loaded()
	{
		if (!NetworkConfigHandler.isMekanismLoaded())
		{
			return false;
		}

		if (NetworkConfigHandler.mekanismOldClassFound != null)
		{
			return !NetworkConfigHandler.mekanismOldClassFound;
		}

		try
		{
			Class.forName("mekanism.api.gas.IGasAcceptor");
		}
		catch (ClassNotFoundException e)
		{
			NetworkConfigHandler.mekanismOldClassFound = false;
			return true;
		}

		NetworkConfigHandler.mekanismOldClassFound = true;
		return false;
	}
}
