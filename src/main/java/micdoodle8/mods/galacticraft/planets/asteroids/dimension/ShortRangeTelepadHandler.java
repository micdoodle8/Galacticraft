package micdoodle8.mods.galacticraft.planets.asteroids.dimension;

import com.google.common.collect.Maps;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.planets.asteroids.tick.AsteroidsTickHandlerServer;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldSavedData;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Map;

public class ShortRangeTelepadHandler extends WorldSavedData
{
    public static final String saveDataID = "ShortRangeTelepads";
    private static final Map<Integer, TelepadEntry> tileMap = Maps.newHashMap();

    public ShortRangeTelepadHandler(String saveDataID)
    {
        super(saveDataID);
    }

    public static class TelepadEntry
    {
        public DimensionType dimensionID;
        public BlockVec3 position;
        public boolean enabled;

        public TelepadEntry(DimensionType dimID, BlockVec3 position, boolean enabled)
        {
            this.dimensionID = dimID;
            this.position = position;
            this.enabled = enabled;
        }

        @Override
        public int hashCode()
        {
            return new HashCodeBuilder().append(dimensionID).append(position.hashCode()).append(enabled).toHashCode();
        }

        @Override
        public boolean equals(Object other)
        {
            if (other instanceof TelepadEntry)
            {
                return new EqualsBuilder().append(((TelepadEntry) other).dimensionID, this.dimensionID).append(((TelepadEntry) other).position, this.position).append(((TelepadEntry) other).enabled, this.enabled).isEquals();
            }

            return false;
        }
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        ListNBT tagList = nbt.getList("TelepadList", 10);
        tileMap.clear();

        for (int i = 0; i < tagList.size(); i++)
        {
            CompoundNBT nbt2 = tagList.getCompound(i);
            int address = nbt2.getInt("Address");
            int dimId = nbt.getInt("dimension");
            DimensionType dimType = DimensionType.getById(dimId);
            if (dimType == null)
            {
                throw new IllegalArgumentException("Invalid map dimension: " + i);
            }
            else
            {
                int posX = nbt2.getInt("PosX");
                int posY = nbt2.getInt("PosY");
                int posZ = nbt2.getInt("PosZ");
                boolean enabled = true;
                if (nbt2.contains("Enabled"))
                {
                    enabled = nbt2.getBoolean("Enabled");
                }
                tileMap.put(address, new TelepadEntry(dimType, new BlockVec3(posX, posY, posZ), enabled));
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        ListNBT tagList = new ListNBT();

        for (Map.Entry<Integer, TelepadEntry> e : tileMap.entrySet())
        {
            CompoundNBT nbt2 = new CompoundNBT();
            nbt2.putInt("Address", e.getKey());
            nbt2.putInt("DimID", e.getValue().dimensionID.getId());
            nbt2.putInt("PosX", e.getValue().position.x);
            nbt2.putInt("PosY", e.getValue().position.y);
            nbt2.putInt("PosZ", e.getValue().position.z);
            nbt2.putBoolean("Enabled", e.getValue().enabled);
            tagList.add(nbt2);
        }

        nbt.put("TelepadList", tagList);
        return nbt;
    }

    public static void addShortRangeTelepad(TileEntityShortRangeTelepad telepad)
    {
        if (!telepad.getWorld().isRemote)
        {
            if (telepad.addressValid)
            {
                TelepadEntry newEntry = new TelepadEntry(telepad.getWorld().dimension.getDimension().getType(), new BlockVec3(telepad), !telepad.getDisabled(0));
                TelepadEntry previous = tileMap.put(telepad.address, newEntry);

                if (previous == null || !previous.equals(newEntry))
                {
                    AsteroidsTickHandlerServer.spaceRaceData.setDirty(true);
                }
            }
        }
    }

    public static void removeShortRangeTeleporter(TileEntityShortRangeTelepad telepad)
    {
        if (!telepad.getWorld().isRemote)
        {
            if (telepad.addressValid)
            {
                tileMap.remove(telepad.address);
                AsteroidsTickHandlerServer.spaceRaceData.setDirty(true);
            }
        }
    }

    public static TelepadEntry getLocationFromAddress(int address)
    {
        return tileMap.get(address);
    }
}
