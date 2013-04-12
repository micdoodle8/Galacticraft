package micdoodle8.mods.galacticraft.core.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreItemFuel extends Item
{
	public GCCoreItemFuel(int par1)
	{
		super(par1);
		this.setUnlocalizedName("fuel");
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void updateIcons(IconRegister par1IconRegister)
    {
        this.iconIndex = par1IconRegister.registerIcon("galacticraftcore:fuel_flow");
    }

//	@Override
//    public CreativeTabs getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftTab;
//    }
}
