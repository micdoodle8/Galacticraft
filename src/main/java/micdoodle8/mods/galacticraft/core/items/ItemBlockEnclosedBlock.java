package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.blocks.BlockEnclosed;
import micdoodle8.mods.galacticraft.core.blocks.BlockEnclosed.EnumEnclosedBlock;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreItemBlockEnclosedBlock.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ItemBlockEnclosedBlock extends ItemBlock
{
	public ItemBlockEnclosedBlock(Block block)
	{
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		String name = "";

		switch (par1ItemStack.getItemDamage())
		{
		case 0:
			name = "null";
			break;
		case 1:
			name = "oxygenPipe";
			break;
		case 2:
			name = "copperCable";
			break;
		case 3:
			name = "goldCable";
			break;
		case 4:
			name = "hvCable";
			break;
		case 5:
			name = "glassFibreCable";
			break;
		case 6:
			name = "lvCable";
			break;
		case 13:
			name = "meCable";
			break;
		case 14:
			name = "aluminumWire";
			break;
		case 15:
			name = "aluminumWireHeavy";
			break;
		default:
			try
			{
				name = BlockEnclosed.getTypeFromMeta(par1ItemStack.getItemDamage()).getPipeClass();
			}
			catch (Exception e)
			{
				name = "null";
			}
			break;
		}

		return this.field_150939_a.getUnlocalizedName() + "." + name;
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float par8, float par9, float par10)
	{
		EnumEnclosedBlock type = BlockEnclosed.getTypeFromMeta(itemstack.getItemDamage());

		if (type != null && type.getPipeClass() != null)
		{
			Block block = GCBlocks.sealableBlock;

			Block blockAt = world.getBlock(i, j, k);

			if (blockAt == Blocks.snow)
			{
				side = 1;
			}
			else if (blockAt != Blocks.vine && blockAt != Blocks.tallgrass && blockAt != Blocks.deadbush && (block == Blocks.air || !block.getMaterial().isReplaceable()))
			{
				if (side == 0)
				{
					j--;
				}
				if (side == 1)
				{
					j++;
				}
				if (side == 2)
				{
					k--;
				}
				if (side == 3)
				{
					k++;
				}
				if (side == 4)
				{
					i--;
				}
				if (side == 5)
				{
					i++;
				}
			}

			if (itemstack.stackSize == 0)
			{
				return false;
			}

			if (itemstack.stackSize == 0)
			{
				return false;
			}
			else if (!entityplayer.canPlayerEdit(i, j, k, side, itemstack))
			{
				return false;
			}
			else if (j == 255 && this.field_150939_a.getMaterial().isSolid())
			{
				return false;
			}
			else if (world.canPlaceEntityOnSide(block, i, j, k, false, side, entityplayer, itemstack))
			{
				// try
				// {
				// String name =
				// Character.toLowerCase(type.getPipeClass().charAt(0)) +
				// type.getPipeClass().substring(1);
				//
				// Class<?> clazz = Class.forName("buildcraft.BuildCraftCore");
				// Class<?> clazzConfig =
				// Class.forName("net.minecraftforge.common.Configuration");
				// Class<?> clazzBlockPipe =
				// Class.forName("buildcraft.transport.BlockGenericPipe");
				//
				// Object mainConfiguration =
				// clazz.getField("mainConfiguration").get(null);
				//
				// Method getItem = null;
				//
				// for (Method m : clazzConfig.getDeclaredMethods())
				// {
				// if (m.getName().equals("getItem") &&
				// m.getParameterTypes().length == 2)
				// {
				// getItem = m;
				// }
				// }
				//
				// // -1 is safe since they will have already been set
				// Property prop = (Property) getItem.invoke(mainConfiguration,
				// name + ".id", -1);
				//
				// int pipeID = prop.getInt(-1);
				//
				// Method createPipe = null;
				//
				// for (Method m : clazzBlockPipe.getDeclaredMethods())
				// {
				// if (m.getName().equals("createPipe") &&
				// m.getParameterTypes().length == 1)
				// {
				// createPipe = m;
				// }
				// }
				//
				// Object pipe = createPipe.invoke(null, pipeID + 256);
				//
				// if (pipe == null)
				// {
				// FMLLog.severe("Pipe failed to create during placement at " +
				// i + "," + j + "," + k);
				// return true;
				// }
				//
				// Method placePipe = null;
				//
				// for (Method m : clazzBlockPipe.getDeclaredMethods())
				// {
				// if (m.getName().equals("placePipe") &&
				// m.getParameterTypes().length == 7)
				// {
				// placePipe = m;
				// }
				// }
				//
				// Boolean b = (Boolean) placePipe.invoke(null, pipe, world, i,
				// j, k, blockID, type.getMetadata());
				//
				// if (b)
				// {
				// Block.blocksList[blockID].onBlockPlacedBy(world, i, j, k,
				// entityplayer, itemstack);
				// world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F,
				// block.stepSound.getPlaceSound(), (block.stepSound.getVolume()
				// + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
				// itemstack.stackSize--;
				// }
				//
				// return true;
				// }
				// catch (Exception e)
				// {
				// e.printStackTrace();
				// } TODO Fix BC enclosed pipes

				return false;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return super.onItemUse(itemstack, entityplayer, world, i, j, k, side, par8, par9, par10);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxy.galacticraftItem;
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}
}
