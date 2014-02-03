package micdoodle8.mods.galacticraft.moon.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockAdvancedTile;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.moon.GalacticraftMoon;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMoonBlock.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMoonBlock extends GCCoreBlockAdvancedTile implements IDetectableResource, IPlantableBlock, ITerraformableBlock
{
	// CopperMoon: 0, TinMoon: 1, CheeseStone: 2;
	@SideOnly(Side.CLIENT)
	private Icon[] moonBlockIcons;

	public GCMoonBlock(int i)
	{
		super(i, Material.rock);
		this.blockHardness = 3.0F;
		this.setUnlocalizedName("moonBlock");
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		if (world.getBlockMetadata(x, y, z) == 15)
		{
			return null;
		}

		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		if (world.getBlockMetadata(x, y, z) == 15)
		{
			return AxisAlignedBB.getAABBPool().getAABB(x + 0.0D, y + 0.0D, z + 0.0D, x + 0.0D, y + 0.0D, z + 0.0D);
		}

		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.moonBlockIcons = new Icon[17];
		this.moonBlockIcons[0] = par1IconRegister.registerIcon("galacticraftmoon:top");
		this.moonBlockIcons[1] = par1IconRegister.registerIcon("galacticraftmoon:brick");
		this.moonBlockIcons[2] = par1IconRegister.registerIcon("galacticraftmoon:middle");
		this.moonBlockIcons[3] = par1IconRegister.registerIcon("galacticraftmoon:top_side");
		this.moonBlockIcons[4] = par1IconRegister.registerIcon("galacticraftmoon:grass_step_1");
		this.moonBlockIcons[5] = par1IconRegister.registerIcon("galacticraftmoon:grass_step_2");
		this.moonBlockIcons[6] = par1IconRegister.registerIcon("galacticraftmoon:grass_step_3");
		this.moonBlockIcons[7] = par1IconRegister.registerIcon("galacticraftmoon:grass_step_4");
		this.moonBlockIcons[8] = par1IconRegister.registerIcon("galacticraftmoon:grass_step_5");
		this.moonBlockIcons[9] = par1IconRegister.registerIcon("galacticraftmoon:grass_step_6");
		this.moonBlockIcons[10] = par1IconRegister.registerIcon("galacticraftmoon:grass_step_7");
		this.moonBlockIcons[11] = par1IconRegister.registerIcon("galacticraftmoon:grass_step_8");
		this.moonBlockIcons[12] = par1IconRegister.registerIcon("galacticraftmoon:moonore_copper");
		this.moonBlockIcons[13] = par1IconRegister.registerIcon("galacticraftmoon:moonore_tin");
		this.moonBlockIcons[14] = par1IconRegister.registerIcon("galacticraftmoon:moonore_cheese");
		this.moonBlockIcons[15] = par1IconRegister.registerIcon("galacticraftmoon:bottom");
		this.moonBlockIcons[16] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "blank");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftMoon.galacticraftMoonTab;
	}

	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
	{
		if (world.getBlockMetadata(x, y, z) == 15 || world.getBlockMetadata(x, y, z) == 14)
		{
			return 10000.0F;
		}

		return super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
	}

	@Override
	public float getBlockHardness(World par1World, int par2, int par3, int par4)
	{
		final int meta = par1World.getBlockMetadata(par2, par3, par4);

		if (meta == 3 || meta >= 5 && meta <= 13)
		{
			return 0.1F;
		}

		if (meta == 14 || meta == 15)
		{
			return -1F;
		}

		return this.blockHardness;
	}

	@Override
	public boolean canHarvestBlock(EntityPlayer player, int meta)
	{
		if (meta == 3 || meta >= 5 && meta <= 13)
		{
			return true;
		}

		return super.canHarvestBlock(player, meta);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIcon(int side, int meta)
	{
		if (meta >= 5 && meta <= 13)
		{
			if (side == 1)
			{
				switch (meta - 5)
				{
				case 0:
					return this.moonBlockIcons[0];
				case 1:
					return this.moonBlockIcons[4];
				case 2:
					return this.moonBlockIcons[5];
				case 3:
					return this.moonBlockIcons[6];
				case 4:
					return this.moonBlockIcons[7];
				case 5:
					return this.moonBlockIcons[8];
				case 6:
					return this.moonBlockIcons[9];
				case 7:
					return this.moonBlockIcons[10];
				case 8:
					return this.moonBlockIcons[11];
				}
			}
			else if (side == 0)
			{
				return this.moonBlockIcons[2];
			}
			else
			{
				return this.moonBlockIcons[3];
			}
		}
		else
		{
			switch (meta)
			{
			case 0:
				return this.moonBlockIcons[12];
			case 1:
				return this.moonBlockIcons[13];
			case 2:
				return this.moonBlockIcons[14];
			case 3:
				return this.moonBlockIcons[2];
			case 4:
				return this.moonBlockIcons[15];
			case 14:
				return this.moonBlockIcons[1];
			case 15:
				return this.moonBlockIcons[16];
			default:
				return this.moonBlockIcons[16];
			}
		}

		return null;
	}

	@Override
	public int idDropped(int meta, Random random, int par3)
	{
		switch (meta)
		{
		case 2:
			return GCCoreItems.cheeseCurd.itemID;
		case 15:
			return 0;
		default:
			return this.blockID;
		}
	}

	@Override
	public int damageDropped(int meta)
	{
		if (meta >= 5 && meta <= 13)
		{
			return 5;
		}
		else if (meta == 2)
		{
			return 0;
		}
		else
		{
			return meta;
		}
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random)
	{
		switch (meta)
		{
		case 15:
			return 0;
		default:
			return 1;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		int var4;

		for (var4 = 0; var4 < 6; ++var4)
		{
			par3List.add(new ItemStack(par1, 1, var4));
		}

		for (var4 = 14; var4 < 15; var4++)
		{
			par3List.add(new ItemStack(par1, 1, var4));
		}
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		if (metadata == 15)
		{
			return new GCCoreTileEntityDungeonSpawner();
		}

		return null;
	}

	@Override
	public boolean isValueable(int metadata)
	{
		switch (metadata)
		{
		case 0:
			return true;
		case 1:
			return true;
		case 2:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant)
	{
		final int metadata = world.getBlockMetadata(x, y, z);

		if (metadata < 5 && metadata > 13)
		{
			return false;
		}

		plant.getPlantID(world, x, y + 1, z);

		if (plant instanceof BlockFlower)
		{
			return true;
		}

		return false;
	}

	@Override
	public int requiredLiquidBlocksNearby()
	{
		return 4;
	}

	@Override
	public boolean isPlantable(int metadata)
	{
		if (metadata >= 5 && metadata <= 13)
		{
			return true;
		}

		return false;
	}

	@Override
	public boolean isTerraformable(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);

		if (meta >= 5 && meta <= 13)
		{
			return world.getBlockId(x, y + 1, z) == 0;
		}

		return false;
	}
}
