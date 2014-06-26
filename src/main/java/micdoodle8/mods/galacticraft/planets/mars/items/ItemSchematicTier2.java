package micdoodle8.mods.galacticraft.planets.mars.items;

import java.util.List;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicItem;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemSchematic;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSchematicTier2 extends ItemSchematic implements ISchematicItem
{
	protected IIcon[] icons = new IIcon[1];

	public static final String[] names = { "schematic_rocketT3", "schematic_rocket_cargo" };

	public ItemSchematicTier2()
	{
		super("schematic");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public CreativeTabs getCreativeTab()
	{
		return GalacticraftCore.galacticraftItemsTab;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < ItemSchematicTier2.names.length; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		this.icons = new IIcon[ItemSchematicTier2.names.length];

		for (int i = 0; i < ItemSchematicTier2.names.length; i++)
		{
			this.icons[i] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + ItemSchematicTier2.names[i]);
		}

	}

	@Override
	public IIcon getIconFromDamage(int damage)
	{
		if (this.icons.length > damage)
		{
			return this.icons[damage];
		}

		return super.getIconFromDamage(damage);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if (par2EntityPlayer.worldObj.isRemote)
		{
			switch (par1ItemStack.getItemDamage())
			{
			case 0:
				par3List.add(GCCoreUtil.translate("schematic.rocketT3.name"));
				par3List.add(EnumColor.DARK_AQUA + "COMING SOON");
				break;
			case 1:
				par3List.add(GCCoreUtil.translate("schematic.cargoRocket.name"));
				break;
			}
		}
	}
}
