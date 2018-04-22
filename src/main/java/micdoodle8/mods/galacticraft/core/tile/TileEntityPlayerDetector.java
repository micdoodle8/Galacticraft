package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockConcealedDetector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityPlayerDetector extends TileEntity implements ITickable
{
    private int ticks = 24;
    private AxisAlignedBB playerSearch;
    private boolean result = false;

    @Override
    public void update()
    {
        if (!this.world.isRemote && ++this.ticks >= 25) 
        {
            this.ticks = 0;
            int facing = 0;
            IBlockState state = this.world.getBlockState(this.pos);
            if (state.getBlock() == GCBlocks.concealedDetector)
            {
                facing = state.getValue(BlockConcealedDetector.FACING);
            }
            int x = this.getPos().getX();
            double y = this.getPos().getY();
            int z = this.getPos().getZ();
            double range = 14D;
            double hysteresis = result ? 3D : 0D; 
            switch (facing)
            {
            case 0:
                this.playerSearch = new AxisAlignedBB(x - range / 2 + 0.5D - hysteresis, y - 6 - hysteresis, z - range - hysteresis, x + range / 2 + 0.5D + hysteresis, y + 2 + hysteresis, z + hysteresis);
                break;
            case 1:
                this.playerSearch = new AxisAlignedBB(x + 1 - hysteresis, y - 6 - hysteresis, z - range / 2 + 0.5D - hysteresis, x + range + 1 + hysteresis, y + 2 + hysteresis, z + range / 2 + 0.5D + hysteresis);
                break;
            case 2:
                //South
                this.playerSearch = new AxisAlignedBB(x - range / 2 + 0.5D - hysteresis, y - 6 - hysteresis, z + 1 - hysteresis, x + range / 2 + 0.5D + hysteresis, y + 2 + hysteresis, z + range + 1D + hysteresis);
                break;
            case 3:
                this.playerSearch = new AxisAlignedBB(x - range - hysteresis, y - 6 - hysteresis, z - range / 2 + 0.5D - hysteresis, x + hysteresis, y + 2 + hysteresis, z + range / 2 + 0.5D + hysteresis);
            }
            result = !this.world.getEntitiesWithinAABB(EntityPlayer.class, playerSearch).isEmpty();
            if (this.getBlockType() instanceof BlockConcealedDetector)
            {
                ((BlockConcealedDetector) this.blockType).updateState(this.world, this.getPos(), result);
            }
        }
    }
    
    public boolean detectingPlayer()
    {
        return result;
    }
}
