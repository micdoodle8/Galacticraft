package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.block.AirBlock;
import net.minecraft.state.BooleanProperty;

public abstract class BlockThermalAir extends AirBlock
{
    public static final BooleanProperty THERMAL = BooleanProperty.create("thermal");

    public BlockThermalAir(Properties builder)
    {
        super(builder);
    }
}
