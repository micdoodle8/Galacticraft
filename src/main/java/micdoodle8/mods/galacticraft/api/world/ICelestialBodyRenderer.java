package micdoodle8.mods.galacticraft.api.world;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

/**
 * Used for rendering planet on the Galaxy Map, as well as the planet selection
 * GUI
 */
public interface ICelestialBodyRenderer
{
	/**
	 * @return the location of the planet sprite
	 */
	public ResourceLocation getPlanetSprite();

	/**
	 * @return the name of this planet. Must match celestial body being rendered
	 */
	public String getPlanetName();

	/**
	 * Renders the slot. @link GCCoreSlotRendererSun as an example
	 * 
	 * @param index
	 *            the slot number, if applicable
	 * @param x
	 *            the x-coord to be rendered at
	 * @param y
	 *            the y-coord to be rendered at
	 * @param slotHeight
	 *            the height and width to be rendered at
	 * @param tessellator
	 *            the tessellator used for rendering
	 */
	public void renderSlot(int index, int x, int y, float slotHeight, Tessellator tessellator);
}
