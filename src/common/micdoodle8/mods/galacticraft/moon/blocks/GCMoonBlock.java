package micdoodle8.mods.galacticraft.moon.blocks;

import net.minecraft.src.Block;
import net.minecraft.src.Material;

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