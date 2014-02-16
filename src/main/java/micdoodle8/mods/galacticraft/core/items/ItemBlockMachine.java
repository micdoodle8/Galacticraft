package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.blocks.BlockMachine;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine2;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreItemBlockMachine.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ItemBlockMachine extends ItemBlock
{
	public ItemBlockMachine(Block block)
	{
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxy.galacticraftItem;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		int metadata = 0;

		if (this.field_150939_a == GCBlocks.machineBase)
		{
			if (itemstack.getItemDamage() >= BlockMachine.COMPRESSOR_METADATA)
			{
				metadata = 3;
			}
			else if (itemstack.getItemDamage() >= BlockMachine.ELECTRIC_FURNACE_METADATA)
			{
				metadata = 2;
			}
			else if (itemstack.getItemDamage() >= BlockMachine.STORAGE_MODULE_METADATA)
			{
				metadata = 1;
			}
		}
		else
		{
			if (itemstack.getItemDamage() >= BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA)
			{
				metadata = 6;
			}
			else if (itemstack.getItemDamage() >= BlockMachine2.CIRCUIT_FABRICATOR_METADATA)
			{
				metadata = 5;
			}
			else if (itemstack.getItemDamage() >= BlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
			{
				metadata = 4;
			}
		}

		return this.field_150939_a.getUnlocalizedName() + "." + metadata;
	}

	@Override
	public String getUnlocalizedName()
	{
		return this.field_150939_a.getUnlocalizedName() + ".0";
	}
}
