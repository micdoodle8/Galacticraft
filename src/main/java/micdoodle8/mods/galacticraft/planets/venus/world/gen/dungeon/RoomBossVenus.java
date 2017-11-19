package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockTorchWeb;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class RoomBossVenus extends SizedPieceVenus
{
    private EnumFacing exitDirection;
    private BlockPos chestPos;

    public RoomBossVenus()
    {
    }

    public RoomBossVenus(DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ, EnumFacing entranceDir)
    {
        this(configuration, rand, blockPosX, blockPosZ, rand.nextInt(6) + 20, rand.nextInt(2) + 10, rand.nextInt(6) + 20, entranceDir);
    }

    public RoomBossVenus(DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, EnumFacing entranceDir)
    {
        super(configuration, sizeX, sizeY, sizeZ, entranceDir.getOpposite());
        this.setCoordBaseMode(EnumFacing.SOUTH);
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
        this.sizeY = sizeY;
        int yPos = configuration.getYPosition();

        this.boundingBox = new StructureBoundingBox(blockPosX, yPos - 2, blockPosZ, blockPosX + this.sizeX, yPos + this.sizeY, blockPosZ + this.sizeZ);
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound tagCompound)
    {
        super.writeStructureToNBT(tagCompound);

        if (this.exitDirection != null)
        {
            tagCompound.setInteger("direction_exit", this.exitDirection.ordinal());
        }


        tagCompound.setBoolean("chestPosNull", this.chestPos == null);
        if (this.chestPos != null)
        {
            tagCompound.setInteger("chestX", this.chestPos.getX());
            tagCompound.setInteger("chestY", this.chestPos.getY());
            tagCompound.setInteger("chestZ", this.chestPos.getZ());
        }
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager manager)
    {
        super.readStructureFromNBT(tagCompound, manager);

        if (tagCompound.hasKey("direction_exit"))
        {
            this.exitDirection = EnumFacing.getFront(tagCompound.getInteger("direction_exit"));
        }
        else
        {
            this.exitDirection = null;
        }

        if (tagCompound.hasKey("chestPosNull") && !tagCompound.getBoolean("chestPosNull"))
        {
            this.chestPos = new BlockPos(tagCompound.getInteger("chestX"), tagCompound.getInteger("chestY"), tagCompound.getInteger("chestZ"));
        }
    }

    @Override
    public boolean addComponentParts(World worldIn, Random random, StructureBoundingBox chunkBox)
    {
        StructureBoundingBox box = new StructureBoundingBox(new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE });

        for (int i = 0; i <= this.sizeX; i++)
        {
            for (int j = 0; j <= this.sizeY; j++)
            {
                for (int k = 0; k <= this.sizeZ; k++)
                {
                    double dX = (i - this.sizeX / 2);
                    double dZ = (k - this.sizeZ / 2);
                    double dXZ = Math.sqrt(dX * dX + dZ * dZ);

                    double f = -Math.pow((dXZ * 1.5) / (this.sizeX / 2 - 1), 6) + this.sizeY - 1;

                    if (j == 0)
                    {
                        this.setBlockState(worldIn, this.configuration.getBrickBlockFloor(), i, j, k, chunkBox);
                    }
                    else if (j < f)
                    {
                        this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), i, j, k, chunkBox);

                        if (j + 1 >= f && (dXZ > 5) && random.nextInt(12) == 0)
                        {
                            int distFromFloor = random.nextInt(5) + 2;
                            for (int j0 = j; j0 >= distFromFloor + 1; --j0)
                            {
                                BlockTorchWeb.EnumWebType webType;
                                if (j0 == distFromFloor + 1)
                                {
                                    webType = BlockTorchWeb.EnumWebType.WEB_1;
                                }
                                else
                                {
                                    webType = BlockTorchWeb.EnumWebType.WEB_0;
                                }
                                this.setBlockState(worldIn, VenusBlocks.torchWeb.getDefaultState().withProperty(BlockTorchWeb.WEB_TYPE, webType), i, j0, k, chunkBox);
                            }
                        }

                        if (i < box.minX)
                        {
                            box.minX = i;
                        }
                        if (i > box.maxX)
                        {
                            box.maxX = i;
                        }
                        if (j < box.minY)
                        {
                            box.minY = j;
                        }
                        if (j > box.maxY)
                        {
                            box.maxY = j;
                        }
                        if (k < box.minZ)
                        {
                            box.minZ = k;
                        }
                        if (k > box.maxZ)
                        {
                            box.maxZ = k;
                        }
                    }
                    else
                    {
                        boolean placeBlock = true;

                        int start = (this.boundingBox.maxX - this.boundingBox.minX) / 2 - 1;
                        int end = (this.boundingBox.maxX - this.boundingBox.minX) / 2 + 1;
                        if (i > start && i <= end && j < 5 && j > 2)
                        {
                            if ((getDirection() == EnumFacing.SOUTH || (this.exitDirection != null && this.exitDirection == EnumFacing.SOUTH)) && k < 7)
                            {
                                placeBlock = false;
                            }
                            if ((getDirection() == EnumFacing.NORTH || (this.exitDirection != null && this.exitDirection == EnumFacing.NORTH)) && k > this.sizeZ - 7)
                            {
                                placeBlock = false;
                            }
                        }

                        start = (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - 1;
                        end = (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1;
                        if (k > start && k <= end && j < 5 && j > 2)
                        {
                            if ((getDirection() == EnumFacing.EAST || (this.exitDirection != null && this.exitDirection == EnumFacing.EAST)) && i < 7)
                            {
                                placeBlock = false;
                            }
                            if ((getDirection() == EnumFacing.WEST || (this.exitDirection != null && this.exitDirection == EnumFacing.WEST)) && i > this.sizeX - 7)
                            {
                                placeBlock = false;
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
            worldIn.setBlockState(blockpos, VenusBlocks.bossSpawner.getDefaultState(), 2);
            TileEntityDungeonSpawner spawner = (TileEntityDungeonSpawner) worldIn.getTileEntity(blockpos);
            if (spawner != null)
            {
                if (box.getXSize() > 10000 || box.getYSize() > 10000 || box.getZSize() > 10000)
                {
                    GCLog.severe("Failed to set correct boss room size. This is a bug!");
                }
                else
                {
                    spawner.setRoom(new Vector3(box.minX + this.boundingBox.minX, box.minY + this.boundingBox.minY, box.minZ + this.boundingBox.minZ), new Vector3(box.maxX - box.minX + 1, box.maxY - box.minY + 1, box.maxZ - box.minZ + 1));
                    spawner.setChestPos(this.chestPos);
                }
            }
        }

        return true;
    }

    public BlockPos getChestPos()
    {
        return chestPos;
    }

    public void setChestPos(BlockPos chestPos)
    {
        this.chestPos = chestPos;
    }

    @Override
    public PieceVenus getNextPiece(DungeonStartVenus startPiece, Random rand)
    {
        DirectionalPieceVenus corridor = (DirectionalPieceVenus) getCorridor(rand, startPiece, 10, true);
        this.exitDirection = corridor == null ? null : corridor.getDirection().getOpposite();
        return corridor;
    }
}