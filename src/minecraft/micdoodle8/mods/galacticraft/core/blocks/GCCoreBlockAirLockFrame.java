package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import micdoodle8.mods.galacticraft.core.tile.GCCoreBlockAdvanced;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAirLock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class GCCoreBlockAirLockFrame extends GCCoreBlockAdvanced
{
	public GCCoreBlockAirLockFrame(int par1, int par2) 
	{
		super(par1, par2, Material.rock);
	}

	@Override
    @SideOnly(Side.CLIENT)
    public int getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
		TileEntity te = par1IBlockAccess.getBlockTileEntity(par2, par3, par4);

		if (par1IBlockAccess.getBlockId(par2 - 1, par3, par4) == GCCoreBlocks.airLockSeal.blockID || par1IBlockAccess.getBlockId(par2 + 1, par3, par4) == GCCoreBlocks.airLockSeal.blockID || par1IBlockAccess.getBlockId(par2, par3 - 1, par4) == GCCoreBlocks.airLockSeal.blockID || par1IBlockAccess.getBlockId(par2, par3 + 1, par4) == GCCoreBlocks.airLockSeal.blockID || par1IBlockAccess.getBlockId(par2, par3, par4 - 1) == GCCoreBlocks.airLockSeal.blockID || par1IBlockAccess.getBlockId(par2, par3, par4 + 1) == GCCoreBlocks.airLockSeal.blockID)
		{
			return 39;
		}
		else
		{
			return 38;
		}
    }

	@Override
    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
		return 38;
    }

	@Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote)
        {
        	TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);

        	if (te instanceof GCCoreTileEntityAirLock)
        	{
                if (((GCCoreTileEntityAirLock) te).active && !this.gettingPowered(par1World, par2, par3, par4))
                {
                    par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, 4);
                }
                else if (!((GCCoreTileEntityAirLock) te).active && this.gettingPowered(par1World, par2, par3, par4))
                {
            		((GCCoreTileEntityAirLock) te).active = true;
                }
        	}
        }
    }

	@Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.isRemote)
        {
        	TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);

        	if (te instanceof GCCoreTileEntityAirLock)
        	{
        		((GCCoreTileEntityAirLock) te).updateState();
        		
                if (!this.gettingPowered(par1World, par2, par3, par4))
                {
            		((GCCoreTileEntityAirLock) te).active = false;
                }
                else
                {
            		((GCCoreTileEntityAirLock) te).active = true;
                }
        	}
        }
    }

	@Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote)
        {
        	TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);
        	
        	if (te instanceof GCCoreTileEntityAirLock)
        	{
        		if (!this.gettingPowered(par1World, par2, par3, par4))
                {
            		((GCCoreTileEntityAirLock) te).active = false;
                }
        	}
        }
    }

	@Override
    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
    	TileEntity te = par1World.getBlockTileEntity(par3, par4, par5);
    	
    	if (te instanceof GCCoreTileEntityAirLock)
    	{
    		((GCCoreTileEntityAirLock) te).updateState();
    	}
		
    	return par9;
    }

	@Override
	public TileEntity createNewTileEntity(World var1) 
	{
        return new GCCoreTileEntityAirLock();
	}

	@Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
    	return true;
    }
	
	private boolean gettingPowered(World par1World, int x, int y, int z)
	{
		int var1;
		int var2;
    	TileEntity te = par1World.getBlockTileEntity(x, y, z);
    	
    	if (te instanceof GCCoreTileEntityAirLock)
    	{
    		GCCoreTileEntityAirLock airLock = (GCCoreTileEntityAirLock) te;

    		if (airLock.index == 0 || airLock.index == 1)
    		{
    			var1 = airLock.orientation == 0 ? 1 : 0;
    			var2 = airLock.orientation;
    			
    			if (par1World.isBlockGettingPowered(x - var1, y, z - var2) || par1World.isBlockGettingPowered(x + var1, y, z + var2))
    			{
    				return false;
    			}
    		}
    	}
		
		if (par1World.isBlockIndirectlyGettingPowered(x, y, z) || par1World.isBlockGettingPowered(x, y, z))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
    
	@Override
    public String getTextureFile()
    {
    	return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
    }
}
