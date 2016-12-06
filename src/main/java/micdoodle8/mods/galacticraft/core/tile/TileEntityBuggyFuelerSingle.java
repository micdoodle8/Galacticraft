package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.blocks.BlockLandingPadFull;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;

import java.util.ArrayList;

public class TileEntityBuggyFuelerSingle extends TileEntity implements ITickable
{
    @Override
    public void update()
    {
        if (!this.worldObj.isRemote)
        {
            final ArrayList<TileEntity> attachedLaunchPads = new ArrayList<TileEntity>();

            for (int x = this.getPos().getX() - 1; x < this.getPos().getX() + 2; x++)
            {
                for (int z = this.getPos().getZ() - 1; z < this.getPos().getZ() + 2; z++)
                {
                    final TileEntity tile = this.worldObj.getTileEntity(new BlockPos(x, this.getPos().getY(), z));

                    if (tile instanceof TileEntityBuggyFuelerSingle)
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
                    tile.getWorld().setBlockState(tile.getPos(), Blocks.air.getDefaultState(), 3);
                }

                this.worldObj.setBlockState(this.getPos(), GCBlocks.landingPadFull.getStateFromMeta(BlockLandingPadFull.EnumLandingPadFullType.BUGGY_PAD.getMeta()), 3);
                final TileEntityBuggyFueler tile = (TileEntityBuggyFueler) this.worldObj.getTileEntity(this.getPos());

                if (tile != null)
                {
                    tile.onCreate(worldObj, this.getPos());
                }
            }
        }
    }
}
