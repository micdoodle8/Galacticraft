package codechicken.lib.render;

import java.util.ArrayList;
import codechicken.lib.render.SpriteSheetManager.SpriteSheet;
import codechicken.lib.render.TextureUtils.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class TextureSpecial extends TextureAtlasSprite implements IIconRegister
{
    //sprite sheet fields
    private int spriteIndex;
    private SpriteSheet spriteSheet;
    
    //textureFX fields
    private TextureFX textureFX;
    
    private int blankSize = -1;

    private ArrayList<TextureDataHolder> baseTextures;
    
    private boolean selfRegister;
    public int atlasIndex;
    
    protected TextureSpecial(String par1)
    {
        super(par1);
    }
    
    public TextureSpecial addTexture(TextureDataHolder t)
    {
        if(baseTextures == null)
            baseTextures = new ArrayList<TextureDataHolder>();
        baseTextures.add(t);
        return this;
    }
    
    public TextureSpecial baseFromSheet(SpriteSheet spriteSheet, int spriteIndex)
    {
        this.spriteSheet = spriteSheet;
        this.spriteIndex = spriteIndex;
        return this;
    }
    
    public TextureSpecial addTextureFX(TextureFX fx)
    {
        textureFX = fx;
        return this;
    }
    
    @Override
    public void initSprite(int sheetWidth, int sheetHeight, int originX, int originY, boolean rotated)
    {
        super.initSprite(sheetWidth, sheetHeight, originX, originY, rotated);
        if(textureFX != null)
            textureFX.onTextureDimensionsUpdate(width, height);
    }
    
    @Override
    public void updateAnimation()
    {
        if(textureFX != null)
        {
            textureFX.update();
            if(textureFX.changed())
                TextureUtil.uploadTextureSub(textureFX.imageData, width, height, originX, originY, false, false);
        }
    }
    
    @Override
    public boolean load(ResourceManager manager, ResourceLocation location)
    {
        if(baseTextures != null)
        {
            for(TextureDataHolder tex : baseTextures)
            {
                framesTextureData.add(tex.data);
                width = tex.width;
                height = tex.height;
            }
        }
        
        if(spriteSheet != null)
        {
            TextureDataHolder tex = spriteSheet.createSprite(spriteIndex);
            width = tex.width;
            height = tex.height;
            framesTextureData.add(tex.data);
        }
        
        if(blankSize > 0)
        {
            width = height = blankSize;
            framesTextureData.add(new int[blankSize*blankSize]);
        }
        
        if(framesTextureData.isEmpty())
            throw new RuntimeException("No base frame for texture: "+getIconName());
        
        return true;
    }
    
    @Override
    public boolean hasAnimationMetadata()
    {
        return textureFX != null || super.hasAnimationMetadata();
    }
    
    @Override
    public int getFrameCount()
    {
        if(textureFX != null)
            return 1;
        
        return super.getFrameCount();
    }

    public TextureSpecial blank(int size)
    {
        blankSize = size;
        return this;
    }
    
    public TextureSpecial selfRegister()
    {
        selfRegister = true;
        TextureUtils.addIconRegistrar(this);
        return this;
    }
    
    @Override
    public void registerIcons(IconRegister register)
    {
        if(selfRegister)
            ((TextureMap)register).setTextureEntry(getIconName(), this);
    }
    
    @Override
    public int atlasIndex()
    {
        return atlasIndex;
    }
}
