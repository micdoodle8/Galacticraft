package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityRefinery;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreBlockRefinery.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockRefinery extends GCCoreBlockAdvancedTile
{
	private final Random refineryRand = new Random();

	private Icon iconMachineSide;
	private Icon iconFuelOutput;
	private Icon iconOilInput;
	private Icon iconFront;
	private Icon iconBack;
	private Icon iconTop;

	protected GCCoreBlockRefinery(int id, String assetName)
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
		this.iconMachineSide = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_blank");
		this.iconFuelOutput = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_fuel_input");
		this.iconOilInput = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_oil_input");
		this.iconFront = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "refinery_front");
		this.iconBack = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "refinery_side");
		this.iconTop = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_input");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		final TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);

		if (te instanceof GCCoreTileEntityRefinery)
		{
			final GCCoreTileEntityRefinery refinery = (GCCoreTileEntityRefinery) te;

			if (refinery.processTicks > 0)
			{
				par1World.getBlockMetadata(par2, par3, par4);
				final float var7 = par2 + 0.5F;
				final float var8 = par3 + 1.1F;
				final float var9 = par4 + 0.5F;
				final float var10 = 0.0F;
				final float var11 = 0.0F;

				for (int i = -1; i <= 1; i++)
				{
					for (int j = -1; j <= 1; j++)
					{
						par1World.spawnParticle("smoke", var7 + var11 + i * 0.2, var8, var9 + var10 + j * 0.2, 0.0D, 0.01D, 0.0D);
						par1World.spawnParticle("flame", var7 + var11 + i * 0.1, var8 - 0.2, var9 + var10 + j * 0.1, 0.0D, 0.0001D, 0.0D);
					}
				}
			}
		}
	}

	@Override
	public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		entityPlayer.openGui(GalacticraftCore.instance, -1, world, x, y, z);
		return true;
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

		par1World.setBlockMetadataWithNotify(x, y, z, change, 3);
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new GCCoreTileEntityRefinery();
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
	{
		final GCCoreTileEntityRefinery var7 = (GCCoreTileEntityRefinery) par1World.getBlockTileEntity(par2, par3, par4);

		if (var7 != null)
		{
			for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
			{
				final ItemStack var9 = var7.getStackInSlot(var8);

				if (var9 != null)
				{
					final float var10 = this.refineryRand.nextFloat() * 0.8F + 0.1F;
					final float var11 = this.refineryRand.nextFloat() * 0.8F + 0.1F;
					final float var12 = this.refineryRand.nextFloat() * 0.8F + 0.1F;

					while (var9.stackSize > 0)
					{
						int var13 = this.refineryRand.nextInt(21) + 10;

						if (var13 > var9.stackSize)
						{
							var13 = var9.stackSize;
						}

						var9.stackSize -= var13;
						final EntityItem var14 = new EntityItem(par1World, par2 + var10, par3 + var11, par4 + var12, new ItemStack(var9.itemID, var13, var9.getItemDamage()));

						if (var9.hasTagCompound())
						{
							var14.getEntityItem().setTagCompound((NBTTagCompound) var9.getTagCompound().copy());
						}

						final float var15 = 0.05F;
						var14.motionX = (float) this.refineryRand.nextGaussian() * var15;
						var14.motionY = (float) this.refineryRand.nextGaussian() * var15 + 0.2F;
						var14.motionZ = (float) this.refineryRand.nextGaussian() * var15;
						par1World.spawnEntityInWorld(var14);
					}
				}
			}
		}

		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	@Override
	public Icon getIcon(int side, int metadata)
	{
		if (side == metadata + 2)
		{
			return this.iconFuelOutput;
		}
		else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
		{
			return this.iconOilInput;
		}

		if (side == 1)
		{
			return this.iconTop;
		}

		if (side == 0)
		{
			return this.iconMachineSide;
		}

		if (metadata == 0 && side == 4 || metadata == 1 && side == 5 || metadata == 2 && side == 3 || metadata == 3 && side == 2)
		{
			return this.iconFront;
		}

		return this.iconBack;
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
}
