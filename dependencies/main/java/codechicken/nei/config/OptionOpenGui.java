package codechicken.nei.config;

import codechicken.nei.NEIClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class OptionOpenGui extends OptionButton {
    public final Class<? extends GuiScreen> guiClass;

    public OptionOpenGui(String name, Class<? extends GuiScreen> guiClass) {
        super(name, null, name, null);
        this.guiClass = guiClass;
    }

    @Override
    public boolean onClick(int button) {
        try {
            Minecraft.getMinecraft().displayGuiScreen(guiClass.getConstructor(Option.class).newInstance(this));
        } catch (Exception e) {
            NEIClientConfig.logger.error("Unable to open gui class: " + guiClass.getName() + " from option " + fullName(), e);
        }
        return true;
    }
}
