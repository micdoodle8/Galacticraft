package codechicken.lib.model.blockstate;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.ModelProcessingHelper;
import net.minecraftforge.common.model.IModelState;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by covers1624 on 19/11/2016.
 * Here because ForgeVariant is yet again private..
 * Credits to fry.
 */
public class CCFinalVariant extends Variant {

    private final IModelState state;
    private final boolean smooth;
    private final boolean gui3d;
    private final ImmutableMap<String, String> customData;
    private final ImmutableMap<String, String> textures;

    public CCFinalVariant(ResourceLocation model, IModelState state, boolean uvLock, boolean smooth, boolean gui3d, int weight, Map<String, String> textures, String textureDomain, Map<String, String> customData) {
        super(model == null ? new ResourceLocation("builtin/missing") : model, state instanceof ModelRotation ? ((ModelRotation) state) : ModelRotation.X0_Y0, uvLock, weight);
        this.state = state;
        this.smooth = smooth;
        this.gui3d = gui3d;

        Map<String, String> newTextures = new HashMap<String, String>();
        for (Entry<String, String> entry : textures.entrySet()) {
            String prefixedTexture = entry.getValue();
            if (!entry.getValue().contains(":")) {
                prefixedTexture = textureDomain + ":" + prefixedTexture;
            }
            newTextures.put(entry.getKey(), prefixedTexture);
        }
        this.textures = ImmutableMap.copyOf(newTextures);
        this.customData = ImmutableMap.copyOf(customData);
    }

    public static IModel runModelHooks(IModel base, boolean smooth, boolean gui3d, boolean uvlock, ImmutableMap<String, String> textureMap, ImmutableMap<String, String> customData) {
        base = ModelProcessingHelper.customData(base, customData);
        base = ModelProcessingHelper.retexture(base, textureMap);
        base = ModelProcessingHelper.smoothLighting(base, smooth);
        base = ModelProcessingHelper.gui3d(base, gui3d);
        base = ModelProcessingHelper.uvlock(base, uvlock);
        return base;
    }

    @Override
    public IModel process(IModel base) {

        boolean hasBase = base != ModelLoaderRegistry.getMissingModel();

        if (hasBase) {
            base = runModelHooks(base, smooth, gui3d, isUvLock(), textures, customData);
        }

        return base;
    }

    @Override
    public IModelState getState() {
        return state;
    }
}
