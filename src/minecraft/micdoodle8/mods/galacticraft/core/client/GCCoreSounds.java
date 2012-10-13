package micdoodle8.mods.galacticraft.core.client;

import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

/**
 * Copyright 2012, micdoodle8
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
            event.manager.soundPoolSounds.addSound("shuttle/sound.wav", GalacticraftMars.class.getResource("/micdoodle8/mods/galacticraft/core/client/sounds/shuttle/sound.wav"));                      
       
        }
        catch (Exception e)
        {
            System.err.println("[GCCore] Failed to register one or more sounds.");
        }
    }
}