package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.src.Block;
import net.minecraft.src.Material;

/**
 * Copyright 2012, micdoodle8
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
