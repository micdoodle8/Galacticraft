package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.core.blocks.BlockT1TreasureChest;
import micdoodle8.mods.galacticraft.core.blocks.BlockUnlitTorch;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.List;
import java.util.Random;

public class StructureMoonDungeonPieces
{
    abstract static class Piece extends StructureComponent
    {
        protected IBlockState brickBlock;

        public Piece()
        {
        }

        public Piece(IBlockState brickBlock)
        {
            this.brickBlock = brickBlock;
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound tagCompound)
        {
            tagCompound.setString("brickBlock", Block.blockRegistry.getNameForObject(this.brickBlock.getBlock()).toString());
            tagCompound.setInteger("brickBlockMeta", this.brickBlock.getBlock().getMetaFromState(this.brickBlock));
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound tagCompound)
        {
            try
            {
                this.brickBlock = Block.getBlockFromName(tagCompound.getString("brickBlock")).getStateFromMeta(tagCompound.getInteger("brickBlockMeta"));
            }
            catch (Exception e)
            {
                System.err.println("Failed to read structure piece from NBT");
                e.printStackTrace();
            }
        }

        protected StructureBoundingBox getExtension(EnumFacing direction, int length, int width)
        {
            int blockX, blockZ, sizeX, sizeZ;
            switch (direction)
            {
            case NORTH:
                sizeX = width;
                sizeZ = length;
                blockX = this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2 - sizeX / 2;
                blockZ = this.boundingBox.minZ - sizeZ;
                break;
            case EAST:
                sizeX = length;
                sizeZ = width;
                blockX = this.boundingBox.maxX;
                blockZ = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - sizeZ / 2;
                break;
            case SOUTH:
                sizeX = width;
                sizeZ = length;
                blockX = this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2 - sizeX / 2;
                blockZ = this.boundingBox.maxZ;
                break;
            case WEST:
            default:
                sizeX = length;
                sizeZ = width;
                blockX = this.boundingBox.minX - sizeX;
                blockZ = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - sizeZ / 2;
                break;
            }
            return new StructureBoundingBox(blockX, blockZ, blockX + sizeX, blockZ + sizeZ);
        }

        public Piece getNextPiece(Start startPiece, Random rand)
        {
            return null;
        }
    }

    private static abstract class DirectionalPiece extends Piece
    {
        private EnumFacing direction;

        public DirectionalPiece()
        {
        }

        public DirectionalPiece(IBlockState brickBlock, EnumFacing direction)
        {
            super(brickBlock);
            this.direction = direction;
        }

        public EnumFacing getDirection()
        {
            return direction;
        }

        public void setDirection(EnumFacing direction)
        {
            this.direction = direction;
        }

        public Piece getCorridor(Random rand, Start startPiece, int maxAttempts, boolean treasure)
        {
            EnumFacing randomDir;
            int blockX;
            int blockZ;
            int sizeX;
            int sizeZ;
            boolean valid;
            int attempts = maxAttempts;
            do
            {
                int randDir = rand.nextInt(4);
                randomDir = EnumFacing.getHorizontal((randDir == getDirection().getOpposite().getHorizontalIndex() ? randDir + 1 : randDir) % 4);
                StructureBoundingBox extension = getExtension(randomDir, 10, 3);
                blockX = extension.minX;
                blockZ = extension.minZ;
                sizeX = extension.maxX - extension.minX;
                sizeZ = extension.maxZ - extension.minZ;
                valid = !startPiece.checkIntersection(extension);
                attempts--;
            }
            while (!valid && attempts > 0);

            if (!valid)
            {
                return null;
            }

            if (treasure)
            {
                return new TreasureCorridor(this.brickBlock, rand, blockX, blockZ, sizeX, sizeZ, randomDir);
            }
            else
            {
                return new Corridor(this.brickBlock, rand, blockX, blockZ, sizeX, sizeZ, randomDir);
            }
        }
    }

    public static class Start extends EntranceRoom
    {
        public List<StructureComponent> attachedComponents = Lists.newArrayList();
        public List<StructureBoundingBox> componentBounds = Lists.newArrayList();

