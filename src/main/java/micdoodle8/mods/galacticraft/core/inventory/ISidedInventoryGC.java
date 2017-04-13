package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.text.ITextComponent;

/*
 * Like ISidedInventory
 * but providing defaults for the methods we never use
 */
public interface ISidedInventoryGC extends ISidedInventory
{
    //We don't use these because we use forge containers
    @Override
    public default void openInventory(EntityPlayer player)
    {
    }

    //We don't use these because we use forge containers
    @Override
    public default void closeInventory(EntityPlayer player)
    {
    }

    @Override
    public default int getField(int id)
    {
        return 0;
    }

    @Override
    public default void setField(int id, int value)
    {
    }

    @Override
    public default int getFieldCount()
    {
        return 0;
    }

    @Override
    public default void clear()
    {

    }

    @Override
    public default boolean hasCustomName()
    {
        return false;
    }

    @Override
    public default ITextComponent getDisplayName()
    {
        return null;
    }
}
