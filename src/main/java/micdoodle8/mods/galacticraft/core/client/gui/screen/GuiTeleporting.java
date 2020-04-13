package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.core.dimension.WorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerClient;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.client.gui.GuiScreen;

public class GuiTeleporting extends GuiScreen
{
    private final int targetDimensionID;
    private final String message;

    public GuiTeleporting(int targetDimensionID)
    {
        this.targetDimensionID = targetDimensionID;
        String[] possibleStrings = new String[] { "Taking one small step", "Taking one giant leap", "Prepare for entry!" };
        this.message = possibleStrings[(int) (Math.random() * possibleStrings.length)];
        TickHandlerClient.teleportingGui = this;
    }

    @Override
    public void initGui()
    {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawBackground(0);
        this.drawCenteredString(this.fontRenderer, this.message, this.width / 2, this.height / 2, ColorUtil.to32BitColor(255, 255, 255, 255));
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
		if (mc.player != null && mc.player.world != null)
        {
            // Screen will exit when the player is in the target dimension and has started moving down
            if (mc.player.world.provider.getDimension() == this.targetDimensionID)
            {
                if ((mc.player.world.provider instanceof WorldProviderSpaceStation || (mc.player.posY - mc.player.lastTickPosY) < 0.0))
                {
                    mc.displayGuiScreen(null);
                    TickHandlerClient.teleportingGui = null;
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
