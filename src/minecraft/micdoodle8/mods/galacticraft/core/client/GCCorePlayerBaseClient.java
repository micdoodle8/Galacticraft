package micdoodle8.mods.galacticraft.core.client;

import java.util.Random;

import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.src.PlayerAPI;
import net.minecraft.src.PlayerBase;
import cpw.mods.fml.client.FMLClientHandler;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCorePlayerBaseClient extends PlayerBase
{
	private final Random rand = new Random();
	
	private boolean usingParachute;
	
	public boolean usingAdvancedGoggles;
	
	public GCCorePlayerBaseClient(PlayerAPI var1)
	{
		super(var1);
		GalacticraftCore.playersClient.add(this);
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
		
        for (final String name : ClientProxyCore.playersUsingParachutes)
        {
			if (this.player.username.equals(name))
			{
				this.usingParachute = true;
				changed = true;
			}
        }
        
        if (!changed)
        {
        	this.usingParachute = false;
        }

		if (!this.getParachute() && this.player.worldObj.provider instanceof IGalacticraftWorldProvider && !this.player.capabilities.isFlying && !FMLClientHandler.instance().getClient().isGamePaused && !this.player.handleWaterMovement())
		{
			final IGalacticraftWorldProvider wp = (IGalacticraftWorldProvider) this.player.worldObj.provider;
			this.player.motionY = this.player.motionY + wp.getGravity();
		}
		
		if (this.getParachute() && this.player.onGround)
		{
			this.setParachute(false);
			FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = 0;
		}
	}
	
	@Override
	public void onUpdate()
	{
		if (this.player != null && this.getParachute() && !this.player.capabilities.isFlying && !FMLClientHandler.instance().getClient().isGamePaused && !this.player.handleWaterMovement())
		{
			this.player.motionY = -0.5D;
			this.player.motionX *= 0.5F;
			this.player.motionZ *= 0.5F;
		}
		
		super.onUpdate();
	}
	
	public void setUsingGoggles(boolean b)
	{
		this.usingAdvancedGoggles = b;
	}
	
	public boolean getUsingGoggles()
	{
		return this.usingAdvancedGoggles;
	}
	
	public void toggleGoggles()
	{
		if (this.usingAdvancedGoggles)
		{
			this.usingAdvancedGoggles = false;
		}
		else
		{
			this.usingAdvancedGoggles = true;
		}
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
        this.setUsingGoggles(par1NBTTagCompound.getBoolean("usingAdvGoggles"));
        
        super.readEntityFromNBT(par1NBTTagCompound);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setBoolean("usingParachute", this.getParachute());
        par1NBTTagCompound.setBoolean("usingAdvGoggles", this.getUsingGoggles());
        
        super.writeEntityToNBT(par1NBTTagCompound);
    }
}
