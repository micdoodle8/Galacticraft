package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntitySolar;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreBlockSolar.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockSolar extends GCCoreBlockTile
{
	public static final int BASIC_METADATA = 0;
	public static final int ADVANCED_METADATA = 4;

	public static String[] names = { "basic", "advanced" };

	private Icon[] icons = new Icon[6];

	public GCCoreBlockSolar(int id, String assetName)
	{
		super(id, Material.iron);
		this.setHardness(1.0F);
		this.setStepSound(Block.soundMetalFootstep);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setUnlocalizedName(assetName);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(par1, 1, GCCoreBlockSolar.BASIC_METADATA));
		par3List.add(new ItemStack(par1, 1, GCCoreBlockSolar.ADVANCED_METADATA));
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.icons[0] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "solar_basic_0");
		this.icons[1] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "solar_basic_1");
		this.icons[2] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "solar_advanced_0");
		this.icons[3] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "solar_advanced_1");
		this.icons[4] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_blank");
		this.icons[5] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_output");
		this.blockIcon = this.icons[0];
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftTab;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta)
	{
		if (meta >= GCCoreBlockSolar.ADVANCED_METADATA)
		{
			int shiftedMeta = meta -= GCCoreBlockSolar.ADVANCED_METADATA;

			if (side == ForgeDirection.getOrientation(shiftedMeta + 2).getOpposite().ordinal())
			{
				return this.icons[5];
			}
			else if (side == ForgeDirection.UP.ordinal())
			{
				return this.icons[2];
			}
			else if (side == ForgeDirection.DOWN.ordinal())
			{
				return this.icons[4];
			}
			else
			{
				return this.icons[3];
			}
		}
		else if (meta >= GCCoreBlockSolar.BASIC_METADATA)
		{
			int shiftedMeta = meta -= GCCoreBlockSolar.BASIC_METADATA;

			if (side == ForgeDirection.getOrientation(shiftedMeta + 2).getOpposite().ordinal())
			{
				return this.icons[5];
			}
			else if (side == ForgeDirection.UP.ordinal())
			{
				return this.icons[0];
			}
			else if (side == ForgeDirection.DOWN.ordinal())
			{
				return this.icons[4];
			}
			else
			{
				return this.icons[1];
			}
		}

		return this.blockIcon;
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, int x1, int y1, int z1, int side)
	{
		for (int y = 1; y <= 2; y++)
		{
			for (int x = -1; x <= 1; x++)
			{
				for (int z = -1; z <= 1; z++)
				{
					int blockID = world.getBlockId(x1 + (y == 2 ? x : 0), y1 + 2, z1 + (y == 2 ? z : 0));

					if (blockID > 0 && !Block.blocksList[blockID].isBlockReplaceable(world, x1 + x, y1 + 2, z1 + z))
					{
						return false;
					}
				}
			}
		}

		return new Vector3(x1, y1, z1).clone().modifyPositionFromSide(ForgeDirection.getOrientation(side).getOpposite()).getBlockID(world) != GCCoreBlocks.fakeBlock.blockID;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{
		int metadata = world.getBlockMetadata(x, y, z);

		int angle = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		int change = 0;

		switch (angle)
		{
		case 0:
			change = 1;
			break;
		case 1:
			change = 2;
			break;
		case 2:
			change = 0;
			break;
		case 3:
			change = 3;
			break;
		}

		if (metadata >= GCCoreBlockSolar.ADVANCED_METADATA)
		{
			world.setBlockMetadataWithNotify(x, y, z, GCCoreBlockSolar.ADVANCED_METADATA + change, 3);
		}
		else
		{
			world.setBlockMetadataWithNotify(x, y, z, GCCoreBlockSolar.BASIC_METADATA + change, 3);
		}

		TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (tile instanceof GCCoreTileEntitySolar)
		{
			((GCCoreTileEntitySolar) tile).onCreate(new Vector3(x, y, z));
		}
	}

	@Override
	public void breakBlock(World var1, int var2, int var3, int var4, int var5, int var6)
	{
		final TileEntity var9 = var1.getBlockTileEntity(var2, var3, var4);

		if (var9 instanceof GCCoreTileEntitySolar)
		{
			((GCCoreTileEntitySolar) var9).onDestroy(var9);
		}

		super.breakBlock(var1, var2, var3, var4, var5, var6);
	}

	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);
		int original = metadata;

		int change = 0;

		if (metadata >= GCCoreBlockSolar.ADVANCED_METADATA)
		{
			original -= GCCoreBlockSolar.ADVANCED_METADATA;
		}

		switch (original)
		{
		case 0:
			change = 3;
			break;
		case 3:
			change = 1;
			break;
		case 1:
			change = 2;
			break;
		case 2:
			change = 0;
			break;
		}

		if (metadata >= GCCoreBlockSolar.ADVANCED_METADATA)
		{
			change += GCCoreBlockSolar.ADVANCED_METADATA;
		}

		par1World.setBlockMetadataWithNotify(x, y, z, change, 3);
		return true;
	}

	@Override
	public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		entityPlayer.openGui(GalacticraftCore.instance, -1, world, x, y, z);
		return true;
	}

	@Override
	public int damageDropped(int metadata)
	{
		if (metadata >= GCCoreBlockSolar.ADVANCED_METADATA)
		{
			return GCCoreBlockSolar.ADVANCED_METADATA;
		}
		else
		{
			return GCCoreBlockSolar.BASIC_METADATA;
		}
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		Item item = Item.itemsList[this.blockID];

		if (item == null)
		{
			return null;
		}

		int metadata = this.getDamageValue(world, x, y, z);

		return new ItemStack(this.blockID, 1, metadata);
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		if (metadata >= GCCoreBlockSolar.ADVANCED_METADATA)
		{
			return new GCCoreTileEntitySolar(100);
		}
		else
		{
			return new GCCoreTileEntitySolar(50);
		}
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
}
