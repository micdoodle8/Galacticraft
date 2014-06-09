package micdoodle8.mods.galacticraft.core.client.gui.screen;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreGuiGalaxyMap.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GuiGalaxyMap extends GuiStarBackground
{
	private static final ResourceLocation guiTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/gui.png");
    private static final ResourceLocation vanillaSun = new ResourceLocation("textures/environment/sun.png");
	
	private static int guiMapMinX;

	private static int guiMapMinY;

	private static int guiMapMaxX;

	private static int guiMapMaxY;

	protected float mouseX = 0;

	protected float mouseY = 0;

	protected double field_74117_m;
	protected double field_74115_n;

	protected double guiMapX;

	protected double guiMapY;
	protected double field_74124_q;
	protected double field_74123_r;

	private float zoom = 0.2F;

	EntityPlayer player;

	private CelestialBody selectedPlanet = null;

	private String[] listOfDimensions;

	public GuiGalaxyMap(EntityPlayer player)
	{
		this.player = player;
	}

	public GuiGalaxyMap(EntityPlayer player, String[] listOfDimensions)
	{
		this.player = player;
		this.listOfDimensions = listOfDimensions;
	}

	@Override
	public void initGui()
	{
		this.mc.mouseHelper.grabMouseCursor();
		this.field_74117_m = this.guiMapX = this.field_74124_q = 0 - this.width / 4;
		this.field_74115_n = this.guiMapY = this.field_74123_r = 0 - this.height / 4;
	}

	@Override
	protected void keyTyped(char par1, int par2)
	{
		if (par2 == this.mc.gameSettings.keyBindInventory.getKeyCode())
		{
			this.mc.displayGuiScreen((GuiScreen) null);
			this.mc.setIngameFocus();
		}
		else if (par2 == Keyboard.KEY_ESCAPE)
		{
			if (this.listOfDimensions != null)
			{
				FMLClientHandler.instance().getClient().currentScreen = null;
				FMLClientHandler.instance().getClient().displayGuiScreen(new GuiChoosePlanet(this.player, this.listOfDimensions));
				this.mc.inGameHasFocus = true;
				this.mc.mouseHelper.ungrabMouseCursor();
			}
			else
			{
				super.keyTyped(par1, par2);
			}
		}
		else
		{
			super.keyTyped(par1, par2);
		}
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		GuiGalaxyMap.guiMapMinX = -250000 - this.width;
		GuiGalaxyMap.guiMapMaxX = 250000 + this.width;
		GuiGalaxyMap.guiMapMinY = -250000 - this.height;
		GuiGalaxyMap.guiMapMaxY = 250000 + this.height;

		while (!Mouse.isButtonDown(0) && Mouse.next())
		{
			float wheel = Mouse.getEventDWheel();

			if (Mouse.hasWheel() && wheel != 0)
			{
				wheel /= 7500F;

				if (this.zoom <= 0.0171F)
				{
					wheel /= 10F;
				}

				this.zoom = MathHelper.clamp_float(this.zoom + wheel, 0.0011000001F, 2);
			}
		}

		if (ConfigManagerCore.wasdMapMovement)
		{
			if (Keyboard.isKeyDown(Keyboard.KEY_W))
			{
				this.mouseY = 1 * (1 / (this.zoom * 2));
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_S))
			{
				this.mouseY = -1 * (1 / (this.zoom * 2));
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_D))
			{
				this.mouseX = -1 * (1 / (this.zoom * 2));
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_A))
			{
				this.mouseX = 1 * (1 / (this.zoom * 2));
			}

			this.mouseY *= 0.9;
			this.mouseX *= 0.9;
		}
		else
		{
			this.mouseY = (-(this.mc.displayHeight / 2) + Mouse.getY()) / 100F * (1 / (this.zoom * 5));
			this.mouseX = (this.mc.displayWidth / 2 - Mouse.getX()) / 100F * (1 / (this.zoom * 5));

			if (Mouse.getX() > this.mc.displayWidth / 2 - 90 && Mouse.getX() < this.mc.displayWidth / 2 + 90 && Mouse.getY() > this.mc.displayHeight / 2 - 90 && Mouse.getY() < this.mc.displayHeight / 2 + 90)
			{
				this.mouseX = 0;
				this.mouseY = 0;
			}
		}

		if (this.field_74124_q < GuiGalaxyMap.guiMapMinX)
		{
			this.field_74124_q = GuiGalaxyMap.guiMapMinX;

			if (this.mouseX > 0)
			{
				this.mouseX = 0;
			}
		}

		if (this.field_74123_r < GuiGalaxyMap.guiMapMinY)
		{
			this.field_74123_r = GuiGalaxyMap.guiMapMinY;

			if (this.mouseY > 0)
			{
				this.mouseY = 0;
			}
		}

		if (this.field_74124_q >= GuiGalaxyMap.guiMapMaxX)
		{
			this.field_74124_q = GuiGalaxyMap.guiMapMaxX - 1;

			if (this.mouseX < 0)
			{
				this.mouseX = 0;
			}
		}

		if (this.field_74123_r >= GuiGalaxyMap.guiMapMaxY)
		{
			this.field_74123_r = GuiGalaxyMap.guiMapMaxY - 1;

			if (this.mouseY < 0)
			{
				this.mouseY = 0;
			}
		}

		this.guiMapX -= this.mouseX;
		this.guiMapY -= this.mouseY;

		this.field_74124_q = this.field_74117_m = this.guiMapX;
		this.field_74123_r = this.field_74115_n = this.guiMapY;

		this.drawDefaultBackground();
		this.genAchievementBackground(par1, par2, par3);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	@Override
	public void updateScreen()
	{
		this.field_74117_m = this.guiMapX;
		this.field_74115_n = this.guiMapY;
	}

	// protected void drawTitle()
	// {
	// this.fontRendererObj.drawString("Galaxy Map", 15, 5, 4210752);
	// }

	@SuppressWarnings("rawtypes")
	protected void genAchievementBackground(int par1, int par2, float par3)
	{
		final ScaledResolution var13 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		var13.getScaledWidth();
		var13.getScaledHeight();
		int var4 = MathHelper.floor_double(this.field_74117_m + (this.guiMapX - this.field_74117_m) * par3);
		int var5 = MathHelper.floor_double(this.field_74115_n + (this.guiMapY - this.field_74115_n) * par3);

		if (var4 < GuiGalaxyMap.guiMapMinX)
		{
			var4 = GuiGalaxyMap.guiMapMinX;
		}

		if (var5 < GuiGalaxyMap.guiMapMinY)
		{
			var5 = GuiGalaxyMap.guiMapMinY;
		}

		if (var4 >= GuiGalaxyMap.guiMapMaxX)
		{
			var4 = GuiGalaxyMap.guiMapMaxX - 1;
		}

		if (var5 >= GuiGalaxyMap.guiMapMaxY)
		{
			var5 = GuiGalaxyMap.guiMapMaxY - 1;
		}

		this.zLevel = 0.0F;
		GL11.glDepthFunc(GL11.GL_GEQUAL);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);

		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);

		GL11.glPushMatrix();

		int var26;

		this.drawBlackBackground();
		this.renderSkybox(1);

		this.zoom(this.zoom);

		int var27;
		int var42;
		int var41;

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.renderSun(488-var4-var4, -488-var5-var5);
		
		for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
		{
			if (planet.getParentGalaxy() != null)
			{
				final int galaxyCenterX = (int) (-var4 + planet.getParentGalaxy().getMapPosition().x * 10000);
				final int galaxyCenterY = (int) (-var5 + planet.getParentGalaxy().getMapPosition().y * 10000);

				this.drawCircles(planet, galaxyCenterX + galaxyCenterX, galaxyCenterY + galaxyCenterY);

				Vector3 planetCenter = this.drawCelestialBody(planet, galaxyCenterX, galaxyCenterY);

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

				for (Moon moon : GalaxyRegistry.getRegisteredMoons().values())
				{
					if (moon.getParentPlanet() == planet)
					{
						this.drawCelestialBody(moon, galaxyCenterX + planetCenter.intX() / 2, galaxyCenterY + planetCenter.intY() / 2);
					}
				}

				GL11.glDepthMask(true);
			}
		}

		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		this.zLevel = 0.0F;
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		this.mc.getTextureManager().bindTexture(GuiGalaxyMap.guiTexture);
		this.drawTexturedModalRect(this.width / 2 - 5, this.height / 2 - 5, 123, 0, 10, 10);

		final int col = GCCoreUtil.to32BitColor(255, 198, 198, 198);
		final int col2 = GCCoreUtil.to32BitColor(255, 145, 145, 145);
		final int col3 = GCCoreUtil.to32BitColor(255, 33, 33, 33);
		final int textCol1 = GCCoreUtil.to32BitColor(255, 133, 133, 133);

		Gui.drawRect(0, 0, this.width, 20, col);
		Gui.drawRect(0, this.height - 24, this.width, this.height, col);
		Gui.drawRect(0, 0, 10, this.height, col);
		Gui.drawRect(this.width - 10, 0, this.width, this.height, col);

		Gui.drawRect(10, 20, this.width - 10, 22, col3);
		Gui.drawRect(10, this.height - 26, this.width - 10, this.height - 24, col2);
		Gui.drawRect(10, 20, 12, this.height - 24, col3);
		Gui.drawRect(this.width - 12, 0 + 20, this.width - 10, this.height - 24, col2);

		this.fontRendererObj.drawString("ASWD - Move Map", 5, 1, textCol1, false);
		this.fontRendererObj.drawString("Mouse Wheel - Zoom", 5, 10, textCol1, false);
		this.fontRendererObj.drawString("Mouse 1 - Select Planet", this.width - this.fontRendererObj.getStringWidth("Mouse 1 - Select Planet") - 5, 1, textCol1, false);
		this.fontRendererObj.drawString("Esc - Exit Map", this.width - this.fontRendererObj.getStringWidth("Esc - Exit Map") - 5, 10, textCol1, false);

		if (this.selectedPlanet != null)
		{
			final int textCol2 = GCCoreUtil.to32BitColor(255, MathHelper.floor_double(this.selectedPlanet.getRingColorR() * 255), MathHelper.floor_double(this.selectedPlanet.getRingColorG() * 255), MathHelper.floor_double(this.selectedPlanet.getRingColorB() * 255));
			final int width = this.fontRendererObj.getStringWidth(this.selectedPlanet.getLocalizedName());
			Gui.drawRect(this.width / 2 - width / 2 - 4, 4, this.width / 2 + width / 2 + 4, 15, textCol1);
			this.fontRendererObj.drawString(this.selectedPlanet.getLocalizedName(), this.width / 2 - width / 2, 6, textCol2, false);

			if (this.selectedPlanet instanceof Moon)
			{
				this.fontRendererObj.drawString("Moon of " + ((Moon) this.selectedPlanet).getParentPlanet().getLocalizedName(), this.width / 2 - width / 2 + width + 10, 6, textCol2, false);
			}
		}

		super.drawScreen(par1, par2, par3);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		RenderHelper.disableStandardItemLighting();
	}

	private Vector3 drawCelestialBody(CelestialBody cBody, int centerX, int centerY)
	{
		int var42b = 0;
		int var41b = 0;

		if (cBody != null)
		{
			final Map[] posMaps2 = this.computePlanetPos(centerX, centerY, cBody.getRelativeDistanceFromCenter() * (cBody instanceof Planet ? 750.0F : 0.5F), 2880);

			if (posMaps2[0] != null && posMaps2[1] != null)
			{
				if (posMaps2[0].get(MathHelper.floor_float(Sys.getTime() / (720F * cBody.getRelativeOrbitTime()) % 2880)) != null && posMaps2[1].get(MathHelper.floor_float(Sys.getTime() / 720F % 2880)) != null)
				{
					var42b = (int) Math.floor((Float) posMaps2[0].get(MathHelper.floor_float((cBody.getPhaseShift() + Sys.getTime() / (720F * 1 * cBody.getRelativeOrbitTime())) % 2880)));
					var41b = (int) Math.floor((Float) posMaps2[1].get(MathHelper.floor_float((cBody.getPhaseShift() + Sys.getTime() / (720F * 1 * cBody.getRelativeOrbitTime())) % 2880)));
				}
			}

			this.checkSelection(cBody, centerX + var42b, centerY + var41b);

			if (cBody.getPlanetIcon() != null)
			{
				this.mc.renderEngine.bindTexture(cBody.getPlanetIcon());

				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

				GuiGalaxyMap.renderPlanet(0, centerX + var42b, centerY + var41b, (float) (cBody.getRelativeSize() * 40.0F + 1 / Math.pow(this.zoom, -2)), Tessellator.instance);

				if (this.selectedPlanet != null && cBody.equals(this.selectedPlanet))
				{
					GuiGalaxyMap.renderPlanet(0, centerX + var42b, centerY + var41b, (float) (cBody.getRelativeSize() * 40.0F + 1 / Math.pow(this.zoom, -2)), Tessellator.instance);
					this.drawInfoBox(centerX + var42b, centerY + var41b, cBody);
				}
			}
		}

		return new Vector3(var42b - centerX, var41b - centerY, 0);
	}

	private void checkSelection(CelestialBody cBody, int centerX, int centerY)
	{
		if (Mouse.isButtonDown(0))
		{
			int width = (int) (cBody.getRelativeSize() + 1 / this.zoom * 3F);
			final int pointerMinX = this.width / 2 - 5;
			final int pointerMaxX = this.width / 2 + 5;
			final int pointerMinY = this.height / 2 - 5;
			final int pointerMaxY = this.height / 2 + 5;
			final int planetMinX = centerX - width;
			final int planetMaxX = centerX + width;
			final int planetMinY = centerY - width;
			final int planetMaxY = centerY + width;

			if ((pointerMaxX >= planetMinX && pointerMinX <= planetMinX || pointerMinX <= planetMinX && pointerMaxY >= planetMaxX || pointerMinX >= planetMinX && pointerMinX <= planetMaxX) && (pointerMaxY >= planetMinY && pointerMinY <= planetMinY || pointerMinY <= planetMinY && pointerMaxY >= planetMaxY || pointerMinY >= planetMinY && pointerMinY <= planetMaxY))
			{
				this.selectedPlanet = cBody;
			}
		}
	}

	public void renderSun(int x, int y)
	{
		this.mc.renderEngine.bindTexture(vanillaSun);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

		GuiGalaxyMap.renderPlanet(0, x, y, (float) (1000.0F + 1 / Math.pow(this.zoom, -2)), Tessellator.instance);
	}
	
	public static void renderPlanet(int index, int x, int y, float slotHeight, Tessellator tessellator)
	{
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x + 12 - slotHeight, y - 11 + slotHeight, -90.0D, 0.0, 1.0);
		tessellator.addVertexWithUV(x + 12, y - 11 + slotHeight, -90.0D, 1.0, 1.0);
		tessellator.addVertexWithUV(x + 12, y - 11, -90.0D, 1.0, 0.0);
		tessellator.addVertexWithUV(x + 12 - slotHeight, y - 11, -90.0D, 0.0, 0.0);
		tessellator.draw();
	}

	public void drawCircles(Planet planet, float cx, float cy)
	{
		final float theta = (float) (2 * Math.PI / 500);
		final float c = (float) Math.cos(theta);
		final float s = (float) Math.sin(theta);
		float t;

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);

		float x = planet.getRelativeDistanceFromCenter() * 750.0F;
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

	public void drawAsteroidBelt(float cx, float cy)
	{
		final Random rand = new Random();
		rand.setSeed(1234);

		GL11.glColor4f(139 / 255F, 69 / 255F, 19 / 255F, 1.0F);

		for (int i = 1; i < 15; i++)
		{
			final float theta = (float) (2 * Math.PI / (15 * i));
			final float c = (float) Math.cos(theta);
			final float s = (float) Math.sin(theta);
			float t;

			float x;

			if (i < 8)
			{
				x = (float) (4400F + Math.pow(2.5, i));
			}
			else
			{
				x = (float) (4400F - Math.pow(2.5, 15 - i));
			}

			float y = 0;

			for (int ii = 0; ii < 15 * i; ii++)
			{
				final Tessellator var0 = Tessellator.instance;

				var0.startDrawingQuads();
				var0.addVertex(x + cx - 2 + rand.nextInt(50), y + cy + 2 + rand.nextInt(50), -90.0D);
				var0.addVertex(x + cx + 2 + rand.nextInt(50), y + cy + 2 + rand.nextInt(50), -90.0D);
				var0.addVertex(x + cx + 2 + rand.nextInt(50), y + cy - 2 + rand.nextInt(50), -90.0D);
				var0.addVertex(x + cx - 2 + rand.nextInt(50), y + cy - 2 + rand.nextInt(50), -90.0D);
				var0.draw();

				t = x;
				x = c * x - s * y;
				y = s * t + c * y;
			}
		}
	}

	private void drawInfoBox(int cx, int cy, CelestialBody planet)
	{
		// for (final IMapPlanet planet : GalacticraftCore.mapPlanets)
		{
			this.drawGradientRect(cx + MathHelper.floor_float(planet.getRelativeSize() * 15.0F), cy - 3, cx + MathHelper.floor_float(planet.getRelativeSize() * 15.0F) + 8 * 10, cy + 10, GCCoreUtil.to32BitColor(100, 50, 50, 50), GCCoreUtil.to32BitColor(100, 50, 50, 50));
			this.fontRendererObj.drawStringWithShadow(planet.getLocalizedName(), cx + MathHelper.floor_float(planet.getRelativeSize() * 15.0F) + 3, cy - 1, GCCoreUtil.to32BitColor(255, 200, 200, 200));

			for (int i = 0; i < WorldUtil.getPlayersOnPlanet(planet).size(); i++)
			{
				this.drawGradientRect(cx + MathHelper.floor_float(planet.getRelativeSize() * 15.0F), cy + 10 * (i + 1), cx + MathHelper.floor_float(planet.getRelativeSize() * 15.0F) + 80, cy + 10 * (i + 1) + 10, GCCoreUtil.to32BitColor(255, 50, 50, 50), GCCoreUtil.to32BitColor(255, 50, 50, 50));
				this.fontRendererObj.drawStringWithShadow(String.valueOf(WorldUtil.getPlayersOnPlanet(planet).get(i)), cx + MathHelper.floor_float(planet.getRelativeSize() * 30.0F), cy + 1 + 10 * (i + 1), GCCoreUtil.to32BitColor(255, 220, 220, 220));
				this.width = Math.max(this.width, String.valueOf(WorldUtil.getPlayersOnPlanet(planet).get(i)).length());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Map<Integer, Float>[] computePlanetPos(float cx, float cy, float r, float stretch)
	{
		final Map<Integer, Float> mapX = new HashMap<Integer, Float>();
		final Map<Integer, Float> mapY = new HashMap<Integer, Float>();

		final float theta = (float) (2 * Math.PI / stretch);
		final float c = (float) Math.cos(theta);
		final float s = (float) Math.sin(theta);
		float t;

		float x = r;
		float y = 0;

		for (int ii = 0; ii < stretch; ii++)
		{
			mapX.put(ii, x + cx);
			mapY.put(ii, y + cy);

			t = x;
			x = c * x - s * y;
			y = s * t + c * y;
		}

		return new Map[] { mapX, mapY };
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return true;
	}

	private void zoom(float f)
	{
		final ScaledResolution var5 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		final int x = var5.getScaledWidth();
		final int y = var5.getScaledHeight();

		GL11.glTranslatef(x / 2, y / 2, 0);
		GL11.glScalef(f, f, 0);
		GL11.glTranslatef(-(x / 2), -(y / 2), 0);
	}

	@Override
	public void doCustomTranslation(int type, float coord1, float coord2, float coord3, float mX, float mY)
	{
		switch (type)
		{
		case 0:
			GL11.glTranslatef(coord1 - mX / (50F / this.zoom), coord2 - mY / (50F / this.zoom), coord3 + 0.5F);
			final float i = MathHelper.clamp_float(7 * (this.zoom / 1.1F), 3F, 9F);
			GL11.glScalef(i, i, i);
			return;
		case 1:
			GL11.glTranslatef(coord1 - mX / (200F / this.zoom), coord2 - mY / (200F / this.zoom), coord3 + 0.5F);
			final float i2 = MathHelper.clamp_float(7 * (this.zoom / 1.1F), 3F, 9F);
			GL11.glScalef(i2 / 3F, i2 / 3F, i2 / 3F);
			return;
		}
	}

	// public float getLargestOrbit(IGalaxy galaxy)
	// {
	// float orbit = -1F;
	//
	// for (final IGalacticraftSubMod mod : GalacticraftCore.subMods)
	// {
	// if (mod.getParentGalaxy() == galaxy)
	// {
	// for (final IGalacticraftSubModClient client :
	// GalacticraftCore.clientSubMods)
	// {
	// if (mod.getDimensionName().equals(client.getDimensionName()))
	// {
	// if (client.getPlanetForMap().getDistanceFromCenter() > orbit)
	// {
	// orbit = client.getPlanetForMap().getDistanceFromCenter();
	// }
	// }
	// }
	// }
	// }
	//
	// return orbit;
	// }
}