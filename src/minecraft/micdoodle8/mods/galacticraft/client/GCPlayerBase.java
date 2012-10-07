package micdoodle8.mods.galacticraft.client;

import java.util.Random;

import micdoodle8.mods.galacticraft.GCBlocks;
import micdoodle8.mods.galacticraft.GCEntitySpaceship;
import net.minecraft.src.MathHelper;
import net.minecraft.src.PlayerAPI;
import net.minecraft.src.PlayerBase;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCPlayerBase extends PlayerBase
{
	private Random rand = new Random();
	
	private int heartbeatCooldown;
	
	public GCPlayerBase(PlayerAPI var1) 
	{
		super(var1);
		this.heartbeatCooldown = this.rand.nextInt(2000);
	}
	
	@Override
	public void onLivingUpdate()
    {
		super.onLivingUpdate();
		
		if (FMLClientHandler.instance().getClient().currentScreen instanceof GCGuiChoosePlanet)
		{
			this.player.motionY = 0;
		}
		
		if (isNearCreeperDungeon())
		{
			if (this.heartbeatCooldown > 0)
			{
				this.heartbeatCooldown--;
			}
			
			if (this.heartbeatCooldown == 0)
			{
				FMLClientHandler.instance().getClient().sndManager.playSoundFX("creepernest.heartbeat", 1F, 1F);
				this.heartbeatCooldown = 1000 + this.rand.nextInt(2000);
			}
		}
    }
	
	private boolean isNearCreeperDungeon()
	{
		for (int i = MathHelper.floor_double(this.player.posX) - 5; i < this.player.posX + 6; i++)
		{
			for (int j = MathHelper.floor_double(this.player.posY) - 5; j < this.player.posY + 6; j++)
			{
				for (int k = MathHelper.floor_double(this.player.posZ) - 5; k < this.player.posZ + 6; k++)
				{
					if (this.player.worldObj.getBlockId(i, j, k) == GCBlocks.creeperDungeonWall.blockID)
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}

	public void moveEntityWithHeading(float par1, float par2)
    {
		if (ClientProxy.handleBacterialMovement(player))
		{
			if (player.capabilities.isFlying)
			{
	            double var9 = player.motionY;
	            float var11 = player.jumpMovementFactor;
	            player.jumpMovementFactor = player.capabilities.getFlySpeed();
	            this.moveEntityWithHeading2(par1, par2);
	            player.motionY = var9 * 0.6D;
	            player.jumpMovementFactor = var11;
			}
			else
			{
	            this.moveEntityWithHeading2(par1, par2);
			}
		}
		else
		{
			super.moveEntityWithHeading(par1, par2);
		}
    }
	
	public void moveEntityWithHeading2(float par1, float par2)
	{
        double var9 = player.posY;
        player.moveFlying(par1, par2, 0.02F);
        player.moveEntity(player.motionX, player.motionY, player.motionZ);
        player.motionX *= 0.5D;
        player.motionY *= 0.5D;
        player.motionZ *= 0.5D;
        player.motionY -= 0.02D;
        
        if (FMLClientHandler.instance().getClient().gameSettings.keyBindJump.pressed)
        {
            player.motionY += 0.035D;
        }

        if (player.isCollidedHorizontally && player.isOffsetPositionInLiquid(player.motionX, player.motionY + 0.6000000238418579D - player.posY + var9, player.motionZ))
        {
        	player.motionY = 0.30000001192092896D;
        }
	}
	
	public void jump()
    {
		if (!ClientProxy.handleBacterialMovement(player))
		{
			super.jump();
		}
    }
}
