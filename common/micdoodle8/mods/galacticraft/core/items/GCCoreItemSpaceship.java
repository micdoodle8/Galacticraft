package micdoodle8.mods.galacticraft.core.items;

import java.util.List;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityRocketT1;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.liquids.LiquidDictionary;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCCoreItemSpaceship extends Item
{
    public GCCoreItemSpaceship(int par1)
    {
        super(par1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = null;
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        int amountOfCorrectBlocks = 0;

        if (par3World.isRemote)
        {
            return false;
        }
        else
        {
            float centerX = -1;
            float centerY = -1;
            float centerZ = -1;

            for (int i = -1; i < 2; i++)
            {
                for (int j = -1; j < 2; j++)
                {
                    final int id = par3World.getBlockId(par4 + i, par5, par6 + j);
                    final int id2 = par3World.getBlockId(par4 + i, par5 + 1, par6 + j);

                    if (id == GCCoreBlocks.landingPad.blockID || id == GCCoreBlocks.landingPadFull.blockID || id2 == GCCoreBlocks.landingPadFull.blockID)
                    {
                        amountOfCorrectBlocks++;

                        centerX = par4 + i + 0.5F;
                        centerY = par5 - 2.2F;
                        centerZ = par6 + j + 0.5F;

                        if (id == GCCoreBlocks.landingPadFull.blockID || id2 == GCCoreBlocks.landingPadFull.blockID)
                        {
                            amountOfCorrectBlocks = 9;
                        }

                        if (id2 == GCCoreBlocks.landingPadFull.blockID)
                        {
                            centerX = par4 + i + 0.5F;
                            centerY = par5 + 1 - 2.2F;
                            centerZ = par6 + j + 0.5F;
                        }
                    }
                }
            }

            if (amountOfCorrectBlocks == 9)
            {
                final GCCoreEntityRocketT1 spaceship = new GCCoreEntityRocketT1(par3World, centerX, centerY + 0.2D, centerZ, par1ItemStack.getItemDamage());

                par3World.spawnEntityInWorld(spaceship);
                if (!par2EntityPlayer.capabilities.isCreativeMode)
                {
                    par2EntityPlayer.inventory.consumeInventoryItem(par1ItemStack.getItem().itemID);
                }
                spaceship.setSpaceshipType(par1ItemStack.getItemDamage());

                if (par1ItemStack.getItemDamage() == 2)
                {
                    spaceship.spaceshipFuelTank.fill(LiquidDictionary.getLiquid("Fuel", 2000), true);
                }
            }
            else
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < 3; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
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
            case 2:
                par2List.add("Pre-fueled");
                break;
            }
        }
    }
}
