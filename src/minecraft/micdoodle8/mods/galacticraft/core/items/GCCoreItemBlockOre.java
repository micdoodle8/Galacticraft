package micdoodle8.mods.galacticraft.core.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class GCCoreItemBlockOre extends ItemBlock
{
	public GCCoreItemBlockOre(int i)
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
	public String getUnlocalizedName(ItemStack itemstack)
	{
		String name = "";

		switch(itemstack.getItemDamage())
		{
		case 0:
		{
			name = "copperearth";
			break;
		}
		case 1:
		{
			name = "aluminiumearth";
			break;
		}
		case 2:
		{
			name = "titaniumearth";
			break;
		}
		default:
			name = "null";
		}

		return Block.blocksList[this.getBlockID()].getUnlocalizedName() + "." + name;
	}

	@Override
	public String getUnlocalizedName()
	{
		return Block.blocksList[this.getBlockID()].getUnlocalizedName() + ".0";
	}
}
