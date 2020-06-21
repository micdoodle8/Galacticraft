//package micdoodle8.mods.galacticraft.core.dimension;
//
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.world.World;
//import net.minecraft.world.storage.WorldSavedData;
//
//public class OrbitSpinSaveData extends WorldSavedData
//{
//    public static final String saveDataID = Constants.GCDATAFOLDER + "GCSpinData";
//    public CompoundNBT datacompound;
//    private CompoundNBT alldata;
//    private int dim = 0;
//
//    public OrbitSpinSaveData(String s)
//    {
//        super(OrbitSpinSaveData.saveDataID);
//        this.datacompound = new CompoundNBT();
//    }
//
//    @Override
//    public void readFromNBT(CompoundNBT nbt)
//    {
//        this.alldata = nbt;
//        //world.loadData calls this but can't extract from alldata until we know the dimension ID
//    }
//
//    @Override
//    public CompoundNBT writeToNBT(CompoundNBT nbt)
//    {
//        if (this.dim != 0)
//        {
//            nbt.put("" + this.dim, this.datacompound);
//        }
//
//        return nbt;
//    }
//
//    public static OrbitSpinSaveData initWorldData(World world)
//    {
//        OrbitSpinSaveData worldData = (OrbitSpinSaveData) world.loadData(OrbitSpinSaveData.class, OrbitSpinSaveData.saveDataID);
//
//        if (worldData == null)
//        {
//            worldData = new OrbitSpinSaveData("");
//            world.setData(OrbitSpinSaveData.saveDataID, worldData);
//            if (world.getDimension() instanceof DimensionSpaceStation)
//            {
//                worldData.dim = GCCoreUtil.getDimensionID(world);
//                ((DimensionSpaceStation) world.getDimension()).getSpinManager().writeToNBT(worldData.datacompound);
//            }
//            worldData.markDirty();
//        }
//        else if (world.getDimension() instanceof DimensionSpaceStation)
//        {
//            worldData.dim = GCCoreUtil.getDimensionID(world);
//
//            worldData.datacompound = null;
//            if (worldData.alldata != null)
//            {
//                worldData.datacompound = worldData.alldata.getCompound("" + worldData.dim);
//            }
//            if (worldData.datacompound == null)
//            {
//                worldData.datacompound = new CompoundNBT();
//            }
//        }
//
//        return worldData;
//    }
//}
