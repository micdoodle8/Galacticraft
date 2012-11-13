package micdoodle8.mods.galacticraft.moon.items;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class GCMoonItemBlockOre extends ItemBlock
{
	public GCMoonItemBlockOre(int i)
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
		default: 
			name = "null";
		}
		
		return getItemName() + "." + name;
	}
}
