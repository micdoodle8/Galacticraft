package micdoodle8.mods.galacticraft.core;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreItemSensorGlasses extends GCCoreItemBreathableHelmet implements IArmorTextureProvider
{
	public boolean attachedMask;
	
	public GCCoreItemSensorGlasses(int par1, EnumArmorMaterial material, int i, int j, boolean breathable) 
	{
		super(par1, material, i, j);
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.attachedMask = breathable;
	}

    public String getArmorTextureFile(ItemStack itemstack)
    {
    	return attachedMask ? "/micdoodle8/mods/galacticraft/mars/client/armor/sensorox_1.png" : "/micdoodle8/mods/galacticraft/mars/client/armor/sensor_1.png";
    }
	
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/mars/client/items/core.png";
	}
}
