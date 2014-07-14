package micdoodle8.mods.galacticraft.core.client.gui.screen;

import com.google.common.collect.Maps;
import micdoodle8.mods.galacticraft.api.event.client.CelestialBodyRenderEvent;
import micdoodle8.mods.galacticraft.api.galaxies.*;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class GuiCelestialSelection extends GuiScreen
{
	private float zoom = 0.0F;
	private float planetZoom = 0.0F;
	private boolean doneZooming = false;
	private float preSelectZoom = 0.0F;
	public static ResourceLocation guiMain = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/celestialselection.png");
	private int ticksSinceSelection = 0;
	private int ticksSinceUnselection = -1;
	private Vector2f position = new Vector2f(0, 0);
	private Map<CelestialBody, Vector3f> planetPosMap = Maps.newHashMap();
	private Map<CelestialBody, Integer> celestialBodyTicks = Maps.newHashMap();
	private CelestialBody selectedBody;
	private CelestialBody lastSelectedBody;
	private static int BORDER_WIDTH = 0;
	private static int BORDER_EDGE_WIDTH = 0;

	@Override
	public void initGui()
	{
		for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
		{
			this.celestialBodyTicks.put(planet, 0);
		}

		for (Moon moon : GalaxyRegistry.getRegisteredMoons().values())
		{
			this.celestialBodyTicks.put(moon, 0);
		}

		GuiCelestialSelection.BORDER_WIDTH = this.width / 65;
		GuiCelestialSelection.BORDER_EDGE_WIDTH = GuiCelestialSelection.BORDER_WIDTH / 4;
	}

	private float lerp(float v0, float v1, float t)
	{
		return v0 + t * (v1 - v0);
	}

	private Vector2f lerpVec2(Vector2f v0, Vector2f v1, float t)
	{
		return new Vector2f(v0.x + t * (v1.x - v0.x), v0.y + t * (v1.y - v0.y));
	}

	private float getZoomAdvanced()
	{
		if (this.selectedBody == null)
		{
			if (this.ticksSinceUnselection > 0)
			{
				float unselectScale = this.lerp(this.zoom, this.preSelectZoom, Math.max(0.0F, Math.min(this.ticksSinceUnselection / 100.0F, 1.0F)));

				if (unselectScale <= this.preSelectZoom + 0.05F)
				{
					this.zoom = this.preSelectZoom;
					this.preSelectZoom = 0.0F;
					this.ticksSinceUnselection = -1;
				}

				return unselectScale;
			}

			return this.zoom;
		}

		if (!this.doneZooming)
		{
			float f = this.lerp(this.zoom, 12, Math.max(0.0F, Math.min((this.ticksSinceSelection - 20) / 40.0F, 1.0F)));

			if (f >= 11.95F)
			{
				this.doneZooming = true;
			}

			return f;
		}

		return 12 + this.planetZoom;
	}

	private Vector2f getTranslationAdvanced(float partialTicks)
	{
		if (this.selectedBody == null)
		{
			if (this.ticksSinceUnselection > 0)
			{
				return this.lerpVec2(this.position, new Vector2f(0, 0), Math.max(0.0F, Math.min((this.ticksSinceUnselection + partialTicks) / 100.0F, 1.0F)));
			}

			return this.position;
		}

		Vector3f posVec = this.getCelestialBodyPosition(this.selectedBody);
		return this.lerpVec2(this.position, new Vector2f(posVec.x, posVec.y), Math.max(0.0F, Math.min((this.ticksSinceSelection + partialTicks - 18) / 7.5F, 1.0F)));
	}

	@Override
	protected void keyTyped(char keyChar, int keyID)
	{
		// Override and do nothing, so it isn't possible to exit the GUI

		if (keyID == 1)
		{
			if (this.selectedBody != null)
			{
				this.unselectCelestialBody();
			}
		}

		// Keyboard shortcut - teleport to dimension by pressing 'Enter'
		if (keyID == 28 && this.selectedBody != null)
		{
			final String dimension = WorldProvider.getProviderForDimension(this.selectedBody.getDimensionID()).getDimensionName();
			if (dimension.contains("$"))
			{
				this.mc.gameSettings.thirdPersonView = 0;
			}
			GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_TELEPORT_ENTITY, new Object[] { dimension }));
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
			return;
		}
		
		// Temporarily allow to get to Space Station by pressing 'X'
		if (keyID == 45)
		{
			final String dimension = WorldProvider.getProviderForDimension(2).getDimensionName();
			if (dimension.contains("$"))
			{
				this.mc.gameSettings.thirdPersonView = 0;
			}
			GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_TELEPORT_ENTITY, new Object[] { dimension }));
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
		}
	}

	private void unselectCelestialBody()
	{
		this.ticksSinceUnselection = 0;
		this.lastSelectedBody = this.selectedBody;
		this.selectedBody = null;
	}

	@Override
	public void updateScreen()
	{
		if (Mouse.hasWheel())
		{
			float wheel = Mouse.getDWheel() / (this.selectedBody == null ? 500.0F : 250.0F);

			if (wheel != 0)
			{
				if (this.selectedBody == null)
				{
					this.zoom = Math.min(Math.max(this.zoom + wheel, -0.5F), 3);
				}
				else
				{
					this.planetZoom = Math.min(Math.max(this.planetZoom + wheel, -4.9F), 5);
				}
			}
		}

		for (CelestialBody e : this.celestialBodyTicks.keySet())
		{
			if (!(e instanceof Planet && e == this.selectedBody) && !(e instanceof Planet && this.selectedBody instanceof Moon && GalaxyRegistry.getMoonsForPlanet((Planet) e).contains(this.selectedBody)))
			{
				Integer i = this.celestialBodyTicks.get(e);

				if (i != null)
				{
					i++;
				}

				this.celestialBodyTicks.put(e, i);
			}
		}

		if (this.selectedBody != null)
		{
			this.ticksSinceSelection++;
		}

		if (this.ticksSinceUnselection >= 0 && this.selectedBody == null)
		{
			this.ticksSinceUnselection++;
		}
		else
		{
			this.ticksSinceUnselection = -1;
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int button)
	{
		super.mouseClicked(x, y, button);

		if (this.selectedBody != null && x > BORDER_WIDTH + BORDER_EDGE_WIDTH && x < BORDER_WIDTH + BORDER_EDGE_WIDTH + 88 && y > BORDER_WIDTH + BORDER_EDGE_WIDTH && y < BORDER_WIDTH + BORDER_EDGE_WIDTH + 13)
		{
			this.unselectCelestialBody();
			return;
		}

        if (this.selectedBody != null && x > width - BORDER_WIDTH - BORDER_EDGE_WIDTH - 88 && x < width - BORDER_WIDTH - BORDER_EDGE_WIDTH && y > BORDER_WIDTH + BORDER_EDGE_WIDTH && y < BORDER_WIDTH + BORDER_EDGE_WIDTH + 13)
        {
            try
            {
                final String dimension = WorldProvider.getProviderForDimension(this.selectedBody.getDimensionID()).getDimensionName();
                if (dimension.contains("$"))
                {
                    this.mc.gameSettings.thirdPersonView = 0;
                }
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_TELEPORT_ENTITY, new Object[] { dimension }));
                mc.displayGuiScreen(null);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return;
        }

		// Need unscaled mouse coords
		int mouseX = Mouse.getX();
		int mouseY = Mouse.getY() * -1 + Minecraft.getMinecraft().displayHeight - 1;

		for (Map.Entry<CelestialBody, Vector3f> e : this.planetPosMap.entrySet())
		{
			if (this.selectedBody == null && e.getKey() instanceof Moon)
			{
				continue;
			}

			int iconSize = (int) e.getValue().z; // Z value holds size on-screen

			if (mouseX >= e.getValue().x - iconSize && mouseX <= e.getValue().x + iconSize && mouseY >= e.getValue().y - iconSize && mouseY <= e.getValue().y + iconSize)
			{
				if (this.selectedBody != e.getKey())
				{
					if (this.selectedBody == null)
					{
						this.preSelectZoom = this.zoom;
					}

					this.doneZooming = false;
					this.planetZoom = 0.0F;
					this.lastSelectedBody = this.selectedBody;
					this.selectedBody = e.getKey();
					this.ticksSinceSelection = 0;
					break;
				}
			}
		}
	}

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

	@Override
	public void drawScreen(int mousePosX, int mousePosY, float partialTicks)
	{
		GL11.glPushMatrix();

		Matrix4f camMatrix = new Matrix4f();
		Matrix4f.translate(new Vector3f(0.0F, 0.0F, -2000.0F), camMatrix, camMatrix); // See EntityRenderer.java:setupOverlayRendering
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.m00 = 2.0F / (float) width;
		viewMatrix.m11 = 2.0F / (float) -height;
		viewMatrix.m22 = -2.0F / 2000.0F;
		viewMatrix.m30 = -1.0F;
		viewMatrix.m31 = 1.0F;
		viewMatrix.m32 = -2.0F;

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		FloatBuffer fb = BufferUtils.createFloatBuffer(16 * Float.SIZE);
		fb.rewind();
		viewMatrix.store(fb);
		fb.flip();
		GL11.glMultMatrix(fb);
		fb.clear();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		fb.rewind();
		camMatrix.store(fb);
		fb.flip();
		fb.clear();
		GL11.glMultMatrix(fb);

		this.setBlackBackground();

		GL11.glPushMatrix();
		Matrix4f worldMatrix = this.setIsometric(partialTicks);
		this.drawGrid(height / 3, height / 3 / 3.5F);
		this.drawCircles();
		GL11.glPopMatrix();

		HashMap<CelestialBody, Matrix4f> matrixMap = this.drawCelestialBodies(worldMatrix);

		for (Map.Entry<CelestialBody, Matrix4f> e : matrixMap.entrySet())
		{
			Matrix4f planetMatrix = e.getValue();
			Matrix4f matrix0 = Matrix4f.mul(viewMatrix, planetMatrix, planetMatrix);
			int x = (int) Math.floor((matrix0.m30 * 0.5 + 0.5) * Minecraft.getMinecraft().displayWidth);
			int y = (int) Math.floor(Minecraft.getMinecraft().displayHeight - (matrix0.m31 * 0.5 + 0.5) * Minecraft.getMinecraft().displayHeight);
			Vector2f vec = new Vector2f(x, y);

			Matrix4f scaleVec = new Matrix4f();
			scaleVec.m00 = matrix0.m00;
			scaleVec.m11 = matrix0.m11;
			scaleVec.m22 = matrix0.m22;
			Vector4f newVec = Matrix4f.transform(scaleVec, new Vector4f(2, -2, 0, 0), null);
			int iconSize = (int) (newVec.y * (Minecraft.getMinecraft().displayHeight / 2.0F)) * (e.getKey() instanceof Star ? 2 : 1);

			this.planetPosMap.put(e.getKey(), new Vector3f(vec.x, vec.y, iconSize)); // Store size on-screen in Z-value for ease
		}

		if (this.selectedBody != null)
		{
			GL11.glPushMatrix();
			Matrix4f worldMatrix0 = new Matrix4f(worldMatrix);
			Matrix4f.translate(this.getCelestialBodyPosition(this.selectedBody), worldMatrix0, worldMatrix0);
			Matrix4f worldMatrix1 = new Matrix4f();
			Matrix4f.rotate((float) Math.toRadians(45), new Vector3f(0, 0, 1), worldMatrix1, worldMatrix1);
			Matrix4f.rotate((float) Math.toRadians(-55), new Vector3f(1, 0, 0), worldMatrix1, worldMatrix1);
			worldMatrix1 = Matrix4f.mul(worldMatrix0, worldMatrix1, worldMatrix1);
			fb.rewind();
			worldMatrix1.store(fb);
			fb.flip();
			GL11.glMultMatrix(fb);
			fb.clear();
			float scale = Math.max(0.3F, 1.5F / (this.ticksSinceSelection / 5.0F));
			float scale0 = this.getZoomAdvanced() < 0 || this.getZoomAdvanced() > 1 ? scale + this.planetZoom / 75.0F - 0.45F : scale;//(0.6F / this.getZoomAdvanced()) : scale;
			GL11.glScalef(scale0, scale0, 1);
			this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain);
			float colMod = this.getZoomAdvanced() < 4.9F ? (float) (Math.sin(this.ticksSinceSelection / 1.0F) * 0.5F + 0.5F) : 1.0F;
			GL11.glColor4f(0.4F, 0.8F, 1.0F, 1 * colMod);
			this.drawTexturedModalRect(-50, -50, 100, 100, 266, 29, 100, 100, false, false);
			GL11.glPopMatrix();
		}

		this.drawButtons(mousePosX, mousePosY);
		this.drawBorder();
		GL11.glPopMatrix();

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
	}

	private Vector3f getCelestialBodyPosition(CelestialBody cBody)
	{
		if (cBody instanceof Star)
		{
			return new Vector3f();
		}

		int cBodyTicks = this.celestialBodyTicks.get(cBody);
		float distanceScale = cBody instanceof Planet ? 25.0F : 1.0F / 8.0F;
		float timeScale = cBody instanceof Planet ? 200.0F : 2.0F;
		Vector3f cBodyPos = new Vector3f((float) Math.sin(cBodyTicks / timeScale * cBody.getRelativeOrbitTime() + cBody.getPhaseShift()) * cBody.getRelativeDistanceFromCenter() * distanceScale, (float) Math.cos(cBodyTicks / timeScale * cBody.getRelativeOrbitTime() + cBody.getPhaseShift()) * cBody.getRelativeDistanceFromCenter() * distanceScale, 0);

		if (cBody instanceof Moon)
		{
			Vector3f parentVec = this.getCelestialBodyPosition(((Moon) cBody).getParentPlanet());
			return Vector3f.add(cBodyPos, parentVec, null);
		}

		return cBodyPos;
	}

	public HashMap<CelestialBody, Matrix4f> drawCelestialBodies(Matrix4f worldMatrix)
	{
		GL11.glColor3f(1, 1, 1);
		FloatBuffer fb = BufferUtils.createFloatBuffer(16 * Float.SIZE);
		HashMap<CelestialBody, Matrix4f> matrixMap = Maps.newHashMap();

		for (SolarSystem solarSystem : GalaxyRegistry.getRegisteredSolarSystems().values())
		{
			Star star = solarSystem.getMainStar();

			if (star != null && star.getBodyIcon() != null)
			{
				GL11.glPushMatrix();
				Matrix4f worldMatrix0 = new Matrix4f(worldMatrix);

				Matrix4f.translate(this.getCelestialBodyPosition(star), worldMatrix0, worldMatrix0);

				Matrix4f worldMatrix1 = new Matrix4f();
				Matrix4f.rotate((float) Math.toRadians(45), new Vector3f(0, 0, 1), worldMatrix1, worldMatrix1);
				Matrix4f.rotate((float) Math.toRadians(-55), new Vector3f(1, 0, 0), worldMatrix1, worldMatrix1);
				worldMatrix1 = Matrix4f.mul(worldMatrix0, worldMatrix1, worldMatrix1);

				fb.rewind();
				worldMatrix1.store(fb);
				fb.flip();
				GL11.glMultMatrix(fb);

				float alpha = 1.0F;

				if (this.selectedBody != null && this.selectedBody != star)
				{
					alpha = 1.0F - Math.min(this.ticksSinceSelection / 25.0F, 1.0F);
				}

                if (this.selectedBody != null)
                {
                    if (star != this.selectedBody)
                    {
                        alpha = 1.0F - Math.min(this.ticksSinceSelection / 25.0F, 1.0F);

                        if (!(this.lastSelectedBody instanceof Star) && this.lastSelectedBody != null)
                        {
                            alpha = 0.0F;
                        }
                    }
                }

				if (alpha != 0)
				{
                    CelestialBodyRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.Pre(star, star.getBodyIcon(), 8);
                    MinecraftForge.EVENT_BUS.post(preEvent);

                    GL11.glColor4f(1, 1, 1, alpha);
                    this.mc.renderEngine.bindTexture(preEvent.celestialBodyTexture);

                    if (!preEvent.isCanceled())
                    {
                        this.drawTexturedModalRect(-4, -4, 8, 8, 0, 0, preEvent.textureSize, preEvent.textureSize, false, false, preEvent.textureSize);
                        matrixMap.put(star, worldMatrix1);
                    }

                    CelestialBodyRenderEvent.Post postEvent = new CelestialBodyRenderEvent.Post(star);
                    MinecraftForge.EVENT_BUS.post(postEvent);
				}

                fb.clear();
				GL11.glPopMatrix();
			}
		}

		for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
		{
			if (planet.getBodyIcon() != null)
			{
				GL11.glPushMatrix();
				Matrix4f worldMatrix0 = new Matrix4f(worldMatrix);

				Matrix4f.translate(this.getCelestialBodyPosition(planet), worldMatrix0, worldMatrix0);

				Matrix4f worldMatrix1 = new Matrix4f();
				Matrix4f.rotate((float) Math.toRadians(45), new Vector3f(0, 0, 1), worldMatrix1, worldMatrix1);
				Matrix4f.rotate((float) Math.toRadians(-55), new Vector3f(1, 0, 0), worldMatrix1, worldMatrix1);
				worldMatrix1 = Matrix4f.mul(worldMatrix0, worldMatrix1, worldMatrix1);

				fb.rewind();
				worldMatrix1.store(fb);
				fb.flip();
				GL11.glMultMatrix(fb);

				float alpha = 1.0F;

				if (this.selectedBody != null)
				{
					if (!(planet == this.selectedBody) && !(this.selectedBody instanceof Moon && GalaxyRegistry.getMoonsForPlanet(planet).contains(this.selectedBody)))
					{
						alpha = 1.0F - Math.min(this.ticksSinceSelection / 25.0F, 1.0F);

						if ((this.lastSelectedBody instanceof Moon && ((Moon) this.lastSelectedBody).getParentPlanet() != planet) || (this.lastSelectedBody instanceof Planet && this.lastSelectedBody != planet))
						{
							alpha = 0.0F;
						}
					}
				}

				if (alpha != 0)
				{
                    CelestialBodyRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.Pre(planet, planet.getBodyIcon(), 12);
                    MinecraftForge.EVENT_BUS.post(preEvent);

                    GL11.glColor4f(1, 1, 1, alpha);
                    this.mc.renderEngine.bindTexture(preEvent.celestialBodyTexture);

                    if (!preEvent.isCanceled())
                    {
                        this.drawTexturedModalRect(-2, -2, 4, 4, 0, 0, preEvent.textureSize, preEvent.textureSize, false, false, preEvent.textureSize);
                        matrixMap.put(planet, worldMatrix1);
                    }

                    CelestialBodyRenderEvent.Post postEvent = new CelestialBodyRenderEvent.Post(planet);
                    MinecraftForge.EVENT_BUS.post(postEvent);
				}

                fb.clear();
				GL11.glPopMatrix();
			}
		}

		if (this.selectedBody != null)
		{
			Matrix4f worldMatrix0 = new Matrix4f(worldMatrix);

			for (Moon moon : GalaxyRegistry.getRegisteredMoons().values())
			{
				if (moon == this.selectedBody || moon.getParentPlanet() == this.selectedBody && (this.ticksSinceSelection > 35 || this.lastSelectedBody instanceof Moon && GalaxyRegistry.getMoonsForPlanet(((Moon) this.lastSelectedBody).getParentPlanet()).contains(moon)))
				{
					GL11.glPushMatrix();
					Matrix4f worldMatrix1 = new Matrix4f(worldMatrix0);
					Matrix4f.translate(this.getCelestialBodyPosition(moon), worldMatrix1, worldMatrix1);

					Matrix4f worldMatrix2 = new Matrix4f();
					Matrix4f.rotate((float) Math.toRadians(45), new Vector3f(0, 0, 1), worldMatrix2, worldMatrix2);
					Matrix4f.rotate((float) Math.toRadians(-55), new Vector3f(1, 0, 0), worldMatrix2, worldMatrix2);
					Matrix4f.scale(new Vector3f(0.25F, 0.25F, 1.0F), worldMatrix2, worldMatrix2);
					worldMatrix2 = Matrix4f.mul(worldMatrix1, worldMatrix2, worldMatrix2);

					fb.rewind();
					worldMatrix2.store(fb);
					fb.flip();
					GL11.glMultMatrix(fb);

                    CelestialBodyRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.Pre(moon, moon.getBodyIcon(), 8);
                    MinecraftForge.EVENT_BUS.post(preEvent);

                    GL11.glColor4f(1, 1, 1, 1);
                    this.mc.renderEngine.bindTexture(preEvent.celestialBodyTexture);

                    if (!preEvent.isCanceled())
                    {
                        this.drawTexturedModalRect(-2, -2, 4, 4, 0, 0, preEvent.textureSize, preEvent.textureSize, false, false, preEvent.textureSize);
                        matrixMap.put(moon, worldMatrix1);
                    }

                    CelestialBodyRenderEvent.Post postEvent = new CelestialBodyRenderEvent.Post(moon);
                    MinecraftForge.EVENT_BUS.post(postEvent);
                    fb.clear();
					GL11.glPopMatrix();
				}
			}

		}

		return matrixMap;
	}

	public void drawBorder()
	{
		Gui.drawRect(0, 0, GuiCelestialSelection.BORDER_WIDTH, height, GCCoreUtil.to32BitColor(255, 100, 100, 100));
		Gui.drawRect(width - GuiCelestialSelection.BORDER_WIDTH, 0, width, height, GCCoreUtil.to32BitColor(255, 100, 100, 100));
		Gui.drawRect(0, 0, width, GuiCelestialSelection.BORDER_WIDTH, GCCoreUtil.to32BitColor(255, 100, 100, 100));
		Gui.drawRect(0, height - GuiCelestialSelection.BORDER_WIDTH, width, height, GCCoreUtil.to32BitColor(255, 100, 100, 100));
		Gui.drawRect(GuiCelestialSelection.BORDER_WIDTH, GuiCelestialSelection.BORDER_WIDTH, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, height - GuiCelestialSelection.BORDER_WIDTH, GCCoreUtil.to32BitColor(255, 40, 40, 40));
		Gui.drawRect(GuiCelestialSelection.BORDER_WIDTH, GuiCelestialSelection.BORDER_WIDTH, width - GuiCelestialSelection.BORDER_WIDTH, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, GCCoreUtil.to32BitColor(255, 40, 40, 40));
		Gui.drawRect(width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH, GuiCelestialSelection.BORDER_WIDTH, width - GuiCelestialSelection.BORDER_WIDTH, height - GuiCelestialSelection.BORDER_WIDTH, GCCoreUtil.to32BitColor(255, 80, 80, 80));
		Gui.drawRect(GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, height - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH, width - GuiCelestialSelection.BORDER_WIDTH, height - GuiCelestialSelection.BORDER_WIDTH, GCCoreUtil.to32BitColor(255, 80, 80, 80));
	}

	public void drawButtons(int mousePosX, int mousePosY)
	{
		this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain);
		GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
		this.drawTexturedModalRect(width / 2 - 43, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, 86, 15, 266, 0, 172, 29, false, false);
		String str = GCCoreUtil.translate("gui.message.catalog.name").toUpperCase();
		this.fontRendererObj.drawString(str, width / 2 - this.fontRendererObj.getStringWidth(str) / 2, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + this.fontRendererObj.FONT_HEIGHT / 2, GCCoreUtil.to32BitColor(255, 255, 255, 255));

		if (this.selectedBody != null)
		{
			this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain);

			if (mousePosX > BORDER_WIDTH + BORDER_EDGE_WIDTH && mousePosX < BORDER_WIDTH + BORDER_EDGE_WIDTH + 88 && mousePosY > BORDER_WIDTH + BORDER_EDGE_WIDTH && mousePosY < BORDER_WIDTH + BORDER_EDGE_WIDTH + 13)
			{
				GL11.glColor3f(3.0F, 0.0F, 0.0F);
			}
			else
			{
				GL11.glColor3f(0.9F, 0.2F, 0.2F);
			}

            this.drawTexturedModalRect(BORDER_WIDTH + BORDER_EDGE_WIDTH, BORDER_WIDTH + BORDER_EDGE_WIDTH, 88, 13, 0, 392, 148, 22, false, false);
			str = GCCoreUtil.translate("gui.message.back.name").toUpperCase();
			this.fontRendererObj.drawString(str, BORDER_WIDTH + BORDER_EDGE_WIDTH + 45 - this.fontRendererObj.getStringWidth(str) / 2, BORDER_WIDTH + BORDER_EDGE_WIDTH + this.fontRendererObj.FONT_HEIGHT / 2 - 2, GCCoreUtil.to32BitColor(255, 255, 255, 255));

            this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain);
            if (mousePosX > width - BORDER_WIDTH - BORDER_EDGE_WIDTH - 88 && mousePosX < width - BORDER_WIDTH - BORDER_EDGE_WIDTH && mousePosY > BORDER_WIDTH + BORDER_EDGE_WIDTH && mousePosY < BORDER_WIDTH + BORDER_EDGE_WIDTH + 13)
            {
                GL11.glColor3f(0.0F, 3.0F, 0.0F);
            }
            else
            {
                GL11.glColor3f(0.2F, 0.9F, 0.2F);
            }

            this.drawTexturedModalRect(width - BORDER_WIDTH - BORDER_EDGE_WIDTH - 88, BORDER_WIDTH + BORDER_EDGE_WIDTH, 88, 13, 0, 392, 148, 22, true, false);

			GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
            this.drawTexturedModalRect(BORDER_WIDTH + BORDER_EDGE_WIDTH, height - BORDER_WIDTH - BORDER_EDGE_WIDTH - 13, 88, 13, 0, 392, 148, 22, false, true);
            this.drawTexturedModalRect(width - BORDER_WIDTH - BORDER_EDGE_WIDTH - 88, height - BORDER_WIDTH - BORDER_EDGE_WIDTH - 13, 88, 13, 0, 392, 148, 22, true, true);
			int menuTopLeft = GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH - 115 + height / 2 - 4;
			int posX = GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + Math.min(this.ticksSinceSelection * 10, 133) - 134;
			int posX2 = (int) (GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + Math.min(this.ticksSinceSelection * 1.25F, 15) - 15);
			int fontPosY = menuTopLeft + GuiCelestialSelection.BORDER_EDGE_WIDTH + this.fontRendererObj.FONT_HEIGHT / 2 - 2;
			this.drawTexturedModalRect(posX, menuTopLeft + 12, 133, 196, 0, 0, 266, 392, false, false);

