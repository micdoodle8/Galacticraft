package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

import static micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures.CVENUS_DUNGEON_ENTRANCE;
import static micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures.CVENUS_DUNGEON_SPAWNER;

public class RoomSpawnerVenus extends RoomEmptyVenus
{
    public RoomSpawnerVenus(TemplateManager templateManager, CompoundNBT nbt)
    {
        super(CVENUS_DUNGEON_SPAWNER, nbt);
    }

    public RoomSpawnerVenus(DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir)
    {
        super(CVENUS_DUNGEON_SPAWNER, configuration, rand, blockPosX, blockPosZ, sizeX, sizeY, sizeZ, entranceDir);
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

            this.placeMobSpawner(worldIn, randomIn, mutableBoundingBoxIn, 1, 0, 1);
            this.placeMobSpawner(worldIn, randomIn, mutableBoundingBoxIn, this.sizeX - 1, 0, this.sizeZ - 1);

            return true;
        }

        return false;
    }

    private void placeMobSpawner(IWorld worldIn, Random random, MutableBoundingBox chunkBox, int x, int y, int z)
    {
        this.setBlockState(worldIn, Blocks.SPAWNER.getDefaultState(), 1, 0, 1, boundingBox);
        BlockPos blockpos = new BlockPos(this.getXWithOffset(1, 1), this.getYWithOffset(0), this.getZWithOffset(1, 1));
        MobSpawnerTileEntity spawner = (MobSpawnerTileEntity) worldIn.getTileEntity(blockpos);

        if (spawner != null)
        {
            spawner.getSpawnerBaseLogic().setEntityType(getMob(random));
        }
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