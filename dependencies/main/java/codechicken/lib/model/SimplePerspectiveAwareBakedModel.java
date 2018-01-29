package codechicken.lib.model;

import codechicken.lib.render.CCModelState;
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
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

/**
 * Created by covers1624 on 7/25/2016.
 * A simple IPerspectiveAwareModel see SimplePerspectiveAwareLayerModelBakery for baking to this.
 * <p/>
 * Basically a wrapper for baked quads and IPerspectiveAwareModel.
 */
@Deprecated
public class SimplePerspectiveAwareBakedModel implements IPerspectiveAwareModel {

    private final ImmutableMap<TransformType, TRSRTransformation> transforms;
    private final ImmutableList<BakedQuad> quads;
    private final TextureAtlasSprite particle;

    public SimplePerspectiveAwareBakedModel(List<BakedQuad> quads, CCModelState modelState) {
        this(quads, null, modelState.getTransforms());
    }

    public SimplePerspectiveAwareBakedModel(List<BakedQuad> quads, ImmutableMap<TransformType, TRSRTransformation> transforms) {
        this(quads, null, transforms);
    }

    public SimplePerspectiveAwareBakedModel(List<BakedQuad> quads, TextureAtlasSprite particle, ImmutableMap<TransformType, TRSRTransformation> transforms) {
        this.transforms = transforms;
        this.quads = ImmutableList.copyOf(quads);
        this.particle = particle;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (side == null) {
            return quads;
        }
        return ImmutableList.of();
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
        return MapWrapper.handlePerspective(this, transforms, cameraTransformType);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return particle;
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
