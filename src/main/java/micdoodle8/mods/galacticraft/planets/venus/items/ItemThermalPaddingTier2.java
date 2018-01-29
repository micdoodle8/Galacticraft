package micdoodle8.mods.galacticraft.planets.venus.items;

import micdoodle8.mods.galacticraft.api.item.IItemThermal;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemThermalPaddingTier2 extends Item implements IItemThermal, ISortableItem
{
    public static String[] names = { "thermal_helm_t2", "thermal_chestplate_t2", "thermal_leggings_t2", "thermal_boots_t2" };

    public ItemThermalPaddingTier2(String assetName)
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
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add(GCCoreUtil.translate("item.tier2.desc"));
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        for (int i = 0; i < ItemThermalPaddingTier2.names.length; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        if (names.length > par1ItemStack.getItemDamage())
        {
            return "item." + ItemThermalPaddingTier2.names[par1ItemStack.getItemDamage()];
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
        return 2;
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
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World worldIn, EntityPlayer player, EnumHand hand)
    {
        if (player instanceof EntityPlayerMP)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            ItemStack gear = stats.getExtendedInventory().getStackInSlot(6);
            ItemStack gear1 = stats.getExtendedInventory().getStackInSlot(7);
            ItemStack gear2 = stats.getExtendedInventory().getStackInSlot(8);
            ItemStack gear3 = stats.getExtendedInventory().getStackInSlot(9);

            if (itemStack.getItemDamage() == 0)
            {
                if (gear == null)
                {
                    stats.getExtendedInventory().setInventorySlotContents(6, itemStack.copy());
                    itemStack.stackSize = 0;
                }
            }
            else if (itemStack.getItemDamage() == 1)
            {
                if (gear1 == null)
                {
                    stats.getExtendedInventory().setInventorySlotContents(7, itemStack.copy());
                    itemStack.stackSize = 0;
                }
            }
            else if (itemStack.getItemDamage() == 2)
            {
                if (gear2 == null)
                {
                    stats.getExtendedInventory().setInventorySlotContents(8, itemStack.copy());
                    itemStack.stackSize = 0;
                }
            }
            else if (itemStack.getItemDamage() == 3)
            {
                if (gear3 == null)
                {
                    stats.getExtendedInventory().setInventorySlotContents(9, itemStack.copy());
                    itemStack.stackSize = 0;
                }
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }
}
