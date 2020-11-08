package micdoodle8.mods.galacticraft.core.util;

import com.google.common.collect.*;
import io.netty.buffer.Unpooled;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IAntiGrav;
import micdoodle8.mods.galacticraft.api.galaxies.*;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.DimensionSpace;
import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.api.world.SpaceStationType;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.GCDimensions;
import micdoodle8.mods.galacticraft.core.dimension.SpaceStationWorldData;
import micdoodle8.mods.galacticraft.core.entities.EntityCelestialFake;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SRespawnPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.permission.PermissionAPI;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.Map.Entry;

//import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;

public class WorldUtil
{
    public static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, Constants.MOD_ID_CORE);
    //    public static HashMap<DimensionType, DimensionType> registeredSpaceStations;  //Dimension IDs and providers (providers are -26 or -27 by default)
    public static HashSet<DimensionType> registeredSpaceStations = new HashSet<>();
    //    public static Map<DimensionType, ResourceLocation> dimNames = new TreeMap<>();  //Dimension IDs and dimension names
    public static Map<ServerPlayerEntity, HashMap<String, DimensionType>> celestialMapCache = new MapMaker().weakKeys().makeMap();
    public static List<DimensionType> registeredPlanets;

//    public static DimensionType MOON_DIMENSION;
////    public static final RegistryObject<ModDimension> MOON_MOD_DIMENSION = register("moon", WorldUtil::moonFactory);
////    public static final RegistryObject<ModDimension> SPACE_STATION_MOD_DIMENSION = register("space_station_overworld", WorldUtil::spaceStationFactory);
//
//    private static ModDimension moonFactory()
//    {
//        return new ModDimension()
//        {
//            @Override
//            public BiFunction<World, DimensionType, ? extends Dimension> getFactory()
//            {
//                return DimensionMoon::new;
//            }
//        };
//    }
//
//    private static ModDimension spaceStationFactory()
//    {
//        return new ModDimension()
//        {
//            @Override
//            public BiFunction<World, DimensionType, ? extends Dimension> getFactory()
//            {
//                return DimensionOverworldOrbit::new;
//            }
//        };
//    }
//
//    private static RegistryObject<ModDimension> register(final String name, final Supplier<ModDimension> sup)
//    {
//        return DIMENSIONS.register(name, sup);
//    }

