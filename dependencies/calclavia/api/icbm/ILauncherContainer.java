package calclavia.api.icbm;

import net.minecraft.inventory.IInventory;

/** Applied to TileEntities that contains missiles within them.
 * 
 * @author Calclavia */
public interface ILauncherContainer extends IInventory
{
    @Deprecated
    public IMissile getContainingMissile();

    @Deprecated
    public void setContainingMissile(IMissile missile);

    /** Gets the slots in the tile's inventory that can contant missiles */
    public int[] getMissileSlots();

    /** Retrieves the launcher controller controlling this container. */
    public ILauncherController getController();
}
