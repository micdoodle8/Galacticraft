package micdoodle8.mods.galacticraft.uranus.client;

import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import net.minecraft.client.renderer.Tessellator;

public class GCUranusSlotRenderer implements IPlanetSlotRenderer
{
	@Override
	public String getPlanetSprite()
	{
		return "/micdoodle8/mods/galacticraft/uranus/client/planets/uranus.png";
	}

	@Override
	public String getPlanetName()
	{
		return "Uranus";
	}

	@Override
	public void renderSlot(int index, int x, int y, float slotHeight, Tessellator tessellator)
	{
		tessellator.startDrawingQuads();
	    tessellator.addVertexWithUV(x - slotHeight / 2 * 1.3, 	y + slotHeight / 2 * 1.3, 	-90.0D, 0.35D, 0.65D);
	    tessellator.addVertexWithUV(x + slotHeight / 2 * 1.3, 	y + slotHeight / 2 * 1.3, 	-90.0D, 0.65D, 0.65D);
	    tessellator.addVertexWithUV(x + slotHeight / 2 * 1.3, 	y - slotHeight / 2 * 1.3, 	-90.0D, 0.65D, 0.35D);
	    tessellator.addVertexWithUV(x - slotHeight / 2 * 1.3, 	y - slotHeight / 2 * 1.3, 	-90.0D, 0.35D, 0.35D);
	    tessellator.draw();
	}
}
