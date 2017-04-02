package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base.BaseRoom.EnumRoomType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaseDeck extends SizedPiece
{
    static final int ROOMSMALL = 4;
    static final int ROOMLARGE = 8;
    private int roomsOnSide;
    private int roomDepth;
    private int largeA;
    private int largeB;
    
    public enum EnumDeckType 
    {
        HUMANOID(3, 3, GCBlocks.basicBlock.getStateFromMeta(4)),
        AVIAN(3, 3, GCBlocks.blockMoon.getStateFromMeta(4)),
        TUNNELER(4, 4, GCBlocks.blockMoon.getStateFromMeta(4));
        
        public final int height;
        public final int width;
        public final IBlockState wall;
        
        EnumDeckType(int height, int width, IBlockState wallBlock)
        {
            this.height = height;
            this.width = width;
            this.wall = wallBlock;
        }
    }
    
    public BaseDeck()
    {
    }

    public BaseDeck(World world, BaseConfiguration configuration, Random rand, int blockPosX, int blockPosZ)
    {
        super(configuration, configuration.getCorridorWidth() + 1, configuration.getRoomHeight() + 1, configuration.getCorridorLength(), EnumFacing.NORTH);//TODO EnumFacing.Plane.HORIZONTAL.random(rand));
        this.roomsOnSide = configuration.getRoomsNo();
        this.roomDepth = rand.nextInt(3) + 5;
        this.coordBaseMode = EnumFacing.NORTH;
        if (this.getDirection().getAxis() == EnumFacing.Axis.X)
        {
            int w = this.sizeX;
            this.sizeX = this.sizeZ;
            this.sizeZ = w;
        }
        int xMin = blockPosX - this.sizeX / 2;
        int zMin = blockPosZ - this.sizeZ / 2;
        this.boundingBox = new StructureBoundingBox(xMin, configuration.getYPosition(), zMin, xMin + this.sizeX, configuration.getYPosition() + this.sizeY, zMin + this.sizeZ);
        System.out.println("Generating base at " + blockPosX + " " + configuration.getYPosition() + " " + blockPosZ);
        if (this.roomsOnSide <= 1)
        {
            largeA = 2;
            largeB = 2;
        }
        else
        {
            largeA = rand.nextInt(this.roomsOnSide - 1);
            largeB = largeA + 1 + rand.nextInt(this.roomsOnSide - largeA - 1);
        }
    }

    @Override
    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
    {
        IBlockState block1;
        IBlockState blockWall = this.configuration.getWallBlock();
        IBlockState blockGrid = AsteroidBlocks.blockWalkway.getDefaultState();
        Block blockStair = GCBlocks.moonStoneStairs;
        IBlockState blockAir = Blocks.air.getDefaultState();
        boolean directionNS = this.getDirection().getAxis() == EnumFacing.Axis.Z;
        int ceilingSpacer = (directionNS ? this.sizeZ : this.sizeX) / this.roomsOnSide + 1;
        int ceilingDeco = ceilingSpacer;

        int startX = 0;
        int startZ = 0;
        int endX = this.sizeX;
        int endZ = this.sizeZ;

        //This is the central corridor of every base
        for (int x = startX; x <= endX; x++)
        {
            for (int y = 0; y <= this.sizeY; y++)
            {
                for (int z = startZ; z <= endZ; z++)
                {
                    if (x == startX || x == endX || y == 0 || y == this.sizeY || z == startZ || z == endZ)
                    {
                        if (this.configuration.isHangarDeck() && (this.getDirection() == EnumFacing.NORTH && z == endZ || this.getDirection() == EnumFacing.SOUTH && z == startZ || this.getDirection() == EnumFacing.WEST && x == endX || this.getDirection() == EnumFacing.EAST && x == startX))
                        {
                            //Make airlock at the hangar end, if it's a hangar deck
                            IBlockState blockLintel = GCBlocks.airLockFrame.getDefaultState();
                            IBlockState blockAirlock = GCBlocks.airLockFrame.getStateFromMeta(1);
                            if ((!directionNS && (z == startZ + 1 || z == endZ - 1) || directionNS && (x == startX + 1 || x == endX - 1)) && y < sizeY)
                            {
                                this.setBlockState(worldIn, blockLintel, x, y, z, boundingBox);
                            }
                            else if (y == this.sizeY || directionNS && (x == startX || x == endX) || !directionNS && (z == startZ || z == endZ))
                            {
                                this.setBlockState(worldIn, blockWall, x, y, z, boundingBox);
                            }
                            else if (y == 0 || y == this.sizeY - 1)
                            {
                                //Test mid point bottom
                                if (y == 0 && (!directionNS && z == (startZ + endZ) / 2 || directionNS && x == (startX + endX) / 2))
                                    this.setBlockState(worldIn, blockAirlock, x, y, z, boundingBox);
                                else
                                    this.setBlockState(worldIn, blockLintel, x, y, z, boundingBox);
                            }
                            else
                            {
                                this.setBlockState(worldIn, blockAir, x, y, z, boundingBox);
                            }
                        }
                        else if (!((y == 0 || y == this.sizeY) && (directionNS && (z == startZ || z == endZ) || !directionNS && (x == startX || x == endX))))
                        {
                            if (y == 0 && this.configuration.getDeckType() == EnumDeckType.HUMANOID && (directionNS && (x == startX + 2 + (this.configuration.isHangarDeck() ? 1 : 0) || x == startX + 4 + (this.configuration.isHangarDeck() ? 1 : 0)) || !directionNS && (z == startZ + 2 + (this.configuration.isHangarDeck() ? 1 : 0) || z == startZ + 4 + (this.configuration.isHangarDeck() ? 1 : 0))))
                            {
                                this.setBlockState(worldIn, blockGrid, x, y, z, boundingBox);
                            }
                            else
                            {
                                this.setBlockState(worldIn, blockWall, x, y, z, boundingBox);
                            }
                        }
                    }
                    else if (this.configuration.getDeckType().ordinal() >= EnumDeckType.AVIAN.ordinal() && (y == 1 || y == this.sizeY - 1))
                    {
                        int top = (y == 1) ? 0 : 4;
                        if (this.configuration.getDeckType() == EnumDeckType.TUNNELER)
                            top ++;
                        
                        if (directionNS)
                        {
                            if (x == startX + 1)
                            {
                                this.setBlockState(worldIn, blockStair.getStateFromMeta(0 ^ top), x, y, z, boundingBox);
                            }
                            else if (x == endX - 1)
                            {
                                this.setBlockState(worldIn, blockStair.getStateFromMeta(1 ^ top), x, y, z, boundingBox);
                            }
                            
                            if (this.configuration.getDeckType() == EnumDeckType.AVIAN)
                            {
                                if (z == ceilingDeco && x == 2 && top >= 4)
                                {
                                    this.setBlockState(worldIn, blockStair.getStateFromMeta(3 ^ top), x, y, z, boundingBox);
                                }
                                else if (z == ceilingDeco + 1 && x == 2 && top >= 4)
                                {
                                    this.setBlockState(worldIn, blockStair.getStateFromMeta(2 ^ top), x, y, z, boundingBox);
                                    ceilingDeco += ceilingSpacer;
                                }
                            }
                        }
                        else
                        {
                            if (z == startZ + 1)
                            {
                                this.setBlockState(worldIn, blockStair.getStateFromMeta(3 ^ top), x, y, z, boundingBox);
                            }
                            else if (z == endZ - 1)
                            {
                                this.setBlockState(worldIn, blockStair.getStateFromMeta(2 ^ top), x, y, z, boundingBox);
                            }
                            
                            if (x == ceilingDeco && z == 2 && top == 4)
                            {
                                this.setBlockState(worldIn, blockStair.getStateFromMeta(0 ^ top), x, y, z, boundingBox);
                            }
                            else if (x == ceilingDeco + 1 && z == 2 && top == 4)
                            {
                                this.setBlockState(worldIn, blockStair.getStateFromMeta(1 ^ top), x, y, z, boundingBox);
                                ceilingDeco += ceilingSpacer;
                            }
                        }
                    }
                    else
                    {
                        this.setBlockState(worldIn, blockAir, x, y, z, boundingBox);
                    }
                }
            }
        }

        int leftX = 2;
        int leftZ = 2;
        int rightX = 2;
        int rightZ = 2;
        for (int i = 0; i < this.roomsOnSide; i++)
        {
            boolean largeRoom = (i == largeA || i == largeB);
            int roomsize = largeRoom ? ROOMLARGE : ROOMSMALL;

            if (directionNS)
            {
                this.makeDoorway(worldIn, this.sizeX, leftZ, directionNS);
                this.makeDoorway(worldIn, 0, rightZ, directionNS);

                leftZ += roomsize;
                rightZ += roomsize;
            }
            else
            {
                this.makeDoorway(worldIn, leftX, this.sizeZ, directionNS);
                this.makeDoorway(worldIn, rightX, 0, directionNS);

                leftX += roomsize;
                rightX += roomsize;
            }
        }

        return true;
    }
    
    public List<Piece> getRooms(BaseStart startPiece, Random rand)
    {
        List<Piece> rooms = new ArrayList<Piece>();
        
        boolean directionNS = this.getDirection().getAxis() == EnumFacing.Axis.Z;
        EnumFacing left = directionNS ? EnumFacing.WEST : EnumFacing.NORTH;
        EnumFacing right = directionNS ? EnumFacing.EAST : EnumFacing.SOUTH;
        int leftX = directionNS ? this.boundingBox.maxX + 1: this.boundingBox.minX;
        int leftZ = directionNS ? this.boundingBox.minZ : this.boundingBox.maxZ + 1;
        int rightX = directionNS ? this.boundingBox.minX - this.roomDepth - 1: this.boundingBox.minX;
        int rightZ = directionNS ? this.boundingBox.minZ : this.boundingBox.minZ - this.roomDepth - 1;
        for (int i = 0; i < this.roomsOnSide; i++)
        {
            boolean largeRoom = (i == largeA || i == largeB);
            int roomsize = largeRoom ? ROOMLARGE : ROOMSMALL;
            rooms.add(this.getRoom(i, left, leftX, leftZ, largeRoom, true, rand));
            rooms.add(this.getRoom(i, right, rightX, rightZ, largeRoom, false, rand));
            if (directionNS)
            {
                leftZ += roomsize;
                rightZ += roomsize;
            }
            else
            {
                leftX += roomsize;
                rightX += roomsize;
            }
        }
        
        if (this.configuration.isHangarDeck())
        {
            int blockX = 0;
            int blockZ = 0;
            switch(this.direction)
            {
            case NORTH:
                blockX = this.boundingBox.minX + (this.sizeX - BaseHangar.HANGARWIDTH) / 2;
                blockZ = this.boundingBox.maxZ + 1;
                break;
            case SOUTH:
                blockX = this.boundingBox.minX + (this.sizeX - BaseHangar.HANGARWIDTH) / 2;
                blockZ = this.boundingBox.minZ - 1 - BaseHangar.HANGARLENGTH;
                break;
            case WEST:
                blockX = this.boundingBox.maxX + 1;
                blockZ = this.boundingBox.minZ + (this.sizeZ - BaseHangar.HANGARWIDTH) / 2;
                break;
            case EAST:
            default:
                blockX = this.boundingBox.minX - 1 - BaseHangar.HANGARLENGTH;
                blockZ = this.boundingBox.minZ + (this.sizeZ - BaseHangar.HANGARWIDTH) / 2;
            }
            
            rooms.add(new BaseHangar(this.configuration, rand, blockX, blockZ, this.direction.getOpposite()));
        }
        
        return rooms;
    }
    
    private void makeDoorway(World worldIn, int x, int z, boolean directionNS)
    {
        IBlockState blockLintel = GCBlocks.airLockFrame.getDefaultState();
        IBlockState blockAirlock = GCBlocks.airLockFrame.getStateFromMeta(1);
        Block blockStair = GCBlocks.moonStoneStairs;
        IBlockState blockAir = Blocks.air.getDefaultState();
        int meta = directionNS ? 2 : 0;
        
        switch (this.configuration.getDeckType())
        {
        case HUMANOID:
            if (directionNS) z--; else x--;
            this.setBlockState(worldIn, blockLintel, x, 1, z, boundingBox);
            this.setBlockState(worldIn, blockLintel, x, 2, z, boundingBox);
            if (directionNS) z++; else x++;
            this.setBlockState(worldIn, blockAir, x, 1, z, boundingBox);
            this.setBlockState(worldIn, blockAir, x, 2, z, boundingBox);
            this.setBlockState(worldIn, blockAirlock, x, 0, z, boundingBox);
            this.setBlockState(worldIn, blockLintel, x, 3, z, boundingBox);
            if (directionNS) z++; else x++;
            this.setBlockState(worldIn, blockLintel, x, 1, z, boundingBox);
            this.setBlockState(worldIn, blockLintel, x, 2, z, boundingBox);
            break;
        case AVIAN:
            this.setBlockState(worldIn, blockStair.getStateFromMeta(0 + meta), x, 1, z, boundingBox);
            this.setBlockState(worldIn, blockAir, x, 2, z, boundingBox);
            this.setBlockState(worldIn, blockStair.getStateFromMeta(4 + meta), x, 3, z, boundingBox);
            if (directionNS) z++; else x++;
            this.setBlockState(worldIn, blockStair.getStateFromMeta(1 + meta), x, 1, z, boundingBox);
            this.setBlockState(worldIn, blockAir, x, 2, z, boundingBox);
            this.setBlockState(worldIn, blockStair.getStateFromMeta(5 + meta), x, 3, z, boundingBox);
            break;
        case TUNNELER:
            if (directionNS) z--; else x--;
            this.setBlockState(worldIn, blockStair.getStateFromMeta(1 + meta), x, 2, z, boundingBox);
            this.setBlockState(worldIn, blockStair.getStateFromMeta(5 + meta), x, 3, z, boundingBox);
            if (directionNS) z++; else x++;
            this.setBlockState(worldIn, blockAir, x, 2, z, boundingBox);
            this.setBlockState(worldIn, blockAir, x, 3, z, boundingBox);
            if (directionNS) z++; else x++;
            this.setBlockState(worldIn, blockStair.getStateFromMeta(0 + meta), x, 2, z, boundingBox);
            this.setBlockState(worldIn, blockStair.getStateFromMeta(4 + meta), x, 3, z, boundingBox);
            break;
        default:
        }
            
    }

    private Piece getRoom(int i, EnumFacing dir, int blockX, int blockZ, boolean large, boolean left, Random rand)
    {
        int sX = large ? ROOMLARGE : ROOMSMALL;
        int sZ = sX;
        if (dir.getAxis() == EnumFacing.Axis.Z)
        {
            sZ = this.roomDepth;
        }
        else
        {
            sX = this.roomDepth;
        }
        int sY = this.sizeY;
        int choices = EnumRoomType.values().length;
        EnumRoomType block = EnumRoomType.values()[(i * 2 + (left ? 0 : 1)) % choices];
        return new BaseRoom(this.configuration, rand, blockX, blockZ, sX, sY, sZ, dir, block, left ? (i == 0) : (i == this.roomsOnSide - 1), left ? (i == this.roomsOnSide - 1) : (i == 0));
    }
}