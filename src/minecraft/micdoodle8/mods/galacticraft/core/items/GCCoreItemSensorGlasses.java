package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreItemSensorGlasses extends ItemArmor implements IArmorTextureProvider
{
	public GCCoreItemSensorGlasses(int par1, EnumArmorMaterial material, int i, int j)
	{
		super(par1, material, i, j);
	}

	@Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon("galacticraftcore:sensor_glasses" + GalacticraftCore.TEXTURE_SUFFIX);
    }

    @Override
	public String getArmorTextureFile(ItemStack itemstack)
    {
    	return "/micdoodle8/mods/galacticraft/core/client/armor/sensor_1_alt.png";
    }
}
