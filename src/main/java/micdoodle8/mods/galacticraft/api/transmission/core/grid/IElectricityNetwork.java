package micdoodle8.mods.galacticraft.api.transmission.core.grid;

import micdoodle8.mods.galacticraft.api.transmission.ElectricityPack;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import net.minecraft.tileentity.TileEntity;

/**
 * The Electrical Network in interface form.
 * 
 * @author Calclavia
 * 
 */
public interface IElectricityNetwork extends IGridNetwork<IElectricityNetwork, IConductor, TileEntity>
{
	/**
	 * Produces electricity in this electrical network.
	 * 
	 * @return Rejected energy in Joules.
	 */
	public float produce(ElectricityPack electricityPack, boolean doReceive, TileEntity... ignoreTiles);

	/**
	 * Gets the total amount of electricity requested/needed in the electricity
	 * network.
	 * 
	 * @param ignoreTiles
	 *            The TileEntities to ignore during this calculation (optional).
	 */
	public ElectricityPack getRequest(TileEntity... ignoreTiles);

	/**
	 * @return The total amount of resistance of this electrical network. In
	 *         Ohms.
	 */
	public float getTotalResistance();

	/**
	 * @return The lowest amount of current (amperage) that this electrical
	 *         network can tolerate.
	 */
	public float getLowestCurrentCapacity();
}
