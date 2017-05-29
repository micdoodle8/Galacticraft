package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerClient;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GuiTeleporting extends GuiScreen
{
    private final int targetDimensionID;
    private final String message;
    private int progress;

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
        this.drawCenteredString(this.fontRendererObj, this.message, this.width / 2, this.height / 2, ColorUtil.to32BitColor(255, 255, 255, 255));
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        if (mc.thePlayer != null && mc.thePlayer.worldObj != null)
        {
            // Screen will exit when the player is in the target dimension and has started moving down
            if (mc.thePlayer.worldObj.provider.getDimension() == this.targetDimensionID)
            {
                if ((mc.thePlayer.worldObj.provider instanceof IZeroGDimension || (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) < 0.0))
                {
                    mc.displayGuiScreen(null);
                    TickHandlerClient.teleportingGui = null;
                }
            }
        }

        // The following code is copied from GuiDownloadTerrain:
        ++this.progress;

        if (this.progress % 20 == 0)
        {
            ((NetHandlerPlayClient)FMLCommonHandler.instance().getClientPlayHandler()).sendPacket(new CPacketKeepAlive());
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
