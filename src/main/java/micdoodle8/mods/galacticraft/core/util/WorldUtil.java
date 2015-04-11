package micdoodle8.mods.galacticraft.core.util;

import com.google.common.collect.Lists;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IWorldTransferCallback;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.*;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.SpaceStationWorldData;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderOrbit;
import micdoodle8.mods.galacticraft.core.entities.EntityArrowGC;
import micdoodle8.mods.galacticraft.core.entities.EntityCelestialFake;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.ItemParaChute;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTelemetry;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

//import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;

public class WorldUtil
{
    public static List<Integer> registeredSpaceStations;
    public static List<Integer> registeredPlanets;
    private static IWorldGenerator generatorGCGreg = null;
    private static IWorldGenerator generatorCoFH = null;
    private static IWorldGenerator generatorDenseOres = null;
    private static IWorldGenerator generatorTCAuraNodes = null;
    private static IWorldGenerator generatorAE2meteors = null;
    private static Method generateTCAuraNodes = null;
    private static boolean generatorsInitialised = false;
	
    public static double getGravityForEntity(Entity entity)
    {
        if (entity.worldObj.provider instanceof IGalacticraftWorldProvider)
        {
            final IGalacticraftWorldProvider customProvider = (IGalacticraftWorldProvider) entity.worldObj.provider;
            return 0.08D - customProvider.getGravity();
        }
        else if (GalacticraftCore.isPlanetsLoaded && entity instanceof EntityAstroMiner)
        {
        	return 0;
        }
        else
        {
            return 0.08D;
        }
    }
    
