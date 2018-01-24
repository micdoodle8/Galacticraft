package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.Random;

public class BasePlate extends SizedPiece
{
    public BasePlate()
    {
    }

    public BasePlate(BaseConfiguration configuration, int blockPosX, int yPos, int blockPosZ, int sizeX, int sizeZ, EnumFacing dir)
    {
        super(configuration, sizeX, 1, sizeZ, dir);
        this.setCoordBaseMode(dir);
        this.boundingBox = new StructureBoundingBox(blockPosX, yPos, blockPosZ, blockPosX + this.sizeX, yPos, blockPosZ + this.sizeZ);
    }

    @Override
    public boolean addComponentParts(World worldIn, Random random, StructureBoundingBox boundingBox)
    {
        IBlockState blockWall = this.configuration.getWallBlock();
        boolean axisEW = getDirection().getAxis() == EnumFacing.Axis.X;
        int maxX = axisEW ? this.sizeZ : this.sizeX;
        int maxZ = axisEW ? this.sizeX : this.sizeZ;
        for (int xx = 0; xx <= maxX; xx++)
        {
            for (int zz = 0; zz <= maxZ; zz++)
            {
                this.setBlockState(worldIn, blockWall, xx, 0, zz, boundingBox);
            }
        }

        return true;
    }
    
}