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
		default:
			return 0;
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
	
	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/client/blocks/mars.png";
	}

	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int blockid, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int meta = 0; meta < 9; ++meta)
        {
            par3List.add(new ItemStack(blockid, 1, meta));
        }
    }
}
