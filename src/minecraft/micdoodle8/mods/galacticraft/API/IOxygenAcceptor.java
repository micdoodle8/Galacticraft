package micdoodle8.mods.galacticraft.API;

/**
 *  The TILE ENTITY should implement this interface, not the block. 
 *  
 *  For the block, see {@link="IConnectableToPipe"}
 */
public interface IOxygenAcceptor
{
	public boolean getActive();
	
	public void setActive(boolean active);
	
	public double getPower();
	
	public void setPower(double power);
	
	public void addSource(TileEntityOxygenSource source);
	
	public void setIndexFromSource(int index);
}
