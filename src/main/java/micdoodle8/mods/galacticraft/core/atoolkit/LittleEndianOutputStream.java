/*
 * LittleEndianOutputStream.java
 *
 * Created on 07 November 2006, 08:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package micdoodle8.mods.galacticraft.core.atoolkit;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Writes little-endian data to a target <tt>OutputStream</tt> by reversing byte ordering.
 * @author Ian McDonagh
 */
public class LittleEndianOutputStream extends DataOutputStream {
  
  /**
   * Creates a new instance of <tt>LittleEndianOutputStream</tt>, which will write to the specified target.
   * @param out the target <tt>OutputStream</tt>
   */
  public LittleEndianOutputStream(java.io.OutputStream out) {
    super(out);
  }
  
  /**
   * Writes a little-endian <tt>short</tt> value
   * @param value the source value to convert
   * @throws java.io.IOException if an error occurs
   */
  public void writeShortLE(short value) throws IOException {
    value = LittleEndianOutputStream.swapShort(value);
    super.writeShort(value);
  }
  
  /**
   * Writes a little-endian <tt>int</tt> value
   * @param value the source value to convert
   * @throws java.io.IOException if an error occurs
   */
  public void writeIntLE(int value) throws IOException {
    value = LittleEndianOutputStream.swapInteger(value);
    super.writeInt(value);
  }
  
  /**
   * Writes a little-endian <tt>float</tt> value
   * @param value the source value to convert
   * @throws java.io.IOException if an error occurs
   */
  public void writeFloatLE(float value) throws IOException {
    value = LittleEndianOutputStream.swapFloat(value);
    super.writeFloat(value);
  }
  
  /**
   * Writes a little-endian <tt>long</tt> value
   * @param value the source value to convert
   * @throws java.io.IOException if an error occurs
   */
  public void writeLongLE(long value) throws IOException {
    value = LittleEndianOutputStream.swapLong(value);
    super.writeLong(value);
  }
  
  /**
   * Writes a little-endian <tt>double</tt> value
   * @param value the source value to convert
   * @throws java.io.IOException if an error occurs
   */
  public void writeDoubleLE(double value) throws IOException {
    value = LittleEndianOutputStream.swapDouble(value);
    super.writeDouble(value);
  }
  
  /**
   * @since 0.6
   */
  public void writeUnsignedInt(long value) throws IOException {
    int i1 = (int)(value >> 24);
    int i2 = (int)((value >> 16) & 0xFF);
    int i3 = (int)((value >> 8) & 0xFF);
    int i4 = (int)(value & 0xFF);
    
    write(i1);
    write(i2);
    write(i3);
    write(i4);
  }
  
  /**
   * @since 0.6
   */
  public void writeUnsignedIntLE(long value) throws IOException {
    int i1 = (int)(value >> 24);
    int i2 = (int)((value >> 16) & 0xFF);
    int i3 = (int)((value >> 8) & 0xFF);
    int i4 = (int)(value & 0xFF);
    
    write(i4);
    write(i3);
    write(i2);
    write(i1);
  }
  
  /**
   * Reverses the byte order of the source <tt>short</tt> value
   * @param value the source value
   * @return the converted value
   */
  public static short swapShort(short value) {
    return
        (short) (
        ((value & 0xFF00) >> 8)
        | ((value & 0x00FF) << 8)
        )
        ;
  }
  
  /**
   * Reverses the byte order of the source <tt>int</tt> value
   * @param value the source value
   * @return the converted value
   */
  public static int swapInteger(int value) {
    return
        ((value & 0xFF000000) >> 24)
        | ((value & 0x00FF0000) >> 8)
        | ((value & 0x0000FF00) << 8)
        | ((value & 0x000000FF) << 24)
        ;
  }
  
  /**
   * Reverses the byte order of the source <tt>long</tt> value
   * @param value the source value
   * @return the converted value
   */
  public static long swapLong(long value) {
    return
        ((value & 0xFF00000000000000L) >> 56)
        | ((value & 0x00FF000000000000L) >> 40)
        | ((value & 0x0000FF0000000000L) >> 24)
        | ((value & 0x000000FF00000000L) >> 8)
        | ((value & 0x00000000FF000000L) << 8)
        | ((value & 0x0000000000FF0000L) << 24)
        | ((value & 0x000000000000FF00L) << 40)
        | ((value & 0x00000000000000FFL) << 56)
        ;
  }
  
  /**
   * Reverses the byte order of the source <tt>float</tt> value
   * @param value the source value
   * @return the converted value
   */
  public static float swapFloat(float value) {
    int i = Float.floatToIntBits(value);
    i = swapInteger(i);
    return Float.intBitsToFloat(i);
  }
  
  /**
   * Reverses the byte order of the source <tt>double</tt> value
   * @param value the source value
   * @return the converted value
   */
  public static double swapDouble(double value) {
    long l = Double.doubleToLongBits(value);
    l = swapLong(l);
    return Double.longBitsToDouble(l);
  }
}
