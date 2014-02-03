package micdoodle8.mods.galacticraft.mars.items;

import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsItemArmor.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsItemArmor extends ItemArmor
{
	public boolean attachedMask;
	private final EnumArmorMaterial material;

	public GCMarsItemArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4, boolean breathable)
	{
		super(par1, par2EnumArmorMaterial, par3, par4);
		this.material = par2EnumArmorMaterial;
		this.attachedMask = breathable;
	}

	@Override
	public Item setUnlocalizedName(String par1Str)
	{
		super.setTextureName(par1Str);
		super.setUnlocalizedName(par1Str);
		return this;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer)
	{
		if (this.material == GCMarsItems.ARMORDESH)
		{
			if (stack.getItem().itemID == GCMarsItems.deshHelmet.itemID)
			{
				return "textures/model/armor/desh_1.png";
			}
			else if (stack.getItem().itemID == GCMarsItems.deshChestplate.itemID || stack.getItem().itemID == GCMarsItems.deshBoots.itemID)
			{
				return "textures/model/armor/desh_2.png";
			}
			else if (stack.getItem().itemID == GCMarsItems.deshLeggings.itemID)
			{
				return "textures/model/armor/desh_3.png";
			}
		}

		return null;
	}

	@Override
	public CreativeTabs getCreativeTab()
	{
		return GalacticraftMars.galacticraftMarsTab;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxyCore.galacticraftItem;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.itemIcon = par1IconRegister.registerIcon(this.getUnlocalizedName().replace("item.", "galacticraftmars:"));
	}
}
