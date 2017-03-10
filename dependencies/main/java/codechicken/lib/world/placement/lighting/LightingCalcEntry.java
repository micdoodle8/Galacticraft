package codechicken.lib.world.placement.lighting;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

/**
 * Created by covers1624 on 8/1/2016.
 */
public class LightingCalcEntry {

    public final int dimension;
    public final BlockPos pos;
    public final boolean chunkHeightModified;
    public final IBlockState oldState;
    public final IBlockState newState;
    public final int oldLightOpacity;
    public final int newLightOpacity;
    public final int skyLight;
    public final int blockLight;
    public final int height;

    public LightingCalcEntry(int dimension, BlockPos pos, boolean chunkHeightModified, IBlockState oldState, IBlockState newState, int oldLightOpacity, int newLightOpacity, int skyLight, int blockLight, int height) {
        this.dimension = dimension;
        this.pos = pos;
        this.chunkHeightModified = chunkHeightModified;
        this.oldState = oldState;
        this.newState = newState;
        this.oldLightOpacity = oldLightOpacity;
        this.newLightOpacity = newLightOpacity;
        this.skyLight = skyLight;
        this.blockLight = blockLight;
        this.height = height;
    }
}
