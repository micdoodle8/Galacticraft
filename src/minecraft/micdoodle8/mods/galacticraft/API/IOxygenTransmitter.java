package micdoodle8.mods.galacticraft.API;


/**
 *  The TILE ENTITY should implement this interface, not the block. 
 *  
 *  For the block, see {@link="IConnectableToPipe"}
 */
public interface IOxygenTransmitter
{
	public void setOxygenInTransmitter(double oxygen);
	
	public double getOxygenInTransmitter();
	
	public void addSource(TileEntityOxygenSource source);
	
	public void setIndexFromSource(int index);
}
