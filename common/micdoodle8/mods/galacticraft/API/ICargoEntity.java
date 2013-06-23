package micdoodle8.mods.galacticraft.API;

import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoLoader.EnumCargoLoadingState;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoLoader.RemovalResult;
import net.minecraft.item.ItemStack;

public interface ICargoEntity
{
    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd);

    public RemovalResult removeCargo(boolean doRemove);
}
