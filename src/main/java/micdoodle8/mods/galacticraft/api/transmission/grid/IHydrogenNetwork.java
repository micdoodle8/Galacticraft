package micdoodle8.mods.galacticraft.api.transmission.grid;

import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import net.minecraft.tileentity.TileEntity;

/**
 * The hydrogen Network in interface form.
 *
 * @author Calclavia
 */
public interface IHydrogenNetwork extends IGridNetwork<IHydrogenNetwork, ITransmitter, TileEntity>
{
    /**
     * Produces hydrogen in this hydrogen network.
     *
     * @return Rejected energy in Joules.
     */
    public float produce(float sendAmount, TileEntity... ignoreTiles);

    /**
     * Gets the total amount of hydrogen requested/needed in the network.
     *
     * @param ignoreTiles The TileEntities to ignore during this calculation (optional).
     */
    public float getRequest(TileEntity... ignoreTiles);
}
