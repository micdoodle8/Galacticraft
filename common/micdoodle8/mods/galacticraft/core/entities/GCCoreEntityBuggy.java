package micdoodle8.mods.galacticraft.core.entities;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketControllableEntity;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketEntityUpdate;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.tick.GCCoreKeyHandlerClient;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityBuggyFueler;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import org.lwjgl.input.Keyboard;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * GCCoreEntityBuggy.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreEntityBuggy extends GCCoreEntityControllable implements IInventory, IPacketReceiver, IDockable
{
	private final int tankCapacity = 1000;
	public FluidTank buggyFuelTank = new FluidTank(this.tankCapacity);
	protected long ticks = 0;
	public int buggyType;
	public int fuel;
	public int currentDamage;
	public int timeSinceHit;
	public int rockDirection;
	public double speed;
	public float wheelRotationZ;
	public float wheelRotationX;
	float maxSpeed = 0.5F;
	float accel = 0.2F;
	float turnFactor = 3.0F;
	public String texture;
	ItemStack[] cargoItems = new ItemStack[60];
	public float turnProgress = 0;
	public float rotationYawBuggy;
	public double boatX;
	public double boatY;
	public double boatZ;
	public double boatYaw;
	public double boatPitch;
	public int boatPosRotationIncrements;
	private IFuelDock landingPad;
	public Vector3 radarDishRotation = new Vector3();

	public GCCoreEntityBuggy(World var1)
	{
		super(var1);
		this.setSize(0.98F, 0.7F);
		this.yOffset = 2.5F;
		this.fuel = 0;
		this.currentDamage = 18;
		this.timeSinceHit = 19;
		this.rockDirection = 20;
		this.speed = 0.0D;
		this.preventEntitySpawning = true;
		this.dataWatcher.addObject(this.currentDamage, new Integer(0));
		this.dataWatcher.addObject(this.timeSinceHit, new Integer(0));
		this.dataWatcher.addObject(this.rockDirection, new Integer(1));
		this.ignoreFrustumCheck = true;
		this.isImmuneToFire = true;
	}

	public GCCoreEntityBuggy(World var1, double var2, double var4, double var6, int type)
	{
		this(var1);
		this.setPosition(var2, var4 + this.yOffset, var6);
		this.setBuggyType(type);
		this.cargoItems = new ItemStack[this.buggyType * 18];
	}

	public int getScaledFuelLevel(int i)
	{
		final double fuelLevel = this.buggyFuelTank.getFluid() == null ? 0 : this.buggyFuelTank.getFluid().amount;

		return (int) (fuelLevel * i / this.tankCapacity);
	}

	public ModelBase getModel()
	{
		return null;
	}

	public int getType()
	{
		return this.buggyType;
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
		return this.height - 3.0D;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return !this.isDead;
	}

	public void setBuggyType(int par1)
	{
		this.buggyType = par1;
	}

	@Override
	public void updateRiderPosition()
	{
		if (this.riddenByEntity != null)
		{
			final double var1 = Math.cos(this.rotationYaw * Math.PI / 180.0D + 114.8) * -0.5D;
			final double var3 = Math.sin(this.rotationYaw * Math.PI / 180.0D + 114.8) * -0.5D;
			this.riddenByEntity.setPosition(this.posX + var1, this.posY - 2 + this.riddenByEntity.getYOffset(), this.posZ + var3);
		}
	}

	@Override
	public void setPositionRotationAndMotion(double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ, boolean onGround)
	{
		if (this.worldObj.isRemote)
		{
			this.boatX = x;
			this.boatY = y;
			this.boatZ = z;
			this.boatYaw = yaw;
			this.boatPitch = pitch;
			this.motionX = motX;
			this.motionY = motY;
			this.motionZ = motZ;
			this.boatPosRotationIncrements = 5;
		}
		else
		{
			this.setPosition(x, y, z);
			this.setRotation(yaw, pitch);
			this.motionX = motX;
			this.motionY = motY;
			this.motionZ = motZ;
		}
	}

	@Override
	public void performHurtAnimation()
	{
		this.dataWatcher.updateObject(this.rockDirection, Integer.valueOf(-this.dataWatcher.getWatchableObjectInt(this.rockDirection)));
		this.dataWatcher.updateObject(this.timeSinceHit, Integer.valueOf(10));
		this.dataWatcher.updateObject(this.currentDamage, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(this.currentDamage) * 5));
	}

	@Override
	public boolean attackEntityFrom(DamageSource var1, float var2)
	{
		if (this.isDead || var1.equals(DamageSource.cactus))
		{
			return true;
		}
		else
		{
			this.dataWatcher.updateObject(this.rockDirection, Integer.valueOf(-this.dataWatcher.getWatchableObjectInt(this.rockDirection)));
			this.dataWatcher.updateObject(this.timeSinceHit, Integer.valueOf(10));
			this.dataWatcher.updateObject(this.currentDamage, Integer.valueOf((int) (this.dataWatcher.getWatchableObjectInt(this.currentDamage) + var2 * 10)));
			this.setBeenAttacked();

			if (var1.getEntity() instanceof EntityPlayer && ((EntityPlayer) var1.getEntity()).capabilities.isCreativeMode)
			{
				this.dataWatcher.updateObject(this.currentDamage, 100);
			}

			if (this.dataWatcher.getWatchableObjectInt(this.currentDamage) > 2)
			{
				if (this.riddenByEntity != null)
				{
					this.riddenByEntity.mountEntity(this);
				}

				if (!this.worldObj.isRemote)
				{
					if (this.riddenByEntity != null)
					{
						this.riddenByEntity.mountEntity(this);
					}

					this.dropBuggyAsItem();
				}

				this.setDead();
			}

			return true;
		}
	}

	public void dropBuggyAsItem()
	{
		if (this.getItemsDropped() == null)
		{
			return;
		}

		for (final ItemStack item : this.getItemsDropped())
		{
			this.entityDropItem(item, 0);
		}
	}

	public List<ItemStack> getItemsDropped()
	{
		final List<ItemStack> items = new ArrayList<ItemStack>();
		items.add(new ItemStack(GCCoreItems.buggy, 1, this.buggyType));

		for (ItemStack item : this.cargoItems)
		{
			if (item != null)
			{
				items.add(item);
			}
		}

		return items;
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
				this.boatPosRotationIncrements = i + 5;
				this.boatX = d;
				this.boatY = d1 + (this.riddenByEntity == null ? 1 : 0);
				this.boatZ = d2;
				this.boatYaw = f;
				this.boatPitch = f1;
			}
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
			if (this.boatPosRotationIncrements > 0)
			{
				x = this.posX + (this.boatX - this.posX) / this.boatPosRotationIncrements;
				y = this.posY + (this.boatY - this.posY) / this.boatPosRotationIncrements;
				z = this.posZ + (this.boatZ - this.posZ) / this.boatPosRotationIncrements;
				var12 = MathHelper.wrapAngleTo180_double(this.boatYaw - this.rotationYaw);
				this.rotationYaw = (float) (this.rotationYaw + var12 / this.boatPosRotationIncrements);
				this.rotationPitch = (float) (this.rotationPitch + (this.boatPitch - this.rotationPitch) / this.boatPosRotationIncrements);
				--this.boatPosRotationIncrements;
				this.setPosition(x, y, z);
				this.setRotation(this.rotationYaw, this.rotationPitch);
			}
			else
			{
				x = this.posX + this.motionX;
				y = this.posY + this.motionY;
				z = this.posZ + this.motionZ;
				if (this.riddenByEntity != null)
				{
					this.setPosition(x, y, z);
				}

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
			return;
		}

		if (this.dataWatcher.getWatchableObjectInt(this.timeSinceHit) > 0)
		{
			this.dataWatcher.updateObject(this.timeSinceHit, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(this.timeSinceHit) - 1));
		}

		if (this.dataWatcher.getWatchableObjectInt(this.currentDamage) > 0)
		{
			this.dataWatcher.updateObject(this.currentDamage, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(this.currentDamage) - 1));
		}

		final byte var20 = 5;
		final double var2 = 0.0D;
		int var4;

		for (var4 = 0; var4 < var20; ++var4)
		{
		}

		if (var2 < 1.0D)
		{
			this.motionY -= 0.04D;
		}

		if (this.riddenByEntity == null)
		{
			this.yOffset = 5;
		}

		if (this.inWater && this.speed > 0.2D)
		{
			this.worldObj.playSoundEffect((float) this.posX, (float) this.posY, (float) this.posZ, "random.fizz", 0.5F, 2.6F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.8F);
		}

		this.speed *= 0.98D;

		if (this.speed > this.maxSpeed)
		{
			this.speed = this.maxSpeed;
		}

		if (this.isCollidedHorizontally)
		{
			this.speed *= 0.9;
			this.motionY = 0.1D;
		}

		if (this.worldObj.isRemote && this.buggyFuelTank.getFluid() != null && this.buggyFuelTank.getFluid().amount > 0)
		{
			this.motionX = -(this.speed * Math.cos((this.rotationYaw - 90F) * Math.PI / 180.0D));
			this.motionZ = -(this.speed * Math.sin((this.rotationYaw - 90F) * Math.PI / 180.0D));
		}

		if (this.worldObj.isRemote)
		{
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.wheelRotationX += (this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ) * 250.0F * (this.speed < 0 ? 1 : -1);
			this.wheelRotationZ = Math.max(-30.0F, Math.min(30.0F, this.wheelRotationZ * 0.9F));
			this.radarDishRotation.x = Math.sin(this.ticks * 0.05) * 50.0F;
			this.radarDishRotation.z = Math.cos(this.ticks * 0.1) * 50.0F;
		}

		if (!this.worldObj.isRemote && Math.abs(this.motionX * this.motionZ) > 0.000001)
		{
			double d = this.motionX * this.motionX + this.motionZ * this.motionZ;

			if (d == 0)
			{
				d += 0.0001F;
			}

			int speedMod = MathHelper.floor_double(2 / d);

			if (speedMod == 0 || this.ticks % speedMod == 0)
			{
				this.removeFuel(1);
			}

		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.worldObj.isRemote)
		{
			PacketDispatcher.sendPacketToServer(GCCorePacketEntityUpdate.buildUpdatePacket(this));
		}

		if (!this.worldObj.isRemote && this.ticks % 5 == 0)
		{
			PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, 50, this.dimension, GCCorePacketEntityUpdate.buildUpdatePacket(this));
		}

		if (!this.worldObj.isRemote && this.ticks % 5 == 0)
		{
			GCCorePacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 50);
		}
	}

	public Packet getDescriptionPacket()
	{
		return GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.buggyType, this.buggyFuelTank.getFluid() != null ? this.buggyFuelTank.getFluid().amount : 0);
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			if (this.worldObj.isRemote)
			{
				this.buggyType = dataStream.readInt();
				this.buggyFuelTank.setFluid(new FluidStack(GalacticraftCore.fluidFuel, dataStream.readInt()));
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound var1)
	{
		this.fuel = var1.getInteger("Fuel");
		this.buggyType = var1.getInteger("buggyType");
		final NBTTagList var2 = var1.getTagList("Items");
		this.cargoItems = new ItemStack[this.getSizeInventory()];

		if (var1.hasKey("fuelTank"))
		{
			this.buggyFuelTank.readFromNBT(var1.getCompoundTag("fuelTank"));
		}

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			final NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
			final int var5 = var4.getByte("Slot") & 255;

			if (var5 >= 0 && var5 < this.cargoItems.length)
			{
				this.cargoItems[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound var1)
	{
		var1.setInteger("fuel", this.fuel);
		var1.setInteger("buggyType", this.buggyType);
		final NBTTagList var2 = new NBTTagList();

		if (this.buggyFuelTank.getFluid() != null)
		{
			var1.setTag("fuelTank", this.buggyFuelTank.writeToNBT(new NBTTagCompound()));
		}

		for (int var3 = 0; var3 < this.cargoItems.length; ++var3)
		{
			if (this.cargoItems[var3] != null)
			{
				final NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.cargoItems[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		var1.setTag("Items", var2);
	}

	@Override
	public int getSizeInventory()
	{
		return this.buggyType * 18;
	}

	@Override
	public ItemStack getStackInSlot(int var1)
	{
		return this.cargoItems[var1];
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2)
	{
		if (this.cargoItems[var1] != null)
		{
			ItemStack var3;

			if (this.cargoItems[var1].stackSize <= var2)
			{
				var3 = this.cargoItems[var1];
				this.cargoItems[var1] = null;
				return var3;
			}
			else
			{
				var3 = this.cargoItems[var1].splitStack(var2);

				if (this.cargoItems[var1].stackSize == 0)
				{
					this.cargoItems[var1] = null;
				}

				return var3;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1)
	{
		if (this.cargoItems[var1] != null)
		{
			final ItemStack var2 = this.cargoItems[var1];
			this.cargoItems[var1] = null;
			return var2;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2)
	{
		this.cargoItems[var1] = var2;

		if (var2 != null && var2.stackSize > this.getInventoryStackLimit())
		{
			var2.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName()
	{
		return "Buggy";
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void onInventoryChanged()
	{
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1)
	{
		return this.isDead ? false : var1.getDistanceSqToEntity(this) <= 64.0D;
	}

	@Override
	public void openChest()
	{
	}

	@Override
	public void closeChest()
	{
	}

	@Override
	public boolean interactFirst(EntityPlayer var1)
	{
		if (this.worldObj.isRemote)
		{
			if (this.riddenByEntity == null)
			{
				var1.sendChatToPlayer(ChatMessageComponent.createFromText(Keyboard.getKeyName(GCCoreKeyHandlerClient.leftKey.keyCode) + " / " + Keyboard.getKeyName(GCCoreKeyHandlerClient.rightKey.keyCode) + "  - " + StatCollector.translateToLocal("gui.buggy.turn.name")));
				var1.sendChatToPlayer(ChatMessageComponent.createFromText(Keyboard.getKeyName(GCCoreKeyHandlerClient.accelerateKey.keyCode) + "       - " + StatCollector.translateToLocal("gui.buggy.accel.name")));
				var1.sendChatToPlayer(ChatMessageComponent.createFromText(Keyboard.getKeyName(GCCoreKeyHandlerClient.decelerateKey.keyCode) + "       - " + StatCollector.translateToLocal("gui.buggy.decel.name")));
				var1.sendChatToPlayer(ChatMessageComponent.createFromText(Keyboard.getKeyName(GCCoreKeyHandlerClient.openSpaceshipInv.keyCode) + "       - " + StatCollector.translateToLocal("gui.buggy.inv.name")));
			}

			return true;
		}
		else
		{
			if (this.riddenByEntity != null)
			{
				var1.mountEntity(null);
				return true;
			}
			else
			{
				var1.mountEntity(this);
				return true;
			}
		}
	}

	@Override
	public boolean pressKey(int key)
	{
		if (this.worldObj.isRemote && (key == 6 || key == 8 || key == 9))
		{
			PacketDispatcher.sendPacketToServer(GCCorePacketControllableEntity.buildKeyPacket(key));
			return true;
		}
		switch (key)
		{
		case 0: // Accelerate
		{
			this.speed += this.accel / 20D;
			return true;
		}
		case 1: // Deccelerate
		{
			this.speed -= this.accel / 20D;
			return true;
		}
		case 2: // Left
		{
			this.rotationYaw -= 0.5F * this.turnFactor;
			this.wheelRotationZ = Math.max(-30.0F, Math.min(30.0F, this.wheelRotationZ + 0.5F));
			return true;
		}
		case 3: // Right
		{
			this.rotationYaw += 0.5F * this.turnFactor;
			this.wheelRotationZ = Math.max(-30.0F, Math.min(30.0F, this.wheelRotationZ - 0.5F));
			return true;
		}
		}

		return false;
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}

	@Override
	public int addFuel(FluidStack liquid, boolean doDrain)
	{
		final FluidStack liquidInTank = this.buggyFuelTank.getFluid();

		if (liquid != null && FluidRegistry.getFluidName(liquid).equalsIgnoreCase("Fuel") && this.landingPad != null)
		{
			if (liquidInTank == null || liquidInTank.amount + liquid.amount <= this.buggyFuelTank.getCapacity())
			{
				return this.buggyFuelTank.fill(liquid, doDrain);
			}
		}

		return 0;
	}

	@Override
	public FluidStack removeFuel(int amount)
	{
		return this.buggyFuelTank.drain(amount, true);
	}

	@Override
	public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
	{
		if (this.buggyType == 0)
		{
			return EnumCargoLoadingState.NOINVENTORY;
		}

		int count = 0;

		for (count = 0; count < this.cargoItems.length; count++)
		{
			ItemStack stackAt = this.cargoItems[count];

			if (stackAt != null && stackAt.itemID == stack.itemID && stackAt.getItemDamage() == stack.getItemDamage() && stackAt.stackSize < stackAt.getMaxStackSize())
			{
				if (doAdd)
				{
					this.cargoItems[count].stackSize += stack.stackSize;
				}

				return EnumCargoLoadingState.SUCCESS;
			}
		}

		for (count = 0; count < this.cargoItems.length; count++)
		{
			ItemStack stackAt = this.cargoItems[count];

			if (stackAt == null)
			{
				if (doAdd)
				{
					this.cargoItems[count] = stack;
				}

				return EnumCargoLoadingState.SUCCESS;
			}
		}

		return EnumCargoLoadingState.FULL;
	}

	@Override
	public RemovalResult removeCargo(boolean doRemove)
	{
		for (int i = 0; i < this.cargoItems.length; i++)
		{
			ItemStack stackAt = this.cargoItems[i];

			if (stackAt != null)
			{
				if (doRemove && --this.cargoItems[i].stackSize <= 0)
				{
					this.cargoItems[i] = null;
				}

				return new RemovalResult(EnumCargoLoadingState.SUCCESS, new ItemStack(stackAt.itemID, 1, stackAt.getItemDamage()));
			}
		}

		return new RemovalResult(EnumCargoLoadingState.EMPTY, null);
	}

	@Override
	public void setPad(IFuelDock pad)
	{
		this.landingPad = pad;
	}

	@Override
	public IFuelDock getLandingPad()
	{
		return this.landingPad;
	}

	@Override
	public void onPadDestroyed()
	{

	}

	@Override
	public boolean isDockValid(IFuelDock dock)
	{
		return dock instanceof GCCoreTileEntityBuggyFueler;
	}
}
