package micdoodle8.mods.galacticraft.moon.items;

import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.moon.GalacticraftMoon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMoonItemCheese.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMoonItemCheese extends ItemFood
{
	public GCMoonItemCheese(int par1, float par2, boolean par3)
	{
		super(par1, par2, par3);
		this.setUnlocalizedName("cheeseCurd");
	}

	public GCMoonItemCheese(int par1, boolean par2)
	{
		this(par1, 0.6F, par2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("galacticraftmoon:cheese_curd");
	}

	@Override
	public CreativeTabs getCreativeTab()
	{
		return GalacticraftMoon.galacticraftMoonTab;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxyCore.galacticraftItem;
	}
}
