package tconstruct.client.tabs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import cpw.mods.fml.client.FMLClientHandler;

public class TabRegistry
{
    private static ArrayList<AbstractTab> tabList = new ArrayList<AbstractTab>();

    public static void registerTab (AbstractTab tab)
    {
        tabList.add(tab);
    }

    public static ArrayList<AbstractTab> getTabList ()
    {
        return tabList;
    }

    public static void addTabsToInventory (GuiContainer gui)
    {
        if (gui.getClass() == GuiInventory.class)
        {
            //Values are public at runtime.

            int cornerX = 0;
            int cornerY = 0;
            List bList = null;
            
        	try
        	{
        		Class<?> clazz = Class.forName("net.minecraft.client.gui.inventory.GuiContainer");
        		Class<?> clazz2 = Class.forName("net.minecraft.client.gui.GuiScreen");
        		Field f = clazz.getDeclaredField("guiLeft");
        		f.setAccessible(true);
        		cornerX = f.getInt(gui);
        		f = clazz.getDeclaredField("guiTop");
        		f.setAccessible(true);
        		cornerY = f.getInt(gui);
        		f = clazz2.getField("buttonList");
        		f.setAccessible(true);
        		bList = (List) f.get(gui);
        	}
        	catch (Exception e)
        	{
        		e.printStackTrace();
        	}
        	
        	System.out.println(cornerX + " " + cornerY + " " + bList.size());
        	
        	bList.clear();

            updateTabValues(cornerX, cornerY, InventoryTabVanilla.class);
            addTabsToList(bList);
        	System.out.println(cornerX + " " + cornerY + " " + bList.size());
        }
    }

    private static Minecraft mc = FMLClientHandler.instance().getClient();

    public static void openInventoryGui ()
    {
        GuiInventory inventory = new GuiInventory(mc.thePlayer);
        mc.displayGuiScreen(inventory);
        TabRegistry.addTabsToInventory(inventory);
    }

    public static void updateTabValues (int cornerX, int cornerY, Class<?> selectedButton)
    {
        int count = 2;
        for (int i = 0; i < tabList.size(); i++)
        {
            AbstractTab t = tabList.get(i);

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

    public static void addTabsToList (List field_146292_n)
    {
        for (AbstractTab tab : tabList)
        {
            if (tab.shouldAddToList())
            {
                field_146292_n.add(tab);
            }
        }
    }
}
