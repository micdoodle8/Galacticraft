package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * GCCoreBlockAdvanced.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public abstract class GCCoreBlockAdvancedTile extends GCCoreBlockAdvanced implements ITileEntityProvider
{
	public GCCoreBlockAdvancedTile(int par1, Material par3Material)
	{
		super(par1, par3Material);
		this.isBlockContainer = true;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		int id = this.blockID;

		if (id == 0)
		{
			return null;
		}

		Item item = Item.itemsList[id];
		if (item == null)
		{
			return null;
		}

		return new ItemStack(id, 1, this.getDamageValue(world, x, y, z));
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return null;
	}

	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
	{
		super.onBlockAdded(par1World, par2, par3, par4);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		this.dropEntireInventory(world, x, y, z, par5, par6);
		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6)
	{
		super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
		TileEntity tileentity = par1World.getBlockTileEntity(par2, par3, par4);
		return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
	}

	public void dropEntireInventory(World world, int x, int y, int z, int par5, int par6)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity != null)
		{
			if (tileEntity instanceof IInventory)
			{
				IInventory inventory = (IInventory) tileEntity;

				for (int var6 = 0; var6 < inventory.getSizeInventory(); ++var6)
				{
					ItemStack var7 = inventory.getStackInSlot(var6);

					if (var7 != null)
					{
						Random random = new Random();
						float var8 = random.nextFloat() * 0.8F + 0.1F;
						float var9 = random.nextFloat() * 0.8F + 0.1F;
						float var10 = random.nextFloat() * 0.8F + 0.1F;

						while (var7.stackSize > 0)
						{
							int var11 = random.nextInt(21) + 10;

							if (var11 > var7.stackSize)
							{
								var11 = var7.stackSize;
							}

							var7.stackSize -= var11;
							EntityItem var12 = new EntityItem(world, x + var8, y + var9, z + var10, new ItemStack(var7.itemID, var11, var7.getItemDamage()));

							if (var7.hasTagCompound())
							{
								var12.getEntityItem().setTagCompound((NBTTagCompound) var7.getTagCompound().copy());
							}

							float var13 = 0.05F;
							var12.motionX = (float) random.nextGaussian() * var13;
							var12.motionY = (float) random.nextGaussian() * var13 + 0.2F;
							var12.motionZ = (float) random.nextGaussian() * var13;
							world.spawnEntityInWorld(var12);
						}
					}
				}
			}
		}
	}
}
