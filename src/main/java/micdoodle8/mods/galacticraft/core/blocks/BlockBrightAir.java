package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockBrightAir extends BlockAir
{
	public BlockBrightAir(String assetName)
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
		return false;
	}
}
