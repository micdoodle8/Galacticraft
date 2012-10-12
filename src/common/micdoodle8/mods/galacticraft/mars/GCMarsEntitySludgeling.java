package micdoodle8.mods.galacticraft.mars;

import net.minecraft.src.Block;
import net.minecraft.src.BlockSilverfish;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityDamageSource;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumCreatureAttribute;
import net.minecraft.src.Facing;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class GCMarsEntitySludgeling extends EntityMob
{
    private int allySummonCooldown;

    public GCMarsEntitySludgeling(World par1World)
    {
        super(par1World);
        this.texture = "/micdoodle8/mods/galacticraft/mars/client/entities/sludgeling.png";
        this.setSize(0.3F, 0.3F);
        this.moveSpeed = 0.6F;
        this.attackStrength = 1;
    }

    public int getMaxHealth()
    {
        return 4;
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }
    
    protected Entity findPlayerToAttack()
    {
        double var1 = 8.0D;
        return this.worldObj.getClosestVulnerablePlayerToEntity(this, var1);
    }

    protected String getLivingSound()
    {
        return "mob.silverfish.say";
    }

    protected String getHurtSound()
    {
        return "mob.silverfish.hit";
    }

    protected String getDeathSound()
    {
        return "mob.silverfish.kill";
    }

    protected void attackEntity(Entity par1Entity, float par2)
    {
        if (this.attackTime <= 0 && par2 < 1.2F && par1Entity.boundingBox.maxY > this.boundingBox.minY && par1Entity.boundingBox.minY < this.boundingBox.maxY)
        {
            this.attackTime = 20;
            par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), this.attackStrength);
        }
    }

    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.worldObj.playSoundAtEntity(this, "mob.silverfish.step", 1.0F, 1.0F);
    }

    protected int getDropItemId()
    {
        return 0;
    }
    
    public void onUpdate()
    {
        this.renderYawOffset = this.rotationYaw;
        super.onUpdate();
    }

    protected boolean isValidLightLevel()
    {
        return true;
    }

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

    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }
}
