package micdoodle8.mods.galacticraft.API;

/**
 *  Implement this interface to let Sensor Goggles see your block.
 */
public interface IDetectableMetadataResource
{
	/**
	 * @return array of metadata values that are considered valueable.
	 */
	public boolean isValueable(int metadata);
}