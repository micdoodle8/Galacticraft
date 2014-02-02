package micdoodle8.mods.galacticraft.mars.items;

import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsItemBlock.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsItemBlock extends ItemBlock
{
	public GCMarsItemBlock(int i)
	{
		super(i);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int meta)
	{
		return meta;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxyCore.galacticraftItem;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		String name = "";

		switch (itemstack.getItemDamage())
		{
		case 0:
		{
			name = "coppermars";
			break;
		}
		case 1:
		{
			name = "tinmars";
			break;
		}
		case 3:
		{
			name = "ironmars";
			break;
		}
		case 2:
		{
			name = "deshmars";
			break;
		}
		case 4:
		{
			name = "marscobblestone";
			break;
		}
		case 5:
		{
			name = "marsgrass";
			break;
		}
		case 6:
		{
			name = "marsdirt";
			break;
		}
		case 7:
		{
			name = "marsdungeon";
			break;
		}
		case 8:
		{
			name = "marsdeco";
			break;
		}
		case 9:
		{
			name = "marsstone";
			break;
		}
		default:
			name = "null";
		}

		return Block.blocksList[this.getBlockID()].getUnlocalizedName() + "." + name;
	}

	@Override
	public CreativeTabs getCreativeTab()
	{
		return GalacticraftMars.galacticraftMarsTab;
	}

	@Override
	public String getUnlocalizedName()
	{
		return Block.blocksList[this.getBlockID()].getUnlocalizedName() + ".0";
	}
}
