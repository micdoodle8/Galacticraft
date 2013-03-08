package micdoodle8.mods.galacticraft.moon.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.API.IDetectableMetadataResource;
import micdoodle8.mods.galacticraft.API.IPlantableMetadataBlock;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.moon.items.GCMoonItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCMoonBlock extends Block implements IDetectableMetadataResource, IPlantableMetadataBlock
{
	// AluminumMoon: 0, IronMoon: 1, CheeseStone: 2;

	public GCMoonBlock(int i)
	{
		super(i, 4, Material.rock);
        this.setRequiresSelfNotify();
        this.setCreativeTab(GalacticraftCore.galacticraftTab);
	}

	@Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta)
    {
		if (meta >= 5 && meta <= 13)
		{
			if (side == 1)
			{
				switch (meta - 5)
				{
				case 0:
					return 0;
				case 1:
					return 4;
				case 2:
					return 5;
				case 3:
					return 20;
				case 4:
					return 21;
				case 5:
					return 22;
				case 6:
					return 23;
				case 7:
					return 24;
				case 8:
					return 25;
				}
			}
			else if (side == 0)
			{
				return 2;
			}
		}
		else
		{
			switch (meta)
			{
			case 0:
				return 6;
			case 1:
				return 7;
			case 2:
				return 8;
			case 3:
				return 2;
			case 4:
				return 9;
			case 14:
				return 1;
			default:
				return meta;
			}
		}

		return 3;
    }

	@Override
	public int idDropped(int meta, Random random, int par3)
	{
		switch (meta)
		{
		case 2:
			return GCMoonItems.cheeseCurd.itemID;
		default:
			return this.blockID;
		}
	}

	@Override
    public int damageDropped(int meta)
    {
		if (meta >= 5 && meta <= 13)
		{
			return 5;
		}
		else
		{
			return meta;
		}
    }

	@Override
    public int quantityDropped(int meta, int fortune, Random random)
    {
		return 1;
    }

    @SideOnly(Side.CLIENT)
	@Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 6; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }

        par3List.add(new ItemStack(par1, 1, 14));
    }

	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/moon/client/blocks/moon.png";
	}

	@Override
	public boolean isValueable(int metadata)
	{
		switch (metadata)
		{
		case 0:
			return true;
		case 1:
			return true;
		case 2:
			return true;
		default:
			return false;
		}
	}

	@Override
    public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant)
    {
		final int metadata = world.getBlockMetadata(x, y, z);

		if (metadata < 5 && metadata > 13)
		{
			return false;
		}

        plant.getPlantID(world, x, y + 1, z);

        if (plant instanceof BlockFlower)
        {
            return true;
        }

        return false;
    }

	@Override
	public int requiredLiquidBlocksNearby()
	{
		return 4;
	}

	@Override
	public boolean isPlantable(int metadata)
	{
		if (metadata >= 5 && metadata <= 13)
		{
			return true;
		}

		return false;
	}
}
