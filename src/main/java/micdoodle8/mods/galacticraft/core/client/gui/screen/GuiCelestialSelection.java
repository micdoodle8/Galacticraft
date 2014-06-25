package micdoodle8.mods.galacticraft.core.client.gui.screen;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.google.common.collect.Maps;

public class GuiCelestialSelection extends GuiScreen
{
	private float zoom = 0.0F;
	private float planetZoom = 0.0F;
	private boolean doneZooming = false;
	private float preSelectZoom = 0.0F;
	public static ResourceLocation guiMain = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/celestialselection.png");
	private int ticks = 0;
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
			celestialBodyTicks.put(planet, 0);
		}
		
		for (Moon moon : GalaxyRegistry.getRegisteredMoons().values())
		{
			celestialBodyTicks.put(moon, 0);
		}
		
		BORDER_WIDTH = this.width / 65;
		BORDER_EDGE_WIDTH = BORDER_WIDTH / 4;
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
		
		return 12 + planetZoom;
	}
	
	private Vector2f getTranslationAdvanced(float partialTicks)
	{
		if (this.selectedBody == null)
		{
			if (this.ticksSinceUnselection > 0)
			{
				return this.lerpVec2(position, new Vector2f(0, 0), Math.max(0.0F, Math.min((this.ticksSinceUnselection + partialTicks) / 100.0F, 1.0F)));
			}
			
			return position;
		}
		
		Vector3f posVec = this.getCelestialBodyPosition(selectedBody);
		return this.lerpVec2(position, new Vector2f(posVec.x, posVec.y), Math.max(0.0F, Math.min((this.ticksSinceSelection + partialTicks - 18) / 7.5F, 1.0F)));
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
		ticks++;
		
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
		
		for (CelestialBody e : celestialBodyTicks.keySet())
		{
			if (!(e instanceof Planet && e == this.selectedBody) && !(e instanceof Planet && this.selectedBody instanceof Moon && GalaxyRegistry.getMoonsForPlanet((Planet) e).contains(this.selectedBody)))
			{
				Integer i = celestialBodyTicks.get(e);
				
				if (i != null)
				{
					i++;
				}
				
				celestialBodyTicks.put(e, i);
			}
		}
		
		if (selectedBody != null)
		{
			ticksSinceSelection++;
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

		if (this.selectedBody != null && x > width / 2 - 38 && x < width / 2 + 38 && y > height - this.BORDER_WIDTH - this.BORDER_EDGE_WIDTH - 15 && y < height - this.BORDER_WIDTH - this.BORDER_EDGE_WIDTH)
		{
			this.unselectCelestialBody();
			return;
		}

    	// Need unscaled mouse coords
		int mouseX = Mouse.getX();
		int mouseY = Mouse.getY() * -1 + Minecraft.getMinecraft().displayHeight - 1;
		
		for (Map.Entry<CelestialBody, Vector3f> e : planetPosMap.entrySet())
		{
			int iconSize = (int) e.getValue().z; // Z value holds size on-screen
			
			if (mouseX >= e.getValue().x - iconSize && mouseX <= e.getValue().x + iconSize &&
					mouseY >= e.getValue().y - iconSize && mouseY <= e.getValue().y + iconSize)
			{
				if (selectedBody != e.getKey())
				{
					if (this.selectedBody == null)
					{
						this.preSelectZoom = this.zoom;
					}
					
					this.doneZooming = false;
					this.planetZoom = 0.0F;
					this.lastSelectedBody = this.selectedBody;
					selectedBody = e.getKey();
					ticksSinceSelection = 0;
					break;
				}
			}
		}
    }
	
	@Override
	public void drawScreen(int mousePosX, int mousePosY, float partialTicks)
	{
		GL11.glPushMatrix();
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		int scaledW = scaledRes.getScaledWidth();
		int scaledH = scaledRes.getScaledHeight();
		
		Matrix4f camMatrix = new Matrix4f();
		Matrix4f.translate(new Vector3f(0.0F, 0.0F, -2000.0F), camMatrix, camMatrix); // See EntityRenderer.java:setupOverlayRendering
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.m00 = 2.0F / (float)scaledRes.getScaledWidth_double();
		viewMatrix.m11 = 2.0F / (float)-scaledRes.getScaledHeight_double();
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

		this.setBlackBackground(scaledW, scaledH);
				
		GL11.glPushMatrix();
		Matrix4f worldMatrix = this.setIsometric(partialTicks);
		this.drawGrid(scaledH / 3, (scaledH / 3) / 3.5F);
		this.drawCircles();
		GL11.glPopMatrix();

		HashMap<CelestialBody, Matrix4f> matrixMap = this.drawCelestialBodies(scaledW, scaledH, worldMatrix);
		
		for (Map.Entry<CelestialBody, Matrix4f> e : matrixMap.entrySet())
		{
			Matrix4f planetMatrix = e.getValue();
			Matrix4f matrix0 = planetMatrix.mul(viewMatrix, planetMatrix, planetMatrix);
			int x = (int) Math.floor((matrix0.m30 * 0.5 + 0.5) * Minecraft.getMinecraft().displayWidth);
			int y = (int) Math.floor((Minecraft.getMinecraft().displayHeight - ((matrix0.m31 * 0.5 + 0.5) * Minecraft.getMinecraft().displayHeight)));
			Vector2f vec = new Vector2f(x, y);
			
			Matrix4f scaleVec = new Matrix4f();
			scaleVec.m00 = matrix0.m00;
			scaleVec.m11 = matrix0.m11;
			scaleVec.m22 = matrix0.m22;
			Vector4f newVec = Matrix4f.transform(scaleVec, new Vector4f(2, -2, 0, 0), null);
			int iconSize = (int) (newVec.y * (Minecraft.getMinecraft().displayHeight / 2.0F));
			
			this.planetPosMap.put(e.getKey(), new Vector3f(vec.x, vec.y, iconSize)); // Store size on-screen in Z-value for ease
		}
		
		if (this.selectedBody != null)
		{
			GL11.glPushMatrix();
			Matrix4f worldMatrix0 = new Matrix4f(worldMatrix);
			Matrix4f.translate(this.getCelestialBodyPosition(this.selectedBody), worldMatrix0, worldMatrix0);
			Matrix4f worldMatrix1 = new Matrix4f();
			Matrix4f.rotate((float)Math.toRadians(45), new Vector3f(0, 0, 1), worldMatrix1, worldMatrix1);
			Matrix4f.rotate((float)Math.toRadians(-55), new Vector3f(1, 0, 0), worldMatrix1, worldMatrix1);
			worldMatrix1 = worldMatrix1.mul(worldMatrix0, worldMatrix1, worldMatrix1);
	        fb.rewind();
	        worldMatrix1.store(fb);
	        fb.flip();
	        GL11.glMultMatrix(fb);
	        fb.clear();
			Vector3f planetLoc = this.getCelestialBodyPosition(this.selectedBody);
			float scale = Math.max(0.3F, (1.5F / (this.ticksSinceSelection / 5.0F)));
			float scale0 = this.getZoomAdvanced() < 0 || this.getZoomAdvanced() > 1 ? scale + this.planetZoom / 70.0F - 0.49F : scale;//(0.6F / this.getZoomAdvanced()) : scale;
			GL11.glScalef(scale0, scale0, 1);
			mc.renderEngine.bindTexture(guiMain);
			float colMod = this.getZoomAdvanced() < 4.9F ? (float)(Math.sin(this.ticksSinceSelection / 1.0F) * 0.5F + 0.5F) : 1.0F;
	    	GL11.glColor4f(0.4F, 0.8F, 1.0F, 1 * colMod);
			this.drawTexturedModalRect(-50, -50, 100, 100, 266, 29, 100, 100, false);
			GL11.glPopMatrix();
		}

		this.drawButtons(scaledW, scaledH, mousePosX, mousePosY);
		this.drawBorder(scaledW, scaledH);	
		GL11.glPopMatrix();
	}
	
	private Vector3f getCelestialBodyPosition(CelestialBody cBody)
	{
		int cBodyTicks = this.celestialBodyTicks.get(cBody);
		float distanceScale = (cBody instanceof Planet ? 25.0F : (1.0F / 8.0F));
		float timeScale = (cBody instanceof Planet ? 200.0F : 2.0F);
		Vector3f cBodyPos = new Vector3f((float)Math.sin(cBodyTicks / timeScale * cBody.getRelativeOrbitTime() + cBody.getPhaseShift()) * cBody.getRelativeDistanceFromCenter() * distanceScale, (float)Math.cos(cBodyTicks / timeScale * cBody.getRelativeOrbitTime() + cBody.getPhaseShift()) * cBody.getRelativeDistanceFromCenter() * distanceScale, 0);
		
		if (cBody instanceof Moon)
		{
			Vector3f parentVec = this.getCelestialBodyPosition(((Moon) cBody).getParentPlanet());
			return Vector3f.add(cBodyPos, parentVec, null);
		}
		
		return cBodyPos;
	}
	
	public HashMap<CelestialBody, Matrix4f> drawCelestialBodies(int width, int height, Matrix4f worldMatrix)
	{
		GL11.glColor3f(1, 1, 1);
        FloatBuffer fb = BufferUtils.createFloatBuffer(16 * Float.SIZE);
        HashMap<CelestialBody, Matrix4f> matrixMap = Maps.newHashMap();
        
		for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
		{
			if (planet.getPlanetIcon() != null)
			{
				GL11.glPushMatrix();
				Matrix4f worldMatrix0 = new Matrix4f(worldMatrix);
				
				Matrix4f.translate(this.getCelestialBodyPosition(planet), worldMatrix0, worldMatrix0);
				
				Matrix4f worldMatrix1 = new Matrix4f();
				Matrix4f.rotate((float)Math.toRadians(45), new Vector3f(0, 0, 1), worldMatrix1, worldMatrix1);
				Matrix4f.rotate((float)Math.toRadians(-55), new Vector3f(1, 0, 0), worldMatrix1, worldMatrix1);
				worldMatrix1 = worldMatrix1.mul(worldMatrix0, worldMatrix1, worldMatrix1);
				
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
						
						if (this.lastSelectedBody instanceof Moon && ((Moon) this.lastSelectedBody).getParentPlanet() != planet)
						{
							alpha = 0.0F;
						}
						else if (this.lastSelectedBody instanceof Planet && this.lastSelectedBody != planet)
						{
							alpha = 0.0F;
						}
					}
				}
				
				if (alpha != 0)
				{
					GL11.glColor4f(1, 1, 1, alpha);
					
					mc.renderEngine.bindTexture(planet.getPlanetIcon());
					this.drawTexturedModalRect(-2, -2, 4, 4, 0, 0, 256, 256, false, 256);
			        
					fb.clear();
					matrixMap.put(planet, worldMatrix1);
				}
				
				GL11.glPopMatrix();
				
			}
		}

		if (this.selectedBody != null)
		{
			Matrix4f worldMatrix0 = new Matrix4f(worldMatrix);
			
			for (Moon moon : GalaxyRegistry.getRegisteredMoons().values())
			{
				if (moon == this.selectedBody || (moon.getParentPlanet() == this.selectedBody && (this.ticksSinceSelection > 35 || (this.lastSelectedBody instanceof Moon && GalaxyRegistry.getMoonsForPlanet(((Moon) this.lastSelectedBody).getParentPlanet()).contains(moon)))))
				{
					GL11.glPushMatrix();
					Matrix4f worldMatrix1 = new Matrix4f(worldMatrix0);
					Matrix4f.translate(this.getCelestialBodyPosition(moon), worldMatrix1, worldMatrix1);

					Matrix4f worldMatrix2 = new Matrix4f();
					Matrix4f.rotate((float)Math.toRadians(45), new Vector3f(0, 0, 1), worldMatrix2, worldMatrix2);
					Matrix4f.rotate((float)Math.toRadians(-55), new Vector3f(1, 0, 0), worldMatrix2, worldMatrix2);
					Matrix4f.scale(new Vector3f(0.25F, 0.25F, 1.0F), worldMatrix2, worldMatrix2);
					worldMatrix2 = worldMatrix2.mul(worldMatrix1, worldMatrix2, worldMatrix2);
					
					fb.rewind();
					worldMatrix2.store(fb);
					fb.flip();
					GL11.glMultMatrix(fb);
					
					GL11.glColor4f(1, 1, 1, 1);
					
					mc.renderEngine.bindTexture(moon.getPlanetIcon());
					this.drawTexturedModalRect(-2, -2, 4, 4, 0, 0, 8, 8, false, 8);
			        
					fb.clear();
					matrixMap.put(moon, worldMatrix1);
					GL11.glPopMatrix();
				}
			}		
			
		}
		
		return matrixMap;
	}
	
	public void drawBorder(int width, int height)
	{
		Gui.drawRect(0, 0, this.BORDER_WIDTH, height, GCCoreUtil.to32BitColor(255, 100, 100, 100));
		Gui.drawRect(width - this.BORDER_WIDTH, 0, width, height, GCCoreUtil.to32BitColor(255, 100, 100, 100));
		Gui.drawRect(0, 0, width, this.BORDER_WIDTH, GCCoreUtil.to32BitColor(255, 100, 100, 100));
		Gui.drawRect(0, height - this.BORDER_WIDTH, width, height, GCCoreUtil.to32BitColor(255, 100, 100, 100));
		Gui.drawRect(this.BORDER_WIDTH, this.BORDER_WIDTH, this.BORDER_WIDTH + this.BORDER_EDGE_WIDTH, height - this.BORDER_WIDTH, GCCoreUtil.to32BitColor(255, 40, 40, 40));
		Gui.drawRect(this.BORDER_WIDTH, this.BORDER_WIDTH, width - this.BORDER_WIDTH, this.BORDER_WIDTH + this.BORDER_EDGE_WIDTH, GCCoreUtil.to32BitColor(255, 40, 40, 40));
		Gui.drawRect(width - this.BORDER_WIDTH - this.BORDER_EDGE_WIDTH, this.BORDER_WIDTH, width - this.BORDER_WIDTH, height - this.BORDER_WIDTH, GCCoreUtil.to32BitColor(255, 80, 80, 80));
		Gui.drawRect(this.BORDER_WIDTH + this.BORDER_EDGE_WIDTH, height - this.BORDER_WIDTH - this.BORDER_EDGE_WIDTH, width - this.BORDER_WIDTH, height - this.BORDER_WIDTH, GCCoreUtil.to32BitColor(255, 80, 80, 80));
	}
	
	public void drawButtons(int width, int height, int mousePosX, int mousePosY)
	{
		mc.renderEngine.bindTexture(guiMain);
    	GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
		this.drawTexturedModalRect(width / 2 - 43, this.BORDER_WIDTH + this.BORDER_EDGE_WIDTH, 86, 15, 266, 0, 172, 29, false);
		String str = GCCoreUtil.translate("gui.message.catalog.name").toUpperCase();
		this.fontRendererObj.drawString(str, width / 2 - this.fontRendererObj.getStringWidth(str) / 2, this.BORDER_WIDTH + this.BORDER_EDGE_WIDTH + fontRendererObj.FONT_HEIGHT / 2, GCCoreUtil.to32BitColor(255, 255, 255, 255));

		
		if (this.selectedBody != null)
		{
			mc.renderEngine.bindTexture(guiMain);
			
			if (mousePosX > width / 2 - 38 && mousePosX < width / 2 + 38 && mousePosY > height - this.BORDER_WIDTH - this.BORDER_EDGE_WIDTH - 15 && mousePosY < height - this.BORDER_WIDTH - this.BORDER_EDGE_WIDTH)
			{
		        GL11.glColor3f(1.0F, 0.4F, 0.4F);
			}
			else
			{
		        GL11.glColor3f(0.9F, 0.2F, 0.2F);
			}
			
			this.drawTexturedModalRect(width / 2 - 43, height - this.BORDER_WIDTH - this.BORDER_EDGE_WIDTH - 15, 86, 15, 266, 0, 172, 29, true);
			str = GCCoreUtil.translate("gui.message.back.name").toUpperCase();
			this.fontRendererObj.drawString(str, width / 2 - this.fontRendererObj.getStringWidth(str) / 2, height - this.BORDER_WIDTH - this.BORDER_EDGE_WIDTH - 15 + fontRendererObj.FONT_HEIGHT / 2, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			
			mc.renderEngine.bindTexture(guiMain);
	    	GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
			int menuTopLeft = this.BORDER_WIDTH + this.BORDER_EDGE_WIDTH - 115 + height / 2;
			int posX = this.BORDER_WIDTH + this.BORDER_EDGE_WIDTH + Math.min(ticksSinceSelection * 10, 133) - 133;
			int fontPosY = menuTopLeft + this.BORDER_EDGE_WIDTH + fontRendererObj.FONT_HEIGHT / 2 - 2;
			this.drawTexturedModalRect(posX, menuTopLeft, 133, 209, 0, 0, 266, 418, false);
			
			str = this.selectedBody.getLocalizedName();
			this.fontRendererObj.drawString(str, posX + 20, fontPosY, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			
			str = GCCoreUtil.translate("gui.message.daynightcycle.name") + ":";
			this.fontRendererObj.drawString(str, posX + 5, fontPosY + 14, GCCoreUtil.to32BitColor(255, 150, 200, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".daynightcycle.0.name");
			this.fontRendererObj.drawString(str, posX + 10, fontPosY + 25, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".daynightcycle.1.name");
			if (!str.isEmpty()) this.fontRendererObj.drawString(str, posX + 10, fontPosY + 36, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			
			str = GCCoreUtil.translate("gui.message.surfacegravity.name") + ":";
			this.fontRendererObj.drawString(str, posX + 5, fontPosY + 50, GCCoreUtil.to32BitColor(255, 150, 200, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".surfacegravity.0.name");
			this.fontRendererObj.drawString(str, posX + 10, fontPosY + 61, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".surfacegravity.1.name");
			if (!str.isEmpty()) this.fontRendererObj.drawString(str, posX + 10, fontPosY + 72, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			
			str = GCCoreUtil.translate("gui.message.surfacecomposition.name") + ":";
			this.fontRendererObj.drawString(str, posX + 5, fontPosY + 88, GCCoreUtil.to32BitColor(255, 150, 200, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".surfacecomposition.0.name");
			this.fontRendererObj.drawString(str, posX + 10, fontPosY + 99, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".surfacecomposition.1.name");
			if (!str.isEmpty()) this.fontRendererObj.drawString(str, posX + 10, fontPosY + 110, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			
			str = GCCoreUtil.translate("gui.message.atmosphere.name") + ":";
			this.fontRendererObj.drawString(str, posX + 5, fontPosY + 126, GCCoreUtil.to32BitColor(255, 150, 200, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".atmosphere.0.name");
			this.fontRendererObj.drawString(str, posX + 10, fontPosY + 137, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".atmosphere.1.name");
			if (!str.isEmpty()) this.fontRendererObj.drawString(str, posX + 10, fontPosY + 148, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			
			str = GCCoreUtil.translate("gui.message.meansurfacetemp.name") + ":";
			this.fontRendererObj.drawString(str, posX + 5, fontPosY + 165, GCCoreUtil.to32BitColor(255, 150, 200, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".meansurfacetemp.0.name");
			this.fontRendererObj.drawString(str, posX + 10, fontPosY + 176, GCCoreUtil.to32BitColor(255, 255, 255, 255));
			str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".meansurfacetemp.1.name");
			if (!str.isEmpty()) this.fontRendererObj.drawString(str, posX + 10, fontPosY + 187, GCCoreUtil.to32BitColor(255, 255, 255, 255));
		}
	}

    public void drawTexturedModalRect(int x, int y, int width, int height, int u, int v, int uWidth, int vHeight, boolean invert)
    {
    	this.drawTexturedModalRect(x, y, width, height, u, v, uWidth, vHeight, invert, 512);
    }

    public void drawTexturedModalRect(int x, int y, int width, int height, int u, int v, int uWidth, int vHeight, boolean invert, int texSize)
    {
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        float texMod = 1 / (float)texSize;
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
	
	public Matrix4f setIsometric(float partialTicks)
	{
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		int scaledW = scaledRes.getScaledWidth();
		int scaledH = scaledRes.getScaledHeight();
		Matrix4f mat0 = new Matrix4f();
		Matrix4f.translate(new Vector3f(scaledW / 2.0F, scaledH / 2, 0), mat0, mat0);
		Matrix4f.rotate((float) Math.toRadians(55), new Vector3f(1, 0, 0), mat0, mat0);
		Matrix4f.rotate((float) Math.toRadians(-45), new Vector3f(0, 0, 1), mat0, mat0);
		float zoomLocal = getZoomAdvanced();
		this.zoom = getZoomAdvanced();
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
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		int count = 0;
		
		if (!(this.selectedBody instanceof Moon))
		{
			for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
			{
				if (planet.getParentSolarSystem() != null)
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
					
					float alpha = 1.0F;
					
					if (this.selectedBody != null)
					{
						if (this.lastSelectedBody == null)
						{
							alpha = 1.0F - Math.min(this.ticksSinceSelection / 25.0F, 1.0F);
						}
						else
						{
							alpha = 0;
						}
					}
					
					switch (count % 2)
					{
					case 0:
				    	GL11.glColor4f(0.0F, 0.6F, 1.0F, alpha);
						break;
					case 1:
				    	GL11.glColor4f(0.4F, 0.9F, 1.0F, alpha);
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
					count++;
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
				if ((moon.getParentPlanet() == this.selectedBody && this.ticksSinceSelection > 24) || moon == this.selectedBody || this.lastSelectedBody instanceof Moon)
				{
					final float theta = (float) (2 * Math.PI / 500);
					final float c = (float) Math.cos(theta);
					final float s = (float) Math.sin(theta);
					float t;

					GL11.glDisable(GL11.GL_TEXTURE_2D);
					GL11.glEnable(GL11.GL_LIGHTING);
					GL11.glDisable(GL12.GL_RESCALE_NORMAL);

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
					
					switch (count % 2)
					{
					case 0:
				    	GL11.glColor4f(0.0F, 0.6F, 1.0F, alpha);
						break;
					case 1:
				    	GL11.glColor4f(0.4F, 0.9F, 1.0F, alpha);
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
					count++;
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
