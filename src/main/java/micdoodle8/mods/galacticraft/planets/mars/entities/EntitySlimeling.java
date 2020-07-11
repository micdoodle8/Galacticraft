package micdoodle8.mods.galacticraft.planets.mars.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.planets.mars.MarsModuleClient;
import micdoodle8.mods.galacticraft.planets.mars.inventory.InventorySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.EnumSet;
import java.util.UUID;

public class EntitySlimeling extends TameableEntity implements IEntityBreathable
{
    public InventorySlimeling slimelingInventory = new InventorySlimeling(this);

    private static final DataParameter<Float> HEALTH = EntityDataManager.createKey(EntitySlimeling.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> COLOR_RED = EntityDataManager.createKey(EntitySlimeling.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> COLOR_GREEN = EntityDataManager.createKey(EntitySlimeling.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> COLOR_BLUE = EntityDataManager.createKey(EntitySlimeling.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> AGE = EntityDataManager.createKey(EntitySlimeling.class, DataSerializers.VARINT);
    private static final DataParameter<String> NAME = EntityDataManager.createKey(EntitySlimeling.class, DataSerializers.STRING);
    private static final DataParameter<Integer> FAV_FOOD_ID = EntityDataManager.createKey(EntitySlimeling.class, DataSerializers.VARINT);
    private static final DataParameter<Float> ATTACK_DAMAGE = EntityDataManager.createKey(EntitySlimeling.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> KILLS = EntityDataManager.createKey(EntitySlimeling.class, DataSerializers.VARINT);

    public float colorRed;
    public float colorGreen;
    public float colorBlue;
    public long ticksAlive;
    public int age = 0;
    public final int MAX_AGE = 100000;
    public String slimelingName = GCCoreUtil.translate("gui.message.unnamed.name");
    public int favFoodID = 1;
    public float attackDamage = 0.05F;
    public int kills;

    public EntitySlimeling(EntityType<? extends EntitySlimeling> type, World worldIn)
    {
        super(type, worldIn);
//        this.setSize(0.45F, 0.7F);
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.sitGoal = new EntityAISitGC(this);
        this.goalSelector.addGoal(2, this.sitGoal);
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(6, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(9, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NonTamedTargetGoal(this, EntitySludgeling.class, false, p_apply_1_ -> p_apply_1_ instanceof EntitySludgeling));
        this.setTamed(false);

        switch (this.rand.nextInt(3))
        {
        case 0:
            this.colorRed = 1.0F;
            break;
        case 1:
            this.colorBlue = 1.0F;
            break;
        case 2:
            this.colorRed = 1.0F;
            this.colorGreen = 1.0F;
            break;
        }

        this.setRandomFavFood();
    }

    public static EntitySlimeling createEntitySlimeling(World worldIn, float r, float g, float b)
    {
        EntitySlimeling slimeling = new EntitySlimeling(MarsEntities.SLIMELING.get(), worldIn);
        slimeling.colorRed = r;
        slimeling.colorGreen = g;
        slimeling.colorBlue = b;
        return slimeling;
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return new SSpawnObjectPacket(this);
    }

    @Override
    public LivingEntity getOwner()
    {
        LivingEntity owner = super.getOwner();
        if (owner == null)
        {
            UUID ownerId = getOwnerId();
            if (ownerId != null)
            {
                return this.world.getPlayerByUuid(ownerId);
            }
        }
        return owner;
    }

    @Override
    public boolean isOwner(LivingEntity entityLivingBase)
    {
        return entityLivingBase == this.getOwner();
    }

    @Override
    public boolean canBreatheUnderwater()
    {
        return true;
    }

    public float getSlimelingSize()
    {
        return this.getScale() * 2.0F;
    }

//    @Override
//    public void setScaleForAge(boolean par1)
//    {
//        this.setScale(this.getSlimelingSize());
//    }

    @Override
    public boolean isChild()
    {
        return this.getAge() / (float) this.MAX_AGE < 0.33F;
    }

    private void setRandomFavFood()
    {
        switch (this.rand.nextInt(10))
        {
        case 0:
            this.favFoodID = Item.getIdFromItem(Items.GOLD_INGOT);
            break;
        case 1:
            this.favFoodID = Item.getIdFromItem(Items.FLINT_AND_STEEL);
            break;
        case 2:
            this.favFoodID = Item.getIdFromItem(Items.BAKED_POTATO);
            break;
        case 3:
            this.favFoodID = Item.getIdFromItem(Items.STONE_SWORD);
            break;
        case 4:
            this.favFoodID = Item.getIdFromItem(Items.GUNPOWDER);
            break;
        case 5:
            this.favFoodID = Item.getIdFromItem(Items.WOODEN_HOE);
            break;
        case 6:
            this.favFoodID = Item.getIdFromItem(Items.EMERALD);
            break;
        case 7:
            this.favFoodID = Item.getIdFromItem(Items.TROPICAL_FISH);
            break;
        case 8:
            this.favFoodID = Item.getIdFromItem(Items.REPEATER);
            break;
        case 9:
            this.favFoodID = Item.getIdFromItem(Items.OAK_BOAT);
            break;
        }
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getMaxHealthSlimeling());
    }

//    @Override
//    public boolean isAIEnabled()
//    {
//        return true;
//    }


    @Override
    protected void updateAITasks()
    {
        this.dataManager.set(HEALTH, this.getHealth());
    }

    @Override
    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(HEALTH, this.getHealth());
        this.dataManager.register(COLOR_RED, this.colorRed);
        this.dataManager.register(COLOR_GREEN, this.colorGreen);
        this.dataManager.register(COLOR_BLUE, this.colorBlue);
        this.dataManager.register(AGE, this.age);
        this.dataManager.register(NAME, "");
        this.dataManager.register(FAV_FOOD_ID, this.favFoodID);
        this.dataManager.register(ATTACK_DAMAGE, this.attackDamage);
        this.dataManager.register(KILLS, this.kills);
        this.setSlimelingName(GCCoreUtil.translate("gui.message.unnamed.name"));
    }

    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        super.writeAdditional(nbt);
        nbt.put("SlimelingInventory", this.slimelingInventory.writeToNBT(new ListNBT()));
        nbt.putFloat("SlimeRed", this.colorRed);
        nbt.putFloat("SlimeGreen", this.colorGreen);
        nbt.putFloat("SlimeBlue", this.colorBlue);
        nbt.putInt("SlimelingAge", this.age);
        nbt.putString("SlimelingName", this.slimelingName);
        nbt.putInt("FavFoodID", this.favFoodID);
        nbt.putFloat("SlimelingDamage", this.attackDamage);
        nbt.putInt("SlimelingKills", this.kills);
    }

    @Override
    public void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);
        this.slimelingInventory.readFromNBT(nbt.getList("SlimelingInventory", 10));
        this.colorRed = nbt.getFloat("SlimeRed");
        this.colorGreen = nbt.getFloat("SlimeGreen");
        this.colorBlue = nbt.getFloat("SlimeBlue");
        this.age = nbt.getInt("SlimelingAge");
        this.slimelingName = nbt.getString("SlimelingName");
        this.favFoodID = nbt.getInt("FavFoodID");
        this.attackDamage = nbt.getFloat("SlimelingDamage");
        this.kills = nbt.getInt("SlimelingKills");
        this.setColorRed(this.colorRed);
        this.setColorGreen(this.colorGreen);
        this.setColorBlue(this.colorBlue);
        this.setAge(this.age);
        this.setSlimelingName(this.slimelingName);
        this.setKillCount(this.kills);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        this.playSound(SoundEvents.BLOCK_SLIME_BLOCK_STEP, this.getSoundVolume(), 1.1F);
        return null;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        this.playSound(GCSounds.slimeDeath, this.getSoundVolume(), 0.8F);
        return null;
    }

//    @Override
//    protected Item getDropItem()
//    {
//        return Items.SLIME_BALL;
//    }

    @Override
    public void livingTick()
    {
        super.livingTick();

        if (!this.world.isRemote)
        {
            if (this.ticksAlive <= 0)
            {
                this.setColorRed(this.colorRed);
                this.setColorGreen(this.colorGreen);
                this.setColorBlue(this.colorBlue);
            }

            this.ticksAlive++;

            if (this.ticksAlive % 2 == 0)
            {
                if (this.age < this.MAX_AGE)
                {
                    this.age++;
                }

                this.setAge(Math.min(this.age, this.MAX_AGE));
            }

            this.setFavoriteFood(this.favFoodID);
            this.setAttackDamage(this.attackDamage);
            this.setKillCount(this.kills);
//            this.setCargoSlot(this.slimelingInventory.getStackInSlot(1));
        }

        if (!this.world.isRemote)
        {
            this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getMaxHealthSlimeling());

            if (this.getOwnerId() != null)
            {
                Entity owner = this.getOwner();

                if (owner != null)
                {
                    this.setOwnerId(owner.getUniqueID());
                }
            }
        }
    }

