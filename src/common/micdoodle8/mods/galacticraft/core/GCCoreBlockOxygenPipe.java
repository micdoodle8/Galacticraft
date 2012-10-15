package micdoodle8.mods.galacticraft.core;

import java.util.List;
import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class GCCoreBlockOxygenPipe extends BlockContainer
{
	private float oxygenPipeMin = 0.4F;
	private float oxygenPipeMax = 0.6F;
	
	public GCCoreBlockOxygenPipe(int i, int j) 
	{
		super(i, j, Material.glass);
	}

	@Override
	public int getRenderType() 
	{
		return GalacticraftCore.proxy.getGCOxygenPipeRenderID();
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() 
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) 
	{
		return null;
	}
	
    public void onBlockAdded(World par1World, int par2, int par3, int par4) 
    {
    	par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate());
    }
	
	@Override
    public int tickRate()
    {
        return 5;
    }
	
	@Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
		int pipesActiveAndNearby = 0;
		
		int[] idSet = new int[6];
		int[] metaSet = new int[6];
		
		idSet[0] = par1World.getBlockId(par2 + 1, par3, par4);
		idSet[1] = par1World.getBlockId(par2 - 1, par3, par4);
		idSet[2] = par1World.getBlockId(par2, par3, par4 + 1);
		idSet[3] = par1World.getBlockId(par2, par3, par4 - 1);
		idSet[4] = par1World.getBlockId(par2, par3 + 1, par4);
		idSet[5] = par1World.getBlockId(par2, par3 - 1, par4);
		metaSet[0] = par1World.getBlockMetadata(par2 + 1, par3, par4);
		metaSet[1] = par1World.getBlockMetadata(par2 - 1, par3, par4);
		metaSet[2] = par1World.getBlockMetadata(par2, par3, par4 + 1);
		metaSet[3] = par1World.getBlockMetadata(par2, par3, par4 - 1);
		metaSet[4] = par1World.getBlockMetadata(par2, par3 + 1, par4);
		metaSet[5] = par1World.getBlockMetadata(par2, par3 - 1, par4);
		
		for (int i = 0; i < idSet.length; i++)
		{
			if (idSet[i] == GCCoreBlocks.oxygenPipe.blockID && metaSet[i] == 1)
			{
				pipesActiveAndNearby += 1;
			}
		}
		
		if (pipesActiveAndNearby > 0)
		{
			par1World.setBlockMetadata(par2, par3, par4, 1);
		}
		else
		{
			par1World.setBlockMetadata(par2, par3, par4, 0);
		}
		
		par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, 5);
    }
	
	@Override
	public int getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
    {
		return this.blockIndexInTexture;
//		if (world.getBlockId(x + 1, y, z) == this.blockID && world.getBlockId(x, y, z + 1) == this.blockID
//				|| world.getBlockId(x + 1, y, z) == this.blockID && world.getBlockId(x, y, z - 1) == this.blockID
//				|| world.getBlockId(x - 1, y, z) == this.blockID && world.getBlockId(x, y, z + 1) == this.blockID
//				|| world.getBlockId(x - 1, y, z) == this.blockID && world.getBlockId(x, y, z - 1) == this.blockID
//				|| world.getBlockId(x - 1, y, z) == this.blockID && world.getBlockId(x, y - 1, z) == this.blockID
//				|| world.getBlockId(x - 1, y, z) == this.blockID && world.getBlockId(x, y + 1, z) == this.blockID
//				|| world.getBlockId(x + 1, y, z) == this.blockID && world.getBlockId(x, y - 1, z) == this.blockID
//				|| world.getBlockId(x + 1, y, z) == this.blockID && world.getBlockId(x, y + 1, z) == this.blockID
//				|| world.getBlockId(x, y, z - 1) == this.blockID && world.getBlockId(x, y - 1, z) == this.blockID
//				|| world.getBlockId(x, y, z - 1) == this.blockID && world.getBlockId(x, y + 1, z) == this.blockID
//				|| world.getBlockId(x, y, z + 1) == this.blockID && world.getBlockId(x, y - 1, z) == this.blockID
//				|| world.getBlockId(x, y, z + 1) == this.blockID && world.getBlockId(x, y + 1, z) == this.blockID)
//		{
//			return this.blockIndexInTexture - 1;
//		}
//		else
//		{
//			if (side != 0 && side != 1 && (world.getBlockId(x, y + 1, z) == this.blockID) && (world.getBlockId(x, y - 1, z) == this.blockID))
//			{
//				return this.blockIndexInTexture + 1;
//			}
//			
//			if (side == 0 && (world.getBlockId(x, y - 1, z) == 0) && (world.getBlockId(x, y + 1, z) == this.blockID))
//			{
//				return this.blockIndexInTexture - 1;
//			}
//			
//			if (side == 1 && (world.getBlockId(x, y + 1, z) == 0) && (world.getBlockId(x, y - 1, z) == this.blockID))
//			{
//				return this.blockIndexInTexture - 1;
//			}
//			
//			if (side == 2 && world.getBlockId(x, y, z - 1) == this.blockID)
//			{
//				return 25;
//			}
//			else if (side == 2 && world.getBlockId(x, y, z - 1) == 0 && world.getBlockId(x + 1, y, z) == 0 && world.getBlockId(x - 1, y, z) == 0)
//			{
//				return this.blockIndexInTexture - 1;
//			}
//			
//			if (side == 3 && world.getBlockId(x, y, z + 1) == this.blockID)
//			{
//				return 25;
//			}
//			else if (side == 3 && world.getBlockId(x, y, z + 1) == 0 && world.getBlockId(x + 1, y, z) == 0 && world.getBlockId(x - 1, y, z) == 0)
//			{
//				return this.blockIndexInTexture - 1;
//			}
//			
//			if (side == 4 && world.getBlockId(x - 1, y, z) == this.blockID)
//			{
//				return 25;
//			}
//			else if (side == 4 && world.getBlockId(x - 1, y, z) == 0 && world.getBlockId(x, y, z + 1) == 0 && world.getBlockId(x, y, z - 1) == 0)
//			{
//				return this.blockIndexInTexture - 1;
//			}
//			
//			if (side == 5 && world.getBlockId(x + 1, y, z) == this.blockID)
//			{
//				return 25;
//			}
//			else if (side == 5 && world.getBlockId(x + 1, y, z) == 0 && world.getBlockId(x, y, z + 1) == 0 && world.getBlockId(x, y, z - 1) == 0)
//			{
//				return this.blockIndexInTexture - 1;
//			}
//			
//			if ((side == 0 || side == 1) && (world.getBlockId(x, y, z + 1) == this.blockID || world.getBlockId(x, y, z - 1) == this.blockID))
//			{
//				return this.blockIndexInTexture + 1;
//			}
//			
//			return this.blockIndexInTexture;
//		}
    }
	
	@Override
	public void addCollidingBlockToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity) 
	{
		setBlockBounds(this.oxygenPipeMin, this.oxygenPipeMin, this.oxygenPipeMin, this.oxygenPipeMax, this.oxygenPipeMax, this.oxygenPipeMax);
		super.addCollidingBlockToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);

		TileEntity tile1 = world.getBlockTileEntity(i, j, k);

		if (world.getBlockId(i - 1, j, k) == this.blockID || world.getBlockId(i - 1, j, k) == GCCoreBlocks.airDistributor.blockID || world.getBlockId(i - 1, j, k) == GCCoreBlocks.airDistributorActive.blockID || world.getBlockId(i - 1, j, k) == GCCoreBlocks.blockAirCollector.blockID)
		{
			setBlockBounds(0.0F, this.oxygenPipeMin, this.oxygenPipeMin, this.oxygenPipeMax, this.oxygenPipeMax, this.oxygenPipeMax);
			super.addCollidingBlockToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
		}

		if (world.getBlockId(i + 1, j, k) == this.blockID || world.getBlockId(i + 1, j, k) == GCCoreBlocks.airDistributor.blockID || world.getBlockId(i + 1, j, k) == GCCoreBlocks.airDistributorActive.blockID || world.getBlockId(i + 1, j, k) == GCCoreBlocks.blockAirCollector.blockID)
		{
			setBlockBounds(this.oxygenPipeMin, this.oxygenPipeMin, this.oxygenPipeMin, 1.0F, this.oxygenPipeMax, this.oxygenPipeMax);
			super.addCollidingBlockToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
		}

		if (world.getBlockId(i, j - 1, k) == this.blockID || world.getBlockId(i, j - 1, k) == GCCoreBlocks.airDistributor.blockID || world.getBlockId(i, j - 1, k) == GCCoreBlocks.airDistributorActive.blockID || world.getBlockId(i, j - 1, k) == GCCoreBlocks.blockAirCollector.blockID)
		{
			setBlockBounds(this.oxygenPipeMin, 0.0F, this.oxygenPipeMin, this.oxygenPipeMax, this.oxygenPipeMax, this.oxygenPipeMax);
			super.addCollidingBlockToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
		}

		if (world.getBlockId(i, j + 1, k) == this.blockID || world.getBlockId(i, j + 1, k) == GCCoreBlocks.airDistributor.blockID || world.getBlockId(i, j + 1, k) == GCCoreBlocks.airDistributorActive.blockID || world.getBlockId(i, j + 1, k) == GCCoreBlocks.blockAirCollector.blockID)
		{
			setBlockBounds(this.oxygenPipeMin, this.oxygenPipeMin, this.oxygenPipeMin, this.oxygenPipeMax, 1.0F, this.oxygenPipeMax);
			super.addCollidingBlockToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
		}

		if (world.getBlockId(i, j, k - 1) == this.blockID || world.getBlockId(i, j, k - 1) == GCCoreBlocks.airDistributor.blockID || world.getBlockId(i, j, k - 1) == GCCoreBlocks.airDistributorActive.blockID || world.getBlockId(i, j, k - 1) == GCCoreBlocks.blockAirCollector.blockID)
		{
			setBlockBounds(this.oxygenPipeMin, this.oxygenPipeMin, 0.0F, this.oxygenPipeMax, this.oxygenPipeMax, this.oxygenPipeMax);
			super.addCollidingBlockToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
		}

		if (world.getBlockId(i, j, k + 1) == this.blockID || world.getBlockId(i, j, k + 1) == GCCoreBlocks.airDistributor.blockID || world.getBlockId(i, j, k + 1) == GCCoreBlocks.airDistributorActive.blockID || world.getBlockId(i, j, k + 1) == GCCoreBlocks.blockAirCollector.blockID)
		{
			setBlockBounds(this.oxygenPipeMin, this.oxygenPipeMin, this.oxygenPipeMin, this.oxygenPipeMax, this.oxygenPipeMax, 1.0F);
			super.addCollidingBlockToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
		}

		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) 
	{
		float xMin = this.oxygenPipeMin, xMax = this.oxygenPipeMax, yMin = this.oxygenPipeMin, yMax = this.oxygenPipeMax, zMin = this.oxygenPipeMin, zMax = this.oxygenPipeMax;

		if (world.getBlockId(i - 1, j, k) == this.blockID || world.getBlockId(i - 1, j, k) == GCCoreBlocks.airDistributor.blockID || world.getBlockId(i - 1, j, k) == GCCoreBlocks.airDistributorActive.blockID || world.getBlockId(i - 1, j, k) == GCCoreBlocks.blockAirCollector.blockID)
			xMin = 0.0F;

		if (world.getBlockId(i + 1, j, k) == this.blockID || world.getBlockId(i + 1, j, k) == GCCoreBlocks.airDistributor.blockID || world.getBlockId(i + 1, j, k) == GCCoreBlocks.airDistributorActive.blockID || world.getBlockId(i + 1, j, k) == GCCoreBlocks.blockAirCollector.blockID)
			xMax = 1.0F;

		if (world.getBlockId(i, j - 1, k) == this.blockID || world.getBlockId(i, j - 1, k) == GCCoreBlocks.airDistributor.blockID || world.getBlockId(i, j - 1, k) == GCCoreBlocks.airDistributorActive.blockID || world.getBlockId(i, j - 1, k) == GCCoreBlocks.blockAirCollector.blockID)
			yMin = 0.0F;

		if (world.getBlockId(i, j + 1, k) == this.blockID || world.getBlockId(i, j + 1, k) == GCCoreBlocks.airDistributor.blockID || world.getBlockId(i, j + 1, k) == GCCoreBlocks.airDistributorActive.blockID || world.getBlockId(i, j + 1, k) == GCCoreBlocks.blockAirCollector.blockID)
			yMax = 1.0F;

		if (world.getBlockId(i, j, k - 1) == this.blockID || world.getBlockId(i, j, k - 1) == GCCoreBlocks.airDistributor.blockID || world.getBlockId(i, j, k - 1) == GCCoreBlocks.airDistributorActive.blockID || world.getBlockId(i, j, k - 1) == GCCoreBlocks.blockAirCollector.blockID)
			zMin = 0.0F;

		if (world.getBlockId(i, j, k + 1) == this.blockID || world.getBlockId(i, j, k + 1) == GCCoreBlocks.airDistributor.blockID || world.getBlockId(i, j, k + 1) == GCCoreBlocks.airDistributorActive.blockID || world.getBlockId(i, j, k + 1) == GCCoreBlocks.blockAirCollector.blockID)
			zMax = 1.0F;

		return AxisAlignedBB.getBoundingBox((double) i + xMin, (double) j + yMin, (double) k + zMin, (double) i + xMax, (double) j + yMax, (double) k + zMax);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) 
	{
		return getCollisionBoundingBoxFromPool(world, i, j, k);
	}
	
	@Override
	public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 vec3d, Vec3 vec3d1) 
	{
		float xMin = this.oxygenPipeMin, xMax = this.oxygenPipeMax, yMin = this.oxygenPipeMin, yMax = this.oxygenPipeMax, zMin = this.oxygenPipeMin, zMax = this.oxygenPipeMax;

		if (world.getBlockId(i - 1, j, k) == this.blockID || world.getBlockId(i - 1, j, k) == GCCoreBlocks.airDistributor.blockID || world.getBlockId(i - 1, j, k) == GCCoreBlocks.airDistributorActive.blockID)
			xMin = 0.0F;

		if (world.getBlockId(i + 1, j, k) == this.blockID || world.getBlockId(i + 1, j, k) == GCCoreBlocks.airDistributor.blockID || world.getBlockId(i + 1, j, k) == GCCoreBlocks.airDistributorActive.blockID)
			xMax = 1.0F;

		if (world.getBlockId(i, j - 1, k) == this.blockID || world.getBlockId(i, j - 1, k) == GCCoreBlocks.airDistributor.blockID || world.getBlockId(i, j - 1, k) == GCCoreBlocks.airDistributorActive.blockID)
			yMin = 0.0F;

		if (world.getBlockId(i, j + 1, k) == this.blockID || world.getBlockId(i, j + 1, k) == GCCoreBlocks.airDistributor.blockID || world.getBlockId(i, j + 1, k) == GCCoreBlocks.airDistributorActive.blockID)
			yMax = 1.0F;

		if (world.getBlockId(i, j, k - 1) == this.blockID || world.getBlockId(i, j, k - 1) == GCCoreBlocks.airDistributor.blockID || world.getBlockId(i, j, k - 1) == GCCoreBlocks.airDistributorActive.blockID)
			zMin = 0.0F;

		if (world.getBlockId(i, j, k + 1) == this.blockID || world.getBlockId(i, j, k + 1) == GCCoreBlocks.airDistributor.blockID || world.getBlockId(i, j, k + 1) == GCCoreBlocks.airDistributorActive.blockID)
			zMax = 1.0F;

		setBlockBounds(xMin, yMin, zMin, xMax, yMax, zMax);

		MovingObjectPosition r = super.collisionRayTrace(world, i, j, k, vec3d, vec3d1);

		setBlockBounds(0, 0, 0, 1, 1, 1);

		return r;
	}

	@Override
    public String getTextureFile()
    {
    	return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
    }
}
