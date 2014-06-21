package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.FMLLog;

public class GuiCelestialSelection extends GuiScreen
{
	private float zoom = 0.0F;
	
	@Override
	public void initGui()
	{
		
	}

	@Override
	public void updateScreen()
	{
		float wheel = Mouse.getDWheel() / 500.0F;

		if (Mouse.hasWheel() && wheel != 0)
		{
			this.zoom += wheel;
			
			this.zoom = Math.min(Math.max(this.zoom, -0.5F), 3);
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
		for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
		{
			if (planet.getParentGalaxy() != null)
			{
				this.drawCircles(planet, 0, 0);
			}
		}
		GL11.glPopMatrix();
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

	public void drawCircles(Planet planet, float cx, float cy)
	{
		GL11.glLineWidth(3);
		final float theta = (float) (2 * Math.PI / 500);
		final float c = (float) Math.cos(theta);
		final float s = (float) Math.sin(theta);
		float t;

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);

		float x = planet.getRelativeDistanceFromCenter() * 25.0F;
		float y = 0;

		GL11.glColor4f(planet.getRingColorR(), planet.getRingColorG(), planet.getRingColorB(), 1.0F);

		GL11.glBegin(GL11.GL_LINE_LOOP);

		for (int ii = 0; ii < 500; ii++)
		{
			GL11.glVertex2f(x + cx, y + cy);

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
