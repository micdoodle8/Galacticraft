package micdoodle8.mods.galacticraft.core.client;

import java.awt.image.BufferedImage;

import net.minecraft.client.renderer.texture.DynamicTexture;

public class DynamicTextureProper extends DynamicTexture
{
	private boolean updateFlag = false;
    private final int width;	//We could transform these in the base class to protected
    private final int height;	//but whatever.

	public DynamicTextureProper(BufferedImage img)
	{
        this(img.getWidth(), img.getHeight());
        this.update(img);
	}

    public DynamicTextureProper(int width, int height)
    {
    	super(width, height);
    	this.width = width;
    	this.height = height;
    }

    public void update(BufferedImage img)
    {
   		img.getRGB(0, 0, this.width, this.height, this.getTextureData(), 0, this.width);
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
