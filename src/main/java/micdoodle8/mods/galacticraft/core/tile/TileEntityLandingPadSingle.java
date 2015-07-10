package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;

public class TileEntityLandingPadSingle extends TileEntity implements IUpdatePlayerListBox
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
	                tile.invalidate();
	                tile.getWorld().setBlockState(tile.getPos(), Blocks.air.getDefaultState(), 3);
	                // ((GCCoreBlockLandingPadFull)GCCoreBlocks.landingPadFull).onBlockAdded(worldObj,
	                // tile.xCoord, tile.yCoord, tile.zCoord);
	            }
	
	            this.worldObj.setBlockState(this.getPos(), GCBlocks.landingPadFull.getDefaultState(), 3);
	            final TileEntityLandingPad tilePadFull = (TileEntityLandingPad) this.worldObj.getTileEntity(this.getPos());
	
	            if (tilePadFull != null)
	            {
	                tilePadFull.onCreate(worldObj, this.getPos());
	            }
	        }
        }
    }
}
