package micdoodle8.mods.galacticraft.core.client;

import java.util.ArrayList;
import java.util.Random;

import micdoodle8.mods.galacticraft.API.ISchematicPage;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GCLog;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.util.Session;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCorePlayerSP extends EntityClientPlayerMP
{
	private final Random rand = new Random();

	private boolean usingParachute;
	private boolean lastUsingParachute;
	private boolean showTutorialText = true;
	public boolean usingAdvancedGoggles;
	private int thirdPersonView = 0;
	public long tick;
	public boolean oxygenSetupValid = true;
	AxisAlignedBB boundingBoxBefore;

	public ArrayList<ISchematicPage> unlockedSchematics = new ArrayList<ISchematicPage>();

    public GCCorePlayerSP(Minecraft par1Minecraft, World par2World, Session par3Session, NetClientHandler par4NetClientHandler)
    {
		super(par1Minecraft, par2World, par3Session, par4NetClientHandler);
		
		if (!GalacticraftCore.playersClient.containsKey(this.username))
		{
			GalacticraftCore.playersClient.put(this.username, this);
		}
	}

	@Override
    public void updateCloak()
    {
    	super.updateCloak();

    	if (GCCoreConfigManager.overrideCapes)
    	{
            this.cloakUrl = "http://micdoodle8.com/oldsite/galacticraft/capes/" + StringUtils.stripControlCodes(this.username) + ".png";
    	}
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
		if (this.boundingBox != null && this.boundingBoxBefore == null/* && this.worldObj.provider instanceof GCCoreWorldProviderInnerSpace*/)
		{
			this.boundingBoxBefore = this.boundingBox;
			this.boundingBox.setBounds(this.boundingBoxBefore.minX + 0.4, this.boundingBoxBefore.minY + 0.9, this.boundingBoxBefore.minZ + 0.4, this.boundingBoxBefore.maxX - 0.4, this.boundingBoxBefore.maxY - 0.9, this.boundingBoxBefore.maxZ - 0.4);
		}
		else if (this.boundingBox != null && this.boundingBoxBefore != null)
		{
			this.boundingBox.setBB(this.boundingBoxBefore);
		}

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

		if (this.getParachute() && this.onGround)
		{
			this.setParachute(false);
			FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = this.getThirdPersonView();
		}

        if (!this.lastUsingParachute && this.usingParachute)
        {
            FMLClientHandler.instance().getClient().sndManager.playSound("galacticraft.player.parachute", (float)this.posX, (float)this.posY, (float)this.posZ, 0.95F + this.rand.nextFloat() * 0.1F, 1.0F);
        }

		this.lastUsingParachute = this.usingParachute;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public Icon getItemIcon(ItemStack par1ItemStack, int par2)
    {
        Icon icon = super.getItemIcon(par1ItemStack, par2);

        if (par1ItemStack.itemID == Item.fishingRod.itemID && this.fishEntity != null)
        {
            icon = Item.fishingRod.func_94597_g();
        }
        else
        {
            if (par1ItemStack.getItem().requiresMultipleRenderPasses())
            {
                return par1ItemStack.getItem().getIcon(par1ItemStack, par2);
            }

            if (this.getItemInUse() != null && par1ItemStack.itemID == GCCoreItems.gravityBow.itemID)
            {
                final int j = par1ItemStack.getMaxItemUseDuration() - this.getItemInUseCount();

                if (j >= 18)
                {
                    return Item.bow.getItemIconForUseDuration(2);
                }

                if (j > 13)
                {
                    return Item.bow.getItemIconForUseDuration(1);
                }

                if (j > 0)
                {
                    return Item.bow.getItemIconForUseDuration(0);
                }
            }
            else
            {
            	return super.getItemIcon(par1ItemStack, par2);
            }

            icon = par1ItemStack.getItem().getIcon(par1ItemStack, par2, this, this.getItemInUse(), this.getItemInUseCount());
        }

        return icon;
    }

	@Override
	public void onUpdate()
	{
		this.tick++;

		if (!GalacticraftCore.playersClient.containsKey(this.username) || this.tick % 360 == 0)
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
