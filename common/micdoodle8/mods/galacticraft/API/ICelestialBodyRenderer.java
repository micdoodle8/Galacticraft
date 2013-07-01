package micdoodle8.mods.galacticraft.API;

import net.minecraft.client.renderer.Tessellator;

public interface ICelestialBodyRenderer
{
    public String getPlanetSprite();

    public String getPlanetName();

    public void renderSlot(int index, int x, int y, float slotHeight, Tessellator tessellator);
}
