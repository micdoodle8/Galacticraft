package micdoodle8.mods.galacticraft.core.world.gen.layer_mapping;

public class GenLayerFuzzyZoom extends GenLayerZoom
{
    public GenLayerFuzzyZoom(long p_i2123_1_, GenLayerGCMap p_i2123_3_)
    {
        super(p_i2123_1_, p_i2123_3_);
    }

    /**
     * returns the most frequently occurring number of the set, or a random number from those provided
     */
    protected int selectModeOrRandom(int p_151617_1_, int p_151617_2_, int p_151617_3_, int p_151617_4_)
    {
        return this.selectRandom(new int[] {p_151617_1_, p_151617_2_, p_151617_3_, p_151617_4_});
    }
}