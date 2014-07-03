package micdoodle8.mods.galacticraft.planets.asteroids.entities;

import java.util.List;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityGrapple extends Entity implements IProjectile
{
	private BlockVec3 hitVec;
	private Block hitBlock;
	private int inData;
	private boolean inGround;
	public int canBePickedUp;
	public int arrowShake;
	public EntityPlayer shootingEntity;
	public int shootingEntityID;
	private int ticksInGround;
	private int ticksInAir;
	public float rotationRoll;
	public float prevRotationRoll;
	public boolean pullingPlayer;

	public EntityGrapple(World par1World)
	{
		super(par1World);
		this.renderDistanceWeight = 10.0D;
		this.ignoreFrustumCheck = false;
		this.setSize(0.5F, 0.5F);
	}

	public EntityGrapple(World par1World, EntityPlayer shootingEntity, float par3)
	{
		super(par1World);
		this.renderDistanceWeight = 10.0D;
		this.shootingEntity = shootingEntity;

		if (shootingEntity instanceof EntityPlayer)
		{
			this.canBePickedUp = 1;
		}

		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(shootingEntity.posX, shootingEntity.posY + shootingEntity.getEyeHeight(), shootingEntity.posZ, shootingEntity.rotationYaw, shootingEntity.rotationPitch);
		this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
		this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
		this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI);
		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0F;
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, par3 * 1.5F, 1.0F);
	}

	@Override
	protected void entityInit()
	{
		this.dataWatcher.addObject(10, 0);
		this.dataWatcher.addObject(11, 0);
	}

	@Override
	public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8)
	{
		float f2 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
		par1 /= f2;
		par3 /= f2;
		par5 /= f2;
		par1 += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * par8;
		par3 += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * par8;
		par5 += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * par8;
		par1 *= par7;
		par3 *= par7;
		par5 *= par7;
		this.motionX = par1;
		this.motionY = par3;
		this.motionZ = par5;
		float f3 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
		this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(par3, f3) * 180.0D / Math.PI);
		this.ticksInGround = 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
	{
		this.setPosition(par1, par3, par5);
		this.setRotation(par7, par8);
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
	public void onUpdate()
	{
		super.onUpdate();

		this.prevRotationRoll = this.rotationRoll;

		if (!this.worldObj.isRemote)
		{
			this.updateShootingEntity();
		}
		else
		{
			if (this.getPullingEntity())
			{
				EntityPlayer shootingEntity = this.getShootingEntity();
				if (shootingEntity != null)
				{
					shootingEntity.setVelocity((this.posX - shootingEntity.posX) / 16.0F, (this.posY - shootingEntity.posY) / 16.0F, (this.posZ - shootingEntity.posZ) / 16.0F);

					double deltaPositionX = shootingEntity.posX - shootingEntity.lastTickPosX;
					double deltaPositionY = shootingEntity.posY - shootingEntity.lastTickPosY;
					double deltaPositionZ = shootingEntity.posZ - shootingEntity.lastTickPosZ;
					double deltaPositionSqrd = deltaPositionX * deltaPositionX + deltaPositionY * deltaPositionY + deltaPositionZ * deltaPositionZ;

					FMLLog.info("" + deltaPositionSqrd);
					if (deltaPositionSqrd < 0.01 && this.pullingPlayer)
					{
						this.updatePullingEntity(false);
						this.setDead();
					}

					this.pullingPlayer = true;
				}

			}
		}

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, f) * 180.0D / Math.PI);
		}

		if (this.hitVec != null)
		{
			Block block = this.worldObj.getBlock(this.hitVec.x, this.hitVec.y, this.hitVec.z);

			if (block.getMaterial() != Material.air)
			{
				block.setBlockBoundsBasedOnState(this.worldObj, this.hitVec.x, this.hitVec.y, this.hitVec.z);
				AxisAlignedBB axisalignedbb = block.getCollisionBoundingBoxFromPool(this.worldObj, this.hitVec.x, this.hitVec.y, this.hitVec.z);

				if (axisalignedbb != null && axisalignedbb.isVecInside(this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ)))
				{
					this.inGround = true;
				}
			}
		}

		if (this.arrowShake > 0)
		{
			--this.arrowShake;
		}

		if (this.inGround)
		{
			if (this.hitVec != null)
			{
				Block block = this.worldObj.getBlock(this.hitVec.x, this.hitVec.y, this.hitVec.z);
				int j = this.worldObj.getBlockMetadata(this.hitVec.x, this.hitVec.y, this.hitVec.z);

				if (block == this.hitBlock && j == this.inData)
				{
					if (this.shootingEntity != null)
					{
						this.shootingEntity.setVelocity((this.posX - this.shootingEntity.posX) / 16.0F, (this.posY - this.shootingEntity.posY) / 16.0F, (this.posZ - this.shootingEntity.posZ) / 16.0F);
					}

					if (!this.worldObj.isRemote)
					{
						this.updatePullingEntity(true);
					}

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
		}
		else
		{
			this.rotationRoll += 5;
			++this.ticksInAir;

			if (!this.worldObj.isRemote)
			{
				this.updatePullingEntity(false);
			}

			if (this.shootingEntity != null && this.getDistanceSqToEntity(this.shootingEntity) >= 40 * 40)
			{
				this.setDead();
			}

			Vec3 vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
			Vec3 vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			MovingObjectPosition movingobjectposition = this.worldObj.func_147447_a(vec31, vec3, false, true, false);
			vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
			vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

			if (movingobjectposition != null)
			{
				vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
			}

			Entity entity = null;
			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;
			int i;
			float f1;

			for (i = 0; i < list.size(); ++i)
			{
				Entity entity1 = (Entity) list.get(i);

				if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5))
				{
					f1 = 0.3F;
					AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand(f1, f1, f1);
					MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);

					if (movingobjectposition1 != null)
					{
						double d1 = vec31.distanceTo(movingobjectposition1.hitVec);

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

				if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !this.shootingEntity.canAttackPlayer(entityplayer))
				{
					movingobjectposition = null;
				}
			}

			float motion;

			if (movingobjectposition != null)
			{
				if (movingobjectposition.entityHit == null)
				{
					this.hitVec = new BlockVec3(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
					this.hitBlock = this.worldObj.getBlock(this.hitVec.x, this.hitVec.y, this.hitVec.z);
					this.inData = this.worldObj.getBlockMetadata(this.hitVec.x, this.hitVec.y, this.hitVec.z);
					this.motionX = (float) (movingobjectposition.hitVec.xCoord - this.posX);
					this.motionY = (float) (movingobjectposition.hitVec.yCoord - this.posY);
					this.motionZ = (float) (movingobjectposition.hitVec.zCoord - this.posZ);
					motion = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
					this.posX -= this.motionX / motion * 0.05000000074505806D;
					this.posY -= this.motionY / motion * 0.05000000074505806D;
					this.posZ -= this.motionZ / motion * 0.05000000074505806D;
					this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					this.inGround = true;
					this.arrowShake = 7;

					if (this.hitBlock.getMaterial() != Material.air)
					{
						this.hitBlock.onEntityCollidedWithBlock(this.worldObj, this.hitVec.x, this.hitVec.y, this.hitVec.z, this);
					}
				}
			}

			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			motion = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.rotationPitch = (float) (Math.atan2(this.motionY, motion) * 180.0D / Math.PI);

            while (this.rotationPitch - this.prevRotationPitch < -180.0F)
            {
                this.prevRotationPitch -= 360.0F;
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
			float f3 = 0.99F;
			f1 = 0.05F;

			if (this.isInWater())
			{
				float f4 = 0.25F;
				for (int l = 0; l < 4; ++l)
				{
					this.worldObj.spawnParticle("bubble", this.posX - this.motionX * f4, this.posY - this.motionY * f4, this.posZ - this.motionZ * f4, this.motionX, this.motionY, this.motionZ);
				}

				f3 = 0.8F;
			}

			if (this.isWet())
			{
				this.extinguish();
			}

			//            this.motionX *= (double)f3;
			//            this.motionY *= (double)f3;
			//            this.motionZ *= (double)f3;
			//            this.motionY -= (double)f1;
			this.setPosition(this.posX, this.posY, this.posZ);
			this.func_145775_I();
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		par1NBTTagCompound.setShort("xTile", (short) this.hitVec.x);
		par1NBTTagCompound.setShort("yTile", (short) this.hitVec.y);
		par1NBTTagCompound.setShort("zTile", (short) this.hitVec.z);
		par1NBTTagCompound.setShort("life", (short) this.ticksInGround);
		par1NBTTagCompound.setByte("inTile", (byte) Block.getIdFromBlock(this.hitBlock));
		par1NBTTagCompound.setByte("inData", (byte) this.inData);
		par1NBTTagCompound.setByte("shake", (byte) this.arrowShake);
		par1NBTTagCompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
		par1NBTTagCompound.setByte("pickup", (byte) this.canBePickedUp);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		this.hitVec = new BlockVec3(par1NBTTagCompound.getShort("xTile"), par1NBTTagCompound.getShort("yTile"), par1NBTTagCompound.getShort("zTile"));
		this.ticksInGround = par1NBTTagCompound.getShort("life");
		this.hitBlock = Block.getBlockById(par1NBTTagCompound.getByte("inTile") & 255);
		this.inData = par1NBTTagCompound.getByte("inData") & 255;
		this.arrowShake = par1NBTTagCompound.getByte("shake") & 255;
		this.inGround = par1NBTTagCompound.getByte("inGround") == 1;

		if (par1NBTTagCompound.hasKey("pickup", 99))
		{
			this.canBePickedUp = par1NBTTagCompound.getByte("pickup");
		}
		else if (par1NBTTagCompound.hasKey("player", 99))
		{
			this.canBePickedUp = par1NBTTagCompound.getBoolean("player") ? 1 : 0;
		}
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
	{
		if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0)
		{
			boolean flag = this.canBePickedUp == 1 || this.canBePickedUp == 2 && par1EntityPlayer.capabilities.isCreativeMode;

			if (this.canBePickedUp == 1 && !par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(AsteroidsItems.grapple, 1)))
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
	protected boolean canTriggerWalking()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize()
	{
		return 0.0F;
	}

	@Override
	public boolean canAttackWithItem()
	{
		return false;
	}

	private void updateShootingEntity()
	{
		if (this.shootingEntity != null)
		{
			this.dataWatcher.updateObject(10, this.shootingEntity.getEntityId());
		}
	}

	public EntityPlayer getShootingEntity()
	{
		Entity entity = this.worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(10));

		if (entity instanceof EntityPlayer)
		{
			return (EntityPlayer) entity;
		}

		return null;
	}

	public void updatePullingEntity(boolean pulling)
	{
		this.dataWatcher.updateObject(11, pulling ? 1 : 0);
	}

	public boolean getPullingEntity()
	{
		return this.dataWatcher.getWatchableObjectInt(11) == 1;
	}
}