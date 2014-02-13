package micdoodle8.mods.galacticraft.core.items;

import javax.swing.Icon;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreItemOxygenGear.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreItemOxygenGear extends Item
{
	public GCCoreItemOxygenGear(int id, String assetName)
	{
		super(id);
		this.setUnlocalizedName(assetName);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
	}

	@Override
	public CreativeTabs getCreativeTab()
	{
		return GalacticraftCore.galacticraftTab;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxy.galacticraftItem;
	}

	@Override
	public Icon getIconFromDamage(int damage)
	{
		return super.getIconFromDamage(damage);
	}
}
