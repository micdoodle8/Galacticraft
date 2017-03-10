package codechicken.lib.model;

import codechicken.lib.model.loader.CCBakedModelLoader;
import codechicken.lib.texture.TextureUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by covers1624 on 7/31/2016.
 */
@Deprecated
public class CCOverrideBakedBlockModel implements IBakedModel {

    private final String particle;
    private final boolean isAmbientOcclusion;
    private final ItemOverrideList overrideList;

    public CCOverrideBakedBlockModel(String particle) {
        this(particle, ItemOverrideList.NONE);
    }

    public CCOverrideBakedBlockModel(String particle, ItemOverrideList overrideList) {
        this(particle, false, overrideList);
    }

    public CCOverrideBakedBlockModel(String particle, boolean isAmbientOcclusion, ItemOverrideList overrideList) {
        this.particle = particle;
        this.isAmbientOcclusion = isAmbientOcclusion;
        this.overrideList = overrideList;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        LinkedList<BakedQuad> quads = new LinkedList<BakedQuad>();
        if (state == null) {
            return quads;
        }
        IBakedModel correctModel = CCBakedModelLoader.getModel(state);
        if (correctModel != null) {
            quads.addAll(correctModel.getQuads(state, side, rand));
        }
        return quads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return isAmbientOcclusion;
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
        return TextureUtils.getTexture(particle);
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return overrideList;
    }
}
