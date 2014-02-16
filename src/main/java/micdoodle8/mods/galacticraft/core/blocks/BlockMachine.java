package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCoalGenerator;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricFurnace;
import micdoodle8.mods.galacticraft.core.tile.TileEntityEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.tile.TileEntityIngotCompressor;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * GCCoreBlockMachine.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class BlockMachine extends BlockTile
{
	public static final int COAL_GENERATOR_METADATA = 0;
	public static final int STORAGE_MODULE_METADATA = 4;
	public static final int ELECTRIC_FURNACE_METADATA = 8;
	public static final int COMPRESSOR_METADATA = 12;

	private IIcon iconMachineSide;
	private IIcon iconInput;
	private IIcon iconOutput;

	private IIcon iconCoalGenerator;
	private IIcon[] iconEnergyStorageModule;
	private IIcon iconElectricFurnace;
	private IIcon iconCompressor;

	public BlockMachine(String assetName)
	{
		super(GCBlocks.machine);
		this.setHardness(1.0F);
		this.setStepSound(Block.soundTypeMetal);
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
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.blockIcon = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine");
		this.iconInput = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_input");
		this.iconOutput = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_output");

		this.iconMachineSide = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_side");
		this.iconCoalGenerator = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "coalGenerator");
		this.iconEnergyStorageModule = new IIcon[17];

		for (int i = 0; i < this.iconEnergyStorageModule.length; i++)
		{
			this.iconEnergyStorageModule[i] = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "energyStorageModule_" + i);
		}

		this.iconElectricFurnace = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "electricFurnace");
		this.iconCompressor = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "compressor");
	}

	@Override
	public void randomDisplayTick(World par1World, int x, int y, int z, Random par5Random)
	{
		TileEntity tile = par1World.getTileEntity(x, y, z);

		if (tile instanceof TileEntityCoalGenerator)
		{
			TileEntityCoalGenerator tileEntity = (TileEntityCoalGenerator) tile;
			if (tileEntity.generateWatts > 0)
			{
				int metadata = par1World.getBlockMetadata(x, y, z);
				float var7 = x + 0.5F;
				float var8 = y + 0.0F + par5Random.nextFloat() * 6.0F / 16.0F;
				float var9 = z + 0.5F;
				float var10 = 0.52F;
				float var11 = par5Random.nextFloat() * 0.6F - 0.3F;

				if (metadata == 3)
				{
					par1World.spawnParticle("smoke", var7 - var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
					par1World.spawnParticle("flame", var7 - var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
				}
				else if (metadata == 2)
				{
					par1World.spawnParticle("smoke", var7 + var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
					par1World.spawnParticle("flame", var7 + var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
				}
				else if (metadata == 1)
				{
					par1World.spawnParticle("smoke", var7 + var11, var8, var9 - var10, 0.0D, 0.0D, 0.0D);
					par1World.spawnParticle("flame", var7 + var11, var8, var9 - var10, 0.0D, 0.0D, 0.0D);
				}
				else if (metadata == 0)
				{
					par1World.spawnParticle("smoke", var7 + var11, var8, var9 + var10, 0.0D, 0.0D, 0.0D);
					par1World.spawnParticle("flame", var7 + var11, var8, var9 + var10, 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		int metadata = world.getBlockMetadata(x, y, z);

		if (metadata >= BlockMachine.STORAGE_MODULE_METADATA && metadata < BlockMachine.ELECTRIC_FURNACE_METADATA)
		{
			TileEntity tile = world.getTileEntity(x, y, z);

			metadata -= BlockMachine.STORAGE_MODULE_METADATA;

			if (side == 0 || side == 1)
			{
				return this.blockIcon;
			}

			// If it is the front side
			if (side == metadata + 2)
			{
				return this.iconOutput;
			}
			// If it is the back side
			else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
			{
				return this.iconInput;
			}

			if (tile instanceof TileEntityEnergyStorageModule)
			{
				return this.iconEnergyStorageModule[((TileEntityEnergyStorageModule) tile).scaledEnergyLevel];
			}
			else
			{
				return this.iconEnergyStorageModule[0];
			}
		}

		return this.getIcon(side, world.getBlockMetadata(x, y, z));
	}

	@Override
	public IIcon getIcon(int side, int metadata)
	{
		if (side == 0 || side == 1)
		{
			return this.blockIcon;
		}

		if (metadata >= BlockMachine.COMPRESSOR_METADATA)
		{
			metadata -= BlockMachine.COMPRESSOR_METADATA;

			if (metadata == 0 && side == 4 || metadata == 1 && side == 5 || metadata == 2 && side == 3 || metadata == 3 && side == 2)
			{
				return this.iconCompressor;
			}
		}
		else if (metadata >= BlockMachine.ELECTRIC_FURNACE_METADATA)
		{
			metadata -= BlockMachine.ELECTRIC_FURNACE_METADATA;

			// If it is the front side
			if (side == metadata + 2)
			{
				return this.iconInput;
			}
			// If it is the back side
			else if (metadata == 0 && side == 4 || metadata == 1 && side == 5 || metadata == 2 && side == 3 || metadata == 3 && side == 2)
			{
				return this.iconElectricFurnace;
			}
		}
		else if (metadata >= BlockMachine.STORAGE_MODULE_METADATA)
		{
			metadata -= BlockMachine.STORAGE_MODULE_METADATA;

			// If it is the front side
			if (side == metadata + 2)
			{
				return this.iconOutput;
			}
			// If it is the back side
			else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
			{
				return this.iconInput;
			}

			return this.iconEnergyStorageModule[16];
		}
		else
		{
			// If it is the front side
			if (side == metadata + 2)
			{
				return this.iconOutput;
			}
			// If it is the back side
			if (metadata == 0 && side == 4 || metadata == 1 && side == 5 || metadata == 2 && side == 3 || metadata == 3 && side == 2)
			{
				return this.iconCoalGenerator;
			}
		}

		return this.iconMachineSide;
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

		if (metadata >= BlockMachine.COMPRESSOR_METADATA)
		{
			world.setBlockMetadataWithNotify(x, y, z, BlockMachine.COMPRESSOR_METADATA + change, 3);
		}
		else if (metadata >= BlockMachine.ELECTRIC_FURNACE_METADATA)
		{
			world.setBlockMetadataWithNotify(x, y, z, BlockMachine.ELECTRIC_FURNACE_METADATA + change, 3);
		}
		else if (metadata >= BlockMachine.STORAGE_MODULE_METADATA)
		{
			world.setBlockMetadataWithNotify(x, y, z, BlockMachine.STORAGE_MODULE_METADATA + change, 3);
		}
		else
		{
			world.setBlockMetadataWithNotify(x, y, z, BlockMachine.COAL_GENERATOR_METADATA + change, 3);
		}
	}

	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);
		int original = metadata;

		int change = 0;

		if (metadata >= BlockMachine.COMPRESSOR_METADATA)
		{
			original -= BlockMachine.COMPRESSOR_METADATA;
		}
		else if (metadata >= BlockMachine.ELECTRIC_FURNACE_METADATA)
		{
			original -= BlockMachine.ELECTRIC_FURNACE_METADATA;
		}
		else if (metadata >= BlockMachine.STORAGE_MODULE_METADATA)
		{
			original -= BlockMachine.STORAGE_MODULE_METADATA;
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

		if (metadata >= BlockMachine.COMPRESSOR_METADATA)
		{
			change += BlockMachine.COMPRESSOR_METADATA;
		}
		else if (metadata >= BlockMachine.ELECTRIC_FURNACE_METADATA)
		{
			change += BlockMachine.ELECTRIC_FURNACE_METADATA;
		}
		else if (metadata >= BlockMachine.STORAGE_MODULE_METADATA)
		{
			change += BlockMachine.STORAGE_MODULE_METADATA;
		}

		par1World.setBlockMetadataWithNotify(x, y, z, change, 3);
		return true;
	}

	/**
	 * Called when the block is right clicked by the player
	 */
	@Override
	public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);

		if (!par1World.isRemote)
		{
			if (metadata >= BlockMachine.COMPRESSOR_METADATA)
			{
				par5EntityPlayer.openGui(GalacticraftCore.instance, -1, par1World, x, y, z);
				return true;
			}
			else if (metadata >= BlockMachine.ELECTRIC_FURNACE_METADATA)
			{
				par5EntityPlayer.openGui(GalacticraftCore.instance, -1, par1World, x, y, z);
				return true;
			}
			else if (metadata >= BlockMachine.STORAGE_MODULE_METADATA)
			{
				par5EntityPlayer.openGui(GalacticraftCore.instance, -1, par1World, x, y, z);
				return true;
			}
			else
			{
				par5EntityPlayer.openGui(GalacticraftCore.instance, -1, par1World, x, y, z);
				return true;
			}
		}

		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		if (metadata >= BlockMachine.COMPRESSOR_METADATA)
		{
			return new TileEntityIngotCompressor();
		}
		else if (metadata >= BlockMachine.ELECTRIC_FURNACE_METADATA)
		{
			return new TileEntityElectricFurnace();
		}
		else if (metadata >= BlockMachine.STORAGE_MODULE_METADATA)
		{
			return new TileEntityEnergyStorageModule();
		}
		else
		{
			return new TileEntityCoalGenerator();
		}
	}

	public ItemStack getCompressor()
	{
		return new ItemStack(this, 1, BlockMachine.COMPRESSOR_METADATA);
	}

	public ItemStack getCoalGenerator()
	{
		return new ItemStack(this, 1, BlockMachine.COAL_GENERATOR_METADATA);
	}

	public ItemStack getEnergyStorageModule()
	{
		return new ItemStack(this, 1, BlockMachine.STORAGE_MODULE_METADATA);
	}

	public ItemStack getElectricFurnace()
	{
		return new ItemStack(this, 1, BlockMachine.ELECTRIC_FURNACE_METADATA);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(this.getCoalGenerator());
		par3List.add(this.getEnergyStorageModule());
		par3List.add(this.getElectricFurnace());
		par3List.add(this.getCompressor());
	}

	@Override
	public int damageDropped(int metadata)
	{
		if (metadata >= BlockMachine.COMPRESSOR_METADATA)
		{
			return BlockMachine.COMPRESSOR_METADATA;
		}
		else if (metadata >= BlockMachine.ELECTRIC_FURNACE_METADATA)
		{
			return BlockMachine.ELECTRIC_FURNACE_METADATA;
		}
		else if (metadata >= BlockMachine.STORAGE_MODULE_METADATA)
		{
			return BlockMachine.STORAGE_MODULE_METADATA;
		}
		else
		{
			return BlockMachine.COAL_GENERATOR_METADATA;
		}
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) 
	{
		return null;
	}
}
