package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import micdoodle8.mods.galacticraft.api.block.IOxygenReliantBlock;
import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenSealer;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;

public class OxygenPressureProtocol
{
//    private static HashMap<Integer, HashSet<SealedSet>> sealedEdgeList = new HashMap<Integer, HashSet<SealedSet>>();
    private static ArrayList<Integer> vanillaPermeableBlocks = new ArrayList<Integer>();
    private static Map<Integer, ArrayList<Integer>> nonPermeableBlocks = new HashMap<Integer, ArrayList<Integer>>();
    public static final int MAX_SEAL_CHECKS = 400;

    static
    {
        OxygenPressureProtocol.vanillaPermeableBlocks.add(Block.sponge.blockID);

        try
        {
            for (final String s : GCCoreConfigManager.sealableIDs)
            {
                final String[] split = s.split(":");

                if (OxygenPressureProtocol.nonPermeableBlocks.containsKey(Integer.parseInt(split[0])))
                {
                    final ArrayList<Integer> l = OxygenPressureProtocol.nonPermeableBlocks.get(Integer.parseInt(split[0]));
                    l.add(Integer.parseInt(split[1]));
                    OxygenPressureProtocol.nonPermeableBlocks.put(Integer.parseInt(split[0]), l);
                }
                else
                {
                    final ArrayList<Integer> a = new ArrayList<Integer>();
                    a.add(Integer.parseInt(split[1]));
                    OxygenPressureProtocol.nonPermeableBlocks.put(Integer.parseInt(split[0]), a);
                }
            }
        }
        catch (final Exception e)
        {
            System.err.println();
            System.err.println("Error finding sealable IDs from the Galacticraft config, check that they are listed properly!");
            System.err.println();
            e.printStackTrace();
        }
    }
    
    public static class VecDirPair
    {
        private final Vector3 position;
        private final ForgeDirection direction;
        
        public VecDirPair(Vector3 position, ForgeDirection direction)
        {
            this.position = position;
            this.direction = direction;
        }
        
        public Vector3 getPosition()
        {
            return position;
        }
        
        public ForgeDirection getDirection()
        {
            return direction;
        }
        
        @Override
        public int hashCode()
        {
            return this.position.hashCode() + ("Dir: " + this.direction).hashCode();
        }
    }
    
    public static class SealedSet
    {
        private ArrayList<GCCoreTileEntityOxygenSealer> sealers;
        private HashSet<Vector3> sealedAirBlocks;
        private HashSet<Vector3> edgeBlocks;
        private HashSet<Vector3> oxygenReliantBlocks;
        
        public SealedSet(ArrayList<GCCoreTileEntityOxygenSealer> sealers, HashSet<Vector3> sealedAirBlocks, HashSet<Vector3> edgeBlocks, HashSet<Vector3> oxygenReliantBlocks)
        {
            this.sealers = sealers;
            this.sealedAirBlocks = sealedAirBlocks;
            this.edgeBlocks = edgeBlocks;
            this.setOxygenReliantBlocks(oxygenReliantBlocks);
        }

        public ArrayList<GCCoreTileEntityOxygenSealer> getSealers()
        {
            return sealers;
        }
        
        public void setSealers(ArrayList<GCCoreTileEntityOxygenSealer> sealers)
        {
            this.sealers = sealers;
        }
        
        public HashSet<Vector3> getSealedAirBlocks()
        {
            return sealedAirBlocks;
        }
        
        public void setSealedAirBlocks(HashSet<Vector3> sealedAirBlocks)
        {
            this.sealedAirBlocks = sealedAirBlocks;
        }

        public HashSet<Vector3> getEdgeBlocks()
        {
            return edgeBlocks;
        }

        public void setEdgeBlocks(HashSet<Vector3> edgeBlocks)
        {
            this.edgeBlocks = edgeBlocks;
        }

        public HashSet<Vector3> getOxygenReliantBlocks()
        {
            return oxygenReliantBlocks;
        }

