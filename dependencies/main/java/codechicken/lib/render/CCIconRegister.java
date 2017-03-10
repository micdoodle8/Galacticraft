package codechicken.lib.render;

import codechicken.lib.texture.TextureUtils;
import codechicken.lib.texture.TextureUtils.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

/**
 * Created by covers1624 on 5/22/2016.
 */
public class CCIconRegister implements IIconRegister {

    public static ArrayList<ResourceLocation> locations = new ArrayList<ResourceLocation>();

    static {
        TextureUtils.addIconRegister(new CCIconRegister());
    }

    @Override
    public void registerIcons(TextureMap textureMap) {
        for (ResourceLocation texture : locations) {
            textureMap.registerSprite(texture);
        }
    }

    public static void registerTexture(String texture) {
        registerTexture(new ResourceLocation(texture));
    }

    public static void registerTexture(ResourceLocation location) {
        locations.add(location);
    }

    public static void registerBlockTexture(String string) {
        registerBlockTexture(new ResourceLocation(string));
    }

    public static void registerBlockTexture(ResourceLocation location) {
        registerTexture(new ResourceLocation(location.getResourceDomain(), "blocks/" + location.getResourcePath()));
    }

    public static void registerItemTexture(String string) {
        registerItemTexture(new ResourceLocation(string));
    }

    public static void registerItemTexture(ResourceLocation location) {
        registerTexture(new ResourceLocation(location.getResourceDomain(), "items/" + location.getResourcePath()));
    }

}
