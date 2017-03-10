package micdoodle8.mods.galacticraft.api.client.tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

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
		if (event.getGui() instanceof GuiInventory)
		{
			int guiLeft = (event.getGui().width - 176) / 2;
			int guiTop = (event.getGui().height - 166) / 2;
			guiLeft += getPotionOffset();

			TabRegistry.updateTabValues(guiLeft, guiTop, InventoryTabVanilla.class);
			TabRegistry.addTabsToList(event.getButtonList());
		}
	}

	private static Minecraft mc = FMLClientHandler.instance().getClient();
	private static boolean initWithPotion;

	public static void openInventoryGui()
	{
		TabRegistry.mc.thePlayer.connection.sendPacket(new CPacketCloseWindow(mc.thePlayer.openContainer.windowId));
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
				t.potionOffsetLast = getPotionOffsetNEI();
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
			initWithPotion = true;
			return 60 + getPotionOffsetNEI();
		}
		
		// No potions, no offset needed
		initWithPotion = false;
		return 0;
	}

	public static int getPotionOffsetNEI()
	{
		if (initWithPotion && Loader.isModLoaded("NotEnoughItems"))
		{
			try 
			{
				// Check whether NEI is hidden and enabled
				Class<?> c = Class.forName("codechicken.nei.NEIClientConfig");
				Object hidden = c.getMethod("isHidden").invoke(null);
				Object enabled = c.getMethod("isEnabled").invoke(null);
				if (hidden instanceof Boolean && enabled instanceof Boolean)
				{
					if ((Boolean)hidden || !((Boolean)enabled))
					{
						// If NEI is disabled or hidden, offset the tabs by the standard 60 
						return 0;
					}
					//Active NEI undoes the standard potion offset
					return -60;
				}
			} 
			catch (Exception e) 
			{
			}
		}
		//No NEI, no change
		return 0;
	}
}
