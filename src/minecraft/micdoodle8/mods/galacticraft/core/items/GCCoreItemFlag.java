package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreItemFlag extends GCCoreItem
{
	public static final String[] names = {
			"american", // 0
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
			"yellow", // 15
			"white"}; // 16
	public int placeProgress;
	
	public GCCoreItemFlag(int par1) 
	{
		super(par1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
    	for (int i = 0; i < 17; i++)
    	{
            par3List.add(new ItemStack(par1, 1, i));
    	}
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4)
    {
        final int useTime = this.getMaxItemUseDuration(par1ItemStack) - par4;
        
        final MovingObjectPosition var12 = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, true);

        float var7 = useTime / 20.0F;
        var7 = (var7 * var7 + var7 * 2.0F) / 3.0F;

        if (var7 > 1.0F)
        {
            var7 = 1.0F;
        }
        
        if (var7 == 1.0F && var12 != null && var12.typeOfHit == EnumMovingObjectType.TILE)
        {
            final int x = var12.blockX;
            final int y = var12.blockY;
            final int z = var12.blockZ;
            
            if (!par2World.isRemote)
            {
            	final GCCoreEntityFlag flag = new GCCoreEntityFlag(par2World, x + 0.5F, y + 1.0F, z + 0.5F, par3EntityPlayer.rotationYaw - 90F);
            	par2World.spawnEntityInWorld(flag);
                flag.setType(par1ItemStack.getItemDamage());
                flag.setOwner(par3EntityPlayer.username);
            }
            
            final int var2 = this.getInventorySlotContainItem(par3EntityPlayer, this.itemID);

            if (var2 >= 0)
            {
                if (--par3EntityPlayer.inventory.mainInventory[var2].stackSize <= 0)
                {
                	par3EntityPlayer.inventory.mainInventory[var2] = null;
                }
            }
        }
    }
    
    private int getInventorySlotContainItem(EntityPlayer player, int par1)
    {
        for (int var2 = 0; var2 < player.inventory.mainInventory.length; ++var2)
        {
            if (player.inventory.mainInventory[var2] != null && player.inventory.mainInventory[var2].itemID == par1)
            {
                return var2;
            }
        }

        return -1;
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
    public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        return par1ItemStack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.none;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));

        return par1ItemStack;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return EnumRarity.epic;
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
    
    public static int getFlagDamageValueFromDye(int meta)
    {
    	switch (meta)
    	{
    	case 0:
    		return 1;
    	case 1:
    		return 13;
    	case 2:
    		return 7;
    	case 3:
    		return 4;
    	case 4:
    		return 5;
    	case 5:
    		return 12;
    	case 6:
    		return 14;
    	case 7:
    		return 8;
    	case 8:
    		return 6;
    	case 9:
    		return 11;
    	case 10:
    		return 3;
    	case 11:
    		return 15;
    	case 12:
    		return 2;
    	case 13:
    		return 9;
    	case 14:
    		return 10;
    	case 15:
    		return 16;
    	}
    	
    	return -1;
    }
}
