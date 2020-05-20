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
    public void readFromNBT(CompoundNBT nbt)
    {
        this.datacompound = nbt.getCompoundTag("asteroids");
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT nbt)
    {
        nbt.setTag("asteroids", this.datacompound);
        return nbt;
    }
}
