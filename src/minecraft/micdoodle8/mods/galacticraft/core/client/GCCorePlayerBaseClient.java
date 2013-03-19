package micdoodle8.mods.galacticraft.core.client;

import java.util.Random;

import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.API.ISpaceship;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Session;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCorePlayerBaseClient extends EntityClientPlayerMP
{
	private final Random rand = new Random();

	private boolean usingParachute;
	private boolean lastUsingParachute;
	private boolean showTutorialText = true;
	public boolean usingAdvancedGoggles;
	private int thirdPersonView = 0;
	public int spaceStationDimensionIDClient = 0;

    public GCCorePlayerBaseClient(Minecraft par1Minecraft, World par2World, Session par3Session, NetClientHandler par4NetClientHandler)
    {
		super(par1Minecraft, par2World, par3Session, par4NetClientHandler);
	}

	@Override
    public void updateCloak()
    {
    	super.updateCloak();

        this.cloakUrl = "http://www.micdoodle8.com/galacticraft/capes/" + StringUtils.stripControlCodes(this.username) + ".png";
    }

	@Override
    public void onDeath(DamageSource var1)
    {
		GalacticraftCore.playersClient.remove(this);

    	super.onDeath(var1);
    }

	@Override
	public void onLivingUpdate()
    {
		super.onLivingUpdate();

    	boolean changed = false;

    	if (this.getParachute())
    	{
    		this.fallDistance = 0.0F;
    	}

        for (final String name : ClientProxyCore.playersUsingParachutes)
        {
			if (this.username.equals(name))
			{
				this.usingParachute = true;
				changed = true;
			}
        }

        if (!changed)
        {
        	this.usingParachute = false;
        }

		if (!this.getParachute() && this.worldObj.provider instanceof IGalacticraftWorldProvider && !this.capabilities.isFlying && !FMLClientHandler.instance().getClient().isGamePaused && !this.handleWaterMovement())
		{
			final IGalacticraftWorldProvider wp = (IGalacticraftWorldProvider) this.worldObj.provider;
			this.motionY = this.motionY + wp.getGravity();
		}

		if (this.getParachute() && this.onGround)
		{
			this.setParachute(false);
			FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = this.getThirdPersonView();
		}

        if (!this.lastUsingParachute && this.usingParachute)
        {
            FMLClientHandler.instance().getClient().sndManager.playSound("player.parachute", (float)this.posX, (float)this.posY, (float)this.posZ, 0.95F + this.rand.nextFloat() * 0.1F, 1.0F);
        }

		this.lastUsingParachute = this.usingParachute;
	}

	@Override
	public void onUpdate()
	{
		if (!GalacticraftCore.playersClient.containsKey(this.username) || GalacticraftCore.slowTick % 360 == 0)
		{
			GalacticraftCore.playersClient.put(this.username, this);
		}

		if (this != null && this.getParachute() && !this.capabilities.isFlying && !this.handleWaterMovement())
		{
			this.motionY = -0.5D;
			this.motionX *= 0.5F;
			this.motionZ *= 0.5F;
		}

		super.onUpdate();
	}

	public void setUseTutorialText(boolean b)
	{
		this.showTutorialText = b;
	}

	public boolean getUseTutorialText()
	{
		return this.showTutorialText && !GCCoreConfigManager.disableTutorialItemText;
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

    	if (!tf)
    	{
    		this.lastUsingParachute = false;
    	}
    }

    public boolean getParachute()
    {
    	return this.usingParachute;
    }

    public void setThirdPersonView(int view)
    {
    	this.thirdPersonView = view;
    }

    public int getThirdPersonView()
    {
    	return this.thirdPersonView;
    }
}
