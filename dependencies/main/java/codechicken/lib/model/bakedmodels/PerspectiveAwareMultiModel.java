package codechicken.lib.model.bakedmodels;

import codechicken.lib.model.BakedModelProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.IModelState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by covers1624 on 16/12/2016.
 */
public class PerspectiveAwareMultiModel implements IPerspectiveAwareModel {

    private final IBakedModel baseModel;
    private final List<IBakedModel> subModels;
    private final IModelState baseState;
    private final BakedModelProperties properties;

    public PerspectiveAwareMultiModel(IBakedModel baseModel, List<IBakedModel> subModels, IModelState baseState, BakedModelProperties properties) {
        this.baseModel = baseModel;
        this.subModels = subModels;
        this.baseState = baseState;
        this.properties = properties;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> quads = new LinkedList<BakedQuad>();
        if (baseModel != null) {
            quads.addAll(baseModel.getQuads(state, side, rand));
        }
        for (IBakedModel subModel : subModels) {
            quads.addAll(subModel.getQuads(state, side, rand));
        }
        return quads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return properties.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return properties.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return properties.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return properties.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
        return MapWrapper.handlePerspective(this, baseState, cameraTransformType);
    }
}
