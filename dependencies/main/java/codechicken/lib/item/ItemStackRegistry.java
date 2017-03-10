package codechicken.lib.item;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Created by covers1624 on 22/10/2016.
 * TODO, We need to find another way to do this.
 */
public class ItemStackRegistry {

    private static Table<String, String, ItemStack> stackRegistry = HashBasedTable.create();

    public static void registerCustomItemStack(String name, ItemStack stack) {
        stackRegistry.put(Loader.instance().activeModContainer().getModId(), name, stack);
    }

    public static ItemStack findItemStack(String modId, String name, int stackSize) {
        ItemStack foundStack = findItemStack(modId, name);
        if (foundStack != null) {
            ItemStack copy = foundStack.copy();
            copy.stackSize = Math.min(stackSize, copy.getMaxStackSize());
            return copy;
        }
        return null;
    }

    public static ItemStack findItemStack(String modId, String name) {
        ItemStack stack = stackRegistry.get(modId, name);
        if (stack == null) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modId, name));
            if (item != null) {
                stack = new ItemStack(item, 0, 0);
            }
        }
        if (stack == null) {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(modId, name));
            if (block != null && block != Blocks.AIR) {
                stack = new ItemStack(block, 0, Short.MAX_VALUE);
            }
        }
        return stack;
    }

}
