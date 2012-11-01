package micdoodle8.mods.galacticraft.moon;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.src.Block;
import net.minecraft.src.ItemReed;

public class GCMoonItemReed extends ItemReed
{
	public GCMoonItemReed(int par1, Block par2Block) 
	{
		super(par1, par2Block);
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
	}

	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/moon/client/items/moon.png";
	}
}
