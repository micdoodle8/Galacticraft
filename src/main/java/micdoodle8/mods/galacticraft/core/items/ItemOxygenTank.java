package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;

public class ItemOxygenTank extends Item implements ISortable, IClickableItem
{
    public ItemOxygenTank(Item.Properties builder)
    {
        super(builder);
//        this.setMaxStackSize(1);
//        this.setMaxDamage(tier * 900);
//        this.setUnlocalizedName(assetName);
//        this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
//        this.setNoRepair();
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            list.add(new ItemStack(this, 1, 0));
//            list.add(new ItemStack(this, 1, this.getMaxDamage()));
//        }
//    }

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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        float damagePercent = (1.0F - stack.getDamage() / (float)stack.getMaxDamage()) * 100.0F;
        DecimalFormat df = new DecimalFormat("#.##");
        String percentageStr = df.format(damagePercent) + "%" + EnumColor.WHITE;
        if (damagePercent < 20.0F)
        {
            percentageStr = EnumColor.RED + percentageStr;
        }
        else if (damagePercent < 80.0F)
        {
            percentageStr = EnumColor.YELLOW + percentageStr;
        }
        else
        {
            percentageStr = EnumColor.DARK_GREEN + percentageStr;
        }
        tooltip.add(new StringTextComponent(GCCoreUtil.translate("gui.tank.oxygen_remaining") + ": " + (stack.getMaxDamage() - stack.getDamage()) + " (" + percentageStr + ")"));
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.TANKS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand)
    {
        ItemStack itemStack = player.getHeldItem(hand);

        if (player instanceof ServerPlayerEntity)
        {
            if (itemStack.getItem() instanceof IClickableItem)
            {
                itemStack = ((IClickableItem) itemStack.getItem()).onItemRightClick(itemStack, worldIn, player);
            }

            if (itemStack.isEmpty())
            {
                return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
            }
        }
        return new ActionResult<>(ActionResultType.PASS, itemStack);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World worldIn, PlayerEntity player)
    {
        GCPlayerStats stats = GCPlayerStats.get(player);
        ItemStack gear = stats.getExtendedInventory().getStackInSlot(2);
        ItemStack gear1 = stats.getExtendedInventory().getStackInSlot(3);

        if (gear.isEmpty())
        {
            stats.getExtendedInventory().setInventorySlotContents(2, itemStack.copy());
            itemStack = ItemStack.EMPTY;
        }
        else if (gear1.isEmpty())
        {
            stats.getExtendedInventory().setInventorySlotContents(3, itemStack.copy());
            itemStack = ItemStack.EMPTY;
        }

        return itemStack;
    }
}