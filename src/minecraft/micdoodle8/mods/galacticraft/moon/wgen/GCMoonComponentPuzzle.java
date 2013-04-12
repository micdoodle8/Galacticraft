package micdoodle8.mods.galacticraft.moon.wgen;

import java.util.Random;

import micdoodle8.mods.galacticraft.moon.blocks.GCMoonBlocks;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class GCMoonComponentPuzzle extends GCMoonComponent
{
    static int MSIZE = 16;
    static int RADIUS = GCMoonComponentPuzzle.MSIZE / 2 * 3 + 1;
    static int DIAMETER = 2 * GCMoonComponentPuzzle.RADIUS;
    static int FLOOR_LEVEL = 3;

    public GCMoonComponentPuzzle(World var1, Random var2, int var3, int var4, int var5, int var6)
    {
        super(var3);
        this.coordBaseMode = 0;
        this.boundingBox = GCMoonComponent.getComponentToAddBoundingBox(var4, var5, var6, -GCMoonComponentPuzzle.RADIUS, -3, -GCMoonComponentPuzzle.RADIUS, GCMoonComponentPuzzle.RADIUS * 2, 10, GCMoonComponentPuzzle.RADIUS * 2, 0);
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    @Override
	public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3)
    {
        final GCMoonPuzzle var4 = new GCMoonPuzzle(GCMoonComponentPuzzle.MSIZE, GCMoonComponentPuzzle.MSIZE);
        var4.oddBias = 2;
        var4.wallBlockID = GCMoonBlocks.blockMoon.blockID;
        var4.wallBlockMeta = 14;
        var4.type = 4;
        var4.tall = 3;
        var4.roots = 3;
        var4.setSeed(var1.getSeed() + this.boundingBox.minX * this.boundingBox.minZ);
        int var5;

        for (var5 = 0; var5 <= GCMoonComponentPuzzle.DIAMETER; ++var5)
        {
            for (int var6 = 0; var6 <= GCMoonComponentPuzzle.DIAMETER; ++var6)
            {
            	for (int var7 = -1; var7 <= GCMoonComponentPuzzle.FLOOR_LEVEL + 2; var7++)
            	{
                    this.placeBlockAtCurrentPosition(var1, 0, 0, var5, var7, var6, var3);
            	}
            }
        }

        for (var5 = 0; var5 <= GCMoonComponentPuzzle.DIAMETER; ++var5)
        {
            for (int var6 = 0; var6 <= GCMoonComponentPuzzle.DIAMETER; ++var6)
            {
                this.placeBlockAtCurrentPosition(var1, Block.grass.blockID, 0, var5, GCMoonComponentPuzzle.FLOOR_LEVEL - 1, var6, var3);
            }
        }

        var5 = GCMoonComponentPuzzle.MSIZE / 3;
        final int[] var10 = new int[var5 * 2];

        for (int var7 = 0; var7 < var5; ++var7)
        {
            int var8;
            int var9;

            do
            {
                var8 = var4.rand.nextInt(GCMoonComponentPuzzle.MSIZE - 2) + 1;
                var9 = var4.rand.nextInt(GCMoonComponentPuzzle.MSIZE - 2) + 1;
            }
            while (this.isNearRoom(var8, var9, var10));

            var4.carveRoom1(var8, var9);
            var10[var7 * 2] = var8;
            var10[var7 * 2 + 1] = var9;
        }

        var4.generateRecursiveBacktracker(0, 0);
        var4.add4Exits();
        var4.copyToStructure(var1, var2, 1, GCMoonComponentPuzzle.FLOOR_LEVEL, 1, this, var3);
        this.decorate3x3Rooms(var1, var10, var3);
        return true;
    }

    protected boolean isNearRoom(int var1, int var2, int[] var3)
    {
        if (var1 == 1 && var2 == 1)
        {
            return true;
        }
        else
        {
            for (int var4 = 0; var4 < var3.length / 2; ++var4)
            {
                final int var5 = var3[var4 * 2];
                final int var6 = var3[var4 * 2 + 1];

                if ((var5 != 0 || var6 != 0) && Math.abs(var1 - var5) < 3 && Math.abs(var2 - var6) < 3)
                {
                    return true;
                }
            }

            return false;
        }
    }

    void decorate3x3Rooms(World var1, int[] var2, StructureBoundingBox var3)
    {
        for (int var4 = 0; var4 < var2.length / 2; ++var4)
        {
            int var5 = var2[var4 * 2];
            int var6 = var2[var4 * 2 + 1];
            var5 = var5 * 3 + 3;
            var6 = var6 * 3 + 3;
            this.decorate3x3Room(var1, var5, var6, var3);
        }
    }

    void decorate3x3Room(World var1, int var2, int var3, StructureBoundingBox var4)
    {
        final Random var5 = new Random(var1.getSeed() ^ var2 + var3);

        this.roomSpawner(var1, var5, var2, var3, 8, var4);
    }

    private void roomSpawner(World var1, Random var2, int var3, int var4, int var5, StructureBoundingBox var6)
    {
        final int var7 = var3 + var2.nextInt(var5) - var5 / 2;
        final int var8 = var4 + var2.nextInt(var5) - var5 / 2;
        final String var9 = "Evolved Skeleton";

        this.placeSpawnerAtCurrentPosition(var1, var2, var7, GCMoonComponentPuzzle.FLOOR_LEVEL, var8, var9, var6);
    }
}
