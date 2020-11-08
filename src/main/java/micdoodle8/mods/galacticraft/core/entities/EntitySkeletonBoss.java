package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.advancement.GCTriggers;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;
import java.util.Random;

public class EntitySkeletonBoss extends EntityBossBase implements IEntityBreathable, IRangedAttackMob, IIgnoreShift
{
    protected long ticks = 0;
    private static final ItemStack defaultHeldItem = new ItemStack(Items.BOW, 1);

    public int throwTimer;
    public int postThrowDelay = 20;
    public Entity thrownEntity;
    public Entity targetEntity;

    public EntitySkeletonBoss(EntityType<? extends EntitySkeletonBoss> type, World worldIn)
    {
        super(type, worldIn);
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new EntityAIArrowAttack(this, 1.0D, 25, 10.0F));
        this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false, true));
    }

    public static EntitySkeletonBoss create(World world)
    {
        return new EntitySkeletonBoss(GCEntities.SKELETON_BOSS, world);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
        super.setEquipmentBasedOnDifficulty(difficulty);
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
        this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.BOW));
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        double difficulty = 0;
        switch (this.world.getDifficulty())
        {
        case HARD:
            difficulty = 2D;
            break;
        case NORMAL:
            difficulty = 1D;
            break;
        }
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(150.0F * ConfigManagerCore.dungeonBossHealthMod.get());
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D + 0.075 * difficulty);
    }

    @Override
    protected void onDeathUpdate()
    {
        super.onDeathUpdate();

        if (!this.world.isRemote)
        {
            if (this.deathTicks == 100)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(PacketSimple.EnumSimplePacket.C_PLAY_SOUND_BOSS_DEATH, GCCoreUtil.getDimensionType(this.world), new Object[]{1.5F}), new PacketDistributor.TargetPoint(this.getPosX(), this.getPosY(), this.getPosZ(), 40.0D, GCCoreUtil.getDimensionType(this.world)));
            }
        }
    }

    @Override
    public boolean isInWater()
    {
        return false;
    }

    @Override
    public boolean handleWaterMovement()
    {
        return false;
    }

    @Override
    public void updatePassenger(Entity passenger)
    {
        if (this.isPassenger(passenger))
        {
            final double offsetX = Math.sin(-this.rotationYawHead / Constants.RADIANS_TO_DEGREES_D);
            final double offsetZ = Math.cos(this.rotationYawHead / Constants.RADIANS_TO_DEGREES_D);
            final double offsetY = 2 * Math.cos((this.throwTimer + this.postThrowDelay) * 0.05F);

            passenger.setPosition(this.getPosX() + offsetX, this.getPosY() + this.getMountedYOffset() + passenger.getYOffset() + offsetY, this.getPosZ() + offsetZ);
        }
    }

    @Override
    public void knockBack(Entity par1Entity, float par2, double par3, double par5)
    {
    }

    @Override
    public void onCollideWithPlayer(PlayerEntity par1EntityPlayer)
    {
        if (!this.isAIDisabled() && this.getPassengers().isEmpty() && this.postThrowDelay == 0 && this.throwTimer == 0 && par1EntityPlayer.equals(this.targetEntity) && this.deathTicks == 0)
        {
            if (!this.world.isRemote)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_PLAY_SOUND_BOSS_LAUGH, GCCoreUtil.getDimensionType(this.world), new Object[]{}), new PacketDistributor.TargetPoint(this.getPosX(), this.getPosY(), this.getPosZ(), 40.0D, GCCoreUtil.getDimensionType(this.world)));
                par1EntityPlayer.startRiding(this);
            }

            this.throwTimer = 40;
        }

        super.onCollideWithPlayer(par1EntityPlayer);
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        this.playSound(GCSounds.bossOoh, this.getSoundVolume(), this.getSoundPitch() + 1.0F);
        return null;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return null;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public ItemStack getHeldItem()
