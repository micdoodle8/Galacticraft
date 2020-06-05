package micdoodle8.mods.galacticraft.planets.venus.items;

import micdoodle8.mods.galacticraft.api.item.IItemThermal;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import javax.annotation.Nullable;

public class ItemThermalPaddingTier2 extends Item implements IItemThermal, ISortableItem
{
    public static String[] names = { "thermal_helm_t2", "thermal_chestplate_t2", "thermal_leggings_t2", "thermal_boots_t2" };

    public ItemThermalPaddingTier2(Item.Properties properties)
    {
        super(properties);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setUnlocalizedName(assetName);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    @Override
    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(GCCoreUtil.translate("item.tier2.desc"));
    }

    @Override
    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> subItems)
    {
        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
        {
            for (int i = 0; i < ItemThermalPaddingTier2.names.length; i++)
            {
                subItems.add(new ItemStack(this, 1, i));
            }
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand)
    {
        ItemStack itemStack = player.getHeldItem(hand);

        if (player instanceof ServerPlayerEntity)
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
        return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
    }
}
