package micdoodle8.mods.galacticraft.core.energy.tile;

import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

public abstract class TileBaseElectricBlockWithInventory extends TileBaseElectricBlock implements IInventoryDefaults
{
    public TileBaseElectricBlockWithInventory(String tileName)
    {
        super(tileName);
    }

    @Override
    public Direction getElectricInputDirection()
    {
        return Direction.getHorizontal(((this.getBlockMetadata() & 3) + 1) % 4);
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }
}
