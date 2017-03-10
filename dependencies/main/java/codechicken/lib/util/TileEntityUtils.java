package codechicken.lib.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by covers1624 on 6/30/2016.
 */
public class TileEntityUtils {

    /**
     * Retrieves an IInventory at the given location, Useful for receiving the entire inventory of a double chest.
     */
    public static IInventory getInventory(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof IInventory) {
            IInventory inventory = (IInventory) tileEntity;
            if (inventory instanceof TileEntityChest) {
                TileEntityChest first = (TileEntityChest) tileEntity;
                TileEntityChest second = null;
                for (BlockPos adjacentPos : BlockUtils.getAdjacent(pos, false)) {
                    TileEntity suspect = world.getTileEntity(adjacentPos);
                    if (suspect instanceof TileEntityChest) {
                        second = (TileEntityChest) suspect;
                        break;
                    }
                }
                if (second != null) {
                    return new InventoryLargeChest("large chest", first, second);
                }
            }
            return inventory;
        }
        return null;
    }

    /**
     * Gets an IItemHandler at the given position. If the capability doesn't exist it returns null.
     *
     * @param world  World the tile is in.
     * @param pos    Position the tile is at.
     * @param facing The face of the tile to get the capability for.
     * @return The IItemHandler existing on the tile.
     */
    public static IItemHandler getIItemHandler(World world, BlockPos pos, EnumFacing facing) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null) {
            if (tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
                return tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
            }
        }
        return null;
    }

}
