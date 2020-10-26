package micdoodle8.mods.galacticraft.core.energy;

import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

import java.util.ArrayList;

/**
 * The universal energy compatibility module allows Galacticraft to be
 * compatible with most other major power systems in Minecraft as well.
 *
 * @author Calclavia, Micdoodle, radfast
 */
public class EnergyConfigHandler
{
    /**
     * Ratio of Build craft(MJ) energy to Galacticraft energy(gJ).
     * Multiply BC3 energy by this to convert to gJ.
     */
    public static DoubleValue BC_RATIO;
    public static float BC8_MICROJOULE_RATIO = 1000000F;
    public static float BC8_INTERNAL_RATIO;

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
    public static DoubleValue RF_RATIO;

    /**
     * Ratio of IC2 energy (EU) to Galacticraft energy(gJ).
     * Multiply IC2 power by this to convert to gJ.
     */
    public static DoubleValue IC2_RATIO;

    public static DoubleValue MEKANISM_RATIO;

    public static IntValue conversionLossFactor;

    /**
     * Convert gJ back to Buildcraft MJ (microJoules)
     */
    public static float TO_BC_RATIO;

    /**
     * Convert gJ back to RF
     */
    public static float TO_RF_RATIO;

    /**
     * Convert gJ back to IC2 EU
     */
    public static float TO_IC2_RATIO;

    public static float TO_MEKANISM_RATIO;

    public static float TO_BC_RATIOdisp;

    public static float TO_RF_RATIOdisp;

    public static float TO_IC2_RATIOdisp;

    public static float TO_MEKANISM_RATIOdisp;

    /**
     * Oxygen gas used when Mekanism is loaded. Always null otherwise.
     */
    public static Object gasOxygen = null;
    public static Object gasHydrogen = null;

    public static BooleanValue displayEnergyUnitsBC;
    public static BooleanValue displayEnergyUnitsIC2;
    public static BooleanValue displayEnergyUnitsMek;
    public static BooleanValue displayEnergyUnitsRF;

    private static final boolean cachedBCLoaded = false;
    private static final boolean cachedBCLoadedValue = false;
    private static final int cachedBCVersion = -1;
    private static final boolean cachedBCRLoaded = false;
    private static final boolean cachedBCRLoadedValue = false;
    private static boolean cachedRFLoaded = false;
    private static boolean cachedRFLoadedValue = false;
    private static boolean cachedRF1LoadedValue = false;
    private static boolean cachedRF2LoadedValue = false;

    public static BooleanValue disableMJinterface;

    public static BooleanValue disableBuildCraftInput;
    public static BooleanValue disableBuildCraftOutput;
    public static BooleanValue disableRFInput;
    public static BooleanValue disableRFOutput;
    public static BooleanValue disableFEInput;
    public static BooleanValue disableFEOutput;
    public static BooleanValue disableIC2Input;
    public static BooleanValue disableIC2Output;
    public static BooleanValue disableMekanismInput;
    public static BooleanValue disableMekanismOutput;

