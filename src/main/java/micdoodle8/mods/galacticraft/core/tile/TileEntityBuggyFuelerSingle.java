package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBuggyFuelerSingle extends TileEntity
{
	@Override
	public void updateEntity()
	{
		final ArrayList<TileEntity> attachedLaunchPads = new ArrayList<TileEntity>();

		for (int x = this.xCoord - 1; x < this.xCoord + 2; x++)
		{
			for (int z = this.zCoord - 1; z < this.zCoord + 2; z++)
			{
				final BlockVec3 vector = new BlockVec3(x, this.yCoord, z);

				final TileEntity tile = vector.getTileEntity(this.worldObj);

				if (tile != null && tile instanceof TileEntityBuggyFuelerSingle)
				{
					attachedLaunchPads.add(tile);
				}
			}
		}

		if (attachedLaunchPads.size() == 9)
		{
			for (final TileEntity tile : attachedLaunchPads)
			{
				tile.invalidate();
				tile.getWorldObj().setBlock(tile.xCoord, tile.yCoord, tile.zCoord, Blocks.air);
			}

			this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, GCBlocks.landingPadFull, 1, 3);
			final TileEntityBuggyFueler tile = (TileEntityBuggyFueler) this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord);

			if (tile instanceof IMultiBlock)
			{
				((IMultiBlock) tile).onCreate(new BlockVec3(this.xCoord, this.yCoord, this.zCoord));
			}
		}
	}
}
