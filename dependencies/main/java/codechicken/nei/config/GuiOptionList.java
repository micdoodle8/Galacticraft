package codechicken.nei.config;

import codechicken.core.gui.GuiCCButton;
import codechicken.core.gui.GuiScreenWidget;
import codechicken.core.gui.GuiScrollSlot;
import codechicken.lib.vec.Rectangle4i;
import codechicken.nei.LayoutManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static codechicken.lib.gui.GuiDraw.*;
import static net.minecraft.util.StatCollector.translateToLocal;

public class GuiOptionList extends GuiScreenWidget
{
    public class OptionScrollSlot extends GuiScrollSlot
    {
        public ArrayList<Option> options = new ArrayList<Option>();
        
        public OptionScrollSlot()
        {
            super(0, 0, 0, 0);
        }
        
        @Override
        public void onAdded(GuiScreen s)
        {
            super.onAdded(s);
            for(Option o : optionList.optionList)
                options.add(o);
            for(Option o : options)
                o.onAdded(this);
        }

        @Override
        public int getSlotHeight()
        {
            return 24;
        }
        
        public int contentWidth()
        {
            return width-20;
        }

        @Override
        protected int getNumSlots()
        {
            return options.size();
        }

        @Override
        public void selectNext()
        {
        }

        @Override
        public void selectPrev()
        {
        }

        @Override
        protected boolean isSlotSelected(int slot)
        {
            return false;
        }

        @Override
        protected void drawSlot(int slot, int x, int y, int mousex, int mousey, boolean selected, float frame)
        {
            GL11.glTranslatef(x, y, 0);
            Option o = options.get(slot);
            if(o.showWorldSelector() && world)
                drawWorldSelector(o, mousex, mousey);
            o.draw(mousex, mousey, frame);
            GL11.glTranslatef(-x, -y, 0);
        }
        
        public Rectangle4i worldButtonSize()
        {
            return new Rectangle4i(-24, 2, 20, 20);
        }
        
        private void drawWorldSelector(Option o, int mousex, int mousey)
        {
            Rectangle4i b = worldButtonSize();
            boolean set = o.hasWorldOverride();
            boolean mouseover = b.contains(mousex, mousey);
            GL11.glColor4f(1, 1, 1, 1);
            LayoutManager.drawButtonBackground(b.x, b.y, b.w, b.h, true, !set ? 0 : mouseover ? 2 : 1);
            drawStringC("W", b.x, b.y, b.w, b.h, -1);
        }

        @Override
        public void drawOverlay(float frame)
        {
            drawOverlayTex(0, 0, parentScreen.width, y);
            drawOverlayTex(0, y+height, parentScreen.width, parentScreen.height-y-height);
            drawOverlayGrad(0, parentScreen.width, y, y+4);
            drawOverlayGrad(0, parentScreen.width, y+height, y+height-4);
        }
        
        public void drawOverlayTex(int x, int y, int w, int h)
        {
            GL11.glColor4f(1, 1, 1, 1);
            renderEngine.bindTexture(Gui.optionsBackground);
            Tessellator t = Tessellator.instance;
            t.startDrawingQuads();
            t.addVertexWithUV(x, y, zLevel, 0, 0);
            t.addVertexWithUV(x, y+h, zLevel, 0, h/16D);
            t.addVertexWithUV(x+w, y+h, zLevel, w/16D, h/16D);
            t.addVertexWithUV(x+w, y, zLevel, w/16D, 0);
            t.draw();
        }
        
        public void drawOverlayGrad(int x1, int x2, int y1, int y2)
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            Tessellator t = Tessellator.instance;
            t.startDrawingQuads();
            t.setColorRGBA_I(0, 255);
            t.addVertex(x2, y1, zLevel);
            t.addVertex(x1, y1, zLevel);
            t.setColorRGBA_I(0, 0);
            t.addVertex(x1, y2, zLevel);
            t.addVertex(x2, y2, zLevel);
            t.draw();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
        
        @Override
        public void drawSlotBox(float frame)
        {
        }
        
        @Override
        public boolean drawLineGuide()
        {
            return false;
        }
        
        @Override
        public int getScrollBarWidth()
        {
            return 6;
        }
        
        @Override
        public void drawScrollBar(float frame)
        {
            int sbarw = getScrollBarWidth();
            int sbarx = x + width - sbarw;
            drawRect(sbarx, y, sbarx+sbarw, y+height, 0xFF000000);
            
            super.drawScrollBar(frame);
        }
        
        @Override
        public void update()
        {
            super.update();
            for(Option o : options)
                o.update();
        }

        @Override
        protected void slotClicked(int slot, int button, int mousex, int mousey, boolean doubleclick)
        {
            options.get(slot).mouseClicked(mousex, mousey, button);
        }
        
