package micdoodle8.mods.galacticraft.moon.client;

import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import net.minecraft.client.renderer.Tessellator;

public class GCMoonSlotRenderer implements IPlanetSlotRenderer
{
	@Override
	public String getPlanetSprite()
	{
		return "/terrain/moon.png";
	}

	@Override
	public String getPlanetName()
	{
		return "Moon";
	}

	@Override
	public void renderSlot(int index, int x, int y, float slotHeight, Tessellator tessellator)
	{
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x - slotHeight / 2 * 1.25, 	y + slotHeight / 2 * 1.25, 		-90.0D, 0.35D, 0.65D);
		tessellator.addVertexWithUV(x + slotHeight / 2 * 1.25, 	y + slotHeight / 2 * 1.25, 		-90.0D, 0.65D, 0.65D);
		tessellator.addVertexWithUV(x + slotHeight / 2 * 1.25, 	y - slotHeight / 2 * 1.25, 		-90.0D, 0.65D, 0.35D);
		tessellator.addVertexWithUV(x - slotHeight / 2 * 1.25, 	y - slotHeight / 2 * 1.25, 		-90.0D, 0.35D, 0.35D);
	    tessellator.draw();
	}
}
