package micdoodle8.mods.galacticraft.api.entity;

import net.minecraftforge.fluids.FluidStack;

/**
 * Implement into entities that can be fueled when placed on fuel docks
 */
public interface IFuelable
{
	/**
	 * Add fuel to the entity
	 * 
	 * @param fluid
	 *            The fluid to add. Be sure to check if the correct fluid is
	 *            being loaded before adding it.
	 * @param doDrain
	 *            Whether or not fluid should actually be added, or is just a
	 *            test.
	 * @return the amount of fluid that was added to the entity.
	 */
	public int addFuel(FluidStack fluid, boolean doDrain);

	/**
	 * Remove fuel from an entity
	 * 
	 * @param amount
	 *            The amount of fluid to drain
	 * @return The fluidstack that was drained
	 */
	public FluidStack removeFuel(int amount);
}
