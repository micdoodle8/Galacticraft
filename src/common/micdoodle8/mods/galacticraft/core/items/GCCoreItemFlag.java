package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityFlag;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumRarity;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreItemFlag extends GCCoreItem
{
	public GCCoreItemFlag(int par1) 
	{
		super(par1);
	}

	@Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
    	GCCoreEntityFlag spaceship = new GCCoreEntityFlag(par3World, par4, par5, par6, 0, par2EntityPlayer);

        if (!par3World.isRemote)
        {
            int var5 = par3World.getBlockId(par4, par5, par6 - 1);
            int var6 = par3World.getBlockId(par4, par5, par6 + 1);
            int var7 = par3World.getBlockId(par4 - 1, par5, par6);
            int var8 = par3World.getBlockId(par4 + 1, par5, par6);
            byte var9 = 3;

            if (Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var6])
            {
                var9 = 3;
            }

            if (Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var5])
            {
                var9 = 2;
            }

            if (Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var8])
            {
                var9 = 5;
            }

            if (Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var7])
            {
                var9 = 4;
            }
            
            spaceship.setDirection(var9);
        }
        
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
