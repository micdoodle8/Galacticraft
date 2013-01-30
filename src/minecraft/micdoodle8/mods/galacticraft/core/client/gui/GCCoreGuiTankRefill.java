package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityPlayer;
import micdoodle8.mods.galacticraft.core.tile.GCCoreContainerTankRefill;
import micdoodle8.mods.galacticraft.core.tile.GCCoreInventoryTankRefill;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreGuiTankRefill extends GuiContainer 
{
	static Container tank = new GCCoreContainerTankRefill(FMLClientHandler.instance().getClient().thePlayer, new GCCoreInventoryTankRefill());

    private float xSize_lo;
    private float ySize_lo;
    
    private float rotation;
    
	public GCCoreGuiTankRefill(EntityPlayer player)
    {
        super(tank);
        this.allowUserInput = true;
    }

    @Override
	public void initGui()
    {
    	super.initGui();
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		rotation = 0;
    	this.controlList.add(new GuiButton(0, var5 + 36, var6 + 71, 7, 7, ""));
    	this.controlList.add(new GuiButton(1, var5 + 60, var6 + 71, 7, 7, ""));
    }

	@Override
    protected void actionPerformed(GuiButton par1GuiButton) 
    {
    	switch (par1GuiButton.id)
    	{
    	case 0:
    		rotation -= 10;
    		break;
    	case 1:
    		rotation += 10;
    		break;
    	}
    }

	@Override
    public void drawScreen(int par1, int par2, float par3)
    {
    	super.drawScreen(par1, par2, par3);
    	
        this.xSize_lo = par1;
        this.ySize_lo = par2;
    	
    	for (int i = 0; i < GalacticraftCore.gcPlayers.size(); i++)
    	{
    		GCCoreEntityPlayer player = (GCCoreEntityPlayer) GalacticraftCore.gcPlayers.get(i);
    		
    		if (FMLClientHandler.instance().getClient().thePlayer.username == player.getPlayer().username)
    		{
    			float var20 = player.astrBarCap();

    	        ScaledResolution var5 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
    	        int var6 = var5.getScaledWidth();
    	        int var7 = var5.getScaledHeight();
    	        this.mc.entityRenderer.setupOverlayRendering();
    	        GL11.glDisable(GL11.GL_LIGHTING);
    	        GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	        
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/gui/gui.png"));
    	        
                short var21 = 248;
                int var22 = (int)(this.mc.thePlayer.experience * (var21 + 1));
                int var23 = (this.height - this.ySize) / 2 - 5;
                int var18 = (this.width - this.xSize) / 2 + 2 - 50;
                this.drawTexturedModalRect(var18 - 2, var23 - 10 - 5, 0, 226, 256, 15);
                this.drawTexturedModalRect(var18 + 2, var23 - 5 - 5, 0, 242, var21, 7);

                if (var22 > 0)
                {
                    this.drawTexturedModalRect(var18 + 2, var23 - 5 - 5, 0, 249, var22, 7);
                }

                int var19b = par1 - (var18 - 2);
                int var20b = (par2 - (var23 - 10));

                if (var19b >= 0 && var20b >= 0 && var19b < 256 && var20b < 15)
                {
                	List list = new ArrayList();
                	list.add(String.valueOf("Galactic Level " + player.astronomyPointsLevel));
                	list.add(String.valueOf(Math.round(var22 / 2.56) + "%"));
                	this.drawInfoText(list, par1, par2);
                }
    		}
    	}
    }

    protected void drawInfoText(List list, int par2, int par3)
    {
    	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        if (!list.isEmpty())
        {
            int var5 = 0;
            int var6;
            int var7;

            for (var6 = 0; var6 < list.size(); ++var6)
            {
                var7 = this.fontRenderer.getStringWidth((String)list.get(var6));

                if (var7 > var5)
                {
                    var5 = var7;
                }
            }

            var6 = par2 + 12;
            var7 = par3 - 12;
            int var9 = 8;

            if (list.size() > 1)
            {
                var9 += 2 + (list.size() - 1) * 10;
            }

            if (this.guiTop + var7 + var9 + 6 > this.height)
            {
                var7 = this.height - var9 - this.guiTop - 6;
            }

            this.zLevel = 300.0F;
            itemRenderer.zLevel = 300.0F;
            int var10 = -267386864;
            this.drawGradientRect(var6 - 3, var7 - 4, var6 + var5 + 3, var7 - 3, var10, var10);
            this.drawGradientRect(var6 - 3, var7 + var9 + 3, var6 + var5 + 3, var7 + var9 + 4, var10, var10);
            this.drawGradientRect(var6 - 3, var7 - 3, var6 + var5 + 3, var7 + var9 + 3, var10, var10);
            this.drawGradientRect(var6 - 4, var7 - 3, var6 - 3, var7 + var9 + 3, var10, var10);
            this.drawGradientRect(var6 + var5 + 3, var7 - 3, var6 + var5 + 4, var7 + var9 + 3, var10, var10);
            int var11 = 1347420415;
            int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
            this.drawGradientRect(var6 - 3, var7 - 3 + 1, var6 - 3 + 1, var7 + var9 + 3 - 1, var11, var12);
            this.drawGradientRect(var6 + var5 + 2, var7 - 3 + 1, var6 + var5 + 3, var7 + var9 + 3 - 1, var11, var12);
            this.drawGradientRect(var6 - 3, var7 - 3, var6 + var5 + 3, var7 - 3 + 1, var11, var11);
            this.drawGradientRect(var6 - 3, var7 + var9 + 2, var6 + var5 + 3, var7 + var9 + 3, var12, var12);

            for (int var13 = 0; var13 < list.size(); ++var13)
            {
                String var14 = (String)list.get(var13);

                if (var13 == 0)
                {
                    var14 = "\u00a7" + Integer.toHexString(13) + var14;
                }
                else
                {
                    var14 = "\u00a77" + var14;
                }

                this.fontRenderer.drawStringWithShadow(var14, var6, var7, -1);

                if (var13 == 0)
                {
                    var7 += 2;
                }

                var7 += 10;
            }

            this.zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
        }
    }
	
    protected boolean isMouseOverBox(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        int var7 = this.guiLeft;
        int var8 = this.guiTop;
        par5 -= var7;
        par6 -= var8;
        return par5 >= par1 - 1 && par5 < par1 + par3 + 1 && par6 >= par2 - 1 && par6 < par2 + par4 + 1;
    }

	@Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) 
	{
		final int texture = this.mc.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/gui/airtank.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(texture);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
        int var5b = this.guiLeft;
        int var6b = this.guiTop;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
		this.drawPlayerOnGui(mc, var5 + 51, var6 + 75 + 1, 34, (var5 + 51) - this.xSize_lo, (var6 + 75 - 50) - this.ySize_lo);
	}

    public void drawPlayerOnGui(Minecraft par0Minecraft, int par1, int par2, int par3, float par4, float par5)
    {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(par1, par2, 50.0F);
        GL11.glScalef((-par3), par3, par3);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float var6 = par0Minecraft.thePlayer.renderYawOffset;
        float var7 = par0Minecraft.thePlayer.rotationYaw;
        float var8 = par0Minecraft.thePlayer.rotationPitch;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float)Math.atan((rotation / 40.0F))) * 00.0F, 1.0F, 0.0F, 0.0F);
        par0Minecraft.thePlayer.renderYawOffset = rotation;
        par0Minecraft.thePlayer.rotationYaw = rotation;
//        par0Minecraft.thePlayer.rotationPitch = -((float)Math.atan((double)(par5 / 40.0F))) * 80.0F;
        par0Minecraft.thePlayer.rotationYawHead = par0Minecraft.thePlayer.rotationYaw;
        GL11.glTranslatef(0.0F, par0Minecraft.thePlayer.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;
        RenderManager.instance.renderEntityWithPosYaw(par0Minecraft.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        par0Minecraft.thePlayer.renderYawOffset = var6;
        par0Minecraft.thePlayer.rotationYaw = var7;
//        par0Minecraft.thePlayer.rotationPitch = var8;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}
