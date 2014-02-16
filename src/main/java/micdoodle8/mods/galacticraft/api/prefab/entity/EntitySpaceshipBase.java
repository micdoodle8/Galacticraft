package micdoodle8.mods.galacticraft.api.prefab.entity;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.world.IExitHeight;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCConfigManager;
import micdoodle8.mods.galacticraft.core.util.GCDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Do not include this prefab class in your released mod download.
 */
public abstract class EntitySpaceshipBase extends Entity implements IPacketReceiver
{
	public static enum EnumLaunchPhase
	{
		UNIGNITED(1),
		IGNITED(2),
		LAUNCHED(3);

		private int phase;

		private EnumLaunchPhase(int phase)
		{
			this.phase = phase;
		}

		public int getPhase()
		{
			return this.phase;
		}
	}

	public int launchPhase = EnumLaunchPhase.UNIGNITED.getPhase();

	protected long ticks = 0;
	protected double dragAir;
	public int timeUntilLaunch;
	public float timeSinceLaunch;
	public float rollAmplitude;
	public float shipDamage;

	public EntitySpaceshipBase(World par1World)
	{
		super(par1World);
		this.preventEntitySpawning = true;
		this.ignoreFrustumCheck = true;
		this.renderDistanceWeight = 5.0D;
	}

	public abstract int getMaxFuel();

	public abstract int getScaledFuelLevel(int i);

