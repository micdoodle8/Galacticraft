package micdoodle8.mods.galacticraft.core.client;

import net.minecraft.src.GuiSlot;
import net.minecraft.src.Tessellator;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
@SideOnly(Side.CLIENT)
public class GCCoreGuiChoosePlanetSlot extends GuiSlot
{
    final GCCoreGuiChoosePlanet languageGui;

    public GCCoreGuiChoosePlanetSlot(GCCoreGuiChoosePlanet par1GCGuiChoosePlanet)
    {
        super(FMLClientHandler.instance().getClient(), par1GCGuiChoosePlanet.width, par1GCGuiChoosePlanet.height, 32, par1GCGuiChoosePlanet.height - 32, 20);
        this.languageGui = par1GCGuiChoosePlanet;
    }

    @Override
	protected int getSize()
    {
        return this.languageGui.getDestinations(languageGui).length;
    }

    @Override
	protected void elementClicked(int par1, boolean par2)
    {
    	if (par1 < this.languageGui.getDestinations(languageGui).length)
    	{
    		GCCoreGuiChoosePlanet.setSelectedDimension(languageGui, par1);
    	}
    	
        GCCoreGuiChoosePlanet.getSendButton(this.languageGui).displayString = "Send To Dimension";
        GCCoreGuiChoosePlanet.getSendButton(this.languageGui).enabled = true;
    }

    @Override
	protected boolean isSelected(int par1)
    {
        return par1 == GCCoreGuiChoosePlanet.getSelectedDimension(languageGui);
    }

    @Override
	protected int getContentHeight()
    {
        return this.getSize() * 20;
    }

    @Override
	protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
    	if (this.isSelected(par1))
    	{
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            String lowercase = this.languageGui.getDestinations(languageGui)[par1].toLowerCase();
            Tessellator var3 = Tessellator.instance;
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, FMLClientHandler.instance().getClient().renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/planets/" + lowercase + ".png"));

            if (this.languageGui.getDestinations(languageGui)[par1] == "Overworld")
            {
                var3.startDrawingQuads();
                var3.addVertexWithUV(par2 - 10 - this.slotHeight * 0.9, 	par3 - 1 + this.slotHeight * 0.9, 	-90.0D, 0.0, 1.0);
                var3.addVertexWithUV(par2 - 10, 							par3 - 1 + this.slotHeight * 0.9, 	-90.0D, 1.0, 1.0);
                var3.addVertexWithUV(par2 - 10, 							par3 - 1, 							-90.0D, 1.0, 0.0);
                var3.addVertexWithUV(par2 - 10 - this.slotHeight * 0.9, 	par3 - 1, 							-90.0D, 0.0, 0.0);
                var3.draw();
            }
            else if (this.languageGui.getDestinations(languageGui)[par1] == "Moon")
            {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, FMLClientHandler.instance().getClient().renderEngine.getTexture("/terrain/moon.png"));
                var3.startDrawingQuads();
                var3.addVertexWithUV(par2 - 10 - this.slotHeight * 0.9, 	par3 - 1 + this.slotHeight * 0.9, 	-90.0D, 0.35D, 0.65D);
                var3.addVertexWithUV(par2 - 10, 							par3 - 1 + this.slotHeight * 0.9, 	-90.0D, 0.65D, 0.65D);
                var3.addVertexWithUV(par2 - 10, 							par3 - 1, 							-90.0D, 0.65D, 0.35D);
                var3.addVertexWithUV(par2 - 10 - this.slotHeight * 0.9, 	par3 - 1, 							-90.0D, 0.35D, 0.35D);
                var3.draw();
            }
            else
            {
                var3.startDrawingQuads();
                var3.addVertexWithUV(par2 - 10 - this.slotHeight * 0.9, 	par3 - 1 + this.slotHeight * 0.9, 	-90.0D, 0.35D, 0.65D);
                var3.addVertexWithUV(par2 - 10, 							par3 - 1 + this.slotHeight * 0.9, 	-90.0D, 0.65D, 0.65D);
                var3.addVertexWithUV(par2 - 10, 							par3 - 1, 							-90.0D, 0.65D, 0.35D);
                var3.addVertexWithUV(par2 - 10 - this.slotHeight * 0.9, 	par3 - 1, 							-90.0D, 0.35D, 0.35D);
                var3.draw();
            }
            
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	}
        this.languageGui.drawCenteredString(this.languageGui.fontRenderer, this.languageGui.getDestinations(languageGui)[par1], this.languageGui.width / 2, par3 + 3, 16777215);
    }

	@Override
	protected void drawBackground() 
	{
	}
	
	@Override
	public void drawContainerBackground(Tessellator par1Tessellator)
	{
		this.languageGui.drawBlackBackground();
		this.languageGui.renderSkybox(1);
	}
}
