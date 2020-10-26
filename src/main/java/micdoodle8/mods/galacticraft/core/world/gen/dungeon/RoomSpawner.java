package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

import static micdoodle8.mods.galacticraft.core.world.gen.GCFeatures.CMOON_DUNGEON_SPAWNER;

public class RoomSpawner extends RoomEmpty
{
    public RoomSpawner(TemplateManager templateManager, CompoundNBT nbt)
    {
        super(CMOON_DUNGEON_SPAWNER, nbt);
    }

    public RoomSpawner(DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir)
    {
        super(CMOON_DUNGEON_SPAWNER, configuration, rand, blockPosX, blockPosZ, sizeX, sizeY, sizeZ, entranceDir);
    }

    @Override
    public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn)
    {
        if (super.create(worldIn, chunkGeneratorIn, randomIn, mutableBoundingBoxIn, chunkPosIn))
        {
            for (int i = 1; i <= this.sizeX - 1; ++i)
            {
                for (int j = 1; j <= this.sizeY - 1; ++j)
                {
                    for (int k = 1; k <= this.sizeZ - 1; ++k)
                    {
                        if (randomIn.nextFloat() < 0.05F)
                        {
                            this.setBlockState(worldIn, Blocks.COBWEB.getDefaultState(), i, j, k, boundingBox);
                        }
                    }
                }
            }

            this.setBlockState(worldIn, Blocks.SPAWNER.getDefaultState(), 1, 0, 1, boundingBox);
            this.setBlockState(worldIn, Blocks.SPAWNER.getDefaultState(), this.sizeX - 1, 0, this.sizeZ - 1, boundingBox);

            BlockPos blockpos = new BlockPos(this.getXWithOffset(1, 1), this.getYWithOffset(0), this.getZWithOffset(1, 1));
            MobSpawnerTileEntity spawner = (MobSpawnerTileEntity) worldIn.getTileEntity(blockpos);

            if (spawner != null)
            {
                spawner.getSpawnerBaseLogic().setEntityType(getMob(randomIn));
            }

            blockpos = new BlockPos(this.getXWithOffset(this.sizeX - 1, this.sizeZ - 1), this.getYWithOffset(0), this.getZWithOffset(this.sizeX - 1, this.sizeZ - 1));
            spawner = (MobSpawnerTileEntity) worldIn.getTileEntity(blockpos);

            if (spawner != null)
            {
                spawner.getSpawnerBaseLogic().setEntityType(getMob(randomIn));
            }

            return true;
        }

        return false;
    }

    private static EntityType<?> getMob(Random rand)
    {
        switch (rand.nextInt(4))
        {
        case 0:
            return GCEntities.EVOLVED_SPIDER;
        case 1:
            return GCEntities.EVOLVED_CREEPER;
        case 2:
            return GCEntities.EVOLVED_SKELETON;
        case 3:
        default:
            return GCEntities.EVOLVED_ZOMBIE;
        }
    }
}