package codechicken.nei.thirdparty.colossalchests;

import codechicken.lib.util.ReflectionManager;
import codechicken.nei.api.API;
import codechicken.nei.util.LogHelper;
import net.minecraft.client.gui.inventory.GuiContainer;

/**
 * Created by covers1624 on 9/02/2017.
 */
public class ColossalChestsConfig {

    private static boolean hasInit = false;

    public static void init() {
        if (hasInit) {
            return;
        }
        hasInit = true;
        try {
            Class<?> clazz = null;
            try {
                clazz = ReflectionManager.findClass("org.cyclops.colossalchests.client.gui.container.GuiColossalChest");
            } catch (Exception ignored) {
            }
            if (clazz != null) {
                API.addFastTransferExemptContainer((Class<? extends GuiContainer>) clazz);
            }
        } catch (Exception e) {
            LogHelper.fatalError("Something went wring trying to enable ColossalChests ingegration.", e);
        }
    }

}
