package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockConcealedDetector;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.AsteroidFeatures;
import net.minecraft.block.*;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import static micdoodle8.mods.galacticraft.planets.asteroids.world.gen.AsteroidFeatures.CBASE_DECK;
import static micdoodle8.mods.galacticraft.planets.asteroids.world.gen.AsteroidFeatures.CBASE_START;

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
        HUMANOID(5, 3, GCBlocks.decoBlock1.getDefaultState(), new ItemStack(Items.GHAST_TEAR, 3)),
        AVIAN(4, 3, GCBlocks.decoBlock1.getDefaultState(), new ItemStack(AsteroidsItems.strangeSeed2, 1)),
        TUNNELER(4, 4, GCBlocks.decoBlock1.getDefaultState(), new ItemStack(AsteroidsItems.strangeSeed1, 1));
        
        public final int height;
        public final int width;
        public final BlockState wall;
        public final ItemStack treasure;
        
        EnumBaseType(int height, int width, BlockState wallBlock, ItemStack treasure)
        {
            this.height = height;
            this.width = width;
            this.wall = wallBlock;
            this.treasure = treasure;
        }
    }

    public BaseDeck(TemplateManager templateManager, CompoundNBT nbt)
    {
        super(CBASE_DECK, nbt);
    }
    
    public BaseDeck(IStructurePieceType type, CompoundNBT nbt)
    {
        super(type, nbt);
    }

    public BaseDeck(BaseConfiguration configuration, Random rand, int blockPosX, int blockPosY, int blockPosZ, int tier, Direction dir)
    {
        this(CBASE_DECK, configuration, rand, blockPosX, blockPosY, blockPosZ, tier, dir);
    }

    public BaseDeck(IStructurePieceType type, BaseConfiguration configuration, Random rand, int blockPosX, int blockPosY, int blockPosZ, int tier, Direction dir)
    {
        super(type, configuration, configuration.getCorridorWidth() + 1, configuration.getRoomHeight() + 1, configuration.getCorridorLength(), dir);
        this.roomsOnSide = configuration.getRoomsNo();
        this.roomDepth = configuration.getRoomDepth() + (tier == 0 ? 1 : 0);
        this.deckTier = tier;
        this.setCoordBaseMode(Direction.NORTH);
        if (this.getDirection().getAxis() == Direction.Axis.X)
        {
            int w = this.sizeX;
            this.sizeX = this.sizeZ;
            this.sizeZ = w;
        }
        int xMin = blockPosX - this.sizeX / 2;
        int zMin = blockPosZ - this.sizeZ / 2;
        this.boundingBox = new MutableBoundingBox(xMin, blockPosY, zMin, xMin + this.sizeX, blockPosY + this.sizeY, zMin + this.sizeZ);
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
            if (this.getDirection().getAxis() == Direction.Axis.X)
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
            this.boundingBox = new MutableBoundingBox(xMin, blockPosY, zMin, xMin + this.sizeX, blockPosY + this.sizeY, zMin + this.sizeZ);
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
    protected void writeStructureToNBT(CompoundNBT tagCompound)
    {
        super.writeStructureToNBT(tagCompound);

        int details = this.deckTier + (this.largeRoomPosA << 4) + (this.largeRoomPosB << 8);
        tagCompound.putInt("dD", details);
        if (this.otherDecks != null)
        {
            ListNBT tagList = new ListNBT();
            for (BaseDeck deck : this.otherDecks)
            {
                CompoundNBT tag1 = new CompoundNBT();
                deck.writeStructureToNBT(tag1);
                tagList.add(tag1);
            }
            tagCompound.put("oD", tagList);
        }
    }

    @Override
    protected void readStructureFromNBT(CompoundNBT tagCompound)
    {
        super.readStructureFromNBT(tagCompound);
        try
        {
            int details = tagCompound.getInt("dD");
            this.deckTier = details & 15;
            this.largeRoomPosB = details >> 8;
            this.largeRoomPosA = (details >> 4) & 15;
            this.roomsOnSide = configuration.getRoomsNo();
            this.roomDepth = configuration.getRoomDepth() + (this.deckTier == 0 ? 1 : 0) - ((this.deckTier & 4) == 4 ? 3 : 0);
            
            if (tagCompound.contains("oD"))
            {
                ListNBT tagList = tagCompound.getList("oD", 10);
                this.otherDecks = new ArrayList<BaseDeck>();
                for (int i = 0; i < tagList.size(); i++)
                {
                    CompoundNBT tagAt = tagList.getCompound(i);
                    BaseDeck deck = new BaseDeck(AsteroidFeatures.CBASE_DECK, tagAt);
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
    public boolean addComponentParts(IWorld worldIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos p_74875_4_)
    {
        BlockState block1;
        BlockState blockWall = this.configuration.getWallBlock();
        BlockState blockGrid = AsteroidBlocks.blockWalkway.getDefaultState();
        Block blockStair = GCBlocks.moonStoneStairs;
        BlockState blockAir = Blocks.AIR.getDefaultState();
        boolean directionNS = this.getDirection().getAxis() == Direction.Axis.Z;
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
                        if (this.configuration.isHangarDeck() && this.deckTier == 1 && (this.getDirection() == Direction.NORTH && z == endZ || this.getDirection() == Direction.SOUTH && z == startZ || this.getDirection() == Direction.WEST && x == endX || this.getDirection() == Direction.EAST && x == startX))
                        {
                            //Make airlock at the hangar end, if it's a hangar deck
                            BlockState blockLintel = GCBlocks.airLockFrame.getDefaultState();
                            BlockState blockAirlock = GCBlocks.airLockController.getDefaultState();
                            if ((!directionNS && (z == startZ + 1 || z == endZ - 1) || directionNS && (x == startX + 1 || x == endX - 1)) && y < sizeY - 1)
                            {
                                this.setBlockState(worldIn, blockLintel, x, y, z, this.boundingBox);
                            }
                            else if (y >= endY - 1 || directionNS && (x == startX || x == endX) || !directionNS && (z == startZ || z == endZ))
                            {
                                this.setBlockState(worldIn, blockWall, x, y, z, this.boundingBox);
                            }
                            else if (y == 0 || y == endY - 2)
                            {
                                //Test mid point bottom
                                if (y == 0 && (!directionNS && z == (startZ + endZ) / 2 || directionNS && x == (startX + endX) / 2))
                                    this.setBlockState(worldIn, blockAirlock, x, y, z, this.boundingBox);
                                else
                                    this.setBlockState(worldIn, blockLintel, x, y, z, this.boundingBox);
                                skipCap = true;
                            }
                            else
                            {
                                this.setBlockState(worldIn, blockAir, x, y, z, this.boundingBox);
                            }
                        }
                        //Skip certain blocks at top and bottom to give chamfered edge
                        else if (!((y == 0 && (this.deckTier & 1) == 1 || y == endY && (this.deckTier & 1) == 2) && (directionNS && (z == startZ || z == endZ) || !directionNS && (x == startX || x == endX))))
                        {
                            if (y == 0 && (this.deckTier & 1) == 1 && this.configuration.getDeckType() == EnumBaseType.HUMANOID && (directionNS && (x == startX + 2 + (this.configuration.isHangarDeck() ? 1 : 0) || x == startX + 4 + (this.configuration.isHangarDeck() ? 1 : 0)) || !directionNS && (z == startZ + 2 + (this.configuration.isHangarDeck() ? 1 : 0) || z == startZ + 4 + (this.configuration.isHangarDeck() ? 1 : 0))))
                            {
                                this.setBlockState(worldIn, blockGrid, x, y, z, this.boundingBox);
                            }
                            else if (!this.configuration.isHangarDeck() && (this.getDirection() == Direction.SOUTH && z == endZ || this.getDirection() == Direction.NORTH && z == startZ || this.getDirection() == Direction.EAST && x == endX || this.getDirection() == Direction.WEST && x == startX))
                            {
                                //Some end windows at the ends of the corridors
                                BlockState windowOrWall = blockWall;
                                BlockState blockGlass;
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

                                this.setBlockState(worldIn, windowOrWall, x, y, z, this.boundingBox);
                            }
                            else
                            {
                                this.setBlockState(worldIn, blockWall, x, y, z, this.boundingBox);
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
                                    this.setBlockState(worldIn, blockWall, x, y, z, this.boundingBox);
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
                                int val = 0 ^ top;
                                BlockState state = blockStair.getDefaultState();
                                state.with(StairsBlock.HALF, (val & 4) > 0 ? Half.TOP : Half.BOTTOM);
                                state.with(StairsBlock.FACING, Direction.byIndex(5 - (val & 3)));
                                this.setBlockState(worldIn, state, x, y, z, this.boundingBox);
                            }
                            else if (x == endX - 1)
                            {
                                int val = 1 ^ top;
                                BlockState state = blockStair.getDefaultState();
                                state.with(StairsBlock.HALF, (val & 4) > 0 ? Half.TOP : Half.BOTTOM);
                                state.with(StairsBlock.FACING, Direction.byIndex(5 - (val & 3)));
                                this.setBlockState(worldIn, state, x, y, z, this.boundingBox);
                            }
                            else if (this.configuration.getDeckType() == EnumBaseType.AVIAN)
                            {
                                if (z == ceilingDeco && top >= 4)
                                {
                                    BlockState state = blockStair.getDefaultState();
                                    state.with(StairsBlock.HALF, (top & 4) > 0 ? Half.TOP : Half.BOTTOM);
                                    state.with(StairsBlock.FACING, Direction.byIndex(5 - (top & 3)));
                                    this.setBlockState(worldIn, state, x, y, z, this.boundingBox);
                                }
                                else if (z == ceilingDeco + 1 && top >= 4)
                                {
                                    int val = 2 ^ top;
                                    BlockState state = blockStair.getDefaultState();
                                    state.with(StairsBlock.HALF, (val & 4) > 0 ? Half.TOP : Half.BOTTOM);
                                    state.with(StairsBlock.FACING, Direction.byIndex(5 - (val & 3)));
                                    this.setBlockState(worldIn, state, x, y, z, this.boundingBox);
                                    if (x >= endX - 2) ceilingDeco += ceilingSpacer;
                                }
                                else
                                {
                                    this.setBlockState(worldIn, blockAir, x, y, z, this.boundingBox);
                                }
                            }
                            else
                            {
                                this.setBlockState(worldIn, blockAir, x, y, z, this.boundingBox);
                            }
                        }
                        else
                        {
                            if (z == startZ + 1)
                            {
                                int val = 2 ^ top;
                                BlockState state = blockStair.getDefaultState();
                                state.with(StairsBlock.HALF, (val & 4) > 0 ? Half.TOP : Half.BOTTOM);
                                state.with(StairsBlock.FACING, Direction.byIndex(5 - (val & 3)));
                                this.setBlockState(worldIn, state, x, y, z, this.boundingBox);
                            }
                            else if (z == endZ - 1)
                            {
                                int val = 3 ^ top;
                                BlockState state = blockStair.getDefaultState();
                                state.with(StairsBlock.HALF, (val & 4) > 0 ? Half.TOP : Half.BOTTOM);
                                state.with(StairsBlock.FACING, Direction.byIndex(5 - (val & 3)));
                                this.setBlockState(worldIn, state, x, y, z, this.boundingBox);
                            }
                            else if (this.configuration.getDeckType() == EnumBaseType.AVIAN)
                            {
                                if (x == ceilingDeco && top == 4)
                                {
                                    int val = 1 ^ top;
                                    BlockState state = blockStair.getDefaultState();
                                    state.with(StairsBlock.HALF, (val & 4) > 0 ? Half.TOP : Half.BOTTOM);
                                    state.with(StairsBlock.FACING, Direction.byIndex(5 - (val & 3)));
                                    this.setBlockState(worldIn, state, x, y, z, this.boundingBox);
                                }
                                else if (x == ceilingDeco + 1 && top == 4)
                                {
                                    BlockState state = blockStair.getDefaultState();
                                    state.with(StairsBlock.HALF, (top & 4) > 0 ? Half.TOP : Half.BOTTOM);
                                    state.with(StairsBlock.FACING, Direction.byIndex(5 - (top & 3)));
                                    this.setBlockState(worldIn, state, x, y, z, this.boundingBox);
                                    if (z >= endZ - 2) ceilingDeco += ceilingSpacer;
                                }
                                else
                                {
                                    this.setBlockState(worldIn, blockAir, x, y, z, this.boundingBox);
                                }
                            }
                            else
                            {
                                this.setBlockState(worldIn, blockAir, x, y, z, this.boundingBox);
                            }
                        }
                    }
                    else
                    {
                        this.setBlockState(worldIn, blockAir, x, y, z, this.boundingBox);
                    }
                }
                
                //Infill for the extra height layer
                if (endY != this.sizeY)
                {
                    this.setBlockState(worldIn, blockWall, x, this.sizeY, z, this.boundingBox);
                }
            }
        }

        int leftX = ((this.deckTier & 4) == 4 && (this.direction == Direction.SOUTH || this.direction == Direction.EAST)) ? 3 : 2;
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
                this.makeDoorway(worldIn, this.sizeX, leftZ + largeRoomOffset, directionNS, this.boundingBox);
                this.makeDoorway(worldIn, 0, rightZ + largeRoomOffset, directionNS, this.boundingBox);

                leftZ += roomsize;
                rightZ += roomsize;
            }
            else
            {
                this.makeDoorway(worldIn, leftX + largeRoomOffset, this.sizeZ, directionNS, this.boundingBox);
                this.makeDoorway(worldIn, rightX + largeRoomOffset, 0, directionNS, this.boundingBox);

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
            this.setBlockState(worldIn, blockAir, 0, this.sizeY, 1, this.boundingBox);
            this.setBlockState(worldIn, blockAir, 1, this.sizeY, 1, this.boundingBox);
            this.setBlockState(worldIn, blockAir, 1, this.sizeY - 1, 1, this.boundingBox);
            this.setBlockState(worldIn, blockAir, width - 1, this.sizeY - 1, 1, this.boundingBox);
            this.setBlockState(worldIn, blockAir, width - 1, this.sizeY, 1, this.boundingBox);
            this.setBlockState(worldIn, blockAir, width, this.sizeY, 1, this.boundingBox);
        }

        //Special settings for Control Room
        if ((this.deckTier & 4) == 4)
        {
            BlockState blockGlass;
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
                    this.setBlockState(worldIn, blockGlass, x, y, endZ, this.boundingBox);
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

//            BlockState lever = GCBlocks.concealedDetector.getStateFromMeta(8 + facing + (this.configuration.getDeckType() == EnumBaseType.HUMANOID ? 0 : 4));
            BlockState lever = GCBlocks.concealedDetector.getDefaultState().with(BlockConcealedDetector.DETECTED, true).with(BlockConcealedDetector.FACING, facing).with(BlockConcealedDetector.VARIANT, this.configuration.getDeckType() == EnumBaseType.HUMANOID ? 0 : 4);
            this.setBlockState(worldIn, lever, endX / 2 - 2, this.sizeY - 1, endZ, this.boundingBox);
            this.setBlockState(worldIn, lever, endX / 2 + 2, this.sizeY - 1, endZ, this.boundingBox);
            lever = Blocks.LEVER.getDefaultState().with(LeverBlock.FACE, AttachFace.WALL).with(LeverBlock.HORIZONTAL_FACING, this.direction.getAxis() == Direction.Axis.Z ? Direction.NORTH : Direction.SOUTH);
            this.setBlockState(worldIn, lever, endX / 2 - 2, this.sizeY - 1, endZ - 1, this.boundingBox);
            this.setBlockState(worldIn, lever, endX / 2 + 2, this.sizeY - 1, endZ - 1, this.boundingBox);
            
            //Create two redstone blocks
            this.setBlockState(worldIn, GCBlocks.concealedRedstone.getDefaultState().with(StairsBlock.HALF, Half.TOP).with(StairsBlock.FACING, Direction.NORTH), endX / 2 - 2, this.sizeY, endZ, this.boundingBox);
            this.setBlockState(worldIn, GCBlocks.concealedRedstone.getDefaultState().with(StairsBlock.HALF, Half.TOP).with(StairsBlock.FACING, Direction.NORTH), endX / 2 + 2, this.sizeY, endZ, this.boundingBox);

            //Create an access hole between the floors
            this.setBlockState(worldIn, blockAir, 2, 0, 1, this.boundingBox);
            this.setBlockState(worldIn, blockAir, 3, 0, 1, this.boundingBox);
            this.setBlockState(worldIn, blockAir, 11, 0, 1, this.boundingBox);
            this.setBlockState(worldIn, blockAir, 12, 0, 1, this.boundingBox);
        }
        this.setCoordBaseMode(Direction.NORTH);

        return true;
    }
    
    public List<Piece> getRooms(int roomIndex, BaseStart startPiece, Random rand)
    {
        List<Piece> rooms = new ArrayList<Piece>();
        
        boolean directionNS = this.getDirection().getAxis() == Direction.Axis.Z;
        Direction left = directionNS ? Direction.WEST : Direction.SOUTH;
        Direction right = directionNS ? Direction.EAST : Direction.NORTH;
        int leftX = directionNS ? this.boundingBox.maxX + 1: this.boundingBox.minX;
        int leftZ = directionNS ? this.boundingBox.minZ : this.boundingBox.maxZ + 1;
        int rightX = directionNS ? this.boundingBox.minX - this.roomDepth - 1: this.boundingBox.minX;
        int rightZ = directionNS ? this.boundingBox.minZ : this.boundingBox.minZ - this.roomDepth - 1;
        //now offset effect of moving control room 1 position North or West, in two cases
        if ((this.deckTier & 4) == 4 && this.direction == Direction.SOUTH)
        {
            rightZ++;
            leftZ++;
        }
        if ((this.deckTier & 4) == 4 && this.direction == Direction.EAST)
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
    
    protected void makeDoorway(IWorld worldIn, int x, int z, boolean directionNS, MutableBoundingBox chunkBounds)
    {
//        System.out.println("Making doorway at " + x + "," + z + " NS:" + directionNS + " Tier " + this.deckTier);
        BlockState blockLintel = GCBlocks.airLockFrame.getDefaultState();
        BlockState blockAirlock = GCBlocks.airLockController.getDefaultState();
        Block blockStair = GCBlocks.moonStoneStairs;
        BlockState blockAir = Blocks.AIR.getDefaultState();
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
            this.setBlockState(worldIn, blockStair.getDefaultState().with(StairsBlock.HALF, (meta & 4) > 0 ? Half.TOP : Half.BOTTOM).with(StairsBlock.FACING, Direction.byIndex(5 - (meta & 3))), x, this.sizeY - 4, z, chunkBounds);
            this.setBlockState(worldIn, blockAir, x, this.sizeY - 3, z, chunkBounds);
            this.setBlockState(worldIn, blockStair.getDefaultState().with(StairsBlock.HALF, ((4 + meta) & 4) > 0 ? Half.TOP : Half.BOTTOM).with(StairsBlock.FACING, Direction.byIndex(5 - ((4 + meta) & 3))), x, this.sizeY - 2, z, chunkBounds);
            if (directionNS) z++; else x++;
            this.setBlockState(worldIn, blockStair.getDefaultState().with(StairsBlock.HALF, ((1 + meta) & 4) > 0 ? Half.TOP : Half.BOTTOM).with(StairsBlock.FACING, Direction.byIndex(5 - ((1 + meta) & 3))), x, this.sizeY - 4, z, chunkBounds);
            this.setBlockState(worldIn, blockAir, x, this.sizeY - 3, z, chunkBounds);
            this.setBlockState(worldIn, blockStair.getDefaultState().with(StairsBlock.HALF, ((5 + meta) & 4) > 0 ? Half.TOP : Half.BOTTOM).with(StairsBlock.FACING, Direction.byIndex(5 - ((5 + meta) & 3))), x, this.sizeY - 2, z, chunkBounds);
            break;
        case TUNNELER:
            if (directionNS) z--; else x--;
            this.setBlockState(worldIn, blockStair.getDefaultState().with(StairsBlock.HALF, ((1 + meta) & 4) > 0 ? Half.TOP : Half.BOTTOM).with(StairsBlock.FACING, Direction.byIndex(5 - ((1 + meta) & 3))), x, 2, z, chunkBounds);
            this.setBlockState(worldIn, blockStair.getDefaultState().with(StairsBlock.HALF, ((5 + meta) & 4) > 0 ? Half.TOP : Half.BOTTOM).with(StairsBlock.FACING, Direction.byIndex(5 - ((5 + meta) & 3))), x, 3, z, chunkBounds);
            if (directionNS) z++; else x++;
            this.setBlockState(worldIn, blockAir, x, 2, z, chunkBounds);
            this.setBlockState(worldIn, blockAir, x, 3, z, chunkBounds);
            if (directionNS) z++; else x++;
            this.setBlockState(worldIn, blockStair.getDefaultState().with(StairsBlock.HALF, ((meta) & 4) > 0 ? Half.TOP : Half.BOTTOM).with(StairsBlock.FACING, Direction.byIndex(5 - ((meta) & 3))), x, 2, z, chunkBounds);
            this.setBlockState(worldIn, blockStair.getDefaultState().with(StairsBlock.HALF, ((4 + meta) & 4) > 0 ? Half.TOP : Half.BOTTOM).with(StairsBlock.FACING, Direction.byIndex(5 - ((4 + meta) & 3))), x, 3, z, chunkBounds);
            break;
        default:
        }
            
    }

    protected Piece getRoom(int i, Direction dir, int blockX, int blockZ, boolean large, boolean left, Random rand)
    {
        int sX = large ? ROOMLARGE : ROOMSMALL;
        int sZ = sX;
        if (dir.getAxis() == Direction.Axis.Z)
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