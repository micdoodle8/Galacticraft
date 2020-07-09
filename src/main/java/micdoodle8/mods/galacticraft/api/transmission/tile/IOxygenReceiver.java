package micdoodle8.mods.galacticraft.api.transmission.tile;

import net.minecraft.util.Direction;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Applied to all TileEntities that can interact with oxygen.
 */
public interface IOxygenReceiver extends IConnector
{
    boolean shouldPullOxygen();

    /**
     * Adds oxygen reception to a tile entity. Returns the quantity of oxygen
     * that was accepted. This should always return 0 if the block cannot
     * receive oxygen.
     *
     * @param from    Orientation the oxygen is sent in from.
     * @param receive Maximum amount of oxygen to be sent into the block.
     * @param action  Whether the transfer will be executed.
     * @return Amount of oxygen that was accepted by the block.
     */
    int receiveOxygen(Direction from, int receive, IFluidHandler.FluidAction action);

    /**
     * Adds oxygen provision to a block. Returns the quantity of oxygen
     * provided. This should always return 0 if the tile entity cannot provide
     * oxygen.
     *
     * @param from    Orientation the oxygen is requested from.
     * @param request Maximum amount of oxygen to be pushed out of the block.
     * @param action  Whether the transfer will be executed.
     * @return Amount of oxygen that was given out by the block.
     */
    int provideOxygen(Direction from, int request, IFluidHandler.FluidAction action);

    /**
     * @return How much oxygen does this TileEntity want?
     */
    int getOxygenRequest(Direction direction);

    /**
     * @return How much oxygen does this TileEntity want to provide?
     */
    int getOxygenProvide(Direction direction);

}
