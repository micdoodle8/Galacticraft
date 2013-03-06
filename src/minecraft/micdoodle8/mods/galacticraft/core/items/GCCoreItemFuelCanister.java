package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerBaseClient;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GCCoreItemFuelCanister extends Item
{
	public GCCoreItemFuelCanister(int par1) 
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
    }

    @Override
	@SideOnly(Side.CLIENT)
    public int getIconFromDamage(int par1)
    {
    	int damage = (int) Math.floor(par1 / 10);
    	
    	switch (damage)
    	{
    	case 0:
    		return 70;
    	case 1:
    		return 69;
    	case 2:
    		return 68;
    	case 3:
    		return 67;
    	case 4:
    		return 66;
    	case 5:
    		return 65;
    	case 6:
    		return 64;
    	}
    	
    	return 0;
    }

    @Override
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
		return EnumRarity.uncommon;
    }

    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) 
    {
    	if ((par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()) == 0)
    	{
    		int stackSize = par1ItemStack.stackSize;
    		
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
    	if ((par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()) > 0)
    	{
        	par3List.add("Fuel: " + (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()));
    	}
    }

    public String getItemNameIS(ItemStack par1ItemStack)
    {
    	if (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage() > 0 && par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage() < 60)
    	{
    		return "item.fuelCanisterPartial";
    	}
    	else if ((par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()) == 60)
    	{
    		return "item.fuelCanister";
    	}
    	
    	return "";
    }

	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/core/client/items/core.png";
	}
}
