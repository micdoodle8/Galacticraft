package micdoodle8.mods.galacticraft.mars.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLanderFlameFX;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityAdvanced;
import micdoodle8.mods.galacticraft.core.entities.IScaleableFuelLevel;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumPacketClient;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerServer.EnumPacketServer;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.mars.util.GCMarsUtil;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsEntityLandingBalloons.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsEntityLandingBalloons extends GCCoreEntityAdvanced implements IInventorySettable, IPacketReceiver, IScaleableFuelLevel
{
	private final int tankCapacity = 5000;
	public FluidTank fuelTank = new FluidTank(this.tankCapacity);

	float maxSpeed = 0.05F;
	float minSpeed = 0.3F;
	float accel = 0.04F;
	float turnFactor = 0.2F;
	public ItemStack[] chestContents = new ItemStack[0];
	public int numUsingPlayers;
	public GCCorePlayerMP playerSpawnedIn;
	private int groundHitCount;
	private float rotationPitchSpeed;
	private float rotationYawSpeed;
	private boolean hasReceivedPacket = false;

	public GCMarsEntityLandingBalloons(World var1)
	{
		super(var1, -5.0D, 2.5F);
		this.setSize(3.0F, 3.0F);
	}

	@Override
	public int getScaledFuelLevel(int i)
	{
		final double fuelLevel = this.fuelTank.getFluid() == null ? 0 : this.fuelTank.getFluid().amount;

		return (int) (fuelLevel * i / this.tankCapacity);
	}

	public GCMarsEntityLandingBalloons(World var1, double var2, double var4, double var6)
	{
		this(var1);
		this.setPosition(var2, var4 + this.yOffset, var6);
	}

	public GCMarsEntityLandingBalloons(GCCorePlayerMP player)
	{
		this(player.worldObj, player.posX, player.posY, player.posZ);
		this.playerSpawnedIn = player;

		this.chestContents = new ItemStack[player.getRocketStacks().length + 1];
		this.fuelTank.setFluid(new FluidStack(GalacticraftCore.fluidFuel, player.getFuelLevel()));

		for (int i = 0; i < player.getRocketStacks().length; i++)
		{
			this.chestContents[i] = player.getRocketStacks()[i];
		}
	}

	@Override
	public void updateRiderPosition()
	{
		if (this.riddenByEntity != null)
		{
			this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
		}
	}

	@Override
	public double getMountedYOffset()
	{
		return this.height * 0.75D - 4.5;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (this.ticks < 40 && this.posY > 150)
		{
			if (this.riddenByEntity == null)
			{
				final EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 5);

				if (player != null)
				{
					player.mountEntity(this);
				}
			}
		}

		if (!this.worldObj.isRemote)
		{
			final FluidStack liquid = this.fuelTank.getFluid();

			if (liquid != null && this.fuelTank.getFluid() != null && this.fuelTank.getFluid().getFluid().getName().equalsIgnoreCase("Fuel"))
			{
				if (FluidContainerRegistry.isEmptyContainer(this.chestContents[this.chestContents.length - 1]))
				{
					boolean isCanister = this.chestContents[this.chestContents.length - 1].isItemEqual(new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage()));
					final int amountToFill = Math.min(liquid.amount, isCanister ? GCCoreItems.fuelCanister.getMaxDamage() - 1 : FluidContainerRegistry.BUCKET_VOLUME);

					if (isCanister)
					{
						this.chestContents[this.chestContents.length - 1] = new ItemStack(GCCoreItems.fuelCanister, 1, GCCoreItems.fuelCanister.getMaxDamage() - amountToFill);
					}
					else
					{
						this.chestContents[this.chestContents.length - 1] = FluidContainerRegistry.fillFluidContainer(liquid, this.chestContents[this.chestContents.length - 1]);
					}

					this.fuelTank.drain(amountToFill, true);
				}
			}
		}

		if (this.groundHitCount >= 14)
		{
			this.yOffset = 2.0F;
		}

		this.rotationPitch += this.rotationPitchSpeed;
		this.rotationYaw += this.rotationYawSpeed;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
		final NBTTagList var2 = nbt.getTagList("Items");

		this.chestContents = new ItemStack[nbt.getInteger("rocketStacksLength")];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			final NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
			final int var5 = var4.getByte("Slot") & 255;

			if (var5 >= 0 && var5 < this.chestContents.length)
			{
				this.chestContents[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		if (nbt.hasKey("fuelTank"))
		{
			this.fuelTank.readFromNBT(nbt.getCompoundTag("fuelTank"));
		}

		this.groundHitCount = nbt.getInteger("GroundHitCount");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
		final NBTTagList nbttaglist = new NBTTagList();

		nbt.setInteger("rocketStacksLength", this.chestContents.length);

		for (int i = 0; i < this.chestContents.length; ++i)
		{
			if (this.chestContents[i] != null)
			{
				final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.chestContents[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbt.setTag("Items", nbttaglist);

		if (this.fuelTank.getFluid() != null)
		{
			nbt.setTag("fuelTank", this.fuelTank.writeToNBT(new NBTTagCompound()));
		}

		nbt.setInteger("GroundHitCount", this.groundHitCount);
	}

	@Override
	public int getSizeInventory()
	{
		return this.chestContents.length;
	}

	@Override
	public ItemStack getStackInSlot(int par1)
	{
		return this.chestContents[par1];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.chestContents[par1] != null)
		{
			ItemStack itemstack;

			if (this.chestContents[par1].stackSize <= par2)
			{
				itemstack = this.chestContents[par1];
				this.chestContents[par1] = null;
				this.onInventoryChanged();
				return itemstack;
			}
			else
			{
				itemstack = this.chestContents[par1].splitStack(par2);

				if (this.chestContents[par1].stackSize == 0)
				{
					this.chestContents[par1] = null;
				}

				this.onInventoryChanged();
				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (this.chestContents[par1] != null)
		{
			final ItemStack itemstack = this.chestContents[par1];
			this.chestContents[par1] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.chestContents[par1] = par2ItemStack;

		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
		{
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	@Override
	public String getInvName()
	{
		return "Landing Balloons";
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return true;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return par1EntityPlayer.getDistanceSq(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D) <= 64.0D;
	}

	@Override
	public void openChest()
	{
		if (this.numUsingPlayers < 0)
		{
			this.numUsingPlayers = 0;
		}

		++this.numUsingPlayers;
	}

	@Override
	public void closeChest()
	{
		--this.numUsingPlayers;
	}

	@Override
	public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
	{
		return false;
	}

	@Override
	public void onInventoryChanged()
	{
	}

	@Override
	public boolean interactFirst(EntityPlayer var1)
	{
		if (this.worldObj.isRemote)
		{
			if (this.riddenByEntity != null)
			{
				this.riddenByEntity.mountEntity(this);
			}

			return true;
		}
		else if (this.riddenByEntity == null && this.groundHitCount >= 14 && var1 instanceof EntityPlayerMP)
		{
			GCMarsUtil.openParachestInventory((EntityPlayerMP) var1, this);
			return true;
		}
		else if (var1 instanceof EntityPlayerMP)
		{
			((EntityPlayerMP) var1).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.ZOOM_CAMERA, new Object[] { 0 }));
			var1.mountEntity(null);
			return true;
		}
		else
		{
			return true;
		}
	}

	@Override
	public boolean pressKey(int key)
	{
		switch (key)
		{
		case 0: // Accelerate
		{
			if (!this.onGround)
			{
				this.rotationPitchSpeed -= 0.5F * this.turnFactor;
			}

			return true;
		}
		case 1: // Deccelerate
		{
			if (!this.onGround)
			{
				this.rotationPitchSpeed += 0.5F * this.turnFactor;
			}

			return true;
		}
		case 2: // Left
			if (!this.onGround)
			{
				this.rotationYawSpeed -= 0.5F * this.turnFactor;
			}

			return true;
		case 3: // Right
			if (!this.onGround)
			{
				this.rotationYawSpeed += 0.5F * this.turnFactor;
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean shouldMove()
	{
		if (this.ticks < 40)
		{
			return this.riddenByEntity != null;
		}

		return true;
	}

	@Override
	public boolean shouldSpawnParticles()
	{
		return false;
	}

	@Override
	public Map<Vector3, Vector3> getParticleMap()
	{
		final Map<Vector3, Vector3> particleMap = new HashMap<Vector3, Vector3>();
		return particleMap;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public EntityFX getParticle(Random rand, double x, double y, double z, double motX, double motY, double motZ)
	{
		return new GCCoreEntityLanderFlameFX(this.worldObj, x, y, z, motX, motY, motZ);
	}

	@Override
	public void tickInAir()
	{
		if (this.worldObj.isRemote)
		{
			if (this.groundHitCount == 0)
			{
				this.motionY = -this.posY / 50.0D;
			}
			else if (this.groundHitCount < 14)
			{
				this.motionY *= 0.95D;
				this.motionY -= 0.08D;
			}
			else
			{
				this.motionY = this.motionX = this.motionZ = this.rotationPitch = this.rotationYaw = this.rotationPitchSpeed = this.rotationYawSpeed = 0.0F;
			}
		}
	}

	@Override
	public void tickOnGround()
	{
	}

	@Override
	public void onGroundHit()
	{
		this.groundHitCount++;

		if (this.worldObj.isRemote && !this.hasReceivedPacket)
		{
			return;
		}

		if (this.groundHitCount < 14)
		{
			double mag = 1.0D / this.groundHitCount * 4.0D;
			this.motionX = this.rand.nextDouble() - 0.5;
			this.motionY = 1.0;
			this.motionZ = this.rand.nextDouble() - 0.5;
			this.motionX *= mag / 3.0D;
			this.motionY *= mag;
			this.motionZ *= mag / 3.0D;
		}
		else
		{
			this.motionY = this.motionX = this.motionZ = this.rotationPitch = this.rotationYaw = this.rotationPitchSpeed = this.rotationYawSpeed = 0.0F;
		}
	}

	@Override
	public Vector3 getMotionVec()
	{
		return new Vector3(this.motionX, this.ticks < 40 ? 0 : this.motionY, this.motionZ);
	}

	@Override
	public ArrayList<Object> getNetworkedData()
	{
		final ArrayList<Object> objList = new ArrayList<Object>();
		objList.add(this.groundHitCount);
		Integer cargoLength = this.chestContents != null ? this.chestContents.length : 0;
		objList.add(cargoLength);
		objList.add(this.fuelTank.getFluid() == null ? 0 : this.fuelTank.getFluid().amount);
		return objList;
	}

	@Override
	public int getPacketTickSpacing()
	{
		return 5;
	}

	@Override
	public double getPacketSendDistance()
	{
		return 50.0D;
	}

	@Override
	public void readNetworkedData(ByteArrayDataInput dataStream)
	{
		try
		{
			if (this.worldObj.isRemote)
			{
				this.hasReceivedPacket = true;
				this.groundHitCount = dataStream.readInt();
				int cargoLength = dataStream.readInt();
				if (this.chestContents == null || this.chestContents.length == 0)
				{
					this.chestContents = new ItemStack[cargoLength];
					PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.UPDATE_DYNAMIC_ENTITY_INV, new Object[] { this.entityId }));
				}

				this.fuelTank.setFluid(new FluidStack(GalacticraftCore.fluidFuel, dataStream.readInt()));
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean allowDamageSource(DamageSource damageSource)
	{
		return this.groundHitCount > 0 && !damageSource.isExplosion();
	}

	@Override
	public List<ItemStack> getItemsDropped()
	{
		final List<ItemStack> items = new ArrayList<ItemStack>();

		for (final ItemStack stack : this.chestContents)
		{
			items.add(stack);
		}

		return items;
	}

	@Override
	public void setSizeInventory(int size)
	{
		this.chestContents = new ItemStack[size];
	}
}
