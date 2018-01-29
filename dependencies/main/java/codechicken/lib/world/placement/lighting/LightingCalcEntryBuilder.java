package codechicken.lib.world.placement.lighting;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

/**
 * Created by covers1624 on 8/1/2016.
 */
public class LightingCalcEntryBuilder {

    private int dimension;
    private BlockPos pos;
    private boolean chunkHeightModified = false;
    private IBlockState oldState;
    private IBlockState newState;
    private int oldLightOpacity;
    private int newLightOpacity;
    private int skyLight;
    private int blockLight;
    private int height;

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public void setBasePos(BlockPos pos) {
        this.pos = pos;
    }

    public void setChunkHeightModified() {
        chunkHeightModified = true;
    }

    public void setOldState(IBlockState state) {
        oldState = state;
    }

    public void setNewState(IBlockState state) {
        newState = state;
    }

    public void setOldLightOpacity(int lightOpacity) {
        oldLightOpacity = lightOpacity;
    }

    public void setNewLightOpacity(int lightOpacity) {
        newLightOpacity = lightOpacity;
    }

    public void setSkyLight(int light) {
        skyLight = light;
    }

    public void setBlockLight(int light) {
        blockLight = light;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public LightingCalcEntry build() {
        return new LightingCalcEntry(dimension, pos, chunkHeightModified, oldState, newState, oldLightOpacity, newLightOpacity, skyLight, blockLight, height);
    }
}
