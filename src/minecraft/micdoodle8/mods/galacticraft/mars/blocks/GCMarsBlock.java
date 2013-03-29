package micdoodle8.mods.galacticraft.mars.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCMarsBlock extends Block
{
	protected GCMarsBlock(int i, int j, Material material)
	{
		super(i, j, material);
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
	}

	@Override
    public String getTextureFile()
    {
    	return "/micdoodle8/mods/galacticraft/mars/client/blocks/mars.png";
    }
}
