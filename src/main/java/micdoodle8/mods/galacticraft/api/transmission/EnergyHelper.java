package micdoodle8.mods.galacticraft.api.transmission;

import micdoodle8.mods.galacticraft.api.transmission.compatibility.NetworkConfigHandler;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class EnergyHelper
{
    public static void getEnergyDisplayTooltip(float energyVal, float maxEnergy, List strList)
    {
        strList.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("gui.message.energy") + ": " + getEnergyDisplayS(energyVal));
        strList.add(EnumChatFormatting.RED + StatCollector.translateToLocal("gui.message.maxEnergy") + ": " + getEnergyDisplayS(maxEnergy));
    }

    public static String getEnergyDisplayS(float energyVal)
    {
    	if (NetworkConfigHandler.displayEnergyUnitsIC2)
    	{
    		return getEnergyDisplayIC2(energyVal * NetworkConfigHandler.TO_IC2_RATIO);
    	}
    	else if (NetworkConfigHandler.displayEnergyUnitsBC)
    	{
    		return getEnergyDisplayBC(energyVal * NetworkConfigHandler.TO_BC_RATIO);
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
    
    public static int getEnergyDisplayI(float energyVal)
    {
        return (int)Math.floor(energyVal);
    }
}
