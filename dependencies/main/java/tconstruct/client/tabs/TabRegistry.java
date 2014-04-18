package tconstruct.client.tabs;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import cpw.mods.fml.client.FMLClientHandler;

public class TabRegistry
{
	private static ArrayList<AbstractTab> tabList = new ArrayList<AbstractTab>();

	public static void registerTab(AbstractTab tab)
	{
		TabRegistry.tabList.add(tab);
	}

	public static ArrayList<AbstractTab> getTabList()
	{
		return TabRegistry.tabList;
	}

	public static void addTabsToInventory(GuiContainer gui)
	{
		if (gui.getClass() == GuiInventory.class)
		{
			// Values are public at runtime.

			int cornerX = gui.guiLeft;
			int cornerY = gui.guiTop;
			List bList = gui.buttonList;

			bList.clear();

			TabRegistry.updateTabValues(cornerX, cornerY, InventoryTabVanilla.class);
			TabRegistry.addTabsToList(bList);
		}
	}

	private static Minecraft mc = FMLClientHandler.instance().getClient();

	public static void openInventoryGui()
	{
		GuiInventory inventory = new GuiInventory(TabRegistry.mc.thePlayer);
		TabRegistry.mc.displayGuiScreen(inventory);
		TabRegistry.addTabsToInventory(inventory);
	}

	public static void updateTabValues(int cornerX, int cornerY, Class<?> selectedButton)
	{
		int count = 2;
		for (int i = 0; i < TabRegistry.tabList.size(); i++)
		{
			AbstractTab t = TabRegistry.tabList.get(i);

			if (t.shouldAddToList())
			{
				t.id = count;
				t.xPosition = cornerX + (count - 2) * 28;
				t.yPosition = cornerY - 28;
				t.enabled = !t.getClass().equals(selectedButton);
				count++;
			}
		}
	}

	public static void addTabsToList(List field_146292_n)
	{
		for (AbstractTab tab : TabRegistry.tabList)
		{
			if (tab.shouldAddToList())
			{
				field_146292_n.add(tab);
			}
		}
	}
}
