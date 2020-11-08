package micdoodle8.mods.galacticraft.planets.venus.entities;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.entities.EntityBossBase;
import micdoodle8.mods.galacticraft.core.entities.IBoss;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCreeperBoss;
import micdoodle8.mods.galacticraft.planets.mars.entities.MarsEntities;
import micdoodle8.mods.galacticraft.planets.venus.items.VenusItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class EntitySpiderQueen extends EntityBossBase implements IEntityBreathable, IBoss, IRangedAttackMob
{
    private static final DataParameter<Byte> BURROWED_COUNT = EntityDataManager.createKey(EntitySpiderQueen.class, DataSerializers.BYTE);
    public boolean shouldEvade;
    private final List<EntityJuicer> juicersSpawned = Lists.newArrayList();
    private List<UUID> spawnedPreload;

    private int rangedAttackTime;
    private final int minRangedAttackTime;
    private final int maxRangedAttackTime;

    public EntitySpiderQueen(EntityType<? extends EntitySpiderQueen> type, World worldIn)
    {
        super(type, worldIn);
//        this.setSize(1.4F, 0.9F);
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(5, new RandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.maxRangedAttackTime = 60;
        this.minRangedAttackTime = 20;
        this.ignoreFrustumCheck = true;
    }

    public static EntitySpiderQueen create(World world)
    {
        return new EntitySpiderQueen(VenusEntities.SPIDER_QUEEN, world);
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    public double getMountedYOffset()
    {
        return this.getHeight() * 0.5F;
    }

    @Override
    protected PathNavigator createNavigator(World worldIn)
    {
        return new GroundPathNavigator(this, worldIn);
    }

    @Override
    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(BURROWED_COUNT, (byte) -1);
    }

    public byte getBurrowedCount()
    {
        return this.dataManager.get(BURROWED_COUNT);
    }

    public void setBurrowedCount(byte count)
    {
        this.dataManager.set(BURROWED_COUNT, count);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.world.isRemote)
        {
            if (!this.shouldEvade && this.deathTicks <= 0)
            {
                LivingEntity attackTarget = this.getAttackTarget();

                if (attackTarget != null)
                {
                    double dX = attackTarget.getPosX() - this.getPosX();
                    double dY = attackTarget.getBoundingBox().minY + (double) (attackTarget.getHeight() / 3.0F) - this.getPosY();
                    double dZ = attackTarget.getPosZ() - this.getPosZ();

                    float distance = 5.0F;
                    double d0 = this.getDistanceSq(attackTarget.getPosX(), attackTarget.getBoundingBox().minY, attackTarget.getPosZ());

                    this.getLookController().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);

                    if (--this.rangedAttackTime == 0)
                    {
                        if (dX * dX + dY * dY + dZ * dZ > distance * distance)
                        {
                            float f = MathHelper.sqrt(d0) / distance;
                            this.attackEntityWithRangedAttack(attackTarget, 0.0F);
                            this.rangedAttackTime = MathHelper.floor(f * (float) (this.maxRangedAttackTime - this.minRangedAttackTime) + (float) this.minRangedAttackTime);
                        }
                    }
                    else if (this.rangedAttackTime < 0)
                    {
                        float f2 = MathHelper.sqrt(d0) / distance;
                        this.rangedAttackTime = MathHelper.floor(f2 * (float) (this.maxRangedAttackTime - this.minRangedAttackTime) + (float) this.minRangedAttackTime);
                    }
                }
            }
        }

        if (this.spawnedPreload != null)
        {
            for (UUID id : this.spawnedPreload)
            {
                Entity entity = null;
                Optional<Entity> first = ((ServerWorld) this.world).getEntities().filter((e) -> e.getUniqueID().equals(id)).findFirst();
                if (first.isPresent())
                {
                    entity = first.get();
                }
                if (entity instanceof EntityJuicer)
                {
                    this.juicersSpawned.add((EntityJuicer) entity);
                }
            }
            if (this.juicersSpawned.size() == this.spawnedPreload.size())
            {
                this.spawnedPreload.clear();
                this.spawnedPreload = null;
            }
        }

        if (!this.world.isRemote && this.shouldEvade)
        {
            if (this.spawner != null)
            {
                AxisAlignedBB roomBounds = this.spawner.getRangeBounds();
                double tarX = (roomBounds.minX + roomBounds.maxX) / 2.0;
                double tarZ = (roomBounds.minZ + roomBounds.maxZ) / 2.0;
                double dX = tarX - this.getPosX();
                double dY = roomBounds.maxY - this.getPosY();
                double dZ = tarZ - this.getPosZ();

                double movespeed = 1.0 * this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
                this.setMotion(Math.min(Math.max(dX / 2.0F, -movespeed), movespeed), this.getMotion().y, Math.min(Math.max(dZ / 2.0F, -movespeed), movespeed));
                this.navigator.tryMoveToXYZ(tarX, this.getPosY(), tarZ, movespeed);

                if (Math.abs(dX) < 0.1 && Math.abs(dZ) < 0.1)
                {
                    this.setMotion(this.getMotion().x, Math.min(dY, 0.2), this.getMotion().z);

                    if (Math.abs(dY) - this.getHeight() < 1.1 && Math.abs(this.getPosY() - this.lastTickPosY) < 0.05)
                    {
                        if (this.getBurrowedCount() >= 0)
                        {
                            if (this.ticksExisted % 20 == 0)
                            {
                                if (this.juicersSpawned.size() < 6)
                                {
                                    EntityJuicer juicer = new EntityJuicer(VenusEntities.JUICER, this.world);
                                    double angle = Math.random() * 2 * Math.PI;
                                    double dist = 3.0F;
                                    juicer.setPosition(this.getPosX() + dist * Math.sin(angle), this.getPosY() + 0.2F, this.getPosZ() + dist * Math.cos(angle));
                                    juicer.setHanging(true);
                                    this.world.addEntity(juicer);
                                    this.juicersSpawned.add(juicer);
                                }
                            }

                            if (this.getBurrowedCount() < 20)
                            {
                                this.setBurrowedCount((byte) (this.getBurrowedCount() + 1));
                            }
                        }
                        else
                        {
                            this.setBurrowedCount((byte) 0);
                        }
                    }
                }
            }

            if (!this.juicersSpawned.isEmpty())
            {
                boolean allDead = true;
                for (EntityJuicer juicer : this.juicersSpawned)
                {
                    if (juicer.isAlive())
                    {
                        allDead = false;
                    }
                }
                if (allDead)
                {
                    this.juicersSpawned.clear();
                    this.shouldEvade = false;
                    this.setBurrowedCount((byte) -1);
                }
            }
        }
    }

    @Override
    public ItemStack getGuaranteedLoot(Random rand)
    {
        List<ItemStack> stackList = GalacticraftRegistry.getDungeonLoot(3);
        return stackList.get(rand.nextInt(stackList.size())).copy();
    }

    @Override
    public void knockBack(Entity entityIn, float par2, double par3, double par4)
    {
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(150.0F * ConfigManagerCore.dungeonBossHealthMod.get());
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.250000001192092896D);
    }

    @Override
    protected float getSoundPitch()
    {
        return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F + 0.4F;
    }

    @Override
    protected float getSoundVolume()
    {
        return 5.0F;
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SPIDER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn)
    {
        this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.5F, 0.5F);
    }

