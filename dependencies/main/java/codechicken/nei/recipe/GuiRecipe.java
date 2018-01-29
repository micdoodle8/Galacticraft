package codechicken.nei.recipe;

import codechicken.lib.gui.GuiDraw;
import codechicken.lib.texture.TextureUtils;
import codechicken.nei.GuiNEIButton;
import codechicken.nei.LayoutManager;
import codechicken.nei.api.IGuiContainerOverlay;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.api.stack.PositionedStack;
import codechicken.nei.config.KeyBindings;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.guihook.IContainerTooltipHandler;
import codechicken.nei.guihook.IGuiClientSide;
import codechicken.nei.guihook.IGuiHandleMouseWheel;
import codechicken.nei.util.NEIClientUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class GuiRecipe extends GuiContainer implements IGuiContainerOverlay, IGuiClientSide, IGuiHandleMouseWheel, IContainerTooltipHandler {
    public ArrayList<? extends IRecipeHandler> currenthandlers = new ArrayList<IRecipeHandler>();

    public int page;
    public int recipetype;
    public ContainerRecipe slotcontainer;
    public GuiContainer firstGui;
    public GuiContainer prevGui;
    public GuiButton nextpage;
    public GuiButton prevpage;
    public GuiButton overlay1;
    public GuiButton overlay2;

    protected GuiRecipe(GuiContainer prevgui) {
        super(new ContainerRecipe());
        slotcontainer = (ContainerRecipe) inventorySlots;

        this.prevGui = prevgui;
        this.firstGui = prevgui;
        if (prevgui instanceof IGuiContainerOverlay) {
            this.firstGui = ((IGuiContainerOverlay) prevgui).getFirstScreen();
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        currenthandlers = getCurrentRecipeHandlers();
        GuiButton nexttype = new GuiNEIButton(0, width / 2 - 70, (height - ySize) / 2 + 3, 13, 12, "<");
        GuiButton prevtype = new GuiNEIButton(1, width / 2 + 57, (height - ySize) / 2 + 3, 13, 12, ">");
        nextpage = new GuiNEIButton(2, width / 2 - 70, (height + ySize) / 2 - 18, 13, 12, "<");
        prevpage = new GuiNEIButton(3, width / 2 + 57, (height + ySize) / 2 - 18, 13, 12, ">");
        overlay1 = new GuiNEIButton(4, width / 2 + 65, (height - ySize) / 2 + 63, 13, 12, "?");
        overlay2 = new GuiNEIButton(5, width / 2 + 65, (height - ySize) / 2 + 128, 13, 12, "?");
        buttonList.add(nexttype);
        buttonList.add(prevtype);
        buttonList.add(nextpage);
        buttonList.add(prevpage);
        buttonList.add(overlay1);
        buttonList.add(overlay2);

        if (currenthandlers.size() == 1) {
            nexttype.visible = false;
            prevtype.visible = false;
        }
        refreshPage();
    }

    @Override
    public void keyTyped(char c, int i) {
        if (i == 1)//esc
        {
            mc.displayGuiScreen(firstGui);
            return;
        }
        if (GuiContainerManager.getManager(this).lastKeyTyped(i, c)) {
            return;
        }

        IRecipeHandler recipehandler = currenthandlers.get(recipetype);
        for (int recipe = page * recipehandler.recipiesPerPage(); recipe < recipehandler.numRecipes() && recipe < (page + 1) * recipehandler.recipiesPerPage(); recipe++) {
            if (recipehandler.keyTyped(this, c, i, recipe)) {
                return;
            }
        }

        if (mc.gameSettings.keyBindInventory.isActiveAndMatches(i)) {
            mc.displayGuiScreen(firstGui);
        } else if (KeyBindings.get("nei.options.keys.gui.back").isActiveAndMatches(i)) {
            mc.displayGuiScreen(prevGui);
        }

    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) throws IOException {
        IRecipeHandler recipehandler = currenthandlers.get(recipetype);
        for (int recipe = page * recipehandler.recipiesPerPage(); recipe < recipehandler.numRecipes() && recipe < (page + 1) * recipehandler.recipiesPerPage(); recipe++) {
            if (recipehandler.mouseClicked(this, par3, recipe)) {
                return;
            }
        }

        super.mouseClicked(par1, par2, par3);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        switch (guibutton.id) {
        case 0:
            prevType();
            break;
        case 1:
            nextType();
            break;
        case 2:
            prevPage();
            break;
        case 3:
            nextPage();
            break;
        case 4:
            overlayRecipe(page * currenthandlers.get(recipetype).recipiesPerPage());
            break;
        case 5:
            overlayRecipe(page * currenthandlers.get(recipetype).recipiesPerPage() + 1);
            break;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        currenthandlers.get(recipetype).onUpdate();
        refreshPage();
    }

    @Override
    public List<String> handleTooltip(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
        IRecipeHandler recipehandler = currenthandlers.get(recipetype);
        for (int i = page * recipehandler.recipiesPerPage(); i < recipehandler.numRecipes() && i < (page + 1) * recipehandler.recipiesPerPage(); i++) {
            currenttip = recipehandler.handleTooltip(this, currenttip, i);
        }

        return currenttip;
    }

    @Override
    public List<String> handleItemTooltip(GuiContainer gui, ItemStack stack, int mousex, int mousey, List<String> currenttip) {
        IRecipeHandler recipehandler = currenthandlers.get(recipetype);
        for (int i = page * recipehandler.recipiesPerPage(); i < recipehandler.numRecipes() && i < (page + 1) * recipehandler.recipiesPerPage(); i++) {
            currenttip = recipehandler.handleItemTooltip(this, stack, currenttip, i);
        }

        return currenttip;
    }

    @Override
    public List<String> handleItemDisplayName(GuiContainer gui, ItemStack itemstack, List<String> currenttip) {
        return currenttip;
    }

    private void nextPage() {
        page++;
        if (page > (currenthandlers.get(recipetype).numRecipes() - 1) / currenthandlers.get(recipetype).recipiesPerPage()) {
            page = 0;
        }
    }

    private void prevPage() {
        page--;
        if (page < 0) {
            page = (currenthandlers.get(recipetype).numRecipes() - 1) / currenthandlers.get(recipetype).recipiesPerPage();
        }
    }

    private void nextType() {
        recipetype++;
        if (recipetype >= currenthandlers.size()) {
            recipetype = 0;
        }
        page = 0;
    }

    private void prevType() {
        recipetype--;
        if (recipetype < 0) {
            recipetype = currenthandlers.size() - 1;
        }
        page = 0;
    }

    private void overlayRecipe(int recipe) {
        IRecipeOverlayRenderer renderer = currenthandlers.get(recipetype).getOverlayRenderer(firstGui, recipe);
        IOverlayHandler handler = currenthandlers.get(recipetype).getOverlayHandler(firstGui, recipe);
        boolean shift = NEIClientUtils.shiftKey();

        if (handler != null && (renderer == null || shift)) {
            mc.displayGuiScreen(firstGui);
            handler.overlayRecipe(firstGui, currenthandlers.get(recipetype), recipe, shift);
        } else if (renderer != null) {
            mc.displayGuiScreen(firstGui);
            LayoutManager.overlayRenderer = renderer;
        }
    }

    public void refreshPage() {
        refreshSlots();

        IRecipeHandler handler = currenthandlers.get(recipetype);
        boolean multiplepages = handler.numRecipes() > handler.recipiesPerPage();
        nextpage.visible = multiplepages;
        prevpage.visible = multiplepages;

        if (firstGui != null) {
            overlay1.yPosition = (height - ySize) / 2 + (handler.recipiesPerPage() == 2 ? 63 : 128);
            overlay1.visible = handler.hasOverlay(firstGui, firstGui.inventorySlots, page * handler.recipiesPerPage());
            overlay2.visible = handler.recipiesPerPage() == 2 && page * handler.recipiesPerPage() + 1 < handler.numRecipes() &&
                    handler.hasOverlay(firstGui, firstGui.inventorySlots, page * handler.recipiesPerPage() + 1);
        } else {
            overlay1.visible = overlay2.visible = false;
        }
    }

    private void refreshSlots() {
        slotcontainer.inventorySlots.clear();
        IRecipeHandler recipehandler = currenthandlers.get(recipetype);
        for (int i = page * recipehandler.recipiesPerPage(); i < recipehandler.numRecipes() && i < (page + 1) * recipehandler.recipiesPerPage(); i++) {
            Point p = getRecipePosition(i);

            List<PositionedStack> stacks = recipehandler.getIngredientStacks(i);
            for (PositionedStack stack : stacks) {
                slotcontainer.addSlot(stack, p.x, p.y);
            }

            stacks = recipehandler.getOtherStacks(i);
            for (PositionedStack stack : stacks) {
                slotcontainer.addSlot(stack, p.x, p.y);
            }

            PositionedStack result = recipehandler.getResultStack(i);
            if (result != null) {
                slotcontainer.addSlot(result, p.x, p.y);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        GuiContainerManager.enable2DRender();

        IRecipeHandler recipehandler = currenthandlers.get(recipetype);
        String s = recipehandler.getRecipeName();
        fontRendererObj.drawString(s, (xSize - fontRendererObj.getStringWidth(s)) / 2, 5, 0x404040);
        s = NEIClientUtils.translate("recipe.page", page + 1, (currenthandlers.get(recipetype).numRecipes() - 1) / recipehandler.recipiesPerPage() + 1);
        fontRendererObj.drawString(s, (xSize - fontRendererObj.getStringWidth(s)) / 2, ySize - 16, 0x404040);

        GlStateManager.pushMatrix();
        GlStateManager.translate(5, 16, 0);
        for (int i = page * recipehandler.recipiesPerPage(); i < recipehandler.numRecipes() && i < (page + 1) * recipehandler.recipiesPerPage(); i++) {
            recipehandler.drawForeground(i);
            GlStateManager.translate(0, 65, 0);
        }
        GlStateManager.popMatrix();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mx, int my) {
        GlStateManager.color(1, 1, 1, 1);
        TextureUtils.changeTexture("nei:textures/gui/recipebg.png");
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);

        GlStateManager.pushMatrix();
        GlStateManager.translate(j + 5, k + 16, 0);
        IRecipeHandler recipehandler = currenthandlers.get(recipetype);
        for (int i = page * recipehandler.recipiesPerPage(); i < recipehandler.numRecipes() && i < (page + 1) * recipehandler.recipiesPerPage(); i++) {
            recipehandler.drawBackground(i);
            GlStateManager.translate(0, 65, 0);
        }
        GlStateManager.popMatrix();
    }

    @Override
    public GuiContainer getFirstScreen() {
        return firstGui;
    }

    public boolean isMouseOver(PositionedStack stack, int recipe) {
        Slot stackSlot = slotcontainer.getSlotWithStack(stack, getRecipePosition(recipe).x, getRecipePosition(recipe).y);
        Point mousepos = GuiDraw.getMousePosition();
        Slot mouseoverSlot = getSlotAtPosition(mousepos.x, mousepos.y);

        return stackSlot == mouseoverSlot;
    }

    public Point getRecipePosition(int recipe) {
        return new Point(5, 16 + (recipe % currenthandlers.get(recipetype).recipiesPerPage()) * 65);
    }

    @Override
    public void mouseScrolled(int i) {
        if (new Rectangle(guiLeft, guiTop, xSize, ySize).contains(GuiDraw.getMousePosition())) {
            if (i > 0) {
                prevPage();
            } else {
                nextPage();
            }
        }
    }

    public abstract ArrayList<? extends IRecipeHandler> getCurrentRecipeHandlers();
}
