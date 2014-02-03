package micdoodle8.mods.galacticraft.core.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketEntityUpdate;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumPacketClient;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreEntityAdvanced.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public abstract class GCCoreEntityAdvanced extends GCCoreEntityControllable implements IPacketReceiver
{
	protected long ticks = 0;

	public float currentDamage;
	public int timeSinceHit;
	public int rockDirection;

	public double advancedPositionX;
	public double advancedPositionY;
	public double advancedPositionZ;
	public double advancedYaw;
	public double advancedPitch;
	public int posRotIncrements;

	protected boolean lastOnGround;

	public GCCoreEntityAdvanced(World world, double initialSpeed, float yOffset)
	{
		super(world);
		this.preventEntitySpawning = true;
		this.ignoreFrustumCheck = true;
		this.isImmuneToFire = true;
		this.motionY = initialSpeed;
		this.yOffset = yOffset;
	}

	public GCCoreEntityAdvanced(World world, double initialSpeed, float yOffset, double var2, double var4, double var6)
	{
		this(world, initialSpeed, yOffset);
		this.yOffset = yOffset;
		this.setPosition(var2, var4 + this.yOffset, var6);
	}

	@Override
	protected void entityInit()
	{
	}

	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox()
	{
		return this.boundingBox;
	}

	@Override
	public boolean canBePushed()
	{
		return false;
	}

	@Override
	public double getMountedYOffset()
	{
		return this.height - 1.0D;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return !this.isDead;
	}

	@Override
	public void updateRiderPosition()
	{
		if (this.riddenByEntity != null)
		{
			final double var1 = Math.cos(this.rotationYaw * Math.PI / 180.0D + 114.8) * -0.5D;
			final double var3 = Math.sin(this.rotationYaw * Math.PI / 180.0D + 114.8) * -0.5D;
			this.riddenByEntity.setPosition(this.posX + var1, this.posY + this.riddenByEntity.getYOffset(), this.posZ + var3);
		}
	}

	@Override
	public void setPositionRotationAndMotion(double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ, boolean onGround)
	{
		if (this.worldObj.isRemote)
		{
			this.advancedPositionX = x;
			this.advancedPositionY = y;
			this.advancedPositionZ = z;
			this.advancedYaw = yaw;
			this.advancedPitch = pitch;
			this.motionX = motX;
			this.motionY = motY;
			this.motionZ = motZ;
			this.posRotIncrements = 5;
		}
		else
		{
			this.setPosition(x, y, z);
			this.setRotation(yaw, pitch);
			this.motionX = motX;
			this.motionY = motY;
			this.motionZ = motZ;
			if (onGround || this.forceGroundUpdate())
			{
				this.onGround = onGround;
			}
		}
	}

	protected boolean forceGroundUpdate()
	{
		return true;
	}

	@Override
	public void performHurtAnimation()
	{
		this.rockDirection = -this.rockDirection;
		this.timeSinceHit = 10;
		this.currentDamage *= 5;
	}

	@Override
	public boolean attackEntityFrom(DamageSource var1, float var2)
	{
		if (this.isDead || var1.equals(DamageSource.cactus) || !this.allowDamageSource(var1))
		{
			return true;
		}
		else
		{
			this.rockDirection = -this.rockDirection;
			this.timeSinceHit = 10;
			this.currentDamage = this.currentDamage + var2 * 10;
			this.setBeenAttacked();

			if (var1.getEntity() instanceof EntityPlayer && ((EntityPlayer) var1.getEntity()).capabilities.isCreativeMode)
			{
				this.currentDamage = 100;
			}

			if (this.currentDamage > 70)
			{
				if (this.riddenByEntity != null)
				{
					if (this.riddenByEntity instanceof EntityPlayerMP)
					{
						final Object[] toSend2 = { 0 };
						((EntityPlayerMP) this.riddenByEntity).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.ZOOM_CAMERA, toSend2));
					}

					this.riddenByEntity.mountEntity(this);

					return false;
				}

				if (!this.worldObj.isRemote)
				{
					if (this.riddenByEntity != null)
					{
						this.riddenByEntity.mountEntity(this);
					}

					this.dropItems();

					this.setDead();
				}
			}

			return true;
		}
	}

	public abstract List<ItemStack> getItemsDropped();

	public abstract boolean shouldMove();

	public abstract boolean shouldSpawnParticles();

	/**
	 * @return map of the particle vectors. Map key is the position and map
	 *         value is the motion of the particles. Each entry will be spawned
	 *         as a separate particle
	 */
	public abstract Map<Vector3, Vector3> getParticleMap();

	@SideOnly(Side.CLIENT)
	public abstract EntityFX getParticle(Random rand, double x, double y, double z, double motX, double motY, double motZ);

	public abstract void tickInAir();

	public abstract void tickOnGround();

	public abstract void onGroundHit();

	public abstract Vector3 getMotionVec();

	public abstract ArrayList<Object> getNetworkedData();

	/**
	 * @return ticks between packets being sent to client
	 */
	public abstract int getPacketTickSpacing();

	/**
	 * @return players within this distance will recieve packets from this
	 *         entity
	 */
	public abstract double getPacketSendDistance();

	public abstract void readNetworkedData(ByteArrayDataInput dataStream);

	public abstract boolean allowDamageSource(DamageSource damageSource);

	public void dropItems()
	{
		if (this.getItemsDropped() == null)
		{
			return;
		}

		for (final ItemStack item : this.getItemsDropped())
		{
			if (item != null)
			{
				this.entityDropItem(item, 0);
			}
		}
	}

	@Override
	public void setPositionAndRotation2(double d, double d1, double d2, float f, float f1, int i)
	{
		if (this.riddenByEntity != null)
		{
			if (this.riddenByEntity instanceof EntityPlayer && FMLClientHandler.instance().getClient().thePlayer.equals(this.riddenByEntity))
			{
			}
			else
			{
				this.posRotIncrements = i + 5;
				this.advancedPositionX = d;
				this.advancedPositionY = d1 + (this.riddenByEntity == null ? 1 : 0);
				this.advancedPositionZ = d2;
				this.advancedYaw = f;
				this.advancedPitch = f1;
			}
		}
	}

	@Override
	public void moveEntity(double par1, double par3, double par5)
	{
		if (this.shouldMove())
		{
			super.moveEntity(par1, par3, par5);
		}
	}

	@Override
	public void onUpdate()
	{
		if (this.ticks >= Long.MAX_VALUE)
		{
			this.ticks = 1;
		}

		this.ticks++;

		super.onUpdate();

		if (this.worldObj.isRemote && (this.riddenByEntity == null || !(this.riddenByEntity instanceof EntityPlayer) || !FMLClientHandler.instance().getClient().thePlayer.equals(this.riddenByEntity)))
		{
			double x;
			double y;
			double var12;
			double z;
			if (this.posRotIncrements > 0)
			{
				x = this.posX + (this.advancedPositionX - this.posX) / this.posRotIncrements;
				y = this.posY + (this.advancedPositionY - this.posY) / this.posRotIncrements;
				z = this.posZ + (this.advancedPositionZ - this.posZ) / this.posRotIncrements;
				var12 = MathHelper.wrapAngleTo180_double(this.advancedYaw - this.rotationYaw);
				this.rotationYaw = (float) (this.rotationYaw + var12 / this.posRotIncrements);
				this.rotationPitch = (float) (this.rotationPitch + (this.advancedPitch - this.rotationPitch) / this.posRotIncrements);
				--this.posRotIncrements;
				this.setPosition(x, y, z);
				this.setRotation(this.rotationYaw, this.rotationPitch);
			}
			else
			{
				x = this.posX + this.motionX;
				y = this.posY + this.motionY;
				z = this.posZ + this.motionZ;
				this.setPosition(x, y, z);

				if (this.onGround)
				{
					this.motionX *= 0.5D;
					this.motionY *= 0.5D;
					this.motionZ *= 0.5D;
				}

				this.motionX *= 0.9900000095367432D;
				this.motionY *= 0.949999988079071D;
				this.motionZ *= 0.9900000095367432D;
			}
		}

		if (this.timeSinceHit > 0)
		{
			this.timeSinceHit--;
		}

		if (this.currentDamage > 0)
		{
			this.currentDamage--;
		}

		if (this.shouldSpawnParticles() && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			this.spawnParticles(this.getParticleMap());
		}

		if (this.onGround)
		{
			this.tickOnGround();
		}
		else
		{
			this.tickInAir();
		}

		if (this.worldObj.isRemote)
		{
			this.motionX = this.getMotionVec().x;
			this.motionY = this.getMotionVec().y;
			this.motionZ = this.getMotionVec().z;
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
		}

		if (this.onGround && !this.lastOnGround)
		{
			this.onGroundHit();
		}

		if (this.worldObj.isRemote)
		{
			PacketDispatcher.sendPacketToServer(GCCorePacketEntityUpdate.buildUpdatePacket(this));
		}

		if (!this.worldObj.isRemote && this.ticksExisted % 5 == 0)
		{
			PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, 50, this.dimension, GCCorePacketEntityUpdate.buildUpdatePacket(this));
		}

		if (!this.worldObj.isRemote && this.ticks % this.getPacketTickSpacing() == 0)
		{
			GCCorePacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), this.getPacketSendDistance());
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.lastOnGround = this.onGround;
	}

	public Packet getDescriptionPacket()
	{
		return GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.getNetworkedData());
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			if (this.worldObj.isRemote)
			{
				this.readNetworkedData(dataStream);
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	@SideOnly(Side.CLIENT)
	public void spawnParticles(Map<Vector3, Vector3> points)
	{
		for (final Entry<Vector3, Vector3> vec : points.entrySet())
		{
			final Vector3 posVec = vec.getKey();
			final Vector3 motionVec = vec.getValue();

			this.spawnParticle(this.getParticle(this.rand, posVec.x, posVec.y, posVec.z, motionVec.x, motionVec.y, motionVec.z));
		}
	}

	@SideOnly(Side.CLIENT)
	public void spawnParticle(EntityFX fx)
	{
		final Minecraft mc = FMLClientHandler.instance().getClient();

		if (mc != null && mc.renderViewEntity != null && mc.effectRenderer != null)
		{
			if (fx != null)
			{
				mc.effectRenderer.addEffect(fx);
			}
		}
	}
}
