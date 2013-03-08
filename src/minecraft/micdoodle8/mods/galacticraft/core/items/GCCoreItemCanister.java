package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GCCoreItemCanister extends Item
{
	public static final String[] names = {
		"aluminium", // 0
		"copper"}; // 1

	public static final int[] spriteIndexes = {
    	22,
    	71};

	public GCCoreItemCanister(int par1)
	{
		super(par1);
		this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setCreativeTab(GalacticraftCore.galacticraftTab);
	}

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
    	for (int i = 0; i < 2; i++)
    	{
            par3List.add(new ItemStack(par1, 1, i));
    	}
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
	public int getIconFromDamage(int par1)
    {
        if (par1 < 0 || par1 >= GCCoreItemCanister.names.length)
        {
            par1 = 0;
        }

        return spriteIndexes[par1];
    }

    @Override
	public String getItemNameIS(ItemStack par1ItemStack)
    {
        int var2 = par1ItemStack.getItemDamage();

        if (var2 < 0 || var2 >= GCCoreItemCanister.names.length)
        {
            var2 = 0;
        }

        return super.getItemName() + "." + GCCoreItemCanister.names[var2];
    }

	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/core/client/items/core.png";
	}
}
