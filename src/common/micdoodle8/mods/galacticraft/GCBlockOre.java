package micdoodle8.mods.galacticraft;

import java.util.List;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCBlockOre extends Block
{
	// Desh: 0, Electrum: 1, Quandrium: 2, AluminumEarth: 3, AluminumMars: 4, CopperEarth: 5, CopperMars: 6, TitaniumEarth: 7, TitaniumMars: 8;
	
	public GCBlockOre(int i, int j) 
	{
		super(i, j, Material.rock);
        this.setRequiresSelfNotify();
        this.setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) 
	{
		switch (meta) 
		{
		case 0:
			return 6;
		case 1:
			return 7;
		case 2:
			return 8;
		case 3:
			return 9;
		case 4:
			return 10;
		case 5:
			return 11;
		case 6:
			return 12;
		case 7:
			return 13;
		case 8:
			return 14;
		default:
			return meta;
		}
	}

	@Override
	public int idDropped(int meta, Random random, int par3) 
	{
		switch (meta)
		{
		case 0:
			return this.blockID; // TODO Return desh item id
		default:
			return this.blockID;
		}
	}

	@Override
    protected int damageDropped(int meta)
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
		case 0:
	        return random.nextInt(4) + 1;
		default:
			return 1;
		}
    }

    @SideOnly(Side.CLIENT)
	@Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 9; ++var4)
        {
        	if (var4 != 1)
        	{
                par3List.add(new ItemStack(par1, 1, var4));
        	}
        }
    }
	
	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/client/blocks/mars.png";
	}
}
