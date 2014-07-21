package micdoodle8.mods.galacticraft.planets.asteroids.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemCanisterLiquidOxygen extends ItemCanisterGeneric
{
	protected IIcon[] icons = new IIcon[7];

	public ItemCanisterLiquidOxygen(String assetName)
	{
		super(assetName);
		this.setContainerItem(GCItems.oilCanister);
		this.setTextureName(AsteroidsModule.TEXTURE_PREFIX + assetName);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		for (int i = 0; i < this.icons.length; i++)
		{
			this.icons[i] = iconRegister.registerIcon(this.getIconString() + "_" + i);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		if (itemStack.getMaxDamage() - itemStack.getItemDamage() == 0)
		{
			return "item.emptyGasCanister";
		}

		if (itemStack.getItemDamage() == 1)
		{
			return "item.canister.LOX.full";
		}

		return "item.canister.LOX.partial";
	}

	@Override
	public IIcon getIconFromDamage(int par1)
	{
		final int damage = 6 * par1 / this.getMaxDamage();

		if (this.icons.length > damage)
		{
			return this.icons[this.icons.length - damage - 1];
		}

		return super.getIconFromDamage(damage);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage() > 0)
		{
			par3List.add(GCCoreUtil.translate("item.canister.LOX.name") +  ": " + (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()));
		}
	}
}
