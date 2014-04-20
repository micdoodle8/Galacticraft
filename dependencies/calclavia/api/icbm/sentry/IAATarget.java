package calclavia.api.icbm.sentry;

import net.minecraft.util.DamageSource;

/** Apply this to an entity if it is meant to be targeted by the AA Turret.
 * 
 * @author Darkguardsman */
public interface IAATarget
{
    /** Attacks the aa target in the same way as Entity.attackEntityFrom
     * 
     * @param source - Damage source
     * @param damage - actual damage to apply
     * @return true if the damage was applied */
    public boolean attackEntityFrom(DamageSource source, float damage);

    /** Can this be targeted by automated targeting systems or AIs. Used to implement radar jammers,
     * cloaking devices, and other addons for the Entity being targeted
     * 
     * @param entity - entity that is targeting this, can be an Entity, EntityLiving, or TileEntity
     * @return true if it can */
    public boolean canBeTargeted(Object entity);
}