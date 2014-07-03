/*
 * LittleEndianInputStream.java
 *
 * Created on 07 November 2006, 08:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package micdoodle8.mods.galacticraft.core.atoolkit;

import java.io.EOFException;
import java.io.IOException;

/**
 * Reads little-endian data from a source <tt>InputStream</tt> by reversing byte
 * ordering.
 * 
 * @author Ian McDonagh
 */
public class LittleEndianInputStream extends java.io.DataInputStream
{

	/**
	 * Creates a new instance of <tt>LittleEndianInputStream</tt>, which will
	 * read from the specified source.
	 * 
	 * @param in
	 *            the source <tt>InputStream</tt>
	 */
	public LittleEndianInputStream(java.io.InputStream in)
	{
		super(in);
	}

	/**
	 * Reads a little-endian <tt>short</tt> value
	 * 
	 * @throws java.io.IOException
	 *             if an error occurs
	 * @return <tt>short</tt> value with reversed byte order
	 */
	public short readShortLE() throws IOException
	{

		int b1 = this.read();
		int b2 = this.read();

		if (b1 < 0 || b2 < 0)
		{
			throw new EOFException();
		}

		return (short) ((b2 << 8) + b1);
	}

	/**
	 * Reads a little-endian <tt>int</tt> value.
	 * 
	 * @throws java.io.IOException
	 *             if an error occurs
	 * @return <tt>int</tt> value with reversed byte order
	 */
	public int readIntLE() throws IOException
	{
		int b1 = this.read();
		int b2 = this.read();
		int b3 = this.read();
		int b4 = this.read();

		if (b1 < -1 || b2 < -1 || b3 < -1 || b4 < -1)
		{
			throw new EOFException();
		}

		return (b4 << 24) + (b3 << 16) + (b2 << 8) + b1;
	}

	/**
	 * Reads a little-endian <tt>float</tt> value.
	 * 
	 * @throws java.io.IOException
	 *             if an error occurs
	 * @return <tt>float</tt> value with reversed byte order
	 */
	public float readFloatLE() throws IOException
	{
		int i = this.readIntLE();

		return Float.intBitsToFloat(i);
	}

	/**
	 * Reads a little-endian <tt>long</tt> value.
	 * 
	 * @throws java.io.IOException
	 *             if an error occurs
	 * @return <tt>long</tt> value with reversed byte order
	 */
	public long readLongLE() throws IOException
	{

		int i1 = this.readIntLE();
		int i2 = this.readIntLE();

		return ((long) i1 << 32) + (i2 & 0xFFFFFFFFL);
	}

	/**
	 * Reads a little-endian <tt>double</tt> value.
	 * 
	 * @throws java.io.IOException
	 *             if an error occurs
	 * @return <tt>double</tt> value with reversed byte order
	 */
	public double readDoubleLE() throws IOException
	{

		long l = this.readLongLE();

		return Double.longBitsToDouble(l);
	}

	/**
	 * @since 0.6
	 */
	public long readUnsignedInt() throws IOException
	{
		long i1 = this.readUnsignedByte();
		long i2 = this.readUnsignedByte();
		long i3 = this.readUnsignedByte();
		long i4 = this.readUnsignedByte();

		return i1 << 24 | i2 << 16 | i3 << 8 | i4;
	}

	/**
	 * @since 0.6
	 */
	public long readUnsignedIntLE() throws IOException
	{
		long i1 = this.readUnsignedByte();
		long i2 = this.readUnsignedByte();
		long i3 = this.readUnsignedByte();
		long i4 = this.readUnsignedByte();

		return i4 << 24 | i3 << 16 | i2 << 8 | i1;
	}
}
