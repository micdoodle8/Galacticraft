package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Achievement;
import net.minecraft.src.AchievementList;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSmallButton;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderItem;
import net.minecraft.src.StatCollector;
import net.minecraft.src.StatFileWriter;
import net.minecraft.src.Tessellator;
import net.minecraftforge.common.AchievementPage;

import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class GCCoreGuiGalaxyMap extends GuiScreen
{
    /** The top x coordinate of the achievement map */
    private static final int guiMapTop = -200 - 112;

    /** The left y coordinate of the achievement map */
    private static final int guiMapLeft = -200 - 112;

    /** The bottom x coordinate of the achievement map */
    private static final int guiMapBottom = 200 - 77;

    /** The right y coordinate of the achievement map */
    private static final int guiMapRight = 50 - 77;
    protected int achievementsPaneWidth;
    protected int achievementsPaneHeight = 202;

    /** The current mouse x coordinate */
    protected float mouseX = 0;

    /** The current mouse y coordinate */
    protected float mouseY = 0;
    
    protected double field_74117_m;
    protected double field_74115_n;

    /** The x position of the achievement map */
    protected double guiMapX;

    /** The y position of the achievement map */
    protected double guiMapY;
    protected double field_74124_q;
    protected double field_74123_r;

    /** Whether the Mouse Button is down or not */
    private int isMouseButtonDown = 0;
    private StatFileWriter statFileWriter;
    
    private int currentPage = -1;
    private GuiSmallButton button;
    private LinkedList<Achievement> minecraftAchievements = new LinkedList<Achievement>();
    
    private float zoom = 1.0F;

    public GCCoreGuiGalaxyMap(EntityPlayer player)
    {
        this.statFileWriter = FMLClientHandler.instance().getClient().statFileWriter;
        short var2 = 141;
        short var3 = 141;
        this.field_74117_m = this.guiMapX = this.field_74124_q = (double)(AchievementList.openInventory.displayColumn * 24 - var2 / 2 - 12);
        this.field_74115_n = this.guiMapY = this.field_74123_r = (double)(AchievementList.openInventory.displayRow * 24 - var3 / 2);
        minecraftAchievements.clear();
        for (Object achievement : AchievementList.achievementList)
        {
            if (!AchievementPage.isAchievementInPages((Achievement)achievement))
            {
                minecraftAchievements.add((Achievement)achievement);
            }
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
    	this.achievementsPaneWidth = this.width;
        this.achievementsPaneHeight = this.height;
        this.controlList.clear();
        this.controlList.add(new GuiSmallButton(1, this.width - 90, this.height - 22, 80, 20, StatCollector.translateToLocal("gui.done")));
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.id == 1)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }

        if (par1GuiButton.id == 2) 
        {
            currentPage++;
            if (currentPage >= AchievementPage.getAchievementPages().size())
            {
                currentPage = -1;
            }
            button.displayString = AchievementPage.getTitle(currentPage);
        }

        super.actionPerformed(par1GuiButton);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (par2 == this.mc.gameSettings.keyBindInventory.keyCode)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
        else
        {
            super.keyTyped(par1, par2);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
    	this.achievementsPaneWidth = this.width;
        this.achievementsPaneHeight = this.height;
        
        while (Mouse.next())
        {
        	float wheel = Mouse.getEventDWheel();
            
            if (Mouse.hasWheel() && wheel != 0)
            {
            	wheel /= 1000F;
            	
            	FMLLog.info("" + wheel);
            	
            	this.zoom = this.zoom + wheel;

            	if (this.zoom >= 2)
            	{
            		this.zoom = 2;
            	}
            	else if (this.zoom <= 1F)
            	{
            		this.zoom = 1F;
            	}
            }
        }
    	
//        if (Mouse.isButtonDown(0))
        {
            int var4 = (this.width - this.achievementsPaneWidth) / 2;
            int var5 = (this.height - this.achievementsPaneHeight) / 2;
            int var6 = var4 + 8;
            int var7 = var5 + 17;
            
        	if (Mouse.getY() < this.height)
        	{
        		this.mouseY = ((-this.height + Mouse.getY()) / 100F);
        	}
        	else
        	{
        		this.mouseY = ((-this.height + Mouse.getY()) / 100F);
        	}
        	
        	if (Mouse.getX() > this.width - 50 && Mouse.getX() < this.width + 50)
        	{
        		this.mouseX = 0;
        	}
        	
        	if (Mouse.getY() > this.height - 50 && Mouse.getY() < this.height + 50)
        	{
        		this.mouseY = 0;
        	}
            
            this.mouseX -= Mouse.getDX() / 100F;

            if (this.field_74124_q < (double)guiMapTop)
            {
                this.field_74124_q = (double)guiMapTop;
                
                if (mouseX > 0)
                {
                    this.mouseX = 0;
                }
            }

            if (this.field_74123_r < (double)guiMapLeft)
            {
                this.field_74123_r = (double)guiMapLeft;
                
                if (mouseY > 0)
                {
                    this.mouseY = 0;
                }
            }

            if (this.field_74124_q >= (double)guiMapBottom)
            {
                this.field_74124_q = (double)(guiMapBottom - 1);
                
                if (mouseX < 0)
                {
                    this.mouseX = 0;
                }
            }

            if (this.field_74123_r >= (double)guiMapRight)
            {
                this.field_74123_r = (double)(guiMapRight - 1);
                
                if (mouseY < 0)
                {
                    this.mouseY = 0;
                }
            }
            
            FMLLog.info("" + this.mouseX);
            
            this.guiMapX -= (double)(this.mouseX);
            this.guiMapY -= (double)(this.mouseY);
            
//            if (this.guiMapY < (double)guiMapLeft && this.guiMapY >= (double)guiMapRight)
            {
                this.field_74124_q = this.field_74117_m = this.guiMapX;
                this.field_74123_r = this.field_74115_n = this.guiMapY;
            }
        }
//        else
        {
//            this.isMouseButtonDown = 0;
        }

        this.drawDefaultBackground();
        this.genAchievementBackground(par1, par2, par3);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        this.drawTitle();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.field_74117_m = this.guiMapX;
        this.field_74115_n = this.guiMapY;
        double var1 = this.field_74124_q - this.guiMapX;
        double var3 = this.field_74123_r - this.guiMapY;

//        if (var1 * var1 + var3 * var3 < 4.0D)
//        {
//        	FMLLog.info("5555");
//            this.guiMapX += var1;
//            this.guiMapY += var3;
//        }
//        else
        {
//            this.guiMapX += var1 * 0.85D;
//            this.guiMapY += var3 * 0.85D;
        }
    }

    /**
     * Draws the "Achievements" title at the top of the GUI.
     */
    protected void drawTitle()
    {
        int var1 = (this.width - this.achievementsPaneWidth) / 2;
        int var2 = (this.height - this.achievementsPaneHeight) / 2;
        this.fontRenderer.drawString("Achievements", var1 + 15, var2 + 5, 4210752);
    }

    protected void genAchievementBackground(int par1, int par2, float par3)
    {
        int var4 = MathHelper.floor_double(this.field_74117_m + (this.guiMapX - this.field_74117_m) * (double)par3);
        int var5 = MathHelper.floor_double(this.field_74115_n + (this.guiMapY - this.field_74115_n) * (double)par3);

        if (var4 < guiMapTop)
        {
            var4 = guiMapTop;
        }

        if (var5 < guiMapLeft)
        {
            var5 = guiMapLeft;
        }

        if (var4 >= guiMapBottom)
        {
            var4 = guiMapBottom - 1;
        }

        if (var5 >= guiMapRight)
        {
            var5 = guiMapRight - 1;
        }

        int var6 = this.mc.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/gui/mapbg.png");
//        int var7 = this.mc.renderEngine.getTexture("/achievement/bg.png");
        int var8 = (this.width - this.achievementsPaneWidth) / 2;
        int var9 = (this.height - this.achievementsPaneHeight) / 2;
        int var10 = var8;
        int var11 = var9;
        this.zLevel = 0.0F;
        GL11.glDepthFunc(GL11.GL_GEQUAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(Mouse.getX() / 2, Mouse.getY() / 2, 0);
        GL11.glScalef(this.zoom, this.zoom, this.zoom);
        GL11.glTranslatef(-Mouse.getX() / 2, -Mouse.getY() / 2, 0);
        GL11.glTranslatef(0.0F, 0.0F, -200.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        this.mc.renderEngine.bindTexture(var6);
        int var12 = var4 + 288 >> 4;
        int var13 = var5 + 288 >> 4;
        int var14 = (var4 + 288) % 16;
        int var15 = (var5 + 288) % 16;
        Random var21 = new Random();
        int var22;
        int var25;
        int var24;
        int var26;

        for (var22 = 0; var22 * 16 - var15 < this.height; ++var22)
        {
            float var23 = 0.6F - (float)(var13 + var22) / 25.0F * 0.3F;
            GL11.glColor4f(var23, var23, var23, 1.0F);

            for (var24 = 0; var24 * 16 - var14 < this.width; ++var24)
            {
                var21.setSeed((long)(System.currentTimeMillis()));
                var21.nextInt();
                var25 = var21.nextInt(16);
                var26 = var21.nextInt(16);

                this.drawTexturedModalRect(var10 + var24 * 16 - var14, var11 + var22 * 16 - var15, (var25 % 16) * 16, 0, 16, 16);
            }
        }

        int var27;
        int var30;

        Achievement var32 = null;
        RenderItem var37 = new RenderItem();
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        int var42;
        int var41;
        
        int test = -1;

        for (var24 = 0; var24 < GalacticraftCore.clientSubMods.size(); ++var24)
        {
            IGalacticraftSubModClient client = GalacticraftCore.clientSubMods.get(var24);
            var26 = 0;
            var27 = 0;
            
            if (var24 == 0)
            {
            	var26 = MathHelper.floor_float(0F * (MathHelper.cos(((((Sys.getTime() / 100) % 10000)) / 1590F)))) - var4;
                var27 = MathHelper.floor_float(0F * (MathHelper.sin(((((Sys.getTime() / 100) % 10000)) / 1590F)))) - var5;
            }
            else if (var24 == 1)
            {
            	var26 = MathHelper.floor_float(70F * (MathHelper.cos(((((Sys.getTime() / 100) % 10000)) / 1590F)))) - var4;
                var27 = MathHelper.floor_float(70F * (MathHelper.sin(((((Sys.getTime() / 100) % 10000)) / 1590F)))) - var5;
            }
            else if (!client.isMoon())
            {
            	var26 = MathHelper.floor_float(client.getDistanceFromCenter() * (MathHelper.cos((( (client.getStretchValue()) * ((Sys.getTime() / 100) % 10000)) / 1590F)))) - var4;
                var27 = MathHelper.floor_float(client.getDistanceFromCenter() * (MathHelper.sin((( (client.getStretchValue()) * ((Sys.getTime() / 100) % 10000)) / 1590F)))) - var5;
            }
//            var26 = MathHelper.floor_float(client.getDistanceFromCenter() * (MathHelper.cos((((System.currentTimeMillis()) % 10000) / 1590F)))) - var4;
//            var27 = MathHelper.floor_float(client.getDistanceFromCenter() * (MathHelper.sin((((System.currentTimeMillis()) % 10000) / 1590F)))) - var5;
//            var26 = var35.displayColumn * 24 - var4;
//            var27 = var35.displayRow * 24 - var5;

//            if (var26 >= -24 && var27 >= -24 && var26 <= this.width - 24 && var27 <= this.height - 24)
            {
                float var38;

//                if (this.statFileWriter.hasAchievementUnlocked(var35))
//                {
//                    var38 = 1.0F;
//                    GL11.glColor4f(var38, var38, var38, 1.0F);
//                }
//                else if (this.statFileWriter.canUnlockAchievement(var35))
//                {
//                    var38 = Math.sin((double)(Minecraft.getSystemTime() % 600L) / 600.0D * Math.PI * 2.0D) < 0.6D ? 0.6F : 0.8F;
//                    GL11.glColor4f(var38, var38, var38, 1.0F);
//                }
//                else
//                {
//                    var38 = 0.3F;
//                    GL11.glColor4f(var38, var38, var38, 1.0F);
//                }

                var42 = var10 + var26;
                var41 = var11 + var27;
                
//                var42 *= this.zoom;
//                var41 *= this.zoom;

//                if (var35.getSpecial())
                {
//                    this.drawTexturedModalRect(var42 - 2, var41 - 2, 26, 202, 26, 26);
                }
//                else
                {
//                    this.drawTexturedModalRect(var42 - 2, var41 - 2, 0, 202, 26, 26);
                }

//                if (!this.statFileWriter.canUnlockAchievement(var35))
                {
                    float var40 = 0.1F;
                    GL11.glColor4f(var40, var40, var40, 1.0F);
                    var37.field_77024_a = false;
                }
                
                IPlanetSlotRenderer renderer = client.getSlotRenderer();
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                
                Tessellator var3 = Tessellator.instance;
                
                if (var24 == 0)
                {
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, FMLClientHandler.instance().getClient().renderEngine.getTexture("/terrain/sun.png"));

                    var3.startDrawingQuads();
                    var3.addVertexWithUV(var42 - 45, 	var41 + 45, 	-90.0D, 0.0, 1.0);
                    var3.addVertexWithUV(var42 + 45, 	var41 + 45, 	-90.0D, 1.0, 1.0);
                    var3.addVertexWithUV(var42 + 45, 	var41 - 45, 			-90.0D, 1.0, 0.0);
                    var3.addVertexWithUV(var42 - 45, 	var41 - 45, 			-90.0D, 0.0, 0.0);
                    var3.draw();
                }
                else if (var24 == 1)
                {
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, FMLClientHandler.instance().getClient().renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/planets/overworld.png"));

                    var3.startDrawingQuads();
                    var3.addVertexWithUV(var42 - 9 * 0.9, 	var41 + 9 * 0.9, 	-90.0D, 0.0, 1.0);
                    var3.addVertexWithUV(var42 + 9 * 0.9, 	var41 + 9 * 0.9, 	-90.0D, 1.0, 1.0);
                    var3.addVertexWithUV(var42 + 9 * 0.9, 	var41 - 9 * 0.9, 	-90.0D, 1.0, 0.0);
                    var3.addVertexWithUV(var42 - 9 * 0.9, 	var41 - 9 * 0.9, 	-90.0D, 0.0, 0.0);
                    var3.draw();
                }
                else if (!client.isMoon())
                {
                    this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture(renderer.getPlanetSprite()));
                    renderer.renderSlot(0, var42, var41, client.getPlanetSize(), var3);
                }
                
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

//                if (!this.statFileWriter.canUnlockAchievement(var35))
                {
                    var37.field_77024_a = true;
                }

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                
                int size = -1;
                
                if (var24 == 0)
                {
                	size = MathHelper.floor_float(20 / 2) + 2;
                }
                else if (var24 == 1)
                {
                	size = MathHelper.floor_float(12 / 2) + 2;
                }
                else if (!client.isMoon())
                {
                	size = MathHelper.floor_float(client.getPlanetSize() / 2) + 2;
                }
                
                if (par1 >= var10 && par2 >= var11 && par1 < var10 + this.width && par2 < var11 + this.height && par1 >= var42 - size && par1 <= var42 + size && par2 >= var41 - size && par2 <= var41 + size)
                {
                	String var34 = "";
                	
                	if (var24 == 0)
                	{
                		var34 = "Sun";
                	}
                	else if (var24 == 1)
                	{
                		var34 = "Overworld";
                	}
                	else if (!client.isMoon())
                	{
                		var34 = client.getDimensionName();
                	}
                	
                    var26 = par1 + 12;
                    var27 = par2 - 4;

                    var42 = this.fontRenderer.getStringWidth(var34);

                    this.drawGradientRect(var26 - 3, var27 - 3, var26 + var42 + 3, var27 + 3 + 12, -1073741824, -1073741824);
                    this.fontRenderer.drawStringWithShadow(var34, var26, var27, -16777226);
                }
            }
        }
        
        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawBackground(0, 					0, 					this.width, 	20);
        this.drawBackground(0,	 				this.height - 24, 	this.width, 	this.height);
        this.drawBackground(0, 					0, 					10, 			this.height);
        this.drawBackground(this.width - 10, 	0, 					this.width, 	this.height);
        this.zLevel = 0.0F;
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        super.drawScreen(par1, par2, par3);

        if (test == 1)
        {
            
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        RenderHelper.disableStandardItemLighting();
    }
    
    public void drawBackground(int x1, int x2, int y1, int y2)
    {
    	FMLLog.info("" + x1 + " " + x2 + " " + y1 + " " + y2);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator var2 = Tessellator.instance;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var3 = 32.0F;
        var2.startDrawingQuads();
        var2.setColorOpaque_I(4210752);
//        var2.addVertexWithUV(x1, 					y2,					 	0.0D, 			0.0D, 									(double)((float)y2 / var3 + 0.0F));
//        var2.addVertexWithUV(x2, 					y2, 					0.0D, 			(double)((float)x2 / var3), 			(double)((float)y2 / var3 + 0.0F));
//        var2.addVertexWithUV(x2,	 				y1, 					0.0D, 			(double)((float)x2 / var3), 			0.0D);
//        var2.addVertexWithUV(x1, 					y1,	 					0.0D, 			0.0D, 									0.0D);

        var2.addVertex((double)x1, (double)y2, 0.0D);
        var2.addVertex((double)x2, (double)y2, 0.0D);
        var2.addVertex((double)x2, (double)y1, 0.0D);
        var2.addVertex((double)x1, (double)y1, 0.0D);
        var2.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return true;
    }
}
