package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay;
import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay.ElectricUnit;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockAluminumWire;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreItemBlockAluminumWire.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreItemBlockAluminumWire extends ItemBlock
{
	public GCCoreItemBlockAluminumWire(int id)
	{
		super(id);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		switch (par1ItemStack.getItemDamage())
		{
		case 0:
			par3List.add("Resistance: " + ElectricityDisplay.getDisplay(0.05F, ElectricUnit.RESISTANCE));
			par3List.add("Max Amps: " + ElectricityDisplay.getDisplay(200.0F, ElectricUnit.AMPERE));
			break;
		case 1:
			par3List.add("Resistance: " + ElectricityDisplay.getDisplay(0.025F, ElectricUnit.RESISTANCE));
			par3List.add("Max Amps: " + ElectricityDisplay.getDisplay(400.0F, ElectricUnit.AMPERE));
			break;
		default:
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return Block.blocksList[this.getBlockID()].getIcon(0, par1);
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		String name = "";

		switch (par1ItemStack.getItemDamage())
		{
		case 0:
			name = GCCoreBlockAluminumWire.names[0];
			break;
		case 1:
			name = GCCoreBlockAluminumWire.names[1];
			break;
		default:
			name = "null";
			break;
		}

		return "tile." + name;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxyCore.galacticraftItem;
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}
}
