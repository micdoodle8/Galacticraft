package codechicken.lib.model.blockbakery.loader;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

/**
 * Basically a wrapper to allow the use of the BlockBakery from a json file.
 * Created by covers1624 on 13/02/2017.
 */
public class CCBakeryModelLoader implements ICustomModelLoader {

    public static final CCBakeryModelLoader INSTANCE = new CCBakeryModelLoader();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        String path = modelLocation.getResourcePath();
        return modelLocation.getResourceDomain().equals("ccl") && (path.equals("bakery") || path.equals("models/block/bakery") || path.equals("models/item/bakery"));
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return BakeryModel.INSTANCE;
    }
}
