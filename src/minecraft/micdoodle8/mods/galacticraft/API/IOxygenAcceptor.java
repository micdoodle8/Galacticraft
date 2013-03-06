package micdoodle8.mods.galacticraft.API;

import java.util.Set;

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
	
	public void removeSource(TileEntityOxygenSource source);

	public Set<TileEntityOxygenSource> getSourceCollectors();
	
	public void setSourceCollectors(Set<TileEntityOxygenSource> sources);
	
	public int getIndexFromSource();
	
	public void setIndexFromSource(int index);
}
