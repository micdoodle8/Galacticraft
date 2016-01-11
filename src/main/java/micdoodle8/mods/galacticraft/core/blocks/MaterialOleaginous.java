package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import org.apache.commons.lang3.exception.ExceptionUtils;

//This avoids water and oil mixing, by being a different material
public class MaterialOleaginous extends MaterialLiquid
{
	private String blockLiquidName = BlockLiquid.class.getName();
	private String blockLiquidStaticName = BlockStaticLiquid.class.getName();
	private String blockLiquidDynamicName = BlockDynamicLiquid.class.getName();
	
	public MaterialOleaginous(MapColor color)
    {
		super(color);
		this.setNoPushMobility();
	}

    //Water and other liquids cannot displace oil, but solid blocks can
	public boolean blocksMovement()
    {
        String string = ExceptionUtils.getStackTrace(new Throwable());
        return string.contains(blockLiquidName) || string.contains(blockLiquidStaticName) || string.contains(blockLiquidDynamicName);
    }
}
