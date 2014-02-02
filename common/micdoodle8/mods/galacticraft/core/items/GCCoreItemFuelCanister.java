package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreItemFuelCanister.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreItemFuelCanister extends Item
{
	protected Icon[] icons = new Icon[7];

	public GCCoreItemFuelCanister(int id, String assetName)
	{
		super(id);
		this.setMaxDamage(FluidContainerRegistry.BUCKET_VOLUME + 1);
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setUnlocalizedName(assetName);
		this.setContainerItem(GCCoreItems.oilCanister);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxyCore.galacticraftItem;
	}

	@Override
	public CreativeTabs getCreativeTab()
	{
		return GalacticraftCore.galacticraftTab;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(par1, 1, 1));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		for (int i = 0; i < this.icons.length; i++)
		{
			this.icons[i] = iconRegister.registerIcon(this.getIconString() + "_" + i);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		if (itemStack.getItemDamage() == 1)
		{
			return "item.fuelCanister";
		}

		return "item.fuelCanisterPartial";
	}

	@Override
	public Icon getIconFromDamage(int par1)
	{
		final int damage = (int) Math.floor(par1 / 166.666666666666666666666666666666666666666666666666667);

		if (this.icons.length > damage)
		{
			return this.icons[this.icons.length - damage - 1];
		}

		return super.getIconFromDamage(damage);
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
	{
		if (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage() == 0)
		{
			final int stackSize = par1ItemStack.stackSize;

			if (!(par1ItemStack.getItem() instanceof GCCoreItemOilCanister))
			{
				par1ItemStack = new ItemStack(GCCoreItems.oilCanister, stackSize, GCCoreItems.oilCanister.getMaxDamage());
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage() > 0)
		{
			par3List.add("Fuel: " + (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()));
		}
	}

	@Override
	public ItemStack getContainerItemStack(ItemStack itemStack)
	{
		if (itemStack != null && itemStack.itemID == this.getContainerItem().itemID && itemStack.getItemDamage() == itemStack.getMaxDamage())
		{
			return null;
		}

		return new ItemStack(this.getContainerItem(), 1, this.getContainerItem().getMaxDamage());
	}
}
