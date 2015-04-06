package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;

//This avoids water and oil mixing, by being a different material
public class MaterialOleaginous extends MaterialLiquid
{
	private String blockLiquidName = BlockLiquid.class.getName();
	private String blockLiquidStaticName = BlockStaticLiquid.class.getName();
	private String blockLiquidDynamicName = BlockDynamicLiquid.class.getName();
	
	public MaterialOleaginous(MapColor color)
    {
		super(color);
	}

    //Water and other liquids cannot displace oil, but solid blocks can
	public boolean blocksMovement()
    {
		StackTraceElement[] st = Thread.currentThread().getStackTrace();
		int imax = st.length;
		if (imax > 5) imax = 5;
        for (int i = 1; i < imax; i++)
        {
            String ste = st[i].getClassName();
            if (ste.equals(blockLiquidName) || ste.equals(blockLiquidStaticName) || ste.equals(blockLiquidDynamicName))
            {
                return true;
            }
        }

        return false;
    }
}
