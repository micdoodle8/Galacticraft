package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.BlockStationary;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.liquids.ILiquid;

public class GCCoreBlockCrudeOilStationary extends BlockStationary implements ILiquid
{
	public GCCoreBlockCrudeOilStationary(int par1, Material par2Material) 
	{
		super(par1, par2Material);
		this.setHardness(100F);
		this.setLightOpacity(3);
		this.blockIndexInTexture = 237;
	}

	@Override
	public int stillLiquidId() 
	{
		return GCCoreBlocks.crudeOilStill.blockID;
	}

	@Override
	public boolean isMetaSensitive() 
	{
		return false;
	}

	@Override
	public int stillLiquidMeta() 
	{
		return 0;
	}

	@Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getGCCrudeOilRenderID();
    }

	@Override
    public String getTextureFile()
    {
    	return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
    }
}
