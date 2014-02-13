package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems.EnumArmorIndex;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxy;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreItemSensorGlasses.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreItemSensorGlasses extends ItemArmor
{
	public GCCoreItemSensorGlasses(String assetName)
	{
		super(EnumArmorIndex.SENSOR_GLASSES.getMaterial(), EnumArmorIndex.SENSOR_GLASSES.getRenderIndex(), 0);
		this.setUnlocalizedName(assetName);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
	}

	@Override
	public CreativeTabs getCreativeTab()
	{
		return GalacticraftCore.galacticraftTab;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
		return "textures/model/armor/sensor_1.png";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxy.galacticraftItem;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHelmetOverlay(ItemStack stack, EntityPlayer player, ScaledResolution resolution, float partialTicks, boolean hasScreen, int mouseX, int mouseY)
	{
		OverlaySensorGlasses.renderSensorGlassesMain(stack, player, resolution, partialTicks, hasScreen, mouseX, mouseY);
		OverlaySensorGlasses.renderSensorGlassesValueableBlocks(stack, player, resolution, partialTicks, hasScreen, mouseX, mouseY);
	}
}