        public Start()
        {
        }

        public Start(World world, IBlockState brickBlock, Random rand, int blockPosX, int blockPosZ)
        {
            super(world, brickBlock, rand, blockPosX, blockPosZ);
        }

        @Override
        public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
        {
            boolean validAttempt = false;
            int attempts = 10;
            while (!validAttempt && attempts > 0)
            {
                attachedComponents.clear();
                componentBounds.clear();
                componentBounds.add(this.boundingBox);
                listIn.clear();
                listIn.add(this);
                Piece next = getNextPiece(this, rand);
                while (next != null)
                {
                    listIn.add(next);
                    attachedComponents.add(next);
                    componentBounds.add(next.getBoundingBox());

                    next = next.getNextPiece(this, rand);
                }
                if (attachedComponents.size() >= 3 && attachedComponents.get(attachedComponents.size() - 1) instanceof RoomTreasure &&
                        attachedComponents.get(attachedComponents.size() - 3) instanceof RoomBoss)
                {
                    validAttempt = true;
                }
                attempts--;
            }

            if (!validAttempt)
            {
                int xPos = this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2;
                int zPos = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2;
                System.err.println("Could not find valid dungeon layout! This is a bug, please report it, including your world seed (/seed) and dungeon location (" + xPos + ", " + zPos + ")");
            }

            super.buildComponent(componentIn, listIn, rand);
        }

        public boolean checkIntersection(int blockX, int blockZ, int sizeX, int sizeZ)
        {
            return this.checkIntersection(new StructureBoundingBox(blockX, blockZ, blockX + sizeX, blockZ + sizeZ));
        }

        public boolean checkIntersection(StructureBoundingBox bounds)
        {
            for (int i = 0; i < componentBounds.size() - 1; ++i)
            {
                StructureBoundingBox boundingBox = componentBounds.get(i);
                if (boundingBox.intersectsWith(bounds))
                {
                    return true;
                }
            }

            return false;
        }
    }

    public static class TreasureCorridor extends Corridor
    {
        public TreasureCorridor()
        {
        }

        public TreasureCorridor(IBlockState brickBlock, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeZ, EnumFacing direction)
        {
            super(brickBlock, rand, blockPosX, blockPosZ, sizeX, sizeZ, direction);
        }

        @Override
        public Piece getNextPiece(Start startPiece, Random rand)
        {
            StructureBoundingBox extension = getExtension(this.getDirection(), rand.nextInt(4) + 6, rand.nextInt(4) + 6);

            int sizeX = extension.maxX - extension.minX;
            int sizeZ = extension.maxZ - extension.minZ;
            int blockX = extension.minX;
            int blockZ = extension.minZ;

            if (startPiece.checkIntersection(extension))
            {
                return null;
            }

            return new RoomTreasure(this.brickBlock, rand, blockX, blockZ, sizeX, sizeZ, this.getDirection().getOpposite());
        }
    }

    public static class Corridor extends DirectionalPiece
    {
        public Corridor()
        {
        }