//    @Override
//    protected Item getDropItem()
//    {
//        return Items.STRING;
//    }
//
//    @Override
//    protected void dropFewItems(boolean par1, int par2)
//    {
//        super.dropFewItems(par1, par2);
//
//        if (par1 && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + par2) > 0))
//        {
//            this.dropItem(Items.SPIDER_EYE, 1);
//        }
//    }

//    @Override
//    public EnumCreatureAttribute getCreatureAttribute()
//    {
//        return EnumCreatureAttribute.ARTHROPOD;
//    }

    @Override
    public boolean isPotionApplicable(EffectInstance potioneffectIn)
    {
        return potioneffectIn.getPotion() != Effects.POISON && super.isPotionApplicable(potioneffectIn);
    }

    @Override
    public float getEyeHeight(Pose p_213307_1_)
    {
        return 0.65F;
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source)
    {
        if (this.getBurrowedCount() >= 0)
        {
            return true;
        }

        return super.isInvulnerableTo(source);
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount)
    {
        float healthLast = this.getHealth();
        super.damageEntity(damageSrc, damageAmount);
        float health = this.getHealth();

        float thirdHealth = this.getMaxHealth() / 3.0F;

        if (health < thirdHealth && healthLast >= thirdHealth)
        {
            shouldEvade = true;
        }
        else if (health < 2 * thirdHealth && healthLast >= 2 * thirdHealth)
        {
            shouldEvade = true;
        }
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float damage)
    {
        EntityWebShot entityarrow = EntityWebShot.createEntityWebShot(this.world, this, target, 0.8F, (float) (14 - this.world.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.addEntity(entityarrow);
    }

    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        super.writeAdditional(nbt);

        nbt.putBoolean("should_evade", this.shouldEvade);

        ListNBT list = new ListNBT();
        for (EntityJuicer juicer : this.juicersSpawned)
        {
            list.add(LongNBT.valueOf(juicer.getUniqueID().getMostSignificantBits()));
            list.add(LongNBT.valueOf(juicer.getUniqueID().getLeastSignificantBits()));
        }
        nbt.put("spawned_children", list);
    }

    @Override
    public void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);

        this.shouldEvade = nbt.getBoolean("should_evade");

        if (nbt.contains("spawned_children"))
        {
            this.spawnedPreload = Lists.newArrayList();
            ListNBT list = nbt.getList("spawned_children", 4);
            for (int i = 0; i < list.size(); i += 2)
            {
                LongNBT tagMost = (LongNBT) list.get(i);
                LongNBT tagLeast = (LongNBT) list.get(i + 1);
                this.spawnedPreload.add(new UUID(tagMost.getLong(), tagLeast.getLong()));
            }
        }
    }

    @Override
    public int getChestTier()
    {
        return 3;
    }

    @Override
    public ItemEntity entityDropItem(ItemStack par1ItemStack, float par2)
    {
        final ItemEntity entityitem = new ItemEntity(this.world, this.getPosX(), this.getPosY() + par2, this.getPosZ(), par1ItemStack);
        entityitem.setMotion(entityitem.getMotion().x, -2.0, entityitem.getMotion().z);
        entityitem.setDefaultPickupDelay();
        if (this.captureDrops() != null)
        {
            this.captureDrops().add(entityitem);
        }
        else
        {
            this.world.addEntity(entityitem);
        }
        return entityitem;
    }

    @Override
    public void dropKey()
    {
        this.entityDropItem(new ItemStack(VenusItems.keyT3, 1), 0.5F);
    }

    @Override
    public BossInfo.Color getHealthBarColor()
    {
        return BossInfo.Color.PURPLE;
    }

//    @Override
//    public void setSwingingArms(boolean swingingArms)
//    {
//    }
}
