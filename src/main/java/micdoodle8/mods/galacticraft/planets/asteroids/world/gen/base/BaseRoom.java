package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base.BaseDeck.EnumDeckType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.Random;

public class BaseRoom extends SizedPiece
{
    private EnumRoomType type;
    private boolean nearEnd;
    private boolean farEnd;
    
    public BaseRoom()
    {
    }

    public BaseRoom(BaseConfiguration configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, EnumFacing entranceDir, EnumRoomType roomType, boolean near, boolean far)
    {
        super(configuration, sizeX, sizeY, sizeZ, entranceDir.getOpposite());
        this.coordBaseMode = EnumFacing.SOUTH;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.type = roomType;
        int yPos = configuration.getYPosition();
        this.nearEnd = near;
        this.farEnd = far;

        this.boundingBox = new StructureBoundingBox(blockPosX, yPos, blockPosZ, blockPosX + this.sizeX, yPos + this.sizeY, blockPosZ + this.sizeZ);
        //TODO check save nbt
    }

    @Override
    public boolean addComponentParts(World worldIn, Random random, StructureBoundingBox boundingBox)
    {
        IBlockState blockAir = Blocks.air.getDefaultState();
        Block blockStair = GCBlocks.moonStoneStairs;
        
        boolean axisEW = getDirection().getAxis() == EnumFacing.Axis.X;
        int maxX = axisEW ? this.sizeZ : this.sizeX;
        int maxZ = axisEW ? this.sizeX : this.sizeZ;
        for (int xx = 0; xx <= maxX; xx++)
        {
            for (int yy = 0; yy <= this.sizeY; yy++)
            {
                for (int zz = 0; zz <= maxZ; zz++)
                {
                    //5 outer walls of the room
                    if (xx == 0 || xx == maxX || yy == 0 || yy == this.sizeY || zz == maxZ)
                    {
                        //Shave the top and bottom corners
                        boolean near = this.nearEnd && xx == maxX;
                        boolean far = this.farEnd && xx == 0;
                        if (!((zz == maxZ || near || far) && (yy == 0 || yy == this.sizeY || (zz == maxZ && (near || far)))))
                        {
                            this.setBlockStateDirectional(worldIn, this.configuration.getWallBlock(), xx, yy, zz);
                        }
                    }
                    else
                    {
                        //Room internals
                        if ((xx > 1 && xx < maxX - 1) && (zz > 0 && zz < maxZ - 1) || (yy > 1 && yy < this.sizeY - 1))
                        {
                            this.buildRoomContents(worldIn, xx, yy, zz, maxX, maxZ);
                        }
                        else if (this.configuration.getDeckType() == EnumDeckType.TUNNELER && (yy == 1 || yy == this.sizeY - 1))
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
                            this.setBlockStateDirectional(worldIn, blockStair.getStateFromMeta(meta), xx, yy, zz);
                        }
                        else
                        {
                            this.setBlockStateDirectional(worldIn, blockAir, xx, yy, zz);
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
        IBlockState blockAir = Blocks.air.getDefaultState();
        
        //if (this.configuration.getDeckType() != EnumDeckType.TUNNEL)
            
        if ((y == 2 || y == 3) && x == 1)
            this.setBlockStateDirectional(worldIn, this.type.blockRight, x, y, z);
        else
            this.setBlockStateDirectional(worldIn, blockAir, x, y, z);
    }

    
    public enum EnumRoomType 
    {
        STORE(GCBlocks.cargoLoader.getStateFromMeta(3), GCBlocks.cargoLoader.getStateFromMeta(2)),
        MACHINE(GCBlocks.aluminumWire.getStateFromMeta(1), GCBlocks.aluminumWire.getStateFromMeta(0)),
        SALON(Blocks.trapdoor.getDefaultState(), GCBlocks.slabGCHalf.getDefaultState()),
        CREW(Blocks.wool.getStateFromMeta(8), Blocks.wool.getStateFromMeta(0)),
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