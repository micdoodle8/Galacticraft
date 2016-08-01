package codechicken.lib.render;

import codechicken.lib.render.SpriteSheetManager.SpriteSheet;
import codechicken.lib.render.TextureUtils.IIconRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class TextureSpecial extends TextureAtlasSprite implements IIconRegister {
    //sprite sheet fields
    private int spriteIndex;
    private SpriteSheet spriteSheet;

    //textureFX fields
    private TextureFX textureFX;
    private int mipmapLevels;
    private int rawWidth;
    private int rawHeight;

    private int blankSize = -1;
    private ArrayList<TextureDataHolder> baseTextures;

    private boolean selfRegister;
    public int atlasIndex;

    protected TextureSpecial(String par1) {
        super(par1);
    }

    public TextureSpecial addTexture(TextureDataHolder t) {
        if (baseTextures == null) {
            baseTextures = new ArrayList<TextureDataHolder>();
        }
        baseTextures.add(t);
        return this;
    }

    public TextureSpecial baseFromSheet(SpriteSheet spriteSheet, int spriteIndex) {
        this.spriteSheet = spriteSheet;
        this.spriteIndex = spriteIndex;
        return this;
    }

    public TextureSpecial addTextureFX(TextureFX fx) {
        textureFX = fx;
        return this;
    }

    @Override
    public void initSprite(int sheetWidth, int sheetHeight, int originX, int originY, boolean rotated) {
        super.initSprite(sheetWidth, sheetHeight, originX, originY, rotated);
        if (textureFX != null) {
            textureFX.onTextureDimensionsUpdate(rawWidth, rawHeight);
        }
    }

    @Override
    public void updateAnimation() {
        if (textureFX != null) {
            textureFX.update();
            if (textureFX.changed()) {
                int[][] mipmaps = new int[mipmapLevels + 1][];
                mipmaps[0] = textureFX.imageData;
                //TODO mipmaps = prepareAnisotropicFiltering(mipmaps);
                mipmaps = TextureUtil.generateMipmapData(mipmapLevels, width, mipmaps);
                TextureUtil.uploadTextureMipmap(mipmaps, width, height, originX, originY, false, false);
            }
        }
    }

    /**
     * Copy paste mojang code because it's private, and CCL can't have access transformers or reflection
     */
    /*public int[][] prepareAnisotropicFiltering(int[][] mipmaps) {
        if (Minecraft.getMinecraft().gameSettings.anisotropicFiltering <= 1)
        {
            return mipmaps;
        }
        else
        {
            int[][] aint1 = new int[mipmaps.length][];

            for (int k = 0; k < mipmaps.length; ++k)
            {
                int[] aint2 = mipmaps[k];

                if (aint2 != null)
                {
                    int[] aint3 = new int[(rawWidth + 16 >> k) * (rawHeight + 16 >> k)];
                    System.arraycopy(aint2, 0, aint3, 0, aint2.length);
                    aint1[k] = TextureUtil.prepareAnisotropicData(aint3, rawWidth >> k, rawHeight >> k, 8 >> k);
                }
            }

            return aint1;
        }
    }*/
    @Override
    public void loadSprite(BufferedImage[] images, AnimationMetadataSection animationMeta) {
        rawWidth = images[0].getWidth();
        rawHeight = images[0].getHeight();
        try {
            super.loadSprite(images, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void generateMipmaps(int p_147963_1_) {
        super.generateMipmaps(p_147963_1_);
        mipmapLevels = p_147963_1_;
    }

    @Override
    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }

    public void addFrame(int[] data, int width, int height) {
        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        BufferedImage[] images = new BufferedImage[settings.mipmapLevels + 1];
        images[0] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        images[0].setRGB(0, 0, width, height, data, 0, width);

        loadSprite(images, null);
    }

    @Override
    public boolean load(IResourceManager manager, ResourceLocation location) {
        if (baseTextures != null) {
            for (TextureDataHolder tex : baseTextures) {
                addFrame(tex.data, tex.width, tex.height);
            }
        } else if (spriteSheet != null) {
            TextureDataHolder tex = spriteSheet.createSprite(spriteIndex);
            addFrame(tex.data, tex.width, tex.height);
        } else if (blankSize > 0) {
            addFrame(new int[blankSize * blankSize], blankSize, blankSize);
        }

        if (framesTextureData.isEmpty()) {
            throw new RuntimeException("No base frame for texture: " + getIconName());
        }

        return false;
    }

    @Override
    public boolean hasAnimationMetadata() {
        return textureFX != null || super.hasAnimationMetadata();
    }

    @Override
    public int getFrameCount() {
        if (textureFX != null) {
            return 1;
        }

        return super.getFrameCount();
    }

    public TextureSpecial blank(int size) {
        blankSize = size;
        return this;
    }

    public TextureSpecial selfRegister() {
        selfRegister = true;
        TextureUtils.addIconRegister(this);
        return this;
    }

    @Override
    public void registerIcons(TextureMap textureMap) {
        textureMap.setTextureEntry(getIconName(), this);
    }
}
