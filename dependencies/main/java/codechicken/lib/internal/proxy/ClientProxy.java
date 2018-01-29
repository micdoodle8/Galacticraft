package codechicken.lib.internal.proxy;

import codechicken.lib.internal.command.client.DumpModelLocationsCommand;
import codechicken.lib.internal.command.client.NukeCCModelCacheCommand;
import codechicken.lib.model.blockbakery.BlockBakery;
import codechicken.lib.model.blockbakery.loader.CCBakeryModelLoader;
import codechicken.lib.model.cube.CCCubeLoader;
import codechicken.lib.render.CCRenderEventHandler;
import codechicken.lib.render.block.CCExtendedBlockRendererDispatcher;
import codechicken.lib.render.item.CCRenderItem;
import codechicken.lib.render.item.map.MapRenderRegistry;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by covers1624 on 23/11/2016.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        BlockBakery.init();
        CCRenderEventHandler.init();
        MinecraftForge.EVENT_BUS.register(new MapRenderRegistry());
        ModelLoaderRegistry.registerLoader(CCCubeLoader.INSTANCE);
        ModelLoaderRegistry.registerLoader(CCBakeryModelLoader.INSTANCE);
        ClientCommandHandler.instance.registerCommand(new DumpModelLocationsCommand());
        ClientCommandHandler.instance.registerCommand(new NukeCCModelCacheCommand());
    }

    @Override
    public void init() {
        CCExtendedBlockRendererDispatcher.init();
        CCRenderItem.init();
    }
}