        public void setOxygenReliantBlocks(HashSet<Vector3> oxygenReliantBlocks)
        {
            this.oxygenReliantBlocks = oxygenReliantBlocks;
        }
    }

    public static boolean checkSeal(World var1, int x, int y, int z, int maxChecks)
    {
        boolean airTight = true;
        LinkedList<VecDirPair> checked = new LinkedList<VecDirPair>();
        SealedSet set = new SealedSet(new ArrayList<GCCoreTileEntityOxygenSealer>(), new HashSet<Vector3>(), new HashSet<Vector3>(), new HashSet<Vector3>());
//        SealedSet set = getSealedSetFromAirBlock(var1, new Vector3(x, y, z));
//        
//        if (set == null)
//        {
//            TileEntity tile = var1.getBlockTileEntity(x, y, z);
//            
//            if (tile instanceof GCCoreTileEntityOxygenSealer)
//            {
//                set = getSealedSetFromSealer((GCCoreTileEntityOxygenSealer) tile);
//            }
//        }
//        
//        if (set == null)
//        {
//            return false;
//        }

        if (var1.getBlockMetadata(x, y, z) != 100)
        {
            airTight = OxygenPressureProtocol.nextVec2(var1, x, y, z, maxChecks, checked, set);
        }

        if (airTight)
        {
            for (VecDirPair var7 : checked)
            {
                if (!OxygenPressureProtocol.getIsSealed2(var1, var7.position.intX(), var7.position.intY(), var7.position.intZ(), checked))
                {
                    airTight = false;
                }
            }
        }

        return airTight;
    }
    
    private static boolean sealFromSet(World world, Vector3 vec, SealedSet set, int maxChecks)
    {
//        int dimID = world.provider.dimensionId;

//        if (set == null)
        {
            set = new SealedSet(new ArrayList<GCCoreTileEntityOxygenSealer>(), new HashSet<Vector3>(), new HashSet<Vector3>(), new HashSet<Vector3>());
            LinkedList<VecDirPair> checked = new LinkedList<VecDirPair>();
            
            boolean airTight = nextVec2(world, vec.intX(), vec.intY(), vec.intZ(), maxChecks, checked, set);

            if (airTight)
            {
                for (VecDirPair nextCheckedBlock : checked)
                {
                    if (world.getBlockId(nextCheckedBlock.position.intX(), nextCheckedBlock.position.intY(), nextCheckedBlock.position.intZ()) == 0)
                    {
                        world.setBlock(nextCheckedBlock.position.intX(), nextCheckedBlock.position.intY(), nextCheckedBlock.position.intZ(), GCCoreBlocks.breatheableAir.blockID, 0, 2);
                    }
                }

                for (Vector3 nextVector : set.oxygenReliantBlocks)
                {
                    Block block = Block.blocksList[nextVector.getBlockID(world)];

                    if (block != null && block instanceof IOxygenReliantBlock)
                    {
                        ((IOxygenReliantBlock) block).onOxygenAdded(world, nextVector.intX(), nextVector.intY(), nextVector.intZ());
                    }
                }
            }
            else
            {
                set = null;
                unSeal2(world, vec);
            }
        }

//        HashSet<SealedSet> sealedSetList = sealedEdgeList.get(dimID);
//        
//        if (sealedSetList == null)
//        {
//            sealedSetList = new HashSet<SealedSet>();
//        }
//        
//        if (set != null)
//        {
//            sealedSetList.add(set);
//        }
//        
//        sealedEdgeList.put(dimID, sealedSetList);
        return set == null;
    }
    
    public static boolean seal2(World world, Vector3 vec, int maxChecks)
    {
        return sealFromSet(world, vec, null, maxChecks);
    }
    
    public static boolean seal2(GCCoreTileEntityOxygenSealer sealer, int maxChecks)
    {
        return sealFromSet(sealer.worldObj, new Vector3(sealer).translate(new Vector3(0, 1, 0)), null, maxChecks);
    }

