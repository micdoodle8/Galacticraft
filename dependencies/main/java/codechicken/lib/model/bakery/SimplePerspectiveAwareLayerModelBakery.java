package codechicken.lib.model.bakery;

import codechicken.lib.model.BakedModelProperties;
import codechicken.lib.model.bakedmodels.PerspectiveAwareBakedModel;
import codechicken.lib.texture.TextureUtils;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.common.model.IModelState;

import java.util.Arrays;

/**
 * Created by covers1624 on 7/25/2016.
 */
//TODO Find better name and integrate with IModel / Jsons.
public class SimplePerspectiveAwareLayerModelBakery {

    private final ResourceLocation baseTexture;
    private final ResourceLocation[] layers;

    public SimplePerspectiveAwareLayerModelBakery(ResourceLocation baseTexture, ResourceLocation... layers) {
        this.baseTexture = baseTexture;
        this.layers = layers;
    }

    public IBakedModel bake(IModelState state) {

        ImmutableList.Builder<BakedQuad> quadBuilder = ImmutableList.builder();
        ImmutableList<ResourceLocation> textureLayers = ImmutableList.<ResourceLocation>builder().add(baseTexture).addAll(Arrays.asList(layers)).build();

        IBakedModel layerModel = new ItemLayerModel(textureLayers).bake(state, DefaultVertexFormats.ITEM, TextureUtils.bakedTextureGetter);
        quadBuilder.addAll(layerModel.getQuads(null, null, 0));

        return new PerspectiveAwareBakedModel(quadBuilder.build(), state, BakedModelProperties.createFromModel(layerModel));
    }
}
