package micdoodle8.mods.galacticraft.core;

import net.minecraft.src.Block;
import net.minecraft.src.Material;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreBlock extends Block
{
	protected GCCoreBlock(int i, int j, Material material) 
	{
		super(i, j, material);
	}

	@Override
    public String getTextureFile()
    {
    	return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
    }
}
