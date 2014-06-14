package micdoodle8.mods.galacticraft.api.transmission;

import micdoodle8.mods.galacticraft.api.transmission.core.grid.IElectricityNetwork;
import micdoodle8.mods.galacticraft.api.transmission.tile.IElectrical;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

public class ElectricalEvent extends Event
{
	@Cancelable
	public static class ElectricityProduceEvent extends ElectricalEvent
	{
		public World world;
		public IElectrical tileEntity;

		public ElectricityProduceEvent(IElectrical tileEntity)
		{
			this.tileEntity = tileEntity;
			this.world = ((TileEntity) this.tileEntity).getWorldObj();
		}
	}

	public static class NetworkEvent extends ElectricalEvent
	{
		public final IElectricityNetwork network;
		public int energy;
		public TileEntity[] ignoreTiles;

		public NetworkEvent(IElectricityNetwork network, int energy, TileEntity... ignoreTiles)
		{
			this.network = network;
			this.energy = energy;
			this.ignoreTiles = ignoreTiles;
		}
	}
	
	@Cancelable
	public static class ElectricityProductionEvent extends NetworkEvent
	{
		public ElectricityProductionEvent(IElectricityNetwork network, int energy, TileEntity... ignoreTiles)
		{
			super(network, energy, ignoreTiles);
		}
	}

	public static class ElectricityRequestEvent extends NetworkEvent
	{
		public ElectricityRequestEvent(IElectricityNetwork network, int energy, TileEntity... ignoreTiles)
		{
			super(network, energy, ignoreTiles);
		}
	}

}
