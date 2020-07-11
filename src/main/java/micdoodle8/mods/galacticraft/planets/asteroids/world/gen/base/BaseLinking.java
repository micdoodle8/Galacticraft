package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

import static micdoodle8.mods.galacticraft.planets.asteroids.world.gen.AsteroidFeatures.CBASE_LINKING;

public class BaseLinking extends SizedPiece
{
    public BaseLinking(TemplateManager templateManager, CompoundNBT nbt)
    {
        super(CBASE_LINKING, nbt);
    }

    public BaseLinking(BaseConfiguration configuration, Random rand, int blockPosX, int blockPosY, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction direction)
    {
        super(CBASE_LINKING, configuration, sizeX, sizeY, sizeZ, direction);
        this.setCoordBaseMode(Direction.SOUTH);
        this.boundingBox = new MutableBoundingBox(blockPosX, blockPosY, blockPosZ, blockPosX + sizeX, blockPosY + sizeY, blockPosZ + sizeZ);
    }

    @Override
    public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn)
    {
        for (int i = 0; i < this.boundingBox.getXSize(); i++)
        {
            for (int j = 0; j < this.boundingBox.getYSize(); j++)
            {
                for (int k = 0; k < this.boundingBox.getZSize(); k++)
                {
                    if ((this.getDirection().getAxis() == Direction.Axis.Z && (i == 0 || i == this.boundingBox.getXSize() - 1)) ||
                            j == 0 || j == this.boundingBox.getYSize() - 1 ||
                            (this.getDirection().getAxis() == Direction.Axis.X && (k == 0 || k == this.boundingBox.getZSize() - 1)))
                    {
                        this.setBlockState(worldIn, this.configuration.getWallBlock(), i, j, k, this.boundingBox);
                    }
                    else
                    {
                        if (j == this.boundingBox.getYSize() - 2)
                        {
                            if (this.getDirection().getAxis() == Direction.Axis.Z && (k + 1) % 4 == 0 && (i == 1 || i == this.boundingBox.getXSize() - 2))
                            {
                                //TODO: windows or decor
//                                this.setBlockState(worldIn, GCBlocks.unlitTorch.getDefaultState().with(BlockUnlitTorch.FACING, i == 1 ? EnumFacing.WEST.getOpposite() : EnumFacing.EAST.getOpposite()), i, j, k, this.boundingBox);
                                continue;
                            }
                            else if (this.getDirection().getAxis() == Direction.Axis.X && (i + 1) % 4 == 0 && (k == 1 || k == this.boundingBox.getZSize() - 2))
                            {
//                                this.setBlockState(worldIn, GCBlocks.unlitTorch.getDefaultState().with(BlockUnlitTorch.FACING, k == 1 ? EnumFacing.NORTH.getOpposite() : EnumFacing.SOUTH.getOpposite()), i, j, k, this.boundingBox);
                                continue;
                            }
                        }

                        this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), i, j, k, this.boundingBox);
                    }
                }
            }
        }

        return true;
    }
}