package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockAdvanced;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityFake extends TileEntity
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.fakeBlock)
    public static TileEntityType<TileEntityFake> TYPE;
    //NOTE: No need for networking in 1.8+: see comment in initialiseMultiTiles()
    
    // The the position of the main block
    public BlockPos mainBlockPosition;

    public TileEntityFake()
    {
        this(TYPE);
    }

    public TileEntityFake(TileEntityType<?> type)
    {
        super(type);
    }

    public TileEntityFake(BlockPos mainBlockPosition)
    {
        this(TYPE);
        this.mainBlockPosition = mainBlockPosition;
    }

    //    public void setMainBlock(BlockPos mainBlock)
//    {
//        this.mainBlockPosition = mainBlock;
//
//        if (!this.world.isRemote)
//        {
//            this.world.notifyBlockUpdate(this.getPos());
//        }
//    }

    public void onBlockRemoval()
    {
        if (this.mainBlockPosition != null)
        {
            TileEntity tileEntity = this.world.getTileEntity(this.mainBlockPosition);

            if (tileEntity instanceof IMultiBlock)
            {
                IMultiBlock mainBlock = (IMultiBlock) tileEntity;
                mainBlock.onDestroy(this);
            }
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, PlayerEntity player)
    {
        if (this.mainBlockPosition != null)
        {
            TileEntity tileEntity = this.world.getTileEntity(this.mainBlockPosition);

            if (tileEntity instanceof IMultiBlock)
            {
                return ((IMultiBlock) tileEntity).onActivated(player);
            }
        }

        return false;
    }

    public boolean onBlockWrenched(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, BlockRayTraceResult hit)
    {
        if (this.mainBlockPosition != null)
        {
            BlockState state = this.world.getBlockState(this.mainBlockPosition);

            if (state.getBlock() instanceof BlockAdvanced)
            {
                return ((BlockAdvanced) state.getBlock()).onBlockActivated(state, world, this.mainBlockPosition, entityPlayer, hand, hit);
            }
        }

        return false;
    }

    public TileEntity getMainBlockTile()
    {
        if (this.mainBlockPosition != null)
        {
            return this.world.getTileEntity(this.mainBlockPosition);
        }

        return null;
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        CompoundNBT tag = nbt.getCompound("mainBlockPosition");
        this.mainBlockPosition = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);

        if (this.mainBlockPosition != null)
        {
            CompoundNBT tag = new CompoundNBT();
            tag.putInt("x", this.mainBlockPosition.getX());
            tag.putInt("y", this.mainBlockPosition.getY());
            tag.putInt("z", this.mainBlockPosition.getZ());
            nbt.put("mainBlockPosition", tag);
        }

        return nbt;
    }

    protected boolean initialiseMultiTiles(BlockPos pos, World world)
    {
        IMultiBlock thisTile = (IMultiBlock)this;
        
        //Client can create its own fake blocks and tiles - no need for networking in 1.8+
        if (world.isRemote)
        {
            thisTile.onCreate(world, pos);
        }
        
        List<BlockPos> positions = new ArrayList<>();
        thisTile.getPositions(pos, positions);
        boolean result = true;
        for (BlockPos vecToAdd : positions)
        {
            TileEntity tile = world.getTileEntity(vecToAdd);
            if (tile instanceof TileEntityFake)
            {
                ((TileEntityFake) tile).mainBlockPosition = pos;
            }
            else if (tile == null)
            {
                Block b = world.getBlockState(vecToAdd).getBlock();
                if (!(b instanceof BlockMulti))
                {
                    world.setBlockState(vecToAdd, GCBlocks.fakeBlock.getDefaultState().with(BlockMulti.MULTI_TYPE, thisTile.getMultiType()), 2);
                }
                world.setTileEntity(vecToAdd, new TileEntityFake(pos));
            }
            else
            {
                result = false;
            }
        }
        if (result == false && !world.isRemote)
        {
//            //Try again to create all the multiblocks - currently disabled because making new tiles here interferes with server->client tileEntity sync during worldgen (Abandoned Base)
//            thisTile.onCreate(world, pos);
        }

        return result;
    }
}
