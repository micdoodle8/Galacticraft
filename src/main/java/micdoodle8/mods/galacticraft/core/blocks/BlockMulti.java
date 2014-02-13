package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * BlockMulti.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class BlockMulti extends BlockContainer
{
	public String textureName = null;

	public BlockMulti()
	{
		super(GCCoreBlocks.machine);
		this.setHardness(0.8F);
		this.setBlockName("multiBlock");
	}

	@Override
	public BlockMulti setBlockTextureName(String name)
	{
		this.textureName = name;
		return this;
	}

	public void makeFakeBlock(World worldObj, Vector3 position, Vector3 mainBlock)
	{
		worldObj.setBlock(position.intX(), position.intY(), position.intZ(), this);
		((TileEntityMulti) position.getTileEntity(worldObj)).setMainBlock(mainBlock);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		if (this.textureName != null)
		{
			this.blockIcon = iconRegister.registerIcon(this.textureName);
		}
		else
		{
			super.registerBlockIcons(iconRegister);
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		if (tileEntity instanceof TileEntityMulti)
		{
			((TileEntityMulti) tileEntity).onBlockRemoval();
		}

		super.breakBlock(world, x, y, z, par5, par6);
	}

	/**
	 * Called when the block is right clicked by the player. This modified
	 * version detects electric items and wrench actions on your machine block.
	 * Do not override this function. Use machineActivated instead! (It does the
	 * same thing)
	 */
	@Override
	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		TileEntityMulti tileEntity = (TileEntityMulti) par1World.getTileEntity(x, y, z);
		return tileEntity.onBlockActivated(par1World, x, y, z, par5EntityPlayer);
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}

	@Override
	public int getRenderType()
	{
		return -1;
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
	public TileEntity createNewTileEntity(World var1, int meta)
	{
		return new TileEntityMulti();
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World par1World, int x, int y, int z)
	{
		TileEntity tileEntity = par1World.getTileEntity(x, y, z);
		Vector3 mainBlockPosition = ((TileEntityMulti) tileEntity).mainBlockPosition;

		if (mainBlockPosition != null)
		{
			Block mainBlockID = par1World.getBlock(mainBlockPosition.intX(), mainBlockPosition.intY(), mainBlockPosition.intZ());

			if (mainBlockID != Blocks.air)
			{
				return mainBlockID.getPickBlock(target, par1World, mainBlockPosition.intX(), mainBlockPosition.intY(), mainBlockPosition.intZ());
			}
		}

		return null;
	}
}
