package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockOxygenCompressor;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreItemBlockOxygenCompressor.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreItemBlockOxygenCompressor extends ItemBlock
{
	public GCCoreItemBlockOxygenCompressor(int id)
	{
		super(id);
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
		return ClientProxyCore.galacticraftItem;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		int metadata = 0;

		if (itemstack.getItemDamage() >= GCCoreBlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
		{
			metadata = 1;
		}
		else if (itemstack.getItemDamage() >= GCCoreBlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
		{
			metadata = 0;
		}

		return Block.blocksList[this.getBlockID()].getUnlocalizedName() + "." + metadata;
	}

	@Override
	public String getUnlocalizedName()
	{
		return Block.blocksList[this.getBlockID()].getUnlocalizedName() + ".0";
	}
}
