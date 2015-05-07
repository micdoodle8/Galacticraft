package micdoodle8.mods.galacticraft.planets.asteroids.client.sounds;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import net.minecraft.client.audio.ISound;
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

	public SoundUpdaterMiner(EntityPlayerSP par1EntityPlayerSP, EntityAstroMiner par2Entity)
	{
        super(new ResourceLocation(GalacticraftCore.TEXTURE_PREFIX + "entity.astrominer"));
		this.theRocket = par2Entity;
		this.thePlayer = par1EntityPlayerSP;
        this.field_147666_i = ISound.AttenuationType.NONE;
        this.volume = 0.00001F;  //If it's zero it won't start playing
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
        	if (this.volume < 1.0F)
        	{
        		this.volume += 0.1F;
        		if (this.volume > 1.0F)
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
