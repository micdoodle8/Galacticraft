package micdoodle8.mods.galacticraft.core.items;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.entity.IRocketType.EnumRocketType;
import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.entities.EntityTier1Rocket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ItemTier1Rocket extends Item implements IHoldableItem
{
    public ItemTier1Rocket(String assetName)
    {
        super();
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setTextureName("arrow");
        this.setUnlocalizedName(assetName);
    }

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

        if (par3World.isRemote && par2EntityPlayer instanceof EntityPlayerSP)
        {
        	ClientProxyCore.playerClientHandler.onBuild(8, (EntityPlayerSP) par2EntityPlayer);
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

            	final EntityTier1Rocket spaceship = new EntityTier1Rocket(par3World, centerX, centerY, centerZ, EnumRocketType.values()[par1ItemStack.getItemDamage()]);

                spaceship.setPosition(spaceship.posX, spaceship.posY + spaceship.getOnPadYOffset(), spaceship.posZ);
                par3World.spawnEntityInWorld(spaceship);

                if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("RocketFuel"))
                {
                    spaceship.fuelTank.fill(new FluidStack(GalacticraftCore.fluidFuel, par1ItemStack.getTagCompound().getInteger("RocketFuel")), true);
                }

                if (!par2EntityPlayer.capabilities.isCreativeMode)
                {
                    par1ItemStack.stackSize--;

                    if (par1ItemStack.stackSize <= 0)
                    {
                        par1ItemStack = null;
                    }
                }

                if (spaceship.rocketType.getPreFueled())
                {
                    spaceship.fuelTank.fill(new FluidStack(GalacticraftCore.fluidFuel, 2000), true);
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
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer player, List par2List, boolean b)
    {
        EnumRocketType type = EnumRocketType.values()[par1ItemStack.getItemDamage()];

        if (!type.getTooltip().isEmpty())
        {
            par2List.add(type.getTooltip());
        }

        if (type.getPreFueled())
        {
            par2List.add(EnumColor.RED + "\u00a7o" + GCCoreUtil.translate("gui.creativeOnly.desc"));
        }

        if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("RocketFuel"))
        {
            EntityTier1Rocket rocket = new EntityTier1Rocket(FMLClientHandler.instance().getWorldClient(), 0, 0, 0, EnumRocketType.values()[par1ItemStack.getItemDamage()]);
            par2List.add(GCCoreUtil.translate("gui.message.fuel.name") + ": " + par1ItemStack.getTagCompound().getInteger("RocketFuel") + " / " + rocket.fuelTank.getCapacity());
        }
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
}
