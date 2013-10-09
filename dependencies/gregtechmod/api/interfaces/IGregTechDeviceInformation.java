package gregtechmod.api.interfaces;

/**
 * You are allowed to include this File in your Download, as i will not change it.
 */
public interface IGregTechDeviceInformation {
	/**
	 * Is this even a TileEntity which allows GregTech Sensor Kits?
	 * I need things like this Function for MetaTileEntities, you MUST check this!!!
	 * Do not assume that it's a Information returning Device, when it just implements this Interface.
	 */
	public boolean isGivingInformation();
	
	/**
	 * @return the first Line displayed by the Nuclear Control Panel
	 */
	public String getMainInfo();
	
	/**
	 * @return the second Line displayed by the Nuclear Control Panel
	 */
	public String getSecondaryInfo();
	
	/**
	 * @return the third Line displayed by the Nuclear Control Panel
	 */
	public String getTertiaryInfo();
}
