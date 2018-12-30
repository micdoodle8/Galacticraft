package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class RoomBoss extends SizedPiece
{
    private BlockPos chestPos;

    public RoomBoss()
    {
    }

    public RoomBoss(DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, EnumFacing entranceDir)
    {
        this(configuration, rand, blockPosX, blockPosZ, rand.nextInt(6) + 14, rand.nextInt(2) + 8, rand.nextInt(6) + 14, entranceDir);
    }

    public RoomBoss(DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, EnumFacing entranceDir)
    {
        super(configuration, sizeX, sizeY, sizeZ, entranceDir.getOpposite());
        this.setCoordBaseMode(EnumFacing.SOUTH);
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
        this.sizeY = sizeY;
        int yPos = configuration.getYPosition();

        this.boundingBox = new StructureBoundingBox(blockPosX, yPos, blockPosZ, blockPosX + this.sizeX, yPos + this.sizeY, blockPosZ + this.sizeZ);
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
                    if (i == 0 || i == this.sizeX || j == 0 || j == this.sizeY || k == 0 || k == this.sizeZ)
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
                    else if ((i == 1 && k == 1) || (i == 1 && k == this.sizeZ - 1) || (i == this.sizeX - 1 && k == 1) || (i == this.sizeX - 1 && k == this.sizeZ - 1))
                    {
                        this.setBlockState(worldIn, Blocks.FLOWING_LAVA.getDefaultState(), i, j, k, chunkBox);
                    }
                    else if (j % 3 == 0 && j >= 2 && ((i == 1 || i == this.sizeX - 1 || k == 1 || k == this.sizeZ - 1) || (i == 2 && k == 2) || (i == 2 && k == this.sizeZ - 2) || (i == this.sizeX - 2 && k == 2) || (i == this.sizeX - 2 && k == this.sizeZ - 2)))
                    {
                        // Horizontal bars
                        this.setBlockState(worldIn, Blocks.IRON_BARS.getDefaultState(), i, j, k, chunkBox);
                    }
                    else if ((i == 1 && k == 2) || (i == 2 && k == 1) ||
                            (i == 1 && k == this.sizeZ - 2) || (i == 2 && k == this.sizeZ - 1) ||
                            (i == this.sizeX - 1 && k == 2) || (i == this.sizeX - 2 && k == 1) ||
                            (i == this.sizeX - 1 && k == this.sizeZ - 2) || (i == this.sizeX - 2 && k == this.sizeZ - 1))
                    {
                        // Vertical bars
                        this.setBlockState(worldIn, Blocks.IRON_BARS.getDefaultState(), i, j, k, chunkBox);
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
            worldIn.setBlockState(blockpos, GCBlocks.bossSpawner.getDefaultState(), 2);
            TileEntityDungeonSpawner spawner = (TileEntityDungeonSpawner) worldIn.getTileEntity(blockpos);
            if (spawner != null)
            {
                spawner.setRoom(new Vector3(this.boundingBox.minX + 1, this.boundingBox.minY + 1, this.boundingBox.minZ + 1), new Vector3(this.sizeX - 1, this.sizeY - 1, this.sizeZ - 1));
                spawner.setChestPos(this.chestPos);
            }
        }

        return true;
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound tagCompound)
    {
        super.writeStructureToNBT(tagCompound);

        if(this.chestPos != null)
        {
            tagCompound.setInteger("chestX", this.chestPos.getX());
            tagCompound.setInteger("chestY", this.chestPos.getY());
            tagCompound.setInteger("chestZ", this.chestPos.getZ());
        }
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound nbt, TemplateManager manager)
    {
        super.readStructureFromNBT(nbt, manager);
        this.chestPos = new BlockPos(nbt.getInteger("chestX"), nbt.getInteger("chestY"), nbt.getInteger("chestZ"));
    }

    @Override
    public Piece getNextPiece(DungeonStart startPiece, Random rand)
    {
        return getCorridor(rand, startPiece, 10, true);
    }

    public BlockPos getChestPos()
    {
        return chestPos;
    }

    public void setChestPos(BlockPos chestPos)
    {
        this.chestPos = chestPos;
    }
}