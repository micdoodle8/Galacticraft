package codechicken.nei.recipe;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import codechicken.core.gui.GuiDraw;
import codechicken.lib.render.CCRenderState;
import codechicken.nei.GuiNEIButton;
import codechicken.nei.LayoutManager;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.IGuiContainerOverlay;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.forge.GuiContainerManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;

public abstract class GuiRecipe extends GuiContainer implements IGuiContainerOverlay
{
    protected GuiRecipe(GuiContainer prevgui)
    {
        super(new ContainerRecipe());
        slotcontainer = (ContainerRecipe) inventorySlots;
        
        this.prevGui = prevgui;
        this.firstGui = prevgui;
        if(prevgui instanceof IGuiContainerOverlay)
        {
            this.firstGui = ((IGuiContainerOverlay)prevgui).getFirstScreen();
        }
    }
    
    @Override
    public boolean isClientOnly()
    {
        return true;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        
        currenthandlers = getCurrentRecipeHandlers();
        GuiButton nexttype = new GuiNEIButton(0, width/2 - 70, (height-ySize)/2 + 3, 13, 12, "<");
        GuiButton prevtype = new GuiNEIButton(1, width/2 + 57, (height-ySize)/2 + 3, 13, 12, ">");
        nextpage = new GuiNEIButton(2, width/2 - 70, (height+ySize)/2 - 18, 13, 12, "<");
        prevpage = new GuiNEIButton(3, width/2 + 57, (height+ySize)/2 - 18, 13, 12, ">");
        overlay1 = new GuiNEIButton(4, width/2 + 65, (height-ySize)/2 + 63, 13, 12, "?");
        overlay2 = new GuiNEIButton(5, width/2 + 65, (height-ySize)/2 + 128, 13, 12, "?");
        buttonList.add(nexttype);
        buttonList.add(prevtype);
        buttonList.add(nextpage);
        buttonList.add(prevpage);
        buttonList.add(overlay1);
        buttonList.add(overlay2);
        
        if(currenthandlers.size() == 1)
        {
            nexttype.drawButton = false;
            prevtype.drawButton = false;
        }        
        refreshPage();
    }

