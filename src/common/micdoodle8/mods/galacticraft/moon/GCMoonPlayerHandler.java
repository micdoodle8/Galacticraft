package micdoodle8.mods.galacticraft.moon;

import net.minecraft.src.EntityPlayer;
import cpw.mods.fml.common.IPlayerTracker;

public class GCMoonPlayerHandler implements IPlayerTracker
{
	@Override
	public void onPlayerLogin(EntityPlayer player) 
	{
		new GCMoonEntityPlayer(player);
		
		for (int i = 0; i < GalacticraftMoon.gcMoonPlayers.size(); i++)
		{
			GCMoonEntityPlayer player2 = (GCMoonEntityPlayer) GalacticraftMoon.gcMoonPlayers.get(i);
			
			if (player2.getPlayer().username == player.username)
			{
				player2.readEntityFromNBT();
			}
		}
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) 
	{
		GalacticraftMoon.moonPlayers.remove(player);
		
		for (int i = 0; i < GalacticraftMoon.gcMoonPlayers.size(); i++)
		{
			GCMoonEntityPlayer player2 = (GCMoonEntityPlayer) GalacticraftMoon.gcMoonPlayers.get(i);
			
			if (player2.getPlayer().username == player.username)
			{
				player2.writeEntityToNBT();
				
				GalacticraftMoon.gcMoonPlayers.remove(player2);
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
