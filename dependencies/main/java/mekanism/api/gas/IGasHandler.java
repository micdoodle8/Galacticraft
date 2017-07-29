package mekanism.api.gas;

import net.minecraft.util.EnumFacing;

/**
 * Implement this if your tile entity accepts gas from an external source.
 * @author AidanBrady
 *
 */
public interface IGasHandler
{
	/**
	 * Transfer a certain amount of gas to this block.
	 * @param stack - gas to add
	 * @return gas added
	 */
	public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer);

	/**
	 * Draws a certain amount of gas from this block.
	 * @param amount - amount to draw
	 * @return gas drawn
	 */
	public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer);

	/**
	 * Whether or not this block can accept gas from a certain side.
	 * @param side - side to check
	 * @param type - type of gas to check
	 * @return if block accepts gas
	 */
	public boolean canReceiveGas(EnumFacing side, Gas type);

	/**
	 * Whether or not this block can be drawn of gas from a certain side.
	 * @param side - side to check
	 * @param type - type of gas to check
	 * @return if block can be drawn of gas
	 */
	public boolean canDrawGas(EnumFacing side, Gas type);
}
