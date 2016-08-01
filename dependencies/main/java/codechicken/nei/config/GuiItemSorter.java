package codechicken.nei.config;

import codechicken.lib.math.MathHelper;
import codechicken.nei.ItemSorter;
import codechicken.nei.ItemSorter.SortEntry;
import codechicken.nei.LayoutManager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static codechicken.lib.gui.GuiDraw.drawStringC;
import static codechicken.lib.gui.GuiDraw.getMousePosition;

public class GuiItemSorter extends GuiOptionPane {
    public class SortItem {
        public double ya;
        public double y;
        public SortEntry e;

        public SortItem(SortEntry e) {
            this.e = e;
            y = slotY();
        }

        public int slotY() {
            return GuiItemSorter.this.slotY(list.indexOf(e));
        }

        public void update(int my) {
            ya = y;
            if (dragging && this == dragged) {
                y = dragstarty + my - mouseclickedy;
            } else {
                y = MathHelper.approachExp(y, slotY(), 0.4, 20);
            }
        }

        public boolean contains(int my) {
            my -= drawBounds().y;
            return my >= y && my < y + 20;
        }
    }

    public Option opt;
    public List<SortItem> slots = new ArrayList<SortItem>();
    public List<SortEntry> list;

    boolean dragging;
    public SortItem dragged;
    public int mouseclickedy;
    public double dragstarty;

    public GuiItemSorter(Option opt) {
        this.opt = opt;
        list = ItemSorter.fromSaveString(opt.renderTag().getValue());
        for (SortEntry e : list) {
            slots.add(new SortItem(e));
        }
    }

    public int slotY(int slot) {
        return 2 + slot * 24;
    }

    @Override
    public int contentHeight() {
        return slots.size() * 24;
    }

    @Override
    public void drawContent(int mx, int my, float frame) {
        Rectangle w = drawBounds();

        for (SortItem item : slots) {
            if (item != dragged) {
                drawItem(w, item, mx, my, frame);
            }
        }

        if (dragged != null) {
            drawItem(w, dragged, mx, my, frame);
        }
    }

    public void drawItem(Rectangle w, SortItem item, int mx, int my, float frame) {
        double y = MathHelper.interpolate(item.ya, item.y, frame);
        GlStateManager.translate(0, y, 0);
        Rectangle b = new Rectangle(w.x, w.y, w.width, 20);
        boolean mouseOver = itemAt(w.x + mx, w.y + my) == item;

        GlStateManager.color(1, 1, 1, 1);
        LayoutManager.drawButtonBackground(b.x, b.y, b.width, b.height, true, mouseOver ? 2 : 1);
        drawStringC(item.e.getLocalisedName(), b.x, b.y, b.width, b.height, mouseOver ? 0xFFFFFFA0 : 0xFFE0E0E0);

        GlStateManager.translate(0, -y, 0);
    }

    @Override
    public String getTitle() {
        return opt.translateN(opt.name);
    }

    @Override
    public GuiScreen getParentScreen() {
        return opt.slot.getGui();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        int my = getMousePosition().y;
        for (SortItem item : slots) {
            item.update(my);
        }

        if (dragging) {
            int nslot = (int) MathHelper.clip((dragged.y - (2 - 12)) / 24, 0, slots.size() - 1);
            if (nslot != list.indexOf(dragged.e)) {
                list.remove(dragged.e);
                list.add(nslot, dragged.e);
                opt.getTag().setValue(ItemSorter.getSaveString(list));
                if (opt.activeTag() == opt.getTag()) {
                    ItemSorter.list = new ArrayList<SortEntry>(list);
                }
            }
        }
    }

    public SortItem itemAt(int mx, int my) {
        if (!pane.windowBounds().contains(mx, my)) {
            return null;
        }

        if (dragged != null && dragged.contains(my)) {
            return dragged;
        }

        for (SortItem item : slots) {
            if (item.contains(my)) {
                return item;
            }
        }

        return null;
    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        super.mouseClicked(x, y, button);

        if (!dragging && button == 0) {
            SortItem item = itemAt(x, y);
            if (item != null) {
                dragging = true;
                dragged = item;
                mouseclickedy = y;
                dragstarty = item.y;
            }
        }
    }

    @Override
    protected void mouseReleased(int x, int y, int button) {
        if (button == 0) {
            dragging = false;
        }
    }

    @Override
    public List<String> handleTooltip(int mx, int my, List<String> tooltip) {
        if (!dragging) {
            SortItem item = itemAt(mx, my);
            if (item != null && item.e.getTooltip() != null) {
                tooltip.add(item.e.getTooltip());
            }
        }
        return tooltip;
    }
}
