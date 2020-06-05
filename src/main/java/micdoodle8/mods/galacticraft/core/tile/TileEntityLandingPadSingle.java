package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

import java.util.ArrayList;

public class TileEntityLandingPadSingle extends TileEntity implements ITickable
{
    private int corner = 0;

    @Override
    public void update()
    {
        if (!this.world.isRemote && this.corner == 0)
        {
            final ArrayList<TileEntity> attachedLaunchPads = new ArrayList<>();

            for (int x = this.getPos().getX() - 1; x < this.getPos().getX() + 2; x++)
            {
                for (int z = this.getPos().getZ() - 1; z < this.getPos().getZ() + 2; z++)
                {
                    final TileEntity tile = this.world.getTileEntity(new BlockPos(x, this.getPos().getY(), z));

                    if (tile instanceof TileEntityLandingPadSingle && !tile.isRemoved() && ((TileEntityLandingPadSingle)tile).corner == 0)
                    {
                        attachedLaunchPads.add(tile);
                    }
                }
            }

            if (attachedLaunchPads.size() == 9)
            {
                for (final TileEntity tile : attachedLaunchPads)
                {
                    this.world.markTileEntityForRemoval(tile);
                    ((TileEntityLandingPadSingle)tile).corner = 1;
                }

                this.world.setBlockState(this.getPos(), GCBlocks.landingPadFull.getDefaultState(), 2);
            }
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }
}
