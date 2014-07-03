package micdoodle8.mods.galacticraft.api.power;

import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

public abstract class EnergySource
{
	public static class EnergySourceWireless extends EnergySource
	{
		public final List<ILaserNode> nodes;

		public EnergySourceWireless(List<ILaserNode> nodes)
		{
			this.nodes = nodes;
		}
	}

	public static class EnergySourceAdjacent extends EnergySource
	{
		public final ForgeDirection direction;

		public EnergySourceAdjacent(ForgeDirection direction)
		{
			this.direction = direction;
		}
	}
}
