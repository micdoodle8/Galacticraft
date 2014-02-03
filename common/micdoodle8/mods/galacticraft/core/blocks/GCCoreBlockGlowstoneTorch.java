package micdoodle8.mods.galacticraft.core.blocks;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.WEST;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * GCCoreBlockGlowstoneTorch.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockGlowstoneTorch extends Block
{
	protected GCCoreBlockGlowstoneTorch(int id, String assetName)
	{
		super(id, Material.circuits);
		this.setTickRandomly(true);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setUnlocalizedName(assetName);
		this.setLightValue(0.85F);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftTab;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
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
		return GalacticraftCore.proxy.getBlockRenderID(this.blockID);
	}

	private boolean canPlaceTorchOn(World par1World, int par2, int par3, int par4)
	{
		if (par1World.doesBlockHaveSolidTopSurface(par2, par3, par4))
		{
			return true;
		}
		else
		{
			int l = par1World.getBlockId(par2, par3, par4);
			return Block.blocksList[l] != null && Block.blocksList[l].canPlaceTorchOnTop(par1World, par2, par3, par4);
		}
	}

	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
	{
		return par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST, true) || par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST, true) || par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH, true) || par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH, true) || this.canPlaceTorchOn(par1World, par2, par3 - 1, par4);
	}

	@Override
	public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
	{
		int j1 = par9;

		if (par5 == 1 && this.canPlaceTorchOn(par1World, par2, par3 - 1, par4))
		{
			j1 = 5;
		}

		if (par5 == 2 && par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH, true))
		{
			j1 = 4;
		}

		if (par5 == 3 && par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH, true))
		{
			j1 = 3;
		}

		if (par5 == 4 && par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST, true))
		{
			j1 = 2;
		}

		if (par5 == 5 && par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST, true))
		{
			j1 = 1;
		}

		return j1;
	}

	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		super.updateTick(par1World, par2, par3, par4, par5Random);

		if (par1World.getBlockMetadata(par2, par3, par4) == 0)
		{
			this.onBlockAdded(par1World, par2, par3, par4);
		}
	}

	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
	{
		if (par1World.getBlockMetadata(par2, par3, par4) == 0)
		{
			if (par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST, true))
			{
				par1World.setBlockMetadataWithNotify(par2, par3, par4, 1, 2);
			}
			else if (par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST, true))
			{
				par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
			}
			else if (par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH, true))
			{
				par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
			}
			else if (par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH, true))
			{
				par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
			}
			else if (this.canPlaceTorchOn(par1World, par2, par3 - 1, par4))
			{
				par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
			}
		}

		this.dropTorchIfCantStay(par1World, par2, par3, par4);
	}

	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
	{
		if (this.dropTorchIfCantStay(par1World, par2, par3, par4))
		{
			int i1 = par1World.getBlockMetadata(par2, par3, par4);
			boolean flag = false;

			if (!par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST, true) && i1 == 1)
			{
				flag = true;
			}

			if (!par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST, true) && i1 == 2)
			{
				flag = true;
			}

			if (!par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH, true) && i1 == 3)
			{
				flag = true;
			}

			if (!par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH, true) && i1 == 4)
			{
				flag = true;
			}

			if (!this.canPlaceTorchOn(par1World, par2, par3 - 1, par4) && i1 == 5)
			{
				flag = true;
			}

			if (flag)
			{
				this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
				par1World.setBlockToAir(par2, par3, par4);
			}
		}
	}

	protected boolean dropTorchIfCantStay(World par1World, int par2, int par3, int par4)
	{
		if (!this.canPlaceBlockAt(par1World, par2, par3, par4))
		{
			if (par1World.getBlockId(par2, par3, par4) == this.blockID)
			{
				this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
				par1World.setBlockToAir(par2, par3, par4);
			}

			return false;
		}
		else
		{
			return true;
		}
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
	{
		int l = par1World.getBlockMetadata(par2, par3, par4) & 7;
		float f = 0.15F;

		if (l == 1)
		{
			this.setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
		}
		else if (l == 2)
		{
			this.setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
		}
		else if (l == 3)
		{
			this.setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
		}
		else if (l == 4)
		{
			this.setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
		}
		else
		{
			f = 0.1F;
			this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
		}

		return super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
	}
}
