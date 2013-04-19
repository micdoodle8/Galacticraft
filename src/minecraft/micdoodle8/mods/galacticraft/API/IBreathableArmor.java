package micdoodle8.mods.galacticraft.API;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IBreathableArmor
{
	public boolean handleGearType(EnumGearType gearType);
	
	public boolean canBreathe(ItemStack helmetInSlot, EntityPlayer playerWearing, EnumGearType type);
}
