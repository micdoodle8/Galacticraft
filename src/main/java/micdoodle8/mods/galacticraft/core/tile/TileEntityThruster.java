package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityThruster extends TileEntity
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.spinThruster)
    public static TileEntityType<TileEntityThruster> TYPE;

    public TileEntityThruster()
    {
        super(TYPE);
    }

    //    @Override
//    public boolean canUpdate()
//    {
//        return false;
//    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }
}
