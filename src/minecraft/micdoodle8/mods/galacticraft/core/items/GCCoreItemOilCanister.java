package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreItemOilCanister extends Item
{
	public GCCoreItemOilCanister(int par1)
	{
		super(par1);
		this.setMaxDamage(61);
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
		this.setMaxStackSize(1);
	}

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 61));
    }

    @Override
	@SideOnly(Side.CLIENT)
    public int getIconFromDamage(int par1)
    {
    	final int damage = (int) Math.floor(par1 / 10);

    	switch (damage)
    	{
    	case 0:
    		return 63;
    	case 1:
    		return 62;
    	case 2:
    		return 61;
    	case 3:
    		return 60;
    	case 4:
    		return 59;
    	case 5:
    		return 58;
    	case 6:
    		return 57;
    	}

    	return 0;
    }

    @Override
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
		return EnumRarity.uncommon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
    	if (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage() > 0)
    	{
        	par3List.add("Oil: " + (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()));
    	}
    }

    @Override
	public String getItemNameIS(ItemStack par1ItemStack)
    {
    	if (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage() > 0 && par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage() < 60)
    	{
            return this.getItemName();
    	}
    	else if (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage() == 60)
    	{
    		return "item.oilCanister";
    	}
    	else if (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage() == 0)
    	{
    		return "item.emptyLiquidCanister";
    	}

    	return "";
    }

	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/core/client/items/core.png";
	}
}
