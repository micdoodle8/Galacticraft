package micdoodle8.mods.galacticraft.core.energy.tile;

import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public abstract class TileBaseElectricBlockWithInventory extends TileBaseElectricBlock implements IInventoryDefaults
{
    public TileBaseElectricBlockWithInventory(TileEntityType<?> type)
    {
        super(type);
    }

//    @Override
//    public Direction getElectricInputDirection()
//    {
//        return Direction.byHorizontalIndex(((this.getBlockMetadata() & 3) + 1) % 4);
//    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }
}
