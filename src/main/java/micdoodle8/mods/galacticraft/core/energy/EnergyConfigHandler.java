package micdoodle8.mods.galacticraft.core.energy;

import cpw.mods.fml.common.Loader;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
     * Ratio of Build craft(MJ) energy to Galacticraft energy(gJ).
     * Multiply BC3 energy by this to convert to gJ.
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
     * Ratio of RF energy to Galacticraft energy(gJ).
     * Multiply RF energy by this to convert to gJ.
     */
    public static float RF_RATIO = EnergyConfigHandler.BC3_RATIO / 10F;

    /**
     * Ratio of IC2 energy (EU) to Galacticraft energy(gJ).
     * Multiply IC2 power by this to convert to gJ.
     */
    public static float IC2_RATIO = EnergyConfigHandler.BC3_RATIO / 2.44F;

    public static float MEKANISM_RATIO = EnergyConfigHandler.IC2_RATIO / 10F;
    
    private static int conversionLossFactor = 100; 

    /**
     * Convert gJ back to Buildcraft MJ
     */
    public static float TO_BC_RATIO = 1 / EnergyConfigHandler.BC3_RATIO;

    /**
     * Convert gJ back to RF
     */
    public static float TO_RF_RATIO = 1 / EnergyConfigHandler.RF_RATIO;

    /**
     * Convert gJ back to IC2 EU
     */
    public static float TO_IC2_RATIO = 1 / EnergyConfigHandler.IC2_RATIO;

    public static float TO_MEKANISM_RATIO = 1 / EnergyConfigHandler.MEKANISM_RATIO;

    public static float TO_BC_RATIOdisp = 1 / EnergyConfigHandler.BC3_RATIO;
    public static float TO_RF_RATIOdisp = 1 / EnergyConfigHandler.RF_RATIO;
    public static float TO_IC2_RATIOdisp = 1 / EnergyConfigHandler.IC2_RATIO;
    public static float TO_MEKANISM_RATIOdisp = 1 / EnergyConfigHandler.MEKANISM_RATIO;

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
    private static boolean cachedRFLoaded = false;
    private static boolean cachedRFLoadedValue = false;
    private static boolean cachedRF1LoadedValue = false;
    private static boolean cachedRF2LoadedValue = false;
    
    private static boolean disableMJinterface = false;


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
        EnergyConfigHandler.RF_RATIO = (float) EnergyConfigHandler.config.get("Compatibility", "RF Conversion Ratio", EnergyConfigHandler.RF_RATIO).getDouble(EnergyConfigHandler.RF_RATIO);
        EnergyConfigHandler.BC3_RATIO = (float) EnergyConfigHandler.config.get("Compatibility", "BuildCraft Conversion Ratio", EnergyConfigHandler.BC3_RATIO).getDouble(EnergyConfigHandler.BC3_RATIO);
        EnergyConfigHandler.MEKANISM_RATIO = (float) EnergyConfigHandler.config.get("Compatibility", "Mekanism Conversion Ratio", EnergyConfigHandler.MEKANISM_RATIO).getDouble(EnergyConfigHandler.MEKANISM_RATIO);
        EnergyConfigHandler.conversionLossFactor = EnergyConfigHandler.config.get("Compatibility", "Loss factor when converting energy as a percentage (100 = no loss, 90 = 10% loss ...)", 100).getInt(100);
        if (EnergyConfigHandler.conversionLossFactor > 100) EnergyConfigHandler.conversionLossFactor = 100;
        if (EnergyConfigHandler.conversionLossFactor < 5) EnergyConfigHandler.conversionLossFactor = 5;

        updateRatios();
        
        EnergyConfigHandler.displayEnergyUnitsBC = EnergyConfigHandler.config.get("Display", "If BuildCraft is loaded, show Galacticraft machines energy as MJ instead of gJ?", false).getBoolean(false);
        EnergyConfigHandler.displayEnergyUnitsIC2 = EnergyConfigHandler.config.get("Display", "If IndustrialCraft2 is loaded, show Galacticraft machines energy as EU instead of gJ?", false).getBoolean(false);
        EnergyConfigHandler.displayEnergyUnitsMek = EnergyConfigHandler.config.get("Display", "If Mekanism is loaded, show Galacticraft machines energy as Joules (J) instead of gJ?", false).getBoolean(false);
        EnergyConfigHandler.displayEnergyUnitsRF = EnergyConfigHandler.config.get("Display", "Show Galacticraft machines energy in RF instead of gJ?", false).getBoolean(false);

        EnergyConfigHandler.disableMJinterface = EnergyConfigHandler.config.get("Compatibility", "Disable old Buildcraft API (MJ) interfacing completely?", false).getBoolean(false);

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
            cachedBCLoadedValue = false;

            if (disableMJinterface)
            	return false;
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
            	
            	if (count < 6) return false;
            	
            	try {
            		Class clazz = Class.forName("buildcraft.api.core.JavaTools");
            		Method methodz = clazz.getMethod("getAllFields", Class.class);
            		if (methodz != null && methodz.getReturnType() == List.class)
            		{
            			cachedBCLoadedValue = true;
            			return true;           		
            		}
            	} catch (Exception e) { }
            	
            	GCLog.severe("Other mods with two different versions of Buildcraft API detected.  Galacticraft cannot use MJ until this is fixed.  You may have more serious problems with other mods.  More info at: http://wiki.micdoodle8.com/wiki/Compatibility.");
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

    public static boolean isRFAPILoaded()
    {
        if (!cachedRFLoaded)
        {
        	initialiseRF();
        }

        return cachedRFLoadedValue;
    }
    
    public static boolean isRFAPIv1Loaded()
    {
        if (!cachedRFLoaded)
        {
        	initialiseRF();
        }

        return cachedRF1LoadedValue;
    }

    public static boolean isRFAPIv2Loaded()
    {
        if (!cachedRFLoaded)
        {
        	initialiseRF();
        }

        return cachedRF2LoadedValue;
    }
    
    private static void initialiseRF()
    {
        cachedRFLoaded = true;
        cachedRFLoadedValue = false;
        cachedRF2LoadedValue = false;
    	int count = 0;
    	int count2 = 0;
        try {
        	if (Class.forName("cofh.api.energy.IEnergyConnection") != null) count++;
        	if (Class.forName("cofh.api.energy.IEnergyHandler") != null) count+=2;
        } catch (Exception e) { }
        try {
        	if (Class.forName("cofh.api.energy.IEnergyProvider") != null) count2++;
        } catch (Exception e) { }
        try {
        	if (Class.forName("cofh.api.energy.IEnergyReceiver") != null) count2++;
        } catch (Exception e) { }
        
        if ((count + count2 == 3 && count2 != 1) || count + count2 == 5)
        {
        	cachedRFLoadedValue = true;
        	cachedRF1LoadedValue = (count == 3);
        	cachedRF2LoadedValue = (count2 == 2);
        }
        else if (count > 0 || count2 > 0)
        	GCLog.severe("Incomplete Redstone Flux API detected: Galacticraft will not support RF energy connections until this is fixed.");
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
    
    private static void updateRatios()
    {
    	//Sense checks to avoid crazy large inverse ratios or ratios
    	if (EnergyConfigHandler.IC2_RATIO < 0.01F) EnergyConfigHandler.IC2_RATIO = 0.01F; 
    	if (EnergyConfigHandler.RF_RATIO < 0.001F) EnergyConfigHandler.RF_RATIO = 0.001F; 
    	if (EnergyConfigHandler.BC3_RATIO < 0.01F) EnergyConfigHandler.BC3_RATIO = 0.01F; 
    	if (EnergyConfigHandler.MEKANISM_RATIO < 0.001F) EnergyConfigHandler.MEKANISM_RATIO = 0.001F;
    	if (EnergyConfigHandler.IC2_RATIO > 1000F) EnergyConfigHandler.IC2_RATIO = 1000F; 
    	if (EnergyConfigHandler.RF_RATIO > 100F) EnergyConfigHandler.RF_RATIO = 100F; 
    	if (EnergyConfigHandler.BC3_RATIO > 1000F) EnergyConfigHandler.BC3_RATIO = 1000F; 
    	if (EnergyConfigHandler.MEKANISM_RATIO > 100F) EnergyConfigHandler.MEKANISM_RATIO = 100F;

    	float factor = conversionLossFactor / 100;
        TO_BC_RATIO = factor / EnergyConfigHandler.BC3_RATIO;
        TO_RF_RATIO = factor / EnergyConfigHandler.RF_RATIO;
        TO_IC2_RATIO = factor / EnergyConfigHandler.IC2_RATIO;
        TO_MEKANISM_RATIO = factor / EnergyConfigHandler.MEKANISM_RATIO;
        TO_BC_RATIOdisp = 1 / EnergyConfigHandler.BC3_RATIO;
        TO_RF_RATIOdisp = 1 / EnergyConfigHandler.RF_RATIO;
        TO_IC2_RATIOdisp = 1 / EnergyConfigHandler.IC2_RATIO;
        TO_MEKANISM_RATIOdisp = 1 / EnergyConfigHandler.MEKANISM_RATIO;
        EnergyConfigHandler.BC3_RATIO *= factor;
        EnergyConfigHandler.RF_RATIO *= factor;
        EnergyConfigHandler.IC2_RATIO *= factor;
        EnergyConfigHandler.MEKANISM_RATIO *= factor;
    }

	public static void serverConfigOverride(ArrayList<Object> returnList)
	{
		returnList.add(EnergyConfigHandler.BC3_RATIO);
		returnList.add(EnergyConfigHandler.RF_RATIO);
		returnList.add(EnergyConfigHandler.IC2_RATIO);
		returnList.add(EnergyConfigHandler.MEKANISM_RATIO);
		returnList.add(EnergyConfigHandler.conversionLossFactor);
	}

	public static void setConfigOverride(float sBC3, float sRF, float sIC2, float sMEK, int sLossRatio)
	{
        EnergyConfigHandler.BC3_RATIO = sBC3;
        EnergyConfigHandler.RF_RATIO = sRF;
        EnergyConfigHandler.IC2_RATIO = sIC2;
        EnergyConfigHandler.MEKANISM_RATIO = sMEK;
        EnergyConfigHandler.conversionLossFactor = sLossRatio;
        updateRatios();
	}
}
