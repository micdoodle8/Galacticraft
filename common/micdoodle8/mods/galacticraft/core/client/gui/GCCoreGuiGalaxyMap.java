package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ICelestialBody;
import micdoodle8.mods.galacticraft.api.world.ICelestialBodyRenderer;
import micdoodle8.mods.galacticraft.api.world.IGalaxy;
import micdoodle8.mods.galacticraft.api.world.IMapObject;
import micdoodle8.mods.galacticraft.api.world.IMoon;
import micdoodle8.mods.galacticraft.api.world.IPlanet;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
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
public class GCCoreGuiGalaxyMap extends GCCoreGuiStarBackground
{
	private static final ResourceLocation guiTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/gui.png");

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

	private ICelestialBody selectedPlanet = null;

	private String[] listOfDimensions;

	public GCCoreGuiGalaxyMap(EntityPlayer player)
	{
		this.player = player;
	}

	public GCCoreGuiGalaxyMap(EntityPlayer player, String[] listOfDimensions)
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
		if (par2 == this.mc.gameSettings.keyBindInventory.keyCode)
		{
			this.mc.displayGuiScreen((GuiScreen) null);
			this.mc.setIngameFocus();
		}
		else if (par2 == Keyboard.KEY_ESCAPE)
		{
			if (this.listOfDimensions != null)
			{
				FMLClientHandler.instance().getClient().currentScreen = null;
				FMLClientHandler.instance().getClient().displayGuiScreen(new GCCoreGuiChoosePlanet(this.player, this.listOfDimensions));
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
		GCCoreGuiGalaxyMap.guiMapMinX = -250000 - this.width;
		GCCoreGuiGalaxyMap.guiMapMaxX = 250000 + this.width;
		GCCoreGuiGalaxyMap.guiMapMinY = -250000 - this.height;
		GCCoreGuiGalaxyMap.guiMapMaxY = 250000 + this.height;

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

		if (GCCoreConfigManager.wasdMapMovement)
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

		if (this.field_74124_q < GCCoreGuiGalaxyMap.guiMapMinX)
		{
			this.field_74124_q = GCCoreGuiGalaxyMap.guiMapMinX;

			if (this.mouseX > 0)
			{
				this.mouseX = 0;
			}
		}

		if (this.field_74123_r < GCCoreGuiGalaxyMap.guiMapMinY)
		{
			this.field_74123_r = GCCoreGuiGalaxyMap.guiMapMinY;

			if (this.mouseY > 0)
			{
				this.mouseY = 0;
			}
		}

		if (this.field_74124_q >= GCCoreGuiGalaxyMap.guiMapMaxX)
		{
			this.field_74124_q = GCCoreGuiGalaxyMap.guiMapMaxX - 1;

			if (this.mouseX < 0)
			{
				this.mouseX = 0;
			}
		}

		if (this.field_74123_r >= GCCoreGuiGalaxyMap.guiMapMaxY)
		{
			this.field_74123_r = GCCoreGuiGalaxyMap.guiMapMaxY - 1;

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
	// this.fontRenderer.drawString("Galaxy Map", 15, 5, 4210752);
	// }

	@SuppressWarnings("rawtypes")
	protected void genAchievementBackground(int par1, int par2, float par3)
	{
		final ScaledResolution var13 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		var13.getScaledWidth();
		var13.getScaledHeight();
		int var4 = MathHelper.floor_double(this.field_74117_m + (this.guiMapX - this.field_74117_m) * par3);
		int var5 = MathHelper.floor_double(this.field_74115_n + (this.guiMapY - this.field_74115_n) * par3);

		if (var4 < GCCoreGuiGalaxyMap.guiMapMinX)
		{
			var4 = GCCoreGuiGalaxyMap.guiMapMinX;
		}

		if (var5 < GCCoreGuiGalaxyMap.guiMapMinY)
		{
			var5 = GCCoreGuiGalaxyMap.guiMapMinY;
		}

		if (var4 >= GCCoreGuiGalaxyMap.guiMapMaxX)
		{
			var4 = GCCoreGuiGalaxyMap.guiMapMaxX - 1;
		}

		if (var5 >= GCCoreGuiGalaxyMap.guiMapMaxY)
		{
			var5 = GCCoreGuiGalaxyMap.guiMapMaxY - 1;
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

		for (IGalaxy galaxy : GalacticraftRegistry.getGalaxyList())
		{
			final int var10 = -var4 + galaxy.getXCoord() * 10000;
			final int var11 = -var5 + galaxy.getYCoord() * 10000;

			this.drawCircles(galaxy, var10 + var10, var11 + var11);

			for (IPlanet thePlanet : GalacticraftCore.mapPlanets)
			{
				IMapObject planet = thePlanet.getMapObject();

				if (thePlanet.getParentGalaxy() != null && thePlanet.getParentGalaxy() == galaxy)
				{
					var26 = 0;
					var27 = 0;

					final Map[] posMaps = this.computePlanetPos(var10, var11, planet.getDistanceFromCenter() * 750.0F, 2880);

					if (posMaps[0] != null && posMaps[1] != null)
					{
						if (posMaps[0].get(MathHelper.floor_float(Sys.getTime() / (720F * 1 * planet.getStretchValue()) % 2880)) != null && posMaps[1].get(MathHelper.floor_float(Sys.getTime() / 720F % 2880)) != null)
						{
							final int x = MathHelper.floor_float((Float) posMaps[0].get(MathHelper.floor_float((planet.getPhaseShift() + Sys.getTime() / (720F * 1 * planet.getStretchValue())) % 2880)));
							final int y = MathHelper.floor_float((Float) posMaps[1].get(MathHelper.floor_float((planet.getPhaseShift() + Sys.getTime() / (720F * 1 * planet.getStretchValue())) % 2880)));

							var26 = x;
							var27 = y;
						}
					}

					var42 = var10 + var26;
					var41 = var11 + var27;

					final ICelestialBodyRenderer renderer = planet.getSlotRenderer();

					GL11.glDisable(GL11.GL_BLEND);

					GL11.glEnable(GL11.GL_DEPTH_TEST);

					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

					int width = (int) (planet.getPlanetSize() * 15.0F + 1 / this.zoom * 3F);

					if (Mouse.isButtonDown(0))
					{
						final int pointerMinX = this.width / 2 - 5;
						final int pointerMaxX = this.width / 2 + 5;
						final int pointerMinY = this.height / 2 - 5;
						final int pointerMaxY = this.height / 2 + 5;
						final int planetMinX = var42 - width;
						final int planetMaxX = var42 + width;
						final int planetMinY = var41 - width;
						final int planetMaxY = var41 + width;

						if ((pointerMaxX >= planetMinX && pointerMinX <= planetMinX || pointerMinX <= planetMinX && pointerMaxY >= planetMaxX || pointerMinX >= planetMinX && pointerMinX <= planetMaxX) && (pointerMaxY >= planetMinY && pointerMinY <= planetMinY || pointerMinY <= planetMinY && pointerMaxY >= planetMaxY || pointerMinY >= planetMinY && pointerMinY <= planetMaxY))
						{
							if (!planet.getSlotRenderer().getPlanetName().equals("Sun"))
							{
								if (this.zoom <= 0.2)
								{
									if (!GalacticraftCore.mapMoons.containsValue(planet))
									{
										this.selectedPlanet = thePlanet;
									}
								}
								else
								{
									this.selectedPlanet = thePlanet;
								}
							}
						}
					}

					final Tessellator var3 = Tessellator.instance;

					if (renderer != null)
					{
						this.mc.renderEngine.bindTexture(renderer.getPlanetSprite());
						renderer.renderSlot(0, var42, var41, planet.getPlanetSize() * 15.0F + 1 / this.zoom * 3F, var3);

						if (this.selectedPlanet != null && planet.getSlotRenderer().getPlanetName().equals(this.selectedPlanet.getMapObject().getSlotRenderer().getPlanetName()))
						{
							renderer.renderSlot(0, var42, var41, planet.getPlanetSize() * 15.0F + 1 / this.zoom * 3F, var3);
						}
					}

					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

					for (final Map.Entry<IPlanet, ArrayList<IMoon>> e : GalacticraftCore.mapMoons.entrySet())
					{
						if (e.getKey().equals(thePlanet))
						{
							final List<IMoon> moonList = e.getValue();

							for (final IMoon theMoon : moonList)
							{
								IMapObject moon = theMoon.getMapObject();

								int var26b = 0;
								int var27b = 0;

								int var42b = 0;
								int var41b = 0;

								if (moon != null)
								{
									final Map[] posMaps2 = this.computePlanetPos(var42, var41, moon.getDistanceFromCenter() / 2, 2880);

									if (posMaps2[0] != null && posMaps2[1] != null)
									{
										if (posMaps2[0].get(MathHelper.floor_float(Sys.getTime() / (720F * moon.getStretchValue()) % 2880)) != null && posMaps2[1].get(MathHelper.floor_float(Sys.getTime() / 720F % 2880)) != null)
										{
											final int x = MathHelper.floor_float((Float) posMaps2[0].get(MathHelper.floor_float((moon.getPhaseShift() + Sys.getTime() / (720F * 1 * moon.getStretchValue())) % 2880)));
											final int y = MathHelper.floor_float((Float) posMaps2[1].get(MathHelper.floor_float((moon.getPhaseShift() + Sys.getTime() / (720F * 1 * moon.getStretchValue())) % 2880)));

											var26b = x;
											var27b = y;
										}
									}

									var42b = var26b;
									var41b = var27b;

									width = (int) (moon.getPlanetSize() + 1 / this.zoom * 3F);

									if (Mouse.isButtonDown(0))
									{
										final int pointerMinX = this.width / 2 - 5;
										final int pointerMaxX = this.width / 2 + 5;
										final int pointerMinY = this.height / 2 - 5;
										final int pointerMaxY = this.height / 2 + 5;
										final int planetMinX = var42b - width;
										final int planetMaxX = var42b + width;
										final int planetMinY = var41b - width;
										final int planetMaxY = var41b + width;

										if ((pointerMaxX >= planetMinX && pointerMinX <= planetMinX || pointerMinX <= planetMinX && pointerMaxY >= planetMaxX || pointerMinX >= planetMinX && pointerMinX <= planetMaxX) && (pointerMaxY >= planetMinY && pointerMinY <= planetMinY || pointerMinY <= planetMinY && pointerMaxY >= planetMaxY || pointerMinY >= planetMinY && pointerMinY <= planetMaxY))
										{
											this.selectedPlanet = theMoon;
										}
									}

									final ICelestialBodyRenderer moonRenderer = moon.getSlotRenderer();

									if (moonRenderer != null)
									{
										this.mc.renderEngine.bindTexture(moonRenderer.getPlanetSprite());
										moonRenderer.renderSlot(0, var42b, var41b, (float) (moon.getPlanetSize() * 15.0F + 1 / Math.pow(this.zoom, -2)), var3);

										if (this.selectedPlanet != null && moon.getSlotRenderer().getPlanetName().equals(this.selectedPlanet.getMapObject().getSlotRenderer().getPlanetName()))
										{
											moonRenderer.renderSlot(0, var42b, var41b, (float) (moon.getPlanetSize() * 15.0F + 1 / Math.pow(this.zoom, -2)), var3);
										}
									}

									if (this.selectedPlanet != null && moon.getSlotRenderer().getPlanetName().equals(this.selectedPlanet.getMapObject().getSlotRenderer().getPlanetName()))
									{
										this.drawInfoBox(var42b, var41b, moon);
									}
								}
							}
						}
					}

					// this.drawAsteroidBelt(var10 + var10, var11 + var11);

					if (!planet.getSlotRenderer().getPlanetName().equals("Sun"))
					{
					}

					if (this.selectedPlanet != null && planet.getSlotRenderer().getPlanetName().equals(this.selectedPlanet.getMapObject().getSlotRenderer().getPlanetName()))
					{
						this.drawInfoBox(var42, var41, planet);
					}

					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

					GL11.glDepthMask(true);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				}
			}

		}

		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		this.zLevel = 0.0F;
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		this.mc.getTextureManager().bindTexture(GCCoreGuiGalaxyMap.guiTexture);
		this.drawTexturedModalRect(this.width / 2 - 5, this.height / 2 - 5, 123, 0, 10, 10);

		final int col = GCCoreUtil.convertTo32BitColor(255, 198, 198, 198);
		final int col2 = GCCoreUtil.convertTo32BitColor(255, 145, 145, 145);
		final int col3 = GCCoreUtil.convertTo32BitColor(255, 33, 33, 33);
		final int textCol1 = GCCoreUtil.convertTo32BitColor(255, 133, 133, 133);

		Gui.drawRect(0, 0, this.width, 20, col);
		Gui.drawRect(0, this.height - 24, this.width, this.height, col);
		Gui.drawRect(0, 0, 10, this.height, col);
		Gui.drawRect(this.width - 10, 0, this.width, this.height, col);

		Gui.drawRect(10, 20, this.width - 10, 22, col3);
		Gui.drawRect(10, this.height - 26, this.width - 10, this.height - 24, col2);
		Gui.drawRect(10, 20, 12, this.height - 24, col3);
		Gui.drawRect(this.width - 12, 0 + 20, this.width - 10, this.height - 24, col2);

		this.fontRenderer.drawString("ASWD - Move Map", 5, 1, textCol1, false);
		this.fontRenderer.drawString("Mouse Wheel - Zoom", 5, 10, textCol1, false);
		this.fontRenderer.drawString("Mouse 1 - Select Planet", this.width - this.fontRenderer.getStringWidth("Mouse 1 - Select Planet") - 5, 1, textCol1, false);
		this.fontRenderer.drawString("Esc - Exit Map", this.width - this.fontRenderer.getStringWidth("Esc - Exit Map") - 5, 10, textCol1, false);

		if (this.selectedPlanet != null)
		{
			final Vector3 vecCol = this.selectedPlanet.getMapObject().getParentGalaxy().getRGBRingColors();
			final int textCol2 = GCCoreUtil.convertTo32BitColor(255, MathHelper.floor_double(vecCol.x) * 255, MathHelper.floor_double(vecCol.z) * 255, MathHelper.floor_double(vecCol.y) * 255);
			final int width = this.fontRenderer.getStringWidth(this.selectedPlanet.getMapObject().getSlotRenderer().getPlanetName());
			Gui.drawRect(this.width / 2 - width / 2 - 4, 4, this.width / 2 + width / 2 + 4, 15, textCol1);
			this.fontRenderer.drawString(this.selectedPlanet.getMapObject().getSlotRenderer().getPlanetName(), this.width / 2 - width / 2, 6, textCol2, false);

			for (int j = 0; j < GalacticraftCore.mapPlanets.size(); j++)
			{
				IPlanet planet = GalacticraftCore.mapPlanets.get(j);

				if (planet != null)
				{
					for (final Map.Entry<IPlanet, ArrayList<IMoon>> e : GalacticraftCore.mapMoons.entrySet())
					{
						final List<IMoon> moonList = e.getValue();

						for (IMoon moon : moonList)
						{
							if (this.selectedPlanet.equals(moon) && e.getKey().equals(planet))
							{
								this.fontRenderer.drawString("Moon of " + planet.getMapObject().getSlotRenderer().getPlanetName(), this.width / 2 - width / 2 + width + 10, 6, textCol2, false);
							}
						}
					}
				}
			}
		}

		super.drawScreen(par1, par2, par3);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		RenderHelper.disableStandardItemLighting();
	}

	public void drawCircles(IGalaxy galaxy, float cx, float cy)
	{
		final float theta = (float) (2 * Math.PI / 500);
		final float c = (float) Math.cos(theta);
		final float s = (float) Math.sin(theta);
		float t;

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);

		for (IPlanet planet : GalacticraftCore.mapPlanets)
		{
			if (planet.getMapObject().getParentGalaxy() == galaxy)
			{
				float x = planet.getMapObject().getDistanceFromCenter() * 750.0F;
				float y = 0;

				GL11.glColor4f((float) galaxy.getRGBRingColors().x, (float) galaxy.getRGBRingColors().y, (float) galaxy.getRGBRingColors().z, 1.0F);

				GL11.glBegin(GL11.GL_LINE_LOOP);

				for (int ii = 0; ii < 500; ii++)
				{
					GL11.glVertex2f(x + cx, y + cy);

					t = x;
					x = c * x - s * y;
					y = s * t + c * y;
				}

				GL11.glEnd();
			}
		}

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

	private void drawInfoBox(int cx, int cy, IMapObject planet)
	{
		// for (final IMapPlanet planet : GalacticraftCore.mapPlanets)
		{
			this.drawGradientRect(cx + MathHelper.floor_float(planet.getPlanetSize() * 15.0F), cy - 3, cx + MathHelper.floor_float(planet.getPlanetSize() * 15.0F) + 8 * 10, cy + 10, GCCoreUtil.convertTo32BitColor(100, 50, 50, 50), GCCoreUtil.convertTo32BitColor(100, 50, 50, 50));
			this.fontRenderer.drawStringWithShadow(planet.getSlotRenderer().getPlanetName(), cx + MathHelper.floor_float(planet.getPlanetSize() * 15.0F) + 3, cy - 1, GCCoreUtil.convertTo32BitColor(255, 200, 200, 200));

			for (int i = 0; i < WorldUtil.getPlayersOnPlanet(planet).size(); i++)
			{
				this.drawGradientRect(cx + MathHelper.floor_float(planet.getPlanetSize() * 15.0F), cy + 10 * (i + 1), cx + MathHelper.floor_float(planet.getPlanetSize() * 15.0F) + 80, cy + 10 * (i + 1) + 10, GCCoreUtil.convertTo32BitColor(255, 50, 50, 50), GCCoreUtil.convertTo32BitColor(255, 50, 50, 50));
				this.fontRenderer.drawStringWithShadow(String.valueOf(WorldUtil.getPlayersOnPlanet(planet).get(i)), cx + MathHelper.floor_float(planet.getPlanetSize() * 30.0F), cy + 1 + 10 * (i + 1), GCCoreUtil.convertTo32BitColor(255, 220, 220, 220));
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
