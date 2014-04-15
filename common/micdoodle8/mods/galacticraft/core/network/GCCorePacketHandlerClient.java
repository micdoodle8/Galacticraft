package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GCLog;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityWeldingSmoke;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiBuggy;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiChoosePlanet;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiGalaxyMap;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiParachest;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreSpaceStationData;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP.EnumModelPacket;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import micdoodle8.mods.galacticraft.core.tick.GCCoreKeyHandlerClient;
import micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerClient;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityConductor;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCorePacketHandlerClient.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCorePacketHandlerClient implements IPacketHandler
{
	Minecraft mc = FMLClientHandler.instance().getClient();

	public static enum EnumPacketClient
	{
		AIR_REMAINING(0, Integer.class, Integer.class, String.class),
		INVALID(1),
		UPDATE_DIMENSION_LIST(2, String.class, String.class),
		UNUSED_0(3),
		UNUSED_1(4),
		UNUSED_2(5),
		UNUSED_3(6),
		UNUSED_4(7),
		MOUNT_ROCKET(8, String.class),
		SPAWN_SPARK_PARTICLES(9, Integer.class, Integer.class, Integer.class),
		UPDATE_GEAR_SLOT(10, String.class, Integer.class, Integer.class),
		UNUSED_5(11),
		CLOSE_GUI(12),
		RESET_THIRD_PERSON(13, String.class),
		UPDATE_CONTROLLABLE_ENTITY(14),
		UNUSED_6(15),
		UPDATE_SPACESTATION_LIST(16),
		UPDATE_SPACESTATION_DATA(17),
		UPDATE_SPACESTATION_CLIENT_ID(18, Integer.class),
		UPDATE_PLANETS_LIST(19),
		ADD_NEW_SCHEMATIC(20, Integer.class),
		UPDATE_SCHEMATIC_LIST(21),
		ZOOM_CAMERA(22, Integer.class),
		PLAY_SOUND_BOSS_DEATH(23),
		PLAY_SOUND_EXPLODE(24),
		PLAY_SOUND_BOSS_LAUGH(25),
		PLAY_SOUND_BOW(26),
		UPDATE_OXYGEN_VALIDITY(27, Boolean.class),
		OPEN_PARACHEST_GUI(28, Integer.class, Integer.class, Integer.class),
		UPDATE_LANDER(29),
		UPDATE_PARACHEST(30),
		UPDATE_WIRE_BOUNDS(31, Integer.class, Integer.class, Integer.class);

		private int index;
		private Class<?>[] decodeAs;

		private EnumPacketClient(int index, Class<?>... decodeAs)
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

		final EntityPlayer player = (EntityPlayer) p;

		GCCorePlayerSP playerBaseClient = null;

		if (player != null)
		{
			playerBaseClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);
		}

		EnumPacketClient packetType = EnumPacketClient.values()[PacketUtil.readPacketID(data)];

		Class<?>[] decodeAs = packetType.getDecodeClasses();
		Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

		switch (packetType)
		{
		case AIR_REMAINING:
			if (String.valueOf(packetReadout[2]).equals(String.valueOf(FMLClientHandler.instance().getClient().thePlayer.username)))
			{
				GCCoreTickHandlerClient.airRemaining = (Integer) packetReadout[0];
				GCCoreTickHandlerClient.airRemaining2 = (Integer) packetReadout[1];
			}
			break;
		case INVALID:
			GCLog.severe("Found incorrect packet! Please report this as a bug.");
			break;
		case UPDATE_DIMENSION_LIST:
			if (String.valueOf(packetReadout[0]).equals(FMLClientHandler.instance().getClient().thePlayer.username))
			{
				final String[] destinations = ((String) packetReadout[1]).split("\\.");

				if (FMLClientHandler.instance().getClient().theWorld != null && !(FMLClientHandler.instance().getClient().currentScreen instanceof GCCoreGuiChoosePlanet || FMLClientHandler.instance().getClient().currentScreen instanceof GCCoreGuiGalaxyMap))
				{
					FMLClientHandler.instance().getClient().displayGuiScreen(new GCCoreGuiChoosePlanet(FMLClientHandler.instance().getClient().thePlayer, destinations));
				}
				else if (FMLClientHandler.instance().getClient().currentScreen instanceof GCCoreGuiChoosePlanet)
				{
					((GCCoreGuiChoosePlanet) FMLClientHandler.instance().getClient().currentScreen).updateDimensionList(destinations);
				}
			}
			break;
		case UNUSED_0:
			break;
		case UNUSED_1:
			break;
		case UNUSED_2:
			break;
		case UNUSED_3:
			break;
		case UNUSED_4:
			break;
		case MOUNT_ROCKET:
			if (playerBaseClient != null)
			{
				playerBaseClient.setThirdPersonView(FMLClientHandler.instance().getClient().gameSettings.thirdPersonView);
			}

			FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = 1;

			player.sendChatToPlayer(ChatMessageComponent.createFromText("SPACE - Launch"));
			player.sendChatToPlayer(ChatMessageComponent.createFromText("A / D  - Turn left-right"));
			player.sendChatToPlayer(ChatMessageComponent.createFromText("W / S  - Turn up-down"));
			player.sendChatToPlayer(ChatMessageComponent.createFromText(Keyboard.getKeyName(GCCoreKeyHandlerClient.openSpaceshipInv.keyCode) + "       - Inventory / Fuel"));
			break;
		case SPAWN_SPARK_PARTICLES:
			int x,
			y,
			z;
			x = (Integer) packetReadout[0];
			y = (Integer) packetReadout[1];
			z = (Integer) packetReadout[2];

			for (int i = 0; i < 4; i++)
			{
				if (this.mc != null && this.mc.renderViewEntity != null && this.mc.effectRenderer != null && this.mc.theWorld != null)
				{
					final EntityFX fx = new GCCoreEntityWeldingSmoke(this.mc.theWorld, x - 0.15 + 0.5, y + 1.2, z + 0.15 + 0.5, this.mc.theWorld.rand.nextDouble() / 20 - this.mc.theWorld.rand.nextDouble() / 20, 0.06, this.mc.theWorld.rand.nextDouble() / 20 - this.mc.theWorld.rand.nextDouble() / 20, 1.0F);
					if (fx != null)
					{
						this.mc.effectRenderer.addEffect(fx);
					}
				}
			}
			break;
		case UPDATE_GEAR_SLOT:
			PlayerGearData gearData = null;
			int subtype = (Integer) packetReadout[2];

			for (PlayerGearData gearData2 : ClientProxyCore.playerItemData)
			{
				if (gearData2.getPlayer().username.equals(packetReadout[0]))
				{
					gearData = gearData2;
					break;
				}
			}

			if (gearData == null)
			{
				gearData = new PlayerGearData(player);
			}

			switch (EnumModelPacket.values()[(Integer) packetReadout[1]])
			{
			case ADDMASK:
				gearData.setMask(0);
				break;
			case REMOVEMASK:
				gearData.setMask(-1);
				break;
			case ADDGEAR:
				gearData.setGear(0);
				break;
			case REMOVEGEAR:
				gearData.setGear(-1);
				break;
			case ADDLEFTGREENTANK:
				gearData.setLeftTank(0);
				break;
			case ADDLEFTORANGETANK:
				gearData.setLeftTank(1);
				break;
			case ADDLEFTREDTANK:
				gearData.setLeftTank(2);
				break;
			case ADDRIGHTGREENTANK:
				gearData.setRightTank(0);
				break;
			case ADDRIGHTORANGETANK:
				gearData.setRightTank(1);
				break;
			case ADDRIGHTREDTANK:
				gearData.setRightTank(2);
				break;
			case REMOVE_LEFT_TANK:
				gearData.setLeftTank(-1);
				break;
			case REMOVE_RIGHT_TANK:
				gearData.setRightTank(-1);
				break;
			case ADD_PARACHUTE:
				String name = "";

				if (subtype != -1)
				{
					name = GCCoreItemParachute.names[subtype];
					gearData.setParachute(new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/parachute/" + name + ".png"));
				}
				break;
			case REMOVE_PARACHUTE:
				gearData.setParachute(null);
				break;
			case ADD_FREQUENCY_MODULE:
				gearData.setFrequencyModule(0);
				break;
			case REMOVE_FREQUENCY_MODULE:
				gearData.setFrequencyModule(-1);
				break;
			default:
				break;
			}

			ClientProxyCore.playerItemData.add(gearData);

			break;
		case UNUSED_5:
			break;
		case CLOSE_GUI:
			FMLClientHandler.instance().getClient().displayGuiScreen(null);
			break;
		case RESET_THIRD_PERSON:
			if (playerBaseClient != null)
			{
				FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = playerBaseClient.getThirdPersonView();
			}
			break;
		case UPDATE_CONTROLLABLE_ENTITY:
			try
			{
				new GCCorePacketEntityUpdate().handlePacket(data, new Object[] { player }, Side.SERVER);
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
			break;
		case UNUSED_6:
			break;
		case UPDATE_SPACESTATION_LIST:
			if (WorldUtil.registeredSpaceStations == null)
			{
				WorldUtil.registeredSpaceStations = new ArrayList<Integer>();
			}

			try
			{
				final int var1 = data.readInt();

				for (int var2 = 0; var2 < var1; ++var2)
				{
					final int var3 = data.readInt();
					boolean staticDim = data.readBoolean();

					if (!WorldUtil.registeredSpaceStations.contains(Integer.valueOf(var3)))
					{
						WorldUtil.registeredSpaceStations.add(Integer.valueOf(var3));
						DimensionManager.registerDimension(var3, staticDim ? GCCoreConfigManager.idDimensionOverworldOrbitStatic : GCCoreConfigManager.idDimensionOverworldOrbit);
					}
				}
			}
			catch (final IOException e)
			{
				e.printStackTrace();
			}
			break;
		case UPDATE_SPACESTATION_DATA:
			try
			{
				final int var2 = data.readInt();
				NBTTagCompound var3;

				final short var1 = data.readShort();

				if (var1 < 0)
				{
					var3 = null;
				}
				else
				{
					final byte[] var21 = new byte[var1];
					data.readFully(var21);
					var3 = CompressedStreamTools.decompress(var21);
				}

				final GCCoreSpaceStationData var4 = GCCoreSpaceStationData.getMPSpaceStationData(player.worldObj, var2, player);
				var4.readFromNBT(var3);
			}
			catch (final IOException var5)
			{
				var5.printStackTrace();
			}
			break;
		case UPDATE_SPACESTATION_CLIENT_ID:
			ClientProxyCore.clientSpaceStationID = (Integer) packetReadout[0];
			break;
		case UPDATE_PLANETS_LIST:
			if (WorldUtil.registeredPlanets == null)
			{
				WorldUtil.registeredPlanets = new ArrayList<Integer>();
			}

			try
			{
				final int var1 = data.readInt();

				for (int var2 = 0; var2 < var1; ++var2)
				{
					final int var3 = data.readInt();

					if (!WorldUtil.registeredPlanets.contains(Integer.valueOf(var3)))
					{
						WorldUtil.registeredPlanets.add(Integer.valueOf(var3));
						DimensionManager.registerDimension(var3, var3);
					}
				}
			}
			catch (final IOException e)
			{
				e.printStackTrace();
			}
			break;
		case ADD_NEW_SCHEMATIC:
			if (playerBaseClient != null)
			{
				final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID((Integer) packetReadout[0]);

				if (!playerBaseClient.unlockedSchematics.contains(page))
				{
					playerBaseClient.unlockedSchematics.add(page);
				}
			}
			break;
		case UPDATE_SCHEMATIC_LIST:
			if (playerBaseClient != null)
			{
				try
				{
					final int var1 = data.readInt();

					for (int var2 = 0; var2 < var1; ++var2)
					{
						final int var3 = data.readInt();

						if (var3 != -2)
						{
							Collections.sort(playerBaseClient.unlockedSchematics);

							if (!playerBaseClient.unlockedSchematics.contains(SchematicRegistry.getMatchingRecipeForID(Integer.valueOf(var3))))
							{
								playerBaseClient.unlockedSchematics.add(SchematicRegistry.getMatchingRecipeForID(Integer.valueOf(var3)));
							}
						}
					}
				}
				catch (final IOException e)
				{
					e.printStackTrace();
				}
			}
			break;
		case ZOOM_CAMERA:
			GCCoreTickHandlerClient.zoom((Integer) packetReadout[0] == 0 ? 4.0F : 15.0F);
			break;
		case PLAY_SOUND_BOSS_DEATH:
			player.playSound(GalacticraftCore.ASSET_PREFIX + "entity.bossdeath", 10.0F, 0.8F);
			break;
		case PLAY_SOUND_EXPLODE:
			player.playSound("random.explode", 10.0F, 0.7F);
			break;
		case PLAY_SOUND_BOSS_LAUGH:
			player.playSound(GalacticraftCore.ASSET_PREFIX + "entity.bosslaugh", 10.0F, 0.2F);
			break;
		case PLAY_SOUND_BOW:
			player.playSound("random.bow", 10.0F, 0.2F);
			break;
		case UPDATE_OXYGEN_VALIDITY:
			if (playerBaseClient != null)
			{
				playerBaseClient.oxygenSetupValid = (Boolean) packetReadout[0];
			}
			break;
		case OPEN_PARACHEST_GUI:
			switch ((Integer) packetReadout[1])
			{
			case 0:
				if (player.ridingEntity instanceof GCCoreEntityBuggy)
				{
					FMLClientHandler.instance().getClient().displayGuiScreen(new GCCoreGuiBuggy(player.inventory, (GCCoreEntityBuggy) player.ridingEntity, ((GCCoreEntityBuggy) player.ridingEntity).getType()));
					player.openContainer.windowId = (Integer) packetReadout[0];
				}
				break;
			case 1:
				int entityID = (Integer) packetReadout[2];
				Entity entity = player.worldObj.getEntityByID(entityID);

				if (entity != null && entity instanceof IInventorySettable)
				{
					FMLClientHandler.instance().getClient().displayGuiScreen(new GCCoreGuiParachest(player.inventory, (IInventorySettable) entity));
				}

				player.openContainer.windowId = (Integer) packetReadout[0];
				break;
			}
			break;
		case UPDATE_LANDER:
			try
			{
				new GCCorePacketLanderUpdate().handlePacket(data, new Object[] { player }, Side.CLIENT);
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
			break;
		case UPDATE_PARACHEST:
			try
			{
				new GCCorePacketParachestUpdate().handlePacket(data, new Object[] { player }, Side.CLIENT);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			break;
		case UPDATE_WIRE_BOUNDS:
			TileEntity tile = player.worldObj.getBlockTileEntity((Integer) packetReadout[0], (Integer) packetReadout[1], (Integer) packetReadout[2]);

			if (tile instanceof GCCoreTileEntityConductor)
			{
				((GCCoreTileEntityConductor) tile).adjacentConnections = null;
				Block.blocksList[player.worldObj.getBlockId(tile.xCoord, tile.yCoord, tile.zCoord)].setBlockBoundsBasedOnState(player.worldObj, tile.xCoord, tile.yCoord, tile.zCoord);
			}
			break;
		}
	}
}
