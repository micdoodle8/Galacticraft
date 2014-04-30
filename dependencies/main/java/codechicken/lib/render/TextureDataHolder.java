package codechicken.lib.render;

import java.awt.image.BufferedImage;

public class TextureDataHolder
{
    public int width;
    public int height;
    public int[] data;
    
    public TextureDataHolder(int width, int height)
    {
        this.width = width;
        this.height = height;
        data = new int[width*height];
    }

    public TextureDataHolder(int[] data, int width)
    {
        this.data = data;
        this.width = width;
        height = data.length/width;
    }

    public TextureDataHolder(BufferedImage img)
    {
        this(img.getWidth(), img.getHeight());
        img.getRGB(0, 0, width, height, data, 0, width);
    }

    public TextureDataHolder copyData()
    {
        int[] copy = new int[data.length];
        System.arraycopy(data, 0, copy, 0, data.length);
        data = copy;
        return this;
    }
}
