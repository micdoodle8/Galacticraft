package micdoodle8.mods.galacticraft.api.power;

import net.minecraft.util.EnumFacing;

import java.util.List;

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
        public final EnumFacing direction;

        public EnergySourceAdjacent(EnumFacing direction)
        {
            this.direction = direction;
        }
    }
}
