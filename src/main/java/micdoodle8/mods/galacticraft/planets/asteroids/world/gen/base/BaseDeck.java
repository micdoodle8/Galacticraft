package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
    static final int ROOMSMALL = 6;
    static final int ROOMLARGE = 8;
    protected int roomsOnSide;
    protected int roomDepth;
    protected int largeRoomPosA;
    protected int largeRoomPosB;
    protected int deckTier; // flag 1 means lowest deck in a stack, flag 2 is for highest deck, 0 is for middle decks - flag 4 is for control room
    private ArrayList<BaseDeck> otherDecks = null;
    
    public enum EnumBaseType 
    {
        HUMANOID(5, 3, GCBlocks.basicBlock.getStateFromMeta(4), new ItemStack(Items.GHAST_TEAR, 3, 0)),
        AVIAN(4, 3, GCBlocks.blockMoon.getStateFromMeta(4), new ItemStack(AsteroidsItems.strangeSeed, 1, 1)),
        TUNNELER(4, 4, GCBlocks.blockMoon.getStateFromMeta(4), new ItemStack(AsteroidsItems.strangeSeed, 1, 0));
        
        public final int height;
        public final int width;
        public final IBlockState wall;
        public final ItemStack treasure;
        
        EnumBaseType(int height, int width, IBlockState wallBlock, ItemStack treasure)
        {
            this.height = height;
            this.width = width;
            this.wall = wallBlock;
            this.treasure = treasure;
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
            this.roomDepth -= 2;
            if (this.getDirection().getAxis() == EnumFacing.Axis.X)
            {
                this.sizeZ += 4;
            }
            else
            {
                this.sizeX += 4;
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
            this.otherDecks.add(new BaseDeck(configuration, rand, blockPosX, blockPosY + this.sizeY, blockPosZ, 0, this.direction));
            this.otherDecks.add(new BaseDeck(configuration, rand, blockPosX, blockPosY + this.sizeY + this.sizeY, blockPosZ, 2, this.direction));
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
    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox chunkBounds)
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
        int endY = this.sizeY;
        if (this.configuration.getDeckType() == EnumBaseType.AVIAN && !this.configuration.isHangarDeck())
            endY--;

        //This is the central corridor of every base
        for (int x = startX; x <= endX; x++)
        {
            for (int z = startZ; z <= endZ; z++)
            {
                for (int y = 0; y <= endY; y++)
                {
                    if (x == startX || x == endX || y == 0 || y == endY || z == startZ || z == endZ)
                    {
                        boolean skipCap = false;
                        if (this.configuration.isHangarDeck() && this.deckTier == 1 && (this.getDirection() == EnumFacing.NORTH && z == endZ || this.getDirection() == EnumFacing.SOUTH && z == startZ || this.getDirection() == EnumFacing.WEST && x == endX || this.getDirection() == EnumFacing.EAST && x == startX))
                        {
                            //Make airlock at the hangar end, if it's a hangar deck
                            IBlockState blockLintel = GCBlocks.airLockFrame.getDefaultState();
                            IBlockState blockAirlock = GCBlocks.airLockFrame.getStateFromMeta(1);
                            if ((!directionNS && (z == startZ + 1 || z == endZ - 1) || directionNS && (x == startX + 1 || x == endX - 1)) && y < sizeY - 1)
                            {
                                this.setBlockState(worldIn, blockLintel, x, y, z, chunkBounds);
                            }
                            else if (y >= endY - 1 || directionNS && (x == startX || x == endX) || !directionNS && (z == startZ || z == endZ))
                            {
                                this.setBlockState(worldIn, blockWall, x, y, z, chunkBounds);
                            }
                            else if (y == 0 || y == endY - 2)
                            {
                                //Test mid point bottom
                                if (y == 0 && (!directionNS && z == (startZ + endZ) / 2 || directionNS && x == (startX + endX) / 2))
                                    this.setBlockState(worldIn, blockAirlock, x, y, z, chunkBounds);
                                else
                                    this.setBlockState(worldIn, blockLintel, x, y, z, chunkBounds);
                                skipCap = true;
                            }
                            else
                            {
                                this.setBlockState(worldIn, blockAir, x, y, z, chunkBounds);
                            }
                        }
                        //Skip certain blocks at top and bottom to give chamfered edge
                        else if (!((y == 0 && (this.deckTier & 1) == 1 || y == endY && (this.deckTier & 1) == 2) && (directionNS && (z == startZ || z == endZ) || !directionNS && (x == startX || x == endX))))
                        {
                            if (y == 0 && (this.deckTier & 1) == 1 && this.configuration.getDeckType() == EnumBaseType.HUMANOID && (directionNS && (x == startX + 2 + (this.configuration.isHangarDeck() ? 1 : 0) || x == startX + 4 + (this.configuration.isHangarDeck() ? 1 : 0)) || !directionNS && (z == startZ + 2 + (this.configuration.isHangarDeck() ? 1 : 0) || z == startZ + 4 + (this.configuration.isHangarDeck() ? 1 : 0))))
                            {
                                this.setBlockState(worldIn, blockGrid, x, y, z, chunkBounds);
                            }
                            else if (!this.configuration.isHangarDeck() && (this.getDirection() == EnumFacing.SOUTH && z == endZ || this.getDirection() == EnumFacing.NORTH && z == startZ || this.getDirection() == EnumFacing.EAST && x == endX || this.getDirection() == EnumFacing.WEST && x == startX))
                            {
                                //Some end windows at the ends of the corridors
                                IBlockState windowOrWall = blockWall;
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

                                int w, startW, endW;
                                if (directionNS)
                                {
                                    w = x;
                                    startW = startX;
                                    endW = endX;
                                }
                                else
                                {
                                    w = z;
                                    startW = startZ;
                                    endW = endZ;
                                }

                                if (this.configuration.getDeckType() == EnumBaseType.AVIAN)
                                {
                                    if (y == 2 && w == startW + 2)
                                    {
                                        windowOrWall = blockGlass;
                                    }
                                }
                                else
                                {
                                    int edge = this.configuration.getDeckType() == EnumBaseType.TUNNELER ? 1 : 0;
                                    if ((y == 2 || y == 3) && w > startW + edge && w < endW - edge)
                                    {
                                        windowOrWall = blockGlass;
                                    }
                                }

                                this.setBlockState(worldIn, windowOrWall, x, y, z, chunkBounds);
                            }
                            else
                            {
                                this.setBlockState(worldIn, blockWall, x, y, z, chunkBounds);
                            }
                        }
                        
                        //Special case: cap the grid ends
                        int hangarOffset = (this.configuration.isHangarDeck() ? 1 : 0);
                        if (y == 0 && (this.deckTier & 1) == 1 && this.configuration.getDeckType() == EnumBaseType.HUMANOID && (directionNS && (x >= startX + 2 + hangarOffset && x <= startX + 4 + hangarOffset) || !directionNS && (z >= startZ + 2 + hangarOffset && z <= startZ + 4 + hangarOffset)))
                        {
                            if (directionNS && (z == startZ || z == endZ) || !directionNS && (x == startX || x == endX))
                            {
                                if (!skipCap)
                                {
                                    this.setBlockState(worldIn, blockWall, x, y, z, chunkBounds);
                                }
                            }
                        }
                        
                    }
                    else if (this.configuration.getDeckType().ordinal() >= EnumBaseType.AVIAN.ordinal() && (y == 1 || y == endY - 1))
                    {
                        //Internal decoration - deck corridor corners
                        int top = (y == 1) ? 0 : 4;
                        if (this.configuration.getDeckType() == EnumBaseType.TUNNELER)
                            top ++;
                        
                        if (directionNS)
                        {
                            if (x == startX + 1)
                            {
                                this.setBlockState(worldIn, blockStair.getStateFromMeta(0 ^ top), x, y, z, chunkBounds);
                            }
                            else if (x == endX - 1)
                            {
                                this.setBlockState(worldIn, blockStair.getStateFromMeta(1 ^ top), x, y, z, chunkBounds);
                            }
                            else if (this.configuration.getDeckType() == EnumBaseType.AVIAN)
                            {
                                if (z == ceilingDeco && top >= 4)
                                {
                                    this.setBlockState(worldIn, blockStair.getStateFromMeta(3 ^ top), x, y, z, chunkBounds);
                                }
                                else if (z == ceilingDeco + 1 && top >= 4)
                                {
                                    this.setBlockState(worldIn, blockStair.getStateFromMeta(2 ^ top), x, y, z, chunkBounds);
                                    if (x >= endX - 2) ceilingDeco += ceilingSpacer;
                                }
                                else
                                {
                                    this.setBlockState(worldIn, blockAir, x, y, z, chunkBounds);
                                }
                            }
                            else
                            {
                                this.setBlockState(worldIn, blockAir, x, y, z, chunkBounds);
                            }
                        }
                        else
                        {
                            if (z == startZ + 1)
                            {
                                this.setBlockState(worldIn, blockStair.getStateFromMeta(2 ^ top), x, y, z, chunkBounds);
                            }
                            else if (z == endZ - 1)
                            {
                                this.setBlockState(worldIn, blockStair.getStateFromMeta(3 ^ top), x, y, z, chunkBounds);
                            }
                            else if (this.configuration.getDeckType() == EnumBaseType.AVIAN)
                            {
                                if (x == ceilingDeco && top == 4)
                                {
                                    this.setBlockState(worldIn, blockStair.getStateFromMeta(1 ^ top), x, y, z, chunkBounds);
                                }
                                else if (x == ceilingDeco + 1 && top == 4)
                                {
                                    this.setBlockState(worldIn, blockStair.getStateFromMeta(top), x, y, z, chunkBounds);
                                    if (z >= endZ - 2) ceilingDeco += ceilingSpacer;
                                }
                                else
                                {
                                    this.setBlockState(worldIn, blockAir, x, y, z, chunkBounds);
                                }
                            }
                            else
                            {
                                this.setBlockState(worldIn, blockAir, x, y, z, chunkBounds);
                            }
                        }
                    }
                    else
                    {
                        this.setBlockState(worldIn, blockAir, x, y, z, chunkBounds);
                    }
                }
                
                //Infill for the extra height layer
                if (endY != this.sizeY)
                {
                    this.setBlockState(worldIn, blockWall, x, this.sizeY, z, chunkBounds);
                }
            }
        }

        int leftX = ((this.deckTier & 4) == 4 && (this.direction == EnumFacing.SOUTH || this.direction == EnumFacing.EAST)) ? 3 : 2;
        int rightX = leftX;
        int leftZ = leftX;
        int rightZ = rightX;
        for (int i = 0; i < this.roomsOnSide; i++)
        {
            boolean largeRoom = (i == largeRoomPosA || i == largeRoomPosB);
            int roomsize = largeRoom ? ROOMLARGE : ROOMSMALL;
            int largeRoomOffset = largeRoom ? 2 : 1;

            if (directionNS)
            {
                this.makeDoorway(worldIn, this.sizeX, leftZ + largeRoomOffset, directionNS, chunkBounds);
                this.makeDoorway(worldIn, 0, rightZ + largeRoomOffset, directionNS, chunkBounds);

                leftZ += roomsize;
                rightZ += roomsize;
            }
            else
            {
                this.makeDoorway(worldIn, leftX + largeRoomOffset, this.sizeZ, directionNS, chunkBounds);
                this.makeDoorway(worldIn, rightX + largeRoomOffset, 0, directionNS, chunkBounds);

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
            this.setBlockState(worldIn, blockAir, 0, this.sizeY, 1, chunkBounds);
            this.setBlockState(worldIn, blockAir, 1, this.sizeY, 1, chunkBounds);
            this.setBlockState(worldIn, blockAir, 1, this.sizeY - 1, 1, chunkBounds);
            this.setBlockState(worldIn, blockAir, width - 1, this.sizeY - 1, 1, chunkBounds);
            this.setBlockState(worldIn, blockAir, width - 1, this.sizeY, 1, chunkBounds);
            this.setBlockState(worldIn, blockAir, width, this.sizeY, 1, chunkBounds);
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
                for (int x = 2; x < endX - 1; x++)
                {
                    this.setBlockState(worldIn, blockGlass, x, y, endZ, chunkBounds);
                }
            }
            
            //Create two levers
            int facing = 0; 
            switch (this.direction)
            {
            case NORTH:
                break;
            case SOUTH:
                facing = 2;
                break;
            case EAST:
                facing = 1;
                break;
            case WEST:
                facing = 3;
            }

            IBlockState lever = GCBlocks.concealedDetector.getStateFromMeta(8 + facing + (this.configuration.getDeckType() == EnumBaseType.HUMANOID ? 0 : 4));
            this.setBlockState(worldIn, lever, endX / 2 - 2, this.sizeY - 1, endZ, chunkBounds);
            this.setBlockState(worldIn, lever, endX / 2 + 2, this.sizeY - 1, endZ, chunkBounds);
            lever = Blocks.LEVER.getStateFromMeta(this.direction.getAxis() == EnumFacing.Axis.Z ? 4 : 3);
            this.setBlockState(worldIn, lever, endX / 2 - 2, this.sizeY - 1, endZ - 1, chunkBounds);
            this.setBlockState(worldIn, lever, endX / 2 + 2, this.sizeY - 1, endZ - 1, chunkBounds);
            
            //Create two redstone blocks
            this.setBlockState(worldIn, GCBlocks.concealedRedstone.getStateFromMeta(15), endX / 2 - 2, this.sizeY, endZ, chunkBounds);
            this.setBlockState(worldIn, GCBlocks.concealedRedstone.getStateFromMeta(15), endX / 2 + 2, this.sizeY, endZ, chunkBounds);

            //Create an access hole between the floors
            this.setBlockState(worldIn, blockAir, 2, 0, 1, chunkBounds);
            this.setBlockState(worldIn, blockAir, 3, 0, 1, chunkBounds);
            this.setBlockState(worldIn, blockAir, 11, 0, 1, chunkBounds);
            this.setBlockState(worldIn, blockAir, 12, 0, 1, chunkBounds);
        }
        this.setCoordBaseMode(EnumFacing.NORTH);

        return true;
    }
    
    public List<Piece> getRooms(int roomIndex, BaseStart startPiece, Random rand)
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
            rooms.add(this.getRoom(roomIndex++, left, leftX, leftZ, largeRoom, true, rand));
            rooms.add(this.getRoom(roomIndex++, right, rightX, rightZ, largeRoom, false, rand));
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
                List<Piece> newRooms = deck.getRooms(roomIndex, startPiece, rand); 
                rooms.addAll(newRooms);
                roomIndex += newRooms.size();
            }
        }
        
        return rooms;
    }
    
    protected void makeDoorway(World worldIn, int x, int z, boolean directionNS, StructureBoundingBox chunkBounds)
    {
//        System.out.println("Making doorway at " + x + "," + z + " NS:" + directionNS + " Tier " + this.deckTier);
        IBlockState blockLintel = GCBlocks.airLockFrame.getDefaultState();
        IBlockState blockAirlock = GCBlocks.airLockFrame.getStateFromMeta(1);
        Block blockStair = GCBlocks.moonStoneStairs;
        IBlockState blockAir = Blocks.AIR.getDefaultState();
        int meta = directionNS ? 2 : 0;
        
        switch (this.configuration.getDeckType())
        {
        case HUMANOID:
            if (directionNS) z--; else x--;
            this.setBlockState(worldIn, blockLintel, x, 1, z, chunkBounds);
            this.setBlockState(worldIn, blockLintel, x, 2, z, chunkBounds);
            this.setBlockState(worldIn, blockLintel, x, 3, z, chunkBounds);
            if (directionNS) z++; else x++;
            this.setBlockState(worldIn, blockAir, x, 1, z, chunkBounds);
            this.setBlockState(worldIn, blockAir, x, 2, z, chunkBounds);
            this.setBlockState(worldIn, blockAirlock, x, 0, z, chunkBounds);
            this.setBlockState(worldIn, blockLintel, x, 3, z, chunkBounds);
            if (directionNS) z++; else x++;
            this.setBlockState(worldIn, blockLintel, x, 1, z, chunkBounds);
            this.setBlockState(worldIn, blockLintel, x, 2, z, chunkBounds);
            this.setBlockState(worldIn, blockLintel, x, 3, z, chunkBounds);
            break;
        case AVIAN:
            this.setBlockState(worldIn, blockStair.getStateFromMeta(0 + meta), x, this.sizeY - 4, z, chunkBounds);
            this.setBlockState(worldIn, blockAir, x, this.sizeY - 3, z, chunkBounds);
            this.setBlockState(worldIn, blockStair.getStateFromMeta(4 + meta), x, this.sizeY - 2, z, chunkBounds);
            if (directionNS) z++; else x++;
            this.setBlockState(worldIn, blockStair.getStateFromMeta(1 + meta), x, this.sizeY - 4, z, chunkBounds);
            this.setBlockState(worldIn, blockAir, x, this.sizeY - 3, z, chunkBounds);
            this.setBlockState(worldIn, blockStair.getStateFromMeta(5 + meta), x, this.sizeY - 2, z, chunkBounds);
            break;
        case TUNNELER:
            if (directionNS) z--; else x--;
            this.setBlockState(worldIn, blockStair.getStateFromMeta(1 + meta), x, 2, z, chunkBounds);
            this.setBlockState(worldIn, blockStair.getStateFromMeta(5 + meta), x, 3, z, chunkBounds);
            if (directionNS) z++; else x++;
            this.setBlockState(worldIn, blockAir, x, 2, z, chunkBounds);
            this.setBlockState(worldIn, blockAir, x, 3, z, chunkBounds);
            if (directionNS) z++; else x++;
            this.setBlockState(worldIn, blockStair.getStateFromMeta(0 + meta), x, 2, z, chunkBounds);
            this.setBlockState(worldIn, blockStair.getStateFromMeta(4 + meta), x, 3, z, chunkBounds);
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
        return new BaseRoom(this.configuration, rand, blockX, this.boundingBox.minY, blockZ, sX, sY, sZ, dir, this.configuration.getRandomRoom(i), left ? (i == 0) : (i == this.roomsOnSide - 1), left ? (i == this.roomsOnSide - 1) : (i == 0), this.deckTier);
    }
}