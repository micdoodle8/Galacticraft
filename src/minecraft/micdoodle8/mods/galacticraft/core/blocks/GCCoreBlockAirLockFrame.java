package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAirLock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockAirLockFrame extends BlockContainer
{
    @SideOnly(Side.CLIENT)
    private Icon[] airLockIcons;

	public GCCoreBlockAirLockFrame(int par1)
	{
		super(par1, Material.rock);
	}

	@Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
//    	int airLocksAround = 0;
//
//    	for(final ForgeDirection orientation : ForgeDirection.values())
//    	{
//    		if(orientation != ForgeDirection.UNKNOWN)
//    		{
//    			final Vector3 vec = new Vector3(par2, par3, par4);
//
//    			final Vector3 vec2 = vec.clone().modifyPositionFromSide(orientation);
//
//    			final TileEntity tilePos = vec2.getTileEntity(par1World);
//
//    			if (tilePos != null && tilePos instanceof GCCoreTileEntityAirLock)
//    			{
//        	    	for(final ForgeDirection orientation2 : ForgeDirection.values())
//        	    	{
//        	    		if(orientation2 != ForgeDirection.UNKNOWN)
//        	    		{
//        	    			final TileEntity tilePos2 = vec2.clone().modifyPositionFromSide(orientation2).getTileEntity(par1World);
//
//        	    			if (tilePos2 != null && tilePos2 instanceof GCCoreTileEntityAirLock)
//        	    			{
//        	    				airLocksAround++;
//        	    			}
//        	    		}
//        	    	}
//    			}
//    		}
//    	}
//
//    	if (airLocksAround > 0)
//    	{
//    		return false;
//    	}

    	return true;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.airLockIcons = new Icon[6];
        this.airLockIcons[0] = par1IconRegister.registerIcon("galacticraftcore:airlock_off");
        this.airLockIcons[1] = par1IconRegister.registerIcon("galacticraftcore:airlock_on_1");
        this.airLockIcons[2] = par1IconRegister.registerIcon("galacticraftcore:airlock_on_2");
        this.airLockIcons[3] = par1IconRegister.registerIcon("galacticraftcore:airlock_on_3");
        this.airLockIcons[4] = par1IconRegister.registerIcon("galacticraftcore:airlock_on_4");
        this.airLockIcons[5] = par1IconRegister.registerIcon("galacticraftcore:airlock_on_5");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
    	return this.airLockIcons[0];
    }

    @Override
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int side)
    {
    	for(final ForgeDirection orientation : ForgeDirection.values())
		{
			if(orientation != ForgeDirection.UNKNOWN)
			{
				final Vector3 vector = new Vector3(par2, par3, par4);
				Vector3 blockVec = this.modifyPositionFromSide(vector.clone(), orientation, 1);
				Block connection = Block.blocksList[blockVec.getBlockID(par1IBlockAccess)];

				if (connection != null && connection.equals(GCCoreBlocks.airLockSeal))
				{
					if (orientation.offsetY == -1)
					{
						if (side == 0)
						{
							return this.airLockIcons[1];
						}
						else if (side == 1)
						{
							return this.airLockIcons[0];
						}
						else
						{
							return this.airLockIcons[2];
						}
					}
					else if (orientation.offsetY == 1)
					{
						if (side == 0)
						{
							return this.airLockIcons[0];
						}
						else if (side == 1)
						{
							return this.airLockIcons[1];
						}
						else
						{
							return this.airLockIcons[3];
						}
					}
					else if (orientation.ordinal() == side)
					{
						if (side == 0)
						{
							return this.airLockIcons[0];
						}
						else if (side == 1)
						{
							return this.airLockIcons[1];
						}
						else
						{
							return this.airLockIcons[3];
						}
					}
					else if (orientation.getOpposite().ordinal() == side)
					{
						return this.airLockIcons[0];
					}

					blockVec = vector.clone().add(new Vector3(orientation.offsetX, orientation.offsetY, orientation.offsetZ));
					connection = Block.blocksList[blockVec.getBlockID(par1IBlockAccess)];

					if (connection != null && connection.equals(GCCoreBlocks.airLockSeal))
					{
						if (orientation.offsetX == 1)
						{
							if (side == 0)
							{
								return this.airLockIcons[4];
							}
							else if (side == 1)
							{
								return this.airLockIcons[4];
							}
							else if (side == 3)
							{
								return this.airLockIcons[4];
							}
							else if (side == 2)
							{
								return this.airLockIcons[5];
							}
						}
						else if (orientation.offsetX == -1)
						{
							if (side == 0)
							{
								return this.airLockIcons[5];
							}
							else if (side == 1)
							{
								return this.airLockIcons[5];
							}
							else if (side == 3)
							{
								return this.airLockIcons[5];
							}
							else if (side == 2)
							{
								return this.airLockIcons[4];
							}
						}
						else if (orientation.offsetZ == 1)
						{
							if (side == 0)
							{
								return this.airLockIcons[2];
							}
							else if (side == 1)
							{
								return this.airLockIcons[2];
							}
							else if (side == 4)
							{
								return this.airLockIcons[4];
							}
							else if (side == 5)
							{
								return this.airLockIcons[5];
							}
						}
						else if (orientation.offsetZ == -1)
						{
							if (side == 0)
							{
								return this.airLockIcons[3];
							}
							else if (side == 1)
							{
								return this.airLockIcons[3];
							}
							else if (side == 4)
							{
								return this.airLockIcons[5];
							}
							else if (side == 5)
							{
								return this.airLockIcons[4];
							}
						}
					}
				}
			}
		}

    	return this.airLockIcons[0];
    }

	public Vector3 modifyPositionFromSide(Vector3 vec, ForgeDirection side, double amount)
	{
		switch (side.ordinal())
		{
			case 0:
				vec.y -= amount;
				break;
			case 1:
				vec.y += amount;
				break;
			case 2:
				vec.z -= amount;
				break;
			case 3:
				vec.z += amount;
				break;
			case 4:
				vec.x -= amount;
				break;
			case 5:
				vec.x += amount;
				break;
		}
		return vec;
	}

	@Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote)
        {
        	final TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);

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
        	final TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);

        	if (te instanceof GCCoreTileEntityAirLock)
        	{
        		((GCCoreTileEntityAirLock) te).active = this.gettingPowered(par1World, par2, par3, par4);
        	}
        }
    }

	@Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote)
        {
        	final TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);

        	if (te instanceof GCCoreTileEntityAirLock)
        	{
        		((GCCoreTileEntityAirLock) te).active = this.gettingPowered(par1World, par2, par3, par4);
        	}
        }
    }

