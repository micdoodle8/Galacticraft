package gregtechmod.api.interfaces;

public interface IGearEnergyTileEntity {
	/**
	 * If Rotation Energy can be accepted on this Side.
	 * This means that the Gear will connect to this Side, and can cause the Gear to stop if the Energy isn't accepted.
	 */
	public boolean acceptsRotationalEnergy(byte aSide);
	
	/**
	 * Inject Energy Call for Rotational Energy.
	 * Rotation Energy can't be stored, this is just for things like internal Dynamos, which convert it into Energy, or into Progress.
	 */
	public boolean injectRotationalEnergy(byte aSide, int aSpeed, int aEnergy);
}