        public Corridor(IBlockState brickBlock, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeZ, EnumFacing direction)
        {
            super(brickBlock, direction);
            this.coordBaseMode = EnumFacing.SOUTH;
            this.boundingBox = new StructureBoundingBox(blockPosX, EntranceRoom.Y_POS, blockPosZ, blockPosX + sizeX, EntranceRoom.Y_POS + 5, blockPosZ + sizeZ);
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
                        if ((this.getDirection().getAxis() == EnumFacing.Axis.Z && (i == 0 || i == this.boundingBox.getXSize() - 1)) ||
                                j == 0 || j == this.boundingBox.getYSize() - 1 ||
                                (this.getDirection().getAxis() == EnumFacing.Axis.X && (k == 0 || k == this.boundingBox.getZSize() - 1)))
                        {
                            this.setBlockState(worldIn, this.brickBlock, i, j, k, this.boundingBox);
                        }
                        else
                        {
                            if (j == this.boundingBox.getYSize() - 2)
                            {
                                if (this.getDirection().getAxis() == EnumFacing.Axis.Z && (k + 1) % 4 == 0 && (i == 1 || i == this.boundingBox.getXSize() - 2))
                                {
                                    this.setBlockState(worldIn, GCBlocks.unlitTorch.getDefaultState().withProperty(BlockUnlitTorch.FACING, i == 1 ? EnumFacing.WEST.getOpposite() : EnumFacing.EAST.getOpposite()), i, j, k, this.boundingBox);
                                    continue;
                                }
                                else if (this.getDirection().getAxis() == EnumFacing.Axis.X && (i + 1) % 4 == 0 && (k == 1 || k == this.boundingBox.getZSize() - 2))
                                {
                                    this.setBlockState(worldIn, GCBlocks.unlitTorch.getDefaultState().withProperty(BlockUnlitTorch.FACING, k == 1 ? EnumFacing.NORTH.getOpposite() : EnumFacing.SOUTH.getOpposite()), i, j, k, this.boundingBox);
                                    continue;
                                }
                            }

                            this.setBlockState(worldIn, Blocks.air.getDefaultState(), i, j, k, this.boundingBox);
                        }
                    }
                }
            }

            return true;
        }

        public Piece getNextPiece(Start startPiece, Random rand)
        {
            if (Math.abs(startPiece.getBoundingBox().maxZ - boundingBox.minZ) > 200)
            {
                return null;
            }

            if (Math.abs(startPiece.getBoundingBox().maxX - boundingBox.minX) > 200)
            {
                return null;
            }

            boolean bossRoom = rand.nextInt((int) (1.0 / Math.pow(startPiece.attachedComponents.size() / 55.0, 2))) == 0;

            StructureBoundingBox extension = getExtension(this.getDirection(), bossRoom ? (rand.nextInt(6) + 14) : (rand.nextInt(4) + 6), bossRoom ? (rand.nextInt(6) + 14) : (rand.nextInt(4) + 6));

            int sizeX = extension.maxX - extension.minX;
            int sizeZ = extension.maxZ - extension.minZ;
            int blockX = extension.minX;
            int blockZ = extension.minZ;

            if (startPiece.checkIntersection(extension))
            {
                return null;
            }

            if (bossRoom)
            {
                return new RoomBoss(this.brickBlock, rand, blockX, blockZ, sizeX, sizeZ, this.getDirection().getOpposite());
            }
            else
            {
                return new RoomEmpty(this.brickBlock, rand, blockX, blockZ, sizeX, sizeZ, this.getDirection().getOpposite());
            }
        }
    }

    public static class EntranceRoom extends DirectionalPiece
    {
        private final int range = 4;
        public static final int Y_POS = 25;
        private int sizeX;
        private int sizeY;
        private int sizeZ;

        public EntranceRoom()
        {
        }

        public EntranceRoom(World world, IBlockState brickBlock, Random rand, int blockPosX, int blockPosZ)
        {
            super(brickBlock, EnumFacing.Plane.HORIZONTAL.random(rand));
            this.coordBaseMode = EnumFacing.SOUTH;

            this.sizeX = rand.nextInt(4) + 6;
            this.sizeY = rand.nextInt(2) + 5;
            this.sizeZ = rand.nextInt(4) + 6;

            this.boundingBox = new StructureBoundingBox(blockPosX - range, Y_POS, blockPosZ - range, blockPosX + range, 150, blockPosZ + range);
            System.err.println("findthis " + blockPosX + " " + blockPosZ);
        }

        @Override
        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
        {
            IBlockState block1;

            int maxLevel = 0;

            for (int i = -range; i <= range; i++)
            {
                for (int k = -range; k <= range; k++)
                {
                    int j = this.boundingBox.getYSize();

                    while (j >= 0)
                    {
                        j--;

                        Block block = getBlockStateFromPos(worldIn, i + range, j, k + range, boundingBox).getBlock();

                        if (Blocks.air != block && block != null)
                        {
                            break;
                        }
                    }

                    maxLevel = Math.max(maxLevel, j + 3);
                }
            }

            int startX = range - this.sizeX / 2;
            int startZ = range - this.sizeZ / 2;
            int endX = range + this.sizeX / 2;
            int endZ = range + this.sizeZ / 2;

            for (int i = startX; i <= endX; i++)
            {
                for (int j = 0; j <= this.sizeY; j++)
                {
                    for (int k = startZ; k <= endZ; k++)
                    {
                        if (i == startX || i == endX || j == 0 || j == this.sizeY || k == startZ || k == endZ)
                        {
                            this.setBlockState(worldIn, this.brickBlock, i, j, k, boundingBox);
                        }
                        else
                        {
                            this.setBlockState(worldIn, Blocks.air.getDefaultState(), i, j, k, boundingBox);
                        }
                    }
                }
            }

            for (int i = -range; i < range; i++)
            {
                for (int k = -range; k < range; k++)
                {
                    final double xDev = (i) / 10D;
                    final double zDev = (k) / 10D;
                    final double distance = xDev * xDev + zDev * zDev;
                    final int depth = (int) Math.abs(0.5 / (distance + .00001D));
                    int helper = 0;
                    for (int j = maxLevel; j > 1 && helper <= depth; j--)
                    {
                        block1 = this.getBlockStateFromPos(worldIn, i + range, j, k + range, boundingBox);
                        if (block1 == this.brickBlock || j != this.sizeY)
                        {
                            this.setBlockState(worldIn, Blocks.air.getDefaultState(), i + range, j, k + range, boundingBox);
                            helper++;
                        }
                    }
                }
            }

            return true;
        }

        public Piece getNextPiece(Start startPiece, Random rand)
        {
            if (startPiece.attachedComponents.isEmpty())
            {
                return getCorridor(rand, startPiece, 10, false);
            }

            return null;
        }

        @Override
        protected StructureBoundingBox getExtension(EnumFacing direction, int length, int width)
        {
            int blockX, blockZ, sizeX, sizeZ;
            int startX = this.boundingBox.minX + range - this.sizeX / 2;
            int startZ = this.boundingBox.minZ + range - this.sizeZ / 2;
            int endX = this.boundingBox.minX + range + this.sizeX / 2;
            int endZ = this.boundingBox.minZ + range + this.sizeZ / 2;
            switch (direction)
            {
            case NORTH:
                sizeX = width;
                sizeZ = length;
                blockX = startX + (endX - startX) / 2 - sizeX / 2;
                blockZ = startZ - sizeZ;
                break;
            case EAST:
                sizeX = length;
                sizeZ = width;
                blockX = endX;
                blockZ = startZ + (endZ - startZ) / 2 - sizeZ / 2;
                break;
            case SOUTH:
                sizeX = width;
                sizeZ = length;
                blockX = startX + (endX - startX) / 2 - sizeX / 2;
                blockZ = endZ;
                break;
            case WEST:
            default:
                sizeX = length;
                sizeZ = width;
                blockX = startX - sizeX;
                blockZ = startZ + (endZ - startZ) / 2 - sizeZ / 2;
                break;
            }
            return new StructureBoundingBox(blockX, blockZ, blockX + sizeX, blockZ + sizeZ);
        }
    }

    public static class RoomEmpty extends DirectionalPiece
    {
        private int sizeX;
        private int sizeY;
        private int sizeZ;

        public RoomEmpty()
        {
        }

        public RoomEmpty(IBlockState brickBlock, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeZ, EnumFacing entranceDir)
        {
            super(brickBlock, entranceDir.getOpposite());
            this.coordBaseMode = EnumFacing.SOUTH;
            this.sizeX = sizeX;
            this.sizeZ = sizeZ;
            this.sizeY = rand.nextInt(2) + 5;
            int yPos = EntranceRoom.Y_POS;

            this.boundingBox = new StructureBoundingBox(blockPosX, yPos, blockPosZ, blockPosX + this.sizeX, yPos + this.sizeY, blockPosZ + this.sizeZ);
        }

        @Override
        public boolean addComponentParts(World worldIn, Random random, StructureBoundingBox boundingBox)
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
                                if (i > start && i <= end && j < 4 && j > 0)
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
                                if (k > start && k <= end && j < 4 && j > 0)
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
                                this.setBlockState(worldIn, this.brickBlock, i, j, k, boundingBox);
                            }
                            else
                            {
                                this.setBlockState(worldIn, Blocks.air.getDefaultState(), i, j, k, boundingBox);
                            }
                        }
                        else
                        {
                            this.setBlockState(worldIn, Blocks.air.getDefaultState(), i, j, k, boundingBox);
                        }
                    }
                }
            }

            return true;
        }

        public Piece getNextPiece(Start startPiece, Random rand)
        {
            if (Math.abs(startPiece.getBoundingBox().maxZ - boundingBox.minZ) > 200)
            {
                return null;
            }

            if (Math.abs(startPiece.getBoundingBox().maxX - boundingBox.minX) > 200)
            {
                return null;
            }

            return getCorridor(rand, startPiece, 10, false);
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound tagCompound)
        {
            super.writeStructureToNBT(tagCompound);

            tagCompound.setInteger("sizeX", this.sizeX);
            tagCompound.setInteger("sizeY", this.sizeY);
            tagCompound.setInteger("sizeZ", this.sizeZ);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound tagCompound)
        {
            super.readStructureFromNBT(tagCompound);

            this.sizeX = tagCompound.getInteger("sizeX");
            this.sizeY = tagCompound.getInteger("sizeY");
            this.sizeZ = tagCompound.getInteger("sizeZ");
        }
    }

    public static class RoomBoss extends DirectionalPiece
    {
        private int sizeX;
        private int sizeY;
        private int sizeZ;

        public RoomBoss()
        {
        }

        public RoomBoss(IBlockState brickBlock, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeZ, EnumFacing entranceDir)
        {
            super(brickBlock, entranceDir.getOpposite());
            this.coordBaseMode = EnumFacing.SOUTH;
            this.sizeX = sizeX;
            this.sizeZ = sizeZ;
            this.sizeY = rand.nextInt(2) + 8;
            int yPos = EntranceRoom.Y_POS;

            this.boundingBox = new StructureBoundingBox(blockPosX, yPos, blockPosZ, blockPosX + this.sizeX, yPos + this.sizeY, blockPosZ + this.sizeZ);
        }

        @Override
        public boolean addComponentParts(World worldIn, Random random, StructureBoundingBox boundingBox)
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
                                if (i > start && i <= end && j < 4 && j > 0)
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
                                if (k > start && k <= end && j < 4 && j > 0)
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
                                this.setBlockState(worldIn, this.brickBlock, i, j, k, boundingBox);
                            }
                            else
                            {
                                this.setBlockState(worldIn, Blocks.air.getDefaultState(), i, j, k, boundingBox);
                            }
                        }
                        else if ((i == 1 && k == 1) || (i == 1 && k == this.sizeZ - 1) || (i == this.sizeX - 1 && k == 1) || (i == this.sizeX - 1 && k == this.sizeZ - 1))
                        {
                            this.setBlockState(worldIn, Blocks.flowing_lava.getDefaultState(), i, j, k, boundingBox);
                        }
                        else if (j % 3 == 0 && j >= 2 && ((i == 1 || i == this.sizeX - 1 || k == 1 || k == this.sizeZ - 1) || (i == 2 && k == 2) || (i == 2 && k == this.sizeZ - 2) || (i == this.sizeX - 2 && k == 2) || (i == this.sizeX - 2 && k == this.sizeZ - 2)))
                        {
                            // Horizontal bars
                            this.setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), i, j, k, boundingBox);
                        }
                        else if ((i == 1 && k == 2) || (i == 2 && k == 1) ||
                                (i == 1 && k == this.sizeZ - 2) || (i == 2 && k == this.sizeZ - 1) ||
                                (i == this.sizeX - 1 && k == 2) || (i == this.sizeX - 2 && k == 1) ||
                                (i == this.sizeX - 1 && k == this.sizeZ - 2) || (i == this.sizeX - 2 && k == this.sizeZ - 1))
                        {
                            // Vertical bars
                            this.setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), i, j, k, boundingBox);
                        }
                        else
                        {
                            this.setBlockState(worldIn, Blocks.air.getDefaultState(), i, j, k, boundingBox);
                        }
                    }
                }
            }

            return true;
        }

        public Piece getNextPiece(Start startPiece, Random rand)
        {
            return getCorridor(rand, startPiece, 10, true);
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound tagCompound)
        {
            super.writeStructureToNBT(tagCompound);

            tagCompound.setInteger("sizeX", this.sizeX);
            tagCompound.setInteger("sizeY", this.sizeY);
            tagCompound.setInteger("sizeZ", this.sizeZ);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound tagCompound)
        {
            super.readStructureFromNBT(tagCompound);

            this.sizeX = tagCompound.getInteger("sizeX");
            this.sizeY = tagCompound.getInteger("sizeY");
            this.sizeZ = tagCompound.getInteger("sizeZ");
        }
    }

    public static class RoomTreasure extends DirectionalPiece
    {
        private int sizeX;
        private int sizeY;
        private int sizeZ;

        public RoomTreasure()
        {
        }

        public RoomTreasure(IBlockState brickBlock, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeZ, EnumFacing entranceDir)
        {
            super(brickBlock, entranceDir.getOpposite());
            this.coordBaseMode = EnumFacing.SOUTH;
            this.sizeX = sizeX;
            this.sizeZ = sizeZ;
            this.sizeY = rand.nextInt(2) + 5;
            int yPos = EntranceRoom.Y_POS;

            this.boundingBox = new StructureBoundingBox(blockPosX, yPos, blockPosZ, blockPosX + this.sizeX, yPos + this.sizeY, blockPosZ + this.sizeZ);
        }

        @Override
        public boolean addComponentParts(World worldIn, Random random, StructureBoundingBox boundingBox)
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
                                if (i > 2 && i < 5 && j < 4 && j > 0)
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
                                if (k > 2 && k < 5 && j < 4 && j > 0)
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
                                this.setBlockState(worldIn, this.brickBlock, i, j, k, boundingBox);
                            }
                            else
                            {
                                this.setBlockState(worldIn, Blocks.air.getDefaultState(), i, j, k, boundingBox);
                            }
                        }
                        else if ((i == 1 && k == 1) || (i == 1 && k == this.sizeZ - 1) || (i == this.sizeX - 1 && k == 1) || (i == this.sizeX - 1 && k == this.sizeZ - 1))
                        {
                            this.setBlockState(worldIn, Blocks.glowstone.getDefaultState(), i, j, k, boundingBox);
                        }
                        else if (i == this.sizeX / 2 && j == 1 && k == this.sizeZ / 2)
                        {
                            this.setBlockState(worldIn, GCBlocks.treasureChestTier1.getDefaultState().withProperty(BlockT1TreasureChest.FACING, this.getDirection().getOpposite()), i, j, k, boundingBox);
                        }
                        else
                        {
                            this.setBlockState(worldIn, Blocks.air.getDefaultState(), i, j, k, boundingBox);
                        }
                    }
                }
            }

            this.setBlockState(worldIn, Blocks.glowstone.getDefaultState(), 1, 1, 1, boundingBox);

            return true;
        }

        public Piece getNextPiece(Start startPiece, Random rand)
        {
            return null;
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound tagCompound)
        {
            super.writeStructureToNBT(tagCompound);

            tagCompound.setInteger("sizeX", this.sizeX);
            tagCompound.setInteger("sizeY", this.sizeY);
            tagCompound.setInteger("sizeZ", this.sizeZ);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound tagCompound)
        {
            super.readStructureFromNBT(tagCompound);

            this.sizeX = tagCompound.getInteger("sizeX");
            this.sizeY = tagCompound.getInteger("sizeY");
            this.sizeZ = tagCompound.getInteger("sizeZ");
        }
    }
}
