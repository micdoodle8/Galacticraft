package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.GuiIdsCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemPreLaunchChecklist extends Item implements ISortableItem
{
    public ItemPreLaunchChecklist(String assetName)
    {
        super();
        this.setUnlocalizedName(assetName);
        this.setMaxStackSize(1);
        //this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
    }

    @Override
    public boolean isItemTool(ItemStack stack)
    {
        return false;
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> tooltip, boolean par4)
    {
        if (par1ItemStack != null && this == GCItems.heavyPlatingTier1)
        {
            tooltip.add(GCCoreUtil.translate("item.tier1.desc"));
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        if (worldIn.isRemote)
        {
            playerIn.openGui(GalacticraftCore.instance, GuiIdsCore.PRE_LAUNCH_CHECKLIST, playerIn.worldObj, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
        }

        return itemStackIn;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.GENERAL;
    }
}
