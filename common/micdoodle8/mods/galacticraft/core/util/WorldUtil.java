package micdoodle8.mods.galacticraft.core.util;

import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergyTile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import mekanism.api.gas.IGasTransmitter;
import mekanism.api.gas.ITubeConnection;
import mekanism.api.transmitters.TransmissionType;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IWorldTransferCallback;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.compatibility.NetworkConfigHandler;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ICelestialBody;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.IMapObject;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.api.world.SpaceStationType;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GCLog;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreSpaceStationData;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreWorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketDimensionListPlanets;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketDimensionListSpaceStations;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumPacketClient;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketSpaceStationData;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet43Experience;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.power.IPowerReceptor;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * WorldUtil.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class WorldUtil
{
	public static Collection<Integer> registeredSpaceStations;
	public static Collection<Integer> registeredPlanets;
	public static Collection<String> registeredPlanetNames;

	public static double getGravityForEntity(EntityLivingBase eLiving)
	{
		if (eLiving.worldObj.provider instanceof IGalacticraftWorldProvider)
		{
			final IGalacticraftWorldProvider customProvider = (IGalacticraftWorldProvider) eLiving.worldObj.provider;

			if (eLiving instanceof EntityPlayer)
			{
				if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && eLiving instanceof GCCorePlayerSP)
				{
					return ((GCCorePlayerSP) eLiving).touchedGround ? 0.08D - customProvider.getGravity() : 0.08D;
				}
				else if (eLiving instanceof GCCorePlayerMP)
				{
					return ((GCCorePlayerMP) eLiving).isTouchedGround() ? 0.08D - customProvider.getGravity() : 0.08D;
				}
				else
				{
					return 0.08D;
				}
			}
			else
			{
				return 0.08D - customProvider.getGravity();
			}
		}
		else
		{
			return 0.08D;
		}
	}

	public static double getItemGravity(EntityItem e)
	{
		if (e.worldObj.provider instanceof IGalacticraftWorldProvider)
		{
			final IGalacticraftWorldProvider customProvider = (IGalacticraftWorldProvider) e.worldObj.provider;
			return 0.03999999910593033D - (customProvider instanceof IOrbitDimension ? 0.05999999910593033D : customProvider.getGravity()) / 1.75D;
		}
		else
		{
			return 0.03999999910593033D;
		}
	}

	public static double getItemGravity2(EntityItem e)
	{
		if (e.worldObj.provider instanceof IGalacticraftWorldProvider)
		{
			return 1.0D;
		}
		else
		{
			return 0.9800000190734863D;
		}
	}

	public static Vector3 getWorldColor(World world)
	{
		if (world.provider instanceof GCMoonWorldProvider)
		{
			float f1 = world.getCelestialAngle(1);
			float f2 = 1.0F - (MathHelper.cos(f1 * (float) Math.PI * 2.0F) * 2.0F + 0.25F);

			if (f2 < 0.0F)
			{
				f2 = 0.0F;
			}

			if (f2 > 1.0F)
			{
				f2 = 1.0F;
			}

			double d = 1.0 - f2 * f2 * 0.7;
			return new Vector3(d, d, d);
		}

		return new Vector3(1, 1, 1);
	}

	public static float getColorRed(World world)
	{
		return (float) WorldUtil.getWorldColor(world).x;
	}

	public static float getColorGreen(World world)
	{
		return (float) WorldUtil.getWorldColor(world).y;
	}

	public static float getColorBlue(World world)
	{
		return (float) WorldUtil.getWorldColor(world).z;
	}

	public static Vec3 getFogColorHook(World world)
	{
		if (world.provider instanceof WorldProviderSurface && FMLClientHandler.instance().getClient().thePlayer.posY >= 200)
		{
			float var20 = (float) (FMLClientHandler.instance().getClient().thePlayer.posY - 200.0F) / 1000.0F;
			final float var21 = Math.max(1.0F - var20 * 4.0F, 0.0F);

			Vec3 vec = world.getFogColor(1.0F);

			return Vec3.createVectorHelper(vec.xCoord * var21, vec.yCoord * var21, vec.zCoord * var21);
		}

		return world.getFogColor(1.0F);
	}

	public static Vec3 getSkyColorHook(World world)
	{
		if (world.provider instanceof WorldProviderSurface && FMLClientHandler.instance().getClient().thePlayer.posY >= 200)
		{
			float var20 = (float) (FMLClientHandler.instance().getClient().thePlayer.posY - 200.0F) / 1000.0F;
			final float var21 = Math.max(1.0F - var20 * 2.0F, 0.0F);

			Vec3 vec = world.getSkyColor(FMLClientHandler.instance().getClient().renderViewEntity, 1.0F);

			return Vec3.createVectorHelper(vec.xCoord * var21, vec.yCoord * var21, vec.zCoord * var21);
		}

		return world.getSkyColor(FMLClientHandler.instance().getClient().renderViewEntity, 1.0F);
	}

	public static WorldProvider getProviderForName(String par1String)
	{
		final Integer[] var1 = WorldUtil.getArrayOfPossibleDimensions();

		for (final Integer element : var1)
		{
			if (WorldProvider.getProviderForDimension(element) != null && WorldProvider.getProviderForDimension(element).getDimensionName() != null)
			{
				if (par1String.contains("$"))
				{
					final String[] twoDimensions = par1String.split("\\$");

					if (WorldProvider.getProviderForDimension(element).getDimensionName().equals(twoDimensions[0]))
					{
						return WorldProvider.getProviderForDimension(element);
					}
				}
				else if (WorldProvider.getProviderForDimension(element).getDimensionName().equals(par1String))
				{
					return WorldProvider.getProviderForDimension(element);
				}
			}
		}

		return null;
	}

	public static List<Integer> getPossibleDimensionsForSpaceshipTier(int tier)
	{
		List<Integer> temp = new ArrayList<Integer>();

		temp.add(0);

		for (Integer element : WorldUtil.registeredPlanets)
		{
			WorldProvider provider = WorldProvider.getProviderForDimension(element);

			if (provider != null)
			{
				if (provider instanceof IGalacticraftWorldProvider)
				{
					if (((IGalacticraftWorldProvider) provider).canSpaceshipTierPass(tier))
					{
						temp.add(element);
					}
				}
				else
				{
					temp.add(element);
				}
			}
		}

		for (Integer element : WorldUtil.registeredSpaceStations)
		{
			WorldProvider provider = WorldProvider.getProviderForDimension(element);

			if (provider != null)
			{
				if (provider instanceof IGalacticraftWorldProvider)
				{
					if (((IGalacticraftWorldProvider) provider).canSpaceshipTierPass(tier))
					{
						temp.add(element);
					}
				}
				else
				{
					temp.add(element);
				}
			}
		}

		return temp;
	}

	public static HashMap<String, Integer> getArrayOfPossibleDimensions(List<Integer> ids, GCCorePlayerMP playerBase)
	{
		final HashMap<String, Integer> map = new HashMap<String, Integer>();

		for (Integer id : ids)
		{
			if (WorldProvider.getProviderForDimension(id) != null)
			{
				if (WorldProvider.getProviderForDimension(id) instanceof IGalacticraftWorldProvider && !(WorldProvider.getProviderForDimension(id) instanceof IOrbitDimension) || WorldProvider.getProviderForDimension(id).dimensionId == 0)
				{
					map.put(WorldProvider.getProviderForDimension(id).getDimensionName(), WorldProvider.getProviderForDimension(id).dimensionId);
				}
				else if (playerBase != null && WorldProvider.getProviderForDimension(id) instanceof IOrbitDimension)
				{
					final GCCoreSpaceStationData data = GCCoreSpaceStationData.getStationData(playerBase.worldObj, id, playerBase);

					if (!GCCoreConfigManager.spaceStationsRequirePermission || data.getAllowedPlayers().contains(playerBase.username.toLowerCase()) || data.getAllowedPlayers().contains(playerBase.username))
					{
						map.put(WorldProvider.getProviderForDimension(id).getDimensionName() + "$" + data.getOwner() + "$" + data.getSpaceStationName(), WorldProvider.getProviderForDimension(id).dimensionId);
					}
				}
			}
		}

		for (int j = 0; j < GalacticraftRegistry.getCelestialBodies().size(); j++)
		{
			ICelestialBody object = GalacticraftRegistry.getCelestialBodies().get(j);

			if (!object.isReachable() && object.addToList())
			{
				map.put(object.getName() + "*", 0);
			}
		}

		return map;
	}

	public static List<String> getPlayersOnPlanet(IMapObject planet)
	{
		final List<String> list = new ArrayList<String>();

		for (final WorldServer world : DimensionManager.getWorlds())
		{
			if (world != null && world.provider instanceof IGalacticraftWorldProvider)
			{
				if (planet.getSlotRenderer().getPlanetName().toLowerCase().equals(world.provider.getDimensionName().toLowerCase()))
				{
					for (int j = 0; j < world.getLoadedEntityList().size(); j++)
					{
						if (world.getLoadedEntityList().get(j) != null && world.getLoadedEntityList().get(j) instanceof EntityPlayer)
						{
							list.add(((EntityPlayer) world.getLoadedEntityList().get(j)).username);
						}
					}
				}
			}
		}

		return list;
	}

	private static List<Integer> getExistingSpaceStationList(File var0)
	{
		final ArrayList<Integer> var1 = new ArrayList<Integer>();
		final File[] var2 = var0.listFiles();
		final int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4)
		{
			final File var5 = var2[var4];

			if (var5.getName().contains("spacestation_"))
			{
				String var6 = var5.getName();
				var6 = var6.substring(13, var6.length() - 4);
				var1.add(Integer.valueOf(Integer.parseInt(var6)));
			}
		}

		return var1;
	}

	public static void unregisterSpaceStations()
	{
		if (WorldUtil.registeredSpaceStations != null)
		{
			for (Integer registeredID : WorldUtil.registeredSpaceStations)
			{
				DimensionManager.unregisterDimension(registeredID);
			}

			WorldUtil.registeredSpaceStations = null;
		}
	}

	public static void registerSpaceStations(File spaceStationList)
	{
		WorldUtil.registeredSpaceStations = WorldUtil.getExistingSpaceStationList(spaceStationList);

		for (Integer registeredID : WorldUtil.registeredSpaceStations)
		{
			int id = Arrays.binarySearch(GCCoreConfigManager.staticLoadDimensions, registeredID.intValue());

			if (id >= 0)
			{
				DimensionManager.registerDimension(registeredID.intValue(), GCCoreConfigManager.idDimensionOverworldOrbitStatic);
				FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(registeredID.intValue());
			}
			else
			{
				DimensionManager.registerDimension(registeredID.intValue(), GCCoreConfigManager.idDimensionOverworldOrbit);
			}
		}
	}

	/**
	 * Call this on FMLServerStartingEvent to add a hotloadable planet ID
	 */
	public static void registerPlanet(int planetID, boolean isStatic)
	{
		if (WorldUtil.registeredPlanets == null)
		{
			WorldUtil.registeredPlanets = new ArrayList<Integer>();
		}

		WorldUtil.registeredPlanets.add(planetID);

		if (isStatic)
		{
			DimensionManager.registerDimension(planetID, planetID);
			GCLog.info("Registered Dimension: " + planetID);
		}
	}

	public static void unregisterPlanets()
	{
		if (WorldUtil.registeredPlanets != null)
		{
			final Iterator<Integer> var0 = WorldUtil.registeredPlanets.iterator();

			while (var0.hasNext())
			{
				final Integer var1 = var0.next();
				DimensionManager.unregisterDimension(var1.intValue());
				GCLog.info("Unregistered Dimension: " + var1.intValue());
			}

			WorldUtil.registeredPlanets = null;
		}
	}

	/**
	 * This doesn't check if player is using the correct rocket, this is just a
	 * total list of all space dimensions.
	 */
	public static Integer[] getArrayOfPossibleDimensions()
	{
		final ArrayList<Integer> temp = new ArrayList<Integer>();

		temp.add(0);

		for (final Integer i : WorldUtil.registeredPlanets)
		{
			temp.add(i);
		}

		for (final Integer i : WorldUtil.registeredSpaceStations)
		{
			temp.add(i);
		}

		final Integer[] finalArray = new Integer[temp.size()];

		int count = 0;

		for (final Integer integ : temp)
		{
			finalArray[count++] = integ;
		}

		return finalArray;
	}

	public static GCCoreSpaceStationData bindSpaceStationToNewDimension(World world, GCCorePlayerMP player)
	{
		int newID = DimensionManager.getNextFreeDimId();
		GCCoreSpaceStationData data = WorldUtil.createSpaceStation(world, newID, player);
		player.setSpaceStationDimensionID(newID);
		player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.UPDATE_SPACESTATION_CLIENT_ID, new Object[] { newID }));
		return data;
	}

	public static GCCoreSpaceStationData createSpaceStation(World world, int dimID, GCCorePlayerMP player)
	{
		WorldUtil.registeredSpaceStations.add(dimID);
		int id = Arrays.binarySearch(GCCoreConfigManager.staticLoadDimensions, dimID);

		if (id >= 0)
		{
			DimensionManager.registerDimension(dimID, GCCoreConfigManager.idDimensionOverworldOrbitStatic);
		}
		else
		{
			DimensionManager.registerDimension(dimID, GCCoreConfigManager.idDimensionOverworldOrbit);
		}

		final MinecraftServer var2 = FMLCommonHandler.instance().getMinecraftServerInstance();

		if (var2 != null)
		{
			final ArrayList<Integer> var1 = new ArrayList<Integer>();
			var1.add(dimID);
			var2.getConfigurationManager().sendPacketToAllPlayers(GCCorePacketDimensionListSpaceStations.buildDimensionListPacket(var1));
		}

		final GCCoreSpaceStationData var3 = GCCoreSpaceStationData.getStationData(world, dimID, player);
		return var3;
	}

	public static Entity transferEntityToDimension(Entity entity, int dimensionID, WorldServer world)
	{
		return WorldUtil.transferEntityToDimension(entity, dimensionID, world, true, null);
	}

	public static Entity transferEntityToDimension(Entity entity, int dimensionID, WorldServer world, boolean transferInv, EntityAutoRocket ridingRocket)
	{
		if (!world.isRemote)
		{
			final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

			if (server != null)
			{
				final ArrayList<Integer> array = new ArrayList<Integer>();

				for (int i : WorldUtil.registeredPlanets)
				{
					array.add(i);
				}

				server.getConfigurationManager().sendPacketToAllPlayers(GCCorePacketDimensionListPlanets.buildDimensionListPacket(array));
			}

			MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();

			if (mcServer != null)
			{
				final WorldServer var6 = mcServer.worldServerForDimension(dimensionID);

				if (var6 == null)
				{
					System.err.println("Cannot Transfer Entity to Dimension: Could not get World for Dimension " + dimensionID);
				}

				final ITeleportType type = GalacticraftRegistry.getTeleportTypeForDimension(var6.provider.getClass());

				if (type != null)
				{
					return WorldUtil.teleportEntity(var6, entity, dimensionID, type, transferInv, ridingRocket);
				}
			}
		}

		return null;
	}

	private static Entity teleportEntity(World worldNew, Entity entity, int dimID, ITeleportType type, boolean transferInv, EntityAutoRocket ridingRocket)
	{
		if (entity.ridingEntity != null && entity.ridingEntity instanceof EntitySpaceshipBase)
		{
			entity.mountEntity(entity.ridingEntity);
		}

		boolean dimChange = entity.worldObj != worldNew;
		entity.worldObj.updateEntityWithOptionalForce(entity, false);
		GCCorePlayerMP player = null;

		if (entity instanceof GCCorePlayerMP)
		{
			player = (GCCorePlayerMP) entity;
			player.closeScreen();

			if (dimChange)
			{
				player.dimension = dimID;
				player.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(player.dimension, (byte) player.worldObj.difficultySetting, worldNew.getWorldInfo().getTerrainType(), worldNew.getHeight(), player.theItemInWorldManager.getGameType()));

				if (worldNew.provider instanceof GCCoreWorldProviderSpaceStation && WorldUtil.registeredSpaceStations.contains(player))
				{
					player.playerNetServerHandler.sendPacketToPlayer(GCCorePacketSpaceStationData.buildSpaceStationDataPacket(worldNew, worldNew.provider.dimensionId, player));
				}

				((WorldServer) entity.worldObj).getPlayerManager().removePlayer(player);
			}

			player.setNotUsingPlanetGui();
		}

		if (dimChange)
		{
			if (ridingRocket == null)
			{
				WorldUtil.removeEntityFromWorld(entity.worldObj, entity, true);
			}
			else
			{
				WorldUtil.removeEntityFromWorld(entity.worldObj, entity, true);
			}
		}

		if (dimChange)
		{
			if (entity instanceof EntityPlayerMP)
			{
				player = (GCCorePlayerMP) entity;
				entity.setLocationAndAngles(type.getPlayerSpawnLocation((WorldServer) entity.worldObj, player).x, type.getPlayerSpawnLocation((WorldServer) entity.worldObj, player).y, type.getPlayerSpawnLocation((WorldServer) entity.worldObj, player).z, entity.rotationYaw, entity.rotationPitch);
				micdoodle8.mods.galacticraft.api.vector.Vector3 spawnPos = type.getPlayerSpawnLocation((WorldServer) entity.worldObj, (EntityPlayerMP) entity);
				ChunkCoordIntPair pair = worldNew.getChunkFromChunkCoords(spawnPos.intX(), spawnPos.intZ()).getChunkCoordIntPair();
				((WorldServer) worldNew).theChunkProviderServer.loadChunk(pair.chunkXPos, pair.chunkZPos);

				entity.setPosition(spawnPos.x, spawnPos.y, spawnPos.z);
			}
		}

		if (ridingRocket != null)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			ridingRocket.isDead = false;
			ridingRocket.riddenByEntity = null;
			ridingRocket.writeToNBTOptional(nbt);

			((WorldServer) ridingRocket.worldObj).getEntityTracker().removeEntityFromAllTrackingPlayers(ridingRocket);
			ridingRocket.worldObj.loadedEntityList.remove(ridingRocket);
			ridingRocket.worldObj.onEntityRemoved(ridingRocket);

			ridingRocket = (EntityAutoRocket) EntityList.createEntityFromNBT(nbt, worldNew);

			if (ridingRocket != null)
			{
				ridingRocket.setWaitForPlayer(true);

				if (ridingRocket instanceof IWorldTransferCallback)
				{
					((IWorldTransferCallback) ridingRocket).onWorldTransferred(worldNew);
				}
			}
		}

		if (dimChange)
		{
			if (!(entity instanceof EntityPlayer))
			{
				NBTTagCompound nbt = new NBTTagCompound();
				entity.isDead = false;
				entity.writeToNBTOptional(nbt);
				entity.isDead = true;
				entity = EntityList.createEntityFromNBT(nbt, worldNew);

				if (entity == null)
				{
					return null;
				}

				if (entity instanceof IWorldTransferCallback)
				{
					((IWorldTransferCallback) entity).onWorldTransferred(worldNew);
				}
			}

			worldNew.spawnEntityInWorld(entity);
			entity.setWorld(worldNew);
		}

		if (dimChange)
		{
			if (entity instanceof EntityPlayer)
			{
				entity.setLocationAndAngles(type.getPlayerSpawnLocation((WorldServer) entity.worldObj, (EntityPlayerMP) entity).x, type.getPlayerSpawnLocation((WorldServer) entity.worldObj, (EntityPlayerMP) entity).y, type.getPlayerSpawnLocation((WorldServer) entity.worldObj, (EntityPlayerMP) entity).z, entity.rotationYaw, entity.rotationPitch);
			}
		}

		worldNew.updateEntityWithOptionalForce(entity, false);

		if (dimChange)
		{
			if (entity instanceof EntityPlayer)
			{
				entity.setLocationAndAngles(type.getPlayerSpawnLocation((WorldServer) entity.worldObj, (EntityPlayerMP) entity).x, type.getPlayerSpawnLocation((WorldServer) entity.worldObj, (EntityPlayerMP) entity).y, type.getPlayerSpawnLocation((WorldServer) entity.worldObj, (EntityPlayerMP) entity).z, entity.rotationYaw, entity.rotationPitch);
			}
		}

		if (entity instanceof GCCorePlayerMP)
		{
			player = (GCCorePlayerMP) entity;

			if (dimChange)
			{
				player.mcServer.getConfigurationManager().func_72375_a(player, (WorldServer) worldNew);
			}

			player.playerNetServerHandler.setPlayerLocation(type.getPlayerSpawnLocation((WorldServer) entity.worldObj, (EntityPlayerMP) entity).x, type.getPlayerSpawnLocation((WorldServer) entity.worldObj, (EntityPlayerMP) entity).y, type.getPlayerSpawnLocation((WorldServer) entity.worldObj, (EntityPlayerMP) entity).z, entity.rotationYaw, entity.rotationPitch);

			GCLog.info("Server attempting to transfer player " + player.username + " to dimension " + worldNew.provider.dimensionId);
		}

		worldNew.updateEntityWithOptionalForce(entity, false);

		if (entity instanceof GCCorePlayerMP)
		{
			player = (GCCorePlayerMP) entity;

			if (ridingRocket == null && type.useParachute() && player.getExtendedInventory().getStackInSlot(4) != null && player.getExtendedInventory().getStackInSlot(4).getItem() instanceof GCCoreItemParachute)
			{
				player.setUsingParachute(true);
			}
			else
			{
				player.setUsingParachute(false);
			}
		}

		if (entity instanceof GCCorePlayerMP && dimChange)
		{
			player = (GCCorePlayerMP) entity;
			player.theItemInWorldManager.setWorld((WorldServer) worldNew);
			player.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(player, (WorldServer) worldNew);
			player.mcServer.getConfigurationManager().syncPlayerInventory(player);
			final Iterator<?> var9 = player.getActivePotionEffects().iterator();

			while (var9.hasNext())
			{
				final PotionEffect var10 = (PotionEffect) var9.next();
				player.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(player.entityId, var10));
			}

			player.playerNetServerHandler.sendPacketToPlayer(new Packet43Experience(player.experience, player.experienceTotal, player.experienceLevel));
		}

		if (entity instanceof GCCorePlayerMP)
		{
			micdoodle8.mods.galacticraft.api.vector.Vector3 spawnPos = null;

			if (player != null)
			{
				spawnPos = type.getPlayerSpawnLocation((WorldServer) entity.worldObj, (EntityPlayerMP) entity);
				entity.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
			}
			else
			{
				spawnPos = type.getEntitySpawnLocation((WorldServer) entity.worldObj, entity);
				entity.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
			}
		}

		if (entity instanceof GCCorePlayerMP)
		{
			player = (GCCorePlayerMP) entity;

			if (player.getRocketStacks() != null && player.getRocketStacks().length > 0)
			{
				for (int stack = 0; stack < player.getRocketStacks().length; stack++)
				{
					if (transferInv)
					{
						if (player.getRocketStacks()[stack] == null)
						{
							if (stack == player.getRocketStacks().length - 1)
							{
								if (player.getRocketItem() != null)
								{
									player.getRocketStacks()[stack] = new ItemStack(player.getRocketItem(), 1, player.getRocketType());
								}
							}
							else if (stack == player.getRocketStacks().length - 2)
							{
								player.getRocketStacks()[stack] = new ItemStack(GCCoreBlocks.landingPad, 9, 0);
							}
						}
					}
					else
					{
						player.getRocketStacks()[stack] = null;
					}
				}
			}

			if (transferInv && player.getChestSpawnCooldown() == 0)
			{
				player.setChestSpawnVector(type.getParaChestSpawnLocation((WorldServer) entity.worldObj, player, new Random()));
				player.setChestSpawnCooldown(200);
			}
		}

		if (ridingRocket != null)
		{
			entity.setPositionAndRotation(ridingRocket.posX, ridingRocket.posY, ridingRocket.posZ, 0, 0);
			worldNew.updateEntityWithOptionalForce(entity, true);

			worldNew.spawnEntityInWorld(ridingRocket);
			ridingRocket.setWorld(worldNew);

			worldNew.updateEntityWithOptionalForce(ridingRocket, true);
		}

		if (entity instanceof EntityPlayerMP)
		{
			GameRegistry.onPlayerChangedDimension((EntityPlayerMP) entity);
			type.onSpaceDimensionChanged(worldNew, (EntityPlayerMP) entity, ridingRocket != null);
		}

		if (ridingRocket != null)
		{
			entity.ridingEntity = ridingRocket;
			ridingRocket.riddenByEntity = entity;
		}

		return entity;
	}

	private static void removeEntityFromWorld(World var0, Entity var1, boolean directlyRemove)
	{
		if (var1 instanceof EntityPlayer)
		{
			final EntityPlayer var2 = (EntityPlayer) var1;
			var2.closeScreen();
			var0.playerEntities.remove(var2);
			var0.updateAllPlayersSleepingFlag();
			final int var3 = var1.chunkCoordX;
			final int var4 = var1.chunkCoordZ;

			if (var1.addedToChunk && var0.getChunkProvider().chunkExists(var3, var4))
			{
				var0.getChunkFromChunkCoords(var3, var4).removeEntity(var1);
				var0.getChunkFromChunkCoords(var3, var4).isModified = true;
			}

			if (directlyRemove)
			{
				var0.loadedEntityList.remove(var1);
				var0.onEntityRemoved(var1);
			}
		}

		var1.isDead = false;
	}

	public static SpaceStationRecipe getSpaceStationRecipe(int planetID)
	{
		for (SpaceStationType type : GalacticraftRegistry.getSpaceStationData())
		{
			if (type.getWorldToOrbitID() == planetID)
			{
				return type.getRecipeForSpaceStation();
			}
		}

		return null;
	}

	public static TileEntity[] getAdjacentOxygenConnections(TileEntity tile)
	{
		TileEntity[] adjacentConnections = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];

		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			Vector3 tileVec = new Vector3(tile);
			TileEntity tileEntity = tileVec.modifyPositionFromSide(direction).getTileEntity(tile.worldObj);

			if (tileEntity instanceof IConnector)
			{
				if (((IConnector) tileEntity).canConnect(direction.getOpposite(), NetworkType.OXYGEN))
				{
					adjacentConnections[direction.ordinal()] = tileEntity;
				}
			}
			else if (NetworkConfigHandler.isMekanismLoaded())
			{
				if (tileEntity instanceof ITubeConnection && (!(tileEntity instanceof IGasTransmitter) || TransmissionType.checkTransmissionType(tileEntity, TransmissionType.GAS, tileEntity)))
				{
					if (((ITubeConnection) tileEntity).canTubeConnect(direction))
					{
						adjacentConnections[direction.ordinal()] = tileEntity;
					}
				}
			}
		}

		return adjacentConnections;
	}

	public static TileEntity[] getAdjacentPowerConnections(TileEntity tile)
	{
		TileEntity[] adjacentConnections = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];

		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			Vector3 tileVec = new Vector3(tile);
			TileEntity tileEntity = tileVec.modifyPositionFromSide(direction).getTileEntity(tile.worldObj);

			if (tileEntity instanceof IConnector)
			{
				if (((IConnector) tileEntity).canConnect(direction.getOpposite(), NetworkType.POWER))
				{
					adjacentConnections[direction.ordinal()] = tileEntity;
				}
			}
			else if (NetworkConfigHandler.isIndustrialCraft2Loaded() && tileEntity instanceof IEnergyTile)
			{
				if (tileEntity instanceof IEnergyAcceptor)
				{
					if (((IEnergyAcceptor) tileEntity).acceptsEnergyFrom(tile, direction.getOpposite()))
					{
						adjacentConnections[direction.ordinal()] = tileEntity;
						continue;
					}
				}

				if (tileEntity instanceof IEnergyEmitter)
				{
					if (((IEnergyEmitter) tileEntity).emitsEnergyTo(tileEntity, direction.getOpposite()))
					{
						adjacentConnections[direction.ordinal()] = tileEntity;
						continue;
					}
				}

				adjacentConnections[direction.ordinal()] = tileEntity;
			}
			else if (NetworkConfigHandler.isBuildcraftLoaded() && tileEntity instanceof IPowerReceptor)
			{
				if (((IPowerReceptor) tileEntity).getPowerReceiver(direction.getOpposite()) != null)
				{
					adjacentConnections[direction.ordinal()] = tileEntity;
				}
			}
			else if (NetworkConfigHandler.isThermalExpansionLoaded() && tileEntity instanceof IEnergyHandler)
			{
				if (((IEnergyHandler) tileEntity).canInterface(direction.getOpposite()))
				{
					adjacentConnections[direction.ordinal()] = tileEntity;
				}
			}
		}

		return adjacentConnections;
	}
}
