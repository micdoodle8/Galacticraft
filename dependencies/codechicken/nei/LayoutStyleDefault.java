package codechicken.nei;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.nbt.NBTTagCompound;
import codechicken.nei.api.LayoutStyle;

import static codechicken.nei.LayoutManager.*;
import static codechicken.nei.NEIClientConfig.*;
import static codechicken.core.gui.GuiDraw.*;

public abstract class LayoutStyleDefault extends LayoutStyle
{    
    @Override
    public void layout(GuiContainer gui, VisiblityData visiblity)
    {
        int windowWidth = gui.width;
        int windowHeight = gui.height;
        int containerWidth = gui.xSize;
        int containerLeft = gui.guiLeft;
        
        reset();
        
        prev.y = 2;
        prev.height = 16;
        prev.width = containerLeft/3;
        prev.x = (containerWidth + windowWidth) / 2 + 2;
        next.x = windowWidth-prev.width-2;
        
        next.y = prev.y;
        next.width = prev.width;
        next.height = prev.height;
        pageLabel.x = containerLeft*3/2+containerWidth+1;
        pageLabel.y = prev.y+5;
        if(itemPanel.getNumPages() == 0)
            pageLabel.text = "(0/0)";
        else
            pageLabel.text = "("+(itemPanel.getPage()+1)+"/"+itemPanel.getNumPages()+")";
        
        itemPanel.y = prev.height+prev.y;
        itemPanel.x = (containerWidth + windowWidth) / 2 + 3;
        itemPanel.width = windowWidth - 3 - itemPanel.x;
        itemPanel.height = windowHeight - 15 - itemPanel.y;
        if(!canPerformAction("item"))
            itemPanel.height+=15;
        itemPanel.resize();
        
        more.width = more.height = less.width = less.height = 16;
        less.x = prev.x;
        more.x = windowWidth-less.width-2;
        more.y = less.y = windowHeight-more.height-2;
        
        quantity.x = less.x+less.width+2;
        quantity.y = less.y;
        quantity.width = more.x-quantity.x-2;
        quantity.height = less.height;
                
        options.x = isEnabled() ? 0 : 6;
        options.y = isEnabled() ? windowHeight - 22 : windowHeight - 28;
        options.width = 80;
        options.height = 22;

        delete.state = 0x4;
        if(NEIController.deleteMode)
            delete.state |= 1;
        else if(!visiblity.enableDeleteMode)
            delete.state |= 2;
        
        rain.state = 0x4;
        if(disabledActions.contains("rain"))
            rain.state |= 2;
        else if(NEIClientUtils.isRaining())
            rain.state |= 1;
        
        gamemode.state = 0x4;
        if(NEIClientUtils.getGamemode() != 0)
        {
            gamemode.state |= 0x1;
            gamemode.index = NEIClientUtils.getGamemode()-1;
        }
        else
        {
            if(NEIClientUtils.isValidGamemode("creative"))
                gamemode.index = 0;
            else if(NEIClientUtils.isValidGamemode("creative+"))
                gamemode.index = 1;
            else if(NEIClientUtils.isValidGamemode("adventure"))
                gamemode.index = 2;
        }
        
        magnet.state = 0x4 | (getMagnetMode() ? 1 : 0);
        
        if(canPerformAction("delete"))
            layoutButton(delete);
        if(canPerformAction("rain"))
            layoutButton(rain);
        if(NEIClientUtils.isValidGamemode("creative") || 
                NEIClientUtils.isValidGamemode("creative+") || 
                NEIClientUtils.isValidGamemode("adventure"))
            layoutButton(gamemode);
        if(canPerformAction("magnet"))
            layoutButton(magnet);
        if(canPerformAction("time"))
        {
            for(int i = 0; i < 4; i++)
            {
                timeButtons[i].state = disabledActions.contains(NEIActions.timeZones[i]) ? 2 : 0;
                layoutButton(timeButtons[i]);
            }
        }
        if(canPerformAction("heal"))
            layoutButton(heal);
        
        searchField.y = windowHeight - searchField.height - 2;
                
        dropDown.height = 20;
        dropDown.width = prev.x - dropDown.x - 3;
        searchField.height = 20;
        searchField.width = 150;
        searchField.x = (windowWidth - searchField.width) / 2;
        
        if(!visiblity.showItemSection)
        {
            dropDown.setDropDown(0);
            searchField.setFocus(false);
        }
        
        int maxWidth = 0;
        for(int i = 0; i < 7; i++)
        {
            deleteButtons[i].width = 16;
            deleteButtons[i].height = 16;
            
            NBTTagCompound statelist = global.nbt.getCompoundTag("statename");
            global.nbt.setTag("statename", statelist);
            String name = statelist.getString(""+i);
            if(statelist.getTag(""+i) == null)
            {
                name = ""+(i+1);
                statelist.setString(""+i, name);
            }
            stateButtons[i].label = name;
            stateButtons[i].saved = isStateSaved(i);
            
            int width = getStringWidth(stateButtons[i].getRenderLabel()) + 26;
            if(width + 22 > containerLeft)
                width = containerLeft - 22;
            
            if(width > maxWidth)
                maxWidth = width;
        }

        for(int i = 0; i < 7; i++)
        {
            stateButtons[i].x = 0;
            stateButtons[i].y = 58 + i * 22;
            stateButtons[i].height = 20;
            
            stateButtons[i].x = 0;
            stateButtons[i].width = maxWidth;
            deleteButtons[i].x = stateButtons[i].width + 3;
            deleteButtons[i].y = stateButtons[i].y + 2;
        }
    }

    public abstract void layoutButton(Button button);
}
