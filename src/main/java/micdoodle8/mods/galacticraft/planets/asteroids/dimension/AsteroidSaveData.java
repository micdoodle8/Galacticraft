package micdoodle8.mods.galacticraft.planets.asteroids.dimension;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public class AsteroidSaveData extends WorldSavedData
{
    public static final String saveDataID = Constants.GCDATAFOLDER + "GCAsteroidData";
    public NBTTagCompound datacompound;

    public AsteroidSaveData(String s)
    {
        super(AsteroidSaveData.saveDataID);
        this.datacompound = new NBTTagCompound();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        this.datacompound = nbt.getCompoundTag("asteroids");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setTag("asteroids", this.datacompound);
        return nbt;
    }
}
