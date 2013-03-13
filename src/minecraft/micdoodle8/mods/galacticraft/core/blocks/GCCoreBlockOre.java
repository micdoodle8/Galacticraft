package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.API.IDetectableResource;
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
public class GCCoreBlockOre extends Block implements IDetectableResource
{
	// Copper: 0, Aluminum: 1, Titanium: 2.
	Icon[] iconBuffer;

	public GCCoreBlockOre(int i)
	{
		super(i, Material.rock);
        this.setCreativeTab(GalacticraftCore.galacticraftTab);
	}

    @Override
    public void func_94332_a(IconRegister par1IconRegister)
    {
    	this.iconBuffer = new Icon[3];
    	this.iconBuffer[0] = par1IconRegister.func_94245_a("galacticraftcore:ore_copper");
    	this.iconBuffer[1] = par1IconRegister.func_94245_a("galacticraftcore:ore_aluminium");
    	this.iconBuffer[2] = par1IconRegister.func_94245_a("galacticraftcore:ore_titanium");
    }

	@Override
	public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
	{
		return this.iconBuffer[meta];
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