    private double getMaxHealthSlimeling()
    {
        if (this.isTamed())
        {
            return 20.001D + 30.0 * ((double) this.age / (double) this.MAX_AGE);
        }
        else
        {
            return 8.0D;
        }
    }

    @Override
    public float getEyeHeight(Pose pose)
    {
        return this.getHeight() * 0.8F;
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (this.isInvulnerableTo(par1DamageSource))
        {
            return false;
        }
        else
        {
            Entity entity = par1DamageSource.getTrueSource();
            this.setSittingAI(false);

            if (entity != null && !(entity instanceof PlayerEntity))
            {
                par2 = (par2 + 1.0F) / 2.0F;
            }

            return super.attackEntityFrom(par1DamageSource, par2);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity par1Entity)
    {
        return par1Entity.attackEntityFrom(new EntityDamageSource("slimeling", this), this.getDamage());
    }

    public float getDamage()
    {
        int i = this.isTamed() ? 5 : 2;
        return i * this.getAttackDamage();
    }

    @Override
    public void setTamed(boolean par1)
    {
        super.setTamed(par1);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getMaxHealthSlimeling());
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand)
    {
        ItemStack itemstack = player.inventory.getCurrentItem();

        if (this.isTamed())
        {
            if (!itemstack.isEmpty())
            {
                if (itemstack.getItem() == this.getFavoriteFood())
                {
                    if (this.isOwner(player))
                    {
                        itemstack.shrink(1);

                        if (itemstack.isEmpty())
                        {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                        }

                        if (this.world.isRemote)
                        {
                            MarsModuleClient.openSlimelingGui(this, 1);
                        }

                        if (this.rand.nextInt(3) == 0)
                        {
                            this.setRandomFavFood();
                        }
                    }
                    else
                    {
                        if (player instanceof ServerPlayerEntity)
                        {
                            GCPlayerStats stats = GCPlayerStats.get(player);
                            if (stats.getChatCooldown() == 0)
                            {
                                player.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.slimeling.chat.wrong_player")));
                                stats.setChatCooldown(100);
                            }
                        }
                    }
                }
                else
                {
                    if (this.world.isRemote)
                    {
                        MarsModuleClient.openSlimelingGui(this, 0);
                    }
                }
            }
            else
            {
                if (this.world.isRemote)
                {
                    MarsModuleClient.openSlimelingGui(this, 0);
                }
            }

            return true;
        }
        else if (!itemstack.isEmpty() && itemstack.getItem() == Items.SLIME_BALL)
        {
            if (!player.abilities.isCreativeMode)
            {
                itemstack.shrink(1);
            }

            if (itemstack.isEmpty())
            {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
            }

            if (!this.world.isRemote)
            {
                if (this.rand.nextInt(3) == 0)
                {
                    this.setTamed(true);
                    this.getNavigator().clearPath();
                    this.setAttackTarget(null);
                    this.setSittingAI(true);
                    this.setHealth(20.0F);
                    this.setOwnerId(player.getUniqueID());
                    this.playTameEffect(true);
                    this.world.setEntityState(this, (byte) 7);
                }
                else
                {
                    this.playTameEffect(false);
                    this.world.setEntityState(this, (byte) 6);
                }
            }

            return true;
        }

        return super.processInteract(player, hand);
    }

