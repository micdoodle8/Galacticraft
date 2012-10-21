package micdoodle8.mods.galacticraft.core;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;

public class GCCoreBlockFallenMeteor extends GCCoreBlock
{
	public GCCoreBlockFallenMeteor(int i) 
	{
		super(i, 0, Material.rock);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	@Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getGCMeteorRenderID();
    }
}
