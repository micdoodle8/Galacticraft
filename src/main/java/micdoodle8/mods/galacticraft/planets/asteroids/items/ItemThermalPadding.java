package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.api.item.IItemThermal;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemThermalPadding extends Item implements IItemThermal, ISortable
{
//    public static String[] names = { "thermal_helm", "thermal_chestplate", "thermal_leggings", "thermal_boots", "thermal_helm0", "thermal_chestplate0", "thermal_leggings0", "thermal_boots0" };

    public ItemThermalPadding(Item.Properties properties)
    {
        super(properties);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//        this.setMaxStackSize(1);
//        this.setUnlocalizedName(assetName);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            for (int i = 0; i < ItemThermalPadding.names.length / 2; i++)
//            {
//                list.add(new ItemStack(this, 1, i));
//            }
//        }
//    }

//    @Override
//    public String getUnlocalizedName(ItemStack par1ItemStack)
//    {
//        if (names.length > par1ItemStack.getDamage())
//        {
//            return "item." + ItemThermalPadding.names[par1ItemStack.getDamage()];
//        }
//
//        return "unnamed";
//    }

//    @Override
//    public int getMetadata(int par1)
//    {
//        return par1;
//    }

    @Override
    public int getThermalStrength()
    {
        return 1;
    }

    @Override
    public boolean isValidForSlot(ItemStack stack, int armorSlot)
    {
        return stack.getDamage() == armorSlot;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.ARMOR;
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

            if (itemStack.getDamage() == 0)
            {
                if (gear.isEmpty())
                {
                    stats.getExtendedInventory().setInventorySlotContents(6, itemStack.copy());
                    itemStack.setCount(0);
                }
            }
            else if (itemStack.getDamage() == 1)
            {
                if (gear1.isEmpty())
                {
                    stats.getExtendedInventory().setInventorySlotContents(7, itemStack.copy());
                    itemStack.setCount(0);
                }
            }
            else if (itemStack.getDamage() == 2)
            {
                if (gear2.isEmpty())
                {
                    stats.getExtendedInventory().setInventorySlotContents(8, itemStack.copy());
                    itemStack.setCount(0);
                }
            }
            else if (itemStack.getDamage() == 3)
            {
                if (gear3.isEmpty())
                {
                    stats.getExtendedInventory().setInventorySlotContents(9, itemStack.copy());
                    itemStack.setCount(0);
                }
            }
        }
        return new ActionResult<>(ActionResultType.PASS, itemStack);
    }
}
