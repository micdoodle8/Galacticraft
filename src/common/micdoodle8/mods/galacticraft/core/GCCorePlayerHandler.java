package micdoodle8.mods.galacticraft.core;

import net.minecraft.src.EntityPlayer;
import cpw.mods.fml.common.IPlayerTracker;

public class GCCorePlayerHandler implements IPlayerTracker
{
	@Override
	public void onPlayerLogin(EntityPlayer player) 
	{
		new GCCoreEntityPlayer(player);
		
		for (int i = 0; i < GalacticraftCore.instance.gcPlayers.size(); i++)
		{
			GCCoreEntityPlayer player2 = (GCCoreEntityPlayer) GalacticraftCore.instance.gcPlayers.get(i);
			
			if (player2.getPlayer().username == player.username)
			{
				player2.readEntityFromNBT();
			}
		}
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) 
	{
		GalacticraftCore.instance.players.remove(player);
		
		for (int i = 0; i < GalacticraftCore.instance.gcPlayers.size(); i++)
		{
			GCCoreEntityPlayer player2 = (GCCoreEntityPlayer) GalacticraftCore.instance.gcPlayers.get(i);
			
			if (player2.getPlayer().username == player.username)
			{
				player2.writeEntityToNBT();
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
