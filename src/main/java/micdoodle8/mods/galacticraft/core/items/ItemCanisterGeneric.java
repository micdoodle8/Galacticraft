package micdoodle8.mods.galacticraft.core.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;

import java.util.List;

public abstract class ItemCanisterGeneric extends Item
{
	public ItemCanisterGeneric(String assetName)
	{
		super();
		this.setMaxDamage(FluidContainerRegistry.BUCKET_VOLUME + 1);
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setUnlocalizedName(assetName);
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
		return GalacticraftCore.galacticraftItemsTab;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(par1, 1, 1));
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack)
	{
		if (itemStack != null && itemStack.getItem() == this.getContainerItem() && itemStack.getItemDamage() == itemStack.getMaxDamage())
		{
			return null;
		}

		return new ItemStack(this.getContainerItem(), 1, this.getContainerItem().getMaxDamage());
	}
	
	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
	{
		if (par1ItemStack.getMaxDamage() == par1ItemStack.getItemDamage())
		{
			final int stackSize = par1ItemStack.stackSize;

			if (!(par1ItemStack.getItem() instanceof ItemOilCanister))
			{
				par1ItemStack = new ItemStack(GCItems.oilCanister, stackSize, GCItems.oilCanister.getMaxDamage());
			}
		}
	}
}
