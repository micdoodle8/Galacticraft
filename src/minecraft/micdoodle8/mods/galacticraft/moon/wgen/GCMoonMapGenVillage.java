package micdoodle8.mods.galacticraft.moon.wgen;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

public class GCMoonMapGenVillage extends MapGenStructure
{
    /** A list of all the biomes villages can spawn in. */
    public static List villageSpawnBiomes = Arrays.asList(new BiomeGenBase[] {GCMoonBiomeGenBase.moonFlat});

    /** World terrain type, 0 for normal, 1 for flat map */
    private final int terrainType;
    private final int field_82665_g;
    private final int field_82666_h;

    public GCMoonMapGenVillage()
    {
        this.terrainType = 0;
        this.field_82665_g = 32;
        this.field_82666_h = 8;
    }

    @Override
	protected boolean canSpawnStructureAtCoords(int i, int j)
    {
        final byte numChunks = 32;
        final byte offsetChunks = 8;
        final int oldi = i;
        final int oldj = j;

        if (i < 0)
        {
            i -= numChunks - 1;
        }

        if (j < 0)
        {
            j -= numChunks - 1;
        }

        int randX = i / numChunks;
        int randZ = j / numChunks;
        final Random var7 = this.worldObj.setRandomSeed(i, j, 10387312);
        randX *= numChunks;
        randZ *= numChunks;
        randX += var7.nextInt(numChunks - offsetChunks);
        randZ += var7.nextInt(numChunks - offsetChunks);

        if (oldi == randX && oldj == randZ)
        {
            return true;
        }

        return false;
    }

    @Override
	protected StructureStart getStructureStart(int par1, int par2)
    {
        return new GCMoonStructureVillageStart(this.worldObj, this.rand, par1, par2, this.terrainType);
    }
}
