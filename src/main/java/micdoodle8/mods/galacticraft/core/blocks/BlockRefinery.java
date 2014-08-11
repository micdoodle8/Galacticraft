package micdoodle8.mods.galacticraft.core.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityRefinery;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class BlockRefinery extends BlockAdvancedTile implements ItemBlockDesc.IBlockShiftDesc
{
	private final Random refineryRand = new Random();

	private IIcon iconMachineSide;
	private IIcon iconFuelOutput;
	private IIcon iconOilInput;
	private IIcon iconFront;
	private IIcon iconBack;
	private IIcon iconTop;

	protected BlockRefinery(String assetName)
	{
		super(Material.rock);
		this.setHardness(1.0F);
		this.setStepSound(Block.soundTypeMetal);
		this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
		this.setBlockName(assetName);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftBlocksTab;
	}

	@Override
	public int getRenderType()
	{
		return GalacticraftCore.proxy.getBlockRender(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.iconMachineSide = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_blank");
		this.iconFuelOutput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_fuel_input");
		this.iconOilInput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_oil_input");
		this.iconFront = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "refinery_front");
		this.iconBack = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "refinery_side");
		this.iconTop = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_input");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		final TileEntity te = par1World.getTileEntity(par2, par3, par4);

		if (te instanceof TileEntityRefinery)
		{
			final TileEntityRefinery refinery = (TileEntityRefinery) te;

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
		int change = 0;

		// Re-orient the block
		switch (par1World.getBlockMetadata(x, y, z))
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
		
		TileEntity te = par1World.getTileEntity(x,  y,  z);
		if (te instanceof TileBaseUniversalElectrical)
			((TileBaseUniversalElectrical) te).updateFacing();

		par1World.setBlockMetadataWithNotify(x, y, z, change, 3);
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new TileEntityRefinery();
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
	{
		final TileEntityRefinery var7 = (TileEntityRefinery) par1World.getTileEntity(par2, par3, par4);

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
						final EntityItem var14 = new EntityItem(par1World, par2 + var10, par3 + var11, par4 + var12, new ItemStack(var9.getItem(), var13, var9.getItemDamage()));

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
	public IIcon getIcon(int side, int metadata)
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

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }
}
