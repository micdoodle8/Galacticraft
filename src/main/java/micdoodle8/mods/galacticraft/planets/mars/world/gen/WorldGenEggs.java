package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import com.mojang.datafixers.Dynamic;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockBasicMars;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockSlimelingEgg;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.function.Function;

public class WorldGenEggs extends Feature<NoFeatureConfig>
{
    private final BlockState eggBlock;

    public WorldGenEggs(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn, BlockState eggIn)
    {
        super(configFactoryIn);
        this.eggBlock = eggIn;
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
    {
        int i1 = pos.getX() + rand.nextInt(8) - rand.nextInt(8);
        int j1 = pos.getY() + rand.nextInt(4) - rand.nextInt(4);
        int k1 = pos.getZ() + rand.nextInt(8) - rand.nextInt(8);
        BlockPos newPos = new BlockPos(i1, j1, k1);

        if (!worldIn.isBlockLoaded(newPos.add(1, 0, 1)))
        {
            return false;
        }

        if (worldIn.isAirBlock(newPos) && (j1 < 127 || !worldIn.getDimension().isNether()))
        {
            BlockState below = worldIn.getBlockState(newPos.down());
            if (below.getBlock() == MarsBlocks.rockSurface)
            {
                worldIn.setBlockState(newPos, this.eggBlock.with(BlockSlimelingEgg.EGG_COLOR, BlockSlimelingEgg.EnumEggColor.values()[rand.nextInt(3)]), 2);
            }
        }

        return true;
    }
}
