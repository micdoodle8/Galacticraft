package micdoodle8.mods.galacticraft.core.client.sounds;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraftforge.client.event.sound.PlayBackgroundMusicEvent;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.client.FMLClientHandler;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCCoreSounds
{
    @ForgeSubscribe
    public void onMusicSound(PlayBackgroundMusicEvent event)
    {
        final Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc.thePlayer.worldObj.provider instanceof IGalacticraftWorldProvider)
        {
            for (int i = 0; i < GalacticraftCore.clientSubMods.size(); i++)
            {
                final IGalacticraftSubModClient client = GalacticraftCore.clientSubMods.get(i);

                if (client != null && client.getDimensionName() != null)
                {
                    if (mc.thePlayer.worldObj.provider.getDimensionName().toLowerCase().equals(client.getDimensionName().toLowerCase()))
                    {
                        if (client.getPathToMusicFile() != null)
                        {
                            final String[] strings = client.getPathToMusicFile().split("//");

                            for (final String string : strings)
                            {
                                if (string.toLowerCase().contains(".ogg"))
                                {
                                    event.result = new SoundPoolEntry(string, GalacticraftCore.class.getResource(client.getPathToMusicFile()));
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            final int randInt = FMLClientHandler.instance().getClient().thePlayer.worldObj.rand.nextInt(6);

            if (randInt < ClientProxyCore.newMusic.size())
            {
                event.result = ClientProxyCore.newMusic.get(randInt);
            }
        }
    }
}
