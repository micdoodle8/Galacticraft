package micdoodle8.mods.galacticraft.core.entities;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.fx.EntityFXLanderFlame;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.network.PacketDynamicInventory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * EntityLander.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class EntityLander extends InventoryEntity implements IInventorySettable, IScaleableFuelLevel, IControllableEntity, IPacketReceiver, ICameraZoomEntity
{
	private final int tankCapacity = 5000;
	public FluidTank fuelTank = new FluidTank(this.tankCapacity);
	private boolean waitForPlayer;
	private boolean lastWaitForPlayer;
	public float currentDamage;
	public int timeSinceHit;
	public int rockDirection = 1;
	public boolean lastOnGround;
	private double lastMotionY;

	public EntityLander(World par1World)
	{
		super(par1World);
		this.setSize(3.5F, 4.8F);
		this.ignoreFrustumCheck = true;
		this.isImmuneToFire = true;
	}

	public EntityLander(GCEntityPlayerMP player)
	{
		this(player.worldObj);

		this.containedItems = new ItemStack[player.getPlayerStats().rocketStacks.length + 1];
		this.fuelTank.setFluid(new FluidStack(GalacticraftCore.fluidFuel, player.getPlayerStats().fuelLevel));

		for (int i = 0; i < player.getPlayerStats().rocketStacks.length; i++)
		{
			this.containedItems[i] = player.getPlayerStats().rocketStacks[i];
		}
	}

	@Override
	protected void entityInit()
	{
		;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
		NBTTagList itemList = nbt.getTagList("Items", 10);
		this.containedItems = new ItemStack[nbt.getInteger("rocketStacksLength")];

		for (int i = 0; i < itemList.tagCount(); ++i)
		{
			NBTTagCompound itemTag = itemList.getCompoundTagAt(i);
			int slotID = itemTag.getByte("Slot") & 255;

			if (slotID >= 0 && slotID < this.containedItems.length)
			{
				this.containedItems[slotID] = ItemStack.loadItemStackFromNBT(itemTag);
			}
		}

		if (nbt.hasKey("fuelTank"))
		{
			this.fuelTank.readFromNBT(nbt.getCompoundTag("fuelTank"));
		}

		this.setWaitForPlayer(nbt.getBoolean("WaitingForPlayer"));

		this.lastOnGround = this.onGround;
		this.lastMotionY = this.motionY;
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
		NBTTagList itemList = new NBTTagList();

		nbt.setInteger("rocketStacksLength", this.containedItems.length);

		for (int i = 0; i < this.containedItems.length; ++i)
		{
			if (this.containedItems[i] != null)
			{
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setByte("Slot", (byte) i);
				this.containedItems[i].writeToNBT(itemTag);
				itemList.appendTag(itemTag);
			}
		}

		nbt.setTag("Items", itemList);

		if (this.fuelTank.getFluid() != null)
		{
			nbt.setTag("fuelTank", this.fuelTank.writeToNBT(new NBTTagCompound()));
		}

		nbt.setBoolean("WaitingForPlayer", this.getWaitForPlayer());
	}

	@Override
	public void onUpdate()
	{
		if (this.timeSinceHit > 0)
		{
			this.timeSinceHit--;
		}

		if (this.currentDamage > 0.0F)
		{
			this.currentDamage--;
		}

		if (this.getWaitForPlayer())
		{
			if (this.riddenByEntity != null)
			{
				if (this.ticksExisted >= 40)
				{
					if (!this.worldObj.isRemote)
					{
						Entity e = this.riddenByEntity;
						this.riddenByEntity.ridingEntity = null;
						this.riddenByEntity = null;
						e.mountEntity(this);
					}

					this.setWaitForPlayer(false);
				}
				else
				{
					this.motionX = this.motionY = this.motionZ = 0.0D;
					this.riddenByEntity.motionX = this.riddenByEntity.motionY = this.riddenByEntity.motionZ = 0;
				}
			}
			else
			{
				this.motionX = this.motionY = this.motionZ = 0.0D;
			}
		}

		if (!this.waitForPlayer && this.lastWaitForPlayer)
		{
			this.motionY = -3.5D;
		}

		super.onUpdate();

		if (!this.onGround && Math.abs(this.motionY) > 0.01 && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			this.spawnParticles();
		}

		AxisAlignedBB box = this.boundingBox.expand(0.2D, 0.4D, 0.2D);

		final List<?> var15 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, box);

		if (var15 != null && !var15.isEmpty())
		{
			for (int var52 = 0; var52 < var15.size(); ++var52)
			{
				final Entity var17 = (Entity) var15.get(var52);

				if (var17 != this.riddenByEntity)
				{
					this.pushEntityAway(var17);
				}
			}
		}

		if (!this.worldObj.isRemote)
		{
			final FluidStack liquid = this.fuelTank.getFluid();

			if (liquid != null && this.fuelTank.getFluid() != null && this.fuelTank.getFluid().getFluid().getName().equalsIgnoreCase("Fuel"))
			{
				if (FluidContainerRegistry.isEmptyContainer(this.containedItems[this.containedItems.length - 1]))
				{
					boolean isCanister = this.containedItems[this.containedItems.length - 1].isItemEqual(new ItemStack(GCItems.oilCanister, 1, GCItems.oilCanister.getMaxDamage()));
					final int amountToFill = Math.min(liquid.amount, isCanister ? GCItems.fuelCanister.getMaxDamage() - 1 : FluidContainerRegistry.BUCKET_VOLUME);

					if (isCanister)
					{
						this.containedItems[this.containedItems.length - 1] = new ItemStack(GCItems.fuelCanister, 1, GCItems.fuelCanister.getMaxDamage() - amountToFill);
					}
					else
					{
						this.containedItems[this.containedItems.length - 1] = FluidContainerRegistry.fillFluidContainer(liquid, this.containedItems[this.containedItems.length - 1]);
					}

					this.fuelTank.drain(amountToFill, true);
				}
			}
		}

		this.motionY -= 0.008D;

		if (this.worldObj.isRemote && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			this.sendPacketToServer();
		}

		if (this.onGround)
		{
			this.motionX = this.motionY = this.motionZ = 0.0D;
		}
		else
		{
			if (this.worldObj.isRemote)
			{
				this.motionX = -50.0 * Math.cos(Math.toRadians(this.rotationYaw)) * Math.sin(Math.toRadians(this.rotationPitch * 0.01));
				this.motionZ = -50.0 * Math.sin(Math.toRadians(this.rotationYaw)) * Math.sin(Math.toRadians(this.rotationPitch * 0.01));
			}

			this.moveEntity(this.motionX, this.motionY, this.motionZ);
		}

		if (!this.worldObj.isRemote)
		{
			if (this.onGround && !this.lastOnGround)
			{
				if (Math.abs(this.lastMotionY) > 2.0D)
				{
					if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayerMP)
					{
						this.riddenByEntity.mountEntity(this);
					}

					this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 12, true);

					this.setDead();
				}
			}
		}

		if (this.onGround)
		{
			this.rotationPitch = 0.0F;
		}

		this.lastWaitForPlayer = this.waitForPlayer;
		this.lastOnGround = this.onGround;
		this.lastMotionY = this.motionY;
	}

	@SideOnly(Side.CLIENT)
	private void spawnParticles()
	{
		final double x1 = 2 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
		final double z1 = 2 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
		final double y1 = -5.0D;

		final float angle1 = (float) ((this.rotationYaw - 40.0F) * Math.PI / 180.0F);
		final float angle2 = (float) ((this.rotationYaw + 40.0F) * Math.PI / 180.0F);
		final float angle3 = (float) ((this.rotationYaw + 180 - 40.0F) * Math.PI / 180.0F);
		final float angle4 = (float) ((this.rotationYaw + 180 + 40.0F) * Math.PI / 180.0F);
		final float pitch = (float) Math.sin(this.rotationPitch * Math.PI / 180.0F);

		Vector3 vec1 = new Vector3(this).translate(new Vector3(0.4 * Math.cos(angle1) * Math.cos(pitch), 0.5, 0.4 * Math.sin(angle1) * Math.cos(pitch)));
		Vector3 vec2 = new Vector3(this).translate(new Vector3(0.4 * Math.cos(angle2) * Math.cos(pitch), 0.5, 0.4 * Math.sin(angle2) * Math.cos(pitch)));
		Vector3 vec3 = new Vector3(this).translate(new Vector3(0.4 * Math.cos(angle3) * Math.cos(pitch), 0.5, 0.4 * Math.sin(angle3) * Math.cos(pitch)));
		Vector3 vec4 = new Vector3(this).translate(new Vector3(0.4 * Math.cos(angle4) * Math.cos(pitch), 0.5, 0.4 * Math.sin(angle4) * Math.cos(pitch)));

		this.spawnParticle(new EntityFXLanderFlame(this.worldObj, vec1.x, vec1.y, vec1.z, x1, y1, z1));
		this.spawnParticle(new EntityFXLanderFlame(this.worldObj, vec2.x, vec2.y, vec2.z, x1, y1, z1));
		this.spawnParticle(new EntityFXLanderFlame(this.worldObj, vec3.x, vec3.y, vec3.z, x1, y1, z1));
		this.spawnParticle(new EntityFXLanderFlame(this.worldObj, vec4.x, vec4.y, vec4.z, x1, y1, z1));
	}

	@SideOnly(Side.CLIENT)
	private void spawnParticle(EntityFX fx)
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

	private void sendPacketToServer()
	{
		if (this.riddenByEntity == FMLClientHandler.instance().getClient().thePlayer)
		{
			GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
		}
	}

	public void pushEntityAway(Entity par1Entity)
	{
		if (this.riddenByEntity != par1Entity && this.ridingEntity != par1Entity)
		{
			double d0 = this.posX - par1Entity.posX;
			double d1 = this.posZ - par1Entity.posZ;
			double d2 = MathHelper.abs_max(d0, d1);

			if (d2 >= 0.009999999776482582D)
			{
				d2 = MathHelper.sqrt_double(d2);
				d0 /= d2;
				d1 /= d2;
				double d3 = 1.0D / d2;

				if (d3 > 1.0D)
				{
					d3 = 1.0D;
				}

				d0 *= d3;
				d1 *= d3;
				d0 *= 0.05000000074505806D;
				d1 *= 0.05000000074505806D;
				d0 *= 1.0F - par1Entity.entityCollisionReduction;
				d1 *= 1.0F - par1Entity.entityCollisionReduction;
				par1Entity.addVelocity(-d0, 0.0D, -d1);
			}
		}
	}

	@Override
	public boolean pressKey(int key)
	{
		if (this.onGround)
		{
			return false;
		}

		float turnFactor = 2.0F;

		switch (key)
		{
		case 0:
			if (!this.onGround)
			{
				this.rotationPitch = Math.min(Math.max(this.rotationPitch - 0.5F * turnFactor, -35), 35);
			}

			return true;
		case 1:
			if (!this.onGround)
			{
				this.rotationPitch = Math.min(Math.max(this.rotationPitch + 0.5F * turnFactor, -35), 35);
			}

			return true;
		case 2:
			if (!this.onGround)
			{
				this.rotationYaw -= 0.5F * turnFactor;
			}

			return true;
		case 3:
			if (!this.onGround)
			{
				this.rotationYaw += 0.5F * turnFactor;
			}

			return true;
		case 4:
			this.motionY = Math.min(this.motionY + 0.03F, -1.0);
			return true;
		case 5:
			this.motionY = Math.min(this.motionY - 0.022F, -1.0);
			return true;
		}

		return false;
	}

	@Override
	public double getMountedYOffset()
	{
		return 1.0D;
	}

	@Override
	public int getSizeInventory()
	{
		return this.containedItems.length;
	}

	@Override
	public String getInventoryName()
	{
		return "Lander";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
	}

	@Override
	public void markDirty()
	{

	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}

	@Override
	public int getScaledFuelLevel(int scale)
	{
		return (int) ((double) this.fuelTank.getFluidAmount() / (double) this.tankCapacity * scale);
	}

	@Override
	public void setSizeInventory(int size)
	{
		this.containedItems = new ItemStack[size];
	}

	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox()
	{
		return null;
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity par1Entity)
	{
		return null;
	}

	@Override
	public boolean canBePushed()
	{
		return false;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return !this.isDead;
	}

	@Override
	public boolean attackEntityFrom(DamageSource var1, float var2)
	{
		if (this.isDead || var1.equals(DamageSource.cactus) || this.riddenByEntity != null)
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

	@Override
	public boolean interactFirst(EntityPlayer var1)
	{
		if (this.worldObj.isRemote)
		{
			return false;
		}

		if (this.riddenByEntity == null && var1 instanceof EntityPlayerMP)
		{
			GCCoreUtil.openParachestInv((EntityPlayerMP) var1, this);
			return true;
		}
		else if (var1 instanceof EntityPlayerMP)
		{
			var1.mountEntity(null);
			return true;
		}
		else
		{
			return true;
		}
	}

	@Override
	public void getNetworkedData(ArrayList<Object> list)
	{
		if (!this.worldObj.isRemote)
		{
			list.add(this.containedItems != null ? this.containedItems.length : 0);
			list.add(this.fuelTank.getFluidAmount());

			list.add(this.getWaitForPlayer());

			list.add(this.onGround);
		}
		else
		{
			list.add(this.motionX * 8000.0D);
			list.add(this.motionY * 8000.0D);
			list.add(this.motionZ * 8000.0D);

			list.add(this.rotationPitch);
			list.add(this.rotationYaw);
		}
	}

	@Override
	public void decodePacketdata(ByteBuf buffer)
	{
		if (this.worldObj.isRemote)
		{
			int cargoLength = buffer.readInt();

			if (this.containedItems == null || this.containedItems.length == 0)
			{
				this.containedItems = new ItemStack[cargoLength];
				GalacticraftCore.packetPipeline.sendToServer(new PacketDynamicInventory(this));
			}

			this.fuelTank.setFluid(new FluidStack(GalacticraftCore.fluidFuel, buffer.readInt()));

			this.setWaitForPlayer(buffer.readBoolean());

			this.onGround = buffer.readBoolean();
		}
		else
		{
			this.motionX = buffer.readDouble() / 8000.0D;
			this.motionY = buffer.readDouble() / 8000.0D;
			this.motionZ = buffer.readDouble() / 8000.0D;

			this.rotationPitch = buffer.readFloat();
			this.rotationYaw = buffer.readFloat();
		}
	}

	@Override
	public void handlePacketData(Side side, EntityPlayer player)
	{
	}

	@Override
	public double getPacketRange()
	{
		return 100.0D;
	}

	public boolean getWaitForPlayer()
	{
		return this.waitForPlayer;
	}

	public void setWaitForPlayer(boolean waitForPlayer)
	{
		this.waitForPlayer = waitForPlayer;
	}

	public void dropItems()
	{
		for (ItemStack item : this.getItemsDropped())
		{
			if (item != null)
			{
				this.entityDropItem(item, 0);
			}
		}
	}

	public List<ItemStack> getItemsDropped()
	{
		final List<ItemStack> items = new ArrayList<ItemStack>();

		for (ItemStack stack : this.containedItems)
		{
			if (stack != null)
			{
				items.add(stack);
			}
		}

		return items;
	}

	@Override
	public float getCameraZoom() 
	{
		return 15.0F;
	}

	@Override
	public boolean defaultThirdPerson() 
	{
		return true;
	}
}
