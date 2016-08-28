package micdoodle8.mods.galacticraft.core.entities;

import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.items.GCItems;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.BlockWood;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import ru.gug2.mods.manysystems.core.items.CoreItems;
import ru.gug2.mods.manysystems.planets.venus.blocks.VenusBlocks;

public class EntityAlienVillager extends EntityVillager implements IMerchant, INpc, IEntityBreathable
{
    private int randomTickDivider;
    private boolean isMating;
    private boolean isPlaying;
    Village villageObj;
    /** This villager's current customer. */
    private EntityPlayer buyingPlayer;
    /** Initialises the MerchantRecipeList.java */
    private MerchantRecipeList buyingList;
    private int timeUntilReset;
    /** addDefaultEquipmentAndRecipies is called if this is true */
    private boolean needsInitilization;
    private int wealth;
    /** Last player to trade with this villager, used for aggressivity. */
    private String lastBuyingPlayer;
    private boolean isLookingForHome;
    private float field_82191_bN;
    /** Selling list of Villagers items. */
    public static final Map villagersSellingList = new HashMap();
    /** Selling list of Blacksmith items. */
    public static final Map blacksmithSellingList = new HashMap();
    private static final String __OBFID = "CL_00001707";

    public EntityAlienVillager(World p_i1747_1_)
    {
        this(p_i1747_1_, 0);
    }

