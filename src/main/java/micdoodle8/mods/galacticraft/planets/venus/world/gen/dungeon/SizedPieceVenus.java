package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

public abstract class SizedPieceVenus extends DirectionalPieceVenus
{
    protected int sizeX;
    protected int sizeY;
    protected int sizeZ;

    public SizedPieceVenus(IStructurePieceType type, CompoundNBT nbt)
    {
        super(type, nbt);
    }

    public SizedPieceVenus(IStructurePieceType type, DungeonConfigurationVenus configuration, int sizeX, int sizeY, int sizeZ, Direction direction)
    {
        super(type, configuration, direction);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    @Override
    protected void writeStructureToNBT(CompoundNBT tagCompound)
    {
        super.writeStructureToNBT(tagCompound);

        tagCompound.putInt("sizeX", this.sizeX);
        tagCompound.putInt("sizeY", this.sizeY);
        tagCompound.putInt("sizeZ", this.sizeZ);
    }

    @Override
    protected void readStructureFromNBT(CompoundNBT tagCompound)
    {
        super.readStructureFromNBT(tagCompound);

        this.sizeX = tagCompound.getInt("sizeX");
        this.sizeY = tagCompound.getInt("sizeY");
        this.sizeZ = tagCompound.getInt("sizeZ");
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
}
