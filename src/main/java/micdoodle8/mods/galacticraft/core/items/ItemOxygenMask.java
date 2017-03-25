package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemOxygenMask extends Item implements ISortableItem
{
    public ItemOxygenMask(String assetName)
    {
        super();
        this.setUnlocalizedName(assetName);
        //this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
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
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.GEAR;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            ItemStack gear = stats.getExtendedInventory().getStackInSlot(0);

            if (gear == null)
            {
                stats.getExtendedInventory().setInventorySlotContents(0, itemStack.copy());
                itemStack.stackSize = 0;
            }
        }
        return itemStack;
    }
}