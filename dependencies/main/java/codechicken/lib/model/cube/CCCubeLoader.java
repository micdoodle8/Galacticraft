package codechicken.lib.model.cube;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

/**
 * Created by covers1624 on 19/11/2016.
 */
public class CCCubeLoader implements ICustomModelLoader {

    public static final CCCubeLoader INSTANCE = new CCCubeLoader();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        String path = modelLocation.getResourcePath();
        return modelLocation.getResourceDomain().equals("ccl") && (path.equals("cube") || path.equals("models/block/cube") || path.equals("models/item/cube"));
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return CCModelCube.INSTANCE;
    }
}
