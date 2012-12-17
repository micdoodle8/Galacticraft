package micdoodle8.mods.galacticraft.core.wgen;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.StructureBoundingBox;
import net.minecraft.src.StructureStart;
import net.minecraft.src.World;

public abstract class GCCoreMapGenStructure extends GCCoreMapGenBase
{
    protected Map structureMap = new HashMap();

    @Override
    protected void recursiveGenerate(World par1World, int par2, int par3, int par4, int par5, int[] par6ArrayOfByte)
    {
        this.rand.nextInt();

        if (this.canSpawnStructureAtCoords(par2, par3))
        {
            final StructureStart var7 = this.getStructureStart(par2, par3);
            this.structureMap.put(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(par2, par3)), var7);
        }
    }

    public boolean generateStructuresInChunk(World par1World, Random par2Random, int par3, int par4)
    {
        final int var5 = (par3 << 4) + 8;
        final int var6 = (par4 << 4) + 8;
        boolean var7 = false;
        final Iterator var8 = this.structureMap.values().iterator();

        while (var8.hasNext())
        {
        	final StructureStart var9 = (StructureStart)var8.next();
        	
            if (var9 != null && var9.isSizeableStructure() && var9.getBoundingBox().intersectsWith(var5, var6, var5 + 15, var6 + 15))
            {
                var9.generateStructure(par1World, par2Random, new StructureBoundingBox(var5, var6, var5 + 15, var6 + 15));
                var7 = true;
            }
        }

        return var7;
    }
    
    protected List getCoordList()
    {
        return null;
    }

    protected abstract boolean canSpawnStructureAtCoords(int var1, int var2);

    protected abstract StructureStart getStructureStart(int var1, int var2);
}
