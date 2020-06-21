package micdoodle8.mods.galacticraft.core.client;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;

import java.awt.image.BufferedImage;

public class DynamicTextureProper extends DynamicTexture
{
    private boolean updateFlag = false;
    private final int width;    //We could transform these in the base class to protected
    private final int height;    //but whatever.

    public DynamicTextureProper(NativeImage img)
    {
        this(img.getWidth(), img.getHeight(), false);
        this.update(img);
    }

    public DynamicTextureProper(int width, int height, boolean clearIn)
    {
        super(width, height, clearIn);
        this.width = width;
        this.height = height;
    }

    public void update(NativeImage img)
    {
        this.getTextureData().uploadTextureSub(0, 0, 0, false);
        this.updateFlag = true;
    }

    @Override
    public int getGlTextureId()
    {
        if (this.updateFlag)
        {
            this.updateFlag = false;
            this.updateDynamicTexture();
        }
        return super.getGlTextureId();
    }
}
