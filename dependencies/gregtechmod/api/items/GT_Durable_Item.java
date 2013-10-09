package gregtechmod.api.items;

import net.minecraft.item.ItemStack;

/**
 * This is just a basic Tool, which has normal durability and doesn't break Blocks.
 */
public class GT_Durable_Item extends GT_Generic_Item {
	
	public GT_Durable_Item(int aID, String aName, String aTooltip, int aMaxDamage) {
		super(aID, aName, aTooltip);
		setMaxDamage(aMaxDamage);
		setMaxStackSize(1);
	}
	
	@Override
    public int getItemEnchantability() {
        return 0;
    }
	
	@Override
    public boolean isBookEnchantable(ItemStack aStack, ItemStack aBook) {
        return false;
    }
	
	@Override
    public boolean getIsRepairable(ItemStack aStack, ItemStack aSecondStack) {
        return false;
    }
	
	@Override
    public boolean hasEffect(ItemStack par1ItemStack) {
        return false;
    }
}