    /**
     * You must call this function to enable the Universal Network module.
     */
//    public static void setDefaultValues(File file)
//    {
//        if (EnergyConfigHandler.config == null)
//        {
//            EnergyConfigHandler.config = new Configuration(file);
//        }
//
//        EnergyConfigHandler.config.load();
//        EnergyConfigHandler.IC2_RATIO = (float) EnergyConfigHandler.config.get("Compatibility", "IndustrialCraft Conversion Ratio", EnergyConfigHandler.IC2_RATIO).getDouble(EnergyConfigHandler.IC2_RATIO);
//        EnergyConfigHandler.RF_RATIO = (float) EnergyConfigHandler.config.get("Compatibility", "RF Conversion Ratio", EnergyConfigHandler.RF_RATIO).getDouble(EnergyConfigHandler.RF_RATIO);
//        EnergyConfigHandler.BC_RATIO = (float) EnergyConfigHandler.config.get("Compatibility", "BuildCraft Conversion Ratio", EnergyConfigHandler.BC_RATIO).getDouble(EnergyConfigHandler.BC_RATIO);
//        EnergyConfigHandler.MEKANISM_RATIO = (float) EnergyConfigHandler.config.get("Compatibility", "Mekanism Conversion Ratio", EnergyConfigHandler.MEKANISM_RATIO).getDouble(EnergyConfigHandler.MEKANISM_RATIO);
//        EnergyConfigHandler.conversionLossFactor = EnergyConfigHandler.config.get("Compatibility", "Loss factor when converting energy as a percentage (100 = no loss, 90 = 10% loss ...)", 100).getInt(100);
//        if (EnergyConfigHandler.conversionLossFactor > 100)
//        {
//            EnergyConfigHandler.conversionLossFactor = 100;
//        }
//        if (EnergyConfigHandler.conversionLossFactor < 5)
//        {
//            EnergyConfigHandler.conversionLossFactor = 5;
//        }
//
//        updateRatios();
//
//        EnergyConfigHandler.displayEnergyUnitsBC = EnergyConfigHandler.config.get("Display", "If BuildCraft is loaded, show Galacticraft machines energy as MJ instead of gJ?", false).getBoolean(false);
//        EnergyConfigHandler.displayEnergyUnitsIC2 = EnergyConfigHandler.config.get("Display", "If IndustrialCraft2 is loaded, show Galacticraft machines energy as EU instead of gJ?", false).getBoolean(false);
//        EnergyConfigHandler.displayEnergyUnitsMek = EnergyConfigHandler.config.get("Display", "If Mekanism is loaded, show Galacticraft machines energy as Joules (J) instead of gJ?", false).getBoolean(false);
//        EnergyConfigHandler.displayEnergyUnitsRF = EnergyConfigHandler.config.get("Display", "Show Galacticraft machines energy in RF instead of gJ?", false).getBoolean(false);
//
//        EnergyConfigHandler.disableMJinterface = EnergyConfigHandler.config.get("Compatibility", "Disable old Buildcraft API (MJ) interfacing completely?", false).getBoolean(false);
//
//        EnergyConfigHandler.disableBuildCraftInput = EnergyConfigHandler.config.get("Compatibility", "Disable INPUT of BuildCraft energy", false).getBoolean(false);
//        EnergyConfigHandler.disableBuildCraftOutput = EnergyConfigHandler.config.get("Compatibility", "Disable OUTPUT of BuildCraft energy", false).getBoolean(false);
//        EnergyConfigHandler.disableRFInput = EnergyConfigHandler.config.get("Compatibility", "Disable INPUT of RF energy", false).getBoolean(false);
//        EnergyConfigHandler.disableRFOutput = EnergyConfigHandler.config.get("Compatibility", "Disable OUTPUT of RF energy", false).getBoolean(false);
//        EnergyConfigHandler.disableFEInput = EnergyConfigHandler.config.get("Compatibility", "Disable INPUT of Forge Energy to GC machines", false).getBoolean(false);
//        EnergyConfigHandler.disableFEOutput = EnergyConfigHandler.config.get("Compatibility", "Disable OUTPUT of Forge Energy from GC machines", false).getBoolean(false);
//        EnergyConfigHandler.disableIC2Input = EnergyConfigHandler.config.get("Compatibility", "Disable INPUT of IC2 energy", false).getBoolean(false);
//        EnergyConfigHandler.disableIC2Output = EnergyConfigHandler.config.get("Compatibility", "Disable OUTPUT of IC2 energy", false).getBoolean(false);
//        EnergyConfigHandler.disableMekanismInput = EnergyConfigHandler.config.get("Compatibility", "Disable INPUT of Mekanism energy", false).getBoolean(false);
//        EnergyConfigHandler.disableMekanismOutput = EnergyConfigHandler.config.get("Compatibility", "Disable OUTPUT of Mekanism energy", false).getBoolean(false);
//
//        if (!EnergyConfigHandler.isIndustrialCraft2Loaded())
//        {
//            EnergyConfigHandler.displayEnergyUnitsIC2 = false;
//        }
//        if (!EnergyConfigHandler.isMekanismLoaded())
//        {
//            EnergyConfigHandler.displayEnergyUnitsMek = false;
//        }
//        if (EnergyConfigHandler.displayEnergyUnitsIC2)
//        {
//            EnergyConfigHandler.displayEnergyUnitsBC = false;
//        }
//        if (EnergyConfigHandler.displayEnergyUnitsMek)
//        {
//            EnergyConfigHandler.displayEnergyUnitsBC = false;
//            EnergyConfigHandler.displayEnergyUnitsIC2 = false;
//        }
//        if (EnergyConfigHandler.displayEnergyUnitsRF)
//        {
//            EnergyConfigHandler.displayEnergyUnitsBC = false;
//            EnergyConfigHandler.displayEnergyUnitsIC2 = false;
//            EnergyConfigHandler.displayEnergyUnitsMek = false;
//        }
//
//        EnergyConfigHandler.config.save();
//    }

//    public static void initGas()
//    {
//        if (EnergyConfigHandler.isMekanismLoaded())
//        {
//            Gas oxygen = GasRegistry.getGas("oxygen");
//
//            if (oxygen == null)
//            {
//                EnergyConfigHandler.gasOxygen = GasRegistry.register(new Gas(GCFluidRegistry.fluidOxygenGas)).registerFluid();
//            }
//            else
//            {
//                EnergyConfigHandler.gasOxygen = oxygen;
//            }
//
//            Gas hydrogen = GasRegistry.getGas("hydrogen");
//
//            if (hydrogen == null)
//            {
//                EnergyConfigHandler.gasHydrogen = GasRegistry.register(new Gas(GCFluidRegistry.fluidHydrogenGas)).registerFluid();
//            }
//            else
//            {
//                EnergyConfigHandler.gasHydrogen = hydrogen;
//            }
//        }
//    } TODO Gas

