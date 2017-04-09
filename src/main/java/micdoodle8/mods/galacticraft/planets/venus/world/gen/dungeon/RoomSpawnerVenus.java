package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.Random;

public class RoomSpawnerVenus extends RoomEmptyVenus
{
    public RoomSpawnerVenus()
    {
    }

    public RoomSpawnerVenus(DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, EnumFacing entranceDir)
    {
        super(configuration, rand, blockPosX, blockPosZ, sizeX, sizeY, sizeZ, entranceDir);
    }

    @Override
    public boolean addComponentParts(World worldIn, Random random, StructureBoundingBox chunkBox)
    {
        if (super.addComponentParts(worldIn, random, chunkBox))
        {
            for (int i = 1; i <= this.sizeX - 1; ++i)
            {
                for (int j = 1; j <= this.sizeY - 1; ++j)
                {
                    for (int k = 1; k <= this.sizeZ - 1; ++k)
                    {
                        if (random.nextFloat() < 0.05F)
                        {
                            this.setBlockState(worldIn, Blocks.WEB.getDefaultState(), i, j, k, chunkBox);
                        }
                    }
                }
            }

            this.placeMobSpawner(worldIn, random, chunkBox, 1, 0, 1);
            this.placeMobSpawner(worldIn, random, chunkBox, this.sizeX - 1, 0, this.sizeZ - 1);

            return true;
        }

        return false;
    }

    private void placeMobSpawner(World worldIn, Random random, StructureBoundingBox chunkBox, int x, int y, int z)
    {
        this.setBlockState(worldIn, Blocks.MOB_SPAWNER.getDefaultState(), 1, 0, 1, boundingBox);
        BlockPos blockpos = new BlockPos(this.getXWithOffset(1, 1), this.getYWithOffset(0), this.getZWithOffset(1, 1));
        TileEntityMobSpawner spawner = (TileEntityMobSpawner) worldIn.getTileEntity(blockpos);

        if (spawner != null)
        {
            spawner.getSpawnerBaseLogic().setEntityId(getMob(random));
        }
    }

    private static ResourceLocation getMob(Random rand)
    {
        switch (rand.nextInt(4))
        {
        case 0:
            return new ResourceLocation(Constants.MOD_ID_CORE, "evolved_spider");
        case 1:
            return new ResourceLocation(Constants.MOD_ID_CORE, "evolved_creeper");
        case 2:
            return new ResourceLocation(Constants.MOD_ID_CORE, "evolved_skeleton");
        case 3:
        default:
            return new ResourceLocation(Constants.MOD_ID_CORE, "evolved_zombie");
        }
    }
}