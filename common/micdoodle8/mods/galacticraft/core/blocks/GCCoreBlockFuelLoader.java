package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreBlockFuelLoader.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockFuelLoader extends GCCoreBlockAdvancedTile
{
	private Icon iconMachineSide;
	private Icon iconInput;
	private Icon iconFront;
	private Icon iconFuelInput;

	public GCCoreBlockFuelLoader(int id, String assetName)
	{
		super(id, Material.rock);
		this.setHardness(1.0F);
		this.setStepSound(Block.soundMetalFootstep);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setUnlocalizedName(assetName);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftTab;
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
		this.iconInput = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_input");
		this.iconMachineSide = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_blank");
		this.iconFront = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_fuelloader");
		this.iconFuelInput = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_fuel_input");
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new GCCoreTileEntityFuelLoader();
	}

	@Override
	public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		entityPlayer.openGui(GalacticraftCore.instance, -1, world, x, y, z);
		return true;
	}

	@Override
	public Icon getIcon(int side, int metadata)
	{
		if (side == 0 || side == 1)
		{
			return this.iconMachineSide;
		}
		else if (side == metadata + 2)
		{
			return this.iconInput;
		}
		else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
		{
			return this.iconFuelInput;
		}
		else
		{
			return this.iconFront;
		}
	}

	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		final int metadata = par1World.getBlockMetadata(x, y, z);
		final int original = metadata;

		int change = 0;

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

		par1World.setBlockMetadataWithNotify(x, y, z, change, 3);
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{
		final int angle = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		int change = 0;

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

		world.setBlockMetadataWithNotify(x, y, z, change, 3);

		for (int dX = -2; dX < 3; dX++)
		{
			for (int dZ = -2; dZ < 3; dZ++)
			{
				final int id = world.getBlockId(x + dX, y, z + dZ);

				if (id == GCCoreBlocks.landingPadFull.blockID)
				{
					world.markBlockForUpdate(x + dX, y, z + dZ);
				}
			}
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int par5)
	{
		super.onBlockDestroyedByPlayer(world, x, y, z, par5);

		for (int dX = -2; dX < 3; dX++)
		{
			for (int dZ = -2; dZ < 3; dZ++)
			{
				final int id = world.getBlockId(x + dX, y, z + dZ);

				if (id == GCCoreBlocks.landingPadFull.blockID)
				{
					world.markBlockForUpdate(x + dX, y, z + dZ);
				}
			}
		}
	}
}
