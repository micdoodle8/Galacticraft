package micdoodle8.mods.galacticraft.core.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockCargoLoader extends ItemBlockDesc
{
	public ItemBlockCargoLoader(Block block)
	{
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		String name = "";

		if (par1ItemStack.getItemDamage() < 4)
		{
			name = "loader";
		}
		else
		{
			name = "unloader";
		}

		return this.field_150939_a.getUnlocalizedName() + "." + name;
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}
}
