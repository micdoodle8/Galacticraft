package icbm.api.sentry;

/**
 * Apply this to an entity if it is meant to be targeted by the AA Turret.
 * 
 * @author Calclavia
 * 
 */
public interface IAATarget
{
	/**
	 * destroys the target with a boom. This is a forced way for the sentry too kill the target if
	 * it doesn't take damage
	 */
	public void destroyCraft();

	/**
	 * Applies damage to the the target
	 * 
	 * @param damage - damage in half HP
	 * @return the amount of HP left. Return -1 if this target can't take damage, and will be chance
	 * killed. Return 0 if this target is dead and destroyCraft() will be called.
	 */
	public int doDamage(int damage);

	/**
	 * Can this be targeted by automated targeting systems or AIs. Used to implement radar jammers,
	 * cloaking devices, and other addons for the Entity being targeted
	 * 
	 * @param entity - entity that is targeting this, can be an Entity, EntityLiving, or TileEntity
	 * @return true if it can
	 */
	public boolean canBeTargeted(Object entity);
}
