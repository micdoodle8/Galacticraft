package gregtechmod.api.interfaces;

/**
 * For Machines which have Progress
 */
public interface IMachineProgress extends IHasWorldObjectAndCoords {
	/**
	 * returns the Progress this Machine has made. Warning, this can also be negative!
	 */
	public int getProgress();
	
	/**
	 * returns the Progress the Machine needs to complete its task.
	 */
	public int getMaxProgress();
	
	/**
	 * increases the Progress of the Machine
	 */
	public boolean increaseProgress(int aProgressAmountInTicks);
	
	/**
	 * returns if the Machine currently does something.
	 */
	public boolean hasThingsToDo();
	
	/**
	 * returns if the Machine just got enableWorking called after being disabled.
	 * Used for Translocators, which need to check if they need to transfer immediately.
	 */
	public boolean hasWorkJustBeenEnabled();
	
	/**
	 * allows Machine to work
	 */
	public void enableWorking();
	
	/**
	 * disallows Machine to work
	 */
	public void disableWorking();
	
	/**
	 * disallows Machine to work
	 */
	public boolean isAllowedToWork();
	
	/**
	 * used to control Machines via Redstone Signal Strength by special Covers
	 * only Values between 0 and 15!
	 */
	public void setWorkDataValue(byte aValue);
	
	/**
	 * used to control Machines via Redstone Signal Strength by special Covers
	 * In case of 0 the Machine is very likely doing nothing, or is just not being controlled at all.
	 */
	public byte getWorkDataValue();

	/**
	 * gives you the Active Status of the Machine
	 */
	public boolean isActive();
	
	/**
	 * sets the visible Active Status of the Machine
	 */
	public void setActive(boolean aActive);
}