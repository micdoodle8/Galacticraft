package micdoodle8.mods.galacticraft.mars.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityDungeonSpawner;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
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
 * GCMarsBlock.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsBlock extends Block implements IDetectableResource, IPlantableBlock, ITileEntityProvider, ITerraformableBlock
{
	@SideOnly(Side.CLIENT)
	private Icon[] marsBlockIcons;

	public GCMarsBlock(int i)
	{
		super(i, Material.rock);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		if (world.getBlockMetadata(x, y, z) == 10)
		{
			return null;
		}

		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		if (world.getBlockMetadata(x, y, z) == 10)
		{
			return AxisAlignedBB.getAABBPool().getAABB(x + 0.0D, y + 0.0D, z + 0.0D, x + 0.0D, y + 0.0D, z + 0.0D);
		}

		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
	{
		int metadata = world.getBlockMetadata(x, y, z);

		if (metadata == 10 || metadata == 7)
		{
			return 10000.0F;
		}

		return super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.marsBlockIcons = new Icon[11];
		this.marsBlockIcons[0] = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "cobblestone");
		this.marsBlockIcons[1] = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "decoration_desh");
		this.marsBlockIcons[2] = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "middle");
		this.marsBlockIcons[3] = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "brick");
		this.marsBlockIcons[4] = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "top");
		this.marsBlockIcons[5] = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "copper");
		this.marsBlockIcons[6] = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "desh");
		this.marsBlockIcons[7] = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "tin");
		this.marsBlockIcons[8] = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "bottom");
		this.marsBlockIcons[9] = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "iron");
		this.marsBlockIcons[10] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "blank");
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftMars.galacticraftMarsTab;
	}

	@Override
	public float getBlockHardness(World par1World, int par2, int par3, int par4)
	{
		final int meta = par1World.getBlockMetadata(par2, par3, par4);

		if (meta == 10 || meta == 7)
		{
			return -1.0F;
		}

		return this.blockHardness;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		if (metadata == 10)
		{
			return new GCMarsTileEntityDungeonSpawner();
		}

		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return null;
	}

	@Override
	public boolean canHarvestBlock(EntityPlayer player, int meta)
	{
		if (meta == 10 || meta == 7)
		{
			return false;
		}

		return super.canHarvestBlock(player, meta);
	}

	@Override
	public Icon getIcon(int side, int meta)
	{
		switch (meta)
		{
		case 0:
			return this.marsBlockIcons[5];
		case 1:
			return this.marsBlockIcons[7];
		case 2:
			return this.marsBlockIcons[6];
		case 3:
			return this.marsBlockIcons[9];
		case 4:
			return this.marsBlockIcons[0];
		case 5:
			return this.marsBlockIcons[4];
		case 6:
			return this.marsBlockIcons[2];
		case 7:
			return this.marsBlockIcons[3];
		case 8:
			return this.marsBlockIcons[1];
		case 9:
			return this.marsBlockIcons[8];
		case 10:
			return this.marsBlockIcons[10];
		}

		return this.marsBlockIcons[1];
	}

	@Override
	public int idDropped(int meta, Random random, int par3)
	{
		if (meta == 2)
		{
			return GCMarsItems.marsItemBasic.itemID;
		}
		else if (meta == 10 || meta == 7)
		{
			return 0;
		}

		return this.blockID;
	}

	@Override
	public int damageDropped(int meta)
	{
		if (meta == 9)
		{
			return 4;
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
		if (meta == 10 || meta == 7)
		{
			return 0;
		}

		return 1;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		int var4;

		for (var4 = 0; var4 < 11; ++var4)
		{
			if (var4 != 10)
			{
				par3List.add(new ItemStack(par1, 1, var4));
			}
		}
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
		case 3:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant)
	{
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
		return false;
	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random rand)
	{
		if (rand.nextInt(10) == 0)
		{
			int metadata = world.getBlockMetadata(x, y, z);

			if (metadata == 7)
			{
				GalacticraftMars.proxy.spawnParticle("sludgeDrip", x + rand.nextDouble(), y, z + rand.nextDouble());

				if (rand.nextInt(100) == 0)
				{
					world.playSound(x, y, z, GalacticraftCore.ASSET_PREFIX + "ambience.singledrip", 1, 0.8F + rand.nextFloat() / 5.0F, false);
				}
			}
		}
	}

	@Override
	public boolean isTerraformable(World world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z) == 5 && world.getBlockId(x, y + 1, z) == 0;
	}
}
