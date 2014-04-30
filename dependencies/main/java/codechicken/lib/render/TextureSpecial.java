package codechicken.lib.render;

import codechicken.lib.render.SpriteSheetManager.SpriteSheet;
import codechicken.lib.render.TextureUtils.IIconSelfRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class TextureSpecial extends TextureAtlasSprite implements IIconSelfRegister {
    //sprite sheet fields
    private int spriteIndex;
    private SpriteSheet spriteSheet;

    //textureFX fields
    private TextureFX textureFX;

    private int blankSize = -1;
    private ArrayList<TextureDataHolder> baseTextures;

    private boolean selfRegister;
    public int atlasIndex;

    protected TextureSpecial(String par1) {
        super(par1);
    }

    public TextureSpecial addTexture(TextureDataHolder t) {
        if (baseTextures == null)
            baseTextures = new ArrayList<TextureDataHolder>();
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
        if (textureFX != null)
            textureFX.onTextureDimensionsUpdate(width, height);
    }

    @Override
    public void updateAnimation() {
        if (textureFX != null) {
            textureFX.update();
            if (textureFX.changed())
                TextureUtil.uploadTextureMipmap(new int[][]{textureFX.imageData}, width, height, originX, originY, false, false);
        }
    }

    @Override
    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }

    public void addFrame(int[] data) {
        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        BufferedImage[] images = new BufferedImage[settings.mipmapLevels+1];
        images[0] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        images[0].setRGB(0, 0, width, height, data, 0, width);

        super.loadSprite(images, null, settings.anisotropicFiltering > 1);
    }

    @Override
    public boolean load(IResourceManager manager, ResourceLocation location) {
        if (baseTextures != null) {
            for (TextureDataHolder tex : baseTextures) {
                width = tex.width;
                height = tex.height;
                addFrame(tex.data);
            }
        }

        if (spriteSheet != null) {
            TextureDataHolder tex = spriteSheet.createSprite(spriteIndex);
            width = tex.width;
            height = tex.height;
            addFrame(tex.data);
        }

        if (blankSize > 0) {
            width = height = blankSize;
            addFrame(new int[blankSize * blankSize]);
        }

        if (framesTextureData.isEmpty())
            throw new RuntimeException("No base frame for texture: " + getIconName());

        return false;
    }

    @Override
    public boolean hasAnimationMetadata() {
        return textureFX != null || super.hasAnimationMetadata();
    }

    @Override
    public int getFrameCount() {
        if (textureFX != null)
            return 1;

        return super.getFrameCount();
    }

    public TextureSpecial blank(int size) {
        blankSize = size;
        return this;
    }

    public TextureSpecial selfRegister() {
        selfRegister = true;
        TextureUtils.addIconRegistrar(this);
        return this;
    }

    @Override
    public void registerIcons(IIconRegister register) {
        if (selfRegister)
            ((TextureMap) register).setTextureEntry(getIconName(), this);
    }

    @Override
    public int atlasIndex() {
        return atlasIndex;
    }
}
