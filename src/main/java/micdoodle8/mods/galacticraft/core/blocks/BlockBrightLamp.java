package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityArclamp;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * GCCoreBlockBrightLamp.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8, radfast
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class BlockBrightLamp extends BlockAdvanced
{
	public static IIcon icon;

	protected BlockBrightLamp(String assetName)
	{
		super(Material.glass);
		this.setHardness(0.1F);
		this.setStepSound(Block.soundTypeWood);
		this.setBlockTextureName("stone");
		this.setBlockName(assetName);
		this.setLightLevel(1.0F);
	}
	
	private static boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection direction, boolean nope)
	{
		return world.getBlock(x, y, z).isSideSolid(world, x, y, z, direction);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int x, int y, int z)
	{
		return null;
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
	public int getRenderType()
	{
		return GalacticraftCore.proxy.getBlockRender(this);
	}

	@Override
	public boolean canPlaceBlockAt(World par1World, int x, int y, int z)
	{
		return isBlockSolidOnSide(par1World, x - 1, y, z, ForgeDirection.EAST, true) 
				|| isBlockSolidOnSide(par1World, x + 1, y, z, ForgeDirection.WEST, true) 
				|| isBlockSolidOnSide(par1World, x, y, z - 1, ForgeDirection.SOUTH, true) 
				|| isBlockSolidOnSide(par1World, x, y, z + 1, ForgeDirection.NORTH, true); 
	}

	@Override
	public int onBlockPlaced(World par1World, int x, int y, int z, int par5, float par6, float par7, float par8, int par9)
	{
		int var10 = par9;

		if (par5 == 2 && isBlockSolidOnSide(par1World, x, y, z + 1, ForgeDirection.NORTH, true))
		{
			var10 = 4;
		}

		if (par5 == 3 && isBlockSolidOnSide(par1World, x, y, z - 1, ForgeDirection.SOUTH, true))
		{
			var10 = 3;
		}

		if (par5 == 4 && isBlockSolidOnSide(par1World, x + 1, y, z, ForgeDirection.WEST, true))
		{
			var10 = 2;
		}

		if (par5 == 5 && isBlockSolidOnSide(par1World, x - 1, y, z, ForgeDirection.EAST, true))
		{
			var10 = 1;
		}

		return 0;
	}

	@Override
	public void updateTick(World par1World, int x, int y, int z, Random par5Random)
	{
		super.updateTick(par1World, x, y, z, par5Random);

		if (par1World.getBlockMetadata(x, y, z) == 0)
		{
			this.onBlockAdded(par1World, x, y, z);
		}
	}

	@Override
	public void onBlockAdded(World par1World, int x, int y, int z)
	{
		int metadata = par1World.getBlockMetadata(x, y, z); 
		
		if (metadata == 0)
		{
			if (isBlockSolidOnSide(par1World, x - 1, y, z, ForgeDirection.EAST, true))
			{
				metadata = 1;
				par1World.setBlockMetadataWithNotify(x, y, z, metadata, 3);
			}
			else if (isBlockSolidOnSide(par1World, x + 1, y, z, ForgeDirection.WEST, true))
			{
				metadata = 2;
				par1World.setBlockMetadataWithNotify(x, y, z, metadata, 3);
			}
			else if (isBlockSolidOnSide(par1World, x, y, z - 1, ForgeDirection.SOUTH, true))
			{
				metadata = 3;
				par1World.setBlockMetadataWithNotify(x, y, z, metadata, 3);
			}
			else if (isBlockSolidOnSide(par1World, x, y, z + 1, ForgeDirection.NORTH, true))
			{
				metadata = 4;
				par1World.setBlockMetadataWithNotify(x, y, z, metadata, 3);
			}
		}

		BlockVec3 baseBlock;
		switch (metadata) {
		case 1:
			baseBlock = new BlockVec3(x - 1, y, z);
			break;
		case 2:
			baseBlock = new BlockVec3(x + 1, y, z);
			break;
		case 3:
			baseBlock = new BlockVec3(x, y, z - 1);
			break;
		case 4:
			baseBlock = new BlockVec3(x, y, z + 1);
			break;
		default:
			this.dropTorchIfCantStay(par1World, x, y, z);
			return;
		}
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z,
	 * neighbor blockID
	 */
	@Override
	public void onNeighborBlockChange(World par1World, int x, int y, int z, Block par5)
	{
		if (this.dropTorchIfCantStay(par1World, x, y, z))
		{
			final int var6 = par1World.getBlockMetadata(x, y, z);
			boolean var7 = false;

			if (!isBlockSolidOnSide(par1World, x - 1, y, z, ForgeDirection.EAST, true) && var6 == 1)
			{
				var7 = true;
			}

			if (!isBlockSolidOnSide(par1World, x + 1, y, z, ForgeDirection.WEST, true) && var6 == 2)
			{
				var7 = true;
			}

			if (!isBlockSolidOnSide(par1World, x, y, z - 1, ForgeDirection.SOUTH, true) && var6 == 3)
			{
				var7 = true;
			}

			if (!isBlockSolidOnSide(par1World, x, y, z + 1, ForgeDirection.NORTH, true) && var6 == 4)
			{
				var7 = true;
			}

			if (var7)
			{
				this.dropBlockAsItem(par1World, x, y, z, par1World.getBlockMetadata(x, y, z), 0);
				par1World.setBlock(x, y, z, Blocks.air);
			}
		}
	}

	/**
	 * Tests if the block can remain at its current location and will drop as an
	 * item if it is unable to stay. Returns True if it can stay and False if it
	 * drops. Args: world, x, y, z
	 */
	private boolean dropTorchIfCantStay(World par1World, int x, int y, int z)
	{
		if (!this.canPlaceBlockAt(par1World, x, y, z))
		{
			if (par1World.getBlock(x, y, z) == this)
			{
				this.dropBlockAsItem(par1World, x, y, z, par1World.getBlockMetadata(x, y, z), 0);
				par1World.setBlock(x, y, z, Blocks.air);
			}

			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * Ray traces through the blocks collision from start vector to end vector
	 * returning a ray trace hit. Args: world, x, y, z, startVec, endVec
	 */
	@Override
	public MovingObjectPosition collisionRayTrace(World par1World, int x, int y, int z, Vec3 par5Vec3, Vec3 par6Vec3)
	{
		final int var7 = par1World.getBlockMetadata(x, y, z) & 7;
		float var8 = 0.3F;

		if (var7 == 1)
		{
			this.setBlockBounds(0.0F, 0.2F, 0.5F - var8, var8 * 2.0F, 0.8F, 0.5F + var8);
		}
		else if (var7 == 2)
		{
			this.setBlockBounds(1.0F - var8 * 2.0F, 0.2F, 0.5F - var8, 1.0F, 0.8F, 0.5F + var8);
		}
		else if (var7 == 3)
		{
			this.setBlockBounds(0.5F - var8, 0.2F, 0.0F, 0.5F + var8, 0.8F, var8 * 2.0F);
		}
		else if (var7 == 4)
		{
			this.setBlockBounds(0.5F - var8, 0.2F, 1.0F - var8 * 2.0F, 0.5F + var8, 0.8F, 1.0F);
		}
		else
		{
			var8 = 0.1F;
			this.setBlockBounds(0.5F - var8, 0.0F, 0.5F - var8, 0.5F + var8, 0.6F, 0.5F + var8);
		}

		return super.collisionRayTrace(par1World, x, y, z, par5Vec3, par6Vec3);
	}

	@Override
	public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		//TODO - make sure throughout that Metadata bits 0,1 govern placement (side), bit 3 orientation
		final int metadata = world.getBlockMetadata(x, y, z);
		final int facing = metadata & 8;
		final int change = facing ^ 8;

		world.setBlockMetadataWithNotify(x, y, z, change, 3);
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) 
	{
		return new TileEntityArclamp();
	}
	
	@Override
	public void onBlockPreDestroy(World world, int x, int y, int z, int metadata)
	{
		if (!world.isRemote)
		{
			final int facing = metadata & 8;
		}
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftTab;
	}
}
