package micdoodle8.mods.galacticraft.core.tile;

import java.util.HashSet;
import java.util.Set;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class GCCoreTileEntityAirLock extends GCCoreTileEntityAdvanced
{
	public Set<GCCoreTileEntityAirLock> otherAirLockBlocks = new HashSet<GCCoreTileEntityAirLock>();
	public boolean active;
	public boolean oxygenActive;
	public int index;
	public int orientation;

	@Override
	public void onTileEntityCreation()
	{
	}

    @Override
	public void updateEntity()
    {
    	super.updateEntity();

    	this.updateState();
    }

    public void updateState()
    {
		if (this.checkForCompleteSetup())
		{
			for (final GCCoreTileEntityAirLock tile : this.otherAirLockBlocks)
			{
				tile.otherAirLockBlocks = this.otherAirLockBlocks;
			}
		}
    }

	public boolean checkForCompleteSetup()
	{
		if (this.otherAirLockBlocks.size() > 8)
		{
			return false;
		}

		int var1 = this.xCoord;
		final int var2 = this.yCoord + 1;
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
                    final boolean var9 = var7 == -1 || var7 == 2 || var8 == -1 || var8 == 2;

                    if (var7 != -1 && var7 != 2 || var8 != -1 && var8 != 2)
                    {
                        final int var10 = this.worldObj.getBlockId(var1 + var5 * var7, var2 + var8, var3 + var6 * var7);
                    	final TileEntity tile = this.worldObj.getBlockTileEntity(var1 + var5 * var7, var2 + var8, var3 + var6 * var7);

                        if (var9)
                        {
                            if (!(tile instanceof GCCoreTileEntityAirLock))
                            {
                            	return false;
                            }
                        }
                        else if (var10 != 0 && var10 != GCCoreBlocks.airLockSeal.blockID && var10 != GCCoreBlocks.breatheableAir.blockID)
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
                        	final TileEntity tile = this.worldObj.getBlockTileEntity(var1 + var5 * var7, var2 + var8, var3 + var6 * var7);

                        	if (tile != null && tile instanceof GCCoreTileEntityAirLock)
                        	{
                            	this.otherAirLockBlocks.add((GCCoreTileEntityAirLock) tile);

                            	if (var7 == 0 && var8 == -1)
                            	{
                            		((GCCoreTileEntityAirLock) tile).index = 0;
                            	}
                            	else if (var7 == 1 && var8 == -1)
                            	{
                            		((GCCoreTileEntityAirLock) tile).index = 1;
                            	}
                            	else if (var7 == 2 && var8 == 0)
                            	{
                            		((GCCoreTileEntityAirLock) tile).index = 2;
                            	}
                            	else if (var7 == 2 && var8 == 1)
                            	{
                            		((GCCoreTileEntityAirLock) tile).index = 3;
                            	}
                            	else if (var7 == 1 && var8 == 2)
                            	{
                            		((GCCoreTileEntityAirLock) tile).index = 4;
                            	}
                            	else if (var7 == 0 && var8 == 2)
                            	{
                            		((GCCoreTileEntityAirLock) tile).index = 5;
                            	}
                            	else if (var7 == -1 && var8 == 1)
                            	{
                            		((GCCoreTileEntityAirLock) tile).index = 6;
                            	}
                            	else if (var7 == -1 && var8 == 0)
                            	{
                            		((GCCoreTileEntityAirLock) tile).index = 7;
                            	}
                            	else
                            	{
                            		((GCCoreTileEntityAirLock) tile).index = -1;
                            	}

                            	((GCCoreTileEntityAirLock) tile).orientation = var5 == 1 ? 0 : 1;
                        	}
                        }
                    }
                }
            }

            if (this.areAnyInSetActive() && !this.worldObj.isRemote)
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
                    	final Block block = Block.blocksList[this.worldObj.getBlockId(var1 + var5 * var7, var2 + var8, var3 + var6 * var7)];

                    	if (block != null && !block.isAirBlock(this.worldObj, var1 + var5 * var7, var2 + var8, var3 + var6 * var7))
                    	{
                    		changed = true;
                    	}

                    	if (!this.areAnyInSetOxygenActive())
                    	{
                            this.worldObj.setBlockWithNotify(var1 + var5 * var7, var2 + var8, var3 + var6 * var7, 0);
                    	}
                    	else
                    	{
                            this.worldObj.setBlockWithNotify(var1 + var5 * var7, var2 + var8, var3 + var6 * var7, GCCoreBlocks.breatheableAir.blockID);
                    	}
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

		final TileEntity te = this.worldObj.getBlockTileEntity(x, y, z);

		if (te != null && te instanceof GCCoreTileEntityAirLock)
		{
			return true;
		}

		return false;
	}

	public boolean areAnyInSetActive()
	{
		int numberActive = 0;

		if (this.otherAirLockBlocks != null)
		{
			for (final GCCoreTileEntityAirLock tile : this.otherAirLockBlocks)
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
		else
		{
			return true;
		}
	}

	public boolean areAnyInSetOxygenActive()
	{
		int numberActive = 0;

		if (this.otherAirLockBlocks != null)
		{
			for (final GCCoreTileEntityAirLock tile : this.otherAirLockBlocks)
			{
				if (tile.oxygenActive)
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
		else
		{
			return true;
		}
	}

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.readFromNBT(par1NBTTagCompound);
    	this.active = par1NBTTagCompound.getBoolean("active");
    	this.oxygenActive = par1NBTTagCompound.getBoolean("oxygenActive");
    	this.index = par1NBTTagCompound.getInteger("index");
    	this.orientation = par1NBTTagCompound.getInteger("orientation");
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.writeToNBT(par1NBTTagCompound);
    	par1NBTTagCompound.setBoolean("oxygenActive", this.oxygenActive);
    	par1NBTTagCompound.setBoolean("active", this.active);
    	par1NBTTagCompound.setInteger("index", this.index);
    	par1NBTTagCompound.setInteger("orientation", this.orientation);
    }
}