    public EntityAlienVillager(World par1World, int p_i1748_2_)
    {
        super(par1World);
        this.setProfession(p_i1748_2_);
        this.setSize(0.6F, 1.8F/*0.6F, 1.8F*/);
        this.getNavigator().setBreakDoors(true);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(1, new EntityAITradePlayer(this));
        this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));
        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
        this.tasks.addTask(6, new EntityAIVillagerMate(this));
        this.tasks.addTask(7, new EntityAIFollowGolem(this));
        this.tasks.addTask(8, new EntityAIPlay(this, 0.32D));
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityAlienVillager.class, 5.0F, 0.02F));
        this.tasks.addTask(9, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    /**
     * main AI tick function, replaces updateEntityActionState
     */
    protected void updateAITick()
    {
        if (--this.randomTickDivider <= 0)
        {
            this.worldObj.villageCollectionObj.addVillagerPosition(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
            this.randomTickDivider = 70 + this.rand.nextInt(50);
            this.villageObj = this.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 32);

            if (this.villageObj == null)
            {
                this.detachHome();
            }
            else
            {
                ChunkCoordinates chunkcoordinates = this.villageObj.getCenter();
                this.setHomeArea(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, (int)((float)this.villageObj.getVillageRadius() * 0.6F));

                if (this.isLookingForHome)
                {
                    this.isLookingForHome = false;
                    this.villageObj.setDefaultPlayerReputation(5);
                }
            }
        }

        if (!this.isTrading() && this.timeUntilReset > 0)
        {
            --this.timeUntilReset;

            if (this.timeUntilReset <= 0)
            {
                if (this.needsInitilization)
                {
                    if (this.buyingList.size() > 1)
                    {
                        Iterator iterator = this.buyingList.iterator();

                        while (iterator.hasNext())
                        {
                            MerchantRecipe merchantrecipe = (MerchantRecipe)iterator.next();

                            if (merchantrecipe.isRecipeDisabled())
                            {
                                merchantrecipe.func_82783_a(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
                            }
                        }
                    }

                    this.addDefaultEquipmentAndRecipies(1);
                    this.needsInitilization = false;

                    if (this.villageObj != null && this.lastBuyingPlayer != null)
                    {
                        this.worldObj.setEntityState(this, (byte)14);
                        this.villageObj.setReputationForPlayer(this.lastBuyingPlayer, 1);
                    }
                }

                this.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
            }
        }

        super.updateAITick();
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer p_70085_1_)
    {
        ItemStack itemstack = p_70085_1_.inventory.getCurrentItem();
        boolean flag = itemstack != null && itemstack.getItem() == Items.spawn_egg;

        if (!flag && this.isEntityAlive() && !this.isTrading() && !this.isChild() && !p_70085_1_.isSneaking())
        {
            if (!this.worldObj.isRemote)
            {
                this.setCustomer(p_70085_1_);
                p_70085_1_.displayGUIMerchant(this, this.getCustomNameTag());
            }

            return true;
        }
        else
        {
            return super.interact(p_70085_1_);
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(15, Integer.valueOf(0));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound p_70014_1_)
    {
        super.writeEntityToNBT(p_70014_1_);
        p_70014_1_.setInteger("Profession", this.getProfession());
        p_70014_1_.setInteger("Riches", this.wealth);

        if (this.buyingList != null)
        {
            p_70014_1_.setTag("Offers", this.buyingList.getRecipiesAsTags());
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound p_70037_1_)
    {
        super.readEntityFromNBT(p_70037_1_);
        this.setProfession(p_70037_1_.getInteger("Profession"));
        this.wealth = p_70037_1_.getInteger("Riches");

        if (p_70037_1_.hasKey("Offers", 10))
        {
            NBTTagCompound nbttagcompound1 = p_70037_1_.getCompoundTag("Offers");
            this.buyingList = new MerchantRecipeList(nbttagcompound1);
        }
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return false;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return this.isTrading() ? "mob.villager.haggle" : "mob.villager.idle";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.villager.hit";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.villager.death";
    }

    public void setProfession(int p_70938_1_)
    {
        this.dataWatcher.updateObject(16, Integer.valueOf(p_70938_1_));
    }

    public int getProfession()
    {
        return this.dataWatcher.getWatchableObjectInt(16);
    }

    public boolean isMating()
    {
        return this.isMating;
    }

    public void setMating(boolean p_70947_1_)
    {
        this.isMating = p_70947_1_;
    }

    public void setPlaying(boolean p_70939_1_)
    {
        this.isPlaying = p_70939_1_;
    }

    public boolean isPlaying()
    {
        return this.isPlaying;
    }

    public void setRevengeTarget(EntityLivingBase p_70604_1_)
    {
        super.setRevengeTarget(p_70604_1_);

        if (this.villageObj != null && p_70604_1_ != null)
        {
            this.villageObj.addOrRenewAgressor(p_70604_1_);

            if (p_70604_1_ instanceof EntityPlayer)
            {
                byte b0 = -1;

                if (this.isChild())
                {
                    b0 = -3;
                }

                this.villageObj.setReputationForPlayer(p_70604_1_.getCommandSenderName(), b0);

                if (this.isEntityAlive())
                {
                    this.worldObj.setEntityState(this, (byte)13);
                }
            }
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource p_70645_1_)
    {
        if (this.villageObj != null)
        {
            Entity entity = p_70645_1_.getEntity();

            if (entity != null)
            {
                if (entity instanceof EntityPlayer)
                {
                    this.villageObj.setReputationForPlayer(entity.getCommandSenderName(), -2);
                }
                else if (entity instanceof IMob)
                {
                    this.villageObj.endMatingSeason();
                }
            }
            else if (entity == null)
            {
                EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16.0D);

                if (entityplayer != null)
                {
                    this.villageObj.endMatingSeason();
                }
            }
        }

        super.onDeath(p_70645_1_);
    }

    public void setCustomer(EntityPlayer p_70932_1_)
    {
        this.buyingPlayer = p_70932_1_;
    }

    public EntityPlayer getCustomer()
    {
        return this.buyingPlayer;
    }

    public boolean isTrading()
    {
        return this.buyingPlayer != null;
    }

    public void useRecipe(MerchantRecipe p_70933_1_)
    {
        p_70933_1_.incrementToolUses();
        this.livingSoundTime = -this.getTalkInterval();
        this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());

        if (p_70933_1_.hasSameIDsAs((MerchantRecipe)this.buyingList.get(this.buyingList.size() - 1)))
        {
            this.timeUntilReset = 40;
            this.needsInitilization = true;

            if (this.buyingPlayer != null)
            {
                this.lastBuyingPlayer = this.buyingPlayer.getCommandSenderName();
            }
            else
            {
                this.lastBuyingPlayer = null;
            }
        }

        if (p_70933_1_.getItemToBuy().getItem() == Items.emerald)
        {
            this.wealth += p_70933_1_.getItemToBuy().stackSize;
        }
    }

    public void func_110297_a_(ItemStack p_110297_1_)
    {
        if (!this.worldObj.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20)
        {
            this.livingSoundTime = -this.getTalkInterval();

            if (p_110297_1_ != null)
            {
                this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
            }
            else
            {
                this.playSound("mob.villager.no", this.getSoundVolume(), this.getSoundPitch());
            }
        }
    }

    public MerchantRecipeList getRecipes(EntityPlayer p_70934_1_)
    {
        if (this.buyingList == null)
        {
            this.addDefaultEquipmentAndRecipies(1);
        }

        return this.buyingList;
    }

    /**
     * Adjusts the probability of obtaining a given recipe being offered by a villager
     */
    private float adjustProbability(float p_82188_1_)
    {
        float f1 = p_82188_1_ + this.field_82191_bN;
        return f1 > 0.9F ? 0.9F - (f1 - 0.9F) : f1;
    }

    /**
     * based on the villagers profession add items, equipment, and recipies adds par1 random items to the list of things
     * that the villager wants to buy. (at most 1 of each wanted type is added)
     */
    private void addDefaultEquipmentAndRecipies(int p_70950_1_)
    {
        if (this.buyingList != null)
        {
            this.field_82191_bN = MathHelper.sqrt_float((float)this.buyingList.size()) * 0.2F;
        }
        else
        {
            this.field_82191_bN = 0.0F;
        }

        MerchantRecipeList merchantrecipelist;
        merchantrecipelist = new MerchantRecipeList();
        VillagerRegistry.manageVillagerTrades(merchantrecipelist, this, this.getProfession(), this.rand);
        int k;
        label50:

        switch (this.getProfession())
        { 
            case 0:
    
               	 if (this.rand.nextFloat() < this.adjustProbability(0.9F))
                     {
                     	
               	       merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.cheeseCurd, 16), new ItemStack(GCItems.partBuggy, 1, 2)));
           		merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.basicItem, 11, 2), new ItemStack(GCItems.battery.setMaxDamage(0), 1, 0)));
           	         merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.meteoricIronIngot, 12), new ItemStack(MarsItems.marsItemBasic, 1, 6)));
           	        merchantrecipelist.add(new MerchantRecipe(new ItemStack(MarsItems.carbonFragments, 11), new ItemStack(Items.iron_ingot, 10, 0)));
           	        merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.basicItem, 12, 9), new ItemStack(GCItems.oxTankHeavy.setMaxDamage(2700), 1, 0))); 
                     }

                break;
               
            case 1:
            	   
               	 if (this.rand.nextFloat() < this.adjustProbability(0.9F))
                     {
                     merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.cheeseCurd, 16), new ItemStack(GCItems.partBuggy, 1, 2)));
           		merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.basicItem, 11, 2), new ItemStack(GCItems.battery.setMaxDamage(0), 1, 0)));
           	    merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.meteoricIronIngot, 12), new ItemStack(MarsItems.marsItemBasic, 1, 6)));
           	    merchantrecipelist.add(new MerchantRecipe(new ItemStack(MarsItems.carbonFragments, 11), new ItemStack(Items.iron_ingot, 10, 0)));
           	     merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.basicItem, 12, 9), new ItemStack(GCItems.oxTankHeavy.setMaxDamage(2700), 1, 0))); 
    
                     }
                break;
               
            case 2:
            
               	 if (this.rand.nextFloat() < this.adjustProbability(0.9F))
                     {
               		 merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.cheeseCurd, 16), new ItemStack(GCItems.partBuggy, 1, 2)));
           		merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.basicItem, 11, 2), new ItemStack(GCItems.battery.setMaxDamage(0), 1, 0)));
           	    merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.meteoricIronIngot, 12), new ItemStack(MarsItems.marsItemBasic, 1, 6)));
           	    merchantrecipelist.add(new MerchantRecipe(new ItemStack(MarsItems.carbonFragments, 11), new ItemStack(Items.iron_ingot, 10, 0)));
           	     merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.basicItem, 12, 9), new ItemStack(GCItems.oxTankHeavy.setMaxDamage(2700), 1, 0))); 
            
                     }
                     
                Item[] aitem = new Item[] {};
                Item[] aitem1 = aitem;
                int j = aitem.length;
                k = 0; 
                

                while (true)
                {
                    if (k >= j)
                    {
                        break label50;
                    }

                    Item item = aitem1[k];

            

                    ++k;
                } 
               
            case 3:
 
               	 if (this.rand.nextFloat() < this.adjustProbability(0.9F))
                     {
               		 merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.cheeseCurd, 16), new ItemStack(GCItems.partBuggy, 1, 2)));
           		merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.basicItem, 11, 2), new ItemStack(GCItems.battery.setMaxDamage(0), 1, 0)));
           	    merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.meteoricIronIngot, 12), new ItemStack(MarsItems.marsItemBasic, 1, 6)));
           	    merchantrecipelist.add(new MerchantRecipe(new ItemStack(MarsItems.carbonFragments, 11), new ItemStack(Items.iron_ingot, 10, 0)));
           	     merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.basicItem, 12, 9), new ItemStack(GCItems.oxTankHeavy.setMaxDamage(2700), 1, 0))); 
    
                     }
                break;
               
           case 4:
        	   
           	 if (this.rand.nextFloat() < this.adjustProbability(0.9F))
                 {
           		 merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.cheeseCurd, 16), new ItemStack(GCItems.partBuggy, 1, 2)));
           		merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.basicItem, 11, 2), new ItemStack(GCItems.battery.setMaxDamage(0), 1, 0)));
           	    merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.meteoricIronIngot, 12), new ItemStack(MarsItems.marsItemBasic, 1, 6)));
           	    merchantrecipelist.add(new MerchantRecipe(new ItemStack(MarsItems.carbonFragments, 11), new ItemStack(Items.iron_ingot, 10, 0)));
           	     merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.basicItem, 12, 9), new ItemStack(GCItems.oxTankHeavy.setMaxDamage(2700), 1, 0))); 
           		
                 }
        }

        if (merchantrecipelist.isEmpty())
        {
            merchantrecipelist.add(new MerchantRecipe(new ItemStack(GCItems.cheeseCurd, 16), new ItemStack(GCItems.partBuggy, 1, 2)));
        }

        Collections.shuffle(merchantrecipelist);

        if (this.buyingList == null)
        {
            this.buyingList = new MerchantRecipeList();
        }

        for (int l = 0; l < p_70950_1_ && l < merchantrecipelist.size(); ++l)
        {
            this.buyingList.addToListWithCheck((MerchantRecipe)merchantrecipelist.get(l));
        }
    }

    @SideOnly(Side.CLIENT)
    public void setRecipes(MerchantRecipeList p_70930_1_) {}

    public static void func_146091_a(MerchantRecipeList p_146091_0_, Item p_146091_1_, Random p_146091_2_, float p_146091_3_)
    {
        if (p_146091_2_.nextFloat() < p_146091_3_)
        {
            p_146091_0_.add(new MerchantRecipe(func_146088_a(p_146091_1_, p_146091_2_), Items.emerald));
        }
    }

    private static ItemStack func_146088_a(Item p_146088_0_, Random p_146088_1_)
    {
        return new ItemStack(p_146088_0_, func_146092_b(p_146088_0_, p_146088_1_), 0);
    }

    private static int func_146092_b(Item p_146092_0_, Random p_146092_1_)
    {
        Tuple tuple = (Tuple)villagersSellingList.get(p_146092_0_);
        return tuple == null ? 1 : (((Integer)tuple.getFirst()).intValue() >= ((Integer)tuple.getSecond()).intValue() ? ((Integer)tuple.getFirst()).intValue() : ((Integer)tuple.getFirst()).intValue() + p_146092_1_.nextInt(((Integer)tuple.getSecond()).intValue() - ((Integer)tuple.getFirst()).intValue()));
    }

    public static void func_146089_b(MerchantRecipeList p_146089_0_, Item p_146089_1_, Random p_146089_2_, float p_146089_3_)
    {
        if (p_146089_2_.nextFloat() < p_146089_3_)
        {
            int i = func_146090_c(p_146089_1_, p_146089_2_);
            ItemStack itemstack;
            ItemStack itemstack1;

            if (i < 0)
            {
                itemstack = new ItemStack(Items.emerald, 1, 0);
                itemstack1 = new ItemStack(p_146089_1_, -i, 0);
            }
            else
            {
                itemstack = new ItemStack(Items.emerald, i, 0);
                itemstack1 = new ItemStack(p_146089_1_, 1, 0);
            }

            p_146089_0_.add(new MerchantRecipe(itemstack, itemstack1));
        }
    }

    private static int func_146090_c(Item p_146090_0_, Random p_146090_1_)
    {
        Tuple tuple = (Tuple)blacksmithSellingList.get(p_146090_0_);
        return tuple == null ? 1 : (((Integer)tuple.getFirst()).intValue() >= ((Integer)tuple.getSecond()).intValue() ? ((Integer)tuple.getFirst()).intValue() : ((Integer)tuple.getFirst()).intValue() + p_146090_1_.nextInt(((Integer)tuple.getSecond()).intValue() - ((Integer)tuple.getFirst()).intValue()));
    }

    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte p_70103_1_)
    {
        if (p_70103_1_ == 12)
        {
            this.generateRandomParticles("heart");
        }
        else if (p_70103_1_ == 13)
        {
            this.generateRandomParticles("angryVillager");
        }
        else if (p_70103_1_ == 14)
        {
            this.generateRandomParticles("happyVillager");
        }
        else
        {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_)
    {
        p_110161_1_ = super.onSpawnWithEgg(p_110161_1_);
        VillagerRegistry.applyRandomTrade(this, worldObj.rand);
        return p_110161_1_;
    }

    /**
     * par1 is the particleName
     */
    @SideOnly(Side.CLIENT)
    private void generateRandomParticles(String p_70942_1_)
    {
        for (int i = 0; i < 5; ++i)
        {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle(p_70942_1_, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 1.0D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
        }
    }

    public void setLookingForHome()
    {
        this.isLookingForHome = true;
    }

    public boolean allowLeashing()
    {
        return false;
    }

    static
    {
        //villager selling list
    
        
    }
   //oxygen
	@Override
	public boolean canBreath() {
		
		return true;
	}
}
