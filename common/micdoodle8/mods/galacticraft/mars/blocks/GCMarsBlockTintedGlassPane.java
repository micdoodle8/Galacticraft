package micdoodle8.mods.galacticraft.mars.blocks;

import java.util.List;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
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
public class GCMarsBlockTintedGlassPane extends BlockPane implements IPartialSealableBlock
{
	@SideOnly(Side.CLIENT)
	private Icon[] iconArray;

	protected GCMarsBlockTintedGlassPane(int blockID)
	{
		super(blockID, "", "", Material.glass, false);
		String str = "tintedGlassPane";
		this.setUnlocalizedName(str);
		this.setTextureName(str);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < this.iconArray.length; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	public int getRenderType()
	{
		return GalacticraftMars.proxy.getTintedGlassPaneRenderID();
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftMars.galacticraftMarsTab;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.iconArray = new Icon[16];

		for (int i = 0; i < this.iconArray.length; ++i)
		{
			this.iconArray[i] = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + this.getTextureName() + "_" + ItemDye.dyeItemNames[BlockColored.getDyeFromBlock(i)]);
		}

		this.blockIcon = this.iconArray[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		return this.iconArray[par2 % this.iconArray.length];
	}

	@Override
	public Icon getSideTextureIndex()
	{
		return this.getSideTextureIndex(0);
	}

	public Icon getSideTextureIndex(int metadata)
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
		boolean connectedNorth = this.canThisPaneConnectToThisBlockID(world.getBlockId(x, y, z - 1));
		boolean connectedSouth = this.canThisPaneConnectToThisBlockID(world.getBlockId(x, y, z + 1));
		boolean connectedWest = this.canThisPaneConnectToThisBlockID(world.getBlockId(x - 1, y, z));
		boolean connectedEast = this.canThisPaneConnectToThisBlockID(world.getBlockId(x + 1, y, z));

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
