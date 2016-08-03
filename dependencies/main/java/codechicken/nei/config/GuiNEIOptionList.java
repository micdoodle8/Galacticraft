package codechicken.nei.config;

import codechicken.core.gui.GuiCCButton;
import codechicken.nei.NEIClientConfig;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;

import java.awt.*;
import java.net.URI;

public class GuiNEIOptionList extends GuiOptionList implements GuiYesNoCallback {
    private GuiCCButton patreonButton;

    public GuiNEIOptionList(GuiScreen parent, OptionList optionList, boolean world) {
        super(parent, optionList, world);
    }

    @Override
    public void resize() {
        super.resize();
        patreonButton.x = width - 73;
        patreonButton.y = height - 23;
    }

    @Override
    public void addWidgets() {
        super.addWidgets();
        add(patreonButton = new PatreonButton(0, 0, 70, 20).setActionCommand("patreon"));
    }

    @Override
    public void actionPerformed(String ident, Object... params) {
        if (ident.equals("patreon")) {
            GuiConfirmOpenLink gui = new GuiConfirmOpenLink(this, "patreon.com/cb", 0, true);
            gui.disableSecurityWarning();
            mc.displayGuiScreen(gui);
        } else {
            super.actionPerformed(ident, params);
        }
    }

    @Override
    public void confirmClicked(boolean yes, int id) {
        if (yes && id == 0) {
            try {
                Desktop.getDesktop().browse(new URI("http://patreon.com/cb"));
            } catch (Exception e) {
                NEIClientConfig.logger.error("Failed to open patreon page", e);
            }
        }
        mc.displayGuiScreen(this);
    }
}
