package micdoodle8.mods.galacticraft.mars.blocks;

import java.util.List;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockTile;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.world.IChunkLoader;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.tile.TileEntityCryogenicChamber;
import micdoodle8.mods.galacticraft.mars.tile.TileEntityLaunchController;
import micdoodle8.mods.galacticraft.mars.tile.TileEntityTerraformer;
import micdoodle8.mods.galacticraft.mars.util.ConfigManagerMars;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * GCMarsBlockMachine.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class BlockMachineMars extends BlockTile
{
	public static final int TERRAFORMER_METADATA = 0;
	public static final int CRYOGENIC_CHAMBER_METADATA = 4;
	public static final int LAUNCH_CONTROLLER_METADATA = 8;

	private IIcon iconMachineSide;
	private IIcon iconInput;

	private IIcon iconTerraformer;
	private IIcon iconLaunchController;
	private IIcon iconCryochamber;

	public BlockMachineMars()
	{
		super(GCBlocks.machine);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_blank");
		this.iconInput = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_input");

		this.iconMachineSide = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_blank");
		this.iconTerraformer = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "terraformer_0");
		this.iconLaunchController = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "launchController");
		this.iconCryochamber = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "cryoDummy");
	}

	@Override
	public void breakBlock(World var1, int var2, int var3, int var4, Block var5, int var6)
	{
		final TileEntity var9 = var1.getTileEntity(var2, var3, var4);

		if (var9 instanceof IMultiBlock)
		{
			((IMultiBlock) var9).onDestroy(var9);
		}

		super.breakBlock(var1, var2, var3, var4, var5, var6);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftMars.galacticraftMarsTab;
	}

	@Override
	public IIcon getIcon(int side, int metadata)
	{
		if (side == 0 || side == 1)
		{
			return this.blockIcon;
		}

		if (metadata >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
		{
			metadata -= BlockMachineMars.LAUNCH_CONTROLLER_METADATA;

			// If it is the front side
			if (side == metadata + 2)
			{
				return this.iconInput;
			}
			else if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal())
			{
				return this.iconMachineSide;
			}
			else
			{
				return this.iconLaunchController;
			}
		}
		else if (metadata >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
		{
			return this.iconCryochamber;
		}
		else
		{
			if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal())
			{
				return this.iconMachineSide;
			}
			else if (side == ForgeDirection.getOrientation(metadata + 2).ordinal())
			{
				return this.iconInput;
			}
			else
			{
				return this.iconTerraformer;
			}
		}
	}

	/**
	 * Called when the block is placed in the world.
	 */
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

		if (metadata >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
		{
			switch (angle)
			{
			case 0:
				change = 3;
				break;
			case 1:
				change = 1;
				break;
			case 2:
				change = 2;
				break;
			case 3:
				change = 0;
				break;
			}

			world.setBlockMetadataWithNotify(x, y, z, BlockMachineMars.LAUNCH_CONTROLLER_METADATA + change, 3);
		}
		else if (metadata >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
		{
			switch (angle)
			{
			case 0:
				change = 3;
				break;
			case 1:
				change = 1;
				break;
			case 2:
				change = 2;
				break;
			case 3:
				change = 0;
				break;
			}

			world.setBlockMetadataWithNotify(x, y, z, BlockMachineMars.CRYOGENIC_CHAMBER_METADATA + change, 3);
		}
		else
		{
			world.setBlockMetadataWithNotify(x, y, z, BlockMachineMars.TERRAFORMER_METADATA + change, 3);
		}

		TileEntity var8 = world.getTileEntity(x, y, z);

		if (var8 instanceof IMultiBlock)
		{
			((IMultiBlock) var8).onCreate(new Vector3(x, y, z));
		}

		if (metadata >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
		{
			for (int dX = -2; dX < 3; dX++)
			{
				for (int dZ = -2; dZ < 3; dZ++)
				{
					Block block = world.getBlock(x + dX, y, z + dZ);

					if (block == GCBlocks.landingPadFull)
					{
						world.markBlockForUpdate(x + dX, y, z + dZ);
					}
				}
			}
		}

		if (var8 instanceof IChunkLoader && !var8.getWorldObj().isRemote && ConfigManagerMars.launchControllerChunkLoad && entityLiving instanceof EntityPlayer)
		{
			((IChunkLoader) var8).setOwnerName(((EntityPlayer) entityLiving).getGameProfile().getName());
			((IChunkLoader) var8).onTicketLoaded(ForgeChunkManager.requestTicket(GalacticraftCore.instance, var8.getWorldObj(), Type.NORMAL), true);
		}
		else if (var8 instanceof TileEntityLaunchController && entityLiving instanceof EntityPlayer)
		{
			((TileEntityLaunchController) var8).setOwnerName(((EntityPlayer) entityLiving).getGameProfile().getName());
		}
	}

	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);
		int original = metadata;

		int change = 0;

		if (metadata >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
		{
			original -= BlockMachineMars.LAUNCH_CONTROLLER_METADATA;
		}
		else if (metadata >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
		{
			return false;
		}

		// Re-orient the block
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

		if (metadata >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
		{
			change += BlockMachineMars.LAUNCH_CONTROLLER_METADATA;
		}

		par1World.setBlockMetadataWithNotify(x, y, z, change, 3);
		return true;
	}

	/**
	 * Called when the block is right clicked by the player
	 */
	@Override
	public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = world.getBlockMetadata(x, y, z);

		if (metadata >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
		{
			par5EntityPlayer.openGui(GalacticraftMars.instance, ConfigManagerMars.idGuiMachine, world, x, y, z);
			return true;
		}
		else if (metadata >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
		{
			((IMultiBlock) world.getTileEntity(x, y, z)).onActivated(par5EntityPlayer);
			return true;
		}
		else
		{
			par5EntityPlayer.openGui(GalacticraftMars.instance, ConfigManagerMars.idGuiMachine, world, x, y, z);
			return true;
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

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		if (metadata >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
		{
			return new TileEntityLaunchController();
		}
		if (metadata >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
		{
			return new TileEntityCryogenicChamber();
		}
		else
		{
			return new TileEntityTerraformer();
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int par5)
	{
		super.onBlockDestroyedByPlayer(world, x, y, z, par5);

		if (world.getBlockMetadata(x, y, z) >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
		{
			for (int dX = -2; dX < 3; dX++)
			{
				for (int dZ = -2; dZ < 3; dZ++)
				{
					Block block = world.getBlock(x + dX, y, z + dZ);

					if (block == GCBlocks.landingPadFull)
					{
						world.markBlockForUpdate(x + dX, y, z + dZ);
					}
				}
			}
		}
	}

	public ItemStack getTerraformer()
	{
		return new ItemStack(this, 1, BlockMachineMars.TERRAFORMER_METADATA);
	}

	public ItemStack getChamber()
	{
		return new ItemStack(this, 1, BlockMachineMars.CRYOGENIC_CHAMBER_METADATA);
	}

	public ItemStack getLaunchController()
	{
		return new ItemStack(this, 1, BlockMachineMars.LAUNCH_CONTROLLER_METADATA);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(this.getTerraformer());
		par3List.add(this.getChamber());
		par3List.add(this.getLaunchController());
	}

	@Override
	public int damageDropped(int metadata)
	{
		if (metadata >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
		{
			return BlockMachineMars.LAUNCH_CONTROLLER_METADATA;
		}
		else if (metadata >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
		{
			return BlockMachineMars.CRYOGENIC_CHAMBER_METADATA;
		}
		else
		{
			return BlockMachineMars.TERRAFORMER_METADATA;
		}
	}

	@Override
	public int getRenderType()
	{
		return GalacticraftMars.proxy.getMachineRenderID();
	}

	@Override
	public boolean isBed(IBlockAccess world, int x, int y, int z, EntityLivingBase player)
	{
		return world.getBlockMetadata(x, y, z) >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA;
	}

	@Override
	public ChunkCoordinates getBedSpawnPosition(IBlockAccess world, int x, int y, int z, EntityPlayer player)
	{
		return new ChunkCoordinates(x, y + 1, z);
	}

	@Override
	public void setBedOccupied(IBlockAccess world, int x, int y, int z, EntityPlayer player, boolean occupied)
	{
		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof TileEntityCryogenicChamber)
		{
			((TileEntityCryogenicChamber) tile).isOccupied = true;
		}
	}

	public static ChunkCoordinates getNearestEmptyChunkCoordinates(World par0World, int par1, int par2, int par3, int par4)
	{
		for (int k1 = 0; k1 <= 1; ++k1)
		{
			int l1 = par1 - 1;
			int i2 = par3 - 1;
			int j2 = l1 + 2;
			int k2 = i2 + 2;

			for (int l2 = l1; l2 <= j2; ++l2)
			{
				for (int i3 = i2; i3 <= k2; ++i3)
				{
					if (par0World.doesBlockHaveSolidTopSurface(par0World, l2, par2 - 1, i3) && !par0World.getBlock(l2, par2, i3).getMaterial().isOpaque() && !par0World.getBlock(l2, par2 + 1, i3).getMaterial().isOpaque())
					{
						if (par4 <= 0)
						{
							return new ChunkCoordinates(l2, par2, i3);
						}

						--par4;
					}
				}
			}
		}

		return null;
	}
}
