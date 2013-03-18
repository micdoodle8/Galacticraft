package micdoodle8.mods.galacticraft.core.dimension;

import java.util.Random;

import micdoodle8.mods.galacticraft.API.IInterplanetaryObject;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerBase;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreToOrbitTeleporter extends Teleporter
{
	private final Random random;
    private final WorldServer worldServer;
    private boolean usePlayerBaseCoords;

	public GCCoreToOrbitTeleporter(WorldServer par1WorldServer, boolean usePlayerBaseCoords)
	{
		super(par1WorldServer);
		this.worldServer = par1WorldServer;
		this.random = new Random();
		this.usePlayerBaseCoords = usePlayerBaseCoords;
	}

    @Override
    public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
    {
		if (this.placeInExistingPortal(par1Entity, par2, par4, par6, par8))
		{
			return;
		}
		else
		{
			return;
		}
	}

    @Override
    public boolean makePortal(Entity par1Entity)
    {
    	return false;
    }

    @Override
    public boolean placeInExistingPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
    {
    	if (par1Entity instanceof GCCorePlayerBase)
    	{
    		GCCorePlayerBase playerBase = (GCCorePlayerBase) par1Entity;

            par1Entity.setLocationAndAngles(0.5, 66, 0.5, par1Entity.rotationYaw, 0.0F);
            par1Entity.motionX = par1Entity.motionY = par1Entity.motionZ = 0.0D;

            par1Entity.worldObj.markBlockForUpdate(0, 30, 0);

    		final ItemStack stack = PlayerUtil.getPlayerBaseServerFromPlayer((EntityPlayer) par1Entity).playerTankInventory.getStackInSlot(4);

    		if (stack != null && stack.getItem() instanceof GCCoreItemParachute)
    		{
    			PlayerUtil.getPlayerBaseServerFromPlayer((EntityPlayer) par1Entity).setParachute(true);
    		}
    		else
    		{
    			PlayerUtil.getPlayerBaseServerFromPlayer((EntityPlayer) par1Entity).setParachute(false);
    		}

            if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            {
        		FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = 1;
            }
    	}

		return true;
	}
}
