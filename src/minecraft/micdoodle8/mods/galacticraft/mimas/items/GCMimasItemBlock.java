package micdoodle8.mods.galacticraft.mimas.items;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class GCMimasItemBlock extends ItemBlock
{
	public GCMimasItemBlock(int i)
	{
		super(i);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int meta)
    {
        return meta;
    }

	@Override
	public String getItemNameIS(ItemStack itemstack) 
	{
		String name = "";
		
		switch(itemstack.getItemDamage()) 
		{
		case 0: 
		{
			name = "grass";
			break;
		}
		case 1: 
		{
			name = "stone";
			break;
		}
		default: 
			name = "null";
		}
		return this.getItemName() + "." + name;
	}
}
