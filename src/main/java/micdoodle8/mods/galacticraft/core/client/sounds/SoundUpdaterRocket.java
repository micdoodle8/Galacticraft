package micdoodle8.mods.galacticraft.core.client.sounds;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase.EnumLaunchPhase;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class SoundUpdaterRocket extends MovingSound
{
	//private final SoundHandler soundHandler;
	//private final ISound theSound;
	
	private final EntityPlayerSP thePlayer;
	private final EntitySpaceshipBase theRocket;
	private boolean soundStopped;
	private boolean ignition = false;

	public SoundUpdaterRocket(EntityPlayerSP par1EntityPlayerSP, EntitySpaceshipBase par2Entity)
	{
        super(new ResourceLocation(GalacticraftCore.TEXTURE_PREFIX + "shuttle.shuttle"));
		this.theRocket = par2Entity;
		this.thePlayer = par1EntityPlayerSP;
        this.field_147666_i = ISound.AttenuationType.NONE;
        this.volume = 0.00001F;  //If it's zero it won't start playing
        this.field_147663_c = 0.0F;  //pitch
        this.repeat = true;
        this.field_147665_h = 0;  //repeat delay
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
					this.field_147663_c = 0.0F;
					ignition = true;
				}
        		if (this.theRocket.timeUntilLaunch < this.theRocket.getPreLaunchWait())
    			{
    				if (this.field_147663_c < 1.0F)
    				{
    					this.field_147663_c += 0.0025F;
    				}

    				if (this.field_147663_c > 1.0F)
    				{
    					this.field_147663_c = 1.0F;
    				}
    			}
			}
        	else
        		this.field_147663_c = 1.0F;
        	
        	if (this.theRocket.launchPhase == EnumLaunchPhase.IGNITED.ordinal() || this.theRocket.getLaunched())
        	{
//    			float var10 = MathHelper.clamp_float((float) this.minecartSpeed, 0.0F, 4.0F) / 4.0F;
//    			this.minecartRideSoundVolume = 0.0F + var10 * 0.75F;
//    			var10 = MathHelper.clamp_float(var10 * 2.0F, 0.0F, 1.0F);
//    			this.minecartMoveSoundVolume = 0.0F + var10 * 6.7F;

    			if (this.theRocket.posY > 1000)
    			{
    				this.volume = 0F;
    				this.donePlaying = true;
    			}
    			else if (this.theRocket.posY > 200)
    			{
    				this.volume = (1200F - (float) this.theRocket.posY) * 0.001F;
    			}
    			else
    				this.volume = 1.0F;
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
