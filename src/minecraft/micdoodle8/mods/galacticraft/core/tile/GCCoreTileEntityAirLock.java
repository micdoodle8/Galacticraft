package micdoodle8.mods.galacticraft.core.tile;

import java.util.HashSet;
import java.util.Set;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.FMLLog;

public class GCCoreTileEntityAirLock extends GCCoreTileEntityAdvanced
{
	public Set<GCCoreTileEntityAirLock> otherAirLockBlocks = new HashSet<GCCoreTileEntityAirLock>();
	public boolean active;
	
	@Override
	public void onTileEntityCreation() 
	{
	}

    public void updateEntity() 
    {
    	super.updateEntity();
    	
    	this.updateState();
    	
    	if (this.otherAirLockBlocks.size() == 8)
    	{
    		
    	}
    }
    
    public void updateState()
    {
		if (checkForCompleteSetup())
		{
			for (GCCoreTileEntityAirLock tile : otherAirLockBlocks)
			{
				tile.otherAirLockBlocks = this.otherAirLockBlocks;
			}
		}
    }
	
	public boolean checkForCompleteSetup()
	{
		int var1 = this.xCoord;
		int var2 = this.yCoord + 1;
		int var3 = this.zCoord;

        byte var5 = 0;
        byte var6 = 0;

        if (this.worldObj.getBlockId(var1 - 1, var2, var3) == GCCoreBlocks.airLockFrame.blockID || this.worldObj.getBlockId(var1 + 1, var2, var3) == GCCoreBlocks.airLockFrame.blockID)
        {
            var5 = 1;
        }

        if (this.worldObj.getBlockId(var1, var2, var3 - 1) == GCCoreBlocks.airLockFrame.blockID || this.worldObj.getBlockId(var1, var2, var3 + 1) == GCCoreBlocks.airLockFrame.blockID)
        {
            var6 = 1;
        }

        if (var5 == var6)
        {
            return false;
        }
        else
        {
            if (this.worldObj.getBlockId(var1 - var5, var2, var3 - var6) == 0)
            {
                var1 -= var5;
                var3 -= var6;
            }

            int var7;
            int var8;

            for (var7 = -1; var7 <= 2; ++var7)
            {
                for (var8 = -1; var8 <= 2; ++var8)
                {
                    boolean var9 = var7 == -1 || var7 == 2 || var8 == -1 || var8 == 2;

                    if (var7 != -1 && var7 != 2 || var8 != -1 && var8 != 2)
                    {
                        int var10 = this.worldObj.getBlockId(var1 + var5 * var7, var2 + var8, var3 + var6 * var7);
                    	TileEntity tile = this.worldObj.getBlockTileEntity(var1 + var5 * var7, var2 + var8, var3 + var6 * var7);

                        if (var9)
                        {
                            if (!(tile instanceof GCCoreTileEntityAirLock))
                            {
                            	return false;
                            }
                        }
                        else if (var10 != 0 && var10 != GCCoreBlocks.airLockSeal.blockID)
                        {
                            return false;
                        }
                    }
                }
            }

            for (var7 = -1; var7 <= 2; ++var7)
            {
                for (var8 = -1; var8 <= 2; ++var8)
                {
                    if (var7 != -1 && var7 != 2 || var8 != -1 && var8 != 2)
                    {
                        if (var7 != -1 && var7 != 2 || var8 != -1 && var8 != 2)
                        {
                        	TileEntity tile = this.worldObj.getBlockTileEntity(var1 + var5 * var7, var2 + var8, var3 + var6 * var7);
                        	
                        	if (tile != null && tile instanceof GCCoreTileEntityAirLock)
                        	{
                            	this.otherAirLockBlocks.add((GCCoreTileEntityAirLock) tile);
                        	}
                        }
                    }
                }
            }
                
            if (areAnyInSetActive() && !this.worldObj.isRemote)
            {
                this.worldObj.editingBlocks = true;
                
                boolean changed = false;

                for (var7 = 0; var7 < 2; ++var7)
                {
                    for (var8 = 0; var8 < 2; ++var8)
                    {
                    	if (this.worldObj.getBlockId(var1 + var5 * var7, var2 + var8, var3 + var6 * var7) != GCCoreBlocks.airLockSeal.blockID)
                    	{
                    		changed = true;
                    	}
                    	
                        this.worldObj.setBlockWithNotify(var1 + var5 * var7, var2 + var8, var3 + var6 * var7, GCCoreBlocks.airLockSeal.blockID);
                    }
                }
                
                if (changed)
                {
                	this.worldObj.playSoundEffect(var1 + var5, var2, var3 + var6 * var7, "player.openairlock", 1.0F, 1.0F);
                }

                this.worldObj.editingBlocks = false;
                return true;
            }
            else if (!this.worldObj.isRemote)
            {
                this.worldObj.editingBlocks = true;
                
                boolean changed = false;

                for (var7 = 0; var7 < 2; ++var7)
                {
                    for (var8 = 0; var8 < 2; ++var8)
                    {
                    	if (this.worldObj.getBlockId(var1 + var5 * var7, var2 + var8, var3 + var6 * var7) != 0)
                    	{
                    		changed = true;
                    	}
                    	
                        this.worldObj.setBlockWithNotify(var1 + var5 * var7, var2 + var8, var3 + var6 * var7, 0);
                    }
                }
                
                if (changed)
                {
                	this.worldObj.playSoundEffect(var1 + var5, var2, var3 + var6 * var7, "player.closeairlock", 1.0F, 1.0F);
                }

                this.worldObj.editingBlocks = false;
                return false;
            }
        }
        
        return false;
    }
	
	private boolean isAirLockBlock(int x, int y, int z)
	{
		if (this.xCoord == x && this.yCoord == y && this.zCoord == z)
		{
			return true;
		}
		
		TileEntity te = this.worldObj.getBlockTileEntity(x, y, z);
		
		if (te != null && te instanceof GCCoreTileEntityAirLock)
		{
			return true;
		}
		
		return false;
	}
	
	private boolean areAnyInSetActive()
	{
		int numberActive = 0;
		
		if (this.otherAirLockBlocks != null)
		{
			for (GCCoreTileEntityAirLock tile : this.otherAirLockBlocks)
			{
				if (tile.active)
				{
					numberActive++;
				}
			}
		}
		else
		{
			return false;
		}
		
		if (numberActive == 0)
		{
			return false;
		}
		else if (numberActive % 2 == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
