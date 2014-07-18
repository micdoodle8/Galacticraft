package codechicken.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.KeyManager.IKeyStateTracker;
import codechicken.nei.api.*;
import codechicken.nei.guihook.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import static codechicken.lib.gui.GuiDraw.*;
import static codechicken.nei.NEIClientConfig.*;
import static codechicken.nei.NEIClientUtils.*;

public class LayoutManager implements IContainerInputHandler, IContainerTooltipHandler, IContainerDrawHandler, IContainerObjectHandler, IKeyStateTracker
{
    private static LayoutManager instance;

    private static Widget inputFocused;
    /**
     * Sorted bottom first
     */
    private static TreeSet<Widget> drawWidgets;
    /**
     * Sorted top first
     */
    private static TreeSet<Widget> controlWidgets;

    public static ItemPanel itemPanel;
    public static Button dropDown;
    public static TextField searchField;

    public static Button options;

    public static Button prev;
    public static Button next;
    public static Label pageLabel;
    public static Button more;
    public static Button less;
    public static ItemQuantityField quantity;

    public static SaveLoadButton[] stateButtons;
    public static Button[] deleteButtons;

    public static Button delete;
    public static ButtonCycled gamemode;
    public static Button rain;
    public static Button magnet;
    public static Button[] timeButtons = new Button[4];
    public static Button heal;

    public static IRecipeOverlayRenderer overlayRenderer;

    public static HashMap<Integer, LayoutStyle> layoutStyles = new HashMap<Integer, LayoutStyle>();

    public static void load() {
        API.addLayoutStyle(0, new LayoutStyleMinecraft());
        API.addLayoutStyle(1, new LayoutStyleTMIOld());

        instance = new LayoutManager();
        KeyManager.trackers.add(instance);
        GuiContainerManager.addInputHandler(instance);
        GuiContainerManager.addTooltipHandler(instance);
        GuiContainerManager.addDrawHandler(instance);
        GuiContainerManager.addObjectHandler(instance);
        init();
    }

    @Override
    public void onPreDraw(GuiContainer gui) {
        if (!isHidden() && isEnabled() && gui instanceof InventoryEffectRenderer)//Reset the gui to the center of the screen, for potion effect offsets etc
        {
            gui.guiLeft = (gui.width - gui.xSize) / 2;
            gui.guiTop = (gui.height - gui.ySize) / 2;

            if (gui instanceof GuiContainerCreative && gui.buttonList.size() >= 2) {
                GuiButton button1 = (GuiButton) gui.buttonList.get(0);
                GuiButton button2 = (GuiButton) gui.buttonList.get(1);
                button1.xPosition = gui.guiLeft;
                button2.xPosition = gui.guiLeft + gui.xSize - 20;
            }
        }
    }

    @Override
    public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
        if (isHidden())
            return;

