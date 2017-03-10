package codechicken.lib.lighting;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Simple brightness model that only works for axis planar sides
 */
public class SimpleBrightnessModel implements IVertexOperation {

    public static final int operationIndex = CCRenderState.registerOperation();
    public static SimpleBrightnessModel instance = new SimpleBrightnessModel();

    public IBlockAccess access;
    public BlockPos pos = BlockPos.ORIGIN;

    private int sampled = 0;
    private int[] samples = new int[6];
    private BlockPos c = BlockPos.ORIGIN;

    public void locate(IBlockAccess a, BlockPos bPos) {
        access = a;
        pos = bPos;
        sampled = 0;
    }

    public int sample(int side) {
        if ((sampled & 1 << side) == 0) {
            c = pos.offset(EnumFacing.VALUES[side]);
            IBlockState b = access.getBlockState(c);
            samples[side] = access.getCombinedLight(c, b.getBlock().getLightValue(b, access, c));
            sampled |= 1 << side;
        }
        return samples[side];
    }

    @Override
    public boolean load(CCRenderState state) {

        state.pipeline.addDependency(state.sideAttrib);
        return true;
    }

    @Override
    public void operate(CCRenderState state) {
        state.brightness = sample(state.side);
    }

    @Override
    public int operationID() {
        return operationIndex;
    }
}
