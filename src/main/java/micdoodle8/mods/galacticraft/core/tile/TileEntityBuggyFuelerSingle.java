package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;

public class TileEntityBuggyFuelerSingle extends TileEntity implements ITickableTileEntity
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.buggyPad)
    public static TileEntityType<TileEntityBuggyFuelerSingle> TYPE;

    private int corner = 0;

    public TileEntityBuggyFuelerSingle()
    {
        super(TYPE);
    }

    @Override
    public void tick()
    {
        if (!this.world.isRemote && this.corner == 0)
        {
            final ArrayList<TileEntity> attachedLaunchPads = new ArrayList<TileEntity>();

            for (int x = this.getPos().getX() - 1; x < this.getPos().getX() + 2; x++)
            {
                for (int z = this.getPos().getZ() - 1; z < this.getPos().getZ() + 2; z++)
                {
                    final TileEntity tile = this.world.getTileEntity(new BlockPos(x, this.getPos().getY(), z));

                    if (tile instanceof TileEntityBuggyFuelerSingle && !tile.isRemoved() && ((TileEntityBuggyFuelerSingle)tile).corner == 0)
                    {
                        attachedLaunchPads.add(tile);
                    }
                }
            }

            if (attachedLaunchPads.size() == 9)
            {
                for (final TileEntity tile : attachedLaunchPads)
                {
                    this.world.removeTileEntity(tile.getPos());
                    ((TileEntityBuggyFuelerSingle)tile).corner = 1;
                }

                this.world.setBlockState(this.getPos(), GCBlocks.buggyPad.getDefaultState(), 2);
            }
        }
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }
}
