package micdoodle8.mods.galacticraft.core.util;

import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IAntiGrav;
import micdoodle8.mods.galacticraft.api.entity.IWorldTransferCallback;
import micdoodle8.mods.galacticraft.api.galaxies.*;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.api.world.SpaceStationType;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.SpaceStationWorldData;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.entities.EntityCelestialFake;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.ItemParaChute;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTelemetry;
import micdoodle8.mods.galacticraft.planets.venus.dimension.WorldProviderVenus;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.*;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.Map.Entry;

//import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;

public class WorldUtil
{
    public static HashMap<Integer, Integer> registeredSpaceStations;  //Dimension IDs and providers (providers are -26 or -27 by default)
    public static Map<Integer, String> dimNames = new TreeMap<>();  //Dimension IDs and provider names
    public static Map<EntityPlayerMP, HashMap<String, Integer>> celestialMapCache = new MapMaker().weakKeys().makeMap();
    public static List<Integer> registeredPlanets;

    public static float getGravityFactor(Entity entity)
    {
        if (entity.worldObj.provider instanceof IGalacticraftWorldProvider)
        {
            final IGalacticraftWorldProvider customProvider = (IGalacticraftWorldProvider) entity.worldObj.provider;
            float returnValue = MathHelper.sqrt_float(0.08F / (0.08F - customProvider.getGravity()));
            if (returnValue > 2.5F)
            {
                returnValue = 2.5F;
            }
            if (returnValue < 0.75F)
            {
                returnValue = 0.75F;
            }
            return returnValue;
        }
        else if (entity instanceof IAntiGrav)
        {
            return 1F;
        }
        else
        {
            return 1F;
        }
    }

    public static Vector3 getWorldColor(World world)
    {
        if (GalacticraftCore.isPlanetsLoaded && world.provider instanceof WorldProviderVenus)
        {
            return new Vector3(1, 0.8F, 0.6F);
        }

        return new Vector3(1, 1, 1);
    }

    public static WorldProvider getProviderForNameServer(String par1String)
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

        for (Map.Entry<Integer, String> element : WorldUtil.dimNames.entrySet())
        {
            if (nameToFind.equalsIgnoreCase(element.getValue()))
            {
                return WorldUtil.getProviderForDimensionServer(element.getKey());
            }
        }

        GCLog.info("Failed to find matching world for '" + par1String + "'");
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static WorldProvider getProviderForNameClient(String par1String)
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

        for (Map.Entry<Integer, String> element : WorldUtil.dimNames.entrySet())
        {
            if (nameToFind.equalsIgnoreCase(element.getValue()))
            {
                return WorldUtil.getProviderForDimensionClient(element.getKey());
            }
        }

