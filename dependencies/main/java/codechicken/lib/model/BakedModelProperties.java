package codechicken.lib.model;

import codechicken.lib.util.Copyable;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/**
 * Created by covers1624 on 19/11/2016.
 */
public class BakedModelProperties implements Copyable<BakedModelProperties> {

    private final boolean isAO;
    private final boolean isGui3D;
    private final boolean isBuiltInRenderer;
    private final TextureAtlasSprite particle;

    public BakedModelProperties(boolean isAO, boolean isGui3D) {
        this(isAO, isGui3D, false, null);
    }

    public BakedModelProperties(BakedModelProperties properties, TextureAtlasSprite sprite) {
        this(properties.isAmbientOcclusion(), properties.isGui3d(), properties.isBuiltInRenderer(), sprite);
    }

    public BakedModelProperties(boolean isAO, boolean isGui3D, TextureAtlasSprite sprite) {
        this(isAO, isGui3D, false, sprite);
    }

    public BakedModelProperties(boolean isAO, boolean isGui3D, boolean isBuiltInRenderer, TextureAtlasSprite particle) {
        this.isAO = isAO;
        this.isGui3D = isGui3D;
        this.isBuiltInRenderer = isBuiltInRenderer;
        this.particle = particle;
    }

    public static BakedModelProperties createFromModel(IBakedModel model) {
        return new BakedModelProperties(model.isAmbientOcclusion(), model.isGui3d(), model.isBuiltInRenderer(), model.getParticleTexture());
    }

    public boolean isAmbientOcclusion() {
        return isAO;
    }

    public boolean isGui3d() {
        return isGui3D;
    }

    public boolean isBuiltInRenderer() {
        return isBuiltInRenderer;
    }

    public TextureAtlasSprite getParticleTexture() {
        return particle;
    }

    @Override
    public BakedModelProperties copy() {
        return new BakedModelProperties(isAO, isGui3D, isBuiltInRenderer, particle);
    }
}
