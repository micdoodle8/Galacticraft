package micdoodle8.mods.galacticraft.io.blocks;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;

public class GCIoBlock extends Block
{
	public GCIoBlock(int par1, int par2)
	{
		super(par1, par2, Material.rock);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) 
	{
		switch (meta) 
		{
		case 0:
			return 0;
		case 1:
			return 1;
		case 2:
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
        for (int var4 = 0; var4 < 3; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
	
	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/io/client/blocks/io.png";
	}
}
