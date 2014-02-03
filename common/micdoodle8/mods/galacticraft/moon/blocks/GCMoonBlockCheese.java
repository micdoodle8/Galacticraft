package micdoodle8.mods.galacticraft.moon.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMoonBlockCheese.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMoonBlockCheese extends Block
{
	Icon[] cheeseIcons;

	public GCMoonBlockCheese(int par1)
	{
		super(par1, Material.cake);
		this.setTickRandomly(true);
		this.disableStats();
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.cheeseIcons = new Icon[3];
		this.cheeseIcons[0] = par1IconRegister.registerIcon("galacticraftmoon:cheese_1");
		this.cheeseIcons[1] = par1IconRegister.registerIcon("galacticraftmoon:cheese_2");
		this.cheeseIcons[2] = par1IconRegister.registerIcon("galacticraftmoon:cheese_3");
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y,
	 * z
	 */
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		final int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
		final float var6 = 0.0625F;
		final float var7 = (1 + var5 * 2) / 16.0F;
		final float var8 = 0.5F;
		this.setBlockBounds(var7, 0.0F, var6, 1.0F - var6, var8, 1.0F - var6);
	}

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	@Override
	public void setBlockBoundsForItemRender()
	{
		final float var1 = 0.0625F;
		final float var2 = 0.5F;
		this.setBlockBounds(var1, 0.0F, var1, 1.0F - var1, var2, 1.0F - var1);
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this
	 * box can change after the pool has been cleared to be reused)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		final int var5 = par1World.getBlockMetadata(par2, par3, par4);
		final float var6 = 0.0625F;
		final float var7 = (1 + var5 * 2) / 16.0F;
		final float var8 = 0.5F;
		return AxisAlignedBB.getAABBPool().getAABB(par2 + var7, par3, par4 + var6, par2 + 1 - var6, par3 + var8 - var6, par4 + 1 - var6);
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns the bounding box of the wired rectangular prism to render.
	 */
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		final int var5 = par1World.getBlockMetadata(par2, par3, par4);
		final float var6 = 0.0625F;
		final float var7 = (1 + var5 * 2) / 16.0F;
		final float var8 = 0.5F;
		return AxisAlignedBB.getAABBPool().getAABB(par2 + var7, par3, par4 + var6, par2 + 1 - var6, par3 + var8, par4 + 1 - var6);
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	@Override
	public Icon getIcon(int par1, int par2)
	{
		return par1 == 1 ? this.cheeseIcons[0] : par1 == 0 ? this.cheeseIcons[0] : par2 > 0 && par1 == 4 ? this.cheeseIcons[2] : this.cheeseIcons[1];
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether
	 * or not to render the shared face of two adjacent blocks and also whether
	 * the player can attach torches, redstone wire, etc to this block.
	 */
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		this.eatCakeSlice(par1World, par2, par3, par4, par5EntityPlayer);
		return true;
	}

	/**
	 * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
	 */
	@Override
	public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
	{
		this.eatCakeSlice(par1World, par2, par3, par4, par5EntityPlayer);
	}

	/**
	 * Heals the player and removes a slice from the cake.
	 */
	private void eatCakeSlice(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
	{
		if (par5EntityPlayer.canEat(false))
		{
			par5EntityPlayer.getFoodStats().addStats(2, 0.1F);
			final int l = par1World.getBlockMetadata(par2, par3, par4) + 1;

			if (l >= 6)
			{
				par1World.setBlockToAir(par2, par3, par4);
			}
			else
			{
				par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);
			}
		}
	}

	/**
	 * Checks to see if its valid to put this block at the specified
	 * coordinates. Args: world, x, y, z
	 */
	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
	{
		return !super.canPlaceBlockAt(par1World, par2, par3, par4) ? false : this.canBlockStay(par1World, par2, par3, par4);
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z,
	 * neighbor blockID
	 */
	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
	{
		if (!this.canBlockStay(par1World, par2, par3, par4))
		{
			par1World.setBlockToAir(par2, par3, par4);
		}
	}

	/**
	 * Can this block stay at this position. Similar to canPlaceBlockAt except
	 * gets checked often with plants.
	 */
	@Override
	public boolean canBlockStay(World par1World, int par2, int par3, int par4)
	{
		return par1World.getBlockMaterial(par2, par3 - 1, par4).isSolid();
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
	 */
	public int idPicked(World par1World, int par2, int par3, int par4)
	{
		return Item.cake.itemID;
	}
}
