package micdoodle8.mods.galacticraft.core.util;

import com.mojang.authlib.GameProfile;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class PlayerUtil
{
    public static HashMap<String, GameProfile> knownSkins = new HashMap<>();

    public static EntityPlayerMP getPlayerForUsernameVanilla(MinecraftServer server, String username)
    {
        return server.getPlayerList().getPlayerByUsername(username);
    }

    public static EntityPlayerMP getPlayerBaseServerFromPlayerUsername(String username, boolean ignoreCase)
    {
        MinecraftServer server = GCCoreUtil.getServer();
        return getPlayerBaseServerFromPlayerUsername(server, username, ignoreCase);
    }

    public static EntityPlayerMP getPlayerBaseServerFromPlayerUsername(MinecraftServer server, String username, boolean ignoreCase)
    {
        if (server != null)
        {
            if (ignoreCase)
            {
                return getPlayerForUsernameVanilla(server, username);
            }
            else
            {
                Iterator iterator = server.getPlayerList().getPlayers().iterator();
                EntityPlayerMP entityplayermp;

                do
                {
                    if (!iterator.hasNext())
                    {
                        return null;
                    }

                    entityplayermp = (EntityPlayerMP) iterator.next();
                }
                while (!entityplayermp.getName().equalsIgnoreCase(username));

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

        return PlayerUtil.getPlayerBaseServerFromPlayerUsername(player.getName(), ignoreCase);
    }

    @SideOnly(Side.CLIENT)
    public static EntityPlayerSP getPlayerBaseClientFromPlayer(EntityPlayer player, boolean ignoreCase)
    {
        EntityPlayerSP clientPlayer = FMLClientHandler.instance().getClientPlayerEntity();

        if (clientPlayer == null && player != null)
        {
            GCLog.severe("Warning: Could not find player base client instance for player " + PlayerUtil.getName(player));
        }

        return clientPlayer;
    }

    @SideOnly(Side.CLIENT)
    public static GameProfile getOtherPlayerProfile(String name)
    {
        return knownSkins.get(name);
    }

    @SideOnly(Side.CLIENT)
    public static GameProfile makeOtherPlayerProfile(String strName, String strUUID)
    {
        GameProfile profile = null;
        for (Object e : FMLClientHandler.instance().getWorldClient().getLoadedEntityList())
        {
            if (e instanceof AbstractClientPlayer)
            {
                GameProfile gp2 = ((AbstractClientPlayer) e).getGameProfile();
                if (gp2.getName().equals(strName))
                {
                    profile = gp2;
                    break;
                }
            }
        }
        if (profile == null)
        {
            UUID uuid = strUUID.isEmpty() ? UUID.randomUUID() : UUID.fromString(strUUID);
            profile = new GameProfile(uuid, strName);
        }

        PlayerUtil.knownSkins.put(strName, profile);
        return profile;
    }

    @SideOnly(Side.CLIENT)
    public static GameProfile getSkinForName(String strName, String strUUID, int dimID)
    {
        GameProfile profile = FMLClientHandler.instance().getClientPlayerEntity().getGameProfile();
        if (!strName.equals(profile.getName()))
        {
            profile = PlayerUtil.getOtherPlayerProfile(strName);
            if (profile == null)
            {
                profile = PlayerUtil.makeOtherPlayerProfile(strName, strUUID);
            }
            if (!profile.getProperties().containsKey("textures"))
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_REQUEST_PLAYERSKIN, dimID, new Object[] { strName }));
            }
        }
        return profile;
    }
    
    public static EntityPlayerMP getPlayerByUUID(UUID theUUID)
    {
        List<EntityPlayerMP> players = PlayerUtil.getPlayersOnline();
        EntityPlayerMP entityplayermp;
        for (int i = players.size() - 1; i >= 0; --i)
        {
            entityplayermp = (EntityPlayerMP) players.get(i);

            if (entityplayermp.getUniqueID().equals(theUUID))
            {
                return entityplayermp;
            }
        }
        return null;
    }
    
    
    public static List<EntityPlayerMP> getPlayersOnline()
    {
        return GCCoreUtil.getServer().getPlayerList().getPlayers();
    }


    public static boolean isPlayerOnline(EntityPlayerMP player)
    {
        return player.mcServer.getPlayerList().getPlayers().contains(player);
    }
    
    public static String getName(EntityPlayer player)
    {
        if (player == null) return null;
        
        if (player.getGameProfile() == null) return null;
        
        return player.getGameProfile().getName();
    }
}
