package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public abstract class BlockMachineBase extends BlockTileGC implements IShiftDescription, ISortableBlock
{
    public static final int METADATA_MASK = 0x0c; //Used to select the machine type from metadata
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockMachineBase(Material material)
    {
        super(material);
    }

    public abstract String getUnlocalizedName(int typenum);

    public static EnumFacing getFront(IBlockState state)
    {
        if (state.getBlock() instanceof BlockMachineBase)
        {
            return (state.getValue(BlockMachineBase.FACING));
        }
        return EnumFacing.NORTH;
    }
}
