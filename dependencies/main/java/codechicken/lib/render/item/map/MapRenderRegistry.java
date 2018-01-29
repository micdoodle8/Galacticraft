package codechicken.lib.render.item.map;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.IdentityHashMap;

/**
 * Created by covers1624 on 15/02/2017.
 * TODO PR for 1.11 to add this as an event.
 */
public class MapRenderRegistry {

    private static IdentityHashMap<Item, IMapRenderer> mapRenderers = new IdentityHashMap<Item, IMapRenderer>();

    public static boolean shouldHandle(ItemStack stack, boolean inFrame) {
        IMapRenderer mapRenderer = mapRenderers.get(stack.getItem());
        if (mapRenderer != null) {
            MapData data = Items.FILLED_MAP.getMapData(stack, Minecraft.getMinecraft().theWorld);
            return mapRenderer.shouldHandle(stack, data, inFrame);
        }
        return false;
    }

    public static void handleRender(ItemStack stack, boolean inFrame) {
        IMapRenderer mapRenderer = mapRenderers.get(stack.getItem());
        if (mapRenderer != null) {
            MapData data = Items.FILLED_MAP.getMapData(stack, Minecraft.getMinecraft().theWorld);
            mapRenderer.renderMap(stack, data, inFrame);
        }
    }

    public static void registerMapRenderer(Item item, IMapRenderer mapRenderer) {
        mapRenderers.put(item, mapRenderer);
    }

    @SubscribeEvent
    public void onItemFrameRender(RenderItemInFrameEvent event) {
        if (shouldHandle(event.getItem(), true)) {
            event.setCanceled(true);
            handleRender(event.getItem(), true);
        }
    }

}
