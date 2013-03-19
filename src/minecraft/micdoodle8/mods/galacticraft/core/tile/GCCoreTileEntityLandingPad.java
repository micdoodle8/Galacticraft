package micdoodle8.mods.galacticraft.core.tile;

import java.util.HashSet;
import java.util.List;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.entities.EntitySpaceshipBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.multiblock.IMultiBlock;
import universalelectricity.prefab.multiblock.TileEntityMulti;
import universalelectricity.prefab.network.IPacketReceiver;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.FMLLog;

public class GCCoreTileEntityLandingPad extends TileEntityMulti implements IPacketReceiver, IMultiBlock
{
	protected long ticks = 0;
	private EntitySpaceshipBase spaceshipOnPad;
	public HashSet<TileEntity> connectedTiles = new HashSet<TileEntity>();
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		

		if (!this.worldObj.isRemote)
		{
			for (int x = -2; x < 3; x++)
			{
				for (int z = -2; z < 3; z++)
				{
					if ((x == -2 || x == 2) || (z == -2 || z == 2))
					{
						if (Math.abs(x) != Math.abs(z))
						{
							TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + x, this.yCoord, this.zCoord + z);

							if (tile != null && tile instanceof GCCoreTileEntityFuelLoader)
							{
								this.connectedTiles.add(tile);
							}
						}
					}
				}
			}
			
			for (TileEntity tile : this.connectedTiles)
			{
				GCCoreTileEntityFuelLoader loader = (GCCoreTileEntityFuelLoader) tile;

				TileEntity newTile = this.worldObj.getBlockTileEntity(loader.xCoord, loader.yCoord, loader.zCoord);

				if (newTile == null || !(newTile instanceof GCCoreTileEntityFuelLoader))
				{
					this.connectedTiles.remove(newTile);
				}
			}
			
			List list = this.worldObj.getEntitiesWithinAABB(EntitySpaceshipBase.class, AxisAlignedBB.getAABBPool().getAABB(this.xCoord - 0.5D, this.yCoord, this.zCoord - 0.5D, this.xCoord + 0.5D, this.yCoord + 5, this.zCoord + 0.5D));
			
			boolean changed = false;
			
			for (Object o : list)
			{
				if (o != null && o instanceof EntitySpaceshipBase)
				{
					EntitySpaceshipBase spaceship = (EntitySpaceshipBase) o;
					
					this.spaceshipOnPad = spaceship;
					
					this.spaceshipOnPad.setLandingPad(this);
					
					changed = true;
				}
			}
			
			if (!changed)
			{
				this.spaceshipOnPad = null;
			}
		}
	}

	@Override
	public boolean canUpdate()
	{
		return true;
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
	{
		
	}

	@Override
	public boolean onActivated(EntityPlayer entityPlayer) 
	{
		return false;
	}

	@Override
	public void onCreate(Vector3 placedPosition) 
	{
		this.mainBlockPosition = placedPosition;
		
		for (int x = -1; x < 2; x++)
		{
			for (int z = -1; z < 2; z++)
			{
				Vector3 vecToAdd = Vector3.add(placedPosition, new Vector3(x, 0, z));
				
				if (!vecToAdd.isEqual(placedPosition))
				{
					GCCoreBlocks.dummyBlock.makeFakeBlock(this.worldObj, vecToAdd, placedPosition);
				}
			}
		}
	}

	@Override
	public void onDestroy(TileEntity callingBlock) 
	{
		Vector3 thisBlock = new Vector3(this);
		
		for (int x = -1; x < 2; x++)
		{
			for (int z = -1; z < 2; z++)
			{
				this.worldObj.setBlockAndMetadataWithNotify(thisBlock.intX() + x, thisBlock.intY(), thisBlock.intZ() + z, 0, 0, 3);
			}
		}
	}
}