//			str = this.selectedBody.getLocalizedName();
//			this.fontRendererObj.drawString(str, posX + 20, fontPosY, GCCoreUtil.to32BitColor(255, 255, 255, 255));

			str = GCCoreUtil.translate("gui.message.daynightcycle.name") + ":";
			this.fontRendererObj.drawString(str, posX + 5, fontPosY + 14, GCCoreUtil.to32BitColor(255, 150, 200, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".daynightcycle.0.name");
			this.fontRendererObj.drawString(str, posX + 10, fontPosY + 25, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".daynightcycle.1.name");
			if (!str.isEmpty())
			{
				this.fontRendererObj.drawString(str, posX + 10, fontPosY + 36, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			}

			str = GCCoreUtil.translate("gui.message.surfacegravity.name") + ":";
			this.fontRendererObj.drawString(str, posX + 5, fontPosY + 50, GCCoreUtil.to32BitColor(255, 150, 200, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".surfacegravity.0.name");
			this.fontRendererObj.drawString(str, posX + 10, fontPosY + 61, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".surfacegravity.1.name");
			if (!str.isEmpty())
			{
				this.fontRendererObj.drawString(str, posX + 10, fontPosY + 72, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			}

			str = GCCoreUtil.translate("gui.message.surfacecomposition.name") + ":";
			this.fontRendererObj.drawString(str, posX + 5, fontPosY + 88, GCCoreUtil.to32BitColor(255, 150, 200, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".surfacecomposition.0.name");
			this.fontRendererObj.drawString(str, posX + 10, fontPosY + 99, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".surfacecomposition.1.name");
			if (!str.isEmpty())
			{
				this.fontRendererObj.drawString(str, posX + 10, fontPosY + 110, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			}

			str = GCCoreUtil.translate("gui.message.atmosphere.name") + ":";
			this.fontRendererObj.drawString(str, posX + 5, fontPosY + 126, GCCoreUtil.to32BitColor(255, 150, 200, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".atmosphere.0.name");
			this.fontRendererObj.drawString(str, posX + 10, fontPosY + 137, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".atmosphere.1.name");
			if (!str.isEmpty())
			{
				this.fontRendererObj.drawString(str, posX + 10, fontPosY + 148, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			}

			str = GCCoreUtil.translate("gui.message.meansurfacetemp.name") + ":";
			this.fontRendererObj.drawString(str, posX + 5, fontPosY + 165, GCCoreUtil.to32BitColor(255, 150, 200, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".meansurfacetemp.0.name");
			this.fontRendererObj.drawString(str, posX + 10, fontPosY + 176, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".meansurfacetemp.1.name");
			if (!str.isEmpty())
			{
				this.fontRendererObj.drawString(str, posX + 10, fontPosY + 187, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			}

			this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain);
			GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
			this.drawTexturedModalRect(posX2, menuTopLeft + 12, 17, 199, 439, 0, 32, 399, false, false);
//			this.drawRectD(posX2 + 16.5, menuTopLeft + 13, posX + 131, menuTopLeft + 14, GCCoreUtil.to32BitColor(120, 0, (int) (0.6F * 255), 255));
		}
	}

	public void drawTexturedModalRect(int x, int y, int width, int height, int u, int v, int uWidth, int vHeight, boolean invertX, boolean invertY)
	{
		this.drawTexturedModalRect(x, y, width, height, u, v, uWidth, vHeight, invertX, invertY, 512);
	}

	public void drawTexturedModalRect(int x, int y, int width, int height, int u, int v, int uWidth, int vHeight, boolean invertX, boolean invertY, int texSize)
	{
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		float texMod = 1 / (float) texSize;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		int height0 = invertY ? 0 : vHeight;
		int height1 = invertY ? vHeight : 0;
        int width0 = invertX ? uWidth : 0;
        int width1 = invertX ? 0 : uWidth;
		tessellator.addVertexWithUV(x, y + height, this.zLevel, (u + width0) * texMod, (v + height0) * texMod);
		tessellator.addVertexWithUV(x + width, y + height, this.zLevel, (u + width1) * texMod, (v + height0) * texMod);
		tessellator.addVertexWithUV(x + width, y, this.zLevel, (u + width1) * texMod, (v + height1) * texMod);
		tessellator.addVertexWithUV(x, y, this.zLevel, (u + width0) * texMod, (v + height1) * texMod);
		tessellator.draw();
	}

	public void setBlackBackground()
	{
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        final Tessellator var3 = Tessellator.instance;
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
        var3.startDrawingQuads();
        var3.addVertex(0.0D, height, -90.0D);
        var3.addVertex(width, height, -90.0D);
        var3.addVertex(width, 0.0D, -90.0D);
        var3.addVertex(0.0D, 0.0D, -90.0D);
        var3.draw();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public Matrix4f setIsometric(float partialTicks)
	{
		Matrix4f mat0 = new Matrix4f();
		Matrix4f.translate(new Vector3f(width / 2.0F, height / 2, 0), mat0, mat0);
		Matrix4f.rotate((float) Math.toRadians(55), new Vector3f(1, 0, 0), mat0, mat0);
		Matrix4f.rotate((float) Math.toRadians(-45), new Vector3f(0, 0, 1), mat0, mat0);
		float zoomLocal = this.getZoomAdvanced();
		this.zoom = this.getZoomAdvanced();
		Matrix4f.scale(new Vector3f(1.1f + zoomLocal, 1.1F + zoomLocal, 1.1F + zoomLocal), mat0, mat0);
		Vector2f cBodyPos = this.getTranslationAdvanced(partialTicks);
		this.position = this.getTranslationAdvanced(partialTicks);
		Matrix4f.translate(new Vector3f(-cBodyPos.x, -cBodyPos.y, 0), mat0, mat0);
		FloatBuffer fb = BufferUtils.createFloatBuffer(16);
		fb.rewind();
		mat0.store(fb);
		fb.flip();
		GL11.glMultMatrix(fb);
		return mat0;
	}

	public void drawGrid(float gridSize, float gridScale)
	{
		GL11.glColor4f(0.0F, 0.2F, 0.5F, 0.55F);

		GL11.glBegin(GL11.GL_LINES);

		gridSize += gridScale / 2;
		for (float v = -gridSize; v <= gridSize; v += gridScale)
		{
			GL11.glVertex3f(v, -gridSize, -0.0F);
			GL11.glVertex3f(v, gridSize, -0.0F);
			GL11.glVertex3f(-gridSize, v, -0.0F);
			GL11.glVertex3f(gridSize, v, -0.0F);
		}

		GL11.glEnd();
	}

	public void drawCircles()
	{
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glLineWidth(3);
		int count = 0;

        final float theta = (float) (2 * Math.PI / 90);
        final float cos = (float) Math.cos(theta);
        final float sin = (float) Math.sin(theta);

		if (!(this.selectedBody instanceof Moon))
		{
			for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
			{
				if (planet.getParentSolarSystem() != null)
				{
                    float x = planet.getRelativeDistanceFromCenter() * 25.0F;
                    float y = 0;

                    float alpha = 1.0F;

                    if (this.selectedBody != null)
                    {
                        if (this.lastSelectedBody == null)
                        {
                            alpha = 1.0F - Math.min(this.ticksSinceSelection / 25.0F, 1.0F);
                        }
                        else
                        {
                            alpha = 0.0F;
                        }
                    }

                    if (alpha != 0)
                    {
                        switch (count % 2)
                        {
                            case 0:
                                GL11.glColor4f(0.0F, 0.6F, 1.0F, alpha);
                                break;
                            case 1:
                                GL11.glColor4f(0.4F, 0.9F, 1.0F, alpha);
                                break;
                        }

                        CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre(planet);
                        MinecraftForge.EVENT_BUS.post(preEvent);

                        if (!preEvent.isCanceled())
                        {
                            GL11.glBegin(GL11.GL_LINE_LOOP);

                            float temp;
                            for (int i = 0; i < 90; i++)
                            {
                                GL11.glVertex2f(x, y);

                                temp = x;
                                x = cos * x - sin * y;
                                y = sin * temp + cos * y;
                            }

                            GL11.glEnd();

                            count++;
                        }

                        CelestialBodyRenderEvent.CelestialRingRenderEvent.Post postEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Post(planet);
                        MinecraftForge.EVENT_BUS.post(postEvent);
                    }
				}
			}
		}

		count = 0;

		if (this.selectedBody != null)
		{
			Vector3f planetPos = this.selectedBody instanceof Moon ? this.getCelestialBodyPosition(((Moon) this.selectedBody).getParentPlanet()) : this.getCelestialBodyPosition(this.selectedBody);
			GL11.glTranslatef(planetPos.x, planetPos.y, 0);

			for (Moon moon : GalaxyRegistry.getRegisteredMoons().values())
			{
				if (moon.getParentPlanet() == this.selectedBody && this.ticksSinceSelection > 24 || moon == this.selectedBody || this.lastSelectedBody instanceof Moon)
				{
                    float x = moon.getRelativeDistanceFromCenter() / 8.0F;
                    float y = 0;

                    float alpha = this.selectedBody instanceof Moon ? 1.0F : Math.min(Math.max((this.ticksSinceSelection - 30) / 15.0F, 0.0F), 1.0F);

                    if (this.lastSelectedBody instanceof Moon)
                    {
                        if (GalaxyRegistry.getMoonsForPlanet(((Moon) this.lastSelectedBody).getParentPlanet()).contains(moon))
                        {
                            alpha = 1.0F;
                        }
                    }

                    if (alpha != 0)
                    {
                        switch (count % 2)
                        {
                            case 0:
                                GL11.glColor4f(0.0F, 0.6F, 1.0F, alpha);
                                break;
                            case 1:
                                GL11.glColor4f(0.4F, 0.9F, 1.0F, alpha);
                                break;
                        }

                        CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre(moon);
                        MinecraftForge.EVENT_BUS.post(preEvent);

                        if (!preEvent.isCanceled())
                        {
                            GL11.glBegin(GL11.GL_LINE_LOOP);

                            float temp;
                            for (int i = 0; i < 90; i++)
                            {
                                GL11.glVertex2f(x, y);

                                temp = x;
                                x = cos * x - sin * y;
                                y = sin * temp + cos * y;
                            }

                            GL11.glEnd();

                            count++;
                        }

                        CelestialBodyRenderEvent.CelestialRingRenderEvent.Post postEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Post(moon);
                        MinecraftForge.EVENT_BUS.post(postEvent);
                    }
				}
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
