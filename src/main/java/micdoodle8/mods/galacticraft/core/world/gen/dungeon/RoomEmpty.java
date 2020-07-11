package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

import static micdoodle8.mods.galacticraft.core.world.gen.GCFeatures.CMOON_DUNGEON_EMPTY;

public class RoomEmpty extends SizedPiece
{
    public RoomEmpty(TemplateManager templateManager, CompoundNBT nbt)
    {
        super(CMOON_DUNGEON_EMPTY, nbt);
    }

    public RoomEmpty(IStructurePieceType type, CompoundNBT nbt)
    {
        super(type, nbt);
    }

    public RoomEmpty(DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir)
    {
        this(CMOON_DUNGEON_EMPTY, configuration, rand, blockPosX, blockPosZ, sizeX, sizeY, sizeZ, entranceDir);
    }

    protected RoomEmpty(IStructurePieceType type, DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir)
    {
        super(type, configuration, sizeX, sizeY, sizeZ, entranceDir.getOpposite());
        this.setCoordBaseMode(Direction.SOUTH);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        int yPos = configuration.getYPosition();

        this.boundingBox = new MutableBoundingBox(blockPosX, yPos, blockPosZ, blockPosX + this.sizeX, yPos + this.sizeY, blockPosZ + this.sizeZ);
    }

    @Override
    public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn)
    {
        for (int i = 0; i <= this.sizeX; i++)
        {
            for (int j = 0; j <= this.sizeY; j++)
            {
                for (int k = 0; k <= this.sizeZ; k++)
                {
                    if (i == 0 || i == this.sizeX || j == 0 || j == this.sizeY || k == 0 || k == this.sizeZ)
                    {
                        boolean placeBlock = true;
                        if (getDirection().getAxis() == Direction.Axis.Z)
                        {
                            int start = (this.boundingBox.maxX - this.boundingBox.minX) / 2 - 1;
                            int end = (this.boundingBox.maxX - this.boundingBox.minX) / 2 + 1;
                            if (i > start && i <= end && j < this.configuration.getHallwayHeight() && j > 0)
                            {
                                if (getDirection() == Direction.SOUTH && k == 0)
                                {
                                    placeBlock = false;
                                }
                                else if (getDirection() == Direction.NORTH && k == this.sizeZ)
                                {
                                    placeBlock = false;
                                }
                            }
                        }
                        else
                        {
                            int start = (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - 1;
                            int end = (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1;
                            if (k > start && k <= end && j < this.configuration.getHallwayHeight() && j > 0)
                            {
                                if (getDirection() == Direction.EAST && i == 0)
                                {
                                    placeBlock = false;
                                }
                                else if (getDirection() == Direction.WEST && i == this.sizeX)
                                {
                                    placeBlock = false;
                                }
                            }
                        }
                        if (placeBlock)
                        {
                            this.setBlockState(worldIn, this.configuration.getBrickBlock(), i, j, k, boundingBox);
                        }
                        else
                        {
                            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), i, j, k, boundingBox);
                        }
                    }
                    else
                    {
                        this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), i, j, k, boundingBox);
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Piece getNextPiece(DungeonStart startPiece, Random rand)
    {
        if (Math.abs(startPiece.getBoundingBox().maxZ - boundingBox.minZ) > 200)
        {
            return null;
        }

        if (Math.abs(startPiece.getBoundingBox().maxX - boundingBox.minX) > 200)
        {
            return null;
        }

        return getCorridor(rand, startPiece, 10, false);
    }
}