    public static float getGravityFactor(Entity entity)
    {
        if (entity.worldObj.provider instanceof IGalacticraftWorldProvider)
        {
            final IGalacticraftWorldProvider customProvider = (IGalacticraftWorldProvider) entity.worldObj.provider;
        	float returnValue = MathHelper.sqrt_float(0.08F / (0.08F - customProvider.getGravity()));
        	if (returnValue > 2.5F) returnValue = 2.5F;
        	if (returnValue < 0.75F) returnValue = 0.75F;
        	return returnValue;
        }
        else if (GalacticraftCore.isPlanetsLoaded && entity instanceof EntityAstroMiner)
        {
        	return 1F;
        }
        else
        {
            return 1F;
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

    public static boolean shouldRenderFire(Entity entity)
    {
        if (entity.worldObj == null || !(entity.worldObj.provider instanceof IGalacticraftWorldProvider)) return entity.isBurning();

    	if (!(entity instanceof EntityLivingBase) && !(entity instanceof EntityArrow) && !(entity instanceof EntityArrowGC))
        {
    		return entity.isBurning();
        }

    	if (entity.isBurning())
    	{
	        if (OxygenUtil.noAtmosphericCombustion(entity.worldObj.provider))
	        	return OxygenUtil.isAABBInBreathableAirBlock(entity.worldObj, entity.boundingBox);
	        else
	        	return true;
	        //Disable fire on Galacticraft worlds with no oxygen
    	}
    	
        return false;
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
        String nameToFind = par1String;
        if (par1String.contains("$"))
        {
            final String[] twoDimensions = par1String.split("\\$");
            nameToFind = twoDimensions[0];
        }
        if (nameToFind == null)
        {
            return null;
        }

        for (final Integer element : WorldUtil.getArrayOfPossibleDimensions())
        {
            WorldProvider elementProvider = WorldUtil.getProviderForDimension(element);
            if (elementProvider != null && nameToFind.equals(elementProvider.getDimensionName()))
            {
                return elementProvider;
            }
        }

        GCLog.info("Failed to find matching world for '" + par1String + "'");
        return null;
    }

    public static List<Integer> getPossibleDimensionsForSpaceshipTier(int tier)
    {
        List<Integer> temp = new ArrayList<Integer>();

        if (!ConfigManagerCore.disableRocketsToOverworld)
        {
            temp.add(0);
        }

        for (Integer element : WorldUtil.registeredPlanets)
        {
        	WorldProvider provider = WorldUtil.getProviderForDimension(element);

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
        	WorldProvider provider = WorldUtil.getProviderForDimension(element);

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

    public static WorldProvider getProviderForDimension(int id)
    {
    	WorldProvider provider = null;
    	MinecraftServer theServer = FMLCommonHandler.instance().getMinecraftServerInstance();
    	if (theServer != null)
    	{
    		WorldServer ws = theServer.worldServerForDimension(id);
    		if (ws != null)
    			provider = ws.provider;
    	}
    	if (provider == null) provider = WorldProvider.getProviderForDimension(id);
    	return provider;
    }
    
    public static HashMap<String, Integer> getArrayOfPossibleDimensions(List<Integer> ids, EntityPlayerMP playerBase)
    {
        final HashMap<String, Integer> map = new HashMap<String, Integer>();

        for (Integer id : ids)
        {
            CelestialBody celestialBody = getReachableCelestialBodiesForDimensionID(id);

            if (id > 0 && celestialBody == null)
            {
                celestialBody = GalacticraftCore.satelliteSpaceStation;
            }

            WorldProvider provider = WorldUtil.getProviderForDimension(id);
            if (celestialBody != null && provider != null)
            {
                if (provider instanceof IGalacticraftWorldProvider && !(provider instanceof IOrbitDimension) || provider.dimensionId == 0)
                {
                    map.put(celestialBody.getName(), provider.dimensionId);
                }
                else if (playerBase != null && provider instanceof IOrbitDimension)
                {
                    final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.worldObj, id, playerBase);

                    if (!ConfigManagerCore.spaceStationsRequirePermission || data.getAllowedPlayers().contains(playerBase.getGameProfile().getName()) || VersionUtil.isPlayerOpped(playerBase))
                    {
                        map.put(celestialBody.getName() + "$" + data.getOwner() + "$" + data.getSpaceStationName() + "$" + provider.dimensionId, provider.dimensionId);
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

    private static List<Integer> getExistingSpaceStationList(File var0)
    {
        final ArrayList<Integer> var1 = new ArrayList<Integer>();
        final File[] var2 = var0.listFiles();

        if (var2 != null)
        {
            for (File var5 : var2)
            {
                if (var5.getName().contains("spacestation_"))
                {
                    String var6 = var5.getName();
                    var6 = var6.substring(13, var6.length() - 4);
                    var1.add(Integer.parseInt(var6));
                }
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
    	MinecraftServer theServer = FMLCommonHandler.instance().getMinecraftServerInstance();

        for (Integer registeredID : WorldUtil.registeredSpaceStations)
        {
            int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, registeredID);

            if (!DimensionManager.isDimensionRegistered(registeredID))
            {
	            if (id >= 0)
	            {
	                DimensionManager.registerDimension(registeredID, ConfigManagerCore.idDimensionOverworldOrbitStatic);
	                theServer.worldServerForDimension(registeredID);
                }
	            else
	            {
	                DimensionManager.registerDimension(registeredID, ConfigManagerCore.idDimensionOverworldOrbit);
	            }
            }
            else
            {
                GCLog.severe("Dimension already registered to another mod: unable to register space station dimension " + registeredID);
            }
        }
    }

    /**
     * Call this on FMLServerStartingEvent to add a hotloadable planet ID
     * 
     * IMPORTANT: GalacticraftRegistry.registerProvider() must always be called in parallel with this
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
            if (!DimensionManager.isDimensionRegistered(planetID))
            {
	            DimensionManager.registerDimension(planetID, planetID);
	            GCLog.info("Registered Dimension: " + planetID);
            }
            else
            {
                GCLog.severe("Dimension already registered to another mod: unable to register planet dimension " + planetID);
            }
        }
    }

    public static void unregisterPlanets()
    {
        if (WorldUtil.registeredPlanets != null)
        {
            for (Integer var1 : WorldUtil.registeredPlanets)
            {
                DimensionManager.unregisterDimension(var1);
                GCLog.info("Unregistered Dimension: " + var1);
            }

            WorldUtil.registeredPlanets = null;
        }
    }

    public static void registerPlanetClient(Integer dimID, int providerIndex)
    {
        int providerID = GalacticraftRegistry.getProviderID(providerIndex);

        if (providerID == 0)
        {
        	GCLog.severe("Server dimension " + dimID + " has no match on client due to earlier registration problem.");
        }
        else
        {
            if (!WorldUtil.registeredPlanets.contains(dimID))
            {
                WorldUtil.registeredPlanets.add(dimID);
                DimensionManager.registerDimension(dimID, providerID);
            }
            else
            {
                GCLog.severe("Dimension already registered to another mod: unable to register planet dimension " + dimID);
            }
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

        if (WorldUtil.registeredSpaceStations != null)
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

    public static SpaceStationWorldData bindSpaceStationToNewDimension(World world, EntityPlayerMP player)
    {
        int newID = DimensionManager.getNextFreeDimId();
        SpaceStationWorldData data = WorldUtil.createSpaceStation(world, newID, player);
        GCPlayerStats stats = GCPlayerStats.get(player);
        stats.spaceStationDimensionID = newID;
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, new Object[] { newID }), player);
        return data;
    }

    public static SpaceStationWorldData createSpaceStation(World world, int dimID, EntityPlayerMP player)
    {
        WorldUtil.registeredSpaceStations.add(dimID);
        int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, dimID);

        if (!DimensionManager.isDimensionRegistered(dimID))
        {
	        if (id >= 0)
	        {
	            DimensionManager.registerDimension(dimID, ConfigManagerCore.idDimensionOverworldOrbitStatic);
	        }
	        else
	        {
	            DimensionManager.registerDimension(dimID, ConfigManagerCore.idDimensionOverworldOrbit);
	        }
        }
        else
        {
            GCLog.severe("Dimension already registered to another mod: unable to register space station dimension " + dimID);
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
            //GalacticraftCore.packetPipeline.sendToAll(new PacketSimple(EnumSimplePacket.C_UPDATE_PLANETS_LIST, WorldUtil.getPlanetList()));

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
        if (entity.ridingEntity != null)
        {
        	if (entity.ridingEntity instanceof EntitySpaceshipBase)
        		entity.mountEntity(entity.ridingEntity);
        	else if (entity.ridingEntity instanceof EntityCelestialFake)
        	{
        		entity.ridingEntity.setDead();
        		entity.mountEntity(null);
        	}
        }

        boolean dimChange = entity.worldObj != worldNew;
        entity.worldObj.updateEntityWithOptionalForce(entity, false);
        EntityPlayerMP player = null;
        Vector3 spawnPos = null;
        int oldDimID = entity.worldObj.provider.dimensionId;

        if (ridingRocket != null)
        {
            ArrayList<TileEntityTelemetry> tList = ridingRocket.getTelemetry();
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
            if (entity instanceof EntityPlayerMP)
            {
                player = (EntityPlayerMP) entity;
                World worldOld = player.worldObj;
                if (ConfigManagerCore.enableDebug)
                {
                    try {
	                    GCLog.info("DEBUG: Attempting to remove player from old dimension " + oldDimID);
	                    ((WorldServer) worldOld).getPlayerManager().removePlayer(player);
	                    GCLog.info("DEBUG: Successfully removed player from old dimension " + oldDimID);
                    } catch (Exception e) { e.printStackTrace(); }
                }
                else
                {
                    try {
                    	((WorldServer) worldOld).getPlayerManager().removePlayer(player);
                    } catch (Exception e) {  }
                }

                player.closeScreen();
                GCPlayerStats stats = GCPlayerStats.get(player);
                stats.usingPlanetSelectionGui = false;

                player.dimension = dimID;
                if (ConfigManagerCore.enableDebug)
                {
                    GCLog.info("DEBUG: Sending respawn packet to player for dim " + dimID);
                }
                player.playerNetServerHandler.sendPacket(new S07PacketRespawn(dimID, player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));

                if (worldNew.provider instanceof WorldProviderOrbit)
                {
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

                worldNew.spawnEntityInWorld(entity);
                entity.setWorld(worldNew);

                spawnPos = type.getPlayerSpawnLocation((WorldServer) entity.worldObj, player);
                ChunkCoordIntPair pair = worldNew.getChunkFromChunkCoords(spawnPos.intX(), spawnPos.intZ()).getChunkCoordIntPair();
                if (ConfigManagerCore.enableDebug)
                {
                    GCLog.info("DEBUG: Loading first chunk in new dimension.");
                }
                ((WorldServer) worldNew).theChunkProviderServer.loadChunk(pair.chunkXPos, pair.chunkZPos);
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

                for (Object o : player.getActivePotionEffects())
                {
                    PotionEffect var10 = (PotionEffect) o;
                    player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), var10));
                }

                player.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(player.experience, player.experienceTotal, player.experienceLevel));
            }
            else
            //Non-player entity transfer i.e. it's an EntityCargoRocket
            {
                ArrayList<TileEntityTelemetry> tList = null;
                if (entity instanceof EntitySpaceshipBase)
                {
                	tList = ((EntitySpaceshipBase)entity).getTelemetry();
                }
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
                
                if (tList != null && tList.size() > 0)
                {
                	for (TileEntityTelemetry t : tList)
                	{
                		t.addTrackedEntity(entity);
                	}
                }
            }
        }
        else
        {
            //Same dimension player transfer
            if (entity instanceof EntityPlayerMP)
            {
                player = (EntityPlayerMP) entity;
                player.closeScreen();
                GCPlayerStats stats = GCPlayerStats.get(player);
                stats.usingPlanetSelectionGui = false;

                worldNew.updateEntityWithOptionalForce(entity, false);

                spawnPos = type.getPlayerSpawnLocation((WorldServer) entity.worldObj, (EntityPlayerMP) entity);
                player.playerNetServerHandler.setPlayerLocation(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
                entity.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
                worldNew.updateEntityWithOptionalForce(entity, false);

                GCLog.info("Server attempting to transfer player " + player.getGameProfile().getName() + " within same dimension " + worldNew.provider.dimensionId);
            }

            //Cargo rocket does not needs its location setting here, it will do that itself
        }

        //Update PlayerStatsGC
        if (player != null)
        {
            GCPlayerStats playerStats = GCPlayerStats.get(player);
            if (ridingRocket == null && type.useParachute() && playerStats.extendedInventory.getStackInSlot(4) != null && playerStats.extendedInventory.getStackInSlot(4).getItem() instanceof ItemParaChute)
            {
                GCPlayerHandler.setUsingParachute(player, playerStats, true);
            }
            else
            {
                GCPlayerHandler.setUsingParachute(player, playerStats, false);
            }

            if (playerStats.rocketStacks != null && playerStats.rocketStacks.length > 0)
            {
                for (int stack = 0; stack < playerStats.rocketStacks.length; stack++)
                {
                    if (transferInv)
                    {
                        if (playerStats.rocketStacks[stack] == null)
                        {
                            if (stack == playerStats.rocketStacks.length - 1)
                            {
                                if (playerStats.rocketItem != null)
                                {
                                    playerStats.rocketStacks[stack] = new ItemStack(playerStats.rocketItem, 1, playerStats.rocketType);
                                }
                            }
                            else if (stack == playerStats.rocketStacks.length - 2)
                            {
                                playerStats.rocketStacks[stack] = playerStats.launchpadStack;
                                playerStats.launchpadStack = null;
                            }
                        }
                    }
                    else
                    {
                        playerStats.rocketStacks[stack] = null;
                    }
                }
            }

            if (transferInv && playerStats.chestSpawnCooldown == 0)
            {
                playerStats.chestSpawnVector = type.getParaChestSpawnLocation((WorldServer) entity.worldObj, player, new Random());
                playerStats.chestSpawnCooldown = 200;
            }
        }

        //If in a rocket (e.g. with launch controller) set the player to the rocket's position instead of the player's spawn position
        if (ridingRocket != null)
        {
            entity.setPositionAndRotation(ridingRocket.posX, ridingRocket.posY, ridingRocket.posZ, 0, 0);
            worldNew.updateEntityWithOptionalForce(entity, true);

            worldNew.spawnEntityInWorld(ridingRocket);
            ridingRocket.setWorld(worldNew);

            worldNew.updateEntityWithOptionalForce(ridingRocket, true);
            entity.mountEntity(ridingRocket);
        }
        else if (spawnPos != null)
        {
            entity.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
        }

        //Spawn in a lander if appropriate
        if (entity instanceof EntityPlayerMP)
        {
            FMLCommonHandler.instance().firePlayerChangedDimensionEvent((EntityPlayerMP) entity, oldDimID, dimID);
            type.onSpaceDimensionChanged(worldNew, (EntityPlayerMP) entity, ridingRocket != null);
        }

        return entity;
    }

    @SideOnly(Side.CLIENT)
    public static EntityPlayer forceRespawnClient(int dimID, int par2, String par3, int par4)
    {
        S07PacketRespawn fakePacket = new S07PacketRespawn(dimID, EnumDifficulty.getDifficultyEnum(par2), WorldType.parseWorldType(par3), WorldSettings.GameType.getByID(par4));
        Minecraft.getMinecraft().getNetHandler().handleRespawn(fakePacket);
		return FMLClientHandler.instance().getClientPlayerEntity();
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

    /**
     *  This must return planets in the same order their provider IDs
     *   were registered in GalacticraftRegistry by GalacticraftCore.
     */
    public static List<Object> getPlanetList()
    {
        List<Object> objList = new ArrayList<Object>();
        objList.add(getPlanetListInts());
        return objList;
    }

    public static Integer[] getPlanetListInts()
    {
        Integer[] iArray = new Integer[WorldUtil.registeredPlanets.size()];

        for (int i = 0; i < iArray.length; i++)
        {
            iArray[i] = WorldUtil.registeredPlanets.get(i);
        }

        return iArray;
    }
    
    public static void decodePlanetsListClient(List<Object> data)
    {
        try
        {
            if (ConfigManagerCore.enableDebug)
            	GCLog.info("GC connecting to server: received planets dimension ID list.");
        	if (WorldUtil.registeredPlanets != null)
            {
                for (Integer registeredID : WorldUtil.registeredPlanets)
                {
                    DimensionManager.unregisterDimension(registeredID);
                }
            }
            WorldUtil.registeredPlanets = new ArrayList<Integer>();

            String ids = "";
            if (data.size() > 0)
            {
            	//Start the provider index at offset 2 to skip the two Overworld Orbit dimensions
            	int providerIndex = 2;
                if (data.get(0) instanceof Integer)
                {
                	for (Object o : data)
                    {
                        WorldUtil.registerPlanetClient((Integer) o, providerIndex);
                        providerIndex++;
                        ids += ((Integer)o).toString() + " ";
                    }
                }
                else if (data.get(0) instanceof Integer[])
                {
                    for (Object o : (Integer[]) data.get(0))
                    {
                        WorldUtil.registerPlanetClient((Integer) o, providerIndex);
                        providerIndex++;
                        ids += ((Integer)o).toString() + " ";
                    }
                }
            }
            if (ConfigManagerCore.enableDebug)
            {
            	System.out.println("GC clientside planet dimensions registered: "+ids);
            	WorldProvider dimMoon = WorldUtil.getProviderForName("moon.moon");
            	if (dimMoon != null) System.out.println("Crosscheck: Moon is "+dimMoon.dimensionId);
            	WorldProvider dimMars = WorldUtil.getProviderForName("planet.mars");
            	if (dimMoon != null) System.out.println("Crosscheck: Mars is "+dimMars.dimensionId);
            	WorldProvider dimAst = WorldUtil.getProviderForName("planet.asteroids");
            	if (dimMoon != null) System.out.println("Crosscheck: Asteroids is "+dimAst.dimensionId);
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static List<Object> getSpaceStationList()
    {
        List<Object> objList = new ArrayList<Object>();
        objList.add(getSpaceStationListInts());
        return objList;
    }

    public static Integer[] getSpaceStationListInts()
    {
        Integer[] iArray = new Integer[WorldUtil.registeredSpaceStations.size()];

        for (int i = 0; i < iArray.length; i++)
        {
            iArray[i] = WorldUtil.registeredSpaceStations.get(i);
        }

        return iArray;
    }

    public static void decodeSpaceStationListClient(List<Object> data)
    {
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

            if (data.size() > 0)
            {
                if (data.get(0) instanceof Integer)
                {
                    for (Object dimID : data)
                    {
                        registerSSdim((Integer) dimID);
                    }
                }
                else if (data.get(0) instanceof Integer[])
                {
                    for (Object dimID : (Integer[]) data.get(0))
                    {
                        registerSSdim((Integer) dimID);
                    }
                }
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void registerSSdim(Integer dimID)
    {
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

    public static boolean otherModPreventGenerate(int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        if (!(world.provider instanceof IGalacticraftWorldProvider)) return false;
        if (world.provider instanceof WorldProviderOrbit) return true;
        if (ConfigManagerCore.enableOtherModsFeatures) return false;
        
        if (!generatorsInitialised)
        {
        	generatorsInitialised = true;
        			
        	try {
	        	Class GCGreg = Class.forName("bloodasp.galacticgreg.GT_Worldgenerator_Space");
	        	if (GCGreg != null)
	        	{
		        	final Field regField = Class.forName("cpw.mods.fml.common.registry.GameRegistry").getDeclaredField("worldGenerators");
		            regField.setAccessible(true);
		        	Set<IWorldGenerator> registeredGenerators = (Set<IWorldGenerator>) regField.get(null);
		        	for (IWorldGenerator gen : registeredGenerators)
		        		if (GCGreg.isInstance(gen))
		        		{
		        			generatorGCGreg = gen;
		        			break;
		        		}
	        	}
	        } catch (Exception e) { }

	        try {
	        	Class cofh = Class.forName("cofh.core.world.WorldHandler");
	        	if (cofh != null)
	        	{
		        	final Field regField = Class.forName("cpw.mods.fml.common.registry.GameRegistry").getDeclaredField("worldGenerators");
		            regField.setAccessible(true);
		        	Set<IWorldGenerator> registeredGenerators = (Set<IWorldGenerator>) regField.get(null);
		        	for (IWorldGenerator gen : registeredGenerators)
		        		if (cofh.isInstance(gen))
		        		{
		        			generatorCoFH = gen;
		        			break;
		        		}
	        	}
	        } catch (Exception e) { }

	        try {
	        	Class denseOres = Class.forName("com.rwtema.denseores.WorldGenOres");
	        	if (denseOres != null)
	        	{
		        	final Field regField = Class.forName("cpw.mods.fml.common.registry.GameRegistry").getDeclaredField("worldGenerators");
		            regField.setAccessible(true);
		        	Set<IWorldGenerator> registeredGenerators = (Set<IWorldGenerator>) regField.get(null);
		        	for (IWorldGenerator gen : registeredGenerators)
		        		if (denseOres.isInstance(gen))
		        		{
		        			generatorDenseOres = gen;
		        			break;
		        		}
	        	}
	        } catch (Exception e) { }
	        
	        try {
	        	Class ae2meteorPlace = null;
	        	try
	        	{
	        		ae2meteorPlace = Class.forName("appeng.hooks.MeteoriteWorldGen");
	        	}
	        	catch (ClassNotFoundException e) { }
	        	
	        	if (ae2meteorPlace == null)
	        	{
		        	try
		        	{
		        		ae2meteorPlace = Class.forName("appeng.worldgen.MeteoriteWorldGen");
		        	}
		        	catch (ClassNotFoundException e) {}
	        	}
	        	
	        	if (ae2meteorPlace != null)
	        	{
		        	final Field regField = Class.forName("cpw.mods.fml.common.registry.GameRegistry").getDeclaredField("worldGenerators");
		            regField.setAccessible(true);
		        	Set<IWorldGenerator> registeredGenerators = (Set<IWorldGenerator>) regField.get(null);
		        	for (IWorldGenerator gen : registeredGenerators)
		        		if (ae2meteorPlace.isInstance(gen))
		        		{
		        	        generatorAE2meteors = gen;
		        			break;
		        		}
	        	}
	        } catch (Exception e) { }
	        
	        try {
	        	Class genThaumCraft = Class.forName("thaumcraft.common.lib.world.ThaumcraftWorldGenerator");
	        	if (genThaumCraft != null)
	        	{
		        	final Field regField = Class.forName("cpw.mods.fml.common.registry.GameRegistry").getDeclaredField("worldGenerators");
		            regField.setAccessible(true);
		        	Set<IWorldGenerator> registeredGenerators = (Set<IWorldGenerator>) regField.get(null);
		        	for (IWorldGenerator gen : registeredGenerators)
		        		if (genThaumCraft.isInstance(gen))
		        		{
		        			generatorTCAuraNodes = gen;
		        			break;
		        		}
		        	if (generatorTCAuraNodes != null && ConfigManagerCore.enableThaumCraftNodes)
		        	{
		        		generateTCAuraNodes = genThaumCraft.getDeclaredMethod("generateWildNodes", World.class, Random.class, int.class, int.class, boolean.class, boolean.class);
		        		generateTCAuraNodes.setAccessible(true);
		        	}
	        	}

	        } catch (Exception e) { }
	        
	        if (generatorGCGreg != null) System.out.println("Whitelisting GalacticGreg oregen on planets.");
	        if (generatorCoFH != null) System.out.println("Whitelisting CoFHCore custom oregen on planets.");
	        if (generatorDenseOres != null) System.out.println("Whitelisting Dense Ores oregen on planets.");
	        if (generatorAE2meteors != null) System.out.println("Whitelisting AE2 meteorites worldgen on planets.");
	        if (generatorTCAuraNodes != null && generateTCAuraNodes != null) System.out.println("Whitelisting ThaumCraft aura node generation on planets.");
        }

        if (generatorGCGreg != null || generatorCoFH != null || generatorDenseOres != null || generatorTCAuraNodes != null || generatorAE2meteors != null)
        {
        	try {
	            long worldSeed = world.getSeed();
	            Random fmlRandom = new Random(worldSeed);
	            long xSeed = fmlRandom.nextLong() >> 2 + 1L;
	            long zSeed = fmlRandom.nextLong() >> 2 + 1L;
	            long chunkSeed = (xSeed * chunkX + zSeed * chunkZ) ^ worldSeed;
	            fmlRandom.setSeed(chunkSeed);
	            
	            if (generatorCoFH != null) generatorCoFH.generate(fmlRandom, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
	            if (generatorDenseOres != null) generatorDenseOres.generate(fmlRandom, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
	            if (generatorGCGreg != null) generatorGCGreg.generate(fmlRandom, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
	            if (generatorAE2meteors != null) generatorAE2meteors.generate(fmlRandom, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
	            if (generateTCAuraNodes != null)
	            {
            		generateTCAuraNodes.invoke(generatorTCAuraNodes, world, fmlRandom, chunkX, chunkZ, false, true);	            		
	            }
	            
	        } catch (Exception e) {
	        	GCLog.severe("Error in another mod's worldgen.  This is NOT a Galacticraft bug.");
	        	e.printStackTrace(); }
        }
        return true;
    }

    public static void toCelestialSelection(EntityPlayerMP player, GCPlayerStats stats, int tier)
    {
        player.mountEntity(null);
        stats.spaceshipTier = tier;

        HashMap<String, Integer> map = WorldUtil.getArrayOfPossibleDimensions(WorldUtil.getPossibleDimensionsForSpaceshipTier(tier), player);
        String dimensionList = "";
        int count = 0;
        for (Entry<String, Integer> entry : map.entrySet())
        {
        	dimensionList = dimensionList.concat(entry.getKey() + (count < map.entrySet().size() - 1 ? "?" : ""));
            count++;
        }

        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_DIMENSION_LIST, new Object[] { player.getGameProfile().getName(), dimensionList }), player);
        stats.usingPlanetSelectionGui = true;
        stats.savedPlanetList = new String(dimensionList);
        Entity fakeEntity = new EntityCelestialFake(player.worldObj, player.posX, player.posY, player.posZ, 0.0F);
        player.worldObj.spawnEntityInWorld(fakeEntity);
        player.mountEntity(fakeEntity);
    }

    public static Vector3 getFootprintPosition(World world, float rotation, Vector3 startPosition, BlockVec3 playerCenter)
    {
        Vector3 position = startPosition.clone();
        float footprintScale = 0.375F;

        int mainPosX = position.intX();
        int mainPosY = position.intY();
        int mainPosZ = position.intZ();

        // If the footprint is hovering over air...
        if (world.getBlock(mainPosX, mainPosY, mainPosZ).isAir(world, mainPosX, mainPosY, mainPosZ))
        {
            position.x += (playerCenter.x - mainPosX);
            position.z += (playerCenter.z - mainPosZ);

            // If the footprint is still over air....
            if (world.getBlock(position.intX(), position.intY(), position.intZ()).isAir(world, position.intX(), position.intY(), position.intZ()))
            {
                for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
                {
                    if (direction != ForgeDirection.DOWN && direction != ForgeDirection.UP)
                    {
                        if (!world.getBlock(mainPosX + direction.offsetX, mainPosY, mainPosZ + direction.offsetZ).isAir(world, mainPosX + direction.offsetX, mainPosY, mainPosZ + direction.offsetZ))
                        {
                            position.x += direction.offsetX;
                            position.z += direction.offsetZ;
                            break;
                        }
                    }
                }
            }
        }

        mainPosX = position.intX();
        mainPosZ = position.intZ();

        double x0 = (Math.sin((45 - rotation) * Math.PI / 180.0D) * footprintScale) + position.x;
        double x1 = (Math.sin((135 - rotation) * Math.PI / 180.0D) * footprintScale) + position.x;
        double x2 = (Math.sin((225 - rotation) * Math.PI / 180.0D) * footprintScale) + position.x;
        double x3 = (Math.sin((315 - rotation) * Math.PI / 180.0D) * footprintScale) + position.x;
        double z0 = (Math.cos((45 - rotation) * Math.PI / 180.0D) * footprintScale) + position.z;
        double z1 = (Math.cos((135 - rotation) * Math.PI / 180.0D) * footprintScale) + position.z;
        double z2 = (Math.cos((225 - rotation) * Math.PI / 180.0D) * footprintScale) + position.z;
        double z3 = (Math.cos((315 - rotation) * Math.PI / 180.0D) * footprintScale) + position.z;

        double xMin = Math.min(Math.min(x0, x1), Math.min(x2, x3));
        double xMax = Math.max(Math.max(x0, x1), Math.max(x2, x3));
        double zMin = Math.min(Math.min(z0, z1), Math.min(z2, z3));
        double zMax = Math.max(Math.max(z0, z1), Math.max(z2, z3));

        if (xMin < mainPosX)
        {
            position.x += mainPosX - xMin;
        }

        if (xMax > mainPosX + 1)
        {
            position.x -= xMax - (mainPosX + 1);
        }

        if (zMin < mainPosZ)
        {
            position.z += mainPosZ - zMin;
        }

        if (zMax > mainPosZ + 1)
        {
            position.z -= zMax - (mainPosZ + 1);
        }

        return position;
    }
}
