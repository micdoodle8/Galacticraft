package micdoodle8.mods.galacticraft.moon.items;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class GCMoonItemBlock extends ItemBlock
{
	public GCMoonItemBlock(int i)
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
			name = "aluminummoon";
			break;
		}
		case 1:
		{
			name = "ironmoon";
			break;
		}
		case 2:
		{
			name = "cheesestone";
			break;
		}
		case 3:
		{
			name = "moondirt";
			break;
		}
		case 4:
		{
			name = "moonstone";
			break;
		}
		case 5:
		{
			name = "moongrass";
			break;
		}
		default:
			name = "null";
		}
		
		return this.getItemName() + "." + name;
	}
}
