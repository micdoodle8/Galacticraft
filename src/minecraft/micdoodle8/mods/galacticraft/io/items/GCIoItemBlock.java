package micdoodle8.mods.galacticraft.io.items;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class GCIoItemBlock extends ItemBlock
{
	public GCIoItemBlock(int i)
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
			name = "basalt";
			break;
		}
		case 1:
		{
			name = "stone";
			break;
		}
		case 2:
		{
			name = "sulfur";
			break;
		}
		case 3:
		{
			name = "other";
			break;
		}
		default:
			name = "null";
		}
		return this.getItemName() + "." + name;
	}
}
