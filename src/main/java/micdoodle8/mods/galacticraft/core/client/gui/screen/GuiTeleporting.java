package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.core.dimension.DimensionSpaceStation;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerClient;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.dimension.DimensionType;

public class GuiTeleporting extends Screen
{
    private final DimensionType targetDimensionID;
    private final String message;

    public GuiTeleporting(DimensionType targetDimensionID)
    {
        super(new StringTextComponent("Teleporting"));
        this.targetDimensionID = targetDimensionID;
        String[] possibleStrings = new String[]{"Taking one small step", "Taking one giant leap", "Prepare for entry!"};
        this.message = possibleStrings[(int) (Math.random() * possibleStrings.length)];
        TickHandlerClient.teleportingGui = this;
    }

    @Override
    protected void init()
    {
        super.init();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        super.render(mouseX, mouseY, partialTicks);
        this.renderBackground(0);
        this.drawCenteredString(this.font, this.message, this.width / 2, this.height / 2, ColorUtil.to32BitColor(255, 255, 255, 255));
    }

    @Override
    public void tick()
    {
        super.tick();
        if (minecraft.player != null && minecraft.player.world != null)
        {
            // Screen will exit when the player is in the target dimension and has started moving down
            if (minecraft.player.world.getDimension().getType() == this.targetDimensionID)
            {
                if ((minecraft.player.world.getDimension() instanceof DimensionSpaceStation || (minecraft.player.getPosY() - minecraft.player.lastTickPosY) < 0.0))
                {
                    minecraft.displayGuiScreen(null);
                    TickHandlerClient.teleportingGui = null;
                }
            }
        }
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
