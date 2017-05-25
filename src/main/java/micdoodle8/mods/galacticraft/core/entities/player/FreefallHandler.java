 package micdoodle8.mods.galacticraft.core.entities.player;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.event.ZeroGravityEvent;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.dimension.SpinManager;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.MinecraftForge;

public class FreefallHandler {

	private static double pPrevMotionX;
	public static double pPrevMotionY;
	private static double pPrevMotionZ;
	private static float jetpackBoost;
	private static double pPrevdY;
	public static boolean sneakLast;

	private GCPlayerStatsClient stats;
	
	public FreefallHandler(GCPlayerStatsClient statsClient)
	{
		this.stats = statsClient;
	}
	
	public static boolean testFreefall(EntityPlayer player)
	{
        ZeroGravityEvent zeroGEvent = new ZeroGravityEvent.InFreefall(player);
        MinecraftForge.EVENT_BUS.post(zeroGEvent);
        if (zeroGEvent.isCanceled())
        {
            return false;
        }
            
        //Test whether feet are on a block, also stops the login glitch
        int playerFeetOnY = (int) (player.boundingBox.minY - 0.01D);
        int xx = MathHelper.floor_double(player.posX);
        int zz = MathHelper.floor_double(player.posZ);
        Block b = player.worldObj.getBlock(xx, playerFeetOnY, zz);
        if (b.getMaterial() != Material.air && !(b instanceof BlockLiquid))
        {
        	double blockYmax = playerFeetOnY + b.getBlockBoundsMaxY();
            if (player.boundingBox.minY - blockYmax < 0.01D && player.boundingBox.minY - blockYmax > -0.5D)
            {
                player.onGround = true;
                if (player.boundingBox.minY - blockYmax > 0D)
                {
                    player.posY -= player.boundingBox.minY - blockYmax;
                    player.boundingBox.offset(0, blockYmax - player.boundingBox.minY, 0);              	
                }
                else if (b.canCollideCheck(player.worldObj.getBlockMetadata(xx, playerFeetOnY, zz), false))
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

    @SideOnly(Side.CLIENT)
    private boolean testFreefall(EntityPlayerSP p, boolean flag)
    {
    	World world = p.worldObj;
    	WorldProvider worldProvider = world.provider;
    	if (!(worldProvider instanceof IZeroGDimension))
    	{
    		return false;
    	}
        ZeroGravityEvent zeroGEvent = new ZeroGravityEvent.InFreefall(p);
        MinecraftForge.EVENT_BUS.post(zeroGEvent);
        if (zeroGEvent.isCanceled())
        {
            return false;
        }

    	if (stats.pjumpticks > 0 || (stats.pWasOnGround && p.movementInput.jump))
        {
            return false;
        }
        
        if (p.ridingEntity != null)
        {
        	Entity e = p.ridingEntity;
        	if (e instanceof EntitySpaceshipBase)
        		return ((EntitySpaceshipBase)e).getLaunched();
        	if (e instanceof EntityLanderBase)
        		return false;
        	//TODO: should check whether lander has landed (whatever that means)
        	//TODO: could check other ridden entities - every entity should have its own freefall check :(
        }

        //This is an "on the ground" check
        if (!flag)
        {
            return false;
        }
        else
        {
    		float rY = p.rotationYaw % 360F;
            double zreach = 0D;
            double xreach = 0D;
            if (rY < 80F || rY > 280F) zreach = 0.2D;
            if (rY < 170F && rY > 10F) xreach = 0.2D;
            if (rY < 260F && rY > 100F) zreach = -0.2D;
            if (rY < 350F && rY > 190F) xreach = -0.2D;
            AxisAlignedBB playerReach = p.boundingBox.addCoord(xreach, 0, zreach);            

            boolean checkBlockWithinReach;
            if (worldProvider instanceof WorldProviderSpaceStation)
            {
            	SpinManager spinManager = ((WorldProviderSpaceStation) worldProvider).getSpinManager();
            	checkBlockWithinReach = playerReach.maxX >= spinManager.ssBoundsMinX && playerReach.minX <= spinManager.ssBoundsMaxX && playerReach.maxY >= spinManager.ssBoundsMinY && playerReach.minY <= spinManager.ssBoundsMaxY && playerReach.maxZ >= spinManager.ssBoundsMinZ && playerReach.minZ <= spinManager.ssBoundsMaxZ;
            	//Player is somewhere within the space station boundaries
            }
            else
            {
            	checkBlockWithinReach = true;
            }

            if (checkBlockWithinReach)
          	//Player is somewhere within the space station boundaries
            {
            	//Check if the player's bounding box is in the same block coordinates as any non-vacuum block (including torches etc)
            	//If so, it's assumed the player has something close enough to grab onto, so is not in freefall
	            //Note: breatheable air here means the player is definitely not in freefall
	        	int xm = MathHelper.floor_double(playerReach.minX);
	        	int xx = MathHelper.floor_double(playerReach.maxX);
	            int ym = MathHelper.floor_double(playerReach.minY);
	            int yy = MathHelper.floor_double(playerReach.maxY);
	            int zm = MathHelper.floor_double(playerReach.minZ);
	            int zz = MathHelper.floor_double(playerReach.maxZ);
	            for (int x = xm; x <= xx; x++)
	            {
	                for (int y = ym; y <= yy; y++)
	                {
	                    for (int z = zm; z <= zz; z++)
	                    {
	                        //Blocks.air is hard vacuum - we want to check for that, here
	                    	Block b = world.getBlock(x, y, z);
	                        if (Blocks.air != b && GCBlocks.brightAir != b)
	                        {
	                            return false;
	                        }
	                    }
	                }
	            }
	        }
        }

		/*
		if (freefall)
		{
			//If that check didn't produce a result, see if the player is inside the walls
			//TODO: could apply special weightless movement here like Coriolis force - the player is inside the walls,  not touching them, and in a vacuum
			int quadrant = 0;
			double xd = p.posX - this.spinCentreX;
			double zd = p.posZ - this.spinCentreZ;
			if (xd<0)
			{
				if (xd<-Math.abs(zd))
				{
					quadrant = 2;
				} else
					quadrant = (zd<0) ? 3 : 1;
			} else
				if (xd>Math.abs(zd))
				{
					quadrant = 0;
				} else
					quadrant = (zd<0) ? 3 : 1;
			
			int ymin = MathHelper.floor_double(p.boundingBox.minY)-1;
			int ymax = MathHelper.floor_double(p.boundingBox.maxY);
			int xmin, xmax, zmin, zmax;

			switch (quadrant)
			{
			case 0:
				xmin = MathHelper.floor_double(p.boundingBox.maxX);
				xmax = this.ssBoundsMaxX - 1;
				zmin = MathHelper.floor_double(p.boundingBox.minZ)-1;
				zmax = MathHelper.floor_double(p.boundingBox.maxZ)+1;
				break;
			case 1:
				xmin = MathHelper.floor_double(p.boundingBox.minX)-1;
				xmax = MathHelper.floor_double(p.boundingBox.maxX)+1;
				zmin = MathHelper.floor_double(p.boundingBox.maxZ);
				zmax = this.ssBoundsMaxZ - 1;
				break;
			case 2:
				zmin = MathHelper.floor_double(p.boundingBox.minZ)-1;
				zmax = MathHelper.floor_double(p.boundingBox.maxZ)+1;
				xmin = this.ssBoundsMinX;
				xmax = MathHelper.floor_double(p.boundingBox.minX);
				break;
			case 3:
			default:
				xmin = MathHelper.floor_double(p.boundingBox.minX)-1;
				xmax = MathHelper.floor_double(p.boundingBox.maxX)+1;
				zmin = this.ssBoundsMinZ;
				zmax = MathHelper.floor_double(p.boundingBox.minZ);
				break;
			}
			
			//This block search could cost a lot of CPU (but client side) - maybe optimise later
			BLOCKCHECK0:
			for(int x = xmin; x <= xmax; x++)
				for (int z = zmin; z <= zmax; z++)
					for (int y = ymin; y <= ymax; y++)
						if (Blocks.air != this.worldObj.getBlock(x, y, z))
						{
							freefall = false;
							break BLOCKCHECK0;
						}
		}*/

        return true;
    }

    @SideOnly(Side.CLIENT)
    public static void setupFreefallPre(EntityPlayerSP p)
	{
        double dY = p.motionY - pPrevMotionY;
        jetpackBoost = 0F;
        pPrevdY = dY;
		pPrevMotionX = p.motionX;
        pPrevMotionY = p.motionY;
        pPrevMotionZ = p.motionZ;
	}
	
    @SideOnly(Side.CLIENT)
	public static void freefallMotion(EntityPlayerSP p)
	{
        boolean jetpackUsed = false;
		double dX = p.motionX - pPrevMotionX;
        double dY = p.motionY - pPrevMotionY;
        double dZ = p.motionZ - pPrevMotionZ;

        double posOffsetX = - p.motionX;
        double posOffsetY = - p.motionY;
        if (posOffsetY == - WorldUtil.getGravityForEntity(p)) posOffsetY = 0;
        double posOffsetZ = - p.motionZ;
        //if (p.capabilities.isFlying)
        
        ///Undo whatever vanilla tried to do to our y motion
        if (dY < 0D && p.motionY != 0.0D)
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
//            	posOffsetY += 0.0268;
            	sneakLast = true;
            }
            p.motionY -= ConfigManagerCore.hardMode ? 0.002D : 0.0032D;
        } else if (sneakLast)
        {
        	sneakLast = false;
//        	posOffsetY -= 0.0268;
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
	
    @SideOnly(Side.CLIENT)
    public void preVanillaMotion(EntityPlayerSP p)
    {
        FreefallHandler.setupFreefallPre(p);
        stats.pWasOnGround = p.onGround;
    }
    
    @SideOnly(Side.CLIENT)
    public void postVanillaMotion(EntityPlayerSP p)
    {
    	World world = p.worldObj;
    	WorldProvider worldProvider = world.provider;
    	if (!(worldProvider instanceof IZeroGDimension))
    	{
    		return;
    	}
        ZeroGravityEvent zeroGEvent = new ZeroGravityEvent.Motion(p);
        MinecraftForge.EVENT_BUS.post(zeroGEvent);
        if (zeroGEvent.isCanceled())
        {
            return;
        }

        boolean freefall = stats.inFreefall;
        if (freefall) p.ySize = 0F;  //Undo the sneak height adjust
        freefall = testFreefall(p, freefall);
        stats.inFreefall = freefall;
        stats.inFreefallFirstCheck = true;
        
        SpinManager spinManager = null;
        if (worldProvider instanceof WorldProviderSpaceStation)
        {
            spinManager = ((WorldProviderSpaceStation) worldProvider).getSpinManager();
        }
        boolean doCentrifugal = spinManager != null;

        if (freefall)
        {
            stats.pjumpticks = 0;

            //Reverse effects of deceleration
            p.motionX /= 0.91F;
            p.motionZ /= 0.91F;
            p.motionY /= 0.9800000190734863D;

            if (spinManager != null)
            {
                doCentrifugal = spinManager.updatePlayerForSpin(p, 1F);
            }

            //Do freefall motion
            if (!p.capabilities.isCreativeMode)
            {
            	FreefallHandler.freefallMotion(p);
            }
            else
            {
                p.capabilities.isFlying = true;
                //Half the normal acceleration in Creative mode
                double dx = p.motionX - this.pPrevMotionX;
                double dy = p.motionY - this.pPrevMotionY;
                double dz = p.motionZ - this.pPrevMotionZ;
                p.motionX -= dx / 2;
                p.motionY -= dy / 2;
                p.motionZ -= dz / 2;

                if (p.motionX > 1.2F)
                {
                    p.motionX = 1.2F;
                }
                if (p.motionX < -1.2F)
                {
                    p.motionX = -1.2F;
                }
                if (p.motionY > 0.7F)
                {
                    p.motionY = 0.7F;
                }
                if (p.motionY < -0.7F)
                {
                    p.motionY = -0.7F;
                }
                if (p.motionZ > 1.2F)
                {
                    p.motionZ = 1.2F;
                }
                if (p.motionZ < -1.2F)
                {
                    p.motionZ = -1.2F;
                }
            }
            //TODO: Think about endless drift?
            //Player may run out of oxygen - that will kill the player eventually if can't get back to SS
            //Could auto-kill + respawn the player if floats too far away (config option whether to lose items or not)
            //But we want players to be able to enjoy the view of the spinning space station from the outside
            //Arm and leg movements could start tumbling the player?
        }
        else
        //Not freefall - within arm's length of something or jumping
        {
            double dy = p.motionY - this.pPrevMotionY;
            //if (p.motionY < 0 && this.pPrevMotionY >= 0) p.posY -= p.motionY;
            //if (p.motionY != 0) p.motionY = this.pPrevMotionY;
            if (p.movementInput.jump)
            {
                if ((p.onGround || stats.pWasOnGround) && !p.capabilities.isCreativeMode)
                {
                    if (stats.pjumpticks < 25) stats.pjumpticks++;
                    p.motionY -= dy;
//                    p.onGround = false;
//                    p.posY -= 0.1D;
//                    p.boundingBox.offset(0, -0.1D, 0);
                }
                else
                {
                    p.motionY += 0.015D;
                    if (stats.pjumpticks == 0)
                    {
                        p.motionY -= dy;
                    }
                }
            }
            else if (stats.pjumpticks > 0)
            {
                    p.motionY += 0.0145D * stats.pjumpticks;
                    stats.pjumpticks = 0;
            }
            else if (p.movementInput.sneak)
            {
                if (!p.onGround)
                {
                    p.motionY -= 0.015D;
                }
                stats.pjumpticks = 0;
            }
        }

        //Artificial gravity of a sort...
        if (doCentrifugal && !p.onGround)
        {
            spinManager.applyCentrifugalForce(p);
        }

        this.pPrevMotionX = p.motionX;
        this.pPrevMotionY = p.motionY;
        this.pPrevMotionZ = p.motionZ;
    }
}
