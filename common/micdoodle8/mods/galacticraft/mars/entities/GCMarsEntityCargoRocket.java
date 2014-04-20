package micdoodle8.mods.galacticraft.mars.entities;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.entity.IWorldTransferCallback;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.RuntimeInterface;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.sounds.GCCoreSoundUpdaterSpaceship;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import micdoodle8.mods.galacticraft.mars.util.GCMarsUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import calclavia.api.icbm.IMissile;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;

/**
 * GCMarsEntityCargoRocket.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsEntityCargoRocket extends EntityAutoRocket implements IRocketType, IInventory, IWorldTransferCallback
{
	public EnumRocketType rocketType;
	public float rumble;
	public IUpdatePlayerListBox rocketSoundUpdater;

	public GCMarsEntityCargoRocket(World par1World)
	{
		super(par1World);
	}

	public GCMarsEntityCargoRocket(World par1World, double par2, double par4, double par6, EnumRocketType rocketType)
	{
		super(par1World, par2, par4, par6);
		this.rocketType = rocketType;
		this.cargoItems = new ItemStack[this.getSizeInventory()];
	}

	@Override
	public int getFuelTankCapacity()
	{
		return 2000;
	}

	public float getCargoFilledAmount()
	{
		float weight = 1;

		for (ItemStack stack : this.cargoItems)
		{
			if (stack != null)
			{
				weight += 0.1D;
			}
		}

		return weight;
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();

		if (Loader.isModLoaded("ICBM|Explosion"))
		{
			try
			{
				Class.forName("calclavia.api.icbm.RadarRegistry").getMethod("register", Entity.class).invoke(null, this);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setDead()
	{
		super.setDead();

		if (Loader.isModLoaded("ICBM|Explosion"))
		{
			try
			{
				Class.forName("calclavia.api.icbm.RadarRegistry").getMethod("unregister", Entity.class).invoke(null, this);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		if (this.rocketSoundUpdater != null)
		{
			this.rocketSoundUpdater.update();
		}
	}

	@Override
	public void onUpdate()
	{
		if (this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase() && this.hasValidFuel() && !this.worldObj.isRemote)
		{
			double motionScalar = this.timeSinceLaunch / 250;

			motionScalar = Math.min(motionScalar, 1);

			double modifier = this.getCargoFilledAmount();
			motionScalar *= 5.0D / modifier;

			if (!this.landing)
			{
				if (motionScalar != 0.0)
				{
					this.motionY = -motionScalar * Math.cos((this.rotationPitch - 180) * Math.PI / 180.0D);
				}
			}
			else
			{
				if (this.targetVec != null)
				{
					this.motionY = (this.posY - this.targetVec.y) / -100.0D;
				}
			}

			double multiplier = 1.0D;

			if (this.worldObj.provider instanceof IGalacticraftWorldProvider)
			{
				multiplier = ((IGalacticraftWorldProvider) this.worldObj.provider).getFuelUsageMultiplier();

				if (multiplier <= 0)
				{
					multiplier = 1;
				}
			}

			if (this.timeSinceLaunch % MathHelper.floor_double(3 * (1 / multiplier)) == 0)
			{
				this.removeFuel(1);
			}
		}
		else if (!this.hasValidFuel() && this.getLaunched() && !this.worldObj.isRemote)
		{
			if (Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 10 != 0.0)
			{
				this.motionY -= Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20;
			}
		}

		super.onUpdate();

		if (this.rumble > 0)
		{
			this.rumble--;
		}

		if (this.rumble < 0)
		{
			this.rumble++;
		}

		if (this.launchPhase == EnumLaunchPhase.IGNITED.getPhase() || this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase())
		{
			this.performHurtAnimation();

			this.rumble = (float) this.rand.nextInt(3) - 3;
		}

		int i;

		if (this.timeUntilLaunch >= 100)
		{
			i = Math.abs(this.timeUntilLaunch / 100);
		}
		else
		{
			i = 1;
		}

		if ((this.getLaunched() || this.launchPhase == EnumLaunchPhase.IGNITED.getPhase() && this.rand.nextInt(i) == 0) && !GCCoreConfigManager.disableSpaceshipParticles && this.hasValidFuel())
		{
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
			{
				this.spawnParticles(this.getLaunched());
			}
		}

		if (this.rocketSoundUpdater != null && (this.launchPhase == EnumLaunchPhase.IGNITED.getPhase() || this.getLaunched()))
		{
			this.rocketSoundUpdater.update();
		}
		else
		{
			if (this.rocketSoundUpdater instanceof GCCoreSoundUpdaterSpaceship)
			{
				((GCCoreSoundUpdaterSpaceship) this.rocketSoundUpdater).stopRocketSound();
				this.rocketSoundUpdater.update();
			}
		}
	}

	protected void spawnParticles(boolean launched)
	{
		double x1 = 2 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
		double z1 = 2 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
		double y1 = 2 * Math.cos((this.rotationPitch - 180) * Math.PI / 180.0D);

		if (this.landing && this.targetVec != null)
		{
			double modifier = this.posY - this.targetVec.y;
			modifier = Math.max(modifier, 1.0);
			x1 *= modifier / 60.0D;
			y1 *= modifier / 60.0D;
			z1 *= modifier / 60.0D;
		}

		final double y = this.prevPosY + (this.posY - this.prevPosY) - 0.4;

		if (!this.isDead)
		{
			GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX + 0.2 - this.rand.nextDouble() / 10 + x1, y, this.posZ + 0.2 - this.rand.nextDouble() / 10 + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX - 0.2 + this.rand.nextDouble() / 10 + x1, y, this.posZ + 0.2 - this.rand.nextDouble() / 10 + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX - 0.2 + this.rand.nextDouble() / 10 + x1, y, this.posZ - 0.2 + this.rand.nextDouble() / 10 + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX + 0.2 - this.rand.nextDouble() / 10 + x1, y, this.posZ - 0.2 + this.rand.nextDouble() / 10 + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX + x1, y, this.posZ + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX + 0.2 + x1, y, this.posZ + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX - 0.2 + x1, y, this.posZ + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX + x1, y, this.posZ + 0.2D + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX + x1, y, this.posZ - 0.2D + z1), new Vector3(x1, y1, z1));
		}
	}

	@Override
	public void readNetworkedData(ByteArrayDataInput dataStream)
	{
		this.rocketType = EnumRocketType.values()[dataStream.readInt()];
		super.readNetworkedData(dataStream);
		this.posX = dataStream.readDouble() / 8000.0D;
		this.posY = dataStream.readDouble() / 8000.0D;
		this.posZ = dataStream.readDouble() / 8000.0D;
	}

	@Override
	public ArrayList<Object> getNetworkedData(ArrayList<Object> list)
	{
		list.add(this.rocketType != null ? this.rocketType.getIndex() : 0);
		super.getNetworkedData(list);
		list.add(this.posX * 8000.0D);
		list.add(this.posY * 8000.0D);
		list.add(this.posZ * 8000.0D);
		return list;
	}

	@Override
	public void onReachAtmoshpere()
	{
		if (this.worldObj.isRemote)
		{
			return;
		}

		this.setTarget(true, this.destinationFrequency);

		if (this.targetVec != null)
		{
			if (this.targetDimension != this.worldObj.provider.dimensionId)
			{
				WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(this.targetDimension);

				if (!this.worldObj.isRemote && worldServer != null)
				{
					this.setPosition(this.targetVec.x + 0.5F, this.targetVec.y + 800, this.targetVec.z + 0.5F);
					Entity e = WorldUtil.transferEntityToDimension(this, this.targetDimension, worldServer, false, null);

					if (e instanceof GCMarsEntityCargoRocket)
					{
						e.setPosition(this.targetVec.x + 0.5F, this.targetVec.y + 800, this.targetVec.z + 0.5F);
						((GCMarsEntityCargoRocket) e).landing = true;
					}
				}
			}
			else
			{
				this.setPosition(this.targetVec.x + 0.5F, this.targetVec.y + 800, this.targetVec.z + 0.5F);
				this.landing = true;
			}
		}
		else
		{
			this.setDead();
		}
	}

	@Override
	protected void onRocketLand(int x, int y, int z)
	{
		super.onRocketLand(x, y, z);

		if (this.rocketSoundUpdater instanceof GCCoreSoundUpdaterSpaceship)
		{
			((GCCoreSoundUpdaterSpaceship) this.rocketSoundUpdater).stopRocketSound();
		}
	}

	@Override
	public boolean interactFirst(EntityPlayer par1EntityPlayer)
	{
		if (!this.worldObj.isRemote && par1EntityPlayer instanceof EntityPlayerMP)
		{
			GCMarsUtil.openCargoRocketInventory((EntityPlayerMP) par1EntityPlayer, this);
		}

		return false;
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Type", this.rocketType.getIndex());

		super.writeEntityToNBT(nbt);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
		this.rocketType = EnumRocketType.values()[nbt.getInteger("Type")];

		super.readEntityFromNBT(nbt);
	}

	@Override
	public EnumRocketType getType()
	{
		return this.rocketType;
	}

	@Override
	public int getSizeInventory()
	{
		return this.rocketType.getInventorySpace();
	}

	@Override
	public void onWorldTransferred(World world)
	{
		if (this.targetVec != null)
		{
			this.setPosition(this.targetVec.x + 0.5F, this.targetVec.y + 800, this.targetVec.z + 0.5F);
			this.landing = true;
		}
		else
		{
			this.setDead();
		}
	}

	@Override
	public void onPadDestroyed()
	{
		if (!this.isDead && this.launchPhase != EnumLaunchPhase.LAUNCHED.getPhase())
		{
			this.dropShipAsItem();
			this.setDead();
		}
	}

	@Override
	public int getRocketTier()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public int getPreLaunchWait()
	{
		return 20;
	}

	@Override
	public List<ItemStack> getItemsDropped(List<ItemStack> droppedItemList)
	{
		super.getItemsDropped(droppedItemList);
		droppedItemList.add(new ItemStack(GCMarsItems.spaceship, 1, this.rocketType.getIndex() + 10));
		return droppedItemList;
	}

	@RuntimeInterface(clazz = "calclavia.api.icbm.IMissileLockable", modID = "ICBM|Explosion")
	public boolean canLock(IMissile missile)
	{
		return true;
	}

	@RuntimeInterface(clazz = "calclavia.api.icbm.IMissileLockable", modID = "ICBM|Explosion")
	public Vector3 getPredictedPosition(int ticks)
	{
		return new Vector3(this);
	}

	@RuntimeInterface(clazz = "calclavia.api.icbm.sentry.IAATarget", modID = "ICBM|Explosion")
	public void destroyCraft()
	{
		this.setDead();
	}

	@RuntimeInterface(clazz = "calclavia.api.icbm.sentry.IAATarget", modID = "ICBM|Explosion")
	public int doDamage(int damage)
	{
		return (int) (this.shipDamage += damage);
	}

	@RuntimeInterface(clazz = "calclavia.api.icbm.sentry.IAATarget", modID = "ICBM|Explosion")
	public boolean canBeTargeted(Object entity)
	{
		return this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase() && this.timeSinceLaunch > 50;
	}

	@Override
	public boolean isPlayerRocket()
	{
		return false;
	}
}
