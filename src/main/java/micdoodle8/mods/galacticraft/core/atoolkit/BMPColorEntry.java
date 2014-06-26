/*
 * ColorEntry.java
 *
 * Created on 10 May 2006, 08:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package micdoodle8.mods.galacticraft.core.atoolkit;

import java.io.IOException;

/**
 * Represents an RGB colour entry used in the palette of an indexed image
 * (colour depth <= 8).
 * 
 * @author Ian McDonagh
 */
public class BMPColorEntry
{

	/**
	 * The red component, which should be in the range <tt>0..255</tt>.
	 */
	public int bRed;
	/**
	 * The green component, which should be in the range <tt>0..255</tt>.
	 */
	public int bGreen;
	/**
	 * The blue component, which should be in the range <tt>0..255</tt>.
	 */
	public int bBlue;
	/**
	 * Unused.
	 */
	public int bReserved;

	/**
	 * Reads and creates a colour entry from the source input.
	 * 
	 * @param in
	 *            the source input
	 * @throws java.io.IOException
	 *             if an error occurs
	 */
	public BMPColorEntry(micdoodle8.mods.galacticraft.core.atoolkit.LittleEndianInputStream in) throws IOException
	{
		this.bBlue = in.readUnsignedByte();
		this.bGreen = in.readUnsignedByte();
		this.bRed = in.readUnsignedByte();
		this.bReserved = in.readUnsignedByte();
	}

	/**
	 * Creates a colour entry with colour components initialized to <tt>0</tt>.
	 */
	public BMPColorEntry()
	{
		this.bBlue = 0;
		this.bGreen = 0;
		this.bRed = 0;
		this.bReserved = 0;
	}

	/**
	 * Creates a colour entry with the specified colour components.
	 * 
	 * @param r
	 *            red component
	 * @param g
	 *            green component
	 * @param b
	 *            blue component
	 * @param a
	 *            unused
	 */
	public BMPColorEntry(int r, int g, int b, int a)
	{
		this.bBlue = b;
		this.bGreen = g;
		this.bRed = r;
		this.bReserved = a;
	}

}
