package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.util.JavaUtil;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;

//This avoids water and oil mixing, by being a different material
public class MaterialOleaginous extends MaterialLiquid
{
    private Class blockLiquidName = BlockLiquid.class;
    private Class blockLiquidStaticName = BlockStaticLiquid.class;
    private Class blockLiquidDynamicName = BlockDynamicLiquid.class;

    public MaterialOleaginous(MapColor color)
    {
        super(color);
        this.setNoPushMobility();
    }

    //Water and other liquids cannot displace oil, but solid blocks can
    @Override
    public boolean blocksMovement()
    {
        return JavaUtil.instance.isCalledBy(blockLiquidStaticName, blockLiquidName, blockLiquidDynamicName);
    }
}
