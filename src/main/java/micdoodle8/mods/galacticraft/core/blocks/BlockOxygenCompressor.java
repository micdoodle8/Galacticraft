package micdoodle8.mods.galacticraft.core.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenCompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDecompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityUniversalElectrical;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class BlockOxygenCompressor extends BlockAdvancedTile
{
	public static final int OXYGEN_COMPRESSOR_METADATA = 0;
	public static final int OXYGEN_DECOMPRESSOR_METADATA = 4;

	private IIcon iconMachineSide;
	private IIcon iconCompressor1;
	private IIcon iconCompressor2;
	private IIcon iconDecompressor;
	private IIcon iconOxygenInput;
	private IIcon iconOxygenOutput;
	private IIcon iconInput;

	public BlockOxygenCompressor(boolean isActive, String assetName)
	{
		super(Material.rock);
		this.setHardness(1.0F);
		this.setStepSound(Block.soundTypeStone);
		this.setBlockTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setBlockName(assetName);
	}

	@Override
	public int getRenderType()
	{
		return GalacticraftCore.proxy.getBlockRender(this);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftBlocksTab;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.iconMachineSide = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_blank");
		this.iconCompressor1 = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_compressor_1");
		this.iconCompressor2 = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_compressor_2");
		this.iconDecompressor = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_decompressor_1");
		this.iconOxygenInput = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_oxygen_input");
		this.iconOxygenOutput = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_oxygen_output");
		this.iconInput = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_input");
	}

	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		final int metadata = par1World.getBlockMetadata(x, y, z);
		int original = metadata;

		if (metadata >= BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
		{
			original -= BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA;
		}
		else if (metadata >= BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
		{
			original -= BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA;
		}

		int meta = 0;

		// Re-orient the block
		switch (original)
		{
		case 0:
			meta = 3;
			break;
		case 3:
			meta = 1;
			break;
		case 1:
			meta = 2;
			break;
		case 2:
			meta = 0;
			break;
		}

		if (metadata >= BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
		{
			meta += BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA;
		}
		else if (metadata >= BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
		{
			meta += BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA;
		}

		TileEntity te = par1World.getTileEntity(x,  y,  z);
		if (te instanceof TileEntityUniversalElectrical)
			((TileEntityUniversalElectrical) te).updateFacing();

		par1World.setBlockMetadataWithNotify(x, y, z, meta, 3);
		return true;
	}

	@Override
	public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		entityPlayer.openGui(GalacticraftCore.instance, -1, world, x, y, z);
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		if (metadata >= BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
		{
			return new TileEntityOxygenDecompressor();
		}
		else if (metadata >= BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
		{
			return new TileEntityOxygenCompressor();
		}
		else
		{
			return null;
		}
	}

	@Override
	public IIcon getIcon(int side, int metadata)
	{
		if (side == 0 || side == 1)
		{
			return this.iconMachineSide;
		}

		if (metadata >= BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
		{
			metadata -= BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA;

			if (side == metadata + 2)
			{
				return this.iconInput;
			}
			else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
			{
				return this.iconOxygenOutput;
			}
			else if (metadata == 0 && side == 5 || metadata == 3 && side == 3 || metadata == 1 && side == 4 || metadata == 2 && side == 2)
			{
				return this.iconCompressor2;
			}
			else
			{
				return this.iconDecompressor;
			}
		}
		else if (metadata >= BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
		{
			metadata -= BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA;

			if (side == metadata + 2)
			{
				return this.iconInput;
			}
			else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
			{
				return this.iconOxygenInput;
			}
			else if (metadata == 0 && side == 5 || metadata == 3 && side == 3 || metadata == 1 && side == 4 || metadata == 2 && side == 2)
			{
				return this.iconCompressor2;
			}
			else
			{
				return this.iconCompressor1;
			}
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

		if (itemStack.getItemDamage() >= BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
		{
			change += BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA;
		}
		else if (itemStack.getItemDamage() >= BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
		{
			change += BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA;
		}

		world.setBlockMetadataWithNotify(x, y, z, change, 3);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(this, 1, BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA));
		par3List.add(new ItemStack(this, 1, BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA));
	}

	@Override
	public int damageDropped(int metadata)
	{
		if (metadata >= BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
		{
			return BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA;
		}
		else if (metadata >= BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
		{
			return BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA;
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
