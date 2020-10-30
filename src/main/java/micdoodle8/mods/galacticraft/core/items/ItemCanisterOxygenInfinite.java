package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import javax.annotation.Nullable;

public class ItemCanisterOxygenInfinite extends Item implements IItemOxygenSupply, ISortable
{
    public ItemCanisterOxygenInfinite(Item.Properties properties)
    {
        super(properties);
//        this.setMaxDamage(ItemCanisterGeneric.EMPTY);
//        this.setMaxStackSize(1);
//        this.setNoRepair();
//        this.setUnlocalizedName(assetName);
//        this.setContainerItem(GCItems.oilCanister);
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(EnumColor.DARK_GREEN + GCCoreUtil.translate("gui.infinite_item.desc")));
        tooltip.add(new StringTextComponent(EnumColor.RED + GCCoreUtil.translate("gui.creative_only.desc")));
    }

    /*@Override
    @OnlyIn(Dist.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon(Constants.TEXTURE_PREFIX + "oxygenCanisterInfinite");
    }*/

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    @Override
    public ItemStack getContainerItem(ItemStack itemstack)
    {
        if (super.getContainerItem(itemstack).isEmpty())
        {
            return ItemStack.EMPTY;
        }
        return itemstack;
    }

    @Override
    public int discharge(ItemStack itemStack, int amount)
    {
        return amount;
    }

    @Override
    public int getOxygenStored(ItemStack par1ItemStack)
    {
        return par1ItemStack.getMaxDamage();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
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
        }
        return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
    }
}
