package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;

import mekanism.api.ITileNetwork;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ISidedInventory;

import com.google.common.io.ByteArrayDataInput;

/**
 * Base code by Aidancbrady
 * 
 * Implemented into Galacticraft by micdoodle8
 *
 */
public abstract class GCCoreTileEntityElectricBase extends GCCoreTileEntityContainer implements ISidedInventory, IInventory, ITileNetwork
{
	public double currentEnergy;
	public double maximumElectricity;
	
	/**
	 * The base of all blocks that deal with electricity. It has a facing state, initialized state,
	 * and a current amount of stored energy.
	 * @param name - full name of this block
	 * @param maxEnergy - how much energy this block can store
	 */
	public GCCoreTileEntityElectricBase(String name, double maxEnergy)
	{
		super(name);
		this.maximumElectricity = maxEnergy;
	}
	
	@Override
	public void handlePacketData(ByteArrayDataInput dataStream)
	{
		super.handlePacketData(dataStream);
		this.currentEnergy = dataStream.readDouble();
	}
	
	@Override
	public ArrayList getNetworkedData(ArrayList data)
	{
		super.getNetworkedData(data);
		data.add(this.currentEnergy);
		return data;
	}
    
	@Override
    public void readFromNBT(NBTTagCompound nbtTags)
    {
        super.readFromNBT(nbtTags);

        this.currentEnergy = nbtTags.getDouble("currentEnergy");
    }

	@Override
    public void writeToNBT(NBTTagCompound nbtTags)
    {
        super.writeToNBT(nbtTags);
        
        nbtTags.setDouble("currentEnergy", this.currentEnergy);
    }
}