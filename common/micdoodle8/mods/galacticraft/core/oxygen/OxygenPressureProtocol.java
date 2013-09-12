package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import micdoodle8.mods.galacticraft.api.block.IOxygenReliantBlock;
import micdoodle8.mods.galacticraft.api.block.IPartialSealedBlock;
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
    private LinkedList<Vector3> checked = new LinkedList<Vector3>();
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

    private void loopThrough(World var1, int var2, int var3, int var4, int var5)
    {
        final Vector3 vecAt = new Vector3(var2, var3, var4);

        this.checked.add(vecAt);

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

    private void checkAtVec(World var1, Vector3 vec)
    {
        this.checked.add(vec);

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

                if (this.canBlockPass(var1, vec) && !this.isVisited(vec))
                {
                    this.loopThrough(var1, vec.intX(), vec.intY(), vec.intZ(), var5 - 1);
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
                    this.checkAtVec(var1, vec);
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
            Iterator<Vector3> var6 = this.checked.iterator();
            Vector3 var7;

            while (var6.hasNext())
            {
                var7 = var6.next();

                if (!this.getIsSealed(var1, var7.intX(), var7.intY(), var7.intZ()))
                {
                    this.airtight = false;
                }
            }

            var6 = this.oxygenReliantBlocks.iterator();

            while (var6.hasNext())
            {
                var7 = var6.next();

                final Block block = Block.blocksList[var7.getBlockID(var1)];

                if (block != null && block instanceof IOxygenReliantBlock)
                {
                    ((IOxygenReliantBlock) block).onOxygenAdded(var1, var7.intX(), var7.intY(), var7.intZ());
                }
            }

            var6 = this.checked.iterator();

            while (var6.hasNext())
            {
                var7 = var6.next();

                if (var7.getBlockID(var1) == 0)
                {
                    if (var1.getBlockId(var7.intX(), var7.intY() + 1, var7.intZ()) == Block.doorWood.blockID)
                    {
                        var1.setBlock(var7.intX(), var7.intY() + 1, var7.intZ(), GCCoreBlocks.breatheableAir.blockID);
                    }

                    if (var1.getBlockId(var7.intX(), var7.intY() - 1, var7.intZ()) == Block.doorWood.blockID && var1.getBlockId(var7.intX(), var7.intY() - 2, var7.intZ()) != Block.doorWood.blockID)
                    {
                        var1.setBlock(var7.intX(), var7.intY() - 1, var7.intZ(), GCCoreBlocks.breatheableAir.blockID);
                    }

                    var1.setBlock(var7.intX(), var7.intY(), var7.intZ(), GCCoreBlocks.breatheableAir.blockID, 0, 2);
                }
            }
        }

        this.checked = new LinkedList<Vector3>();
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
            final Iterator<Vector3> var6 = this.checked.iterator();

            while (var6.hasNext())
            {
                final Vector3 var7 = var6.next();

                if (!this.getIsSealed(var1, var7.intX(), var7.intY(), var7.intZ()))
                {
                    this.airtight = false;
                }
            }
        }

        this.checked = new LinkedList<Vector3>();
        return this.airtight;
    }

    public void unSeal(World var1, int var2, int var3, int var4)
    {
        this.nextVecD(var1, var2, var3, var4);
        Iterator<Vector3> var5 = this.checked.iterator();
        Vector3 var6;

        while (var5.hasNext())
        {
            var6 = var5.next();

            if (this.canBlockPass(var1, var6))
            {
                var1.getBlockId(var6.intX(), var6.intY(), var6.intZ());

                Block.blocksList[var1.getBlockId(var6.intX(), var6.intY(), var6.intZ())].dropBlockAsItem(var1, var6.intX(), var6.intY(), var6.intZ(), var1.getBlockMetadata(var6.intX(), var6.intY(), var6.intZ()), 0);
                var1.setBlock(var6.intX(), var6.intY(), var6.intZ(), 0, 0, 2);
            }
        }

        var5 = this.checked.iterator();

        while (var5.hasNext())
        {
            var6 = var5.next();

            if (var1.getBlockId(var6.intX(), var6.intY(), var6.intZ()) == GCCoreBlocks.breatheableAir.blockID)
            {
                var1.setBlock(var6.intX(), var6.intY(), var6.intZ(), 0, 0, 2);
            }
        }

        var5 = this.oxygenReliantBlocks.iterator();

        while (var5.hasNext())
        {
            var6 = var5.next();

            final int idAt = var6.getBlockID(var1);

            if (idAt != 0 && Block.blocksList[idAt] instanceof IOxygenReliantBlock)
            {
                ((IOxygenReliantBlock) Block.blocksList[idAt]).onOxygenRemoved(var1, var6.intX(), var6.intY(), var6.intZ());
            }
        }

        var5 = this.checked.iterator();

        while (var5.hasNext())
        {
            var6 = var5.next();

            if (var6.getBlockID(var1) == 0)
            {
                var1.notifyBlocksOfNeighborChange(var6.intX(), var6.intY(), var6.intZ(), 0);
            }
        }

        this.oxygenReliantBlocks = new LinkedList<Vector3>();
        this.checked = new LinkedList<Vector3>();
    }

    public boolean canBlockPass(World var0, Vector3 vec)
    {
        final Block block = Block.blocksList[vec.getBlockID(var0)];

        return block == null || block.blockID == 0 || block.blockID == GCCoreBlocks.breatheableAir.blockID || OxygenPressureProtocol.vanillaPermeableBlocks.contains(block.blockID) || !block.isOpaqueCube() && !(block instanceof IPartialSealedBlock) && !(OxygenPressureProtocol.nonPermeableBlocks.containsKey(block.blockID) && OxygenPressureProtocol.nonPermeableBlocks.get(block.blockID).contains(vec.getBlockMetadata(var0))) || !block.isOpaqueCube() && block instanceof IPartialSealedBlock && !((IPartialSealedBlock) block).isSealed(var0, vec.intX(), vec.intY(), vec.intZ());
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

            if (this.canBlockPass(var1, vec) && !this.isVisited(vec))
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
        for (final Vector3 vec : this.checked)
        {
            if (vec.equals(var1))
            {
                return true;
            }
        }

        return false;
    }
}
