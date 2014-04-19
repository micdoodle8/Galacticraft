package micdoodle8.mods.galacticraft.mars.entities;

import calclavia.api.icbm.IMissile;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.RuntimeInterface;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.sounds.GCCoreSoundUpdaterSpaceship;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumPacketClient;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

/**
 * GCMarsEntityRocketT2.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsEntityRocketT2 extends EntityTieredRocket
{
	public IUpdatePlayerListBox rocketSoundUpdater;

	public GCMarsEntityRocketT2(World par1World)
	{
		super(par1World);
	}

	public GCMarsEntityRocketT2(World par1World, double par2, double par4, double par6, EnumRocketType rocketType)
	{
		super(par1World, par2, par4, par6);
		this.rocketType = rocketType;
		this.cargoItems = new ItemStack[this.getSizeInventory()];
	}

	public GCMarsEntityRocketT2(World par1World, double par2, double par4, double par6, boolean reversed, EnumRocketType rocketType, ItemStack[] inv)
	{
		this(par1World, par2, par4, par6, rocketType);
		this.cargoItems = inv;
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
	}

	@Override
	public void setDead()
	{
		super.setDead();

		if (this.rocketSoundUpdater != null)
		{
			this.rocketSoundUpdater.update();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onUpdate()
	{
		super.onUpdate();

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

		if (this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase() && this.hasValidFuel())
		{
			if (!this.landing)
			{
				double d = this.timeSinceLaunch / 150;

				d = Math.min(d, 1);

				if (d != 0.0)
				{
					this.motionY = -d * 1.5D * Math.cos((this.rotationPitch - 180) * Math.PI / 180.0D);
				}
			}
			else
			{
				this.motionY -= 0.008D;
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

			if (this.timeSinceLaunch % MathHelper.floor_double(2 * (1 / multiplier)) == 0)
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

		if (!this.worldObj.isRemote)
		{
			PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, 100, this.worldObj.provider.dimensionId, GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.getNetworkedData(new ArrayList())));
		}
	}

	@Override
	public void onTeleport(EntityPlayerMP player)
	{
		final GCCorePlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

		player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.ZOOM_CAMERA, new Object[] { 0 }));

		if (playerBase != null)
		{
			if (this.cargoItems == null || this.cargoItems.length == 0)
			{
				playerBase.setRocketStacks(new ItemStack[3]);
			}
			else
			{
				playerBase.setRocketStacks(this.cargoItems);
			}

			playerBase.setRocketType(this.rocketType.getIndex());
			playerBase.setRocketItem(GCMarsItems.spaceship);
			playerBase.setFuelLevel(this.fuelTank.getFluidAmount());
		}
	}

	protected void spawnParticles(boolean launched)
	{
		final double x1 = 2.9 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
		final double z1 = 2.9 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
		double y1 = 2.9 * Math.cos((this.rotationPitch - 180) * Math.PI / 180.0D);

		final double y = this.prevPosY + (this.posY - this.prevPosY);

		if (!this.isDead)
		{
			GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX + 0.4 - this.rand.nextDouble() / 10 + x1, y - 0.0D + y1, this.posZ + 0.4 - this.rand.nextDouble() / 10 + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX - 0.4 + this.rand.nextDouble() / 10 + x1, y - 0.0D + y1, this.posZ + 0.4 - this.rand.nextDouble() / 10 + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX - 0.4 + this.rand.nextDouble() / 10 + x1, y - 0.0D + y1, this.posZ - 0.4 + this.rand.nextDouble() / 10 + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX + 0.4 - this.rand.nextDouble() / 10 + x1, y - 0.0D + y1, this.posZ - 0.4 + this.rand.nextDouble() / 10 + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX + x1, y - 0.0D + y1, this.posZ + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX + 0.4 + x1, y - 0.0D + y1, this.posZ + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX - 0.4 + x1, y - 0.0D + y1, this.posZ + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX + x1, y - 0.0D + y1, this.posZ + 0.4D + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX + x1, y - 0.0D + y1, this.posZ - 0.4D + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle("blueflame", new Vector3(this.posX + x1 - 0.8, y - 0.0D + y1, this.posZ + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle("blueflame", new Vector3(this.posX + x1 + 0.8, y - 0.0D + y1, this.posZ + z1), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle("blueflame", new Vector3(this.posX + x1, y - 0.0D + y1, this.posZ + z1 - 0.8), new Vector3(x1, y1, z1));
			GalacticraftCore.proxy.spawnParticle("blueflame", new Vector3(this.posX + x1, y - 0.0D + y1, this.posZ + z1 + 0.8), new Vector3(x1, y1, z1));
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
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.isDead ? false : par1EntityPlayer.getDistanceSqToEntity(this) <= 64.0D;
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeEntityToNBT(par1NBTTagCompound);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readEntityFromNBT(par1NBTTagCompound);
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
	public boolean isDockValid(IFuelDock dock)
	{
		return dock instanceof GCCoreTileEntityLandingPad;
	}

	@RuntimeInterface(clazz = "calclavia.api.icbm.sentry.IAATarget", modID = "ICBM|Explosion")
	public void destroyCraft()
	{
		this.setDead();
	}

	@RuntimeInterface(clazz = "calclavia.api.icbm.sentry.IAATarget", modID = "ICBM|Explosion")
	public int doDamage(int damage)
	{
		this.shipDamage += damage;
		return damage;
	}

	@RuntimeInterface(clazz = "calclavia.api.icbm.sentry.IAATarget", modID = "ICBM|Explosion")
	public boolean canBeTargeted(Object entity)
	{
		return this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase() && this.timeSinceLaunch > 50;
	}

	@Override
	public int getRocketTier()
	{
		return 2;
	}

	@Override
	public int getFuelTankCapacity()
	{
		return 1500;
	}

	@Override
	public int getPreLaunchWait()
	{
		return 400;
	}
}
