package micdoodle8.mods.galacticraft.core.command;

import java.util.LinkedList;
import java.util.List;

import com.mojang.authlib.GameProfile;

import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;

public class CommandGCHouston extends CommandBase
{
    private static List<EntityPlayerMP> timerList = new LinkedList<>();

    public static void reset()
    {
        timerList.clear();
    }
    
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getCommandName() + " [<player>]";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
    
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return true;
    }

    @Override
    public String getCommandName()
    {
        return "gchouston";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP playerBase = null;
        MinecraftServer server = MinecraftServer.getServer();
        boolean isOp = false;
        Entity entitySender = sender.getCommandSenderEntity();
        if (entitySender == null)
        {
            isOp = true;
        }
        else if (entitySender instanceof EntityPlayer)
        {
            GameProfile prof = ((EntityPlayer) entitySender).getGameProfile();
            isOp = server.getConfigurationManager().canSendCommands(prof);
        }

        if (args.length < 2)
        {
            try
            {
                if (args.length == 0)
                {
                    playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);
                    if (!playerBase.capabilities.isCreativeMode)
                    {
                        if (ConfigManagerCore.challengeMode || !(playerBase.worldObj.provider instanceof IGalacticraftWorldProvider) )
                        {
                            CommandBase.notifyOperators(sender, this, "commands.gchouston.fail");
                            return;
                        }
                    }

                    if (timerList.contains(playerBase))
                    {
                        timerList.remove(playerBase);
                    }
                    else
                    {
                        timerList.add(playerBase);
                        TickHandlerServer.timerHoustonCommand = 250;
                        String msg = EnumColor.YELLOW + GCCoreUtil.translate("commands.gchouston.confirm.1") + " " + EnumColor.WHITE + GCCoreUtil.translate("commands.gchouston.confirm.2");
                        CommandBase.notifyOperators(sender, this, msg);
                        return;
                    }
                }
                else
                {
                    if (!isOp)
                    {
                        CommandBase.notifyOperators(sender, this, "commands.gchouston.noop");
                        return;
                    }
                    
                    playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(args[0], true);
                }

                if (playerBase != null)
                {
                    int dimID = ConfigManagerCore.idDimensionOverworld;
                    WorldServer worldserver = server.worldServerForDimension(dimID);
                    if (worldserver == null)
                    {
                        worldserver = server.worldServerForDimension(0);
                        if (worldserver == null)
                        {
                            throw new Exception("/gchouston could not find Overworld.");
                        }
                        dimID = 0;
                    }
                    BlockPos spawnPoint = null;
                    BlockPos bedPos = playerBase.getBedLocation(dimID);
                    if (bedPos != null)
                    {
                        spawnPoint = EntityPlayer.getBedSpawnLocation(worldserver, bedPos, false);
                    }
                    if (spawnPoint == null)
                    {
                        spawnPoint = worldserver.getTopSolidOrLiquidBlock(worldserver.getSpawnPoint());
                    }
                    GCPlayerStats stats = GCPlayerStats.get(playerBase);
                    stats.setRocketStacks(new ItemStack[0]);
                    stats.setRocketType(IRocketType.EnumRocketType.DEFAULT.ordinal());
                    stats.setRocketItem(null);
                    stats.setFuelLevel(0);
                    stats.setCoordsTeleportedFromX(spawnPoint.getX());
                    stats.setCoordsTeleportedFromZ(spawnPoint.getZ());
                    stats.setUsingPlanetSelectionGui(false);
                    playerBase.closeScreen();
                    Vector3 spawnPos = new Vector3(spawnPoint.getX() + 0.5D, spawnPoint.getY() + 0.25D, spawnPoint.getZ() + 0.5D);

                    try
                    {
                        WorldUtil.teleportEntitySimple(worldserver, dimID, playerBase, spawnPos);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        throw e;
                    }

                    CommandBase.notifyOperators(sender, this, "commands.gchouston.success", new Object[] { String.valueOf(EnumColor.GREY + "" + playerBase.getName()) });
                }
                else
                {
                    throw new Exception("Could not find player with name: " + args[0]);
                }
            }
            catch (final Exception var6)
            {
                throw new CommandException(var6.getMessage(), new Object[0]);
            }
        }
        else
        {
            throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.dimensiontp.too_many", this.getCommandUsage(sender)), new Object[0]);
        }
    }
}
