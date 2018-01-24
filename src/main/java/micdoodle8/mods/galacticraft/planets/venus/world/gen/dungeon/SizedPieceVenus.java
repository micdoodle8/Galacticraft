package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.template.TemplateManager;

public abstract class SizedPieceVenus extends DirectionalPieceVenus
{
    protected int sizeX;
    protected int sizeY;
    protected int sizeZ;

    public SizedPieceVenus()
    {
    }

    public SizedPieceVenus(DungeonConfigurationVenus configuration, int sizeX, int sizeY, int sizeZ, EnumFacing direction)
    {
        super(configuration, direction);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound tagCompound)
    {
        super.writeStructureToNBT(tagCompound);

        tagCompound.setInteger("sizeX", this.sizeX);
        tagCompound.setInteger("sizeY", this.sizeY);
        tagCompound.setInteger("sizeZ", this.sizeZ);
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager manager)
    {
        super.readStructureFromNBT(tagCompound, manager);

        this.sizeX = tagCompound.getInteger("sizeX");
        this.sizeY = tagCompound.getInteger("sizeY");
        this.sizeZ = tagCompound.getInteger("sizeZ");
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
