package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class GCCoreItemParachute extends GCCoreItem
{
	public static final String[] names = {
		"plain", // 0
		"black", // 1
		"blue", // 2
		"green", // 3
		"brown", // 4
		"darkblue", // 5
		"darkgray", // 6
		"darkgreen", // 7
		"gray", // 8
		"magenta", // 9
		"orange", // 10
		"pink", // 11
		"purple", // 12
		"red", // 13
		"teal", // 14
		"yellow"}; // 15
	
	public GCCoreItemParachute(int par1) 
	{
		super(par1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
    	for (int i = 0; i < names.length; i++)
    	{
            par3List.add(new ItemStack(par1, 1, i));
    	}
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    public String getItemNameIS(ItemStack par1ItemStack)
    {
        int var2 = par1ItemStack.getItemDamage();

        if (var2 < 0 || var2 >= names.length)
        {
            var2 = 0;
        }

        return super.getItemName() + "." + names[var2];
    }

    @SideOnly(Side.CLIENT)
    public int getIconFromDamage(int par1)
    {
    	switch (par1)
    	{
    	case 0: // plain
    		return 49;
    	case 1: // black
    		return 34;
    	case 2: // blue
    		return 46;
    	case 3: // green
    		return 44;
    	case 4: // brown
    		return 37;
    	case 5: // dark blue
    		return 38;
    	case 6: // dark gray
    		return 42;
    	case 7: // dark green
    		return 36;
    	case 8: // gray
    		return 41;
    	case 9: // magenta
    		return 47;
    	case 10: // orange
    		return 48;
    	case 11: // pink
    		return 43;
    	case 12: // purple
    		return 39;
    	case 13: // red
    		return 35;
    	case 14: // teal
    		return 40;
    	case 15: // yellow
    		return 45;
    	}
    	
    	return 0;
    }
}
