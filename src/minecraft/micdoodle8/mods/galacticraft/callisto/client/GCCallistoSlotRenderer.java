package micdoodle8.mods.galacticraft.callisto.client;

import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import net.minecraft.client.renderer.Tessellator;

public class GCCallistoSlotRenderer implements IPlanetSlotRenderer
{

	@Override
	public String getPlanetSprite() 
	{
		return "/micdoodle8/mods/galacticraft/callisto/client/planets/callisto.png";
	}

	@Override
	public String getPlanetName() 
	{
		return "Callisto";
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
