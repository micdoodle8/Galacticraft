package micdoodle8.mods.galacticraft.core.items;

import java.lang.reflect.Method;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.blocks.BlockEnclosed;
import micdoodle8.mods.galacticraft.core.blocks.BlockEnclosed.EnumEnclosedBlock;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemBlockEnclosed extends ItemBlock
{
	public ItemBlockEnclosed(Block block)
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
        int metadata = this.getMetadata(itemstack.getItemDamage());
		EnumEnclosedBlock type = BlockEnclosed.getTypeFromMeta(metadata);

		if (type != null && type.getPipeClass() != null)
		{
			Block block = world.getBlock(i, j, k);

			if (block == Blocks.snow)
			{
				side = 1;
			}
			else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, i, j, k))
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
	            int j1 = this.field_150939_a.onBlockPlaced(world, i, j, k, side, par8, par9, par10, metadata);
				block.onBlockPlacedBy(world, i, j, k, entityplayer, itemstack);

	            if (placeBlockAt(itemstack, entityplayer, world, i, j, k, side, par8, par9, par10, j1))
	            {
	                world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
	                --itemstack.stackSize;
	                
	                if (metadata >= EnumEnclosedBlock.BC_ITEM_STONEPIPE.getMetadata() && metadata <= EnumEnclosedBlock.BC_POWER_GOLDPIPE.getMetadata())
	        		{
	        			if (CompatibilityManager.isBCraftLoaded())
	        			{
	        				try
	        				{
	        					//------
	        					//This section makes these three calls to initialise the TileEntity: 
	        					//	Pipe pipe = BlockGenericPipe.createPipe(Item);
	        					//  tilePipe.initialize(pipe);
	        					//	tilePipe.sendUpdateToClient();

	        					Class<?> clazzBC = Class.forName("buildcraft.BuildCraftTransport");
	        					Class<?> clazzBlockPipe = Class.forName("buildcraft.transport.BlockGenericPipe");
	        					Class<?> clazzTilePipe = Class.forName("buildcraft.transport.TileGenericPipe");
	        					TileEntity tilePipe = world.getTileEntity(i,  j,  k);

	        					String pipeName = EnumEnclosedBlock.values()[metadata].getPipeClass();
	        					pipeName = pipeName.substring(0,1).toLowerCase()+pipeName.substring(1);

	        					Item pipeItem = (Item) clazzBC.getField(pipeName).get(null);
	        					Method createPipe = null;
	        					for (Method m : clazzBlockPipe.getDeclaredMethods())
	        					{
	        						if (m.getName().equals("createPipe") && m.getParameterTypes().length == 1)
	        						{
	        							createPipe = m;
	        							break;
	        						}
	        					}
	        					Object pipe = createPipe.invoke(null, pipeItem);
	        					Method initializePipe = null;
	        					for (Method m : clazzTilePipe.getDeclaredMethods())
	        					{
	        						if (m.getName().equals("initialize") && m.getParameterTypes().length == 1)
	        						{
	        							initializePipe = m;
	        							break;
	        						}
	        					}
	        					initializePipe.invoke(tilePipe, pipe);
	        					clazzTilePipe.getMethod("sendUpdateToClient").invoke(tilePipe);
	        					//------
	        				}
	        				catch (Exception e)
	        				{
	        					e.printStackTrace();
	        				}
	        			}
	        		}
	            }

	            return true;

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
		return ClientProxyCore.galacticraftItem;
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}
}
