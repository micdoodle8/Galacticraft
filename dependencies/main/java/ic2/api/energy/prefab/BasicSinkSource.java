package ic2.api.energy.prefab;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.info.ILocatable;

/**
 * Combination of BasicSink and BasicSource, see their respective documentation for details.
 *
 * <p>A subclass still has to implement acceptsEnergyFrom and emitsEnergyTo.
 */
public abstract class BasicSinkSource extends BasicEnergyTile implements IEnergySink, IEnergySource {
	public BasicSinkSource(TileEntity parent, double capacity, int sinkTier, int sourceTier) {
		super(parent, capacity);

		if (sinkTier < 0) throw new IllegalArgumentException("invalid sink tier: "+sinkTier);
		if (sourceTier < 0) throw new IllegalArgumentException("invalid source tier: "+sourceTier);

		this.sinkTier = sinkTier;
		this.sourceTier = sourceTier;
		double power = EnergyNet.instance.getPowerFromTier(sourceTier);

		if (getCapacity() < power) setCapacity(power);
	}

	public BasicSinkSource(ILocatable parent, double capacity, int sinkTier, int sourceTier) {
		super(parent, capacity);

		if (sinkTier < 0) throw new IllegalArgumentException("invalid sink tier: "+sinkTier);
		if (sourceTier < 0) throw new IllegalArgumentException("invalid source tier: "+sourceTier);

		this.sinkTier = sinkTier;
		this.sourceTier = sourceTier;
		double power = EnergyNet.instance.getPowerFromTier(sourceTier);

		if (getCapacity() < power) setCapacity(power);
	}

	public BasicSinkSource(World world, BlockPos pos, double capacity, int sinkTier, int sourceTier) {
		super(world, pos, capacity);

		if (sinkTier < 0) throw new IllegalArgumentException("invalid sink tier: "+sinkTier);
		if (sourceTier < 0) throw new IllegalArgumentException("invalid source tier: "+sourceTier);

		this.sinkTier = sinkTier;
		this.sourceTier = sourceTier;
		double power = EnergyNet.instance.getPowerFromTier(sourceTier);

		if (getCapacity() < power) setCapacity(power);
	}

	/**
	 * Set the IC2 energy tier for this sink.
	 *
	 * @param tier IC2 Tier.
	 */
	public void setSinkTier(int tier) {
		if (tier < 0) throw new IllegalArgumentException("invalid tier: "+tier);

		this.sinkTier = tier;
	}

	/**
	 * Set the IC2 energy tier for this source.
	 *
	 * @param tier IC2 Tier.
	 */
	public void setSourceTier(int tier) {
		if (tier < 0) throw new IllegalArgumentException("invalid tier: "+tier);

		double power = EnergyNet.instance.getPowerFromTier(tier);

		if (getCapacity() < power) setCapacity(power);

		this.sourceTier = tier;
	}

	// energy net interface >>

	@Override
	public double getDemandedEnergy() {
		return Math.max(0, getCapacity() - getEnergyStored());
	}

	@Override
	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
		setEnergyStored(getEnergyStored() + amount);

		return 0;
	}

	@Override
	public int getSinkTier() {
		return sinkTier;
	}

	@Override
	public double getOfferedEnergy() {
		return getEnergyStored();
	}

	@Override
	public void drawEnergy(double amount) {
		setEnergyStored(getEnergyStored() - amount);
	}

	@Override
	public int getSourceTier() {
		return sourceTier;
	}

	// << energy net interface

	@Override
	protected String getNbtTagName() {
		return "IC2BasicSinkSource";
	}

	protected int sinkTier;
	protected int sourceTier;
}
