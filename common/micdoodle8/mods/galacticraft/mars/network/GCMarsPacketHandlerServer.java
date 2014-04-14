package micdoodle8.mods.galacticraft.mars.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityLander;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketLanderUpdate;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntityCargoRocket;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntitySlimeling;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityLaunchController;
import micdoodle8.mods.galacticraft.mars.util.GCMarsUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

/**
 * GCMarsPacketHandlerServer.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsPacketHandlerServer implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player p)
	{
		if (packet == null)
		{
			FMLLog.severe("Packet received as null!");
			return;
		}

		if (packet.data == null)
		{
			FMLLog.severe("Packet data received as null! ID " + packet.getPacketId());
			return;
		}

		final DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));

		final int packetType = PacketUtil.readPacketID(data);

		final EntityPlayerMP player = (EntityPlayerMP) p;

		GCCorePlayerMP gcPlayer = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

		if (packetType == 0)
		{
			Class<?>[] decodeAs = { Integer.class, Integer.class, String.class };
			Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

			Entity entity = player.worldObj.getEntityByID((Integer) packetReadout[0]);

			if (entity instanceof GCMarsEntitySlimeling)
			{
				GCMarsEntitySlimeling slimeling = (GCMarsEntitySlimeling) entity;

				int subType = (Integer) packetReadout[1];

				switch (subType)
				{
				case 0:
					if (player.getCommandSenderName().equalsIgnoreCase(slimeling.getOwnerName()) && !slimeling.worldObj.isRemote)
					{
						slimeling.getAiSit().setSitting(!slimeling.isSitting());
						slimeling.setJumping(false);
						slimeling.setPathToEntity((PathEntity) null);
						slimeling.setTarget((Entity) null);
						slimeling.setAttackTarget((EntityLivingBase) null);
					}
					break;
				case 1:
					if (player.getCommandSenderName().equalsIgnoreCase(slimeling.getOwnerName()) && !slimeling.worldObj.isRemote)
					{
						slimeling.slimelingName = (String) packetReadout[2];
					}
					break;
				case 2:
					if (player.getCommandSenderName().equalsIgnoreCase(slimeling.getOwnerName()) && !slimeling.worldObj.isRemote)
					{
						slimeling.age += 5000;
					}
					break;
				case 3:
					if (slimeling.inLove <= 0 && player.getCommandSenderName().equalsIgnoreCase(slimeling.getOwnerName()) && !slimeling.worldObj.isRemote)
					{
						slimeling.func_110196_bT();
					}
					break;
				case 4:
					if (player.getCommandSenderName().equalsIgnoreCase(slimeling.getOwnerName()) && !slimeling.worldObj.isRemote)
					{
						slimeling.attackDamage = Math.min(slimeling.attackDamage + 0.1F, 1.0F);
					}
					break;
				case 5:
					if (player.getCommandSenderName().equalsIgnoreCase(slimeling.getOwnerName()) && !slimeling.worldObj.isRemote)
					{
						slimeling.setHealth(slimeling.getHealth() + 5.0F);
					}
					break;
				case 6:
					if (player.getCommandSenderName().equalsIgnoreCase(slimeling.getOwnerName()) && !slimeling.worldObj.isRemote)
					{
						GCMarsUtil.openSlimelingInventory(player, slimeling);
					}
					break;
				}
			}
		}
		else if (packetType == 1)
		{
			FMLLog.severe("Galacticraft Mars: Received bad packet!");
		}
		else if (packetType == 2)
		{
			FMLLog.severe("Galacticraft Mars: Received bad packet!");
		}
		else if (packetType == 3)
		{
			gcPlayer.wakeUpPlayer(false, true, true, true);
		}
		else if (packetType == 4)
		{
			final Class<?>[] decodeAs = { Integer.class };
			final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
			Entity e = player.worldObj.getEntityByID((Integer) packetReadout[0]);

			if (e != null && e instanceof GCCoreEntityLander)
			{
				player.playerNetServerHandler.sendPacketToPlayer(GCCorePacketLanderUpdate.buildKeyPacket(e));
			}
		}
		else if (packetType == 5)
		{
			Class<?>[] decodeAs = { Integer.class, Integer.class, Integer.class, Integer.class, Integer.class };
			Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

			TileEntity tile = player.worldObj.getBlockTileEntity((Integer) packetReadout[1], (Integer) packetReadout[2], (Integer) packetReadout[3]);

			switch ((Integer) packetReadout[0])
			{
			case 0:
				if (tile instanceof GCMarsTileEntityLaunchController)
				{
					GCMarsTileEntityLaunchController launchController = (GCMarsTileEntityLaunchController) tile;
					launchController.setFrequency((Integer) packetReadout[4]);
				}
				break;
			case 1:
				if (tile instanceof GCMarsTileEntityLaunchController)
				{
					GCMarsTileEntityLaunchController launchController = (GCMarsTileEntityLaunchController) tile;
					launchController.launchDropdownSelection = (Integer) packetReadout[4];
				}
				break;
			case 2:
				if (tile instanceof GCMarsTileEntityLaunchController)
				{
					GCMarsTileEntityLaunchController launchController = (GCMarsTileEntityLaunchController) tile;
					launchController.setDestinationFrequency((Integer) packetReadout[4]);
				}
				break;
			case 3:
				if (tile instanceof GCMarsTileEntityLaunchController)
				{
					GCMarsTileEntityLaunchController launchController = (GCMarsTileEntityLaunchController) tile;
					launchController.launchPadRemovalDisabled = (Integer) packetReadout[4] == 1 ? true : false;
				}
				break;
			case 4:
				if (tile instanceof GCMarsTileEntityLaunchController)
				{
					GCMarsTileEntityLaunchController launchController = (GCMarsTileEntityLaunchController) tile;
					launchController.launchSchedulingEnabled = (Integer) packetReadout[4] == 1 ? true : false;
				}
				break;
			case 5:
				if (tile instanceof GCMarsTileEntityLaunchController)
				{
					GCMarsTileEntityLaunchController launchController = (GCMarsTileEntityLaunchController) tile;
					launchController.requiresClientUpdate = true;
				}
				break;
			default:
				break;
			}
		}
		else if (packetType == 6)
		{
			Class<?>[] decodeAs = { Integer.class, Integer.class };
			Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

			Entity entity = player.worldObj.getEntityByID((Integer) packetReadout[0]);

			if (entity instanceof GCMarsEntityCargoRocket)
			{
				GCMarsEntityCargoRocket rocket = (GCMarsEntityCargoRocket) entity;

				int subType = (Integer) packetReadout[1];

				switch (subType)
				{
				default:
					rocket.statusValid = rocket.checkLaunchValidity();
					break;
				}
			}
		}
	}
}
