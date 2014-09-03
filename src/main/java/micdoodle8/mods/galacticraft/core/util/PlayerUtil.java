package micdoodle8.mods.galacticraft.core.util;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.Iterator;

public class PlayerUtil
{
    public static EntityPlayerMP getPlayerForUsernameVanilla(MinecraftServer server, String username)
    {
        return VersionUtil.getPlayerForUsername(server, username);
    }

    public static EntityPlayerMP getPlayerBaseServerFromPlayerUsername(String username, boolean ignoreCase)
    {
        MinecraftServer server = MinecraftServer.getServer();

        if (server != null)
        {
            if (ignoreCase)
            {
                return getPlayerForUsernameVanilla(server, username);
            }
            else
            {
                Iterator iterator = server.getConfigurationManager().playerEntityList.iterator();
                EntityPlayerMP entityplayermp;

                do
                {
                    if (!iterator.hasNext())
                    {
                        return null;
                    }

                    entityplayermp = (EntityPlayerMP) iterator.next();
                }
                while (!entityplayermp.getCommandSenderName().equalsIgnoreCase(username));

                return entityplayermp;
            }
        }

        GCLog.severe("Warning: Could not find player base server instance for player " + username);

        return null;
    }

    public static EntityPlayerMP getPlayerBaseServerFromPlayer(EntityPlayer player, boolean ignoreCase)
    {
        if (player == null)
        {
            return null;
        }

        if (player instanceof EntityPlayerMP)
        {
            return (EntityPlayerMP) player;
        }

        return PlayerUtil.getPlayerBaseServerFromPlayerUsername(player.getCommandSenderName(), ignoreCase);
    }

    @SideOnly(Side.CLIENT)
    public static EntityClientPlayerMP getPlayerBaseClientFromPlayer(EntityPlayer player, boolean ignoreCase)
    {
        EntityClientPlayerMP clientPlayer = FMLClientHandler.instance().getClientPlayerEntity();

        if (clientPlayer == null && player != null)
        {
            GCLog.severe("Warning: Could not find player base client instance for player " + player.getGameProfile().getName());
        }

        return clientPlayer;
    }
}
