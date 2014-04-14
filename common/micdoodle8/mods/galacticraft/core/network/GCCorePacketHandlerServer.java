package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GCLog;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreSpaceStationData;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerSchematic;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumPacketClient;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAirLockController;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityParachest;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCorePacketHandlerServer.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCorePacketHandlerServer implements IPacketHandler
{
	public static enum EnumPacketServer
	{
		UNUSED_0(0),
		RESPAWN_PLAYER(1, String.class),
		TELEPORT_ENTITY(2, String.class),
		IGNITE_ROCKET(3),
		OPEN_SCHEMATIC_PAGE(4, Integer.class),
		UNUSED_1(5),
		OPEN_SPACESHIP_INV(6, Integer.class),
		UPDATE_SHIP_YAW(7, Float.class),
		UPDATE_SHIP_PITCH(8, Float.class),
		UNUSED_2(9),
		SET_ENTITY_FIRE(10, Integer.class),
		OPEN_REFINERY_GUI(11, Integer.class, Integer.class, Integer.class),
		UPDATE_CONTROLLABLE_ENTITY(12),
		UNUSED_3(13),
		UPDATE_ADVANCED_ENTITY(14),
		BIND_SPACE_STATION_ID(15, Integer.class),
		UNLOCK_NEW_SCHEMATIC(16),
		UPDATE_DISABLEABLE_BUTTON(
				17,
				Integer.class,
				Integer.class,
				Integer.class,
				Integer.class),
		ON_FAILED_CHEST_UNLOCK(18, Integer.class),
		RENAME_SPACE_STATION(19, String.class, Integer.class),
		OPEN_BUGGY_INV(20),
		UPDATE_DYNAMIC_ENTITY_INV(21, Integer.class),
		UPDATE_DYNAMIC_TILE_INV(22, Integer.class, Integer.class, Integer.class),
		OPEN_EXTENDED_INVENTORY(23),
		ON_ADVANCED_GUI_CLICKED_INT(
				24,
				Integer.class,
				Integer.class,
				Integer.class,
				Integer.class,
				Integer.class),
		ON_ADVANCED_GUI_CLICKED_STRING(
				25,
				Integer.class,
				Integer.class,
				Integer.class,
				Integer.class,
				String.class),
		UPDATE_SHIP_MOTION_Y(26, Integer.class, Boolean.class);

		private int index;
		private Class<?>[] decodeAs;

		private EnumPacketServer(int index, Class<?>... decodeAs)
		{
			this.index = index;
			this.decodeAs = decodeAs;
		}

		public int getIndex()
		{
			return this.index;
		}

		public Class<?>[] getDecodeClasses()
		{
			return this.decodeAs;
		}
	}

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
		final GCCorePlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

		Object[] packetReadout = null;
		EnumPacketServer packetInfo = EnumPacketServer.values()[packetType];

		if (packetInfo.getDecodeClasses() != null && packetInfo.getDecodeClasses().length > 0)
		{
			packetReadout = PacketUtil.readPacketData(data, packetInfo.getDecodeClasses());
		}

		switch (packetInfo)
		{
		case UNUSED_0:
			break;
		case RESPAWN_PLAYER:
			player.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(player.dimension, (byte) player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.worldObj.getHeight(), player.theItemInWorldManager.getGameType()));
			break;
		case TELEPORT_ENTITY:
			if (playerBase != null)
			{
				try
				{
					final WorldProvider provider = WorldUtil.getProviderForName((String) packetReadout[0]);
					final Integer dim = provider.dimensionId;
					GCLog.info("Found matching world name for " + (String) packetReadout[0]);

					if (playerBase.worldObj instanceof WorldServer)
					{
						final WorldServer world = (WorldServer) playerBase.worldObj;

						if (provider instanceof IOrbitDimension)
						{
							WorldUtil.transferEntityToDimension(playerBase, dim, world);
						}
						else
						{
							WorldUtil.transferEntityToDimension(playerBase, dim, world);
						}
					}

					playerBase.setTeleportCooldown(300);
					final Object[] toSend = { player.username };
					player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.CLOSE_GUI, toSend));
				}
				catch (final Exception e)
				{
					GCLog.severe("Error occurred when attempting to transfer entity to dimension: " + (String) packetReadout[0]);
					e.printStackTrace();
				}
			}
			break;
		case IGNITE_ROCKET:
			if (!player.worldObj.isRemote && !player.isDead && player.ridingEntity != null && !player.ridingEntity.isDead && player.ridingEntity instanceof EntityTieredRocket)
			{
				final EntityTieredRocket ship = (EntityTieredRocket) player.ridingEntity;

				if (ship.hasValidFuel())
				{
					ItemStack stack2 = null;

					if (playerBase != null)
					{
						stack2 = playerBase.getExtendedInventory().getStackInSlot(4);
					}

					if (stack2 != null && stack2.getItem() instanceof GCCoreItemParachute || playerBase != null && playerBase.getLaunchAttempts() > 0)
					{
						ship.igniteCheckingCooldown();
						playerBase.setLaunchAttempts(0);
					}
					else if (playerBase.getChatCooldown() == 0 && playerBase.getLaunchAttempts() == 0)
					{
						player.sendChatToPlayer(ChatMessageComponent.createFromText("I don't have a parachute! If I press launch again, there's no going back!"));
						playerBase.setChatCooldown(250);
						playerBase.setLaunchAttempts(1);
					}
				}
				else if (playerBase.getChatCooldown() == 0)
				{
					player.sendChatToPlayer(ChatMessageComponent.createFromText("I'll need to load in some rocket fuel first!"));
					playerBase.setChatCooldown(250);
				}
			}
			break;
		case OPEN_SCHEMATIC_PAGE:
			if (player != null)
			{
				final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID((Integer) packetReadout[0]);

				player.openGui(GalacticraftCore.instance, page.getGuiID(), player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
			}
			break;
		case UNUSED_1:
			break;
		case OPEN_SPACESHIP_INV:
			player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiSpaceshipInventory, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
			break;
		case UPDATE_SHIP_YAW:
			if (player.ridingEntity instanceof EntitySpaceshipBase)
			{
				final EntitySpaceshipBase ship = (EntitySpaceshipBase) player.ridingEntity;

				if (ship != null)
				{
					ship.rotationYaw = (Float) packetReadout[0];
				}
			}
			break;
		case UPDATE_SHIP_PITCH:
			if (player.ridingEntity instanceof EntitySpaceshipBase)
			{
				final EntitySpaceshipBase ship = (EntitySpaceshipBase) player.ridingEntity;

				if (ship != null)
				{
					ship.rotationPitch = (Float) packetReadout[0];
				}
			}
			break;
		case UNUSED_2:
			break;
		case SET_ENTITY_FIRE:
			for (final Object object : player.worldObj.loadedEntityList)
			{
				if (object instanceof EntityLiving)
				{
					final EntityLiving entity = (EntityLiving) object;

					if (entity.entityId == (Integer) packetReadout[0] && entity.ridingEntity == null)
					{
						entity.setFire(3);
					}
				}
			}
			break;
		case OPEN_REFINERY_GUI:
			player.openGui(GalacticraftCore.instance, -1, player.worldObj, (Integer) packetReadout[0], (Integer) packetReadout[1], (Integer) packetReadout[2]);
			break;
		case UPDATE_CONTROLLABLE_ENTITY:
			try
			{
				new GCCorePacketControllableEntity().handlePacket(data, new Object[] { player }, Side.SERVER);
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
			break;
		case UNUSED_3:
			break;
		case UPDATE_ADVANCED_ENTITY:
			try
			{
				new GCCorePacketEntityUpdate().handlePacket(data, new Object[] { player }, Side.SERVER);
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
			break;
		case BIND_SPACE_STATION_ID:
			if (playerBase.getSpaceStationDimensionID() == -1 || playerBase.getSpaceStationDimensionID() == 0)
			{
				WorldUtil.bindSpaceStationToNewDimension(playerBase.worldObj, playerBase);

				WorldUtil.getSpaceStationRecipe((Integer) packetReadout[0]).matches(playerBase, true);
			}
			break;
		case UNLOCK_NEW_SCHEMATIC:
			final Container container = player.openContainer;

			if (container instanceof GCCoreContainerSchematic)
			{
				final GCCoreContainerSchematic schematicContainer = (GCCoreContainerSchematic) container;

				ItemStack stack = schematicContainer.craftMatrix.getStackInSlot(0);

				if (stack != null)
				{
					final ISchematicPage page = SchematicRegistry.getMatchingRecipeForItemStack(stack);

					if (page != null)
					{
						SchematicRegistry.unlockNewPage(playerBase, stack);

						if (--stack.stackSize <= 0)
						{
							stack = null;
						}

						schematicContainer.craftMatrix.setInventorySlotContents(0, stack);
						schematicContainer.craftMatrix.onInventoryChanged();

						player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.ADD_NEW_SCHEMATIC, new Object[] { page.getPageID() }));
					}
				}
			}
			break;
		case UPDATE_DISABLEABLE_BUTTON:
			final TileEntity tileAt = player.worldObj.getBlockTileEntity((Integer) packetReadout[0], (Integer) packetReadout[1], (Integer) packetReadout[2]);

			if (tileAt instanceof IDisableableMachine)
			{
				final IDisableableMachine machine = (IDisableableMachine) tileAt;

				machine.setDisabled((Integer) packetReadout[3], !machine.getDisabled((Integer) packetReadout[3]));
			}
			break;
		case ON_FAILED_CHEST_UNLOCK:
			if (playerBase.getChatCooldown() == 0)
			{
				player.sendChatToPlayer(ChatMessageComponent.createFromText("I'll probably need a Tier " + packetReadout[0] + " Dungeon key to unlock this!"));
				playerBase.setChatCooldown(100);
			}
			break;
		case RENAME_SPACE_STATION:
			final GCCoreSpaceStationData ssdata = GCCoreSpaceStationData.getStationData(playerBase.worldObj, (Integer) packetReadout[1], playerBase);

			if (ssdata != null && ssdata.getOwner().equalsIgnoreCase(player.username))
			{
				ssdata.setSpaceStationName((String) packetReadout[0]);
				ssdata.setDirty(true);
			}
			break;
		case OPEN_BUGGY_INV:
			if (player.ridingEntity instanceof GCCoreEntityBuggy)
			{
				GCCoreUtil.openBuggyInv(player, (GCCoreEntityBuggy) player.ridingEntity, ((GCCoreEntityBuggy) player.ridingEntity).getType());
			}
			break;
		case UPDATE_DYNAMIC_ENTITY_INV:
			Entity e = player.worldObj.getEntityByID((Integer) packetReadout[0]);

			if (e != null && e instanceof IInventorySettable)
			{
				player.playerNetServerHandler.sendPacketToPlayer(GCCorePacketLanderUpdate.buildKeyPacket(e));
			}
			break;
		case UPDATE_DYNAMIC_TILE_INV:
			TileEntity tile = player.worldObj.getBlockTileEntity((Integer) packetReadout[0], (Integer) packetReadout[1], (Integer) packetReadout[2]);

			if (tile != null && tile instanceof GCCoreTileEntityParachest)
			{
				new GCCorePacketParachestUpdate();
				player.playerNetServerHandler.sendPacketToPlayer(GCCorePacketParachestUpdate.buildKeyPacket((GCCoreTileEntityParachest) tile));
			}
			break;
		case OPEN_EXTENDED_INVENTORY:
			player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiExtendedInventory, player.worldObj, 0, 0, 0);
			break;
		case ON_ADVANCED_GUI_CLICKED_INT:
			TileEntity tile1 = player.worldObj.getBlockTileEntity((Integer) packetReadout[1], (Integer) packetReadout[2], (Integer) packetReadout[3]);

			switch ((Integer) packetReadout[0])
			{
			case 0:
				if (tile1 instanceof GCCoreTileEntityAirLockController)
				{
					GCCoreTileEntityAirLockController launchController = (GCCoreTileEntityAirLockController) tile1;
					launchController.redstoneActivation = (Integer) packetReadout[4] == 1;
				}
				break;
			case 1:
				if (tile1 instanceof GCCoreTileEntityAirLockController)
				{
					GCCoreTileEntityAirLockController launchController = (GCCoreTileEntityAirLockController) tile1;
					launchController.playerDistanceActivation = (Integer) packetReadout[4] == 1;
				}
				break;
			case 2:
				if (tile1 instanceof GCCoreTileEntityAirLockController)
				{
					GCCoreTileEntityAirLockController launchController = (GCCoreTileEntityAirLockController) tile1;
					launchController.playerDistanceSelection = (Integer) packetReadout[4];
				}
				break;
			case 3:
				if (tile1 instanceof GCCoreTileEntityAirLockController)
				{
					GCCoreTileEntityAirLockController launchController = (GCCoreTileEntityAirLockController) tile1;
					launchController.playerNameMatches = (Integer) packetReadout[4] == 1;
				}
				break;
			case 4:
				if (tile1 instanceof GCCoreTileEntityAirLockController)
				{
					GCCoreTileEntityAirLockController launchController = (GCCoreTileEntityAirLockController) tile1;
					launchController.invertSelection = (Integer) packetReadout[4] == 1;
				}
				break;
			case 5:
				if (tile1 instanceof GCCoreTileEntityAirLockController)
				{
					GCCoreTileEntityAirLockController launchController = (GCCoreTileEntityAirLockController) tile1;
					launchController.lastHorizontalModeEnabled = launchController.horizontalModeEnabled;
					launchController.horizontalModeEnabled = (Integer) packetReadout[4] == 1;
				}
				break;
			default:
				break;
			}
			break;
		case ON_ADVANCED_GUI_CLICKED_STRING:
			TileEntity tile2 = player.worldObj.getBlockTileEntity((Integer) packetReadout[1], (Integer) packetReadout[2], (Integer) packetReadout[3]);

			switch ((Integer) packetReadout[0])
			{
			case 0:
				if (tile2 instanceof GCCoreTileEntityAirLockController)
				{
					GCCoreTileEntityAirLockController launchController = (GCCoreTileEntityAirLockController) tile2;
					launchController.playerToOpenFor = (String) packetReadout[4];
				}
				break;
			default:
				break;
			}
			break;
		case UPDATE_SHIP_MOTION_Y:
			int entityID = (Integer) packetReadout[0];
			boolean up = (Boolean) packetReadout[1];

			Entity entity = player.worldObj.getEntityByID(entityID);

			if (entity instanceof EntityAutoRocket)
			{
				EntityAutoRocket autoRocket = (EntityAutoRocket) entity;
				autoRocket.motionY += up ? 0.02F : -0.02F;
			}

			break;
		}
	}
}
