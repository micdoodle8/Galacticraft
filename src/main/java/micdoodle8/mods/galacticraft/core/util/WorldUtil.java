package micdoodle8.mods.galacticraft.core.util;

import buildcraft.api.mj.MjAPI;
import buildcraft.api.power.IPowerReceptor;

import com.google.common.collect.Lists;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergyTile;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IWorldTransferCallback;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.compatibility.NetworkConfigHandler;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.api.world.SpaceStationType;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.SpaceStationWorldData;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderOrbit;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.items.ItemParaChute;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

import java.io.File;
import java.util.*;
//import mekanism.api.energy.IStrictEnergyAcceptor;
//import mekanism.api.gas.IGasTransmitter;
//import mekanism.api.gas.ITubeConnection;
//import mekanism.api.transmitters.TransmissionType;

public class WorldUtil
{
	public static List<Integer> registeredSpaceStations;
	public static List<Integer> registeredPlanets;
	public static List<String> registeredPlanetNames;

	public static double getGravityForEntity(Entity entity)
	{
		if (entity.worldObj.provider instanceof IGalacticraftWorldProvider)
		{
			final IGalacticraftWorldProvider customProvider = (IGalacticraftWorldProvider) entity.worldObj.provider;
            return 0.08D - customProvider.getGravity();
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
			return Math.max(0.002D, 0.03999999910593033D - (customProvider instanceof IOrbitDimension ? 0.05999999910593033D : customProvider.getGravity()) / 1.75D);
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
		return new Vector3(1, 1, 1);
	}

	@SideOnly(Side.CLIENT)
	public static float getWorldBrightness(WorldClient world)
	{
		if (world.provider instanceof WorldProviderMoon)
		{
			float f1 = world.getCelestialAngle(1.0F);
			float f2 = 1.0F - (MathHelper.cos(f1 * (float) Math.PI * 2.0F) * 2.0F + 0.2F);

			if (f2 < 0.0F)
			{
				f2 = 0.0F;
			}

			if (f2 > 1.0F)
			{
				f2 = 1.0F;
			}

			f2 = 1.0F - f2;
			return f2 * 0.8F;
		}

		return world.getSunBrightness(1.0F);
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

    public static CelestialBody getReachableCelestialBodiesForDimensionID(int id)
    {
        List<CelestialBody> celestialBodyList = Lists.newArrayList();
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredSatellites().values());

        for (CelestialBody cBody : celestialBodyList)
        {
            if (cBody.getReachable())
            {
                if (cBody.getDimensionID() == id)
                {
                    return cBody;
                }
            }
        }

        return null;
    }

    public static CelestialBody getReachableCelestialBodiesForName(String name)
    {
        List<CelestialBody> celestialBodyList = Lists.newArrayList();
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredSatellites().values());

        for (CelestialBody cBody : celestialBodyList)
        {
            if (cBody.getReachable())
            {
                if (cBody.getName().equals(name))
                {
                    return cBody;
                }
            }
        }

        return null;
    }

	public static HashMap<String, Integer> getArrayOfPossibleDimensions(List<Integer> ids, GCEntityPlayerMP playerBase)
	{
		final HashMap<String, Integer> map = new HashMap<String, Integer>();

		for (Integer id : ids)
		{
            CelestialBody celestialBody = getReachableCelestialBodiesForDimensionID(id);

            if (id > 0 && celestialBody == null)
            {
                celestialBody = GalacticraftCore.satelliteSpaceStation;
            }

			if (celestialBody != null && WorldProvider.getProviderForDimension(id) != null)
			{
				if (WorldProvider.getProviderForDimension(id) instanceof IGalacticraftWorldProvider && !(WorldProvider.getProviderForDimension(id) instanceof IOrbitDimension) || WorldProvider.getProviderForDimension(id).dimensionId == 0)
				{
                    map.put(celestialBody.getName(), WorldProvider.getProviderForDimension(id).dimensionId);
				}
				else if (playerBase != null && WorldProvider.getProviderForDimension(id) instanceof IOrbitDimension)
				{
					final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.worldObj, id, playerBase);

					if (!ConfigManagerCore.spaceStationsRequirePermission || data.getAllowedPlayers().contains(playerBase.getGameProfile().getName().toLowerCase()) || data.getAllowedPlayers().contains(playerBase.getGameProfile().getName()) || playerBase.mcServer.getConfigurationManager().isPlayerOpped(playerBase.getGameProfile().getName()))
					{
                        map.put(celestialBody.getName() + "$" + data.getOwner() + "$" + data.getSpaceStationName() + "$" + WorldProvider.getProviderForDimension(id).dimensionId, WorldProvider.getProviderForDimension(id).dimensionId);
					}
				}
			}
		}

