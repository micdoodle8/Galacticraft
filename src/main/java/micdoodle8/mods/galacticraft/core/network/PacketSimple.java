package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.fx.EntityFXSparks;
import micdoodle8.mods.galacticraft.core.client.gui.GuiIdsCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiBuggy;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiParaChest;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiChoosePlanet;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiGalaxyMap;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.dimension.SpaceStationWorldData;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderOrbit;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.IBubbleProvider;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityClientPlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP.EnumModelPacket;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSchematic;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.items.ItemParaChute;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerClient;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLockController;
import micdoodle8.mods.galacticraft.core.tile.TileEntityConductor;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FlagData;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketSimple extends Packet implements IPacket
{
	public static enum EnumSimplePacket
	{
		// SERVER
		S_RESPAWN_PLAYER(Side.SERVER, String.class), S_TELEPORT_ENTITY(Side.SERVER, String.class), S_IGNITE_ROCKET(Side.SERVER), S_OPEN_SCHEMATIC_PAGE(Side.SERVER, Integer.class), S_OPEN_FUEL_GUI(Side.SERVER, Integer.class), S_UPDATE_SHIP_YAW(Side.SERVER, Float.class), S_UPDATE_SHIP_PITCH(Side.SERVER, Float.class), S_SET_ENTITY_FIRE(Side.SERVER, Integer.class), S_OPEN_REFINERY_GUI(Side.SERVER, Integer.class, Integer.class, Integer.class), S_BIND_SPACE_STATION_ID(Side.SERVER, Integer.class), S_UNLOCK_NEW_SCHEMATIC(Side.SERVER), S_UPDATE_DISABLEABLE_BUTTON(Side.SERVER, Integer.class, Integer.class, Integer.class, Integer.class), S_ON_FAILED_CHEST_UNLOCK(Side.SERVER, Integer.class), S_RENAME_SPACE_STATION(Side.SERVER, String.class, Integer.class), S_OPEN_EXTENDED_INVENTORY(Side.SERVER), S_ON_ADVANCED_GUI_CLICKED_INT(Side.SERVER, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class), S_ON_ADVANCED_GUI_CLICKED_STRING(Side.SERVER, Integer.class, Integer.class, Integer.class, Integer.class, String.class), S_UPDATE_SHIP_MOTION_Y(Side.SERVER, Integer.class, Boolean.class), S_START_NEW_SPACE_RACE(Side.SERVER, Integer.class, String.class, FlagData.class, Vector3.class, String[].class), S_REQUEST_FLAG_DATA(Side.SERVER, String.class), S_INVITE_RACE_PLAYER(Side.SERVER, String.class, Integer.class), S_REMOVE_RACE_PLAYER(Side.SERVER, String.class, Integer.class), S_ADD_RACE_PLAYER(Side.SERVER, String.class, Integer.class),
		// CLIENT
		C_AIR_REMAINING(Side.CLIENT, Integer.class, Integer.class, String.class), C_UPDATE_DIMENSION_LIST(Side.CLIENT, String.class, String.class), C_SPAWN_SPARK_PARTICLES(Side.CLIENT, Integer.class, Integer.class, Integer.class), C_UPDATE_GEAR_SLOT(Side.CLIENT, String.class, Integer.class, Integer.class), C_CLOSE_GUI(Side.CLIENT), C_RESET_THIRD_PERSON(Side.CLIENT, String.class), C_UPDATE_SPACESTATION_LIST(Side.CLIENT, Integer[].class), C_UPDATE_SPACESTATION_DATA(Side.CLIENT, Integer.class, NBTTagCompound.class), C_UPDATE_SPACESTATION_CLIENT_ID(Side.CLIENT, Integer.class), C_UPDATE_PLANETS_LIST(Side.CLIENT, Integer[].class), C_ADD_NEW_SCHEMATIC(Side.CLIENT, Integer.class), C_UPDATE_SCHEMATIC_LIST(Side.CLIENT, Integer[].class), C_PLAY_SOUND_BOSS_DEATH(Side.CLIENT), C_PLAY_SOUND_EXPLODE(Side.CLIENT), C_PLAY_SOUND_BOSS_LAUGH(Side.CLIENT), C_PLAY_SOUND_BOW(Side.CLIENT), C_UPDATE_OXYGEN_VALIDITY(Side.CLIENT, Boolean.class), C_OPEN_PARACHEST_GUI(Side.CLIENT, Integer.class, Integer.class, Integer.class), C_UPDATE_WIRE_BOUNDS(Side.CLIENT, Integer.class, Integer.class, Integer.class), C_OPEN_SPACE_RACE_GUI(Side.CLIENT), C_UPDATE_SPACE_RACE_DATA(Side.CLIENT, Integer.class, String.class, FlagData.class, Vector3.class, String[].class), C_OPEN_JOIN_RACE_GUI(Side.CLIENT, Integer.class), C_UPDATE_FOOTPRINT_LIST(Side.CLIENT, Footprint[].class), C_UPDATE_STATION_SPIN(Side.CLIENT, Float.class, Boolean.class), C_UPDATE_STATION_DATA(Side.CLIENT, Double.class, Double.class), C_UPDATE_STATION_BOX(Side.CLIENT, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class), C_UPDATE_THERMAL_LEVEL(Side.CLIENT, Integer.class);

		private Side targetSide;
		private Class<?>[] decodeAs;

		private EnumSimplePacket(Side targetSide, Class<?>... decodeAs)
		{
			this.targetSide = targetSide;
			this.decodeAs = decodeAs;
		}

		public Side getTargetSide()
		{
			return this.targetSide;
		}

		public Class<?>[] getDecodeClasses()
		{
			return this.decodeAs;
		}
	}

	private EnumSimplePacket type;
	private List<Object> data;

	public PacketSimple()
	{

	}

	public PacketSimple(EnumSimplePacket packetType, Object[] data)
	{
		this(packetType, Arrays.asList(data));
	}

	public PacketSimple(EnumSimplePacket packetType, List<Object> data)
	{
		if (packetType.getDecodeClasses().length != data.size())
		{
			GCLog.info("Simple Packet Core found data length different than packet type");
			new RuntimeException().printStackTrace();
		}

		this.type = packetType;
		this.data = data;
	}

	@Override
	public void encodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		buffer.writeInt(this.type.ordinal());

		try
		{
			NetworkUtil.encodeData(buffer, this.data);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		this.type = EnumSimplePacket.values()[buffer.readInt()];

		try
		{
			if (this.type.getDecodeClasses().length > 0)
			{
				this.data = NetworkUtil.decodeData(this.type.getDecodeClasses(), buffer);
			}
		}
		catch (Exception e)
		{
			System.err.println("[Galacticraft] Error handling simple packet type: " + this.type.toString() + " " + buffer.toString());
			e.printStackTrace();
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void handleClientSide(EntityPlayer player)
	{
		GCEntityClientPlayerMP playerBaseClient = null;

		if (player instanceof GCEntityClientPlayerMP)
		{
			playerBaseClient = (GCEntityClientPlayerMP) player;
		}
		else
		{
			return;
		}

		switch (this.type)
		{
		case C_AIR_REMAINING:
			if (String.valueOf(this.data.get(2)).equals(String.valueOf(FMLClientHandler.instance().getClient().thePlayer.getGameProfile().getName())))
			{
				TickHandlerClient.airRemaining = (Integer) this.data.get(0);
				TickHandlerClient.airRemaining2 = (Integer) this.data.get(1);
			}
			break;
		case C_UPDATE_DIMENSION_LIST:
			if (String.valueOf(this.data.get(0)).equals(FMLClientHandler.instance().getClient().thePlayer.getGameProfile().getName()))
			{
				final String[] destinations = ((String) this.data.get(1)).split("\\.");

				if (FMLClientHandler.instance().getClient().theWorld != null && !(FMLClientHandler.instance().getClient().currentScreen instanceof GuiChoosePlanet || FMLClientHandler.instance().getClient().currentScreen instanceof GuiGalaxyMap))
				{
					FMLClientHandler.instance().getClient().displayGuiScreen(new GuiCelestialSelection());//new GuiChoosePlanet(FMLClientHandler.instance().getClient().thePlayer, destinations));
				}
//				else if (FMLClientHandler.instance().getClient().currentScreen instanceof GuiCelestialSelection)
//				{
//					((GuiChoosePlanet) FMLClientHandler.instance().getClient().currentScreen).updateDimensionList(destinations);
//				}
			}
			break;
		case C_SPAWN_SPARK_PARTICLES:
			int x,
			y,
			z;
			x = (Integer) this.data.get(0);
			y = (Integer) this.data.get(1);
			z = (Integer) this.data.get(2);
			Minecraft mc = Minecraft.getMinecraft();

			for (int i = 0; i < 4; i++)
			{
				if (mc != null && mc.renderViewEntity != null && mc.effectRenderer != null && mc.theWorld != null)
				{
					final EntityFX fx = new EntityFXSparks(mc.theWorld, x - 0.15 + 0.5, y + 1.2, z + 0.15 + 0.5, mc.theWorld.rand.nextDouble() / 20 - mc.theWorld.rand.nextDouble() / 20, 0.06, mc.theWorld.rand.nextDouble() / 20 - mc.theWorld.rand.nextDouble() / 20, 1.0F);

					if (fx != null)
					{
						mc.effectRenderer.addEffect(fx);
					}
				}
			}
			break;
		case C_UPDATE_GEAR_SLOT:
			int subtype = (Integer) this.data.get(2);
			EntityPlayer gearDataPlayer = null;
			MinecraftServer server = MinecraftServer.getServer();

			if (server != null)
			{
				gearDataPlayer = server.getConfigurationManager().getPlayerForUsername((String) this.data.get(0));
			}
			else
			{
				gearDataPlayer = player.worldObj.getPlayerEntityByName((String) this.data.get(0));
			}

			if (gearDataPlayer != null)
			{
				PlayerGearData gearData = ClientProxyCore.playerItemData.get(gearDataPlayer.getPersistentID());

				if (gearData == null)
				{
					gearData = new PlayerGearData(player);
				}

				EnumModelPacket type = EnumModelPacket.values()[(Integer) this.data.get(1)];

				switch (type)
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
						name = ItemParaChute.names[subtype];
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
				case ADD_THERMAL_HELMET:
					gearData.setThermalPadding(0, 0);
					break;
				case ADD_THERMAL_CHESTPLATE:
					gearData.setThermalPadding(1, 0);
					break;
				case ADD_THERMAL_LEGGINGS:
					gearData.setThermalPadding(2, 0);
					break;
				case ADD_THERMAL_BOOTS:
					gearData.setThermalPadding(3, 0);
					break;
				case REMOVE_THERMAL_HELMET:
					gearData.setThermalPadding(0, -1);
					break;
				case REMOVE_THERMAL_CHESTPLATE:
					gearData.setThermalPadding(1, -1);
					break;
				case REMOVE_THERMAL_LEGGINGS:
					gearData.setThermalPadding(2, -1);
					break;
				case REMOVE_THERMAL_BOOTS:
					gearData.setThermalPadding(3, -1);
					break;
				default:
					break;
				}

				ClientProxyCore.playerItemData.put(playerBaseClient.getPersistentID(), gearData);
			}

			break;
		case C_CLOSE_GUI:
			FMLClientHandler.instance().getClient().displayGuiScreen(null);
			break;
		case C_RESET_THIRD_PERSON:
			FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = playerBaseClient.getThirdPersonView();
			break;
		case C_UPDATE_SPACESTATION_LIST:
			try
			{
				if (WorldUtil.registeredSpaceStations != null)
				{
					for (Integer registeredID : WorldUtil.registeredSpaceStations)
					{
						DimensionManager.unregisterDimension(registeredID);
					}
				}
				WorldUtil.registeredSpaceStations = new ArrayList<Integer>();

				if (this.data.size() > 0)
				{
					if (this.data.get(0) instanceof Integer)
					{
						for (Object o : this.data)
						{
							Integer dimID = (Integer) o;

							if (!WorldUtil.registeredSpaceStations.contains(dimID))
							{
								WorldUtil.registeredSpaceStations.add(dimID);
								if (!DimensionManager.isDimensionRegistered(dimID))
								{
									DimensionManager.registerDimension(dimID, ConfigManagerCore.idDimensionOverworldOrbit);
								}
								else
								{
									GCLog.severe("Dimension already registered on client: unable to register space station dimension " + dimID);
								}
							}
						}
					}
					else if (this.data.get(0) instanceof Integer[])
					{
						for (Object o : (Integer[]) this.data.get(0))
						{
							Integer dimID = (Integer) o;

							if (!WorldUtil.registeredSpaceStations.contains(dimID))
							{
								WorldUtil.registeredSpaceStations.add(dimID);
								if (!DimensionManager.isDimensionRegistered(dimID))
								{
									DimensionManager.registerDimension(dimID, ConfigManagerCore.idDimensionOverworldOrbit);
								}
								else
								{
									GCLog.severe("Dimension already registered on client: unable to register space station dimension " + dimID);
								}
							}
						}
					}
				}
				break;
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		case C_UPDATE_SPACESTATION_DATA:
			SpaceStationWorldData var4 = SpaceStationWorldData.getMPSpaceStationData(player.worldObj, (Integer) this.data.get(0), player);
			var4.readFromNBT((NBTTagCompound) this.data.get(1));
			break;
		case C_UPDATE_SPACESTATION_CLIENT_ID:
			ClientProxyCore.clientSpaceStationID = (Integer) this.data.get(0);
			break;
		case C_UPDATE_PLANETS_LIST:
			try
			{
				if (WorldUtil.registeredPlanets != null)
				{
					for (Integer registeredID : WorldUtil.registeredPlanets)
					{
						DimensionManager.unregisterDimension(registeredID);
					}
				}
				WorldUtil.registeredPlanets = new ArrayList<Integer>();

				if (this.data.size() > 0)
				{
					if (this.data.get(0) instanceof Integer)
					{
						for (Object o : this.data)
						{
							Integer dimID = (Integer) o;

							if (!WorldUtil.registeredPlanets.contains(dimID))
							{
								WorldUtil.registeredPlanets.add(dimID);
								DimensionManager.registerDimension(dimID, dimID);
							}
						}
					}
					else if (this.data.get(0) instanceof Integer[])
					{
						for (Object o : (Integer[]) this.data.get(0))
						{
							Integer dimID = (Integer) o;

							if (!WorldUtil.registeredPlanets.contains(dimID))
							{
								WorldUtil.registeredPlanets.add(dimID);
								DimensionManager.registerDimension(dimID, dimID);
							}
						}
					}
				}
				break;
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		case C_ADD_NEW_SCHEMATIC:
			final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID((Integer) this.data.get(0));
			if (!playerBaseClient.unlockedSchematics.contains(page))
			{
				playerBaseClient.unlockedSchematics.add(page);
			}
			break;
		case C_UPDATE_SCHEMATIC_LIST:
			for (Object o : this.data)
			{
				Integer schematicID = (Integer) o;

				if (schematicID != -2)
				{
					Collections.sort(playerBaseClient.unlockedSchematics);

					if (!playerBaseClient.unlockedSchematics.contains(SchematicRegistry.getMatchingRecipeForID(Integer.valueOf(schematicID))))
					{
						playerBaseClient.unlockedSchematics.add(SchematicRegistry.getMatchingRecipeForID(Integer.valueOf(schematicID)));
					}
				}
			}
			break;
		case C_PLAY_SOUND_BOSS_DEATH:
			player.playSound(GalacticraftCore.ASSET_PREFIX + "entity.bossdeath", 10.0F, 0.8F);
			break;
		case C_PLAY_SOUND_EXPLODE:
			player.playSound("random.explode", 10.0F, 0.7F);
			break;
		case C_PLAY_SOUND_BOSS_LAUGH:
			player.playSound(GalacticraftCore.ASSET_PREFIX + "entity.bosslaugh", 10.0F, 0.2F);
			break;
		case C_PLAY_SOUND_BOW:
			player.playSound("random.bow", 10.0F, 0.2F);
			break;
		case C_UPDATE_OXYGEN_VALIDITY:
			playerBaseClient.oxygenSetupValid = (Boolean) this.data.get(0);
			break;
		case C_OPEN_PARACHEST_GUI:
			switch ((Integer) this.data.get(1))
			{
			case 0:
				if (player.ridingEntity instanceof EntityBuggy)
				{
					FMLClientHandler.instance().getClient().displayGuiScreen(new GuiBuggy(player.inventory, (EntityBuggy) player.ridingEntity, ((EntityBuggy) player.ridingEntity).getType()));
					player.openContainer.windowId = (Integer) this.data.get(0);
				}
				break;
			case 1:
				int entityID = (Integer) this.data.get(2);
				Entity entity = player.worldObj.getEntityByID(entityID);

				if (entity != null && entity instanceof IInventorySettable)
				{
					FMLClientHandler.instance().getClient().displayGuiScreen(new GuiParaChest(player.inventory, (IInventorySettable) entity));
				}

				player.openContainer.windowId = (Integer) this.data.get(0);
				break;
			}
			break;
		case C_UPDATE_WIRE_BOUNDS:
			TileEntity tile = player.worldObj.getTileEntity((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2));

			if (tile instanceof TileEntityConductor)
			{
				((TileEntityConductor) tile).adjacentConnections = null;
				player.worldObj.getBlock(tile.xCoord, tile.yCoord, tile.zCoord).setBlockBoundsBasedOnState(player.worldObj, tile.xCoord, tile.yCoord, tile.zCoord);
			}
			break;
		case C_OPEN_SPACE_RACE_GUI:
			if (Minecraft.getMinecraft().currentScreen == null)
			{
				TickHandlerClient.spaceRaceGuiScheduled = false;
				player.openGui(GalacticraftCore.instance, GuiIdsCore.SPACE_RACE_START, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
			}
			else
			{
				TickHandlerClient.spaceRaceGuiScheduled = true;
			}
			break;
		case C_UPDATE_SPACE_RACE_DATA:
			Integer teamID = (Integer) this.data.get(0);
			String teamName = (String) this.data.get(1);
			FlagData flagData = (FlagData) this.data.get(2);
			Vector3 teamColor = (Vector3) this.data.get(3);
			List<String> playerList = new ArrayList<String>();

			for (int i = 4; i < this.data.size(); i++)
			{
				String playerName = (String) this.data.get(i);
				ClientProxyCore.flagRequestsSent.remove(playerName);
				playerList.add(playerName);
			}

			SpaceRace race = new SpaceRace(playerList, teamName, flagData, teamColor);
			race.setSpaceRaceID(teamID);
			SpaceRaceManager.addSpaceRace(race);
			break;
		case C_OPEN_JOIN_RACE_GUI:
			playerBaseClient.spaceRaceInviteTeamID = (Integer) this.data.get(0);
			player.openGui(GalacticraftCore.instance, GuiIdsCore.SPACE_RACE_JOIN, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
			break;
		case C_UPDATE_FOOTPRINT_LIST:
			ClientProxyCore.footprintRenderer.footprints.clear();
			for (int i = 0; i < this.data.size(); i++)
			{
				Footprint print = (Footprint) this.data.get(i);
				ClientProxyCore.footprintRenderer.addFootprint(print);
			}
			break;
		case C_UPDATE_STATION_SPIN:
			if (playerBaseClient.worldObj.provider instanceof WorldProviderOrbit)
			{
				((WorldProviderOrbit) playerBaseClient.worldObj.provider).setSpinRate((Float) this.data.get(0), (Boolean) this.data.get(1));
			}
			break;
		case C_UPDATE_STATION_DATA:
			if (playerBaseClient.worldObj.provider instanceof WorldProviderOrbit)
			{
				((WorldProviderOrbit) playerBaseClient.worldObj.provider).setSpinCentre((Double) this.data.get(0), (Double) this.data.get(1));
			}
			break;
		case C_UPDATE_STATION_BOX:
			if (playerBaseClient.worldObj.provider instanceof WorldProviderOrbit)
			{
				((WorldProviderOrbit) playerBaseClient.worldObj.provider).setSpinBox((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2), (Integer) this.data.get(3), (Integer) this.data.get(4), (Integer) this.data.get(5));
			}
			break;
		case C_UPDATE_THERMAL_LEVEL:
			playerBaseClient.thermalLevel = (Integer) this.data.get(0);
			break;
		default:
			break;
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		GCEntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);
		if (playerBase == null)
		{
			return;
		}

		switch (this.type)
		{
		case S_RESPAWN_PLAYER:
			playerBase.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), playerBase.theItemInWorldManager.getGameType()));
			break;
		case S_TELEPORT_ENTITY:
			try
			{
				final WorldProvider provider = WorldUtil.getProviderForName((String) this.data.get(0));
				final Integer dim = provider.dimensionId;
				GCLog.info("Found matching world (" + dim.toString() + ") for name: " + (String) this.data.get(0));

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

				playerBase.getPlayerStats().teleportCooldown = 300;
				GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_CLOSE_GUI, new Object[] {}), playerBase);
			}
			catch (final Exception e)
			{
				GCLog.severe("Error occurred when attempting to transfer entity to dimension: " + (String) this.data.get(0));
				e.printStackTrace();
			}
			break;
		case S_IGNITE_ROCKET:
			if (!player.worldObj.isRemote && !player.isDead && player.ridingEntity != null && !player.ridingEntity.isDead && player.ridingEntity instanceof EntityTieredRocket)
			{
				final EntityTieredRocket ship = (EntityTieredRocket) player.ridingEntity;

				if (!ship.landing)
				{
					if (ship.hasValidFuel())
					{
						ItemStack stack2 = playerBase.getPlayerStats().extendedInventory.getStackInSlot(4);

						if (stack2 != null && stack2.getItem() instanceof ItemParaChute || playerBase.getPlayerStats().launchAttempts > 0)
						{
							ship.igniteCheckingCooldown();
							playerBase.getPlayerStats().launchAttempts = 0;
						}
						else if (playerBase.getPlayerStats().chatCooldown == 0 && playerBase.getPlayerStats().launchAttempts == 0)
						{
							player.addChatMessage(new ChatComponentText(GCCoreUtil.translate("gui.rocket.warning.noparachute")));
							playerBase.getPlayerStats().chatCooldown = 250;
							playerBase.getPlayerStats().launchAttempts = 1;
						}
					}
					else if (playerBase.getPlayerStats().chatCooldown == 0)
					{
						player.addChatMessage(new ChatComponentText(GCCoreUtil.translate("gui.rocket.warning.nofuel")));
						playerBase.getPlayerStats().chatCooldown = 250;
					}
				}
			}
			break;
		case S_OPEN_SCHEMATIC_PAGE:
			if (player != null)
			{
				final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID((Integer) this.data.get(0));

				player.openGui(GalacticraftCore.instance, page.getGuiID(), player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
			}
			break;
		case S_OPEN_FUEL_GUI:
			if (player.ridingEntity instanceof EntityBuggy)
			{
				GCCoreUtil.openBuggyInv(playerBase, (EntityBuggy) player.ridingEntity, ((EntityBuggy) player.ridingEntity).getType());
			}
			else if (player.ridingEntity instanceof EntitySpaceshipBase)
			{
				player.openGui(GalacticraftCore.instance, GuiIdsCore.ROCKET_INVENTORY, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
			}
			break;
		case S_UPDATE_SHIP_YAW:
			if (player.ridingEntity instanceof EntitySpaceshipBase)
			{
				final EntitySpaceshipBase ship = (EntitySpaceshipBase) player.ridingEntity;

				if (ship != null)
				{
					ship.rotationYaw = (Float) this.data.get(0);
				}
			}
			break;
		case S_UPDATE_SHIP_PITCH:
			if (player.ridingEntity instanceof EntitySpaceshipBase)
			{
				final EntitySpaceshipBase ship = (EntitySpaceshipBase) player.ridingEntity;

				if (ship != null)
				{
					ship.rotationPitch = (Float) this.data.get(0);
				}
			}
			break;
		case S_SET_ENTITY_FIRE:
			Entity entity = player.worldObj.getEntityByID((Integer) this.data.get(0));

			if (entity instanceof EntityLiving)
			{
				((EntityLiving) entity).setFire(3);
			}
			break;
		case S_OPEN_REFINERY_GUI:
			player.openGui(GalacticraftCore.instance, -1, player.worldObj, (Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2));
			break;
		case S_BIND_SPACE_STATION_ID:
			if ((playerBase.getPlayerStats().spaceStationDimensionID == -1 || playerBase.getPlayerStats().spaceStationDimensionID == 0) && !ConfigManagerCore.disableSpaceStationCreation)
			{
				WorldUtil.bindSpaceStationToNewDimension(playerBase.worldObj, playerBase);

				WorldUtil.getSpaceStationRecipe((Integer) this.data.get(0)).matches(playerBase, true);
			}
			break;
		case S_UNLOCK_NEW_SCHEMATIC:
			final Container container = player.openContainer;

			if (container instanceof ContainerSchematic)
			{
				final ContainerSchematic schematicContainer = (ContainerSchematic) container;

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
						schematicContainer.craftMatrix.markDirty();

						GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_ADD_NEW_SCHEMATIC, new Object[] { page.getPageID() }), playerBase);
					}
				}
			}
			break;
		case S_UPDATE_DISABLEABLE_BUTTON:
			final TileEntity tileAt = player.worldObj.getTileEntity((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2));

			if (tileAt instanceof IDisableableMachine)
			{
				final IDisableableMachine machine = (IDisableableMachine) tileAt;

				machine.setDisabled((Integer) this.data.get(3), !machine.getDisabled((Integer) this.data.get(3)));
			}
			break;
		case S_ON_FAILED_CHEST_UNLOCK:
			if (playerBase.getPlayerStats().chatCooldown == 0)
			{
				player.addChatMessage(new ChatComponentText(GCCoreUtil.translateWithFormat("gui.chest.warning.wrongkey", this.data.get(0))));
				playerBase.getPlayerStats().chatCooldown = 100;
			}
			break;
		case S_RENAME_SPACE_STATION:
			final SpaceStationWorldData ssdata = SpaceStationWorldData.getStationData(playerBase.worldObj, (Integer) this.data.get(1), playerBase);

			if (ssdata != null && ssdata.getOwner().equalsIgnoreCase(player.getGameProfile().getName()))
			{
				ssdata.setSpaceStationName((String) this.data.get(0));
				ssdata.setDirty(true);
			}
			break;
		case S_OPEN_EXTENDED_INVENTORY:
			player.openGui(GalacticraftCore.instance, GuiIdsCore.EXTENDED_INVENTORY, player.worldObj, 0, 0, 0);
			break;
		case S_ON_ADVANCED_GUI_CLICKED_INT:
			TileEntity tile1 = player.worldObj.getTileEntity((Integer) this.data.get(1), (Integer) this.data.get(2), (Integer) this.data.get(3));

			switch ((Integer) this.data.get(0))
			{
			case 0:
				if (tile1 instanceof TileEntityAirLockController)
				{
					TileEntityAirLockController launchController = (TileEntityAirLockController) tile1;
					launchController.redstoneActivation = (Integer) this.data.get(4) == 1;
				}
				break;
			case 1:
				if (tile1 instanceof TileEntityAirLockController)
				{
					TileEntityAirLockController launchController = (TileEntityAirLockController) tile1;
					launchController.playerDistanceActivation = (Integer) this.data.get(4) == 1;
				}
				break;
			case 2:
				if (tile1 instanceof TileEntityAirLockController)
				{
					TileEntityAirLockController launchController = (TileEntityAirLockController) tile1;
					launchController.playerDistanceSelection = (Integer) this.data.get(4);
				}
				break;
			case 3:
				if (tile1 instanceof TileEntityAirLockController)
				{
					TileEntityAirLockController launchController = (TileEntityAirLockController) tile1;
					launchController.playerNameMatches = (Integer) this.data.get(4) == 1;
				}
				break;
			case 4:
				if (tile1 instanceof TileEntityAirLockController)
				{
					TileEntityAirLockController launchController = (TileEntityAirLockController) tile1;
					launchController.invertSelection = (Integer) this.data.get(4) == 1;
				}
				break;
			case 5:
				if (tile1 instanceof TileEntityAirLockController)
				{
					TileEntityAirLockController launchController = (TileEntityAirLockController) tile1;
					launchController.lastHorizontalModeEnabled = launchController.horizontalModeEnabled;
					launchController.horizontalModeEnabled = (Integer) this.data.get(4) == 1;
				}
				break;
			case 6:
				if (tile1 instanceof IBubbleProvider)
				{
					IBubbleProvider distributor = (IBubbleProvider) tile1;
					distributor.setBubbleVisible((Integer) this.data.get(4) == 1);
				}
				break;
			default:
				break;
			}
			break;
		case S_ON_ADVANCED_GUI_CLICKED_STRING:
			TileEntity tile2 = player.worldObj.getTileEntity((Integer) this.data.get(1), (Integer) this.data.get(2), (Integer) this.data.get(3));

			switch ((Integer) this.data.get(0))
			{
			case 0:
				if (tile2 instanceof TileEntityAirLockController)
				{
					TileEntityAirLockController launchController = (TileEntityAirLockController) tile2;
					launchController.playerToOpenFor = (String) this.data.get(4);
				}
				break;
			default:
				break;
			}
			break;
		case S_UPDATE_SHIP_MOTION_Y:
			int entityID = (Integer) this.data.get(0);
			boolean up = (Boolean) this.data.get(1);

			Entity entity2 = player.worldObj.getEntityByID(entityID);

			if (entity2 instanceof EntityAutoRocket)
			{
				EntityAutoRocket autoRocket = (EntityAutoRocket) entity2;
				autoRocket.motionY += up ? 0.02F : -0.02F;
			}

			break;
		case S_START_NEW_SPACE_RACE:
			Integer teamID = (Integer) this.data.get(0);
			String teamName = (String) this.data.get(1);
			FlagData flagData = (FlagData) this.data.get(2);
			Vector3 teamColor = (Vector3) this.data.get(3);
			List<String> playerList = new ArrayList<String>();

			for (int i = 4; i < this.data.size(); i++)
			{
				playerList.add((String) this.data.get(i));
			}

			boolean previousData = SpaceRaceManager.getSpaceRaceFromID(teamID) != null;

			SpaceRace newRace = new SpaceRace(playerList, teamName, flagData, teamColor);

			if (teamID > 0)
			{
				newRace.setSpaceRaceID(teamID);
			}

			SpaceRaceManager.addSpaceRace(newRace);

			if (previousData)
			{
				SpaceRaceManager.sendSpaceRaceData(null, SpaceRaceManager.getSpaceRaceFromPlayer(playerBase.getGameProfile().getName()));
			}
			break;
		case S_REQUEST_FLAG_DATA:
			SpaceRaceManager.sendSpaceRaceData(playerBase, SpaceRaceManager.getSpaceRaceFromPlayer((String) this.data.get(0)));
			break;
		case S_INVITE_RACE_PLAYER:
			GCEntityPlayerMP playerInvited = PlayerUtil.getPlayerBaseServerFromPlayerUsername((String) this.data.get(0), true);
			if (playerInvited != null)
			{
				Integer teamInvitedTo = (Integer) this.data.get(1);
				SpaceRace race = SpaceRaceManager.getSpaceRaceFromID(teamInvitedTo);

				if (race != null)
				{
					playerInvited.getPlayerStats().spaceRaceInviteTeamID = teamInvitedTo;
					String dA = EnumColor.DARK_AQUA.code;
					String bG = EnumColor.BRIGHT_GREEN.code;
					String dB = EnumColor.PURPLE.code;
					String teamNameTotal = "";
					String[] teamNameSplit = race.getTeamName().split(" ");
					for (String teamNamePart : teamNameSplit)
					{
						teamNameTotal = teamNameTotal.concat(dB + teamNamePart + " ");
					}
					playerInvited.addChatMessage(new ChatComponentText("Received Space Race Team Invite from " + bG + player.getGameProfile().getName() + dA + ". To join " + teamNameTotal + dA + "use command " + EnumColor.AQUA + "/joinrace").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA)));
				}
			}
			break;
		case S_REMOVE_RACE_PLAYER:
			Integer teamInvitedTo = (Integer) this.data.get(1);
			SpaceRace race = SpaceRaceManager.getSpaceRaceFromID(teamInvitedTo);

			if (race != null)
			{
				String playerToRemove = (String) this.data.get(0);

				if (!race.getPlayerNames().remove(playerToRemove))
				{
					player.addChatMessage(new ChatComponentText("Could not find player with name: " + playerToRemove));
				}
				else
				{
					SpaceRaceManager.onPlayerRemoval(playerToRemove, race);
				}
			}
			break;
		case S_ADD_RACE_PLAYER:
			Integer teamToAddPlayer = (Integer) this.data.get(1);
			SpaceRace spaceRaceToAddPlayer = SpaceRaceManager.getSpaceRaceFromID(teamToAddPlayer);

			if (spaceRaceToAddPlayer != null)
			{
				String playerToAdd = (String) this.data.get(0);

				if (!spaceRaceToAddPlayer.getPlayerNames().contains(playerToAdd))
				{
					SpaceRace oldRace = null;
					while ((oldRace = SpaceRaceManager.getSpaceRaceFromPlayer(playerToAdd)) != null)
					{
						SpaceRaceManager.removeSpaceRace(oldRace);
					}

					spaceRaceToAddPlayer.getPlayerNames().add(playerToAdd);
					SpaceRaceManager.sendSpaceRaceData(null, spaceRaceToAddPlayer);

					for (String member : spaceRaceToAddPlayer.getPlayerNames())
					{
						EntityPlayerMP memberObj = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(member);

						if (memberObj != null)
						{
							memberObj.addChatMessage(new ChatComponentText(EnumColor.BRIGHT_GREEN + playerToAdd + EnumColor.DARK_AQUA + " has joined the Space Race!").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA)));
						}
					}
				}
				else
				{
					player.addChatMessage(new ChatComponentText("You are already a part of this space race!").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED)));
				}
			}
			break;
		default:
			break;
		}
	}

	/*
	 * 
	 * BEGIN "net.minecraft.network.Packet" IMPLEMENTATION
	 * 
	 * This is for handling server->client packets before the player has joined the world
	 * 
	 */

	@Override
	public void readPacketData(PacketBuffer var1)
	{
		this.decodeInto(null, var1);
	}

	@Override
	public void writePacketData(PacketBuffer var1)
	{
		this.encodeInto(null, var1);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void processPacket(INetHandler var1)
	{
		if (this.type != EnumSimplePacket.C_UPDATE_SPACESTATION_LIST && this.type != EnumSimplePacket.C_UPDATE_PLANETS_LIST)
		{
			return;
		}

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			this.handleClientSide(FMLClientHandler.instance().getClientPlayerEntity());
		}
	}

	/*
	 * 
	 * END "net.minecraft.network.Packet" IMPLEMENTATION
	 * 
	 * This is for handling server->client packets before the player has joined the world
	 * 
	 */
}
