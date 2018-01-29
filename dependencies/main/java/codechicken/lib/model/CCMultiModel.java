package codechicken.lib.model;

import codechicken.lib.model.bakedmodels.PerspectiveAwareMultiModel;
import codechicken.lib.texture.TextureUtils;
import com.google.common.base.Function;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by covers1624 on 16/12/2016.
 */
public class CCMultiModel implements IModel {

    private final IModel base;
    private final BakedModelProperties baseProperties;
    private final List<IModel> subModels;

    public CCMultiModel(IModel base, BakedModelProperties baseProperties, List<IModel> subModels) {
        this.base = base;
        this.baseProperties = baseProperties;
        this.subModels = subModels;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        List<ResourceLocation> deps = new ArrayList<ResourceLocation>();
        if (base != null) {
            deps.addAll(base.getDependencies());
        }
        for (IModel subModel : subModels) {
            deps.addAll(subModel.getDependencies());
        }
        return deps;
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        List<ResourceLocation> deps = new ArrayList<ResourceLocation>();
        if (base != null) {
            deps.addAll(base.getTextures());
        }
        for (IModel subModel : subModels) {
            deps.addAll(subModel.getTextures());
        }
        return deps;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        IBakedModel baseBakedModel = null;
        if (base != null) {
            baseBakedModel = base.bake(state, format, bakedTextureGetter);
        }
        List<IBakedModel> subBakedModels = new ArrayList<IBakedModel>();
        TextureAtlasSprite particle = null;
        if (baseBakedModel != null) {
            particle = baseBakedModel.getParticleTexture();
        }
        for (IModel subModel : subModels) {
            TextureAtlasSprite particleCandidate = null;
            IBakedModel bakedSubModel = subModel.bake(subModel.getDefaultState(), format, bakedTextureGetter);
            subBakedModels.add(bakedSubModel);
            if (particle == null || particle == TextureUtils.getMissingSprite()) {
                if (particleCandidate == null || particleCandidate == TextureUtils.getMissingSprite()) {
                    particleCandidate = bakedSubModel.getParticleTexture();
                }
                if (particleCandidate != null || particleCandidate != TextureUtils.getMissingSprite()) {
                    particle = particleCandidate;
                }
            }
        }
        if (particle == null) {
            particle = TextureUtils.getMissingSprite();
        }

        return new PerspectiveAwareMultiModel(baseBakedModel, subBakedModels, state, new BakedModelProperties(baseProperties, particle));
    }

    @Override
    public IModelState getDefaultState() {
        return base.getDefaultState();
    }
}
