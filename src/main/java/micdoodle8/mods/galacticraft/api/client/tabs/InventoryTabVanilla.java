package micdoodle8.mods.galacticraft.api.client.tabs;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class InventoryTabVanilla extends AbstractTab
{
	public InventoryTabVanilla()
	{
		super(0, 0, 0, new ItemStack(Blocks.CRAFTING_TABLE));
	}

	@Override
	public void onTabClicked()
	{
		TabRegistry.openInventoryGui();
	}

	@Override
	public boolean shouldAddToList()
	{
		return true;
	}
}
