package micdoodle8.mods.galacticraft.core.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;

public interface ISchematicPage 
{
	public int getPageID();
	
	public String getTitle();
	
	public ItemStack getRequiredItem();
	
	public GuiScreen getResultScreen();
}