    public static void unSeal2(World var1, Vector3 vec)
    {
        LinkedList<VecDirPair> checked = new LinkedList<VecDirPair>();
        
//        SealedSet set = getSealedSetFromAirBlock(var1, vec);
//
//        if (set == null)
//        {
//            set = getSealedSetFromEdgeBlock(var1, vec);
//        }
//        
//        if (set != null)
//        {
////            sealedEdgeList.get(var1.provider.dimensionId).remove(set);
//        }
        
        SealedSet set = new SealedSet(new ArrayList<GCCoreTileEntityOxygenSealer>(), new HashSet<Vector3>(), new HashSet<Vector3>(), new HashSet<Vector3>());
        
        nextVecD2(var1, vec.intX(), vec.intY(), vec.intZ(), checked, set);

        for (VecDirPair var6 : checked)
        {
            if (canBlockPass(var1, var6.position, var6.direction))
            {
                var1.getBlockId(var6.position.intX(), var6.position.intY(), var6.position.intZ());

                Block.blocksList[var1.getBlockId(var6.position.intX(), var6.position.intY(), var6.position.intZ())].dropBlockAsItem(var1, var6.position.intX(), var6.position.intY(), var6.position.intZ(), var1.getBlockMetadata(var6.position.intX(), var6.position.intY(), var6.position.intZ()), 0);
                var1.setBlock(var6.position.intX(), var6.position.intY(), var6.position.intZ(), 0, 0, 2);
            }
        }

        for (VecDirPair var6 : checked)
        {
            if (var1.getBlockId(var6.position.intX(), var6.position.intY(), var6.position.intZ()) == GCCoreBlocks.breatheableAir.blockID)
            {
                var1.setBlock(var6.position.intX(), var6.position.intY(), var6.position.intZ(), 0, 0, 2);
            }
        }

        for (Vector3 var6b : set.oxygenReliantBlocks)
        {
            final int idAt = var6b.getBlockID(var1);

            if (idAt != 0 && Block.blocksList[idAt] instanceof IOxygenReliantBlock)
            {
                ((IOxygenReliantBlock) Block.blocksList[idAt]).onOxygenRemoved(var1, var6b.intX(), var6b.intY(), var6b.intZ());
            }
        }

        for (VecDirPair var6 : checked)
        {
            if (var6.position.getBlockID(var1) == 0)
            {
                var1.notifyBlocksOfNeighborChange(var6.position.intX(), var6.position.intY(), var6.position.intZ(), 0);
            }
        }
    }

    private static void nextVecD2(World var1, int var2, int var3, int var4, LinkedList<VecDirPair> checked, SealedSet sealedSet)
    {
        for (final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            Vector3 vec = new Vector3(var2, var3, var4);
            vec = vec.translate(new Vector3(dir));

            if (OxygenPressureProtocol.isBreathableAir(var1, vec.intX(), vec.intY(), vec.intZ()) && !isVisited2(vec, checked))
            {
                sealedSet.sealedAirBlocks.add(vec);
                checkAtVec2(var1, vec, dir, checked, sealedSet);
            }

            final int idAtVec = vec.getBlockID(var1);

            if (idAtVec != 0 && Block.blocksList[idAtVec] instanceof IOxygenReliantBlock)
            {
                sealedSet.oxygenReliantBlocks.add(vec);
            }
        }
    }

    private static void checkAtVec2(World var1, Vector3 vec, ForgeDirection dir, LinkedList<VecDirPair> checked, SealedSet sealedSet)
    {
        checked.add(new VecDirPair(vec, dir));

        if (isTouchingBreathableAir2(var1, vec.intX(), vec.intY(), vec.intZ(), checked, sealedSet))
        {
            nextVecD2(var1, vec.intX(), vec.intY(), vec.intZ(), checked, sealedSet);
        }
    }

