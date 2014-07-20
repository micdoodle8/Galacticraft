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
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
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
		this.blockIcon = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "space_station_top");
		this.iconInput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_input");

		this.iconMachineSide = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "machine_blank");
		this.iconGasLiquefier = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "machine_blank");
		this.iconGasInput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_oxygen_input");
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
}
