package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ITickable;

import java.util.ArrayList;

public class TileEntityMinerBaseSingle extends TileEntity implements ITickable
{
    @Override
    public void update()
    {
        if (!this.worldObj.isRemote)
        {
            final ArrayList<TileEntity> attachedBaseBlocks = new ArrayList<TileEntity>();

            SEARCH:
            for (int x = this.getPos().getX(); x < this.getPos().getX() + 2; x++)
            {
                for (int y = this.getPos().getY(); y < this.getPos().getY() + 2; y++)
                {
                    for (int z = this.getPos().getZ(); z < this.getPos().getZ() + 2; z++)
                    {
                        final TileEntity tile = this.worldObj.getTileEntity(new BlockPos(x, y, z));

                        if (tile instanceof TileEntityMinerBaseSingle)
                        {
                            attachedBaseBlocks.add(tile);
                        }
                        else
                        {
                            break SEARCH;
                        }
                    }
                }
            }

            if (attachedBaseBlocks.size() == 8)
            {
                for (final TileEntity tile : attachedBaseBlocks)
                {
                    tile.invalidate();
                    tile.getWorld().setBlockState(tile.getPos(), AsteroidBlocks.minerBaseFull.getDefaultState(), 3);
                    tile.getWorld().setTileEntity(tile.getPos(), new TileEntityMinerBase());
                }

                for (int x = this.getPos().getX(); x < this.getPos().getX() + 2; x++)
                {
                    for (int y = this.getPos().getY(); y < this.getPos().getY() + 2; y++)
                    {
                        for (int z = this.getPos().getZ(); z < this.getPos().getZ() + 2; z++)
                        {
                            final TileEntity tile = this.worldObj.getTileEntity(new BlockPos(x, y, z));

                            if (tile instanceof TileEntityMinerBase)
                            {
                                ((TileEntityMinerBase) tile).setMainBlockPos(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
                                ((TileEntityMinerBase) tile).updateClientFlag = true;
                            }
                        }
                    }
                }
            }
        }
    }
}
