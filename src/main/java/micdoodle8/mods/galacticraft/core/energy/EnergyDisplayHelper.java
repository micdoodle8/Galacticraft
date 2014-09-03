package micdoodle8.mods.galacticraft.core.energy;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

import java.util.List;

public class EnergyDisplayHelper
{
    public static void getEnergyDisplayTooltip(float energyVal, float maxEnergy, List strList)
    {
        strList.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("gui.message.energy") + ": " + getEnergyDisplayS(energyVal));
        strList.add(EnumChatFormatting.RED + StatCollector.translateToLocal("gui.message.maxEnergy") + ": " + getEnergyDisplayS(maxEnergy));
    }

    public static String getEnergyDisplayS(float energyVal)
    {
        if (EnergyConfigHandler.displayEnergyUnitsIC2)
        {
            return getEnergyDisplayIC2(energyVal * EnergyConfigHandler.TO_IC2_RATIO);
        }
        else if (EnergyConfigHandler.displayEnergyUnitsBC)
        {
            return getEnergyDisplayBC(energyVal * EnergyConfigHandler.TO_BC_RATIO);
        }
        else if (EnergyConfigHandler.displayEnergyUnitsMek)
        {
            return getEnergyDisplayMek(energyVal * EnergyConfigHandler.TO_MEKANISM_RATIO);
        }
        else if (EnergyConfigHandler.displayEnergyUnitsRF)
        {
            return getEnergyDisplayRF(energyVal * EnergyConfigHandler.TO_TE_RATIO);
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
        return MathHelper.floor_float(energyVal);
    }

    public static String getEnergyDisplay1DP(float energyVal)
    {
        return "" + MathHelper.floor_float(energyVal) + "." + (MathHelper.floor_float(energyVal * 10) % 10) + (MathHelper.floor_float(energyVal * 100) % 10);
    }
}
