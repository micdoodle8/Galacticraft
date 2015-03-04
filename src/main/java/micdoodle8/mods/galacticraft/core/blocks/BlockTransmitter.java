package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkConnection;
import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityHydrogenPipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;

public abstract class BlockTransmitter extends BlockContainer
{
    public Vector3 minVector = new Vector3(0.3, 0.3, 0.3);
    public Vector3 maxVector = new Vector3(0.7, 0.7, 0.7);

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
        GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_UPDATE_WIRE_BOUNDS, new Object[] { pos.getX(), pos.getY(), pos.getZ() }), new NetworkRegistry.TargetPoint(worldIn.provider.getDimensionId(), pos.getX(), pos.getY(), pos.getZ(), 10.0D));

        if (tile instanceof INetworkConnection)
        {
            ((INetworkConnection) tile).refresh();
        }
    }

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

        if (tileEntity instanceof ITransmitter)
        {
            TileEntity[] connectable = new TileEntity[6];
            switch (this.getNetworkType())
            {
            case OXYGEN:
                connectable = OxygenUtil.getAdjacentOxygenConnections(tileEntity);
                break;
            case HYDROGEN:
                connectable = TileEntityHydrogenPipe.getAdjacentHydrogenConnections(tileEntity);
                break;
            case POWER:
                connectable = EnergyUtil.getAdjacentPowerConnections(tileEntity);
                break;
            default:
                break;
            }

            float minX = (float) this.minVector.x;
            float minY = (float) this.minVector.y;
            float minZ = (float) this.minVector.z;
            float maxX = (float) this.maxVector.x;
            float maxY = (float) this.maxVector.y;
            float maxZ = (float) this.maxVector.z;

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

    public abstract NetworkType getNetworkType();

    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
    {
        this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof ITransmitter)
        {
            TileEntity[] connectable;
            switch (this.getNetworkType())
            {
            case OXYGEN:
                connectable = OxygenUtil.getAdjacentOxygenConnections(tileEntity);
                break;
            case HYDROGEN:
                connectable = TileEntityHydrogenPipe.getAdjacentHydrogenConnections(tileEntity);
                break;
            case POWER:
                connectable = EnergyUtil.getAdjacentPowerConnections(tileEntity);
                break;
            default:
                connectable = new TileEntity[6];
            }

            if (connectable[4] != null)
            {
                this.setBlockBounds(0, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }

            if (connectable[5] != null)
            {
                this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, 1, (float) this.maxVector.y, (float) this.maxVector.z);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }

            if (connectable[0] != null)
            {
                this.setBlockBounds((float) this.minVector.x, 0, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }

            if (connectable[1] != null)
            {
                this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, 1, (float) this.maxVector.z);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }

            if (connectable[2] != null)
            {
                this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, 0, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }

            if (connectable[3] != null)
            {
                this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, 1);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
