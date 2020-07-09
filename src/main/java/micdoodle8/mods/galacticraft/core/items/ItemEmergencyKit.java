package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemEmergencyKit extends ItemDesc implements ISortableItem
{
    private static final int SIZE = 9;

    public ItemEmergencyKit(Item.Properties properties)
    {
        super(properties);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(false);
//        this.setMaxStackSize(1);
//        this.setUnlocalizedName(assetName);
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.GEAR;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand)
    {
        ItemStack itemStack = player.getHeldItem(hand);
        if (player instanceof ServerPlayerEntity)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);

            for (int i = 0; i < SIZE; i++)
            {
                ItemStack newGear = getContents(i);
                if (newGear.getItem() instanceof IClickableItem)
                {
                    newGear = ((IClickableItem) newGear.getItem()).onItemRightClick(newGear, worldIn, player);
                }
                if (newGear.getCount() >= 1)
                {
                    ItemHandlerHelper.giveItemToPlayer(player, newGear, 0);
                }
            }

            itemStack.setCount(0);
            return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
        }
        return new ActionResult<>(ActionResultType.PASS, itemStack);
    }

    public static ItemStack getContents(int slot)
    {
        switch (slot)
        {
        case 0:
            return new ItemStack(GCItems.oxMask);
        case 1:
            return new ItemStack(GCItems.oxygenGear);
        case 2:
            return new ItemStack(GCItems.oxTankLight);
        case 3:
            return new ItemStack(GCItems.oxTankLight);
        case 4:
            return new ItemStack(GCItems.steelPickaxe);
        case 5:
            return new ItemStack(GCItems.dehydratedPotato, 1);
        case 6:
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.HEALING);
        case 7:
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.LONG_NIGHT_VISION);
        case 8:
            return new ItemStack(GCItems.parachuteRed, 1);
        default:
            return null;
        }
    }

    public static Object[] getRecipe()
    {
        Object[] result = new Object[]{"EAB", "CID", "FGH", 'A', null, 'B', null, 'C', null, 'D', null, 'E', null, 'F', null, 'G', null, 'H', null, 'I', null};
        for (int i = 0; i < SIZE; i++)
        {
            result[i * 2 + 4] = getContents(i);
        }
        return result;
    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return GCCoreUtil.translate("item.emergency_kit.description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }
}