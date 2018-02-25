package micdoodle8.mods.galacticraft.api.client.tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TabRegistry
{
	private static ArrayList<AbstractTab> tabList = new ArrayList<AbstractTab>();
	private static Class<?> clazzJEIConfig = null;
    public static Class<?> clazzNEIConfig = null;
	
	static
	{
	    try 
	    {
	        //Checks for JEI by looking for this class instead of a Loader.isModLoaded() check
	        clazzJEIConfig = Class.forName("mezz.jei.config.Config");
	    }
	    catch (Exception ignore)
	    {
	        //no log spam
	    }
	    //Only activate NEI feature if NEI is standalone
	    if (clazzJEIConfig == null)
	    {
	        try 
	        {
	            clazzNEIConfig = Class.forName("codechicken.nei.NEIClientConfig");
	        }
	        catch (Exception ignore)
	        {
	            //no log spam
	        }
	    }
	}

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
			TabRegistry.addTabsToList(event.buttonList);
		}
	}

	private static Minecraft mc = FMLClientHandler.instance().getClient();
	private static boolean initWithPotion;

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
				t.potionOffsetLast = getPotionOffsetNEI();
				count++;
			}
		}
	}

	public static void addTabsToList(List<GuiButton> buttonList)
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
			return 60 + getPotionOffsetJEI() + getPotionOffsetNEI();
		}
		
		// No potions, no offset needed
		initWithPotion = false;
		return 0;
	}

    public static int getPotionOffsetJEI()
    {
        if (clazzJEIConfig != null)
        {
            try 
            {
                Object enabled = clazzJEIConfig.getMethod("isOverlayEnabled").invoke(null);
                if (enabled instanceof Boolean)
                {
                    if (!((Boolean)enabled))
                    {
                        // If JEI is disabled, no special change to getPotionOffset()
                        return 0;
                    }
                    //Active JEI undoes the standard potion offset (they listen for GuiScreenEvent.PotionShiftEvent)
                    return -60;
                }
            } 
            catch (Exception ignore) 
            {
                //no log spam
            }
        }
        return 0;
    }

	public static int getPotionOffsetNEI()
	{
		if (initWithPotion && clazzNEIConfig != null)
		{
		    try 
		    {
		        // Check whether NEI is hidden and enabled
		        Object hidden = clazzNEIConfig.getMethod("isHidden").invoke(null);
		        Object enabled = clazzNEIConfig.getMethod("isEnabled").invoke(null);
		        if (hidden instanceof Boolean && enabled instanceof Boolean)
		        {
		            if ((Boolean)hidden || !((Boolean)enabled))
		            {
		                // If NEI is disabled or hidden, it does not affect the tabs offset with potions 
		                return 0;
		            }
		            //But active NEI undoes the standard potion offset
		            return -60;
		        }
		    } 
		    catch (Exception ignore) 
		    {
		        //no log spam
		    }
		}
		//No NEI, no change
		return 0;
	}
}
