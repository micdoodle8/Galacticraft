package micdoodle8.mods.galacticraft.mars.entities;

import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import net.minecraft.src.EntityPlayer;
import cpw.mods.fml.common.IPlayerTracker;

public class GCMarsPlayerHandler implements IPlayerTracker
{
	@Override
	public void onPlayerLogin(EntityPlayer player) 
	{
		new GCMarsEntityPlayer(player);
		
		for (int i = 0; i < GalacticraftMars.gcMarsPlayers.size(); i++)
		{
			GCMarsEntityPlayer player2 = (GCMarsEntityPlayer) GalacticraftMars.gcMarsPlayers.get(i);
			
			if (player2.getPlayer().username == player.username)
			{
				player2.readEntityFromNBT();
			}
		}
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) 
	{
		GalacticraftMars.marsPlayers.remove(player);
		
		for (int i = 0; i < GalacticraftMars.gcMarsPlayers.size(); i++)
		{
			GCMarsEntityPlayer player2 = (GCMarsEntityPlayer) GalacticraftMars.gcMarsPlayers.get(i);
			
			if (player2.getPlayer().username == player.username)
			{
				player2.writeEntityToNBT();
				
				GalacticraftMars.gcMarsPlayers.remove(player2);
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
