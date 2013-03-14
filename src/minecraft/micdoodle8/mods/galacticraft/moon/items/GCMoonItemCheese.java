package micdoodle8.mods.galacticraft.moon.items;

import micdoodle8.mods.galacticraft.moon.GalacticraftMoon;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCMoonItemCheese extends ItemFood
{
    public GCMoonItemCheese(int par1, int par2, float par3, boolean par4)
    {
        super(par1, par2, par3, par4);
    }

    public GCMoonItemCheese(int par1, int par2, boolean par3)
    {
        this(par1, par2, 0.6F, par3);
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public void func_94581_a(IconRegister iconRegister)
	{
		this.iconIndex = iconRegister.func_94245_a("galacticraftmoon:cheese_curd");
	}

	@Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftMoon.galacticraftMoonTab;
    }
}
