package micdoodle8.mods.galacticraft;

import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class GCItemBlockOre extends ItemBlock
{
	public GCItemBlockOre(int i, Block block)
	{
		super(i);
		setHasSubtypes(true);
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
			name = "desh";
			break;
		}
		case 1: 
		{
			name = "elecrum";
			break;
		}
		case 2: 
		{
			name = "quandrium";
			break;
		}
		case 3: 
		{
			name = "aluminumearth";
			break;
		}
		case 4: 
		{
			name = "aluminummars";
			break;
		}
		case 5: 
		{
			name = "copperearth";
			break;
		}
		case 6: 
		{
			name = "coppermars";
			break;
		}
		case 7: 
		{
			name = "titaniumearth";
			break;
		}
		case 8: 
		{
			name = "titaniummars";
			break;
		}
		default: 
			name = "ore";
		}
		return getItemName() + "." + name;
	}
}
