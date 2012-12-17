package micdoodle8.mods.galacticraft.enceladus.client;

import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import net.minecraft.src.Tessellator;

public class GCEnceladusSlotRenderer implements IPlanetSlotRenderer
{

	@Override
	public String getPlanetSprite() 
	{
		return "/micdoodle8/mods/galacticraft/enceladus/client/planets/enceladus.png";
	}

	@Override
	public String getPlanetName() 
	{
		return "Enceladus";
	}

	@Override
	public void renderSlot(int index, int x, int y, float slotHeight, Tessellator tessellator) 
	{
		tessellator.startDrawingQuads();
	    tessellator.addVertexWithUV(x - 5 - slotHeight * 1.3, 	y - 2 + slotHeight * 1.3, 	-90.0D, 0.35D, 0.65D);
	    tessellator.addVertexWithUV(x - 5, 						y - 2 + slotHeight * 1.3, 	-90.0D, 0.65D, 0.65D);
	    tessellator.addVertexWithUV(x - 5, 						y - 2, 						-90.0D, 0.65D, 0.35D);
	    tessellator.addVertexWithUV(x - 5 - slotHeight * 1.3, 	y - 2, 						-90.0D, 0.35D, 0.35D);
	    tessellator.draw();
	}
}
