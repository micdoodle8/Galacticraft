package micdoodle8.mods.galacticraft.mars.client.sounds;

import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCMarsSounds
{
    @ForgeSubscribe
    public void onSound(SoundLoadEvent event)
    {
        try
        {
            // event.manager.soundPoolSounds.addSound("creepernest/singledrip1.wav",
            // GalacticraftMars.class.getResource("/micdoodle8/mods/galacticraft/mars/client/sounds/creepernest/singledrip1.wav"));
            // event.manager.soundPoolSounds.addSound("creepernest/singledrip2.wav",
            // GalacticraftMars.class.getResource("/micdoodle8/mods/galacticraft/mars/client/sounds/creepernest/singledrip2.wav"));
            // event.manager.soundPoolSounds.addSound("creepernest/singledrip3.wav",
            // GalacticraftMars.class.getResource("/micdoodle8/mods/galacticraft/mars/client/sounds/creepernest/singledrip3.wav"));
            // event.manager.soundPoolSounds.addSound("creepernest/singledrip4.wav",
            // GalacticraftMars.class.getResource("/micdoodle8/mods/galacticraft/mars/client/sounds/creepernest/singledrip4.wav"));
            // event.manager.soundPoolSounds.addSound("creepernest/singledrip5.wav",
            // GalacticraftMars.class.getResource("/micdoodle8/mods/galacticraft/mars/client/sounds/creepernest/singledrip5.wav"));
            // event.manager.soundPoolSounds.addSound("creepernest/singledrip6.wav",
            // GalacticraftMars.class.getResource("/micdoodle8/mods/galacticraft/mars/client/sounds/creepernest/singledrip6.wav"));
            // event.manager.soundPoolSounds.addSound("creepernest/singledrip7.wav",
            // GalacticraftMars.class.getResource("/micdoodle8/mods/galacticraft/mars/client/sounds/creepernest/singledrip7.wav"));
            // event.manager.soundPoolSounds.addSound("creepernest/singledrip8.wav",
            // GalacticraftMars.class.getResource("/micdoodle8/mods/galacticraft/mars/client/sounds/creepernest/singledrip8.wav"));
            // event.manager.soundPoolSounds.addSound("creepernest/heartbeat.wav",
            // GalacticraftMars.class.getResource("/micdoodle8/mods/galacticraft/mars/client/sounds/creepernest/heartbeat.wav"));
            // event.manager.soundPoolSounds.addSound("creepernest/scaryscape.wav",
            // GalacticraftMars.class.getResource("/micdoodle8/mods/galacticraft/mars/client/sounds/creepernest/scaryscape.wav"));

        }
        catch (final Exception e)
        {
            System.err.println("[GCMars] Failed to register one or more sounds.");
        }
    }
}