        GCLog.info("Failed to find matching world for '" + par1String + "'");
        return null;
    }

    public static void initialiseDimensionNames()
    {
    	WorldProvider provider = WorldUtil.getProviderForDimensionServer(ConfigManagerCore.idDimensionOverworld);
    	WorldUtil.dimNames.put(ConfigManagerCore.idDimensionOverworld, provider.getDimensionName());
    }

    /**
     * This will *load* all the GC dimensions which the player has access to (taking account of space station permissions).
     * Loading the dimensions through Forge activates any chunk loaders or forced chunks in that dimension,
     * if the dimension was not previously loaded.  This may place load on the server.
     *
     * @param tier       - the rocket tier to test
     * @param playerBase - the player who will be riding the rocket (needed for space station permissions)
     * @return a List of integers which are the dimension IDs
     */
    public static List<Integer> getPossibleDimensionsForSpaceshipTier(int tier, EntityPlayerMP playerBase)
    {
        List<Integer> temp = new ArrayList<Integer>();

        if (!ConfigManagerCore.disableRocketsToOverworld)
        {
            temp.add(ConfigManagerCore.idDimensionOverworld);
        }

        for (Integer element : WorldUtil.registeredPlanets)
        {
            if (element == ConfigManagerCore.idDimensionOverworld)
            {
                continue;
            }
            WorldProvider provider = WorldUtil.getProviderForDimensionServer(element);

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

        for (Integer element : WorldUtil.registeredSpaceStations.keySet())
        {
            final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.worldObj, element, null);

            if (!ConfigManagerCore.spaceStationsRequirePermission || data.getAllowedAll() || data.getAllowedPlayers().contains(PlayerUtil.getName(playerBase)) || ArrayUtils.contains(playerBase.mcServer.getConfigurationManager().getOppedPlayerNames(), playerBase.getName()))
            {
                //Satellites always reachable from their own homeworld or from its other satellites
                if (playerBase != null)
                {
                    int currentWorld = playerBase.dimension;
                    //Player is on homeworld
                    if (currentWorld == data.getHomePlanet())
                    {
                        temp.add(element);
                        continue;
                    }
                    if (playerBase.worldObj.provider instanceof IOrbitDimension)
                    {
                        //Player is currently on another space station around the same planet
                        final SpaceStationWorldData dataCurrent = SpaceStationWorldData.getStationData(playerBase.worldObj, playerBase.dimension, null);
                        if (dataCurrent.getHomePlanet() == data.getHomePlanet())
                        {
                            temp.add(element);
                            continue;
                        }
                    }
                }

                //Testing dimension is a satellite, but with a different homeworld - test its tier
                WorldProvider homeWorld = WorldUtil.getProviderForDimensionServer(data.getHomePlanet());

                if (homeWorld != null)
                {
                    if (homeWorld instanceof IGalacticraftWorldProvider)
                    {
                        if (((IGalacticraftWorldProvider) homeWorld).canSpaceshipTierPass(tier))
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

    /**
     * CAUTION: this loads the dimension if it is not already loaded.  This can cause
     * server load if used too frequently or with a list of multiple dimensions.
     *
     * @param id
     * @return
     */
    public static World getWorldForDimensionServer(int id)
    {
        MinecraftServer theServer = MinecraftServer.getServer();
        if (theServer == null)
        {
            GCLog.debug("Called WorldUtil server side method but FML returned no server - is this a bug?");
            return null;
        }
        return theServer.worldServerForDimension(id);
    }

    
    /**
     * CAUTION: this loads the dimension if it is not already loaded.  This can cause
     * server load if used too frequently or with a list of multiple dimensions.
     *
     * @param id
     * @return
     */
    public static WorldProvider getProviderForDimensionServer(int id)
    {
        World ws = getWorldForDimensionServer(id);
        if (ws != null)
        {
            return ws.provider;
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static WorldProvider getProviderForDimensionClient(int id)
    {
        World ws = ClientProxyCore.mc.theWorld;
        if (ws != null && GCCoreUtil.getDimensionID(ws) == id)
        {
            return ws.provider;
        }
        return WorldProvider.getProviderForDimension(id);
    }

    /**
     * This will *load* all the GC dimensions which the player has access to (taking account of space station permissions).
     * Loading the dimensions through Forge activates any chunk loaders or forced chunks in that dimension,
     * if the dimension was not previously loaded.  This may place load on the server.
     *
     * @param tier       - the rocket tier to test
     * @param playerBase - the player who will be riding the rocket (needed for checking space station permissions)
     * @return a Map of the names of the dimension vs. the dimension IDs
     */
    public static HashMap<String, Integer> getArrayOfPossibleDimensions(int tier, EntityPlayerMP playerBase)
    {
        List<Integer> ids = WorldUtil.getPossibleDimensionsForSpaceshipTier(tier, playerBase);
        final HashMap<String, Integer> map = new HashMap<String, Integer>(ids.size(), 1F);

        for (Integer id : ids)
        {
            CelestialBody celestialBody = getReachableCelestialBodiesForDimensionID(id);

            //It's a space station
            if (id > 0 && celestialBody == null)
            {
                celestialBody = GalacticraftCore.satelliteSpaceStation;
                //This no longer checks whether a WorldProvider can be created, for performance reasons (that causes the dimension to load unnecessarily at map building stage)
                if (playerBase != null)
                {
                    final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.worldObj, id, null);
                    map.put(celestialBody.getName() + "$" + data.getOwner() + "$" + data.getSpaceStationName() + "$" + id + "$" + data.getHomePlanet(), id);
                }
            }
            else
            //It's a planet or moon
            {
                if (celestialBody == GalacticraftCore.planetOverworld)
                {
                    map.put(celestialBody.getName(), id);
                }
                else
                {
                    WorldProvider provider = WorldUtil.getProviderForDimensionServer(id);
                    if (celestialBody != null && provider != null)
                    {
                        if (provider instanceof IGalacticraftWorldProvider && !(provider instanceof IOrbitDimension) || GCCoreUtil.getDimensionID(provider) == 0)
                        {
                            map.put(celestialBody.getName(), GCCoreUtil.getDimensionID(provider));
                        }
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

        WorldUtil.celestialMapCache.put(playerBase, map);
        return map;
    }

    /**
     * Get the cached version of getArrayOfPossibleDimensions() to reduce server load + unwanted dimension loading
     * The cache will be updated every time the 'proper' version of getArrayOfPossibleDimensions is called.
     *
     * @param tier       - the rocket tier to test
     * @param playerBase - the player who will be riding the rocket (needed for checking space station permissions)
     * @return a Map of the names of the dimension vs. the dimension IDs
     */
    public static HashMap<String, Integer> getArrayOfPossibleDimensionsAgain(int tier, EntityPlayerMP playerBase)
    {
        HashMap<String, Integer> map = WorldUtil.celestialMapCache.get(playerBase);
        if (map != null)
        {
            return map;
        }
        return getArrayOfPossibleDimensions(tier, playerBase);
    }

    public static void unregisterSpaceStations()
    {
        if (WorldUtil.registeredSpaceStations != null)
        {
            for (Integer registeredID : WorldUtil.registeredSpaceStations.keySet())
            {
                DimensionManager.unregisterDimension(registeredID);
            }

            WorldUtil.registeredSpaceStations = null;
        }
    }

    public static void registerSpaceStations(MinecraftServer theServer, File spaceStationList)
    {
//        WorldUtil.registeredSpaceStations = WorldUtil.getExistingSpaceStationList(spaceStationList);
        WorldUtil.registeredSpaceStations = Maps.newHashMap();
        if (theServer == null || !spaceStationList.exists() && !spaceStationList.isDirectory())
        {
            return;
        }

        final File[] var2 = spaceStationList.listFiles();

        if (var2 != null)
        {
            for (File var5 : var2)
            {
                if (var5.getName().startsWith("spacestation_") && var5.getName().endsWith(".dat"))
                {
                    try
                    {
                        // Note: this is kind of a hacky way of doing this, loading the NBT from each space station file
                        // during dimension registration, to find out what each space station's provider IDs are.

                        String name = var5.getName();
                        SpaceStationWorldData worldDataTemp = new SpaceStationWorldData(name);
                        name = name.substring(13, name.length() - 4);
                        int registeredID = Integer.parseInt(name);

                        FileInputStream fileinputstream = new FileInputStream(var5);
                        NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);
                        fileinputstream.close();
                        worldDataTemp.readFromNBT(nbttagcompound.getCompoundTag("data"));

                        // Search for id in server-defined statically loaded dimensions
                        int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, registeredID);

                        if (!DimensionManager.isDimensionRegistered(registeredID))
                        {
                            if (id >= 0)
                            {
                                DimensionManager.registerDimension(registeredID, worldDataTemp.getDimensionIdStatic());
                                WorldUtil.registeredSpaceStations.put(registeredID, worldDataTemp.getDimensionIdStatic());
                                theServer.worldServerForDimension(registeredID);
                            }
                            else
                            {
                                DimensionManager.registerDimension(registeredID, worldDataTemp.getDimensionIdDynamic());
                                WorldUtil.registeredSpaceStations.put(registeredID, worldDataTemp.getDimensionIdDynamic());
                            }
                            WorldUtil.dimNames.put(registeredID, "Space Station " + registeredID);
                        }
                        else
                        {
                            GCLog.severe("Dimension already registered to another mod: unable to register space station dimension " + registeredID);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

//        for (Integer registeredID : WorldUtil.registeredSpaceStations)
//        {
//            int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, registeredID);
//
//            if (!DimensionManager.isDimensionRegistered(registeredID))
//            {
//	            if (id >= 0)
//	            {
//	                DimensionManager.registerDimension(registeredID, ConfigManagerCore.idDimensionOverworldOrbitStatic);
//	                theServer.worldServerForDimension(registeredID);
//                }
//	            else
//	            {
//	                DimensionManager.registerDimension(registeredID, ConfigManagerCore.idDimensionOverworldOrbit);
//	            }
//            }
//            else
//            {
//                GCLog.severe("Dimension already registered to another mod: unable to register space station dimension " + registeredID);
//            }
//        }
    }

    /**
     * Call this on FMLServerStartingEvent to register a planet which has a dimension ID.
     * Now returns a boolean to indicate whether registration was successful.
     * <p>
     * NOTE: Planets and Moons dimensions should normally be initialised at server init
     * If you do not do this, you must find your own way to register the dimension in DimensionManager
     * and you must find your own way to include the cached provider name in WorldUtil.dimNames
     * <p>
     * IMPORTANT: GalacticraftRegistry.registerProvider() must always be called in parallel with this
     * meaning the CelestialBodies are iterated in the same order when registered there and here.
     */
    public static boolean registerPlanet(int planetID, boolean initialiseDimensionAtServerInit, int defaultID)
    {
        if (WorldUtil.registeredPlanets == null)
        {
            WorldUtil.registeredPlanets = new ArrayList<Integer>();
        }

        if (initialiseDimensionAtServerInit)
        {
            if (!DimensionManager.isDimensionRegistered(planetID))
            {
                DimensionManager.registerDimension(planetID, planetID);
                GCLog.info("Registered Dimension: " + planetID);
                WorldUtil.registeredPlanets.add(planetID);
            }
            else
            {
                GCLog.severe("Dimension already registered to another mod: unable to register planet dimension " + planetID);
                //Add 0 to the list to preserve the correct order of the other planets (e.g. if server/client initialise with different dimension IDs in configs, the order becomes important for figuring out what is going on)
                WorldUtil.registeredPlanets.add(defaultID);
                return false;
            }
            World w = getWorldForDimensionServer(planetID);
            WorldUtil.dimNames.put(planetID, getDimensionName(w.provider));
            return true;
        }

        //Not to be initialised - still add to the registered planets list (for hotloading later?)
        WorldUtil.registeredPlanets.add(planetID);
        return true;
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
        WorldUtil.dimNames.clear();
    }

    /**
     * You should now use WorldUtil.registerPlanet(int planetID, boolean initialiseDimensionAtServerInit, int defaultID)
     * which returns a boolean indicating that the dimension could be successfully created (if initialiseDimensionAtServerInit is true).
     * Always returns true if if initialiseDimensionAtServerInit is false.
     *
     * @param planetID
     * @param initialiseDimensionAtServerInit
     */
    @Deprecated
    public static void registerPlanet(int planetID, boolean initialiseDimensionAtServerInit)
    {
        WorldUtil.registerPlanet(planetID, initialiseDimensionAtServerInit, 0);
    }

    public static void registerPlanetClient(Integer dimID, int providerIndex)
    {
        int providerID = GalacticraftRegistry.getProviderID(providerIndex);

        if (providerID == 0)
        {
            GCLog.severe("Server dimension " + dimID + " has no match on client due to earlier registration problem.");
        }
        else if (dimID == 0)
        {
            GCLog.severe("Client dimension " + providerID + " has no match on server - probably a server dimension ID conflict problem.");
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

    public static SpaceStationWorldData bindSpaceStationToNewDimension(World world, EntityPlayerMP player, int homePlanetID)
    {
        int dynamicProviderID = -1;
        int staticProviderID = -1;
        for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
        {
            if (satellite.getParentPlanet().getDimensionID() == homePlanetID)
            {
                dynamicProviderID = satellite.getDimensionID();
                staticProviderID = satellite.getDimensionIdStatic();
            }
        }
        if (dynamicProviderID == -1 || staticProviderID == -1)
        {
            throw new RuntimeException("Space station being bound on bad provider IDs!");
        }
        int newID = DimensionManager.getNextFreeDimId();
        SpaceStationWorldData data = WorldUtil.createSpaceStation(world, newID, homePlanetID, dynamicProviderID, staticProviderID, player);
        dimNames.put(newID, "Space Station " + newID);
        GCPlayerStats stats = GCPlayerStats.get(player);
        stats.getSpaceStationDimensionData().put(homePlanetID, newID);
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, GCCoreUtil.getDimensionID(player.worldObj), new Object[] { WorldUtil.spaceStationDataToString(stats.getSpaceStationDimensionData()) }), player);
        return data;
    }

    public static SpaceStationWorldData createSpaceStation(World world, int dimID, int homePlanetID, int dynamicProviderID, int staticProviderID, EntityPlayerMP player)
    {
        if (!DimensionManager.isDimensionRegistered(dimID))
        {
            if (ConfigManagerCore.keepLoadedNewSpaceStations)
            {
                ConfigManagerCore.setLoaded(dimID);
            }

            int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, dimID);

	        if (id >= 0)
	        {
	            DimensionManager.registerDimension(dimID, staticProviderID);
                WorldUtil.registeredSpaceStations.put(dimID, staticProviderID);
            }
            else
            {
                DimensionManager.registerDimension(dimID, dynamicProviderID);
                WorldUtil.registeredSpaceStations.put(dimID, dynamicProviderID);
            }
        }
        else
        {
            GCLog.severe("Dimension already registered to another mod: unable to register space station dimension " + dimID);
        }

        for (WorldServer server : GCCoreUtil.getWorldServerList(world))
        {
            GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_LIST, GCCoreUtil.getDimensionID(server), WorldUtil.getSpaceStationList()), GCCoreUtil.getDimensionID(server));
        }
        return SpaceStationWorldData.getStationData(world, dimID, homePlanetID, dynamicProviderID, staticProviderID, player);
    }

    public static Entity transferEntityToDimension(Entity entity, int dimensionID, WorldServer world)
    {
        return WorldUtil.transferEntityToDimension(entity, dimensionID, world, true, null);
    }

    /**
     * It is not necessary to use entity.setDead() following calling this method.
     * If the entity left the old world it was in, it will now automatically be removed from that old world before the next update tick.
     * (See WorldUtil.removeEntityFromWorld())
     */
    public static Entity transferEntityToDimension(Entity entity, int dimensionID, WorldServer world, boolean transferInv, EntityAutoRocket ridingRocket)
    {
        if (!world.isRemote)
        {
            //GalacticraftCore.packetPipeline.sendToAll(new PacketSimple(EnumSimplePacket.C_UPDATE_PLANETS_LIST, WorldUtil.getPlanetList()));

            MinecraftServer mcServer = world.getMinecraftServer();

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
        Entity otherRiddenEntity = null;
        if (entity.ridingEntity != null)
        {
            if (entity.ridingEntity instanceof EntitySpaceshipBase)
            {
                entity.mountEntity(entity.ridingEntity);
            }
            else if (entity.ridingEntity instanceof EntityCelestialFake)
            {
                entity.ridingEntity.setDead();
                entity.mountEntity(null);
            }
        	else
        	{
                otherRiddenEntity = entity.ridingEntity;
        	    entity.mountEntity(null);
        	}
        }

        boolean dimChange = entity.worldObj != worldNew;
        //Make sure the entity is added to the correct chunk in the OLD world so that it will be properly removed later if it needs to be unloaded from that world
        entity.worldObj.updateEntityWithOptionalForce(entity, false);
        EntityPlayerMP player = null;
        Vector3 spawnPos = null;
        int oldDimID = GCCoreUtil.getDimensionID(entity.worldObj);

        if (ridingRocket != null)
        {
            ArrayList<TileEntityTelemetry> tList = ridingRocket.getTelemetry();
            NBTTagCompound nbt = new NBTTagCompound();
            ridingRocket.isDead = false;
            ridingRocket.riddenByEntity = null;
            ridingRocket.writeToNBTOptional(nbt);

            ((WorldServer) ridingRocket.worldObj).getEntityTracker().untrackEntity(ridingRocket);
            removeEntityFromWorld(ridingRocket.worldObj, ridingRocket, true);

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

                GCPlayerStats stats = GCPlayerStats.get(player);
                stats.setUsingPlanetSelectionGui(false);

                player.dimension = dimID;
                if (ConfigManagerCore.enableDebug)
                {
                    GCLog.info("DEBUG: Sending respawn packet to player for dim " + dimID);
                }
                player.playerNetServerHandler.sendPacket(new S07PacketRespawn(dimID, player.worldObj.getDifficulty(), player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));

                if (worldNew.provider instanceof WorldProviderSpaceStation)
                {
                    if (WorldUtil.registeredSpaceStations.containsKey(dimID))
                    //TODO This has never been effective before due to the earlier bug - what does it actually do?
                    {
                        NBTTagCompound var2 = new NBTTagCompound();
                        SpaceStationWorldData.getStationData(worldNew, dimID, player).writeToNBT(var2);
                        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_DATA, GCCoreUtil.getDimensionID(player.worldObj), new Object[] { dimID, var2 }), player);
                    }
                }

                removeEntityFromWorld(worldOld, player, true);

                if (ridingRocket != null)
                {
                    spawnPos = new Vector3(ridingRocket);
                }
                else
                {
                    spawnPos = type.getPlayerSpawnLocation((WorldServer) worldNew, player);
                }
                forceMoveEntityToPos(entity, (WorldServer) worldNew, spawnPos, true);

                GCLog.info("Server attempting to transfer player " + PlayerUtil.getName(player) + " to dimension " + GCCoreUtil.getDimensionID(worldNew));
                if (worldNew.provider instanceof WorldProviderSpaceStation)
                {
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, GCCoreUtil.getDimensionID(worldNew), new Object[] {}), player);
                }
                player.capabilities.isFlying = false;

                player.mcServer.getConfigurationManager().preparePlayer(player, (WorldServer) worldOld);
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
            //Non-player entity transfer i.e. it's an EntityCargoRocket or an empty rocket
            {
                ArrayList<TileEntityTelemetry> tList = null;
                if (entity instanceof EntitySpaceshipBase)
                {
                    tList = ((EntitySpaceshipBase) entity).getTelemetry();
                }
                WorldUtil.removeEntityFromWorld(entity.worldObj, entity, true);

                NBTTagCompound nbt = new NBTTagCompound();
                entity.isDead = false;
                entity.writeToNBTOptional(nbt);
                entity = EntityList.createEntityFromNBT(nbt, worldNew);

                if (entity == null)
                {
                    return null;
                }

                if (entity instanceof IWorldTransferCallback)
                {
                    ((IWorldTransferCallback) entity).onWorldTransferred(worldNew);
                }

                forceMoveEntityToPos(entity, (WorldServer) worldNew, new Vector3(entity), true);

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
                stats.setUsingPlanetSelectionGui(false);

                if (ridingRocket != null)
                {
                    spawnPos = new Vector3(ridingRocket);
                }
                else
                {
                    spawnPos = type.getPlayerSpawnLocation((WorldServer) entity.worldObj, (EntityPlayerMP) entity);
                }
                forceMoveEntityToPos(entity, (WorldServer) worldNew, spawnPos, false);

                GCLog.info("Server attempting to transfer player " + PlayerUtil.getName(player) + " within same dimension " + GCCoreUtil.getDimensionID(worldNew));
                if (worldNew.provider instanceof WorldProviderSpaceStation)
                {
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, GCCoreUtil.getDimensionID(worldNew), new Object[] {}), player);
                }
                player.capabilities.isFlying = false;
            }

            //Cargo rocket does not needs its location setting here, it will do that itself
        }

        //Update PlayerStatsGC
        if (player != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            if (ridingRocket == null && type.useParachute() && stats.getExtendedInventory().getStackInSlot(4) != null && stats.getExtendedInventory().getStackInSlot(4).getItem() instanceof ItemParaChute)
            {
                GCPlayerHandler.setUsingParachute(player, stats, true);
            }
            else
            {
                GCPlayerHandler.setUsingParachute(player, stats, false);
            }

            if (stats.getRocketStacks() != null && stats.getRocketStacks().length > 0)
            {
                for (int stack = 0; stack < stats.getRocketStacks().length; stack++)
                {
                    if (transferInv)
                    {
                        if (stats.getRocketStacks()[stack] == null)
                        {
                            if (stack == stats.getRocketStacks().length - 1)
                            {
                                if (stats.getRocketItem() != null)
                                {
                                    stats.getRocketStacks()[stack] = new ItemStack(stats.getRocketItem(), 1, stats.getRocketType());
                                }
                            }
                            else if (stack == stats.getRocketStacks().length - 2)
                            {
                                stats.getRocketStacks()[stack] = stats.getLaunchpadStack();
                                stats.setLaunchpadStack(null);
                            }
                        }
                    }
                    else
                    {
                        stats.getRocketStacks()[stack] = null;
                    }
                }
            }

            if (transferInv && stats.getChestSpawnCooldown() == 0)
            {
                stats.setChestSpawnVector(type.getParaChestSpawnLocation((WorldServer) entity.worldObj, player, new Random()));
                stats.setChestSpawnCooldown(200);
            }
        }

        if (ridingRocket != null)
        {
            ridingRocket.forceSpawn = true;
            worldNew.spawnEntityInWorld(ridingRocket);
            ridingRocket.setWorld(worldNew);
            worldNew.updateEntityWithOptionalForce(ridingRocket, true);
            entity.mountEntity(ridingRocket);
            GCLog.debug("Entering rocket at : " + entity.posX + "," + entity.posZ + " rocket at: " + ridingRocket.posX + "," + ridingRocket.posZ);
        }
        else if (otherRiddenEntity != null)
        {
            if (dimChange)
            {
                World worldOld = otherRiddenEntity.worldObj;
                NBTTagCompound nbt = new NBTTagCompound();
                otherRiddenEntity.writeToNBTOptional(nbt);
                removeEntityFromWorld(worldOld, otherRiddenEntity, true);
                otherRiddenEntity = EntityList.createEntityFromNBT(nbt, worldNew);
                worldNew.spawnEntityInWorld(otherRiddenEntity);
                otherRiddenEntity.setWorld(worldNew);
            }
            otherRiddenEntity.setPositionAndRotation(entity.posX, entity.posY - 10, entity.posZ, otherRiddenEntity.rotationYaw, otherRiddenEntity.rotationPitch);
            worldNew.updateEntityWithOptionalForce(otherRiddenEntity, true);
        }

        if (entity instanceof EntityPlayerMP)
        {
            if (dimChange) FMLCommonHandler.instance().firePlayerChangedDimensionEvent((EntityPlayerMP) entity, oldDimID, dimID);
            //Spawn in a lander if appropriate
            type.onSpaceDimensionChanged(worldNew, (EntityPlayerMP) entity, ridingRocket != null);
        }

        return entity;
    }
    
    public static Entity teleportEntitySimple(World worldNew, int dimID, EntityPlayerMP player, Vector3 spawnPos)
    {
        if (player.ridingEntity != null)
        {
            player.ridingEntity.setDead();
            player.mountEntity(null);
        }

        World worldOld = player.worldObj;
        int oldDimID = GCCoreUtil.getDimensionID(worldOld);
        boolean dimChange = worldOld != worldNew;
        //Make sure the entity is added to the correct chunk in the OLD world so that it will be properly removed later if it needs to be unloaded from that world
        worldOld.updateEntityWithOptionalForce(player, false);

        if (dimChange)
        {
            player.dimension = dimID;
            if (ConfigManagerCore.enableDebug)
            {
                GCLog.info("DEBUG: Sending respawn packet to player for dim " + dimID);
            }
            player.playerNetServerHandler.sendPacket(new S07PacketRespawn(dimID, player.worldObj.getDifficulty(), player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
            if (worldNew.provider instanceof WorldProviderSpaceStation)
            {
                if (WorldUtil.registeredSpaceStations.containsKey(dimID))
                    //TODO This has never been effective before due to the earlier bug - what does it actually do?
                {
                    NBTTagCompound var2 = new NBTTagCompound();
                    SpaceStationWorldData.getStationData(worldNew, dimID, player).writeToNBT(var2);
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_DATA, GCCoreUtil.getDimensionID(player.worldObj), new Object[] { dimID, var2 }), player);
                }
            }
            removeEntityFromWorld(worldOld, player, true);
            forceMoveEntityToPos(player, (WorldServer) worldNew, spawnPos, true);
            GCLog.info("Server attempting to transfer player " + PlayerUtil.getName(player) + " to dimension " + GCCoreUtil.getDimensionID(worldNew));

            player.mcServer.getConfigurationManager().preparePlayer(player, (WorldServer) worldOld);
            player.theItemInWorldManager.setWorld((WorldServer) worldNew);
            player.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(player, (WorldServer) worldNew);
            player.mcServer.getConfigurationManager().syncPlayerInventory(player);

            for (Object o : player.getActivePotionEffects())
            {
                PotionEffect var10 = (PotionEffect) o;
                player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), var10));
            }

            player.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(player.experience, player.experienceTotal, player.experienceLevel));
            FMLCommonHandler.instance().firePlayerChangedDimensionEvent((EntityPlayerMP) player, oldDimID, dimID);
        }
        else
        {
            forceMoveEntityToPos(player, (WorldServer) worldNew, spawnPos, false);
            GCLog.info("Server attempting to transfer player " + PlayerUtil.getName(player) + " within same dimension " + GCCoreUtil.getDimensionID(worldNew));
        }
        player.capabilities.isFlying = false;
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, GCCoreUtil.getDimensionID(player.worldObj), new Object[] {}), player);

        // Update PlayerStatsGC
        GCPlayerStats stats = GCPlayerStats.get(player);
        GCPlayerHandler.setUsingParachute(player, stats, false);

        return player;
    }
    
    
    /**
     * This correctly positions an entity at spawnPos in worldNew
     * loading and adding it to the chunk as required.
     * 
     * @param entity
     * @param worldNew
     * @param spawnPos
     */
    public static void forceMoveEntityToPos(Entity entity, WorldServer worldNew, Vector3 spawnPos, boolean spawnRequired)
    {
        ChunkCoordIntPair pair = worldNew.getChunkFromChunkCoords(spawnPos.intX() >> 4, spawnPos.intZ() >> 4).getChunkCoordIntPair();
        GCLog.debug("Loading first chunk in new dimension at " + pair.chunkXPos + "," + pair.chunkZPos);
        worldNew.theChunkProviderServer.loadChunk(pair.chunkXPos, pair.chunkZPos);
        if (entity instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP) entity).playerNetServerHandler.setPlayerLocation(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
        }
        entity.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
        if (spawnRequired)
        {
            ((WorldServer) entity.worldObj).getEntityTracker().untrackEntity(entity);
            entity.forceSpawn = true;
            worldNew.spawnEntityInWorld(entity);
            entity.setWorld(worldNew);
        }
        worldNew.updateEntityWithOptionalForce(entity, true);
    }

    public static WorldServer getStartWorld(WorldServer unchanged)
    {
        if (ConfigManagerCore.challengeSpawnHandling)
        {
            WorldProvider wp = WorldUtil.getProviderForNameServer("planet.asteroids");
            WorldServer worldNew = (wp == null) ? null : (WorldServer) wp.worldObj;
            if (worldNew != null)
            {
                return worldNew;
            }
        }
        return unchanged;
    }

    @SideOnly(Side.CLIENT)
    public static EntityPlayer forceRespawnClient(int dimID, int par2, String par3, int par4)
    {
        S07PacketRespawn fakePacket = new S07PacketRespawn(dimID, EnumDifficulty.getDifficultyEnum(par2), WorldType.parseWorldType(par3), WorldSettings.GameType.getByID(par4));
        Minecraft.getMinecraft().getNetHandler().handleRespawn(fakePacket);
        return FMLClientHandler.instance().getClientPlayerEntity();
    }

    /**
     * This is similar to World.removeEntityDangerously() but without the risk of concurrent modification error 
     */
    private static void removeEntityFromWorld(World var0, Entity var1, boolean directlyRemove)
    {
        if (var1 instanceof EntityPlayer)
        {
            final EntityPlayer var2 = (EntityPlayer) var1;
            var2.closeScreen();
            var0.playerEntities.remove(var2);
            var0.updateAllPlayersSleepingFlag();
        }
        
        int i = var1.chunkCoordX;
        int j = var1.chunkCoordZ;

        if (var1.addedToChunk && var0.isBlockLoaded(new BlockPos(i << 4, 63, j << 4), true))
        {
            var0.getChunkFromChunkCoords(i, j).removeEntity(var1);
        }

        if (directlyRemove)
        {
            List<Entity> l = new ArrayList<>();
            l.add(var1);
            var0.unloadEntities(l);
            //This will automatically remove the entity from the world and the chunk prior to the world's next update entities tick
            //It is important NOT to directly modify World.loadedEntityList here, as the World will be currently iterating through that list when updating each entity (see the line "this.loadedEntityList.remove(i--);" in World.updateEntities()
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
     * This must return planets in the same order their provider IDs
     * were registered in GalacticraftRegistry by GalacticraftCore.
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

    /**
     * What's important here is that Galacticraft and the server both register
     * the same reachable Galacticraft planets (and their provider types) in the same order.
     * See WorldUtil.registerPlanet().
     * 
     * Even if there are dimension conflicts or other problems, the planets must be
     * registered in the same order on both client and server.  This should happen
     * automatically if Galacticraft versions match, and if planets modules
     * match  (including Galacticraft-Planets and any other sub-mods).
     * 
     * It is NOT a good idea for sub-mods to make the registration order of planets variable
     * or dependent on configs.
     */
    public static void decodePlanetsListClient(List<Object> data)
    {
        try
        {
            if (ConfigManagerCore.enableDebug)
            {
                GCLog.info("GC connecting to server: received planets dimension ID list.");
            }
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
                //(this will be iterating through GalacticraftRegistry.worldProviderIDs)
                int providerIndex = GalaxyRegistry.getRegisteredSatellites().size() * 2;
                if (data.get(0) instanceof Integer)
                {
                    for (Object o : data)
                    {
                        WorldUtil.registerPlanetClient((Integer) o, providerIndex);
                        providerIndex++;
                        ids += ((Integer) o).toString() + " ";
                    }
                }
                else if (data.get(0) instanceof Integer[])
                {
                    for (Object o : (Integer[]) data.get(0))
                    {
                        WorldUtil.registerPlanetClient((Integer) o, providerIndex);
                        providerIndex++;
                        ids += ((Integer) o).toString() + " ";
                    }
                }
            }
            if (ConfigManagerCore.enableDebug)
            {
                GCLog.debug("GC clientside planet dimensions registered: " + ids);
                WorldProvider dimMoon = WorldUtil.getProviderForNameClient("moon.moon");
                if (dimMoon != null)
                {
                    GCLog.debug("Crosscheck: Moon is " + GCCoreUtil.getDimensionID(dimMoon));
                }
                WorldProvider dimMars = WorldUtil.getProviderForNameClient("planet.mars");
                if (dimMars != null)
                {
                    GCLog.debug("Crosscheck: Mars is " + GCCoreUtil.getDimensionID(dimMars));
                }
                WorldProvider dimAst = WorldUtil.getProviderForNameClient("planet.asteroids");
                if (dimAst != null)
                {
                    GCLog.debug("Crosscheck: Asteroids is " + GCCoreUtil.getDimensionID(dimAst));
                }
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
        Integer[] iArray = new Integer[WorldUtil.registeredSpaceStations.size() * 2];

        int i = 0;
        for (Map.Entry<Integer, Integer> e : WorldUtil.registeredSpaceStations.entrySet())
        {
            iArray[i] = e.getKey();
            iArray[i + 1] = e.getValue();
            i += 2;
        }

//        for (int i = 0; i < iArray.length; i++)
//        {
//            iArray[i] = WorldUtil.registeredSpaceStations.get(i);
//        }

        return iArray;
    }

    public static void decodeSpaceStationListClient(List<Object> data)
    {
        try
        {
            if (WorldUtil.registeredSpaceStations != null)
            {
                for (Integer registeredID : WorldUtil.registeredSpaceStations.keySet())
                {
                    DimensionManager.unregisterDimension(registeredID);
                }
            }
            WorldUtil.registeredSpaceStations = Maps.newHashMap();

            if (data.size() > 0)
            {
                if (data.get(0) instanceof Integer)
                {
                    for (int i = 0; i < data.size(); i += 2)
                    {
                        registerSSdim((Integer) data.get(i), (Integer) data.get(i + 1));
                    }
//                    for (Object dimID : data)
//                    {
//                        registerSSdim((Integer) dimID);
//                    }
                }
                else if (data.get(0) instanceof Integer[])
                {
                    Integer[] array = ((Integer[]) data.get(0));
                    for (int i = 0; i < array.length; i += 2)
                    {
                        registerSSdim(array[i], array[i + 1]);
                    }
//                    for (Object dimID : (Integer[]) data.get(0))
//                    {
//                        registerSSdim((Integer) dimID);
//                    }
                }
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void registerSSdim(Integer dimID, Integer providerKey)
    {
        if (!WorldUtil.registeredSpaceStations.containsKey(dimID))
        {
            if (!DimensionManager.isDimensionRegistered(dimID))
            {
                WorldUtil.registeredSpaceStations.put(dimID, providerKey);
                DimensionManager.registerDimension(dimID, providerKey);
            }
            else
            {
                GCLog.severe("Dimension already registered on client: unable to register space station dimension " + dimID);
            }
        }
    }

    public static void toCelestialSelection(EntityPlayerMP player, GCPlayerStats stats, int tier)
    {
        player.mountEntity(null);
        stats.setSpaceshipTier(tier);

        HashMap<String, Integer> map = WorldUtil.getArrayOfPossibleDimensions(tier, player);
        String dimensionList = "";
        int count = 0;
        for (Entry<String, Integer> entry : map.entrySet())
        {
            dimensionList = dimensionList.concat(entry.getKey() + (count < map.entrySet().size() - 1 ? "?" : ""));
            count++;
        }

        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_DIMENSION_LIST, GCCoreUtil.getDimensionID(player.worldObj), new Object[] { PlayerUtil.getName(player), dimensionList }), player);
        stats.setUsingPlanetSelectionGui(true);
        stats.setSavedPlanetList(dimensionList);
        Entity fakeEntity = new EntityCelestialFake(player.worldObj, player.posX, player.posY, player.posZ);
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
        BlockPos posMain = new BlockPos(mainPosX, mainPosY, mainPosZ);

        // If the footprint is hovering over air...
        if (world.getBlockState(posMain).getBlock().isAir(world, posMain))
        {
            position.x += (playerCenter.x - mainPosX);
            position.z += (playerCenter.z - mainPosZ);

            BlockPos pos1 = new BlockPos(position.intX(), position.intY(), position.intZ());
            // If the footprint is still over air....
            Block b2 = world.getBlockState(pos1).getBlock();
            if (b2 != null && b2.isAir(world, pos1))
            {
                for (EnumFacing direction : EnumFacing.VALUES)
                {
                    BlockPos offsetPos = posMain.offset(direction);
                    if (direction != EnumFacing.DOWN && direction != EnumFacing.UP)
                    {
                        if (!world.getBlockState(offsetPos).getBlock().isAir(world, offsetPos))
                        {
                            position.x += direction.getFrontOffsetX();
                            position.z += direction.getFrontOffsetZ();
                            break;
                        }
                    }
                }
            }
        }

        mainPosX = position.intX();
        mainPosZ = position.intZ();

        double x0 = (Math.sin((45 - rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale) + position.x;
        double x1 = (Math.sin((135 - rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale) + position.x;
        double x2 = (Math.sin((225 - rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale) + position.x;
        double x3 = (Math.sin((315 - rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale) + position.x;
        double z0 = (Math.cos((45 - rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale) + position.z;
        double z1 = (Math.cos((135 - rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale) + position.z;
        double z2 = (Math.cos((225 - rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale) + position.z;
        double z3 = (Math.cos((315 - rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale) + position.z;

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

    public static String spaceStationDataToString(HashMap<Integer, Integer> data)
    {
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<Integer, Integer>> it = data.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<Integer, Integer> e = it.next();
            builder.append(e.getKey());
            builder.append("$");
            builder.append(e.getValue());
            if (it.hasNext())
            {
                builder.append("?");
            }
        }
        return builder.toString();
    }

    public static HashMap<Integer, Integer> stringToSpaceStationData(String input)
    {
        HashMap<Integer, Integer> data = Maps.newHashMap();
        if (!input.isEmpty())
        {
            String[] str0 = input.split("\\?");
            for (int i = 0; i < str0.length; ++i)
            {
                String[] str1 = str0[i].split("\\$");
                data.put(Integer.parseInt(str1[0]), Integer.parseInt(str1[1]));
            }
        }
        return data;
    }

    public static String getDimensionName(WorldProvider wp)
    {
        if (wp instanceof IGalacticraftWorldProvider)
        {
            CelestialBody cb = ((IGalacticraftWorldProvider) wp).getCelestialBody();
            if (cb != null && !(cb instanceof Satellite))
            {
                return cb.getUnlocalizedName();
            }
        }

        if (GCCoreUtil.getDimensionID(wp) == ConfigManagerCore.idDimensionOverworld)
        {
            return "Overworld";
        }

        return wp.getDimensionName();
    }

    public static Map<String, List<String>> getAllChecklistKeys()
    {
        Map<String, List<String>> checklistMap = Maps.newHashMap();

        for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
        {
            if (planet.getReachable())
            {
                checklistMap.put(planet.getUnlocalizedName(), planet.getChecklistKeys());
            }
        }

        for (Moon moon : GalaxyRegistry.getRegisteredMoons().values())
        {
            if (moon.getReachable())
            {
                checklistMap.put(moon.getUnlocalizedName(), moon.getChecklistKeys());
            }
        }

        for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
        {
            if (satellite.getReachable())
            {
                checklistMap.put(satellite.getUnlocalizedName(), satellite.getChecklistKeys());
            }
        }

        return checklistMap;
    }

    public static void markAdjacentPadForUpdate(World worldIn, BlockPos pos)
    {
        BlockPos offsetPos;
        for (int dX = -2; dX <= 2; dX++)
        {
            for (int dZ = -2; dZ <= 2; dZ++)
            {
                offsetPos = pos.add(dX, 0, dZ);
                final Block block = worldIn.getBlockState(offsetPos).getBlock();

                if (block == GCBlocks.landingPadFull)
                {
                    worldIn.markBlockForUpdate(offsetPos);
                }
            }
        }
    }

    public static void setNextMorning(WorldServer world)
    {
        if (world.provider instanceof WorldProviderSpace)
        {
            long current = ((WorldProviderSpace)world.provider).preTickTime;
            long dayLength = ((WorldProviderSpace)world.provider).getDayLength();
            if (dayLength <= 0) return;
            world.setWorldTime(current - current % dayLength + dayLength);
        }
        else
        {
            long newTime = world.getWorldTime();
            for (WorldServer worldServer : GCCoreUtil.getWorldServerList(world))
            {
                if (worldServer == world) continue;
                if (worldServer.provider instanceof WorldProviderSpace)
                {
                    ((WorldProviderSpace)worldServer.provider).adjustTime(newTime);
                }
            }
        }
    }
}
