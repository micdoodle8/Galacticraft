package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public class WorldDataSpaceRaces extends WorldSavedData
{
    public static final String saveDataID = Constants.GCDATAFOLDER + "GCSpaceRaceData";
    private CompoundNBT dataCompound;

    public WorldDataSpaceRaces(String id)
    {
        super(id);
    }

    @Override
    public void readFromNBT(CompoundNBT nbt)
    {
        SpaceRaceManager.loadSpaceRaces(nbt);
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT nbt)
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
            worldData.dataCompound = new CompoundNBT();
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
