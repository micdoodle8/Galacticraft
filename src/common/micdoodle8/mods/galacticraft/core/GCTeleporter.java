package micdoodle8.mods.galacticraft.core;

import java.util.Random;

import micdoodle8.mods.galacticraft.mars.GCMarsBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Teleporter;
import net.minecraft.src.World;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCTeleporter extends Teleporter 
{
	private Random random;

	public GCTeleporter()
	{
		random = new Random();
	}

	public void placeInPortal(World world, Entity entity) 
	{
		if (placeInExistingPortal(world, entity))
		{
			return;
		} 
		else
		{
			return;
		}
	}

	public boolean placeInExistingPortal(World world, Entity entity) 
	{
//		for (double i = 200; i > 10; i--)
//		{
//			if (world.getBlockMaterial(MathHelper.floor_double(entity.posX), MathHelper.floor_double(i), MathHelper.floor_double(entity.posZ)) != Material.air)
//			{
//				entity.setLocationAndAngles(entity.posX, i + 1D, entity.posZ, entity.rotationYaw, 0.0F);
//				entity.motionX = entity.motionY = entity.motionZ = 0.0D;
//				
//				if (entity instanceof EntityPlayer && !((EntityPlayer) entity).capabilities.isCreativeMode)
//				{
//					EntityItem var14 = new EntityItem(world, MathHelper.floor_double(entity.posX + 0.5D), MathHelper.floor_double(i + 1D), MathHelper.floor_double(entity.posZ + 0.5D), new ItemStack(GCItems.spaceship));
//
//			        float var15 = 0.05F;
//			        var14.motionX = (double)((float)this.random.nextGaussian() * var15);
//			        var14.motionY = (double)((float)this.random.nextGaussian() * var15 + 0.2F);
//			        var14.motionZ = (double)((float)this.random.nextGaussian() * var15);
//			        world.spawnEntityInWorld(var14);
//				}
//				
//		        return true;
//			}
//		}
		
		if (!world.isRemote && entity instanceof EntityPlayer)
		{
			GCEntitySpaceship ship = new GCEntitySpaceship(world, entity.posX, 300, entity.posZ, true);
			EntityPlayer player = (EntityPlayer) entity;
			
			world.spawnEntityInWorld(ship);
			
			player.mountEntity(ship);
			ship.timeSinceEntityEntry = 20;
			ship.launched = true;
		}
		
		return true;
	}
	
	public boolean test(World world, Entity entity)
	{
		short var3 = 128;
        double var4 = -1.0D;
        int var6 = 0;
        int var7 = 0;
        int var8 = 0;
        int var9 = MathHelper.floor_double(entity.posX);
        int var10 = MathHelper.floor_double(entity.posZ);
        double var18;

        for (int var11 = var9 - var3; var11 <= var9 + var3; ++var11)
        {
            double var12 = (double)var11 + 0.5D - entity.posX;

            for (int var14 = var10 - var3; var14 <= var10 + var3; ++var14)
            {
                double var15 = (double)var14 + 0.5D - entity.posZ;

                for (int var17 = world.getActualHeight() - 1; var17 >= 0; --var17)
                {
                    if (world.getBlockId(var11, var17, var14) == GCMarsBlocks.landingPad.blockID)
                    {
                        var18 = (double)var17 + 0.5D - entity.posY;
                        double var20 = var12 * var12 + var18 * var18 + var15 * var15;

                        if (var4 < 0.0D || var20 < var4)
                        {
                            var4 = var20;
                            var6 = var11;
                            var7 = var17;
                            var8 = var14;
                        }
                    }
                }
            }
        }

        if (var4 >= 0.0D)
        {
            double var22 = (double)var6 + 0.5D;
            double var16 = (double)var7 + 0.5D;
            var18 = (double)var8 + 0.5D;

            if (world.getBlockId(var6 - 1, var7, var8) == 0)
            {
            	var22 += 1.0D;
            }

            if (world.getBlockId(var6 + 1, var7, var8) == 0)
            {
            	var22 -= 1.0D;
            }

            if (world.getBlockId(var6, var7, var8 - 1) == 0)
            {
                var18 += 1.0D;
            }

            if (world.getBlockId(var6, var7, var8 + 1) == 0)
            {
                var18 -= 1.0D;
            }

            entity.setLocationAndAngles(var22, var16, var18, entity.rotationYaw, 0.0F);
            entity.motionX = entity.motionY = entity.motionZ = 0.0D;
            return true;
        }
        else
        {
            return false;
        }
	}
	
	public boolean createLandingPad(World world, Entity entity)
	{
        int x = getFirstUncoveredBlock(world, (int)Math.floor(entity.posX), (int)Math.floor(entity.posY))[0];
        int y = getFirstUncoveredBlock(world, (int)Math.floor(entity.posX), (int)Math.floor(entity.posY))[1];
        int z = getFirstUncoveredBlock(world, (int)Math.floor(entity.posX), (int)Math.floor(entity.posY))[2];
        
        for (int x2 = x - 1; x2 < x + 2; x2++)
        {
        	for (int z2 = z - 1; z2 < z + 2; z2++)
            {
        		world.setBlockWithNotify(x2, y, z2, GCMarsBlocks.landingPad.blockID);
            }
        }
        
        return true;
	}

    public Integer[] getFirstUncoveredBlock(World world, int par1, int par2)
    {
        int var3;

        for (var3 = 200; !world.isAirBlock(par1, var3 + 1, par2); --var3)
        {
            ;
        }

        return new Integer[] {par1, var3, par2};
    }
    
//	public boolean createPortal(World world, Entity entity)
//	{
//		byte byte0 = 16;
//		double d = -1D;
//		int i = MathHelper.floor_double(entity.posX);
//		int j = MathHelper.floor_double(entity.posY);
//		int k = MathHelper.floor_double(entity.posZ);
//		int l = i;
//		int i1 = j;
//		int j1 = k;
//		int k1 = 0;
//		int l1 = random.nextInt(4);
//		for (int i2 = i - byte0; i2 <= i + byte0; i2++) 
//		{
//			double d1 = (i2 + 0.5D) - entity.posX;
//			for (int j3 = k - byte0; j3 <= k + byte0; j3++)
//			{
//				double d3 = (j3 + 0.5D) - entity.posZ;
//				for (int k4 = 128 - 1; k4 >= 0; k4--) 
//				{
//					if (!world.isAirBlock(i2, k4, j3))
//					{
//						continue;
//					}
//					for (; k4 > 0 && world.isAirBlock(i2, k4 - 1, j3); k4--)
//					{
//						
//					}
//					label0: for (int k5 = l1; k5 < l1 + 4; k5++)
//					{
//						int l6 = k5 % 2;
//						int i8 = 1 - l6;
//						if (k5 % 4 >= 2) 
//						{
//							l6 = -l6;
//							i8 = -i8;
//						}
//						for (int j9 = 0; j9 < 3; j9++)
//						{
//							for (int k10 = 0; k10 < 4; k10++)
//							{
//								for (int l11 = -1; l11 < 4; l11++)
//								{
//									int j12 = i2 + (k10 - 1) * l6 + j9 * i8;
//									int l12 = k4 + l11;
//									int j13 = (j3 + (k10 - 1) * i8) - j9 * l6;
//									if (l11 < 0 && !world.getBlockMaterial(j12, l12, j13).isSolid() || l11 >= 0 && !world.isAirBlock(j12, l12, j13)) 
//									{
//										break label0;
//									}
//								}
//							}
//						}
//
//						double d5 = (k4 + 0.5D) - entity.posY;
//						double d7 = d1 * d1 + d5 * d5 + d3 * d3;
//						if (d < 0.0D || d7 < d) 
//						{
//							d = d7;
//							l = i2;
//							i1 = k4;
//							j1 = j3;
//							k1 = k5 % 4;
//						}
//					}
//				}
//			}
//		}
//
//		if (d < 0.0D) 
//		{
//			for (int j2 = i - byte0; j2 <= i + byte0; j2++)
//			{
//				double d2 = (j2 + 0.5D) - entity.posX;
//				for (int k3 = k - byte0; k3 <= k + byte0; k3++)
//				{
//					double d4 = (k3 + 0.5D) - entity.posZ;
//					for (int l4 = 128 - 1; l4 >= 0; l4--) 
//					{
//						if (!world.isAirBlock(j2, l4, k3)) 
//						{
//							continue;
//						}
//						for (; l4 > 0 && world.isAirBlock(j2, l4 - 1, k3); l4--)
//						{
//							
//						}
//						label1: for (int l5 = l1; l5 < l1 + 2; l5++) 
//						{
//							int i7 = l5 % 2;
//							int j8 = 1 - i7;
//							for (int k9 = 0; k9 < 4; k9++) {
//								for (int l10 = -1; l10 < 4; l10++)
//								{
//									int i12 = j2 + (k9 - 1) * i7;
//									int k12 = l4 + l10;
//									int i13 = k3 + (k9 - 1) * j8;
//									if (l10 < 0 && !world.getBlockMaterial(i12, k12, i13).isSolid() || l10 >= 0 && !world.isAirBlock(i12, k12, i13))
//									{
//										break label1;
//									}
//								}
//							}
//
//							double d6 = (l4 + 0.5D) - entity.posY;
//							double d8 = d2 * d2 + d6 * d6 + d4 * d4;
//							if (d < 0.0D || d8 < d) 
//							{
//								d = d8;
//								l = j2;
//								i1 = l4;
//								j1 = k3;
//								k1 = l5 % 2;
//							}
//						}
//					}
//				}
//			}
//		}
//		int k2 = k1;
//		int l2 = l;
//		int i3 = i1;
//		int l3 = j1;
//		int i4 = k2 % 2;
//		int j4 = 1 - i4;
//		if (k2 % 4 >= 2) 
//		{
//			i4 = -i4;
//			j4 = -j4;
//		}
//		if (d < 0.0D) 
//		{
//			if (i1 < 70) 
//			{
//				i1 = 70;
//			}
//			if (i1 > 128 - 10) 
//			{
//				i1 = 128 - 10;
//			}
//			i3 = i1;
//			for (int i5 = -1; i5 <= 1; i5++)
//			{
//				for (int i6 = 1; i6 < 3; i6++) 
//				{
//					for (int j7 = -1; j7 < 3; j7++) 
//					{
//						int k8 = l2 + (i6 - 1) * i4 + i5 * j4;
//						int l9 = i3 + j7;
//						int i11 = (l3 + (i6 - 1) * j4) - i5 * i4;
//						boolean flag = j7 < 0;
//						world.setBlockWithNotify(k8, l9, i11, flag ? GCBlocks.landingPad.blockID : 0);
//					}
//				}
//			}
//		}
//		for (int j5 = 0; j5 < 4; j5++) 
//		{
//			world.editingBlocks = true;
//			world.setBlockWithNotify(l2, 			i3, l3, GCBlocks.landingPad.blockID);
//			world.setBlockWithNotify(l2 + 1, 		i3, l3, GCBlocks.landingPad.blockID);
//			world.setBlockWithNotify(l2 + 2, 		i3, l3, GCBlocks.landingPad.blockID);
//			
//			world.setBlockWithNotify(l2, 			i3, l3 + 1, GCBlocks.landingPad.blockID);
//			world.setBlockWithNotify(l2 + 1, 		i3, l3 + 1, GCBlocks.landingPad.blockID);
//			world.setBlockWithNotify(l2 + 2, 		i3, l3 + 1, GCBlocks.landingPad.blockID);
//			
//			world.setBlockWithNotify(l2, 			i3, l3 + 2, GCBlocks.landingPad.blockID);
//			world.setBlockWithNotify(l2 + 1, 		i3, l3 + 2, GCBlocks.landingPad.blockID);
//			world.setBlockWithNotify(l2 + 2, 		i3, l3 + 2, GCBlocks.landingPad.blockID);
//			world.editingBlocks = false;
//			
//			for (int k6 = 0; k6 < 4; k6++) 
//			{
//				for (int l7 = -1; l7 < 4; l7++) 
//				{
//					int i9 = l2 + (k6 - 1) * i4;
//					int j10 = i3 + l7;
//					int k11 = l3 + (k6 - 1) * j4;
//					world.notifyBlocksOfNeighborChange(i9, j10, k11, world.getBlockId(i9, j10, k11));
//				}
//			}
//		}
//
//		return true;
//	}
}
