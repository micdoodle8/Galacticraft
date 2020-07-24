package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

import java.util.Random;

public abstract class DirectionalPiece extends Piece
{
    private Direction direction;

    public DirectionalPiece(IStructurePieceType type, CompoundNBT nbt)
    {
        super(type, nbt);
    }

    public DirectionalPiece(IStructurePieceType type, DungeonConfiguration configuration, Direction direction)
    {
        super(type, configuration);
        this.direction = direction;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public void setDirection(Direction direction)
    {
        this.direction = direction;
    }

    @Override
    protected void writeStructureToNBT(CompoundNBT tagCompound)
    {
        super.writeStructureToNBT(tagCompound);

        tagCompound.putInt("direction", this.direction.ordinal());
    }

    @Override
    protected void readStructureFromNBT(CompoundNBT nbt)
    {
        super.readStructureFromNBT(nbt);

        if (nbt.contains("direction"))
        {
            this.direction = Direction.byIndex(nbt.getInt("direction"));
        }
        else
        {
            this.direction = Direction.NORTH;
        }
    }

    public Piece getCorridor(Random rand, DungeonStart startPiece, int maxAttempts, boolean small)
    {
        Direction randomDir;
        int blockX;
        int blockZ;
        int sizeX;
        int sizeZ;
        boolean valid;
        int attempts = maxAttempts;
        int randDir = rand.nextInt(3);
        do
        {
            randomDir = Direction.byHorizontalIndex((getDirection().getOpposite().getHorizontalIndex() + 1 + randDir) % 4);
            MutableBoundingBox extension = getExtension(randomDir, this.configuration.getHallwayLengthMin() + rand.nextInt(this.configuration.getHallwayLengthMax() - this.configuration.getHallwayLengthMin()), 3);
            blockX = extension.minX;
            blockZ = extension.minZ;
            sizeX = extension.maxX - extension.minX;
            sizeZ = extension.maxZ - extension.minZ;
            valid = !startPiece.checkIntersection(extension);
            attempts--;
            randDir++;
        }
        while (!valid && attempts > 0);

        if (!valid)
        {
            return null;
        }

        return new Corridor(this.configuration, rand, blockX, blockZ, sizeX, small ? 3 : this.configuration.getHallwayHeight(), sizeZ, randomDir);
    }
}
