package codechicken.core.featurehack;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameData;

public class GameDataManipulator {
    public static void replaceItem(int id, Item item) {
        try {
            ResourceLocation name = (ResourceLocation) Item.itemRegistry.getNameForObject(Item.getItemById(id));
            Item.itemRegistry.registryObjects.put(name, item);
            Item.itemRegistry.underlyingIntegerMap.put(item, id);
            Block block = Block.getBlockById(id);
            if (block != Blocks.air) {
                GameData.getBlockItemMap().put(block, item);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
