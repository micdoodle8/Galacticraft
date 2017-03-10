package codechicken.lib.model.loader;

import codechicken.lib.model.loader.IBakedModelLoader.IModKeyProvider;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.texture.TextureUtils.IIconRegister;
import codechicken.lib.thread.RestartableTask;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import org.apache.logging.log4j.Level;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by covers1624 on 7/25/2016.
 * This is a flexible IBakedModel loader, It runs off IBakedModelLoaders and IModKeyProviders.
 * When getModel(ItemStack) is called it will first try and obtain a IModKeyProvider, If no KeyProvider is found then it returns a null model.
 * It then calls IModKeyProvider.createKey(ItemStack) if this returns null a null model is returned.
 * It will then check the cache for a key -> IBakedModel reference, if the cache contains a model it is returned otherwise it bakes a model using IBakedModelLoader.
 * <p/>
 * To use this call registerLoader(IBakedModelLoader) from PRE INIT!
 * //TODO This system needs to be scrapped for the new BlockBakery system that was newly implemented.
 */
@Deprecated
public class CCBakedModelLoader implements IIconRegister, IResourceManagerReloadListener {

    public static final CCBakedModelLoader INSTANCE = new CCBakedModelLoader();

    //TODO use cache factories.
    private static final Cache<String, IBakedModel> modelCache = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.MINUTES).build();
    private static final Map<String, IModKeyProvider> modelBakeQue = new HashMap<String, IModKeyProvider>();

    private static final ModelBakeTask bakingTask = new ModelBakeTask();

    private static final Map<String, IModKeyProvider> modKeyProviders = new HashMap<String, IModKeyProvider>();
    private static final Map<IModKeyProvider, IBakedModelLoader> modelLoaders = new HashMap<IModKeyProvider, IBakedModelLoader>();

    static {
        TextureUtils.addIconRegister(INSTANCE);
        TextureUtils.registerReloadListener(INSTANCE);
    }

    /**
     * Registers a IModKeyProvider and IBakedModelLoader.
     *
     * @param loader Registers a loader.
     */
    public static void registerLoader(IBakedModelLoader loader) {
        if (Loader.instance().hasReachedState(LoaderState.INITIALIZATION)) {
            throw new RuntimeException("Unable to register IBakedModelLoader after Pre Initialization! Please register as the first thing you do in Pre Init!");
        }
        if (modelLoaders.containsValue(loader)) {
            throw new RuntimeException("Unable to register IBakedModelLoader as it has already been registered!");
        }

        IModKeyProvider provider = loader.createKeyProvider();
        FMLLog.log("CodeChicken Lib", Level.INFO, "Registered loader for mod: %s", provider.getMod());
        modelLoaders.put(provider, loader);
        modKeyProviders.put(provider.getMod(), provider);
    }

    @Override
    public void registerIcons(TextureMap textureMap) {
        for (ResourceLocation location : getTextures()) {
            textureMap.registerSprite(location);
        }
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        modelCache.invalidateAll();
    }

    public static void clearCache() {
        synchronized (modelCache) {
            modelCache.invalidateAll();
        }
    }

    private static Collection<ResourceLocation> getTextures() {
        ImmutableList.Builder<ResourceLocation> builder = ImmutableList.builder();
        for (IBakedModelLoader loader : modelLoaders.values()) {
            loader.addTextures(builder);
        }
        return builder.build();
    }

    public static synchronized IBakedModel getModel(IBlockState state) {
        if (state.getBlock() == null || state.getBlock().getRegistryName() == null) {
            return null;
        }
        ResourceLocation location = state.getBlock().getRegistryName();
        final IModKeyProvider provider = modKeyProviders.get(location.getResourceDomain());
        if (provider == null) {
            FMLLog.bigWarning("Unable to find IModKeyProvider for domain %s!", location.getResourceDomain());
            return null;
        }
        final String key = provider.createKey(state);
        if (key == null) {
            return null;
        }
        String mapKey = location.toString() + "|" + key;
        synchronized (modelCache) {
            try {
                return modelCache.get(mapKey, new Callable<IBakedModel>() {
                    @Override
                    public IBakedModel call() throws Exception {
                        return modelLoaders.get(provider).bakeModel(key);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Call this from ItemOverrideList.handleItemState()
     *
     * @param stack The ItemStack to attempt to obtain a model for.
     * @return The baked model if it can be found/generated, else null.
     */
    public static synchronized IBakedModel getModel(ItemStack stack) {
        if (stack.getItem() == null || stack.getItem().getRegistryName() == null) {
            return null;
        }
        ResourceLocation location = stack.getItem().getRegistryName();
        IModKeyProvider provider = modKeyProviders.get(location.getResourceDomain());
        if (provider == null) {
            FMLLog.bigWarning("Unable to find IModKeyProvider for domain %s!", location.getResourceDomain());
            return null;
        }
        String key = provider.createKey(stack);
        if (key == null) {
            return null;
        }
        String mapKey = location.toString() + "|" + key;
        synchronized (modelCache) {
            if (modelCache.getIfPresent(mapKey) == null) {
                if (modelBakeQue.containsKey(mapKey)) {
                    return null;
                } else {
                    bakingTask.stop();
                    synchronized (modelBakeQue) {
                        modelBakeQue.put(mapKey, provider);
                    }
                    bakingTask.restart();
                }
            }
            return modelCache.getIfPresent(mapKey);
        }
    }

    public static class ModelBakeTask extends RestartableTask {

        public ModelBakeTask() {
            super("CodeChicken Lib Dynamic model baking");
        }

        @Override
        public void execute() {
            Map<String, IModKeyProvider> localQue;
            synchronized (modelBakeQue) {
                localQue = new HashMap<String, IModKeyProvider>(modelBakeQue);
            }
            Iterator<Entry<String, IModKeyProvider>> queIterator = localQue.entrySet().iterator();
            while (queIterator.hasNext()) {
                if (interrupted()) {
                    return;
                }
                Entry<String, IModKeyProvider> entry = queIterator.next();
                try {
                    IBakedModelLoader loader = modelLoaders.get(entry.getValue());
                    String key = stripMapHeader(entry.getKey());
                    IBakedModel model = loader.bakeModel(key);
                    queIterator.remove();

                    if (model != null) {
                        synchronized (modelCache) {
                            modelCache.put(entry.getKey(), model);
                        }
                    }
                } catch (Exception e) {
                    FMLLog.log("CodeChickenLib Dyn Model baking", Level.FATAL, e, "A fatal exception has occurred whilst baking a model with key: %s", entry.getKey());
                } finally {
                    synchronized (modelBakeQue) {
                        modelBakeQue.remove(entry.getKey());
                    }
                }
            }
        }

        private static String stripMapHeader(String mapKey) {
            int firstPipe = mapKey.indexOf('|');
            return mapKey.substring(firstPipe + 1);
        }
    }
}
