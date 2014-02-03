package micdoodle8.mods.galacticraft.core.entities;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreEntityArrow.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreEntityArrow extends Entity
{
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private int inTile = 0;
	private int inData = 0;
	private boolean inGround = false;

	/** 1 if the player can pick up the arrow */
	public int canBePickedUp = 0;

	/** Seems to be some sort of timer for animating an arrow. */
	public int arrowShake = 0;

	/** The owner of this arrow. */
	public Entity shootingEntity;
	private int ticksInGround;
	private int ticksInAir = 0;
	private double damage = 2.0D;

	/** The amount of knockback an arrow applies when it hits a mob. */
	private int knockbackStrength;

	public GCCoreEntityArrow(World par1World)
	{
		super(par1World);
		this.setSize(0.5F, 0.5F);
	}

	public GCCoreEntityArrow(World par1World, double par2, double par4, double par6)
	{
		super(par1World);
		this.setSize(0.5F, 0.5F);
		this.setPosition(par2, par4, par6);
		this.yOffset = 0.0F;
	}

	public GCCoreEntityArrow(World par1World, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving, float par4, float par5)
	{
		super(par1World);
		this.shootingEntity = par2EntityLiving;

		if (par2EntityLiving instanceof EntityPlayer)
		{
			this.canBePickedUp = 1;
		}

		this.posY = par2EntityLiving.posY + par2EntityLiving.getEyeHeight() - 0.10000000149011612D;
		final double var6 = par3EntityLiving.posX - par2EntityLiving.posX;
		final double var8 = par3EntityLiving.posY + par3EntityLiving.getEyeHeight() - 0.699999988079071D - this.posY;
		final double var10 = par3EntityLiving.posZ - par2EntityLiving.posZ;
		final double var12 = MathHelper.sqrt_double(var6 * var6 + var10 * var10);

		if (var12 >= 1.0E-7D)
		{
			final float var14 = (float) (Math.atan2(var10, var6) * 180.0D / Math.PI) - 90.0F;
			final float var15 = (float) -(Math.atan2(var8, var12) * 180.0D / Math.PI);
			final double var16 = var6 / var12;
			final double var18 = var10 / var12;
			this.setLocationAndAngles(par2EntityLiving.posX + var16, this.posY, par2EntityLiving.posZ + var18, var14, var15);
			this.yOffset = 0.0F;
			final float var20 = (float) var12 * 0.2F;
			this.setArrowHeading(var6, var8 + var20, var10, par4, par5);
		}
	}

	public GCCoreEntityArrow(World par1World, EntityLivingBase par2EntityLiving, float par3)
	{
		super(par1World);
		this.shootingEntity = par2EntityLiving;

		if (par2EntityLiving instanceof EntityPlayer)
		{
			this.canBePickedUp = 1;
		}

		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(par2EntityLiving.posX, par2EntityLiving.posY + par2EntityLiving.getEyeHeight(), par2EntityLiving.posZ, par2EntityLiving.rotationYaw, par2EntityLiving.rotationPitch);
		this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
		this.posY -= 0.10000000149011612D;
		this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0F;
		this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
		this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
		this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI);
		this.setArrowHeading(this.motionX, this.motionY, this.motionZ, par3 * 1.5F, 1.0F);
	}

	@Override
	protected void entityInit()
	{
		this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}

	/**
	 * Uses the provided coordinates as a heading and determines the velocity
	 * from it with the set force and random variance. Args: x, y, z, force,
	 * forceVariation
	 */
	public void setArrowHeading(double par1, double par3, double par5, float par7, float par8)
	{
		final float var9 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
		par1 /= var9;
		par3 /= var9;
		par5 /= var9;
		par1 += this.rand.nextGaussian() * 0.007499999832361937D * par8;
		par3 += this.rand.nextGaussian() * 0.007499999832361937D * par8;
		par5 += this.rand.nextGaussian() * 0.007499999832361937D * par8;
		par1 *= par7;
		par3 *= par7;
		par5 *= par7;
		this.motionX = par1;
		this.motionY = par3;
		this.motionZ = par5;
		final float var10 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
		this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(par3, var10) * 180.0D / Math.PI);
		this.ticksInGround = 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
	 * posY, posZ, yaw, pitch
	 */
	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
	{
		this.setPosition(par1, par3, par5);
		this.setRotation(par7, par8);
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Sets the velocity to the args. Args: x, y, z
	 */
	public void setVelocity(double par1, double par3, double par5)
	{
		this.motionX = par1;
		this.motionY = par3;
		this.motionZ = par5;

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			final float var7 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
			this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(par3, var7) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch;
			this.prevRotationYaw = this.rotationYaw;
			this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.ticksInGround = 0;
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			final float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, var1) * 180.0D / Math.PI);
		}

		final int var16 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);

		if (var16 > 0)
		{
			Block.blocksList[var16].setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
			final AxisAlignedBB var2 = Block.blocksList[var16].getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);

			if (var2 != null && var2.isVecInside(this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ)))
			{
				this.inGround = true;
			}
		}

		if (this.arrowShake > 0)
		{
			--this.arrowShake;
		}

		if (this.inGround)
		{
			final int var18 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
			final int var19 = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);

			if (var18 == this.inTile && var19 == this.inData)
			{
				++this.ticksInGround;

				if (this.ticksInGround == 1200)
				{
					this.setDead();
				}
			}
			else
			{
				this.inGround = false;
				this.motionX *= this.rand.nextFloat() * 0.2F;
				this.motionY *= this.rand.nextFloat() * 0.2F;
				this.motionZ *= this.rand.nextFloat() * 0.2F;
				this.ticksInGround = 0;
				this.ticksInAir = 0;
			}
		}
		else
		{
			++this.ticksInAir;
			Vec3 var17 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
			Vec3 var3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			MovingObjectPosition var4 = this.worldObj.rayTraceBlocks_do_do(var17, var3, false, true);
			var17 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
			var3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

			if (var4 != null)
			{
				var3 = this.worldObj.getWorldVec3Pool().getVecFromPool(var4.hitVec.xCoord, var4.hitVec.yCoord, var4.hitVec.zCoord);
			}

			Entity var5 = null;
			final List<?> var6 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double var7 = 0.0D;
			final Iterator<?> var9 = var6.iterator();
			float var11;

			while (var9.hasNext())
			{
				final Entity var10 = (Entity) var9.next();

				if (var10.canBeCollidedWith() && (var10 != this.shootingEntity || this.ticksInAir >= 5))
				{
					var11 = 0.3F;
					final AxisAlignedBB var12 = var10.boundingBox.expand(var11, var11, var11);
					final MovingObjectPosition var13 = var12.calculateIntercept(var17, var3);

					if (var13 != null)
					{
						final double var14 = var17.distanceTo(var13.hitVec);

						if (var14 < var7 || var7 == 0.0D)
						{
							var5 = var10;
							var7 = var14;
						}
					}
				}
			}

			if (var5 != null)
			{
				var4 = new MovingObjectPosition(var5);
			}

			float var20;

			if (var4 != null)
			{
				if (var4.entityHit != null)
				{
					var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
					int var24 = MathHelper.ceiling_double_int(var20 * this.damage);

					if (this.getIsCritical())
					{
						var24 += this.rand.nextInt(var24 / 2 + 2);
					}

					DamageSource var22 = null;

					if (this.shootingEntity == null)
					{
						var22 = GCCoreEntityArrow.causeArrowDamage(this, this);
					}
					else
					{
						var22 = GCCoreEntityArrow.causeArrowDamage(this, this.shootingEntity);
					}

					if (this.isBurning())
					{
						var4.entityHit.setFire(5);
					}

					if (var4.entityHit.attackEntityFrom(var22, var24))
					{
						if (var4.entityHit instanceof EntityLiving)
						{
							++((EntityLiving) var4.entityHit).arrowHitTimer;

							if (this.knockbackStrength > 0)
							{
								final float var25 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

								if (var25 > 0.0F)
								{
									var4.entityHit.addVelocity(this.motionX * this.knockbackStrength * 0.6000000238418579D / var25, 0.1D, this.motionZ * this.knockbackStrength * 0.6000000238418579D / var25);
								}
							}
						}

						this.worldObj.playSoundAtEntity(this, "random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
						this.setDead();
					}
					else
					{
						this.motionX *= -0.10000000149011612D;
						this.motionY *= -0.10000000149011612D;
						this.motionZ *= -0.10000000149011612D;
						this.rotationYaw += 180.0F;
						this.prevRotationYaw += 180.0F;
						this.ticksInAir = 0;
					}
				}
				else
				{
					this.xTile = var4.blockX;
					this.yTile = var4.blockY;
					this.zTile = var4.blockZ;
					this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
					this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
					this.motionX = (float) (var4.hitVec.xCoord - this.posX);
					this.motionY = (float) (var4.hitVec.yCoord - this.posY);
					this.motionZ = (float) (var4.hitVec.zCoord - this.posZ);
					var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
					this.posX -= this.motionX / var20 * 0.05000000074505806D;
					this.posY -= this.motionY / var20 * 0.05000000074505806D;
					this.posZ -= this.motionZ / var20 * 0.05000000074505806D;
					this.worldObj.playSoundAtEntity(this, "random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					this.inGround = true;
					this.arrowShake = 7;
					this.setIsCritical(false);
				}
			}

			if (this.getIsCritical())
			{
				for (int var21 = 0; var21 < 4; ++var21)
				{
					this.worldObj.spawnParticle("crit", this.posX + this.motionX * var21 / 4.0D, this.posY + this.motionY * var21 / 4.0D, this.posZ + this.motionZ * var21 / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
				}
			}

			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

			for (this.rotationPitch = (float) (Math.atan2(this.motionY, var20) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
			{
				;
			}

			while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
			{
				this.prevRotationPitch += 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw < -180.0F)
			{
				this.prevRotationYaw -= 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
			{
				this.prevRotationYaw += 360.0F;
			}

			this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
			this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
			float var23 = 0.99F;
			var11 = 0.002F;

			if (this.isInWater())
			{
				for (int var26 = 0; var26 < 4; ++var26)
				{
					final float var27 = 0.25F;
					this.worldObj.spawnParticle("bubble", this.posX - this.motionX * var27, this.posY - this.motionY * var27, this.posZ - this.motionZ * var27, this.motionX, this.motionY, this.motionZ);
				}

				var23 = 0.8F;
			}

			this.motionX *= var23;
			this.motionY *= var23;
			this.motionZ *= var23;
			this.motionY -= var11;
			this.setPosition(this.posX, this.posY, this.posZ);
			this.doBlockCollisions();
		}
	}

	public static DamageSource causeArrowDamage(GCCoreEntityArrow par0EntityArrow, Entity par1Entity)
	{
		return new EntityDamageSourceIndirect("arrow", par0EntityArrow, par1Entity).setProjectile();
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		par1NBTTagCompound.setShort("xTile", (short) this.xTile);
		par1NBTTagCompound.setShort("yTile", (short) this.yTile);
		par1NBTTagCompound.setShort("zTile", (short) this.zTile);
		par1NBTTagCompound.setByte("inTile", (byte) this.inTile);
		par1NBTTagCompound.setByte("inData", (byte) this.inData);
		par1NBTTagCompound.setByte("shake", (byte) this.arrowShake);
		par1NBTTagCompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
		par1NBTTagCompound.setByte("pickup", (byte) this.canBePickedUp);
		par1NBTTagCompound.setDouble("damage", this.damage);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		this.xTile = par1NBTTagCompound.getShort("xTile");
		this.yTile = par1NBTTagCompound.getShort("yTile");
		this.zTile = par1NBTTagCompound.getShort("zTile");
		this.inTile = par1NBTTagCompound.getByte("inTile") & 255;
		this.inData = par1NBTTagCompound.getByte("inData") & 255;
		this.arrowShake = par1NBTTagCompound.getByte("shake") & 255;
		this.inGround = par1NBTTagCompound.getByte("inGround") == 1;

		if (par1NBTTagCompound.hasKey("damage"))
		{
			this.damage = par1NBTTagCompound.getDouble("damage");
		}

		if (par1NBTTagCompound.hasKey("pickup"))
		{
			this.canBePickedUp = par1NBTTagCompound.getByte("pickup");
		}
		else if (par1NBTTagCompound.hasKey("player"))
		{
			this.canBePickedUp = par1NBTTagCompound.getBoolean("player") ? 1 : 0;
		}
	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	@Override
	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
	{
		if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0)
		{
			boolean var2 = this.canBePickedUp == 1 || this.canBePickedUp == 2 && par1EntityPlayer.capabilities.isCreativeMode;

			if (this.canBePickedUp == 1 && !par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(Item.arrow, 1)))
			{
				var2 = false;
			}

			if (var2)
			{
				this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				par1EntityPlayer.onItemPickup(this, 1);
				this.setDead();
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize()
	{
		return 0.0F;
	}

	public void setDamage(double par1)
	{
		this.damage = par1;
	}

	public double getDamage()
	{
		return this.damage;
	}

	/**
	 * Sets the amount of knockback the arrow applies when it hits a mob.
	 */
	public void setKnockbackStrength(int par1)
	{
		this.knockbackStrength = par1;
	}

	/**
	 * If returns false, the item will not inflict any damage against entities.
	 */
	@Override
	public boolean canAttackWithItem()
	{
		return false;
	}

	/**
	 * Whether the arrow has a stream of critical hit particles flying behind
	 * it.
	 */
	public void setIsCritical(boolean par1)
	{
		final byte var2 = this.dataWatcher.getWatchableObjectByte(16);

		if (par1)
		{
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 | 1)));
		}
		else
		{
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 & -2)));
		}
	}

	/**
	 * Whether the arrow has a stream of critical hit particles flying behind
	 * it.
	 */
	public boolean getIsCritical()
	{
		final byte var1 = this.dataWatcher.getWatchableObjectByte(16);
		return (var1 & 1) != 0;
	}
}
