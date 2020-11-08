package micdoodle8.mods.galacticraft.core.world.gen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.Dynamic;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class CraterFeature extends Feature<NoFeatureConfig>
{
    private CraterGenerator craterGen;

    public CraterFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
    {
        super(configFactory);
    }

    public void init(long seed)
    {
        Random craterRand = new Random(seed);
        craterGen = new CraterGenerator();
        craterGen.addCraters(1250, 200, 50, craterRand, 20, 90, 0);
        craterGen.addCraters(1600, 65, 20, craterRand, 7, 15, 16 * 52);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
    {
        this.makeCraters(worldIn, pos, craterGen.getCentres(pos));
        return true;
    }

    private void makeCraters(IWorld world, BlockPos pos, List<WorldCrater> craters)
    {
        if (craters.isEmpty()) return;

        BlockPos.Mutable mutable = new BlockPos.Mutable(pos);
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                double toDig = 0;
                boolean largestIsFresh = false;

                for (WorldCrater crater : craters)
                {
                    double xDev = pos.getX() + x - crater.blockX;
                    double zDev = pos.getZ() + z - crater.blockZ;
                    if (xDev * xDev + zDev * zDev < crater.getRadius() * crater.getRadius())
                    {
                        xDev /= crater.getRadius();
                        zDev /= crater.getRadius();
                        final double sqrtY = xDev * xDev + zDev * zDev;
                        double yDev = sqrtY * sqrtY * 6;
                        double craterDepth = 5 - yDev;
                        craterDepth *= crater.getDepthMultiplier();
                        if (craterDepth > 0)
                        {
                            toDig += craterDepth;
                        }
                        largestIsFresh = largestIsFresh || crater.isFresh();
                    }
                }

                if (toDig > 0) toDig++; // Increase crater depth, but for sum, not each crater
                if (largestIsFresh) toDig++; // Dig one more block, because we're not replacing the top with turf

                int dug = 0;
                for (int y = 127; y > 0 && dug < toDig; y--)
                {
                    mutable.setY(y);
                    mutable.setX(pos.getX() + x);
                    mutable.setZ(pos.getZ() + z);
                    if (Blocks.AIR != world.getBlockState(mutable).getBlock())
                    {
                        this.setBlockState(world, mutable, Blocks.AIR.getDefaultState());
                        if (++dug >= toDig && !largestIsFresh)
                        {
                            this.setBlockState(world, mutable.down(), GCBlocks.moonTurf.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    private static class WorldCrater
    {
        private int blockX;
        private int blockZ;
        private int radius;
        private double depthMult;
        private boolean isFresh;

        public WorldCrater(int blockX, int blockZ, int radius, double depthMult, boolean isFresh)
        {
            this.blockX = blockX;
            this.blockZ = blockZ;
            this.radius = radius;
            this.depthMult = depthMult;
            this.isFresh = isFresh;
        }

        public int getBlockX()
        {
            return blockX;
        }

        public int getBlockZ()
        {
            return blockZ;
        }

        public int getRadius()
        {
            return radius;
        }

        public double getDepthMultiplier()
        {
            return depthMult;
        }

        public boolean isFresh()
        {
            return isFresh;
        }
    }

    private static class LocalCraterBB
    {
        private float minX;
        private float maxX;
        private float minZ;
        private float maxZ;
        private int offset;
        private float depthMult;

        public LocalCraterBB(float minX, float maxX, float minZ, float maxZ, int offset, float depthMult)
        {
            this.minX = minX;
            this.maxX = maxX;
            this.minZ = minZ;
            this.maxZ = maxZ;
            this.offset = offset;
            this.depthMult = depthMult;
        }

        public float getCentreX()
        {
            float cX = minX + (maxX - minX) / 2.0F;
            if (cX < 0.0F) cX += 1.0F;
            if (cX > 1.0F) cX -= 1.0F;
            return cX;
        }

        public float getCentreZ()
        {
            float cZ = minZ + (maxZ - minZ) / 2.0F;
            if (cZ < 0.0F) cZ += 1.0F;
            if (cZ > 1.0F) cZ -= 1.0F;
            return cZ;
        }

        public boolean intersects(float inMinX, float inMaxX, float inMinZ, float inMaxZ)
        {
            return inMinX <= maxX && inMaxX >= minX && inMinZ <= maxZ && inMaxZ >= minZ;
        }

        public float getSize()
        {
            return maxX - minX; // sizeX and sizeZ are equal
        }
    }

    private static class CraterGenerator
    {
        private final List<LocalCraterBB> craterList = Lists.newArrayList();
        private final List<LocalCraterBB> freshCraterList = Lists.newArrayList();
        private static final int REPEAT_SIZE = 16 * 125; // Must be multiple of 16
        private static final float SCALE = 1.0F / REPEAT_SIZE;

        public void addCraters(int numCraters, int numDeepCraters, int numFreshCraters, Random random, int minSize, int maxSize, int offset)
        {
            for (int i = 0; i < numCraters; ++i)
            {
                float size = (minSize + (maxSize - minSize) * random.nextFloat()) * SCALE;
                float startX = random.nextFloat() - size / 2.0F;
                float startZ = random.nextFloat() - size / 2.0F;
                if (startX > 0.0F && startX + size < 1.0F && startZ > 0.0F && startZ + size < 1.0F)
                {
                    List<LocalCraterBB> list = i < numFreshCraters ? freshCraterList : craterList;
                    list.add(new LocalCraterBB(startX, startX + size, startZ, startZ + size, offset, i >= numFreshCraters && i < numFreshCraters + numDeepCraters ? 1.75F : 1.0F));
                }
            }
        }

        public List<WorldCrater> getCentres(BlockPos pos)
        {
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            List<WorldCrater> res = Lists.newArrayList();
            for (List<LocalCraterBB> list : ImmutableList.of(craterList, freshCraterList))
            {
                for (LocalCraterBB bb : list)
                {
                    BlockPos posIn = new BlockPos(pos.getX() + bb.offset, pos.getY(), pos.getZ() + bb.offset);
                    BlockPos modPosIn = new BlockPos(Math.floorMod(posIn.getX(), REPEAT_SIZE), posIn.getY(), Math.floorMod(posIn.getZ(), REPEAT_SIZE));
                    if (bb.intersects(modPosIn.getX() * SCALE, (modPosIn.getX() + 15) * SCALE, modPosIn.getZ() * SCALE, (modPosIn.getZ() + 15) * SCALE))
                    {
                        mutable.setPos(Math.round(bb.getCentreX() * REPEAT_SIZE) + (pos.getX() - modPosIn.getX()), 8, Math.round(bb.getCentreZ() * REPEAT_SIZE) + (pos.getZ() - modPosIn.getZ()));
                        int rad = Math.round((bb.getSize() / 2.0F) * REPEAT_SIZE);
                        double depthMult = (0.5 + rad / 45.0) * bb.depthMult;
                        res.add(new WorldCrater(mutable.getX(), mutable.getZ(), rad, depthMult, list == freshCraterList));
                    }
                }
            }
            return res;
        }
    }
}
