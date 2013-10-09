package icbm.api;

/**
 * Applied to TileEntities that contains missiles within them.
 * 
 * @author Calclavia
 * 
 */
public interface ILauncherContainer
{
	public IMissile getContainingMissile();

	public void setContainingMissile(IMissile missile);

	/**
	 * Retrieves the launcher controller controlling this container.
	 */
	public ILauncherController getController();
}
