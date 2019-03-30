package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.lang.reflect.Constructor;
import java.util.Random;

public class CorridorVenus extends SizedPieceVenus
{
    public CorridorVenus()
    {
    }

    public CorridorVenus(DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, EnumFacing direction)
    {
        super(configuration, sizeX, sizeY, sizeZ, direction);
        this.setCoordBaseMode(EnumFacing.SOUTH);
        this.boundingBox = new StructureBoundingBox(blockPosX, configuration.getYPosition(), blockPosZ, blockPosX + sizeX, configuration.getYPosition() + sizeY, blockPosZ + sizeZ);
    }

    @Override
    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
    {
        for (int i = 0; i < this.boundingBox.getXSize(); i++)
        {
            for (int j = 0; j < this.boundingBox.getYSize(); j++)
            {
                for (int k = 0; k < this.boundingBox.getZSize(); k++)
                {
                    if (j == 2 && this.getDirection().getAxis() == EnumFacing.Axis.Z && (k + 1) % 4 == 0 && k != this.boundingBox.getZSize() - 1)
                    {
                        if (i == 0 || i == this.boundingBox.getXSize() - 1)
                        {
                            this.setBlockState(worldIn, Blocks.LAVA.getDefaultState(), i, j, k, this.boundingBox);
                        }
                        else if (i == 1 || i == this.boundingBox.getXSize() - 2)
                        {
                            this.setBlockState(worldIn, Blocks.IRON_BARS.getDefaultState(), i, j, k, this.boundingBox);
                        }
                        else
                        {
                            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), i, j, k, this.boundingBox);
                        }
                    }
                    else if (j == 2 && this.getDirection().getAxis() == EnumFacing.Axis.X && (i + 1) % 4 == 0 && i != this.boundingBox.getXSize() - 1)
                    {
                        if (k == 0 || k == this.boundingBox.getZSize() - 1)
                        {
                            this.setBlockState(worldIn, Blocks.LAVA.getDefaultState(), i, j, k, this.boundingBox);
                        }
                        else if (k == 1 || k == this.boundingBox.getZSize() - 2)
                        {
                            this.setBlockState(worldIn, Blocks.IRON_BARS.getDefaultState(), i, j, k, this.boundingBox);
                        }
                        else
                        {
                            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), i, j, k, this.boundingBox);
                        }
                    }
                    else if ((this.getDirection().getAxis() == EnumFacing.Axis.Z && (i == 1 || i == this.boundingBox.getXSize() - 2)) ||
                            j == 0 || j == this.boundingBox.getYSize() - 1 ||
                            (this.getDirection().getAxis() == EnumFacing.Axis.X && (k == 1 || k == this.boundingBox.getZSize() - 2)))
                    {
                        DungeonConfigurationVenus venusConfig = (DungeonConfigurationVenus) this.configuration;
                        this.setBlockState(worldIn, j == 0 || j == this.boundingBox.getYSize() - 1 ? venusConfig.getBrickBlockFloor() : this.configuration.getBrickBlock(), i, j, k, this.boundingBox);
                    }
                    else if ((this.getDirection().getAxis() == EnumFacing.Axis.Z && (i == 0 || i == this.boundingBox.getXSize() - 1)) ||
                            (this.getDirection().getAxis() == EnumFacing.Axis.X && (k == 0 || k == this.boundingBox.getZSize() - 1)))
                    {
                        DungeonConfigurationVenus venusConfig = (DungeonConfigurationVenus) this.configuration;
                        this.setBlockState(worldIn, j == 0 || j == this.boundingBox.getYSize() - 1 ? venusConfig.getBrickBlockFloor() : this.configuration.getBrickBlock(), i, j, k, this.boundingBox);
                    }
                    else
                    {
                        this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), i, j, k, this.boundingBox);
                    }
                }
            }
        }

        return true;
    }

    private <T extends SizedPieceVenus> T getRoom(Class<?> clazz, DungeonStartVenus startPiece, Random rand)
    {
        try
        {
            Constructor<?> c0 = clazz.getConstructor(DungeonConfigurationVenus.class, Random.class, Integer.TYPE, Integer.TYPE, EnumFacing.class);
            T dummy = (T) c0.newInstance(this.configuration, rand, 0, 0, this.getDirection().getOpposite());
            StructureBoundingBox extension = getExtension(this.getDirection(), getDirection().getAxis() == EnumFacing.Axis.X ? dummy.getSizeX() : dummy.getSizeZ(), getDirection().getAxis() == EnumFacing.Axis.X ? dummy.getSizeZ() : dummy.getSizeX());
            if (startPiece.checkIntersection(extension))
            {
                return null;
            }
            int sizeX = extension.maxX - extension.minX;
            int sizeZ = extension.maxZ - extension.minZ;
            int sizeY = dummy.getSizeY();
            int blockX = extension.minX;
            int blockZ = extension.minZ;
            Constructor<?> c1 = clazz.getConstructor(DungeonConfigurationVenus.class, Random.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, EnumFacing.class);
            return (T) c1.newInstance(this.configuration, rand, blockX, blockZ, sizeX, sizeY, sizeZ, this.getDirection().getOpposite());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public PieceVenus getNextPiece(DungeonStartVenus startPiece, Random rand)
    {
        int pieceCount = startPiece.attachedComponents.size();
        if (pieceCount > 10 && startPiece.attachedComponents.get(pieceCount - 2) instanceof RoomBossVenus)
        {
            try
            {
                return getRoom(this.configuration.getTreasureRoom(), startPiece, rand);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            int bossRoomChance = Math.max((int) (20.0 / (pieceCount - 10)), 1);
            boolean bossRoom = pieceCount > 25 || (pieceCount > 10 && rand.nextInt(bossRoomChance) == 0);
            if (bossRoom)
            {
                try
                {
                    return getRoom(this.configuration.getBossRoom(), startPiece, rand);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return null;
                }
            }
            else
            {
                StructureBoundingBox extension = getExtension(this.getDirection(), rand.nextInt(4) + 6, rand.nextInt(4) + 6);

                if (startPiece.checkIntersection(extension))
                {
                    return null;
                }

                int sizeX = extension.maxX - extension.minX;
                int sizeZ = extension.maxZ - extension.minZ;
                int sizeY = configuration.getRoomHeight();
                int blockX = extension.minX;
                int blockZ = extension.minZ;

                if (Math.abs(startPiece.getBoundingBox().maxZ - boundingBox.minZ) > 200)
                {
                    return null;
                }

                if (Math.abs(startPiece.getBoundingBox().maxX - boundingBox.minX) > 200)
                {
                    return null;
                }

                PieceVenus lastPiece = pieceCount <= 2 ? null : (PieceVenus) startPiece.attachedComponents.get(pieceCount - 2);

                if (!(lastPiece instanceof RoomSpawnerVenus))
                {
                    return new RoomSpawnerVenus(this.configuration, rand, blockX, blockZ, sizeX, sizeY, sizeZ, this.getDirection().getOpposite());
                }
                else
                {
                    if (rand.nextInt(2) == 0)
                    {
                        return new RoomEmptyVenus(this.configuration, rand, blockX, blockZ, sizeX, sizeY, sizeZ, this.getDirection().getOpposite());
                    }
                    else
                    {
                        return new RoomChestVenus(this.configuration, rand, blockX, blockZ, sizeX, sizeY, sizeZ, this.getDirection().getOpposite());
                    }
                }
            }

        }

        return null;
    }
}