    /**
     * Checks using the FML loader too see if IC2 is loaded
     */
    public static boolean isIndustrialCraft2Loaded()
    {
        return CompatibilityManager.isIc2Loaded();
    }

    public static boolean isBuildcraftLoaded()
    {
//        if (!cachedBCRLoaded)
//        {
//            boolean mjAPIFound = false;
//            try
//            {
//                Class.forName("buildcraft.api.mj.MjAPI");
//                mjAPIFound = true;
//                BC8_MICROJOULE_RATIO = MjAPI.MJ;
//                BC8_INTERNAL_RATIO = BC_RATIO / BC8_MICROJOULE_RATIO;
//                TO_BC_RATIO = conversionLossFactor / 100F / EnergyConfigHandler.BC_RATIO * BC8_MICROJOULE_RATIO;
//            }
//            catch (Throwable ignore) {}
//            cachedBCRLoaded = true;
//            cachedBCRLoadedValue = mjAPIFound && CompatibilityManager.isBCraftEnergyLoaded() && CompatibilityManager.classBCTransportPipeTile != null;
//        } TODO BC support

        return cachedBCRLoadedValue;
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
        try
        {
            if (Class.forName("cofh.redstoneflux.api.IEnergyConnection") != null)
            {
                count++;
            }
            if (Class.forName("cofh.redstoneflux.api.IEnergyHandler") != null)
            {
                count += 2;
            }
        }
        catch (Exception e)
        {
        }
        try
        {
            if (Class.forName("cofh.redstoneflux.api.IEnergyProvider") != null)
            {
                count2++;
            }
        }
        catch (Exception e)
        {
        }
        try
        {
            if (Class.forName("cofh.redstoneflux.api.IEnergyReceiver") != null)
            {
                count2++;
            }
        }
        catch (Exception e)
        {
        }

        if ((count + count2 == 3 && count2 != 1) || count + count2 == 5)
        {
            cachedRFLoadedValue = true;
            cachedRF1LoadedValue = (count == 3);
            cachedRF2LoadedValue = (count2 == 2);
        }
        else if (count > 0 || count2 > 0)
        {
            GCLog.severe("Incomplete Redstone Flux API detected: Galacticraft will not support RF energy connections until this is fixed.");
        }
    }

