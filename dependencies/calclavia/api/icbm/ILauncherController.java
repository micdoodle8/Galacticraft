package calclavia.api.icbm;

import net.minecraft.item.ItemStack;

/** Applied to all launcher TileEntitiies that operates the launching of missiles.
 * 
 * @author Calclavia */
public interface ILauncherController extends IBlockFrequency
{
    /** What type of launcher is this? */
    public LauncherType getLauncherType();

    /** Launches the missile into the specified target. */
    public void launch();

    /** Can the launcher launch the missile? */
    public boolean canLaunch();

    /** @return The status of the launcher. */
    public String getStatus();

    /** @return The target of the launcher. */
//    public Vector3 getTarget();
//
//    /** @param target Sets the target of the launcher */
//    public void setTarget(Vector3 target);

    /** Places a missile into the launcher. */
    public void placeMissile(ItemStack itemStack);

    public IMissile getMissile();
}