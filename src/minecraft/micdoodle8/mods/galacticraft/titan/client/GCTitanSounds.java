package micdoodle8.mods.galacticraft.titan.client;

import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCTitanSounds
{
    @ForgeSubscribe
    public void onSound(SoundLoadEvent event)
    {
        try
        {
        }
        catch (Exception e)
        {
            System.err.println("[GCTitan] Failed to register one or more sounds.");
        }
    }
}