package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockConcealedDetector;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityPlayerDetector extends TileEntity implements ITickableTileEntity
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.concealedDetector)
    public static TileEntityType<TileEntityPlayerDetector> TYPE;

    private int ticks = 24;
    private AxisAlignedBB playerSearch;
    private boolean result = false;

    public TileEntityPlayerDetector()
    {
        super(TYPE);
    }

    @Override
    public void tick()
    {
        if (!this.world.isRemote && ++this.ticks >= 25)
        {
            this.ticks = 0;
            int facing = 0;
            BlockState state = this.world.getBlockState(this.pos);
            if (state.getBlock() == GCBlocks.concealedDetector)
            {
                facing = state.get(BlockConcealedDetector.FACING);
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
            result = !this.world.getEntitiesWithinAABB(PlayerEntity.class, playerSearch).isEmpty();
            if (this.getBlockState().getBlock() instanceof BlockConcealedDetector)
            {
                ((BlockConcealedDetector) this.getBlockState().getBlock()).updateState(this.world, this.getPos(), result);
            }
        }
    }

    public boolean detectingPlayer()
    {
        return result;
    }
}
