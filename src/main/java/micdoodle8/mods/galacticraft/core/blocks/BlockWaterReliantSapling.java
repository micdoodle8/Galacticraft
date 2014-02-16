package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.world.gen.WorldGenForest;
import micdoodle8.mods.galacticraft.core.world.gen.WorldGenTaigaGC;
import micdoodle8.mods.galacticraft.core.world.gen.WorldGenTreesGC;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreBlockSapling.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class BlockWaterReliantSapling extends BlockSapling
{
	@Override
	@SideOnly(Side.CLIENT)
	protected String getTextureName()
	{
		return "sapling";
	}

	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		if (!par1World.isRemote)
		{
			super.updateTick(par1World, par2, par3, par4, par5Random);

			int waterBlocksNearby = 0;

			for (int i = -4; i < 5; i++)
			{
				for (int j = -4; j < 5; j++)
				{
					if (par1World.getBlock(par2 + i, par3 - 1, par4 + j) == Blocks.water || par1World.getBlock(par2 + i, par3 - 1, par4 + j) == Blocks.flowing_water)
					{
						waterBlocksNearby++;
					}
				}
			}

			if (!(waterBlocksNearby > 3))
			{
				par1World.setBlock(par2, par3, par4, Blocks.deadbush, 0, 3);
			}

			final int var6 = par1World.getBlockMetadata(par2, par3, par4);

			if ((var6 & 8) == 0)
			{
				par1World.setBlockMetadataWithNotify(par2, par3, par4, var6 | 8, 3);
			}
			else
			{
				this.func_149879_c(par1World, par2, par3, par4, par5Random);
			}
		}
	}

	@Override
	public void func_149879_c(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		final int var6 = par1World.getBlockMetadata(par2, par3, par4) & 3;
		Object var7 = null;
		final int var8 = 0;
		final int var9 = 0;
		final boolean var10 = false;

		if (var6 == 1)
		{
			var7 = new WorldGenTaigaGC(true);
		}
		else if (var6 == 2)
		{
			var7 = new WorldGenForest(true);
		}
		// No jungle trees...
		else
		{
			var7 = new WorldGenTreesGC(true);
		}

		if (var10)
		{
			par1World.setBlockToAir(par2 + var8, par3, par4 + var9);
			par1World.setBlockToAir(par2 + var8 + 1, par3, par4 + var9);
			par1World.setBlockToAir(par2 + var8, par3, par4 + var9 + 1);
			par1World.setBlockToAir(par2 + var8 + 1, par3, par4 + var9 + 1);
		}
		else
		{
			par1World.setBlockToAir(par2, par3, par4);
		}

		if (!((WorldGenerator) var7).generate(par1World, par5Random, par2 + var8, par3, par4 + var9))
		{
			if (var10)
			{
				par1World.setBlock(par2 + var8, par3, par4 + var9, this, var6, 3);
				par1World.setBlock(par2 + var8 + 1, par3, par4 + var9, this, var6, 3);
				par1World.setBlock(par2 + var8, par3, par4 + var9 + 1, this, var6, 3);
				par1World.setBlock(par2 + var8 + 1, par3, par4 + var9 + 1, this, var6, 3);
			}
			else
			{
				par1World.setBlock(par2, par3, par4, this, var6, 3);
			}
		}
	}
}
