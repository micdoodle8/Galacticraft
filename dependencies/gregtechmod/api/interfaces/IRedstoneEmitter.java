package gregtechmod.api.interfaces;

/**
 * This File has just internal Information about the Redstone State of a TileEntity
 */
public interface IRedstoneEmitter extends IHasWorldObjectAndCoords {
	/**
	 * gets the Redstone Level the TileEntity should emit to the given Output Side
	 */
	byte getOutputRedstoneSignal(byte aSide);
	
	/**
	 * sets the Redstone Level the TileEntity should emit to the given Output Side
	 * 
	 * Do not use this if ICoverable is implemented. ICoverable has @getInternalOutputRedstoneSignal for Machine internal Output Redstone, so that it doesnt conflict with Cover Redstone.
	 * This sets the true Redstone Output Signal. Only Cover Behaviors should use it, not MetaTileEntities.
	 */
	void setOutputRedstoneSignal(byte aSide, byte aStrength);
	
	/**
	 * gets the Redstone Level the TileEntity should emit to the given Output Side
	 */
	byte getStrongOutputRedstoneSignal(byte aSide);
	
	/**
	 * sets the Redstone Level the TileEntity should emit to the given Output Side
	 * 
	 * Do not use this if ICoverable is implemented. ICoverable has @getInternalOutputRedstoneSignal for Machine internal Output Redstone, so that it doesnt conflict with Cover Redstone.
	 * This sets the true Redstone Output Signal. Only Cover Behaviors should use it, not MetaTileEntities.
	 */
	void setStrongOutputRedstoneSignal(byte aSide, byte aStrength);
	
	/**
	 * Gets the Output for the comparator on the given Side
	 */
	byte getComparatorValue(byte aSide);
}