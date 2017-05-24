package micdoodle8.mods.galacticraft.api.transmission.tile;

import net.minecraft.util.EnumFacing;

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
     * @param from      Orientation the oxygen is sent in from.
     * @param receive   Maximum amount of oxygen to be sent into the block.
     * @param doReceive If false, the transfer will only be simulated.
     * @return Amount of oxygen that was accepted by the block.
     */
    int receiveOxygen(EnumFacing from, int receive, boolean doReceive);

    /**
     * Adds oxygen provision to a block. Returns the quantity of oxygen
     * provided. This should always return 0 if the tile entity cannot provide
     * oxygen.
     *
     * @param from      Orientation the oxygen is requested from.
     * @param request   Maximum amount of oxygen to be pushed out of the block.
     * @param doProvide If false, the transfer will only be simulated.
     * @return Amount of oxygen that was given out by the block.
     */
    int provideOxygen(EnumFacing from, int request, boolean doProvide);

    /**
     * @return How much oxygen does this TileEntity want?
     */
    int getOxygenRequest(EnumFacing direction);

    /**
     * @return How much oxygen does this TileEntity want to provide?
     */
    int getOxygenProvide(EnumFacing direction);

}
