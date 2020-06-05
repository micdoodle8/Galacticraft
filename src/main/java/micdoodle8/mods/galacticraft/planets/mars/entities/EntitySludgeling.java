package micdoodle8.mods.galacticraft.planets.mars.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSkeleton;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSpider;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntitySludgeling extends MonsterEntity implements IEntityBreathable
{
    public EntitySludgeling(World par1World)
    {
        super(par1World);
        this.setSize(0.3F, 0.2F);
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();
        this.tasks.addTask(1, new MeleeAttackGoal(this, 0.25F, true));
        this.tasks.addTask(4, new MeleeAttackGoal(this, 1.0D, false));
        this.targetTasks.addTask(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 0, false, true, null));
        this.targetTasks.addTask(2, new NearestAttackableTargetGoal<>(this, EntityEvolvedZombie.class, 0, false, true, null));
        this.targetTasks.addTask(2, new NearestAttackableTargetGoal<>(this, EntityEvolvedSkeleton.class, 0, false, true, null));
        this.targetTasks.addTask(2, new NearestAttackableTargetGoal<>(this, EntityEvolvedSpider.class, 0, false, true, null));
        this.targetTasks.addTask(2, new NearestAttackableTargetGoal<>(this, EntityEvolvedCreeper.class, 0, false, true, null));
        this.targetTasks.addTask(3, new NearestAttackableTargetGoal<>(this, EntitySlimeling.class, 200, false, true, null));
    }

    @Override
    public boolean canBreatheUnderwater()
    {
        return true;
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(7.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.0F);
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_SILVERFISH_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_SILVERFISH_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SILVERFISH_DEATH;
    }

    public PlayerEntity getClosestEntityToAttack(double par1, double par3, double par5, double par7)
    {
        double var9 = -1.0D;
        PlayerEntity var11 = null;

        for (int var12 = 0; var12 < this.world.loadedEntityList.size(); ++var12)
        {
            PlayerEntity var13 = (PlayerEntity) this.world.loadedEntityList.get(var12);
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
    protected void playStepSound(BlockPos pos, Block block)
    {
        this.playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.15F, 1.0F);
    }

    @Override
    protected Item getDropItem()
    {
        return Item.getItemFromBlock(Blocks.AIR);
    }

    @Override
    public void tick()
    {
        this.renderYawOffset = this.rotationYaw;
        super.tick();
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
            PlayerEntity var1 = this.world.getClosestPlayerToEntity(this, 5.0D);
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

    @Override
    public boolean canBreath()
    {
        return true;
    }
}
