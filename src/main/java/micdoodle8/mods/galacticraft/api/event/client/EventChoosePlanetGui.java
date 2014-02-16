package micdoodle8.mods.galacticraft.api.event.client;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiSlot;
import cpw.mods.fml.common.eventhandler.Event;

/**
 * 
 */
public class EventChoosePlanetGui extends Event
{
	public List<GuiButton> buttonList;

	public EventChoosePlanetGui(List<GuiButton> buttonList)
	{
		this.buttonList = buttonList;
	}

	/**
	 * Called when the GUI is first opened or when the screen is resized
	 */
	public static class Init extends EventChoosePlanetGui
	{
		public Init(List<GuiButton> buttonList)
		{
			super(buttonList);
		}
	}

	/**
	 * Called when a planet slot is clicked
	 */
	public static class SlotClicked extends EventChoosePlanetGui
	{
		public GuiSlot slotClicked;

		public SlotClicked(List<GuiButton> buttonList, GuiSlot slotClicked)
		{
			super(buttonList);
			this.slotClicked = slotClicked;
		}
	}
}