//    @Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE)
//    public static class EventDimensionType
//    {
//        @SubscribeEvent
//        public static void onModDimensionRegister(final RegisterDimensionsEvent event)
//        {
//            ResourceLocation id = new ResourceLocation(Constants.MOD_ID_CORE, "moon");
//            if (DimensionType.byName(id) == null)
//            {
//                MOON_DIMENSION = DimensionManager.registerDimension(id, MOON_MOD_DIMENSION.get(), new PacketBuffer(Unpooled.buffer()), true);
//                MOON_DIMENSION.setRegistryName(id);
//                DimensionManager.keepLoaded(MOON_DIMENSION, false);
//            }
//            else
//            {
//                MOON_DIMENSION = DimensionType.byName(id);
//            }
//        }
//    }

    public static ResourceLocation getSpaceStationRes(PlayerEntity owner)
    {
        return getSpaceStationRes(owner.getUniqueID());
    }

    public static ResourceLocation getSpaceStationRes(UUID owner)
    {
        return new ResourceLocation(Constants.SS_PREFiX + owner);
    }

    public static boolean doesSpaceStationExist(PlayerEntity owner)
    {
        ResourceLocation id = getSpaceStationRes(owner);
        return DimensionType.byName(id) != null;
    }

    public static DimensionType createNewSpaceStation(UUID owner, boolean keepLoaded)
    {
        DimensionType type;
        ResourceLocation id = getSpaceStationRes(owner);
        if (DimensionType.byName(id) == null)
        {
            type = DimensionManager.registerDimension(id, GCDimensions.SPACE_STATION_MOD_DIMENSION, new PacketBuffer(Unpooled.buffer()), true);
//            type.setRegistryName(id);
            DimensionManager.keepLoaded(type, keepLoaded);
            WorldUtil.registeredSpaceStations.add(type);
            return type;
        }
        throw new RuntimeException("Space station already exists! " + id);
    }

    public static float getGravityFactor(Entity entity)
    {
        if (entity.world.getDimension() instanceof IGalacticraftDimension)
        {
            final IGalacticraftDimension customProvider = (IGalacticraftDimension) entity.world.getDimension();
            float returnValue = MathHelper.sqrt(0.08F / (0.08F - customProvider.getGravity()));
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
//        if (GalacticraftCore.isPlanetsLoaded && world.getDimension() instanceof WorldProviderVenus)
//        {
//            return new Vector3(1, 0.8F, 0.6F);
//        } TODO Planets

        return new Vector3(1, 1, 1);
    }

//    public static Dimension getProviderForNameServer(String search)
//    {
//        String nameToFind = search;
//        if (search.contains("$"))
//        {
//            final String[] twoDimensions = search.split("\\$");
//            nameToFind = twoDimensions[0];
//        }
//        if (nameToFind == null)
//        {
//            return null;
//        }
//
//        for (Map.Entry<DimensionType, String> element : WorldUtil.dimNames.entrySet())
//        {
//            if (nameToFind.equalsIgnoreCase(element.getValue()))
//            {
//                return WorldUtil.getProviderForDimensionServer(element.getKey());
//            }
//        }
//
//        GCLog.info("Failed to find matching world for '" + search + "'");
//        return null;
//    }

//    @OnlyIn(Dist.CLIENT)
//    public static Dimension getProviderForNameClient(String par1String)
//    {
//        String nameToFind = par1String;
//        if (par1String.contains("$"))
//        {
//            final String[] twoDimensions = par1String.split("\\$");
//            nameToFind = twoDimensions[0];
//        }
//        if (nameToFind == null)
//        {
//            return null;
//        }
//
//        for (Map.Entry<DimensionType, String> element : WorldUtil.dimNames.entrySet())
//        {
//            if (nameToFind.equalsIgnoreCase(element.getValue()))
//            {
//                return WorldUtil.getProviderForDimensionClient(element.getKey());
//            }
//        }
//
//        GCLog.info("Failed to find matching world for '" + par1String + "'");
//        return null;
//    }

//    public static void initialiseDimensionNames()
//    {
//    	Dimension dimension = WorldUtil.getProviderForDimensionServer(DimensionType.OVERWORLD);
//    	WorldUtil.dimNames.put(DimensionType.OVERWORLD, dimension.getType().getRegistryName());
//    }

    /**
     * This will *load* all the GC dimensions which the player has access to (taking account of space station permissions).
     * Loading the dimensions through Forge activates any chunk loaders or forced chunks in that dimension,
     * if the dimension was not previously loaded.  This may place load on the server.
     *
     * @param tier       - the rocket tier to test
     * @param playerBase - the player who will be riding the rocket (needed for space station permissions)
     * @return a List of integers which are the dimension IDs
     */
    public static List<DimensionType> getPossibleDimensionsForSpaceshipTier(int tier, ServerPlayerEntity playerBase)
    {
        List<DimensionType> temp = new ArrayList<>();

        if (!ConfigManagerCore.disableRocketsToOverworld.get())
        {
            temp.add(DimensionType.OVERWORLD);
        }

        for (DimensionType element : WorldUtil.registeredPlanets)
        {
            if (element == DimensionType.OVERWORLD)
            {
                continue;
            }

            CelestialBody body = GalaxyRegistry.getCelestialBodyFromDimensionID(element);
            if (body != null && body.getReachable() && body.getTierRequirement() <= tier)
            {
                temp.add(element);
            }

//            Dimension dimension = WorldUtil.getProviderForDimensionServer(element);
//
//            if (dimension != null)
//            {
//                if (dimension instanceof IGalacticraftDimension)
//                {
//                    if (((IGalacticraftDimension) dimension).canSpaceshipTierPass(tier))
//                    {
//                        temp.add(element);
//                    }
//                }
//                else
//                {
//                    temp.add(element);
//                }
//            }
        }

        for (DimensionType element : WorldUtil.registeredSpaceStations)
        {
            final SpaceStationWorldData data = SpaceStationWorldData.getStationData((ServerWorld) playerBase.world, element.getRegistryName(), null, null);

            if (!ConfigManagerCore.spaceStationsRequirePermission.get() || data.getAllowedAll() || data.getAllowedPlayers().contains(playerBase.getUniqueID()) || ArrayUtils.contains(playerBase.server.getPlayerList().getOppedPlayerNames(), playerBase.getName()))
            {
                //Satellites always reachable from their own homeworld or from its other satellites
                if (playerBase != null)
                {
                    DimensionType currentWorld = playerBase.dimension;
                    //Player is on homeworld
                    if (currentWorld == data.getHomePlanet())
                    {
                        temp.add(element);
                        continue;
                    }
                    if (playerBase.world.getDimension() instanceof IOrbitDimension)
                    {
                        //Player is currently on another space station around the same planet
                        final SpaceStationWorldData dataCurrent = SpaceStationWorldData.getStationData((ServerWorld) playerBase.world, playerBase.dimension.getRegistryName(), null, null);
                        if (dataCurrent.getHomePlanet() == data.getHomePlanet())
                        {
                            temp.add(element);
                            continue;
                        }
                    }
                }

                //Testing dimension is a satellite, but with a different homeworld - test its tier
                Dimension homeWorld = WorldUtil.getProviderForDimensionServer(data.getHomePlanet());

                if (homeWorld != null)
                {
                    if (homeWorld instanceof IGalacticraftDimension)
                    {
                        if (((IGalacticraftDimension) homeWorld).canSpaceshipTierPass(tier))
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

    public static CelestialBody getReachableCelestialBodiesForDimensionID(DimensionType id)
    {
        List<CelestialBody> celestialBodyList = Lists.newArrayList();
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredSatellites().values());

        for (CelestialBody cBody : celestialBodyList)
        {
            if (cBody.getReachable())
            {
                if (cBody.getDimensionType() == id)
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
    public static World getWorldForDimensionServer(DimensionType id)
    {
        MinecraftServer theServer = GCCoreUtil.getServer();
        if (theServer == null)
        {
            GCLog.debug("Called WorldUtil server LogicalSide method but FML returned no server - is this a bug?");
            return null;
        }
        return theServer.getWorld(id);
    }

    /**
     * CAUTION: this loads the dimension if it is not already loaded.  This can cause
     * server load if used too frequently or with a list of multiple dimensions.
     *
     * @param id
     * @return
     */
    public static Dimension getProviderForDimensionServer(DimensionType id)
    {
        World ws = getWorldForDimensionServer(id);
        if (ws != null)
        {
            return ws.dimension;
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public static Dimension getProviderForDimensionClient(DimensionType id)
    {
        World ws = ClientProxyCore.mc.world;
        if (ws != null && GCCoreUtil.getDimensionType(ws) == id)
        {
            return ws.dimension;
        }
        return null;
//        return DimensionManager.createProviderFor(id);
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
    public static HashMap<String, DimensionType> getArrayOfPossibleDimensions(int tier, ServerPlayerEntity playerBase)
    {
        List<DimensionType> ids = WorldUtil.getPossibleDimensionsForSpaceshipTier(tier, playerBase);
        final HashMap<String, DimensionType> map = new HashMap<>(ids.size(), 1F);

        for (DimensionType id : ids)
        {
            CelestialBody celestialBody = getReachableCelestialBodiesForDimensionID(id);

            //It's a space station
            if (id.getRegistryName().getPath().contains("spacestation") && celestialBody == null)
            {
                celestialBody = GalacticraftCore.satelliteSpaceStation;
                //This no longer checks whether a WorldProvider can be created, for performance reasons (that causes the dimension to load unnecessarily at map building stage)
                if (playerBase != null)
                {
                    final SpaceStationWorldData data = SpaceStationWorldData.getStationData((ServerWorld) playerBase.world, id.getRegistryName(), null, null);
                    map.put(celestialBody.getName() + "$" + data.getOwner() + "$" + data.getSpaceStationName() + "$" + id.getRegistryName().toString() + "$" + data.getHomePlanet().getRegistryName().toString(), id);
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
//                    Dimension dimension = WorldUtil.getProviderForDimensionServer(id);
                    if (celestialBody != null /*&& dimension != null*/)
                    {
//                        if (dimension instanceof IGalacticraftDimension && !(dimension instanceof IOrbitDimension) || GCCoreUtil.getDimensionType(dimension) == DimensionType.OVERWORLD)
//                        {
                            map.put(celestialBody.getName(), celestialBody.getDimensionType() /*GCCoreUtil.getDimensionType(dimension)*/);
//                        }
                    }
                }
            }
        }

        ArrayList<CelestialBody> cBodyList = new ArrayList<>();
        cBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
        cBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());

        for (CelestialBody body : cBodyList)
        {
            if (!body.getReachable())
            {
                map.put(body.getLocalizedName() + "*", body.getDimensionType());
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
    public static HashMap<String, DimensionType> getArrayOfPossibleDimensionsAgain(int tier, ServerPlayerEntity playerBase)
    {
        HashMap<String, DimensionType> map = WorldUtil.celestialMapCache.get(playerBase);
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
            for (DimensionType registeredID : WorldUtil.registeredSpaceStations)
            {
                DimensionManager.unregisterDimension(registeredID.getId());
            }

            WorldUtil.registeredSpaceStations = null;
        }
    }

    public static void registerSpaceStations(MinecraftServer theServer, File spaceStationList)
    {
//        WorldUtil.registeredSpaceStations = WorldUtil.getExistingSpaceStationList(spaceStationList);
        WorldUtil.registeredSpaceStations = Sets.newHashSet();
        if (theServer == null || !spaceStationList.exists() && !spaceStationList.isDirectory())
        {
            return;
        }

        final File[] var2 = spaceStationList.listFiles();

        if (var2 != null)
        {
            for (File var5 : var2)
            {
                if (var5.getName().startsWith(Constants.SS_PREFiX) && var5.getName().endsWith(".dat"))
                {
                    try
                    {
                        // Note: this is kind of a hacky way of doing this, loading the NBT from each space station file
                        // during dimension registration, to find out what each space station's dimension IDs are.

                        String name = var5.getName();
                        String id = name;
                        SpaceStationWorldData worldDataTemp = new SpaceStationWorldData(id);
                        name = name.substring(Constants.SS_PREFiX.length(), name.length() - 4);
                        UUID ownerID = UUID.fromString(name);
                        ResourceLocation registeredID = getSpaceStationRes(ownerID);
//                        int registeredID = Integer.parseInt(name);

                        FileInputStream fileinputstream = new FileInputStream(var5);
                        CompoundNBT nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);
                        fileinputstream.close();
                        worldDataTemp.read(nbttagcompound.getCompound("data"));

                        // Search for id in server-defined statically loaded dimensions
                        int index = Collections.binarySearch(ConfigManagerCore.staticLoadDimensions.get(), registeredID.toString());

//                        DimensionType providerID = index >= 0 ? worldDataTemp.getDimensionIdStatic() : worldDataTemp.getDimensionIdDynamic();
                        if (DimensionType.byName(registeredID) == null)
                        {
                            createNewSpaceStation(ownerID, false);
                        }
                        DimensionType type = DimensionType.byName(registeredID);
                        WorldUtil.registeredSpaceStations.add(type);
                        if (index >= 0) // Keep loaded
                        {
                            theServer.getWorld(type);
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
//            int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions.get(), registeredID);
//
//            if (!DimensionManager.isDimensionRegistered(registeredID))
//            {
//	            if (id >= 0)
//	            {
//	                DimensionManager.registerDimension(registeredID, ConfigManagerCore.idDimensionOverworldOrbitStatic.get());
//	                theServer.getWorld(registeredID);
//                }
//	            else
//	            {
//	                DimensionManager.registerDimension(registeredID, ConfigManagerCore.idDimensionOverworldOrbit.get());
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
     * and you must find your own way to include the cached dimension name in WorldUtil.dimNames
     * <p>
     * IMPORTANT: GalacticraftRegistry.registerDimension() must always be called in parallel with this
     * meaning the CelestialBodies are iterated in the same order when registered there and here.
     * <p>
     * The defaultID should be 0, and the id should be both a dimension ID and a DimensionType id.
     */
    public static boolean registerPlanet(DimensionType id)
    {
        if (WorldUtil.registeredPlanets == null)
        {
            WorldUtil.registeredPlanets = new ArrayList<>();
        }

        if (id == null)
        {
            throw new RuntimeException("Trying to register null dimension type as a planet!");
        }

        WorldUtil.registeredPlanets.add(id);
        return true;
    }

    public static void unregisterPlanets()
    {
        if (WorldUtil.registeredPlanets != null)
        {
            for (DimensionType var1 : WorldUtil.registeredPlanets)
            {
                DimensionManager.unregisterDimension(var1.getId());
                GCLog.info("Unregistered Dimension: " + var1);
            }

            WorldUtil.registeredPlanets = null;
        }
//        WorldUtil.dimNames.clear();
    }

//    public static void registerPlanetClient(Integer dimID, int providerIndex)
//    {
//        DimensionType typeID = GalacticraftRegistry.getDimensionTypeID(providerIndex);
//
//        if (typeID == 0)
//        {
//            GCLog.severe("Server dimension " + dimID + " has no match on client due to earlier registration problem.");
//        }
//        else if (dimID == 0)
//        {
//            GCLog.severe("Client dimension " + providerIndex + " has no match on server - probably a server dimension ID conflict problem.");
//        }
//        else
//
//        {
//            if (!WorldUtil.registeredPlanets.contains(dimID))
//            {
//                WorldUtil.registeredPlanets.add(dimID);
//                DimensionManager.registerDimension(dimID, WorldUtil.getDimensionTypeById(typeID));
//            }
//            else
//            {
//                GCLog.severe("Dimension already registered to another mod: unable to register planet dimension " + dimID);
//            }
//        }
//    }

//    public static SpaceStationWorldData bindSpaceStationToNewDimension(World world, ServerPlayerEntity player, DimensionType homePlanetID)
//    {
//        DimensionType dynamicProviderID = null;
//        int staticProviderID = -1;
//        for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
//        {
//            if (satellite.getParentPlanet().getDimensionID() == homePlanetID)
//            {
//                dynamicProviderID = satellite.getDimensionID();
//                staticProviderID = satellite.getDimensionIdStatic();
//            }
//        }
//        if (dynamicProviderID == null || staticProviderID == -1)
//        {
//            throw new RuntimeException("Space station being bound on bad dimension IDs!");
//        }
//        int newID = DimensionManager.getNextFreeDimId();
//        SpaceStationWorldData data = WorldUtil.createSpaceStation(world, newID, homePlanetID, dynamicProviderID, staticProviderID, player);
//        dimNames.put(newID, "Space Station " + newID);
//        GCPlayerStats stats = GCPlayerStats.get(player);
//        stats.getSpaceStationDimensionData().put(homePlanetID, newID);
//        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, GCCoreUtil.getDimensionID(player.world), new Object[] { WorldUtil.spaceStationDataToString(stats.getSpaceStationDimensionData()) }), player);
//        return data;
//    }

//    public static SpaceStationWorldData createSpaceStation(ServerWorld world, DimensionType dimID, int homePlanetID, int dynamicProviderID, int staticProviderID, ServerPlayerEntity player)
//    {
//        if (!DimensionManager.isDimensionRegistered(dimID))
//        {
//            if (ConfigManagerCore.keepLoadedNewSpaceStations.get())
//            {
//                ConfigManagerCore.setLoaded.get()(dimID);
//            }
//
//            int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions.get(), dimID);
//
//	        if (id >= 0)
//	        {
//                DimensionManager.registerDimension(dimID, WorldUtil.getDimensionTypeById(staticProviderID));
//                WorldUtil.registeredSpaceStations.add(dimID);
//            }
//            else
//            {
//                DimensionManager.registerDimension(dimID, WorldUtil.getDimensionTypeById(dynamicProviderID));
//                WorldUtil.registeredSpaceStations.add(dimID);
//            }
//        }
//        else
//        {
//            GCLog.severe("Dimension already registered to another mod: unable to register space station dimension " + dimID);
//        }
//
//        for (ServerWorld server : world.getServer().getWorlds())
//        {
//            GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_LIST, GCCoreUtil.getDimensionID(server), WorldUtil.getSpaceStationList()), GCCoreUtil.getDimensionID(server));
//        }
//        return SpaceStationWorldData.getStationData(world, dimID.getRegistryName(), homePlanetID, dynamicProviderID, staticProviderID, player);
//    }

    public static Entity transferEntityToDimension(Entity entity, DimensionType dimensionID, ServerWorld world)
    {
        return WorldUtil.transferEntityToDimension(entity, dimensionID, world, true, null);
    }

    /**
     * It is not necessary to use entity.remove() following calling this method.
     * If the entity left the old world it was in, it will now automatically be removed from that old world before the next update tick.
     * (See WorldUtil.removeEntityFromWorld())
     */
    public static Entity transferEntityToDimension(Entity entity, DimensionType dimensionID, ServerWorld world, boolean transferInv, EntityAutoRocket ridingRocket)
    {
        if (!world.isRemote)
        {
            //GalacticraftCore.packetPipeline.sendToAll(new PacketSimple(EnumSimplePacket.C_UPDATE_PLANETS_LIST, WorldUtil.getPlanetList()));

            MinecraftServer mcServer = world.getServer();

            if (mcServer != null)
            {
                final ServerWorld var6 = mcServer.getWorld(dimensionID);

                if (var6 == null)
                {
                    System.err.println("Cannot Transfer Entity to Dimension: Could not get World for Dimension " + dimensionID);
                    return null;
                }

                final ITeleportType type = GalacticraftRegistry.getTeleportTypeForDimension(var6.dimension.getClass());

                if (type != null)
                {
                    return WorldUtil.teleportEntity(var6, entity, dimensionID, type, transferInv, ridingRocket);
                }
            }
        }

        return null;
    }

    private static Entity teleportEntity(ServerWorld worldNew, Entity entity, DimensionType dimID, ITeleportType type, boolean transferInv, EntityAutoRocket ridingRocket)
    {
//        Entity otherRiddenEntity = null;
//        if (entity.getRidingEntity() != null)
//        {
//            if (entity.getRidingEntity() instanceof EntitySpaceshipBase)
//            {
//                entity.startRiding(entity.getRidingEntity());
//            }
//            else if (entity.getRidingEntity() instanceof EntityCelestialFake)
//            {
//                Entity e = entity.getRidingEntity();
//                e.removePassengers();
//                e.remove();
//            }
//        	else
//        	{
//                otherRiddenEntity = entity.getRidingEntity();
//        	    entity.stopRiding();
//        	}
//        }
//
//        boolean dimChange = entity.world != worldNew;
//        //Make sure the entity is added to the correct chunk in the OLD world so that it will be properly removed later if it needs to be unloaded from that world
//        entity.world.updateEntityWithOptionalForce(entity, false);
//        ServerPlayerEntity player = null;
//        Vector3 spawnPos = null;
//        int oldDimID = GCCoreUtil.getDimensionID(entity.world);
//
//        if (ridingRocket != null)
//        {
//            ArrayList<TileEntityTelemetry> tList = ridingRocket.getTelemetry();
//            CompoundNBT nbt = new CompoundNBT();
//            ridingRocket.isDead = false;
//            ridingRocket.removePassengers();
//            ridingRocket.writeToNBTOptional(nbt);
//
//            ((ServerWorld) ridingRocket.world).getEntityTracker().untrack(ridingRocket);
//            removeEntityFromWorld(ridingRocket.world, ridingRocket, true);
//
//            ridingRocket = (EntityAutoRocket) EntityList.createEntityFromNBT(nbt, worldNew);
//
//            if (ridingRocket != null)
//            {
//                ridingRocket.setWaitForPlayer(true);
//
//                if (ridingRocket instanceof IWorldTransferCallback)
//                {
//                    ((IWorldTransferCallback) ridingRocket).onWorldTransferred(worldNew);
//                }
//            }
//        }
//
//        if (dimChange)
//        {
//            if (entity instanceof ServerPlayerEntity)
//            {
//                player = (ServerPlayerEntity) entity;
//                World worldOld = player.world;
//
//                GCPlayerStats stats = GCPlayerStats.get(player);
//                stats.setUsingPlanetSelectionGui(false);
//
//                player.dimension = dimID;
//                if (ConfigManagerCore.enableDebug.get())
//                {
//                    GCLog.info("DEBUG: Sending respawn packet to player for dim " + dimID);
//                }
//                player.connection.sendPacket(new SRespawnPacket(dimID, worldNew.getDifficulty(), worldNew.getWorldInfo().getGenerator(), player.interactionManager.getGameType()));
//                player.server.getPlayerList().updatePermissionLevel(player);
//                if (worldNew.dimension instanceof DimensionSpaceStation)
//                {
//                    if (WorldUtil.registeredSpaceStations.containsKey(dimID))
//                    //TODO This has never been effective before due to the earlier bug - what does it actually do?
//                    {
//                        CompoundNBT var2 = new CompoundNBT();
//                        SpaceStationWorldData.getStationData(worldNew, dimID, player).writeToNBT(var2);
//                        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_DATA, GCCoreUtil.getDimensionID(player.world), new Object[] { dimID, var2 }), player);
//                    }
//                }
//
//                removeEntityFromWorld(worldOld, player, true);
//
//                if (ridingRocket != null)
//                {
//                    spawnPos = new Vector3(ridingRocket);
//                }
//                else
//                {
//                    spawnPos = type.getPlayerSpawnLocation((ServerWorld) worldNew, player);
//                }
//                forceMoveEntityToPos(entity, (ServerWorld) worldNew, spawnPos, true);
//
//                GCLog.info("Server attempting to transfer player " + PlayerUtil.getName(player) + " to dimension " + GCCoreUtil.getDimensionID(worldNew));
//                if (worldNew.dimension instanceof DimensionSpaceStation)
//                {
//                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, GCCoreUtil.getDimensionID(worldNew), new Object[] {}), player);
//                }
//                player.abilities.isFlying = false;
//
//                player.server.getPlayerList().preparePlayer(player, (ServerWorld) worldOld);
//                player.interactionManager.setWorld((ServerWorld) worldNew);
//                player.connection.sendPacket(new SPlayerAbilitiesPacket(player.abilities));
//                player.server.getPlayerList().updateTimeAndWeatherForPlayer(player, (ServerWorld) worldNew);
//                player.server.getPlayerList().syncPlayerInventory(player);
//
//                for (Object o : player.getActivePotionEffects())
//                {
//                    EffectInstance var10 = (EffectInstance) o;
//                    player.connection.sendPacket(new SPlayEntityEffectPacket(player.getEntityId(), var10));
//                }
//
//                player.connection.sendPacket(new SSetExperiencePacket(player.experience, player.experienceTotal, player.experienceLevel));
//            }
//            else
//            //Non-player entity transfer i.e. it's an EntityCargoRocket or an empty rocket
//            {
//                ArrayList<TileEntityTelemetry> tList = null;
//                if (entity instanceof EntitySpaceshipBase)
//                {
//                    tList = ((EntitySpaceshipBase) entity).getTelemetry();
//                }
//                WorldUtil.removeEntityFromWorld(entity.world, entity, true);
//
//                CompoundNBT nbt = new CompoundNBT();
//                entity.isDead = false;
//                entity.writeToNBTOptional(nbt);
//                entity = EntityList.createEntityFromNBT(nbt, worldNew);
//
//                if (entity == null)
//                {
//                    return null;
//                }
//
//                if (entity instanceof IWorldTransferCallback)
//                {
//                    ((IWorldTransferCallback) entity).onWorldTransferred(worldNew);
//                }
//
//                forceMoveEntityToPos(entity, (ServerWorld) worldNew, new Vector3(entity), true);
//
//                if (tList != null && tList.size() > 0)
//                {
//                    for (TileEntityTelemetry t : tList)
//                    {
//                        t.addTrackedEntity(entity);
//                    }
//                }
//            }
//        }
//        else
//        {
//            //Same dimension player transfer
//            if (entity instanceof ServerPlayerEntity)
//            {
//                player = (ServerPlayerEntity) entity;
//                player.closeScreen();
//                GCPlayerStats stats = GCPlayerStats.get(player);
//                stats.setUsingPlanetSelectionGui(false);
//
//                if (ridingRocket != null)
//                {
//                    spawnPos = new Vector3(ridingRocket);
//                }
//                else
//                {
//                    spawnPos = type.getPlayerSpawnLocation((ServerWorld) entity.world, (ServerPlayerEntity) entity);
//                }
//                forceMoveEntityToPos(entity, (ServerWorld) worldNew, spawnPos, false);
//
//                GCLog.info("Server attempting to transfer player " + PlayerUtil.getName(player) + " within same dimension " + GCCoreUtil.getDimensionID(worldNew));
//                if (worldNew.dimension instanceof DimensionSpaceStation)
//                {
//                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, GCCoreUtil.getDimensionID(worldNew), new Object[] {}), player);
//                }
//                player.abilities.isFlying = false;
//            }
//
//            //Cargo rocket does not needs its location setting here, it will do that itself
//        }
//
//        //Update PlayerStatsGC
//        if (player != null)
//        {
//            GCPlayerStats stats = GCPlayerStats.get(player);
//            if (ridingRocket == null && type.useParachute() && stats.getExtendedInventory().getStackInSlot(4) != null && stats.getExtendedInventory().getStackInSlot(4).getItem() instanceof ItemParaChute)
//            {
//                GCPlayerHandler.setUsingParachute(player, stats, true);
//            }
//            else
//            {
//                GCPlayerHandler.setUsingParachute(player, stats, false);
//            }
//
//            if (stats.getRocketStacks() != null && !stats.getRocketStacks().isEmpty())
//            {
//                for (int stack = 0; stack < stats.getRocketStacks().size(); stack++)
//                {
//                    if (transferInv)
//                    {
//                        if (stats.getRocketStacks().get(stack).isEmpty())
//                        {
//                            if (stack == stats.getRocketStacks().size() - 1)
//                            {
//                                if (stats.getRocketItem() != null)
//                                {
//                                    stats.getRocketStacks().set(stack, new ItemStack(stats.getRocketItem(), 1, stats.getRocketType()));
//                                }
//                            }
//                            else if (stack == stats.getRocketStacks().size() - 2)
//                            {
//                                ItemStack launchpad = stats.getLaunchpadStack();
//                                stats.getRocketStacks().set(stack, launchpad == null ? ItemStack.EMPTY : launchpad);
//                                stats.setLaunchpadStack(null);
//                            }
//                        }
//                    }
//                    else
//                    {
//                        stats.getRocketStacks().set(stack, ItemStack.EMPTY);
//                    }
//                }
//            }
//
//            if (transferInv && stats.getChestSpawnCooldown() == 0)
//            {
//                stats.setChestSpawnVector(type.getParaChestSpawnLocation((ServerWorld) entity.world, player, new Random()));
//                stats.setChestSpawnCooldown(200);
//            }
//        }
//
//        if (ridingRocket != null)
//        {
//            boolean previous = CompatibilityManager.forceLoadChunks((ServerWorld) worldNew);
//            ridingRocket.forceSpawn = true;
//            worldNew.addEntity(ridingRocket);
//            ridingRocket.setWorld(worldNew);
//            worldNew.updateEntityWithOptionalForce(ridingRocket, true);
//            CompatibilityManager.forceLoadChunksEnd((ServerWorld) worldNew, previous);
//            entity.startRiding(ridingRocket);
//            GCLog.debug("Entering rocket at : " + entity.posX + "," + entity.getPosZ() + " rocket at: " + ridingRocket.posX + "," + ridingRocket.posZ);
//        }
//        else if (otherRiddenEntity != null)
//        {
//            if (dimChange)
//            {
//                World worldOld = otherRiddenEntity.world;
//                CompoundNBT nbt = new CompoundNBT();
//                otherRiddenEntity.writeToNBTOptional(nbt);
//                removeEntityFromWorld(worldOld, otherRiddenEntity, true);
//                otherRiddenEntity = EntityList.createEntityFromNBT(nbt, worldNew);
//                worldNew.addEntity(otherRiddenEntity);
//                otherRiddenEntity.setWorld(worldNew);
//            }
//            otherRiddenEntity.setPositionAndRotation(entity.posX, entity.posY - 10, entity.getPosZ(), otherRiddenEntity.rotationYaw, otherRiddenEntity.rotationPitch);
//            worldNew.updateEntityWithOptionalForce(otherRiddenEntity, true);
//        }
//
//        if (entity instanceof ServerPlayerEntity)
//        {
//            if (dimChange) FMLCommonHandler.instance().firePlayerChangedDimensionEvent((ServerPlayerEntity) entity, oldDimID, dimID);
//
//            //Spawn in a lander if appropriate
//            type.onSpaceDimensionChanged(worldNew, (ServerPlayerEntity) entity, ridingRocket != null);
//        }

        Vector3D spawnLocation;
        float yaw = entity.rotationYaw;
        float pitch = entity.rotationPitch;
        if (ridingRocket != null)
        {
            spawnLocation = new Vector3D(ridingRocket);
        }
        else
        {
            spawnLocation = type.getPlayerSpawnLocation((ServerWorld) entity.world, (ServerPlayerEntity) entity);
        }

        if (entity instanceof ServerPlayerEntity)
        {
            ChunkPos chunkpos = new ChunkPos(spawnLocation.toBlockPos());
            worldNew.getChunkProvider().registerTicket(TicketType.POST_TELEPORT, chunkpos, 1, entity.getEntityId());
            entity.stopRiding();
            if (((ServerPlayerEntity) entity).isSleeping())
            {
//                ((ServerPlayerEntity) entity).wakeUpPlayer(true, true, false);
                ((ServerPlayerEntity) entity).wakeUp();
            }

            if (worldNew == entity.world)
            {
                ((ServerPlayerEntity) entity).connection.setPlayerLocation(spawnLocation.x, spawnLocation.y, spawnLocation.z, yaw, pitch, Collections.emptySet());
            }
            else
            {
                ((ServerPlayerEntity) entity).teleport(worldNew, spawnLocation.x, spawnLocation.y, spawnLocation.z, yaw, pitch);
            }

            entity.setRotationYawHead(yaw);
        }
        else
        {
            float f1 = MathHelper.wrapDegrees(yaw);
            float f = MathHelper.wrapDegrees(pitch);
            f = MathHelper.clamp(f, -90.0F, 90.0F);
            if (worldNew == entity.world)
            {
                entity.setLocationAndAngles(spawnLocation.x, spawnLocation.y, spawnLocation.z, f1, f);
                entity.setRotationYawHead(f1);
            }
            else
            {
                entity.detach();
                entity.dimension = worldNew.dimension.getType();
                Entity entityOld = entity;
                entity = entity.getType().create(worldNew);
                if (entity == null)
                {
                    return entity;
                }

                entity.copyDataFromOld(entityOld);
                entity.setLocationAndAngles(spawnLocation.x, spawnLocation.y, spawnLocation.z, f1, f);
                entity.setRotationYawHead(f1);
                worldNew.addFromAnotherDimension(entity); // Special "summon" method
            }
        }

//        if (facing != null) {
//            facing.updateLook(source, entity);
//        }

        if (!(entity instanceof LivingEntity) || !((LivingEntity) entity).isElytraFlying())
        {
            entity.setMotion(entity.getMotion().mul(1.0D, 0.0D, 1.0D));
            entity.onGround = true;
        }

        return entity;
    }

//    public static Entity teleportEntitySimple(World worldNew, DimensionType dimID, ServerPlayerEntity player, Vector3 spawnPos)
//    {
//        if (player.getRidingEntity() != null)
//        {
//            player.getRidingEntity().remove();
//            player.stopRiding();
//        }
//
//        World worldOld = player.world;
//        DimensionType oldDimID = GCCoreUtil.getDimensionID(worldOld);
//        boolean dimChange = worldOld != worldNew;
//        //Make sure the entity is added to the correct chunk in the OLD world so that it will be properly removed later if it needs to be unloaded from that world
//        worldOld.updateEntityWithOptionalForce(player, false);
//
//        if (dimChange)
//        {
//            player.dimension = dimID;
//            if (ConfigManagerCore.enableDebug.get())
//            {
//                GCLog.info("DEBUG: Sending respawn packet to player for dim " + dimID);
//            }
//            player.connection.sendPacket(new SRespawnPacket(dimID, player.world.getDifficulty(), player.world.getWorldInfo().getGenerator(), player.interactionManager.getGameType()));
//            if (worldNew.dimension instanceof DimensionSpaceStation)
//            {
//                if (WorldUtil.registeredSpaceStations.containsKey(dimID))
//                    //TODO This has never been effective before due to the earlier bug - what does it actually do?
//                {
//                    CompoundNBT var2 = new CompoundNBT();
//                    SpaceStationWorldData.getStationData(worldNew, dimID, player).writeToNBT(var2);
//                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_DATA, GCCoreUtil.getDimensionID(player.world), new Object[] { dimID, var2 }), player);
//                }
//            }
//            removeEntityFromWorld(worldOld, player, true);
//            forceMoveEntityToPos(player, (ServerWorld) worldNew, spawnPos, true);
//            GCLog.info("Server attempting to transfer player " + PlayerUtil.getName(player) + " to dimension " + GCCoreUtil.getDimensionID(worldNew));
//
//            player.server.getPlayerList().preparePlayer(player, (ServerWorld) worldOld);
//            player.interactionManager.setWorld((ServerWorld) worldNew);
//            player.server.getPlayerList().updateTimeAndWeatherForPlayer(player, (ServerWorld) worldNew);
//            player.server.getPlayerList().syncPlayerInventory(player);
//
//            for (Object o : player.getActivePotionEffects())
//            {
//                EffectInstance var10 = (EffectInstance) o;
//                player.connection.sendPacket(new SPlayEntityEffectPacket(player.getEntityId(), var10));
//            }
//
//            player.connection.sendPacket(new SSetExperiencePacket(player.experience, player.experienceTotal, player.experienceLevel));
//            BasicEventHooks.firePlayerChangedDimensionEvent((ServerPlayerEntity) player, oldDimID, dimID);
//        }
//        else
//        {
//            forceMoveEntityToPos(player, (ServerWorld) worldNew, spawnPos, false);
//            GCLog.info("Server attempting to transfer player " + PlayerUtil.getName(player) + " within same dimension " + GCCoreUtil.getDimensionID(worldNew));
//        }
//        player.abilities.isFlying = false;
//        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, GCCoreUtil.getDimensionID(player.world), new Object[] {}), player);
//
//        // Update PlayerStatsGC
//        GCPlayerStats stats = GCPlayerStats.get(player);
//        GCPlayerHandler.setUsingParachute(player, stats, false);
//
//        return player;
//    }


    /**
     * This correctly positions an entity at spawnPos in worldNew
     * loading and adding it to the chunk as required.
     */
//    public static void forceMoveEntityToPos(Entity entity, ServerWorld worldNew, Vector3 spawnPos, boolean spawnRequired)
//    {
//        boolean previous = CompatibilityManager.forceLoadChunks(worldNew);
//        ChunkPos pair = worldNew.getChunk(spawnPos.intX() >> 4, spawnPos.intZ() >> 4).getPos();
//        GCLog.debug("Loading first chunk in new dimension at " + pair.x + "," + pair.z);
//        worldNew.getChunkProvider().loadChunk(pair.x, pair.z);
//        entity.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
//        ServerWorld fromWorld = ((ServerWorld) entity.world);
//        if (spawnRequired)
//        {
//            ((ServerWorld) entity.world).getEntityTracker().untrack(entity);
//            entity.forceSpawn = true;
//            worldNew.addEntity(entity);
//            entity.setWorld(worldNew);
//        }
//        worldNew.updateEntityWithOptionalForce(entity, true);
//        if (entity instanceof ServerPlayerEntity)
//        {
//            ((ServerPlayerEntity) entity).connection.setPlayerLocation(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
//        }
//        CompatibilityManager.forceLoadChunksEnd(worldNew, previous);
//    }
    public static ServerWorld getStartWorld(ServerWorld unchanged)
    {
        if (ConfigManagerCore.challengeSpawnHandling)
        {
//            Dimension wp = WorldUtil.getProviderForNameServer("planet.asteroids"); TODO Planets
//            ServerWorld worldNew = (wp == null) ? null : (ServerWorld) wp.getWorld();
//            if (worldNew != null)
//            {
//                return worldNew;
//            }
        }
        return unchanged;
    }

    @OnlyIn(Dist.CLIENT)
    public static PlayerEntity forceRespawnClient(DimensionType dimID, WorldType worldType, GameType gameType)
    {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        SRespawnPacket fakePacket = new SRespawnPacket(dimID, WorldInfo.byHashing(player.world.getWorldInfo().getSeed()), worldType, gameType);
        player.connection.handleRespawn(fakePacket);
        return player;
    }

    /**
     * This is similar to World.removeEntityDangerously() but without the risk of concurrent modification error
     */
//    private static void removeEntityFromWorld(World var0, Entity var1, boolean directlyRemove)
//    {
//        if (var1 instanceof PlayerEntity)
//        {
//            final PlayerEntity var2 = (PlayerEntity) var1;
//            var2.closeScreen();
//            var0.playerEntities.remove(var2);
//            var0.updateAllPlayersSleepingFlag();
//        }
//
//        int i = var1.chunkCoordX;
//        int j = var1.chunkCoordZ;
//
//        if (var1.addedToChunk && var0.isBlockLoaded(new BlockPos(i << 4, 63, j << 4), true))
//        {
//            var0.getChunkFromChunkCoords(i, j).removeEntity(var1);
//        }
//
//        if (directlyRemove)
//        {
//            List<Entity> l = new ArrayList<>();
//            l.add(var1);
//            var0.unloadEntities(l);
//            //This will automatically remove the entity from the world and the chunk prior to the world's next update entities tick
//            //It is important NOT to directly modify World.loadedEntityList here, as the World will be currently iterating through that list when updating each entity (see the line "this.loadedEntityList.remove(i--);" in World.updateEntities()
//        }
//
//        var1.isDead = false;
//    }
    public static SpaceStationRecipe getSpaceStationRecipe(DimensionType planetID)
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
     * This must return planets in the same order their dimension IDs
     * were registered in GalacticraftRegistry by GalacticraftCore.
     */
    public static List<DimensionType> getPlanetList()
    {
//        List<Object> objList = new ArrayList<>();
//        objList.add(getPlanetListInts());
//        return objList;
        return registeredPlanets;
    }

    public static List<DimensionType> getPlanetListInts()
    {
        return WorldUtil.registeredPlanets;
//        return WorldUtil.registeredPlanets.toArray(new DimensionType[registeredPlanets.size()]);
//        DimensionType[] iArray = new DimensionType[WorldUtil.registeredPlanets.size()];
//
//        for (int i = 0; i < iArray.length; i++)
//        {
//            iArray[i] = WorldUtil.registeredPlanets.get(i);
//        }
//
//        return iArray;
    }

    /**
     * What's important here is that Galacticraft and the server both register
     * the same reachable Galacticraft planets (and their dimension types) in the same order.
     * See WorldUtil.registerPlanet().
     * <p>
     * Even if there are dimension conflicts or other problems, the planets must be
     * registered in the same order on both client and server.  This should happen
     * automatically if Galacticraft versions match, and if planets modules
     * match  (including Galacticraft-Planets and any other sub-mods).
     * <p>
     * It is NOT a good idea for sub-mods to make the registration order of planets variable
     * or dependent on configs.
     */
    public static void decodePlanetsListClient(List<Object> data)
    {
        try
        {
            if (ConfigManagerCore.enableDebug.get())
            {
                GCLog.info("GC connecting to server: received planets dimension ID list.");
            }
//            if (WorldUtil.registeredPlanets != null)
//            {
//                for (DimensionType registeredType : WorldUtil.registeredPlanets)
//                {
//                    if (DimensionType.isDimensionRegistered(registeredType))
//                    {
//                        DimensionManager.unregisterDimension(registeredType);
//                    }
//                }
//            }
            WorldUtil.registeredPlanets = new ArrayList<>();

            String ids = "";
            if (data.size() > 0)
            {
                //Start the dimension index at offset 2 to skip the two Overworld Orbit dimensions
                //(this will be iterating through GalacticraftRegistry.worldProviderIDs)
                int providerIndex = GalaxyRegistry.getRegisteredSatellites().size() * 2;
                if (data.get(0) instanceof Integer)
                {
                    for (Object o : data)
                    {
                        WorldUtil.registerPlanet(DimensionType.getById((Integer)o));
//                        WorldUtil.registerPlanetClient((Integer) o, providerIndex);
                        providerIndex++;
                        ids += ((Integer) o).toString() + " ";
                    }
                }
                else if (data.get(0) instanceof Integer[])
                {
                    for (Object o : (Integer[]) data.get(0))
                    {
                        WorldUtil.registerPlanet(DimensionType.getById((Integer)o));
//                        WorldUtil.registerPlanetClient((Integer) o, providerIndex);
                        providerIndex++;
                        ids += ((Integer) o).toString() + " ";
                    }
                }
            }
            if (ConfigManagerCore.enableDebug.get())
            {
                GCLog.debug("GC clientside planet dimensions registered: " + ids);
//                Dimension dimMoon = WorldUtil.getProviderForNameClient("moon.moon");
//                if (dimMoon != null)
//                {
//                    GCLog.debug("Crosscheck: Moon is " + GCCoreUtil.getDimensionID(dimMoon));
//                }
//                Dimension dimMars = WorldUtil.getProviderForNameClient("planet.mars");
//                if (dimMars != null)
//                {
//                    GCLog.debug("Crosscheck: Mars is " + GCCoreUtil.getDimensionID(dimMars));
//                }
//                Dimension dimAst = WorldUtil.getProviderForNameClient("planet.asteroids");
//                if (dimAst != null)
//                {
//                    GCLog.debug("Crosscheck: Asteroids is " + GCCoreUtil.getDimensionID(dimAst));
//                }
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

//    public static List<Object> getSpaceStationList()
//    {
//        List<Object> objList = new ArrayList<>();
//        objList.add(getSpaceStationListInts());
//        return objList;
//    }
//
    public static Set<DimensionType> getSpaceStationListInts()
    {
//        DimensionType[] iArray = new DimensionType[.size() * 2];
//
//        int i = 0;
//        for (DimensionType e : WorldUtil.registeredSpaceStations)
//        {
//            iArray[i] = e.getKey();
//            iArray[i + 1] = e.getValue();
//            i += 2;
//        }
//
////        for (int i = 0; i < iArray.length; i++)
////        {
////            iArray[i] = WorldUtil.registeredSpaceStations.get(i);
////        }
        return WorldUtil.registeredSpaceStations;
    }

    public static void decodeSpaceStationListClient(List<Object> data)
    {
        try
        {
            if (WorldUtil.registeredSpaceStations != null)
            {
                for (DimensionType registeredID : WorldUtil.registeredSpaceStations)
                {
                    DimensionManager.unregisterDimension(registeredID.getId());
                }
            }
            WorldUtil.registeredSpaceStations = Sets.newHashSet();

            if (data.size() > 0)
            {
//                if (data.get(0) instanceof Integer)
//                {
//                    for (int i = 0; i < data.size(); i += 2)
//                    {
//                        registerSSdim((Integer) data.get(i), (Integer) data.get(i + 1));
//                    }
////                    for (Object dimID : data)
////                    {
////                        registerSSdim((Integer) dimID);
////                    }
//                }
//                else if (data.get(0) instanceof Integer[])
//                {
//                    Integer[] array = ((Integer[]) data.get(0));
//                    for (int i = 0; i < array.length; i += 2)
//                    {
//                        registerSSdim(array[i], array[i + 1]);
//                    }
////                    for (Object dimID : (Integer[]) data.get(0))
////                    {
////                        registerSSdim((Integer) dimID);
////                    }
//                } TODO Client SS Syncing
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

//    private static void registerSSdim(DimensionType dimID, Integer providerKey)
//    {
//        if (!WorldUtil.registeredSpaceStations.contains(dimID))
//        {
//            if (!DimensionManager.isDimensionRegistered(dimID))
//            {
//                WorldUtil.registeredSpaceStations.put(dimID, providerKey);
//                DimensionManager.registerDimension(dimID, WorldUtil.getDimensionTypeById(providerKey));
//            }
//            else
//            {
//                GCLog.severe("Dimension already registered on client: unable to register space station dimension " + dimID);
//            }
//        }
//    }

    public static void toCelestialSelection(ServerPlayerEntity player, GCPlayerStats stats, int tier)
    {
        player.stopRiding();
        stats.setSpaceshipTier(tier);

        HashMap<String, DimensionType> map = WorldUtil.getArrayOfPossibleDimensions(tier, player);
        String dimensionList = "";
        int count = 0;
        for (Entry<String, DimensionType> entry : map.entrySet())
        {
            dimensionList = dimensionList.concat(entry.getKey() + (count < map.entrySet().size() - 1 ? "?" : ""));
            count++;
        }

        boolean canCreateStations = PermissionAPI.hasPermission(player, Constants.PERMISSION_CREATE_STATION);
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_DIMENSION_LIST, GCCoreUtil.getDimensionType(player.world), new Object[]{PlayerUtil.getName(player), dimensionList, canCreateStations}), player);
        stats.setUsingPlanetSelectionGui(true);
        stats.setSavedPlanetList(dimensionList);
        Entity fakeEntity = new EntityCelestialFake(player.world, player.getPosX(), player.getPosY(), player.getPosZ());
        player.world.addEntity(fakeEntity);
        player.startRiding(fakeEntity);
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
        if (world.getBlockState(posMain).getBlock().isAir(world.getBlockState(posMain), world, posMain))
        {
            position.x += (playerCenter.x - mainPosX);
            position.z += (playerCenter.z - mainPosZ);

            BlockPos pos1 = new BlockPos(position.intX(), position.intY(), position.intZ());
            // If the footprint is still over air....
            Block b2 = world.getBlockState(pos1).getBlock();
            if (b2 != null && b2.isAir(world.getBlockState(pos1), world, pos1))
            {
                for (Direction direction : Direction.values())
                {
                    BlockPos offsetPos = posMain.offset(direction);
                    if (direction != Direction.DOWN && direction != Direction.UP)
                    {
                        if (!world.getBlockState(offsetPos).getBlock().isAir(world.getBlockState(offsetPos), world, offsetPos))
                        {
                            position.x += direction.getXOffset();
                            position.z += direction.getZOffset();
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

    public static String spaceStationDataToString(HashMap<DimensionType, DimensionType> data)
    {
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<DimensionType, DimensionType>> it = data.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<DimensionType, DimensionType> e = it.next();
            builder.append(e.getKey().getId());
            builder.append("$");
            builder.append(e.getValue().getId());
            if (it.hasNext())
            {
                builder.append("?");
            }
        }
        return builder.toString();
    }

    public static HashMap<DimensionType, DimensionType> stringToSpaceStationData(String input)
    {
        HashMap<DimensionType, DimensionType> data = Maps.newHashMap();
        if (!input.isEmpty())
        {
            String[] str0 = input.split("\\?");
            for (int i = 0; i < str0.length; ++i)
            {
                String[] str1 = str0[i].split("\\$");
                data.put(DimensionType.getById(Integer.parseInt(str1[0])), DimensionType.getById(Integer.parseInt(str1[1])));
            }
        }
        return data;
    }

    public static String getDimensionName(Dimension wp)
    {
        if (wp instanceof IGalacticraftDimension)
        {
            CelestialBody cb = ((IGalacticraftDimension) wp).getCelestialBody();
            if (cb != null && !(cb instanceof Satellite))
            {
                return cb.getUnlocalizedName();
            }
        }

        if (GCCoreUtil.getDimensionType(wp) == DimensionType.OVERWORLD)
        {
            return "overworld";
        }

        return DimensionType.getKey(wp.getType()).toString();
    }

    private static void insertChecklistEntries(CelestialBody body, List<CelestialBody> bodiesDone, List<List<String>> checklistValues)
    {
        if (body.getReachable())
        {
            int insertPos = 0;
            for (CelestialBody prevBody : bodiesDone)
            {
                if (body.getTierRequirement() >= prevBody.getTierRequirement())
                {
                    insertPos++;
                }
            }
            List<String> checklist = Lists.newArrayList();
            checklist.add(body.getUnlocalizedName());
            checklist.addAll(body.getChecklistKeys());
            checklistValues.add(insertPos, checklist);
            bodiesDone.add(body);
        }
    }

    public static List<List<String>> getAllChecklistKeys()
    {
        List<List<String>> checklistValues = Lists.newArrayList();
        List<CelestialBody> bodiesDone = Lists.newArrayList();

        for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
        {
            insertChecklistEntries(planet, bodiesDone, checklistValues);
        }

        for (Moon moon : GalaxyRegistry.getRegisteredMoons().values())
        {
            insertChecklistEntries(moon, bodiesDone, checklistValues);
        }

        for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
        {
            insertChecklistEntries(satellite, bodiesDone, checklistValues);
        }

        return checklistValues;
    }

    public static Dimension getDimensionTypeById(DimensionType id)
    {
//        for (Dimension dimensiontype : Dimension.values())
//        {
//            if (dimensiontype.getId() == id)
//            {
//                return dimensiontype;
//            }
//        }

        GCLog.severe("There was a problem getting WorldProvider type " + id);
        GCLog.severe("(possibly this is a conflict, check Galacticraft config.)");
        return null;
    }

    public static void markAdjacentPadForUpdate(World worldIn, BlockPos pos)
    {
        BlockPos offsetPos;
        for (int dX = -2; dX <= 2; dX++)
        {
            for (int dZ = -2; dZ <= 2; dZ++)
            {
                offsetPos = pos.add(dX, 0, dZ);
                final BlockState blockState = worldIn.getBlockState(offsetPos);

                if (blockState.getBlock() == GCBlocks.landingPadFull)
                {
                    worldIn.notifyBlockUpdate(offsetPos, blockState, blockState, 3);
                }
            }
        }
    }

    public static void setNextMorning(ServerWorld world)
    {
        if (world.getDimension() instanceof DimensionSpace)
        {
            long current = ((DimensionSpace) world.getDimension()).preTickTime;
            long dayLength = ((DimensionSpace) world.getDimension()).getDayLength();
            if (dayLength <= 0)
            {
                return;
            }
            world.setDayTime(current - current % dayLength + dayLength);
        }
        else
        {
            long newTime = world.getDayTime();
            for (ServerWorld worldServer : GCCoreUtil.getWorldServerList(world))
            {
                if (worldServer == world)
                {
                    continue;
                }
                if (worldServer.dimension instanceof DimensionSpace)
                {
                    ((DimensionSpace) worldServer.dimension).adjustTime(newTime);
                }
            }
        }
    }
}
