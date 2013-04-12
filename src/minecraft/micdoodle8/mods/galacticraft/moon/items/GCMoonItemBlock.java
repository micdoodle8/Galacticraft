package micdoodle8.mods.galacticraft.moon.items;

import net.minecraft.block.Block;
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
	public String getUnlocalizedName(ItemStack itemstack)
	{
		String name = "";

		switch(itemstack.getItemDamage())
		{
		case 0:
		{
			name = "coppermoon";
			break;
		}
		case 1:
		{
			name = "tinmoon";
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
		case 14:
		{
			name = "bricks";
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
