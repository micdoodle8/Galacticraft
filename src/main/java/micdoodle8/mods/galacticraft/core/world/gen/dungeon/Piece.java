package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;

import java.util.Random;

public abstract class Piece extends StructurePiece
{
    protected DungeonConfiguration configuration;

    public Piece(IStructurePieceType type)
    {
        super(type, 0);
    }

    public Piece(IStructurePieceType type, DungeonConfiguration configuration)
    {
        this(type);
        this.configuration = configuration;
    }

    public Piece(IStructurePieceType type, CompoundNBT tagCompound)
    {
        super(type, tagCompound);
        this.readStructureFromNBT(tagCompound);
    }

    @Override
    protected void readAdditional(CompoundNBT tagCompound)
    {
        // This is actually write, incorrect name mapping
        this.writeStructureToNBT(tagCompound);
    }

    protected void writeStructureToNBT(CompoundNBT tagCompound)
    {
        this.configuration.writeToNBT(tagCompound);
    }

    protected void readStructureFromNBT(CompoundNBT tagCompound)
    {
        if (this.configuration == null)
        {
            this.configuration = new DungeonConfiguration();
            this.configuration.readFromNBT(tagCompound);
        }
    }

    protected MutableBoundingBox getExtension(Direction direction, int length, int width)
    {
        int blockX, blockZ, sizeX, sizeZ;
        switch (direction)
        {
        case NORTH:
            sizeX = width;
            sizeZ = length;
            blockX = this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2 - sizeX / 2;
            blockZ = this.boundingBox.minZ - sizeZ;
            break;
        case EAST:
            sizeX = length;
            sizeZ = width;
            blockX = this.boundingBox.maxX;
            blockZ = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - sizeZ / 2;
            break;
        case SOUTH:
            sizeX = width;
            sizeZ = length;
            blockX = this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2 - sizeX / 2;
            blockZ = this.boundingBox.maxZ;
            break;
        case WEST:
        default:
            sizeX = length;
            sizeZ = width;
            blockX = this.boundingBox.minX - sizeX;
            blockZ = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - sizeZ / 2;
            break;
        }
        return new MutableBoundingBox(blockX, this.configuration.getYPosition(), blockZ, blockX + sizeX, this.configuration.getYPosition() + this.configuration.getHallwayHeight(), blockZ + sizeZ);
    }

    public Piece getNextPiece(DungeonStart startPiece, Random rand)
    {
        return null;
    }
}