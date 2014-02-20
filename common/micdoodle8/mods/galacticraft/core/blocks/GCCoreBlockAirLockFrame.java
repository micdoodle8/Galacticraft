package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAirLock;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAirLockController;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreBlockAirLockFrame.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockAirLockFrame extends GCCoreBlockAdvancedTile
{
	@SideOnly(Side.CLIENT)
	private Icon[] airLockIcons;

	public static int METADATA_AIR_LOCK_FRAME = 0;
	public static int METADATA_AIR_LOCK_CONTROLLER = 1;

	public GCCoreBlockAirLockFrame(int id, String assetName)
	{
		super(id, Material.rock);
		this.setHardness(1.0F);
		this.setStepSound(Block.soundMetalFootstep);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setUnlocalizedName(assetName);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(par1, 1, GCCoreBlockAirLockFrame.METADATA_AIR_LOCK_FRAME));
		par3List.add(new ItemStack(par1, 1, GCCoreBlockAirLockFrame.METADATA_AIR_LOCK_CONTROLLER));
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftTab;
	}

	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
	{
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{
		super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);

		TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (tile instanceof GCCoreTileEntityAirLockController && entityLiving instanceof EntityPlayer)
		{
			((GCCoreTileEntityAirLockController) tile).ownerName = ((EntityPlayer) entityLiving).username;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.airLockIcons = new Icon[8];
		this.airLockIcons[0] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "airlock_off");
		this.airLockIcons[1] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "airlock_on_1");
		this.airLockIcons[2] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "airlock_on_2");
		this.airLockIcons[3] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "airlock_on_3");
		this.airLockIcons[4] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "airlock_on_4");
		this.airLockIcons[5] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "airlock_on_5");
		this.airLockIcons[6] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "airlock_control_on");
		this.airLockIcons[7] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "airlock_control_off");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		if (par2 >= GCCoreBlockAirLockFrame.METADATA_AIR_LOCK_CONTROLLER)
		{
			if (par1 == ForgeDirection.UP.ordinal() || par1 == ForgeDirection.DOWN.ordinal())
			{
				return this.airLockIcons[0];
			}

			return this.airLockIcons[7];
		}
		else
		{
			return this.airLockIcons[0];
		}
	}

	@Override
	public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int side)
	{
		if (par1IBlockAccess.getBlockMetadata(par2, par3, par4) >= GCCoreBlockAirLockFrame.METADATA_AIR_LOCK_CONTROLLER)
		{
			if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal())
			{
				return this.airLockIcons[0];
			}

			TileEntity tile = par1IBlockAccess.getBlockTileEntity(par2, par3, par4);

			if (tile instanceof GCCoreTileEntityAirLockController)
			{
				GCCoreTileEntityAirLockController controller = (GCCoreTileEntityAirLockController) tile;

				if (controller.active)
				{
					return this.airLockIcons[6];
				}
				else
				{
					return this.airLockIcons[7];
				}
			}
			else
			{
				return this.airLockIcons[6];
			}
		}
		else
		{
			for (final ForgeDirection orientation : ForgeDirection.values())
			{
				if (orientation != ForgeDirection.UNKNOWN)
				{
					final Vector3 vector = new Vector3(par2, par3, par4);
					Vector3 blockVec = this.modifyPositionFromSide(vector.clone(), orientation, 1);
					Block connection = Block.blocksList[blockVec.getBlockID(par1IBlockAccess)];

					if (connection != null && connection.equals(GCCoreBlocks.airLockSeal))
					{
						if (orientation.offsetY == -1)
						{
							if (side == 0)
							{
								return this.airLockIcons[1];
							}
							else if (side == 1)
							{
								return this.airLockIcons[0];
							}
							else
							{
								return this.airLockIcons[2];
							}
						}
						else if (orientation.offsetY == 1)
						{
							if (side == 0)
							{
								return this.airLockIcons[0];
							}
							else if (side == 1)
							{
								return this.airLockIcons[1];
							}
							else
							{
								return this.airLockIcons[3];
							}
						}
						else if (orientation.ordinal() == side)
						{
							if (side == 0)
							{
								return this.airLockIcons[0];
							}
							else if (side == 1)
							{
								return this.airLockIcons[1];
							}
							else
							{
								return this.airLockIcons[3];
							}
						}
						else if (orientation.getOpposite().ordinal() == side)
						{
							return this.airLockIcons[0];
						}

						blockVec = vector.clone().translate(new Vector3(orientation.offsetX, orientation.offsetY, orientation.offsetZ));
						connection = Block.blocksList[blockVec.getBlockID(par1IBlockAccess)];

						if (connection != null && connection.equals(GCCoreBlocks.airLockSeal))
						{
							if (orientation.offsetX == 1)
							{
								if (side == 0)
								{
									return this.airLockIcons[4];
								}
								else if (side == 1)
								{
									return this.airLockIcons[4];
								}
								else if (side == 3)
								{
									return this.airLockIcons[4];
								}
								else if (side == 2)
								{
									return this.airLockIcons[5];
								}
							}
							else if (orientation.offsetX == -1)
							{
								if (side == 0)
								{
									return this.airLockIcons[5];
								}
								else if (side == 1)
								{
									return this.airLockIcons[5];
								}
								else if (side == 3)
								{
									return this.airLockIcons[5];
								}
								else if (side == 2)
								{
									return this.airLockIcons[4];
								}
							}
							else if (orientation.offsetZ == 1)
							{
								if (side == 0)
								{
									return this.airLockIcons[2];
								}
								else if (side == 1)
								{
									return this.airLockIcons[2];
								}
								else if (side == 4)
								{
									return this.airLockIcons[4];
								}
								else if (side == 5)
								{
									return this.airLockIcons[5];
								}
							}
							else if (orientation.offsetZ == -1)
							{
								if (side == 0)
								{
									return this.airLockIcons[3];
								}
								else if (side == 1)
								{
									return this.airLockIcons[3];
								}
								else if (side == 4)
								{
									return this.airLockIcons[5];
								}
								else if (side == 5)
								{
									return this.airLockIcons[4];
								}
							}
						}
					}
				}
			}
		}

		return this.airLockIcons[0];
	}

	public Vector3 modifyPositionFromSide(Vector3 vec, ForgeDirection side, double amount)
	{
		switch (side.ordinal())
		{
		case 0:
			vec.y -= amount;
			break;
		case 1:
			vec.y += amount;
			break;
		case 2:
			vec.z -= amount;
			break;
		case 3:
			vec.z += amount;
			break;
		case 4:
			vec.x -= amount;
			break;
		case 5:
			vec.x += amount;
			break;
		}
		return vec;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		if (metadata < GCCoreBlockAirLockFrame.METADATA_AIR_LOCK_CONTROLLER)
		{
			return new GCCoreTileEntityAirLock();
		}
		else
		{
			return new GCCoreTileEntityAirLockController();
		}
	}

	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}

	@Override
	public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = world.getBlockMetadata(x, y, z);
		TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (metadata >= GCCoreBlockAirLockFrame.METADATA_AIR_LOCK_CONTROLLER && tile instanceof GCCoreTileEntityAirLockController)
		{
			entityPlayer.openGui(GalacticraftCore.instance, -1, world, x, y, z);
			return true;
		}

		return false;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (tile instanceof GCCoreTileEntityAirLockController)
		{
			((GCCoreTileEntityAirLockController) tile).unsealAirLock();
		}

		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public int damageDropped(int metadata)
	{
		if (metadata >= GCCoreBlockAirLockFrame.METADATA_AIR_LOCK_CONTROLLER)
		{
			return GCCoreBlockAirLockFrame.METADATA_AIR_LOCK_CONTROLLER;
		}
		else if (metadata >= GCCoreBlockAirLockFrame.METADATA_AIR_LOCK_FRAME)
		{
			return GCCoreBlockAirLockFrame.METADATA_AIR_LOCK_FRAME;
		}

		return 0;
	}
}
