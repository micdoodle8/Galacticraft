package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreEntitySkeleton.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreEntitySkeleton extends EntitySkeleton implements IEntityBreathable
{
	private static final ItemStack defaultHeldItem = new ItemStack(Item.bow, 1);

	public GCCoreEntitySkeleton(World par1World)
	{
		super(par1World);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIRestrictSun(this));
		this.tasks.addTask(3, new EntityAIFleeSun(this, 0.25F));
		this.tasks.addTask(4, new EntityAIArrowAttack(this, 0.25F, 30, 20));
		this.tasks.addTask(5, new EntityAIWander(this, 0.25F));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(25);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.35F);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
	}

	@Override
	public void fall(float var1)
	{
		;
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	public boolean isAIEnabled()
	{
		return true;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		return "mob.skeleton";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return "mob.skeletonhurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return "mob.skeletonhurt";
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns the item that this EntityLiving is holding, if any.
	 */
	public ItemStack getHeldItem()
	{
		return GCCoreEntitySkeleton.defaultHeldItem;
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	@Override
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.UNDEAD;
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
	{
		if (this.worldObj.isDaytime() && !this.worldObj.isRemote)
		{
			float f = this.getBrightness(1.0F);

			if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)))
			{
				boolean flag = true;
				ItemStack itemstack = this.getCurrentItemOrArmor(4);

				if (itemstack != null)
				{
					if (itemstack.isItemStackDamageable())
					{
						itemstack.setItemDamage(itemstack.getItemDamageForDisplay() + this.rand.nextInt(2));

						if (itemstack.getItemDamageForDisplay() >= itemstack.getMaxDamage())
						{
							this.renderBrokenItemStack(itemstack);
							this.setCurrentItemOrArmor(4, (ItemStack) null);
						}
					}

					flag = false;
				}

				if (flag)
				{
					this.setFire(8);
				}
			}
		}

		if (this.worldObj.isRemote && this.getSkeletonType() == 1)
		{
			this.setSize(0.72F, 2.34F);
		}

		super.onLivingUpdate();
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(DamageSource par1DamageSource)
	{
		super.onDeath(par1DamageSource);

		if (par1DamageSource.getSourceOfDamage() instanceof EntityArrow && par1DamageSource.getEntity() instanceof EntityPlayer)
		{
			final EntityPlayer var2 = (EntityPlayer) par1DamageSource.getEntity();
			final double var3 = var2.posX - this.posX;
			final double var5 = var2.posZ - this.posZ;

			if (var3 * var3 + var5 * var5 >= 2500.0D)
			{
				var2.triggerAchievement(AchievementList.snipeSkeleton);
			}
		}
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	@Override
	protected int getDropItemId()
	{
		return Item.arrow.itemID;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean par1, int par2)
	{
		int var3 = this.rand.nextInt(3 + par2);
		int var4;

		for (var4 = 0; var4 < var3; ++var4)
		{
			this.dropItem(Item.arrow.itemID, 1);
		}

		var3 = this.rand.nextInt(3 + par2);

		for (var4 = 0; var4 < var3; ++var4)
		{
			this.dropItem(Item.bone.itemID, 1);
		}
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
}
