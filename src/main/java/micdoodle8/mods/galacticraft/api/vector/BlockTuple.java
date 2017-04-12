package micdoodle8.mods.galacticraft.api.vector;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IPlantable;

public class BlockTuple
{
    public Block block;
    public int meta;

    public BlockTuple(Block b, int m)
    {
        this.block = b;
        this.meta = m;
    }
    
    @Override
    public String toString()
    {
    	if (this.block instanceof IPlantable) return "tile.crops.name";

        Item item = Item.getItemFromBlock(this.block);
    	if (item == null) return "unknown:" + this.block.getUnlocalizedName(); 
    	return new ItemStack(item, 1, this.meta).getUnlocalizedName() + ".name";
    }
}
