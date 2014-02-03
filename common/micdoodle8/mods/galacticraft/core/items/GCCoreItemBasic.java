package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreItemBasic.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreItemBasic extends Item
{
	public static final String[] names = { "solar_module_0", "solar_module_1", "rawSilicon", "ingotCopper", "ingotTin", "ingotAluminum", "compressedCopper", "compressedTin", "compressedAluminum", "compressedSteel", "compressedBronze", "compressedIron", "waferSolar", "waferBasic", "waferAdvanced", "dehydratedApple", "dehydratedCarrot", "dehydratedMelon", "dehydratedPotato", "frequencyModule" };

	protected Icon[] icons = new Icon[GCCoreItemBasic.names.length];

	public GCCoreItemBasic(int id, String assetName)
	{
		super(id);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
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
		return ClientProxyCore.galacticraftItem;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		int i = 0;

		for (final String name : GCCoreItemBasic.names)
		{
			this.icons[i++] = iconRegister.registerIcon(this.getIconString() + "." + name);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		if (itemStack.getItemDamage() > 14 && itemStack.getItemDamage() < 19)
		{
			return this.getUnlocalizedName() + ".cannedFood";
		}

		return this.getUnlocalizedName() + "." + GCCoreItemBasic.names[itemStack.getItemDamage()];
	}

	@Override
	public Icon getIconFromDamage(int damage)
	{
		if (this.icons.length > damage)
		{
			return this.icons[damage];
		}

		return super.getIconFromDamage(damage);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < GCCoreItemBasic.names.length; i++)
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if (par1ItemStack.getItemDamage() > 14 && par1ItemStack.getItemDamage() < 19)
		{
			par3List.add(EnumColor.BRIGHT_GREEN + StatCollector.translateToLocal(this.getUnlocalizedName() + "." + GCCoreItemBasic.names[par1ItemStack.getItemDamage()] + ".name"));
		}
		else if (par1ItemStack.getItemDamage() == 19)
		{
			par3List.add(EnumColor.AQUA + "Can receive weak sound");
			par3List.add(EnumColor.AQUA + "waves and distant signals");
		}
	}

	public int getHealAmount(ItemStack par1ItemStack)
	{
		switch (par1ItemStack.getItemDamage())
		{
		case 15:
			return 8;
		case 16:
			return 8;
		case 17:
			return 4;
		case 18:
			return 2;
		default:
			return 0;
		}
	}

	public float getSaturationModifier(ItemStack par1ItemStack)
	{
		switch (par1ItemStack.getItemDamage())
		{
		case 15:
			return 0.3F;
		case 16:
			return 0.6F;
		case 17:
			return 0.3F;
		case 18:
			return 0.3F;
		default:
			return 0.0F;
		}
	}

	@Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (par1ItemStack.getItemDamage() > 14 && par1ItemStack.getItemDamage() < 19)
		{
			--par1ItemStack.stackSize;
			par3EntityPlayer.getFoodStats().addStats(this.getHealAmount(par1ItemStack), this.getSaturationModifier(par1ItemStack));
			par2World.playSoundAtEntity(par3EntityPlayer, "random.burp", 0.5F, par2World.rand.nextFloat() * 0.1F + 0.9F);
			par3EntityPlayer.dropPlayerItem(new ItemStack(GCCoreItems.canister, 1, 0));
			return par1ItemStack;
		}

		return super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		if (par1ItemStack.getItemDamage() > 14 && par1ItemStack.getItemDamage() < 19)
		{
			return 32;
		}

		return super.getMaxItemUseDuration(par1ItemStack);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
	{
		if (par1ItemStack.getItemDamage() > 14 && par1ItemStack.getItemDamage() < 19)
		{
			return EnumAction.eat;
		}

		return super.getItemUseAction(par1ItemStack);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (par1ItemStack.getItemDamage() > 14 && par1ItemStack.getItemDamage() < 19 && par3EntityPlayer.canEat(false))
		{
			par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		}

		return par1ItemStack;
	}
}
