package micdoodle8.mods.galacticraft.planets.asteroids.dimension;

import com.google.common.collect.Maps;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.tick.AsteroidsTickHandlerServer;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Map;

public class ShortRangeTelepadHandler extends WorldSavedData
{
    public static final String saveDataID = "ShortRangeTelepads";
    private static Map<Integer, TelepadEntry> tileMap = Maps.newHashMap();

    public ShortRangeTelepadHandler(String saveDataID)
    {
        super(saveDataID);
    }

    public static class TelepadEntry
    {
        public int dimensionID;
        public BlockVec3 position;
        public boolean enabled;

        public TelepadEntry(int dimID, BlockVec3 position, boolean enabled)
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
    public void readFromNBT(NBTTagCompound nbt)
    {
        NBTTagList tagList = nbt.getTagList("TelepadList", 10);
        tileMap.clear();

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound nbt2 = tagList.getCompoundTagAt(i);
            int address = nbt2.getInteger("Address");
            int dimID = nbt2.getInteger("DimID");
            int posX = nbt2.getInteger("PosX");
            int posY = nbt2.getInteger("PosY");
            int posZ = nbt2.getInteger("PosZ");
            boolean enabled = true;
            if (nbt2.hasKey("Enabled"))
            {
                enabled = nbt2.getBoolean("Enabled");
            }
            tileMap.put(address, new TelepadEntry(dimID, new BlockVec3(posX, posY, posZ), enabled));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        NBTTagList tagList = new NBTTagList();

        for (Map.Entry<Integer, TelepadEntry> e : tileMap.entrySet())
        {
            NBTTagCompound nbt2 = new NBTTagCompound();
            nbt2.setInteger("Address", e.getKey());
            nbt2.setInteger("DimID", e.getValue().dimensionID);
            nbt2.setInteger("PosX", e.getValue().position.x);
            nbt2.setInteger("PosY", e.getValue().position.y);
            nbt2.setInteger("PosZ", e.getValue().position.z);
            nbt2.setBoolean("Enabled", e.getValue().enabled);
            tagList.appendTag(nbt2);
        }

        nbt.setTag("TelepadList", tagList);
    }

    public static void addShortRangeTelepad(TileEntityShortRangeTelepad telepad)
    {
        if (!telepad.getWorld().isRemote)
        {
            if (telepad.addressValid)
            {
                TelepadEntry newEntry = new TelepadEntry(GCCoreUtil.getDimensionID(telepad.getWorld()), new BlockVec3(telepad), !telepad.getDisabled(0));
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
