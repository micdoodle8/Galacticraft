package codechicken.lib.model;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.IModelState;

import java.util.Collection;

/**
 * Created by covers1624 on 17/12/2016.
 * TODO Decide on a standard place for CCL's IModels.
 */
public class StateOverrideIModel implements IModel, IRetexturableModel, IModelCustomData, IModelSimpleProperties, IModelUVLock {

    private IModel wrapped;
    private final IModelState wrappedState;

    public StateOverrideIModel(IModel wrapped, IModelState wrappedState) {
        this.wrapped = wrapped;
        this.wrappedState = wrappedState;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return wrapped.getDependencies();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return wrapped.getTextures();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return wrapped.bake(state, format, bakedTextureGetter);
    }

    @Override
    public IModelState getDefaultState() {
        return wrappedState;
    }

    @Override
    public IModel retexture(ImmutableMap<String, String> textures) {
        wrapped = ModelProcessingHelper.retexture(wrapped, textures);
        return this;
    }

    @Override
    public IModel uvlock(boolean value) {
        wrapped = ModelProcessingHelper.uvlock(wrapped, value);
        return this;
    }

    @Override
    public IModel smoothLighting(boolean value) {
        wrapped = ModelProcessingHelper.smoothLighting(wrapped, value);
        return this;
    }

    @Override
    public IModel gui3d(boolean value) {
        wrapped = ModelProcessingHelper.gui3d(wrapped, value);
        return this;
    }

    @Override
    public IModel process(ImmutableMap<String, String> customData) {
        wrapped = ModelProcessingHelper.customData(wrapped, customData);
        return this;
    }
}
