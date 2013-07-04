package micdoodle8.mods.galacticraft.API;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.ResourceLocation;

public interface ICelestialBodyRenderer
{
    public ResourceLocation getPlanetSprite();

    public String getPlanetName();

    public void renderSlot(int index, int x, int y, float slotHeight, Tessellator tessellator);
}
