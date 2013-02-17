package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.Random;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSmallButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.WorldProvider;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreGuiChoosePlanet extends GuiScreen
{
    private int spaceTimer = 0;
    
    private GCCoreGuiChoosePlanetSlot planetSlots;
    
    private static final Random rand = new Random();

    private final float updateCounter = 0.0F;
    
    public int selectedSlot;
    
    private WorldProvider oldProvider;
    
    protected FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRenderer;
    
    private final String[] destinations;
    
    public EntityPlayer playerToSend;
    
    private boolean initialized;
    
    public GuiSmallButton sendButton;

    private static final String[] titlePanoramaPaths = new String[] {"/micdoodle8/mods/galacticraft/core/client/backgrounds/bg3.png"};
    
    public GCCoreGuiChoosePlanet(EntityPlayer player, String[] listOfDestinations)
    {
    	this.playerToSend = player;
    	this.destinations = listOfDestinations;
    }

    // Override keyTyped so you don't accidently hit Escape and fall to your death!
    @Override
	protected void keyTyped(char par1, int par2) {}
    
    @Override
	public void initGui()
    {
    	if (!(this.planetSlots == null))
            this.planetSlots.func_77207_a(2, 10, 10, 10);
    	
        this.planetSlots = new GCCoreGuiChoosePlanetSlot(this);
        
    	if (!this.initialized)
    	{
            this.initialized = true;
    	}
    	else
    	{
    	}

        final StringTranslate var1 = StringTranslate.getInstance();
//        this.controlList.add(new GCCoreGuiTexturedButton(0, this.width - 110, this.height - 26, 105, 20, "Send To Dimension", "" /* TODO */));
        this.controlList.add(new GCCoreGuiTexturedButton(0, this.width - 28, 5, 22, 22, "/micdoodle8/mods/galacticraft/core/client/gui/button1.png", 22, 22));
        this.controlList.add(this.sendButton = new GuiSmallButton(1, this.width - 110, this.height - 26, 105, 20, "Send To Dimension"));
        this.planetSlots.registerScrollButtons(this.controlList, 2, 3);
    }
    
    @Override
	public void updateScreen()
    {
        ++this.spaceTimer;
    }
    
    @Override
	public boolean doesGuiPauseGame()
    {
        return false;
    }
    
    private void drawPanorama2(float par1)
    {
        final Tessellator var4 = Tessellator.instance;
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
        final byte var5 = 1;
        

        for (int var6 = 0; var6 < var5 * var5; ++var6)
        {
            GL11.glPushMatrix();
            final float var7 = ((float)(var6 % var5) / (float)var5 - 0.5F) / 128.0F;
            final float var8 = ((float)(var6 / var5) / (float)var5 - 0.5F) / 128.0F;
            final float var9 = 0.0F;
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
                var4.addVertexWithUV(-1.0D, -1.0D, 1.0D, 0.0F + 1, 0.0F + 1);
                var4.addVertexWithUV(1.0D, -1.0D, 1.0D, 1.0F - 1, 0.0F + 1);
                var4.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0F - 1, 1.0F - 1);
                var4.addVertexWithUV(-1.0D, 1.0D, 1.0D, 0.0F + 1, 1.0F - 1);
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
        final Tessellator var4 = Tessellator.instance;
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
        final byte var5 = 1;
        

        for (int var6 = 0; var6 < var5 * var5; ++var6)
        {
            GL11.glPushMatrix();
            final float var7 = ((float)(var6 % var5) / (float)var5 - 0.5F) / 64.0F;
            final float var8 = ((float)(var6 / var5) / (float)var5 - 0.5F) / 64.0F;
            final float var9 = 0.0F;
            GL11.glTranslatef(var7, var8, var9);
            GL11.glRotatef(MathHelper.sin((this.spaceTimer + par1) / 1000.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-(this.spaceTimer + par1) * 0.005F, 0.0F, 1.0F, 0.0F);
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
                var4.addVertexWithUV(-1.0D, -1.0D, 1.0D, 0.0F + 1, 0.0F + 1);
                var4.addVertexWithUV(1.0D, -1.0D, 1.0D, 1.0F - 1, 0.0F + 1);
                var4.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0F - 1, 1.0F - 1);
                var4.addVertexWithUV(-1.0D, 1.0D, 1.0D, 0.0F + 1, 1.0F - 1);
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
        final Tessellator var4 = Tessellator.instance;
        var4.startDrawingQuads();
        final float var5 = this.width > this.height ? 120.0F / this.width : 120.0F / this.height;
        final float var6 = this.height * var5 / 256.0F;
        final float var7 = this.width * var5 / 256.0F;
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        var4.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        final int var8 = this.width;
        final int var9 = this.height;
        var4.addVertexWithUV(0.0D, var9, this.zLevel, 0.5F - var6, 0.5F + var7);
        var4.addVertexWithUV(var8, var9, this.zLevel, 0.5F - var6, 0.5F - var7);
        var4.addVertexWithUV(var8, 0.0D, this.zLevel, 0.5F + var6, 0.5F - var7);
        var4.addVertexWithUV(0.0D, 0.0D, this.zLevel, 0.5F + var6, 0.5F + var7);
        var4.draw();
        GL11.glPopMatrix();
    }
    
    @Override
	public void drawScreen(int par1, int par2, float par3)
    {
    	String str = null;
    	
    	if (this.initialized)
    	{
            this.planetSlots.drawScreen(par1, par2, par3);
            super.drawScreen(par1, par2, par3);
    	}
    	
		for (final IGalacticraftSubModClient mod : GalacticraftCore.clientSubMods)
		{
    		String dest = this.destinations[this.selectedSlot].toLowerCase();
    		
    		if (dest.contains("*"))
    		{
    			dest = dest.replace("*", "");
    		}
    		
    		if (mod.getDimensionName().toLowerCase().equals(dest))
    		{
    			if (mod.getLanguageFile() != null)
    			{
    				str = mod.getLanguageFile().get("gui.choosePlanet.desc." + dest);
    			}
    		}
    	}
    	
    	if (this.destinations[this.selectedSlot].toLowerCase().equals("overworld"))
    	{
    		str = GalacticraftCore.lang.get("gui.choosePlanet.desc.overworld");
    	}
    	
    	if (str != null)
    	{
	    	final String[] strArray = str.split("#");
	    	
	    	final int j = 260 / strArray.length + 1;
	    	
	    	for (int i = 0; i < strArray.length; i++)
	    	{
	    		if (strArray[i].contains("*"))
	    		{
	    			strArray[i] = strArray[i].replace("*", "");
		            this.drawCenteredString(this.fontRenderer, strArray[i], 50 + i * j, this.height - 20, 16716305);
	    		}
	    		else
	    		{
		            this.drawCenteredString(this.fontRenderer, strArray[i], 50 + i * j, this.height - 20, 16777215);
	    		}
	    	}
    	}
    }
    
    public void drawBlackBackground()
    {
        final ScaledResolution var5 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        final int var6 = var5.getScaledWidth();
        final int var7 = var5.getScaledHeight();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/backgrounds/black.png"));
        final Tessellator var3 = Tessellator.instance;
        var3.startDrawingQuads();
        var3.addVertexWithUV(0.0D, var7, -90.0D, 0.0D, 1.0D);
        var3.addVertexWithUV(var6, var7, -90.0D, 1.0D, 1.0D);
        var3.addVertexWithUV(var6, 0.0D, -90.0D, 1.0D, 0.0D);
        var3.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        var3.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    
    @Override
	protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
        case 0:
        	FMLClientHandler.instance().getClient().displayGuiScreen(new GCCoreGuiGalaxyMap(this.playerToSend, this.destinations));
        	break;
        case 1:
        	if (par1GuiButton.enabled && ClientProxyCore.teleportCooldown <= 0)
        	{
                final Object[] toSend = {this.destinations[this.selectedSlot]};
                PacketDispatcher.sendPacketToServer(GCCoreUtil.createPacket("Galacticraft", 2, toSend));
                FMLClientHandler.instance().getClient().displayGuiScreen(null);
                ClientProxyCore.teleportCooldown = 300;
        	}
        	break;
        }
    }
    
    public boolean isValidDestination(int i)
    {
    	final String str = this.destinations[i];
    	
    	if (str.contains("*"))
    	{
    		return false;
    	}
    	else
    	{
    		return true;
    	}
    }

    static EntityPlayer getPlayerToSend(GCCoreGuiChoosePlanet par0GuiLanguage)
    {
        return par0GuiLanguage.playerToSend;
    }
    
    static String[] getDestinations(GCCoreGuiChoosePlanet par0GuiLanguage)
    {
    	return par0GuiLanguage.destinations;
    }
    
    static GuiSmallButton getSendButton(GCCoreGuiChoosePlanet par0GuiLanguage)
    {
    	return par0GuiLanguage.sendButton;
    }

    static int setSelectedDimension(GCCoreGuiChoosePlanet par0GuiLanguage, int par1)
    {
        return par0GuiLanguage.selectedSlot = par1;
    }

    static int getSelectedDimension(GCCoreGuiChoosePlanet par0GuiLanguage)
    {
        return par0GuiLanguage.selectedSlot;
    }
}
