package micdoodle8.mods.galacticraft.core.wrappers;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.nbt.NBTTagCompound;

import java.awt.image.BufferedImage;

public class FlagData
{
    private int height;
    private int width;
    private byte[][][] color;

    public FlagData(int width, int height)
    {
        this.height = height;
        this.width = width;
        this.color = new byte[width][height][3];

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                this.color[i][j][0] = 127;
                this.color[i][j][1] = 127;
                this.color[i][j][2] = 127;
            }
        }
    }

    public int getHeight()
    {
        return this.height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getWidth()
    {
        return this.width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    /**
     * Returns the color, normalized, at the flag coordinates.
     * <p/>
     * 0, 0 is the top left.
     *
     * @param posX x-position of the flag
     * @param posY y-position of the flag
     * @return The color vector, capped from 0-1
     */
    public Vector3 getColorAt(int posX, int posY)
    {
        if (posX >= this.width || posY >= this.height)
        {
            return new Vector3(0, 0, 0);
        }

        return new Vector3((this.color[posX][posY][0] + 128) / 256.0D, (this.color[posX][posY][1] + 128) / 256.0D, (this.color[posX][posY][2] + 128) / 256.0D);
    }

    /**
     * Set the color at the designated flag coordinates
     *
     * @param posX     The x-position of the flag
     * @param posY     The y-position of the flag
     * @param colorVec The color vector, values from 0-256
     */
    public void setColorAt(int posX, int posY, Vector3 colorVec)
    {
        this.color[posX][posY][0] = (byte) (colorVec.intX() - 128);
        this.color[posX][posY][1] = (byte) (colorVec.intY() - 128);
        this.color[posX][posY][2] = (byte) (colorVec.intZ() - 128);
    }

    public static FlagData readFlagData(NBTTagCompound nbt)
    {
    	if (nbt.hasKey("FlagWidth"))
    	{
    		//Legacy saves
	    	int width = nbt.getInteger("FlagWidth");
	        int height = nbt.getInteger("FlagHeight");
	
	        FlagData flagData = new FlagData(width, height);
	
	        for (int i = 0; i < width; i++)
	        {
	            for (int j = 0; j < height; j++)
	            {
	                flagData.color[i][j][0] = nbt.getByte("ColorR-X" + i + "-Y" + j);
	                flagData.color[i][j][1] = nbt.getByte("ColorG-X" + i + "-Y" + j);
	                flagData.color[i][j][2] = nbt.getByte("ColorB-X" + i + "-Y" + j);
	            }
	        }
	
	        return flagData;
    	}
    	
    	//New more compact flag save style 
    	int width = nbt.getInteger("FWidth");
        int height = nbt.getInteger("FHeight");

        FlagData flagData = new FlagData(width, height);
        for (int i = 0; i < height; i++)
        {
        	int[] colorRow = nbt.getIntArray("FRow"+i);
        	for (int j = 0; j < width; j++)
            {
                int color = colorRow[j]; 
        		flagData.color[j][i][0] = (byte) (color >> 16);
        		flagData.color[j][i][1] = (byte) ((color >> 8) & 255);
        		flagData.color[j][i][2] = (byte) (color & 255);
            }
        }
    	return flagData;
    }

    public void saveFlagData(NBTTagCompound nbt)
    {
        nbt.setInteger("FWidth", this.width);
        nbt.setInteger("FHeight", this.height);

        for (int i = 0; i < this.height; i++)
        {
        	int[] colorRow = new int[this.width]; 
        	for (int j = 0; j < this.width; j++)
            {
                byte[] arrayColor = this.color[j][i];
                colorRow[j] = ColorUtil.to32BitColorB(arrayColor[0], arrayColor[1], arrayColor[2]);
            }
        	nbt.setIntArray("FRow"+i, colorRow);
        }
    }

    public BufferedImage toBufferedImage()
    {
        BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < this.width; i++)
        {
            for (int j = 0; j < this.height; j++)
            {
                int col = ((this.color[i][j][0] + 128) << 16) | ((this.color[i][j][1] + 128) << 8) | (this.color[i][j][2] + 128);
                image.setRGB(i, j, col);
            }
        }
        return image;
    }
}
