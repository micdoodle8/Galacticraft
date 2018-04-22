package micdoodle8.mods.galacticraft.core.command;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandGCEnergyUnits extends CommandBase
{

    @Override
    public String getUsage(ICommandSender var1)
    {
        String options = " [gJ";
        if (EnergyConfigHandler.isBuildcraftLoaded())
        {
            options = options + "|MJ";
        }
        if (EnergyConfigHandler.isIndustrialCraft2Loaded())
        {
            options = options + "|EU";
        }
        if (EnergyConfigHandler.isMekanismLoaded())
        {
            options = options + "|J";
        }
        options = options + "|RF";
        return "/" + this.getName() + options + "]";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public String getName()
    {
        return "gcenergyunits";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);
        if (playerBase == null)
        {
            return;
        }

        if (args.length == 1)
        {
            String param = args[0].toLowerCase();
            if (param.length() <= 2)
            {
                int paramvalue = 0;
                if ("gj".equals(param))
                {
                    paramvalue = 1;
                }
                else if ("mj".equals(param) && EnergyConfigHandler.isBuildcraftLoaded())
                {
                    paramvalue = 2;
                }
                else if ("eu".equals(param) && EnergyConfigHandler.isIndustrialCraft2Loaded())
                {
                    paramvalue = 3;
                }
                else if ("j".equals(param) && EnergyConfigHandler.isMekanismLoaded())
                {
                    paramvalue = 4;
                }
                else if ("rf".equals(param))
                {
                    paramvalue = 5;
                }

                if (paramvalue > 0)
                {
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_ENERGYUNITS, GCCoreUtil.getDimensionID(playerBase.world), new Object[] { paramvalue }), playerBase);
                    return;
                }

            }

            throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.gcenergyunits.invalid_units", this.getUsage(sender)), new Object[0]);
        }

        throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.gcenergyunits.no_units", this.getUsage(sender)), new Object[0]);
    }


    public static void handleParamClientside(int param)
    {
        if (param == 1)
        {
            EnergyConfigHandler.displayEnergyUnitsBC = false;
            EnergyConfigHandler.displayEnergyUnitsIC2 = false;
            EnergyConfigHandler.displayEnergyUnitsMek = false;
            EnergyConfigHandler.displayEnergyUnitsRF = false;
            return;
        }

        if (param == 2 && EnergyConfigHandler.isBuildcraftLoaded())
        {
            EnergyConfigHandler.displayEnergyUnitsBC = true;
            EnergyConfigHandler.displayEnergyUnitsIC2 = false;
            EnergyConfigHandler.displayEnergyUnitsMek = false;
            EnergyConfigHandler.displayEnergyUnitsRF = false;
            return;
        }

        if (param == 3 && EnergyConfigHandler.isIndustrialCraft2Loaded())
        {
            EnergyConfigHandler.displayEnergyUnitsBC = false;
            EnergyConfigHandler.displayEnergyUnitsIC2 = true;
            EnergyConfigHandler.displayEnergyUnitsMek = false;
            EnergyConfigHandler.displayEnergyUnitsRF = false;
            return;
        }

        if (param == 4 && EnergyConfigHandler.isMekanismLoaded())
        {
            EnergyConfigHandler.displayEnergyUnitsBC = false;
            EnergyConfigHandler.displayEnergyUnitsIC2 = false;
            EnergyConfigHandler.displayEnergyUnitsMek = true;
            EnergyConfigHandler.displayEnergyUnitsRF = false;
            return;
        }

        if (param == 5)
        {
            EnergyConfigHandler.displayEnergyUnitsBC = false;
            EnergyConfigHandler.displayEnergyUnitsIC2 = false;
            EnergyConfigHandler.displayEnergyUnitsMek = false;
            EnergyConfigHandler.displayEnergyUnitsRF = true;
            return;
        }
    }
}
