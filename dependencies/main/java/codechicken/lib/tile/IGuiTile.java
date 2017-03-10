package codechicken.lib.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by covers1624 on 3/28/2016.
 */
public interface IGuiTile {

    void openGui(World world, BlockPos pos, EntityPlayer player);
}
