package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;

import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityNasaWorkbench;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockNasaWorkbench extends BlockContainer implements ITileEntityProvider
{
	IIcon[] iconBuffer;

	public BlockNasaWorkbench(String assetName)
	{
		super(Material.iron);
		this.setBlockBounds(-0.3F, 0.0F, -0.3F, 1.3F, 0.5F, 1.3F);
		this.setHardness(2.5F);
		this.setStepSound(Block.soundTypeMetal);
		this.setBlockTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setBlockName(assetName);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftBlocksTab;
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.iconBuffer = new IIcon[2];
		this.iconBuffer[0] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "workbench_nasa_side");
		this.iconBuffer[1] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "workbench_nasa_top");
	}

	@Override
	public int getRenderType()
	{
		return GalacticraftCore.proxy.getBlockRender(this);
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
	{
		return AxisAlignedBB.getBoundingBox((double) i + -0.0F, (double) j + 0.0F, (double) k + -0.0F, (double) i + 1.0F, (double) j + 1.4F, (double) k + 1.0F);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
	{
		return this.getCollisionBoundingBoxFromPool(world, i, j, k);
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 vec3d, Vec3 vec3d1)
	{
		this.setBlockBounds(-0.0F, 0.0F, -0.0F, 1.0F, 1.4F, 1.0F);

		final MovingObjectPosition r = super.collisionRayTrace(world, i, j, k, vec3d, vec3d1);

		this.setBlockBounds(-0.0F, 0.0F, -0.0F, 1.0F, 1.4F, 1.0F);

		return r;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity)
	{
		this.setBlockBounds(-0.0F, 0.0F, -0.0F, 1.0F, 1.4F, 1.0F);
		super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
	}

	@Override
	public boolean canPlaceBlockAt(World par1World, int x0, int y0, int z0)
	{
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x0, int y0, int z0, EntityLivingBase entity, ItemStack var6)
	{
		final TileEntity var8 = world.getTileEntity(x0, y0, z0);

		boolean validSpot = true;

		for (int x = -1; x < 2; x++)
		{
			for (int y = 0; y < 4; y++)
			{
				for (int z = -1; z < 2; z++)
				{
					if (!(x == 0 && y == 0 && z == 0))
					{
						if (Math.abs(x) != 1 || Math.abs(z) != 1)
						{
							Block blockAt = world.getBlock(x0 + x, y0 + y, z0 + z);

							if ((y == 0 || y == 3) && x == 0 && z == 0)
							{
								if (!blockAt.getMaterial().isReplaceable())
								{
									validSpot = false;
								}
							}
							else if (y != 0 && y != 3)
							{
								if (!blockAt.getMaterial().isReplaceable())
								{
									validSpot = false;
								}
							}
						}
					}
				}
			}
		}

		if (!validSpot)
		{
			world.setBlockToAir(x0, y0, z0);

			if (!world.isRemote && entity instanceof EntityPlayer)
			{
				((EntityPlayer) entity).addChatMessage(new ChatComponentText(EnumColor.RED + GCCoreUtil.translate("gui.warning.noroom")));
			}

			return;
		}

		if (var8 instanceof IMultiBlock)
		{
			((IMultiBlock) var8).onCreate(new BlockVec3(x0, y0, z0));
		}

		super.onBlockPlacedBy(world, x0, y0, z0, entity, var6);
	}

	@Override
	public void breakBlock(World world, int x0, int y0, int z0, Block var5, int var6)
	{
		final TileEntity var9 = world.getTileEntity(x0, y0, z0);

		int fakeBlockCount = 0;

		for (int x = -1; x < 2; x++)
		{
			for (int y = 0; y < 4; y++)
			{
				for (int z = -1; z < 2; z++)
				{
					if (!(x == 0 && y == 0 && z == 0))
					{
						if (Math.abs(x) != 1 || Math.abs(z) != 1)
						{
							if ((y == 0 || y == 3) && x == 0 && z == 0)
							{
								if (world.getBlock(x0 + x, y0 + y, z0 + z) == GCBlocks.fakeBlock)
								{
									fakeBlockCount++;
								}
							}
							else if (y != 0 && y != 3)
							{
								if (world.getBlock(x0 + x, y0 + y, z0 + z) == GCBlocks.fakeBlock)
								{
									fakeBlockCount++;
								}
							}
						}
					}
				}
			}
		}

		if (fakeBlockCount > 0 && var9 instanceof IMultiBlock)
		{
			((IMultiBlock) var9).onDestroy(var9);
		}

		super.breakBlock(world, x0, y0, z0, var5, var6);
	}

	@Override
	public IIcon getIcon(int par1, int par2)
	{
		return par1 == 1 ? this.iconBuffer[1] : this.iconBuffer[0];
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		par5EntityPlayer.openGui(GalacticraftCore.instance, SchematicRegistry.getMatchingRecipeForID(0).getGuiID(), par1World, par2, par3, par4);
		return true;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		this.setBlockBounds(-0.0F, 0.0F, -0.0F, 1.0F, 1.4F, 1.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityNasaWorkbench();
	}
}
