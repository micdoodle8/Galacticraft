//package micdoodle8.mods.galacticraft.core.client.model;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//import java.util.function.Predicate;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.model.IUnbakedModel;
//import net.minecraft.resources.IReloadableResourceManager;
//import net.minecraft.resources.IResource;
//import net.minecraft.resources.IResourceManager;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.client.model.ICustomModelLoader;
//import net.minecraftforge.client.model.IModel;
//import net.minecraftforge.client.model.ModelLoaderRegistry;
//import net.minecraftforge.client.model.obj.OBJModel;
//import com.google.common.collect.ImmutableMap;
//import net.minecraftforge.resource.IResourceType;
//import org.apache.commons.io.IOUtils;
//
///*
// * Loader for OBJ models.
// * To enable your mod call instance.addDomain(modid).
// * If you need more control over accepted resources - extend the class, and register a new instance with ModelLoaderRegistry.
// */
//public class OBJLoaderGC implements ICustomModelLoader
//{
//    public static final OBJLoaderGC instance = new OBJLoaderGC();
//    private IResourceManager manager;
//    private final Set<String> enabledDomains = new HashSet<>();
//    private final Map<ResourceLocation, OBJModel> cache = new HashMap<>();
//
//    static
//    {
//        ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(instance);
//    }
//
//    public void addDomain(String domain)
//    {
//        enabledDomains.add(domain.toLowerCase());
//    }
//
//    @Override
//    public void onResourceManagerReload(IResourceManager resourceManager)
//    {
//        this.manager = resourceManager;
//        cache.clear();
//    }
//
//    @Override
//    public boolean accepts(ResourceLocation modelLocation)
//    {
//        return enabledDomains.contains(modelLocation.getNamespace()) && modelLocation.getPath().endsWith(".obj");
//    }
//
//    @Override
//    public IUnbakedModel loadModel(ResourceLocation file) throws IOException
//    {
//        OBJModel model = null;
//        if (cache.containsKey(file))
//        {
//            model = cache.get(file);
//        }
//        else
//        {
//            IResource resource = null;
//            try
//            {
//                try
//                {
//                    resource = manager.getResource(file);
//                }
//                catch (FileNotFoundException e)
//                {
//                    if (file.getPath().startsWith("models/block/"))
//                    {
//                        resource = manager.getResource(new ResourceLocation(file.getNamespace(), "models/item/" + file.getPath().substring("models/block/".length())));
//                    }
//                    else if (file.getPath().startsWith("models/item/"))
//                    {
//                        resource = manager.getResource(new ResourceLocation(file.getNamespace(), "models/block/" + file.getPath().substring("models/item/".length())));
//                    }
//                    else
//                    {
//                        throw e;
//                    }
//                }
//                OBJModel.Parser parser = new OBJModel.Parser(resource, manager);
//                try
//                {
//                    model = parser.parse();
//                }
//                finally
//                {
//                    cache.put(file, model);
//                }
//            }
//            finally
//            {
//                IOUtils.closeQuietly(resource);
//            }
//        }
//        if (model == null)
//        {
//            return ModelLoaderRegistry.getMissingModel();
//        }
//        return model;
//    }
//}
