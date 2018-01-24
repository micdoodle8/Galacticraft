package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.api.item.IItemThermal;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemThermalPadding extends Item implements IItemThermal, ISortableItem
{
    public static String[] names = { "thermal_helm", "thermal_chestplate", "thermal_leggings", "thermal_boots", "thermal_helm0", "thermal_chestplate0", "thermal_leggings0", "thermal_boots0" };

    public ItemThermalPadding(String assetName)
    {
        super();
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setUnlocalizedName(assetName);
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
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        if (tab == GalacticraftCore.galacticraftItemsTab || tab == CreativeTabs.SEARCH)
        {
            for (int i = 0; i < ItemThermalPadding.names.length / 2; i++)
            {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        if (names.length > par1ItemStack.getItemDamage())
        {
            return "item." + ItemThermalPadding.names[par1ItemStack.getItemDamage()];
        }

        return "unnamed";
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
    public int getThermalStrength()
    {
        return 1;
    }

    @Override
    public boolean isValidForSlot(ItemStack stack, int armorSlot)
    {
        return stack.getItemDamage() == armorSlot;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.ARMOR;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack itemStack = player.getHeldItem(hand);

        if (player instanceof EntityPlayerMP)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            ItemStack gear = stats.getExtendedInventory().getStackInSlot(6);
            ItemStack gear1 = stats.getExtendedInventory().getStackInSlot(7);
            ItemStack gear2 = stats.getExtendedInventory().getStackInSlot(8);
            ItemStack gear3 = stats.getExtendedInventory().getStackInSlot(9);

            if (itemStack.getItemDamage() == 0)
            {
                if (gear.isEmpty())
                {
                    stats.getExtendedInventory().setInventorySlotContents(6, itemStack.copy());
                    itemStack.setCount(0);
                }
            }
            else if (itemStack.getItemDamage() == 1)
            {
                if (gear1.isEmpty())
                {
                    stats.getExtendedInventory().setInventorySlotContents(7, itemStack.copy());
                    itemStack.setCount(0);
                }
            }
            else if (itemStack.getItemDamage() == 2)
            {
                if (gear2.isEmpty())
                {
                    stats.getExtendedInventory().setInventorySlotContents(8, itemStack.copy());
                    itemStack.setCount(0);
                }
            }
            else if (itemStack.getItemDamage() == 3)
            {
                if (gear3.isEmpty())
                {
                    stats.getExtendedInventory().setInventorySlotContents(9, itemStack.copy());
                    itemStack.setCount(0);
                }
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }
}
