package micdoodle8.mods.galacticraft.core.entities;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.RuntimeInterface;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.sounds.GCCoreSoundUpdaterSpaceship;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumPacketClient;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import calclavia.api.icbm.IMissile;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCoreEntityRocketT1.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreEntityRocketT1 extends EntityTieredRocket
{
	public IUpdatePlayerListBox rocketSoundUpdater;

	public GCCoreEntityRocketT1(World par1World)
	{
		super(par1World);
	}

	public GCCoreEntityRocketT1(World par1World, double par2, double par4, double par6, EnumRocketType rocketType)
	{
		super(par1World, par2, par4, par6);
		this.rocketType = rocketType;
		this.cargoItems = new ItemStack[this.getSizeInventory()];
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
					this.motionY = -d * Math.cos((this.rotationPitch - 180) * Math.PI / 180.0D);
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

		if (!this.worldObj.isRemote)
		{
			PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, 100, this.worldObj.provider.dimensionId, GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.getNetworkedData(new ArrayList())));
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
	public void onTeleport(EntityPlayerMP player)
	{
		final GCCorePlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

		player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.ZOOM_CAMERA, new Object[] { 0 }));

		if (playerBase != null)
		{
			if (this.cargoItems == null || this.cargoItems.length == 0)
			{
				playerBase.setRocketStacks(new ItemStack[9]);
			}
			else
			{
				playerBase.setRocketStacks(this.cargoItems);
			}

			playerBase.setRocketType(this.rocketType.getIndex());
			playerBase.setRocketItem(GCCoreItems.rocketTier1);
			playerBase.setFuelLevel(this.fuelTank.getFluidAmount());
		}
	}

	protected void spawnParticles(boolean launched)
	{
		final double x1 = 2 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
		final double z1 = 2 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
		double y1 = 2 * Math.cos((this.rotationPitch - 180) * Math.PI / 180.0D);

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

	@Override
	public int getPreLaunchWait()
	{
		return 400;
	}

	@Override
	public List<ItemStack> getItemsDropped(List<ItemStack> droppedItems)
	{
		super.getItemsDropped(droppedItems);
		droppedItems.add(new ItemStack(GCCoreItems.rocketTier1, 1, this.rocketType.getIndex()));
		return droppedItems;
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return false;
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
		return (int) (this.shipDamage += damage);
	}

	@RuntimeInterface(clazz = "calclavia.api.icbm.sentry.IAATarget", modID = "ICBM|Explosion")
	public boolean canBeTargeted(Object entity)
	{
		return this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase() && this.timeSinceLaunch > 50;
	}

	@Override
	public int getRocketTier()
	{
		return 1;
	}

	@Override
	public int getFuelTankCapacity()
	{
		return 1000;
	}
}
