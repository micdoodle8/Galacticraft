package gregtechmod.api.util;

import ic2.api.item.IBoxable;
import net.minecraft.item.ItemStack;

public class GT_IBoxableWrapper implements IBoxable {
	@Override
	public boolean canBeStoredInToolbox(ItemStack itemstack) {
		return true;
	}
}