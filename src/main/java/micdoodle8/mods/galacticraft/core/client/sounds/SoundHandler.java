package micdoodle8.mods.galacticraft.core.client.sounds;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.sound.PlayBackgroundMusicEvent;

public class SoundHandler
{
	@SubscribeEvent
	public void onMusicSound(PlayBackgroundMusicEvent event)
	{
		final Minecraft mc = FMLClientHandler.instance().getClient();

		if (mc.thePlayer != null && mc.thePlayer.worldObj != null && mc.thePlayer.worldObj.provider instanceof IGalacticraftWorldProvider)
		{
			final int randInt = FMLClientHandler.instance().getClient().thePlayer.worldObj.rand.nextInt(ClientProxyCore.newMusic.size() + 2);

			if (randInt < ClientProxyCore.newMusic.size())
			{
				event.result = ClientProxyCore.newMusic.get(randInt);
			}
		}
	}
}
