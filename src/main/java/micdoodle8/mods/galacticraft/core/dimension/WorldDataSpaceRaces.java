package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public class WorldDataSpaceRaces extends WorldSavedData
{
    public static final String saveDataID = Constants.GCDATAFOLDER + "GCSpaceRaceData";
    private NBTTagCompound dataCompound;

    public WorldDataSpaceRaces(String id)
    {
        super(id);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        SpaceRaceManager.loadSpaceRaces(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        return SpaceRaceManager.saveSpaceRaces(nbt);
    }

    public static WorldDataSpaceRaces initWorldData(World world)
    {
        WorldDataSpaceRaces worldData = (WorldDataSpaceRaces) world.loadData(WorldDataSpaceRaces.class, WorldDataSpaceRaces.saveDataID);

        if (worldData == null)
        {
            worldData = new WorldDataSpaceRaces(WorldDataSpaceRaces.saveDataID);
            world.setData(WorldDataSpaceRaces.saveDataID, worldData);
            worldData.dataCompound = new NBTTagCompound();
            worldData.markDirty();
        }

        return worldData;
    }

    @Override
    public boolean isDirty()
    {
        return true;
    }
}
