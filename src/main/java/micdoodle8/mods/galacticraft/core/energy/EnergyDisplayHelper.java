package micdoodle8.mods.galacticraft.core.energy;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class EnergyDisplayHelper
{
    public static void getEnergyDisplayTooltip(float energyVal, float maxEnergy, List<String> strList)
    {
        strList.add(TextFormatting.GREEN + GCCoreUtil.translate("gui.message.energy") + ": " + getEnergyDisplayS(energyVal));
        strList.add(TextFormatting.RED + GCCoreUtil.translate("gui.message.max_energy") + ": " + getEnergyDisplayS(maxEnergy));
    }

    public static String getEnergyDisplayS(float energyVal)
    {
        if (EnergyConfigHandler.displayEnergyUnitsIC2)
        {
            return getEnergyDisplayIC2(energyVal * EnergyConfigHandler.TO_IC2_RATIOdisp);
        }
        else if (EnergyConfigHandler.displayEnergyUnitsBC)
        {
            return getEnergyDisplayBC(energyVal * EnergyConfigHandler.TO_BC_RATIOdisp);
        }
        else if (EnergyConfigHandler.displayEnergyUnitsMek)
        {
            return getEnergyDisplayMek(energyVal * EnergyConfigHandler.TO_MEKANISM_RATIOdisp);
        }
        else if (EnergyConfigHandler.displayEnergyUnitsRF)
        {
            return getEnergyDisplayRF(energyVal * EnergyConfigHandler.TO_RF_RATIOdisp);
        }
        String val = String.valueOf(getEnergyDisplayI(energyVal));
        String newVal = "";

        for (int i = val.length() - 1; i >= 0; i--)
        {
            newVal += val.charAt(val.length() - 1 - i);
            if (i % 3 == 0 && i != 0)
            {
                newVal += ',';
            }
        }

        return newVal + " gJ";
    }

    public static String getEnergyDisplayIC2(float energyVal)
    {
        String val = String.valueOf(getEnergyDisplayI(energyVal));
        String newVal = "";

        for (int i = val.length() - 1; i >= 0; i--)
        {
            newVal += val.charAt(val.length() - 1 - i);
            if (i % 3 == 0 && i != 0)
            {
                newVal += ',';
            }
        }

        return newVal + " EU";
    }

    public static String getEnergyDisplayBC(float energyVal)
    {
        String val = String.valueOf(getEnergyDisplayI(energyVal));

        return val + " MJ";
    }

    public static String getEnergyDisplayMek(float energyVal)
    {
        if (energyVal < 1000)
        {
            String val = String.valueOf(getEnergyDisplayI(energyVal));
            return val + " J";
        }
        else if (energyVal < 1000000)
        {
            String val = getEnergyDisplay1DP(energyVal / 1000);
            return val + " kJ";
        }
        else
        {
            String val = getEnergyDisplay1DP(energyVal / 1000000);
            return val + " MJ";
        }
    }

    public static String getEnergyDisplayRF(float energyVal)
    {
        String val = String.valueOf(getEnergyDisplayI(energyVal));

        return val + " RF";
    }

    public static int getEnergyDisplayI(float energyVal)
    {
        return MathHelper.floor(energyVal);
    }

    public static String getEnergyDisplay1DP(float energyVal)
    {
        return "" + MathHelper.floor(energyVal) + "." + (MathHelper.floor(energyVal * 10) % 10) + (MathHelper.floor(energyVal * 100) % 10);
    }
}
