package micdoodle8.mods.galacticraft.core.tile;

import java.util.HashSet;
import java.util.List;

import micdoodle8.mods.galacticraft.api.entity.ICargoEntity;
import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.entity.IFuelable;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.client.FMLClientHandler;

/**
 * GCCoreTileEntityBuggyFueler.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntityBuggyFueler extends TileEntityMulti implements IMultiBlock, IFuelable, IFuelDock, ICargoEntity
{
	protected long ticks = 0;
	private IDockable dockedEntity;

	public GCCoreTileEntityBuggyFueler()
	{
		super(GalacticraftCore.CHANNELENTITIES);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			final List<?> list = this.worldObj.getEntitiesWithinAABB(IFuelable.class, AxisAlignedBB.getAABBPool().getAABB(this.xCoord - 1.5D, this.yCoord - 2.0, this.zCoord - 1.5D, this.xCoord + 1.5D, this.yCoord + 4.0, this.zCoord + 1.5D));

			boolean changed = false;

			for (final Object o : list)
			{
				if (o != null && o instanceof IDockable && !this.worldObj.isRemote)
				{
					final IDockable fuelable = (IDockable) o;

					if (fuelable.isDockValid(this))
					{
						this.dockedEntity = fuelable;

						this.dockedEntity.setPad(this);

						changed = true;
					}
				}
			}

			if (!changed)
			{
				if (this.dockedEntity != null)
				{
					this.dockedEntity.setPad(null);
				}

				this.dockedEntity = null;
			}
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
				final Vector3 vecToAdd = new Vector3(placedPosition.x + x, placedPosition.y, placedPosition.z + z);

				if (!vecToAdd.equals(placedPosition))
				{
					((GCCoreBlockMulti) GCCoreBlocks.fakeBlock).makeFakeBlock(this.worldObj, vecToAdd, placedPosition, 2);
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
				{
					FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(thisBlock.intX() + x, thisBlock.intY(), thisBlock.intZ() + z, GCCoreBlocks.landingPad.blockID & 4095, GCCoreBlocks.landingPad.blockID >> 12 & 255);
				}
				this.worldObj.destroyBlock(thisBlock.intX() + x, thisBlock.intY(), thisBlock.intZ() + z, x == 0 && z == 0);
			}
		}

		if (this.dockedEntity != null)
		{
			this.dockedEntity.onPadDestroyed();
			this.dockedEntity = null;
		}
	}

	@Override
	public int addFuel(FluidStack liquid, boolean doFill)
	{
		if (this.dockedEntity != null)
		{
			return this.dockedEntity.addFuel(liquid, doFill);
		}

		return 0;
	}

	@Override
	public FluidStack removeFuel(int amount)
	{
		if (this.dockedEntity != null)
		{
			return this.dockedEntity.removeFuel(amount);
		}

		return null;
	}

	@Override
	public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
	{
		if (this.dockedEntity != null)
		{
			return this.dockedEntity.addCargo(stack, doAdd);
		}

		return EnumCargoLoadingState.NOTARGET;
	}

	@Override
	public RemovalResult removeCargo(boolean doRemove)
	{
		if (this.dockedEntity != null)
		{
			return this.dockedEntity.removeCargo(doRemove);
		}

		return new RemovalResult(EnumCargoLoadingState.NOTARGET, null);
	}

	@Override
	public HashSet<ILandingPadAttachable> getConnectedTiles()
	{
		HashSet<ILandingPadAttachable> connectedTiles = new HashSet<ILandingPadAttachable>();

		for (int x = -2; x < 3; x++)
		{
			for (int z = -2; z < 3; z++)
			{
				if (x == -2 || x == 2 || z == -2 || z == 2)
				{
					if (Math.abs(x) != Math.abs(z))
					{
						final TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + x, this.yCoord, this.zCoord + z);

						if (tile != null && tile instanceof ILandingPadAttachable && ((ILandingPadAttachable) tile).canAttachToLandingPad(this.worldObj, this.xCoord, this.yCoord, this.zCoord))
						{
							connectedTiles.add((ILandingPadAttachable) tile);
						}
					}
				}
			}
		}

		return connectedTiles;
	}

	@Override
	public boolean isBlockAttachable(IBlockAccess world, int x, int y, int z)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (tile != null && tile instanceof ILandingPadAttachable)
		{
			return ((ILandingPadAttachable) tile).canAttachToLandingPad(world, this.xCoord, this.yCoord, this.zCoord);
		}

		return false;
	}

	@Override
	public IDockable getDockedEntity()
	{
		return this.dockedEntity;
	}
}
