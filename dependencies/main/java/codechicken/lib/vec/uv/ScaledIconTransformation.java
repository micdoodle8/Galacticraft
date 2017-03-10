package codechicken.lib.vec.uv;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class ScaledIconTransformation extends IconTransformation {

    double su = 0.0F;
    double sv = 0.0F;

    public ScaledIconTransformation(TextureAtlasSprite icon) {

        super(icon);
    }

    public ScaledIconTransformation(TextureAtlasSprite icon, double scaleu, double scalev) {

        super(icon);

        su = scaleu;
        sv = scalev;
    }

    @Override
    public void apply(UV texcoord) {

        texcoord.u = icon.getInterpolatedU(texcoord.u % 2 * 16) + su * (icon.getMaxU() - icon.getMinU());
        texcoord.v = icon.getInterpolatedV(texcoord.v % 2 * 16) + sv * (icon.getMaxV() - icon.getMinV());
    }
}
