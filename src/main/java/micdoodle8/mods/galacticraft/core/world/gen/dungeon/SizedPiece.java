package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.template.TemplateManager;

public abstract class SizedPiece extends DirectionalPiece
{
    protected int sizeX;
    protected int sizeY;
    protected int sizeZ;

    public SizedPiece(IStructurePieceType type)
    {
        super(type);
    }

    public SizedPiece(IStructurePieceType type, DungeonConfiguration configuration, int sizeX, int sizeY, int sizeZ, Direction direction)
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
    protected void readStructureFromNBT(CompoundNBT nbt, TemplateManager manager)
    {
        super.readStructureFromNBT(nbt, manager);

        this.sizeX = nbt.getInt("sizeX");
        this.sizeY = nbt.getInt("sizeY");
        this.sizeZ = nbt.getInt("sizeZ");
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
