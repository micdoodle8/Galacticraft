package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.core.GCCoreEntityMob;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAIAttackOnCollide;
import net.minecraft.src.EntityAINearestAttackableTarget;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumCreatureAttribute;
import net.minecraft.src.World;

public class GCMarsEntitySludgeling extends EntityMob
{
    private int allySummonCooldown;

    public GCMarsEntitySludgeling(World par1World)
    {
        super(par1World);
        this.texture = "/micdoodle8/mods/galacticraft/mars/client/entities/sludgeling.png";
        this.setSize(0.2F, 0.2F);
        this.moveSpeed = 0.6F;
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 0.25F, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, GCCoreEntityMob.class, 16.0F, 0, false, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, false, true));
    }

    @Override
	public int getMaxHealth()
    {
        return 4;
    }

    @Override
	public boolean isAIEnabled()
    {
        return true;
    }
    
    @Override
	protected boolean canTriggerWalking()
    {
        return false;
    }
    
    @Override
	protected Entity findPlayerToAttack()
    {
        double var1 = 8.0D;
        return this.worldObj.getClosestVulnerablePlayerToEntity(this, var1);
    }

    @Override
	protected String getLivingSound()
    {
        return "mob.silverfish.say";
    }

    @Override
	protected String getHurtSound()
    {
        return "mob.silverfish.hit";
    }

    @Override
	protected String getDeathSound()
    {
        return "mob.silverfish.kill";
    }
    
    public EntityPlayer getClosestEntityToAttack(double par1, double par3, double par5, double par7)
    {
        double var9 = -1.0D;
        EntityPlayer var11 = null;

        for (int var12 = 0; var12 < this.worldObj.loadedEntityList.size(); ++var12)
        {
            EntityPlayer var13 = (EntityPlayer)this.worldObj.loadedEntityList.get(var12);
            double var14 = var13.getDistanceSq(par1, par3, par5);

            if ((par7 < 0.0D || var14 < par7 * par7) && (var9 == -1.0D || var14 < var9))
            {
                var9 = var14;
                var11 = var13;
            }
        }

        return var11;
    }

    @Override
	protected void attackEntity(Entity par1Entity, float par2)
    {
        if (this.attackTime <= 0 && par2 < 1.2F && par1Entity.boundingBox.maxY > this.boundingBox.minY && par1Entity.boundingBox.minY < this.boundingBox.maxY)
        {
            this.attackTime = 20;
            par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this),  this.func_82193_c(par1Entity));
        }
    }

    @Override
	protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.worldObj.playSoundAtEntity(this, "mob.silverfish.step", 1.0F, 1.0F);
    }

    @Override
	protected int getDropItemId()
    {
        return 0;
    }
    
    @Override
	public void onUpdate()
    {
        this.renderYawOffset = this.rotationYaw;
        super.onUpdate();
    }

    @Override
	protected boolean isValidLightLevel()
    {
        return true;
    }

    @Override
	public boolean getCanSpawnHere()
    {
        if (super.getCanSpawnHere())
        {
            EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
            return var1 == null;
        }
        else
        {
            return false;
        }
    }

    @Override
	public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }
}
