package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.Random;

public abstract class DirectionalPieceVenus extends PieceVenus
{
    private EnumFacing direction;

    public DirectionalPieceVenus()
    {
    }

    public DirectionalPieceVenus(DungeonConfigurationVenus configuration, EnumFacing direction)
    {
        super(configuration);
        this.direction = direction;
    }

    public EnumFacing getDirection()
    {
        return direction;
    }

    public void setDirection(EnumFacing direction)
    {
        this.direction = direction;
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound tagCompound)
    {
        super.writeStructureToNBT(tagCompound);

        tagCompound.setInteger("direction", this.direction.ordinal());
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound)
    {
        super.readStructureFromNBT(tagCompound);

        if (tagCompound.hasKey("direction"))
        {
            this.direction = EnumFacing.getFront(tagCompound.getInteger("direction"));
        }
        else
        {
            this.direction = EnumFacing.NORTH;
        }
    }

    public PieceVenus getCorridor(Random rand, DungeonStartVenus startPiece, int maxAttempts, boolean small)
    {
        EnumFacing randomDir;
        int blockX;
        int blockZ;
        int sizeX;
        int sizeZ;
        boolean valid;
        int attempts = maxAttempts;
        do
        {
            int randDir = rand.nextInt(4);
            randomDir = EnumFacing.getHorizontal((randDir == getDirection().getOpposite().getHorizontalIndex() ? randDir + 1 : randDir) % 4);
            StructureBoundingBox extension = getExtension(randomDir, this.configuration.getHallwayLengthMin() + rand.nextInt(this.configuration.getHallwayLengthMax() - this.configuration.getHallwayLengthMin()), 5);
            blockX = extension.minX;
            blockZ = extension.minZ;
            sizeX = extension.maxX - extension.minX;
            sizeZ = extension.maxZ - extension.minZ;
            valid = !startPiece.checkIntersection(extension);
            attempts--;
        }
        while (!valid && attempts > 0);

        if (!valid)
        {
            return null;
        }

        return new CorridorVenus(this.configuration, rand, blockX, blockZ, sizeX, small ? 3 : this.configuration.getHallwayHeight(), sizeZ, randomDir);
    }
}
