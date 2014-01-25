package gregtechmod.api.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;

/**
 * Interface used by the Mods Main Class to reference to internals.
 * 
 * Don't even think about including this File in your Mod.
 */
public interface IGT_Mod {
	/**
	 * This means that Server specific Basefiles are definitely existing! Not if the World is actually server side or not!
	 */
	public boolean isServerSide();
	
	/**
	 * This means that Client specific Basefiles are definitely existing! Not if the World is actually client side or not!
	 */
	public boolean isClientSide();
	
	/**
	 * This means that Bukkit specific Basefiles are definitely existing! Not if the World is actually bukkit server or not!
	 */
	public boolean isBukkitSide();
	
	/**
	 * works only ClientSide otherwise returns null
	 * @return Minecraft.thePlayer
	 */
	public EntityPlayer getThePlayer();
	
	//---------- Internal Usage Only ----------
	
	/**
	 * Just checks if said Packet should be sent to the Client. Currently does nothing important.
	 */
    public boolean allowPacketToBeSent(Packet aPacket, EntityPlayerMP aPlayer);
    
	/**
	 * works only ClientSide otherwise returns 0
	 * @return the Index of the added Armor
	 */
	public int addArmor(String aArmorPrefix);
	
	/**
	 * Plays the Sonictron Sound for the ItemStack on the Client Side
	 */
	public void doSonictronSound(ItemStack aStack, World aWorld, double aX, double aY, double aZ);
}