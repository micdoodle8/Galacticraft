package micdoodle8.mods.galacticraft.core.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class GCCoreItemBlockEnclosedBlock extends ItemBlock
{
	public GCCoreItemBlockEnclosedBlock(int id)
	{
		super(id);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

    @Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
    {
    	String name = "";

    	switch (par1ItemStack.getItemDamage())
    	{
    	case 0:
    		name = "copperWire";
    		break;
    	case 1:
    		name = "oxygenPipe";
    		break;
    	}

		return Block.blocksList[this.getBlockID()].getUnlocalizedName() + "." + name;
    }

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}
}
