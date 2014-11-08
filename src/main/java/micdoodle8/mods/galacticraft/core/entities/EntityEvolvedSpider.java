package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.world.World;

public class EntityEvolvedSpider extends EntitySpider implements IEntityBreathable
{
    public EntityEvolvedSpider(World par1World)
    {
        super(par1World);
        this.setSize(1.4F, 0.9F);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(22.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(1.0F);
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
    protected boolean isAIEnabled()
    {
        return false;
    }
    
    @Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData livingData)
	{
		livingData = super.onSpawnWithEgg(livingData);

		if (this.worldObj.rand.nextInt(100) == 0)
		{
			EntityEvolvedSkeleton skeleton = new EntityEvolvedSkeleton(this.worldObj);
			skeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
			skeleton.onSpawnWithEgg(null);
			this.worldObj.spawnEntityInWorld(skeleton);
			skeleton.mountEntity(this);
		}
		return livingData;
	}
}
