package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiCelestialSelection extends GuiScreen
{
	private float zoom = 0.0F;
	public static ResourceLocation guiMain = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/celestialselection.png");
	private int ticks = 0;
	
	@Override
	public void initGui()
	{
		
	}

	@Override
	public void updateScreen()
	{
		ticks++;
		
		if (Mouse.hasWheel())
		{
			float wheel = Mouse.getDWheel() / 500.0F;

			if (wheel != 0)
			{
				this.zoom = Math.min(Math.max(this.zoom + wheel, -0.5F), 3);
			}
		}
	}
	
	@Override
	public void drawScreen(int mousePosX, int mousePosY, float partialTicks)
	{
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		int scaledW = scaledRes.getScaledWidth();
		int scaledH = scaledRes.getScaledHeight();

		this.setBlackBackground(scaledW, scaledH);
				
		GL11.glPushMatrix();
		this.setIsometric();
		this.drawGrid(scaledH / 3, (scaledH / 3) / 3.5F);
		this.drawCircles();
		GL11.glPopMatrix();

		int borderWidth = width / 65;
		int edgeWidth = borderWidth / 4;
		this.drawButtons(scaledW, scaledH, borderWidth, edgeWidth);
		this.drawBorder(scaledW, scaledH, borderWidth, edgeWidth);	
	}
	
	public void drawBorder(int width, int height, int borderWidth, int edgeWidth)
	{
		Gui.drawRect(0, 0, borderWidth, height, GCCoreUtil.to32BitColor(255, 100, 100, 100));
		Gui.drawRect(width - borderWidth, 0, width, height, GCCoreUtil.to32BitColor(255, 100, 100, 100));
		Gui.drawRect(0, 0, width, borderWidth, GCCoreUtil.to32BitColor(255, 100, 100, 100));
		Gui.drawRect(0, height - borderWidth, width, height, GCCoreUtil.to32BitColor(255, 100, 100, 100));
		Gui.drawRect(borderWidth, borderWidth, borderWidth + edgeWidth, height - borderWidth, GCCoreUtil.to32BitColor(255, 40, 40, 40));
		Gui.drawRect(borderWidth, borderWidth, width - borderWidth, borderWidth + edgeWidth, GCCoreUtil.to32BitColor(255, 40, 40, 40));
		Gui.drawRect(width - borderWidth - edgeWidth, borderWidth, width - borderWidth, height - borderWidth, GCCoreUtil.to32BitColor(255, 80, 80, 80));
		Gui.drawRect(borderWidth + edgeWidth, height - borderWidth - edgeWidth, width - borderWidth, height - borderWidth, GCCoreUtil.to32BitColor(255, 80, 80, 80));
	}
	
	public void drawButtons(int width, int height, int borderWidth, int edgeWidth)
	{
		CelestialBody cBody = GalacticraftCore.planetOverworld;
		
		mc.renderEngine.bindTexture(guiMain);
        GL11.glColor3f(0.9F, 0.2F, 0.2F);
		this.drawTexturedModalRect(width / 2 - 43, height - borderWidth - edgeWidth - 15, 86, 15, 266, 0, 172, 29, true);
		String str = GCCoreUtil.translate("gui.message.exit.name").toUpperCase();
		this.fontRendererObj.drawString(str, width / 2 - this.fontRendererObj.getStringWidth(str) / 2, height - borderWidth - edgeWidth - 15 + fontRendererObj.FONT_HEIGHT / 2, GCCoreUtil.to32BitColor(255, 255, 255, 255));

		mc.renderEngine.bindTexture(guiMain);
    	GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
		this.drawTexturedModalRect(width / 2 - 43, borderWidth + edgeWidth, 86, 15, 266, 0, 172, 29, false);
		str = GCCoreUtil.translate("gui.message.catalog.name").toUpperCase();
		this.fontRendererObj.drawString(str, width / 2 - this.fontRendererObj.getStringWidth(str) / 2, borderWidth + edgeWidth + fontRendererObj.FONT_HEIGHT / 2, GCCoreUtil.to32BitColor(255, 255, 255, 255));

		mc.renderEngine.bindTexture(guiMain);
    	GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
		int menuTopLeft = borderWidth + edgeWidth - 115 + height / 2;
//		int posX = (int) ((Math.sin(ticks / 10.0F) * 0.5F + 0.5F) * 133) - 133;
		int posX = borderWidth + edgeWidth;
		int fontPosY = menuTopLeft + edgeWidth + fontRendererObj.FONT_HEIGHT / 2 - 2;
		this.drawTexturedModalRect(posX, menuTopLeft, 133, 209, 0, 0, 266, 418, false);
		str = cBody.getLocalizedName();
		this.fontRendererObj.drawString(str, posX + 20, fontPosY, GCCoreUtil.to32BitColor(255, 255, 255, 255));
		
		str = GCCoreUtil.translate("gui.message.daynightcycle.name") + ":";
		this.fontRendererObj.drawString(str, posX + 5, fontPosY + 14, GCCoreUtil.to32BitColor(255, 150, 200, 255));
		str = GCCoreUtil.translate("gui.message." + cBody.getName() + ".daynightcycle.0.name");
		this.fontRendererObj.drawString(str, posX + 10, fontPosY + 25, GCCoreUtil.to32BitColor(255, 255, 255, 255));
		str = GCCoreUtil.translate("gui.message." + cBody.getName() + ".daynightcycle.1.name");
		if (!str.isEmpty()) this.fontRendererObj.drawString(str, posX + 10, fontPosY + 36, GCCoreUtil.to32BitColor(255, 255, 255, 255));
		
		str = GCCoreUtil.translate("gui.message.surfacegravity.name") + ":";
		this.fontRendererObj.drawString(str, posX + 5, fontPosY + 50, GCCoreUtil.to32BitColor(255, 150, 200, 255));
		str = GCCoreUtil.translate("gui.message." + cBody.getName() + ".surfacegravity.0.name");
		this.fontRendererObj.drawString(str, posX + 10, fontPosY + 61, GCCoreUtil.to32BitColor(255, 255, 255, 255));
		str = GCCoreUtil.translate("gui.message." + cBody.getName() + ".surfacegravity.1.name");
		if (!str.isEmpty()) this.fontRendererObj.drawString(str, posX + 10, fontPosY + 72, GCCoreUtil.to32BitColor(255, 255, 255, 255));
		
		str = GCCoreUtil.translate("gui.message.surfacecomposition.name") + ":";
		this.fontRendererObj.drawString(str, posX + 5, fontPosY + 88, GCCoreUtil.to32BitColor(255, 150, 200, 255));
		str = GCCoreUtil.translate("gui.message." + cBody.getName() + ".surfacecomposition.0.name");
		this.fontRendererObj.drawString(str, posX + 10, fontPosY + 99, GCCoreUtil.to32BitColor(255, 255, 255, 255));
		str = GCCoreUtil.translate("gui.message." + cBody.getName() + ".surfacecomposition.1.name");
		if (!str.isEmpty()) this.fontRendererObj.drawString(str, posX + 10, fontPosY + 110, GCCoreUtil.to32BitColor(255, 255, 255, 255));
		
		str = GCCoreUtil.translate("gui.message.atmosphere.name") + ":";
		this.fontRendererObj.drawString(str, posX + 5, fontPosY + 126, GCCoreUtil.to32BitColor(255, 150, 200, 255));
		str = GCCoreUtil.translate("gui.message." + cBody.getName() + ".atmosphere.0.name");
		this.fontRendererObj.drawString(str, posX + 10, fontPosY + 137, GCCoreUtil.to32BitColor(255, 255, 255, 255));
		str = GCCoreUtil.translate("gui.message." + cBody.getName() + ".atmosphere.1.name");
		if (!str.isEmpty()) this.fontRendererObj.drawString(str, posX + 10, fontPosY + 148, GCCoreUtil.to32BitColor(255, 255, 255, 255));
		
		str = GCCoreUtil.translate("gui.message.meansurfacetemp.name") + ":";
		this.fontRendererObj.drawString(str, posX + 5, fontPosY + 165, GCCoreUtil.to32BitColor(255, 150, 200, 255));
		str = GCCoreUtil.translate("gui.message." + cBody.getName() + ".meansurfacetemp.0.name");
		this.fontRendererObj.drawString(str, posX + 10, fontPosY + 176, GCCoreUtil.to32BitColor(255, 255, 255, 255));
		str = GCCoreUtil.translate("gui.message." + cBody.getName() + ".meansurfacetemp.1.name");
		if (!str.isEmpty()) this.fontRendererObj.drawString(str, posX + 10, fontPosY + 187, GCCoreUtil.to32BitColor(255, 255, 255, 255));
	}

    public void drawTexturedModalRect(int x, int y, int width, int height, int u, int v, int uWidth, int vHeight, boolean invert)
    {
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        float texMod = 1 / 512.0F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        int height0 = invert ? 0 : vHeight;
        int height1 = invert ? vHeight : 0;
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + height), (double)this.zLevel, (double)((float)(u + 0) * texMod), (double)((float)(v + height0) * texMod));
        tessellator.addVertexWithUV((double)(x + width), (double)(y + height), (double)this.zLevel, (double)((float)(u + uWidth) * texMod), (double)((float)(v + height0) * texMod));
        tessellator.addVertexWithUV((double)(x + width), (double)(y + 0), (double)this.zLevel, (double)((float)(u + uWidth) * texMod), (double)((float)(v + height1) * texMod));
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)((float)(u + 0) * texMod), (double)((float)(v + height1) * texMod));
        tessellator.draw();
    }
	
	public void setBlackBackground(int width, int height)
	{
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		this.mc.getTextureManager().bindTexture(GuiChoosePlanet.blackTexture);
		final Tessellator var3 = Tessellator.instance;
		var3.startDrawingQuads();
		var3.addVertexWithUV(0.0D, height, -90.0D, 0.0D, 1.0D);
		var3.addVertexWithUV(width, height, -90.0D, 1.0D, 1.0D);
		var3.addVertexWithUV(width, 0.0D, -90.0D, 1.0D, 0.0D);
		var3.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		var3.draw();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(false);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	public void setIsometric()
	{
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		int scaledW = scaledRes.getScaledWidth();
		int scaledH = scaledRes.getScaledHeight();
		GL11.glTranslatef(scaledW / 2.0F, scaledH / 3 + 43, 0.0F);
		GL11.glRotatef(55.0F, 1, 0, 0);
		GL11.glRotatef(-45.0F, 0, 0, 1);
		GL11.glScalef(1.1F + zoom, 1.1F + zoom, 1.1F + zoom);
	}
	
	public void drawGrid(float gridSize, float gridScale)
	{		
		GL11.glColor4f(0.0F, 0.2F, 0.5F, 0.25F);
		
		GL11.glBegin(GL11.GL_LINES);

		gridSize += gridScale / 2;
		for (float x = -gridSize; x <= gridSize; x += gridScale)
		{
			GL11.glVertex3f(x, -gridSize, -0.0F);
			GL11.glVertex3f(x, gridSize, -0.0F);
			GL11.glVertex3f(-gridSize, x, -0.0F);
			GL11.glVertex3f(gridSize, x, -0.0F);
		}
		
		GL11.glEnd();
	}

	public void drawCircles()
	{
    	GL11.glColor4f(1, 1, 1, 1);
		GL11.glLineWidth(3);
		int count = 0;
		for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
		{
			if (planet.getParentGalaxy() != null)
			{
				final float theta = (float) (2 * Math.PI / 500);
				final float c = (float) Math.cos(theta);
				final float s = (float) Math.sin(theta);
				float t;

				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDisable(GL12.GL_RESCALE_NORMAL);

				float x = planet.getRelativeDistanceFromCenter() * 25.0F;
				float y = 0;
				
				switch (count % 2)
				{
				case 0:
			    	GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
					break;
				case 1:
			    	GL11.glColor4f(0.4F, 0.9F, 1.0F, 1);
					break;
				}

				GL11.glBegin(GL11.GL_LINE_LOOP);

				for (int ii = 0; ii < 500; ii++)
				{
					GL11.glVertex2f(x, y);

					t = x;
					x = c * x - s * y;
					y = s * t + c * y;
				}

				GL11.glEnd();

				GL11.glDepthFunc(GL11.GL_GEQUAL);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
				GL11.glEnable(GL11.GL_COLOR_MATERIAL);
				count++;
			}
		}
		GL11.glLineWidth(1);
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		switch (button.id)
		{
		default:
			break;
		}
	}
}
