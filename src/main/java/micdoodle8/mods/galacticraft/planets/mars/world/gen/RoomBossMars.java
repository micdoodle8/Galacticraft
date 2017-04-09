package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.RoomBoss;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.Random;

public class RoomBossMars extends RoomBoss
{
    public RoomBossMars()
    {
    }

    public RoomBossMars(DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, EnumFacing entranceDir)
    {
        super(configuration, rand, blockPosX, blockPosZ, sizeX, sizeY, sizeZ, entranceDir);
    }

    public RoomBossMars(DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, EnumFacing entranceDir)
    {
        super(configuration, rand, blockPosX, blockPosZ, 24, 11, 24, entranceDir);
    }

    @Override
    public boolean addComponentParts(World worldIn, Random random, StructureBoundingBox chunkBox)
    {
        for (int i = 0; i <= this.sizeX; i++)
        {
            for (int j = 0; j <= this.sizeY; j++)
            {
                for (int k = 0; k <= this.sizeZ; k++)
                {
                    if (i == 0 || i == this.sizeX || j == 0 || k == 0 || k == this.sizeZ)
                    {
                        boolean placeBlock = true;
                        if (getDirection().getAxis() == EnumFacing.Axis.Z)
                        {
                            int start = (this.boundingBox.maxX - this.boundingBox.minX) / 2 - 1;
                            int end = (this.boundingBox.maxX - this.boundingBox.minX) / 2 + 1;
                            if (i > start && i <= end && j < 3 && j > 0)
                            {
                                if (getDirection() == EnumFacing.SOUTH && k == 0)
                                {
                                    placeBlock = false;
                                }
                                else if (getDirection() == EnumFacing.NORTH && k == this.sizeZ)
                                {
                                    placeBlock = false;
                                }
                            }
                        }
                        else
                        {
                            int start = (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - 1;
                            int end = (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1;
                            if (k > start && k <= end && j < 3 && j > 0)
                            {
                                if (getDirection() == EnumFacing.EAST && i == 0)
                                {
                                    placeBlock = false;
                                }
                                else if (getDirection() == EnumFacing.WEST && i == this.sizeX)
                                {
                                    placeBlock = false;
                                }
                            }
                        }
                        if (placeBlock)
                        {
                            this.setBlockState(worldIn, this.configuration.getBrickBlock(), i, j, k, chunkBox);
                        }
                        else
                        {
                            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), i, j, k, chunkBox);
                        }
                    }
                    else if (j == this.sizeY)
                    {
                        if ((i <= 2 || k <= 2 || i >= this.sizeX - 2 || k >= this.sizeZ - 2) && random.nextInt(4) == 0)
                        {
                            this.setBlockState(worldIn, Blocks.GLOWSTONE.getDefaultState(), i, j, k, chunkBox);
                        }
                        else
                        {
                            this.setBlockState(worldIn, this.configuration.getBrickBlock(), i, j, k, chunkBox);
                        }
                    }
                    else if (j == 1 && (i <= 2 || k <= 2 || i >= this.sizeX - 2 || k >= this.sizeZ - 2) && random.nextInt(6) == 0)
                    {
                        this.setBlockState(worldIn, MarsBlocks.creeperEgg.getDefaultState(), i, j, k, chunkBox);
                    }
                    else
                    {
                        this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), i, j, k, chunkBox);
                    }
                }
            }
        }

        int spawnerX = this.sizeX / 2;
        int spawnerY = 1;
        int spawnerZ = this.sizeZ / 2;
        BlockPos blockpos = new BlockPos(this.getXWithOffset(spawnerX, spawnerZ), this.getYWithOffset(spawnerY), this.getZWithOffset(spawnerX, spawnerZ));
        //Is this position inside the chunk currently being generated?
        if (chunkBox.isVecInside(blockpos))
        {
            worldIn.setBlockState(blockpos, MarsBlocks.bossSpawner.getDefaultState(), 2);
            TileEntityDungeonSpawner spawner = (TileEntityDungeonSpawner) worldIn.getTileEntity(blockpos);
            if (spawner != null)
            {
                spawner.setRoom(new Vector3(this.boundingBox.minX + 1, this.boundingBox.minY + 1, this.boundingBox.minZ + 1), new Vector3(this.sizeX - 1, this.sizeY - 1, this.sizeZ - 1));
            }
        }

        return true;
    }
}
