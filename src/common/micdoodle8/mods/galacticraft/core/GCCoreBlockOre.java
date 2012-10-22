package micdoodle8.mods.galacticraft.core;

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
public class GCCoreBlockOre extends Block
{
	// Copper: 0, Aluminum: 1, Titanium: 2.
	
	public GCCoreBlockOre(int i, int j) 
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
			return 1;
		case 1:
			return 0;
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
		return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
	}
}
