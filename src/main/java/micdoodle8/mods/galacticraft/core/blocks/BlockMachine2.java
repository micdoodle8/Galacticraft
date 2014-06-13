package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCircuitFabricator;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCoalGenerator;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenStorageModule;
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
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;



public class BlockMachine2 extends BlockTileGC
{
	public static final int ELECTRIC_COMPRESSOR_METADATA = 0;
	public static final int CIRCUIT_FABRICATOR_METADATA = 4;
	public static final int OXYGEN_STORAGE_MODULE_METADATA = 8;

	private IIcon iconMachineSide;
	private IIcon iconOutput;
	private IIcon iconOxygenInput;
	private IIcon iconOxygenOutput;

	private IIcon iconElectricCompressor;
	private IIcon iconCircuitFabricator;
	private IIcon[] iconOxygenStorageModule;

	public BlockMachine2(String assetName)
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
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.blockIcon = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine");
		this.iconOutput = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_output");
		this.iconOxygenInput = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_oxygen_input");
		this.iconOxygenOutput = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_oxygen_output");

		this.iconMachineSide = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_side");
		this.iconElectricCompressor = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "electric_compressor");
		this.iconCircuitFabricator = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "circuit_fabricator");
		this.iconOxygenStorageModule = new IIcon[17];

		for (int i = 0; i < this.iconOxygenStorageModule.length; i++)
		{
			this.iconOxygenStorageModule[i] = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "oxygenStorageModule_" + i);
		}
	}

	@Override
	public int getRenderType()
	{
		return GalacticraftCore.proxy.getBlockRender(this);
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

		if (metadata >= BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA)
		{
			metadata -= BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA;
			TileEntity tile = world.getTileEntity(x, y, z);

			if (side == 0 || side == 1)
			{
				return this.blockIcon;
			}

			// If it is the front side
			if (side == metadata + 2)
			{
				return this.iconOxygenInput;
			}
			// If it is the back side
			else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
			{
				return this.iconOxygenOutput;
			}

			int oxygenLevel = Math.min(((TileEntityOxygenStorageModule) tile).scaledOxygenLevel, 16);

			return this.iconOxygenStorageModule[oxygenLevel];
		}

		return super.getIcon(world, x, y, z, side);
	}

	@Override
	public IIcon getIcon(int side, int metadata)
	{
		if (side == 0 || side == 1)
		{
			return this.blockIcon;
		}

		if (metadata >= BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA)
		{
			metadata -= BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA;

			if (side == 0 || side == 1)
			{
				return this.blockIcon;
			}

			// If it is the front side
			if (side == metadata + 2)
			{
				return this.iconOxygenInput;
			}
			// If it is the back side
			else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
			{
				return this.iconOxygenOutput;
			}

			return this.iconOxygenStorageModule[16];
		}
		else if (metadata >= BlockMachine2.CIRCUIT_FABRICATOR_METADATA)
		{
			metadata -= BlockMachine2.CIRCUIT_FABRICATOR_METADATA;

			if (metadata == 0 && side == 4 || metadata == 1 && side == 5 || metadata == 2 && side == 3 || metadata == 3 && side == 2)
			{
				return this.iconCircuitFabricator;
			}

			if (side == ForgeDirection.getOrientation(metadata + 2).ordinal())
			{
				return this.iconOutput;
			}
		}
		else if (metadata >= BlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
		{
			metadata -= BlockMachine2.ELECTRIC_COMPRESSOR_METADATA;

			if (metadata == 0 && side == 4 || metadata == 1 && side == 5 || metadata == 2 && side == 3 || metadata == 3 && side == 2)
			{
				return this.iconElectricCompressor;
			}

			if (side == ForgeDirection.getOrientation(metadata + 2).ordinal())
			{
				return this.iconOutput;
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

		if (metadata >= BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA)
		{
			world.setBlockMetadataWithNotify(x, y, z, BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA + change, 3);
		}
		else if (metadata >= BlockMachine2.CIRCUIT_FABRICATOR_METADATA)
		{
			world.setBlockMetadataWithNotify(x, y, z, BlockMachine2.CIRCUIT_FABRICATOR_METADATA + change, 3);
		}
		else if (metadata >= BlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
		{
			world.setBlockMetadataWithNotify(x, y, z, BlockMachine2.ELECTRIC_COMPRESSOR_METADATA + change, 3);
		}
	}

	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);
		int original = metadata;

		int change = 0;

		if (metadata >= BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA)
		{
			original -= BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA;
		}
		else if (metadata >= BlockMachine2.CIRCUIT_FABRICATOR_METADATA)
		{
			original -= BlockMachine2.CIRCUIT_FABRICATOR_METADATA;
		}
		else if (metadata >= BlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
		{
			original -= BlockMachine2.ELECTRIC_COMPRESSOR_METADATA;
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

		if (metadata >= BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA)
		{
			change += BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA;
		}
		else if (metadata >= BlockMachine2.CIRCUIT_FABRICATOR_METADATA)
		{
			change += BlockMachine2.CIRCUIT_FABRICATOR_METADATA;
		}
		else if (metadata >= BlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
		{
			change += BlockMachine2.ELECTRIC_COMPRESSOR_METADATA;
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
		if (!par1World.isRemote)
		{
			par5EntityPlayer.openGui(GalacticraftCore.instance, -1, par1World, x, y, z);
			return true;
		}

		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		if (metadata >= BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA)
		{
			return new TileEntityOxygenStorageModule();
		}
		else if (metadata >= BlockMachine2.CIRCUIT_FABRICATOR_METADATA)
		{
			return new TileEntityCircuitFabricator();
		}
		else if (metadata >= BlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
		{
			return new TileEntityElectricIngotCompressor();
		}
		else
		{
			return null;
		}
	}

	public ItemStack getElectricCompressor()
	{
		return new ItemStack(this, 1, BlockMachine2.ELECTRIC_COMPRESSOR_METADATA);
	}

	public ItemStack getCircuitFabricator()
	{
		return new ItemStack(this, 1, BlockMachine2.CIRCUIT_FABRICATOR_METADATA);
	}

	public ItemStack getOxygenStorageModule()
	{
		return new ItemStack(this, 1, BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(this.getElectricCompressor());
		par3List.add(this.getCircuitFabricator());
		par3List.add(this.getOxygenStorageModule());
	}

	@Override
	public int damageDropped(int metadata)
	{
		if (metadata >= BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA)
		{
			return BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA;
		}
		else if (metadata >= BlockMachine2.CIRCUIT_FABRICATOR_METADATA)
		{
			return BlockMachine2.CIRCUIT_FABRICATOR_METADATA;
		}
		else if (metadata >= BlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
		{
			return BlockMachine2.ELECTRIC_COMPRESSOR_METADATA;
		}
		else
		{
			return 0;
		}
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		int metadata = this.getDamageValue(world, x, y, z);

		return new ItemStack(this, 1, metadata);
	}
}
