package micdoodle8.mods.galacticraft.planets.mars.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.entity.IRocketType.EnumRocketType;
import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityTier2Rocket;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ItemTier2Rocket extends Item implements IHoldableItem
{
    public ItemTier2Rocket()
    {
        super();
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        boolean padFound = false;
        TileEntity tile = null;

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
                    final Block id = par3World.getBlock(par4 + i, par5, par6 + j);
                    int meta = par3World.getBlockMetadata(par4 + i, par5, par6 + j);

                    if (id == GCBlocks.landingPadFull && meta == 0)
                    {
                        padFound = true;
                        tile = par3World.getTileEntity(par4 + i, par5, par6 + j);

                        centerX = par4 + i + 0.5F;
                        centerY = par5 + 0.4F;
                        centerZ = par6 + j + 0.5F;
                        
                        break;
                    }
                }
                
                if (padFound) break;
            }

            if (padFound)
            {
            	//Check whether there is already a rocket on the pad
            	if (tile instanceof TileEntityLandingPad)
            	{
            		if (((TileEntityLandingPad)tile).getDockedEntity() != null)
            			return false;
            	}
            	else
            	{
            		return false;
            	}

                EntitySpaceshipBase rocket = null;

                if (par1ItemStack.getItemDamage() < 10)
                {
                    rocket = new EntityTier2Rocket(par3World, centerX, centerY + 1.6D, centerZ, EnumRocketType.values()[par1ItemStack.getItemDamage()]);
                }
                else
                {
                    rocket = new EntityCargoRocket(par3World, centerX, centerY, centerZ, EnumRocketType.values()[par1ItemStack.getItemDamage() - 10]);
                }

                par3World.spawnEntityInWorld(rocket);

                if (!par2EntityPlayer.capabilities.isCreativeMode)
                {
                    par1ItemStack.stackSize--;

                    if (par1ItemStack.stackSize <= 0)
                    {
                        par1ItemStack = null;
                    }
                }

                if (rocket instanceof IRocketType && ((IRocketType) rocket).getType().getPreFueled())
                {
                    if (rocket instanceof EntityTieredRocket)
                    {
                        ((EntityTieredRocket) rocket).fuelTank.fill(new FluidStack(GalacticraftCore.fluidFuel, rocket.getMaxFuel()), true);
                    }
                    else if (rocket instanceof EntityCargoRocket)
                    {
                        ((EntityCargoRocket) rocket).fuelTank.fill(new FluidStack(GalacticraftCore.fluidFuel, rocket.getMaxFuel()), true);
                    }
                }
            }
            else
            {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < EnumRocketType.values().length; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }

        for (int i = 11; i < 10 + EnumRocketType.values().length; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer player, List par2List, boolean b)
    {
        EnumRocketType type = null;

        if (par1ItemStack.getItemDamage() < 10)
        {
            type = EnumRocketType.values()[par1ItemStack.getItemDamage()];
        }
        else
        {
            type = EnumRocketType.values()[par1ItemStack.getItemDamage() - 10];
        }

        if (!type.getTooltip().isEmpty())
        {
            par2List.add(type.getTooltip());
        }

        if (type.getPreFueled())
        {
            par2List.add(EnumColor.RED + "\u00a7o" + GCCoreUtil.translate("gui.creativeOnly.desc"));
        }

        if (par1ItemStack.getItemDamage() >= 10)
        {
            par2List.add(EnumColor.AQUA + GCCoreUtil.translate("gui.requiresController.desc"));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return super.getUnlocalizedName(par1ItemStack) + (par1ItemStack.getItemDamage() < 10 ? ".t2Rocket" : ".cargoRocket");
    }

    @Override
    public boolean shouldHoldLeftHandUp(EntityPlayer player)
    {
        return true;
    }

    @Override
    public boolean shouldHoldRightHandUp(EntityPlayer player)
    {
        return true;
    }

    @Override
    public boolean shouldCrouch(EntityPlayer player)
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister)
    {
    }
}
