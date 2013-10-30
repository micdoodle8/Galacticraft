package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
import cpw.mods.fml.common.FMLLog;

public class OxygenPressureProtocol
{
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

    private static void nextVecD(World var1, int var2, int var3, int var4, LinkedList<VecDirPair> checked)
    {
        for (final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            Vector3 vec = new Vector3(var2, var3, var4);
            vec = vec.translate(new Vector3(dir));

            if (OxygenPressureProtocol.isBreathableAir(var1, vec.intX(), vec.intY(), vec.intZ()) && !isVisited(vec, checked))
            {
                checkAtVec(var1, vec, dir, checked);
            }
        }
    }

    private static void checkAtVec(World var1, Vector3 vec, ForgeDirection dir, LinkedList<VecDirPair> checked)
    {
        checked.add(new VecDirPair(vec, dir));

        if (isTouchingBreathableAir(var1, vec.intX(), vec.intY(), vec.intZ(), checked))
        {
            nextVecD(var1, vec.intX(), vec.intY(), vec.intZ(), checked);
        }
    }

    private static boolean isTouchingBreathableAir(World world, int x, int y, int z, LinkedList<VecDirPair> checked)
    {
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            Vector3 vec = new Vector3(x, y, z);
            vec = vec.translate(new Vector3(dir));

            if (OxygenPressureProtocol.isBreathableAir(world, vec.intX(), vec.intY(), vec.intZ()) && !isVisited(vec, checked))
            {
                return true;
            }
        }

        return false;
    }

    private static boolean nextVec(World world, int x, int y, int z, int maxChecks, LinkedList<VecDirPair> checked, List<GCCoreTileEntityOxygenSealer> sealers, List<Vector3> oxygenReliantBlocks)
    {
        boolean airTight = true;
        
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            maxChecks--;
            Vector3 vec = new Vector3(x, y, z);
            vec = vec.translate(new Vector3(dir));

            if (canBlockPass(world, vec, dir) && !isVisited(vec, checked))
            {
                airTight = loopThrough(world, vec.intX(), vec.intY(), vec.intZ(), maxChecks, dir, checked, sealers, oxygenReliantBlocks);
                
                if (!airTight)
                {
                    return false;
                }
            }

            int idAtVec = vec.getBlockID(world);

            if (idAtVec != 0 && Block.blocksList[idAtVec] instanceof IOxygenReliantBlock)
            {
                oxygenReliantBlocks.add(vec);
            }

            TileEntity tileAtVec = vec.getTileEntity(world);

            if (tileAtVec != null && tileAtVec instanceof GCCoreTileEntityOxygenSealer && dir.equals(ForgeDirection.DOWN))
            {
                sealers.add((GCCoreTileEntityOxygenSealer) tileAtVec);
                maxChecks += Math.floor(((GCCoreTileEntityOxygenSealer) tileAtVec).storedOxygen / 15.55D);
            }
        }

        return airTight;
    }

    private static boolean loopThrough(World var1, int var2, int var3, int var4, int var5, ForgeDirection dir, LinkedList<VecDirPair> checked, List<GCCoreTileEntityOxygenSealer> sealers, List<Vector3> oxygenReliantBlocks)
    {
        Vector3 vecAt = new Vector3(var2, var3, var4);
        checked.add(new VecDirPair(vecAt, dir));
        boolean airTight = true;

        if (touchingUnsealedBlock(var1, var2, var3, var4, checked))
        {
            if (airTight)
            {
                if (var5 > 0)
                {
                    airTight = nextVec(var1, var2, var3, var4, var5, checked, sealers, oxygenReliantBlocks);
                }
                else if (!getIsSealed(var1, var2, var3, var4, checked))
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

    public static boolean isBreathableAir(World var0, int var1, int var2, int var3)
    {
        return var0.getBlockId(var1, var2, var3) == GCCoreBlocks.breatheableAir.blockID;
    }

    private static boolean touchingUnsealedBlock(World var1, int var2, int var3, int var4, LinkedList<VecDirPair> checked)
    {
        for (final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            Vector3 vec = new Vector3(var2, var3, var4);
            vec = vec.translate(new Vector3(dir));

            if (canBlockPass(var1, vec, dir) && !isVisited(vec, checked))
            {
                return true;
            }
        }

        return false;
    }

    private static boolean isVisited(Vector3 var1, LinkedList<VecDirPair> checked)
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

    private static boolean getIsSealed(World var1, int var2, int var3, int var4, LinkedList<VecDirPair> checked)
    {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            Vector3 vec = new Vector3(var2, var3, var4);
            vec = vec.translate(new Vector3(direction));

            if (vec.getBlockID(var1) == 0 && !isVisited(vec, checked))
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
    
    public static class ThreadFindSeal extends Thread
    {
        public World world;
        public GCCoreTileEntityOxygenSealer head;
        public boolean sealed;
        public List<GCCoreTileEntityOxygenSealer> sealers;
        public List<Vector3> oxygenReliantBlocks;
        public LinkedList<VecDirPair> checked;
        
        public ThreadFindSeal()
        {
            super("GC Sealer Roomfinder Thread");
        }
        
        @Override
        public void run()
        {
            long startTime = System.nanoTime();
            this.sealed = nextVec(this.world, this.head.xCoord, this.head.yCoord, this.head.zCoord, MAX_SEAL_CHECKS, checked, this.sealers, this.oxygenReliantBlocks);
            
            if (!this.sealed)
            {
                this.checked.clear();
                nextVecD(world, this.head.xCoord, this.head.yCoord, this.head.zCoord, checked);
            }
            
            for (GCCoreTileEntityOxygenSealer sealer : this.sealers)
            {
                if (sealer != null && sealer != this.head)
                {
                    sealer.threadFindSeal = this;
                }
            }
            
            for (VecDirPair checkedVec : checked)
            {
                int blockID = checkedVec.getPosition().getBlockID(world);
                
                if (blockID == (this.sealed ? 0 : GCCoreBlocks.breatheableAir.blockID))
                {
                    world.setBlock(checkedVec.position.intX(), checkedVec.position.intY(), checkedVec.position.intZ(), this.sealed ? GCCoreBlocks.breatheableAir.blockID : 0, 0, 2);
                }
            }
            
            long endTime = System.nanoTime();
            
            if (GCCoreConfigManager.enableDebug)
            {
                FMLLog.info("Oxygen Sealer Check Completed.");
                FMLLog.info("   Sealed: " + this.sealed);
                FMLLog.info("   Time taken: " + ((endTime - startTime) / 1000000.0D) + "ms");
                FMLLog.info("   Found: " + this.sealers.size() + " sealers");
                FMLLog.info("   Looped through: " + checked.size() + " blocks");
            }
        }
    }
    
    public static void updateSealerStatus(GCCoreTileEntityOxygenSealer head)
    {
        if (head.threadFindSeal != null)
        {
            head.threadFindSeal.interrupt();
        }
        
        head.threadFindSeal = new ThreadFindSeal();
        head.threadFindSeal.world = head.worldObj;
        head.threadFindSeal.head = head;
        head.threadFindSeal.sealed = false;
        head.threadFindSeal.sealers = new ArrayList<GCCoreTileEntityOxygenSealer>();
        head.threadFindSeal.oxygenReliantBlocks = new ArrayList<Vector3>();
        head.threadFindSeal.checked = new LinkedList<VecDirPair>();
        head.threadFindSeal.start();
    }
}
