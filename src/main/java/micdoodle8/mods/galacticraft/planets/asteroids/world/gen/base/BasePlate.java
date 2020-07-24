package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

import static micdoodle8.mods.galacticraft.planets.asteroids.world.gen.AsteroidFeatures.CBASE_PLATE;

public class BasePlate extends SizedPiece
{
    public BasePlate(TemplateManager templateManager, CompoundNBT nbt)
    {
        super(CBASE_PLATE, nbt);
    }

    public BasePlate(BaseConfiguration configuration, int blockPosX, int yPos, int blockPosZ, int sizeX, int sizeZ, Direction dir)
    {
        super(CBASE_PLATE, configuration, sizeX, 1, sizeZ, dir);
        this.setCoordBaseMode(dir);
        this.boundingBox = new MutableBoundingBox(blockPosX, yPos, blockPosZ, blockPosX + this.sizeX, yPos, blockPosZ + this.sizeZ);
    }

    @Override
    public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn)
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