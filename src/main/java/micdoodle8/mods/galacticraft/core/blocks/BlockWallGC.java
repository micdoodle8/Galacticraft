package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWallGC extends BlockWall
{
	private IIcon[] wallBlockIcon;
	private IIcon[] tinSideIcon;

	public BlockWallGC(String name, Block par2Block)
	{
		super(par2Block);
		this.setBlockName(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.wallBlockIcon = new IIcon[6];
		this.wallBlockIcon[0] = par1IconRegister.registerIcon("galacticraftcore:deco_aluminium_4");
		this.wallBlockIcon[1] = par1IconRegister.registerIcon("galacticraftcore:deco_aluminium_2");
		this.wallBlockIcon[2] = par1IconRegister.registerIcon("galacticraftmoon:bottom");
		this.wallBlockIcon[3] = par1IconRegister.registerIcon("galacticraftmoon:brick");

		if (GalacticraftCore.isPlanetsLoaded)
		{
			try
			{
				Class<?> c = Class.forName("micdoodle8.mods.galacticraft.planets.mars.MarsModule");
				String texturePrefix = (String) c.getField("TEXTURE_PREFIX").get(null);
				this.wallBlockIcon[4] = par1IconRegister.registerIcon(texturePrefix + "cobblestone");
				this.wallBlockIcon[5] = par1IconRegister.registerIcon(texturePrefix + "brick");
			}
			catch (Exception e)
			{
				this.wallBlockIcon[4] = this.wallBlockIcon[3];
				this.wallBlockIcon[5] = this.wallBlockIcon[3];
				e.printStackTrace();
			}
		}
		else
		{
			this.wallBlockIcon[4] = this.wallBlockIcon[3];
			this.wallBlockIcon[5] = this.wallBlockIcon[3];
		}

		this.tinSideIcon = new IIcon[1];
		this.tinSideIcon[0] = par1IconRegister.registerIcon("galacticraftcore:deco_aluminium_1");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta)
	{
		if (meta == 1)
		{
			switch (side)
			{
			case 0:
				return this.wallBlockIcon[0]; //BOTTOM
			case 1:
				return this.wallBlockIcon[1]; //TOP
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
		return this.wallBlockIcon[meta];
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		boolean flag = this.canConnectWallTo(par1IBlockAccess, par2, par3, par4 - 1);
		boolean flag1 = this.canConnectWallTo(par1IBlockAccess, par2, par3, par4 + 1);
		boolean flag2 = this.canConnectWallTo(par1IBlockAccess, par2 - 1, par3, par4);
		boolean flag3 = this.canConnectWallTo(par1IBlockAccess, par2 + 1, par3, par4);
		float f = 0.25F;
		float f1 = 0.75F;
		float f2 = 0.25F;
		float f3 = 0.75F;
		float f4 = 1.0F;

		if (flag)
		{
			f2 = 0.0F;
		}

		if (flag1)
		{
			f3 = 1.0F;
		}

		if (flag2)
		{
			f = 0.0F;
		}

		if (flag3)
		{
			f1 = 1.0F;
		}

		if (flag && flag1 && !flag2 && !flag3)
		{
			f4 = 0.8125F;
			f = 0.3125F;
			f1 = 0.6875F;
		}
		else if (!flag && !flag1 && flag2 && flag3)
		{
			f4 = 0.8125F;
			f2 = 0.3125F;
			f3 = 0.6875F;
		}
		this.setBlockBounds(f, 0.0F, f2, f1, f4, f3);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
		this.maxY = 1.5D;
		return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
	}

	@Override
	public boolean canConnectWallTo(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		Block block = par1IBlockAccess.getBlock(par2, par3, par4);

		if (block != this && block != Blocks.fence_gate)
		{
			return block != null && block.getMaterial().isOpaque() && block.renderAsNormalBlock() ? block.getMaterial() != Material.gourd : false;
		}
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		if (GalacticraftCore.isPlanetsLoaded)
		{
			for (int var4 = 0; var4 < 6; ++var4)
			{
				par3List.add(new ItemStack(par1, 1, var4));
			}
		}
		else
		{
			for (int var4 = 0; var4 < 4; ++var4)
			{
				par3List.add(new ItemStack(par1, 1, var4));
			}
		}
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftBlocksTab;
	}

	@Override
	public int damageDropped(int par1)
	{
		return par1;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		return par5 == 0 ? super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5) : true;
	}
}