package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems.EnumArmorIndex;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreItemArmor.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreItemArmor extends ItemArmor
{
	public GCCoreItemArmor(int armorIndex, micdoodle8.mods.galacticraft.core.items.GCCoreItems.EnumArmorIndex type, String assetSuffix)
	{
		super(type.getMaterial(), GalacticraftCore.proxy.getArmorRenderID(type), armorIndex);
		this.setUnlocalizedName("steel_" + assetSuffix);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + "steel_" + assetSuffix);
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
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
		if (this.getArmorMaterial() == EnumArmorIndex.HEAVY_DUTY.getMaterial())
		{
			if (stack.getItem() == GCCoreItems.steelHelmet)
			{
				return "textures/model/armor/titanium_1.png";
			}
			else if (stack.getItem() == GCCoreItems.steelChestplate || stack.getItem() == GCCoreItems.steelBoots)
			{
				return "textures/model/armor/titanium_2.png";
			}
			else if (stack.getItem() == GCCoreItems.steelLeggings)
			{
				return "textures/model/armor/titanium_3.png";
			}
		}

		return null;
	}
}
