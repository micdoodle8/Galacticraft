package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkConnection;
import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;

public abstract class BlockTransmitter extends BlockAdvanced
{
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");

    public BlockTransmitter(Material material)
    {
        super(material);
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);

        TileEntity tile = worldIn.getTileEntity(pos);

        this.setBlockBoundsBasedOnState(worldIn, pos);
        GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_UPDATE_WIRE_BOUNDS, GCCoreUtil.getDimensionID(worldIn), new Object[] { pos }), new NetworkRegistry.TargetPoint(GCCoreUtil.getDimensionID(worldIn), pos.getX(), pos.getY(), pos.getZ(), 10.0D));

        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tile instanceof INetworkConnection)
        {
            ((INetworkConnection) tile).refresh();
        }
    }

    public abstract Vector3 getMinVector(IBlockState state);

    public abstract Vector3 getMaxVector(IBlockState state);

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this
     * box can change after the pool has been cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        IBlockState bs = worldIn.getBlockState(pos);

        if (tileEntity instanceof ITransmitter && bs.getBlock() instanceof BlockTransmitter)
        {
            Vector3 minVector = this.getMinVector(bs);
            Vector3 maxVector = this.getMaxVector(bs);

            TileEntity[] connectable = new TileEntity[6];
            switch (this.getNetworkType(bs))
            {
            case FLUID:
                connectable = OxygenUtil.getAdjacentFluidConnections(tileEntity);
                break;
            case POWER:
                connectable = EnergyUtil.getAdjacentPowerConnections(tileEntity);
                break;
            default:
                break;
            }

            float minX = (float) minVector.x;
            float minY = (float) minVector.y;
            float minZ = (float) minVector.z;
            float maxX = (float) maxVector.x;
            float maxY = (float) maxVector.y;
            float maxZ = (float) maxVector.z;

            if (connectable[0] != null)
            {
                minY = 0.0F;
            }

            if (connectable[1] != null)
            {
                maxY = 1.0F;
            }

            if (connectable[2] != null)
            {
                minZ = 0.0F;
            }

            if (connectable[3] != null)
            {
                maxZ = 1.0F;
            }

            if (connectable[4] != null)
            {
                minX = 0.0F;
            }

            if (connectable[5] != null)
            {
                maxX = 1.0F;
            }

            this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }
    }

    public abstract NetworkType getNetworkType(IBlockState state);

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
    {
        Vector3 minVector = this.getMinVector(state);
        Vector3 maxVector = this.getMaxVector(state);
        this.setBlockBounds((float) minVector.x, (float) minVector.y, (float) minVector.z, (float) maxVector.x, (float) maxVector.y, (float) maxVector.z);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof ITransmitter)
        {
            TileEntity[] connectable;
            switch (this.getNetworkType(state))
            {
            case FLUID:
                connectable = OxygenUtil.getAdjacentFluidConnections(tileEntity);
                break;
            case POWER:
                connectable = EnergyUtil.getAdjacentPowerConnections(tileEntity);
                break;
            default:
                connectable = new TileEntity[6];
            }

            if (connectable[4] != null)
            {
                this.setBlockBounds(0, (float) minVector.y, (float) minVector.z, (float) maxVector.x, (float) maxVector.y, (float) maxVector.z);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }

            if (connectable[5] != null)
            {
                this.setBlockBounds((float) minVector.x, (float) minVector.y, (float) minVector.z, 1, (float) maxVector.y, (float) maxVector.z);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }

            if (connectable[0] != null)
            {
                this.setBlockBounds((float) minVector.x, 0, (float) minVector.z, (float) maxVector.x, (float) maxVector.y, (float) maxVector.z);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }

            if (connectable[1] != null)
            {
                this.setBlockBounds((float) minVector.x, (float) minVector.y, (float) minVector.z, (float) maxVector.x, 1, (float) maxVector.z);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }

            if (connectable[2] != null)
            {
                this.setBlockBounds((float) minVector.x, (float) minVector.y, 0, (float) maxVector.x, (float) maxVector.y, (float) maxVector.z);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }

            if (connectable[3] != null)
            {
                this.setBlockBounds((float) minVector.x, (float) minVector.y, (float) minVector.z, (float) maxVector.x, (float) maxVector.y, 1);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof ITransmitter)
        {
            TileEntity[] connectable = new TileEntity[6];
            switch (this.getNetworkType(state))
            {
            case FLUID:
                connectable = OxygenUtil.getAdjacentFluidConnections(tileEntity);
                break;
            case POWER:
                connectable = EnergyUtil.getAdjacentPowerConnections(tileEntity);
                break;
            default:
                break;
            }

            return state.withProperty(DOWN, Boolean.valueOf(connectable[EnumFacing.DOWN.ordinal()] != null))
                    .withProperty(UP, Boolean.valueOf(connectable[EnumFacing.UP.ordinal()] != null))
                    .withProperty(NORTH, Boolean.valueOf(connectable[EnumFacing.NORTH.ordinal()] != null))
                    .withProperty(EAST, Boolean.valueOf(connectable[EnumFacing.EAST.ordinal()] != null))
                    .withProperty(SOUTH, Boolean.valueOf(connectable[EnumFacing.SOUTH.ordinal()] != null))
                    .withProperty(WEST, Boolean.valueOf(connectable[EnumFacing.WEST.ordinal()] != null));
        }

        return state;
    }
}
