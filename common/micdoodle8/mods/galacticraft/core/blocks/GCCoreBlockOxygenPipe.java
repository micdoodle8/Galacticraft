package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.tile.IColorable;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenPipe;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreBlockOxygenPipe.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockOxygenPipe extends GCCoreBlockTransmitter
{
	private Icon[] pipeIcons = new Icon[16];

	public GCCoreBlockOxygenPipe(int id, String assetName)
	{
		super(id, Material.glass);
		this.setHardness(0.3F);
		this.setStepSound(Block.soundGlassFootstep);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setUnlocalizedName(assetName);
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
	{
		final GCCoreTileEntityOxygenPipe tile = (GCCoreTileEntityOxygenPipe) par1World.getBlockTileEntity(par2, par3, par4);

		if (tile != null && tile.getColor() != 15)
		{
			final float f = 0.7F;
			final double d0 = par1World.rand.nextFloat() * f + (1.0F - f) * 0.5D;
			final double d1 = par1World.rand.nextFloat() * f + (1.0F - f) * 0.2D + 0.6D;
			final double d2 = par1World.rand.nextFloat() * f + (1.0F - f) * 0.5D;
			final EntityItem entityitem = new EntityItem(par1World, par2 + d0, par3 + d1, par4 + d2, new ItemStack(Item.dyePowder, 1, tile.getColor()));
			entityitem.delayBeforeCanPickup = 10;
			par1World.spawnEntityInWorld(entityitem);
		}

		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
	{
		super.onNeighborBlockChange(world, x, y, z, blockID);
		world.markBlockForRenderUpdate(x, y, z);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftTab;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int x, int y, int z, int par5)
	{
		Vector3 thisVec = new Vector3(x, y, z).modifyPositionFromSide(ForgeDirection.getOrientation(par5));
		final int idAtSide = thisVec.getBlockID(par1IBlockAccess);

		final GCCoreTileEntityOxygenPipe tileEntity = (GCCoreTileEntityOxygenPipe) par1IBlockAccess.getBlockTileEntity(x, y, z);

		if (idAtSide == GCCoreBlocks.oxygenPipe.blockID && ((GCCoreTileEntityOxygenPipe) thisVec.getTileEntity(par1IBlockAccess)).getColor() == tileEntity.getColor())
		{
			return this.pipeIcons[15];
		}

		return this.pipeIcons[tileEntity.getColor()];
	}

	@Override
	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		final GCCoreTileEntityOxygenPipe tileEntity = (GCCoreTileEntityOxygenPipe) par1World.getBlockTileEntity(x, y, z);

		if (!par1World.isRemote)
		{
			final ItemStack stack = par5EntityPlayer.inventory.getCurrentItem();

			if (stack != null)
			{
				if (stack.getItem() instanceof ItemDye)
				{
					final int dyeColor = par5EntityPlayer.inventory.getCurrentItem().getItemDamageForDisplay();

					final byte colorBefore = tileEntity.getColor();

					tileEntity.setColor((byte) dyeColor);

					if (colorBefore != (byte) dyeColor && !par5EntityPlayer.capabilities.isCreativeMode && --par5EntityPlayer.inventory.getCurrentItem().stackSize == 0)
					{
						par5EntityPlayer.inventory.mainInventory[par5EntityPlayer.inventory.currentItem] = null;
					}

					if (colorBefore != (byte) dyeColor && colorBefore != 15)
					{
						final float f = 0.7F;
						final double d0 = par1World.rand.nextFloat() * f + (1.0F - f) * 0.5D;
						final double d1 = par1World.rand.nextFloat() * f + (1.0F - f) * 0.2D + 0.6D;
						final double d2 = par1World.rand.nextFloat() * f + (1.0F - f) * 0.5D;
						final EntityItem entityitem = new EntityItem(par1World, x + d0, y + d1, z + d2, new ItemStack(Item.dyePowder, 1, colorBefore));
						entityitem.delayBeforeCanPickup = 10;
						par1World.spawnEntityInWorld(entityitem);
					}

					GCCorePacketManager.sendPacketToClients(GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, tileEntity, tileEntity.getColor(), (byte) -1));

					for (final ForgeDirection dir : ForgeDirection.values())
					{
						Vector3 vec = new Vector3(tileEntity);
						vec = vec.modifyPositionFromSide(dir);
						final TileEntity tileAt = vec.getTileEntity(tileEntity.worldObj);

						if (tileAt != null && tileAt instanceof IColorable)
						{
							((IColorable) tileAt).onAdjacentColorChanged(dir);
						}
					}

					return true;
				}

			}

		}

		return false;

	}

	@Override
	public int getRenderType()
	{
		return GalacticraftCore.proxy.getBlockRenderID(this.blockID);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.pipeIcons = new Icon[16];

		for (int count = 0; count < ItemDye.dyeColorNames.length; count++)
		{
			this.pipeIcons[count] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "pipe_oxygen_" + ItemDye.dyeColorNames[count]);
		}

		this.blockIcon = this.pipeIcons[15];
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		return true;
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
	public TileEntity createNewTileEntity(World var1)
	{
		return new GCCoreTileEntityOxygenPipe();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
	{
		return this.getCollisionBoundingBoxFromPool(world, i, j, k);
	}

	@Override
	public NetworkType getNetworkType()
	{
		return NetworkType.OXYGEN;
	}
}
