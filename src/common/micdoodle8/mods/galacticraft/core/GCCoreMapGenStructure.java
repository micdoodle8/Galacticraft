package micdoodle8.mods.galacticraft.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.StructureBoundingBox;
import net.minecraft.src.StructureComponent;
import net.minecraft.src.StructureStart;
import net.minecraft.src.World;

public abstract class GCCoreMapGenStructure extends GCCoreMapGenBase
{
    /**
     * Used to store a list of all structures that have been recursively generated. Used so that during recursive
     * generation, the structure generator can avoid generating structures that intersect ones that have already been
     * placed.
     */
    protected Map structureMap = new HashMap();

    /**
     * Recursively called by generate() (generate) and optionally by itself.
     */
    @Override
	protected void recursiveGenerate(World par1World, int par2, int par3, int par4, int par5, int[] par6ArrayOfByte)
    {
        if (!this.structureMap.containsKey(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(par2, par3))))
        {
            this.rand.nextInt();

            if (this.canSpawnStructureAtCoords(par2, par3))
            {
                StructureStart var7 = this.getStructureStart(par2, par3);
                this.structureMap.put(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(par2, par3)), var7);
            }
        }
    }

    /**
     * Generates structures in specified chunk next to existing structures. Does *not* generate StructureStarts.
     */
    public boolean generateStructuresInChunk(World par1World, Random par2Random, int par3, int par4)
    {
        int var5 = (par3 << 4) + 8;
        int var6 = (par4 << 4) + 8;
        boolean var7 = false;
        Iterator var8 = this.structureMap.values().iterator();

        while (var8.hasNext())
        {
            StructureStart var9 = (StructureStart)var8.next();

            if (var9.isSizeableStructure() && var9.getBoundingBox().intersectsWith(var5, var6, var5 + 15, var6 + 15))
            {
                var9.generateStructure(par1World, par2Random, new StructureBoundingBox(var5, var6, var5 + 15, var6 + 15));
                var7 = true;
            }
        }

        return var7;
    }

    /**
     * Returns true if the structure generator has generated a structure located at the given position tuple.
     */
    public boolean hasStructureAt(int par1, int par2, int par3)
    {
        Iterator var4 = this.structureMap.values().iterator();

        while (var4.hasNext())
        {
            StructureStart var5 = (StructureStart)var4.next();

            if (var5.isSizeableStructure() && var5.getBoundingBox().intersectsWith(par1, par3, par1, par3))
            {
                Iterator var6 = var5.getComponents().iterator();

                while (var6.hasNext())
                {
                    StructureComponent var7 = (StructureComponent)var6.next();

                    if (var7.getBoundingBox().isVecInside(par1, par2, par3))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public ChunkPosition getNearestInstance(World par1World, int par2, int par3, int par4)
    {
        this.worldObj = par1World;
        this.rand.setSeed(par1World.getSeed());
        long var5 = this.rand.nextLong();
        long var7 = this.rand.nextLong();
        long var9 = (par2 >> 4) * var5;
        long var11 = (par4 >> 4) * var7;
        this.rand.setSeed(var9 ^ var11 ^ par1World.getSeed());
        this.recursiveGenerate(par1World, par2 >> 4, par4 >> 4, 0, 0, (int[])null);
        double var13 = Double.MAX_VALUE;
        ChunkPosition var15 = null;
        Iterator var16 = this.structureMap.values().iterator();
        ChunkPosition var19;
        int var21;
        int var20;
        double var23;
        int var22;

        while (var16.hasNext())
        {
            StructureStart var17 = (StructureStart)var16.next();

            if (var17.isSizeableStructure())
            {
                StructureComponent var18 = (StructureComponent)var17.getComponents().get(0);
                var19 = var18.getCenter();
                var20 = var19.x - par2;
                var21 = var19.y - par3;
                var22 = var19.z - par4;
                var23 = var20 + var20 * var21 * var21 + var22 * var22;

                if (var23 < var13)
                {
                    var13 = var23;
                    var15 = var19;
                }
            }
        }

        if (var15 != null)
        {
            return var15;
        }
        else
        {
            List var25 = this.getCoordList();

            if (var25 != null)
            {
                ChunkPosition var26 = null;
                Iterator var27 = var25.iterator();

                while (var27.hasNext())
                {
                    var19 = (ChunkPosition)var27.next();
                    var20 = var19.x - par2;
                    var21 = var19.y - par3;
                    var22 = var19.z - par4;
                    var23 = var20 + var20 * var21 * var21 + var22 * var22;

                    if (var23 < var13)
                    {
                        var13 = var23;
                        var26 = var19;
                    }
                }

                return var26;
            }
            else
            {
                return null;
            }
        }
    }

    /**
     * Returns a list of other locations at which the structure generation has been run, or null if not relevant to this
     * structure generator.
     */
    protected List getCoordList()
    {
        return null;
    }

    protected abstract boolean canSpawnStructureAtCoords(int var1, int var2);

    protected abstract StructureStart getStructureStart(int var1, int var2);
}
