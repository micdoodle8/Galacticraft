package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.Random;

public class RoomSpawner extends RoomEmpty
{
    public RoomSpawner()
    {
    }

    public RoomSpawner(DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, EnumFacing entranceDir)
    {
        super(configuration, rand, blockPosX, blockPosZ, sizeX, sizeY, sizeZ, entranceDir);
    }

    @Override
    public boolean addComponentParts(World worldIn, Random random, StructureBoundingBox boundingBox)
    {
        if (super.addComponentParts(worldIn, random, boundingBox))
        {
            for (int i = 1; i <= this.sizeX - 1; ++i)
            {
                for (int j = 1; j <= this.sizeY - 1; ++j)
                {
                    for (int k = 1; k <= this.sizeZ - 1; ++k)
                    {
                        if (random.nextFloat() < 0.05F)
                        {
                            this.setBlockState(worldIn, Blocks.web.getDefaultState(), i, j, k, boundingBox);
                        }
                    }
                }
            }

            this.setBlockState(worldIn, Blocks.mob_spawner.getDefaultState(), 1, 0, 1, boundingBox);
            this.setBlockState(worldIn, Blocks.mob_spawner.getDefaultState(), this.sizeX - 1, 0, this.sizeZ - 1, boundingBox);

            BlockPos blockpos = new BlockPos(this.getXWithOffset(1, 1), this.getYWithOffset(0), this.getZWithOffset(1, 1));
            TileEntityMobSpawner spawner = (TileEntityMobSpawner) worldIn.getTileEntity(blockpos);

            if (spawner != null)
            {
                spawner.getSpawnerBaseLogic().setEntityName(getMob(random));
            }

            blockpos = new BlockPos(this.getXWithOffset(this.sizeX - 1, this.sizeZ - 1), this.getYWithOffset(0), this.getZWithOffset(this.sizeX - 1, this.sizeZ - 1));
            spawner = (TileEntityMobSpawner) worldIn.getTileEntity(blockpos);

            if (spawner != null)
            {
                spawner.getSpawnerBaseLogic().setEntityName(getMob(random));
            }

            return true;
        }

        return false;
    }

    private static String getMob(Random rand)
    {
        switch (rand.nextInt(4))
        {
        case 0:
            return "GalacticraftCore.evolved_spider";
        case 1:
            return "GalacticraftCore.evolved_creeper";
        case 2:
            return "GalacticraftCore.evolved_skeleton";
        case 3:
        default:
            return "GalacticraftCore.evolved_zombie";
        }
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound tagCompound)
    {
        super.writeStructureToNBT(tagCompound);
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound)
    {
        super.readStructureFromNBT(tagCompound);
    }
}