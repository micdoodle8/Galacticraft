package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreItemOxygenMask extends GCCoreItemBreathableHelmet implements IArmorTextureProvider
{
	protected GCCoreItemOxygenMask(int par1, EnumArmorMaterial material, int i, int j) 
	{
		super(par1, material, j, j);
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
	}

    @Override
	public String getArmorTextureFile(ItemStack itemstack)
    {
    	return "/micdoodle8/mods/galacticraft/core/client/armor/oxygen_1.png";
    }
	
	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/core/client/items/core.png";
	}
}
