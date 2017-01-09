package micdoodle8.mods.galacticraft.planets.venus.entities;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.entities.IBoss;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class EntitySpiderQueen extends EntityMob implements IEntityBreathable, IBoss, IBossDisplayData, IRangedAttackMob
{
    private Vector3 roomCoords;
    private Vector3 roomSize;
    public int entitiesWithin;
    public int entitiesWithinLast;
    private TileEntityDungeonSpawner spawner;
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
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0, true));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.maxRangedAttackTime = 60;
        this.minRangedAttackTime = 20;
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
    protected PathNavigate getNewNavigator(World worldIn)
    {
        return new PathNavigateGround(this, worldIn);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte)-1);
    }

    public byte getBurrowedCount()
    {
        return this.dataWatcher.getWatchableObjectByte(16);
    }

    public void setBurrowedCount(byte count)
    {
        this.dataWatcher.updateObject(16, count);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.roomCoords != null && this.roomSize != null)
        {
            List<EntityPlayer> playersWithin = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class,
                    AxisAlignedBB.fromBounds(
                            this.roomCoords.intX(),
                            this.roomCoords.intY(),
                            this.roomCoords.intZ(),
                            this.roomCoords.intX() + this.roomSize.intX(),
                            this.roomCoords.intY() + this.roomSize.intY(),
                            this.roomCoords.intZ() + this.roomSize.intZ()));

            this.entitiesWithin = playersWithin.size();

            if (this.entitiesWithin == 0 && this.entitiesWithinLast != 0)
            {
                List<EntityPlayer> entitiesWithin2 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.fromBounds(this.roomCoords.intX() - 11, this.roomCoords.intY() - 11, this.roomCoords.intZ() - 11, this.roomCoords.intX() + this.roomSize.intX() + 10, this.roomCoords.intY() + this.roomSize.intY() + 10, this.roomCoords.intZ() + this.roomSize.intZ() + 10));

                for (EntityPlayer p : entitiesWithin2)
                {
                    p.addChatMessage(new ChatComponentText(GCCoreUtil.translate("gui.skeleton_boss.message")));
                }

                this.setDead();

                if (this.spawner != null)
                {
                    this.spawner.playerCheated = true;
                }

                return;
            }

            this.entitiesWithinLast = this.entitiesWithin;
        }

        if (!this.worldObj.isRemote)
        {
            if (!this.shouldEvade)
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
                            float f = MathHelper.sqrt_double(d0) / distance;
                            this.attackEntityWithRangedAttack(attackTarget, 0.0F);
                            this.rangedAttackTime = MathHelper.floor_float(f * (float) (this.maxRangedAttackTime - this.minRangedAttackTime) + (float) this.minRangedAttackTime);
                        }
                    }
                    else if (this.rangedAttackTime < 0)
                    {
                        float f2 = MathHelper.sqrt_double(d0) / distance;
                        this.rangedAttackTime = MathHelper.floor_float(f2 * (float)(this.maxRangedAttackTime - this.minRangedAttackTime) + (float)this.minRangedAttackTime);
                    }
                }
            }
        }

        if (this.spawnedPreload != null)
        {
            for (UUID id : this.spawnedPreload)
            {
                Entity entity = null;
                for (Entity e : this.worldObj.getLoadedEntityList())
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

        if (!this.worldObj.isRemote && this.shouldEvade)
        {
            if (this.roomCoords != null && this.roomSize != null)
            {
                double tarX = this.roomCoords.x + this.roomSize.x / 2.0;
                double tarZ = this.roomCoords.z + this.roomSize.z / 2.0;
                double dX = tarX - this.posX;
                double dY = (this.roomCoords.y + this.roomSize.y) - this.posY;
                double dZ = tarZ - this.posZ;

                double movespeed = 1.0 * this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
                this.motionX = Math.min(Math.max(dX / 10.0F, -movespeed), movespeed);
                this.motionZ = Math.min(Math.max(dZ / 10.0F, -movespeed), movespeed);
                this.navigator.tryMoveToXYZ(tarX, this.posY, tarZ, movespeed);

                if (Math.abs(dX) < 0.1 && Math.abs(dZ) < 0.1)
                {
                    this.motionY = Math.min(dY, 0.1);

                    if (Math.abs(dY) - this.height < 1.1)
                    {
                        if (this.getBurrowedCount() >= 0)
                        {
                            if (this.ticksExisted % 20 == 0)
                            {
                                if (this.juicersSpawned.size() < 6)
                                {
                                    EntityJuicer juicer = new EntityJuicer(this.worldObj);
                                    double angle = Math.random() * 2 * Math.PI;
                                    double dist = 3.0F;
                                    juicer.setPosition(this.posX + dist * Math.sin(angle), this.posY + 0.2F, this.posZ + dist * Math.cos(angle));
                                    juicer.setHanging(true);
                                    this.worldObj.spawnEntityInWorld(juicer);
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
    public void knockBack(Entity entityIn, float par2, double par3, double par4)
    {
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(150.0F * ConfigManagerCore.dungeonBossHealthMod);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.250000001192092896D);
    }

    @Override
    protected float getSoundPitch()
    {
        return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F + 0.75F;
    }

    @Override
    protected String getLivingSound()
    {
        return "mob.spider.say";
    }

    @Override
    protected String getHurtSound()
    {
        return "mob.spider.say";
    }

    @Override
    protected String getDeathSound()
    {
        return "mob.spider.death";
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound("mob.spider.step", 0.15F, 0.7F);
    }

    @Override
    protected Item getDropItem()
    {
        return Items.string;
    }

    @Override
    protected void dropFewItems(boolean par1, int par2)
    {
        super.dropFewItems(par1, par2);

        if (par1 && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + par2) > 0))
        {
            this.dropItem(Items.spider_eye, 1);
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
        return potioneffectIn.getPotionID() != Potion.poison.id && super.isPotionApplicable(potioneffectIn);
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
    public void setRoom(Vector3 roomCoords, Vector3 roomSize)
    {
        this.roomCoords = roomCoords;
        this.roomSize = roomSize;
    }

    @Override
    public void onBossSpawned(TileEntityDungeonSpawner spawner)
    {
        this.spawner = spawner;
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
        EntityWebShot entityarrow = new EntityWebShot(this.worldObj, this, target, 0.8F, (float)(14 - this.worldObj.getDifficulty().getDifficultyId() * 4));
        this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.worldObj.spawnEntityInWorld(entityarrow);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);

        if (this.roomCoords != null && this.roomSize != null)
        {
            tagCompound.setDouble("room_x", this.roomCoords.x);
            tagCompound.setDouble("room_y", this.roomCoords.y);
            tagCompound.setDouble("room_z", this.roomCoords.z);
            tagCompound.setDouble("room_size_x", this.roomSize.x);
            tagCompound.setDouble("room_size_y", this.roomSize.y);
            tagCompound.setDouble("room_size_z", this.roomSize.z);
        }

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

        if (tagCompound.hasKey("room_x"))
        {
            this.roomCoords = new Vector3(tagCompound.getDouble("room_x"), tagCompound.getDouble("room_y"), tagCompound.getDouble("room_z"));
            this.roomSize = new Vector3(tagCompound.getDouble("room_size_x"), tagCompound.getDouble("room_size_y"), tagCompound.getDouble("room_size_z"));
        }

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
}