package codechicken.nei;

import codechicken.lib.vec.Rectangle4i;
import codechicken.nei.api.GuiInfo;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static codechicken.lib.gui.GuiDraw.drawRect;

public class ItemPanel extends Widget
{
    public class ItemPanelSlot
    {
        public ItemPanelObject contents;
        public int slotIndex;
        
        public ItemPanelSlot(int index)
        {
            contents = visibleitems.get(index);
            slotIndex = index;
        }

        public ItemStack getItemStack()
        {
            return contents instanceof ItemPanelStack ? ((ItemPanelStack)contents).item : null;
        }
    }
    
    public static interface ItemPanelObject
    {
        void draw(int x, int y);
        List<String> handleTooltip(List<String> tooltip);    
    }

    public void resize()
    {
        marginLeft = x + (width % 18) / 2;
        marginTop = y + (height % 18) / 2;
        columns = width / 18;
        rows = height / 18;
        
        calculatePage();
        updateValidSlots();
    }

	private void calculatePage() {
    	if(itemsPerPage == 0)
    		numPages = 0;
    	else
            numPages = (int)Math.ceil((float)visibleitems.size() / (float)itemsPerPage);

        if(firstIndex >= visibleitems.size())
            firstIndex = 0;

    	if(numPages == 0)
    		page = 0;
    	else
    		page = firstIndex/itemsPerPage+1;
    }
    
    private void updateValidSlots() {
    	GuiContainer gui = NEIClientUtils.getGuiContainer();
		validSlotMap = new boolean[rows*columns];
		itemsPerPage = 0;
		for(int i = 0; i < validSlotMap.length; i++)
	        if(slotValid(gui, i)) {
	        	validSlotMap[i] = true;
	        	itemsPerPage++;
	        }
	}
    
    private boolean slotValid(GuiContainer gui, int i) {
    	Rectangle4i rect = getSlotRect(i);
    	for(INEIGuiHandler handler : GuiInfo.guiHandlers)
        	if(handler.hideItemPanelSlot(gui, rect.x, rect.y, rect.w, rect.h))
        		return false;
    	return true;
    }
    
    public Rectangle4i getSlotRect(int i) {
    	return getSlotRect(i/columns, i%columns);
    }
    
    public Rectangle4i getSlotRect(int row, int column) {
    	return new Rectangle4i(marginLeft+column*18, marginTop+row*18, 18, 18);
    }

    @Override
    public void draw(int mousex, int mousey)
    {
        if(itemsPerPage == 0)
            return;

        GuiContainerManager.enableMatrixStackLogging();
        int index = firstIndex;
        for(int i = 0; i < rows*columns && index < visibleitems.size(); i++) {
        	if(validSlotMap[i]) {
                Rectangle4i rect = getSlotRect(i);
                if(rect.contains(mousex, mousey))
                    drawRect(rect.x, rect.y, rect.w, rect.h, 0xee555555);//highlight
            	
            	visibleitems.get(index).draw(rect.x, rect.y);
                
                index++;
        	}
        }
        GuiContainerManager.disableMatrixStackLogging();
    }
    
    @Override
    public void postDraw(int mousex, int mousey)
    {       
        if(draggedStack != null)
        {
            GuiContainerManager.drawItems.zLevel += 100;
            GuiContainerManager.drawItem(mousex - 8, mousey - 8, draggedStack);
            GuiContainerManager.drawItems.zLevel -= 100;
        }
    }
    
    @Override
    public void mouseDragged(int mousex, int mousey, int button, long heldTime)
    {
        if(mouseDownSlot >= 0 && draggedStack == null && NEIClientUtils.getHeldItem() == null)
        {
            ItemPanelSlot mouseOverSlot = getSlotMouseOver(mousex, mousey);
            ItemStack stack = new ItemPanelSlot(mouseDownSlot).getItemStack();
            if(stack != null && (mouseOverSlot == null || mouseOverSlot.slotIndex != mouseDownSlot || heldTime > 500))
            {
                int amount = NEIClientConfig.getItemQuantity();
                if(amount == 0)
                    amount = stack.getMaxStackSize();
                
                draggedStack = NEIServerUtils.copyStack(stack, amount);
            }
        }
    }

    @Override
    public boolean handleClick(int mousex, int mousey, int button)
    {
        if(handleDraggedClick(mousex, mousey, button))
            return true;
        
        if(NEIClientUtils.getHeldItem() != null)
        {
        	for(INEIGuiHandler handler : GuiInfo.guiHandlers)
            	if(handler.hideItemPanelSlot(NEIClientUtils.getGuiContainer(), mousex, mousey, 1, 1))
        			return false;
        	
            if(NEIClientConfig.canPerformAction("delete") && NEIClientConfig.canPerformAction("item"))
            {
                if(button == 1)
                    NEIClientUtils.decreaseSlotStack(-999);
                else
                    NEIClientUtils.deleteHeldItem();
            }
            else
            {
                NEIClientUtils.dropHeldItem();
            }
            return true;
        }
        
        ItemPanelSlot hoverSlot = getSlotMouseOver(mousex, mousey);        
        if(hoverSlot != null)
        {
            if(button == 2)
            {
                ItemStack stack = hoverSlot.getItemStack();
                if(stack != null)
                {
                    int amount = NEIClientConfig.getItemQuantity();
                    if(amount == 0)
                        amount = stack.getMaxStackSize();
                    
                    draggedStack = NEIServerUtils.copyStack(stack, amount);
                }
            }
            else
            {
                mouseDownSlot = hoverSlot.slotIndex;
            }
            return true;
        }
        return false;
    }
    
