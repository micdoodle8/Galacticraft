package micdoodle8.mods.galacticraft.core.client;

import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import net.minecraft.src.Tessellator;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class GCCoreSlotRendererSun implements IPlanetSlotRenderer
{
	@Override
	public String getPlanetSprite() 
	{
		return "/terrain/sun.png";
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
		tessellator.addVertexWithUV(x - slotHeight * 0.9, 	y + slotHeight * 0.9, 	-90.0D, 0.0, 1.0);
		tessellator.addVertexWithUV(x + slotHeight * 0.9, 	y + slotHeight * 0.9, 	-90.0D, 1.0, 1.0);
		tessellator.addVertexWithUV(x + slotHeight * 0.9, 	y - slotHeight * 0.9, 	-90.0D, 1.0, 0.0);
		tessellator.addVertexWithUV(x - slotHeight * 0.9, 	y - slotHeight * 0.9, 	-90.0D, 0.0, 0.0);
		tessellator.draw();
	}
}
