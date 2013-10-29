package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    private LinkedList<VecDirPair> checked = new LinkedList<VecDirPair>();
    private LinkedList<Vector3> oxygenReliantBlocks = new LinkedList<Vector3>();
    private boolean airtight;
    private static ArrayList<Integer> vanillaPermeableBlocks = new ArrayList<Integer>();
    private static Map<Integer, ArrayList<Integer>> nonPermeableBlocks = new HashMap<Integer, ArrayList<Integer>>();

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

    private void loopThrough(World var1, int var2, int var3, int var4, int var5, ForgeDirection dir)
    {
        final Vector3 vecAt = new Vector3(var2, var3, var4);

        this.checked.add(new VecDirPair(vecAt, dir));

        if (this.touchingUnsealedBlock(var1, var2, var3, var4) && this.airtight)
        {
            if (var5 > 0)
            {
                this.nextVec(var1, var2, var3, var4, var5);
            }
            else if (!this.getIsSealed(var1, var2, var3, var4))
            {
                this.airtight = false;
            }
        }
    }

    private void checkAtVec(World var1, Vector3 vec, ForgeDirection dir)
    {
        this.checked.add(new VecDirPair(vec, dir));

        if (this.isTouchingBreathableAir(var1, vec.intX(), vec.intY(), vec.intZ()))
        {
            this.nextVecD(var1, vec.intX(), vec.intY(), vec.intZ());
        }
    }

    private void nextVec(World var1, int var2, int var3, int var4, int var5)
    {
        for (final ForgeDirection dir : ForgeDirection.values())
        {
            if (dir != ForgeDirection.UNKNOWN)
            {
                Vector3 vec = new Vector3(var2, var3, var4);
                vec = vec.translate(new Vector3(dir));

                if (this.canBlockPass(var1, vec, dir) && !this.isVisited(vec))
                {
                    this.loopThrough(var1, vec.intX(), vec.intY(), vec.intZ(), var5 - 1, dir);
                }

                final int idAtVec = vec.getBlockID(var1);

                if (idAtVec != 0 && Block.blocksList[idAtVec] instanceof IOxygenReliantBlock)
                {
                    this.oxygenReliantBlocks.add(vec);
                }

                final TileEntity tileAtVec = vec.getTileEntity(var1);

                if (tileAtVec != null && tileAtVec instanceof GCCoreTileEntityOxygenSealer)
                {
                    var5 += ((GCCoreTileEntityOxygenSealer) tileAtVec).storedOxygen / 25.0D;
                }
            }
        }
    }

    private void nextVecD(World var1, int var2, int var3, int var4)
    {
        for (final ForgeDirection dir : ForgeDirection.values())
        {
            if (dir != ForgeDirection.UNKNOWN)
            {
                Vector3 vec = new Vector3(var2, var3, var4);
                vec = vec.translate(new Vector3(dir));

                if (OxygenPressureProtocol.isBreathableAir(var1, vec.intX(), vec.intY(), vec.intZ()) && !this.isVisited(vec))
                {
                    this.checkAtVec(var1, vec, dir);
                }

                final int idAtVec = vec.getBlockID(var1);

                if (idAtVec != 0 && Block.blocksList[idAtVec] instanceof IOxygenReliantBlock)
                {
                    this.oxygenReliantBlocks.add(vec);
                }
            }
        }
    }

    public boolean seal(World var1, int var2, int var3, int var4, int var5)
    {
        this.airtight = true;
        this.nextVec(var1, var2, var3, var4, var5);

        if (this.airtight)
        {
            Iterator<VecDirPair> var6 = this.checked.iterator();
            VecDirPair var7;

            while (var6.hasNext())
            {
                var7 = var6.next();

                if (!this.getIsSealed(var1, var7.position.intX(), var7.position.intY(), var7.position.intZ()))
                {
                    this.airtight = false;
                }
            }

            Iterator<Vector3> var6b = this.oxygenReliantBlocks.iterator();
            Vector3 var7b;

            while (var6b.hasNext())
            {
                var7b = var6b.next();

                final Block block = Block.blocksList[var7b.getBlockID(var1)];

                if (block != null && block instanceof IOxygenReliantBlock)
                {
                    ((IOxygenReliantBlock) block).onOxygenAdded(var1, var7b.intX(), var7b.intY(), var7b.intZ());
                }
            }

            var6 = this.checked.iterator();

            while (var6.hasNext())
            {
                var7 = var6.next();

                if (var7.position.getBlockID(var1) == 0)
                {
                    if (var1.getBlockId(var7.position.intX(), var7.position.intY() + 1, var7.position.intZ()) == Block.doorWood.blockID)
                    {
                        var1.setBlock(var7.position.intX(), var7.position.intY() + 1, var7.position.intZ(), GCCoreBlocks.breatheableAir.blockID);
                    }

                    if (var1.getBlockId(var7.position.intX(), var7.position.intY() - 1, var7.position.intZ()) == Block.doorWood.blockID && var1.getBlockId(var7.position.intX(), var7.position.intY() - 2, var7.position.intZ()) != Block.doorWood.blockID)
                    {
                        var1.setBlock(var7.position.intX(), var7.position.intY() - 1, var7.position.intZ(), GCCoreBlocks.breatheableAir.blockID);
                    }

                    var1.setBlock(var7.position.intX(), var7.position.intY(), var7.position.intZ(), GCCoreBlocks.breatheableAir.blockID, 0, 2);
                }
            }
        }

        this.checked = new LinkedList<VecDirPair>();
        this.oxygenReliantBlocks = new LinkedList<Vector3>();
        return this.airtight;
    }

    public boolean checkSeal(World var1, int var2, int var3, int var4, int var5)
    {
        this.airtight = true;

        if (var1.getBlockMetadata(var2, var3, var4) != 100)
        {
            this.nextVec(var1, var2, var3, var4, var5);
        }

        if (this.airtight)
        {
            final Iterator<VecDirPair> var6 = this.checked.iterator();

            while (var6.hasNext())
            {
                final VecDirPair var7 = var6.next();

                if (!this.getIsSealed(var1, var7.position.intX(), var7.position.intY(), var7.position.intZ()))
                {
                    this.airtight = false;
                }
            }
        }

        this.checked = new LinkedList<VecDirPair>();
        return this.airtight;
    }

    public void unSeal(World var1, int var2, int var3, int var4)
    {
        this.nextVecD(var1, var2, var3, var4);
        Iterator<VecDirPair> var5 = this.checked.iterator();
        VecDirPair var6;

        while (var5.hasNext())
        {
            var6 = var5.next();

            if (this.canBlockPass(var1, var6.position, var6.direction))
            {
                var1.getBlockId(var6.position.intX(), var6.position.intY(), var6.position.intZ());

                Block.blocksList[var1.getBlockId(var6.position.intX(), var6.position.intY(), var6.position.intZ())].dropBlockAsItem(var1, var6.position.intX(), var6.position.intY(), var6.position.intZ(), var1.getBlockMetadata(var6.position.intX(), var6.position.intY(), var6.position.intZ()), 0);
                var1.setBlock(var6.position.intX(), var6.position.intY(), var6.position.intZ(), 0, 0, 2);
            }
        }

        var5 = this.checked.iterator();

        while (var5.hasNext())
        {
            var6 = var5.next();

            if (var1.getBlockId(var6.position.intX(), var6.position.intY(), var6.position.intZ()) == GCCoreBlocks.breatheableAir.blockID)
            {
                var1.setBlock(var6.position.intX(), var6.position.intY(), var6.position.intZ(), 0, 0, 2);
            }
        }

        Iterator<Vector3> var5b = this.oxygenReliantBlocks.iterator();
        Vector3 var6b;

        while (var5b.hasNext())
        {
            var6b = var5b.next();

            final int idAt = var6b.getBlockID(var1);

            if (idAt != 0 && Block.blocksList[idAt] instanceof IOxygenReliantBlock)
            {
                ((IOxygenReliantBlock) Block.blocksList[idAt]).onOxygenRemoved(var1, var6b.intX(), var6b.intY(), var6b.intZ());
            }
        }

        var5 = this.checked.iterator();

        while (var5.hasNext())
        {
            var6 = var5.next();

            if (var6.position.getBlockID(var1) == 0)
            {
                var1.notifyBlocksOfNeighborChange(var6.position.intX(), var6.position.intY(), var6.position.intZ(), 0);
            }
        }

        this.oxygenReliantBlocks = new LinkedList<Vector3>();
        this.checked = new LinkedList<VecDirPair>();
    }

    public boolean canBlockPass(World var0, Vector3 vec, ForgeDirection direction)
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

    public static boolean isBreathableAir(World var0, int var1, int var2, int var3)
    {
        return var0.getBlockId(var1, var2, var3) == GCCoreBlocks.breatheableAir.blockID;
    }

    private boolean touchingUnsealedBlock(World var1, int var2, int var3, int var4)
    {
        for (final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            Vector3 vec = new Vector3(var2, var3, var4);
            vec = vec.translate(new Vector3(dir));

            if (this.canBlockPass(var1, vec, dir) && !this.isVisited(vec))
            {
                return true;
            }
        }

        return false;
    }

    private boolean getIsSealed(World var1, int var2, int var3, int var4)
    {
        for (final ForgeDirection dir : ForgeDirection.values())
        {
            if (dir != ForgeDirection.UNKNOWN)
            {
                Vector3 vec = new Vector3(var2, var3, var4);
                vec = vec.translate(new Vector3(dir));

                if (vec.getBlockID(var1) == 0 && !this.isVisited(vec))
                {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isTouchingBreathableAir(World var1, int var2, int var3, int var4)
    {
        for (final ForgeDirection dir : ForgeDirection.values())
        {
            if (dir != ForgeDirection.UNKNOWN)
            {
                Vector3 vec = new Vector3(var2, var3, var4);
                vec = vec.translate(new Vector3(dir));

                if (OxygenPressureProtocol.isBreathableAir(var1, vec.intX(), vec.intY(), vec.intZ()) && !this.isVisited(vec))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isVisited(Vector3 var1)
    {
        this.checked.iterator();
        for (final VecDirPair vec : this.checked)
        {
            if (vec.position.equals(var1))
            {
                return true;
            }
        }

        return false;
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
    }
}
