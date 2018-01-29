package codechicken.lib.render.item.map;

import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.MapData;

import javax.annotation.Nullable;

/**
 * Created by covers1624 on 15/02/2017.
 * Used to handle the rendering of map data for your custom maps.
 */
public interface IMapRenderer {

    /**
     * If the IMapRenderer should handle rendering the data from this map.
     *
     * @param stack   The ItemStack to render map data from.
     * @param data    The Map data contained inside this Map.
     * @param inFrame If the ItemStack is inside an ItemFrame.
     * @return Should this IMapRenderer handle with this context.
     */
    boolean shouldHandle(ItemStack stack, @Nullable MapData data, boolean inFrame);

    /**
     * If this is called you are expected to do something.
     * This is ONLY called if shouldHandle returns true.
     *
     * @param stack   The ItemStack to render the map data from.
     * @param data    The MapData contained in the ItemStack.
     * @param inFrame If the ItemStack is inside an ItemFrame.
     */
    void renderMap(ItemStack stack, @Nullable MapData data, boolean inFrame);

}
