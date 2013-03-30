package micdoodle8.mods.galacticraft.mars.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.API.IDetectableMetadataResource;
import micdoodle8.mods.galacticraft.API.IPlantableMetadataBlock;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
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
public class GCMarsBlock extends Block implements IDetectableMetadataResource, IPlantableMetadataBlock
{
	// Copper Ore Mars, Tin Ore Mars, Desh Ore, Stone, Cobblestone, Grass, Dirt, Dungeon Wall, Decoration Block
    @SideOnly(Side.CLIENT)
    private Icon[] marsBlockIcons;

	public GCMarsBlock(int i)
	{
		super(i, Material.rock);
	}

    @Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.marsBlockIcons = new Icon[16];
        this.marsBlockIcons[0] = par1IconRegister.registerIcon(GalacticraftMars.MARS_TEXTURE_PREFIX + "cobblestone");
        this.marsBlockIcons[1] = par1IconRegister.registerIcon(GalacticraftMars.MARS_TEXTURE_PREFIX + "decoration_desh");
        this.marsBlockIcons[2] = par1IconRegister.registerIcon(GalacticraftMars.MARS_TEXTURE_PREFIX + "dirt");
        this.marsBlockIcons[3] = par1IconRegister.registerIcon(GalacticraftMars.MARS_TEXTURE_PREFIX + "dungeon_wall");
        this.marsBlockIcons[4] = par1IconRegister.registerIcon(GalacticraftMars.MARS_TEXTURE_PREFIX + "grass_side");
        this.marsBlockIcons[5] = par1IconRegister.registerIcon(GalacticraftMars.MARS_TEXTURE_PREFIX + "grass_top");
        this.marsBlockIcons[6] = par1IconRegister.registerIcon(GalacticraftMars.MARS_TEXTURE_PREFIX + "ore_copper");
        this.marsBlockIcons[7] = par1IconRegister.registerIcon(GalacticraftMars.MARS_TEXTURE_PREFIX + "ore_desh");
        this.marsBlockIcons[8] = par1IconRegister.registerIcon(GalacticraftMars.MARS_TEXTURE_PREFIX + "ore_tin");
        this.marsBlockIcons[9] = par1IconRegister.registerIcon(GalacticraftMars.MARS_TEXTURE_PREFIX + "stone");
    }

	@Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftMars.galacticraftMarsTab;
    }

	@Override
    public float getBlockHardness(World par1World, int par2, int par3, int par4)
    {
    	int meta = par1World.getBlockMetadata(par2, par3, par4);
    	
		if (meta == 3 || meta == 5)
		{
			return 0.1F;
		}
		
        return this.blockHardness;
    }

	@Override
    public boolean canHarvestBlock(EntityPlayer player, int meta)
    {
		if (meta == 3 || meta == 5)
		{
			return true;
		}
		
    	return super.canHarvestBlock(player, meta);
    }

	@Override
    public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
    {
		switch (meta)
		{
		case 0:
			return this.marsBlockIcons[6];
		case 1:
			return this.marsBlockIcons[8];
		case 2:
			return this.marsBlockIcons[7];
		case 3:
			return this.marsBlockIcons[9];
		case 4:
			return this.marsBlockIcons[0];
		case 5:
			switch (side)
			{
			case 0:
				return this.marsBlockIcons[5];
			case 1:
				return this.marsBlockIcons[2];
			default:
				return this.marsBlockIcons[4];
			}
		case 6:
			return this.marsBlockIcons[2];
		case 7:
			return this.marsBlockIcons[3];
		case 8:
			return this.marsBlockIcons[1];
		}

		return this.marsBlockIcons[1];
    }

	@Override
	public int idDropped(int meta, Random random, int par3)
	{
		return this.blockID;
	}

	@Override
    public int damageDropped(int meta)
    {
		if (meta == 3)
		{
			return 4;
		}
		else
		{
			return meta;
		}
    }

	@Override
    public int quantityDropped(int meta, int fortune, Random random)
    {
		if (meta == 2)
		{
			return random.nextInt(3) + 1;
		}
		
		return 1;
    }

    @SideOnly(Side.CLIENT)
	@Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 9; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
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

		if (metadata != 5 && metadata != 6)
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
		if (metadata != 5 && metadata != 6)
		{
			return false;
		}

		return true;
	}
}
