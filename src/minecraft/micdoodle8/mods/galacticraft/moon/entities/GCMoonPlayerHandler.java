//package micdoodle8.mods.galacticraft.moon.entities;
//
//import micdoodle8.mods.galacticraft.moon.GalacticraftMoon;
//import net.minecraft.entity.player.EntityPlayer;
//import cpw.mods.fml.common.IPlayerTracker;
//
//public class GCMoonPlayerHandler implements IPlayerTracker
//{
//	@Override
//	public void onPlayerLogin(EntityPlayer player)
//	{
//		new GCMoonEntityPlayer(player);
//
//		for (int i = 0; i < GalacticraftMoon.gcMoonPlayers.size(); i++)
//		{
//			final GCMoonEntityPlayer player2 = (GCMoonEntityPlayer) GalacticraftMoon.gcMoonPlayers.get(i);
//
//			if (player2.getPlayer().username == player.username)
//			{
//				player2.readEntityFromNBT();
//			}
//		}
//	}
//
//	@Override
//	public void onPlayerLogout(EntityPlayer player)
//	{
//		GalacticraftMoon.moonPlayers.remove(player);
//
//		for (int i = 0; i < GalacticraftMoon.gcMoonPlayers.size(); i++)
//		{
//			final GCMoonEntityPlayer player2 = (GCMoonEntityPlayer) GalacticraftMoon.gcMoonPlayers.get(i);
//
//			if (player2.getPlayer().username == player.username)
//			{
//				player2.writeEntityToNBT();
//
//				GalacticraftMoon.gcMoonPlayers.remove(player2);
//			}
//		}
//	}
//
//	@Override
//	public void onPlayerChangedDimension(EntityPlayer player)
//	{
//
//	}
//
//	@Override
//	public void onPlayerRespawn(EntityPlayer player)
//	{
//
//	}
//}
