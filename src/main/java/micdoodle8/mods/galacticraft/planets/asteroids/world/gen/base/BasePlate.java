package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.util.math.MutableBoundingBox;

import java.util.Random;

public class BasePlate extends SizedPiece
{
    public BasePlate()
    {
    }

    public BasePlate(BaseConfiguration configuration, int blockPosX, int yPos, int blockPosZ, int sizeX, int sizeZ, Direction dir)
    {
        super(configuration, sizeX, 1, sizeZ, dir);
        this.setCoordBaseMode(dir);
        this.boundingBox = new MutableBoundingBox(blockPosX, yPos, blockPosZ, blockPosX + this.sizeX, yPos, blockPosZ + this.sizeZ);
    }

    @Override
    public boolean addComponentParts(World worldIn, Random random, MutableBoundingBox boundingBox)
    {
        BlockState blockWall = this.configuration.getWallBlock();
        boolean axisEW = getDirection().getAxis() == Direction.Axis.X;
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