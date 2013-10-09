package gregtechmod.api.interfaces;


/**
 * Implemented by all my Machines. However without any security checks, if the Players are even allowed to rotate it.
 */
public interface ITurnable {
	/**
	 * Get the block's facing.
	 * 
	 * @return front Block facing
	 */
	byte getFrontFacing();
	
	/**
	 * Get the block's back facing.
	 * 
	 * @return opposite Block facing
	 */
	byte getBackFacing();
	
	/**
	 * Determine if the wrench can be used to set the block's facing.
	 */
	boolean isValidFacing(byte aSide);
	
	/**
	 * Set the block's facing
	 * 
	 * @param facing facing to set the block to
	 */
	void setFrontFacing(byte aSide);
}