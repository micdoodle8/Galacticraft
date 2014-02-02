package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkConnection;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumPacketClient;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.PacketDispatcher;

public abstract class GCCoreBlockTransmitter extends BlockContainer
{
	public Vector3 minVector = new Vector3(0.3, 0.3, 0.3);
	public Vector3 maxVector = new Vector3(0.7, 0.7, 0.7);

	public GCCoreBlockTransmitter(int id, Material material)
	{
		super(id, material);
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		super.onBlockAdded(world, x, y, z);

		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof INetworkConnection)
		{
			((INetworkConnection) tileEntity).refresh();
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
	{
		super.onNeighborBlockChange(world, x, y, z, blockID);

		TileEntity tile = world.getBlockTileEntity(x, y, z);

		this.setBlockBoundsBasedOnState(world, x, y, z);
		PacketDispatcher.sendPacketToAllAround(x, y, z, 10, world.provider.dimensionId, PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.UPDATE_WIRE_BOUNDS, new Object[] { x, y, z }));

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
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	/**
	 * Returns the bounding box of the wired rectangular prism to render.
	 */
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		TileEntity[] connectable = new TileEntity[6];

		if (tileEntity != null)
		{
			switch (this.getNetworkType())
			{
			case OXYGEN:
				connectable = WorldUtil.getAdjacentOxygenConnections(tileEntity);
				break;
			case POWER:
				connectable = WorldUtil.getAdjacentPowerConnections(tileEntity);
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
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List list, Entity entity)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		TileEntity[] connectable = new TileEntity[6];

		switch (this.getNetworkType())
		{
		case OXYGEN:
			connectable = WorldUtil.getAdjacentOxygenConnections(tileEntity);
			break;
		case POWER:
			connectable = WorldUtil.getAdjacentPowerConnections(tileEntity);
			break;
		default:
			break;
		}

		this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
		super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);

		if (connectable[4] != null)
		{
			this.setBlockBounds(0, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
			super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
		}

		if (connectable[5] != null)
		{
			this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, 1, (float) this.maxVector.y, (float) this.maxVector.z);
			super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
		}

		if (connectable[0] != null)
		{
			this.setBlockBounds((float) this.minVector.x, 0, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
			super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
		}

		if (connectable[1] != null)
		{
			this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, 1, (float) this.maxVector.z);
			super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
		}

		if (connectable[2] != null)
		{
			this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, 0, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
			super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
		}

		if (connectable[3] != null)
		{
			this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, 1);
			super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
		}

		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
}
