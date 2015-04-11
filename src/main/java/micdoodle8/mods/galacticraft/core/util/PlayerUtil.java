package micdoodle8.mods.galacticraft.core.util;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class PlayerUtil
{
	public static HashMap<String, GameProfile> knownSkins = new HashMap();
	
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
				GameProfile gp2 = ((AbstractClientPlayer)e).getGameProfile();
				if (gp2.getName().equals(strName))
				{
					profile = gp2;
					break;
				}
			}
		}
		if (profile == null) 
			try {
				UUID uuid = strUUID.isEmpty() ? UUID.randomUUID() : UUID.fromString(strUUID);
				profile = VersionUtil.constructGameProfile(uuid, strName);
			} catch (Exception e) { e.printStackTrace(); }
		if (profile == null) profile = VersionUtil.constructGameProfile(UUID.randomUUID(), strName);
		
		PlayerUtil.knownSkins.put(strName, profile); 
		return profile;
	}
}
