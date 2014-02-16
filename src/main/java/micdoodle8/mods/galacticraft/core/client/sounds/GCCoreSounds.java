package micdoodle8.mods.galacticraft.core.client.sounds;

import net.minecraftforge.client.event.sound.PlayBackgroundMusicEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * GCCoreSounds.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreSounds
{
	@SubscribeEvent
	public void onMusicSound(PlayBackgroundMusicEvent event)
	{
//		final Minecraft mc = FMLClientHandler.instance().getClient();
//
//		if (mc.thePlayer != null && mc.thePlayer.worldObj != null && mc.thePlayer.worldObj.provider instanceof IGalacticraftWorldProvider)
//		{
//			final int randInt = FMLClientHandler.instance().getClient().thePlayer.worldObj.rand.nextInt(ClientProxyCore.newMusic.size() + 2);
//
//			if (randInt < ClientProxyCore.newMusic.size())
//			{
//				event.result = ClientProxyCore.newMusic.get(randInt);
//			}
//		} TODO Fix Music
	}
}
