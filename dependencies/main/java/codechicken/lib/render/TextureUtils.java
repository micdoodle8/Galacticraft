package codechicken.lib.render;

import codechicken.lib.colour.Colour;
import codechicken.lib.colour.ColourARGB;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TextureUtils {
    public static interface IIconRegister {
        public void registerIcons(TextureMap textureMap);
    }

    static {
        MinecraftForge.EVENT_BUS.register(new TextureUtils());
    }

    private static ArrayList<IIconRegister> iconRegisters = new ArrayList<IIconRegister>();

    public static void addIconRegister(IIconRegister registrar) {
        iconRegisters.add(registrar);
    }

    @SubscribeEvent
    public void textureLoad(TextureStitchEvent.Pre event) {
        for (IIconRegister reg : iconRegisters) {
            reg.registerIcons(event.map);
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
        GL11.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, min_mag_filter);
        GL11.glTexParameteri(target, GL11.GL_TEXTURE_MAG_FILTER, min_mag_filter);
        if (target == GL11.GL_TEXTURE_2D) {
            GlStateManager.bindTexture(target);
        } else {
            GL11.glBindTexture(target, texture);
        }

        switch (target) {
        case GL12.GL_TEXTURE_3D:
            GL11.glTexParameteri(target, GL12.GL_TEXTURE_WRAP_R, wrap);
        case GL11.GL_TEXTURE_2D:
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_T, wrap);
        case GL11.GL_TEXTURE_1D:
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_S, wrap);
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
}
