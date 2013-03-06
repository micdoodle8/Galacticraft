package micdoodle8.mods.galacticraft.API;

/**
 *  The TILE ENTITY should implement this interface, not the block. 
 *  
 *  For the block, see {@link="IConnectableToPipe"}
 */
public interface IOxygenSource
{
	public double getPower();
	
	public void setPower(double power);
}