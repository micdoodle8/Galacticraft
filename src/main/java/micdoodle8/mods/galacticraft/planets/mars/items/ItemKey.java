package micdoodle8.mods.galacticraft.planets.mars.items;

import java.util.List;

import micdoodle8.mods.galacticraft.api.item.IKeyItem;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxy;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsItemKey.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ItemKey extends Item implements IKeyItem
{
	public static String[] keyTypes = new String[] { "T2" };
	public IIcon[] keyIcons = new IIcon[1];

	public ItemKey()
	{
		this.setMaxStackSize(1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public CreativeTabs getCreativeTab()
	{
		return GalacticraftPlanets.creativeTab;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return "item." + "key." + ItemKey.keyTypes[itemStack.getItemDamage()];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxy.galacticraftItem;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		int i = 0;

		for (final String name : ItemKey.keyTypes)
		{
			this.keyIcons[i++] = iconRegister.registerIcon(GalacticraftPlanets.TEXTURE_PREFIX + "key_" + name);
		}
	}

	@Override
	public IIcon getIconFromDamage(int damage)
	{
		if (this.keyIcons.length > damage)
		{
			return this.keyIcons[damage];
		}

		return super.getIconFromDamage(damage);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < ItemKey.keyTypes.length; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}

	@Override
	public int getTier(ItemStack keyStack)
	{
		return 2;
	}
}
