package codechicken.lib.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * Created by covers1624 on 3/28/2016.
 */
public interface IDisplayTickTile {

    @SideOnly (Side.CLIENT)
    void randomDisplayTick(World world, BlockPos position, IBlockState state, Random random);

}
