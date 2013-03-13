package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreBlock extends Block
{
	/**
	 *  0: COPPER BLOCK
	 *  1: ALUMINIUM BLOCK
	 *  2: TITANIUM BLOCK
	 *  3: DECO 1
	 *  4: DECO 2
	 */
	Icon[][] iconBuffer;
	
	protected GCCoreBlock(int i)
	{
		super(i, Material.rock);
        this.setCreativeTab(GalacticraftCore.galacticraftTab);
	}

    @Override
    public void func_94332_a(IconRegister par1IconRegister)
    {
    	this.iconBuffer = new Icon[5][3];
    	this.iconBuffer[0][0] = par1IconRegister.func_94245_a("galacticraftcore:oreblock_copper");
    	this.iconBuffer[1][0] = par1IconRegister.func_94245_a("galacticraftcore:oreblock_aluminium");
    	this.iconBuffer[2][0] = par1IconRegister.func_94245_a("galacticraftcore:oreblock_titanium");
    	this.iconBuffer[3][0] = par1IconRegister.func_94245_a("galacticraftcore:deco_aluminium_2");
    	this.iconBuffer[3][1] = par1IconRegister.func_94245_a("galacticraftcore:deco_aluminium_4");
    	this.iconBuffer[3][2] = par1IconRegister.func_94245_a("galacticraftcore:deco_aluminium_1");
    	this.iconBuffer[4][0] = par1IconRegister.func_94245_a("galacticraftcore:deco_aluminium_4");
    }

	@Override
	public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
	{
		switch (meta)
		{
		case 0:
			return this.iconBuffer[0][0];
		case 1:
			return this.iconBuffer[1][0];
		case 2:
			return this.iconBuffer[2][0];
		case 3:
			switch (side)
			{
			case 0:
				return this.iconBuffer[3][0];
			case 1:
				return this.iconBuffer[3][1];
			default:
				return this.iconBuffer[3][2];
			}
		case 4:
			return this.iconBuffer[4][0];
		default:
			return null;
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
        for (int var4 = 0; var4 < 5; ++var4)
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
