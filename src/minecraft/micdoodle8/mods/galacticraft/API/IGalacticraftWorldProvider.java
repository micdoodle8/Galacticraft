package micdoodle8.mods.galacticraft.API;

public interface IGalacticraftWorldProvider
{
	/**
	 * gets additional gravity to add to players in this dimension. Typical values range from 0.040 to 0.065
	 *
	 * @return additional gravity for this provider
	 */
	public float getGravity();

	/**
	 * 	Determines the rate to spawn meteors in this planet. Lower means MORE meteors.
	 *
	 * 	Typical value would be about 7. Return 0 for no meteors.
	 *
	 * @return
	 */
	public float getMeteorFrequency();
}
