package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
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
		super(par1, "");
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}

	@Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftTab;
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
