package micdoodle8.mods.galacticraft.mars.entities;

import java.util.UUID;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.mars.client.gui.GCMarsGuiSlimeling;
import micdoodle8.mods.galacticraft.mars.client.gui.GCMarsGuiSlimelingFeed;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class GCMarsEntitySlimeling extends EntityTameable implements IEntityBreathable
{
    private static final AttributeModifier field_110185_bq = (new AttributeModifier(UUID.randomUUID(), "Slimeling Health Boost", -0.25D, 0));
    
    public float colorRed;
    public float colorGreen;
    public float colorBlue;
    public long ticksAlive;
    public int age = 0;
    public final int MAX_AGE = 100000;
    public String slimelingName = "Unnamed";
    public int favFoodID = 1;
    public float attackDamage = 0.05F;
    public int kills;

    public GCMarsEntitySlimeling(World par1World)
    {
        super(par1World);
        this.setSize(1.0F, 1.0F);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
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
        this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, GCMarsEntitySludgeling.class, 200, false));
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
    
    private void setRandomFavFood()
    {
        switch (this.rand.nextInt(10))
        {
        case 0:
            this.favFoodID = Item.ingotGold.itemID;
            break;
        case 1:
            this.favFoodID = Item.flintAndSteel.itemID;
            break;
        case 2:
            this.favFoodID = Item.bakedPotato.itemID;
            break;
        case 3:
            this.favFoodID = Item.swordStone.itemID;
            break;
        case 4:
            this.favFoodID = Item.gunpowder.itemID;
            break;
        case 5:
            this.favFoodID = Item.doorWood.itemID;
            break;
        case 6:
            this.favFoodID = Item.emerald.itemID;
            break;
        case 7:
            this.favFoodID = Item.fishCooked.itemID;
            break;
        case 8:
            this.favFoodID = Item.redstoneRepeater.itemID;
            break;
        case 9:
            this.favFoodID = Item.boat.itemID;
            break;
        }
    }

    public GCMarsEntitySlimeling(World par1World, float red, float green, float blue)
    {
        this(par1World);
        this.colorRed = red;
        this.colorGreen = green;
        this.colorBlue = blue;
    }

    protected void func_110147_ax()
    {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.30000001192092896D);

        if (this.isTamed())
        {
            this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(20.0D);
        }
        else
        {
            this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(8.0D);
        }
    }

    public boolean isAIEnabled()
    {
        return true;
    }

    protected void updateAITick()
    {
        this.dataWatcher.updateObject(18, Float.valueOf(this.func_110143_aJ()));
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(18, new Float(this.func_110143_aJ()));
        this.dataWatcher.addObject(19, new Float(this.colorRed));
        this.dataWatcher.addObject(20, new Float(this.colorGreen));
        this.dataWatcher.addObject(21, new Float(this.colorBlue));
        this.dataWatcher.addObject(22, new Integer(this.age));
        this.dataWatcher.addObject(23, "");
        this.dataWatcher.addObject(24, new Integer(this.favFoodID));
        this.dataWatcher.addObject(25, new Float(this.attackDamage));
        this.dataWatcher.addObject(26, new Integer(this.kills));
        this.setName("Unnamed");
    }

    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setFloat("SlimeRed", this.colorRed);
        nbt.setFloat("SlimeGreen", this.colorGreen);
        nbt.setFloat("SlimeBlue", this.colorBlue);
        nbt.setInteger("SlimelingAge", this.age);
        nbt.setString("SlimelingName", this.slimelingName);
        nbt.setInteger("FavFoodID", this.favFoodID);
        nbt.setFloat("SlimelingDamage", this.attackDamage);
        nbt.setInteger("SlimelingKills", this.kills);
    }

    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        this.colorRed = nbt.getFloat("SlimeRed");
        this.colorGreen = nbt.getFloat("SlimeGreen");
        this.colorBlue = nbt.getFloat("SlimeBlue");
        this.age = nbt.getInteger("SlimelingAge");
        this.slimelingName = nbt.getString("SlimelingName");
        this.favFoodID = nbt.getInteger("FavFoodID");
        this.attackDamage = nbt.getFloat("SlimelingDamage");
        this.kills = nbt.getInteger("SlimelingKills");
        this.setColorRed(this.colorRed);
        this.setColorGreen(this.colorGreen);
        this.setColorBlue(this.colorBlue);
        this.setAge(this.age);
        this.setName(this.slimelingName);
        this.setKillCount(this.kills);
    }

    protected String getLivingSound()
    {
        return "";
    }

    protected String getHurtSound()
    {
        return "";
    }

    protected String getDeathSound()
    {
        return "";
    }

    protected float getSoundVolume()
    {
        return 0.0F;
    }
    
    protected int getDropItemId()
    {
        return Item.slimeBall.itemID;
    }

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
            
            if (this.ticksAlive >= Long.MAX_VALUE)
            {
                this.ticksAlive = 0;
            }
            
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

        }
        
        this.height = this.getScale();
        this.width = this.getScale();
        
        this.boundingBox.maxX = this.boundingBox.minX + (double)this.width;
        this.boundingBox.maxZ = this.boundingBox.minZ + (double)this.width;
        this.boundingBox.maxY = this.boundingBox.minY + (double)this.height;

        if (!this.worldObj.isRemote)
        {
            this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(20 + (30.0 * ((double)this.age / (double)this.MAX_AGE)));
        }
    }

    public float getEyeHeight()
    {
        return this.height * 0.8F;
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else
        {
            Entity entity = par1DamageSource.getEntity();
            this.aiSit.setSitting(false);

            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
            {
                par2 = (par2 + 1.0F) / 2.0F;
            }

            return super.attackEntityFrom(par1DamageSource, par2);
        }
    }

    public boolean attackEntityAsMob(Entity par1Entity)
    {
        return par1Entity.attackEntityFrom(new EntityDamageSource("slimeling", this), this.getDamage());
    }
    
    public float getDamage()
    {
        int i = this.isTamed() ? 5 : 2;
        return (float)i * this.getAttackDamage();
    }

    public void setTamed(boolean par1)
    {
        super.setTamed(par1);

        if (par1)
        {
            this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(20.0D);
        }
        else
        {
            this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(8.0D);
        }
    }

    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();

        if (this.isTamed())
        {
            if (itemstack != null)
            {
                if (itemstack.itemID == this.getFavoriteFood())
                {
                    if (par1EntityPlayer.username.equals(this.getOwnerName()))
                    {
                        --itemstack.stackSize;

                        if (itemstack.stackSize <= 0)
                        {
                            par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
                        }
                        
                        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                        {
                            FMLClientHandler.instance().getClient().displayGuiScreen(new GCMarsGuiSlimelingFeed(this));
                        }
                        
                        if (this.rand.nextInt(2) == 0)
                        {
                            this.setRandomFavFood();
                        }
                    }
                    else
                    {
                        if (par1EntityPlayer instanceof GCCorePlayerMP)
                        {
                            if (((GCCorePlayerMP) par1EntityPlayer).getChatCooldown() == 0)
                            {
                                par1EntityPlayer.sendChatToPlayer(ChatMessageComponent.func_111066_d("This isn't my Slimeling!"));
                                ((GCCorePlayerMP) par1EntityPlayer).setChatCooldown(100);
                            }
                        }
                    }
                }
                else
                {
                    if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                    {
                        FMLClientHandler.instance().getClient().displayGuiScreen(new GCMarsGuiSlimeling(this));
                    }
                }
            }
            else
            {
                if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                {
                    FMLClientHandler.instance().getClient().displayGuiScreen(new GCMarsGuiSlimeling(this));
                }
            }
        }
        else if (itemstack != null && itemstack.itemID == Item.slimeBall.itemID)
        {
            if (!par1EntityPlayer.capabilities.isCreativeMode)
            {
                --itemstack.stackSize;
            }

            if (itemstack.stackSize <= 0)
            {
                par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
            }

            if (!this.worldObj.isRemote)
            {
                if (this.rand.nextInt(3) == 0)
                {
                    this.setTamed(true);
                    this.setPathToEntity((PathEntity)null);
                    this.setAttackTarget((EntityLivingBase)null);
                    this.aiSit.setSitting(true);
                    this.setEntityHealth(20.0F);
                    this.setOwner(par1EntityPlayer.getCommandSenderName());
                    this.playTameEffect(true);
                    this.worldObj.setEntityState(this, (byte)7);
                }
                else
                {
                    this.playTameEffect(false);
                    this.worldObj.setEntityState(this, (byte)6);
                }
            }

            return true;
        }

        return super.interact(par1EntityPlayer);
    }
    
    public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return par1ItemStack == null ? false : (!(Item.itemsList[par1ItemStack.itemID] instanceof ItemFood) ? false : ((ItemFood)Item.itemsList[par1ItemStack.itemID]).isWolfsFavoriteMeat());
    }

    public GCMarsEntitySlimeling spawnBabyAnimal(EntityAgeable par1EntityAgeable)
    {
        if (par1EntityAgeable instanceof GCMarsEntitySlimeling)
        {
            GCMarsEntitySlimeling otherSlimeling = (GCMarsEntitySlimeling) par1EntityAgeable;
            
            GCMarsEntitySlimeling newSlimeling = new GCMarsEntitySlimeling(this.worldObj, (this.getColorRed() + otherSlimeling.getColorRed()) / 2, (this.getColorGreen() + otherSlimeling.getColorGreen()) / 2, (this.getColorBlue() + otherSlimeling.getColorBlue()) / 2);
            
            String s = this.getOwnerName();

            if (s != null && s.trim().length() > 0)
            {
                newSlimeling.setOwner(s);
                newSlimeling.setTamed(true);
            }
            
            return newSlimeling;
        }

        return null;
    }

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
        else if (!(par1EntityAnimal instanceof GCMarsEntitySlimeling))
        {
            return false;
        }
        else
        {
            GCMarsEntitySlimeling slimeling = (GCMarsEntitySlimeling)par1EntityAnimal;
            return !slimeling.isTamed() ? false : (slimeling.isSitting() ? false : this.isInLove() && slimeling.isInLove());
        }
    }

    public boolean func_142018_a(EntityLivingBase par1EntityLivingBase, EntityLivingBase par2EntityLivingBase)
    {
        if (!(par1EntityLivingBase instanceof EntityCreeper) && !(par1EntityLivingBase instanceof EntityGhast))
        {
            if (par1EntityLivingBase instanceof GCMarsEntitySlimeling)
            {
                GCMarsEntitySlimeling entitywolf = (GCMarsEntitySlimeling)par1EntityLivingBase;

                if (entitywolf.isTamed() && entitywolf.func_130012_q() == par2EntityLivingBase)
                {
                    return false;
                }
            }

            return par1EntityLivingBase instanceof EntityPlayer && par2EntityLivingBase instanceof EntityPlayer && !((EntityPlayer)par2EntityLivingBase).func_96122_a((EntityPlayer)par1EntityLivingBase) ? false : !(par1EntityLivingBase instanceof EntityHorse) || !((EntityHorse)par1EntityLivingBase).func_110248_bS();
        }
        else
        {
            return false;
        }
    }

    public EntityAgeable createChild(EntityAgeable par1EntityAgeable)
    {
        return this.spawnBabyAnimal(par1EntityAgeable);
    }
    
    public float getColorRed()
    {
        return this.dataWatcher.func_111145_d(19);
    }
    
    public void setColorRed(float color)
    {
        this.dataWatcher.updateObject(19, color);
    }
    
    public float getColorGreen()
    {
        return this.dataWatcher.func_111145_d(20);
    }
    
    public void setColorGreen(float color)
    {
        this.dataWatcher.updateObject(20, color);
    }
    
    public float getColorBlue()
    {
        return this.dataWatcher.func_111145_d(21);
    }
    
    public void setColorBlue(float color)
    {
        this.dataWatcher.updateObject(21, color);
    }
    
    public int getAge()
    {
        return this.dataWatcher.getWatchableObjectInt(22);
    }
    
    public void setAge(int age)
    {
        this.dataWatcher.updateObject(22, age);
    }
    
    public String getName()
    {
        return this.dataWatcher.getWatchableObjectString(23);
    }
    
    public void setName(String name)
    {
        this.dataWatcher.updateObject(23, name);
    }
    
    public int getFavoriteFood()
    {
        return this.dataWatcher.getWatchableObjectInt(24);
    }
    
    public void setFavoriteFood(int foodID)
    {
        this.dataWatcher.updateObject(24, foodID);
    }
    
    public float getAttackDamage()
    {
        return this.dataWatcher.func_111145_d(25);
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
        return ((this.getAge() / (float)this.MAX_AGE) * 0.5F) + 0.5F;
    }
    
    public EntityAISit getAiSit()
    {
        return this.aiSit;
    }
}
