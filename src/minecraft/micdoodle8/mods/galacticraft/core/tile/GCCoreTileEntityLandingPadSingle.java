package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.multiblock.IMultiBlock;
import cpw.mods.fml.common.FMLLog;

public class GCCoreTileEntityLandingPadSingle extends TileEntity
{
	@Override
	public void updateEntity()
	{
		ArrayList<TileEntity> attachedLaunchPads = new ArrayList<TileEntity>();
		
		for (int x = this.xCoord - 1; x < this.xCoord + 2; x++)
		{
			for (int z = this.zCoord - 1; z < this.zCoord + 2; z++)
			{
				Vector3 vector = new Vector3(x, this.yCoord, z);
				
				TileEntity tile = vector.getTileEntity(worldObj);
				
    			if (tile != null && tile instanceof GCCoreTileEntityLandingPadSingle)
    			{
    				attachedLaunchPads.add(tile);
    			}
			}
		}
    	
    	if (attachedLaunchPads.size() == 9)
    	{
    		for (TileEntity tile : attachedLaunchPads)
    		{
    			tile.invalidate();
    			tile.worldObj.func_94571_i(tile.xCoord, tile.yCoord, tile.zCoord);
//            	((GCCoreBlockLandingPadFull)GCCoreBlocks.landingPadFull).onBlockAdded(worldObj, tile.xCoord, tile.yCoord, tile.zCoord);
    		}
    		
        	this.worldObj.setBlockAndMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, GCCoreBlocks.landingPadFull.blockID, 0, 3);
        	GCCoreTileEntityLandingPad tile = (GCCoreTileEntityLandingPad) this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord);
        	
            if (tile instanceof IMultiBlock)
            {
                ((IMultiBlock)tile).onCreate(new Vector3((double)this.xCoord, (double)this.yCoord, (double)this.zCoord));
            }
    	}
	}
}
