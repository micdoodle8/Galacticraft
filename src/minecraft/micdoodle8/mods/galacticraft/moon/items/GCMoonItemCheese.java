package micdoodle8.mods.galacticraft.moon.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.item.ItemFood;

public class GCMoonItemCheese extends ItemFood
{
    public GCMoonItemCheese(int par1, int par2, float par3, boolean par4)
    {
        super(par1, par2, par3, par4);
        this.setCreativeTab(GalacticraftCore.galacticraftTab);
    }

    public GCMoonItemCheese(int par1, int par2, boolean par3)
    {
        this(par1, par2, 0.6F, par3);
    }

    @Override
	public String getTextureFile()
    {
		return "/micdoodle8/mods/galacticraft/moon/client/items/moon.png";
    }
}
