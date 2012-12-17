package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityFlag;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumAction;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.EnumRarity;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
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
			"green", // 9
			"magenta", // 10
			"orange", // 11
			"pink", // 12
			"purple", // 13
			"red", // 14
			"teal", // 15
			"yellow"}; // 16
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
            
            final int var2 = this.getInventorySlotContainItem(par3EntityPlayer, this.shiftedIndex);

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
}
