package micdoodle8.mods.galacticraft.api.transmission.core.grid;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

/**
 * An Electrical Network specifies a wire connection. Each wire connection line
 * will have its own electrical network.
 * 
 * !! Do not include this class if you do not intend to have custom wires in
 * your mod. This will increase future compatibility. !!
 * 
 * @author Calclavia
 * 
 */
public abstract class ElectricityNetwork implements IElectricityNetwork
{
	public Map<TileEntity, ForgeDirection> electricalTiles = new HashMap<TileEntity, ForgeDirection>();

	private final Set<IConductor> conductors = new HashSet<IConductor>();

	public float acceptorResistance = 500;

	/**
	 * @return Returns all producers in this electricity network.
	 */
	@Override
	public Set<TileEntity> getAcceptors()
	{
		return this.electricalTiles.keySet();
	}

	/**
	 * @param tile
	 *            The tile to get connections for
	 * @return The list of directions that can be connected to for the provided
	 *         tile
	 */
	@Override
	public ForgeDirection getPossibleDirections(TileEntity tile)
	{
		return this.electricalTiles.containsKey(tile) ? this.electricalTiles.get(tile) : null;
	}

	@Override
	public float getTotalResistance()
	{
		float resistance = 0;

		for (IConductor conductor : this.conductors)
		{
			resistance += conductor.getResistance();
		}

		return resistance;
	}

	@Override
	public float getLowestCurrentCapacity()
	{
		float lowestAmperage = 0;

		for (IConductor conductor : this.conductors)
		{
			if (lowestAmperage == 0 || conductor.getCurrentCapacity() < lowestAmperage)
			{
				lowestAmperage = conductor.getCurrentCapacity();
			}
		}

		return lowestAmperage;
	}

	@Override
	public Set<IConductor> getTransmitters()
	{
		return this.conductors;
	}
}
