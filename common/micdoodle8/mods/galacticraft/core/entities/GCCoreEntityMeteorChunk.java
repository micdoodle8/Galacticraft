package micdoodle8.mods.galacticraft.core.entities;

import java.util.List;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentThorns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet70GameEvent;
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
 * GCCoreEntityMeteorChunk.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreEntityMeteorChunk extends Entity implements IProjectile
{
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private int inTile;
	private int inData;
	private double damage = 1.6D;

	public int canBePickedUp;
	public Entity shootingEntity;
	private int ticksInGround;
	private int ticksInAir;
	private boolean inGround;

	private int knockbackStrength;

	public boolean isHot;

	public GCCoreEntityMeteorChunk(World world)
	{
		super(world);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.5F, 0.5F);
	}

	public GCCoreEntityMeteorChunk(World world, double x, double y, double z)
	{
		super(world);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.5F, 0.5F);
		this.setPosition(x, y, z);
		this.yOffset = 0.0F;
	}

	public GCCoreEntityMeteorChunk(World world, EntityLivingBase shootingEntity, EntityLivingBase target, float speed, float randMod)
	{
		super(world);
		this.renderDistanceWeight = 10.0D;
		this.shootingEntity = shootingEntity;

		if (shootingEntity instanceof EntityPlayer)
		{
			this.canBePickedUp = 1;
		}

		this.posY = shootingEntity.posY + shootingEntity.getEyeHeight() - 0.10000000149011612D;
		double d0 = target.posX - shootingEntity.posX;
		double d1 = target.boundingBox.minY + target.height / 3.0F - this.posY;
		double d2 = target.posZ - shootingEntity.posZ;
		double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);

		if (d3 >= 1.0E-7D)
		{
			float f2 = (float) (Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
			float f3 = (float) -(Math.atan2(d1, d3) * 180.0D / Math.PI);
			double d4 = d0 / d3;
			double d5 = d2 / d3;
			this.setLocationAndAngles(shootingEntity.posX + d4, this.posY, shootingEntity.posZ + d5, f2, f3);
			this.yOffset = 0.0F;
			float f4 = (float) d3 * 0.2F;
			this.setThrowableHeading(d0, d1 + f4, d2, speed, randMod);
		}
	}

	public GCCoreEntityMeteorChunk(World par1World, EntityLivingBase par2EntityLivingBase, float speed)
	{
		super(par1World);
		this.renderDistanceWeight = 10.0D;
		this.shootingEntity = par2EntityLivingBase;

		if (par2EntityLivingBase instanceof EntityPlayer)
		{
			this.canBePickedUp = 1;
		}

		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(par2EntityLivingBase.posX, par2EntityLivingBase.posY + par2EntityLivingBase.getEyeHeight(), par2EntityLivingBase.posZ, par2EntityLivingBase.rotationYaw, par2EntityLivingBase.rotationPitch);
		this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
		this.posY -= 0.10000000149011612D;
		this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0F;
		this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
		this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
		this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI);
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, speed * 1.5F, 1.0F);
	}

	@Override
	public void setThrowableHeading(double headingX, double headingY, double headingZ, float speed, float randMod)
	{
		float f2 = MathHelper.sqrt_double(headingX * headingX + headingY * headingY + headingZ * headingZ);
		headingX /= f2;
		headingY /= f2;
		headingZ /= f2;
		headingX += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * randMod;
		headingY += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * randMod;
		headingZ += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * randMod;
		headingX *= speed;
		headingY *= speed;
		headingZ *= speed;
		this.motionX = headingX;
		this.motionY = headingY;
		this.motionZ = headingZ;
		float f3 = MathHelper.sqrt_double(headingX * headingX + headingZ * headingZ);
		this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(headingX, headingZ) * 180.0D / Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(headingY, f3) * 180.0D / Math.PI);
		this.ticksInGround = 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double par1, double par3, double par5)
	{
		this.motionX = par1;
		this.motionY = par3;
		this.motionZ = par5;

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			float f = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
			this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(par3, f) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch;
			this.prevRotationYaw = this.rotationYaw;
			this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.ticksInGround = 0;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onUpdate()
	{
		super.onUpdate();

		if (this.ticksExisted > 400)
		{
			if (this.isHot)
			{
				this.isHot = false;
				this.setHot(this.isHot);
			}
		}
		else if (!this.worldObj.isRemote)
		{
			this.setHot(this.isHot);
		}

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, f) * 180.0D / Math.PI);
		}

		int i = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);

		if (i > 0)
		{
			Block.blocksList[i].setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
			AxisAlignedBB axisalignedbb = Block.blocksList[i].getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);

			if (axisalignedbb != null && axisalignedbb.isVecInside(this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ)))
			{
				this.inGround = true;
			}
		}

		if (this.inGround)
		{
			int j = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
			int k = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);

			if (j == this.inTile && k == this.inData)
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
			Vec3 vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
			Vec3 vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks_do_do(vec3, vec31, false, true);
			vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
			vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

			if (movingobjectposition != null)
			{
				vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
			}

			this.rotationPitch += 1F;

			Entity entity = null;
			List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;
			int l;
			float f1;

			for (l = 0; l < list.size(); ++l)
			{
				Entity entity1 = list.get(l);

				if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5))
				{
					f1 = 0.3F;
					AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand(f1, f1, f1);
					MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec3, vec31);

					if (movingobjectposition1 != null)
					{
						double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

						if (d1 < d0 || d0 == 0.0D)
						{
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}

			if (entity != null)
			{
				movingobjectposition = new MovingObjectPosition(entity);
			}

			if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;

				if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer))
				{
					movingobjectposition = null;
				}
			}

			float f2;
			float f3;

			if (movingobjectposition != null)
			{
				if (movingobjectposition.entityHit != null)
				{
					f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
					int i1 = MathHelper.ceiling_double_int(f2 * this.damage);

					DamageSource damagesource = null;

					if (this.shootingEntity == null)
					{
						damagesource = new EntityDamageSourceIndirect("meteorChunk", this, this).setProjectile();
					}
					else
					{
						damagesource = new EntityDamageSourceIndirect("meteorChunk", this, this.shootingEntity).setProjectile();
					}

					if (this.isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman))
					{
						movingobjectposition.entityHit.setFire(2);
					}

					if (movingobjectposition.entityHit.attackEntityFrom(damagesource, i1))
					{
						if (movingobjectposition.entityHit instanceof EntityLivingBase)
						{
							EntityLivingBase entitylivingbase = (EntityLivingBase) movingobjectposition.entityHit;

							if (!this.worldObj.isRemote)
							{
								entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
							}

							if (this.knockbackStrength > 0)
							{
								f3 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

								if (f3 > 0.0F)
								{
									movingobjectposition.entityHit.addVelocity(this.motionX * this.knockbackStrength * 0.6000000238418579D / f3, 0.1D, this.motionZ * this.knockbackStrength * 0.6000000238418579D / f3);
								}
							}

							if (this.shootingEntity != null)
							{
								EnchantmentThorns.func_92096_a(this.shootingEntity, entitylivingbase, this.rand);
							}

							if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
							{
								((EntityPlayerMP) this.shootingEntity).playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(6, 0));
							}
						}

						if (!(movingobjectposition.entityHit instanceof EntityEnderman))
						{
							this.setDead();
						}
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
					this.xTile = movingobjectposition.blockX;
					this.yTile = movingobjectposition.blockY;
					this.zTile = movingobjectposition.blockZ;
					this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
					this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
					this.motionX = (float) (movingobjectposition.hitVec.xCoord - this.posX);
					this.motionY = (float) (movingobjectposition.hitVec.yCoord - this.posY);
					this.motionZ = (float) (movingobjectposition.hitVec.zCoord - this.posZ);
					f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
					this.posX -= this.motionX / f2 * 0.05000000074505806D;
					this.posY -= this.motionY / f2 * 0.05000000074505806D;
					this.posZ -= this.motionZ / f2 * 0.05000000074505806D;
					this.inGround = true;

					if (this.inTile != 0)
					{
						Block.blocksList[this.inTile].onEntityCollidedWithBlock(this.worldObj, this.xTile, this.yTile, this.zTile, this);
					}
				}
			}

			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

			if (!this.onGround)
			{
				this.rotationPitch += 10;
				this.rotationYaw += 2;
			}

			float f4 = 0.99F;
			f1 = 0.05F;

			if (this.isInWater())
			{
				for (int j1 = 0; j1 < 4; ++j1)
				{
					f3 = 0.25F;
					this.worldObj.spawnParticle("bubble", this.posX - this.motionX * f3, this.posY - this.motionY * f3, this.posZ - this.motionZ * f3, this.motionX, this.motionY, this.motionZ);
				}

				f4 = 0.8F;
			}

			this.motionX *= f4;
			this.motionY *= f4;
			this.motionZ *= f4;
			this.motionY -= this.worldObj.provider instanceof IGalacticraftWorldProvider ? ((IGalacticraftWorldProvider) this.worldObj.provider).getGravity() - (double) f1 : (double) f1;
			this.setPosition(this.posX, this.posY, this.posZ);
			this.doBlockCollisions();
		}
	}

	@Override
	protected void entityInit()
	{
		this.dataWatcher.addObject(16, Integer.valueOf(0));
	}

	public boolean isHot()
	{
		return this.dataWatcher.getWatchableObjectInt(16) == 1;
	}

	public void setHot(boolean isHot)
	{
		this.dataWatcher.updateObject(16, isHot ? 1 : 0);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{

	}

	@Override
	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
	{
		if (!this.worldObj.isRemote && this.inGround)
		{
			boolean flag = this.canBePickedUp == 1 || this.canBePickedUp == 2 && par1EntityPlayer.capabilities.isCreativeMode;

			if (this.canBePickedUp == 1 && !par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(GCCoreItems.meteorChunk, 1, 0)))
			{
				flag = false;
			}

			if (flag)
			{
				this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				par1EntityPlayer.onItemPickup(this, 1);
				this.setDead();
			}
		}
	}

	@Override
	public boolean canAttackWithItem()
	{
		return false;
	}
}
