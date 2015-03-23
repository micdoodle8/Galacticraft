package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;

public class TileEntityMinerBaseSingle extends TileEntity
{
    @Override
    public void updateEntity()
    {
        if (!this.worldObj.isRemote)
        {
	    	final ArrayList<TileEntity> attachedBaseBlocks = new ArrayList<TileEntity>();

	    	SEARCH:
	        for (int x = this.xCoord; x < this.xCoord + 2; x++)
	        {
	            for (int y = this.yCoord; y < this.yCoord + 2; y++)
	            {
		            for (int z = this.zCoord; z < this.zCoord + 2; z++)
		            {
	                	final TileEntity tile = this.worldObj.getTileEntity(x, y, z);
	
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
	                tile.getWorldObj().setBlock(tile.xCoord, tile.yCoord, tile.zCoord, AsteroidBlocks.minerBaseFull, 0, 3);
	            }
	
	            TileEntityMinerBase masterTile = null;
		        for (int x = this.xCoord; x < this.xCoord + 2; x++)
		        {
		            for (int y = this.yCoord; y < this.yCoord + 2; y++)
		            {
			            for (int z = this.zCoord; z < this.zCoord + 2; z++)
			            {
		                	final TileEntity tile = this.worldObj.getTileEntity(x, y, z);
		
			                if (tile instanceof TileEntityMinerBase)
			                {
				                if (x == this.xCoord && y == this.yCoord && z == this.zCoord)
				                {
				                	((TileEntityMinerBase) tile).setMaster();
				                	masterTile = (TileEntityMinerBase) tile;
				                }
				                else if (masterTile != null)
				                	((TileEntityMinerBase) tile).setSlave(masterTile);
			                }
			            }
		            }
		        }	            
	        }
        }
    }
}
