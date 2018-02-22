package micdoodle8.mods.galacticraft.core.client.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import com.google.common.collect.ImmutableMap;

/*
 * Loader for OBJ models.
 * To enable your mod call instance.addDomain(modid).
 * If you need more control over accepted resources - extend the class, and register a new instance with ModelLoaderRegistry.
 */
public class OBJLoaderGC implements ICustomModelLoader {
    public static final OBJLoaderGC instance = new OBJLoaderGC();
    private IResourceManager manager;
    private final Set<String> enabledDomains = new HashSet<>();
    private final Map<ResourceLocation, IModel> cache = new HashMap<>();

    static
    {
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(instance);
    }
    
    public void addDomain(String domain)
    {
        enabledDomains.add(domain.toLowerCase());
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        this.manager = resourceManager;
        cache.clear();
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation)
    {
        return enabledDomains.contains(modelLocation.getResourceDomain()) && modelLocation.getResourcePath().endsWith(".obj");
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws IOException
    {
        IModel model = null;
        if (cache.containsKey(modelLocation))
        {
            model = cache.get(modelLocation);
        }
        else
        {
            try
            {
                String prefix = modelLocation.getResourcePath().contains("models/") ? "" : "models/";
                ResourceLocation file = new ResourceLocation(modelLocation.getResourceDomain(), prefix + modelLocation.getResourcePath());
                IResource resource = manager.getResource(file);
                if (resource != null)
                {
                    OBJModel.Parser parser = new OBJModel.Parser(resource, manager);
                    try
                    {
                        model = parser.parse().process(ImmutableMap.of("flip-v", "true"));
                    }
                    finally
                    {
                        resource.getInputStream().close();
                        cache.put(modelLocation, model);
                    }
                }
            }
            catch (IOException e)
            {
                throw e;
            }
        }
        if (model == null) return ModelLoaderRegistry.getMissingModel();
        return model;
    }
}
