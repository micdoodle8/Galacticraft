package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.power.EnergySource.EnergySourceWireless;
import micdoodle8.mods.galacticraft.api.power.ILaserNode;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.world.World;

public class WirelessEnergyNetwork 
{
	public List<TileEntityBeamReceiver> receivers = new ArrayList<TileEntityBeamReceiver>();
	public List<TileEntityBeamReflector> reflectors = new ArrayList<TileEntityBeamReflector>();
	
	public boolean isDirty = false;
	public final World worldObj;
	public final Vector3 color;
	
	public WirelessEnergyNetwork(World worldObj)
	{
		this.worldObj = worldObj;
		this.color = new Vector3(0, 1, 0);
	}
	
//	public void recalculate(TileEntityBeamReceiver start)
//	{
//		this.receivers.clear();
//		this.reflectors.clear();
//		
//		this.receivers.add(start);
//		
//		ILaserNode target = start.target;
//
//		while (target != null)
//		{
//			if (target instanceof TileEntityBeamReceiver)
//			{
//				this.receivers.add((TileEntityBeamReceiver) target);
//			}
//			else if (target instanceof TileEntityBeamReflector)
//			{
//				this.reflectors.add((TileEntityBeamReflector) target);
//			}
//			
//			target = target.getTarget();
//		}
//	}
	
//	public int emit(int maxEmit, boolean simulate)
//	{
//		int targetCount = this.receivers.size();
//		int energyRemaining = maxEmit;
//		int toSend = (int) Math.floor(energyRemaining / (double)targetCount);
//		int sentEnergy = 0;
//		
//		for (TileEntityBeamReceiver receiver : this.receivers)
//		{
//			int received = receiver.receiveEnergyGC(new EnergySourceWireless(this.receivers.get(0)), amount, simulate)
//			targetCount--;
//			toSend = (int) Math.floor(energyRemaining / (double)targetCount);
//		}
//	}
}
