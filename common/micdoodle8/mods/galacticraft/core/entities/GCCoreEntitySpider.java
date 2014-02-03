package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * GCCoreEntitySpider.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreEntitySpider extends EntitySpider implements IEntityBreathable
{
	public GCCoreEntitySpider(World par1World)
	{
		super(par1World);
		this.setSize(1.4F, 0.9F);
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(22.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(1.0F);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (!this.worldObj.isRemote)
		{
			this.setBesideClimbableBlock(this.isCollidedHorizontally);
		}
	}

	@Override
	public void fall(float var1)
	{
		;
	}

	/**
	 * Returns the Y offset from the entity's position for any entity riding
	 * this one.
	 */
	@Override
	public double getMountedYOffset()
	{
		return this.height * 0.75D - 0.5D;
	}

	/**
	 * Finds the closest player within 16 blocks to attack, or null if this
	 * Entity isn't interested in attacking (Animals, Spiders at day, peaceful
	 * PigZombies).
	 */
	@Override
	protected Entity findPlayerToAttack()
	{
		final float var1 = this.getBrightness(1.0F);

		if (var1 < 0.5F)
		{
			final double var2 = 16.0D;
			return this.worldObj.getClosestVulnerablePlayerToEntity(this, var2);
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		return "mob.spider.say";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return "mob.spider.say";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return "mob.spider.death";
	}

	/**
	 * Plays step sound at given x, y, z for the entity
	 */
	@Override
	protected void playStepSound(int par1, int par2, int par3, int par4)
	{
		this.playSound("mob.spider.step", 0.15F, 1.0F);
	}

	/**
	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden
	 * by each mob to define their attack.
	 */
	@Override
	protected void attackEntity(Entity par1Entity, float par2)
	{
		final float var3 = this.getBrightness(1.0F);

		if (var3 > 0.5F && this.rand.nextInt(100) == 0)
		{
			this.entityToAttack = null;
		}
		else
		{
			if (par2 > 2.0F && par2 < 6.0F && this.rand.nextInt(10) == 0)
			{
				if (this.onGround)
				{
					final double var4 = par1Entity.posX - this.posX;
					final double var6 = par1Entity.posZ - this.posZ;
					final float var8 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
					this.motionX = var4 / var8 * 0.5D * 0.800000011920929D + this.motionX * 0.20000000298023224D;
					this.motionZ = var6 / var8 * 0.5D * 0.800000011920929D + this.motionZ * 0.20000000298023224D;
					this.motionY = 0.4000000059604645D;
				}
			}
			else
			{
				super.attackEntity(par1Entity, par2);
			}
		}
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	@Override
	protected int getDropItemId()
	{
		return Item.silk.itemID;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean par1, int par2)
	{
		super.dropFewItems(par1, par2);

		if (par1 && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + par2) > 0))
		{
			this.dropItem(Item.spiderEye.itemID, 1);
		}
	}

	/**
	 * returns true if this entity is by a ladder, false otherwise
	 */
	@Override
	public boolean isOnLadder()
	{
		return this.isBesideClimbableBlock();
	}

	/**
	 * Sets the Entity inside a web block.
	 */
	@Override
	public void setInWeb()
	{
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	@Override
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.ARTHROPOD;
	}

	@Override
	public boolean isPotionApplicable(PotionEffect par1PotionEffect)
	{
		return par1PotionEffect.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(par1PotionEffect);
	}

	/**
	 * Returns true if the WatchableObject (Byte) is 0x01 otherwise returns
	 * false. The WatchableObject is updated using setBesideClimableBlock.
	 */
	@Override
	public boolean isBesideClimbableBlock()
	{
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	/**
	 * Updates the WatchableObject (Byte) created in entityInit(), setting it to
	 * 0x01 if par1 is true or 0x00 if it is false.
	 */
	@Override
	public void setBesideClimbableBlock(boolean par1)
	{
		byte var2 = this.dataWatcher.getWatchableObjectByte(16);

		if (par1)
		{
			var2 = (byte) (var2 | 1);
		}
		else
		{
			var2 &= -2;
		}

		this.dataWatcher.updateObject(16, Byte.valueOf(var2));
	}

	/**
	 * Initialize this creature.
	 */
	@Override
	public EntityLivingData onSpawnWithEgg(EntityLivingData par1EntityLivingData)
	{
		EntityLivingData par1EntityLivingData1 = super.onSpawnWithEgg(par1EntityLivingData);

		if (this.worldObj.rand.nextInt(100) == 0)
		{
			EntitySkeleton entityskeleton = new EntitySkeleton(this.worldObj);
			entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
			entityskeleton.onSpawnWithEgg((EntityLivingData) null);
			this.worldObj.spawnEntityInWorld(entityskeleton);
			entityskeleton.mountEntity(this);
		}

		return par1EntityLivingData1;
	}

	@Override
	public boolean canBreath()
	{
		return true;
	}
}
