package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.renderer.texture.IconRegister;
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
	public boolean attachedMask;

	public GCCoreItemSensorGlasses(int par1, EnumArmorMaterial material, int i, int j, boolean breathable)
	{
		super(par1, material, i, j);
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
		this.attachedMask = breathable;
	}

    @Override
	@SideOnly(Side.CLIENT)
    public void func_94581_a(IconRegister par1IconRegister)
    {
        this.iconIndex = par1IconRegister.func_94245_a("galacticraftcore:sensor_glasses");
    }

    @Override
	public String getArmorTextureFile(ItemStack itemstack)
    {
    	return this.attachedMask ? "/micdoodle8/mods/galacticraft/core/client/armor/sensorox_1.png" : "/micdoodle8/mods/galacticraft/core/client/armor/sensor_1.png";
    }
}
