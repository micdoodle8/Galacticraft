package tconstruct.client.tabs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraftforge.client.event.GuiScreenEvent;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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

	//Retained for backwards compatibility with TC pre version 1.6.0d40
	public static void addTabsToInventory (GuiContainer gui)
	{
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void guiPostInit (GuiScreenEvent.InitGuiEvent.Post event)
	{
		if (event.gui instanceof GuiInventory)
		{
			int guiLeft = (event.gui.width - 176) / 2;
			int guiTop = (event.gui.height - 166) / 2;
			guiLeft += getPotionOffset();

			TabRegistry.updateTabValues(guiLeft, guiTop, InventoryTabVanilla.class);
			TabRegistry.addTabsToList(event.gui.buttonList);
		}
	}

	private static Minecraft mc = FMLClientHandler.instance().getClient();

	public static void openInventoryGui()
	{
		TabRegistry.mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(mc.thePlayer.openContainer.windowId));
		GuiInventory inventory = new GuiInventory(TabRegistry.mc.thePlayer);
		TabRegistry.mc.displayGuiScreen(inventory);
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

	public static void addTabsToList(List buttonList)
	{
		for (AbstractTab tab : TabRegistry.tabList)
		{
			if (tab.shouldAddToList())
			{
				buttonList.add(tab);
			}
		}
	}
	
	public static int getPotionOffset()
	{
		// If at least one potion is active...
		if (!mc.thePlayer.getActivePotionEffects().isEmpty())
		{
			if (Loader.isModLoaded("NotEnoughItems"))
			{
				try 
				{
					// Check whether NEI is hidden and enabled
					Class<?> c = Class.forName("codechicken.nei.NEIClientConfig");
					Object hidden = c.getMethod("isHidden").invoke(null);
					Object enabled = c.getMethod("isEnabled").invoke(null);
					if (hidden != null && hidden instanceof Boolean && enabled != null && enabled instanceof Boolean)
					{
						if ((Boolean)hidden || !((Boolean)enabled))
						{
							// If NEI is disabled or hidden, offset the tabs by 60 
							return 60;
						}
					}
				} 
				catch (Exception e) 
				{
				}
			}
			else
			{
				// If NEI is not installed, offset the tabs 
				return 60;
			}
		}
		
		// No potions, no offset needed
		return 0;
	}
}
