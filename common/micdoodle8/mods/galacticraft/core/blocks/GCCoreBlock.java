package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreBlock.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlock extends Block implements IDetectableResource
{
	Icon[] iconBuffer;

	protected GCCoreBlock(int id, String assetName)
	{
		super(id, Material.rock);
		this.setHardness(1.0F);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setUnlocalizedName(assetName);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftTab;
	}

	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		this.iconBuffer = new Icon[11];
		this.iconBuffer[0] = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "deco_aluminium_2");
		this.iconBuffer[1] = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "deco_aluminium_4");
		this.iconBuffer[2] = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "deco_aluminium_1");
		this.iconBuffer[3] = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "deco_aluminium_4");
		this.iconBuffer[4] = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "oreCopper");
		this.iconBuffer[5] = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "oreTin");
		this.iconBuffer[6] = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "oreAluminum");
		this.iconBuffer[7] = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "oreSilicon");
		this.iconBuffer[8] = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "deco_copper_block");
		this.iconBuffer[9] = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "deco_tin_block");
		this.iconBuffer[10] = iconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "deco_aluminium_block");
	}

	@Override
	public Icon getIcon(int side, int meta)
	{
		switch (meta)
		{
		case 3:
			switch (side)
			{
			case 0:
				return this.iconBuffer[1];
			case 1:
				return this.iconBuffer[0];
			default:
				return this.iconBuffer[2];
			}
		case 4:
			return this.iconBuffer[3];
		case 5:
			return this.iconBuffer[4];
		case 6:
			return this.iconBuffer[5];
		case 7:
			return this.iconBuffer[6];
		case 8:
			return this.iconBuffer[7];
		case 9:
			return this.iconBuffer[8];
		case 10:
			return this.iconBuffer[9];
		case 11:
			return this.iconBuffer[10];
		default:
			return meta < this.iconBuffer.length ? this.iconBuffer[meta] : this.iconBuffer[0];
		}
	}

	@Override
	public int idDropped(int meta, Random random, int par3)
	{
		switch (meta)
		{
		case 8:
			return GCCoreItems.basicItem.itemID;
		default:
			return this.blockID;
		}
	}

	@Override
	public int damageDropped(int meta)
	{
		switch (meta)
		{
		case 8:
			return 2;
		default:
			return meta;
		}
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random)
	{
		if (fortune > 0 && this.blockID != this.idDropped(meta, random, fortune))
		{
			int j = random.nextInt(fortune + 2) - 1;

			if (j < 0)
			{
				j = 0;
			}

			return this.quantityDropped(random) * (j + 1);
		}
		else
		{
			return this.quantityDropped(random);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 3; var4 < 12; ++var4)
		{
			par3List.add(new ItemStack(par1, 1, var4));
		}
	}

	@Override
	public boolean isValueable(int metadata)
	{
		return metadata >= 5 && metadata <= 8;
	}
}
