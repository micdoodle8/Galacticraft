package micdoodle8.mods.galacticraft.mimas.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCMimasBlock extends Block
{
	public GCMimasBlock(int par1, int par2)
	{
		super(par1, par2, Material.rock);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta)
	{
		switch (meta)
		{
		case 0:
			switch (side)
			{
			case 0:
				return 2;
			case 1:
				return 0;
			default:
				return 1;
			}
		case 1:
			return 2;
		default:
			return -1;
		}
	}

	@Override
	public int idDropped(int meta, Random random, int par3)
	{
		switch (meta)
		{
		default:
			return this.blockID;
		}
	}

	@Override
    public int damageDropped(int meta)
    {
		switch (meta)
		{
		default:
			return meta;
		}
    }

	@Override
    public int quantityDropped(int meta, int fortune, Random random)
    {
		switch (meta)
		{
		default:
			return 1;
		}
    }

    @SideOnly(Side.CLIENT)
	@Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 2; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
	
	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/mimas/client/blocks/mimas.png";
	}
}