	public abstract int getPreLaunchWait();

	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}

	@Override
	protected void entityInit()
	{
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity par1Entity)
	{
		return null;
	}

	@Override
	public AxisAlignedBB getBoundingBox()
	{
		return null;
	}

	@Override
	public boolean canBePushed()
	{
		return false;
	}

	@Override
	public void setDead()
	{
		if (this.riddenByEntity != null && this.riddenByEntity instanceof GCEntityPlayerMP)
		{
			GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_ZOOM_CAMERA, new Object[] { 0 }), (EntityPlayerMP)this.riddenByEntity);
		}

		super.setDead();
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
	{
		if (!this.worldObj.isRemote && !this.isDead)
		{
			if (this.isEntityInvulnerable() || this.posY > 300)
			{
				return false;
			}
			else
			{
				this.rollAmplitude = 10;
				this.setBeenAttacked();
				this.shipDamage += par2 * 10;

				if (par1DamageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer) par1DamageSource.getEntity()).capabilities.isCreativeMode)
				{
					this.shipDamage = 100;
				}

				if (this.shipDamage > 90 && !this.worldObj.isRemote)
				{
					if (this.riddenByEntity != null)
					{
						GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_ZOOM_CAMERA, new Object[] { 0 }), (EntityPlayerMP)this.riddenByEntity);
						this.riddenByEntity.mountEntity(this);
					}

					this.setDead();
					this.dropShipAsItem();
					return true;
				}

				return true;
			}
		}
		else
		{
			return true;
		}
	}

	public void dropShipAsItem()
	{
		if (this.worldObj.isRemote)
		{
			return;
		}

		for (final ItemStack item : this.getItemsDropped(new ArrayList<ItemStack>()))
		{
			this.entityDropItem(item, 0);
		}
	}

	public abstract List<ItemStack> getItemsDropped(List<ItemStack> droppedItemList);

	@Override
	public void performHurtAnimation()
	{
		this.rollAmplitude = 5;
		this.shipDamage += this.shipDamage * 10;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return !this.isDead;
	}

	@Override
	public boolean shouldRiderSit()
	{
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onUpdate()
	{
		if (this.ticks >= Long.MAX_VALUE)
		{
			this.ticks = 1;
		}

		this.ticks++;

		super.onUpdate();

		if (this.riddenByEntity != null)
		{
			this.riddenByEntity.fallDistance = 0.0F;
		}

		if (this.posY > (this.worldObj.provider instanceof IExitHeight ? ((IExitHeight) this.worldObj.provider).getYCoordinateToTeleport() : 1200))
		{
			this.onReachAtmoshpere();
		}

		if (this.rollAmplitude > 0)
		{
			this.rollAmplitude--;
		}

		if (this.shipDamage > 0)
		{
			this.shipDamage--;
		}

		if (this.posY < 0.0D || this.posY > (this.worldObj.provider instanceof IExitHeight ? ((IExitHeight) this.worldObj.provider).getYCoordinateToTeleport() : 1200) + 10)
		{
			this.kill();
		}

		if (this.launchPhase == EnumLaunchPhase.UNIGNITED.getPhase())
		{
			this.timeUntilLaunch = this.getPreLaunchWait();
		}

		if (this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase())
		{
			this.timeSinceLaunch++;
		}
		else
		{
			this.timeSinceLaunch = 0;
		}

		if (this.timeUntilLaunch > 0 && this.launchPhase == EnumLaunchPhase.IGNITED.getPhase())
		{
			this.timeUntilLaunch--;
		}

		AxisAlignedBB box = null;

		box = this.boundingBox.expand(0.2D, 0.2D, 0.2D);

		final List<?> var15 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, box);

		if (var15 != null && !var15.isEmpty())
		{
			for (int var52 = 0; var52 < var15.size(); ++var52)
			{
				final Entity var17 = (Entity) var15.get(var52);

				if (var17 != this.riddenByEntity)
				{
					var17.applyEntityCollision(this);
				}
			}
		}

		if (this.timeUntilLaunch == 0 && this.launchPhase == EnumLaunchPhase.IGNITED.getPhase())
		{
			this.launchPhase = EnumLaunchPhase.LAUNCHED.getPhase();
			this.onLaunch();
		}

		if (this.rotationPitch > 90)
		{
			this.rotationPitch = 90;
		}

		if (this.rotationPitch < -90)
		{
			this.rotationPitch = -90;
		}

		this.motionX = -(50 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * 0.01 * Math.PI / 180.0D));
		this.motionZ = -(50 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * 0.01 * Math.PI / 180.0D));

		if (this.timeSinceLaunch > 50 && this.onGround)
		{
			this.failRocket();
		}

		if (this.launchPhase != EnumLaunchPhase.LAUNCHED.getPhase())
		{
			this.motionX = this.motionY = this.motionZ = 0.0F;
		}

		if (this.worldObj.isRemote)
		{
			this.setPosition(this.posX, this.posY, this.posZ);

			if (this.shouldMoveClientSide())
			{
				this.moveEntity(this.motionX, this.motionY, this.motionZ);
			}
		}
		else
		{
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
		}

		this.setRotation(this.rotationYaw, this.rotationPitch);

		if (this.worldObj.isRemote)
		{
			this.setPosition(this.posX, this.posY, this.posZ);
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (!this.worldObj.isRemote && this.ticks % 3 == 0)
		{
			GalacticraftCore.packetPipeline.sendToDimension(new PacketDynamic(this), this.worldObj.provider.dimensionId);
//			PacketDispatcher.sendPacketToAllInDimension(GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.getNetworkedData(new ArrayList())), this.worldObj.provider.dimensionId);
		}
	}

	protected boolean shouldMoveClientSide()
	{
		return true;
	}

	@Override
	public void decodePacketdata(ByteBuf buffer)
	{
		this.launchPhase = buffer.readInt();
		this.timeSinceLaunch = buffer.readFloat();
		this.timeUntilLaunch = buffer.readInt();
	}

	public void getNetworkedData(ArrayList<Object> list)
	{
		list.add(this.launchPhase);
		list.add(this.timeSinceLaunch);
		list.add(this.timeUntilLaunch);
	}

	public void turnYaw(float f)
	{
		this.rotationYaw += f;
	}

	public void turnPitch(float f)
	{
		this.rotationPitch += f;
	}

	protected void failRocket()
	{
		if (this.riddenByEntity != null)
		{
			this.riddenByEntity.attackEntityFrom(GCDamageSource.spaceshipCrash, (int) (4.0D * 20 + 1.0D));
		}

		if (!GCConfigManager.disableSpaceshipGrief)
		{
			this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 5, true);
		}

		this.setDead();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
	{
		this.setRotation(par7, par8);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("launchPhase", this.launchPhase);
		nbt.setInteger("timeUntilLaunch", this.timeUntilLaunch);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
		this.timeUntilLaunch = nbt.getInteger("timeUntilLaunch");

		boolean hasOldTags = false;

		// Backwards compatibility:
		if (nbt.func_150296_c().contains("launched"))
		{
			hasOldTags = true;

			boolean launched = nbt.getBoolean("launched");

			if (launched)
			{
				this.launchPhase = EnumLaunchPhase.LAUNCHED.getPhase();
			}
		}

		// Backwards compatibility:
		if (nbt.func_150296_c().contains("ignite"))
		{
			hasOldTags = true;

			int ignite = nbt.getInteger("ignite");

			if (ignite == 1)
			{
				this.launchPhase = EnumLaunchPhase.IGNITED.getPhase();
			}
		}

		// Backwards compatibility:
		if (hasOldTags)
		{
			if (this.launchPhase != EnumLaunchPhase.LAUNCHED.getPhase() && this.launchPhase != EnumLaunchPhase.IGNITED.getPhase())
			{
				this.launchPhase = EnumLaunchPhase.UNIGNITED.getPhase();
			}
		}
		else
		{
			this.launchPhase = nbt.getInteger("launchPhase");
		}
	}

	public boolean getLaunched()
	{
		return this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase();
	}

	public boolean canBeRidden()
	{
		return false;
	}

	public void ignite()
	{
		this.launchPhase = EnumLaunchPhase.IGNITED.getPhase();
	}

	@Override
	public double getMountedYOffset()
	{
		return -1.0D;
	}

	public void onLaunch()
	{
		;
	}

	public void onReachAtmoshpere()
	{
		;
	}

	@SideOnly(Side.CLIENT)
	public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12)
	{
	}

	@Override
	public boolean canRiderInteract()
	{
		return true;
	}

	public ResourceLocation getSpaceshipGui()
	{
		return GalacticraftRegistry.getResouceLocationForDimension(this.worldObj.provider.getClass());
	}
}
