package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.block.material.Material;

public class BlockDoubleSlabGC extends BlockSlabGC
{
    public BlockDoubleSlabGC(String name, Material material)
    {
        super(material);
        this.setUnlocalizedName(name);
    }

    @Override
    public boolean isDouble()
    {
        return true;
    }
}