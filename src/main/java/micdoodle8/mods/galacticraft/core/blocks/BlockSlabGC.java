package micdoodle8.mods.galacticraft.core.blocks;

import cpw.mods.fml.common.Loader;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockSlabGC extends BlockSlab
{
	public static enum SlabCategoryGC
	{
		WOOD1, WOOD2, STONE;
	}

	private static final String[] woodTypes = new String[] {
		"tin",
		"tin",
		"moon",
		"moonBricks",
		"mars",
		"marsBricks"
	};

	private IIcon[] textures;
	private IIcon[] tinSideIcon;
	private final boolean isDoubleSlab;
	private final SlabCategoryGC category;

	public BlockSlabGC(String name, boolean par2, Material material, SlabCategoryGC cat)
	{
		super(par2, material);
		this.isDoubleSlab = par2;
		this.category = cat;
		this.setBlockName(name);
		this.useNeighborBrightness = true;
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.textures = new IIcon[6];
		this.textures[0] = par1IconRegister.registerIcon("galacticraftcore:deco_aluminium_4");
		this.textures[1] = par1IconRegister.registerIcon("galacticraftcore:deco_aluminium_2");
		this.textures[2] = par1IconRegister.registerIcon("galacticraftmoon:bottom");
		this.textures[3] = par1IconRegister.registerIcon("galacticraftmoon:brick");

		if (Loader.isModLoaded(Constants.MOD_ID_PLANETS))
		{
			this.textures[4] = par1IconRegister.registerIcon("galacticraftmars:cobblestone");
			this.textures[5] = par1IconRegister.registerIcon("galacticraftmars:brick");
		}
		else
		{
			this.textures[4] = this.textures[3];
			this.textures[5] = this.textures[3];
		}

		this.tinSideIcon = new IIcon[1];
		this.tinSideIcon[0] = par1IconRegister.registerIcon("galacticraftcore:deco_aluminium_1");
	}

	@Override
	public IIcon getIcon(int side, int meta)
	{
		if (this.category == SlabCategoryGC.WOOD1)
		{
			if (meta == 1 || meta == 9)
			{
				switch (side)
				{
				case 0:
					return this.textures[0]; //BOTTOM
				case 1:
					return this.textures[1]; //TOP
				case 2:
					return this.tinSideIcon[0]; //Z-
				case 3:
					return this.tinSideIcon[0]; //Z+
				case 4:
					return this.tinSideIcon[0]; //X-
				case 5:
					return this.tinSideIcon[0]; //X+
				}
			}
			return this.textures[getTypeFromMeta(meta)];
		}
		return this.blockIcon;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getSubBlocks(Item block, CreativeTabs creativeTabs, List list)
	{
		int max = 0;

		if (this.category == SlabCategoryGC.WOOD1)
		{
			max = 6;//Maximum slab type
		}
		for (int i = 0; i < max; ++i)
		{
			list.add(new ItemStack(block, 1, i));
		}
	}

	@Override
	public String func_150002_b(int meta)
	{
		return new StringBuilder().append(woodTypes[this.getWoodType(meta)]).append("Slab").toString();
	}

	@Override
	public int damageDropped(int meta)
	{
		return meta & 7;
	}

	@Override
	public Item getItemDropped(int meta, Random par2Random, int par3)
	{
		if (this.isDoubleSlab)
		{
			if (this == GCBlocks.slabGCDouble)
			{
				return Item.getItemFromBlock(GCBlocks.slabGCHalf);
			}
		}
		return Item.getItemFromBlock(this);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		if (!this.isDoubleSlab)
		{
			return GalacticraftCore.galacticraftBlocksTab;
		}
		return null;
	}

	@Override
	public float getBlockHardness(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		float hardness = this.blockHardness;

		if (this.category == SlabCategoryGC.WOOD1)
		{
			switch (getTypeFromMeta(meta))
			{
			case 2:
			case 3:
				hardness = 1.5F;
				break;
			default:
				hardness = 2.0F;
				break;
			}
		}
		return hardness;
	}

	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
	{
		return super.getBlockHardness(world, x, y, z);
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		if (this == GCBlocks.slabGCDouble)
		{
			return new ItemStack(GCBlocks.slabGCHalf, 1, world.getBlockMetadata(x, y, z));
		}
		return new ItemStack(GCBlocks.slabGCHalf, 1, world.getBlockMetadata(x, y, z));
	}

	@Override
	protected ItemStack createStackedBlock(int par1)
	{
		return new ItemStack(this, 2, par1);
	}

	private int getWoodType(int meta)
	{
		meta = getTypeFromMeta(meta) + this.category.ordinal() * 8;

		if (meta < woodTypes.length)
		{
			return meta;
		}
		return 0;
	}

	private static int getTypeFromMeta(int meta)
	{
		return meta & 7;
	}
}