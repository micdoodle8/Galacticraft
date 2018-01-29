package codechicken.lib.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

import java.util.HashMap;

/**
 * Created by covers1624 on 17/10/2016.
 */
public class FuelUtils implements IFuelHandler {

    private static HashMap<ItemStack, Integer> fuelBurnMap = new HashMap<ItemStack, Integer>();

    @Override
    public int getBurnTime(ItemStack fuel) {

        if (fuelBurnMap.containsKey(fuel)) {
            return fuelBurnMap.get(fuel);
        }
        return -1;
    }

    public static void registerFuel(ItemStack stack, int burn) {
        fuelBurnMap.put(stack, burn);
    }

    public static void registerFuel(Block block, int burn) {
        registerFuel(new ItemStack(block), burn);
    }

    public static void registerFuel(Item item, int burn) {
        registerFuel(new ItemStack(item), burn);
    }
}
