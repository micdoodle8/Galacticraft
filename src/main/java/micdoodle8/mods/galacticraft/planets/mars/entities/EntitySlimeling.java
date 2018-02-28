package micdoodle8.mods.galacticraft.planets.mars.entities;

import com.google.common.base.Predicate;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.planets.mars.MarsModuleClient;
import micdoodle8.mods.galacticraft.planets.mars.inventory.InventorySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class EntitySlimeling extends EntityTameable implements IEntityBreathable
{
    public InventorySlimeling slimelingInventory = new InventorySlimeling(this);

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

    public EntitySlimeling(World par1World)
    {
        super(par1World);
        this.setSize(0.45F, 0.7F);
        ((PathNavigateGround) this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.aiSit = new EntityAISitGC(this);
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, true));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(6, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(4, new EntityAITargetNonTamed<>(this, EntitySludgeling.class, false, new Predicate<Entity>()
        {
            @Override
            public boolean apply(Entity entity)
            {
                return entity instanceof EntitySludgeling;
            }
        }));
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

    @Override
    public EntityLivingBase getOwner()
    {
        EntityLivingBase owner = super.getOwner();
        if (owner == null)
        {
            String ownerName = getOwnerUsername();
            if (ownerName != null)
            {
                return this.worldObj.getPlayerEntityByName(ownerName);
            }
        }
        return owner;
    }

    @Override
    public boolean isOwner(EntityLivingBase entityLivingBase)
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

    @Override
    public void setScaleForAge(boolean par1)
    {
        this.setScale(this.getSlimelingSize());
    }

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
            this.favFoodID = Item.getIdFromItem(Items.gold_ingot);
            break;
        case 1:
            this.favFoodID = Item.getIdFromItem(Items.flint_and_steel);
            break;
        case 2:
            this.favFoodID = Item.getIdFromItem(Items.baked_potato);
            break;
        case 3:
            this.favFoodID = Item.getIdFromItem(Items.stone_sword);
            break;
        case 4:
            this.favFoodID = Item.getIdFromItem(Items.gunpowder);
            break;
        case 5:
            this.favFoodID = Item.getIdFromItem(Items.wooden_hoe);
            break;
        case 6:
            this.favFoodID = Item.getIdFromItem(Items.emerald);
            break;
        case 7:
            this.favFoodID = Item.getIdFromItem(Items.fish);
            break;
        case 8:
            this.favFoodID = Item.getIdFromItem(Items.repeater);
            break;
        case 9:
            this.favFoodID = Item.getIdFromItem(Items.boat);
            break;
        }
    }

    public EntitySlimeling(World par1World, float red, float green, float blue)
    {
        this(par1World);
        this.colorRed = red;
        this.colorGreen = green;
        this.colorBlue = blue;
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(this.getMaxHealthSlimeling());
    }

