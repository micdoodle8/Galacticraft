package micdoodle8.mods.galacticraft.core.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderOrbit;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityThruster;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
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

import java.util.Random;

public class BlockSpinThruster extends BlockAdvanced implements ItemBlockDesc.IBlockShiftDesc
{
	public static IIcon thrusterIcon;

	protected BlockSpinThruster(String assetName)
	{
		super(Material.circuits);
		this.setHardness(0.1F);
		this.setStepSound(Block.soundTypeWood);
		this.setBlockTextureName("stone");
		this.setBlockName(assetName);
	}

	private static boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection direction, boolean nope)
	{
		return world.getBlock(x, y, z).isSideSolid(world, x, y, z, direction);
	}

	//	@Override
	//	@SideOnly(Side.CLIENT)
	//	public IIcon getIcon(IBlockAccess par1IBlockAccess, int x, int y, int z, int par5)
	//	{
	//		return BlockSpinThruster.thrusterIcon;
	//	}
	//
	//	@Override
	//	@SideOnly(Side.CLIENT)
	//	public IIcon getIcon(int par1, int x)
	//	{
	//		return BlockSpinThruster.thrusterIcon;
	//	}
	//
	//	@Override
	//	@SideOnly(Side.CLIENT)
	//	public void registerBlockIcons(IIconRegister par1IconRegister)
	//	{
	//		BlockSpinThruster.thrusterIcon = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "spinThruster");
	//	}

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
		return BlockSpinThruster.isBlockSolidOnSide(par1World, x - 1, y, z, ForgeDirection.EAST, true) || BlockSpinThruster.isBlockSolidOnSide(par1World, x + 1, y, z, ForgeDirection.WEST, true) || BlockSpinThruster.isBlockSolidOnSide(par1World, x, y, z - 1, ForgeDirection.SOUTH, true) || BlockSpinThruster.isBlockSolidOnSide(par1World, x, y, z + 1, ForgeDirection.NORTH, true);
	}

	@Override
	public int onBlockPlaced(World par1World, int x, int y, int z, int par5, float par6, float par7, float par8, int par9)
	{
		int var10 = par9;

		if (par5 == 2 && BlockSpinThruster.isBlockSolidOnSide(par1World, x, y, z + 1, ForgeDirection.NORTH, true))
		{
			var10 = 4;
		}

		if (par5 == 3 && BlockSpinThruster.isBlockSolidOnSide(par1World, x, y, z - 1, ForgeDirection.SOUTH, true))
		{
			var10 = 3;
		}

		if (par5 == 4 && BlockSpinThruster.isBlockSolidOnSide(par1World, x + 1, y, z, ForgeDirection.WEST, true))
		{
			var10 = 2;
		}

		if (par5 == 5 && BlockSpinThruster.isBlockSolidOnSide(par1World, x - 1, y, z, ForgeDirection.EAST, true))
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
		TileEntityThruster tile = (TileEntityThruster) par1World.getTileEntity(x, y, z);

		if (metadata == 0)
		{
			if (BlockSpinThruster.isBlockSolidOnSide(par1World, x - 1, y, z, ForgeDirection.EAST, true))
			{
				metadata = 1;
				par1World.setBlockMetadataWithNotify(x, y, z, metadata, 3);
			}
			else if (BlockSpinThruster.isBlockSolidOnSide(par1World, x + 1, y, z, ForgeDirection.WEST, true))
			{
				metadata = 2;
				par1World.setBlockMetadataWithNotify(x, y, z, metadata, 3);
			}
			else if (BlockSpinThruster.isBlockSolidOnSide(par1World, x, y, z - 1, ForgeDirection.SOUTH, true))
			{
				metadata = 3;
				par1World.setBlockMetadataWithNotify(x, y, z, metadata, 3);
			}
			else if (BlockSpinThruster.isBlockSolidOnSide(par1World, x, y, z + 1, ForgeDirection.NORTH, true))
			{
				metadata = 4;
				par1World.setBlockMetadataWithNotify(x, y, z, metadata, 3);
			}
		}

		BlockVec3 baseBlock;
		switch (metadata)
		{
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

		if (!par1World.isRemote)
		{
			if (par1World.provider instanceof WorldProviderOrbit)
			{
				((WorldProviderOrbit) par1World.provider).checkSS(baseBlock, true);
			}
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
			final int var6 = par1World.getBlockMetadata(x, y, z) & 7;
			boolean var7 = false;

			if (!BlockSpinThruster.isBlockSolidOnSide(par1World, x - 1, y, z, ForgeDirection.EAST, true) && var6 == 1)
			{
				var7 = true;
			}

			if (!BlockSpinThruster.isBlockSolidOnSide(par1World, x + 1, y, z, ForgeDirection.WEST, true) && var6 == 2)
			{
				var7 = true;
			}

			if (!BlockSpinThruster.isBlockSolidOnSide(par1World, x, y, z - 1, ForgeDirection.SOUTH, true) && var6 == 3)
			{
				var7 = true;
			}

			if (!BlockSpinThruster.isBlockSolidOnSide(par1World, x, y, z + 1, ForgeDirection.NORTH, true) && var6 == 4)
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
	@SideOnly(Side.CLIENT)
	/**
	 * A randomly called display update to be able to add particles or other items for display
	 */
	public void randomDisplayTick(World par1World, int x, int y, int z, Random par5Random)
	{
		//TODO this is torch code as a placeholder, still need to adjust positioning and particle type
		//Also make small thrust sounds
		if (par1World.provider instanceof WorldProviderOrbit)
		{
			if (((WorldProviderOrbit) par1World.provider).thrustersFiring || par5Random.nextInt(80) == 0)
			{
				final int var6 = par1World.getBlockMetadata(x, y, z) & 7;
				final double var7 = x + 0.5F;
				final double var9 = y + 0.7F;
				final double var11 = z + 0.5F;
				final double var13 = 0.2199999988079071D;
				final double var15 = 0.27000001072883606D;

				if (var6 == 1)
				{
					par1World.spawnParticle("smoke", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
				}
				else if (var6 == 2)
				{
					par1World.spawnParticle("smoke", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
				}
				else if (var6 == 3)
				{
					par1World.spawnParticle("smoke", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
				}
				else if (var6 == 4)
				{
					par1World.spawnParticle("smoke", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}

	@Override
	public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		final int metadata = world.getBlockMetadata(x, y, z);
		final int facing = metadata & 8;
		final int change = (8 + metadata) & 15;
		world.setBlockMetadataWithNotify(x, y, z, change, 2);

		if (world.provider instanceof WorldProviderOrbit && !world.isRemote)
		{
			WorldProviderOrbit worldOrbital = (WorldProviderOrbit) world.provider;
			worldOrbital.checkSS(new BlockVec3(x, y, z), true);
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityThruster();
	}

	@Override
	public void onBlockPreDestroy(World world, int x, int y, int z, int metadata)
	{
		if (!world.isRemote)
		{
			final int facing = metadata & 8;
			if (world.provider instanceof WorldProviderOrbit)
			{
				WorldProviderOrbit worldOrbital = (WorldProviderOrbit) world.provider;
				BlockVec3 baseBlock = new BlockVec3(x, y, z);
				worldOrbital.removeThruster(baseBlock, facing == 0);
				worldOrbital.updateSpinSpeed();
			}
		}
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftBlocksTab;
	}

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }
}