    public static boolean isMekanismLoaded()
    {
        return CompatibilityManager.isMekanismLoaded();
    }

    public static void updateRatios()
    {
        //Sense checks to avoid crazy large inverse ratios or ratios
//        if (EnergyConfigHandler.IC2_RATIO < 0.01F)
//        {
//            EnergyConfigHandler.IC2_RATIO = 0.01F;
//        }
//        if (EnergyConfigHandler.RF_RATIO < 0.001F)
//        {
//            EnergyConfigHandler.RF_RATIO = 0.001F;
//        }
//        if (EnergyConfigHandler.BC_RATIO < 0.01F)
//        {
//            EnergyConfigHandler.BC_RATIO = 0.01F;
//        }
//        if (EnergyConfigHandler.MEKANISM_RATIO < 0.001F)
//        {
//            EnergyConfigHandler.MEKANISM_RATIO = 0.001F;
//        }
//        if (EnergyConfigHandler.IC2_RATIO > 1000F)
//        {
//            EnergyConfigHandler.IC2_RATIO = 1000F;
//        }
//        if (EnergyConfigHandler.RF_RATIO > 100F)
//        {
//            EnergyConfigHandler.RF_RATIO = 100F;
//        }
//        if (EnergyConfigHandler.BC_RATIO > 1000F)
//        {
//            EnergyConfigHandler.BC_RATIO = 1000F;
//        }
//        if (EnergyConfigHandler.MEKANISM_RATIO > 100F)
//        {
//            EnergyConfigHandler.MEKANISM_RATIO = 100F;
//        } Handled by config itself now

        float factor = conversionLossFactor.get() / 100F;
        TO_BC_RATIO = (float) (factor / EnergyConfigHandler.BC_RATIO.get() * BC8_MICROJOULE_RATIO);
        TO_RF_RATIO = (float) (factor / EnergyConfigHandler.RF_RATIO.get());
        TO_IC2_RATIO = (float) (factor / EnergyConfigHandler.IC2_RATIO.get());
        TO_MEKANISM_RATIO = (float) (factor / EnergyConfigHandler.MEKANISM_RATIO.get());
        TO_BC_RATIOdisp = (float) (1 / EnergyConfigHandler.BC_RATIO.get());
        TO_RF_RATIOdisp = (float) (1 / EnergyConfigHandler.RF_RATIO.get());
        TO_IC2_RATIOdisp = (float) (1 / EnergyConfigHandler.IC2_RATIO.get());
        TO_MEKANISM_RATIOdisp = (float) (1 / EnergyConfigHandler.MEKANISM_RATIO.get());
        EnergyConfigHandler.BC_RATIO.set(EnergyConfigHandler.BC_RATIO.get() * factor);
        EnergyConfigHandler.RF_RATIO.set(EnergyConfigHandler.RF_RATIO.get() * factor);
        EnergyConfigHandler.IC2_RATIO.set(EnergyConfigHandler.IC2_RATIO.get() * factor);
        EnergyConfigHandler.MEKANISM_RATIO.set(EnergyConfigHandler.MEKANISM_RATIO.get() * factor);
    }

    public static void serverConfigOverride(ArrayList<Object> returnList)
    {
        returnList.add(EnergyConfigHandler.BC_RATIO);
        returnList.add(EnergyConfigHandler.RF_RATIO);
        returnList.add(EnergyConfigHandler.IC2_RATIO);
        returnList.add(EnergyConfigHandler.MEKANISM_RATIO);
        returnList.add(EnergyConfigHandler.conversionLossFactor);
    }

    public static void setConfigOverride(float sBC3, float sRF, float sIC2, float sMEK, int sLossRatio)
    {
        EnergyConfigHandler.BC_RATIO.set((double) sBC3);
        EnergyConfigHandler.RF_RATIO.set((double) sRF);
        EnergyConfigHandler.IC2_RATIO.set((double) sIC2);
        EnergyConfigHandler.MEKANISM_RATIO.set((double) sMEK);
        EnergyConfigHandler.conversionLossFactor.set(sLossRatio);
        updateRatios();
    }
}
