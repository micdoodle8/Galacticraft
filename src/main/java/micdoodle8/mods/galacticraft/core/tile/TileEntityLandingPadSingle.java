package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.deepspace.dimension.WorldProviderDeepSpace;
import net.minecraft.block.state.IBlockState;
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
        if (!this.worldObj.isRemote && this.corner == 0)
        {
            final ArrayList<TileEntity> attachedLaunchPads = new ArrayList<>();

            int thisX = this.getPos().getX();
            int thisZ = this.getPos().getZ();
            int thisZ_lsb = thisZ & 0x0f; 
            for (int z = thisZ - 1; z < thisZ + 2; z++)
            {
                // Prevent formation across z-chunk boundaries in Deep Space
                if (GalacticraftCore.isPlanetsLoaded && this.worldObj.provider instanceof WorldProviderDeepSpace && (z == -1 && thisZ_lsb == 0 || z == 1 && thisZ_lsb == 15)) continue;

                for (int x = thisX - 1; x < thisX + 2; x++)
                {
                    final TileEntity tile = this.worldObj.getTileEntity(new BlockPos(x, this.getPos().getY(), z));

                    if (tile instanceof TileEntityLandingPadSingle && !tile.isInvalid() && ((TileEntityLandingPadSingle)tile).corner == 0)
                    {
                        attachedLaunchPads.add(tile);
                    }
                }
            }

            if (attachedLaunchPads.size() == 9)
            {
                for (final TileEntity tile : attachedLaunchPads)
                {
                    this.worldObj.markTileEntityForRemoval(tile);
                    ((TileEntityLandingPadSingle)tile).corner = 1;
                }

                this.worldObj.setBlockState(this.getPos(), GCBlocks.landingPadFull.getDefaultState(), 2);
            }
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }
}
