package micdoodle8.mods.galacticraft.API.tile;

/**
 * Currently only used internally when enable/disable buttons are clicked
 */
public interface IDisableableMachine
{
    /**
     * Sets the machine to disabled or enabled
     * 
     * @param disabled
     *            whether or not the machine should be set to the enabled or
     *            disabled state
     */
    public void setDisabled(boolean disabled);

    /**
     * Get the current state of the machine (enabled/disabled)
     * 
     * @return the current state of the machine (enabled/disabled)
     */
    public boolean getDisabled();
}
