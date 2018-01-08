package micdoodle8.mods.galacticraft.planets.venus.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.items.ItemDesc;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBasicVenus extends ItemDesc implements ISortableItem
{
    public static String[] names = { "shield_controller", "ingot_lead", "radioisotope_core", "thermal_fabric" };

    public ItemBasicVenus(String name)
    {
        super();
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setUnlocalizedName(name);
    }

    @SideOnly(Side.CLIENT)
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
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        for (int i = 0; i < ItemBasicVenus.names.length; i++)
        {
            list.add(new ItemStack(itemIn, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        if (names.length > par1ItemStack.getItemDamage())
        {
            return "item." + ItemBasicVenus.names[par1ItemStack.getItemDamage()];
        }

        return "unnamed";
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        switch (meta)
        {
        case 0:
            return EnumSortCategoryItem.GEAR;
        case 1:
            return EnumSortCategoryItem.INGOT;
        default:
            return EnumSortCategoryItem.GENERAL;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP && itemStack.getItemDamage() == 0)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            ItemStack gear = stats.getExtendedInventory().getStackInSlot(10);

            if (gear == null)
            {
                stats.getExtendedInventory().setInventorySlotContents(10, itemStack.copy());
                itemStack.stackSize = 0;
            }
        }
        return itemStack;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        if (meta == 0)
        {
            return GCCoreUtil.translate("item.shield_controller.description");
        }

        return "";
    }

    @Override
    public boolean showDescription(int meta)
    {
        return meta == 0;
    }
}
