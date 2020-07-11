package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkConnection;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public abstract class BlockTransmitter extends BlockAdvanced
{
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    public BlockTransmitter(Properties builder)
    {
        super(builder);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);

        TileEntity tile = worldIn.getTileEntity(pos);

//        this.setBlockBoundsBasedOnState(worldIn, pos);
        GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_UPDATE_WIRE_BOUNDS, GCCoreUtil.getDimensionType(worldIn), new Object[]{pos}), new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 10.0D, GCCoreUtil.getDimensionType(worldIn)));

        if (tile instanceof INetworkConnection)
        {
            ((INetworkConnection) tile).refresh();
        }
    }

//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockReader worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getCollisionBoundingBox(blockState, worldIn, pos);
//    }
//
//    @Override
//    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getSelectedBoundingBox(state, worldIn, pos);
//    }

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
//    @Override
//    public void setBlockBoundsBasedOnState(IBlockReader worldIn, BlockPos pos)
//    {
//        TileEntity tileEntity = worldIn.getTileEntity(pos);
//
//        if (tileEntity instanceof ITransmitter)
//        {
//            Vector3 minVector = this.getMinVector(worldIn.getBlockState(pos));
//            Vector3 maxVector = this.getMaxVector(worldIn.getBlockState(pos));
//
//            TileEntity[] connectable = new TileEntity[6];
//            switch (this.getNetworkType(worldIn.getBlockState(pos)))
//            {
//            case FLUID:
//                connectable = OxygenUtil.getAdjacentFluidConnections(tileEntity);
//                break;
//            case POWER:
//                connectable = EnergyUtil.getAdjacentPowerConnections(tileEntity);
//                break;
//            default:
//                break;
//            }
//
//            float minX = (float) minVector.x;
//            float minY = (float) minVector.y;
//            float minZ = (float) minVector.z;
//            float maxX = (float) maxVector.x;
//            float maxY = (float) maxVector.y;
//            float maxZ = (float) maxVector.z;
//
//            if (connectable[0] != null)
//            {
//                minY = 0.0F;
//            }
//
//            if (connectable[1] != null)
//            {
//                maxY = 1.0F;
//            }
//
//            if (connectable[2] != null)
//            {
//                minZ = 0.0F;
//            }
//
//            if (connectable[3] != null)
//            {
//                maxZ = 1.0F;
//            }
//
//            if (connectable[4] != null)
//            {
//                minX = 0.0F;
//            }
//
//            if (connectable[5] != null)
//            {
//                maxX = 1.0F;
//            }
//
//            this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
//        }
//    }
    public abstract NetworkType getNetworkType(BlockState state);

//    @Override
//    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
//    {
//        Vector3 minVector = this.getMinVector(state);
//        Vector3 maxVector = this.getMaxVector(state);
//        this.setBlockBounds((float) minVector.x, (float) minVector.y, (float) minVector.z, (float) maxVector.x, (float) maxVector.y, (float) maxVector.z);
//        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//
//        TileEntity tileEntity = worldIn.getTileEntity(pos);
//        if (tileEntity instanceof ITransmitter)
//        {
//            TileEntity[] connectable;
//            switch (this.getNetworkType(state))
//            {
//            case FLUID:
//                connectable = OxygenUtil.getAdjacentFluidConnections(tileEntity);
//                break;
//            case POWER:
//                connectable = EnergyUtil.getAdjacentPowerConnections(tileEntity);
//                break;
//            default:
//                connectable = new TileEntity[6];
//            }
//
//            if (connectable[4] != null)
//            {
//                this.setBlockBounds(0, (float) minVector.y, (float) minVector.z, (float) maxVector.x, (float) maxVector.y, (float) maxVector.z);
//                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//            }
//
//            if (connectable[5] != null)
//            {
//                this.setBlockBounds((float) minVector.x, (float) minVector.y, (float) minVector.z, 1, (float) maxVector.y, (float) maxVector.z);
//                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//            }
//
//            if (connectable[0] != null)
//            {
//                this.setBlockBounds((float) minVector.x, 0, (float) minVector.z, (float) maxVector.x, (float) maxVector.y, (float) maxVector.z);
//                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//            }
//
//            if (connectable[1] != null)
//            {
//                this.setBlockBounds((float) minVector.x, (float) minVector.y, (float) minVector.z, (float) maxVector.x, 1, (float) maxVector.z);
//                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//            }
//
//            if (connectable[2] != null)
//            {
//                this.setBlockBounds((float) minVector.x, (float) minVector.y, 0, (float) maxVector.x, (float) maxVector.y, (float) maxVector.z);
//                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//            }
//
//            if (connectable[3] != null)
//            {
//                this.setBlockBounds((float) minVector.x, (float) minVector.y, (float) minVector.z, (float) maxVector.x, (float) maxVector.y, 1);
//                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//            }
//        }
//
//        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
//    }

//    @Override
//    public BlockState getActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        TileEntity tileEntity = worldIn.getTileEntity(pos);
//
//        if (tileEntity instanceof ITransmitter)
//        {
//            TileEntity[] connectable = new TileEntity[6];
//            switch (this.getNetworkType(state))
//            {
//            case FLUID:
//                connectable = OxygenUtil.getAdjacentFluidConnections(tileEntity);
//                break;
//            case POWER:
//                connectable = EnergyUtil.getAdjacentPowerConnections(tileEntity);
//                break;
//            default:
//                break;
//            }
//
//            return state.with(DOWN, Boolean.valueOf(connectable[Direction.DOWN.ordinal()] != null))
//                    .with(UP, Boolean.valueOf(connectable[Direction.UP.ordinal()] != null))
//                    .with(NORTH, Boolean.valueOf(connectable[Direction.NORTH.ordinal()] != null))
//                    .with(EAST, Boolean.valueOf(connectable[Direction.EAST.ordinal()] != null))
//                    .with(SOUTH, Boolean.valueOf(connectable[Direction.SOUTH.ordinal()] != null))
//                    .with(WEST, Boolean.valueOf(connectable[Direction.WEST.ordinal()] != null));
//        }
//
//        return state;
//    }
}
