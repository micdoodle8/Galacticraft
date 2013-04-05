package micdoodle8.mods.galacticraft.API;

/**
 *  For world providers where you would like to specify height for spacecraft to be teleported
 *  
 *  Implement into world providers
 */
public interface IExitHeight 
{
	public double getYCoordinateToTeleport();
}
