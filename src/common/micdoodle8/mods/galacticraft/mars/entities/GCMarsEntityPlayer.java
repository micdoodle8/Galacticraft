package micdoodle8.mods.galacticraft.mars.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent;

public class GCMarsEntityPlayer
{
	private EntityPlayer currentPlayer;
	
	private int lastStep;
	
	public GCMarsEntityPlayer(EntityPlayer player) 
	{
		this.currentPlayer = player;
		GalacticraftMars.marsPlayers.add(player);
		GalacticraftMars.gcMarsPlayers.add(this);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public EntityPlayer getPlayer()
	{
		return this.currentPlayer;
	}
	
	public static boolean handleBacterialMovement(EntityPlayer player)
	{
		return player.worldObj.isMaterialInBB(player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), GCMarsBlocks.bacterialSludge);
	}
	
	@ForgeSubscribe
	public void onUpdate(LivingEvent event)
	{
		if (event.entityLiving instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;
			
			if (handleBacterialMovement(player) && !player.capabilities.isCreativeMode && !player.isPotionActive(Potion.poison))
			{
				player.addPotionEffect(new PotionEffect(Potion.poison.id, 40, 0));
			}
			
			if (GalacticraftCore.instance.tick % 100 == 0 && player.inventory.armorItemInSlot(2) != null && player.inventory.armorItemInSlot(2).getItem().shiftedIndex == GCMarsItems.jetpack.shiftedIndex)
			{
				player.inventory.armorItemInSlot(2).damageItem(1, player);
			}
		}
	}
	
    public void readEntityFromNBT()
    {
    	NBTTagCompound par1NBTTagCompound = this.currentPlayer.getEntityData();
    }

    public void writeEntityToNBT()
    {
    	NBTTagCompound par1NBTTagCompound = this.currentPlayer.getEntityData();
    }
}
