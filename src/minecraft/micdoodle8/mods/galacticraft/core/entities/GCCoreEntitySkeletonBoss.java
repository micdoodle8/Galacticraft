package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.API.IDungeonBoss;
import micdoodle8.mods.galacticraft.API.IDungeonBossSpawner;
import micdoodle8.mods.galacticraft.API.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreEntitySkeletonBoss extends EntitySkeleton implements IEntityBreathable, IDungeonBoss
{
    private static final ItemStack defaultHeldItem = new ItemStack(Item.bow, 1);
    private IDungeonBossSpawner spawner;

    public GCCoreEntitySkeletonBoss(World par1World)
    {
        super(par1World);
        this.setSize(1.5F, 4.0F);
        this.tasks.taskEntries.clear();
        this.texture = "/micdoodle8/mods/galacticraft/core/client/entities/ddasdasd.png";
        this.moveSpeed = 0.25F;
        this.tasks.addTask(1, new GCCoreEntityAIThrowPlayer(this));
//        this.tasks.addTask(1, new EntityAISwimming(this));
//        this.tasks.addTask(2, new EntityAIRestrictSun(this));
//        this.tasks.addTask(3, new EntityAIFleeSun(this, this.moveSpeed));
//        this.tasks.addTask(4, new GCCoreEntityAIArrowAttack(this, this.moveSpeed, 1, 20));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
//        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.taskEntries.clear();
//        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
//        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
    }
    
    public GCCoreEntitySkeletonBoss(World world, IDungeonBossSpawner spawner, Vector3 vec)
    {
    	this(world);
    	this.spawner = spawner;
    	this.setPosition(vec.x, vec.y, vec.z);
    }

    @Override
	public boolean isAIEnabled()
    {
        return true;
    }

    @Override
	public int getMaxHealth()
    {
        return 150;
    }

    @Override
	protected String getLivingSound()
    {
        return "mob.skeleton";
    }

    @Override
	protected String getHurtSound()
    {
        return "mob.skeletonhurt";
    }

    @Override
	protected String getDeathSound()
    {
        return "mob.skeletonhurt";
    }

    @Override
	@SideOnly(Side.CLIENT)
    public ItemStack getHeldItem()
    {
        return GCCoreEntitySkeletonBoss.defaultHeldItem;
    }

    @Override
	public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
	public void onLivingUpdate()
    {
        if (this.worldObj.isDaytime() && !this.worldObj.isRemote)
        {
            final float var1 = this.getBrightness(1.0F);

            if (var1 > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F)
            {
                this.setFire(8);
            }
        }

        super.onLivingUpdate();
    }

    @Override
	public void onDeath(DamageSource par1DamageSource)
    {
        super.onDeath(par1DamageSource);

        if (par1DamageSource.getSourceOfDamage() instanceof EntityArrow && par1DamageSource.getEntity() instanceof EntityPlayer)
        {
            final EntityPlayer var2 = (EntityPlayer)par1DamageSource.getEntity();
            final double var3 = var2.posX - this.posX;
            final double var5 = var2.posZ - this.posZ;

            if (var3 * var3 + var5 * var5 >= 2500.0D)
            {
                var2.triggerAchievement(AchievementList.snipeSkeleton);
            }
        }
    }

    @Override
	protected int getDropItemId()
    {
        return Item.arrow.itemID;
    }

    @Override
	protected void dropFewItems(boolean par1, int par2)
    {
        int var3 = this.rand.nextInt(3 + par2);
        int var4;

        this.entityDropItem(new ItemStack(GCCoreItems.key.itemID, 1, 0), 0.0F);
    }
    
    public EntityItem entityDropItem(ItemStack par1ItemStack, float par2)
    {
        EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY + (double)par2, this.posZ, par1ItemStack);
        entityitem.delayBeforeCanPickup = 10;
        if (captureDrops)
        {
            capturedDrops.add(entityitem);
        }
        else
        {
            this.worldObj.spawnEntityInWorld(entityitem);
        }
        return entityitem;
    }

    @Override
	protected void dropRareDrop(int par1)
    {
        if (par1 > 0)
        {
            final ItemStack var2 = new ItemStack(Item.bow);
            EnchantmentHelper.addRandomEnchantment(this.rand, var2, 5);
            this.entityDropItem(var2, 0.0F);
        }
        else
        {
            this.dropItem(Item.bow.itemID, 1);
        }
    }

	@Override
	public boolean canBreath()
	{
		return true;
	}

	@Override
	public float getExperienceToSpawn() 
	{
		return 50.0F;
	}

	@Override
	public double getDistanceToSpawn() 
	{
		return 20.0D;
	}

	@Override
	public void onBossSpawned(IDungeonBossSpawner spawner)
	{
		
	}
}
