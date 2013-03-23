package micdoodle8.mods.galacticraft.moon.items;

import micdoodle8.mods.galacticraft.moon.GalacticraftMoon;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemReed;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCMoonItemReed extends ItemReed
{
	public GCMoonItemReed(int par1, Block par2Block)
	{
		super(par1, par2Block);
		this.setMaxStackSize(1);
	}
    
	@Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister)
	{
		this.iconIndex = iconRegister.registerIcon("galacticraftmoon:cheese_block");
	}

    @Override
	public CreativeTabs getCreativeTab()
    {
        return GalacticraftMoon.galacticraftMoonTab;
    }
}
