package micdoodle8.mods.galacticraft.API;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface ISchematicPage extends Comparable<ISchematicPage>
{
	public int getPageID();

	public int getGuiID();

	public ItemStack getRequiredItem();

	@SideOnly(Side.CLIENT)
	public String getTitle();

	@SideOnly(Side.CLIENT)
	public GuiScreen getResultScreen(EntityPlayer player, int x, int y, int z);

	public Container getResultContainer(EntityPlayer player, int x, int y, int z);
}
