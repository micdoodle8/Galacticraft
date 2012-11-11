package micdoodle8.mods.galacticraft.API;

public interface IGalacticraftWorldProvider 
{
	/**
	 * gets additional gravity to add to mobs and players in this dimension. Typical values range from 0.040 to 0.065
	 * 
	 * @return additional gravity for this provider
	 */
	public float getGravity();
}