    public void setSittingAI(boolean sitting)
    {
        this.sitGoal.setSitting(sitting);
    }

    @Override
    public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return false;
    }

    public EntitySlimeling spawnBabyAnimal(AgeableEntity par1EntityAgeable)
    {
        if (par1EntityAgeable instanceof EntitySlimeling)
        {
            EntitySlimeling otherSlimeling = (EntitySlimeling) par1EntityAgeable;

            Vector3 colorParentA = new Vector3(this.getColorRed(), this.getColorGreen(), this.getColorBlue());
            Vector3 colorParentB = new Vector3(otherSlimeling.getColorRed(), otherSlimeling.getColorGreen(), otherSlimeling.getColorBlue());
            Vector3 newColor = ColorUtil.addColorsRealistically(colorParentA, colorParentB);
            newColor.x = Math.max(Math.min(newColor.x, 1.0F), 0);
            newColor.y = Math.max(Math.min(newColor.y, 1.0F), 0);
            newColor.z = Math.max(Math.min(newColor.z, 1.0F), 0);
            EntitySlimeling newSlimeling = EntitySlimeling.createEntitySlimeling(this.world, newColor.x, newColor.y, newColor.z);

            UUID id = this.getOwnerId();

            if (id != null)
            {
                newSlimeling.setOwnerId(id);
                newSlimeling.setTamed(true);
            }

            return newSlimeling;
        }

        return null;
    }

    @Override
    public boolean canMateWith(AnimalEntity par1EntityAnimal)
    {
        if (par1EntityAnimal == this)
        {
            return false;
        }
        else if (!this.isTamed())
        {
            return false;
        }
        else if (!(par1EntityAnimal instanceof EntitySlimeling))
        {
            return false;
        }
        else
        {
            EntitySlimeling slimeling = (EntitySlimeling) par1EntityAnimal;
            return slimeling.isTamed() && !slimeling.isSitting() && this.isInLove() && slimeling.isInLove();
        }
    }

    @Override
    public boolean shouldAttackEntity(LivingEntity toAttack, LivingEntity owner)
    {
        if (!(toAttack instanceof CreeperEntity) && !(toAttack instanceof GhastEntity))
        {
            if (toAttack instanceof EntitySlimeling)
            {
                EntitySlimeling slimeling = (EntitySlimeling) toAttack;

                if (slimeling.isTamed() && slimeling.getOwner() == owner)
                {
                    return false;
                }
            }

            return !(toAttack instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity) owner).canAttackPlayer((PlayerEntity) toAttack)) && (!(toAttack instanceof HorseEntity) || !((HorseEntity) toAttack).isTame());
        }
        else
        {
            return false;
        }
    }

    @Override
    public AgeableEntity createChild(AgeableEntity par1EntityAgeable)
    {
        return this.spawnBabyAnimal(par1EntityAgeable);
    }

    public float getColorRed()
    {
        return this.dataManager.get(COLOR_RED);
    }

    public void setColorRed(float color)
    {
        this.dataManager.set(COLOR_RED, color);
    }

    public float getColorGreen()
    {
        return this.dataManager.get(COLOR_GREEN);
    }

    public void setColorGreen(float color)
    {
        this.dataManager.set(COLOR_GREEN, color);
    }

    public float getColorBlue()
    {
        return this.dataManager.get(COLOR_BLUE);
    }

    public void setColorBlue(float color)
    {
        this.dataManager.set(COLOR_BLUE, color);
    }

    public int getAge()
    {
        return this.dataManager.get(AGE);
    }

    public void setAge(int age)
    {
        this.dataManager.set(AGE, age);
    }

    public String getSlimelingName()
    {
        return this.dataManager.get(NAME);
    }

    public void setSlimelingName(String name)
    {
        this.dataManager.set(NAME, name);
    }

    @Override
    public ITextComponent getName()
    {
        return new StringTextComponent(this.getSlimelingName());
    }

    public Item getFavoriteFood()
    {
        return Item.getItemById(this.dataManager.get(FAV_FOOD_ID));
    }

    public void setFavoriteFood(int foodID)
    {
        this.dataManager.set(FAV_FOOD_ID, foodID);
    }

    public float getAttackDamage()
    {
        return this.dataManager.get(ATTACK_DAMAGE);
    }

    public void setAttackDamage(float damage)
    {
        this.dataManager.set(ATTACK_DAMAGE, damage);
    }

    public int getKillCount()
    {
        return this.dataManager.get(KILLS);
    }

    public void setKillCount(int damage)
    {
        this.dataManager.set(KILLS, damage);
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    public float getScale()
    {
        return this.getAge() / (float) this.MAX_AGE * 0.5F + 0.5F;
    }

    public SitGoal getAiSit()
    {
        return this.sitGoal;
    }

    @Override
    public void onDeath(DamageSource p_70645_1_)
    {
        super.onDeath(p_70645_1_);

        if (!this.world.isRemote)
        {
            ItemStack bag = this.slimelingInventory.getStackInSlot(1);
            if (bag != null && bag.getItem() == MarsItems.slimelingCargo)
            {
                this.slimelingInventory.decrStackSize(1, 64);
                this.entityDropItem(bag, 0.5F);
            }
        }
    }

    public static class EntityAISitGC extends SitGoal
    {
        private final TameableEntity theEntity;
        private boolean isSitting;

        public EntityAISitGC(TameableEntity theEntity)
        {
            super(theEntity);
            this.theEntity = theEntity;
//            this.setMutexBits(5);
            this.setMutexFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        @Override
        public boolean shouldExecute()
        {
            if (!this.theEntity.isTamed())
            {
                return false;
            }
            else if (this.theEntity.isInWater())
            {
                return false;
            }
            else
            {
                Entity e = this.theEntity.getOwner();
                if (e instanceof LivingEntity)
                {
                    LivingEntity living = (LivingEntity) e;
                    return living == null || ((!(this.theEntity.getDistanceSq(living) < 144.0D) || living.getRevengeTarget() == null) && this.isSitting);
                }
                return false;
            }
        }

        @Override
        public void startExecuting()
        {
            this.theEntity.getNavigator().clearPath();
            this.theEntity.setSitting(true);
        }

        @Override
        public void resetTask()
        {
            this.theEntity.setSitting(false);
        }

        @Override
        public void setSitting(boolean isSitting)
        {
            this.isSitting = isSitting;
        }
    }

    @Override
    protected void jump()
    {
        this.setMotion(this.getMotion().x, 0.48D / WorldUtil.getGravityFactor(this), this.getMotion().z);
        if (this.getMotion().y < 0.28D)
        {
//            this.motionY = 0.28D;
            this.setMotion(this.getMotion().x, 0.28D, this.getMotion().z);
        }

        if (this.isPotionActive(Effects.JUMP_BOOST))
        {
//            this.motionY += (this.getActivePotionEffect(Effects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
            this.setMotion(this.getMotion().x, this.getMotion().y + (this.getActivePotionEffect(Effects.JUMP_BOOST).getAmplifier() + 1) * 0.1F, this.getMotion().z);
        }

        if (this.isSprinting())
        {
            float f = this.rotationYaw / Constants.RADIANS_TO_DEGREES;
//            this.motionX -= MathHelper.sin(f) * 0.2F;
//            this.motionZ += MathHelper.cos(f) * 0.2F;
            this.setMotion(this.getMotion().x - MathHelper.sin(f) * 0.2F, this.getMotion().y, this.getMotion().z + MathHelper.cos(f) * 0.2F);
        }

        this.isAirBorne = true;
        ForgeHooks.onLivingJump(this);
    }
}