        for (Widget widget : controlWidgets)
            widget.onGuiClick(mousex, mousey);
    }

    @Override
    public boolean mouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
        if (isHidden())
            return false;

        if (!isEnabled())
            return options.contains(mousex, mousey) && options.handleClick(mousex, mousey, button);

        for (Widget widget : controlWidgets) {
            widget.onGuiClick(mousex, mousey);
            if (widget.contains(mousex, mousey) ? widget.handleClick(mousex, mousey, button) : widget.handleClickExt(mousex, mousey, button))
                return true;
        }

        return false;
    }

    @Override
    public boolean objectUnderMouse(GuiContainer gui, int mousex, int mousey) {
        if (!isHidden() && isEnabled()) {
            for (Widget widget : controlWidgets)
                if (widget.contains(mousex, mousey))
                    return true;
        }

        return false;
    }

    public boolean keyTyped(GuiContainer gui, char keyChar, int keyID) {
        if (isEnabled() && !isHidden()) {
            if (inputFocused != null)
                return inputFocused.handleKeyPress(keyID, keyChar);

            for (Widget widget : controlWidgets)
                if (widget.handleKeyPress(keyID, keyChar))
                    return true;
        }

        return false;
    }

    @Override
    public void onKeyTyped(GuiContainer gui, char keyChar, int keyID) {
    }

    @Override
    public boolean lastKeyTyped(GuiContainer gui, char keyChar, int keyID) {
        if (keyID == getKeyBinding("gui.hide")) {
            toggleBooleanSetting("inventory.hidden");
            return true;
        }
        if (isEnabled() && !isHidden()) {
            for (Widget widget : controlWidgets)
                if (inputFocused == null)
                    widget.lastKeyTyped(keyID, keyChar);
        }
        return false;
    }

    public void onMouseUp(GuiContainer gui, int mx, int my, int button) {
        if (!isHidden() && isEnabled()) {
            for (Widget widget : controlWidgets)
                widget.mouseUp(mx, my, button);
        }
    }

    @Override
    public void onMouseDragged(GuiContainer gui, int mx, int my, int button, long heldTime) {
        if (!isHidden() && isEnabled()) {
            for (Widget widget : controlWidgets)
                widget.mouseDragged(mx, my, button, heldTime);
        }
    }

    @Override
    public ItemStack getStackUnderMouse(GuiContainer gui, int mousex, int mousey) {
        if (!isHidden() && isEnabled()) {
            for (Widget widget : controlWidgets) {
                ItemStack stack = widget.getStackMouseOver(mousex, mousey);
                if (stack != null)
                    return stack;
            }
        }
        return null;
    }

    public void renderObjects(GuiContainer gui, int mousex, int mousey) {
        if (!isHidden()) {
            layout(gui);
            if (isEnabled()) {
                getLayoutStyle().drawBackground(GuiContainerManager.getManager(gui));
                for (Widget widget : drawWidgets)
                    widget.draw(mousex, mousey);
            } else {
                options.draw(mousex, mousey);
            }

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
    }

    @Override
    public void postRenderObjects(GuiContainer gui, int mousex, int mousey) {
        if (!isHidden() && isEnabled()) {
            for (Widget widget : drawWidgets)
                widget.postDraw(mousex, mousey);
        }
    }

    @Override
    public List<String> handleTooltip(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
        if (!isHidden() && isEnabled() && GuiContainerManager.shouldShowTooltip(gui)) {
            for (Widget widget : controlWidgets)
                currenttip = widget.handleTooltip(mousex, mousey, currenttip);
        }
        return currenttip;
    }

    @Override
    public List<String> handleItemDisplayName(GuiContainer gui, ItemStack stack, List<String> currenttip) {
        String overridename = ItemInfo.getNameOverride(stack);
        if (overridename != null)
            currenttip.set(0, overridename);

        String mainname = currenttip.get(0);
        if (showIDs()) {
            mainname += " " + Item.getIdFromItem(stack.getItem());
            if (stack.getItemDamage() != 0)
                mainname += ":" + stack.getItemDamage();

            currenttip.set(0, mainname);
        }

        return currenttip;
    }

    @Override
    public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, int mousex, int mousey, List<String> currenttip) {
        return currenttip;
    }

    public static void layout(GuiContainer gui) {
        VisiblityData visiblity = new VisiblityData();
        if (isHidden())
            visiblity.showNEI = false;
        if (gui.height - gui.ySize <= 40)
            visiblity.showSearchSection = false;
        if (gui.guiLeft - 4 < 76)
            visiblity.showWidgets = false;

        for (INEIGuiHandler handler : GuiInfo.guiHandlers)
            handler.modifyVisiblity(gui, visiblity);

        visiblity.translateDependancies();

        getLayoutStyle().layout(gui, visiblity);

        updateWidgetVisiblities(gui, visiblity);
    }

    private static void init() {
        itemPanel = new ItemPanel();
        dropDown = new Button("NEI Subsets")
        {
            @Override
            public boolean onButtonPress(boolean rightclick) {
                return false;
            }
        };
        searchField = new SearchField("search");

        options = new Button("Options")
        {
            @Override
            public boolean onButtonPress(boolean rightclick) {
                if (!rightclick) {
                    getOptionList().showGui(getGuiContainer());
                    return true;
                }
                return false;
            }

            @Override
            public String getRenderLabel() {
                return translate("inventory.options");
            }
        };
        prev = new Button("Prev")
        {
            public boolean onButtonPress(boolean rightclick) {
                if (!rightclick) {
                    LayoutManager.itemPanel.scroll(-1);
                    return true;
                }
                return false;
            }

            @Override
            public String getRenderLabel() {
                return translate("inventory.prev");
            }
        };
        next = new Button("Next")
        {
            public boolean onButtonPress(boolean rightclick) {
                if (!rightclick) {
                    LayoutManager.itemPanel.scroll(1);
                    return true;
                }
                return false;
            }

            @Override
            public String getRenderLabel() {
                return translate("inventory.next");
            }
        };
        pageLabel = new Label("(0/0)", true);
        more = new Button("+")
        {
            @Override
            public boolean onButtonPress(boolean rightclick) {
                if (rightclick)
                    return false;

                int modifier = controlKey() ? 64 : shiftKey() ? 10 : 1;

                int quantity = getItemQuantity() + modifier;
                if (quantity < 0)
                    quantity = 0;

                setItemQuantity(quantity);
                return true;
            }
        };
        less = new Button("-")
        {
            @Override
            public boolean onButtonPress(boolean rightclick) {
                if (rightclick)
                    return false;

                int modifier = controlKey() ? -64 : shiftKey() ? -10 : -1;

                int quantity = getItemQuantity() + modifier;
                if (quantity < 0)
                    quantity = 0;

                setItemQuantity(quantity);
                return true;
            }
        };
        quantity = new ItemQuantityField("quantity");

        stateButtons = new SaveLoadButton[7];
        deleteButtons = new Button[7];

        for (int i = 0; i < 7; i++) {
            final int savestate = i;
            stateButtons[i] = new SaveLoadButton("")
            {
                @Override
                public boolean onButtonPress(boolean rightclick) {
                    if (isStateSaved(savestate))
                        loadState(savestate);
                    else
                        saveState(savestate);
                    return true;
                }

                @Override
                public void onTextChange() {
                    NBTTagCompound statelist = global.nbt.getCompoundTag("statename");
                    global.nbt.setTag("statename", statelist);

                    statelist.setString("" + savestate, label);
                    global.saveNBT();
                }
            };
            deleteButtons[i] = new Button("x")
            {
                @Override
                public boolean onButtonPress(boolean rightclick) {
                    if (!rightclick) {
                        clearState(savestate);
                        return true;
                    }
                    return false;
                }
            };
        }

        delete = new Button()
        {
            @Override
            public boolean onButtonPress(boolean rightclick) {
                if ((state & 0x3) == 2)
                    return false;

                ItemStack held = getHeldItem();
                if (held != null) {
                    if (shiftKey()) {
                        deleteHeldItem();
                        deleteItemsOfType(held);
                    } else if (rightclick)
                        decreaseSlotStack(-999);
                    else
                        deleteHeldItem();
                } else if (shiftKey())
                    deleteEverything();
                else
                    NEIController.toggleDeleteMode();

                return true;
            }

            public String getButtonTip() {
                if ((state & 0x3) != 2) {
                    if (shiftKey())
                        return translate("inventory.delete.all");
                    if (NEIController.canUseDeleteMode())
                        return getStateTip("delete", state);
                }
                return null;
            }

            @Override
            public void postDraw(int mousex, int mousey) {
                if(contains(mousex, mousey) && getHeldItem() != null && (state & 0x3) != 2)
                    GuiDraw.drawTip(mousex + 9, mousey, translate("inventory.delete." + (shiftKey() ? "all" : "one"), GuiContainerManager.itemDisplayNameShort(getHeldItem())));
            }
        };
        gamemode = new ButtonCycled()
        {
            @Override
            public boolean onButtonPress(boolean rightclick) {
                if (!rightclick) {
                    cycleGamemode();
                    return true;
                }
                return false;
            }

            public String getButtonTip() {
                return translate("inventory.gamemode." + getNextGamemode());
            }
        };
        gamemode.icons = new Image[3];
        rain = new Button()
        {
            @Override
            public boolean onButtonPress(boolean rightclick) {
                if (handleDisabledButtonPress("rain", rightclick))
                    return true;

                if (!rightclick) {
                    toggleRaining();
                    return true;
                }
                return false;
            }

            public String getButtonTip() {
                return getStateTip("rain", state);
            }
        };
        magnet = new Button()
        {
            @Override
            public boolean onButtonPress(boolean rightclick) {
                if (!rightclick) {
                    toggleMagnetMode();
                    return true;
                }
                return false;
            }

            public String getButtonTip() {
                return getStateTip("magnet", state);
            }
        };
        for (int i = 0; i < 4; i++) {
            final int zone = i;
            timeButtons[i] = new Button()
            {
                @Override
                public boolean onButtonPress(boolean rightclick) {
                    if (handleDisabledButtonPress(NEIActions.timeZones[zone], rightclick))
                        return true;

                    if (!rightclick) {
                        setHourForward(zone * 6);
                        return true;
                    }
                    return false;
                }

                @Override
                public String getButtonTip() {
                    return getTimeTip(NEIActions.timeZones[zone], state);
                }

            };
        }
        heal = new Button()
        {
            @Override
            public boolean onButtonPress(boolean rightclick) {
                if (!rightclick) {
                    healPlayer();
                    return true;
                }
                return false;
            }

            @Override
            public String getButtonTip() {
                return translate("inventory.heal");
            }
        };

        delete.state |= 0x4;
        gamemode.state |= 0x4;
        rain.state |= 0x4;
        magnet.state |= 0x4;
    }

    private static String getStateTip(String name, int state) {
        String sfx = (state & 0x3) == 2 ? "enable" :
                (state & 0x3) == 1 ? "0" : "1";

        return translate("inventory." + name + "." + sfx);
    }

    private static String getTimeTip(String name, int state) {
        String sfx = (state & 0x3) == 2 ? "enable" : "set";
        return translate("inventory." + name + "." + sfx);
    }

    private static boolean handleDisabledButtonPress(String ident, boolean rightclick) {
        if (!NEIActions.canDisable.contains(ident))
            return false;
        if (rightclick != disabledActions.contains(ident))
            return setPropertyDisabled(ident, rightclick);
        return false;
    }

    private static boolean setPropertyDisabled(String ident, boolean disable) {
        if (disable && NEIActions.base(ident).equals("time")) {
            int count = 0;
            for (int i = 0; i < 4; i++) {
                if (disabledActions.contains(NEIActions.timeZones[i]))
                    count++;
            }
            if (count == 3)
                return false;
        }
        if (hasSMPCounterPart())
            NEICPH.sendSetPropertyDisabled(ident, disable);

        return true;
    }

    @Override
    public void load(GuiContainer gui) {
        if (isEnabled()) {
            setInputFocused(null);

            ItemList.loadItems();
            overlayRenderer = null;

            getLayoutStyle().init();
            layout(gui);
        }

        NEIController.load(gui);

        if (checkCreativeInv(gui)) {
            if (gui.mc.currentScreen instanceof GuiContainerCreative)//makes cycle transititions seemless. Only closes if we are opening from fresh
                gui.mc.displayGuiScreen(null);
            return;
        }
    }

    @Override
    public void refresh(GuiContainer gui) {
    }

    public boolean checkCreativeInv(GuiContainer gui) {
        if (gui instanceof GuiContainerCreative && invCreativeMode()) {
            NEICPH.sendCreativeInv(true);
            return true;
        } else if (gui instanceof GuiExtendedCreativeInv && !invCreativeMode()) {
            NEICPH.sendCreativeInv(false);
            return true;
        }
        return false;
    }

    public static void updateWidgetVisiblities(GuiContainer gui, VisiblityData visiblity) {
        drawWidgets = new TreeSet<Widget>(new WidgetZOrder(false));
        controlWidgets = new TreeSet<Widget>(new WidgetZOrder(true));

        if (!visiblity.showNEI)
            return;

        addWidget(options);
        if (visiblity.showItemPanel) {
            addWidget(itemPanel);
            addWidget(prev);
            addWidget(next);
            addWidget(pageLabel);
            if (canPerformAction("item")) {
                addWidget(more);
                addWidget(less);
                addWidget(quantity);
            }
        }

        if (visiblity.showSearchSection) {
            addWidget(dropDown);
            addWidget(searchField);
        }

        if (canPerformAction("item") && visiblity.showStateButtons) {
            for (int i = 0; i < 7; i++) {
                addWidget(stateButtons[i]);
                if (isStateSaved(i))
                    addWidget(deleteButtons[i]);
            }
        }
        if (visiblity.showUtilityButtons) {
            if (canPerformAction("time")) {
                for (int i = 0; i < 4; i++)
                    addWidget(timeButtons[i]);
            }
            if (canPerformAction("rain"))
                addWidget(rain);
            if (canPerformAction("heal"))
                addWidget(heal);
            if (canPerformAction("magnet"))
                addWidget(magnet);
            if (isValidGamemode("creative") ||
                    isValidGamemode("creative+") ||
                    isValidGamemode("adventure"))
                addWidget(gamemode);
            if (canPerformAction("delete"))
                addWidget(delete);
        }
    }


    public static LayoutStyle getLayoutStyle(int id) {
        LayoutStyle style = layoutStyles.get(id);
        if (style == null)
            style = layoutStyles.get(0);
        return style;
    }

    public static LayoutStyle getLayoutStyle() {
        return getLayoutStyle(NEIClientConfig.getLayoutStyle());
    }

    private static void addWidget(Widget widget) {
        drawWidgets.add(widget);
        controlWidgets.add(widget);
    }

    @Override
    public void guiTick(GuiContainer gui) {
        if (checkCreativeInv(gui))
            return;

        if (!isEnabled())
            return;

        for (Widget widget : controlWidgets)
            widget.update();
    }

    @Override
    public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
        if (isHidden() || !isEnabled())
            return false;

        for (Widget widget : controlWidgets)
            if (widget.onMouseWheel(scrolled, mousex, mousey))
                return true;

        return false;
    }

    @Override
    public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
    }

    @Override
    public boolean shouldShowTooltip(GuiContainer gui) {
        return itemPanel.draggedStack == null;
    }

    public static Widget getInputFocused() {
        return inputFocused;
    }

    public static void setInputFocused(Widget widget) {
        if (inputFocused != null)
            inputFocused.loseFocus();

        inputFocused = widget;
        if (inputFocused != null)
            inputFocused.gainFocus();
    }

    @Override
    public void renderSlotUnderlay(GuiContainer gui, Slot slot) {
        if (overlayRenderer != null)
            overlayRenderer.renderOverlay(GuiContainerManager.getManager(gui), slot);
    }

    @Override
    public void renderSlotOverlay(GuiContainer window, Slot slot) {
        ItemStack item = slot.getStack();
        if (world.nbt.getBoolean("searchinventories") && (item == null ? !getSearchExpression().equals("") : !ItemList.itemMatchesSearch(item))) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glTranslatef(0, 0, 150);
            drawRect(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, 0x80000000);
            GL11.glTranslatef(0, 0, -150);
            GL11.glEnable(GL11.GL_LIGHTING);
        }
    }

    public static void drawIcon(int x, int y, Image image) {
        changeTexture("nei:textures/nei_sprites.png");
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        drawTexturedModalRect(x, y, image.x, image.y, image.width, image.height);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawButtonBackground(int x, int y, int w, int h, boolean edges, int type) {
        int wtiles = 0;
        int ew = w;//end width
        if (w / 2 > 100) {
            wtiles = (w - 200) / 50 + 1;
            ew = 200;
        }

        int w1 = ew / 2;
        int h1 = h / 2;
        int w2 = (ew + 1) / 2;
        int h2 = (h + 1) / 2;

        int x2 = x + w - w2;
        int y2 = y + h - h2;

        int ty = 46 + type * 20;
        int te = (edges ? 0 : 1);//tex edges

        int ty1 = ty + te;
        int tx1 = te;
        int tx3 = 75;
        //halfway the 1 is for odd number adjustment
        int ty2 = ty + 20 - h2 - te;
        int tx2 = 200 - w2 - te;

        changeTexture("textures/gui/widgets.png");
        drawTexturedModalRect(x, y, tx1, ty1, w1, h1);//top left
        drawTexturedModalRect(x, y2, tx1, ty2, w1, h2);//bottom left

        for (int tile = 0; tile < wtiles; tile++) {
            int tilex = x + w1 + 50 * tile;
            drawTexturedModalRect(tilex, y, tx3, ty1, 50, h1);//top
            drawTexturedModalRect(tilex, y2, tx3, ty2, 50, h2);//bottom
        }

        drawTexturedModalRect(x2, y, tx2, ty1, w2, h1);//top right
        drawTexturedModalRect(x2, y2, tx2, ty2, w2, h2);//bottom right
    }

    public static LayoutManager instance() {
        return instance;
    }

    @Override
    public void tickKeyStates() {
        if (Minecraft.getMinecraft().currentScreen != null)
            return;

        if (KeyManager.keyStates.get("world.dawn").down)
            timeButtons[0].onButtonPress(false);
        if (KeyManager.keyStates.get("world.noon").down)
            timeButtons[1].onButtonPress(false);
        if (KeyManager.keyStates.get("world.dusk").down)
            timeButtons[2].onButtonPress(false);
        if (KeyManager.keyStates.get("world.midnight").down)
            timeButtons[3].onButtonPress(false);
        if (KeyManager.keyStates.get("world.rain").down)
            rain.onButtonPress(false);
        if (KeyManager.keyStates.get("world.heal").down)
            heal.onButtonPress(false);
        if (KeyManager.keyStates.get("world.creative").down)
            gamemode.onButtonPress(false);
    }
}