//    {
//        return EntitySkeletonBoss.defaultHeldItem;
//    }


    @Override
    public CreatureAttribute getCreatureAttribute()
    {
        return CreatureAttribute.UNDEAD;
    }

    @Override
    public void tick()
    {
        this.ticks++;

        for (int j2 = 0; j2 < world.getPlayers().size(); ++j2) //World#isAnyPlayerWithinRangeAt
        {
            PlayerEntity entityplayer = world.getPlayers().get(j2);

            if (EntityPredicates.NOT_SPECTATING.test(entityplayer) && entityplayer instanceof ServerPlayerEntity)
            {
                double d0 = entityplayer.getDistanceSq(this.getPosX(), this.getPosY(), this.getPosZ());

                if (d0 < 20 * 20)
                {
                    GCTriggers.FIND_MOON_BOSS.trigger(((ServerPlayerEntity) entityplayer));

                }
            }
        }

        if (!this.world.isRemote && this.getHealth() <= 150.0F * ConfigManagerCore.dungeonBossHealthMod.get() / 2)
        {
            this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        }

        final PlayerEntity player = this.world.getClosestPlayer(this.getPosX(), this.getPosY(), this.getPosZ(), 20.0, false);

        if (player != null && !player.equals(this.targetEntity))
        {
            if (this.getDistanceSq(player) < 400.0D)
            {
                this.getNavigator().getPathToEntity(player, 0);
                this.targetEntity = player;
            }
        }
        else
        {
            this.targetEntity = null;
        }

        if (this.throwTimer > 0)
        {
            this.throwTimer--;
        }

        if (this.postThrowDelay > 0)
        {
            this.postThrowDelay--;
        }

        if (!this.getPassengers().isEmpty() && this.throwTimer == 0)
        {
            this.postThrowDelay = 20;

            this.thrownEntity = this.getPassengers().get(0);

            if (!this.world.isRemote)
            {
                this.removePassengers();
            }
        }

        if (this.thrownEntity != null && this.postThrowDelay == 18)
        {
            double d0 = this.getPosX() - this.thrownEntity.getPosX();
            double d1;

            for (d1 = this.getPosZ() - this.thrownEntity.getPosZ(); d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D)
            {
                d0 = (Math.random() - Math.random()) * 0.01D;
            }


            if (!this.world.isRemote)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_PLAY_SOUND_BOW, GCCoreUtil.getDimensionType(this.world), new Object[]{}), new PacketDistributor.TargetPoint(this.getPosX(), this.getPosY(), this.getPosZ(), 40.0, GCCoreUtil.getDimensionType(this.world)));
            }
            ((PlayerEntity) this.thrownEntity).attackedAtYaw = (float) Math.atan2(d1, d0) * Constants.RADIANS_TO_DEGREES - this.rotationYaw;

            this.thrownEntity.isAirBorne = true;
            final float f = MathHelper.sqrt(d0 * d0 + d1 * d1);
            final float f1 = 2.4F;
            this.thrownEntity.setMotion(this.thrownEntity.getMotion().mul(0.5, 0.5, 0.5));
            this.thrownEntity.setMotion(this.thrownEntity.getMotion().add(-d0 / f * f1, f1 / 5.0, -d1 / f * f1));

            if (this.thrownEntity.getMotion().y > 0.4000000059604645D)
            {
                this.thrownEntity.setMotion(this.getMotion().x, 0.4000000059604645D, this.getMotion().z);
            }
        }

        super.tick();
    }

    @Override
    public ItemEntity entityDropItem(ItemStack par1ItemStack, float par2)
    {
        final ItemEntity entityitem = new ItemEntity(this.world, this.getPosX(), this.getPosY() + par2, this.getPosZ(), par1ItemStack);
        entityitem.setMotion(entityitem.getMotion().x, -2.0, entityitem.getMotion().z);
        entityitem.setDefaultPickupDelay();
        if (captureDrops() != null)
        {
            this.captureDrops().add(entityitem);
        }
        else
        {
            this.world.addEntity(entityitem);
        }
        return entityitem;
    }

//    @Override
//    protected void dropFewItems(boolean b, int i)
//    {
//        if (this.rand.nextInt(200) - i >= 5)
//        {
//            return;
//        }
//
//        if (i > 0)
//        {
//            final ItemStack var2 = new ItemStack(Items.BOW);
//            EnchantmentHelper.addRandomEnchantment(this.rand, var2, 5, false);
//            this.entityDropItem(var2, 0.0F);
//        }
//        else
//        {
//            this.dropItem(Items.BOW, 1);
//        }
//    } TODO Item drops

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float f)
    {
        if (!this.getPassengers().isEmpty())
        {
            return;
        }

        ArrowEntity arrow = new ArrowEntity(this.world, this);
        double d0 = target.getPosX() - this.getPosX();
        double d1 = target.getBoundingBox().minY + (double) (target.getHeight() / 3.0F) - arrow.getPosY();
        double d2 = target.getPosZ() - this.getPosZ();
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        arrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float) (14 - this.world.getDifficulty().getId() * 4));

        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.addEntity(arrow);
    }

    @Override
    public boolean shouldIgnoreShiftExit()
    {
        return true;
    }

    @Override
    public ItemStack getGuaranteedLoot(Random rand)
    {
        List<ItemStack> stackList = GalacticraftRegistry.getDungeonLoot(1);
        return stackList.get(rand.nextInt(stackList.size())).copy();
    }

    @Override
    public int getChestTier()
    {
        return 1;
    }

    @Override
    public void dropKey()
    {
        this.entityDropItem(new ItemStack(GCItems.key, 1), 0.5F);
    }

    @Override
    public BossInfo.Color getHealthBarColor()
    {
        return BossInfo.Color.GREEN;
    }

//    @Override
//    public void setSwingingArms(boolean swingingArms)
//    {
//        // TODO Auto-generated method stub
//        //Unused in this Galacticraft entity
//    }
}
