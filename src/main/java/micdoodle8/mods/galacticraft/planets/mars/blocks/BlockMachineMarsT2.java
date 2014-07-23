package micdoodle8.mods.galacticraft.planets.mars.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityUniversalElectrical;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityGasLiquefier;
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
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockMachineMarsT2 extends BlockTileGC
{
	public static final int GAS_LIQUEFIER = 0;

	private IIcon iconMachineSide;
	private IIcon iconInput;

	private IIcon iconGasLiquefier;
	private IIcon iconGasInput;

	public BlockMachineMarsT2()
	{
		super(GCBlocks.machine);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon("galacticraftasteroids:machine");
		this.iconInput = par1IconRegister.registerIcon("galacticraftasteroids:machine_output");

		this.iconMachineSide = par1IconRegister.registerIcon("galacticraftasteroids:machine_side");
		this.iconGasLiquefier = par1IconRegister.registerIcon("galacticraftasteroids:gasLiquefier");
		this.iconGasInput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_oxygen_input");
	}

	@Override
	public void breakBlock(World var1, int var2, int var3, int var4, Block var5, int var6)
	{
		final TileEntity z = var1.getTileEntity(var2, var3, var4);

		if (z instanceof IMultiBlock)
		{
			((IMultiBlock) z).onDestroy(z);
		}

		super.breakBlock(var1, var2, var3, var4, var5, var6);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftBlocksTab;
	}

	@Override
	public int getRenderType()
	{
		return GalacticraftPlanets.getBlockRenderID(this);
	}

	@Override
	public IIcon getIcon(int side, int metadata)
	{
		int metaside = (metadata & 3) + 2;
		
		if (side == 0 || side == 1)
		{
			return this.blockIcon;
		}

		if (side == metaside)
		{
			return this.iconInput;
		}
		else if (side == (metaside ^ 1))
		{
			return this.iconGasInput;
		}
		
		return this.iconGasLiquefier;	
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

		world.setBlockMetadataWithNotify(x, y, z, (metadata & 12) + change, 3);
	}

	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);
		int original = metadata & 3;
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

		TileEntity te = par1World.getTileEntity(x, y, z);
		if (te instanceof TileEntityUniversalElectrical)
				((TileEntityUniversalElectrical) te).updateFacing();

		par1World.setBlockMetadataWithNotify(x, y, z, (metadata & 12) + change, 3);
		return true;
	}

	/**
	 * Called when the block is right clicked by the player
	 */
	@Override
	public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = world.getBlockMetadata(x, y, z);

		par5EntityPlayer.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_MARS, world, x, y, z);
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new TileEntityGasLiquefier();
	}

	public ItemStack getGasLiquefier()
	{
		return new ItemStack(this, 1, BlockMachineMarsT2.GAS_LIQUEFIER);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(this.getGasLiquefier());
	}

	@Override
	public int damageDropped(int metadata)
	{
		return metadata & 12;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		int metadata = this.getDamageValue(world, x, y, z);

		return new ItemStack(this, 1, metadata);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random rand)
	{
		final TileEntity te = par1World.getTileEntity(par2, par3, par4);

		if (te instanceof TileEntityGasLiquefier)
		{
			final TileEntityGasLiquefier tileEntity = (TileEntityGasLiquefier) te;

			if (tileEntity.processTicks > 0)
			{
				//par1World.getBlockMetadata(par2, par3, par4);
				final float x = par2 + 0.5F;
				final float y = par3 + 0.1F + 0.05F * rand.nextInt(3);
				final float z = par4 + 0.5F;

				for (float i = -0.41F + 0.16F * rand.nextFloat(); i < 0.5F; i+= 0.167F)
				{
					if (rand.nextInt(4) == 0) par1World.spawnParticle("smoke", x + i, y, par2 - 0.01F, 0.0D, -0.01D, -0.005D);
					if (rand.nextInt(4) == 0) par1World.spawnParticle("smoke", x + i, y, par2 + 1.01F, 0.0D, -0.01D, 0.005D);
					if (rand.nextInt(4) == 0) par1World.spawnParticle("smoke", x - 0.01F, y, z + i, -0.005D, -0.01D, 0.0D);
					if (rand.nextInt(4) == 0) par1World.spawnParticle("smoke", x + 1.01F, y, z + i, 0.005D, -0.01D, 0.0D);
				}
			}
		}
	}
}
