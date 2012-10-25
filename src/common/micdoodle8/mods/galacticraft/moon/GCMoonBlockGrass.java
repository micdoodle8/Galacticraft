package micdoodle8.mods.galacticraft.moon;

import java.util.Random;

import micdoodle8.mods.galacticraft.API.GCBlockGrass;
import micdoodle8.mods.galacticraft.moon.client.GCMoonColorizerGrass;
import net.minecraft.src.Block;
import net.minecraft.src.BlockFlower;
import net.minecraft.src.ColorizerGrass;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMoonBlockGrass extends GCBlockGrass
{
	public GCMoonBlockGrass(int par1, int par2) 
	{
		super(par1, par2);
		this.setTickRandomly(true);
		this.setStepSound(Block.soundClothFootstep);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setRequiresSelfNotify();
	}

//	@Override
//    public int getRenderType()
//    {
//        return ClientProxyMoon.getGCMoonTurfRenderID();
//    }
    
	@Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta)
    {
		if (side == 1)
		{
			if (meta == 0)
			{
				return 0;
			}
			else if (meta == 1)
			{
				return 4;
			}
			else if (meta == 2)
			{
				return 5;
			}
		}
		else if (side == 0)
		{
			return 2;
		}
		
		return 3;
    }
    
	@Override
	public int getBlockTexture(IBlockAccess par1IBlockAccess, int x, int y, int z, int side) 
	{
		int meta = par1IBlockAccess.getBlockMetadata(x, y, z);
		
        if (side == 1)
        {
			if (meta == 0)
			{
				return 0;
			}
			else if (meta == 1)
			{
				return 4;
			}
			else if (meta == 2)
			{
				return 5;
			}
        }
        else if (side == 0)
        {
            return 2;
        }
        
        Material var6 = par1IBlockAccess.getBlockMaterial(x, y + 1, z);
        return var6 != Material.snow && var6 != Material.craftedSnow ? 3 : 68;
	}

	@Override
    public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant)
    {
        int plantID = plant.getPlantID(world, x, y + 1, z);
        
        if (plant instanceof BlockFlower)
        {
            return true;
        }
        
        return false;
    }

	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) 
	{
		if (!par1World.isRemote) 
		{
			if (par1World.getBlockLightValue(par2, par3 + 1, par4) < 4 && Block.lightOpacity[par1World.getBlockId(par2, par3 + 1, par4)] > 2)
			{
				par1World.setBlockWithNotify(par2, par3, par4, GCMoonBlocks.moonDirt.blockID);
			} 
			else if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9) 
			{
				for (int var6 = 0; var6 < 4; ++var6) 
				{
					int var7 = par2 + par5Random.nextInt(3) - 1;
					int var8 = par3 + par5Random.nextInt(5) - 3;
					int var9 = par4 + par5Random.nextInt(3) - 1;
					
					int var10 = par1World.getBlockId(var7, var8 + 1, var9);

					if (par1World.getBlockId(var7, var8, var9) == GCMoonBlocks.moonDirt.blockID	&& par1World.getBlockLightValue(var7, var8 + 1, var9) >= 4 && Block.lightOpacity[var10] <= 2) 
					{
						par1World.setBlockWithNotify(var7, var8, var9, this.blockID);
					}
				}
			}
		}
	}

    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        int var5 = 0;
        int var6 = 0;
        int var7 = 0;

        for (int var8 = -1; var8 <= 1; ++var8)
        {
            for (int var9 = -1; var9 <= 1; ++var9)
            {
            	int var10 = getGrassColorAtYCoord(50);
                var5 += (var10 & 255);
                var6 += (var10 & 255);
                var7 += var10 & 255;
            }
        }

        return (var5 / 9 & 255) << 16 | (var6 / 9 & 255) << 8 | var7 / 9 & 255;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderColor(int par1)
    {
        return this.getBlockColor();
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int var5 = 0;
        int var6 = 0;
        int var7 = 0;

        for (int var8 = -1; var8 <= 1; ++var8)
        {
            for (int var9 = -1; var9 <= 1; ++var9)
            {
            	int var10 = getGrassColorAtYCoord(par3);
                var5 += (var10 & 255);
                var6 += (var10 & 255);
                var7 += var10 & 255;
            }
        }

        return (var5 / 9 & 255) << 16 | (var6 / 9 & 255) << 8 | var7 / 9 & 255;
    }
    
    private int getGrassColorAtYCoord(int y)
    {
    	return GCMoonColorizerGrass.getGrassColor((y + 100) / 1.7D, (y + 100) / 1.7D);
    }

	@Override
	public int idDropped(int par1, Random par2Random, int par3) 
	{
		return GCMoonBlocks.moonDirt.blockID;
	}

	@Override
    public String getTextureFile()
    {
		return "/micdoodle8/mods/galacticraft/moon/client/blocks/moon.png";
    }
}
