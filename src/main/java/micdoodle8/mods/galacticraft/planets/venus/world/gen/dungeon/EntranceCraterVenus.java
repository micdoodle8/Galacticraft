package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

import static micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures.CVENUS_DUNGEON_ENTRANCE;

public class EntranceCraterVenus extends SizedPieceVenus
{
    private final int range = 16;

    public EntranceCraterVenus(IStructurePieceType type, CompoundNBT nbt)
    {
        super(type, nbt);
    }

    public EntranceCraterVenus(IStructurePieceType type, DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ)
    {
        super(type, configuration, rand.nextInt(4) + 6, 12, rand.nextInt(4) + 6, Direction.Plane.HORIZONTAL.random(rand));
        this.setCoordBaseMode(Direction.SOUTH);

        this.boundingBox = new MutableBoundingBox(blockPosX - range, configuration.getYPosition() + 11, blockPosZ - range, blockPosX + range, 150, blockPosZ + range);
    }

    @Override
    public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn)
    {
        BlockState block1;

        int maxLevel = 0;

        for (int i = -range; i <= range; i++)
        {
            for (int k = -range; k <= range; k++)
            {
                int j = 150;
                int x = this.getXWithOffset(i + range, k + range);
                int z = this.getZWithOffset(i + range, k + range);

                while (j >= 0)
                {
                    j--;

                    int y = this.getYWithOffset(j);
                    BlockPos blockpos = new BlockPos(x, y, z);
                    Block block = worldIn.getBlockState(blockpos).getBlock();

                    if (Blocks.AIR != block)
                    {
                        break;
                    }
                }

                maxLevel = Math.max(maxLevel, j + 3);
            }
        }

        Mirror mirror = Mirror.NONE;
        Rotation rotation = Rotation.NONE;
        if (this.getCoordBaseMode() != null)
        {
            switch (this.getCoordBaseMode())
            {
            case SOUTH:
                mirror = Mirror.LEFT_RIGHT;
                break;
            case WEST:
                mirror = Mirror.LEFT_RIGHT;
                rotation = Rotation.CLOCKWISE_90;
                break;
            case EAST:
                rotation = Rotation.CLOCKWISE_90;
                break;
            default:
                break;
            }
        }

        for (int i = -range; i < range; i++)
        {
            for (int k = -range; k < range; k++)
            {
                final double xDev = i / 20D;
                final double zDev = k / 20D;
                final double distance = xDev * xDev + zDev * zDev;
                final int depth = (int) Math.abs(0.5 / (distance + .00001D));
                int helper = 0;
                for (int j = maxLevel; j > 1 && helper <= depth; j--)
                {
                    block1 = this.getBlockStateFromPos(worldIn, i + range, j, k + range, boundingBox);
//                    if (block1 == this.configuration.getBrickBlock() || j != this.sizeY)
                    {
                        BlockPos blockpos = new BlockPos(this.getXWithOffset(i + range, k + range), this.getYWithOffset(j), this.getZWithOffset(i + range, k + range));
                        BlockState state = Blocks.AIR.getDefaultState();

                        if (mirror != Mirror.NONE)
                        {
                            state = state.mirror(mirror);
                        }

                        if (rotation != Rotation.NONE)
                        {
                            state = state.rotate(rotation);
                        }

                        worldIn.setBlockState(blockpos, state, 2);
//                        this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), i + range, j, k + range, boundingBox);
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
        return new RoomEntranceVenus(this.configuration, rand, this.boundingBox.minX + this.boundingBox.getXSize() / 2, this.boundingBox.minZ + this.boundingBox.getZSize() / 2);
    }
}
