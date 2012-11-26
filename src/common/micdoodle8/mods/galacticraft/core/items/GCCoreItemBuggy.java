package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumRarity;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreItemBuggy extends GCCoreItem
{
	public GCCoreItemBuggy(int par1) 
	{
		super(par1);
	}

	@Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
    	GCCoreEntityBuggy spaceship = new GCCoreEntityBuggy(par3World, par4 + 0.5F, par5 + 2.5F, par6 + 0.5F);
    	
    	if (par3World.isRemote)
    	{
    		return false;
    	}
    	else
    	{
    		par3World.spawnEntityInWorld(spaceship);
    		if (!par2EntityPlayer.capabilities.isCreativeMode)
    		par2EntityPlayer.inventory.consumeInventoryItem(par1ItemStack.getItem().shiftedIndex);
    	}
        return true;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return EnumRarity.epic;
    }
}
