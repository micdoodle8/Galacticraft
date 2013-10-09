package gregtechmod.api.interfaces;

/**
 * This File has just internal Information about the Redstone State of a TileEntity
 */
public interface IRedstoneTileEntity extends IRedstoneEmitter, IRedstoneReceiver {
	/**
	 * enables/disables Redstone Output in general.
	 */
	void setGenericRedstoneOutput(boolean aOnOff);
	
	/**
	 * Causes a general Block update.
	 * Sends nothing to Client, just causes a Block Update.
	 */
	public void issueBlockUpdate();
}