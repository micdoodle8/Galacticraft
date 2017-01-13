package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockTorchWeb;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntitySpiderQueen;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.Random;

public class RoomBossVenus extends SizedPieceVenus
{
    private EnumFacing exitDirection;

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
        this.coordBaseMode = EnumFacing.SOUTH;
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
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound)
    {
        super.readStructureFromNBT(tagCompound);

        if (tagCompound.hasKey("direction_exit"))
        {
            this.exitDirection = EnumFacing.getFront(tagCompound.getInteger("direction_exit"));
        }
        else
        {
            this.exitDirection = null;
        }
    }

    @Override
    public boolean addComponentParts(World worldIn, Random random, StructureBoundingBox boundingBox)
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
                        this.setBlockState(worldIn, this.configuration.getBrickBlockFloor(), i, j, k, boundingBox);
                    }
                    else if (j < f)
                    {
                        this.setBlockState(worldIn, Blocks.air.getDefaultState(), i, j, k, boundingBox);

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
                                this.setBlockState(worldIn, VenusBlocks.torchWeb.getDefaultState().withProperty(BlockTorchWeb.WEB_TYPE, webType), i, j0, k, boundingBox);
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
                            this.setBlockState(worldIn, this.configuration.getBrickBlock(), i, j, k, boundingBox);
                        }
                        else
                        {
                            this.setBlockState(worldIn, Blocks.air.getDefaultState(), i, j, k, boundingBox);
                        }
                    }
                }
            }
        }

        int spawnerX = this.sizeX / 2;
        int spawnerY = 1;
        int spawnerZ = this.sizeZ / 2;
        this.setBlockState(worldIn, VenusBlocks.bossSpawner.getDefaultState(), spawnerX, spawnerY, spawnerZ, boundingBox);
        BlockPos blockpos = new BlockPos(this.getXWithOffset(spawnerX, spawnerZ), this.getYWithOffset(spawnerY), this.getZWithOffset(spawnerX, spawnerZ));
        TileEntityDungeonSpawner spawner = (TileEntityDungeonSpawner) worldIn.getTileEntity(blockpos);

        if (spawner == null)
        {
            spawner = new TileEntityDungeonSpawner(EntitySpiderQueen.class);
            worldIn.setTileEntity(blockpos, spawner);
        }

        if (box.getXSize() > 10000 || box.getYSize() > 10000 || box.getZSize() > 10000)
        {
            GCLog.severe("Failed to set correct boss room size. This is a bug!");
        }
        else
        {
            spawner.setRoom(new Vector3(box.minX + this.boundingBox.minX, box.minY + this.boundingBox.minY, box.minZ + this.boundingBox.minZ), new Vector3(box.maxX - box.minX + 1, box.maxY - box.minY + 1, box.maxZ - box.minZ + 1));
        }

        return true;
    }

    @Override
    public PieceVenus getNextPiece(DungeonStartVenus startPiece, Random rand)
    {
        DirectionalPieceVenus corridor = (DirectionalPieceVenus) getCorridor(rand, startPiece, 10, true);
        this.exitDirection = corridor.getDirection().getOpposite();
        return corridor;
    }
}