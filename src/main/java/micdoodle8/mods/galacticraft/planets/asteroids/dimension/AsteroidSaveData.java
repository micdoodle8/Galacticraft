package micdoodle8.mods.galacticraft.planets.asteroids.dimension;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

public class AsteroidSaveData extends WorldSavedData
{
    public static final String saveDataID = Constants.GCDATAFOLDER + "GCAsteroidData";
    public CompoundNBT datacompound;

    public AsteroidSaveData(String s)
    {
        super(AsteroidSaveData.saveDataID);
        this.datacompound = new CompoundNBT();
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        this.datacompound = nbt.getCompound("asteroids");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        nbt.put("asteroids", this.datacompound);
        return nbt;
    }
}
