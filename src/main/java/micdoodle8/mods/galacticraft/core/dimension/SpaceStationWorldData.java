package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Satellite;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.UUID;

public class SpaceStationWorldData extends WorldSavedData
{
    private String spaceStationName = "NoName";
    private String owner = "NoOwner";
    private final ArrayList<UUID> allowedPlayers;
    private boolean allowAllPlayers;
    private DimensionType homePlanet;
    //    private DimensionType dimensionIdDynamic;
//    private DimensionType dimensionIdStatic;
    private DimensionType dimensionType;
    private CompoundNBT dataCompound;

    public SpaceStationWorldData(String dataName)
    {
        super(dataName);

        this.allowedPlayers = new ArrayList<UUID>();
    }

    public ArrayList<UUID> getAllowedPlayers()
    {
        return this.allowedPlayers;
    }

    public boolean getAllowedAll()
    {
        return this.allowAllPlayers;
    }

    public void putAllowedAll(boolean b)
    {
        this.allowAllPlayers = b;
        this.markDirty();
    }

    public String getOwner()
    {
        return this.owner;
    }

    public void putOwner(String name)
    {
        this.owner = name.replace(".", "");
        this.markDirty();
    }

    public String getSpaceStationName()
    {
        return this.spaceStationName;
    }

    public DimensionType getHomePlanet()
    {
        return homePlanet;
    }

    public void putSpaceStationName(String string)
    {
        this.spaceStationName = string;
    }

//    public DimensionType getDimensionIdStatic()
//    {
//        return dimensionIdStatic;
//    }
//
//    public void putDimensionIdStatic(DimensionType dimensionIdStatic)
//    {
//        this.dimensionIdStatic = dimensionIdStatic;
//    }
//
//    public DimensionType getDimensionIdDynamic()
//    {
//        return dimensionIdDynamic;
//    }
//
//    public void putDimensionIdDynamic(DimensionType dimensionIdDynamic)
//    {
//        this.dimensionIdDynamic = dimensionIdDynamic;
//    }


    public DimensionType getDimensionType()
    {
        return dimensionType;
    }

