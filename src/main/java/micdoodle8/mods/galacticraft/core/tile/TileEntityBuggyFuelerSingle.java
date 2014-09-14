package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;

public class TileEntityBuggyFuelerSingle extends TileEntity
{
    @Override
    public void updateEntity()
    {
        if (!this.worldObj.isRemote)
        {
	        final ArrayList<TileEntity> attachedLaunchPads = new ArrayList<TileEntity>();
	
	        for (int x = this.xCoord - 1; x < this.xCoord + 2; x++)
	        {
	            for (int z = this.zCoord - 1; z < this.zCoord + 2; z++)
	            {
	                final TileEntity tile = this.worldObj.getTileEntity(x, this.yCoord, z);
	
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
	                tile.getWorldObj().setBlock(tile.xCoord, tile.yCoord, tile.zCoord, Blocks.air, 0, 3);
	            }
	
	            this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, GCBlocks.landingPadFull, 1, 3);
	            final TileEntityBuggyFueler tile = (TileEntityBuggyFueler) this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord);
	
	            if (tile != null)
	            {
	                tile.onCreate(new BlockVec3(this.xCoord, this.yCoord, this.zCoord));
	            }
	        }
        }
    }
}
