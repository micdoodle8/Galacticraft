package gregtechmod.api.interfaces;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IItemContainer {
	public Item getItem();
	public Block getBlock();
	public boolean isStackEqual(Object aStack);
	public boolean isStackEqual(Object aStack, boolean aWildcard, boolean aIgnoreNBT);
	public ItemStack get(long aAmount, Object... aReplacements);
	public ItemStack getWildcard(long aAmount, Object... aReplacements);
	public ItemStack getUndamaged(long aAmount, Object... aReplacements);
	public ItemStack getAlmostBroken(long aAmount, Object... aReplacements);
	public ItemStack getWithDamage(long aAmount, long aMetaValue, Object... aReplacements);
	public IItemContainer registerOre(Object... aOreNames);
	public IItemContainer registerWildcardAsOre(Object... aOreNames);
}
