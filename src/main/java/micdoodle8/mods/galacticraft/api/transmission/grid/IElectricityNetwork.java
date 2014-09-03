package micdoodle8.mods.galacticraft.api.transmission.grid;

import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import net.minecraft.tileentity.TileEntity;

/**
 * The Electrical Network in interface form.
 *
 * @author Calclavia
 */
public interface IElectricityNetwork extends IGridNetwork<IElectricityNetwork, IConductor, TileEntity>
{
    /**
     * Produce energy into the network
     *
     * @param energy      Amount of energy to send into the network
     * @param doReceive   Whether to put energy into the network (true) or just simulate (false)
     * @param tierGC      The tier of the producing tile (must be 1 or 2)
     * @param ignoreTiles TileEntities to ignore for energy transfers.
     * @return Amount of energy REMAINING from the passed energy parameter
     */
    public float produce(float energy, boolean doReceive, int tierGC, TileEntity... ignoreTiles);

    /**
     * Get the total energy request in this network
     *
     * @param ignoreTiles Tiles to ignore in the request calculations (NOTE: only used in initial (internal) check.
     * @return Amount of energy requested in this network
     */
    public float getRequest(TileEntity... ignoreTiles);
}
