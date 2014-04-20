package calclavia.api.icbm;

import calclavia.api.icbm.explosion.IExplosiveContainer;

/** This is an interface applied by all missile entities. You may cast this into an @Entity. The
 * "set" version of the function will make the entity do the action on the next tick.
 * 
 * @author Calclavia */
public interface IMissile extends IExplosiveContainer
{
    /** Blows up this missile. It will detonate the missile with the appropriate explosion. */
    public void explode();

    public void setExplode();

    /** Blows up this missile like a TNT explosion. Small explosion used for events such as a missile
     * crashing or failure to explode will result in this function being called. */
    public void normalExplode();

    public void setNormalExplode();

    /** Drops the specified missile as an item. */
    public void dropMissileAsItem();

    /** The amount of ticks this missile has been flying for. Returns -1 if the missile is not
     * flying. */
    public int getTicksInAir();

    /** Gets the launcher this missile is launched from. */
    public ILauncherContainer getLauncher();

    /** Launches the missile into a specific target.
//     * 
//     * @param target */
//    public void launch(Vector3 target);
//
//    public void launch(Vector3 target, int height);
}
