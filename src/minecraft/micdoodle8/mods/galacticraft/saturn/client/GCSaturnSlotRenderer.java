package micdoodle8.mods.galacticraft.saturn.client;

import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import net.minecraft.client.renderer.Tessellator;

public class GCSaturnSlotRenderer implements IPlanetSlotRenderer
{

	@Override
	public String getPlanetSprite()
	{
		return "/micdoodle8/mods/galacticraft/saturn/client/planets/saturn.png";
	}

	@Override
	public String getPlanetName()
	{
		return "Saturn";
	}

	@Override
	public void renderSlot(int index, int x, int y, float slotHeight, Tessellator tessellator)
	{
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x - slotHeight / 2 * 1.6, 	y + slotHeight / 2 * 1.6, 	-90.0D, 0.0, 1.0);
        tessellator.addVertexWithUV(x + slotHeight / 2 * 1.6, 	y + slotHeight / 2 * 1.6, 	-90.0D, 1.0, 1.0);
        tessellator.addVertexWithUV(x + slotHeight / 2 * 1.6, 	y - slotHeight / 2 * 1.6, 	-90.0D, 1.0, 0.0);
        tessellator.addVertexWithUV(x - slotHeight / 2 * 1.6, 	y - slotHeight / 2 * 1.6, 	-90.0D, 0.0, 0.0);
	    tessellator.draw();
	}
}
