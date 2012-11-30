package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.EnumRarity;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreItemBuggy extends GCCoreItem
{
	public GCCoreItemBuggy(int par1) 
	{
		super(par1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        float var4 = 1.0F;
        float var5 = par3EntityPlayer.prevRotationPitch + (par3EntityPlayer.rotationPitch - par3EntityPlayer.prevRotationPitch) * var4;
        float var6 = par3EntityPlayer.prevRotationYaw + (par3EntityPlayer.rotationYaw - par3EntityPlayer.prevRotationYaw) * var4;
        double var7 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * var4;
        double var9 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * var4 + 1.62D - par3EntityPlayer.yOffset;
        double var11 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * var4;
        Vec3 var13 = par2World.getWorldVec3Pool().getVecFromPool(var7, var9, var11);
        float var14 = MathHelper.cos(-var6 * 0.017453292F - (float)Math.PI);
        float var15 = MathHelper.sin(-var6 * 0.017453292F - (float)Math.PI);
        float var16 = -MathHelper.cos(-var5 * 0.017453292F);
        float var17 = MathHelper.sin(-var5 * 0.017453292F);
        float var18 = var15 * var16;
        float var20 = var14 * var16;
        double var21 = 5.0D;
        Vec3 var23 = var13.addVector(var18 * var21, var17 * var21, var20 * var21);
        MovingObjectPosition var24 = par2World.rayTraceBlocks_do(var13, var23, true);

        if (var24 == null)
        {
            return par1ItemStack;
        }
        else
        {
            Vec3 var25 = par3EntityPlayer.getLook(var4);
            boolean var26 = false;
            float var27 = 1.0F;
            List var28 = par2World.getEntitiesWithinAABBExcludingEntity(par3EntityPlayer, par3EntityPlayer.boundingBox.addCoord(var25.xCoord * var21, var25.yCoord * var21, var25.zCoord * var21).expand(var27, var27, var27));
            int var29;

            for (var29 = 0; var29 < var28.size(); ++var29)
            {
                Entity var30 = (Entity)var28.get(var29);

                if (var30.canBeCollidedWith())
                {
                    float var31 = var30.getCollisionBorderSize();
                    AxisAlignedBB var32 = var30.boundingBox.expand(var31, var31, var31);

                    if (var32.isVecInside(var13))
                    {
                        var26 = true;
                    }
                }
            }

            if (var26)
            {
                return par1ItemStack;
            }
            else
            {
                if (var24.typeOfHit == EnumMovingObjectType.TILE)
                {
                    var29 = var24.blockX;
                    int var33 = var24.blockY;
                    int var34 = var24.blockZ;

                    if (par2World.getBlockId(var29, var33, var34) == Block.snow.blockID)
                    {
                        --var33;
                    }

                    GCCoreEntityBuggy var35 = new GCCoreEntityBuggy(par2World, (var29 + 0.5F), (var33 + 3.0F), (var34 + 0.5F));

                    if (!par2World.getCollidingBoundingBoxes(var35, var35.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                    {
                        return par1ItemStack;
                    }

                    if (!par2World.isRemote)
                    {
                        par2World.spawnEntityInWorld(var35);
                    }

                    if (!par3EntityPlayer.capabilities.isCreativeMode)
                    {
                        --par1ItemStack.stackSize;
                    }
                }

                return par1ItemStack;
            }
        }
    }

    @Override
	@SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return EnumRarity.epic;
    }
}
