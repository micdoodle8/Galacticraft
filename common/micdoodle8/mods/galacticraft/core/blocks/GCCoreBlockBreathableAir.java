package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.oxygen.BlockVec3;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenPressureProtocol;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * GCCoreBlockBreathableAir.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockBreathableAir extends Block
{
	public GCCoreBlockBreathableAir(int id, String assetName)
	{
		super(id, Material.air);
		this.setResistance(1000.0F);
		this.setHardness(0.0F);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setUnlocalizedName(assetName);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean isAirBlock(World var1, int var2, int var3, int var4)
	{
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4)
	{
		return null;
	}

	@Override
	public boolean isBlockReplaceable(World var1, int var2, int var3, int var4)
	{
		return true;
	}

	@Override
	public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4)
	{
		return true;
	}

	@Override
	public boolean canCollideCheck(int var1, boolean var2)
	{
		return false;
	}

	@Override
	public int getRenderBlockPass()
	{
		return 1;
	}

	@Override
	public int getMobilityFlag()
	{
		return 1;
	}

	@Override
	public int idDropped(int var1, Random var2, int var3)
	{
		return -1;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		if (par1IBlockAccess.getBlockId(par2, par3, par4) == this.blockID)
		{
			return false;
		}
		else
		{
			final int i = par1IBlockAccess.getBlockId(par2, par3, par4);
			boolean var6 = false;

			if (Block.blocksList[i] != null)
			{
				var6 = !Block.blocksList[i].isOpaqueCube();
			}

			final boolean var7 = i == 0;

			if ((var6 || var7) && par5 == 3 && !var6)
			{
				return true;
			}
			else if ((var6 || var7) && par5 == 4 && !var6)
			{
				return true;
			}
			else if ((var6 || var7) && par5 == 5 && !var6)
			{
				return true;
			}
			else if ((var6 || var7) && par5 == 2 && !var6)
			{
				return true;
			}
			else if ((var6 || var7) && par5 == 0 && !var6)
			{
				return true;
			}
			else if ((var6 || var7) && par5 == 1 && !var6)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int idBroken)
	{
		//Do nothing if an air neighbour was replaced (probably because replacing with breatheableAir)
		//but do a check if replacing breatheableAir as that could be dividing a sealed space
		if (idBroken != 0) 
		{
			OxygenPressureProtocol.onEdgeBlockUpdated(world, new BlockVec3(x, y, z));
		}
	}
}
