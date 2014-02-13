package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenPressureProtocol.VecDirPair;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreBlockOxygenSealer.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockOxygenSealer extends GCCoreBlockAdvancedTile
{
	private IIcon iconMachineSide;
	private IIcon iconSealer;
	private IIcon iconInput;
	private IIcon iconOutput;

	public GCCoreBlockOxygenSealer(String assetName)
	{
		super(Material.rock);
		this.setHardness(1.0F);
		this.setStepSound(Block.soundTypeStone);
		this.setBlockTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setBlockName(assetName);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftTab;
	}

	@Override
	public int getRenderType()
	{
		return GalacticraftCore.proxy.getBlockRenderID(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.iconMachineSide = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_blank");
		this.iconSealer = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_sealer");
		this.iconInput = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_oxygen_input");
		this.iconOutput = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_input");
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
	public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		entityPlayer.openGui(GalacticraftCore.instance, -1, world, x, y, z);
		return true;
	}

	@Override
	public IIcon getIcon(int side, int metadata)
	{
		if (side == 1)
		{
			return this.iconSealer;
		}
		else if (side == metadata + 2)
		{
			return this.iconOutput;
		}
		else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
		{
			return this.iconInput;
		}
		else
		{
			return this.iconMachineSide;
		}
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
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new TileEntityOxygenSealer();
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof TileEntityOxygenSealer)
		{
			TileEntityOxygenSealer sealer = (TileEntityOxygenSealer) tile;

			if (sealer.threadSeal != null && sealer.threadSeal.sealed)
			{
				for (VecDirPair checkedVec : sealer.threadSeal.checked)
				{
					Block block = checkedVec.getPosition().getBlock(world);

					if (block == GCCoreBlocks.breatheableAir)
					{
						world.setBlock(checkedVec.getPosition().intX(), checkedVec.getPosition().intY(), checkedVec.getPosition().intZ(), Blocks.air, 0, 2);
					}
				}
			}
		}

		super.breakBlock(world, x, y, z, par5, par6);
	}
}
