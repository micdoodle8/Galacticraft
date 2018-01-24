/*
 * (C) 2014-2017 Team CoFH / CoFH / Cult of the Full Hub
 * http://www.teamcofh.com
 */
package cofh.redstoneflux.api;

import cofh.redstoneflux.impl.TileEnergyHandler;
import net.minecraft.util.EnumFacing;

/**
 * Implement this interface on Tile Entities which should handle energy, generally storing it in one or more internal {@link IEnergyStorage} objects.
 *
 * A reference implementation is provided {@link TileEnergyHandler}.
 *
 * Note that {@link IEnergyReceiver} and {@link IEnergyProvider} are extensions of this.
 *
 * @author King Lemming
 */
public interface IEnergyHandler extends IEnergyConnection {

	/**
	 * Returns the amount of energy currently stored.
	 */
	int getEnergyStored(EnumFacing from);

	/**
	 * Returns the maximum amount of energy that can be stored.
	 */
	int getMaxEnergyStored(EnumFacing from);

}
