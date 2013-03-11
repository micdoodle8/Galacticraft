package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.ArrayList;
import java.util.Arrays;

import mekanism.api.EnumGas;
import mekanism.api.IGasAcceptor;
import mekanism.api.IPressurizedTube;
import mekanism.api.ITubeConnection;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import cpw.mods.fml.common.FMLLog;

/**
 * 
 * @author Aidancbrady
 * 
 * Implemented into Galacticraft by micdoodle8
 *
 */

public class OxygenNetwork 
{
	/** List of iterated tubes, to prevent infinite loops. */
	public ArrayList<TileEntity> iteratedTubes = new ArrayList<TileEntity>();
	
	/** List of IGasAcceptors that can take in the type of gas requested. */
	public ArrayList<IGasAcceptor> availableAcceptors = new ArrayList<IGasAcceptor>();
	
	/** Pointer of this calculation */
	public TileEntity pointer;
	
	/** Type of gas to distribute */
	public EnumGas transferType;
	
	/** Amount of gas to distribute  */
	public int gasToSend;
	
	/**
	 * OxygenNetwork -- a calculation used to distribute gasses through a tube network.
	 * @param head - pointer tile entity
	 * @param type - type of gas to distribute
	 * @param amount - amount of gas to distribute
	 */
	public OxygenNetwork(TileEntity head, EnumGas type, int amount)
	{
		pointer = head;
		transferType = type;
		gasToSend = amount;
	}
	
	/**
	 * Recursive loop that iterates through connected tubes and adds connected acceptors to an ArrayList.
	 * @param tile - pointer tile entity
	 */
	public void loopThrough(TileEntity tile)
	{
		IGasAcceptor[] acceptors = this.getConnectedAcceptors(tile);
		
		for(IGasAcceptor acceptor : acceptors)
		{
			if(acceptor != null)
			{
				if(acceptor.canReceiveGas(ForgeDirection.getOrientation(Arrays.asList(acceptors).indexOf(acceptor)).getOpposite(), transferType))
				{
					availableAcceptors.add(acceptor);
				}
			}
		}
		
		iteratedTubes.add(tile);
		
		TileEntity[] tubes = this.getConnectedTubes(tile);
		
		for(TileEntity tube : tubes)
		{
			if(tube != null)
			{
				if(!iteratedTubes.contains(tube))
				{
					loopThrough(tube);
				}
			}
		}
	}
    
    /**
     * Gets all the tubes around a tile entity.
     * @param tileEntity - center tile entity
     * @return array of TileEntities
     */
    public static TileEntity[] getConnectedTubes(TileEntity tileEntity)
    {
    	TileEntity[] tubes = new TileEntity[] {null, null, null, null, null, null};
    	
    	for(ForgeDirection orientation : ForgeDirection.values())
    	{
    		if(orientation != ForgeDirection.UNKNOWN)
    		{
    			Vector3 vec = new Vector3(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
    			
    			TileEntity tube = vec.clone().modifyPositionFromSide(orientation).getTileEntity(tileEntity.worldObj);
    			
    			if(tube instanceof IPressurizedTube && ((IPressurizedTube)tube).canTransferGas())
    			{
    				tubes[orientation.ordinal()] = tube;
    			}
    		}
    	}
    	
    	return tubes;
    }
    
    /**
     * Gets all the acceptors around a tile entity.
     * @param tileEntity - center tile entity
     * @return array of IGasAcceptors
     */
    public static IGasAcceptor[] getConnectedAcceptors(TileEntity tileEntity)
    {
    	IGasAcceptor[] acceptors = new IGasAcceptor[] {null, null, null, null, null, null};
    	
    	for(ForgeDirection orientation : ForgeDirection.values())
    	{
    		if(orientation != ForgeDirection.UNKNOWN)
    		{
    			Vector3 vec = new Vector3(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
    			
    			TileEntity acceptor = vec.clone().modifyPositionFromSide(orientation).getTileEntity(tileEntity.worldObj);
    			
    			if(acceptor instanceof IGasAcceptor)
    			{
    				acceptors[orientation.ordinal()] = (IGasAcceptor)acceptor;
    			}
    		}
    	}
    	
    	return acceptors;
    }
    
    /**
     * Gets all the tube connections around a tile entity.
     * @param tileEntity - center tile entity
     * @return array of ITubeConnections
     */
    public static ITubeConnection[] getConnections(TileEntity tileEntity)
    {
    	ITubeConnection[] connections = new ITubeConnection[] {null, null, null, null, null, null};
    	
    	for(ForgeDirection orientation : ForgeDirection.values())
    	{
    		if(orientation != ForgeDirection.UNKNOWN)
    		{
    			TileEntity connection = VectorHelper.getTileEntityFromSide(tileEntity.worldObj, new Vector3(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord), orientation);
    			
    			if(connection instanceof ITubeConnection)
    			{
    				connections[orientation.ordinal()] = (ITubeConnection)connection;
    			}
    		}
    	}
    	
    	return connections;
    }
    
    /**
     * Emits a defined gas to the network.
     * @param type - gas type to send
     * @param amount - amount of gas to send
     * @param sender - the sender of the gas
     * @param facing - side the sender is outputting from
     * @return rejected gas
     */
    public static int emitGasToNetwork(EnumGas type, int amount, TileEntity sender, ForgeDirection facing)
    {
		TileEntity pointer = VectorHelper.getTileEntityFromSide(sender.worldObj, new Vector3(sender), facing);
		
    	if(pointer != null)
    	{
	    	OxygenNetwork calculation = new OxygenNetwork(pointer, type, amount);
	    	return calculation.calculate();
    	}
    	
    	return amount;
    }
	
	/**
	 * Runs the protocol and distributes the gas.
	 * @return rejected gas
	 */
	public int calculate()
	{
		loopThrough(pointer);
		
		if(!availableAcceptors.isEmpty())
		{
			boolean sentRemaining = false;
			int divider = availableAcceptors.size();
			int remaining = gasToSend % divider;
			int sending = (gasToSend-remaining)/divider;
			
			for(IGasAcceptor acceptor : availableAcceptors)
			{
				int currentSending = sending;
				
				if(remaining > 0)
				{
					currentSending++;
					remaining--;
				}
				
				int rejects = acceptor.transferGasToAcceptor(currentSending, transferType);
				gasToSend -= (currentSending - rejects);
			}
		}
		
		return gasToSend;
	}
}