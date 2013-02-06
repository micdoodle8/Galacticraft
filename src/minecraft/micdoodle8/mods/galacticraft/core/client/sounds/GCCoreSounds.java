package micdoodle8.mods.galacticraft.core.client.sounds;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraftforge.client.event.sound.PlayBackgroundMusicEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreSounds
{
    @ForgeSubscribe
    public void onSound(SoundLoadEvent event)
    {
        try
        {
            event.manager.soundPoolSounds.addSound("shuttle/sound.wav", GalacticraftCore.class.getResource("/micdoodle8/mods/galacticraft/core/client/sounds/shuttle/sound.wav"));                      
       
        }
        catch (final Exception e)
        {
            System.err.println("[GCCore] Failed to register one or more sounds.");
        }
    }
    
    @ForgeSubscribe
    public void onMusicSound(PlayBackgroundMusicEvent event)
    {
    	if (FMLClientHandler.instance().getClient().thePlayer.worldObj.provider instanceof IGalacticraftWorldProvider)
    	{
    		if (FMLClientHandler.instance().getClient().thePlayer.worldObj.rand.nextInt(1) == 0)
    		{
    			event.result = new SoundPoolEntry("music/spacerace_JT.ogg", GalacticraftCore.class.getResource("/micdoodle8/mods/galacticraft/core/client/sounds/music/spacerace_JT.ogg"));
    		}
    	}
    	else
    	{
    		event.result = null;
    	}
    }
}