    public void setDimensionType(DimensionType dimensionType)
    {
        this.dimensionType = dimensionType;
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        this.owner = nbt.getString("owner").replace(".", "");
        this.spaceStationName = nbt.getString("spaceStationName");

        if (nbt.contains("dataCompound"))
        {
            this.dataCompound = nbt.getCompound("dataCompound");
        }
        else
        {
            this.dataCompound = new CompoundNBT();
        }

        if (nbt.contains("homePlanetRes"))
        {
            this.homePlanet = DimensionType.byName(new ResourceLocation(nbt.getString("homePlanetRes")));
        }
        else
        {
            GCLog.info("Home planet data not found in space station save file for \"" + this.spaceStationName + "\". Using default overworld.");
            this.homePlanet = DimensionType.OVERWORLD; // Overworld dimension ID
        }

//        if (nbt.contains("dimensionIdStatic"))
//        {
//            this.dimensionIdStatic = nbt.getInt("dimensionIdStatic");
//        }
//        else
//        {
//            GCLog.info("Static dimension ID not found in space station save file for \"" + this.spaceStationName + "\". Using default overworld.");
//            this.dimensionIdStatic = ConfigManagerCore.idDimensionOverworldOrbitStatic.get();
//        }
//
//        if (nbt.contains("dimensionIdDynamic"))
//        {
//            this.dimensionIdDynamic = nbt.getInt("dimensionIdDynamic");
//        }
//        else
//        {
//            GCLog.info("Dynamic dimension ID not found in space station save file for \"" + this.spaceStationName + "\". Using default overworld.");
//            this.dimensionIdDynamic = ConfigManagerCore.idDimensionOverworldOrbit.get();
//        }

        this.allowAllPlayers = nbt.getBoolean("allowedAll");

        ListNBT nbtList = nbt.getList("allowedPlayers", 10);
        this.allowedPlayers.clear();

        for (int i = 0; i < nbtList.size(); ++i)
        {
            CompoundNBT compound = nbtList.getCompound(i);
            UUID uid = compound.getUniqueId("allowedPlayer");

            if (!this.allowedPlayers.contains(uid))
            {
                this.allowedPlayers.add(uid);
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        nbt.putString("owner", this.owner);
        nbt.putString("spaceStationName", this.spaceStationName);
//        nbt.putInt("homePlanet", this.homePlanet);
//        nbt.putInt("dimensionIdDynamic", this.dimensionIdDynamic);
//        nbt.putInt("dimensionIdStatic", this.dimensionIdStatic); TODO
        nbt.put("dataCompound", this.dataCompound);
        nbt.putBoolean("allowedAll", this.allowAllPlayers);

        ListNBT nbtList = new ListNBT();

        for (int i = 0; i < this.allowedPlayers.size(); ++i)
        {
            UUID player = this.allowedPlayers.get(i);

            if (player != null)
            {
                CompoundNBT compound = new CompoundNBT();
                compound.putUniqueId("allowedPlayer", player);
                nbtList.add(compound);
            }
        }

        nbt.put("allowedPlayers", nbtList);
        return nbt;
    }

    /**
     * Retrieve an already created space station date entry
     */
    public static SpaceStationWorldData getStationData(ServerWorld world, ResourceLocation stationID, PlayerEntity player)
    {
        return getStationData(world, stationID, -1, -1, -1, player);
    }

    /**
     * Retrieve a space station data entry, creating if necessary (with provided data)
     */
    public static SpaceStationWorldData getStationData(ServerWorld world, ResourceLocation stationID, int homeID, int providerIdDynamic, int providerIdStatic, PlayerEntity owner)
    {
        DimensionType providerType = DimensionType.byName(stationID);

        boolean foundMatch = false;

        // Loop through all registered satellites, checking for a dimension ID match. If none is found, this method is
        // being called on an incorrect
        for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
        {
            if (satellite.getDimensionIdStatic() == providerType.getId() || satellite.getDimensionType() == providerType)
            {
                foundMatch = true;
                break;
            }
        }

        if (!foundMatch)
        {
            return null;
        }
        else
        {
//            final String stationIdentifier = SpaceStationWorldData.getSpaceStationID(stationID);
            String id = Constants.GCDATAFOLDER + stationID.toString();
            SpaceStationWorldData stationData = world.getSavedData().getOrCreate(() -> new SpaceStationWorldData(id), id);

//            world.putData(Constants.GCDATAFOLDER + stationIdentifier, stationData);
            stationData.dataCompound = new CompoundNBT();

//            if (owner != null)
//            {
//                stationData.owner = PlayerUtil.getName(owner).replace(".", "");
//            }

            stationData.spaceStationName = "Station: " + stationData.owner;

            if (owner != null)
            {
                stationData.allowedPlayers.add(owner.getUniqueID());
            }

//            if (homeID == -1)
//            {
//                throw new RuntimeException("Space station being created on bad home planet ID!");
//            }
//            else
//            {
//                stationData.homePlanet = homeID;
//            }

//            if (providerIdDynamic == -1 || providerIdStatic == -1)
//            {
//                throw new RuntimeException("Space station being created on bad dimension IDs!");
//            }
//            else
//            {
//                stationData.dimensionIdDynamic = providerIdDynamic;
//                stationData.dimensionIdStatic = providerIdStatic;
//            }

//            stationData.markDirty();
            return stationData;
        }

//        if (stationData.getSpaceStationName().replace(" ", "").isEmpty())
//        {
//            stationData.putSpaceStationName("Station: " + stationData.owner);
//            stationData.markDirty();
//        }

//        return stationData;
    }

//    public static SpaceStationWorldData getMPSpaceStationData(World var0, ResourceLocation var1, PlayerEntity player)
//    {
//        final String var2 = SpaceStationWorldData.getSpaceStationID(var1);
//        if (var0 == null)
//        {
//            var0 = WorldUtil.getWorldForDimensionServer(0);
//        }
//        SpaceStationWorldData var3 = null;
//
//        if (var0 != null)
//        {
//            var3 = (SpaceStationWorldData) var0.loadData(SpaceStationWorldData.class, Constants.GCDATAFOLDER + var2);
//        }
//        else
//        {
//            GCLog.severe("No world for dimension 0?  That should be unpossible!  Please report at https://github.com/micdoodle8/Galacticraft/issues/2617");
//        }
//
//        if (var3 == null)
//        {
//            var3 = new SpaceStationWorldData(var2);
//            var0.putData(Constants.GCDATAFOLDER + var2, var3);
//            var3.dataCompound = new CompoundNBT();
//
//            if (player != null)
//            {
//                var3.owner = PlayerUtil.getName(player).replace(".", "");
//            }
//
//            var3.spaceStationName = "Station: " + var3.owner;
//
//            if (player != null)
//            {
//                var3.allowedPlayers.add(PlayerUtil.getName(player));
//            }
//
//            var3.markDirty();
//        }
//
//        if (var3.getSpaceStationName().replace(" ", "").isEmpty())
//        {
//            var3.putSpaceStationName("Station: " + var3.owner);
//            var3.markDirty();
//        }
//
//        return var3;
//    } TODO ?

    public static String getSpaceStationID(DimensionType dimID)
    {
        return "spacestation_" + dimID;
    }

//    public static void updateSSOwnership(ServerPlayerEntity player, String playerName, GCPlayerStats stats, int stationID, SpaceStationWorldData stationData)
//    {
//        if (stationData == null)
//        {
//            stationData = SpaceStationWorldData.getMPSpaceStationData(null, stationID, null);
//        }
//
//        if (stationData.owner.equals(playerName))
//        {
//            //This player is the owner of the station - ensure stats data matches
//            if (!(stats.getSpaceStationDimensionData().values().contains(stationID)))
//            {
//                GCLog.debug("Player owns station: " + stationData.getSpaceStationName() + " with home planet " + stationData.getHomePlanet());
//                stats.getSpaceStationDimensionData().put(stationData.getHomePlanet(), stationID);
//            }
//        }
//        else
//        {
//            //This player is the owner of the station - remove from stats data
//            Integer savedOwned = stats.getSpaceStationDimensionData().get(stationData.getHomePlanet());
//            if (savedOwned != null && savedOwned == stationID)
//            {
//                GCLog.debug("Player does not own station: " + stationData.getSpaceStationName() + " with home planet " + stationData.getHomePlanet());
//                stats.getSpaceStationDimensionData().remove(savedOwned);
//            }
//        }
//    } TODO ?

//    public static void checkAllStations(ServerPlayerEntity thePlayer, GCPlayerStats stats)
//    {
//        String name = PlayerUtil.getName(thePlayer).replace(".", "");
//        for (int id : WorldUtil.registeredSpaceStations.keyput())
//        {
//            SpaceStationWorldData.updateSSOwnership(thePlayer, name, stats, id, null);
//        }
//    } TODO ?
}
