package codechicken.lib.render;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.util.ResourceLocation;

public class PlaceholderTexture extends TextureAtlasSprite
{
    protected PlaceholderTexture(String par1)
    {
        super(par1);
    }
    
    @Override
    public boolean load(ResourceManager manager, ResourceLocation location)
    {
        return false;
    }
}