    private static boolean nextVec2(World world, int x, int y, int z, int maxChecks, LinkedList<VecDirPair> checked, SealedSet sealedSet)
    {
        boolean airTight = true;
        
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            maxChecks--;
            Vector3 vec = new Vector3(x, y, z);
            vec = vec.translate(new Vector3(dir));

            if (canBlockPass(world, vec, dir) && !isVisited2(vec, checked))
            {
                airTight = loopThrough2(world, vec.intX(), vec.intY(), vec.intZ(), maxChecks, dir, checked, sealedSet);
                
                if (!airTight)
                {
                    return false;
                }
            }

            int idAtVec = vec.getBlockID(world);

            if (idAtVec != 0 && Block.blocksList[idAtVec] instanceof IOxygenReliantBlock)
            {
                sealedSet.oxygenReliantBlocks.add(vec);
            }

            TileEntity tileAtVec = vec.getTileEntity(world);

            if (tileAtVec != null && tileAtVec instanceof GCCoreTileEntityOxygenSealer && dir.equals(ForgeDirection.DOWN))
            {
                sealedSet.sealers.add((GCCoreTileEntityOxygenSealer) tileAtVec);
                maxChecks += Math.floor(((GCCoreTileEntityOxygenSealer) tileAtVec).storedOxygen / 15.55D);
            }
        }

        return airTight;
    }

    private static boolean loopThrough2(World var1, int var2, int var3, int var4, int var5, ForgeDirection dir, LinkedList<VecDirPair> checked, SealedSet sealedSet)
    {
        Vector3 vecAt = new Vector3(var2, var3, var4);
        checked.add(new VecDirPair(vecAt, dir));
        boolean airTight = true;

        if (touchingUnsealedBlock2(var1, var2, var3, var4, checked, sealedSet))
        {
            if (airTight)
            {
                if (var5 > 0)
                {
                    airTight = nextVec2(var1, var2, var3, var4, var5, checked, sealedSet);
                }
                else if (!getIsSealed2(var1, var2, var3, var4, checked))
                {
                    airTight = false;
                }
            }
            else
            {
                return false;
            }
        }
        
        return airTight;
    }
    
    public static void onBlockBroken(World world, int x, int y, int z, int oldBlockID, int oldBlockMetadata)
    {
//        if (oldBlockID != GCCoreBlocks.breatheableAir.blockID)
//        {
//            SealedSet sealedSet = getSealedSetFromEdgeBlock(world, new Vector3(x, y, z));
//            
//            if (sealedSet != null)
//            {
//                seal2(world, new Vector3(x, y, z), MAX_SEAL_CHECKS);
//                
////                FMLLog.info("Edge block broken: " + new Vector3(x, y, z));
////                
////                if (sealedSet.sealers.size() > 0)
////                {
////                    seal2(sealedSet.sealers.get(0), MAX_SEAL_CHECKS);
////                }
//            }
//        }
    }
    
//    private static SealedSet getSealedSetFromSealer(GCCoreTileEntityOxygenSealer sealer)
//    {
//        HashSet<SealedSet> sealedSets = getSealedSets(sealer.worldObj);
//
//        if (sealedSets != null && !sealedSets.isEmpty())
//        {
//            for (SealedSet sealedSet : sealedSets)
//            {
//                if (sealedSet.getSealers().contains(sealer))
//                {
//                    return sealedSet;
//                }
//            }
//        }
//        
//        return null;
//    }
//    
//    private static SealedSet getSealedSetFromAirBlock(World world, Vector3 airBlock)
//    {
//        HashSet<SealedSet> sealedSets = getSealedSets(world);
//
//        if (sealedSets != null && !sealedSets.isEmpty())
//        {
//            for (SealedSet sealedSet : sealedSets)
//            {
//                if (sealedSet.getSealedAirBlocks().contains(airBlock))
//                {
//                    return sealedSet;
//                }
//            }
//        }
//        
//        return null;
//    }
//    
//    private static SealedSet getSealedSetFromEdgeBlock(World world, Vector3 edgeBlock)
//    {
//        HashSet<SealedSet> sealedSets = getSealedSets(world);
//        
//        if (sealedSets != null && !sealedSets.isEmpty())
//        {
//            for (SealedSet sealedSet : sealedSets)
//            {
//                if (sealedSet.getEdgeBlocks().contains(edgeBlock))
//                {
//                    return sealedSet;
//                }
//            }
//        }
//        
//        return null;
//    }
    
