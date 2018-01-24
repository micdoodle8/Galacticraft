package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

import java.util.ArrayList;

public class TileEntityLandingPadSingle extends TileEntity implements ITickable
{
    @Override
    public void update()
    {
        if (!this.world.isRemote)
        {
            final ArrayList<TileEntity> attachedLaunchPads = new ArrayList<TileEntity>();

            for (int x = this.getPos().getX() - 1; x < this.getPos().getX() + 2; x++)
            {
                for (int z = this.getPos().getZ() - 1; z < this.getPos().getZ() + 2; z++)
                {
                    final TileEntity tile = this.world.getTileEntity(new BlockPos(x, this.getPos().getY(), z));

                    if (tile instanceof TileEntityLandingPadSingle)
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
                }

                this.world.setBlockState(this.getPos(), GCBlocks.landingPadFull.getDefaultState(), 2);
            }
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }
}
