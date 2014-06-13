package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



public class BlockOreGC extends Block
{
	public BlockOreGC(String name)
	{
		super(Material.rock);
		this.setBlockName(name);
		this.setBlockTextureName(GalacticraftCore.ASSET_PREFIX + name);
		this.setHardness(2.0F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftTab;
	}
}
