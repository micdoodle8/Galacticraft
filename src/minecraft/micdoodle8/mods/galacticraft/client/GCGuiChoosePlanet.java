package micdoodle8.mods.galacticraft.client;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import micdoodle8.mods.galacticraft.GCUtil;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSmallButton;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.Tessellator;
import net.minecraft.src.WorldProvider;
import net.minecraftforge.common.DimensionManager;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCGuiChoosePlanet extends GuiScreen
{
    private int spaceTimer = 0;
    
    private GCGuiChoosePlanetSlot planetSlots;
    
    private static final Random rand = new Random();

    private float updateCounter = 0.0F;
    
    public int selectedSlot;
    
    private WorldProvider oldProvider;
    
    protected FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRenderer;
    
    private String[] destinations;
    
    public EntityPlayer playerToSend;
    
    private boolean initialized;
    
    private GuiSmallButton sendButton;

    private static final String[] titlePanoramaPaths = new String[] {"/micdoodle8/mods/galacticraft/client/backgrounds/bg3.png"};
    
    public GCGuiChoosePlanet(EntityPlayer player)
    {
    	this.playerToSend = player;
    	
    	Integer[] ids = DimensionManager.getIDs();
    	
    	Set set = GCUtil.getArrayOfPossibleDimensions(ids).entrySet();
    	Iterator i = set.iterator();
    	
    	this.destinations = new String[set.size()];
    	
    	for (int k = 0; i.hasNext(); k++)
    	{
    		Map.Entry entry = (Map.Entry)i.next();
    		destinations[k] = (String) entry.getKey();
    	}
    }
    
    public void initGui()
    {
    	if (!this.initialized)
    	{
            this.planetSlots = new GCGuiChoosePlanetSlot(this);
            this.initialized = true;
    	}
    	else
    	{
            this.planetSlots.func_77207_a(2, 10, 10, 10);
    	}

        StringTranslate var1 = StringTranslate.getInstance();
        this.controlList.add(this.sendButton = new GuiSmallButton(1, this.width - 110, this.height - 26, 105, 20, "Send To Dimension"));
        this.planetSlots.registerScrollButtons(this.controlList, 2, 3);
    }
    
    public void updateScreen()
    {
        ++this.spaceTimer;
    }
    
    public boolean doesGuiPauseGame()
    {
        return false;
    }
    
    private void drawPanorama2(float par1)
    {
        Tessellator var4 = Tessellator.instance;
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GLU.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        byte var5 = 1;
        

        for (int var6 = 0; var6 < var5 * var5; ++var6)
        {
            GL11.glPushMatrix();
            float var7 = ((float)(var6 % var5) / (float)var5 - 0.5F) / 128.0F;
            float var8 = ((float)(var6 / var5) / (float)var5 - 0.5F) / 128.0F;
            float var9 = 0.0F;
            GL11.glTranslatef(var7, var8, var9 + 0.5F);
            GL11.glScalef(7F, 7F, 7F);

            for (int var10 = 0; var10 < 9; ++var10)
            {
                GL11.glPushMatrix();

                if (var10 == 1)
                {
                	GL11.glTranslatef(1.96F, 0.0F, 0.0F);
                }

                if (var10 == 2)
                {
                	GL11.glTranslatef(-1.96F, 0.0F, 0.0F);
                }

                if (var10 == 3)
                {
                	GL11.glTranslatef(0.0F, 1.96F, 0.0F);
                }

                if (var10 == 4)
                {
                	GL11.glTranslatef(0.0F, -1.96F, 0.0F);
                }

                if (var10 == 5)
                {
                	GL11.glTranslatef(-1.96F, -1.96F, 0.0F);
                }

                if (var10 == 6)
                {
                	GL11.glTranslatef(-1.96F, 1.96F, 0.0F);
                }

                if (var10 == 7)
                {
                	GL11.glTranslatef(1.96F, -1.96F, 0.0F);
                }

                if (var10 == 8)
                {
                	GL11.glTranslatef(1.96F, 1.96F, 0.0F);
                }

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture(titlePanoramaPaths[var10 - var10]));
                var4.startDrawingQuads();
                var4.setColorRGBA_I(16777215, 255 / (var6 + 1));
                var4.addVertexWithUV(-1.0D, -1.0D, 1.0D, (double)(0.0F + 1), (double)(0.0F + 1));
                var4.addVertexWithUV(1.0D, -1.0D, 1.0D, (double)(1.0F - 1), (double)(0.0F + 1));
                var4.addVertexWithUV(1.0D, 1.0D, 1.0D, (double)(1.0F - 1), (double)(1.0F - 1));
                var4.addVertexWithUV(-1.0D, 1.0D, 1.0D, (double)(0.0F + 1), (double)(1.0F - 1));
                var4.draw();
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
        }

        var4.setTranslation(0.0D, 0.0D, 0.0D);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private void drawPanorama(float par1)
    {
        Tessellator var4 = Tessellator.instance;
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GLU.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        byte var5 = 1;
        

        for (int var6 = 0; var6 < var5 * var5; ++var6)
        {
            GL11.glPushMatrix();
            float var7 = ((float)(var6 % var5) / (float)var5 - 0.5F) / 64.0F;
            float var8 = ((float)(var6 / var5) / (float)var5 - 0.5F) / 64.0F;
            float var9 = 0.0F;
            GL11.glTranslatef(var7, var8, var9);
            GL11.glRotatef(MathHelper.sin(((float)this.spaceTimer + par1) / 1000.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-((float)this.spaceTimer + par1) * 0.005F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(41, 0, 0, 1);

            for (int var10 = 0; var10 < 6; ++var10)
            {
                GL11.glPushMatrix();
                

                if (var10 == 1)
                {
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (var10 == 2)
                {
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (var10 == 3)
                {
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (var10 == 4)
                {
                    GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (var10 == 5)
                {
                    GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture(titlePanoramaPaths[0]));
                var4.startDrawingQuads();
                var4.setColorRGBA_I(16777215, 255 / (var6 + 1));
                var4.addVertexWithUV(-1.0D, -1.0D, 1.0D, (double)(0.0F + 1), (double)(0.0F + 1));
                var4.addVertexWithUV(1.0D, -1.0D, 1.0D, (double)(1.0F - 1), (double)(0.0F + 1));
                var4.addVertexWithUV(1.0D, 1.0D, 1.0D, (double)(1.0F - 1), (double)(1.0F - 1));
                var4.addVertexWithUV(-1.0D, 1.0D, 1.0D, (double)(0.0F + 1), (double)(1.0F - 1));
                var4.draw();
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
        }

        var4.setTranslation(0.0D, 0.0D, 0.0D);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Rotate and blurs the skybox view in the main menu
     */
    private void rotateAndBlurSkybox()
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture(titlePanoramaPaths[0]));
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColorMask(true, true, true, false);
        GL11.glPushMatrix();
        GL11.glPopMatrix();
        GL11.glColorMask(true, true, true, true);
    }

    /**
     * Renders the skybox in the main menu
     */
    public void renderSkybox(float par1)
    {
        GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GL11.glPushMatrix();
        GL11.glScalef(1.0F, 0.0F, 1.0F);
        this.drawPanorama(par1);
        this.drawPanorama2(par1);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        this.rotateAndBlurSkybox();
        GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        Tessellator var4 = Tessellator.instance;
        var4.startDrawingQuads();
        float var5 = this.width > this.height ? 120.0F / (float)this.width : 120.0F / (float)this.height;
        float var6 = (float)this.height * var5 / 256.0F;
        float var7 = (float)this.width * var5 / 256.0F;
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        var4.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        int var8 = this.width;
        int var9 = this.height;
        var4.addVertexWithUV(0.0D, (double)var9, (double)this.zLevel, (double)(0.5F - var6), (double)(0.5F + var7));
        var4.addVertexWithUV((double)var8, (double)var9, (double)this.zLevel, (double)(0.5F - var6), (double)(0.5F - var7));
        var4.addVertexWithUV((double)var8, 0.0D, (double)this.zLevel, (double)(0.5F + var6), (double)(0.5F - var7));
        var4.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, (double)(0.5F + var6), (double)(0.5F + var7));
        var4.draw();
        GL11.glPopMatrix();
    }
    
    public void drawScreen(int par1, int par2, float par3)
    {
    	if (this.initialized)
    	{
            this.planetSlots.drawScreen(par1, par2, par3);
            super.drawScreen(par1, par2, par3);
    	}
    }
    
    public void drawBlackBackground()
    {
        ScaledResolution var5 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int var6 = var5.getScaledWidth();
        int var7 = var5.getScaledHeight();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/micdoodle8/mods/galacticraft/client/backgrounds/black.png"));
        Tessellator var3 = Tessellator.instance;
        var3.startDrawingQuads();
        var3.addVertexWithUV(0.0D, (double)var7, -90.0D, 0.0D, 1.0D);
        var3.addVertexWithUV((double)var6, (double)var7, -90.0D, 1.0D, 1.0D);
        var3.addVertexWithUV((double)var6, 0.0D, -90.0D, 1.0D, 0.0D);
        var3.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        var3.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    
    protected void actionPerformed(GuiButton par1GuiButton)
    {
    	if (par1GuiButton.enabled)
    	{
    		Integer dim = GCUtil.getProviderForName(this.destinations[this.selectedSlot]).dimensionId;
            Object[] toSend = {dim};
            PacketDispatcher.sendPacketToServer(GCUtil.createPacket("Galacticraft", 2, toSend));
            FMLClientHandler.instance().getClient().displayGuiScreen(null);
    	}
    }

    static EntityPlayer getPlayerToSend(GCGuiChoosePlanet par0GuiLanguage)
    {
        return par0GuiLanguage.playerToSend;
    }
    
    static String[] getDestinations(GCGuiChoosePlanet par0GuiLanguage)
    {
    	return par0GuiLanguage.destinations;
    }
    
    static GuiSmallButton getSendButton(GCGuiChoosePlanet par0GuiLanguage)
    {
    	return par0GuiLanguage.sendButton;
    }

    static int setSelectedDimension(GCGuiChoosePlanet par0GuiLanguage, int par1)
    {
        return par0GuiLanguage.selectedSlot = par1;
    }

    static int getSelectedDimension(GCGuiChoosePlanet par0GuiLanguage)
    {
        return par0GuiLanguage.selectedSlot;
    }
}