//    private static HashSet<SealedSet> getSealedSets(World world)
//    {
//        int dimID = world.provider.dimensionId;
//        HashSet<SealedSet> sealedPositions = sealedEdgeList.get(dimID);
//        
//        if (sealedPositions != null && !sealedPositions.isEmpty())
//        {
//            for (Entry<Integer, HashSet<SealedSet>> e : sealedEdgeList.entrySet())
//            {
//                if (e.getKey() == dimID)
//                {
//                    return new HashSet<SealedSet>(e.getValue());
//                }
//            }
//        }
//        
//        return null;
//    }

    public static boolean isBreathableAir(World var0, int var1, int var2, int var3)
    {
        return var0.getBlockId(var1, var2, var3) == GCCoreBlocks.breatheableAir.blockID;
    }

    private static boolean isTouchingBreathableAir2(World world, int x, int y, int z, LinkedList<VecDirPair> checked, SealedSet sealedSet)
    {
        boolean hitBreathableAir = false;
        
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            Vector3 vec = new Vector3(x, y, z);
            vec = vec.translate(new Vector3(dir));

            if (OxygenPressureProtocol.isBreathableAir(world, vec.intX(), vec.intY(), vec.intZ()))
            {
                sealedSet.sealedAirBlocks.add(vec);
                
                if (!hitBreathableAir)
                {
                    hitBreathableAir = !isVisited2(vec, checked);
                }
            }
        }

        return hitBreathableAir;
    }

    private static boolean touchingUnsealedBlock2(World var1, int var2, int var3, int var4, LinkedList<VecDirPair> checked, SealedSet sealedSet)
    {
        boolean hitUnsealed = false;
        
        for (final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            Vector3 vec = new Vector3(var2, var3, var4);
            vec = vec.translate(new Vector3(dir));

            if (canBlockPass(var1, vec, dir))
            {
                if (!hitUnsealed)
                {
                    hitUnsealed = !isVisited2(vec, checked);
                }
                
                int blockID = vec.getBlockID(var1);
                
                if (blockID != 0 && blockID != GCCoreBlocks.breatheableAir.blockID)
                {
                    sealedSet.edgeBlocks.add(vec);
                }
            }
            else
            {
                sealedSet.edgeBlocks.add(vec);
            }
        }

        return hitUnsealed;
    }

    private static boolean isVisited2(Vector3 var1, LinkedList<VecDirPair> checked)
    {
        for (VecDirPair vec : checked)
        {
            if (vec.position.equals(var1))
            {
                return true;
            }
        }

        return false;
    }

    private static boolean getIsSealed2(World var1, int var2, int var3, int var4, LinkedList<VecDirPair> checked)
    {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            Vector3 vec = new Vector3(var2, var3, var4);
            vec = vec.translate(new Vector3(direction));

            if (vec.getBlockID(var1) == 0 && !isVisited2(vec, checked))
            {
                return false;
            }
        }

        return true;
    }

    public static boolean canBlockPass(World var0, Vector3 vec, ForgeDirection direction)
    {
        int id = vec.getBlockID(var0);

        if (id > 0)
        {
            Block block = Block.blocksList[id];
            int metadata = var0.getBlockMetadata(vec.intX(), vec.intY(), vec.intZ());

            if (id == GCCoreBlocks.breatheableAir.blockID)
            {
                return true;
            }

            if (OxygenPressureProtocol.vanillaPermeableBlocks.contains(id))
            {
                return true;
            }

            if (!block.isOpaqueCube())
            {
                if (block instanceof IPartialSealableBlock)
                {
                    return !((IPartialSealableBlock) block).isSealed(var0, vec.intX(), vec.intY(), vec.intZ(), direction);
                }

                if (OxygenPressureProtocol.nonPermeableBlocks.containsKey(id) && OxygenPressureProtocol.nonPermeableBlocks.get(id).contains(metadata))
                {
                    return false;
                }

                return true;
            }

            return false;
        }

        return true;
    }
}
