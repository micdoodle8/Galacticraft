package micdoodle8.mods.galacticraft.core.client;

import java.util.Random;

import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerBase;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.src.PlayerAPI;
import net.minecraft.src.PlayerBase;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCorePlayerBaseClient extends PlayerBase
{
	private Random rand = new Random();
	
	private boolean usingParachute;
	
	public GCCorePlayerBaseClient(PlayerAPI var1) 
	{
		super(var1);
		GalacticraftCore.players.add(this);
	}
	
	public EntityPlayerSP getPlayer()
	{
		return this.player;
	}

//	@Override
//    public void addStat(StatBase par1StatBase, int par2)
//    {
//		if (par1StatBase != null)
//		{
//			if (par1StatBase instanceof AdvancedAchievement)
//			{
//				AdvancedAchievement achiev = (AdvancedAchievement) par1StatBase;
//				
//				int amountOfCompletedAchievements = 0;
//				
//				if (achiev.parentAchievements != null)
//				{
//					for (int i = 0; i < achiev.parentAchievements.length; i++)
//					{
//						if (FMLClientHandler.instance().getClient().statFileWriter.hasAchievementUnlocked(achiev.parentAchievements[i]))
//						{
//							amountOfCompletedAchievements++;
//						}
//					}
//					
//					if (amountOfCompletedAchievements >= achiev.parentAchievements.length)
//					{
//	                    if (!FMLClientHandler.instance().getClient().statFileWriter.hasAchievementUnlocked(achiev))
//	                    {
//							FMLClientHandler.instance().getClient().guiAchievement.queueTakenAchievement(achiev);
//	                    }
//					}
//				}
//				else
//				{
//					super.addStat(par1StatBase, par2);
//				}
//
//				FMLClientHandler.instance().getClient().statFileWriter.readStat(par1StatBase, par2);
//			}
//			else
//			{
//				super.addStat(par1StatBase, par2);
//			}
//		}
//    }
	
	@Override
	public void onLivingUpdate()
    {
		super.onLivingUpdate();

    	boolean changed = false;
		
        for (int j = 0; j < ClientProxyCore.playersUsingParachutes.size(); ++j)
        {
			final String username = (String) ClientProxyCore.playersUsingParachutes.get(j);
			
			if (player.username.equals(username))
			{
				this.usingParachute = true;
				changed = true;
			}
        }
        
        if (!changed)
        {
        	this.usingParachute = false;
        }

		if (!this.getParachute() && player.worldObj.provider instanceof IGalacticraftWorldProvider && !player.capabilities.isFlying && !FMLClientHandler.instance().getClient().isGamePaused && !player.handleWaterMovement()) 
		{
			FMLLog.info("1a");
			final IGalacticraftWorldProvider wp = (IGalacticraftWorldProvider) player.worldObj.provider;
			player.motionY = player.motionY + wp.getGravity();
		}
		
		if (this.getParachute() && player.onGround)
		{
			this.setParachute(false);
			FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = 0;
		}
	}
	
	@Override
	public void onUpdate()
	{
		if (this.getParachute() && !player.capabilities.isFlying && !FMLClientHandler.instance().getClient().isGamePaused && !player.handleWaterMovement())
		{
			FMLLog.info("2a");
			player.motionY = player.motionY + 0.07F;
		}
		
		super.onUpdate();
	}
    
    public void setParachute(boolean tf)
    {
    	this.usingParachute = tf;
    }
    
    public boolean getParachute()
    {
    	return this.usingParachute;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        this.setParachute(par1NBTTagCompound.getBoolean("usingParachute"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setBoolean("usingParachute", this.getParachute());
    }
}
