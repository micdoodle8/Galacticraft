package micdoodle8.mods.galacticraft.planets.venus.world.gen.layer;

import micdoodle8.mods.miccore.IntCache;
import net.minecraft.world.gen.layer.GenLayer;

public class GenLayerVenusSurround extends GenLayer
{
    public GenLayerVenusSurround(long l, GenLayer parent)
    {
        super(l);
        this.parent = parent;
    }

    public GenLayerVenusSurround(long l)
    {
        super(l);
    }

    @Override
    public int[] getInts(int x, int z, int width, int depth)
    {
        int nx = x - 1;
        int nz = z - 1;
        int nwidth = width + 2;
        int ndepth = depth + 2;
        int input[] = parent.getInts(nx, nz, nwidth, ndepth);
        int output[] = IntCache.getIntCache(width * depth);
        for (int dz = 0; dz < depth; dz++)
        {
            for (int dx = 0; dx < width; dx++)
            {
                int right = input[dx + 0 + (dz + 1) * nwidth];
                int left = input[dx + 2 + (dz + 1) * nwidth];
                int up = input[dx + 1 + (dz + 0) * nwidth];
                int down = input[dx + 1 + (dz + 2) * nwidth];
                int center = input[dx + 1 + (dz + 1) * nwidth];
//                if (surrounded(BiomeGenBaseVenus.venusMountain.biomeID, center, right, left, up, down) && nextInt(25) == 0)
//                {
//                    output[dx + dz * width] = BiomeGenBaseVenus.venusVolcano.biomeID;
//                }
//                else
                {
                    output[dx + dz * width] = center;
                }
            }
        }

        return output;
    }

    boolean surrounded(int biome, int center, int right, int left, int up, int down)
    {
        if (center != biome)
        {
            return false;
        }

        if (right != biome)
        {
            return false;
        }
        if (left != biome)
        {
            return false;
        }
        if (up != biome)
        {
            return false;
        }
        if (down != biome)
        {
            return false;
        }

        return true;
    }
}