		ArrayList<CelestialBody> cBodyList = new ArrayList<CelestialBody>();
		cBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
		cBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());

		for (CelestialBody body : cBodyList)
		{
			if (!body.getReachable())
			{
				map.put(body.getLocalizedName() + "*", body.getDimensionID());
			}
		}

		return map;
	}

	public static List<String> getPlayersOnPlanet(CelestialBody planet)
	{
		final List<String> list = new ArrayList<String>();

		for (final WorldServer world : DimensionManager.getWorlds())
		{
			if (world != null && world.provider instanceof WorldProviderSpace)
			{
				if (planet.getLocalizedName().equals(world.provider.getDimensionName()))
				{
					for (int j = 0; j < world.getLoadedEntityList().size(); j++)
					{
						if (world.getLoadedEntityList().get(j) != null && world.getLoadedEntityList().get(j) instanceof EntityPlayer)
						{
							list.add(((EntityPlayer) world.getLoadedEntityList().get(j)).getGameProfile().getName());
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
			int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, registeredID.intValue());

			if (id >= 0)
			{
				DimensionManager.registerDimension(registeredID.intValue(), ConfigManagerCore.idDimensionOverworldOrbitStatic);
				FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(registeredID.intValue());
			}
			else
			{
				DimensionManager.registerDimension(registeredID.intValue(), ConfigManagerCore.idDimensionOverworldOrbit);
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

	public static SpaceStationWorldData bindSpaceStationToNewDimension(World world, GCEntityPlayerMP player)
	{
		int newID = DimensionManager.getNextFreeDimId();
		SpaceStationWorldData data = WorldUtil.createSpaceStation(world, newID, player);
		player.getPlayerStats().spaceStationDimensionID = newID;
		GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, new Object[] { newID }), player);
		return data;
	}

	public static SpaceStationWorldData createSpaceStation(World world, int dimID, GCEntityPlayerMP player)
	{
		WorldUtil.registeredSpaceStations.add(dimID);
		int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, dimID);

		if (id >= 0)
		{
			DimensionManager.registerDimension(dimID, ConfigManagerCore.idDimensionOverworldOrbitStatic);
		}
		else
		{
			DimensionManager.registerDimension(dimID, ConfigManagerCore.idDimensionOverworldOrbit);
		}

		GalacticraftCore.packetPipeline.sendToAll(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_LIST, WorldUtil.getSpaceStationList()));
		return SpaceStationWorldData.getStationData(world, dimID, player);
	}

	public static Entity transferEntityToDimension(Entity entity, int dimensionID, WorldServer world)
	{
		return WorldUtil.transferEntityToDimension(entity, dimensionID, world, true, null);
	}

	public static Entity transferEntityToDimension(Entity entity, int dimensionID, WorldServer world, boolean transferInv, EntityAutoRocket ridingRocket)
	{
		if (!world.isRemote)
		{
			GalacticraftCore.packetPipeline.sendToAll(new PacketSimple(EnumSimplePacket.C_UPDATE_PLANETS_LIST, WorldUtil.getPlanetList()));

			MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();

			if (mcServer != null)
			{
				final WorldServer var6 = mcServer.worldServerForDimension(dimensionID);

				if (var6 == null)
				{
					System.err.println("Cannot Transfer Entity to Dimension: Could not get World for Dimension " + dimensionID);
					return null;
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
		GCEntityPlayerMP player = null;
		int oldDimID = entity.worldObj.provider.dimensionId;
		
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
			if (entity instanceof GCEntityPlayerMP)
			{
				player = (GCEntityPlayerMP) entity;
				World worldOld = player.worldObj;
				System.out.println("DEBUG: Attempting to remove player from old dimension "+oldDimID); //radtemp
				((WorldServer) worldOld).getPlayerManager().removePlayer(player);
				System.out.println("DEBUG: Successfully removed player from old dimension "+oldDimID); //radtemp
				player.closeScreen();
				player.getPlayerStats().usingPlanetSelectionGui = false;
				
				player.dimension = dimID;
				System.out.println("DEBUG: Sending respawn packet to player for dim "+dimID); //radtemp
				player.playerNetServerHandler.sendPacket(new S07PacketRespawn(dimID, player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));

				if (worldNew.provider instanceof WorldProviderOrbit)
				{
					((WorldProviderOrbit) worldNew.provider).sendPacketsToClient(player);
					if (WorldUtil.registeredSpaceStations.contains(dimID))
						//TODO This has never been effective before due to the earlier bug - what does it actually do?
					{
						NBTTagCompound var2 = new NBTTagCompound();
						SpaceStationWorldData.getStationData(worldNew, dimID, player).writeToNBT(var2);
						GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_DATA, new Object[] { dimID, var2 }), player);
					}
				}

				worldOld.playerEntities.remove(player);
				worldOld.updateAllPlayersSleepingFlag();
				if (player.addedToChunk && worldOld.getChunkProvider().chunkExists(player.chunkCoordX, player.chunkCoordZ))
				{
					Chunk chunkOld = worldOld.getChunkFromChunkCoords(player.chunkCoordX, player.chunkCoordZ); 
					chunkOld.removeEntity(player);
					chunkOld.isModified = true;
				}
				worldOld.loadedEntityList.remove(player);
				worldOld.onEntityRemoved(player);

				Vector3 spawnPos = type.getPlayerSpawnLocation((WorldServer) entity.worldObj, player);
				entity.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
				ChunkCoordIntPair pair = worldNew.getChunkFromChunkCoords(spawnPos.intX(), spawnPos.intZ()).getChunkCoordIntPair();
				System.out.println("DEBUG: Loading first chunk in new dimension.");  //radtemp
				((WorldServer) worldNew).theChunkProviderServer.loadChunk(pair.chunkXPos, pair.chunkZPos);

				worldNew.spawnEntityInWorld(entity);
				entity.setWorld(worldNew);
				//entity.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
				worldNew.updateEntityWithOptionalForce(entity, false);
				entity.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);

				player.mcServer.getConfigurationManager().func_72375_a(player, (WorldServer) worldNew);
				player.playerNetServerHandler.setPlayerLocation(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
				//worldNew.updateEntityWithOptionalForce(entity, false);

				GCLog.info("Server attempting to transfer player " + player.getGameProfile().getName() + " to dimension " + worldNew.provider.dimensionId);

				player.theItemInWorldManager.setWorld((WorldServer) worldNew);
				player.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(player, (WorldServer) worldNew);
				player.mcServer.getConfigurationManager().syncPlayerInventory(player);
				final Iterator<?> var9 = player.getActivePotionEffects().iterator();
				while (var9.hasNext())
				{
					final PotionEffect var10 = (PotionEffect) var9.next();
					player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), var10));
				}

				//	player.playerNetServerHandler.sendPacketToPlayer(new Packet43Experience(player.experience, player.experienceTotal, player.experienceLevel));
			}
			else
			{
				WorldUtil.removeEntityFromWorld(entity.worldObj, entity, true);

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

				worldNew.spawnEntityInWorld(entity);
				entity.setWorld(worldNew);

				worldNew.updateEntityWithOptionalForce(entity, false);
			}			
		}
		else
		{
			if (entity instanceof GCEntityPlayerMP)
			{
				player = (GCEntityPlayerMP) entity;
				player.closeScreen();
				player.getPlayerStats().usingPlanetSelectionGui = false;
	
				worldNew.updateEntityWithOptionalForce(entity, false);
	
				player.playerNetServerHandler.setPlayerLocation(type.getPlayerSpawnLocation((WorldServer) entity.worldObj, (EntityPlayerMP) entity).x, type.getPlayerSpawnLocation((WorldServer) entity.worldObj, (EntityPlayerMP) entity).y, type.getPlayerSpawnLocation((WorldServer) entity.worldObj, (EntityPlayerMP) entity).z, entity.rotationYaw, entity.rotationPitch);
	
				GCLog.info("Server attempting to transfer player " + player.getGameProfile().getName() + " within same dimension " + worldNew.provider.dimensionId);
			}
	
			worldNew.updateEntityWithOptionalForce(entity, false);
		}

		if (player != null)
		{
			if (ridingRocket == null && type.useParachute() && player.getPlayerStats().extendedInventory.getStackInSlot(4) != null && player.getPlayerStats().extendedInventory.getStackInSlot(4).getItem() instanceof ItemParaChute)
			{
				player.setUsingParachute(true);
			}
			else
			{
				player.setUsingParachute(false);
			}

			micdoodle8.mods.galacticraft.api.vector.Vector3 spawnPos = null;
			spawnPos = type.getPlayerSpawnLocation((WorldServer) entity.worldObj, (EntityPlayerMP) entity);
			entity.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);

			if (player.getPlayerStats().rocketStacks != null && player.getPlayerStats().rocketStacks.length > 0)
			{
				for (int stack = 0; stack < player.getPlayerStats().rocketStacks.length; stack++)
				{
					if (transferInv)
					{
						if (player.getPlayerStats().rocketStacks[stack] == null)
						{
							if (stack == player.getPlayerStats().rocketStacks.length - 1)
							{
								if (player.getPlayerStats().rocketItem != null)
								{
									player.getPlayerStats().rocketStacks[stack] = new ItemStack(player.getPlayerStats().rocketItem, 1, player.getPlayerStats().rocketType);
								}
							}
							else if (stack == player.getPlayerStats().rocketStacks.length - 2)
							{
								player.getPlayerStats().rocketStacks[stack] = player.getPlayerStats().launchpadStack;
								player.getPlayerStats().launchpadStack = null;
							}
						}
					}
					else
					{
						player.getPlayerStats().rocketStacks[stack] = null;
					}
				}
			}

			if (transferInv && player.getPlayerStats().chestSpawnCooldown == 0)
			{
				player.getPlayerStats().chestSpawnVector = type.getParaChestSpawnLocation((WorldServer) entity.worldObj, player, new Random());
				player.getPlayerStats().chestSpawnCooldown = 200;
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
			FMLCommonHandler.instance().firePlayerChangedDimensionEvent((EntityPlayerMP) entity, oldDimID, dimID);
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

		//boolean isMekLoaded = NetworkConfigHandler.isMekanismLoaded();

		BlockVec3 thisVec = new BlockVec3(tile);
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			TileEntity tileEntity = thisVec.getTileEntityOnSide(tile.getWorldObj(), direction);

			if (tileEntity instanceof IConnector)
			{
				if (((IConnector) tileEntity).canConnect(direction.getOpposite(), NetworkType.OXYGEN))
				{
					adjacentConnections[direction.ordinal()] = tileEntity;
				}
			}
			/*else if (isMekLoaded)
			{
				if (tileEntity instanceof ITubeConnection && (!(tileEntity instanceof IGasTransmitter) || TransmissionType.checkTransmissionType(tileEntity, TransmissionType.GAS, tileEntity)))
				{
					if (((ITubeConnection) tileEntity).canTubeConnect(direction))
					{
						adjacentConnections[direction.ordinal()] = tileEntity;
					}
				}
			}*/
		}

		return adjacentConnections;
	}

	public static TileEntity[] getAdjacentPowerConnections(TileEntity tile)
	{
		TileEntity[] adjacentConnections = new TileEntity[6];

		//boolean isMekLoaded = NetworkConfigHandler.isMekanismLoaded();
		//boolean isTELoaded = NetworkConfigHandler.isThermalExpansionLoaded();
		boolean isIC2Loaded = NetworkConfigHandler.isIndustrialCraft2Loaded();
		boolean isBCLoaded = NetworkConfigHandler.isBuildcraftLoaded();

		BlockVec3 thisVec = new BlockVec3(tile);
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			TileEntity tileEntity = thisVec.getTileEntityOnSide(tile.getWorldObj(), direction);

			if (tileEntity instanceof IConnector)
			{
				if (((IConnector) tileEntity).canConnect(direction.getOpposite(), NetworkType.POWER))
				{
					adjacentConnections[direction.ordinal()] = tileEntity;
				}
				continue;
			}
			/*else if (isMekLoaded && tileEntity instanceof IStrictEnergyAcceptor)
			{
				if (((IStrictEnergyAcceptor) tileEntity).canReceiveEnergy(direction.getOpposite()))
				{
					adjacentConnections[direction.ordinal()] = tileEntity;
				}
			}
			else if (isTELoaded && tileEntity instanceof IEnergyHandler)
			{
				if (((IEnergyHandler) tileEntity).canInterface(direction.getOpposite()))
				{
					adjacentConnections[direction.ordinal()] = tileEntity;
				}
			}*/
			else if (isIC2Loaded && tileEntity instanceof IEnergyTile)
			{
				if (tileEntity instanceof IEnergyConductor)
					continue;
				
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
					if (((IEnergyEmitter) tileEntity).emitsEnergyTo(tile, direction.getOpposite()))
					{
						adjacentConnections[direction.ordinal()] = tileEntity;
						continue;
					}
				}
			}
			else if (isBCLoaded)
			{
				//Do not connect GC wires to BC wooden power pipes
				try
				{
					Class<?> clazzPipeTile = Class.forName("buildcraft.transport.TileGenericPipe");
					Class<?> clazzPipeWood = Class.forName("buildcraft.transport.pipes.PipePowerWood");
					if (clazzPipeTile.isInstance(tileEntity))
					{				
						Object pipe = clazzPipeTile.getField("pipe").get(tileEntity);
						if (clazzPipeWood.isInstance(pipe))
							continue;
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				//New BC API
				if (MjAPI.getMjBattery(tileEntity, MjAPI.DEFAULT_POWER_FRAMEWORK, direction.getOpposite()) != null)
					adjacentConnections[direction.ordinal()] = tileEntity;
				
				//Legacy BC API
				if (tileEntity instanceof IPowerReceptor && ((IPowerReceptor) tileEntity).getPowerReceiver(direction.getOpposite()) != null)
				{
					adjacentConnections[direction.ordinal()] = tileEntity;
				}			
			}
		}

		return adjacentConnections;
	}

	public static List<Object> getPlanetList()
	{
		Integer[] iArray = new Integer[WorldUtil.registeredPlanets.size()];

		for (int i = 0; i < iArray.length; i++)
		{
			iArray[i] = WorldUtil.registeredPlanets.get(i);
		}

		List<Object> objList = new ArrayList<Object>();
		objList.add(iArray);
		return objList;
	}

	public static List<Object> getSpaceStationList()
	{
		Integer[] iArray = new Integer[WorldUtil.registeredSpaceStations.size()];

		for (int i = 0; i < iArray.length; i++)
		{
			iArray[i] = WorldUtil.registeredSpaceStations.get(i);
		}

		List<Object> objList = new ArrayList<Object>();
		objList.add(iArray);
		return objList;
	}
}
