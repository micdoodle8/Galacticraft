package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.API.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.API.ICelestialBody;
import micdoodle8.mods.galacticraft.API.ICelestialBodyRenderer;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreGuiChoosePlanetSlot extends GuiSlot
{
    final GCCoreGuiChoosePlanet choosePlanetGui;

    public GCCoreGuiChoosePlanetSlot(GCCoreGuiChoosePlanet par1GCGuiChoosePlanet)
    {
        super(FMLClientHandler.instance().getClient(), par1GCGuiChoosePlanet.width, par1GCGuiChoosePlanet.height, 32, par1GCGuiChoosePlanet.height - 32, 20);
        this.choosePlanetGui = par1GCGuiChoosePlanet;
    }

    @Override
    protected int getSize()
    {
        return GCCoreGuiChoosePlanet.getDestinations(this.choosePlanetGui).length;
    }

    @Override
    protected void elementClicked(int par1, boolean par2)
    {
        if (par1 < GCCoreGuiChoosePlanet.getDestinations(this.choosePlanetGui).length)
        {
            GCCoreGuiChoosePlanet.setSelectedDimension(this.choosePlanetGui, par1);
        }

        GCCoreGuiChoosePlanet.getSendButton(this.choosePlanetGui).displayString = LanguageRegistry.instance().getStringLocalization("gui.button.sendtodim.name");
        GCCoreGuiChoosePlanet.getSendButton(this.choosePlanetGui).enabled = this.choosePlanetGui.isValidDestination(this.choosePlanetGui.selectedSlot);

        GCCoreGuiChoosePlanet.getCreateSpaceStationButton(this.choosePlanetGui).displayString = LanguageRegistry.instance().getStringLocalization("gui.button.createsstation.name");
        GCCoreGuiChoosePlanet.getCreateSpaceStationButton(this.choosePlanetGui).enabled = this.choosePlanetGui.canCreateSpaceStation();
    }

    @Override
    protected boolean isSelected(int par1)
    {
        return par1 == GCCoreGuiChoosePlanet.getSelectedDimension(this.choosePlanetGui);
    }

    @Override
    protected int getContentHeight()
    {
        return this.getSize() * 20;
    }

    @Override
    protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GCCoreGuiChoosePlanet.getDestinations(this.choosePlanetGui)[par1].toLowerCase();
        final Tessellator var3 = Tessellator.instance;

        if (this.isSelected(par1))
        {
            for (int i = 0; i < GalacticraftRegistry.getCelestialBodies().size(); i++)
            {
                ICelestialBody celestialBody = GalacticraftRegistry.getCelestialBodies().get(i);
                
                if (celestialBody != null && celestialBody.getMapObject().getSlotRenderer() != null)
                {
                    ICelestialBodyRenderer renderer = celestialBody.getMapObject().getSlotRenderer();

                    String str = GCCoreGuiChoosePlanet.getDestinations(this.choosePlanetGui)[par1].toLowerCase();

                    if (str.contains("*"))
                    {
                        str = str.replace("*", "");
                    }

                    if (str.contains("$"))
                    {
                        final String[] twoDimensions = str.split("\\$");

                        if (twoDimensions.length > 2)
                        {
                            str = twoDimensions[2];
                        }
                        else
                        {
                            str = "";
                        }
                    }

                    if (renderer.getPlanetName().toLowerCase().equals(str))
                    {
                        FMLClientHandler.instance().getClient().renderEngine.bindTexture(renderer.getPlanetSprite());

                        FMLLog.info("" + par4 + " " + this.slotHeight);
                        renderer.renderSlot(par1, par2 - 18, par3 + 9, par4 + 3, par5Tessellator);

                        FMLClientHandler.instance().getClient().renderEngine.resetBoundTexture();
                    }
                }
            }

//            if (GCCoreGuiChoosePlanet.getDestinations(this.choosePlanetGui)[par1].equals("Overworld"))
//            {
//                FMLClientHandler.instance().getClient().renderEngine.bindTexture("/micdoodle8/mods/galacticraft/core/client/planets/overworld.png");
//
//                var3.startDrawingQuads();
//                var3.addVertexWithUV(par2 - 10 - this.slotHeight * 0.9, par3 - 1 + this.slotHeight * 0.9, -90.0D, 0.0, 1.0);
//                var3.addVertexWithUV(par2 - 10, par3 - 1 + this.slotHeight * 0.9, -90.0D, 1.0, 1.0);
//                var3.addVertexWithUV(par2 - 10, par3 - 1, -90.0D, 1.0, 0.0);
//                var3.addVertexWithUV(par2 - 10 - this.slotHeight * 0.9, par3 - 1, -90.0D, 0.0, 0.0);
//                var3.draw();
//
//                FMLClientHandler.instance().getClient().renderEngine.resetBoundTexture();
//            }

            if (this.choosePlanetGui.isValidDestination(par1))
            {
                String str = GCCoreGuiChoosePlanet.getDestinations(this.choosePlanetGui)[par1];

                if (str.contains("$"))
                {
                    final String[] strs = str.split("\\$");

                    if (strs.length > 2)
                    {
                        str = strs[2];
                    }
                    else
                    {
                        str = "";
                    }
                }
                else
                {
                    str = LanguageRegistry.instance().getStringLocalization("dimension." + str + ".name");
                }

                this.choosePlanetGui.drawCenteredString(this.choosePlanetGui.getFontRenderer(), str, this.choosePlanetGui.width / 2, par3 + 3, 0xEEEEEE);
            }
            else
            {
                String str = GCCoreGuiChoosePlanet.getDestinations(this.choosePlanetGui)[par1];
                str = str.replace("*", "");

                this.choosePlanetGui.drawCenteredString(this.choosePlanetGui.getFontRenderer(), str, this.choosePlanetGui.width / 2, par3 + 3, 0xEEEEEE);
            }
        }
        else
        {
            if (this.choosePlanetGui.isValidDestination(par1))
            {
                String str = GCCoreGuiChoosePlanet.getDestinations(this.choosePlanetGui)[par1];

                if (str.contains("$"))
                {
                    final String[] strs = str.split("\\$");

                    if (strs.length > 2)
                    {
                        str = strs[2];
                    }
                    else
                    {
                        str = "";
                    }
                }
                else
                {
                    str = LanguageRegistry.instance().getStringLocalization("dimension." + str + ".name");
                }

                this.choosePlanetGui.drawCenteredString(this.choosePlanetGui.getFontRenderer(), str, this.choosePlanetGui.width / 2, par3 + 3, 0xEEEEEE);
            }
            else
            {
                String str = GCCoreGuiChoosePlanet.getDestinations(this.choosePlanetGui)[par1];
                str = str.replace("*", "");

                this.choosePlanetGui.drawCenteredString(this.choosePlanetGui.getFontRenderer(), str, this.choosePlanetGui.width / 2, par3 + 3, 0xEEEEEE);
            }
        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    @Override
    protected void drawBackground()
    {
    }

    @Override
    public void drawContainerBackground(Tessellator par1Tessellator)
    {
        this.choosePlanetGui.drawBlackBackground();
        this.choosePlanetGui.renderSkybox(1);
    }
}
