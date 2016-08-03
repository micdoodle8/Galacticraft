package codechicken.lib.render.uv;

import codechicken.lib.vec.IrreversibleTransformationException;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class MultiIconTransformation extends UVTransformation {
    public TextureAtlasSprite[] icons;

    public MultiIconTransformation(TextureAtlasSprite... icons) {
        this.icons = icons;
    }

    @Override
    public void apply(UV uv) {
        TextureAtlasSprite icon = icons[uv.tex % icons.length];
        uv.u = icon.getInterpolatedU(uv.u * 16);
        uv.v = icon.getInterpolatedV(uv.v * 16);
    }

    @Override
    public UVTransformation inverse() {
        throw new IrreversibleTransformationException(this);
    }
}