//    @Override
//    public boolean isAIEnabled()
//    {
//        return true;
//    }

    @Override
    protected void updateAITick()
    {
        this.dataWatcher.updateObject(18, Float.valueOf(this.getHealth()));
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(18, new Float(this.getHealth()));
        this.dataWatcher.addObject(19, new Float(this.colorRed));
        this.dataWatcher.addObject(20, new Float(this.colorGreen));
        this.dataWatcher.addObject(21, new Float(this.colorBlue));
        this.dataWatcher.addObject(22, new Integer(this.age));
        this.dataWatcher.addObject(23, "");
        this.dataWatcher.addObject(24, new Integer(this.favFoodID));
        this.dataWatcher.addObject(25, new Float(this.attackDamage));
        this.dataWatcher.addObject(26, new Integer(this.kills));
        this.dataWatcher.addObject(27, new ItemStack(Blocks.stone));
        this.dataWatcher.addObject(28, "");
        this.setName(GCCoreUtil.translate("gui.message.unnamed.name"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setTag("SlimelingInventory", this.slimelingInventory.writeToNBT(new NBTTagList()));
        nbt.setFloat("SlimeRed", this.colorRed);
        nbt.setFloat("SlimeGreen", this.colorGreen);
        nbt.setFloat("SlimeBlue", this.colorBlue);
        nbt.setInteger("SlimelingAge", this.age);
        nbt.setString("SlimelingName", this.slimelingName);
        nbt.setInteger("FavFoodID", this.favFoodID);
        nbt.setFloat("SlimelingDamage", this.attackDamage);
        nbt.setInteger("SlimelingKills", this.kills);
        nbt.setString("OwnerUsername", this.getOwnerUsername());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        this.slimelingInventory.readFromNBT(nbt.getTagList("SlimelingInventory", 10));
        this.colorRed = nbt.getFloat("SlimeRed");
        this.colorGreen = nbt.getFloat("SlimeGreen");
        this.colorBlue = nbt.getFloat("SlimeBlue");
        this.age = nbt.getInteger("SlimelingAge");
        this.slimelingName = nbt.getString("SlimelingName");
        this.favFoodID = nbt.getInteger("FavFoodID");
        this.attackDamage = nbt.getFloat("SlimelingDamage");
        this.kills = nbt.getInteger("SlimelingKills");
        this.setOwnerUsername(nbt.getString("OwnerUsername"));
        this.setColorRed(this.colorRed);
        this.setColorGreen(this.colorGreen);
        this.setColorBlue(this.colorBlue);
        this.setAge(this.age);
        this.setName(this.slimelingName);
        this.setKillCount(this.kills);
    }

    @Override
    protected String getLivingSound()
    {
        return null;
    }

    @Override
    protected String getHurtSound()
    {
        this.playSound("mob.slime.small", this.getSoundVolume(), 1.1F);
        return null;
    }

    @Override
    protected String getDeathSound()
    {
        this.playSound(Constants.TEXTURE_PREFIX + "entity.slime_death", this.getSoundVolume(), 0.8F);
        return null;
    }

    @Override
    protected Item getDropItem()
    {
        return Items.slime_ball;
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (!this.worldObj.isRemote)
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
            this.setCargoSlot(this.slimelingInventory.getStackInSlot(1));
        }

        if (!this.worldObj.isRemote)
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(this.getMaxHealthSlimeling());

            if (this.getOwnerUsername().isEmpty())
            {
                Entity owner = this.getOwner();

                if (owner != null)
                {
                    this.setOwnerUsername(owner.getName());
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
    public float getEyeHeight()
    {
        return this.height * 0.8F;
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (this.isEntityInvulnerable(par1DamageSource))
        {
            return false;
        }
        else
        {
            Entity entity = par1DamageSource.getEntity();
            this.setSittingAI(false);

            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
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
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(this.getMaxHealthSlimeling());
    }

    @Override
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();

        if (this.isTamed())
        {
            if (itemstack != null)
            {
                if (itemstack.getItem() == this.getFavoriteFood())
                {
                    if (this.isOwner(par1EntityPlayer))
                    {
                        --itemstack.stackSize;

                        if (itemstack.stackSize <= 0)
                        {
                            par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack) null);
                        }

                        if (this.worldObj.isRemote)
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
                        if (par1EntityPlayer instanceof EntityPlayerMP)
                        {
                            GCPlayerStats stats = GCPlayerStats.get(par1EntityPlayer);
                            if (stats.getChatCooldown() == 0)
                            {
                                par1EntityPlayer.addChatMessage(new ChatComponentText(GCCoreUtil.translate("gui.slimeling.chat.wrong_player")));
                                stats.setChatCooldown(100);
                            }
                        }
                    }
                }
                else
                {
                    if (this.worldObj.isRemote)
                    {
                        MarsModuleClient.openSlimelingGui(this, 0);
                    }
                }
            }
            else
            {
                if (this.worldObj.isRemote)
                {
                    MarsModuleClient.openSlimelingGui(this, 0);
                }
            }

            return true;
        }
        else if (itemstack != null && itemstack.getItem() == Items.slime_ball)
        {
            if (!par1EntityPlayer.capabilities.isCreativeMode)
            {
                --itemstack.stackSize;
            }

            if (itemstack.stackSize <= 0)
            {
                par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack) null);
            }

            if (!this.worldObj.isRemote)
            {
                if (this.rand.nextInt(3) == 0)
                {
                    this.setTamed(true);
                    this.getNavigator().clearPathEntity();
                    this.setAttackTarget(null);
                    this.setSittingAI(true);
                    this.setHealth(20.0F);
                    this.setOwnerId(par1EntityPlayer.getUniqueID().toString());
                    this.setOwnerUsername(par1EntityPlayer.getName());
                    this.playTameEffect(true);
                    this.worldObj.setEntityState(this, (byte) 7);
                }
                else
                {
                    this.playTameEffect(false);
                    this.worldObj.setEntityState(this, (byte) 6);
                }
            }

            return true;
        }

        return super.interact(par1EntityPlayer);
    }

    public void setSittingAI(boolean sitting)
    {
        this.aiSit.setSitting(sitting);
    }

    public String getOwnerUsername()
    {
        String s = this.dataWatcher.getWatchableObjectString(28);
        return s == null || s.length() == 0 ? "" : s;
    }

    public void setOwnerUsername(String username)
    {
        this.dataWatcher.updateObject(28, username);
    }

    @Override
    public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return false;
    }

    public EntitySlimeling spawnBabyAnimal(EntityAgeable par1EntityAgeable)
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
            EntitySlimeling newSlimeling = new EntitySlimeling(this.worldObj, (float) newColor.x, (float) newColor.y, (float) newColor.z);

            String s = this.getOwnerId();

            if (s != null && s.trim().length() > 0)
            {
                newSlimeling.setOwnerId(s);
                newSlimeling.setTamed(true);
            }

            return newSlimeling;
        }

        return null;
    }

    @Override
    public boolean canMateWith(EntityAnimal par1EntityAnimal)
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
    public boolean shouldAttackEntity(EntityLivingBase toAttack, EntityLivingBase owner)
    {
        if (!(toAttack instanceof EntityCreeper) && !(toAttack instanceof EntityGhast))
        {
            if (toAttack instanceof EntitySlimeling)
            {
                EntitySlimeling slimeling = (EntitySlimeling) toAttack;

                if (slimeling.isTamed() && slimeling.getOwner() == owner)
                {
                    return false;
                }
            }

            return !(toAttack instanceof EntityPlayer && owner instanceof EntityPlayer && !((EntityPlayer) owner).canAttackPlayer((EntityPlayer) toAttack)) && (!(toAttack instanceof EntityHorse) || !((EntityHorse) toAttack).isTame());
        }
        else
        {
            return false;
        }
    }

    @Override
    public EntityAgeable createChild(EntityAgeable par1EntityAgeable)
    {
        return this.spawnBabyAnimal(par1EntityAgeable);
    }

    public float getColorRed()
    {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }

    public void setColorRed(float color)
    {
        this.dataWatcher.updateObject(19, color);
    }

    public float getColorGreen()
    {
        return this.dataWatcher.getWatchableObjectFloat(20);
    }

    public void setColorGreen(float color)
    {
        this.dataWatcher.updateObject(20, color);
    }

    public float getColorBlue()
    {
        return this.dataWatcher.getWatchableObjectFloat(21);
    }

    public void setColorBlue(float color)
    {
        this.dataWatcher.updateObject(21, color);
    }

    @Override
    public int getAge()
    {
        return this.dataWatcher.getWatchableObjectInt(22);
    }

    public void setAge(int age)
    {
        this.dataWatcher.updateObject(22, age);
    }

    @Override
    public String getName()
    {
        return this.dataWatcher.getWatchableObjectString(23);
    }

    public void setName(String name)
    {
        this.dataWatcher.updateObject(23, name);
    }

    public Item getFavoriteFood()
    {
        return Item.getItemById(this.dataWatcher.getWatchableObjectInt(24));
    }

    public void setFavoriteFood(int foodID)
    {
        this.dataWatcher.updateObject(24, foodID);
    }

    public float getAttackDamage()
    {
        return this.dataWatcher.getWatchableObjectFloat(25);
    }

    public void setAttackDamage(float damage)
    {
        this.dataWatcher.updateObject(25, damage);
    }

    public int getKillCount()
    {
        return this.dataWatcher.getWatchableObjectInt(26);
    }

    public void setKillCount(int damage)
    {
        this.dataWatcher.updateObject(26, damage);
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

    public EntityAISit getAiSit()
    {
        return this.aiSit;
    }

    public ItemStack getCargoSlot()
    {
        return this.dataWatcher.getWatchableObjectItemStack(27);
    }

    public void setCargoSlot(ItemStack stack)
    {
        ItemStack stack2 = this.dataWatcher.getWatchableObjectItemStack(27);

        if (stack != stack2)
        {
            this.dataWatcher.updateObject(27, stack);
            this.dataWatcher.setObjectWatched(27);
        }
    }

    @Override
    public void onDeath(DamageSource p_70645_1_)
    {
        super.onDeath(p_70645_1_);

        if (!this.worldObj.isRemote)
        {
            ItemStack bag = this.getCargoSlot();
            if (bag != null && bag.getItem() == MarsItems.marsItemBasic && bag.getItemDamage() == 4)
            {
                this.slimelingInventory.decrStackSize(1, 64);
                this.entityDropItem(bag, 0.5F);
            }
        }
    }

    public static class EntityAISitGC extends EntityAISit
    {
        private EntityTameable theEntity;
        private boolean isSitting;

        public EntityAISitGC(EntityTameable theEntity)
        {
            super(theEntity);
            this.theEntity = theEntity;
            this.setMutexBits(5);
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
                if (e instanceof EntityLivingBase)
                {
                    EntityLivingBase living = (EntityLivingBase) e;
                    return living == null ? true : (this.theEntity.getDistanceSqToEntity(living) < 144.0D && living.getAITarget() != null ? false : this.isSitting);
                }
                return false;
            }
        }

        @Override
        public void startExecuting()
        {
            this.theEntity.getNavigator().clearPathEntity();
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
        this.motionY = 0.48D / WorldUtil.getGravityFactor(this);
        if (this.motionY < 0.28D)
        {
            this.motionY = 0.28D;
        }

        if (this.isPotionActive(Potion.jump))
        {
            this.motionY += (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
        }

        if (this.isSprinting())
        {
            float f = this.rotationYaw / Constants.RADIANS_TO_DEGREES;
            this.motionX -= MathHelper.sin(f) * 0.2F;
            this.motionZ += MathHelper.cos(f) * 0.2F;
        }

        this.isAirBorne = true;
        ForgeHooks.onLivingJump(this);
    }
}
