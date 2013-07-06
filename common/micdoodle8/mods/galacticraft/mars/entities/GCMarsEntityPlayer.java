//package micdoodle8.mods.galacticraft.mars.entities;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
//import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
//import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.potion.Potion;
//import net.minecraft.potion.PotionEffect;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.event.ForgeSubscribe;
//import net.minecraftforge.event.entity.living.LivingEvent;
//
//public class GCMarsEntityPlayer
//{
//	private final EntityPlayer currentPlayer;
//
//	private int lastStep;
//
//	public GCMarsEntityPlayer(EntityPlayer player)
//	{
//		this.currentPlayer = player;
//		GalacticraftMars.marsPlayers.add(player);
//		GalacticraftMars.gcMarsPlayers.add(this);
//		MinecraftForge.EVENT_BUS.register(this);
//	}
//
//	public EntityPlayer getPlayer()
//	{
//		return this.currentPlayer;
//	}
//
//	public static boolean handleBacterialMovement(EntityPlayer player)
//	{
//		return player.worldObj.isMaterialInBB(player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), GCMarsBlocks.bacterialSludge);
//	}
//
//	@ForgeSubscribe
//	public void onUpdate(LivingEvent event)
//	{
//		if (event.entityLiving instanceof EntityPlayerMP)
//		{
//			final EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;
//
//			if (GCMarsEntityPlayer.handleBacterialMovement(player) && !player.capabilities.isCreativeMode && !player.isPotionActive(Potion.poison))
//			{
//				player.addPotionEffect(new PotionEffect(Potion.poison.id, 40, 0));
//			}
//
//			if (GalacticraftCore.tick % 100 == 0 && player.inventory.armorItemInSlot(2) != null && player.inventory.armorItemInSlot(2).getItem().itemID == GCMarsItems.jetpack.itemID)
//			{
//				player.inventory.armorItemInSlot(2).damageItem(1, player);
//			}TODO
//		}
//	}
//
//    public void readEntityFromNBT()
//    {
//    	this.currentPlayer.getEntityData();
//    }
//
//    public void writeEntityToNBT()
//    {
//    	this.currentPlayer.getEntityData();
//    }
// }
