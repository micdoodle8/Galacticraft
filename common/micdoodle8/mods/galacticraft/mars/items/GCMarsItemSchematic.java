package micdoodle8.mods.galacticraft.mars.items;

import java.util.List;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicItem;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemSchematic;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsItemSchematic.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsItemSchematic extends GCCoreItemSchematic implements ISchematicItem
{
	protected Icon[] icons = new Icon[1];

	public static final String[] names = { "schematic_rocketT3", "schematic_rocket_cargo" };

	public GCMarsItemSchematic(int itemID)
	{
		super(itemID, "schematic");
	}

	@Override
	public CreativeTabs getCreativeTab()
	{
		return GalacticraftMars.galacticraftMarsTab;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < GCMarsItemSchematic.names.length; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		this.icons = new Icon[GCMarsItemSchematic.names.length];

		for (int i = 0; i < GCMarsItemSchematic.names.length; i++)
		{
			this.icons[i] = iconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + GCMarsItemSchematic.names[i]);
		}

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
				par3List.add(StatCollector.translateToLocal("schematic.rocketT3.name"));
				par3List.add(EnumColor.DARK_AQUA + "COMING SOON");
				break;
			case 1:
				par3List.add(StatCollector.translateToLocal("schematic.cargoRocket.name"));
				break;
			}
		}
	}
}
