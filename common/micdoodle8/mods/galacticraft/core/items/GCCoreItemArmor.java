package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumArmorMaterial;
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
	private final EnumArmorMaterial material;

	public GCCoreItemArmor(int id, int armorIndex, String assetSuffix)
	{
		super(id, GCCoreItems.ARMOR_STEEL, GalacticraftCore.proxy.getTitaniumArmorRenderIndex(), armorIndex);
		this.material = GCCoreItems.ARMOR_STEEL;
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
		return ClientProxyCore.galacticraftItem;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer)
	{
		if (this.material == GCCoreItems.ARMOR_STEEL)
		{
			if (stack.getItem().itemID == GCCoreItems.steelHelmet.itemID)
			{
				return "textures/model/armor/titanium_1.png";
			}
			else if (stack.getItem().itemID == GCCoreItems.steelChestplate.itemID || stack.getItem().itemID == GCCoreItems.steelBoots.itemID)
			{
				return "textures/model/armor/titanium_2.png";
			}
			else if (stack.getItem().itemID == GCCoreItems.steelLeggings.itemID)
			{
				return "textures/model/armor/titanium_3.png";
			}
		}

		return null;
	}
}
