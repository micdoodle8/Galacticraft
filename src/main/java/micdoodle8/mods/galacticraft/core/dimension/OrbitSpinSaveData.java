package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public class OrbitSpinSaveData extends WorldSavedData
{
    public static final String saveDataID = Constants.GCDATAFOLDER + "GCSpinData";
    public NBTTagCompound datacompound;
    private NBTTagCompound alldata;
    private int dim = 0;

    public OrbitSpinSaveData(String s)
    {
        super(OrbitSpinSaveData.saveDataID);
        this.datacompound = new NBTTagCompound();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        this.alldata = nbt;
        //world.loadData calls this but can't extract from alldata until we know the dimension ID
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        if (this.dim != 0)
        {
            nbt.setTag("" + this.dim, this.datacompound);
        }

        return nbt;
    }

    public static OrbitSpinSaveData initWorldData(World world)
    {
        OrbitSpinSaveData worldData = (OrbitSpinSaveData) world.loadData(OrbitSpinSaveData.class, OrbitSpinSaveData.saveDataID);

        if (worldData == null)
        {
            worldData = new OrbitSpinSaveData("");
            world.setData(OrbitSpinSaveData.saveDataID, worldData);
            if (world.provider instanceof WorldProviderSpaceStation)
            {
                worldData.dim = GCCoreUtil.getDimensionID(world);
                ((WorldProviderSpaceStation) world.provider).getSpinManager().writeToNBT(worldData.datacompound);
            }
            worldData.markDirty();
        }
        else if (world.provider instanceof WorldProviderSpaceStation)
        {
            worldData.dim = GCCoreUtil.getDimensionID(world);

            worldData.datacompound = null;
            if (worldData.alldata != null)
            {
                worldData.datacompound = worldData.alldata.getCompoundTag("" + worldData.dim);
            }
            if (worldData.datacompound == null)
            {
                worldData.datacompound = new NBTTagCompound();
            }
        }

        return worldData;
    }
}
