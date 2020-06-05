package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

public class WorldDataSpaceRaces extends WorldSavedData
{
    public static final String saveDataID = Constants.GCDATAFOLDER + "GCSpaceRaceData";
    private CompoundNBT dataCompound;

    public WorldDataSpaceRaces()
    {
        super(WorldDataSpaceRaces.saveDataID);
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        SpaceRaceManager.loadSpaceRaces(nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        return SpaceRaceManager.saveSpaceRaces(nbt);
    }

    public static WorldDataSpaceRaces initWorldData(ServerWorld world)
    {
        return world.getSavedData().getOrCreate(WorldDataSpaceRaces::new, WorldDataSpaceRaces.saveDataID);
    }

    @Override
    public boolean isDirty()
    {
        return true;
    }
}
