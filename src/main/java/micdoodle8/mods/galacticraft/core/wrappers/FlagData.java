package micdoodle8.mods.galacticraft.core.wrappers;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.nbt.NBTTagCompound;

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
	 * 
	 * 0, 0 is the top left.
	 * 
	 * @param posX
	 *            x-position of the flag
	 * @param posY
	 *            y-position of the flag
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
	 * @param posX
	 *            The x-position of the flag
	 * @param posY
	 *            The y-position of the flag
	 * @param colorVec
	 *            The color vector, values from 0-256
	 */
	public void setColorAt(int posX, int posY, Vector3 colorVec)
	{
		this.color[posX][posY][0] = (byte) (colorVec.intX() - 128);
		this.color[posX][posY][1] = (byte) (colorVec.intY() - 128);
		this.color[posX][posY][2] = (byte) (colorVec.intZ() - 128);
	}

	public static FlagData readFlagData(NBTTagCompound nbt)
	{
		int width = nbt.getInteger("FlagWidth");
		int height = nbt.getInteger("FlagHeight");

		FlagData flagData = new FlagData(width, height);

		for (int i = 0; i < flagData.width; i++)
		{
			for (int j = 0; j < flagData.height; j++)
			{
				flagData.color[i][j][0] = nbt.getByte("ColorR-X" + i + "-Y" + j);
				flagData.color[i][j][1] = nbt.getByte("ColorG-X" + i + "-Y" + j);
				flagData.color[i][j][2] = nbt.getByte("ColorB-X" + i + "-Y" + j);
			}
		}

		return flagData;
	}

	public void saveFlagData(NBTTagCompound nbt)
	{
		nbt.setInteger("FlagWidth", this.width);
		nbt.setInteger("FlagHeight", this.height);

		for (int i = 0; i < this.width; i++)
		{
			for (int j = 0; j < this.height; j++)
			{
				nbt.setByte("ColorR-X" + i + "-Y" + j, this.color[i][j][0]);
				nbt.setByte("ColorG-X" + i + "-Y" + j, this.color[i][j][1]);
				nbt.setByte("ColorB-X" + i + "-Y" + j, this.color[i][j][2]);
			}
		}
	}
}
