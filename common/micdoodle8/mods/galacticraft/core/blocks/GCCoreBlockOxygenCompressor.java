package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDecompressor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreBlockOxygenCompressor.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockOxygenCompressor extends GCCoreBlockAdvancedTile
{
	public static final int OXYGEN_COMPRESSOR_METADATA = 0;
	public static final int OXYGEN_DECOMPRESSOR_METADATA = 4;

	private Icon iconMachineSide;
	private Icon iconCompressor1;
	private Icon iconCompressor2;
	private Icon iconDecompressor;
	private Icon iconOxygenInput;
	private Icon iconOxygenOutput;
	private Icon iconInput;

	public GCCoreBlockOxygenCompressor(int id, boolean isActive, String assetName)
	{
		super(id, Material.rock);
		this.setHardness(1.0F);
		this.setStepSound(Block.soundStoneFootstep);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setUnlocalizedName(assetName);
	}

	@Override
	public int getRenderType()
	{
		return GalacticraftCore.proxy.getBlockRenderID(this.blockID);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftTab;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
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

		if (metadata >= GCCoreBlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
		{
			original -= GCCoreBlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA;
		}
		else if (metadata >= GCCoreBlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
		{
			original -= GCCoreBlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA;
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

		if (metadata >= GCCoreBlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
		{
			meta += GCCoreBlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA;
		}
		else if (metadata >= GCCoreBlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
		{
			meta += GCCoreBlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA;
		}

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
		if (metadata >= GCCoreBlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
		{
			return new GCCoreTileEntityOxygenDecompressor();
		}
		else if (metadata >= GCCoreBlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
		{
			return new GCCoreTileEntityOxygenCompressor();
		}
		else
		{
			return null;
		}
	}

	@Override
	public Icon getIcon(int side, int metadata)
	{
		if (side == 0 || side == 1)
		{
			return this.iconMachineSide;
		}

		if (metadata >= GCCoreBlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
		{
			metadata -= GCCoreBlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA;

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
		else if (metadata >= GCCoreBlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
		{
			metadata -= GCCoreBlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA;

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

		if (itemStack.getItemDamage() >= GCCoreBlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
		{
			change += GCCoreBlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA;
		}
		else if (itemStack.getItemDamage() >= GCCoreBlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
		{
			change += GCCoreBlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA;
		}

		world.setBlockMetadataWithNotify(x, y, z, change, 3);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(this.blockID, 1, GCCoreBlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA));
		par3List.add(new ItemStack(this.blockID, 1, GCCoreBlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA));
	}

	@Override
	public int damageDropped(int metadata)
	{
		if (metadata >= GCCoreBlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
		{
			return GCCoreBlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA;
		}
		else if (metadata >= GCCoreBlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
		{
			return GCCoreBlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA;
		}
		else
		{
			return 0;
		}
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		Item item = Item.itemsList[this.blockID];

		if (item == null)
		{
			return null;
		}

		int metadata = this.getDamageValue(world, x, y, z);

		return new ItemStack(this.blockID, 1, metadata);
	}
}
