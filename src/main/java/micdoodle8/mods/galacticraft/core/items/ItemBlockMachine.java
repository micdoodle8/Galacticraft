package micdoodle8.mods.galacticraft.core.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine2;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachineTiered;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

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
		return ClientProxyCore.galacticraftItem;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		int index = 0;
		int typenum = itemstack.getItemDamage() & 12;

		if (this.field_150939_a == GCBlocks.machineBase)
		{
			if (typenum == BlockMachine.COMPRESSOR_METADATA)
			{
				index = 3;
			}
		}
		else if (this.field_150939_a == GCBlocks.machineTiered)
		{
			if (typenum == BlockMachineTiered.ELECTRIC_FURNACE_METADATA)
			{
				return "tile.machine.2";
			}
			else if (typenum == BlockMachineTiered.STORAGE_MODULE_METADATA)
			{
				return "tile.machine.1";
			}		
			
			//Tier 2 versions of the same
			if (typenum == 8 + BlockMachineTiered.ELECTRIC_FURNACE_METADATA)
			{
				return "tile.machine.7";
			}
			else if (typenum == 8 + BlockMachineTiered.STORAGE_MODULE_METADATA)
			{
				return "tile.machine.8";
			}		
		}
		else
		{
			if (typenum == BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA)
			{
				index = 6;
			}
			else if (typenum ==  BlockMachine2.CIRCUIT_FABRICATOR_METADATA)
			{
				index = 5;
			}
			else if (typenum ==  BlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
			{
				index = 4;
			}
		}

		return this.field_150939_a.getUnlocalizedName() + "." + index;
	}

	@Override
	public String getUnlocalizedName()
	{
		return this.field_150939_a.getUnlocalizedName() + ".0";
	}
}
