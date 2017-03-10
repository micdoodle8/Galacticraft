package codechicken.lib.texture;

import codechicken.lib.colour.Colour;
import codechicken.lib.colour.ColourARGB;
import com.google.common.base.Function;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TextureUtils {

    //TODO, Move this somewhere else.
    public interface IIconRegister {

        @SideOnly (Side.CLIENT)
        void registerIcons(TextureMap textureMap);
    }

    static {
        MinecraftForge.EVENT_BUS.register(new TextureUtils());
    }

    private static ArrayList<IIconRegister> iconRegisters = new ArrayList<IIconRegister>();

    public static Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter = new Function<ResourceLocation, TextureAtlasSprite>() {
        @Override
        public TextureAtlasSprite apply(ResourceLocation input) {
            return getTexture(input);
        }
    };

    //TODO Rename this.
    public static void addIconRegister(IIconRegister registrar) {
        iconRegisters.add(registrar);
    }

    public static void registerReloadListener(IResourceManagerReloadListener reloadListener) {
        //If this crashes people need to stop using reflection..
        getResourceManager().registerReloadListener(reloadListener);
    }

    @SubscribeEvent
    public void textureLoad(TextureStitchEvent.Pre event) {
        for (IIconRegister reg : iconRegisters) {
            reg.registerIcons(event.getMap());
        }
    }

    /**
     * @return an array of ARGB pixel data
     */
    public static int[] loadTextureData(ResourceLocation resource) {
        return loadTexture(resource).data;
    }

    public static Colour[] loadTextureColours(ResourceLocation resource) {
        int[] idata = loadTextureData(resource);
        Colour[] data = new Colour[idata.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = new ColourARGB(idata[i]);
        }
        return data;
    }

    //TODO Move to some sort of IO Utils.
    public static InputStream getTextureResource(ResourceLocation textureFile) throws IOException {
        return Minecraft.getMinecraft().getResourceManager().getResource(textureFile).getInputStream();
    }

    public static BufferedImage loadBufferedImage(ResourceLocation textureFile) {
        try {
            return loadBufferedImage(getTextureResource(textureFile));
        } catch (Exception e) {
            System.err.println("Failed to load texture file: " + textureFile);
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage loadBufferedImage(InputStream in) throws IOException {
        BufferedImage img = ImageIO.read(in);
        in.close();
        return img;
    }

    public static void copySubImg(int[] fromTex, int fromWidth, int fromX, int fromY, int width, int height, int[] toTex, int toWidth, int toX, int toY) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int fp = (y + fromY) * fromWidth + x + fromX;
                int tp = (y + toX) * toWidth + x + toX;

                toTex[tp] = fromTex[fp];
            }
        }
    }

    public static TextureAtlasSprite getBlankIcon(int size, TextureMap textureMap) {
        String s = "blank_" + size;
        TextureAtlasSprite icon = textureMap.getTextureExtry(s);
        if (icon == null) {
            textureMap.setTextureEntry(s, icon = new TextureSpecial(s).blank(size));
        }

        return icon;
    }

    public static TextureSpecial getTextureSpecial(TextureMap textureMap, String name) {
        if (textureMap.getTextureExtry(name) != null) {
            throw new IllegalStateException("Texture: " + name + " is already registered");
        }

        TextureSpecial icon = new TextureSpecial(name);
        textureMap.setTextureEntry(name, icon);
        return icon;
    }

    public static void prepareTexture(int target, int texture, int min_mag_filter, int wrap) {
        GlStateManager.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, min_mag_filter);
        GlStateManager.glTexParameteri(target, GL11.GL_TEXTURE_MAG_FILTER, min_mag_filter);
        if (target == GL11.GL_TEXTURE_2D) {
            GlStateManager.bindTexture(target);
        } else {
            GL11.glBindTexture(target, texture);
        }

        switch (target) {
            case GL12.GL_TEXTURE_3D:
                GlStateManager.glTexParameteri(target, GL12.GL_TEXTURE_WRAP_R, wrap);
            case GL11.GL_TEXTURE_2D:
                GlStateManager.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_T, wrap);
            case GL11.GL_TEXTURE_1D:
                GlStateManager.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_S, wrap);
        }
    }

    public static TextureDataHolder loadTexture(ResourceLocation resource) {
        BufferedImage img = loadBufferedImage(resource);
        if (img == null) {
            throw new RuntimeException("Texture not found: " + resource);
        }
        return new TextureDataHolder(img);
    }

    /**
     * Uses an empty placeholder texture to tell if the map has been reloaded since the last call to refresh texture and the texture with name needs to be reacquired to be valid
     */
    public static boolean refreshTexture(TextureMap map, String name) {
        if (map.getTextureExtry(name) == null) {
            map.setTextureEntry(name, new PlaceholderTexture(name));
            return true;
        }
        return false;
    }

    /*public static IIcon safeIcon(IIcon icon) {
        if (icon == null)
            icon = ((TextureMap) Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");

        return icon;
    }*/

    public static TextureManager getTextureManager() {
        return Minecraft.getMinecraft().renderEngine;
    }

    public static TextureMap getTextureMap() {
        return Minecraft.getMinecraft().getTextureMapBlocks();
    }

    public static IReloadableResourceManager getResourceManager() {
        return (IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager();
    }

    public static TextureAtlasSprite getMissingSprite() {
        return getTextureMap().getMissingSprite();
    }

    public static TextureAtlasSprite getTexture(String location) {
        return getTextureMap().getAtlasSprite(location);
    }

    public static TextureAtlasSprite getTexture(ResourceLocation location) {
        return getTexture(location.toString());
    }

    public static TextureAtlasSprite getBlockTexture(String string) {
        return getBlockTexture(new ResourceLocation(string));
    }

    public static TextureAtlasSprite getBlockTexture(ResourceLocation location) {
        return getTexture(new ResourceLocation(location.getResourceDomain(), "blocks/" + location.getResourcePath()));
    }

    public static TextureAtlasSprite getItemTexture(String string) {
        return getItemTexture(new ResourceLocation(string));
    }

    public static TextureAtlasSprite getItemTexture(ResourceLocation location) {
        return getTexture(new ResourceLocation(location.getResourceDomain(), "items/" + location.getResourcePath()));
    }

    public static IResource getResource(String location) throws IOException {
        return getResource(new ResourceLocation(location));
    }

    public static IResource getResource(ResourceLocation location) throws IOException {
        return getResourceManager().getResource(location);
    }

    public static void changeTexture(String texture) {
        changeTexture(new ResourceLocation(texture));
    }

    public static void changeTexture(ResourceLocation texture) {
        getTextureManager().bindTexture(texture);
    }

    public static void disableMipmap(String texture) {
        disableMipmap(new ResourceLocation(texture));
    }

    public static void disableMipmap(ResourceLocation texture) {
        getTextureManager().getTexture(texture).setBlurMipmap(false, false);
    }

    public static void restoreLastMipmap(String texture) {
        restoreLastMipmap(new ResourceLocation(texture));
    }

    public static void restoreLastMipmap(ResourceLocation location) {
        getTextureManager().getTexture(location).restoreLastBlurMipmap();
    }

    public static void bindBlockTexture() {
        changeTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }

    public static void dissableBlockMipmap() {
        disableMipmap(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }

    public static void restoreBlockMipmap() {
        restoreLastMipmap(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }

    public static TextureAtlasSprite[] getSideIconsForBlock(IBlockState state) {
        TextureAtlasSprite[] sideSprites = new TextureAtlasSprite[6];
        TextureAtlasSprite missingSprite = getMissingSprite();
        for (int i = 0; i < 6; i++) {
            TextureAtlasSprite[] sprites = getIconsForBlock(state, i);
            TextureAtlasSprite sideSprite = missingSprite;
            if (sprites.length != 0) {
                sideSprite = sprites[0];
            }
            sideSprites[i] = sideSprite;
        }
        return sideSprites;
    }

    public static TextureAtlasSprite[] getIconsForBlock(IBlockState state, int side) {
        return getIconsForBlock(state, EnumFacing.values()[side]);
    }

    public static TextureAtlasSprite[] getIconsForBlock(IBlockState state, EnumFacing side) {
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        if (model != null) {
            List<BakedQuad> quads = model.getQuads(state, side, 0);
            if (quads != null && quads.size() > 0) {
                TextureAtlasSprite[] sprites = new TextureAtlasSprite[quads.size()];
                for (int i = 0; i < quads.size(); i++) {
                    sprites[i] = quads.get(i).getSprite();
                }
                return sprites;
            }
        }
        return new TextureAtlasSprite[0];
    }

    public static TextureAtlasSprite getParticleIconForBlock(IBlockState state) {
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        if (model != null) {
            return model.getParticleTexture();
        }
        return null;
    }

}
