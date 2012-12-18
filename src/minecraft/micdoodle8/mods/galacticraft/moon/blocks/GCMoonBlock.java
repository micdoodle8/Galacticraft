package micdoodle8.mods.galacticraft.moon.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class GCMoonBlock extends Block
{
	public GCMoonBlock(int i, int j, Material m)
	{
		super(i, j, m);
	}
	
	@Override
	public String getTextureFile()
	{
    	return "/micdoodle8/mods/galacticraft/moon/client/blocks/moon.png";
	}
}