package codechicken.lib.world;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class TileChunkLoadHook {

    private static boolean init;

    public static void init() {
        if (init) {
            return;
        }
        init = true;

        MinecraftForge.EVENT_BUS.register(new TileChunkLoadHook());
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        List<TileEntity> list = new ArrayList<TileEntity>(event.getChunk().getTileEntityMap().values());
        for (TileEntity t : list) {
            if (t instanceof IChunkLoadTile) {
                ((IChunkLoadTile) t).onChunkLoad();
            }
        }
    }
}
