package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class FreefallHandler {

	private static double pPrevMotionX;
	private static double pPrevMotionY;
	private static double pPrevMotionZ;
	private static float jetpackBoost;
	private static double pPrevdY;
	private static boolean sneakLast;

	public static boolean testFreefall(EntityPlayer player)
	{
        //Test whether feet are on a block, also stops the login glitch
        int playerFeetOnY = (int) (player.boundingBox.minY - 0.001D);
        int xx = MathHelper.floor_double(player.posX);
        int zz = MathHelper.floor_double(player.posZ);
        Block b = player.worldObj.getBlock(xx, playerFeetOnY, zz);
        if (b.getMaterial() != Material.air && !(b instanceof BlockLiquid))
        {
        	double blockYmax = playerFeetOnY + b.getBlockBoundsMaxY();
            if (player.boundingBox.minY - blockYmax < 0.001D && player.boundingBox.minY - blockYmax > -0.5D)
            {
                player.onGround = true;
                if (b.canCollideCheck(player.worldObj.getBlockMetadata(xx, playerFeetOnY, zz), false))
                {
                    AxisAlignedBB collisionBox = b.getCollisionBoundingBoxFromPool(player.worldObj, xx, playerFeetOnY, zz);
                    if (collisionBox != null && collisionBox.intersectsWith(player.boundingBox))
                    {
                        player.posY -= player.boundingBox.minY - blockYmax;
	                    player.boundingBox.offset(0, blockYmax - player.boundingBox.minY, 0);
                    }
                }
                return false;
            }
        }
        return true;
	}

	public static void setupFreefallPre(EntityPlayerSP p)
	{
        double dY = p.motionY - pPrevMotionY;
        jetpackBoost = 0F;
        pPrevdY = dY;
		pPrevMotionX = p.motionX;
        pPrevMotionY = p.motionY;
        pPrevMotionZ = p.motionZ;
	}
	
	public static void freefallMotion(EntityPlayerSP p)
	{
        boolean jetpackUsed = false;
		double dX = p.motionX - pPrevMotionX;
        double dY = p.motionY - pPrevMotionY;
        double dZ = p.motionZ - pPrevMotionZ;

        double posOffsetX = - p.motionX;
        double posOffsetY = - p.motionY;// + WorldUtil.getGravityForEntity(p);
        double posOffsetZ = - p.motionZ;
        //if (p.capabilities.isFlying)

        ///Undo whatever vanilla tried to do to our y motion
        if (dY < 0D)
        {
        	p.motionY = pPrevMotionY;
        }
        else if (dY > 0.01D && GCPlayerStatsClient.get(p).inFreefallLast)
        {
    		//Impulse upwards - it's probably a jetpack from another mod
        	if (dX < 0.01D && dZ < 0.01D)
        	{
        		float pitch = p.rotationPitch / 57.29578F;
       			jetpackBoost = (float) dY * MathHelper.cos(pitch) * 0.1F;
        		float factor = 1 + MathHelper.sin(pitch) / 5;
        		p.motionY -= dY * factor;
        		jetpackUsed = true;
        	}
        	else
        	{
            	p.motionY -= dY / 2;
        	}
        }

        p.motionX -= dX;
//        p.motionY -= dY;    //Enabling this will disable jetpacks
        p.motionZ -= dZ;

        if (p.movementInput.moveForward != 0)
        {
            p.motionX -= p.movementInput.moveForward * MathHelper.sin(p.rotationYaw / 57.29578F) / (ConfigManagerCore.hardMode ? 600F : 200F);
            p.motionZ += p.movementInput.moveForward * MathHelper.cos(p.rotationYaw / 57.29578F) / (ConfigManagerCore.hardMode ? 600F : 200F);
        }

        if (jetpackBoost != 0)
        {
            p.motionX -= jetpackBoost * MathHelper.sin(p.rotationYaw / 57.29578F);
            p.motionZ += jetpackBoost * MathHelper.cos(p.rotationYaw / 57.29578F);
        }

        if (p.movementInput.sneak)
        {
            if (!sneakLast)
            {
            	posOffsetY += 0.0268;
            	sneakLast = true;
            }
            p.motionY -= ConfigManagerCore.hardMode ? 0.002D : 0.0032D;
        } else if (sneakLast)
        {
        	sneakLast = false;
        	posOffsetY -= 0.0268;
        }

        if (!jetpackUsed && p.movementInput.jump)
        {
            p.motionY += ConfigManagerCore.hardMode ? 0.002D : 0.0032D;
        }
        
        float speedLimit = ConfigManagerCore.hardMode ? 0.9F : 0.7F;

        if (p.motionX > speedLimit)
        {
            p.motionX = speedLimit;
        }
        if (p.motionX < -speedLimit)
        {
            p.motionX = -speedLimit;
        }
        if (p.motionY > speedLimit)
        {
            p.motionY = speedLimit;
        }
        if (p.motionY < -speedLimit)
        {
            p.motionY = -speedLimit;
        }
        if (p.motionZ > speedLimit)
        {
            p.motionZ = speedLimit;
        }
        if (p.motionZ < -speedLimit)
        {
            p.motionZ = -speedLimit;
        }
        pPrevMotionX = p.motionX;
        pPrevMotionY = p.motionY;
        pPrevMotionZ = p.motionZ;
        p.moveEntity(p.motionX + posOffsetX, p.motionY + posOffsetY, p.motionZ + posOffsetZ);
	}

	/*				double dyaw = p.rotationYaw - p.prevRotationYaw;
	p.rotationYaw -= dyaw * 0.8D;
	double dyawh = p.rotationYawHead - p.prevRotationYawHead;
	p.rotationYawHead -= dyawh * 0.8D;
	while (p.rotationYaw > 360F)
	{
		p.rotationYaw -= 360F;
	}
	while (p.rotationYaw < 0F)
	{
		p.rotationYaw += 360F;
	}
	while (p.rotationYawHead > 360F)
	{
		p.rotationYawHead -= 360F;
	}
	while (p.rotationYawHead < 0F)
	{
		p.rotationYawHead += 360F;
	}
*/


	public static void updateFreefall(EntityPlayer p)
	{
	    pPrevMotionX = p.motionX;
	    pPrevMotionY = p.motionY;
	    pPrevMotionZ = p.motionZ;
	}
}
