package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import micdoodle8.mods.galacticraft.API.IFuelTank;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreItemFuelCanister extends Item implements IFuelTank
{
	protected Icon[] icons = new Icon[256];

	public static final String[] names = {
			"liquidcan_fuel_6", // 0
			"liquidcan_fuel_5", // 0
			"liquidcan_fuel_4", // 1
			"liquidcan_fuel_3", // 2
			"liquidcan_fuel_2", // 3
			"liquidcan_fuel_1", // 4
			"liquidcan_empty"}; // 5
	
	public GCCoreItemFuelCanister(int par1)
	{
		super(par1);
		this.setMaxDamage(61);
		this.setMaxStackSize(1);
	}

	@Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 1));
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void func_94581_a(IconRegister iconRegister)
	{
		int i = 0;
		
		for (String name : this.names)
		{
			this.icons[i++] = iconRegister.func_94245_a("galacticraftcore:" + name);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		if (itemStack.getItemDamage() == 1)
		{
			return "item.fuelCanister";
		}
		
		return "item.fuelCanisterPartial";
	}

	@Override
	public Icon getIconFromDamage(int par1)
	{
    	final int damage = (int) Math.floor(par1 / 10);
    	
		if (this.icons.length > damage)
		{
			return this.icons[damage];
		}

		return super.getIconFromDamage(damage);
	}

    @Override
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
		return EnumRarity.uncommon;
    }

    @Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
    	if (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage() == 0)
    	{
    		final int stackSize = par1ItemStack.stackSize;

    		if (!(par1ItemStack.getItem() instanceof GCCoreItemOilCanister))
    		{
    			par1ItemStack = new ItemStack(GCCoreItems.oilCanister, stackSize, 61);
    		}
    	}
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
    	if (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage() > 0)
    	{
        	par3List.add("Fuel: " + (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()));
    	}
    }
}
