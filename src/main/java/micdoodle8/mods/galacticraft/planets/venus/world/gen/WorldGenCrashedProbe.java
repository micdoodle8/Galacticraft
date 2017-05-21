package micdoodle8.mods.galacticraft.planets.venus.world.gen;

import micdoodle8.mods.galacticraft.core.event.LootHandlerGC;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityCrashedProbe;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenCrashedProbe extends WorldGenerator
{
    public WorldGenCrashedProbe()
    {
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        for (position = position.add(-8, 0, -8); position.getY() > 5 && worldIn.isAirBlock(position); position = position.down())
        {
            ;
        }

        if (position.getY() <= 4)
        {
            return false;
        }

        int radius = 5 + rand.nextInt(4);
        int radiusSq = radius * radius;

        // Check this crater doesn't cut into a mountain
        for (int poolX = -radius; poolX <= radius; poolX++)
        {
            for (int poolY = -radius; poolY <= Math.min(256 - position.getY(), 50); poolY++)
            {
                for (int poolZ = -radius; poolZ <= radius; poolZ++)
                {
                    BlockPos pos = new BlockPos(poolX + position.getX(), poolY + position.getY(), poolZ + position.getZ());
                    if (poolY > 15 && !worldIn.getBlockState(pos).getBlock().isAir(worldIn.getBlockState(pos), worldIn, pos))
                    {
                        return false;
                    }
                }
            }
        }

        for (int poolX = -radius - 1; poolX <= radius + 1; poolX++)
        {
            for (int poolY = -radius - 1; poolY <= 256 - position.getY(); poolY++)
            {
                for (int poolZ = -radius - 1; poolZ <= radius + 1; poolZ++)
                {
                    int distance = poolX * poolX + Math.min(0, poolY) * Math.min(0, poolY) + poolZ * poolZ;

                    BlockPos pos = new BlockPos(poolX + position.getX(), poolY + position.getY(), poolZ + position.getZ());
                    if (distance <= radiusSq)
                    {
                        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                    }
                    else if (worldIn.getBlockState(pos).getBlock() == VenusBlocks.venusBlock && poolY < 0 && rand.nextInt(5) == 0)
                    {
                        worldIn.setBlockState(pos, VenusBlocks.scorchedRock.getDefaultState(), 2);
                    }
                }
            }
        }

        BlockPos blockpos = position.add(0, -radius + 1, 0);
        worldIn.setBlockState(blockpos, VenusBlocks.crashedProbe.getDefaultState(), 3);
        TileEntityCrashedProbe probe = (TileEntityCrashedProbe) worldIn.getTileEntity(blockpos);

        if (probe != null)
        {
            for (int i = 0; i < probe.getSizeInventory(); ++i)
            {
                // Clear contents
                probe.setInventorySlotContents(i, ItemStack.EMPTY);
            }

            probe.setLootTable(LootHandlerGC.TABLE_CRASHED_PROBE, rand.nextLong());
            probe.setDropCore();
        }

        return true;
    }
}