package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpaceship;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreItemSpaceship extends GCCoreItem
{
	public GCCoreItemSpaceship(int par1)
	{
		super(par1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
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
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
    	int amountOfCorrectBlocks = 0;
    	
    	final GCCoreEntitySpaceship spaceship = new GCCoreEntitySpaceship(par3World, par4 + 0.5F, par5 - 1.5F, par6 + 0.5F, false , par1ItemStack.getItemDamage());
    	
    	if (par3World.isRemote)
    	{
    		return false;
    	}
    	else
    	{
    		for (int i = -1; i < 2; i++)
    		{
    			for (int j = -1; j < 2; j++)
        		{
    				if (par3World.getBlockId(par4 + i, par5, par6 + j) == GCCoreBlocks.landingPad.blockID)
    				{
    					amountOfCorrectBlocks++;
    				}
        		}
    		}
    		
    		if (amountOfCorrectBlocks == 9)
    		{
	    		par3World.spawnEntityInWorld(spaceship);
	    		if (!par2EntityPlayer.capabilities.isCreativeMode)
	    		par2EntityPlayer.inventory.consumeInventoryItem(par1ItemStack.getItem().itemID);
	    		spaceship.setSpaceshipType(par1ItemStack.getItemDamage());
    		}
    		else
    		{
    			return false;
    		}
    	}
        return true;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return EnumRarity.epic;
    }
	
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer player, List par2List, boolean b)
    {
    	if (par1ItemStack.getItemDamage() != 0)
    	{
    		switch (par1ItemStack.getItemDamage())
    		{
    		case 1:
    			par2List.add("Storage Space: 27");
    			break;
    		}
    	}
//    	par2List.add("Failure chance: " + String.valueOf(GCCoreUtil.getSpaceshipFailChance(FMLClientHandler.instance().getClient().thePlayer)) + "%");
    }
}
