package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenPressureProtocol;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;



public class BlockBrightBreathableAir extends BlockAir
{
	public BlockBrightBreathableAir(String assetName)
	{
		this.setResistance(1000.0F);
		this.setHardness(0.0F);
		this.setBlockTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setBlockName(assetName);
		this.setStepSound(new SoundType("sand", 0.0F, 1.0F));
		this.setLightLevel(1.0F);
	}

	@Override
    public boolean canReplace(World world, int x, int y, int z, int side, ItemStack stack)
	{
		return true;
	}

	@Override
	public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4)
	{
		return true;
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
	public Item getItemDropped(int var1, Random var2, int var3)
	{
		return Item.getItemFromBlock(Blocks.air);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		final Block block = par1IBlockAccess.getBlock(par2, par3, par4);
		if (block == this || block == GCBlocks.breatheableAir)
		{
			return false;
		}
		else
		{
			return (block instanceof BlockAir && par5>= 0 && par5 <= 5);
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block idBroken)
	{
		if (idBroken != Blocks.air && idBroken != GCBlocks.brightAir)
		//Do nothing if an air neighbour was replaced (probably because replacing with breatheableAir)
		//but do a check if replacing breatheableAir as that could be dividing a sealed space
		{
			OxygenPressureProtocol.onEdgeBlockUpdated(world, new BlockVec3(x, y, z));
		}
	}
}
