package micdoodle8.mods.galacticraft.api.transmission.tile;

/**
 * This interface is to be applied to all TileEntities which stores electricity
 * within them.
 * 
 * @author Calclavia
 */
public interface IOxygenStorage
{
	/**
	 * Sets the amount of joules this unit has stored.
	 */
	public void setOxygenStored(float energy);

	/**
	 * * @return Get the amount of energy currently stored in the block.
	 */
	public float getOxygenStored();

	/**
	 * @return Get the max amount of energy that can be stored in the block.
	 */
	public float getMaxOxygenStored();
}
