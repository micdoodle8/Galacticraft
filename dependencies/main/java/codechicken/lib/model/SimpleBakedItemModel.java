package codechicken.lib.model;

import codechicken.lib.util.TransformUtils;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ItemLayerModel;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by covers1624 on 7/18/2016.
 * TODO, Cleanup and move to bakedmodels package.
 */
public class SimpleBakedItemModel implements IPerspectiveAwareModel {

    private ImmutableList<BakedQuad> quads;
    private TextureAtlasSprite sprite;
    private ResourceLocation spriteName;

    public SimpleBakedItemModel(ResourceLocation sprite) {
        spriteName = sprite;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (state == null && side == null) {
            if (quads == null) {
                bake();
            }
            return quads;
        }
        return new ArrayList<BakedQuad>();
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
        return sprite;
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
        return MapWrapper.handlePerspective(this, TransformUtils.DEFAULT_ITEM.getTransforms(), cameraTransformType);
    }

    private void bake() {
        Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter = new Function<ResourceLocation, TextureAtlasSprite>() {
            @Override
            public TextureAtlasSprite apply(ResourceLocation input) {
                return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(input.toString());
            }
        };
        IBakedModel layerModel = new ItemLayerModel(ImmutableList.of(spriteName)).bake(TransformUtils.DEFAULT_ITEM, DefaultVertexFormats.ITEM, bakedTextureGetter);
        ImmutableList.Builder<BakedQuad> quadBuilder = ImmutableList.builder();
        quadBuilder.addAll(layerModel.getQuads(null, null, 0));
        this.quads = quadBuilder.build();
        this.sprite = bakedTextureGetter.apply(spriteName);
    }

}
