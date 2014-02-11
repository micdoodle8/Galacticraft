package micdoodle8.mods.galacticraft.core.client;

import micdoodle8.mods.galacticraft.api.world.ICelestialBodyRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

/**
 * GCCoreSlotRendererSun.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreSlotRendererSun implements ICelestialBodyRenderer
{
	@Override
	public ResourceLocation getPlanetSprite()
	{
		return new ResourceLocation("textures/environment/sun.png");
	}

	@Override
	public String getPlanetName()
	{
		return "Sun";
	}

	@Override
	public void renderSlot(int index, int x, int y, float slotHeight, Tessellator tessellator)
	{
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x - slotHeight * 0.9, y + slotHeight * 0.9, -90.0D, 0.0, 1.0);
		tessellator.addVertexWithUV(x + slotHeight * 0.9, y + slotHeight * 0.9, -90.0D, 1.0, 1.0);
		tessellator.addVertexWithUV(x + slotHeight * 0.9, y - slotHeight * 0.9, -90.0D, 1.0, 0.0);
		tessellator.addVertexWithUV(x - slotHeight * 0.9, y - slotHeight * 0.9, -90.0D, 0.0, 0.0);
		tessellator.draw();
	}
}
