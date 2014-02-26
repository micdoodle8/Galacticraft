package micdoodle8.mods.galacticraft.planets.mars.blocks;

import java.util.Iterator;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTreasureChestMars;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsBlockT2TreasureChest.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class BlockTreasureChestTier2 extends BlockContainer implements ITileEntityProvider
{
	private final Random random = new Random();

	protected BlockTreasureChestTier2()
	{
		super(Material.rock);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
	}

	@Override
	public float getBlockHardness(World par1World, int par2, int par3, int par4)
	{
		return -1.0F;
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftPlanets.creativeTab;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return GalacticraftPlanets.proxyMars.getTreasureRenderID();
	}

	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
	{
		super.onBlockAdded(par1World, par2, par3, par4);
		this.unifyAdjacentChests(par1World, par2, par3, par4);
		Block var5 = par1World.getBlock(par2, par3, par4 - 1);
		Block var6 = par1World.getBlock(par2, par3, par4 + 1);
		Block var7 = par1World.getBlock(par2 - 1, par3, par4);
		Block var8 = par1World.getBlock(par2 + 1, par3, par4);

		if (var5 == this)
		{
			this.unifyAdjacentChests(par1World, par2, par3, par4 - 1);
		}

		if (var6 == this)
		{
			this.unifyAdjacentChests(par1World, par2, par3, par4 + 1);
		}

		if (var7 == this)
		{
			this.unifyAdjacentChests(par1World, par2 - 1, par3, par4);
		}

		if (var8 == this)
		{
			this.unifyAdjacentChests(par1World, par2 + 1, par3, par4);
		}
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack stack)
	{
		Block var6 = par1World.getBlock(par2, par3, par4 - 1);
		Block var7 = par1World.getBlock(par2, par3, par4 + 1);
		Block var8 = par1World.getBlock(par2 - 1, par3, par4);
		Block var9 = par1World.getBlock(par2 + 1, par3, par4);
		byte var10 = 0;
		final int var11 = MathHelper.floor_double(par5EntityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		if (var11 == 0)
		{
			var10 = 2;
		}

		if (var11 == 1)
		{
			var10 = 5;
		}

		if (var11 == 2)
		{
			var10 = 3;
		}

		if (var11 == 3)
		{
			var10 = 4;
		}

		if (var6 != this && var7 != this && var8 != this && var9 != this)
		{
			par1World.setBlockMetadataWithNotify(par2, par3, par4, var10, 3);
		}
		else
		{
			if ((var6 == this || var7 == this) && (var10 == 4 || var10 == 5))
			{
				if (var6 == this)
				{
					par1World.setBlockMetadataWithNotify(par2, par3, par4 - 1, var10, 3);
				}
				else
				{
					par1World.setBlockMetadataWithNotify(par2, par3, par4 + 1, var10, 3);
				}

				par1World.setBlockMetadataWithNotify(par2, par3, par4, var10, 3);
			}

			if ((var8 == this || var9 == this) && (var10 == 2 || var10 == 3))
			{
				if (var8 == this)
				{
					par1World.setBlockMetadataWithNotify(par2 - 1, par3, par4, var10, 3);
				}
				else
				{
					par1World.setBlockMetadataWithNotify(par2 + 1, par3, par4, var10, 3);
				}

				par1World.setBlockMetadataWithNotify(par2, par3, par4, var10, 3);
			}
		}
	}

	public void unifyAdjacentChests(World par1World, int par2, int par3, int par4)
	{
		if (!par1World.isRemote)
		{
			Block var5 = par1World.getBlock(par2, par3, par4 - 1);
			Block var6 = par1World.getBlock(par2, par3, par4 + 1);
			Block var7 = par1World.getBlock(par2 - 1, par3, par4);
			Block var8 = par1World.getBlock(par2 + 1, par3, par4);
			Block var10;
			Block var11;
			byte var13;
			int var14;

			if (var5 != this && var6 != this)
			{
				if (var7 != this && var8 != this)
				{
					var13 = 3;

					if (var5.func_149730_j() && !var6.func_149730_j())
					{
						var13 = 3;
					}

					if (var6.func_149730_j() && !var5.func_149730_j())
					{
						var13 = 2;
					}

					if (var7.func_149730_j() && !var8.func_149730_j())
					{
						var13 = 5;
					}

					if (var8.func_149730_j() && !var7.func_149730_j())
					{
						var13 = 4;
					}
				}
				else
				{
					var10 = par1World.getBlock(var7 == this ? par2 - 1 : par2 + 1, par3, par4 - 1);
					var11 = par1World.getBlock(var7 == this ? par2 - 1 : par2 + 1, par3, par4 + 1);
					var13 = 3;
					if (var7 == this)
					{
						var14 = par1World.getBlockMetadata(par2 - 1, par3, par4);
					}
					else
					{
						var14 = par1World.getBlockMetadata(par2 + 1, par3, par4);
					}

					if (var14 == 2)
					{
						var13 = 2;
					}

					if ((var5.func_149730_j() || var10.func_149730_j()) && !var6.func_149730_j() && !var11.func_149730_j())
					{
						var13 = 3;
					}

					if ((var6.func_149730_j() || var11.func_149730_j()) && !var5.func_149730_j() && !var10.func_149730_j())
					{
						var13 = 2;
					}
				}
			}
			else
			{
				var10 = par1World.getBlock(par2 - 1, par3, var5 == this ? par4 - 1 : par4 + 1);
				var11 = par1World.getBlock(par2 + 1, par3, var5 == this ? par4 - 1 : par4 + 1);
				var13 = 5;
				if (var5 == this)
				{
					var14 = par1World.getBlockMetadata(par2, par3, par4 - 1);
				}
				else
				{
					var14 = par1World.getBlockMetadata(par2, par3, par4 + 1);
				}

				if (var14 == 4)
				{
					var13 = 4;
				}

				if ((var7.func_149730_j() || var10.func_149730_j()) && !var8.func_149730_j() && !var11.func_149730_j())
				{
					var13 = 5;
				}

				if ((var8.func_149730_j() || var11.func_149730_j()) && !var7.func_149730_j() && !var10.func_149730_j())
				{
					var13 = 4;
				}
			}

			par1World.setBlockMetadataWithNotify(par2, par3, par4, var13, 3);
		}
	}

	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
	{
		int var5 = 0;

		if (par1World.getBlock(par2 - 1, par3, par4) == this)
		{
			++var5;
		}

		if (par1World.getBlock(par2 + 1, par3, par4) == this)
		{
			++var5;
		}

		if (par1World.getBlock(par2, par3, par4 - 1) == this)
		{
			++var5;
		}

		if (par1World.getBlock(par2, par3, par4 + 1) == this)
		{
			++var5;
		}

		return var5 > 1 ? false : this.isThereANeighborChest(par1World, par2 - 1, par3, par4) ? false : this.isThereANeighborChest(par1World, par2 + 1, par3, par4) ? false : this.isThereANeighborChest(par1World, par2, par3, par4 - 1) ? false : !this.isThereANeighborChest(par1World, par2, par3, par4 + 1);
	}

	/**
	 * Checks the neighbor blocks to see if there is a chest there. Args: world,
	 * x, y, z
	 */
	private boolean isThereANeighborChest(World par1World, int par2, int par3, int par4)
	{
		return par1World.getBlock(par2, par3, par4) != this ? false : par1World.getBlock(par2 - 1, par3, par4) == this ? true : par1World.getBlock(par2 + 1, par3, par4) == this ? true : par1World.getBlock(par2, par3, par4 - 1) == this ? true : par1World.getBlock(par2, par3, par4 + 1) == this;
	}

	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5)
	{
		super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
		final TileEntityTreasureChest var6 = (TileEntityTreasureChest) par1World.getTileEntity(par2, par3, par4);

		if (var6 != null)
		{
			var6.updateContainingBlockInfo();
		}
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
	{
		final TileEntityTreasureChest var7 = (TileEntityTreasureChest) par1World.getTileEntity(par2, par3, par4);

		if (var7 != null)
		{
			for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
			{
				final ItemStack var9 = var7.getStackInSlot(var8);

				if (var9 != null)
				{
					final float var10 = this.random.nextFloat() * 0.8F + 0.1F;
					final float var11 = this.random.nextFloat() * 0.8F + 0.1F;
					EntityItem var14;

					for (final float var12 = this.random.nextFloat() * 0.8F + 0.1F; var9.stackSize > 0; par1World.spawnEntityInWorld(var14))
					{
						int var13 = this.random.nextInt(21) + 10;

						if (var13 > var9.stackSize)
						{
							var13 = var9.stackSize;
						}

						var9.stackSize -= var13;
						var14 = new EntityItem(par1World, par2 + var10, par3 + var11, par4 + var12, new ItemStack(var9.getItem(), var13, var9.getItemDamage()));
						final float var15 = 0.05F;
						var14.motionX = (float) this.random.nextGaussian() * var15;
						var14.motionY = (float) this.random.nextGaussian() * var15 + 0.2F;
						var14.motionZ = (float) this.random.nextGaussian() * var15;

						if (var9.hasTagCompound())
						{
							var14.getEntityItem().setTagCompound((NBTTagCompound) var9.getTagCompound().copy());
						}
					}
				}
			}
		}

		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		Object var10 = par1World.getTileEntity(par2, par3, par4);

		if (var10 == null)
		{
			return true;
		}
		else if (par1World.isSideSolid(par2, par3 + 1, par4, ForgeDirection.DOWN))
		{
			return true;
		}
		else if (BlockTreasureChestTier2.isOcelotBlockingChest(par1World, par2, par3, par4))
		{
			return true;
		}
		else if (par1World.getBlock(par2 - 1, par3, par4) == this && (par1World.isSideSolid(par2 - 1, par3 + 1, par4, ForgeDirection.DOWN) || BlockTreasureChestTier2.isOcelotBlockingChest(par1World, par2 - 1, par3, par4)))
		{
			return true;
		}
		else if (par1World.getBlock(par2 + 1, par3, par4) == this && (par1World.isSideSolid(par2 + 1, par3 + 1, par4, ForgeDirection.DOWN) || BlockTreasureChestTier2.isOcelotBlockingChest(par1World, par2 + 1, par3, par4)))
		{
			return true;
		}
		else if (par1World.getBlock(par2, par3, par4 - 1) == this && (par1World.isSideSolid(par2, par3 + 1, par4 - 1, ForgeDirection.DOWN) || BlockTreasureChestTier2.isOcelotBlockingChest(par1World, par2, par3, par4 - 1)))
		{
			return true;
		}
		else if (par1World.getBlock(par2, par3, par4 + 1) == this && (par1World.isSideSolid(par2, par3 + 1, par4 + 1, ForgeDirection.DOWN) || BlockTreasureChestTier2.isOcelotBlockingChest(par1World, par2, par3, par4 + 1)))
		{
			return true;
		}
		else
		{
			if (par1World.getBlock(par2 - 1, par3, par4) == this)
			{
				var10 = new InventoryLargeChest("container.chestDouble", (TileEntityTreasureChest) par1World.getTileEntity(par2 - 1, par3, par4), (IInventory) var10);
			}

			if (par1World.getBlock(par2 + 1, par3, par4) == this)
			{
				var10 = new InventoryLargeChest("container.chestDouble", (IInventory) var10, (TileEntityTreasureChest) par1World.getTileEntity(par2 + 1, par3, par4));
			}

			if (par1World.getBlock(par2, par3, par4 - 1) == this)
			{
				var10 = new InventoryLargeChest("container.chestDouble", (TileEntityTreasureChest) par1World.getTileEntity(par2, par3, par4 - 1), (IInventory) var10);
			}

			if (par1World.getBlock(par2, par3, par4 + 1) == this)
			{
				var10 = new InventoryLargeChest("container.chestDouble", (IInventory) var10, (TileEntityTreasureChest) par1World.getTileEntity(par2, par3, par4 + 1));
			}

			if (par1World.isRemote)
			{
				return true;
			}
			else
			{
				par5EntityPlayer.displayGUIChest((IInventory) var10);
				return true;
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World par1World, int meta)
	{
		return new TileEntityTreasureChestMars();
	}

	@SuppressWarnings("rawtypes")
	public static boolean isOcelotBlockingChest(World par0World, int par1, int par2, int par3)
	{
		final Iterator var4 = par0World.getEntitiesWithinAABB(EntityOcelot.class, AxisAlignedBB.getAABBPool().getAABB(par1, par2 + 1, par3, par1 + 1, par2 + 2, par3 + 1)).iterator();
		EntityOcelot var6;

		do
		{
			if (!var4.hasNext())
			{
				return false;
			}

			final EntityOcelot var5 = (EntityOcelot) var4.next();
			var6 = var5;
		}
		while (!var6.isSitting());

		return true;
	}
}
