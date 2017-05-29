package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.Random;

public class RoomEntranceVenus extends SizedPieceVenus
{
    private final int range = 4;

    public RoomEntranceVenus()
    {
    }

    public RoomEntranceVenus(World world, DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ)
    {
        super(configuration, rand.nextInt(4) + 6, rand.nextInt(2) + 5, rand.nextInt(4) + 6, EnumFacing.Plane.HORIZONTAL.random(rand));
        this.coordBaseMode = EnumFacing.SOUTH;

        this.boundingBox = new StructureBoundingBox(blockPosX - range, configuration.getYPosition(), blockPosZ - range, blockPosX + range, 150, blockPosZ + range);
    }

    @Override
    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
    {
        IBlockState block1;

        int maxLevel = 0;

        for (int i = -range; i <= range; i++)
        {
            for (int k = -range; k <= range; k++)
            {
                int j = this.boundingBox.getYSize();

                while (j >= 0)
                {
                    j--;

                    Block block = getBlockStateFromPos(worldIn, i + range, j, k + range, boundingBox).getBlock();

                    if (Blocks.air != block && block != null)
                    {
                        break;
                    }
                }

                maxLevel = Math.max(maxLevel, j + 3);
            }
        }

        int startX = range - this.sizeX / 2;
        int startZ = range - this.sizeZ / 2;
        int endX = range + this.sizeX / 2;
        int endZ = range + this.sizeZ / 2;

        for (int i = startX; i <= endX; i++)
        {
            for (int j = 0; j <= this.sizeY; j++)
            {
                for (int k = startZ; k <= endZ; k++)
                {
                    if (i == startX || i == endX || j == 0 || j == this.sizeY || k == startZ || k == endZ)
                    {
                        this.setBlockState(worldIn, j == 0 || j == this.sizeY ? this.configuration.getBrickBlockFloor() : this.configuration.getBrickBlock(), i, j, k, boundingBox);
                    }
                    else
                    {
                        this.setBlockState(worldIn, Blocks.air.getDefaultState(), i, j, k, boundingBox);
                    }
                }
            }
        }

        for (int i = -range; i < range; i++)
        {
            for (int k = -range; k < range; k++)
            {
                final double xDev = i / 15D;
                final double zDev = k / 15D;
                final double distance = xDev * xDev + zDev * zDev;
                final int depth = (int) Math.abs(0.5 / (distance + .00001D));
                int helper = 0;
                for (int j = maxLevel; j > 1 && helper <= depth; j--)
                {
                    block1 = this.getBlockStateFromPos(worldIn, i + range, j, k + range, boundingBox);
                    if (block1 == this.configuration.getBrickBlockFloor() || j != this.sizeY)
                    {
                        this.setBlockState(worldIn, Blocks.air.getDefaultState(), i + range, j, k + range, boundingBox);
                        helper++;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public PieceVenus getNextPiece(DungeonStartVenus startPiece, Random rand)
    {
        if (startPiece.attachedComponents.isEmpty())
        {
            return getCorridor(rand, startPiece, 10, false);
        }

        return null;
    }

    @Override
    protected StructureBoundingBox getExtension(EnumFacing direction, int length, int width)
    {
        int blockX, blockZ, sizeX, sizeZ;
        int startX = this.boundingBox.minX + range - this.sizeX / 2;
        int startZ = this.boundingBox.minZ + range - this.sizeZ / 2;
        int endX = this.boundingBox.minX + range + this.sizeX / 2;
        int endZ = this.boundingBox.minZ + range + this.sizeZ / 2;
        switch (direction)
        {
        case NORTH:
            sizeX = width;
            sizeZ = length;
            blockX = startX + (endX - startX) / 2 - sizeX / 2;
            blockZ = startZ - sizeZ;
            break;
        case EAST:
            sizeX = length;
            sizeZ = width;
            blockX = endX;
            blockZ = startZ + (endZ - startZ) / 2 - sizeZ / 2;
            break;
        case SOUTH:
            sizeX = width;
            sizeZ = length;
            blockX = startX + (endX - startX) / 2 - sizeX / 2;
            blockZ = endZ;
            break;
        case WEST:
        default:
            sizeX = length;
            sizeZ = width;
            blockX = startX - sizeX;
            blockZ = startZ + (endZ - startZ) / 2 - sizeZ / 2;
            break;
        }
        return new StructureBoundingBox(blockX, blockZ, blockX + sizeX, blockZ + sizeZ);
    }
}