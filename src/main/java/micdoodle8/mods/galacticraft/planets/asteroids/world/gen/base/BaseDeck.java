package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base.BaseRoom.EnumRoomType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaseDeck extends SizedPiece
{
    static final int ROOMSMALL = 4;
    static final int ROOMLARGE = 8;
    protected int roomsOnSide;
    protected int roomDepth;
    protected int largeRoomPosA;
    protected int largeRoomPosB;
    protected int deckTier; // flag 1 means lowest deck in a stack, flag 2 is for highest deck, 0 is for middle decks - flag 4 is for control room
    private ArrayList<BaseDeck> otherDecks = null;
    
    public enum EnumBaseType 
    {
        HUMANOID(3, 3, GCBlocks.basicBlock.getStateFromMeta(4)),
        AVIAN(3, 3, GCBlocks.blockMoon.getStateFromMeta(4)),
        TUNNELER(4, 4, GCBlocks.blockMoon.getStateFromMeta(4));
        
        public final int height;
        public final int width;
        public final IBlockState wall;
        
        EnumBaseType(int height, int width, IBlockState wallBlock)
        {
            this.height = height;
            this.width = width;
            this.wall = wallBlock;
        }
    }
    
    public BaseDeck()
    {
    }

    public BaseDeck(BaseConfiguration configuration, Random rand, int blockPosX, int blockPosY, int blockPosZ, int tier, EnumFacing dir)
    {
        super(configuration, configuration.getCorridorWidth() + 1, configuration.getRoomHeight() + 1, configuration.getCorridorLength(), dir);
        this.roomsOnSide = configuration.getRoomsNo();
        this.roomDepth = configuration.getRoomDepth() + (tier == 0 ? 1 : 0);
        this.deckTier = tier;
        this.setCoordBaseMode(EnumFacing.NORTH);
        if (this.getDirection().getAxis() == EnumFacing.Axis.X)
        {
            int w = this.sizeX;
            this.sizeX = this.sizeZ;
            this.sizeZ = w;
        }
        int xMin = blockPosX - this.sizeX / 2;
        int zMin = blockPosZ - this.sizeZ / 2;
        this.boundingBox = new StructureBoundingBox(xMin, blockPosY, zMin, xMin + this.sizeX, blockPosY + this.sizeY, zMin + this.sizeZ);
        if (this.roomsOnSide <= 1)
        {
            largeRoomPosA = 0;
            largeRoomPosB = 2;
        }
        else
        {
            largeRoomPosA = rand.nextInt(this.roomsOnSide - 1);
            largeRoomPosB = largeRoomPosA + 1 + rand.nextInt(this.roomsOnSide - largeRoomPosA - 1);
        }
        
        if (tier == 1)  //1 means lowest tier and there are going to be some higher ones
        {
            this.createOtherDecks(configuration, rand, blockPosX, blockPosY, blockPosZ);
        }
        
        //Special settings for Control Room
        if ((tier & 4) == 4)
        {
            this.roomDepth -= 3;
            if (this.getDirection().getAxis() == EnumFacing.Axis.X)
            {
                this.sizeZ += 6;
            }
            else
            {
                this.sizeX += 6;
            }
            xMin = blockPosX - this.sizeX / 2;
            zMin = blockPosZ - this.sizeZ / 2;
            switch(this.getDirection())
            {
            case NORTH:
                this.sizeZ++;
                break;
            case SOUTH:
                zMin--;
                this.sizeZ++;
                break;
            case WEST:
                this.sizeX++;
                break;
            case EAST:
            default:
                xMin--;
                this.sizeX++;
                break;
            }
            this.boundingBox = new StructureBoundingBox(xMin, blockPosY, zMin, xMin + this.sizeX, blockPosY + this.sizeY, zMin + this.sizeZ);
        }
    }
    
    private void createOtherDecks(BaseConfiguration configuration, Random rand, int blockPosX, int blockPosY, int blockPosZ)
    {
        this.otherDecks = new ArrayList<BaseDeck>();
        if (this.configuration.isHangarDeck())
        {
            this.otherDecks.add(new BaseDeck(configuration, rand, blockPosX, blockPosY + this.sizeY + 1, blockPosZ, 6, this.direction));
        }
        else
        {
            this.otherDecks.add(new BaseDeck(configuration, rand, blockPosX, blockPosY + this.sizeY + 1, blockPosZ, 0, this.direction));
            this.otherDecks.add(new BaseDeck(configuration, rand, blockPosX, blockPosY + this.sizeY + this.sizeY + 1, blockPosZ, 2, this.direction));
        }
    }
    
    @Override
    protected void writeStructureToNBT(NBTTagCompound tagCompound)
    {
        super.writeStructureToNBT(tagCompound);

        int details = this.deckTier + (this.largeRoomPosA << 4) + (this.largeRoomPosB << 8);
        tagCompound.setInteger("dD", details);
        if (this.otherDecks != null)
        {
            NBTTagList tagList = new NBTTagList();
            for (BaseDeck deck : this.otherDecks)
            {
                NBTTagCompound tag1 = new NBTTagCompound();
                deck.writeStructureToNBT(tag1);
                tagList.appendTag(tag1);
            }
            tagCompound.setTag("oD", tagList);
        }
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager manager)
    {
        super.readStructureFromNBT(tagCompound, manager);
        try
        {
            int details = tagCompound.getInteger("dD");
            this.deckTier = details & 15;
            this.largeRoomPosB = details >> 8;
            this.largeRoomPosA = (details >> 4) & 15;
            this.roomsOnSide = configuration.getRoomsNo();
            this.roomDepth = configuration.getRoomDepth() + (this.deckTier == 0 ? 1 : 0) - ((this.deckTier & 4) == 4 ? 3 : 0);
            
            if (tagCompound.hasKey("oD"))
            {
                NBTTagList tagList = tagCompound.getTagList("oD", 10);
                this.otherDecks = new ArrayList<BaseDeck>();
                for (int i = 0; i < tagList.tagCount(); i++)
                {
                    NBTTagCompound tagAt = tagList.getCompoundTagAt(i);
                    BaseDeck deck = new BaseDeck();
                    deck.readStructureFromNBT(tagAt, manager);
                    this.otherDecks.add(deck);
                }
            }
        }
        catch (Exception e)
        {
            System.err.println("Failed to read Abandoned Base configuration from NBT");
            System.err.println(tagCompound.toString());
        }
    }

    @Override
    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
    {
        IBlockState block1;
        IBlockState blockWall = this.configuration.getWallBlock();
        IBlockState blockGrid = AsteroidBlocks.blockWalkway.getDefaultState();
        Block blockStair = GCBlocks.moonStoneStairs;
        IBlockState blockAir = Blocks.AIR.getDefaultState();
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
                        boolean skipCap = false;
                        if (this.configuration.isHangarDeck() && this.deckTier == 1 && (this.getDirection() == EnumFacing.NORTH && z == endZ || this.getDirection() == EnumFacing.SOUTH && z == startZ || this.getDirection() == EnumFacing.WEST && x == endX || this.getDirection() == EnumFacing.EAST && x == startX))
                        {
                            //Make airlock at the hangar end, if it's a hangar deck
                            IBlockState blockLintel = GCBlocks.airLockFrame.getDefaultState();
                            IBlockState blockAirlock = GCBlocks.airLockFrame.getStateFromMeta(1);
                            if ((!directionNS && (z == startZ + 1 || z == endZ - 1) || directionNS && (x == startX + 1 || x == endX - 1)) && y < sizeY - 1)
                            {
                                this.setBlockState(worldIn, blockLintel, x, y, z, boundingBox);
                            }
                            else if (y >= this.sizeY - 1 || directionNS && (x == startX || x == endX) || !directionNS && (z == startZ || z == endZ))
                            {
                                this.setBlockState(worldIn, blockWall, x, y, z, boundingBox);
                            }
                            else if (y == 0 || y == this.sizeY - 2)
                            {
                                //Test mid point bottom
                                if (y == 0 && (!directionNS && z == (startZ + endZ) / 2 || directionNS && x == (startX + endX) / 2))
                                    this.setBlockState(worldIn, blockAirlock, x, y, z, boundingBox);
                                else
                                    this.setBlockState(worldIn, blockLintel, x, y, z, boundingBox);
                                skipCap = true;
                            }
                            else
                            {
                                this.setBlockState(worldIn, blockAir, x, y, z, boundingBox);
                            }
                        }
                        //Skip certain blocks at top and bottom to give chamfered edge
                        else if (!((y == 0 && (this.deckTier & 1) == 1 || y == this.sizeY && (this.deckTier & 1) == 2) && (directionNS && (z == startZ || z == endZ) || !directionNS && (x == startX || x == endX))))
                        {
                            if (y == 0 && (this.deckTier & 1) == 1 && this.configuration.getDeckType() == EnumBaseType.HUMANOID && (directionNS && (x == startX + 2 + (this.configuration.isHangarDeck() ? 1 : 0) || x == startX + 4 + (this.configuration.isHangarDeck() ? 1 : 0)) || !directionNS && (z == startZ + 2 + (this.configuration.isHangarDeck() ? 1 : 0) || z == startZ + 4 + (this.configuration.isHangarDeck() ? 1 : 0))))
                            {
                                this.setBlockState(worldIn, blockGrid, x, y, z, boundingBox);
                            }
                            else
                            {
                                this.setBlockState(worldIn, blockWall, x, y, z, boundingBox);
                            }
                        }
                        
                        //Special case: cap the grid ends
                        if (y == 0 && (this.deckTier & 1) == 1 && this.configuration.getDeckType() == EnumBaseType.HUMANOID && (directionNS && (x >= startX + 2 + (this.configuration.isHangarDeck() ? 1 : 0) && x <= startX + 4 + (this.configuration.isHangarDeck() ? 1 : 0)) || !directionNS && (z >= startZ + 2 + (this.configuration.isHangarDeck() ? 1 : 0) && z <= startZ + 4 + (this.configuration.isHangarDeck() ? 1 : 0))))
                        {
                            if (directionNS && (z == startZ || z == endZ) || !directionNS && (x == startX || x == endX))
                            {
                                if (!skipCap)
                                {
                                    this.setBlockState(worldIn, blockWall, x, y, z, boundingBox);
                                }
                            }
                        }
                        
                    }
                    else if (this.configuration.getDeckType().ordinal() >= EnumBaseType.AVIAN.ordinal() && (y == 1 || y == this.sizeY - 1))
                    {
                        int top = (y == 1) ? 0 : 4;
                        if (this.configuration.getDeckType() == EnumBaseType.TUNNELER)
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
                            
                            if (this.configuration.getDeckType() == EnumBaseType.AVIAN)
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
            boolean largeRoom = (i == largeRoomPosA || i == largeRoomPosB);
            int roomsize = largeRoom ? ROOMLARGE : ROOMSMALL;
            int largeRoomOffset = largeRoom ? 2 : 0;

            if (directionNS)
            {
                this.makeDoorway(worldIn, this.sizeX, leftZ + largeRoomOffset, directionNS);
                this.makeDoorway(worldIn, 0, rightZ + largeRoomOffset, directionNS);

                leftZ += roomsize;
                rightZ += roomsize;
            }
            else
            {
                this.makeDoorway(worldIn, leftX + largeRoomOffset, this.sizeZ, directionNS);
                this.makeDoorway(worldIn, rightX + largeRoomOffset, 0, directionNS);

                leftX += roomsize;
                rightX += roomsize;
            }
        }
        
        if (directionNS)
        {
            this.setCoordBaseMode(this.direction);
        }
        else
        {
            this.setCoordBaseMode(this.direction.getOpposite()); //bug in Vanilla?
        }
        if (this.configuration.isHangarDeck() && this.deckTier == 1)
        {
            //Create an access hole between the floors
            int width = directionNS ? this.sizeX : this.sizeZ;
            this.setBlockState(worldIn, blockAir, 0, this.sizeY, 1, boundingBox);
            this.setBlockState(worldIn, blockAir, 1, this.sizeY, 1, boundingBox);
            this.setBlockState(worldIn, blockAir, 1, this.sizeY - 1, 1, boundingBox);
            this.setBlockState(worldIn, blockAir, width - 1, this.sizeY - 1, 1, boundingBox);
            this.setBlockState(worldIn, blockAir, width - 1, this.sizeY, 1, boundingBox);
            this.setBlockState(worldIn, blockAir, width, this.sizeY, 1, boundingBox);
        }

        //Special settings for Control Room
        if ((this.deckTier & 4) == 4)
        {
            IBlockState blockGlass;
            switch (this.configuration.getDeckType())
            {
            case AVIAN:
                blockGlass = GCBlocks.spaceGlassTinVanilla.getDefaultState();
                break;
            case TUNNELER:
                blockGlass = GCBlocks.spaceGlassTinStrong.getDefaultState();
                break;
            case HUMANOID:
            default:
                blockGlass = GCBlocks.spaceGlassTinClear.getDefaultState();
                break;
            }
            if (!directionNS)
            {
                int w = endX;
                endX = endZ;
                endZ = w;
            }
            for (int y = 1; y < 5; y++)
            {
                for (int x = 3; x < endX - 2; x++)
                {
                    this.setBlockState(worldIn, blockGlass, x, y, endZ, boundingBox);
                }
            }
            
            //Create an access hole between the floors
            this.setBlockState(worldIn, blockAir, 4, 0, 1, boundingBox);
            this.setBlockState(worldIn, blockAir, 3, 0, 1, boundingBox);
            this.setBlockState(worldIn, blockAir, 11, 0, 1, boundingBox);
            this.setBlockState(worldIn, blockAir, 10, 0, 1, boundingBox);
        }
        this.setCoordBaseMode(EnumFacing.NORTH);

        return true;
    }
    
    public List<Piece> getRooms(BaseStart startPiece, Random rand)
    {
        List<Piece> rooms = new ArrayList<Piece>();
        
        boolean directionNS = this.getDirection().getAxis() == EnumFacing.Axis.Z;
        EnumFacing left = directionNS ? EnumFacing.WEST : EnumFacing.SOUTH;
        EnumFacing right = directionNS ? EnumFacing.EAST : EnumFacing.NORTH;
        int leftX = directionNS ? this.boundingBox.maxX + 1: this.boundingBox.minX;
        int leftZ = directionNS ? this.boundingBox.minZ : this.boundingBox.maxZ + 1;
        int rightX = directionNS ? this.boundingBox.minX - this.roomDepth - 1: this.boundingBox.minX;
        int rightZ = directionNS ? this.boundingBox.minZ : this.boundingBox.minZ - this.roomDepth - 1;
        //now offset effect of moving control room 1 position North or West, in two cases
        if ((this.deckTier & 4) == 4 && this.direction == EnumFacing.SOUTH)
        {
            rightZ++;
            leftZ++;
        }
        if ((this.deckTier & 4) == 4 && this.direction == EnumFacing.EAST)
        {
            rightX++;
            leftX++;
        }
        
        for (int i = 0; i < this.roomsOnSide; i++)
        {
            boolean largeRoom = (i == largeRoomPosA || i == largeRoomPosB);
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
        
        if (this.configuration.isHangarDeck() && (this.deckTier & 1) == 1)
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

        if ((this.configuration.isHangarDeck() || this.configuration.getDeckType() == EnumBaseType.HUMANOID) && (this.deckTier & 1) == 1)
        {
            int blockX = this.boundingBox.minX;
            int blockZ = this.boundingBox.minZ;
            int sX = this.sizeX;
            int sZ = this.sizeZ;
            switch(this.direction)
            {
            case NORTH:
                blockZ++;
                sZ--;
                if (!this.configuration.isHangarDeck()) sZ--;
                break;
            case SOUTH:
                sZ--;
                if (!this.configuration.isHangarDeck())
                {
                    blockZ++;
                    sZ--;
                }
                break;
            case WEST:
                blockX++;
                sX--;
                if (!this.configuration.isHangarDeck()) sX--;
                break;
            case EAST:
            default:
                sX--;
                if (!this.configuration.isHangarDeck())
                {
                    blockX++;
                    sX--;
                }
            }
            rooms.add(new BasePlate(this.configuration, blockX, this.boundingBox.minY - 1, blockZ, sX, sZ, this.direction));
        }
        
        if (this.otherDecks != null)
        {
            for (BaseDeck deck : this.otherDecks)
            {
                rooms.add(deck);
                rooms.addAll(deck.getRooms(startPiece, rand));
            }
        }
        
        return rooms;
    }
    
    protected void makeDoorway(World worldIn, int x, int z, boolean directionNS)
    {
        IBlockState blockLintel = GCBlocks.airLockFrame.getDefaultState();
        IBlockState blockAirlock = GCBlocks.airLockFrame.getStateFromMeta(1);
        Block blockStair = GCBlocks.moonStoneStairs;
        IBlockState blockAir = Blocks.AIR.getDefaultState();
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
            this.setBlockState(worldIn, blockStair.getStateFromMeta(0 + meta), x, this.sizeY - 4, z, boundingBox);
            this.setBlockState(worldIn, blockAir, x, this.sizeY - 3, z, boundingBox);
            this.setBlockState(worldIn, blockStair.getStateFromMeta(4 + meta), x, this.sizeY - 2, z, boundingBox);
            if (directionNS) z++; else x++;
            this.setBlockState(worldIn, blockStair.getStateFromMeta(1 + meta), x, this.sizeY - 4, z, boundingBox);
            this.setBlockState(worldIn, blockAir, x, this.sizeY - 3, z, boundingBox);
            this.setBlockState(worldIn, blockStair.getStateFromMeta(5 + meta), x, this.sizeY - 2, z, boundingBox);
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

    protected Piece getRoom(int i, EnumFacing dir, int blockX, int blockZ, boolean large, boolean left, Random rand)
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
        return new BaseRoom(this.configuration, rand, blockX, this.boundingBox.minY, blockZ, sX, sY, sZ, dir, block, left ? (i == 0) : (i == this.roomsOnSide - 1), left ? (i == this.roomsOnSide - 1) : (i == 0), this.deckTier);
    }
}