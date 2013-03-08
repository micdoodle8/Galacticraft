package micdoodle8.mods.galacticraft.enceladus.items;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class GCEnceladusItemBlock extends ItemBlock
{
	public GCEnceladusItemBlock(int i)
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
			name = "dirt";
			break;
		}
		case 2:
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