    private boolean handleDraggedClick(int mousex, int mousey, int button)
    {
        if(draggedStack == null)
            return false;
    
        GuiContainer gui = NEIClientUtils.getGuiContainer();
        boolean handled = false;
        for(INEIGuiHandler handler : GuiInfo.guiHandlers)
            if(handler.handleDragNDrop(gui, mousex, mousey, draggedStack, button))
            {
                handled = true;
                if(draggedStack.stackSize == 0)
                {
                    draggedStack = null;
                    return true;
                }
            }
        
        if(handled)
            return true;
        
        Slot overSlot = gui.getSlotAtPosition(mousex, mousey);
        if(overSlot != null && overSlot.isItemValid(draggedStack))
        {
            if(NEIClientConfig.canPerformAction("item"))
            {
                int contents = overSlot.getHasStack() ? overSlot.getStack().stackSize : 0;
                int add = button == 0 ? draggedStack.stackSize : 1;
                if(overSlot.getHasStack() && !NEIServerUtils.areStacksSameType(draggedStack, overSlot.getStack()))
                    contents = 0;
                int total = Math.min(contents+add, Math.min(overSlot.getSlotStackLimit(), draggedStack.getMaxStackSize()));
                
                if(total > contents)
                {
                    NEIClientUtils.setSlotContents(overSlot.slotNumber, NEIServerUtils.copyStack(draggedStack, total), true);
                    NEICPH.sendGiveItem(NEIServerUtils.copyStack(draggedStack, total), false, false);
                    draggedStack.stackSize-=total-contents;
                }
                if(draggedStack.stackSize == 0)
                    draggedStack = null;
            }
            else
            {
                draggedStack = null;
            }
        }
        else if(mousex < gui.guiLeft || mousey < gui.guiTop || mousex >= gui.guiLeft+gui.xSize || mousey >= gui.guiTop+gui.ySize)
        {
            draggedStack = null;
        }
        
        return true;
    }
    
    @Override
    public boolean handleClickExt(int mousex, int mousey, int button)
    {
        return handleDraggedClick(mousex, mousey, button);
    }
    
    @Override
    public void mouseUp(int mousex, int mousey, int button)
    {
        ItemPanelSlot hoverSlot = getSlotMouseOver(mousex, mousey);        
        if(hoverSlot != null && hoverSlot.slotIndex == mouseDownSlot && hoverSlot.getItemStack() != null && draggedStack == null)
        {
            ItemStack item = hoverSlot.getItemStack();
            if(NEIController.manager.window instanceof GuiRecipe || !NEIClientConfig.canPerformAction("item"))
            {
                if(button == 0)
                    GuiCraftingRecipe.openRecipeGui("item", item);
                else if(button == 1)
                    GuiUsageRecipe.openRecipeGui("item", item);

                draggedStack = null;
                mouseDownSlot = -1;
                return;
            }
            
            NEIClientUtils.cheatItem(item, button, -1);
        }
        
        mouseDownSlot = -1;
    }

    @Override
    public boolean onMouseWheel(int i, int mousex, int mousey)
    {
        if(!contains(mousex, mousey))
            return false;
        
        scroll(-i);
        return true;
    }
    
    @Override
    public boolean handleKeyPress(int keyID, char keyChar)
    {
        if(keyID == NEIClientConfig.getKeyBinding("gui.next"))
        {
            scroll(1);
            return true;
        }
        if(keyID == NEIClientConfig.getKeyBinding("gui.prev"))
        {
            scroll(-1);
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getStackMouseOver(int mousex, int mousey)
    {
        ItemPanelSlot slot = getSlotMouseOver(mousex, mousey);
        return slot == null ? null : slot.getItemStack();
    }
    
    public ItemPanelSlot getSlotMouseOver(int mousex, int mousey)
    {
    	int index = firstIndex;
        for(int i = 0; i < rows*columns && index < visibleitems.size(); i++)
        	if(validSlotMap[i]) {
        		if(getSlotRect(i).contains(mousex, mousey))
        			return new ItemPanelSlot(index);
        		index++;
        	}
        
        return null;
    }
    
    @Override
    public List<String> handleTooltip(int mx, int my, List<String> tooltip)
    {
        if(getSlotMouseOver(mx, my) == null)
            return tooltip;
        
        return getSlotMouseOver(mx, my).contents.handleTooltip(tooltip);
    }
    
    public void scroll(int i)
    {
    	if(itemsPerPage != 0) {
    		int oldIndex = firstIndex;
	        firstIndex += i*itemsPerPage;
	        if(firstIndex >= visibleitems.size())
	        	firstIndex = 0;
	        if(firstIndex < 0)
	        	if(oldIndex > 0)
	        		firstIndex = 0;
	        	else
	        		firstIndex = (visibleitems.size()-1)/itemsPerPage*itemsPerPage;
	        
	    	calculatePage();
    	}
    }
    
    public int getPage()
    {
        return page;
    }
    
    public int getNumPages()
    {
        return numPages;
    }
    
    public static ArrayList<ItemPanelObject> visibleitems = new ArrayList<ItemPanelObject>();
    
    public ItemStack draggedStack;
    public int mouseDownSlot = -1;
    
    private int marginLeft;
    private int marginTop;
    private int rows;
    private int columns;
    
    private boolean[] validSlotMap;
    private int firstIndex;
    private int itemsPerPage;
    
    private int page;
    private int numPages;
}
