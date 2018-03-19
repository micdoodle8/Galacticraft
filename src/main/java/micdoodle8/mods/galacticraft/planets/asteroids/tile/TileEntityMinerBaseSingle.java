package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

import java.util.ArrayList;

public class TileEntityMinerBaseSingle extends TileEntity implements ITickable
{
    private int corner = 0;

    @Override
    public void update()
    {
        if (!this.worldObj.isRemote && this.corner == 0)
        {
            final ArrayList<TileEntity> attachedBaseBlocks = new ArrayList<TileEntity>();
            
            final int thisX = this.getPos().getX();
            final int thisY = this.getPos().getY();
            final int thisZ = this.getPos().getZ();

            boolean success = true;
            SEARCH:
            for (int x = 0; x < 2; x++)
            {
                for (int y = 0; y < 2; y++)
                {
                    for (int z = 0; z < 2; z++)
                    {
                        BlockPos pos = new BlockPos(x + thisX, y + thisY, z + thisZ);
                        final TileEntity tile = this.worldObj.isBlockLoaded(pos, false) ? this.worldObj.getTileEntity(pos) : null;

                        if (tile instanceof TileEntityMinerBaseSingle && !tile.isInvalid() && ((TileEntityMinerBaseSingle) tile).corner == 0)
                        {
                            attachedBaseBlocks.add(tile);
                        }
                        else
                        {
                            success = false;
                            break SEARCH;
                        }
                    }
                }
            }

            if (success)
            {
                TileEntityMinerBase.addNewMinerBase(GCCoreUtil.getDimensionID(this), this.getPos());
                for (final TileEntity tile : attachedBaseBlocks)
                {
                    ((TileEntityMinerBaseSingle) tile).corner = 1;
                    this.worldObj.setBlockToAir(this.getPos());
                }
                //Don't try setting a new block with a TileEntity, because new tiles can
                //get removed after the end of this tileEntity.update() tick - setting a new block
                //here would automatically invalidate this tile.
                //
                //(It's because if this tileEntity is now invalid, World.updateEntities() removes
                // *any* tileEntity at this position - see call to Chunk.removeTileEntity(pos)nee!)
                //
                //Equally if any others of these TileEntityMinerBaseSingle are ticked AFTER this
                //in the same server update tick, then those new ones will also be removed
                //because their TileEntityMinerBaseSingle is now, inevitably, invalid!
            }
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }
}
