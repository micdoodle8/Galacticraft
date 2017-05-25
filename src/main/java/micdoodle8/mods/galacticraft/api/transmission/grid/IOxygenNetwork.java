package micdoodle8.mods.galacticraft.api.transmission.grid;

import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import net.minecraft.tileentity.TileEntity;

/**
 * The Oxygen Network in interface form.
 *
 * @author Calclavia
 */
public interface IOxygenNetwork extends IGridNetwork<IOxygenNetwork, ITransmitter, TileEntity>
{
    /**
     * Produces oxygen in this oxygen network.
     *
     * @return Rejected energy in Joules.
     */
    float produce(float sendAmount, TileEntity... ignoreTiles);

    /**
     * Gets the total amount of oxygen requested/needed in the electricity
     * network.
     *
     * @param ignoreTiles The TileEntities to ignore during this calculation (optional).
     */
    float getRequest(TileEntity... ignoreTiles);
}
