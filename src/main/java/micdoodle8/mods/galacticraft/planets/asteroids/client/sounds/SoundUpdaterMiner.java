package micdoodle8.mods.galacticraft.planets.asteroids.client.sounds;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8, radfast
 *  
 */
public class SoundUpdaterMiner extends MovingSound
{
	private final EntityPlayerSP thePlayer;
	private final EntityAstroMiner theRocket;
	private boolean soundStopped;
	private float targetVolume;
	private float targetPitch;

	public SoundUpdaterMiner(EntityPlayerSP par1EntityPlayerSP, EntityAstroMiner par2Entity)
	{
        super(new ResourceLocation(GalacticraftCore.TEXTURE_PREFIX + "entity.astrominer"));
		this.theRocket = par2Entity;
		this.thePlayer = par1EntityPlayerSP;
        this.volume = 0.00001F;  //If it's zero it won't start playing
        this.targetVolume = 0.6F;
        this.targetPitch = 1.0F;
        this.field_147663_c = 1.0F;  //pitch
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
        	if (this.theRocket.AIstate == EntityAstroMiner.AISTATE_ATBASE || this.theRocket.AIstate == EntityAstroMiner.AISTATE_DOCKING)
        	{
        		this.targetVolume = 0.6F;
        		this.targetPitch = 0.1F;
        	}
        	else
        	{
        		this.targetVolume = 1.0F;
        		this.targetPitch = 1.0F;
        	}
			if (this.volume < this.targetVolume)
        	{
        		this.volume += 0.1F;
        		if (this.volume > this.targetVolume)
        			this.volume = this.targetVolume;
        	}
        	else if (this.volume > this.targetVolume)
        	{
        		this.volume -= 0.1F;
        		if (this.volume < this.targetVolume)
        			this.volume = this.targetVolume;
        	}
			if (this.field_147663_c < this.targetPitch)
        	{
        		this.field_147663_c += 0.05F;
        		if (this.field_147663_c > this.targetPitch)
        			this.field_147663_c = this.targetPitch;
        	}
        	else if (this.field_147663_c > this.targetPitch)
        	{
        		this.field_147663_c -= 0.05F;
        		if (this.field_147663_c < this.targetPitch)
        			this.field_147663_c = this.targetPitch;
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
