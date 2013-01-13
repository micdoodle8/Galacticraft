package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityPlayer;
import micdoodle8.mods.galacticraft.core.tile.GCCoreContainerTankRefill;
import micdoodle8.mods.galacticraft.core.tile.GCCoreInventoryTankRefill;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreGuiTankRefill extends GuiContainer 
{
	static Container tank = new GCCoreContainerTankRefill(FMLClientHandler.instance().getClient().thePlayer.inventory, new GCCoreInventoryTankRefill());
	
	public GCCoreGuiTankRefill(EntityPlayer player)
    {
        super(tank);
        this.allowUserInput = true;
    }

	@Override
    public void drawScreen(int par1, int par2, float par3)
    {
    	super.drawScreen(par1, par2, par3);
    	
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
                int var22 = (int)(this.mc.thePlayer.experience * (float)(var21 + 1));
                int var23 = (this.height - this.ySize) / 2 - 5;
                int var18 = (this.width - this.xSize) / 2 + 2 - 50;
                this.drawTexturedModalRect(var18 - 2, var23 - 10, 0, 226, 256, 15);
                this.drawTexturedModalRect(var18 + 2, var23 - 5, 0, 242, var21, 7);

                if (var22 > 0)
                {
                    this.drawTexturedModalRect(var18 + 2, var23 - 5, 0, 249, var22, 7);
                }

                int var19b = par1 - (var18 - 2);
                int var20b = (int) (par2 - (var23 - 10));

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
		this.fontRenderer.drawString("Oxygen Tank Refill", 8, 10, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) 
	{
		final int texture = this.mc.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/gui/airtank.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(texture);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, this.ySize);
	}
}
