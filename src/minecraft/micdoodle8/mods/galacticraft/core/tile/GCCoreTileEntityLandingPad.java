package micdoodle8.mods.galacticraft.core.tile;

import java.util.HashSet;
import java.util.List;

import micdoodle8.mods.galacticraft.API.IDockable;
import micdoodle8.mods.galacticraft.API.IFuelable;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.multiblock.IMultiBlock;
import universalelectricity.prefab.multiblock.TileEntityMulti;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;

public class GCCoreTileEntityLandingPad extends TileEntityMulti implements IMultiBlock, IFuelable
{
	protected long ticks = 0;
	private IDockable dockedEntity;
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
					if (x == -2 || x == 2 || z == -2 || z == 2)
					{
						if (Math.abs(x) != Math.abs(z))
						{
							final TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + x, this.yCoord, this.zCoord + z);

							if (tile != null && tile instanceof GCCoreTileEntityFuelLoader)
							{
								this.connectedTiles.add(tile);
							}
						}
					}
				}
			}

			for (final TileEntity tile : this.connectedTiles)
			{
				final GCCoreTileEntityFuelLoader loader = (GCCoreTileEntityFuelLoader) tile;

				final TileEntity newTile = this.worldObj.getBlockTileEntity(loader.xCoord, loader.yCoord, loader.zCoord);

				if (newTile == null || !(newTile instanceof GCCoreTileEntityFuelLoader))
				{
					this.connectedTiles.remove(newTile);
				}
			}

			final List list = this.worldObj.getEntitiesWithinAABB(IFuelable.class, AxisAlignedBB.getAABBPool().getAABB(this.xCoord - 0.5D, this.yCoord, this.zCoord - 0.5D, this.xCoord + 0.5D, this.yCoord + 5, this.zCoord + 0.5D));

			boolean changed = false;

			for (final Object o : list)
			{
				if (o != null && o instanceof IDockable && !this.worldObj.isRemote)
				{
					final IDockable fuelable = (IDockable) o;

					this.dockedEntity = fuelable;

					this.dockedEntity.setLandingPad(this);

					changed = true;
				}
			}

			if (!changed)
			{
				this.dockedEntity = null;
			}

//			if (this.dockedEntity != null && this.landingPadFuelTank.getLiquid() != null && this.landingPadFuelTank.getLiquid().amount > 0)
//			{
//				final LiquidStack liquid = LiquidDictionary.getLiquid("Fuel", 1);
//
//				if (liquid != null)
//				{
//					this.removeFuel(null, this.dockedEntity.addFuel(liquid, 1));
//				}
//			}
		}
	}

	@Override
	public boolean canUpdate()
	{
		return true;
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
				final Vector3 vecToAdd = Vector3.add(placedPosition, new Vector3(x, 0, z));

				if (!vecToAdd.equals(placedPosition))
				{
					GCCoreBlocks.dummyBlock.makeFakeBlock(this.worldObj, vecToAdd, placedPosition, 2);
				}
			}
		}
	}

	@Override
	public void onDestroy(TileEntity callingBlock)
	{
		final Vector3 thisBlock = new Vector3(this);

		for (int x = -1; x < 2; x++)
		{
			for (int z = -1; z < 2; z++)
			{
				if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.1D)
					FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(thisBlock.intX() + x, thisBlock.intY(), thisBlock.intZ() + z, GCCoreBlocks.landingPad.blockID & 4095, GCCoreBlocks.landingPad.blockID >> 12 & 255);
				this.worldObj.setBlock(thisBlock.intX() + x, thisBlock.intY(), thisBlock.intZ() + z, 0, 0, 3);
			}
		}

		if (this.dockedEntity != null)
		{
			this.dockedEntity.onPadDestroyed();
			this.dockedEntity = null;
		}
	}

	@Override
	public int addFuel(LiquidStack liquid, int amount, boolean doFill)
	{
		if (this.dockedEntity != null)
		{
			return this.dockedEntity.addFuel(liquid, amount, doFill);
		}

		return 0;
	}

	@Override
	public LiquidStack removeFuel(LiquidStack liquid, int amount)
	{
		if (this.dockedEntity != null)
		{
			return this.dockedEntity.removeFuel(liquid, amount);
		}

		return null;
	}
}
