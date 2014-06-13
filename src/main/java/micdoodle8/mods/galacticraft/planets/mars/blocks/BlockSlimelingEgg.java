package micdoodle8.mods.galacticraft.planets.mars.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntitySlimelingEgg;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



public class BlockSlimelingEgg extends Block implements ITileEntityProvider
{
	private IIcon[] icons;
	public static String[] names = { "redEgg", "blueEgg", "yellowEgg" };

	public BlockSlimelingEgg()
	{
		super(Material.rock);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.icons = new IIcon[6];
		this.icons[0] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "redEgg_0");
		this.icons[1] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "blueEgg_0");
		this.icons[2] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "yellowEgg_0");
		this.icons[3] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "redEgg_1");
		this.icons[4] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "blueEgg_1");
		this.icons[5] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "yellowEgg_1");
		this.blockIcon = this.icons[0];
	}

	@Override
	public boolean canBlockStay(World par1World, int par2, int par3, int par4)
	{
		Block block = par1World.getBlock(par2, par3 - 1, par4);
		return block.isSideSolid(par1World, par2, par3, par4, ForgeDirection.UP);
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z)
	{
		ItemStack currentStack = player.getCurrentEquippedItem();
		int l = world.getBlockMetadata(x, y, z);

		if (currentStack != null && currentStack.getItem() instanceof ItemPickaxe)
		{
			return world.setBlockToAir(x, y, z);
		}
		else if (l < 3)
		{
			world.setBlockMetadataWithNotify(x, y, z, l + 3, 2);

			TileEntity tile = world.getTileEntity(x, y, z);

			if (tile instanceof TileEntitySlimelingEgg)
			{
				((TileEntitySlimelingEgg) tile).timeToHatch = world.rand.nextInt(500) + 100;
				((TileEntitySlimelingEgg) tile).lastTouchedPlayer = player.getGameProfile().getName();
			}

			return false;
		}
		else if (player.capabilities.isCreativeMode)
		{
			return world.setBlockToAir(x, y, z);
		}
		else
		{
			return false;
		}
	}

	@Override
	public void harvestBlock(World world, EntityPlayer par2EntityPlayer, int x, int y, int z, int par6)
	{
		ItemStack currentStack = par2EntityPlayer.getCurrentEquippedItem();

		if (currentStack != null && currentStack.getItem() instanceof ItemPickaxe)
		{
			par2EntityPlayer.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
			par2EntityPlayer.addExhaustion(0.025F);
			this.dropBlockAsItem(world, x, y, z, par6 % 3, 0);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata)
	{
		return this.icons[metadata % 6];
	}

	@Override
	public int getRenderType()
	{
		return GalacticraftPlanets.getBlockRenderID(this);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return MarsModule.galacticraftMarsTab;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public Item getItemDropped(int meta, Random random, int par3)
	{
		return Item.getItemFromBlock(this);
	}

	@Override
	public int damageDropped(int meta)
	{
		return meta;
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random)
	{
		return 1;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 0; var4 < BlockSlimelingEgg.names.length; ++var4)
		{
			par3List.add(new ItemStack(par1, 1, var4));
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntitySlimelingEgg();
	}
}
