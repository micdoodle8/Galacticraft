package micdoodle8.mods.galacticraft.api.client.tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.potion.PotionEffect;
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
	        catch (Throwable ignore)
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
		if (event.getGui() instanceof GuiInventory)
		{
			int guiLeft = (event.getGui().width - 176) / 2;
			int guiTop = (event.getGui().height - 166) / 2;
			recipeBookOffset = getRecipeBookOffset((GuiInventory) event.getGui());
			guiLeft += getPotionOffset() + recipeBookOffset;

			TabRegistry.updateTabValues(guiLeft, guiTop, InventoryTabVanilla.class);
			TabRegistry.addTabsToList(event.getButtonList());
		}
	}

    private static Minecraft mc = FMLClientHandler.instance().getClient();
	private static boolean initWithPotion;
    public static int recipeBookOffset;

	public static void openInventoryGui()
	{
		TabRegistry.mc.player.connection.sendPacket(new CPacketCloseWindow(mc.player.openContainer.windowId));
		GuiInventory inventory = new GuiInventory(TabRegistry.mc.player);
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
				t.x = cornerX + (count - 2) * 28;
				t.y = cornerY - 28;
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
/*Disabled in 1.12.2 because a vanilla bug means potion offsets are currently not a thing
 *The vanilla bug is that GuiInventory.initGui() resets GuiLeft to the recipe book version of GuiLeft,
 *and in GuiRecipeBook.updateScreenPosition() it takes no account of potion offset even if the recipe book is inactive.

		// If at least one potion is active...
		if (doPotionOffsetVanilla())
		{
			initWithPotion = true;
			return 60 + getPotionOffsetJEI() + getPotionOffsetNEI();
		}
 */
		
		// No potions, no offset needed
		initWithPotion = false;
		return 0;
	}
	
	public static boolean doPotionOffsetVanilla()
	{
	    for (PotionEffect potioneffect : mc.player.getActivePotionEffects())
	    {
	        if (potioneffect.getPotion().shouldRender(potioneffect))
	        {
	            return true;
	        }
	    }
	    return false;
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

    public static int getRecipeBookOffset(GuiInventory gui)
    {
        boolean widthTooNarrow = gui.width < 379;
        gui.func_194310_f().func_194303_a(gui.width, gui.height, mc, widthTooNarrow, ((ContainerPlayer)gui.inventorySlots).craftMatrix);
        return gui.func_194310_f().updateScreenPosition(widthTooNarrow, gui.width, gui.xSize) - (gui.width - 176) / 2;
    }
}
