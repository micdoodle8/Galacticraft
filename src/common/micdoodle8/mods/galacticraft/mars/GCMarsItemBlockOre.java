package micdoodle8.mods.galacticraft.mars;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class GCMarsItemBlockOre extends ItemBlock
{
	public GCMarsItemBlockOre(int i)
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
		case 1: 
		{
			name = "electrum";
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
			name = "desh";
		}
		return getItemName() + "." + name;
	}
}
