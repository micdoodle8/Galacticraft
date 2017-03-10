package codechicken.lib.model.blockbakery.loader;

import codechicken.lib.model.blockbakery.CCBakeryModel;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.Collection;

/**
 * Basically a wrapper to allow the use of the BlockBakery from a json file.
 * Created by covers1624 on 13/02/2017.
 */
public class BakeryModel implements IModel {

    public static final BakeryModel INSTANCE = new BakeryModel();

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return ImmutableList.of();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return ImmutableList.of();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return new CCBakeryModel("");
    }

    @Override
    public IModelState getDefaultState() {
        //This doesn't matter as we are a wrapper.
        return TRSRTransformation.identity();
    }
}
