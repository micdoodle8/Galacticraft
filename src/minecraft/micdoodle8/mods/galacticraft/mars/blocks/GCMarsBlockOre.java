package micdoodle8.mods.galacticraft.mars.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCMarsBlockOre extends Block
{
	// Desh: 0, Quandrium: 1, AluminumMars: 2, CopperMars: 3, TitaniumMars: 4;

	public GCMarsBlockOre(int i)
	{
		super(i, 4, Material.rock);
        this.setRequiresSelfNotify();
        this.setCreativeTab(GalacticraftCore.galacticraftTab);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta)
	{
		switch (meta)
		{
		case 0:
			return 4;
		case 1:
			return 5;
		case 2:
			return 6;
		case 3:
			return 7;
		case 4:
			return 8;
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
		case 0:
	        return random.nextInt(3) + 1;
		default:
			return 1;
		}
    }

    @SideOnly(Side.CLIENT)
	@Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 5; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }

	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/mars/client/blocks/mars.png";
	}
}
