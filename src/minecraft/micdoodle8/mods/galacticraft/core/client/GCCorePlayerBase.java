//package micdoodle8.mods.galacticraft.core.client;
//
//import java.util.Random;
//
//import micdoodle8.mods.galacticraft.API.AdvancedAchievement;
//import micdoodle8.mods.galacticraft.core.client.GCCoreGuiChoosePlanet;
//import micdoodle8.mods.galacticraft.mars.GCMarsBlocks;
//import net.minecraft.src.MathHelper;
//import net.minecraft.src.PlayerAPI;
//import net.minecraft.src.PlayerBase;
//import net.minecraft.src.StatBase;
//import cpw.mods.fml.client.FMLClientHandler;
//
///**
// * Copyright 2012, micdoodle8
// * 
// *  All rights reserved.
// *
// */
//public class GCCorePlayerBase extends PlayerBase
//{
//	private Random rand = new Random();
//	
//	public GCCorePlayerBase(PlayerAPI var1) 
//	{
//		super(var1);
//	}
//
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
//	
//	@Override
//	public void onLivingUpdate()
//    {
//		super.onLivingUpdate();
//		
//		if (FMLClientHandler.instance().getClient().currentScreen instanceof GCCoreGuiChoosePlanet)
//		{
//			this.player.motionY = 0;
//		}
//    }
//}
