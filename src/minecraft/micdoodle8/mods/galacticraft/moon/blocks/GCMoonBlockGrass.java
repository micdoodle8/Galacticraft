package micdoodle8.mods.galacticraft.moon.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.API.IPlantableBlock;
import micdoodle8.mods.galacticraft.moon.client.GCMoonColorizerGrass;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMoonBlockGrass extends Block implements IPlantableBlock
{
	public GCMoonBlockGrass(int par1, int par2)
	{
		super(par1, par2, Material.grass);
		this.setTickRandomly(true);
		this.setStepSound(Block.soundClothFootstep);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setRequiresSelfNotify();
	}
    
	@Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta)
    {
		if (side == 1)
		{
			switch (meta)
			{
			case 0:
				return 0;
			case 1:
				return 5;
			case 2:
				return 6;
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
//			if (meta == 0)
//			{
//				return 0;
//			}
//			else if (meta == 1)
//			{
//				return 4;
//			}
//			else if (meta == 2)
//			{
//				return 5;
//			}
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
		final int meta = par1IBlockAccess.getBlockMetadata(x, y, z);
		
		if (side == 1)
		{
			switch (meta)
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
//			if (meta == 0)
//			{
//				return 0;
//			}
//			else if (meta == 1)
//			{
//				return 4;
//			}
//			else if (meta == 2)
//			{
//				return 5;
//			}
		}
		else if (side == 0)
		{
			return 2;
		}
		
		return 3;
	}

	@Override
    public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant)
    {
        final int plantID = plant.getPlantID(world, x, y + 1, z);
        
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
					final int var7 = par2 + par5Random.nextInt(3) - 1;
					final int var8 = par3 + par5Random.nextInt(5) - 3;
					final int var9 = par4 + par5Random.nextInt(3) - 1;
					
					final int var10 = par1World.getBlockId(var7, var8 + 1, var9);

					if (par1World.getBlockId(var7, var8, var9) == GCMoonBlocks.moonDirt.blockID	&& par1World.getBlockLightValue(var7, var8 + 1, var9) >= 4 && Block.lightOpacity[var10] <= 2)
					{
						par1World.setBlockWithNotify(var7, var8, var9, this.blockID);
					}
				}
			}
		}
	}

    @Override
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
            	final int var10 = this.getGrassColorAtYCoord(50);
                var5 += var10 & 255;
                var6 += var10 & 255;
                var7 += var10 & 255;
            }
        }
        
        return (var5 / 9 & 255) << 16 | (var6 / 9 & 255) << 8 | var7 / 9 & 255;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public int getRenderColor(int par1)
    {
        return this.getBlockColor();
    }

    @Override
	@SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        float var5 = 0;
        float var6 = 0;
        float var7 = 0;

        for (int var8 = -1; var8 <= 1; ++var8)
        {
            for (int var9 = -1; var9 <= 1; ++var9)
            {
            	// TODO config boolean to disable this...
            	
            	final int var10 = this.getGrassColorAtYCoord(par3);
            	
                var5 += var10 & 255;
                var6 += var10 & 255;
                var7 += var10 & 255;
                
            	float i = 0;
            	
            	if (FMLClientHandler.instance().getClient().theWorld != null && FMLClientHandler.instance().getClient().thePlayer != null)
                {
                    final float var3 = FMLClientHandler.instance().getClient().theWorld.getCelestialAngle(1.0F);
                    i = var3;
                }
            	
            	var5 -= i * 1.5 - 0.5;
            	var6 -= i * 1.5 - 0.5;
            	var7 -= i * 1.5 - 0.5;
            }
        }


        return (MathHelper.floor_float(var5) / 9 & 255) << 16 | (MathHelper.floor_float(var6) / 9 & 255) << 8 | MathHelper.floor_float(var7) / 9 & 255;
    }
    
    private int getGrassColorAtYCoord(int y)
    {
    	float i = 0;
    	
    	if (FMLClientHandler.instance().getClient().theWorld != null && FMLClientHandler.instance().getClient().thePlayer != null)
        {
            final float var3 = FMLClientHandler.instance().getClient().theWorld.getCelestialAngle(1.0F);
            i = var3;
        }
    	
    	return GCMoonColorizerGrass.getGrassColor((y + 90 + i * 50) / 1.7D, (y + 90 + i * 50) / 1.7D);
    }

	@Override
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    	if (par2 % 16 == 0 && par4 % 16 == 0)
    	{
        	par1World.markBlockForRenderUpdate(par2, par3, par4);
    	}
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

	@Override
	public int requiredLiquidBlocksNearby()
	{
		return 4;
	}
}
