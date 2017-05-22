package micdoodle8.mods.galacticraft.core.client.sounds;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase.EnumLaunchPhase;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * This file is part of the Galacticraft project
 *
 * @author micdoodle8
 */
public class SoundUpdaterRocket extends MovingSound
{
    private final EntityPlayerSP thePlayer;
    private final EntityAutoRocket theRocket;
    private boolean soundStopped;
    private boolean ignition = false;

    public SoundUpdaterRocket(EntityPlayerSP par1EntityPlayerSP, EntityAutoRocket par2Entity)
    {
        super(new ResourceLocation(Constants.TEXTURE_PREFIX + "shuttle.shuttle"));
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
    public void update()
    {
        if (!this.theRocket.isDead)
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
                if (this.theRocket.posY > 1000)
                {
                    this.volume = 0F;
                    if (this.theRocket.launchPhase != EnumLaunchPhase.LANDING.ordinal())
                    {
                        this.donePlaying = true;
                    }
                }
                else if (this.theRocket.posY > Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT)
                {
                    this.volume = 1.0F - (float) ((this.theRocket.posY - Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT) / (1000.0 - Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT));
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
        this.xPosF = (float) e.posX;
        this.yPosF = (float) e.posY;
        this.zPosF = (float) e.posZ;
    }
}
