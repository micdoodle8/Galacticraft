package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.EnumDifficulty;
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
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(ConfigManagerCore.hardMode ? 1.2F : 1.0F);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(ConfigManagerCore.hardMode ? 4.0D : 2.0D);
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

        if (livingData == null)
        {
            livingData = new EntityEvolvedSpider.GroupData();

            if (this.worldObj.difficultySetting == EnumDifficulty.HARD && this.worldObj.rand.nextFloat() < 0.1F * this.worldObj.func_147462_b(this.posX, this.posY, this.posZ))
            {
                ((EntityEvolvedSpider.GroupData)livingData).func_111104_a(this.worldObj.rand);
            }
        }

        if (livingData instanceof EntityEvolvedSpider.GroupData)
        {
            int i = ((EntityEvolvedSpider.GroupData)livingData).field_111105_a;

            if (i > 0 && Potion.potionTypes[i] != null)
            {
                this.addPotionEffect(new PotionEffect(i, Integer.MAX_VALUE));
            }
        }

		return livingData;
	}
}
