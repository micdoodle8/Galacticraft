package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
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
        return GCCoreGuiChoosePlanet.getDestinations(this.languageGui).length;
    }

    @Override
	protected void elementClicked(int par1, boolean par2)
    {
    	if (par1 < GCCoreGuiChoosePlanet.getDestinations(this.languageGui).length)
    	{
    		GCCoreGuiChoosePlanet.setSelectedDimension(this.languageGui, par1);
    	}

        GCCoreGuiChoosePlanet.getSendButton(this.languageGui).displayString = "Send To Dimension";
        GCCoreGuiChoosePlanet.getSendButton(this.languageGui).enabled = this.languageGui.isValidDestination(this.languageGui.selectedSlot);
    }

    @Override
	protected boolean isSelected(int par1)
    {
        return par1 == GCCoreGuiChoosePlanet.getSelectedDimension(this.languageGui);
    }

    @Override
	protected int getContentHeight()
    {
        return this.getSize() * 20;
    }

    @Override
	protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
    	if (this.languageGui.isValidDestination(par1))
    	{
            this.languageGui.drawCenteredString(this.languageGui.fontRenderer, GCCoreGuiChoosePlanet.getDestinations(this.languageGui)[par1], this.languageGui.width / 2, par3 + 3, 16777215);
    	}
    	else
    	{
    		String str = GCCoreGuiChoosePlanet.getDestinations(this.languageGui)[par1];
    		str = str.replace("*", "");
            this.languageGui.drawCenteredString(this.languageGui.fontRenderer, str, this.languageGui.width / 2, par3 + 3, 16716305);
    	}
    	
    	if (this.isSelected(par1))
    	{
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GCCoreGuiChoosePlanet.getDestinations(this.languageGui)[par1].toLowerCase();
            final Tessellator var3 = Tessellator.instance;

            for (int i = 0; i < GalacticraftCore.clientSubMods.size(); i++)
            {
            	if (GalacticraftCore.clientSubMods.get(i) != null && GalacticraftCore.clientSubMods.get(i).getSlotRenderer() != null)
            	{
            		final IPlanetSlotRenderer renderer = GalacticraftCore.clientSubMods.get(i).getSlotRenderer();

        			String str = GCCoreGuiChoosePlanet.getDestinations(this.languageGui)[par1].toLowerCase();

        			if (str.contains("*"))
        			{
        				str = str.replace("*", "");
        			}

        			if (renderer.getPlanetName().toLowerCase().equals(str))
        			{
            			GL11.glBindTexture(GL11.GL_TEXTURE_2D, FMLClientHandler.instance().getClient().renderEngine.getTexture(renderer.getPlanetSprite()));

        				renderer.renderSlot(par1, par2 - 18, par3 + 7, par4, par5Tessellator);
        			}
            	}
            }

    		if (GCCoreGuiChoosePlanet.getDestinations(this.languageGui)[par1].equals("Overworld"))
            {
                FMLClientHandler.instance().getClient().renderEngine.func_98187_b("/micdoodle8/mods/galacticraft/core/client/planets/overworld.png");

                var3.startDrawingQuads();
                var3.addVertexWithUV(par2 - 10 - this.slotHeight * 0.9, 	par3 - 1 + this.slotHeight * 0.9, 	-90.0D, 0.0, 1.0);
                var3.addVertexWithUV(par2 - 10, 							par3 - 1 + this.slotHeight * 0.9, 	-90.0D, 1.0, 1.0);
                var3.addVertexWithUV(par2 - 10, 							par3 - 1, 							-90.0D, 1.0, 0.0);
                var3.addVertexWithUV(par2 - 10 - this.slotHeight * 0.9, 	par3 - 1, 							-90.0D, 0.0, 0.0);
                var3.draw();
                
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	}
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
