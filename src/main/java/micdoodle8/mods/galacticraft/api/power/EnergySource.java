package micdoodle8.mods.galacticraft.api.power;

import net.minecraftforge.common.util.ForgeDirection;

public abstract class EnergySource
{
	public class EnergySourceWireless extends EnergySource
	{
		public final ILaserNode node;
		
		public EnergySourceWireless(ILaserNode node)
		{
			this.node = node;
		}
	}

	public class EnergySourceAdjacent extends EnergySource
	{
		public final ForgeDirection direction;
		
		public EnergySourceAdjacent(ForgeDirection direction)
		{
			this.direction = direction;
		}
	}
}
