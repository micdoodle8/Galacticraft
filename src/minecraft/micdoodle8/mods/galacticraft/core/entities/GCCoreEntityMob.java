package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public abstract class GCCoreEntityMob extends EntityMob
{
	public GCCoreEntityMob(World par1World)
	{
		super(par1World);
	}
    
    private boolean handleBacterialMovement()
    {
    	return false; // TODO
//        return this.worldObj.isMaterialInBB(this.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), GCMarsBlocks.bacterialSludge);
    }

	@Override
    public void onUpdate()
    {
        this.motionY += 0.06F;
        
    	super.onUpdate();
    	
    	this.fallDistance = 0;

        if (this.rand.nextFloat() < 0.8F && this.handleBacterialMovement())
        {
            this.isJumping = true;
        }
		
		if (this.handleBacterialMovement() && !this.isPotionActive(Potion.poison))
		{
			this.addPotionEffect(new PotionEffect(Potion.poison.id, 40, 0));
		}
		
		if (this.handleBacterialMovement() && !this.isPotionActive(Potion.weakness))
		{
			this.addPotionEffect(new PotionEffect(Potion.weakness.id, 40, 0));
		}
    }

	@Override
    public void moveEntityWithHeading(float par1, float par2)
    {
        super.moveEntityWithHeading(par1, par2);
		
        if (this.handleBacterialMovement())
        {
            final double var9 = this.posY;
            this.moveFlying(par1, par2, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
            this.motionY -= 0.02D;
        }
    }

	@Override
    public void fall(float var1)
    {
		;
    }
}
