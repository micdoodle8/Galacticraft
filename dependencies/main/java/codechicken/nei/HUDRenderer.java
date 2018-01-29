package codechicken.nei;

import codechicken.nei.KeyManager.IKeyStateTracker;
import codechicken.nei.api.ItemInfo;
import codechicken.nei.guihook.GuiContainerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static codechicken.lib.gui.GuiDraw.*;

@Deprecated
public class HUDRenderer implements IKeyStateTracker {
    @Override
    public void tickKeyStates() {
//        if (KeyBindings.get("nei.options.keys.world.highlight_tips").isPressed()) {
//            ConfigTag tag = NEIClientConfig.getSetting("world.highlight_tips");
//            tag.setBooleanValue(!tag.getBooleanValue());
//        }
    }

    public static void renderOverlay() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen == null &&
                mc.theWorld != null &&
                !mc.gameSettings.keyBindPlayerList.isKeyDown() &&
                NEIClientConfig.getBooleanSetting("world.highlight_tips") &&
                mc.objectMouseOver != null &&
                mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            World world = mc.theWorld;
            ArrayList<ItemStack> items = ItemInfo.getIdentifierItems(world, mc.thePlayer, mc.objectMouseOver);
            if (items.isEmpty()) {
                return;
            }

            int minDamage = Integer.MAX_VALUE;
            ItemStack stack = null;
            for (ItemStack astack : items) {
                if (astack.getItem() != null && astack.getItemDamage() < minDamage) {
                    stack = astack;
                    minDamage = stack.getItemDamage();
                }
            }

            renderOverlay(stack, ItemInfo.getText(stack, world, mc.thePlayer, mc.objectMouseOver), getPositioning());
        }
    }

    public static void renderOverlay(ItemStack stack, List<String> textData, Point pos) {
        if ((stack == null || stack.getItem() == null) && textData.isEmpty()) {
            return;
        }

        int w = 0;
        for (String s : textData) {
            w = Math.max(w, getStringWidth(s) + 29);
        }
        int h = Math.max(24, 10 + 10 * textData.size());

        Dimension size = displaySize();
        int x = (size.width - w - 1) * pos.x / 10000;
        int y = (size.height - h - 1) * pos.y / 10000;

        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableDepth();

        //drawTooltipBox(x, y, w, h);

        int ty = (h - 8 * textData.size()) / 2;
        for (int i = 0; i < textData.size(); i++) {
            drawString(textData.get(i), x + 24, y + ty + 10 * i, 0xFFA0A0A0, true);
        }

        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();

        if (stack != null && stack.getItem() != null) {
            GuiContainerManager.drawItem(x + 5, y + h / 2 - 8, stack);
        }
    }

    private static Point getPositioning() {
        return new Point(NEIClientConfig.getSetting("world.highlight_tips.x").getIntValue(), NEIClientConfig.getSetting("world.highlight_tips.y").getIntValue());
    }

    public static void load() {
        //KeyManager.trackers.add(new HUDRenderer());
    }
}
