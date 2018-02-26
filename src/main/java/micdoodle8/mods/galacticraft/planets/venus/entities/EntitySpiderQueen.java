package micdoodle8.mods.galacticraft.planets.venus.entities;

import com.google.common.collect.Lists;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.entities.EntityBossBase;
import micdoodle8.mods.galacticraft.core.entities.IBoss;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.venus.VenusItems;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class EntitySpiderQueen extends EntityBossBase implements IEntityBreathable, IBoss, IRangedAttackMob
{
    private static final DataParameter<Byte> BURROWED_COUNT = EntityDataManager.createKey(EntitySpiderQueen.class, DataSerializers.BYTE);
    public boolean shouldEvade;
    private List<EntityJuicer> juicersSpawned = Lists.newArrayList();
    private List<UUID> spawnedPreload;

    private int rangedAttackTime;
    private int minRangedAttackTime;
    private int maxRangedAttackTime;

    public EntitySpiderQueen(World worldIn)
    {
        super(worldIn);
        this.setSize(1.4F, 0.9F);
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0, true));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.maxRangedAttackTime = 60;
        this.minRangedAttackTime = 20;
        this.ignoreFrustumCheck = true;
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    public double getMountedYOffset()
    {
        return (double)(this.height * 0.5F);
    }

    @Override
    protected PathNavigate createNavigator(World worldIn)
    {
        return new PathNavigateGround(this, worldIn);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(BURROWED_COUNT, (byte)-1);
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
    public void onUpdate()
    {
        super.onUpdate();

        if (!this.world.isRemote)
        {
            if (!this.shouldEvade && this.deathTicks <= 0)
            {
                EntityLivingBase attackTarget = this.getAttackTarget();

                if (attackTarget != null)
                {
                    double dX = attackTarget.posX - this.posX;
                    double dY = attackTarget.getEntityBoundingBox().minY + (double)(attackTarget.height / 3.0F) - this.posY;
                    double dZ = attackTarget.posZ - this.posZ;

                    float distance = 5.0F;
                    double d0 = this.getDistanceSq(attackTarget.posX, attackTarget.getEntityBoundingBox().minY, attackTarget.posZ);

                    this.getLookHelper().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);

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
                        this.rangedAttackTime = MathHelper.floor(f2 * (float)(this.maxRangedAttackTime - this.minRangedAttackTime) + (float)this.minRangedAttackTime);
                    }
                }
            }
        }

        if (this.spawnedPreload != null)
        {
            for (UUID id : this.spawnedPreload)
            {
                Entity entity = null;
                for (Entity e : this.world.getLoadedEntityList())
                {
                    if (e.getUniqueID().equals(id))
                    {
                        entity = e;
                        break;
                    }
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
                double dX = tarX - this.posX;
                double dY = roomBounds.maxY - this.posY;
                double dZ = tarZ - this.posZ;

                double movespeed = 1.0 * this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
                this.motionX = Math.min(Math.max(dX / 2.0F, -movespeed), movespeed);
                this.motionZ = Math.min(Math.max(dZ / 2.0F, -movespeed), movespeed);
                this.navigator.tryMoveToXYZ(tarX, this.posY, tarZ, movespeed);

                if (Math.abs(dX) < 0.1 && Math.abs(dZ) < 0.1)
                {
                    this.motionY = Math.min(dY, 0.2);

                    if (Math.abs(dY) - this.height < 1.1 && Math.abs(this.posY - this.lastTickPosY) < 0.05)
                    {
                        if (this.getBurrowedCount() >= 0)
                        {
                            if (this.ticksExisted % 20 == 0)
                            {
                                if (this.juicersSpawned.size() < 6)
                                {
                                    EntityJuicer juicer = new EntityJuicer(this.world);
                                    double angle = Math.random() * 2 * Math.PI;
                                    double dist = 3.0F;
                                    juicer.setPosition(this.posX + dist * Math.sin(angle), this.posY + 0.2F, this.posZ + dist * Math.cos(angle));
                                    juicer.setHanging(true);
                                    this.world.spawnEntity(juicer);
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
                    if (!juicer.isDead)
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
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(150.0F * ConfigManagerCore.dungeonBossHealthMod);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.250000001192092896D);
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
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.5F, 0.5F);
    }

    @Override
    protected Item getDropItem()
    {
        return Items.STRING;
    }

    @Override
    protected void dropFewItems(boolean par1, int par2)
    {
        super.dropFewItems(par1, par2);

        if (par1 && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + par2) > 0))
        {
            this.dropItem(Items.SPIDER_EYE, 1);
        }
    }

    @Override
    public void setInWeb()
    {
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    @Override
    public boolean isPotionApplicable(PotionEffect potioneffectIn)
    {
        return potioneffectIn.getPotion() != MobEffects.POISON && super.isPotionApplicable(potioneffectIn);
    }

    @Override
    public float getEyeHeight()
    {
        return 0.65F;
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source)
    {
        if (this.getBurrowedCount() >= 0)
        {
            return true;
        }

        return super.isEntityInvulnerable(source);
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
    public void attackEntityWithRangedAttack(EntityLivingBase target, float damage)
    {
        EntityWebShot entityarrow = new EntityWebShot(this.world, this, target, 0.8F, (float)(14 - this.world.getDifficulty().getDifficultyId() * 4));
        this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(entityarrow);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);

        tagCompound.setBoolean("should_evade", this.shouldEvade);

        NBTTagList list = new NBTTagList();
        for (EntityJuicer juicer : this.juicersSpawned)
        {
            list.appendTag(new NBTTagLong(juicer.getPersistentID().getMostSignificantBits()));
            list.appendTag(new NBTTagLong(juicer.getPersistentID().getLeastSignificantBits()));
        }
        tagCompound.setTag("spawned_children", list);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound)
    {
        super.readEntityFromNBT(tagCompound);

        this.shouldEvade = tagCompound.getBoolean("should_evade");

        if (tagCompound.hasKey("spawned_children"))
        {
            this.spawnedPreload = Lists.newArrayList();
            NBTTagList list = tagCompound.getTagList("spawned_children", 4);
            for (int i = 0; i < list.tagCount(); i += 2)
            {
                NBTTagLong tagMost = (NBTTagLong) list.get(i);
                NBTTagLong tagLeast = (NBTTagLong) list.get(i + 1);
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
    public EntityItem entityDropItem(ItemStack par1ItemStack, float par2)
    {
        final EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY + par2, this.posZ, par1ItemStack);
        entityitem.motionY = -2.0D;
        entityitem.setDefaultPickupDelay();
        if (this.captureDrops)
        {
            this.capturedDrops.add(entityitem);
        }
        else
        {
            this.world.spawnEntity(entityitem);
        }
        return entityitem;
    }
    
    @Override
    public void dropKey()
    {
        this.entityDropItem(new ItemStack(VenusItems.key, 1, 0), 0.5F);
    }

    @Override
    public BossInfo.Color getHealthBarColor()
    {
        return BossInfo.Color.PURPLE;
    }

    @Override
    public void setSwingingArms(boolean swingingArms)
    {
        // TODO Auto-generated method stub
        //TODO for 1.12.2
    }
}
