package micdoodle8.mods.galacticraft.api.block;

/**
 * Implement this interface to let Sensor Goggles see your block.
 */
public interface IDetectableResource
{
	/**
	 * @return array of metadata values that are considered valueable.
	 */
	public boolean isValueable(int metadata);
}
