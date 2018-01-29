package codechicken.lib.model.bakedmodels;

import codechicken.lib.model.BakedModelProperties;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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
import java.util.List;
import java.util.Map;

/**
 * Created by covers1624 on 25/11/2016.
 */
public class PerspectiveAwareBakedModel implements IPerspectiveAwareModel {

    private final ImmutableMap<EnumFacing, List<BakedQuad>> faceQuads;
    private final ImmutableList<BakedQuad> generalQuads;
    private final IModelState state;
    private final BakedModelProperties properties;

    public PerspectiveAwareBakedModel(Map<EnumFacing, List<BakedQuad>> faceQuads, IModelState state, BakedModelProperties properties) {
        this(faceQuads, ImmutableList.<BakedQuad>of(), state, properties);
    }

    public PerspectiveAwareBakedModel(List<BakedQuad> generalQuads, IModelState state, BakedModelProperties properties) {
        this(ImmutableMap.<EnumFacing, List<BakedQuad>>of(), generalQuads, state, properties);
    }

    public PerspectiveAwareBakedModel(Map<EnumFacing, List<BakedQuad>> faceQuads, List<BakedQuad> generalQuads, IModelState state, BakedModelProperties properties) {
        this.faceQuads = ImmutableMap.copyOf(faceQuads);
        this.generalQuads = ImmutableList.copyOf(generalQuads);
        this.state = state;
        this.properties = properties.copy();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (side == null) {
            return generalQuads;
        } else {
            if (faceQuads.containsKey(side)) {
                return faceQuads.get(side);
            }
        }
        return ImmutableList.of();
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
        return MapWrapper.handlePerspective(this, state, cameraTransformType);
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
}
