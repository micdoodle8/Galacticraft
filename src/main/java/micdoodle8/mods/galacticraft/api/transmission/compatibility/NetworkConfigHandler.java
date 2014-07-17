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
	public static float BC3_RATIO = 1.95F;

	/**
	 * Ratio of RF energy to Galacticraft energy(kJ).
	 * Multiply TE energy by this to convert to kJ.
	 */
	public static float TE_RATIO = NetworkConfigHandler.BC3_RATIO / 10F;

	/**
	 * Ratio of IC2 energy (EU) to Galacticraft energy(kJ).
	 * Multiply IC2 power by this to convert to kJ.
	 */
	public static float IC2_RATIO = 0.8f;

	public static float MEKANISM_RATIO = 0.008F;

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
	
	private static boolean cachedIC2Loaded = false;
	private static boolean cachedIC2LoadedValue = false;
	private static boolean cachedBCLoaded = false;
	private static boolean cachedBCLoadedValue = false;

	/** You must call this function to enable the Universal Network module. */
	public static void setDefaultValues(File file)
	{
		if (NetworkConfigHandler.config == null)
		{
			NetworkConfigHandler.config = new Configuration(file);
		}

		NetworkConfigHandler.config.load();
		NetworkConfigHandler.IC2_RATIO = (float) NetworkConfigHandler.config.get("Compatiblity", "IndustrialCraft Conversion Ratio", NetworkConfigHandler.IC2_RATIO).getDouble(NetworkConfigHandler.IC2_RATIO);
		NetworkConfigHandler.TE_RATIO = (float) NetworkConfigHandler.config.get("Compatiblity", "Thermal Expansion Conversion Ratio", NetworkConfigHandler.TE_RATIO).getDouble(NetworkConfigHandler.TE_RATIO);
		NetworkConfigHandler.BC3_RATIO = (float) NetworkConfigHandler.config.get("Compatiblity", "BuildCraft Conversion Ratio", NetworkConfigHandler.BC3_RATIO).getDouble(NetworkConfigHandler.BC3_RATIO);
		NetworkConfigHandler.MEKANISM_RATIO = (float) NetworkConfigHandler.config.get("Compatiblity", "Mekanism Conversion Ratio", NetworkConfigHandler.MEKANISM_RATIO).getDouble(NetworkConfigHandler.MEKANISM_RATIO);
		NetworkConfigHandler.TO_IC2_RATIO = 1 / NetworkConfigHandler.IC2_RATIO;
		NetworkConfigHandler.TO_BC_RATIO = 1 / NetworkConfigHandler.BC3_RATIO;
		NetworkConfigHandler.TO_TE_RATIO = 1 / NetworkConfigHandler.TE_RATIO;
		NetworkConfigHandler.TO_MEKANISM_RATIO = 1 / NetworkConfigHandler.MEKANISM_RATIO;
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
