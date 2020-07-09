package micdoodle8.mods.galacticraft.core.util;

import com.mojang.authlib.GameProfile;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class PlayerUtil
{
    public static HashMap<String, GameProfile> knownSkins = new HashMap<>();

    public static ServerPlayerEntity getPlayerForUsernameVanilla(MinecraftServer server, String username)
    {
        return server.getPlayerList().getPlayerByUsername(username);
    }

    public static ServerPlayerEntity getPlayerBaseServerFromPlayerUsername(String username, boolean ignoreCase)
    {
        MinecraftServer server = GCCoreUtil.getServer();
        return getPlayerBaseServerFromPlayerUsername(server, username, ignoreCase);
    }

    public static ServerPlayerEntity getPlayerBaseServerFromPlayerUsername(MinecraftServer server, String username, boolean ignoreCase)
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
                ServerPlayerEntity entityplayermp;

                do
                {
                    if (!iterator.hasNext())
                    {
                        return null;
                    }

                    entityplayermp = (ServerPlayerEntity) iterator.next();
                }
                while (!entityplayermp.getName().getString().equalsIgnoreCase(username));

                return entityplayermp;
            }
        }

        GCLog.severe("Warning: Could not find player base server instance for player " + username);

        return null;
    }

    public static ServerPlayerEntity getPlayerBaseServerFromPlayer(PlayerEntity player, boolean ignoreCase)
    {
        if (player == null)
        {
            return null;
        }

        if (player instanceof ServerPlayerEntity)
        {
            return (ServerPlayerEntity) player;
        }

        return PlayerUtil.getPlayerBaseServerFromPlayerUsername(player.getName().getString(), ignoreCase);
    }

    @OnlyIn(Dist.CLIENT)
    public static ClientPlayerEntity getPlayerBaseClientFromPlayer(PlayerEntity player, boolean ignoreCase)
    {
        ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;

        if (clientPlayer == null && player != null)
        {
            GCLog.severe("Warning: Could not find player base client instance for player " + PlayerUtil.getName(player));
        }

        return clientPlayer;
    }

    @OnlyIn(Dist.CLIENT)
    public static GameProfile getOtherPlayerProfile(String name)
    {
        return knownSkins.get(name);
    }

    @OnlyIn(Dist.CLIENT)
    public static GameProfile makeOtherPlayerProfile(String strName, String strUUID)
    {
        GameProfile profile = null;
        for (Object e : Minecraft.getInstance().world.getAllEntities())
        {
            if (e instanceof AbstractClientPlayerEntity)
            {
                GameProfile gp2 = ((AbstractClientPlayerEntity) e).getGameProfile();
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

    @OnlyIn(Dist.CLIENT)
    public static GameProfile getSkinForName(String strName, String strUUID, DimensionType dimID)
    {
        GameProfile profile = Minecraft.getInstance().player.getGameProfile();
        if (!strName.equals(profile.getName()))
        {
            profile = PlayerUtil.getOtherPlayerProfile(strName);
            if (profile == null)
            {
                profile = PlayerUtil.makeOtherPlayerProfile(strName, strUUID);
            }
            if (!profile.getProperties().containsKey("textures"))
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_REQUEST_PLAYERSKIN, dimID, new Object[]{strName}));
            }
        }
        return profile;
    }

    public static ServerPlayerEntity getPlayerByUUID(UUID theUUID)
    {
        List<ServerPlayerEntity> players = PlayerUtil.getPlayersOnline();
        ServerPlayerEntity entityplayermp;
        for (int i = players.size() - 1; i >= 0; --i)
        {
            entityplayermp = players.get(i);

            if (entityplayermp.getUniqueID().equals(theUUID))
            {
                return entityplayermp;
            }
        }
        return null;
    }


    public static List<ServerPlayerEntity> getPlayersOnline()
    {
        return GCCoreUtil.getServer().getPlayerList().getPlayers();
    }


    public static boolean isPlayerOnline(ServerPlayerEntity player)
    {
        return player.server.getPlayerList().getPlayers().contains(player);
    }

    public static String getName(PlayerEntity player)
    {
        if (player == null)
        {
            return null;
        }

        if (player.getGameProfile() == null)
        {
            return null;
        }

        return player.getGameProfile().getName();
    }
}