    @Override
    public void keyTyped(char c, int i)
    {
        if(i == 1)//esc
        {
            firstGui.refresh();
            mc.displayGuiScreen(firstGui);
            return;
        }
        if(manager.lastKeyTyped(i, c))
        {
            return;
        }
        IRecipeHandler recipehandler = currenthandlers.get(recipetype);
        for(int recipe = page * recipehandler.recipiesPerPage(); recipe < recipehandler.numRecipes() && recipe < (page + 1) * recipehandler.recipiesPerPage(); recipe++)
        {
            if(recipehandler.keyTyped(this, c, i, recipe))
                return;
        }
        if(i == mc.gameSettings.keyBindInventory.keyCode)
        {
            firstGui.refresh();
            mc.displayGuiScreen(firstGui);
        }
        else if(i == NEIClientConfig.getKeyBinding("gui.back"))
        {
            firstGui.refresh();
            mc.displayGuiScreen(prevGui);
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        IRecipeHandler recipehandler = currenthandlers.get(recipetype);
        for(int recipe = page * recipehandler.recipiesPerPage(); recipe < recipehandler.numRecipes() && recipe < (page + 1) * recipehandler.recipiesPerPage(); recipe++)
        {
            if(recipehandler.mouseClicked(this, par3, recipe))
                return;
        }
        super.mouseClicked(par1, par2, par3);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        super.actionPerformed(guibutton);
        switch(guibutton.id)
        {
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
    public void updateScreen()
    {
        super.updateScreen();
        currenthandlers.get(recipetype).onUpdate();
        refreshPage();
    }
    
    @Override
    public List<String> handleTooltip(int mousex, int mousey, List<String> currenttip)
    {
        IRecipeHandler recipehandler = currenthandlers.get(recipetype);
        for(int i = page * recipehandler.recipiesPerPage(); i < recipehandler.numRecipes() && i < (page + 1) * recipehandler.recipiesPerPage(); i++)
        {
            currenttip = recipehandler.handleTooltip(this, currenttip, i);
        }
        return currenttip;
    }
    
    @Override
    public List<String> handleItemTooltip(ItemStack stack, int mousex, int mousey, List<String> currenttip)
    {
        IRecipeHandler recipehandler = currenthandlers.get(recipetype);
        for(int i = page * recipehandler.recipiesPerPage(); i < recipehandler.numRecipes() && i < (page + 1) * recipehandler.recipiesPerPage(); i++)
        {
            currenttip = recipehandler.handleItemTooltip(this, stack, currenttip, i);
        }
        return currenttip;
    }
    
    private void nextPage()
    {
        page++;
        if(page > (currenthandlers.get(recipetype).numRecipes()-1) / currenthandlers.get(recipetype).recipiesPerPage())
        {
            page = 0;
        }
    }
    
    private void prevPage()
    {
        page--;
        if(page < 0 )
        {
            page = (currenthandlers.get(recipetype).numRecipes()-1) / currenthandlers.get(recipetype).recipiesPerPage();
        }
    }
    
    private void nextType()
    {
        recipetype++;
        if(recipetype >= currenthandlers.size())
        {
            recipetype = 0;
        }
        page = 0;
    }
    
    private void prevType()
    {
        recipetype--;
        if(recipetype < 0)
        {
            recipetype = currenthandlers.size() - 1;
        }
        page = 0;
    }
    
    private void overlayRecipe(int recipe)
    {
        IRecipeOverlayRenderer renderer = currenthandlers.get(recipetype).getOverlayRenderer(firstGui, recipe);
        IOverlayHandler handler = currenthandlers.get(recipetype).getOverlayHandler(firstGui, recipe);
        boolean shift = NEIClientUtils.shiftKey();
        
        if(handler != null && (renderer == null || shift))
        {
            firstGui.refresh();
            mc.displayGuiScreen(firstGui);
            handler.overlayRecipe(firstGui, currenthandlers.get(recipetype), recipe, shift);
        }
        else if(renderer != null && (handler == null || !shift))
        {            
            firstGui.refresh();
            mc.displayGuiScreen(firstGui);
            LayoutManager.overlayRenderer = renderer;
        }
    }

    public void refreshPage()
    {
        refreshSlots();
        
        IRecipeHandler handler = currenthandlers.get(recipetype);
        boolean multiplepages = handler.numRecipes() > handler.recipiesPerPage();
        nextpage.drawButton = multiplepages;
        prevpage.drawButton = multiplepages;

        GuiContainer mainInv = getFirstScreen();
        
        overlay1.yPosition = (height-ySize)/2 + (handler.recipiesPerPage() == 2 ? 63 : 128);
        overlay1.drawButton = handler.hasOverlay(mainInv, mainInv.inventorySlots, page * handler.recipiesPerPage());
        overlay2.drawButton = handler.recipiesPerPage() == 2 && page * handler.recipiesPerPage() + 1 < handler.numRecipes() && 
                handler.hasOverlay(mainInv, mainInv.inventorySlots, page * handler.recipiesPerPage() + 1);
    }
    
    private void refreshSlots()
    {
        slotcontainer.inventorySlots.clear();
        IRecipeHandler recipehandler = currenthandlers.get(recipetype);
        for(int i = page * recipehandler.recipiesPerPage(); i < recipehandler.numRecipes() && i < (page + 1) * recipehandler.recipiesPerPage(); i++)
        {
            Point p = getRecipePosition(i);
            
            List<PositionedStack> stacks = recipehandler.getIngredientStacks(i);
            for(PositionedStack stack : stacks)
            {
                slotcontainer.addSlot(stack, p.x, p.y);
            }
            stacks = recipehandler.getOtherStacks(i);
            for(PositionedStack stack : stacks)
            {
                slotcontainer.addSlot(stack, p.x, p.y);
            }
            PositionedStack result = recipehandler.getResultStack(i);
            if(result != null)
                slotcontainer.addSlot(result, p.x, p.y);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        GuiContainerManager.enable2DRender();
        
        IRecipeHandler recipehandler = currenthandlers.get(recipetype);
        String s = recipehandler.getRecipeName();
        fontRenderer.drawString(s, (xSize - fontRenderer.getStringWidth(s)) / 2, 5, 0x404040);
        s = NEIClientUtils.translate("recipe.page", page+1, (currenthandlers.get(recipetype).numRecipes()-1) / recipehandler.recipiesPerPage() + 1);
        fontRenderer.drawString(s, (xSize - fontRenderer.getStringWidth(s)) / 2, ySize - 16, 0x404040);
        
        GL11.glPushMatrix();
        GL11.glTranslatef(5, 16, 0);
        for(int i = page * recipehandler.recipiesPerPage(); i < recipehandler.numRecipes() && i < (page + 1) * recipehandler.recipiesPerPage(); i++)
        {
            recipehandler.drawForeground(i);
            GL11.glTranslatef(0, 65, 0);
        }
        GL11.glPopMatrix();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
    {
        GL11.glColor4f(1, 1, 1, 1);
        CCRenderState.changeTexture("nei:textures/gui/recipebg.png");
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        
        GL11.glPushMatrix();
        GL11.glTranslatef(j + 5, k + 16, 0);
        IRecipeHandler recipehandler = currenthandlers.get(recipetype);
        for(int i = page * recipehandler.recipiesPerPage(); i < recipehandler.numRecipes() && i < (page + 1) * recipehandler.recipiesPerPage(); i++)
        {
            recipehandler.drawBackground(i);
            GL11.glTranslatef(0, 65, 0);
        }
        GL11.glPopMatrix();
    }

    @Override
    public GuiContainer getFirstScreen()
    {
        return firstGui;
    }

    public boolean isMouseOver(PositionedStack stack, int recipe)
    {
        Slot stackSlot = slotcontainer.getSlotWithStack(stack, getRecipePosition(recipe).x, getRecipePosition(recipe).y);
        Point mousepos = GuiDraw.getMousePosition();
        Slot mouseoverSlot = getSlotAtPosition(mousepos.x, mousepos.y);
        
        return stackSlot == mouseoverSlot;
    }

    public Point getRecipePosition(int recipe)
    {
        return new Point(5, 16 + (recipe % currenthandlers.get(recipetype).recipiesPerPage()) * 65);
    }
    
    @Override
    public void mouseScrolled(int i)
    {
        if(new Rectangle(guiLeft, guiTop, xSize, ySize).contains(GuiDraw.getMousePosition()))
        {
            if(i > 0)
                prevPage();
            else
                nextPage();
        }
    }
    
    public abstract ArrayList<? extends IRecipeHandler> getCurrentRecipeHandlers();
    
    public int page;
    public int recipetype;
    public ContainerRecipe slotcontainer;
    public GuiContainer firstGui;
    public GuiContainer prevGui;
    public GuiButton nextpage;
    public GuiButton prevpage;
    public GuiButton overlay1;
    public GuiButton overlay2;
    
    public ArrayList<? extends IRecipeHandler> currenthandlers = new ArrayList<IRecipeHandler>();
}
