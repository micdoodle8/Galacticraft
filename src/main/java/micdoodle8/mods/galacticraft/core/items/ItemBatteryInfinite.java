package micdoodle8.mods.galacticraft.core.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.transmission.item.ItemElectric;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemBatteryInfinite extends ItemElectric
{
	public ItemBatteryInfinite(String assetName)
	{
		super();
		this.setUnlocalizedName(assetName);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
	}

	@Override
	protected void setMaxTransfer()
	{
		this.transferMax = 400;
	}

	@Override
	public int getTierGC(ItemStack itemStack)
	{
		return 4;
	}

	@Override
	public CreativeTabs getCreativeTab()
	{
		return GalacticraftCore.galacticraftItemsTab;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxyCore.galacticraftItem;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("\u00a72" + GCCoreUtil.translate("gui.infiniteBattery.desc"));
	}

	@Override
	public float getElectricityStored(ItemStack itemStack)
	{
		return this.getMaxElectricityStored(itemStack);
	}

	@Override
	public void setElectricity(ItemStack itemStack, float joules)
	{
	}

	@Override
	public float getMaxElectricityStored(ItemStack itemStack)
	{
		return Float.POSITIVE_INFINITY;
	}

	@Override
	public float getTransfer(ItemStack itemStack)
	{
		return 0.0F;
	}

	@Override
	public float recharge(ItemStack theItem, float energy, boolean doReceive)
	{
		return 0F;
	}

	@Override
	public float discharge(ItemStack theItem, float energy, boolean doTransfer)
	{
		return energy;
	}
	
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(par1, 1, 0));
	}
}
