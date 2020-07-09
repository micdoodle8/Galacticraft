package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public abstract class SizedPiece extends Piece
{
    protected Direction direction;
    protected int sizeX;
    protected int sizeY;
    protected int sizeZ;

    public SizedPiece(IStructurePieceType type, CompoundNBT nbt)
    {
        super(type, nbt);
    }

    public SizedPiece(IStructurePieceType type, BaseConfiguration configuration, int sizeX, int sizeY, int sizeZ, Direction direction)
    {
        super(type, configuration);
        this.direction = direction;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
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

        tagCompound.putInt("dir", this.direction.ordinal());
        tagCompound.putInt("sX", this.sizeX);
        tagCompound.putInt("sY", this.sizeY);
        tagCompound.putInt("sZ", this.sizeZ);
    }

    @Override
    protected void readStructureFromNBT(CompoundNBT tagCompound)
    {
        super.readStructureFromNBT(tagCompound);

        this.sizeX = tagCompound.getInt("sX");
        this.sizeY = tagCompound.getInt("sY");
        this.sizeZ = tagCompound.getInt("sZ");

        if (tagCompound.contains("dir"))
        {
            this.direction = Direction.byIndex(tagCompound.getInt("dir"));
        }
        else
        {
            this.direction = Direction.NORTH;
        }
    }

    public int getSizeX()
    {
        return sizeX;
    }

    public int getSizeY()
    {
        return sizeY;
    }

    public int getSizeZ()
    {
        return sizeZ;
    }

    @Override
    protected int getXWithOffset(int x, int z)
    {
        if (this.getCoordBaseMode() == null)
        {
            return x;
        }
        else
        {
            switch (this.getCoordBaseMode())
            {
            case NORTH:
                return this.boundingBox.minX + x;
            case SOUTH:
                return this.boundingBox.maxX - x;
            case WEST:
                return this.boundingBox.maxX - z;
            case EAST:
                return this.boundingBox.minX + z;
            default:
                return x;
            }
        }
    }

    @Override
    protected int getZWithOffset(int x, int z)
    {
        if (this.getCoordBaseMode() == null)
        {
            return z;
        }
        else
        {
            switch (this.getCoordBaseMode())
            {
            case NORTH:
                return this.boundingBox.minZ + z;
            case SOUTH:
                return this.boundingBox.maxZ - z;
            case WEST:
                return this.boundingBox.minZ + x;
            case EAST:
                return this.boundingBox.maxZ - x;
            default:
                return z;
            }
        }
    }

    //Unused currently
    public Piece getDoorway(Random rand, BaseStart startPiece, int maxAttempts, boolean small)
    {
        Direction randomDir;
        int blockX;
        int blockZ;
        int sizeX;
        int sizeZ;
        boolean valid;
        int attempts = maxAttempts;
        do
        {
            int randDir = rand.nextInt(4);
            randomDir = Direction.byHorizontalIndex((randDir == getDirection().getOpposite().getHorizontalIndex() ? randDir + 1 : randDir) % 4);
            MutableBoundingBox extension = getExtension(randomDir, 1, 3);
            blockX = extension.minX;
            blockZ = extension.minZ;
            sizeX = extension.maxX - extension.minX;
            sizeZ = extension.maxZ - extension.minZ;
            valid = true;
            attempts--;
        }
        while (!valid && attempts > 0);

        if (!valid)
        {
            return null;
        }

        return new BaseLinking(this.configuration, rand, blockX, this.boundingBox.minY, blockZ, sizeX, 3, sizeZ, randomDir);
    }
}
