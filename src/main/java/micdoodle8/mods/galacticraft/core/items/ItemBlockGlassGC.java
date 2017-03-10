package micdoodle8.mods.galacticraft.core.items;

import net.minecraft.block.Block;

public class ItemBlockGlassGC extends ItemBlockGC
{
    public ItemBlockGlassGC(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }
}
