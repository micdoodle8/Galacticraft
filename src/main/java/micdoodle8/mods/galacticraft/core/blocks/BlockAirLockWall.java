package micdoodle8.mods.galacticraft.core.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class BlockAirLockWall extends BlockBreakable implements IPartialSealableBlock
{
	public BlockAirLockWall(String assetName)
	{
		super(GalacticraftCore.ASSET_PREFIX + "oxygentile_3", Material.portal, false);
		this.setTickRandomly(true);
		this.setHardness(1000.0F);
		this.setStepSound(Block.soundTypeMetal);
		this.setBlockTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setBlockName(assetName);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "deco_aluminium_4");
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		float var5;
		float var6;

		Block frameID = GCBlocks.airLockFrame;
		Block sealID = GCBlocks.airLockSeal;

		Block idXMin = world.getBlock(x - 1, y, z);
		Block idXMax = world.getBlock(x + 1, y, z);

		if (idXMin != frameID && idXMax != frameID && idXMin != sealID && idXMax != sealID)
		{
			var5 = 0.325F;
			var6 = 0.5F;
			this.setBlockBounds(0.5F - var5, 0.0F, 0.5F - var6, 0.5F + var5, 1.0F, 0.5F + var6);
		}
		else
		{
			int adjacentCount = 0;

			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
			{
				if (dir != ForgeDirection.UP && dir != ForgeDirection.DOWN)
				{
					Vector3 thisVec = new Vector3(x, y, z);
					thisVec = thisVec.modifyPositionFromSide(dir);
					Block blockID = thisVec.getBlock(world);

					if (blockID == GCBlocks.airLockFrame || blockID == GCBlocks.airLockSeal)
					{
						adjacentCount++;
					}
				}
			}

			if (adjacentCount == 4)
			{
				var5 = 0.5F;
				var6 = 0.325F;
				this.setBlockBounds(0.0F, 0.0F + var6, 0.0F, 1.0F, 1.0F - var6, 1.0F);
			}
			else
			{
				var5 = 0.5F;
				var6 = 0.325F;
				this.setBlockBounds(0.5F - var5, 0.0F, 0.5F - var6, 0.5F + var5, 1.0F, 0.5F + var6);
			}
		}
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
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		return true;
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}

	@Override
	public boolean isSealed(World world, int x, int y, int z, ForgeDirection direction)
	{
		return true;
	}
}