//	@Override
//    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
//    {
//    	final TileEntity te = par1World.getBlockTileEntity(par3, par4, par5);
//
//    	if (te instanceof GCCoreTileEntityAirLock)
//    	{
//    		((GCCoreTileEntityAirLock) te).updateState();
//    	}
//
//    	return par9;
//    }

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
		final int var1;
		final int var2;
    	final TileEntity te = par1World.getBlockTileEntity(x, y, z);

//    	if (te instanceof GCCoreTileEntityAirLock)
//    	{
//    		final GCCoreTileEntityAirLock airLock = (GCCoreTileEntityAirLock) te;
//
//    		if (airLock.index == 0 || airLock.index == 1)
//    		{
//    			var1 = airLock.orientation == 0 ? 1 : 0;
//    			var2 = airLock.orientation;
//
//    			if (par1World.isBlockIndirectlyGettingPowered(x - var1, y, z - var2) || par1World.isBlockIndirectlyGettingPowered(x + var1, y, z + var2))
//    			{
//    				return false;
//    			}
//    		}
//    	}

		if (par1World.isBlockIndirectlyGettingPowered(x, y, z) || par1World.isBlockIndirectlyGettingPowered(x, y, z))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

//	private boolean gettingOxygen(World par1World, int x, int y, int z)
//	{
//		int var1;
//		int var2;
//    	final TileEntity te = par1World.getBlockTileEntity(x, y, z);
//
//    	if (te instanceof GCCoreTileEntityAirLock)
//    	{
//    		final GCCoreTileEntityAirLock airLock = (GCCoreTileEntityAirLock) te;
//
//    		if (airLock.index == 0 || airLock.index == 1)
//    		{
//    			var1 = airLock.orientation == 0 ? 1 : 0;
//    			var2 = airLock.orientation;
//
//    			if (OxygenUtil.isBlockGettingOxygen(par1World, x - var1, y, z - var2) || OxygenUtil.isBlockGettingOxygen(par1World, x + var1, y, z + var2))
//    			{
//    				return false;
//    			}
//    		}
//    	}
//
//		if (OxygenUtil.isBlockGettingOxygen(par1World, x, y, z))
//		{
//			return true;
//		}
//		else
//		{
//			return false;
//		}
//	}
}
