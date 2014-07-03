/*
 * InfoHeader.java
 *
 * Created on 10 May 2006, 08:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package micdoodle8.mods.galacticraft.core.atoolkit;

import java.io.IOException;

/**
 * Represents a bitmap <tt>InfoHeader</tt> structure, which provides header
 * information.
 * 
 * @author Ian McDonagh
 */
public class BMPInfoHeader
{

	/**
	 * The size of this <tt>InfoHeader</tt> structure in bytes.
	 */
	public int iSize;
	/**
	 * The width in pixels of the bitmap represented by this <tt>InfoHeader</tt>
	 * .
	 */
	public int iWidth;
	/**
	 * The height in pixels of the bitmap represented by this
	 * <tt>InfoHeader</tt>.
	 */
	public int iHeight;
	/**
	 * The number of planes, which should always be <tt>1</tt>.
	 */
	public short sPlanes;
	/**
	 * The bit count, which represents the colour depth (bits per pixel). This
	 * should be either <tt>1</tt>, <tt>4</tt>, <tt>8</tt>, <tt>24</tt> or
	 * <tt>32</tt>.
	 */
	public short sBitCount;
	/**
	 * The compression type, which should be one of the following:
	 * <ul>
	 * <li>{@link BMPConstants#BI_RGB BI_RGB} - no compression</li>
	 * <li>{@link BMPConstants#BI_RLE8 BI_RLE8} - 8-bit RLE compression</li>
	 * <li>{@link BMPConstants#BI_RLE4 BI_RLE4} - 4-bit RLE compression</li>
	 * </ul>
	 */
	public int iCompression;
	/**
	 * The compressed size of the image in bytes, or <tt>0</tt> if
	 * <tt>iCompression</tt> is <tt>0</tt>.
	 */
	public int iImageSize;
	/**
	 * Horizontal resolution in pixels/m.
	 */
	public int iXpixelsPerM;
	/**
	 * Vertical resolution in pixels/m.
	 */
	public int iYpixelsPerM;
	/**
	 * Number of colours actually used in the bitmap.
	 */
	public int iColorsUsed;
	/**
	 * Number of important colours (<tt>0</tt> = all).
	 */
	public int iColorsImportant;

	/**
	 * Calculated number of colours, based on the colour depth specified by
	 * {@link #sBitCount sBitCount}.
	 */
	public int iNumColors;

	/**
	 * Creates an <tt>InfoHeader</tt> structure from the source input.
	 * 
	 * @param in
	 *            the source input
	 * @throws java.io.IOException
	 *             if an error occurs
	 */
	public BMPInfoHeader(micdoodle8.mods.galacticraft.core.atoolkit.LittleEndianInputStream in) throws IOException
	{
		//Size of InfoHeader structure = 40
		this.iSize = in.readIntLE();

		this.init(in, this.iSize);
	}

	/**
	 * @since 0.6
	 */
	public BMPInfoHeader(micdoodle8.mods.galacticraft.core.atoolkit.LittleEndianInputStream in, int infoSize) throws IOException
	{
		this.init(in, infoSize);
	}

	/**
	 * @since 0.6
	 */
	protected void init(micdoodle8.mods.galacticraft.core.atoolkit.LittleEndianInputStream in, int infoSize) throws IOException
	{
		this.iSize = infoSize;

		//Width
		this.iWidth = in.readIntLE();
		//Height
		this.iHeight = in.readIntLE();
		//Planes (=1)
		this.sPlanes = in.readShortLE();
		//Bit count
		this.sBitCount = in.readShortLE();

		//calculate NumColors
		this.iNumColors = (int) Math.pow(2, this.sBitCount);

		//Compression
		this.iCompression = in.readIntLE();
		//Image size - compressed size of image or 0 if Compression = 0
		this.iImageSize = in.readIntLE();
		//horizontal resolution pixels/meter
		this.iXpixelsPerM = in.readIntLE();
		//vertical resolution pixels/meter
		this.iYpixelsPerM = in.readIntLE();
		//Colors used - number of colors actually used
		this.iColorsUsed = in.readIntLE();
		//Colors important - number of important colors 0 = all
		this.iColorsImportant = in.readIntLE();
	}

	/**
	 * Creates an <tt>InfoHeader</tt> with default values.
	 */
	public BMPInfoHeader()
	{
		//Size of InfoHeader structure = 40
		this.iSize = 40;
		//Width
		this.iWidth = 0;
		//Height
		this.iHeight = 0;
		//Planes (=1)
		this.sPlanes = 1;
		//Bit count
		this.sBitCount = 0;

		//caculate NumColors
		this.iNumColors = 0;

		//Compression
		this.iCompression = BMPConstants.BI_RGB;
		//Image size - compressed size of image or 0 if Compression = 0
		this.iImageSize = 0;
		//horizontal resolution pixels/meter
		this.iXpixelsPerM = 0;
		//vertical resolution pixels/meter
		this.iYpixelsPerM = 0;
		//Colors used - number of colors actually used
		this.iColorsUsed = 0;
		//Colors important - number of important colors 0 = all
		this.iColorsImportant = 0;
	}

	/**
	 * Creates a copy of the source <tt>InfoHeader</tt>.
	 * 
	 * @param source
	 *            the source to copy
	 */
	public BMPInfoHeader(BMPInfoHeader source)
	{
		this.iColorsImportant = source.iColorsImportant;
		this.iColorsUsed = source.iColorsUsed;
		this.iCompression = source.iCompression;
		this.iHeight = source.iHeight;
		this.iWidth = source.iWidth;
		this.iImageSize = source.iImageSize;
		this.iNumColors = source.iNumColors;
		this.iSize = source.iSize;
		this.iXpixelsPerM = source.iXpixelsPerM;
		this.iYpixelsPerM = source.iYpixelsPerM;
		this.sBitCount = source.sBitCount;
		this.sPlanes = source.sPlanes;

	}

	/**
	 * Writes the <tt>InfoHeader</tt> structure to output
	 * 
	 * @param out
	 *            the output to which the structure will be written
	 * @throws java.io.IOException
	 *             if an error occurs
	 */
	public void write(micdoodle8.mods.galacticraft.core.atoolkit.LittleEndianOutputStream out) throws IOException
	{
		//Size of InfoHeader structure = 40
		out.writeIntLE(this.iSize);
		//Width
		out.writeIntLE(this.iWidth);
		//Height
		out.writeIntLE(this.iHeight);
		//Planes (=1)
		out.writeShortLE(this.sPlanes);
		//Bit count
		out.writeShortLE(this.sBitCount);

		//caculate NumColors
		//iNumColors = (int) Math.pow(2, sBitCount);

		//Compression
		out.writeIntLE(this.iCompression);
		//Image size - compressed size of image or 0 if Compression = 0
		out.writeIntLE(this.iImageSize);
		//horizontal resolution pixels/meter
		out.writeIntLE(this.iXpixelsPerM);
		//vertical resolution pixels/meter
		out.writeIntLE(this.iYpixelsPerM);
		//Colors used - number of colors actually used
		out.writeIntLE(this.iColorsUsed);
		//Colors important - number of important colors 0 = all
		out.writeIntLE(this.iColorsImportant);
	}
}
