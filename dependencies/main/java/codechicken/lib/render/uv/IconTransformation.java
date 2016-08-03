package codechicken.lib.render.uv;

import codechicken.lib.vec.IrreversibleTransformationException;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class IconTransformation extends UVTransformation {
    public TextureAtlasSprite icon;

    public IconTransformation(TextureAtlasSprite icon) {
        this.icon = icon;
    }

    @Override
    public void apply(UV uv) {
        uv.u = icon.getInterpolatedU(uv.u * 16);
        uv.v = icon.getInterpolatedV(uv.v * 16);
    }

    @Override
    public UVTransformation inverse() {
        throw new IrreversibleTransformationException(this);
    }
}
