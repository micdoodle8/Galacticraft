package gregtechmod.api.interfaces;

/**
 * To access my Machines a bit easier
 */
public interface IUpgradableMachine extends IMachineProgress {
	/**
	 * Accepts Upgrades. Some Machines have an Upgrade Limit.
	 */
	boolean isUpgradable();

	/**
	 * Accepts Muffler Upgrades
	 */
	boolean isMufflerUpgradable();
	
	/**
	 * Accepts MJ-Converter Upgrades
	 */
	boolean isMJConverterUpgradable();
	
	/**
	 * Accepts Steam-Converter Upgrades
	 */
	boolean isSteamEngineUpgradable();
	
	/**
	 * Accepts Overclocker Upgrades
	 */
	boolean isOverclockerUpgradable();
	
	/**
	 * Accepts Transformer Upgrades
	 */
	boolean isTransformerUpgradable();
	
	/**
	 * Accepts Battery Upgrades
	 * if Storage and Tier are 0 then it just checks if it can accept Battery Upgrades in general
	 */
	boolean isBatteryUpgradable(int aStorage, byte aTier);

	/**
	 * Adds Muffler Upgrade
	 */
	boolean addMufflerUpgrade();
	
	/**
	 * Adds MJ-Converter Upgrade
	 */
	boolean addMJConverterUpgrade();
	
	/**
	 * Adds MJ-Converter Upgrade
	 */
	boolean addSteamEngineUpgrade();
	
	/**
	 * Adds Overclocker Upgrade
	 */
	boolean addOverclockerUpgrade();
	
	/**
	 * Adds Transformer Upgrade
	 */
	boolean addTransformerUpgrade();
	
	/**
	 * Adds Battery Upgrade with that much additional Power
	 */
	boolean addBatteryUpgrade(int aStorage, byte aTier);

	/**
	 * Does this Machine have an Muffler
	 */
	boolean hasMufflerUpgrade();
	
	/**
	 * Does this Machine have an MJ-Converter
	 */
	boolean hasMJConverterUpgrade();
	
	/**
	 * Does this Machine have a Steam-Converter
	 */
	boolean hasSteamEngineUpgrade();
	
	/**
	 * returns the amount of Overclockers in this Machine
	 */
	public byte getOverclockerUpgradeCount();
	
	/**
	 * returns the amount of Transformer Upgrades in this Machine
	 */
	public byte getTransformerUpgradeCount();
	
	/**
	 * returns the amount of additional Energy Storage this Machine has due to Upgrade in EU
	 */
	public int getUpgradeStorageVolume();
}
