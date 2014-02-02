package micdoodle8.mods.galacticraft.api.transmission.tile;

import net.minecraftforge.common.ForgeDirection;

/**
 * Applied to all TileEntities that can interact with electricity.
 * 
 * @author Calclavia, King_Lemming
 * 
 */
public interface IOxygenReceiver extends IConnector
{
	/**
	 * Adds electricity to an block. Returns the quantity of electricity that
	 * was accepted. This should always return 0 if the block cannot be
	 * externally charged.
	 * 
	 * @param from
	 *            Orientation the electricity is sent in from.
	 * @param receive
	 *            Maximum amount of electricity to be sent into the block.
	 * @param doReceive
	 *            If false, the charge will only be simulated.
	 * @return Amount of energy that was accepted by the block.
	 */
	public float receiveOxygen(ForgeDirection from, float receive, boolean doReceive);

	/**
	 * Adds electricity to an block. Returns the ElectricityPack, the
	 * electricity provided. This should always return null if the block cannot
	 * be externally discharged.
	 * 
	 * @param from
	 *            Orientation the electricity is requested from.
	 * @param request
	 *            Maximum amount of energy to be sent into the block.
	 * @param doReceive
	 *            If false, the charge will only be simulated.
	 * @return Amount of energy that was given out by the block.
	 */
	public float provideOxygen(ForgeDirection from, float request, boolean doProvide);

	/**
	 * @return How much energy does this TileEntity want?
	 */
	public float getOxygenRequest(ForgeDirection direction);

	/**
	 * @return How much energy does this TileEntity want to provide?
	 */
	public float getOxygenProvide(ForgeDirection direction);

}
