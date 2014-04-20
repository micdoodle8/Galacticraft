package micdoodle8.mods.galacticraft.planets.mars.blocks;

import java.util.List;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsBlockTintedGlassPane.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class BlockTintedGlassPane extends BlockPane implements IPartialSealableBlock
{
	@SideOnly(Side.CLIENT)
	private IIcon[] iconArray;

	protected BlockTintedGlassPane()
	{
		super("", "", Material.glass, false);
		String str = "tintedGlassPane";
		this.setBlockName(str);
		this.setBlockTextureName(str);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < this.iconArray.length; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
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
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.iconArray = new IIcon[16];

		for (int i = 0; i < this.iconArray.length; ++i)
		{
			this.iconArray[i] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + this.getTextureName() + "_" + ItemDye.field_150921_b[~i & 15]);
		}

		this.blockIcon = this.iconArray[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2)
	{
		return this.iconArray[par2 % this.iconArray.length];
	}

	public IIcon getSideTextureIndex()
	{
		return this.getSideTextureIndex(0);
	}

	public IIcon getSideTextureIndex(int metadata)
	{
		return this.iconArray[metadata % this.iconArray.length];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass()
	{
		return 1;
	}

	@Override
	public boolean isSealed(World world, int x, int y, int z, ForgeDirection direction)
	{
		boolean connectedNorth = this.canPaneConnectTo(world, x, y, z, ForgeDirection.NORTH);
		boolean connectedSouth = this.canPaneConnectTo(world, x, y, z, ForgeDirection.SOUTH);
		boolean connectedWest = this.canPaneConnectTo(world, x, y, z, ForgeDirection.WEST);
		boolean connectedEast = this.canPaneConnectTo(world, x, y, z, ForgeDirection.EAST);

		switch (direction)
		{
		case UP:
			return false;
		case DOWN:
			return false;
		case NORTH:
			return connectedWest && connectedEast;
		case EAST:
			return connectedNorth && connectedSouth;
		case SOUTH:
			return connectedWest && connectedEast;
		case WEST:
			return connectedNorth && connectedSouth;
		default:
			return false;
		}
	}
}
