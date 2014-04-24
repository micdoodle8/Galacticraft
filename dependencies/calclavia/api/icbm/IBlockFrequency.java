package calclavia.api.icbm;

/** Applied to all blocks that has a frequency.
 * 
 * @author Calclavia */

public interface IBlockFrequency
{
    /** @param data - Pass an ItemStack if dealing with items with frequencies.
     * @return The frequency of this object. */
    public int getFrequency();

    /** Sets the frequency
     * 
     * @param frequency - The frequency of this object.
     * @param data - Pass an ItemStack if dealing with items with frequencies. */
    public void setFrequency(int frequency);
}
