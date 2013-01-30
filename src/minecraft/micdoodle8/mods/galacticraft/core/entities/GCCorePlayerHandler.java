package micdoodle8.mods.galacticraft.core.entities;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IPlayerTracker;

public class GCCorePlayerHandler implements IPlayerTracker
{
	@Override
	public void onPlayerLogin(EntityPlayer player) 
	{
		new GCCoreEntityPlayer(player);
		
		if (player.worldObj.isRemote)
			this.loadDownloadableImageTexture("https://minotar.net/helm/" + StringUtils.stripControlCodes(player.username) + "/32.png", player.getTexture());
		
		for (int i = 0; i < GalacticraftCore.gcPlayers.size(); i++)
		{
			final GCCoreEntityPlayer player2 = (GCCoreEntityPlayer) GalacticraftCore.gcPlayers.get(i);
			
			if (player2.getPlayer().username == player.username)
			{
				player2.readEntityFromNBT();
				
				if (player.posY > 420D)
				{
					final Integer[] ids = DimensionManager.getIDs();
			    	
			    	final Set set = GCCoreUtil.getArrayOfPossibleDimensions(ids).entrySet();
			    	final Iterator iter = set.iterator();
			    	
			    	String temp = "";
			    	
			    	for (int k = 0; iter.hasNext(); k++)
			    	{
			    		final Map.Entry entry = (Map.Entry)iter.next();
			    		temp = k == 0 ? temp.concat(String.valueOf(entry.getKey())) : temp.concat("." + String.valueOf(entry.getKey()));
			    	}
			    	
			    	final Object[] toSend = {player.username, temp};
			        FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(player.username).playerNetServerHandler.sendPacketToPlayer(GCCoreUtil.createPacket("Galacticraft", 2, toSend));
				}
			}
		}
	}
	
    protected boolean loadDownloadableImageTexture(String par1Str, String par2Str)
    {
        final RenderEngine var3 = FMLClientHandler.instance().getClient().renderEngine;
        final int var4 = var3.getTextureForDownloadableImage(par1Str, par2Str);

        if (var4 >= 0)
        {
            var3.bindTexture(var4);
            return true;
        }
        else
        {
            return false;
        }
    }

	@Override
	public void onPlayerLogout(EntityPlayer player) 
	{
		GalacticraftCore.players.remove(player);
		
		for (int i = 0; i < GalacticraftCore.gcPlayers.size(); i++)
		{
			final GCCoreEntityPlayer player2 = (GCCoreEntityPlayer) GalacticraftCore.gcPlayers.get(i);
			
			if (player2.getPlayer().username == player.username)
			{
				player2.writeEntityToNBT();
				
				GalacticraftCore.gcPlayers.remove(player2);
			}
		}
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) 
	{
		
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player) 
	{
		
	}
}
