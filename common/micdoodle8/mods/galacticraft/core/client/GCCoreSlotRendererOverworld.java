package micdoodle8.mods.galacticraft.core.client;

import micdoodle8.mods.galacticraft.API.ICelestialBodyRenderer;
import net.minecraft.client.renderer.Tessellator;

public class GCCoreSlotRendererOverworld implements ICelestialBodyRenderer
{
    @Override
    public String getPlanetSprite()
    {
        return "/micdoodle8/mods/galacticraft/core/client/planets/overworld.png";
    }

    @Override
    public String getPlanetName()
    {
        return "Overworld";
    }

    @Override
    public void renderSlot(int index, int x, int y, float slotHeight, Tessellator tessellator)
    {
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 12 - slotHeight, y - 11 + slotHeight, -90.0D, 0.0, 1.0);
        tessellator.addVertexWithUV(x + 12, y - 11 + slotHeight, -90.0D, 1.0, 1.0);
        tessellator.addVertexWithUV(x + 12, y - 11, -90.0D, 1.0, 0.0);
        tessellator.addVertexWithUV(x + 12 - slotHeight, y - 11, -90.0D, 0.0, 0.0);
        tessellator.draw();
    }
}
