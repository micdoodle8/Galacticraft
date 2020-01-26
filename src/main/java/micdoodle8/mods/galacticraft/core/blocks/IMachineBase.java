package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public interface IMachineBase
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public String getUnlocalizedName(int typenum);

    public static EnumFacing getFront(IBlockState state)
    {
        if (state.getBlock() instanceof IMachineBase)
        {
            return (state.getValue(IMachineBase.FACING));
        }
        return EnumFacing.NORTH;
    }
}
