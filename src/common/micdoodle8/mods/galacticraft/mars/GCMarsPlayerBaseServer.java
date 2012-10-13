package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraft.src.ServerPlayerBase;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMarsPlayerBaseServer extends ServerPlayerBase
{
	public static GCMarsPlayerBaseServer instance;
	
	public GCMarsPlayerBaseServer(ServerPlayerAPI var1) 
	{
		super(var1);
		this.instance = this;
		GalacticraftMars.instance.serverPlayerBaseList.add(player);
		GalacticraftMars.instance.serverPlayerAPIs.add(this);
	}
	
	public EntityPlayerMP getPlayer()
	{
		return this.player;
	}
    
    public static boolean handleBacterialMovement(EntityPlayer player)
    {
        return player.worldObj.isMaterialInBB(player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), GCMarsBlocks.bacterialSludge);
    }

	@Override
    public void onUpdate()
	{
		if (handleBacterialMovement(player) && !player.capabilities.isCreativeMode && !player.isPotionActive(Potion.poison))
		{
			player.addPotionEffect(new PotionEffect(Potion.poison.id, 40, 0));
		}
		
		if (GalacticraftCore.instance.tick % 100 == 0 && player.inventory.armorItemInSlot(2) != null && player.inventory.armorItemInSlot(2).getItem().shiftedIndex == GCMarsItems.jetpack.shiftedIndex)
		{
			player.inventory.armorItemInSlot(2).damageItem(1, player);
		}

		super.onUpdate();
	}
}