        @Override
        public void mouseClicked(int mousex, int mousey, int button)
        {
            for(Option o : options)
                o.onMouseClicked(mousex, mousey, button);
            
            super.mouseClicked(mousex, mousey, button);
            
            int slot = getClickedSlot(mousey);
            if(slot >= 0)
            {
                Option o = options.get(slot);
                if(world && o.showWorldSelector() && worldButtonSize().contains(mousex-contentx, mousey-getSlotY(slot)))
                {
                    if(button == 1 && o.hasWorldOverride())
                    {
                        o.useGlobals();
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
                    }
                    else if(button == 0 && !o.hasWorldOverride())
                    {
                        o.copyGlobals();
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
                    }
                }
            }
        }
        
        @Override
        public void keyTyped(char c, int keycode)
        {
            super.keyTyped(c, keycode);
            for(Option o : options)
                o.keyTyped(c, keycode);
        }

        public void resize()
        {
            int width = Math.min(parentScreen.width-80, 320);
            setSize((parentScreen.width-width)/2, 20, width, parentScreen.height-50);
            setContentSize(x, y+4, height-8);
        }
        
        @Override
        public void mouseScrolled(int x, int y, int scroll)
        {
            scroll(-scroll);
        }

        public GuiOptionList getGui()
        {
            return ((GuiOptionList)parentScreen);
        }

        public List<String> handleTooltip(int mousex, int mousey, List<String> tooltip)
        {
            for(int slot = 0; slot < getNumSlots(); slot++)
            {
                int sloty = getSlotY(slot);
                if(sloty > contenty - getSlotHeight() && sloty < contenty + contentheight)
                    tooltip = handleTooltip(slot, mousex-contentx, mousey-sloty, tooltip);
            }
            return tooltip;
        }

        private List<String> handleTooltip(int slot, int mousex, int mousey, List<String> tooltip)
        {
            Option o = options.get(slot);
            if(world && o.showWorldSelector() && worldButtonSize().contains(mousex, mousey))
                tooltip.add(translateToLocal("nei.options.wbutton.tip."+(o.hasWorldOverride() ? "1" : "0")));
            
            return o.handleTooltip(mousex, mousey, tooltip);
        }
    }
    
    private final GuiScreen parent;
    private final OptionList optionList;
    private boolean world;
    
    private OptionScrollSlot slot;
    private GuiCCButton backButton;
    private GuiCCButton worldButton;
    
    public GuiOptionList(GuiScreen parent, OptionList optionList, boolean world)
    {
        this.parent = parent;
        this.optionList = optionList;
        this.world = world;
    }
    
    @Override
    public void initGui()
    {
        xSize = width;
        ySize = height;
        super.initGui();
        
        if(slot != null)
        {
            slot.resize();
            backButton.width = Math.min(200, width-40);
            backButton.x = (width-backButton.width)/2;
            backButton.y = height-25;
            worldButton.width = 60;
            worldButton.x = width-worldButton.width-15;
        }
    }
    
    @Override
    public void addWidgets()
    {
        add(slot = new OptionScrollSlot());
        add(backButton = new GuiCCButton(0, 0, 0, 20, translateToLocal("nei.options.back")).setActionCommand("back"));
        add(worldButton = new GuiCCButton(0, 2, 0, 16, worldButtonName()).setActionCommand("world"));
        initGui();
    }
    
    private String worldButtonName()
    {
        return translateToLocal("nei.options." + (world ? "world" : "global"));
    }

    @Override
    public void actionPerformed(String ident, Object... params)
    {
        if(ident.equals("back"))
        {
            if(parent instanceof GuiOptionList)
                ((GuiOptionList)parent).world = world;
            Minecraft.getMinecraft().displayGuiScreen(parent);
        }
        else if(ident.equals("world"))
        {
            world = !world;
            worldButton.text = worldButtonName();
        }
    }
    
    @Override
    public void drawBackground()
    {
        drawDefaultBackground();
    }
    
    @Override
    public void drawForeground()
    {
        drawCenteredString(fontRenderer, translateToLocal(optionList.fullName()), width/2, 6, -1);
        drawTooltip();
    }
    
    private void drawTooltip()
    {
        List<String> tooltip = new LinkedList<String>();
        Point mouse = getMousePosition();
        if(worldButton.pointInside(mouse.x, mouse.y))
            tooltip.addAll(Arrays.asList(translateToLocal("nei.options.global.tip." + (world ? "1" : "0")).split(":")));
        
        tooltip = slot.handleTooltip(mouse.x, mouse.y, tooltip);
        drawMultilineTip(mouse.x+12, mouse.y-12, tooltip);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
    
    @Override
    public void keyTyped(char c, int keycode)
    {
        if (keycode == 1)//esc
        {
            GuiScreen p = parent;
            while(p instanceof GuiOptionList)
                p = ((GuiOptionList)p).parent;

            Minecraft.getMinecraft().displayGuiScreen(p);
        }
        else
        {
            super.keyTyped(c, keycode);
        }
    }

    public boolean worldConfig()
    {
        return world;
    }
}
