//package micdoodle8.mods.galacticraft.moon.entities;
//
//import micdoodle8.mods.galacticraft.moon.GalacticraftMoon;
//import micdoodle8.mods.galacticraft.moon.blocks.GCMoonBlocks;
//import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.util.MathHelper;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.event.ForgeSubscribe;
//import net.minecraftforge.event.entity.living.LivingEvent;
//
//public class GCMoonEntityPlayer
//{
//	private final EntityPlayer currentPlayer;
//
//	public GCMoonEntityPlayer(EntityPlayer player)
//	{
//		this.currentPlayer = player;
//		GalacticraftMoon.moonPlayers.add(player);
//		MinecraftForge.EVENT_BUS.register(this);
//	}
//
//	public EntityPlayer getPlayer()
//	{
//		return this.currentPlayer;
//	}
//
//	@ForgeSubscribe
//	public void onUpdate(LivingEvent event)
//	{
//		if (event.entityLiving instanceof EntityPlayer)
//		{
//			final EntityPlayer player = (EntityPlayer) event.entityLiving;
//
//
//		}
//	}
//
//    public void readEntityFromNBT()
//    {
//    	final NBTTagCompound par1NBTTagCompound = this.currentPlayer.getEntityData();
//    }
//
//    public void writeEntityToNBT()
//    {
//    	final NBTTagCompound par1NBTTagCompound = this.currentPlayer.getEntityData();
//    }
//}
