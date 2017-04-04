package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base.BaseDeck.EnumBaseType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.Random;

public class BaseRoom extends SizedPiece
{
    private EnumRoomType type;
    private boolean nearEnd;
    private boolean farEnd;
    private int deckTier;
    
    public BaseRoom()
    {
    }

    public BaseRoom(BaseConfiguration configuration, Random rand, int blockPosX, int yPos, int blockPosZ, int sizeX, int sizeY, int sizeZ, EnumFacing entranceDir, EnumRoomType roomType, boolean near, boolean far, int deckTier)
    {
        super(configuration, sizeX, sizeY, sizeZ, entranceDir.getOpposite());
        this.setCoordBaseMode(this.direction);
        this.type = roomType;
        this.nearEnd = near;
        this.farEnd = far;
        this.deckTier = deckTier;

        this.boundingBox = new StructureBoundingBox(blockPosX, yPos, blockPosZ, blockPosX + this.sizeX, yPos + this.sizeY, blockPosZ + this.sizeZ);
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound tagCompound)
    {
        super.writeStructureToNBT(tagCompound);

        int details = this.deckTier + (this.nearEnd ? 16 : 0) + (this.farEnd ? 32 : 0);
        tagCompound.setInteger("brT", type.ordinal());
        tagCompound.setInteger("brD", details);
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound)
    {
        super.readStructureFromNBT(tagCompound);
        try
        {
            int typeNo = tagCompound.getInteger("brT");
            if (typeNo < EnumRoomType.values().length)
            {
                this.type = EnumRoomType.values()[typeNo];
            }
            else
                this.type = EnumRoomType.EMPTY;

            int details = tagCompound.getInteger("brD");
            this.deckTier = details & 15;
            this.nearEnd = (details & 16) == 16;
            this.farEnd = (details & 32) == 32;
        }
        catch (Exception e)
        {
            System.err.println("Failed to read Abandoned Base configuration from NBT");
            System.err.println(tagCompound.toString());
        }
    }
    
    @Override
    public boolean addComponentParts(World worldIn, Random random, StructureBoundingBox boundingBox)
    {
        IBlockState blockAir = Blocks.AIR.getDefaultState();
        Block blockStair = GCBlocks.moonStoneStairs;
        
        boolean axisEW = getDirection().getAxis() == EnumFacing.Axis.X;
        int maxX = axisEW ? this.sizeZ : this.sizeX;
        int maxZ = axisEW ? this.sizeX : this.sizeZ;
        for (int xx = 0; xx <= maxX; xx++)
        {
            boolean near = this.nearEnd && xx == maxX;
            boolean far = this.farEnd && xx == 0;
            for (int yy = 0; yy <= this.sizeY; yy++)
            {
                for (int zz = 0; zz <= maxZ; zz++)
                {
                    //5 outer walls of the room
                    if (xx == 0 || xx == maxX || yy == 0 || yy == this.sizeY || zz == maxZ)
                    {
                        //Shave the top and bottom corners
                        if (!((zz == maxZ || near || far) && (yy == 0 && (this.deckTier & 1) == 1 || yy == this.sizeY && (this.deckTier & 2) == 2 || (zz == maxZ && (near || far)))))
                        {
                            this.setBlockState(worldIn, this.configuration.getWallBlock(), xx, yy, zz, boundingBox);
                        }
                        //Special case, fill in some corners on hangardeck top deck
                        else if (yy == this.sizeY && (this.deckTier & 2) == 2 && this.configuration.isHangarDeck() && zz < 3)
                        {
                            if (xx == 0 || xx == maxX)
                            {
                                this.setBlockState(worldIn, this.configuration.getWallBlock(), xx, yy, zz, boundingBox);
                            }
                        }
                    }
                    else
                    {
                        //Room internals
                        if ((xx > 1 && xx < maxX - 1) && (zz > 0 && zz < maxZ - 1) || (yy > 1 && yy < this.sizeY - 1))
                        {
                            this.buildRoomContents(worldIn, xx, yy, zz, maxX, maxZ);
                        }
                        else if (this.configuration.getDeckType() == EnumBaseType.TUNNELER && (yy == 1 || yy == this.sizeY - 1))
                        {
                            int meta = 1;
                            if (xx == 1) meta = 3;
                            else if (xx == maxX - 1) meta = 2;
                            else if (zz == 0) meta = 0;
                            if ((this.direction == EnumFacing.SOUTH) && meta > 1)
                                meta ^= 1;
                            if (this.direction == EnumFacing.SOUTH || this.direction == EnumFacing.NORTH)
                                meta ^= 2;
                            if (this.direction == EnumFacing.EAST) 
                                meta ^= 1;
                            meta += (yy == 1) ? 0 : 4;
                            this.setBlockState(worldIn, blockStair.getStateFromMeta(meta), xx, yy, zz, boundingBox);
                        }
                        else
                        {
                            this.setBlockState(worldIn, blockAir, xx, yy, zz, boundingBox);
                        }
                    }
                }
            }
        }

        return true;
    }
    
    /**
     * Room contents boundaries are:
     *    x from 1 to maxX - 1
     *    y from 1 to this.sizeY - 1
     *    z from 1 to maxZ - 1
     */
    private void buildRoomContents(World worldIn, int x, int y, int z, int maxX, int maxZ)
    {
        IBlockState blockAir = Blocks.AIR.getDefaultState();
        
        //if (this.configuration.getDeckType() != EnumDeckType.TUNNEL)
            
        if ((y == 2 || y == 3) && x == 1)
            this.setBlockState(worldIn, this.type.blockRight, x, y, z, boundingBox);
        else
            this.setBlockState(worldIn, blockAir, x, y, z, boundingBox);
    }

    
    public enum EnumRoomType 
    {
        EMPTY(Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState()),
        STORE(GCBlocks.cargoLoader.getStateFromMeta(3), GCBlocks.cargoLoader.getStateFromMeta(2)),
        MACHINE(GCBlocks.aluminumWire.getStateFromMeta(1), GCBlocks.aluminumWire.getStateFromMeta(0)),
        ENGINEERING(GCBlocks.aluminumWire.getStateFromMeta(1), GCBlocks.aluminumWire.getStateFromMeta(0)),
        SALOON(Blocks.TRAPDOOR.getDefaultState(), GCBlocks.slabGCHalf.getDefaultState()),
        CREW(Blocks.WOOL.getStateFromMeta(8), Blocks.WOOL.getStateFromMeta(0)),
        CONTROL(GCBlocks.screen.getStateFromMeta(3), GCBlocks.screen.getStateFromMeta(2));
        //gym, medical, galley, power, 
                
        public final IBlockState blockLeft;
        public final IBlockState blockRight;
        
        EnumRoomType(IBlockState leftBlock, IBlockState rightBlock)
        {
            this.blockLeft = leftBlock;
            this.blockRight = rightBlock;
        }
    }
}