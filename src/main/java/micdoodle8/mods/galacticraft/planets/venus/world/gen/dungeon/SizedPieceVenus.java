package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import micdoodle8.mods.galacticraft.core.world.gen.dungeon.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.Random;

public abstract class SizedPieceVenus extends SizedPiece
{
    public SizedPieceVenus()
    {
    }

    public SizedPieceVenus(DungeonConfiguration configuration, int sizeX, int sizeY, int sizeZ, EnumFacing direction)
    {
        super(configuration, sizeX, sizeY, sizeZ, direction);
    }

    @Override
    public Piece getCorridor(Random rand, DungeonStart startPiece, int maxAttempts, boolean small)
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

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound)
    {
        this.configuration = new DungeonConfigurationVenus();
        this.configuration.readFromNBT(tagCompound);

        super.readStructureFromNBT(tagCompound);
    }
}
