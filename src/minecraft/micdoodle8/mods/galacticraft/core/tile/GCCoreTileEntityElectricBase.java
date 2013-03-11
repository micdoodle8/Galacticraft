package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;

import mekanism.api.ITileNetwork;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ISidedInventory;
import net.minecraftforge.common.MinecraftForge;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;

import com.google.common.io.ByteArrayDataInput;

/**
 * Base code by Aidancbrady
 * 
 * Implemented into Galacticraft by micdoodle8
 *
 */
public abstract class GCCoreTileEntityElectricBase extends GCCoreTileEntityContainer implements ISidedInventory, IInventory, ITileNetwork, IPowerReceptor
{
	public IPowerProvider powerProvider;

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
		maximumElectricity = maxEnergy;
		
		if(PowerFramework.currentFramework != null)
		{
			powerProvider = PowerFramework.currentFramework.createPowerProvider();
			powerProvider.configure(0, 0, 100, 0, (int)(maxEnergy * GalacticraftCore.BuildcraftEnergyScalar));
		}
	}
	
	@Override
	public void handlePacketData(ByteArrayDataInput dataStream)
	{
		super.handlePacketData(dataStream);
		currentEnergy = dataStream.readDouble();
	}
	
	@Override
	public ArrayList getNetworkedData(ArrayList data)
	{
		super.getNetworkedData(data);
		data.add(currentEnergy);
		return data;
	}
    
	@Override
    public void readFromNBT(NBTTagCompound nbtTags)
    {
        super.readFromNBT(nbtTags);
        
        if(PowerFramework.currentFramework != null)
        {
        	PowerFramework.currentFramework.loadPowerProvider(this, nbtTags);
        }

        currentEnergy = nbtTags.getDouble("currentEnergy");
    }

	@Override
    public void writeToNBT(NBTTagCompound nbtTags)
    {
        super.writeToNBT(nbtTags);
        
        if(PowerFramework.currentFramework != null)
        {
        	PowerFramework.currentFramework.savePowerProvider(this, nbtTags);
        }
        
        nbtTags.setDouble("currentEnergy", currentEnergy);
    }
	
	@Override
	public void setPowerProvider(IPowerProvider provider)
	{
		powerProvider = provider;
	}
	
	@Override
	public IPowerProvider getPowerProvider() 
	{
		return powerProvider;
	}
	
	@Override
	public int powerRequest() 
	{
		return (int)((maximumElectricity - currentEnergy) * GalacticraftCore.BuildcraftEnergyScalar);
	}
	
	@Override
	public void doWork() {}
}