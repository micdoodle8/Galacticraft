package gregtechmod.api.interfaces;

/**
 * Interface for internal Code, which is mainly used for independent Energy conversion.
 */
public interface IBasicEnergyContainer {
	/**
	 * Gets if that Amount of Energy is stored inside the Machine.
	 * It is used for checking the contained Energy before consuming it.
	 * If this returns false, it will also give a Message inside the Scanner, that this Machine doesn't have enough Energy.
	 */
	public boolean isUniversalEnergyStored(int aEnergyAmount);
	
	/**
	 * Gets the stored electric, kinetic or steam Energy (with EU as reference Value)
	 * Always returns the largest one.
	 */
	public int getUniversalEnergyStored();
	
	/**
	 * Gets the largest electric, kinetic or steam Energy Capacity (with EU as reference Value)
	 */
	public int getUniversalEnergyCapacity();
	
	/**
	 * Gets the amount of Energy Packets per tick.
	 */
	public int getOutputAmperage();
	
	/**
	 * Gets the Output in EU/p.
	 */
	public int getOutputVoltage();
	
	/**
	 * Gets the maximum Input in EU/p.
	 */
	public int getInputVoltage();
	
	/**
	 * Decreases the Amount of stored universal Energy. If ignoring too less Energy, then it just sets the Energy to 0 and returns false.
	 */
	public boolean decreaseStoredEnergyUnits(int aEnergy, boolean aIgnoreTooLessEnergy);
	
	/**
	 * Increases the Amount of stored electric Energy. If ignoring too much Energy, then the Energy Limit is just being ignored.
	 */
	public boolean increaseStoredEnergyUnits(int aEnergy, boolean aIgnoreTooMuchEnergy);

	/**
	 * Inject Energy Call for Electricity.
	 */
	public boolean injectEnergyUnits(byte aSide, int aVoltage, int aAmperage);
	
	/**
	 * Drain Energy Call for Electricity.
	 */
	public boolean drainEnergyUnits(byte aSide, int aVoltage, int aAmperage);
	
	/**
	 * Sided Energy Input
	 */
	public boolean inputEnergyFrom(byte aSide);
	
	/**
	 * Sided Energy Output
	 */
	public boolean outputsEnergyTo(byte aSide);
	
	/**
	 * returns the amount of Electricity, accepted by this Block the last 5 ticks as Average.
	 */
	public int getAverageElectricInput();
	
	/**
	 * returns the amount of Electricity, outputted by this Block the last 5 ticks as Average.
	 */
	public int getAverageElectricOutput();
	
	/**
	 * returns the amount of electricity contained in this Block, in EU units!
	 */
	public int getStoredEU();
	
	/**
	 * returns the amount of electricity containable in this Block, in EU units!
	 */
	public int getEUCapacity();
	
	/**
	 * returns the amount of MJ contained in this Block, in EU units!
	 */
	public int getStoredMJ();
	
	/**
	 * returns the amount of MJ containable in this Block, in EU units!
	 */
	public int getMJCapacity();
	
	/**
	 * Increases stored Energy. Energy Base Value is in EU, even though it's MJ!
	 * @param aEnergy The Energy to add to the Machine.
	 * @param aIgnoreTooMuchEnergy if it shall ignore if it has too much Energy.
	 * @return if it was successful
	 * 
	 * And yes, you can't directly decrease the MJ of a Machine. That is done by decreaseStoredEnergyUnits
	 */
	public boolean increaseStoredMJ(int aEnergy, boolean aIgnoreTooMuchEnergy);
	
	/**
	 * returns the amount of Steam contained in this Block, in EU units!
	 */
	public int getStoredSteam();
	
	/**
	 * returns the amount of Steam containable in this Block, in EU units!
	 */
	public int getSteamCapacity();
	
	/**
	 * Increases stored Energy. Energy Base Value is in EU, even though it's Steam!
	 * @param aEnergy The Energy to add to the Machine.
	 * @param aIgnoreTooMuchEnergy if it shall ignore if it has too much Energy.
	 * @return if it was successful
	 * 
	 * And yes, you can't directly decrease the Steam of a Machine. That is done by decreaseStoredEnergyUnits
	 */
	public boolean increaseStoredSteam(int aEnergy, boolean aIgnoreTooMuchEnergy);
}