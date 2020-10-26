package micdoodle8.mods.galacticraft.core.client.sounds;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase.EnumLaunchPhase;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;

/**
 * This file is part of the Galacticraft project
 *
 * @author micdoodle8
 */
public class SoundUpdaterRocket extends TickableSound
{
    private final ClientPlayerEntity thePlayer;
    private final EntityAutoRocket theRocket;
    private boolean soundStopped;
    private boolean ignition = false;

    public SoundUpdaterRocket(ClientPlayerEntity par1EntityPlayerSP, EntityAutoRocket par2Entity)
    {
        super(GCSounds.shuttle, SoundCategory.NEUTRAL);
        this.theRocket = par2Entity;
        this.thePlayer = par1EntityPlayerSP;
        this.attenuationType = ISound.AttenuationType.NONE;
        this.volume = 0.00001F;  //If it's zero it won't start playing
        this.pitch = 0.0F;  //pitch
        this.repeat = true;
        this.repeatDelay = 0;  //repeat delay
        this.updateSoundLocation(par2Entity);
    }

    /**
     * Updates the JList with a new model.
     */
    @Override
    public void tick()
    {
        if (this.theRocket.isAlive())
        {
            if (this.theRocket.launchPhase == EnumLaunchPhase.IGNITED.ordinal())
            {
                if (!ignition)
                {
                    this.pitch = 0.0F;
                    ignition = true;
                }
                if (this.theRocket.timeUntilLaunch < this.theRocket.getPreLaunchWait())
                {
                    if (this.pitch < 1.0F)
                    {
                        this.pitch += 0.0025F;
                    }

                    if (this.pitch > 1.0F)
                    {
                        this.pitch = 1.0F;
                    }
                }
            }
            else
            {
                this.pitch = 1.0F;
            }

            if (this.theRocket.launchPhase >= EnumLaunchPhase.IGNITED.ordinal())
            {
                if (this.theRocket.getPosY() > 1000)
                {
                    this.volume = 0F;
                    if (this.theRocket.launchPhase != EnumLaunchPhase.LANDING.ordinal())
                    {
                        this.donePlaying = true;
                    }
                }
                else if (this.theRocket.getPosY() > Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT)
                {
                    this.volume = 1.0F - (float) ((this.theRocket.getPosY() - Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT) / (1000.0 - Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT));
                }
                else
                {
                    this.volume = 1.0F;
                }
            }

            this.updateSoundLocation(this.theRocket);
        }
        else
        {
            this.donePlaying = true;
        }
    }

    public void stopRocketSound()
    {
        this.donePlaying = true;
        this.soundStopped = true;
    }

    public void updateSoundLocation(Entity e)
    {
        this.x = (float) e.getPosX();
        this.y = (float) e.getPosY();
        this.z = (float) e.getPosZ();
    }
}
